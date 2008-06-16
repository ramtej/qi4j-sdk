/*
 * Copyright (c) 2008, Rickard Öberg. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.qi4j.runtime.injection;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Collection;
import org.qi4j.runtime.composite.BindingException;
import org.qi4j.runtime.composite.Resolution;
import org.qi4j.runtime.structure.ModelVisitor;
import org.qi4j.runtime.structure.Specification;

/**
 * TODO
 */
public final class InjectedFieldModel
{
    private final DependencyModel dependencyModel;
    private final Field injectedField;

    public InjectedFieldModel( Field injectedField, DependencyModel dependencyModel )
    {
        injectedField.setAccessible( true );
        this.injectedField = injectedField;
        this.dependencyModel = dependencyModel;
    }

    public DependencyModel dependency()
    {
        return dependencyModel;
    }

    public Field field()
    {
        return injectedField;
    }

    public void bind( Resolution resolution ) throws BindingException
    {
        dependencyModel.bind( resolution.forField( injectedField ) );
    }

    public void inject( InjectionContext context, Object instance )
    {
        Object value = dependencyModel.inject( context );
        try
        {
            injectedField.set( instance, value );
        }
        catch( IllegalAccessException e )
        {
            throw new InjectionException( e );
        }
        catch( IllegalArgumentException e )
        {
            throw new InjectionException( e );
        }
    }


    public void visitModel( ModelVisitor modelVisitor )
    {
        modelVisitor.visit( this );
    }

    public Collection<DependencyModel> filter( Specification<DependencyModel> specification )
    {
        return specification.matches( dependencyModel ) ? Collections.singleton( dependencyModel ) : Collections.<DependencyModel>emptyList();
    }
}