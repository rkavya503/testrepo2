package com.akuacom.pss2.web.options;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

public class DbpSimulatorAction extends Action {
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        DbpSimulatorForm f = (DbpSimulatorForm) form;
        FormFile xmlFile = f.getXmlFile();
        if (xmlFile == null) {
            return mapping.findForward("success");
        }

        try {
            // hand shake
            URLConnection connection = getAutoDbpServletConnection("http://localhost:8080/pss2.program.autodbp/AUTODBP");
            connection.connect();
            PrintWriter out = new PrintWriter(connection.getOutputStream());
            Scanner fileIn = new Scanner(new InputStreamReader(xmlFile.getInputStream()));
            while (fileIn.hasNextLine()) {
                out.append(fileIn.nextLine());
                out.append("\n");
            }
            fileIn.close();
            out.close();
            Scanner httpIn;
            try {
                httpIn = new Scanner(connection.getInputStream());
            } catch (IOException e) {
                if (!(connection instanceof HttpURLConnection)) {
                    throw e;
                }
                InputStream err = ((HttpURLConnection) connection).
                        getErrorStream();
                if (err == null) {
                    throw e;
                }
                httpIn = new Scanner(err);
            }//catch io
            StringBuffer sbf = new StringBuffer();
            while (httpIn.hasNextLine()) {
                sbf.append(httpIn.nextLine());
                sbf.append("\n");
            }
            f.setResult(sbf.toString());
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return mapping.findForward("success");
    }

    private static URLConnection getAutoDbpServletConnection(String urlString) throws IOException {
        URL url = new URL(urlString);
        HostnameVerifier hv = new HostnameVerifier() {
            public boolean verify(String urlHostName, SSLSession session) {
                System.out.println("Warning: URL Host: " + urlHostName + " vs. " + session.getPeerHost());
                return true;
            }
        };
        HttpsURLConnection.setDefaultHostnameVerifier(hv);
        URLConnection connection = url.openConnection();
        String plain = "pss2-test:PSS2Akua";
        String enocoded = new sun.misc.BASE64Encoder().encode(plain.getBytes());
        connection.setRequestProperty("Authorization", "Basic " + enocoded);
        connection.setDoOutput(true);
        return connection;
    }
}
