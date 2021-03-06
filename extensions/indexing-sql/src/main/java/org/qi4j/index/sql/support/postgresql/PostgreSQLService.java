/*
 * Copyright (c) 2010, Stanislav Muhametsin. All Rights Reserved.
 * Copyright (c) 2012, Paul Merlin. All Rights Reserved.
 *
 * Licensed  under the  Apache License,  Version 2.0  (the "License");
 * you may not use  this file  except in  compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed  under the  License is distributed on an "AS IS" BASIS,
 * WITHOUT  WARRANTIES OR CONDITIONS  OF ANY KIND, either  express  or
 * implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.qi4j.index.sql.support.postgresql;

import org.qi4j.api.mixin.Mixins;
import org.qi4j.index.sql.SQLIndexingEngineService;
import org.qi4j.index.sql.internal.SQLEntityFinder;
import org.qi4j.index.sql.internal.SQLStateChangeListener;
import org.qi4j.index.sql.support.api.SQLAppStartup;
import org.qi4j.index.sql.support.api.SQLIndexing;
import org.qi4j.index.sql.support.api.SQLQuerying;
import org.qi4j.index.sql.support.postgresql.PostgreSQLTypeHelper.SQLTypeHelperMixin;
import org.qi4j.spi.query.IndexExporter;

/**
 * This is actual service responsible of managing indexing and queries and creating database structure.
 * 
 * The reason why all these components are in one single service is that they all require some data about the database
 * structure. Rather than exposing all of that data publicly to be available via another service, it is stored in a
 * state-style private mixin. Thus all the database-related data is available only to this service, and no one else.
 */
@Mixins(
{
    SQLEntityFinder.class,
    SQLStateChangeListener.class,
    PostgreSQLAppStartup.class,//
    PostgreSQLIndexing.class,//
    PostgreSQLQuerying.class,//
    SQLTypeHelperMixin.class,//
    PostgreSQLIndexExporter.class
})
public interface PostgreSQLService
    extends SQLAppStartup, SQLIndexing, SQLQuerying, IndexExporter, SQLIndexingEngineService
{

}
