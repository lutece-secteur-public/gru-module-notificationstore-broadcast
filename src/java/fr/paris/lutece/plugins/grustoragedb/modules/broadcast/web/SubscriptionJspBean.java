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
package fr.paris.lutece.plugins.grustoragedb.modules.broadcast.web;

import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.security.SecurityTokenService;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.util.mvc.admin.annotations.Controller;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.util.url.UrlItem;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import fr.paris.lutece.plugins.grustoragedb.modules.broadcast.business.Subscription;
import fr.paris.lutece.plugins.grustoragedb.modules.broadcast.business.SubscriptionHome;

/**
 * This class provides the user interface to manage Subscription features ( manage, create, modify, remove )
 */
@Controller( controllerJsp = "ManageSubscriptions.jsp", controllerPath = "jsp/admin/plugins/grustoragedb/modules/broadcast/", right = "GRUSTORAGEDB_BROADCAST_MANAGEMENT" )
public class SubscriptionJspBean extends AbstractManageSubscriptionsJspBean
{
    // Templates
    private static final String TEMPLATE_MANAGE_SUBSCRIPTIONS = "/admin/plugins/grustoragedb/modules/broadcast/manage_subscriptions.html";
    private static final String TEMPLATE_CREATE_SUBSCRIPTION = "/admin/plugins/grustoragedb/modules/broadcast/create_subscription.html";
    private static final String TEMPLATE_MODIFY_SUBSCRIPTION = "/admin/plugins/grustoragedb/modules/broadcast/modify_subscription.html";

    // Parameters
    private static final String PARAMETER_ID_SUBSCRIPTION = "id";

    // Properties for page titles
    private static final String PROPERTY_PAGE_TITLE_MANAGE_SUBSCRIPTIONS = "module.grustoragedb.broadcast.manage_subscriptions.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_MODIFY_SUBSCRIPTION = "module.grustoragedb.broadcast.modify_subscription.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_CREATE_SUBSCRIPTION = "module.grustoragedb.broadcast.create_subscription.pageTitle";

    // Markers
    private static final String MARK_SUBSCRIPTION_LIST = "subscription_list";
    private static final String MARK_SUBSCRIPTION = "subscription";

    private static final String JSP_MANAGE_SUBSCRIPTIONS = "jsp/admin/plugins/grustoragedb/modules/broadcast/ManageSubscriptions.jsp";

    // Properties
    private static final String MESSAGE_CONFIRM_REMOVE_SUBSCRIPTION = "module.grustoragedb.broadcast.message.confirmRemoveSubscription";

    // Validations
    private static final String VALIDATION_ATTRIBUTES_PREFIX = "module.grustoragedb.broadcast.model.entity.subscription.attribute.";

    // Views
    private static final String VIEW_MANAGE_SUBSCRIPTIONS = "manageSubscriptions";
    private static final String VIEW_CREATE_SUBSCRIPTION = "createSubscription";
    private static final String VIEW_MODIFY_SUBSCRIPTION = "modifySubscription";

    // Actions
    private static final String ACTION_CREATE_SUBSCRIPTION = "createSubscription";
    private static final String ACTION_MODIFY_SUBSCRIPTION = "modifySubscription";
    private static final String ACTION_REMOVE_SUBSCRIPTION = "removeSubscription";
    private static final String ACTION_CONFIRM_REMOVE_SUBSCRIPTION = "confirmRemoveSubscription";

    // Infos
    private static final String INFO_SUBSCRIPTION_CREATED = "module.grustoragedb.broadcast.info.subscription.created";
    private static final String INFO_SUBSCRIPTION_UPDATED = "module.grustoragedb.broadcast.info.subscription.updated";
    private static final String INFO_SUBSCRIPTION_REMOVED = "module.grustoragedb.broadcast.info.subscription.removed";

    // Session variable to store working values
    private Subscription _subscription;

    /**
     * Build the Manage View
     * 
     * @param request
     *            The HTTP request
     * @return The page
     */
    @View( value = VIEW_MANAGE_SUBSCRIPTIONS, defaultView = true )
    public String getManageSubscriptions( HttpServletRequest request )
    {
        _subscription = null;
        List<Subscription> listSubscriptions = SubscriptionHome.getSubscriptionsList( );
        Map<String, Object> model = getPaginatedListModel( request, MARK_SUBSCRIPTION_LIST, listSubscriptions, JSP_MANAGE_SUBSCRIPTIONS );

        return getPage( PROPERTY_PAGE_TITLE_MANAGE_SUBSCRIPTIONS, TEMPLATE_MANAGE_SUBSCRIPTIONS, model );
    }

    /**
     * Returns the form to create a subscription
     *
     * @param request
     *            The Http request
     * @return the html code of the subscription form
     */
    @View( VIEW_CREATE_SUBSCRIPTION )
    public String getCreateSubscription( HttpServletRequest request )
    {
        _subscription = ( _subscription != null ) ? _subscription : new Subscription( );

        Map<String, Object> model = getModel( );
        model.put( MARK_SUBSCRIPTION, _subscription );
        model.put( SecurityTokenService.MARK_TOKEN, SecurityTokenService.getInstance( ).getToken( request, ACTION_CREATE_SUBSCRIPTION ) );

        return getPage( PROPERTY_PAGE_TITLE_CREATE_SUBSCRIPTION, TEMPLATE_CREATE_SUBSCRIPTION, model );
    }

    /**
     * Process the data capture form of a new subscription
     *
     * @param request
     *            The Http Request
     * @return The Jsp URL of the process result
     * @throws AccessDeniedException
     */
    @Action( ACTION_CREATE_SUBSCRIPTION )
    public String doCreateSubscription( HttpServletRequest request ) throws AccessDeniedException
    {
        populate( _subscription, request, getLocale( ) );

        if ( !SecurityTokenService.getInstance( ).validate( request, ACTION_CREATE_SUBSCRIPTION ) )
        {
            throw new AccessDeniedException( "Invalid security token" );
        }

        // Check constraints
        if ( !validateBean( _subscription, VALIDATION_ATTRIBUTES_PREFIX ) )
        {
            return redirectView( request, VIEW_CREATE_SUBSCRIPTION );
        }

        SubscriptionHome.create( _subscription );
        addInfo( INFO_SUBSCRIPTION_CREATED, getLocale( ) );

        return redirectView( request, VIEW_MANAGE_SUBSCRIPTIONS );
    }

    /**
     * Manages the removal form of a subscription whose identifier is in the http request
     *
     * @param request
     *            The Http request
     * @return the html code to confirm
     */
    @Action( ACTION_CONFIRM_REMOVE_SUBSCRIPTION )
    public String getConfirmRemoveSubscription( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_SUBSCRIPTION ) );
        UrlItem url = new UrlItem( getActionUrl( ACTION_REMOVE_SUBSCRIPTION ) );
        url.addParameter( PARAMETER_ID_SUBSCRIPTION, nId );

        String strMessageUrl = AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_REMOVE_SUBSCRIPTION, url.getUrl( ), AdminMessage.TYPE_CONFIRMATION );

        return redirect( request, strMessageUrl );
    }

    /**
     * Handles the removal form of a subscription
     *
     * @param request
     *            The Http request
     * @return the jsp URL to display the form to manage subscriptions
     */
    @Action( ACTION_REMOVE_SUBSCRIPTION )
    public String doRemoveSubscription( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_SUBSCRIPTION ) );
        SubscriptionHome.remove( nId );
        addInfo( INFO_SUBSCRIPTION_REMOVED, getLocale( ) );

        return redirectView( request, VIEW_MANAGE_SUBSCRIPTIONS );
    }

    /**
     * Returns the form to update info about a subscription
     *
     * @param request
     *            The Http request
     * @return The HTML form to update info
     */
    @View( VIEW_MODIFY_SUBSCRIPTION )
    public String getModifySubscription( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_SUBSCRIPTION ) );

        if ( _subscription == null || ( _subscription.getId( ) != nId ) )
        {
            _subscription = SubscriptionHome.findByPrimaryKey( nId );
        }

        Map<String, Object> model = getModel( );
        model.put( MARK_SUBSCRIPTION, _subscription );
        model.put( SecurityTokenService.MARK_TOKEN, SecurityTokenService.getInstance( ).getToken( request, ACTION_MODIFY_SUBSCRIPTION ) );

        return getPage( PROPERTY_PAGE_TITLE_MODIFY_SUBSCRIPTION, TEMPLATE_MODIFY_SUBSCRIPTION, model );
    }

    /**
     * Process the change form of a subscription
     *
     * @param request
     *            The Http request
     * @return The Jsp URL of the process result
     * @throws AccessDeniedException
     */
    @Action( ACTION_MODIFY_SUBSCRIPTION )
    public String doModifySubscription( HttpServletRequest request ) throws AccessDeniedException
    {
        populate( _subscription, request, getLocale( ) );

        if ( !SecurityTokenService.getInstance( ).validate( request, ACTION_MODIFY_SUBSCRIPTION ) )
        {
            throw new AccessDeniedException( "Invalid security token" );
        }

        // Check constraints
        if ( !validateBean( _subscription, VALIDATION_ATTRIBUTES_PREFIX ) )
        {
            return redirect( request, VIEW_MODIFY_SUBSCRIPTION, PARAMETER_ID_SUBSCRIPTION, _subscription.getId( ) );
        }

        SubscriptionHome.update( _subscription );
        addInfo( INFO_SUBSCRIPTION_UPDATED, getLocale( ) );

        return redirectView( request, VIEW_MANAGE_SUBSCRIPTIONS );
    }
}
