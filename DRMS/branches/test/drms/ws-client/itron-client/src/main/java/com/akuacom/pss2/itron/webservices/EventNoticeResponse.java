/**
 * EventNoticeResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.akuacom.pss2.itron.webservices;

public class EventNoticeResponse  implements java.io.Serializable {
    private java.lang.Integer curtailmentEventID;

    private java.lang.String accountID;

    private java.lang.String servicePointID;

    private ResponseAction responseAction;

    private java.lang.Boolean useDefaultReductionForAllBlocks;

    private EventNoticeResponseBlock[] responseBlocks;
    
    private java.lang.Boolean applyBaselineLoadAdjustment;

    public EventNoticeResponse() {
    }

    public EventNoticeResponse(
           java.lang.Integer curtailmentEventID,
           java.lang.String accountID,
           java.lang.String servicePointID,
           ResponseAction responseAction,
           java.lang.Boolean useDefaultReductionForAllBlocks,
           java.lang.Boolean applyBaselineLoadAdjustment,
           EventNoticeResponseBlock[] responseBlocks) {
           this.curtailmentEventID = curtailmentEventID;
           this.accountID = accountID;
           this.servicePointID = servicePointID;
           this.responseAction = responseAction;
           this.useDefaultReductionForAllBlocks = useDefaultReductionForAllBlocks;
           this.applyBaselineLoadAdjustment = useDefaultReductionForAllBlocks;
           this.responseBlocks = responseBlocks;
    }


    /**
     * Gets the curtailmentEventID value for this EventNoticeResponse.
     * 
     * @return curtailmentEventID
     */
    public java.lang.Integer getCurtailmentEventID() {
        return curtailmentEventID;
    }


    /**
     * Sets the curtailmentEventID value for this EventNoticeResponse.
     * 
     * @param curtailmentEventID
     */
    public void setCurtailmentEventID(java.lang.Integer curtailmentEventID) {
        this.curtailmentEventID = curtailmentEventID;
    }


    /**
     * Gets the accountID value for this EventNoticeResponse.
     * 
     * @return accountID
     */
    public java.lang.String getAccountID() {
        return accountID;
    }


    /**
     * Sets the accountID value for this EventNoticeResponse.
     * 
     * @param accountID
     */
    public void setAccountID(java.lang.String accountID) {
        this.accountID = accountID;
    }


    /**
     * Gets the servicePointID value for this EventNoticeResponse.
     * 
     * @return servicePointID
     */
    public java.lang.String getServicePointID() {
        return servicePointID;
    }


    /**
     * Sets the servicePointID value for this EventNoticeResponse.
     * 
     * @param servicePointID
     */
    public void setServicePointID(java.lang.String servicePointID) {
        this.servicePointID = servicePointID;
    }


    /**
     * Gets the responseAction value for this EventNoticeResponse.
     * 
     * @return responseAction
     */
    public ResponseAction getResponseAction() {
        return responseAction;
    }


    /**
     * Sets the responseAction value for this EventNoticeResponse.
     * 
     * @param responseAction
     */
    public void setResponseAction(ResponseAction responseAction) {
        this.responseAction = responseAction;
    }


    /**
     * Gets the useDefaultReductionForAllBlocks value for this EventNoticeResponse.
     * 
     * @return useDefaultReductionForAllBlocks
     */
    public java.lang.Boolean getUseDefaultReductionForAllBlocks() {
        return useDefaultReductionForAllBlocks;
    }


    /**
     * Sets the useDefaultReductionForAllBlocks value for this EventNoticeResponse.
     * 
     * @param useDefaultReductionForAllBlocks
     */
    public void setUseDefaultReductionForAllBlocks(java.lang.Boolean useDefaultReductionForAllBlocks) {
        this.useDefaultReductionForAllBlocks = useDefaultReductionForAllBlocks;
    }


    /**
     * Gets the responseBlocks value for this EventNoticeResponse.
     * 
     * @return responseBlocks
     */
    public EventNoticeResponseBlock[] getResponseBlocks() {
        return responseBlocks;
    }


    public java.lang.Boolean getApplyBaselineLoadAdjustment() {
		return applyBaselineLoadAdjustment;
	}

	public void setApplyBaselineLoadAdjustment(
			java.lang.Boolean applyBaselineLoadAdjustment) {
		this.applyBaselineLoadAdjustment = applyBaselineLoadAdjustment;
	}
    /**
     * Sets the responseBlocks value for this EventNoticeResponse.
     * 
     * @param responseBlocks
     */
    public void setResponseBlocks(EventNoticeResponseBlock[] responseBlocks) {
        this.responseBlocks = responseBlocks;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof EventNoticeResponse)) return false;
        EventNoticeResponse other = (EventNoticeResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.curtailmentEventID==null && other.getCurtailmentEventID()==null) || 
             (this.curtailmentEventID!=null &&
              this.curtailmentEventID.equals(other.getCurtailmentEventID()))) &&
            ((this.accountID==null && other.getAccountID()==null) || 
             (this.accountID!=null &&
              this.accountID.equals(other.getAccountID()))) &&
            ((this.servicePointID==null && other.getServicePointID()==null) || 
             (this.servicePointID!=null &&
              this.servicePointID.equals(other.getServicePointID()))) &&
            ((this.responseAction==null && other.getResponseAction()==null) || 
             (this.responseAction!=null &&
              this.responseAction.equals(other.getResponseAction()))) &&
            ((this.useDefaultReductionForAllBlocks==null && other.getUseDefaultReductionForAllBlocks()==null) || 
             (this.useDefaultReductionForAllBlocks!=null &&
              this.useDefaultReductionForAllBlocks.equals(other.getUseDefaultReductionForAllBlocks()))) &&
              ((this.applyBaselineLoadAdjustment==null && other.getApplyBaselineLoadAdjustment()==null) || 
                      (this.applyBaselineLoadAdjustment!=null &&
                       this.applyBaselineLoadAdjustment.equals(other.getApplyBaselineLoadAdjustment()))) &&  
            ((this.responseBlocks==null && other.getResponseBlocks()==null) || 
             (this.responseBlocks!=null &&
              java.util.Arrays.equals(this.responseBlocks, other.getResponseBlocks())));
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
        if (getCurtailmentEventID() != null) {
            _hashCode += getCurtailmentEventID().hashCode();
        }
        if (getAccountID() != null) {
            _hashCode += getAccountID().hashCode();
        }
        if (getServicePointID() != null) {
            _hashCode += getServicePointID().hashCode();
        }
        if (getResponseAction() != null) {
            _hashCode += getResponseAction().hashCode();
        }
        if (getUseDefaultReductionForAllBlocks() != null) {
            _hashCode += getUseDefaultReductionForAllBlocks().hashCode();
        }
        if (getResponseBlocks() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getResponseBlocks());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getResponseBlocks(), i);
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
        new org.apache.axis.description.TypeDesc(EventNoticeResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://itron.com/webservices/", "EventNoticeResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("curtailmentEventID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://itron.com/webservices/", "CurtailmentEventID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("accountID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://itron.com/webservices/", "AccountID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("servicePointID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://itron.com/webservices/", "ServicePointID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("responseAction");
        elemField.setXmlName(new javax.xml.namespace.QName("http://itron.com/webservices/", "ResponseAction"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://itron.com/webservices/", "ResponseAction"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("useDefaultReductionForAllBlocks");
        elemField.setXmlName(new javax.xml.namespace.QName("http://itron.com/webservices/", "UseDefaultReductionForAllBlocks"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("applyBaselineLoadAdjustment");
        elemField.setXmlName(new javax.xml.namespace.QName("http://itron.com/webservices/", "ApplyBaselineLoadAdjustment"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("responseBlocks");
        elemField.setXmlName(new javax.xml.namespace.QName("http://itron.com/webservices/", "ResponseBlocks"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://itron.com/webservices/", "EventNoticeResponseBlock"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://itron.com/webservices/", "ResponseBlock"));
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
