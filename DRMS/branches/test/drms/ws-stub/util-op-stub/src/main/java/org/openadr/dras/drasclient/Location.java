/*
 * www.akuacom.com - Automating Demand Response
 * 
 * org.openadr.dras.drasclient.Location.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */

package org.openadr.dras.drasclient;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * An entity used to describe a location.
 * 
 * 
 * <p>Java class for Location complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Location">
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="gridLocation" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 * &lt;element name="address" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 * &lt;element name="coordinate" minOccurs="0">
 * &lt;complexType>
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="latitude" type="{http://www.w3.org/2001/XMLSchema}double"/>
 * &lt;element name="longitude" type="{http://www.w3.org/2001/XMLSchema}double"/>
 * &lt;/sequence>
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * &lt;/element>
 * &lt;/sequence>
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Location", propOrder = {
    "gridLocation",
    "address",
    "coordinate"
})
public class Location
    implements Serializable
{

    /** The grid location. */
    protected String gridLocation;
    
    /** The address. */
    protected String address;
    
    /** The coordinate. */
    @XmlElement(nillable = true)
    protected Location.Coordinate coordinate;

    /**
     * Gets the value of the gridLocation property.
     * 
     * @return the grid location
     * 
     * possible object is
     * {@link String }
     */
    public String getGridLocation() {
        return gridLocation;
    }

    /**
     * Sets the value of the gridLocation property.
     * 
     * @param value allowed object is
     * {@link String }
     */
    public void setGridLocation(String value) {
        this.gridLocation = value;
    }

    /**
     * Gets the value of the address property.
     * 
     * @return the address
     * 
     * possible object is
     * {@link String }
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets the value of the address property.
     * 
     * @param value allowed object is
     * {@link String }
     */
    public void setAddress(String value) {
        this.address = value;
    }

    /**
     * Gets the value of the coordinate property.
     * 
     * @return the coordinate
     * 
     * possible object is
     * {@link Location.Coordinate }
     */
    public Location.Coordinate getCoordinate() {
        return coordinate;
    }

    /**
     * Sets the value of the coordinate property.
     * 
     * @param value allowed object is
     * {@link Location.Coordinate }
     */
    public void setCoordinate(Location.Coordinate value) {
        this.coordinate = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     * &lt;complexContent>
     * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     * &lt;sequence>
     * &lt;element name="latitude" type="{http://www.w3.org/2001/XMLSchema}double"/>
     * &lt;element name="longitude" type="{http://www.w3.org/2001/XMLSchema}double"/>
     * &lt;/sequence>
     * &lt;/restriction>
     * &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "latitude",
        "longitude"
    })
    public static class Coordinate
        implements Serializable
    {

        /** The latitude. */
        protected double latitude;
        
        /** The longitude. */
        protected double longitude;

        /**
         * Gets the value of the latitude property.
         * 
         * @return the latitude
         */
        public double getLatitude() {
            return latitude;
        }

        /**
         * Sets the value of the latitude property.
         * 
         * @param value the value
         */
        public void setLatitude(double value) {
            this.latitude = value;
        }

        /**
         * Gets the value of the longitude property.
         * 
         * @return the longitude
         */
        public double getLongitude() {
            return longitude;
        }

        /**
         * Sets the value of the longitude property.
         * 
         * @param value the value
         */
        public void setLongitude(double value) {
            this.longitude = value;
        }

    }

}
