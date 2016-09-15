/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.adsws.client.ADSWSClient.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.adsws.client;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.security.KeyStore;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.rpc.Stub;
import javax.xml.transform.stream.StreamSource;
import javax.xml.ws.BindingProvider;

import sun.misc.BASE64Decoder;

import com.akuacom.pss2.adsws.ads.APIDispatchResponse;
import com.akuacom.pss2.adsws.ads.DispatchBatch;
import com.akuacom.pss2.adsws.stubs.APIWebService_PortType;
import com.akuacom.pss2.adsws.stubs.APIWebService_Service_Impl;


/**
 * The Class ADSWSClient.
 */
public class ADSWSClient 
{
    
    /**
     * Instantiates a new aDSWS client.
     */
    public ADSWSClient()
    {
    }
    
    /**
     * The main method.
     * 
     * @param args the arguments
     */
    public static void main(String[] args) 
    {
    	try {
			System.setProperty("javax.net.ssl.keyStore",
				"..\\resource\\pss2\\PCG2_DRAS.jks");
			System.setProperty("javax.net.ssl.keyStorePassword", "tuS3u=pA");
			System.setProperty("javax.net.ssl.trustStore",
				"..\\resource\\pss2\\cacerts.jks");
			System.setProperty("javax.net.ssl.trustStorePassword", "epriceLBL");
			
	    	APIWebService_PortType adsws = ADSWSClient.createADSWS();
	
	    	JAXBContext jc = JAXBContext.newInstance("com.akuacom.pss2.adsws.ads");
	        Unmarshaller unmarshaller = jc.createUnmarshaller();
	        BASE64Decoder decoder = new BASE64Decoder();
	        String lastDispatchUID = "-1";
	    	while(true)
	    	{
	            String dispatchReponseString = 
	            	adsws.getDispatchBatchesSinceUID(lastDispatchUID);
	            APIDispatchResponse dispatchReponse = (APIDispatchResponse)
	            	unmarshaller.unmarshal(new StreamSource(
	            	new StringReader(dispatchReponseString)));
	            for(DispatchBatch dispatchBatch: 
	            	dispatchReponse.getDispatchBatchList().getDispatchBatch())
	            {
	            	String rawBatchDataString = 
	            		adsws.getDispatchBatch(dispatchBatch.getBatchUID());
	            	byte[] zipDatchDataBytes = decoder.decodeBuffer(rawBatchDataString);
	            	StringBuilder batchData = new StringBuilder();
	            	BufferedReader in = new BufferedReader(
	            		new InputStreamReader(
	            		new GZIPInputStream(
	            		new ByteArrayInputStream(zipDatchDataBytes))));
	            	String s;
	            	while((s = in.readLine()) != null)
	            	{
	            		batchData.append(s);
	            	}
	            	DispatchBatch dispatchBatch2 = (DispatchBatch)
	            		unmarshaller.unmarshal(new StreamSource(
	            		new StringReader(batchData.toString())));
	            	System.out.println("batchData = " + batchData.toString());
	            	lastDispatchUID = dispatchBatch2.getBatchUID();
	            }
	            break;
	    	}
    	}
        catch (Exception ex) 
        {
            ex.printStackTrace();
        }
    }
    
    /**
     * Shutdown.
     */
    public  void shutdown()
    {
        System.out.println("exiting...");
        System.exit(0);
    }

    // initialize everything and fire off the timer threads. note that when 
    // this method exits, the main thread dies.
    /**
     * Creates the adsws.
     * 
     * @return the aPI web service_ port type
     */
    public static APIWebService_PortType createADSWS()
    {

        // create stub
//        try 
//        {
			// set up ssl
//	 		KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
//			keyStore.load(new FileInputStream(
//				"C:\\keys\\PCG2_DRAS\\tomcat.keystore"), 
//				"tuS3u=pA".toCharArray());
//			KeyManagerFactory kmf = 
//				KeyManagerFactory.getInstance(
//				KeyManagerFactory.getDefaultAlgorithm());
//			kmf.init(keyStore, "tuS3u=pA".toCharArray());
//			KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
//			trustStore.load(new FileInputStream(
//				"C:\\dev\\pss2-core-all\\pss2.core\\cacerts"), 
//				"changeit".toCharArray());
//			TrustManagerFactory tmf = 
//				TrustManagerFactory.getInstance(
//				TrustManagerFactory.getDefaultAlgorithm());
//			tmf.init(trustStore);
//			SSLContext ctx = SSLContext.getInstance("SSLv3");
//			ctx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
//			SSLSocketFactory factory = ctx.getSocketFactory();
	

			APIWebService_PortType adsws = 
				new APIWebService_Service_Impl().getAPIWebServicePort();
            Stub stub = (Stub)(adsws);
            stub._setProperty(javax.xml.rpc.Stub.ENDPOINT_ADDRESS_PROPERTY,
                "https://adssta.caiso.com:447/ADS/APIWebService");

            return adsws;
//        }
//        catch (Exception ex) 
//        {
//            ex.printStackTrace();
//        }
    }
     
}