/**
 * EventNoticeResponseList.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.akuacom.pss2.itron.webservices;

public class EventNoticeResponseList  implements java.io.Serializable {
    private java.lang.String transactionID;

    private EventNoticeResponse[] eventNoticeResponses;

    public EventNoticeResponseList() {
    }

    public EventNoticeResponseList(
           java.lang.String transactionID,
           EventNoticeResponse[] eventNoticeResponses) {
           this.transactionID = transactionID;
           this.eventNoticeResponses = eventNoticeResponses;
    }


    /**
     * Gets the transactionID value for this EventNoticeResponseList.
     * 
     * @return transactionID
     */
    public java.lang.String getTransactionID() {
        return transactionID;
    }


    /**
     * Sets the transactionID value for this EventNoticeResponseList.
     * 
     * @param transactionID
     */
    public void setTransactionID(java.lang.String transactionID) {
        this.transactionID = transactionID;
    }


    /**
     * Gets the eventNoticeResponses value for this EventNoticeResponseList.
     * 
     * @return eventNoticeResponses
     */
    public EventNoticeResponse[] getEventNoticeResponses() {
        return eventNoticeResponses;
    }


    /**
     * Sets the eventNoticeResponses value for this EventNoticeResponseList.
     * 
     * @param eventNoticeResponses
     */
    public void setEventNoticeResponses(EventNoticeResponse[] eventNoticeResponses) {
        this.eventNoticeResponses = eventNoticeResponses;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof EventNoticeResponseList)) return false;
        EventNoticeResponseList other = (EventNoticeResponseList) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.transactionID==null && other.getTransactionID()==null) || 
             (this.transactionID!=null &&
              this.transactionID.equals(other.getTransactionID()))) &&
            ((this.eventNoticeResponses==null && other.getEventNoticeResponses()==null) || 
             (this.eventNoticeResponses!=null &&
              java.util.Arrays.equals(this.eventNoticeResponses, other.getEventNoticeResponses())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getTransactionID() != null) {
            _hashCode += getTransactionID().hashCode();
        }
        if (getEventNoticeResponses() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getEventNoticeResponses());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getEventNoticeResponses(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(EventNoticeResponseList.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://itron.com/webservices/", "EventNoticeResponseList"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("transactionID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://itron.com/webservices/", "TransactionID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("eventNoticeResponses");
        elemField.setXmlName(new javax.xml.namespace.QName("http://itron.com/webservices/", "EventNoticeResponses"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://itron.com/webservices/", "EventNoticeResponse"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://itron.com/webservices/", "EventNoticeResponse"));
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
