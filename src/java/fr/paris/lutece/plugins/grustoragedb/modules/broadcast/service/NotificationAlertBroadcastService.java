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
package fr.paris.lutece.plugins.grustoragedb.modules.broadcast.service;

import fr.paris.lutece.plugins.grubusiness.business.demand.DemandService;
import fr.paris.lutece.plugins.grubusiness.business.notification.NotificationEvent;
import fr.paris.lutece.plugins.grustoragedb.modules.broadcast.business.Subscription;
import fr.paris.lutece.plugins.grustoragedb.modules.broadcast.business.SubscriptionHome;
import fr.paris.lutece.portal.service.mail.MailService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.html.HtmlTemplate;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map.Entry;

/**
 *
 * @author seboo
 */
public class NotificationAlertBroadcastService
{

    // constants
    private static String DEMAND_SERVICE_BEAN_NAME = "grusupply.storageService";
    private static String STATUS_FAILED = "FAILED";
    private static String KEY_NOTIFICATION_EVENT_LIST = "notification_event_list";
    private static String KEY_START = "start";
    private static String KEY_END = "end";
    
    private static String PROPERTY_GRU_ALERTS_FROM_NAME ="grustoragedb-broadcast.mail.from.name";
    private static String PROPERTY_GRU_ALERTS_FROM_MAIL ="grustoragedb-broadcast.mail.from.mail";
    private static String PROPERTY_GRU_ALERTS_SUBJECT   ="grustoragedb-broadcast.mail.subject";
    private static String GRU_ALERTS_FROM_NAME = AppPropertiesService.getProperty( PROPERTY_GRU_ALERTS_FROM_NAME, "GRU ESB Notification alerts Daemon" );
    private static String GRU_ALERTS_FROM_MAIL = AppPropertiesService.getProperty( PROPERTY_GRU_ALERTS_FROM_MAIL, "no-reply@paris.fr" );
    private static String GRU_ALERTS_SUBJECT = AppPropertiesService.getProperty( PROPERTY_GRU_ALERTS_SUBJECT, "GRU ESB notifications alerts - demand type : %s" );
    
    // Templates
    private static final String TEMPLATE_MAIL = "/admin/plugins/grustoragedb/modules/broadcast/mail.html";

    public static String broadcast( Locale defaultLocale )
    {

        // get subscripters list
        List<Subscription> listSub = SubscriptionHome.getSubscriptionsList( );

        if ( listSub.isEmpty( ) )
        {
            return "no subscribers";
        }

        // get concerned demand_type_id
        Map<String, Subscription> mapBroadcastFeeds = new HashMap<>( );
        Map<String, List<String>> mapBroadcastFeedRecipients = new HashMap<>( );

        for ( Subscription sub : listSub )
        {
            String strBroadcastFeedKey = getKey( sub );

            if ( !mapBroadcastFeeds.containsKey( strBroadcastFeedKey ) )
            {
                Subscription broadcastFeed = new Subscription( );
                broadcastFeed.setDemandTypeId( sub.getDemandTypeId( ) );
                broadcastFeed.setFrequency( sub.getFrequency( ) );

                mapBroadcastFeeds.put( strBroadcastFeedKey, broadcastFeed );
                mapBroadcastFeedRecipients.put( strBroadcastFeedKey, new ArrayList<String>( ) );
            }

            mapBroadcastFeedRecipients.get( strBroadcastFeedKey ).add( sub.getMail( ) );
        }

        // select last alerts by demand_type_id by frequency
        // & send alert list to subscribers
        DemandService storageService = SpringContextService.getBean( DEMAND_SERVICE_BEAN_NAME );
        int nbMailSent = 0;
        int nbEvent = 0;

        for ( Map.Entry<String, Subscription> entry : mapBroadcastFeeds.entrySet( ) )
        {
            Subscription broadcastFeed = entry.getValue( );

            LocalDateTime ldtNow = LocalDateTime.now( );
            long endPeriod = Timestamp.valueOf( ldtNow ).getTime( );
            long startPeriod = Timestamp.valueOf( ldtNow.minusHours( broadcastFeed.getFrequency( ) ) ).getTime( );

            List<NotificationEvent> listEvent = storageService.findEventsByDateAndDemandTypeIdAndStatus(startPeriod, endPeriod,
                    String.valueOf( broadcastFeed.getDemandTypeId( ) ), STATUS_FAILED );

            if ( !listEvent.isEmpty( ) )
            {
                Map<String, Object> model = new HashMap<>( );
                model.put( KEY_START, startPeriod );
                model.put( KEY_END, endPeriod );
                
                model.put( KEY_NOTIFICATION_EVENT_LIST, listEvent );

                HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MAIL, defaultLocale, model );
                String strToList = String.join( ",", mapBroadcastFeedRecipients.get( entry.getKey( ) ) );

                MailService.sendMailHtml( strToList, GRU_ALERTS_FROM_NAME, GRU_ALERTS_FROM_MAIL,
                		String.format(GRU_ALERTS_SUBJECT, String.valueOf( broadcastFeed.getDemandTypeId( ) ) ), template.getHtml( ) );

                nbMailSent++;
                nbEvent += listEvent.size( );
            }
        }

        return nbMailSent + " mail(s) sent.";
    }

    /**
     * get unique key
     * 
     * @param sub
     * @return the key as String
     */
    private static String getKey( Subscription sub )
    {
        return sub.getDemandTypeId( ) + "|" + sub.getFrequency( );
    }
}
