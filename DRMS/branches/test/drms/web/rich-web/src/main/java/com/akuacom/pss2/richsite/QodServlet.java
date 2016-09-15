package com.akuacom.pss2.richsite;

import java.beans.XMLEncoder;
import java.io.BufferedOutputStream;
import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.akuacom.pss2.system.QodService;
import com.akuacom.pss2.system.Quotation;
import com.akuacom.pss2.system.SystemManager;

public class QodServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @EJB
    QodService.L qodService;
        
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/xml");
        
        XMLEncoder encoder = null;
        Quotation qod = qodService.nextQod();
        try {
            encoder = new XMLEncoder(new BufferedOutputStream(response.getOutputStream()));
            encoder.writeObject(qod);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (encoder != null) {
                encoder.close();
            }
        }
    }

}
