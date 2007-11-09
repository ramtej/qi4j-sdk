/*
 * Copyright (c) 2007, Rickard �berg. All Rights Reserved.
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

package org.qi4j.model;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.annotation.Annotation;
import java.text.MessageFormat;
import java.util.Iterator;

/**
 * Modifiers provide stateless modifications of method invocation behaviour.
 * <p/>
 * Modifiers can either be classes implementing the interfaces of the modified
 * methods, or they can be generic InvocationHandler mixins.
 */
public abstract class ModifierModel<T>
    extends FragmentModel<T>
{
    private Dependency modifiesDependency;

    public ModifierModel( Class<T> fragmentClass, Iterable<ConstructorDependency> constructorDependencies, Iterable<FieldDependency> fieldDependencies, Iterable<MethodDependency> methodDependencies, Class[] appliesTo, Class declaredBy )
    {
        super( fragmentClass, constructorDependencies, fieldDependencies, methodDependencies, appliesTo, declaredBy );

        Iterator<Dependency> modifies = getDependenciesByScope( getModifiesAnnotationType() ).iterator();
        if( modifies.hasNext() )
        {
            this.modifiesDependency = modifies.next();
        }
        else
        {
            String msg = MessageFormat.format( "Invocation {0} does not have any member fields marked with @{1}.", fragmentClass.getName(), getModifiesAnnotationType().getSimpleName() );
            throw new InvalidFragmentException( msg, fragmentClass );
        }
        if( modifies.hasNext() )
        {
            String msg = MessageFormat.format( "Invocation {0} has many member fields marked with @{1}.", fragmentClass.getName(), getModifiesAnnotationType().getSimpleName() );
            throw new InvalidFragmentException( msg, fragmentClass );
        }
    }

    public abstract Class<? extends Annotation> getModifiesAnnotationType();

    public Dependency getModifiesDependency()
    {
        return modifiesDependency;
    }

    public String toString()
    {
        String string = super.toString();

        StringWriter str = new StringWriter();
        PrintWriter out = new PrintWriter( str );
        out.println( "  @" + getModifiesAnnotationType().getSimpleName() );
        out.println( "    " + modifiesDependency.getKey().getRawType().getSimpleName() );

        if( getAppliesTo() != null )
        {
            out.println( "  @AppliesTo" );
            for( Class aClass : getAppliesTo() )
            {
                out.println( "    " + aClass.getName() );
            }
        }
        out.close();
        return string + str.toString();
    }
}