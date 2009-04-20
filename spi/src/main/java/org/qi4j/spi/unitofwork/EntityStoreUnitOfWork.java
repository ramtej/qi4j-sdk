/*
 * Copyright (c) 2009, Rickard Öberg. All Rights Reserved.
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

package org.qi4j.spi.unitofwork;

import org.qi4j.api.entity.EntityReference;
import org.qi4j.spi.entity.EntityNotFoundException;
import org.qi4j.spi.entity.EntityState;
import org.qi4j.spi.entity.EntityStoreException;
import org.qi4j.spi.unitofwork.event.UnitOfWorkEvents;

/**
 * JAVADOC
 */
public interface EntityStoreUnitOfWork
    extends UnitOfWorkEvents
{
    String identity();

    /**
     * Create new EntityState for a given identity.
     * <p/>
     * This should only create the EntityState and not insert it into any database, since that should occur during
     * the {@link org.qi4j.spi.entity.EntityStore#apply(String, Iterable, org.qi4j.api.usecase.Usecase, org.qi4j.api.common.MetaInfo)} call.
     *
     * @param anIdentity the identity of the entity
     * @return The new entity state.
     * @throws org.qi4j.spi.entity.EntityStoreException
     *          Thrown if creational fails.
     */
    EntityState newEntityState( EntityReference anIdentity )
        throws EntityStoreException;

    /**
     * Get the EntityState for a given identity. Throws {@link org.qi4j.spi.entity.EntityNotFoundException}
     * if the entity with given {@code anIdentity} is not found.
     *
     * @param anIdentity The entity identity. This argument must not be {@code null}.
     * @return Entity state given the composite descriptor and identity.
     * @throws EntityStoreException thrown if retrieval failed.
     * @throws org.qi4j.spi.entity.EntityNotFoundException
     *                              thrown if wanted entity does not exist
     */
    EntityState getEntityState( EntityReference anIdentity )
        throws EntityStoreException, EntityNotFoundException;
}
