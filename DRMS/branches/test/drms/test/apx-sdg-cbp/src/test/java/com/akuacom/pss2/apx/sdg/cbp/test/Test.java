/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.apx.sdg.cbp.test.Test.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.apx.sdg.cbp.test;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

/**
 * The Class Test.
 */
public class Test 
{
    
    /**
     * Instantiates a new test.
     */
    public Test()
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
            System.setProperty("javax.net.ssl.keyStore", "C:\\keys\\dras-sdg-apx\\tomcat.keystore");
            System.setProperty("javax.net.ssl.keyStorePassword", "changeit");
//            System.setProperty("javax.net.ssl.trustStore", "../resource/pss2/cacerts.jks");
//            System.setProperty("javax.net.ssl.trustStorePassword", "epriceLBL");

//            URL url = new URL("http://localhost:8080/pss2.sdgecpp/SDGECPP");
//            URL url = new URL("http://cheetah/pss2.sdgecpp/SDGECPP");
//            URL url = new URL("https://cheetah/pss2.sdgecpp/SDGECPP");
           URL url = new URL("https://www.sdg.openadr.com/pss2.sdgecpp/SDGECPP");

           System.out.println("url" + url);
            HostnameVerifier hv = new HostnameVerifier() {
                public boolean verify(String urlHostName, SSLSession session) {
                    System.out.println("Warning: URL Host: "+urlHostName+" vs. "+session.getPeerHost());
                    return true;
                }
            };
             
            HttpsURLConnection.setDefaultHostnameVerifier(hv);

            URLConnection connection = url.openConnection();
//            String plain = "operator:PSS2Akua";
//            String encoded = new sun.misc.BASE64Encoder().encode(plain.getBytes());
//            connection.setRequestProperty("Authorization", "Basic " + encoded);
            connection.setDoOutput(true);
            connection.connect();
            PrintWriter out = new PrintWriter(connection.getOutputStream());
            
        	// send zone 1 request
            String fileName = "sample_request_sdge.xml";
            //String fileName = "sample_request2.xml";
            Scanner fileIn = new Scanner(new FileReader(fileName));
            while (fileIn.hasNextLine())
            {
                out.append(fileIn.nextLine());
                out.append("\n");
            }
            fileIn.close();
            out.close();
            
            Scanner httpIn;
            try
            {  
                httpIn = new Scanner(connection.getInputStream());
            }
            catch (IOException e)
            {  
                if(!(connection instanceof HttpURLConnection))
                {
                    throw e;
                }
                InputStream err = ((HttpURLConnection)connection).
                  getErrorStream();
                if (err == null)
                {
                    throw e;
                }
                httpIn = new Scanner(err);
            }
            while (httpIn.hasNextLine())
            {
                System.out.println(httpIn.nextLine());
            }
            
            httpIn.close();           
            
         }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
