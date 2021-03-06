/*
 * Copyright 2010 Niclas Hedhman.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.qi4j.library.cxf;

import java.util.LinkedHashMap;
import java.util.Map;
import javax.jws.WebService;

@WebService( endpointInterface = "org.qi4j.library.cxf.HelloWorld",
             serviceName = "HelloWorld" )
public class HelloWorldImpl implements HelloWorld
{
    private final Map<Integer, User> users;

    public HelloWorldImpl()
    {
        users = new LinkedHashMap<>();
    }

    public String sayHi( String text )
    {
        System.out.println( "sayHi called" );
        return "Hello " + text;
    }

    public String sayHiToUser( User user )
    {
        System.out.println( "sayHiToUser called" );
        users.put( users.size() + 1, user );
        return "Hello " + user.getName();
    }

    public Map<Integer, User> getUsers()
    {
        System.out.println( "getUsers called" );
        return users;
    }
}
