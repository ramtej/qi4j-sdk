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

package org.qi4j.service;

import org.qi4j.composite.Mixins;
import org.qi4j.injection.scope.Uses;
import java.io.Serializable;

/**
 * Services which simply wraps some resource should extend this interface
 * and specify the type of resource.
 * <p/>
 * If the default WrapperMixin is used, then register the wrapped object
 * as meta-info for the service using:<br/>
 * assembly.addService(YouService.class).setMetaInfo(new WrappedObject(wrappedObject));
 */
@Mixins( Wrapper.WrapperMixin.class)
public interface Wrapper<T>
{
    T get();

    public final class WrapperMixin<T>
        implements Wrapper<T>
    {
        T wrapped;

        public WrapperMixin(@Uses ServiceDescriptor descriptor)
        {
            wrapped = (T) descriptor.metaInfo().get( WrappedObject.class ).get();
        }

        public T get()
        {
            return wrapped;
        }
    }

    public final class WrappedObject
        implements Serializable, Wrapper<Object>
    {
        Object wrapped;

        public WrappedObject( Object wrapped )
        {
            this.wrapped = wrapped;
        }

        public Object get()
        {
            return wrapped;
        }
    }
}