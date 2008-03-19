/*
 * Copyright (c) 2007, Rickard Öberg. All Rights Reserved.
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

package org.qi4j.spi.association;

import java.lang.reflect.Type;
import java.util.AbstractList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import org.qi4j.association.AssociationInfo;
import org.qi4j.association.AssociationVetoException;
import org.qi4j.association.ListAssociation;
import org.qi4j.entity.EntityComposite;

/**
 * Implementation of ListAssociation, which delegates to an
 * ordinary List.
 */
public final class ListAssociationInstance<T> extends AbstractList<T>
    implements ListAssociation<T>
{
    private AssociationInfo associationInfo;
    private List<T> associated;

    public ListAssociationInstance( List<T> associated, AssociationInfo associationInfo )
    {
        this.associationInfo = associationInfo;
        this.associated = associated;
    }

    public <T> T getAssociationInfo( Class<T> infoType )
    {
        return associationInfo.getAssociationInfo( infoType );
    }

    public String getName()
    {
        return associationInfo.getName();
    }

    public String getQualifiedName()
    {
        return associationInfo.getQualifiedName();
    }

    public Type getAssociationType()
    {
        return associationInfo.getAssociationType();
    }

    @Override public T get( int i )
    {
        return associated.get( i );
    }

    @Override public int size()
    {
        return associated.size();
    }


    @Override public boolean add( T t )
    {
        if( !( t instanceof EntityComposite ) )
        {
            throw new AssociationVetoException( "Associated object must be an EntityComposite" );
        }

        return associated.add( t );
    }


    @Override public T set( int i, T t )
    {
        if( !( t instanceof EntityComposite ) )
        {
            throw new AssociationVetoException( "Associated object must be an EntityComposite" );
        }

        return associated.set( i, t );
    }

    @Override public void add( int i, T t )
    {
        if( !( t instanceof EntityComposite ) )
        {
            throw new AssociationVetoException( "Associated object must be an EntityComposite" );
        }

        associated.add( i, t );
    }

    @Override public T remove( int i )
    {
        return associated.remove( i );
    }

    @Override public int indexOf( Object o )
    {
        if( !( o instanceof EntityComposite ) )
        {
            throw new AssociationVetoException( "Object must be an EntityComposite" );
        }

        return associated.indexOf( o );
    }

    @Override public int lastIndexOf( Object o )
    {
        if( !( o instanceof EntityComposite ) )
        {
            throw new AssociationVetoException( "Object must be an EntityComposite" );
        }

        return associated.lastIndexOf( o );
    }

    @Override public void clear()
    {
        associated.clear();
    }

    @Override public boolean addAll( int i, Collection<? extends T> ts )
    {
        return associated.addAll( i, ts );
    }

    @Override public Iterator<T> iterator()
    {
        return associated.iterator();
    }

    @Override public ListIterator<T> listIterator()
    {
        return associated.listIterator();
    }

    @Override public ListIterator<T> listIterator( int i )
    {
        return associated.listIterator( i );
    }

    @Override public List<T> subList( int i, int i1 )
    {
        return associated.subList( i, i1 );
    }

    @Override public boolean equals( Object o )
    {
        return associated.equals( o );
    }

    @Override public int hashCode()
    {
        return associated.hashCode();
    }

    @Override public boolean isEmpty()
    {
        return associated.isEmpty();
    }

    @Override public boolean contains( Object o )
    {
        return associated.contains( o );
    }

    @Override public Object[] toArray()
    {
        return associated.toArray();
    }

    @Override public <T> T[] toArray( T[] ts )
    {
        return associated.toArray( ts );
    }

    @Override public boolean remove( Object o )
    {
        return associated.remove( o );
    }

    @Override public boolean containsAll( Collection<?> objects )
    {
        return associated.containsAll( objects );
    }

    @Override public boolean addAll( Collection<? extends T> ts )
    {
        return associated.addAll( ts );
    }

    @Override public boolean removeAll( Collection<?> objects )
    {
        return associated.removeAll( objects );
    }

    @Override public boolean retainAll( Collection<?> objects )
    {
        return associated.retainAll( objects );
    }

    @Override public String toString()
    {
        return associated.toString();
    }
}