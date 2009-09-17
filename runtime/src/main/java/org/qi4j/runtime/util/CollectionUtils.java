package org.qi4j.runtime.util;

import java.util.Iterator;

public class CollectionUtils
{
    public static <K> Object firstElementOrNull( Iterable iterable )
    {
        Iterator iterator = iterable.iterator();
        if( iterator.hasNext() )
        {
            return iterator.next();
        }
        else
        {
            return null;
        }
    }
}