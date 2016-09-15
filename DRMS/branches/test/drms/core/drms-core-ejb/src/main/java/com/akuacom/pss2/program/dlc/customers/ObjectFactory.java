
package com.akuacom.pss2.program.dlc.customers;

import java.math.BigInteger;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.akuacom.pss2.dlc.stub.customers package.
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _ClientPassword_QNAME = new QName("", "clientPassword");
    private final static QName _ParticipantName_QNAME = new QName("", "participantName");
    private final static QName _ServiceId_QNAME = new QName("", "serviceId");
    private final static QName _ClientName_QNAME = new QName("", "clientName");
    private final static QName _ParticipantPassword_QNAME = new QName("", "participantPassword");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.akuacom.pss2.dlc.stub.customers
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Customers }
     * 
     */
    public Customers createCustomers() {
        return new Customers();
    }

    /**
     * Create an instance of {@link Customer }
     * 
     */
    public Customer createCustomer() {
        return new Customer();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "clientPassword")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createClientPassword(String value) {
        return new JAXBElement<String>(_ClientPassword_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "participantName")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createParticipantName(String value) {
        return new JAXBElement<String>(_ParticipantName_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigInteger }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "serviceId")
    public JAXBElement<BigInteger> createServiceId(BigInteger value) {
        return new JAXBElement<BigInteger>(_ServiceId_QNAME, BigInteger.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "clientName")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createClientName(String value) {
        return new JAXBElement<String>(_ClientName_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "participantPassword")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createParticipantPassword(String value) {
        return new JAXBElement<String>(_ParticipantPassword_QNAME, String.class, null, value);
    }

}
