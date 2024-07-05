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

import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotEmpty;
import java.io.Serializable;

/**
 * This is the business class for the object Subscription
 */
public class Subscription implements Serializable
{
    private static final long serialVersionUID = 1L;

    // Variables declarations
    private int _nId;

    private int _nDemandTypeId;

    @NotEmpty( message = "#i18n{module.notificationstore.broadcast.validation.subscription.Mail.notEmpty}" )
    @Size( max = 255, message = "#i18n{module.notificationstore.broadcast.validation.subscription.Mail.size}" )
    private String _strMail;

    private int _nFrequency = 1;

    /**
     * Returns the Id
     * 
     * @return The Id
     */
    public int getId( )
    {
        return _nId;
    }

    /**
     * Sets the Id
     * 
     * @param nId
     *            The Id
     */
    public void setId( int nId )
    {
        _nId = nId;
    }

    /**
     * Returns the DemandTypeId
     * 
     * @return The DemandTypeId
     */
    public int getDemandTypeId( )
    {
        return _nDemandTypeId;
    }

    /**
     * Sets the DemandTypeId
     * 
     * @param nDemandTypeId
     *            The DemandTypeId
     */
    public void setDemandTypeId( int nDemandTypeId )
    {
        _nDemandTypeId = nDemandTypeId;
    }

    /**
     * Returns the Mail
     * 
     * @return The Mail
     */
    public String getMail( )
    {
        return _strMail;
    }

    /**
     * Sets the Mail
     * 
     * @param strMail
     *            The Mail
     */
    public void setMail( String strMail )
    {
        _strMail = strMail;
    }

    /**
     * Returns the Frequency (in hours)
     * 
     * @return The Frequency
     */
    public int getFrequency( )
    {
        return _nFrequency;
    }

    /**
     * Sets the Frequency (in hours)
     * 
     * @param nFrequency
     *            The Frequency
     */
    public void setFrequency( int nFrequency )
    {
        _nFrequency = nFrequency;
    }
}
