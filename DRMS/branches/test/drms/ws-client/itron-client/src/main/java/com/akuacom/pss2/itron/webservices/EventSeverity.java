/**
 * EventSeverity.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.akuacom.pss2.itron.webservices;

public class EventSeverity implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected EventSeverity(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _CriticalError = "CriticalError";
    public static final java.lang.String _Error = "Error";
    public static final java.lang.String _Warning = "Warning";
    public static final java.lang.String _Information = "Information";
    public static final java.lang.String _Trace = "Trace";
    public static final java.lang.String _Debug = "Debug";
    public static final EventSeverity CriticalError = new EventSeverity(_CriticalError);
    public static final EventSeverity Error = new EventSeverity(_Error);
    public static final EventSeverity Warning = new EventSeverity(_Warning);
    public static final EventSeverity Information = new EventSeverity(_Information);
    public static final EventSeverity Trace = new EventSeverity(_Trace);
    public static final EventSeverity Debug = new EventSeverity(_Debug);
    public java.lang.String getValue() { return _value_;}
    public static EventSeverity fromValue(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        EventSeverity enumeration = (EventSeverity)
            _table_.get(value);
        if (enumeration==null) throw new java.lang.IllegalArgumentException();
        return enumeration;
    }
    public static EventSeverity fromString(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        return fromValue(value);
    }
    public boolean equals(java.lang.Object obj) {return (obj == this);}
    public int hashCode() { return toString().hashCode();}
    public java.lang.String toString() { return _value_;}
    public java.lang.Object readResolve() throws java.io.ObjectStreamException { return fromValue(_value_);}
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new org.apache.axis.encoding.ser.EnumSerializer(
            _javaType, _xmlType);
    }
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new org.apache.axis.encoding.ser.EnumDeserializer(
            _javaType, _xmlType);
    }
    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(EventSeverity.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://itron.com/webservices/", "EventSeverity"));
    }
    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

}
