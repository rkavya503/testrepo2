/**
 * CurtailmentOperationsLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.akuacom.pss2.itron.webservices;



public class CurtailmentOperationsLocator extends org.apache.axis.client.Service implements CurtailmentOperations {

/**
 * Curtailment Operations
 */

    public CurtailmentOperationsLocator() {
    }


    public CurtailmentOperationsLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public CurtailmentOperationsLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for CurtailmentOperationsSoap
    private java.lang.String CurtailmentOperationsSoap_address = null;

    public java.lang.String getCurtailmentOperationsSoapAddress() {
    	//removed call to system manager DRMS-2142    
        return CurtailmentOperationsSoap_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String CurtailmentOperationsSoapWSDDServiceName = "CurtailmentOperationsSoap";

    public java.lang.String getCurtailmentOperationsSoapWSDDServiceName() {

        return CurtailmentOperationsSoapWSDDServiceName;
    }

    public void setCurtailmentOperationsSoapWSDDServiceName(java.lang.String name) {
        CurtailmentOperationsSoapWSDDServiceName = name;
    }

    public CurtailmentOperationsSoap getCurtailmentOperationsSoap() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(getCurtailmentOperationsSoapAddress());
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getCurtailmentOperationsSoap(endpoint);
    }

    public CurtailmentOperationsSoap getCurtailmentOperationsSoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            CurtailmentOperationsSoapStub _stub = new CurtailmentOperationsSoapStub(portAddress, this);
            _stub.setPortName(getCurtailmentOperationsSoapWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setCurtailmentOperationsSoapEndpointAddress(java.lang.String address) {
    	
        CurtailmentOperationsSoap_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (CurtailmentOperationsSoap.class.isAssignableFrom(serviceEndpointInterface)) {
                CurtailmentOperationsSoapStub _stub = new CurtailmentOperationsSoapStub(new java.net.URL(getCurtailmentOperationsSoapAddress()), this);
                _stub.setPortName(getCurtailmentOperationsSoapWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("CurtailmentOperationsSoap".equals(inputPortName)) {
            return getCurtailmentOperationsSoap();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://itron.com/webservices/", "CurtailmentOperations");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://itron.com/webservices/", "CurtailmentOperationsSoap"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("CurtailmentOperationsSoap".equals(portName)) {
            setCurtailmentOperationsSoapEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
