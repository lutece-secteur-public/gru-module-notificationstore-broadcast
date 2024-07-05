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

package fr.paris.lutece.plugins.notificationstore.modules.broadcast.rs;

import fr.paris.lutece.plugins.notificationstore.modules.broadcast.business.Subscription;
import fr.paris.lutece.plugins.notificationstore.modules.broadcast.business.SubscriptionHome;
import fr.paris.lutece.plugins.rest.service.RestConstants;
import fr.paris.lutece.util.json.ErrorJsonResponse;
import fr.paris.lutece.util.json.JsonResponse;
import fr.paris.lutece.util.json.JsonUtil;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import java.util.List;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * SubscriptionRest
 */
@Path( RestConstants.BASE_PATH + Constants.API_PATH + Constants.VERSION_PATH + Constants.SUBSCRIPTION_PATH )
public class SubscriptionRest
{
    private static final int VERSION_1 = 1;
    private final Logger _logger = Logger.getLogger( RestConstants.REST_LOGGER );

    /**
     * Get Subscription List
     * 
     * @param nVersion
     *            the API version
     * @return the Subscription List
     */
    @GET
    @Path( StringUtils.EMPTY )
    @Produces( MediaType.APPLICATION_JSON )
    public Response getSubscriptionList( @PathParam( Constants.VERSION ) Integer nVersion )
    {
        if ( nVersion == VERSION_1 )
        {
            return getSubscriptionListV1( );
        }
        _logger.error( Constants.ERROR_NOT_FOUND_VERSION );
        return Response.status( Response.Status.NOT_FOUND )
                .entity( JsonUtil.buildJsonResponse( new ErrorJsonResponse( Response.Status.NOT_FOUND.name( ), Constants.ERROR_NOT_FOUND_VERSION ) ) ).build( );
    }

    /**
     * Get Subscription List V1
     * 
     * @return the Subscription List for the version 1
     */
    private Response getSubscriptionListV1( )
    {
        List<Subscription> listSubscriptions = SubscriptionHome.getSubscriptionsList( );

        if ( listSubscriptions.isEmpty( ) )
        {
            return Response.status( Response.Status.NO_CONTENT ).entity( JsonUtil.buildJsonResponse( new JsonResponse( Constants.EMPTY_OBJECT ) ) ).build( );
        }
        return Response.status( Response.Status.OK ).entity( JsonUtil.buildJsonResponse( new JsonResponse( listSubscriptions ) ) ).build( );
    }

    /**
     * Create Subscription
     * 
     * @param nVersion
     *            the API version
     * @param demand_type_id
     *            the demand_type_id
     * @param mail
     *            the mail
     * @param frequency
     *            the frequency
     * @return the Subscription if created
     */
    @POST
    @Path( StringUtils.EMPTY )
    @Produces( MediaType.APPLICATION_JSON )
    public Response createSubscription( @FormParam( Constants.SUBSCRIPTION_ATTRIBUTE_DEMAND_TYPE_ID ) String demand_type_id,
            @FormParam( Constants.SUBSCRIPTION_ATTRIBUTE_MAIL ) String mail, @FormParam( Constants.SUBSCRIPTION_ATTRIBUTE_FREQUENCY ) String frequency,
            @PathParam( Constants.VERSION ) Integer nVersion )
    {
        if ( nVersion == VERSION_1 )
        {
            return createSubscriptionV1( demand_type_id, mail, frequency );
        }
        _logger.error( Constants.ERROR_NOT_FOUND_VERSION );
        return Response.status( Response.Status.NOT_FOUND )
                .entity( JsonUtil.buildJsonResponse( new ErrorJsonResponse( Response.Status.NOT_FOUND.name( ), Constants.ERROR_NOT_FOUND_VERSION ) ) ).build( );
    }

    /**
     * Create Subscription V1
     * 
     * @param demand_type_id
     *            the demand_type_id
     * @param mail
     *            the mail
     * @param frequency
     *            the frequency
     * @return the Subscription if created for the version 1
     */
    private Response createSubscriptionV1( String demand_type_id, String mail, String frequency )
    {
        if ( StringUtils.isEmpty( demand_type_id ) || StringUtils.isEmpty( mail ) || StringUtils.isEmpty( frequency ) )
        {
            _logger.error( Constants.ERROR_BAD_REQUEST_EMPTY_PARAMETER );
            return Response.status( Response.Status.BAD_REQUEST )
                    .entity( JsonUtil
                            .buildJsonResponse( new ErrorJsonResponse( Response.Status.BAD_REQUEST.name( ), Constants.ERROR_BAD_REQUEST_EMPTY_PARAMETER ) ) )
                    .build( );
        }

        Subscription subscription = new Subscription( );
        subscription.setDemandTypeId( Integer.parseInt( demand_type_id ) );
        subscription.setMail( mail );
        subscription.setFrequency( Integer.parseInt( frequency ) );
        SubscriptionHome.create( subscription );

        return Response.status( Response.Status.OK ).entity( JsonUtil.buildJsonResponse( new JsonResponse( subscription ) ) ).build( );
    }

    /**
     * Modify Subscription
     * 
     * @param nVersion
     *            the API version
     * @param id
     *            the id
     * @param demand_type_id
     *            the demand_type_id
     * @param mail
     *            the mail
     * @param frequency
     *            the frequency
     * @return the Subscription if modified
     */
    @PUT
    @Path( Constants.ID_PATH )
    @Produces( MediaType.APPLICATION_JSON )
    public Response modifySubscription( @PathParam( Constants.ID ) Integer id,
            @FormParam( Constants.SUBSCRIPTION_ATTRIBUTE_DEMAND_TYPE_ID ) String demand_type_id,
            @FormParam( Constants.SUBSCRIPTION_ATTRIBUTE_MAIL ) String mail, @FormParam( Constants.SUBSCRIPTION_ATTRIBUTE_FREQUENCY ) String frequency,
            @PathParam( Constants.VERSION ) Integer nVersion )
    {
        if ( nVersion == VERSION_1 )
        {
            return modifySubscriptionV1( id, demand_type_id, mail, frequency );
        }
        _logger.error( Constants.ERROR_NOT_FOUND_VERSION );
        return Response.status( Response.Status.NOT_FOUND )
                .entity( JsonUtil.buildJsonResponse( new ErrorJsonResponse( Response.Status.NOT_FOUND.name( ), Constants.ERROR_NOT_FOUND_VERSION ) ) ).build( );
    }

    /**
     * Modify Subscription V1
     * 
     * @param id
     *            the id
     * @param demand_type_id
     *            the demand_type_id
     * @param mail
     *            the mail
     * @param frequency
     *            the frequency
     * @return the Subscription if modified for the version 1
     */
    private Response modifySubscriptionV1( Integer id, String demand_type_id, String mail, String frequency )
    {
        if ( StringUtils.isEmpty( demand_type_id ) || StringUtils.isEmpty( mail ) || StringUtils.isEmpty( frequency ) )
        {
            _logger.error( Constants.ERROR_BAD_REQUEST_EMPTY_PARAMETER );
            return Response.status( Response.Status.BAD_REQUEST )
                    .entity( JsonUtil
                            .buildJsonResponse( new ErrorJsonResponse( Response.Status.BAD_REQUEST.name( ), Constants.ERROR_BAD_REQUEST_EMPTY_PARAMETER ) ) )
                    .build( );
        }

        Subscription subscription = SubscriptionHome.findByPrimaryKey( id );
        if ( subscription == null )
        {
            _logger.error( Constants.ERROR_NOT_FOUND_RESOURCE );
            return Response.status( Response.Status.NOT_FOUND )
                    .entity( JsonUtil.buildJsonResponse( new ErrorJsonResponse( Response.Status.NOT_FOUND.name( ), Constants.ERROR_NOT_FOUND_RESOURCE ) ) )
                    .build( );
        }

        subscription.setDemandTypeId( Integer.parseInt( demand_type_id ) );
        subscription.setMail( mail );
        subscription.setFrequency( Integer.parseInt( frequency ) );
        SubscriptionHome.update( subscription );

        return Response.status( Response.Status.OK ).entity( JsonUtil.buildJsonResponse( new JsonResponse( subscription ) ) ).build( );
    }

    /**
     * Delete Subscription
     * 
     * @param nVersion
     *            the API version
     * @param id
     *            the id
     * @return the Subscription List if deleted
     */
    @DELETE
    @Path( Constants.ID_PATH )
    @Produces( MediaType.APPLICATION_JSON )
    public Response deleteSubscription( @PathParam( Constants.VERSION ) Integer nVersion, @PathParam( Constants.ID ) Integer id )
    {
        if ( nVersion == VERSION_1 )
        {
            return deleteSubscriptionV1( id );
        }
        _logger.error( Constants.ERROR_NOT_FOUND_VERSION );
        return Response.status( Response.Status.NOT_FOUND )
                .entity( JsonUtil.buildJsonResponse( new ErrorJsonResponse( Response.Status.NOT_FOUND.name( ), Constants.ERROR_NOT_FOUND_VERSION ) ) ).build( );
    }

    /**
     * Delete Subscription V1
     * 
     * @param id
     *            the id
     * @return the Subscription List if deleted for the version 1
     */
    private Response deleteSubscriptionV1( Integer id )
    {
        Subscription subscription = SubscriptionHome.findByPrimaryKey( id );
        if ( subscription == null )
        {
            _logger.error( Constants.ERROR_NOT_FOUND_RESOURCE );
            return Response.status( Response.Status.NOT_FOUND )
                    .entity( JsonUtil.buildJsonResponse( new ErrorJsonResponse( Response.Status.NOT_FOUND.name( ), Constants.ERROR_NOT_FOUND_RESOURCE ) ) )
                    .build( );
        }

        SubscriptionHome.remove( id );

        return Response.status( Response.Status.OK ).entity( JsonUtil.buildJsonResponse( new JsonResponse( Constants.EMPTY_OBJECT ) ) ).build( );
    }

    /**
     * Get Subscription
     * 
     * @param nVersion
     *            the API version
     * @param id
     *            the id
     * @return the Subscription
     */
    @GET
    @Path( Constants.ID_PATH )
    @Produces( MediaType.APPLICATION_JSON )
    public Response getSubscription( @PathParam( Constants.VERSION ) Integer nVersion, @PathParam( Constants.ID ) Integer id )
    {
        if ( nVersion == VERSION_1 )
        {
            return getSubscriptionV1( id );
        }
        _logger.error( Constants.ERROR_NOT_FOUND_VERSION );
        return Response.status( Response.Status.NOT_FOUND )
                .entity( JsonUtil.buildJsonResponse( new ErrorJsonResponse( Response.Status.NOT_FOUND.name( ), Constants.ERROR_NOT_FOUND_VERSION ) ) ).build( );
    }

    /**
     * Get Subscription V1
     * 
     * @param id
     *            the id
     * @return the Subscription for the version 1
     */
    private Response getSubscriptionV1( Integer id )
    {
        Subscription subscription = SubscriptionHome.findByPrimaryKey( id );
        if ( subscription == null )
        {
            _logger.error( Constants.ERROR_NOT_FOUND_RESOURCE );
            return Response.status( Response.Status.NOT_FOUND )
                    .entity( JsonUtil.buildJsonResponse( new ErrorJsonResponse( Response.Status.NOT_FOUND.name( ), Constants.ERROR_NOT_FOUND_RESOURCE ) ) )
                    .build( );
        }

        return Response.status( Response.Status.OK ).entity( JsonUtil.buildJsonResponse( new JsonResponse( subscription ) ) ).build( );
    }
}
