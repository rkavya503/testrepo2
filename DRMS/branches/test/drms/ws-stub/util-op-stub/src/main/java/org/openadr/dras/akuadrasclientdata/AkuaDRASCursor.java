/*
 * www.akuacom.com - Automating Demand Response
 * 
 * org.openadr.dras.akuadrasclientdata.AkuaDRASCursor.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */

package org.openadr.dras.akuadrasclientdata;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AkuaDRASCursor complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AkuaDRASCursor">
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="startIndex" type="{http://www.w3.org/2001/XMLSchema}int"/>
 * &lt;element name="pageSize" type="{http://www.w3.org/2001/XMLSchema}int"/>
 * &lt;element name="total" type="{http://www.w3.org/2001/XMLSchema}int"/>
 * &lt;element name="selectedPage" type="{http://www.w3.org/2001/XMLSchema}int"/>
 * &lt;element name="maxPageSize" type="{http://www.w3.org/2001/XMLSchema}int"/>
 * &lt;/sequence>
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AkuaDRASCursor", propOrder = {
    "startIndex",
    "pageSize",
    "total",
    "selectedPage",
    "maxPageSize"
})
public class AkuaDRASCursor
    implements Serializable
{

    /** The start index. */
    protected int startIndex;
    
    /** The page size. */
    protected int pageSize;
    
    /** The total. */
    protected int total;
    
    /** The selected page. */
    protected int selectedPage;
    
    /** The max page size. */
    protected int maxPageSize;

    /**
     * Gets the value of the startIndex property.
     * 
     * @return the start index
     */
    public int getStartIndex() {
        return startIndex;
    }

    /**
     * Sets the value of the startIndex property.
     * 
     * @param value the value
     */
    public void setStartIndex(int value) {
        this.startIndex = value;
    }

    /**
     * Gets the value of the pageSize property.
     * 
     * @return the page size
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * Sets the value of the pageSize property.
     * 
     * @param value the value
     */
    public void setPageSize(int value) {
        this.pageSize = value;
    }

    /**
     * Gets the value of the total property.
     * 
     * @return the total
     */
    public int getTotal() {
        return total;
    }

    /**
     * Sets the value of the total property.
     * 
     * @param value the value
     */
    public void setTotal(int value) {
        this.total = value;
    }

    /**
     * Gets the value of the selectedPage property.
     * 
     * @return the selected page
     */
    public int getSelectedPage() {
        return selectedPage;
    }

    /**
     * Sets the value of the selectedPage property.
     * 
     * @param value the value
     */
    public void setSelectedPage(int value) {
        this.selectedPage = value;
    }

    /**
     * Gets the value of the maxPageSize property.
     * 
     * @return the max page size
     */
    public int getMaxPageSize() {
        return maxPageSize;
    }

    /**
     * Sets the value of the maxPageSize property.
     * 
     * @param value the value
     */
    public void setMaxPageSize(int value) {
        this.maxPageSize = value;
    }

}
