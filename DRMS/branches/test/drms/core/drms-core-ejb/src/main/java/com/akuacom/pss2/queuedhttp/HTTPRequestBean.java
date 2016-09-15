/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.email.HTTPRequestBean.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved.
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.queuedhttp;

import com.akuacom.pss2.email.*;
import org.apache.http.conn.ssl.SSLSocketFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.annotation.Resource;
import javax.annotation.Resources;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.jms.JMSException;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.QueueConnectionFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import org.apache.log4j.Logger;
import org.jboss.ejb3.annotation.Depends;

/**
 * The Class HTTPRequestBean.
 */
@TransactionManagement
@MessageDriven(name = "HTTPPushMDB", activationConfig = {
    @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge"),
    @ActivationConfigProperty(propertyName = "subscriptionDurability", propertyValue = "Durable"),
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue")})
@Resources(@Resource(name = "jms/QueueFactory", type = QueueConnectionFactory.class))
@Depends("jboss.messaging.destination:service=Queue,name=pss2push")
public class HTTPRequestBean implements MessageListener {
    
    /** The Constant log. */
    private static final Logger log = Logger.getLogger(HTTPRequestBean.class);
    
    @EJB
    private MessageDispatcher.L messageDispatcher;
    
    /* (non-Javadoc)
     * @see javax.jms.MessageListener#onMessage(javax.jms.Message)
     */
    /**
     * all Messages should fall though this channel no matter whether the message is 
     * sent at first time or resent manually by the operator
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public void onMessage(javax.jms.Message message) {
        if (message instanceof ObjectMessage) {
            final ObjectMessage obj = (ObjectMessage) message;
            try {
                final Serializable object = obj.getObject();
                if (object instanceof HTTPRequestDef) {
                    final HTTPRequestDef request = (HTTPRequestDef) object;
                    doRequest(request);
                }
            } catch (JMSException e) {
                log.error("error in consuming message", e);
            }
        }
    }

    private boolean relaxSSLCertRequirements = false;
    private void doRequest(HTTPRequestDef req) {
        try {
            HttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, 4000);
            HttpConnectionParams.setSoTimeout(httpParams, 2000);

            DefaultHttpClient conn = new DefaultHttpClient(httpParams);               
            
            // For debugging only.
            // Sometimes necessary on Windows
            // Setting this variable to true will cause the system to
            // permit connections with certificates that would otherwise
            // be rejected.
            if (relaxSSLCertRequirements) {  conn = getRelaxedSSLClient(conn);  }

            conn.getCredentialsProvider().setCredentials(
                    AuthScope.ANY,
                    new UsernamePasswordCredentials(req.getName(), req.getPassword()));

            HttpUriRequest request = null;
            if (req.getMethod() == HTTPRequestDef.METHOD_POST) {
                HttpPost thePost = new HttpPost(req.getDestination());
                thePost.setEntity(new StringEntity(req.getPayload(), "UTF-8"));
                request = thePost;
            }
            else if (req.getMethod() == HTTPRequestDef.METHOD_GET) {
                request = new HttpGet(req.getDestination());
            }
            if (request != null) {
                HttpResponse resp = conn.execute(request);
                HttpEntity rEnt = resp.getEntity();
                StatusLine status = resp.getStatusLine();
                InputStream answerStream = rEnt.getContent();
                long answerLength = rEnt.getContentLength();
                if (answerLength > 0) {
                    byte[] ansBuf = new byte[1000];
                    answerStream.read(ansBuf);  // Read the answer, limiting overflows
                    String answer = new String(ansBuf).trim();
                    if (!answer.equalsIgnoreCase("SUCCESS")) {
                        // Got a goofy answer
                        log.error("Unexpected answer from http push: "+
                                status.toString()+" "+answer);
                    }
                }
            }
        } catch (IOException ex) {
            // The post failed
            log.error("HTTP push failed for request:"+ 
                    req.getDescription()+" "+
                    ex.getMessage());
        }
    }

    private DefaultHttpClient getRelaxedSSLClient(DefaultHttpClient base) {
        DefaultHttpClient retval = null;
        try {
            SSLContext ctx = SSLContext.getInstance("SSL");
            X509TrustManager tm = new X509TrustManager() {
                // A very trusting trust manager
                @Override
                public void checkClientTrusted(X509Certificate[] xcs, String string) throws CertificateException {  }
                @Override
                public void checkServerTrusted(X509Certificate[] xcs, String string) throws CertificateException {  }
                @Override
                public X509Certificate[] getAcceptedIssuers() { return null; }
            };
            ctx.init(null, new TrustManager[]{tm}, null);
            SSLSocketFactory ssf = new SSLSocketFactory(ctx);
            ssf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            ClientConnectionManager ccm = base.getConnectionManager();
            SchemeRegistry sr = ccm.getSchemeRegistry();
            sr.register(new Scheme("https", ssf, 443));
            retval = new DefaultHttpClient(ccm, base.getParams());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return retval;
    }

}
