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
package fr.paris.lutece.plugins.notificationstore.modules.broadcast.business;

import fr.paris.lutece.plugins.notificationstore.modules.broadcast.business.SubscriptionHome;
import fr.paris.lutece.plugins.notificationstore.modules.broadcast.business.Subscription;
import fr.paris.lutece.test.LuteceTestCase;

/**
 * This is the business class test for the object Subscription
 */
public class SubscriptionBusinessTest extends LuteceTestCase
{
    private static final int DEMANDTYPEID1 = 1;
    private static final int DEMANDTYPEID2 = 2;
    private static final String MAIL1 = "Mail1";
    private static final String MAIL2 = "Mail2";
    private static final int FREQUENCY1 = 1;
    private static final int FREQUENCY2 = 2;

    /**
     * test Subscription
     */
    public void testBusiness( )
    {
        // Initialize an object
        Subscription subscription = new Subscription( );
        subscription.setDemandTypeId( DEMANDTYPEID1 );
        subscription.setMail( MAIL1 );
        subscription.setFrequency( FREQUENCY1 );

        // Create test
        SubscriptionHome.create( subscription );
        Subscription subscriptionStored = SubscriptionHome.findByPrimaryKey( subscription.getId( ) );
        assertEquals( subscriptionStored.getDemandTypeId( ), subscription.getDemandTypeId( ) );
        assertEquals( subscriptionStored.getMail( ), subscription.getMail( ) );
        assertEquals( subscriptionStored.getFrequency( ), subscription.getFrequency( ) );

        // Update test
        subscription.setDemandTypeId( DEMANDTYPEID2 );
        subscription.setMail( MAIL2 );
        subscription.setFrequency( FREQUENCY2 );
        SubscriptionHome.update( subscription );
        subscriptionStored = SubscriptionHome.findByPrimaryKey( subscription.getId( ) );
        assertEquals( subscriptionStored.getDemandTypeId( ), subscription.getDemandTypeId( ) );
        assertEquals( subscriptionStored.getMail( ), subscription.getMail( ) );
        assertEquals( subscriptionStored.getFrequency( ), subscription.getFrequency( ) );

        // List test
        SubscriptionHome.getSubscriptionsList( );

        // Delete test
        SubscriptionHome.remove( subscription.getId( ) );
        subscriptionStored = SubscriptionHome.findByPrimaryKey( subscription.getId( ) );
        assertNull( subscriptionStored );

    }

}
