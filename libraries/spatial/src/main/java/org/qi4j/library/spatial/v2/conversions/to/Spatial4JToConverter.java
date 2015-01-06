package org.qi4j.library.spatial.v2.conversions.to;

import com.spatial4j.core.context.SpatialContext;
import com.spatial4j.core.context.jts.JtsSpatialContext;
import com.spatial4j.core.context.jts.JtsSpatialContextFactory;
import com.spatial4j.core.io.jts.JtsWKTReaderShapeParser;
import com.spatial4j.core.io.jts.JtsWktShapeParser;
import com.spatial4j.core.shape.Circle;
import com.spatial4j.core.shape.Shape;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.util.GeometricShapeFactory;
import org.geojson.GeoJsonObject;
import org.geojson.Point;
import org.qi4j.api.geometry.TPoint;
import org.qi4j.api.geometry.internal.TCircle;
import org.qi4j.api.geometry.internal.TGeometry;
import org.qi4j.api.structure.Module;

/**
 * Created by jj on 04.12.14.
 */
public class Spatial4JToConverter<T> {

    // http://code.google.com/p/shape-metrics/source/browse/trunk/src/main/java/edu/psu/geovista/ian/utils/Circle.java?r=2

    final SpatialContext ctx;
    {
        JtsSpatialContextFactory factory = new JtsSpatialContextFactory();
        factory.srid = 4326;
        factory.datelineRule = JtsWktShapeParser.DatelineRule.ccwRect;
        factory.wktShapeParserClass = JtsWKTReaderShapeParser.class;
        ctx = factory.newSpatialContext();
    }

    public static final double DATELINE = 180;
    public static final JtsSpatialContext SPATIAL_CONTEXT = JtsSpatialContext.GEO;
    public static final GeometryFactory FACTORY = SPATIAL_CONTEXT.getGeometryFactory();
    public static final GeometricShapeFactory SHAPE_FACTORY = new GeometricShapeFactory();

    protected final boolean multiPolygonMayOverlap = false;
    protected final boolean autoValidateJtsGeometry = true;
    protected final boolean autoIndexJtsGeometry = true;

    protected final boolean wrapdateline = SPATIAL_CONTEXT.isGeo();


    private Module module;

    public Spatial4JToConverter(Module module)
    {
        this.module = module;
    }

    public GeoJsonObject convert(TGeometry intemediate)
    {
        // return transform(intemediate);
        return null;
    }

    private Shape transform(TGeometry intermediate)
    {

        switch(intermediate.getType())
        {
            case POINT              : return null; // return newPoint((TPoint) intemediate);
            case MULTIPOINT         : return null;
            case LINESTRING         : return null;
            case MULTILINESTRING    : return null;
            case POLYGON            : return null;
            case MULTIPOLYGON       : return null;
            case FEATURE            : return null;
            case FEATURECOLLECTION  : return null;
        }

        if (intermediate instanceof TCircle)
        {
            return newCircle((TCircle)intermediate);
        }
        else
            return null;


    }

    private Point newPoint(TPoint point)
    {
        // SPATIAL_CONTEXT.makeCircle(0,0,0).

        // SHAPE_FACTORY.set

        System.out.println("point.x() " + point.x());
        System.out.println("point.y() " + point.y());
        return new Point(point.x(), point.y());
    }

    private Circle newCircle(TCircle circle)
    {
        return ctx.makeCircle(circle.getCentre().x(), circle.getCentre().y(), circle.radius().get());
    }

}