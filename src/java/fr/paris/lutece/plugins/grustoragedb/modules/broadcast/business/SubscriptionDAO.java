/*
 * Copyright (c) 2002-2021, City of Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */

package fr.paris.lutece.plugins.grustoragedb.modules.broadcast.business;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.sql.DAOUtil;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.List;

/**
 * This class provides Data Access methods for Subscription objects
 */
public final class SubscriptionDAO implements ISubscriptionDAO
{
    // Constants
    private static final String SQL_QUERY_SELECT = "SELECT id_subscription, demand_type_id, mail, frequency FROM grustoragedb_broadcast_subscription WHERE id_subscription = ?";
    private static final String SQL_QUERY_INSERT = "INSERT INTO grustoragedb_broadcast_subscription ( demand_type_id, mail, frequency ) VALUES ( ?, ?, ? ) ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM grustoragedb_broadcast_subscription WHERE id_subscription = ? ";
    private static final String SQL_QUERY_UPDATE = "UPDATE grustoragedb_broadcast_subscription SET id_subscription = ?, demand_type_id = ?, mail = ?, frequency = ? WHERE id_subscription = ?";
    private static final String SQL_QUERY_SELECTALL = "SELECT id_subscription, demand_type_id, mail, frequency FROM grustoragedb_broadcast_subscription";
    private static final String SQL_QUERY_SELECTALL_ID = "SELECT id_subscription FROM grustoragedb_broadcast_subscription";

    /**
     * {@inheritDoc }
     */
    @Override
    public void insert( Subscription subscription, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, Statement.RETURN_GENERATED_KEYS, plugin ) )
        {
            int nIndex = 1;
            daoUtil.setInt( nIndex++, subscription.getDemandTypeId( ) );
            daoUtil.setString( nIndex++, subscription.getMail( ) );
            daoUtil.setInt( nIndex++, subscription.getFrequency( ) );

            daoUtil.executeUpdate( );
            if ( daoUtil.nextGeneratedKey( ) )
            {
                subscription.setId( daoUtil.getGeneratedKeyInt( 1 ) );
            }
        }

    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Subscription load( int nKey, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, plugin ) )
        {
            daoUtil.setInt( 1, nKey );
            daoUtil.executeQuery( );
            Subscription subscription = null;

            if ( daoUtil.next( ) )
            {
                subscription = new Subscription( );
                int nIndex = 1;

                subscription.setId( daoUtil.getInt( nIndex++ ) );
                subscription.setDemandTypeId( daoUtil.getInt( nIndex++ ) );
                subscription.setMail( daoUtil.getString( nIndex++ ) );
                subscription.setFrequency( daoUtil.getInt( nIndex ) );
            }

            daoUtil.free( );
            return subscription;
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void delete( int nKey, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin ) )
        {
            daoUtil.setInt( 1, nKey );
            daoUtil.executeUpdate( );
            daoUtil.free( );
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void store( Subscription subscription, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin ) )
        {
            int nIndex = 1;

            daoUtil.setInt( nIndex++, subscription.getId( ) );
            daoUtil.setInt( nIndex++, subscription.getDemandTypeId( ) );
            daoUtil.setString( nIndex++, subscription.getMail( ) );
            daoUtil.setInt( nIndex++, subscription.getFrequency( ) );
            daoUtil.setInt( nIndex, subscription.getId( ) );

            daoUtil.executeUpdate( );
            daoUtil.free( );
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<Subscription> selectSubscriptionsList( Plugin plugin )
    {
        List<Subscription> subscriptionList = new ArrayList<>( );
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin ) )
        {
            daoUtil.executeQuery( );

            while ( daoUtil.next( ) )
            {
                Subscription subscription = new Subscription( );
                int nIndex = 1;

                subscription.setId( daoUtil.getInt( nIndex++ ) );
                subscription.setDemandTypeId( daoUtil.getInt( nIndex++ ) );
                subscription.setMail( daoUtil.getString( nIndex++ ) );
                subscription.setFrequency( daoUtil.getInt( nIndex ) );

                subscriptionList.add( subscription );
            }

            daoUtil.free( );
            return subscriptionList;
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<Integer> selectIdSubscriptionsList( Plugin plugin )
    {
        List<Integer> subscriptionList = new ArrayList<>( );
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL_ID, plugin ) )
        {
            daoUtil.executeQuery( );

            while ( daoUtil.next( ) )
            {
                subscriptionList.add( daoUtil.getInt( 1 ) );
            }

            daoUtil.free( );
            return subscriptionList;
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public ReferenceList selectSubscriptionsReferenceList( Plugin plugin )
    {
        ReferenceList subscriptionList = new ReferenceList( );
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin ) )
        {
            daoUtil.executeQuery( );

            while ( daoUtil.next( ) )
            {
                subscriptionList.addItem( daoUtil.getInt( 1 ), daoUtil.getString( 2 ) );
            }

            daoUtil.free( );
            return subscriptionList;
        }
    }
}
