/**
 * Message.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.akuacom.pss2.itron.webservices;

public class Message  implements java.io.Serializable {
    private EventSeverity severity;

    private java.lang.String text;

    private java.lang.String ID;

    private java.util.Calendar messageTime;

    private MessageDataItem[] messageData;

    public Message() {
    }

    public Message(
           EventSeverity severity,
           java.lang.String text,
           java.lang.String ID,
           java.util.Calendar messageTime,
           MessageDataItem[] messageData) {
           this.severity = severity;
           this.text = text;
           this.ID = ID;
           this.messageTime = messageTime;
           this.messageData = messageData;
    }


    /**
     * Gets the severity value for this Message.
     * 
     * @return severity
     */
    public EventSeverity getSeverity() {
        return severity;
    }


    /**
     * Sets the severity value for this Message.
     * 
     * @param severity
     */
    public void setSeverity(EventSeverity severity) {
        this.severity = severity;
    }


    /**
     * Gets the text value for this Message.
     * 
     * @return text
     */
    public java.lang.String getText() {
        return text;
    }


    /**
     * Sets the text value for this Message.
     * 
     * @param text
     */
    public void setText(java.lang.String text) {
        this.text = text;
    }


    /**
     * Gets the ID value for this Message.
     * 
     * @return ID
     */
    public java.lang.String getID() {
        return ID;
    }


    /**
     * Sets the ID value for this Message.
     * 
     * @param ID
     */
    public void setID(java.lang.String ID) {
        this.ID = ID;
    }


    /**
     * Gets the messageTime value for this Message.
     * 
     * @return messageTime
     */
    public java.util.Calendar getMessageTime() {
        return messageTime;
    }


    /**
     * Sets the messageTime value for this Message.
     * 
     * @param messageTime
     */
    public void setMessageTime(java.util.Calendar messageTime) {
        this.messageTime = messageTime;
    }


    /**
     * Gets the messageData value for this Message.
     * 
     * @return messageData
     */
    public MessageDataItem[] getMessageData() {
        return messageData;
    }


    /**
     * Sets the messageData value for this Message.
     * 
     * @param messageData
     */
    public void setMessageData(MessageDataItem[] messageData) {
        this.messageData = messageData;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Message)) return false;
        Message other = (Message) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.severity==null && other.getSeverity()==null) || 
             (this.severity!=null &&
              this.severity.equals(other.getSeverity()))) &&
            ((this.text==null && other.getText()==null) || 
             (this.text!=null &&
              this.text.equals(other.getText()))) &&
            ((this.ID==null && other.getID()==null) || 
             (this.ID!=null &&
              this.ID.equals(other.getID()))) &&
            ((this.messageTime==null && other.getMessageTime()==null) || 
             (this.messageTime!=null &&
              this.messageTime.equals(other.getMessageTime()))) &&
            ((this.messageData==null && other.getMessageData()==null) || 
             (this.messageData!=null &&
              java.util.Arrays.equals(this.messageData, other.getMessageData())));
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
        if (getSeverity() != null) {
            _hashCode += getSeverity().hashCode();
        }
        if (getText() != null) {
            _hashCode += getText().hashCode();
        }
        if (getID() != null) {
            _hashCode += getID().hashCode();
        }
        if (getMessageTime() != null) {
            _hashCode += getMessageTime().hashCode();
        }
        if (getMessageData() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getMessageData());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getMessageData(), i);
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
        new org.apache.axis.description.TypeDesc(Message.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://itron.com/webservices/", "Message"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("severity");
        elemField.setXmlName(new javax.xml.namespace.QName("http://itron.com/webservices/", "Severity"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://itron.com/webservices/", "EventSeverity"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("text");
        elemField.setXmlName(new javax.xml.namespace.QName("http://itron.com/webservices/", "Text"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://itron.com/webservices/", "ID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("messageTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://itron.com/webservices/", "MessageTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("messageData");
        elemField.setXmlName(new javax.xml.namespace.QName("http://itron.com/webservices/", "MessageData"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://itron.com/webservices/", "MessageDataItem"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://itron.com/webservices/", "MessageDataItem"));
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
