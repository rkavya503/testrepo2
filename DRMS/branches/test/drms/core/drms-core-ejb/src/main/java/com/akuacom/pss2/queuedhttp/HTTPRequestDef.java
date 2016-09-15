/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.akuacom.pss2.queuedhttp;

import java.io.Serializable;
import java.net.URI;
import java.net.URL;

/**
 *
 * @author spierson
 */
public class HTTPRequestDef implements Serializable {

    public final static int METHOD_GET =0;
    public final static int METHOD_POST =1;

    private String description;
    private String payload;
    private URI destination;
    private int method = METHOD_GET;
    private String name;
    private String password;

    public HTTPRequestDef(String description, URI destination, String payload, int method, String name, String password) {
        this.description = description;
        this.payload = payload;
        this.destination = destination;
        this.method = method;
        this.name = name;
        this.password = password;
    }


    /**
     * @return the payload
     */
    public String getPayload() {
        return payload;
    }

    /**
     * @param payload the payload to set
     */
    public void setPayload(String payload) {
        this.payload = payload;
    }

    /**
     * @return the destination
     */
    public URI getDestination() {
        return destination;
    }

    /**
     * @param destination the destination to set
     */
    public void setDestination(URI destination) {
        this.destination = destination;
    }

    /**
     * @return the method
     */
    public int getMethod() {
        return method;
    }

    /**
     * @param method the method to set
     */
    public void setMethod(int method) {
        this.method = method;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    

}
