/*
 * www.akuacom.com - Automating Demand Response
 * 
 * services.web.bacnet.ObjectFactory.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */

package services.web.bacnet;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the services.web.bacnet package.
 * <p>An ObjectFactory allows you to programatically
 * construct new instances of the Java representation
 * for XML content. The Java representation of XML
 * content can consist of schema derived interfaces
 * and classes representing the binding of schema
 * type definitions, element declarations and model
 * groups.  Factory methods for each of these are
 * provided in this class.
 */
@XmlRegistry
public class ObjectFactory {

    /** The Constant _Request_QNAME. */
    private final static QName _Request_QNAME = new QName("urn:bacnet-web-services", "request");
    
    /** The Constant _Start_QNAME. */
    private final static QName _Start_QNAME = new QName("urn:bacnet-web-services", "start");
    
    /** The Constant _Count_QNAME. */
    private final static QName _Count_QNAME = new QName("urn:bacnet-web-services", "count");
    
    /** The Constant _Interval_QNAME. */
    private final static QName _Interval_QNAME = new QName("urn:bacnet-web-services", "interval");
    
    /** The Constant _Index_QNAME. */
    private final static QName _Index_QNAME = new QName("urn:bacnet-web-services", "index");
    
    /** The Constant _Result_QNAME. */
    private final static QName _Result_QNAME = new QName("urn:bacnet-web-services", "result");
    
    /** The Constant _BasePath_QNAME. */
    private final static QName _BasePath_QNAME = new QName("urn:bacnet-web-services", "basePath");
    
    /** The Constant _Response_QNAME. */
    private final static QName _Response_QNAME = new QName("urn:bacnet-web-services", "response");
    
    /** The Constant _Options_QNAME. */
    private final static QName _Options_QNAME = new QName("urn:bacnet-web-services", "options");
    
    /** The Constant _Path_QNAME. */
    private final static QName _Path_QNAME = new QName("urn:bacnet-web-services", "path");
    
    /** The Constant _Value_QNAME. */
    private final static QName _Value_QNAME = new QName("urn:bacnet-web-services", "value");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: services.web.bacnet
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ArrayOfstring }.
     * 
     * @return the array ofstring
     */
    public ArrayOfstring createArrayOfstring() {
        return new ArrayOfstring();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link byte[]}{@code >}}.
     * 
     * @param value the value
     * 
     * @return the JAXB element<byte[]>
     */
    @XmlElementDecl(namespace = "urn:bacnet-web-services", name = "request")
    @XmlJavaTypeAdapter(HexBinaryAdapter.class)
    public JAXBElement<byte[]> createRequest(byte[] value) {
        return new JAXBElement<byte[]>(_Request_QNAME, byte[].class, null, ((byte[]) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}.
     * 
     * @param value the value
     * 
     * @return the JAXB element< string>
     */
    @XmlElementDecl(namespace = "urn:bacnet-web-services", name = "start")
    public JAXBElement<String> createStart(String value) {
        return new JAXBElement<String>(_Start_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Long }{@code >}}.
     * 
     * @param value the value
     * 
     * @return the JAXB element< long>
     */
    @XmlElementDecl(namespace = "urn:bacnet-web-services", name = "count")
    public JAXBElement<Long> createCount(Long value) {
        return new JAXBElement<Long>(_Count_QNAME, Long.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}.
     * 
     * @param value the value
     * 
     * @return the JAXB element< string>
     */
    @XmlElementDecl(namespace = "urn:bacnet-web-services", name = "interval")
    public JAXBElement<String> createInterval(String value) {
        return new JAXBElement<String>(_Interval_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Long }{@code >}}.
     * 
     * @param value the value
     * 
     * @return the JAXB element< long>
     */
    @XmlElementDecl(namespace = "urn:bacnet-web-services", name = "index")
    public JAXBElement<Long> createIndex(Long value) {
        return new JAXBElement<Long>(_Index_QNAME, Long.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}.
     * 
     * @param value the value
     * 
     * @return the JAXB element< string>
     */
    @XmlElementDecl(namespace = "urn:bacnet-web-services", name = "result")
    public JAXBElement<String> createResult(String value) {
        return new JAXBElement<String>(_Result_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}.
     * 
     * @param value the value
     * 
     * @return the JAXB element< string>
     */
    @XmlElementDecl(namespace = "urn:bacnet-web-services", name = "basePath")
    public JAXBElement<String> createBasePath(String value) {
        return new JAXBElement<String>(_BasePath_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link byte[]}{@code >}}.
     * 
     * @param value the value
     * 
     * @return the JAXB element<byte[]>
     */
    @XmlElementDecl(namespace = "urn:bacnet-web-services", name = "response")
    @XmlJavaTypeAdapter(HexBinaryAdapter.class)
    public JAXBElement<byte[]> createResponse(byte[] value) {
        return new JAXBElement<byte[]>(_Response_QNAME, byte[].class, null, ((byte[]) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}.
     * 
     * @param value the value
     * 
     * @return the JAXB element< string>
     */
    @XmlElementDecl(namespace = "urn:bacnet-web-services", name = "options")
    public JAXBElement<String> createOptions(String value) {
        return new JAXBElement<String>(_Options_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}.
     * 
     * @param value the value
     * 
     * @return the JAXB element< string>
     */
    @XmlElementDecl(namespace = "urn:bacnet-web-services", name = "path")
    public JAXBElement<String> createPath(String value) {
        return new JAXBElement<String>(_Path_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}.
     * 
     * @param value the value
     * 
     * @return the JAXB element< string>
     */
    @XmlElementDecl(namespace = "urn:bacnet-web-services", name = "value")
    public JAXBElement<String> createValue(String value) {
        return new JAXBElement<String>(_Value_QNAME, String.class, null, value);
    }

}
