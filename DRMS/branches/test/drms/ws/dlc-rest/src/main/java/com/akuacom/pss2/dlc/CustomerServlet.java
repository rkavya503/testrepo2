package com.akuacom.pss2.dlc;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;

import com.akuacom.ejb.client.EJB3Factory;
import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.core.ErrorUtil;
import com.akuacom.pss2.program.dlc.DlcManager;
import com.akuacom.pss2.program.dlc.customers.Customer;
import com.akuacom.pss2.program.dlc.customers.Customers;
import com.akuacom.pss2.system.property.CoreProperty;
import com.akuacom.pss2.system.property.CorePropertyEAO;
import com.akuacom.pss2.util.LogUtils;

public class CustomerServlet extends HttpServlet {

	private static final long serialVersionUID = -7950402189671441538L;
	
    private static final Logger log = Logger.getLogger(CustomerServlet.class);

    private String contentType = null;

    @Override
    public void init() throws ServletException {
        super.init();

        CorePropertyEAO corePropertyEAO = EJB3Factory.getBean(CorePropertyEAO.class);
        List<CoreProperty> coreProperties = corePropertyEAO.getAll();

        for (CoreProperty coreProperty : coreProperties) {
            if ("feature.dlc.contentType".equals(coreProperty.getPropertyName())) {
                contentType = coreProperty.getStringValue();
            }
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) 
    		throws ServletException, IOException {
		
        int errors=0;
        String errMessage="";

		try {
            InputStream in = request.getInputStream();

	        JAXBContext jc = JAXBContext.newInstance("com.akuacom.pss2.program.dlc.customers");
	        Unmarshaller unmarshaller = jc.createUnmarshaller();
	        Customers customers = (Customers) unmarshaller.unmarshal(in);
	        
	        final DlcManager mgr=EJBFactory.getBean(DlcManager.class);
	        mgr.createParticipant(customers);
		} catch(Exception e) {
	        errors=1;
	        errMessage=ErrorUtil.getErrorMessage(e);
			log.error(ErrorUtil.getErrorMessage(e));
			log.debug(LogUtils.createExceptionLogEntry("DLC", LogUtils.CATAGORY_WEBSERVICE, e));
		}
        sendResponse(response, errors, errMessage);
    }
	
	@Override
    public void doPut(HttpServletRequest request, HttpServletResponse response) 
    		throws ServletException, IOException {
        int errors=0;
        String errMessage="";

		try {
            InputStream in = request.getInputStream();

	        JAXBContext jc = JAXBContext.newInstance("com.akuacom.pss2.program.dlc.customers");
	        Unmarshaller unmarshaller = jc.createUnmarshaller();
	        Customers customers = (Customers) unmarshaller.unmarshal(in);
	        
	        final DlcManager mgr=EJBFactory.getBean(DlcManager.class);
	        mgr.updateParticipant(customers);
		} catch(Exception e) {
	        errors=1;
	        errMessage=ErrorUtil.getErrorMessage(e);
			log.error(ErrorUtil.getErrorMessage(e));
			log.debug(LogUtils.createExceptionLogEntry("DLC", LogUtils.CATAGORY_WEBSERVICE, e));
		}
        sendResponse(response, errors, errMessage);
    }
	
	@Override	
	public void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
        int errors=0;
        String errMessage="";

		try {
            String pathInfo = request.getPathInfo();
            if (pathInfo != null && !"/".equals(pathInfo)) {
                String serviceId = pathInfo.substring(1);
                final DlcManager mgr = EJBFactory.getBean(DlcManager.class);
                mgr.removeParticipant(serviceId);
            }
		} catch(Exception e) {
	        errors=1;
	        errMessage=ErrorUtil.getErrorMessage(e);
			log.error(ErrorUtil.getErrorMessage(e));
			log.debug(LogUtils.createExceptionLogEntry("DLC", LogUtils.CATAGORY_WEBSERVICE, e));
		}
        sendResponse(response, errors, errMessage);
	}
	
    protected void sendResponse(HttpServletResponse response, int errors, String errMessage) throws ServletException, IOException {

        if (contentType != null) {
            response.setContentType(contentType);
        }

        final PrintWriter out = response.getWriter();
        StringBuilder builder = new StringBuilder();
        builder.append("<?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?>\n");
        builder.append("<Response>\n    <Errors>");
        builder.append(errors);
        builder.append("</Errors>\n");
        if (errors > 0) {
            builder.append("    <ErrorDescription>");
            builder.append(errMessage);
            builder.append("</ErrorDescription>\n");
        }
        builder.append("</Response>");
        out.println(builder.toString());
        out.flush();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int errors = 0;
        String errMessage = "";
        List<Customer> results = null;

        try {
            String pathInfo = request.getPathInfo();
            final DlcManager mgr = EJBFactory.getBean(DlcManager.class);
            if (pathInfo == null || "/".equals(pathInfo)) {
                results = mgr.findParticipant(new ArrayList<String>());
            } else {
                String serviceId = pathInfo.substring(1);
                Customer participant = mgr.findParticipant(serviceId);
                if (participant == null) {
                    ServiceUtil.notFound(response);
                    return;
                }
                results = new ArrayList<Customer>();
                results.add(participant);
            }

        } catch (Exception e) {
            errors = 1;
            errMessage = ErrorUtil.getErrorMessage(e);
            log.error(ErrorUtil.getErrorMessage(e));
            log.debug(LogUtils.createExceptionLogEntry("DLC", LogUtils.CATAGORY_WEBSERVICE, e));
        }
        sendResponse2(response, errors, errMessage, results);
    }

    protected void sendResponse2(HttpServletResponse response, int errors, String errMessage, List<Customer> list) throws ServletException, IOException {

        if (contentType != null) {
            response.setContentType(contentType);
        }

        final PrintWriter out = response.getWriter();
        StringBuilder builder=new StringBuilder();
        builder.append("<?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?>\n");
        builder.append("<Response>\n    <Errors>");
        builder.append(errors);
        builder.append("</Errors>\n");
        if (errors > 0) {
            builder.append("    <ErrorDescription>");
            builder.append(errMessage);
            builder.append("</ErrorDescription>\n");
        } else {
            builder.append("<customers>\n");
            for (Customer d : list) {
                builder.append("<customer>\n");

                builder.append("<serviceId>");
                builder.append(d.getServiceId());
                builder.append("</serviceId>\n");

                builder.append("<participantName>");
                builder.append(d.getParticipantName());
                builder.append("</participantName>\n");

                if (d.getClientName() != null) {
                    builder.append("<clientName>");
                    builder.append(d.getClientName());
                    builder.append("</clientName>\n");
                }

                builder.append("</customer>\n");
            }
            builder.append("</customers>\n");
        }
        builder.append("</Response>");
        out.println(builder.toString());
        out.flush();
    }

    @Override
    protected void doHead(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServiceUtil.notAllowed(resp);
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServiceUtil.notAllowed(resp);
    }

    @Override
    protected void doTrace(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServiceUtil.notAllowed(resp);
    }
}
