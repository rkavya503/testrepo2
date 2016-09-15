package com.akuacom.pss2.dlc;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;

import com.akuacom.ejb.client.EJB3Factory;
import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.core.ErrorUtil;
import com.akuacom.pss2.program.dlc.DlcProgramEJB;
import com.akuacom.pss2.program.dlc.signal.SignalsType;
import com.akuacom.pss2.system.property.CoreProperty;
import com.akuacom.pss2.system.property.CorePropertyEAO;
import com.akuacom.pss2.util.LogUtils;

public class SignalServlet extends HttpServlet {

	private static final long serialVersionUID = -7999108725485020975L;
    private static final Logger log = Logger.getLogger(SignalServlet.class);
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
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPut(req, resp);
    }

	@Override
    public void doPut(HttpServletRequest request, HttpServletResponse response) 
    		throws ServletException, IOException {

        int errors = 0;
        String errMessage = "";

        try {
            InputStream in = request.getInputStream();

            JAXBContext jc = JAXBContext.newInstance("com.akuacom.pss2.program.dlc.signal");
            Unmarshaller unmarshaller = jc.createUnmarshaller();

            JAXBElement element = (JAXBElement) unmarshaller.unmarshal(in);
            SignalsType signals = (SignalsType) element.getValue();

            final DlcProgramEJB program = EJBFactory.getBean(DlcProgramEJB.class);
            program.updateSignals(signals);
        } catch (JAXBException e) {
            errors = 1;
            errMessage = e.getMessage();
            log.error("Parsing error", e);
        } catch (Exception e) {
            errors = 1;
            errMessage = ErrorUtil.getErrorMessage(e);
            log.error(ErrorUtil.getErrorMessage(e));
            log.debug(LogUtils.createExceptionLogEntry("DLC", LogUtils.CATAGORY_WEBSERVICE, e));
        }
        sendResponse(response, errors, errMessage);
    }

    protected void sendResponse(HttpServletResponse response, int errors, String errMessage)
            throws ServletException, IOException {

        if (contentType != null) {
            response.setContentType(contentType);
        }

    	final PrintWriter out = response.getWriter();
		StringBuilder builder=new StringBuilder();
		builder.append("<?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?>\n");
		builder.append("<Response>\n    <Errors>");
		builder.append(errors);
		builder.append("</Errors>\n");
		if (errors>0) {
			builder.append("    <ErrorDescription>");
			builder.append(errMessage);
			builder.append("</ErrorDescription>\n");
		}
		builder.append("</Response>");
		out.println(builder.toString());
		out.flush();
	}

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServiceUtil.notAllowed(resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServiceUtil.notAllowed(resp);
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
