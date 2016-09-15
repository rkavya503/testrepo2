/**
 * EventNoticeResponseBlock.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.akuacom.pss2.itron.webservices;

public class EventNoticeResponseBlock  implements java.io.Serializable {
    private java.lang.Integer blockNumber;

    private java.lang.Integer hour;

    private java.lang.Boolean blockAccepted;

    private java.lang.Double committedReduction;

    private java.lang.Float bidPrice;

    public EventNoticeResponseBlock() {
    }

    public EventNoticeResponseBlock(
           java.lang.Integer blockNumber,
           java.lang.Integer hour,
           java.lang.Boolean blockAccepted,
           java.lang.Double committedReduction,
           java.lang.Float bidPrice) {
           this.blockNumber = blockNumber;
           this.hour = hour;
           this.blockAccepted = blockAccepted;
           this.committedReduction = committedReduction;
           this.bidPrice = bidPrice;
    }


    /**
     * Gets the blockNumber value for this EventNoticeResponseBlock.
     * 
     * @return blockNumber
     */
    public java.lang.Integer getBlockNumber() {
        return blockNumber;
    }


    /**
     * Sets the blockNumber value for this EventNoticeResponseBlock.
     * 
     * @param blockNumber
     */
    public void setBlockNumber(java.lang.Integer blockNumber) {
        this.blockNumber = blockNumber;
    }


    /**
     * Gets the hour value for this EventNoticeResponseBlock.
     * 
     * @return hour
     */
    public java.lang.Integer getHour() {
        return hour;
    }


    /**
     * Sets the hour value for this EventNoticeResponseBlock.
     * 
     * @param hour
     */
    public void setHour(java.lang.Integer hour) {
        this.hour = hour;
    }


    /**
     * Gets the blockAccepted value for this EventNoticeResponseBlock.
     * 
     * @return blockAccepted
     */
    public java.lang.Boolean getBlockAccepted() {
        return blockAccepted;
    }


    /**
     * Sets the blockAccepted value for this EventNoticeResponseBlock.
     * 
     * @param blockAccepted
     */
    public void setBlockAccepted(java.lang.Boolean blockAccepted) {
        this.blockAccepted = blockAccepted;
    }


    /**
     * Gets the committedReduction value for this EventNoticeResponseBlock.
     * 
     * @return committedReduction
     */
    public java.lang.Double getCommittedReduction() {
        return committedReduction;
    }


    /**
     * Sets the committedReduction value for this EventNoticeResponseBlock.
     * 
     * @param committedReduction
     */
    public void setCommittedReduction(java.lang.Double committedReduction) {
        this.committedReduction = committedReduction;
    }


    /**
     * Gets the bidPrice value for this EventNoticeResponseBlock.
     * 
     * @return bidPrice
     */
    public java.lang.Float getBidPrice() {
        return bidPrice;
    }


    /**
     * Sets the bidPrice value for this EventNoticeResponseBlock.
     * 
     * @param bidPrice
     */
    public void setBidPrice(java.lang.Float bidPrice) {
        this.bidPrice = bidPrice;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof EventNoticeResponseBlock)) return false;
        EventNoticeResponseBlock other = (EventNoticeResponseBlock) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.blockNumber==null && other.getBlockNumber()==null) || 
             (this.blockNumber!=null &&
              this.blockNumber.equals(other.getBlockNumber()))) &&
            ((this.hour==null && other.getHour()==null) || 
             (this.hour!=null &&
              this.hour.equals(other.getHour()))) &&
            ((this.blockAccepted==null && other.getBlockAccepted()==null) || 
             (this.blockAccepted!=null &&
              this.blockAccepted.equals(other.getBlockAccepted()))) &&
            ((this.committedReduction==null && other.getCommittedReduction()==null) || 
             (this.committedReduction!=null &&
              this.committedReduction.equals(other.getCommittedReduction()))) &&
            ((this.bidPrice==null && other.getBidPrice()==null) || 
             (this.bidPrice!=null &&
              this.bidPrice.equals(other.getBidPrice())));
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
        if (getBlockNumber() != null) {
            _hashCode += getBlockNumber().hashCode();
        }
        if (getHour() != null) {
            _hashCode += getHour().hashCode();
        }
        if (getBlockAccepted() != null) {
            _hashCode += getBlockAccepted().hashCode();
        }
        if (getCommittedReduction() != null) {
            _hashCode += getCommittedReduction().hashCode();
        }
        if (getBidPrice() != null) {
            _hashCode += getBidPrice().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(EventNoticeResponseBlock.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://itron.com/webservices/", "EventNoticeResponseBlock"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("blockNumber");
        elemField.setXmlName(new javax.xml.namespace.QName("http://itron.com/webservices/", "BlockNumber"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("hour");
        elemField.setXmlName(new javax.xml.namespace.QName("http://itron.com/webservices/", "Hour"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("blockAccepted");
        elemField.setXmlName(new javax.xml.namespace.QName("http://itron.com/webservices/", "BlockAccepted"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("committedReduction");
        elemField.setXmlName(new javax.xml.namespace.QName("http://itron.com/webservices/", "CommittedReduction"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("bidPrice");
        elemField.setXmlName(new javax.xml.namespace.QName("http://itron.com/webservices/", "BidPrice"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "float"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
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
