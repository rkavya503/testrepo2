/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.email.NotificationMethod.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.email;

import java.io.Serializable;

/**
 * The Class NotificationMethod.
 */
public class NotificationMethod implements Serializable {
    
    /** The Constant MEDIA_AKUA_MAIL. */
    public static final String MEDIA_AKUA_MAIL = "akuacom";
    
    /** The Constant ENVOY_MESSAGE. */
    public static final String ENVOY_MESSAGE = "envoy";

    /** The media. */
    private String media;
    
    /** The email. */
    private boolean email;
    
    /** The fax. */
    private boolean fax;
    
    /** The voice. */
    private boolean voice;
    
    /** The epage. */
    private boolean epage;

    private boolean sms;

    /**
     * Gets the media.
     * 
     * @return the media
     */
    public String getMedia() {
        return media;
    }

    /**
     * Sets the media.
     * 
     * @param media the new media
     */
    public void setMedia(String media) {
        this.media = media;
    }

    /**
     * Checks if is email.
     * 
     * @return true, if is email
     */
    public boolean isEmail() {
        return email;
    }

    /**
     * Sets the email.
     * 
     * @param email the new email
     */
    public void setEmail(boolean email) {
        this.email = email;
    }

    /**
     * Checks if is fax.
     * 
     * @return true, if is fax
     */
    public boolean isFax() {
        return fax;
    }

    /**
     * Sets the fax.
     * 
     * @param fax the new fax
     */
    public void setFax(boolean fax) {
        this.fax = fax;
    }

    /**
     * Checks if is voice.
     * 
     * @return true, if is voice
     */
    public boolean isVoice() {
        return voice;
    }

    /**
     * Sets the voice.
     * 
     * @param voice the new voice
     */
    public void setVoice(boolean voice) {
        this.voice = voice;
    }

    /**
     * Checks if is epage.
     * 
     * @return true, if is epage
     */
    public boolean isEpage() {
        return epage;
    }

    /**
     * Sets the epage.
     * 
     * @param epage the new epage
     */
    public void setEpage(boolean epage) {
        this.epage = epage;
    }

    public boolean isSms() {
        return sms;
    }

    public void setSms(boolean sms) {
        this.sms = sms;
    }

    /**
     * Gets the single instance of NotificationMethod.
     * 
     * @return single instance of NotificationMethod
     */
    public static NotificationMethod getInstance() {
        NotificationMethod method = new NotificationMethod();
        method.setMedia(MEDIA_AKUA_MAIL);
        method.setEmail(true);
        return method;
    }
}
