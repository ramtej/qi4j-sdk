package org.qi4j.index.elasticsearch.extensions.spatial.internal;

import org.elasticsearch.common.geo.ShapeRelation;
import org.elasticsearch.common.geo.builders.CircleBuilder;
import org.elasticsearch.common.geo.builders.PolygonBuilder;
import org.elasticsearch.common.geo.builders.ShapeBuilder;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.*;
import org.elasticsearch.index.search.geo.GeoDistanceFilter;
import org.qi4j.api.composite.Composite;
import org.qi4j.api.geometry.TUnit;
import org.qi4j.api.geometry.internal.TGeometry;
import org.qi4j.api.geometry.TPoint;
import org.qi4j.api.geometry.TPolygon;
import org.qi4j.api.property.GenericPropertyInfo;
import org.qi4j.api.query.grammar.PropertyFunction;
import org.qi4j.api.query.grammar.extensions.spatial.convert.SpatialConvertSpecification;
import org.qi4j.api.query.grammar.extensions.spatial.predicate.SpatialPredicatesSpecification;
import org.qi4j.api.structure.Module;
import org.qi4j.api.util.Classes;
import org.qi4j.functional.Specification;
import org.qi4j.index.elasticsearch.ElasticSearchFinder;
import org.qi4j.index.elasticsearch.extensions.spatial.ElasticSearchSpatialFinderSupport;
import org.qi4j.library.spatial.v2.projections.ProjectionsRegistry;

import java.lang.reflect.Type;
import java.util.Map;

import static org.qi4j.library.spatial.v2.transformations.TTransformations.Transform;

/**
 * Created by jj on 20.11.14.
 */
public abstract class AbstractElasticSearchSpatialFunction {

    private static final String EPSG_4326 = "EPSG:4326";
    private static final String DefaultProjection = EPSG_4326;
    private static final double DefaultProjectionConversionPrecisionInMeters = 2.00;

    protected Module module;

    public void setModule(Module module) {
        this.module = module;
    }

    protected void addFilter( FilterBuilder filter, FilterBuilder into )
    {
        if ( into instanceof AndFilterBuilder) {
            ( (AndFilterBuilder) into ).add( filter );
        } else if ( into instanceof OrFilterBuilder) {
            ( (OrFilterBuilder) into ).add( filter );
        } else {
            throw new UnsupportedOperationException( "FilterBuilder is nor an AndFB nor an OrFB, cannot continue." );
        }
    }

    protected TGeometry verifyProjection(TGeometry tGeometry)
    {
        if (new ProjectionsRegistry().getCRS(tGeometry.getCRS()) == null)
            throw new RuntimeException("Project with the CRS Identity " + tGeometry.getCRS() + " is unknown. Supported projections are JJ TODO" );


        try {
            Transform(module).from(tGeometry).to(DefaultProjection, DefaultProjectionConversionPrecisionInMeters);
        } catch (Exception _ex)
        {
            _ex.printStackTrace();
        }

        return tGeometry; // ATTENTION - we are transforming as per "Reference"
    }

    protected boolean isPropertyTypeTPoint(PropertyFunction propertyFunction) throws Exception
    {
        // String typeName = Classes.typeOf(propertyFunction.accessor()).getTypeName();
        // System.out.println(typeName);

        Type returnType = Classes.typeOf(propertyFunction.accessor());
        Type propertyTypeAsType = GenericPropertyInfo.toPropertyType(returnType);


        System.out.println("---- > " + propertyTypeAsType.getTypeName());


        Class clazz = Class.forName(propertyTypeAsType.getTypeName());
        // if (clazz instanceof TGeometry)

        if (TPoint.class.isAssignableFrom(clazz))
            return true;
        else
            return false;
    }


    protected GeoShapeFilterBuilder createShapeFilter(String name, TPoint point, ShapeRelation relation, double distance, TUnit unit)
    {
        return createShapeFilter(name, point, relation, distance, convertDistanceUnit(unit));
    }

    protected GeoShapeFilterBuilder createShapeFilter(String name, TGeometry geometry, ShapeRelation relation)
    {
        return createShapeFilter(name, geometry, relation, 0, null);
    }

    protected GeoDistanceFilterBuilder createGeoDistanceFilter(String name, TPoint tPoint, double distance, TUnit unit)
    {
      return  FilterBuilders.geoDistanceFilter(name)
                .lat(tPoint.x())
                .lon(tPoint.y())
                .distance(distance, convertDistanceUnit(unit));
    }


    private DistanceUnit convertDistanceUnit(TUnit tUnit)
    {
        switch (tUnit) {
            case METER      : return DistanceUnit.METERS;
            case MILLIMETER : return DistanceUnit.MILLIMETERS;
            case CENTIMETER : return DistanceUnit.CENTIMETERS;
            case KILOMETER  :return DistanceUnit.KILOMETERS;
            default : throw new RuntimeException("Can not convert Units");
        }

    }


    private GeoShapeFilterBuilder createShapeFilter(String name, TGeometry geometry, ShapeRelation relation, double distance, DistanceUnit unit  )
    {
        if (geometry instanceof TPoint)
        {
            CircleBuilder circleBuilder = ShapeBuilder.newCircleBuilder();
            circleBuilder.center(((TPoint) geometry).x(), ((TPoint)geometry).y()).radius(distance, unit);
            return FilterBuilders.geoShapeFilter(name, circleBuilder, relation);
        }
        else if (geometry instanceof TPolygon)
        {
            PolygonBuilder polygonBuilder = ShapeBuilder.newPolygon();

            for (int i = 0; i < ((TPolygon) geometry).shell().get().points().get().size(); i++) {
                TPoint point = ((TPolygon) geometry).shell().get().getPointN(i);
                System.out.println(point);

                polygonBuilder.point(
                        point.x(), point.y()
                );
            }

            return  FilterBuilders.geoShapeFilter(name, polygonBuilder, relation);
        }
        else
        {

        }

        return null;
    }


    protected TGeometry resolveGeometry( FilterBuilder filterBuilder, Specification<Composite> spec, Module module) throws Exception
    {

        if (spec instanceof SpatialPredicatesSpecification)
        {
            if (((SpatialPredicatesSpecification)spec).value() != null)
            {
                return ((SpatialPredicatesSpecification)spec).value();
            }
            else if (((SpatialPredicatesSpecification)spec).operator() != null)
            {

                if (((SpatialPredicatesSpecification) spec).operator() instanceof SpatialConvertSpecification)
                {
                    // return ((SpatialConvertSpecification) ((SpatialPredicatesSpecification) spec).operator()).convert(module);
                    return executeSpecification(filterBuilder, (SpatialPredicatesSpecification)spec, null);
                }

                return null;
            }
        }

        return null;
    }

    private TGeometry executeSpecification( FilterBuilder filterBuilder,
                                            SpatialPredicatesSpecification<?> spec,
                                            Map<String, Object> variables ) throws Exception
    {


        if (((SpatialPredicatesSpecification) spec).operator() instanceof SpatialConvertSpecification) {
            // return ((SpatialConvertSpecification) ((SpatialPredicatesSpecification) spec).operator()).convert(module);

            System.out.println("####### " + spec.operator().getClass().getSuperclass());

            if (ElasticSearchFinder.Mixin.EXTENDED_QUERY_EXPRESSIONS_CATALOG.get(spec.operator().getClass().getSuperclass()) != null) {

                ElasticSearchSpatialFinderSupport.SpatialQuerySpecSupport spatialQuerySpecSupport = ElasticSearchFinder.Mixin.EXTENDED_QUERY_EXPRESSIONS_CATALOG.get(spec.operator().getClass().getSuperclass());
                spatialQuerySpecSupport.setModule(module);
                return spatialQuerySpecSupport.processSpecification(filterBuilder, spec.operator(), variables);

            } else {
                throw new UnsupportedOperationException("Query specification unsupported by Elastic Search "
                        + "(New Query API support missing?): "
                        + spec.getClass() + ": " + spec);
            }
        }

        return null;
    }





}