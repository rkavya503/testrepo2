package com.honeywell.dras.vtn.web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.DatatypeConfigurationException;

import com.honeywell.dras.openadr2.eipayloads.OadrCreatedEvent;
import com.honeywell.dras.openadr2.eipayloads.OadrDistributeEvent;
import com.honeywell.dras.openadr2.eipayloads.OadrRequestEvent;
import com.honeywell.dras.openadr2.eipayloads.OadrResponse;
import com.honeywell.dras.openadr2.eipayloads.PayloadHelper;
import com.honeywell.dras.payload.PayloadHandler;

/**
 * VTN  
 */
//COPY VTN fron dras 8 overt to DRAS 7 to let current 1.0 customer get 2.0 support. This is temp. solution
public class VTNServlet extends HttpServlet {
	
	/**
	 * Serial id
	 */
	private static final long serialVersionUID = -2202680220316911860L;

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {
			delegate(request, response);
		} catch (Exception e) {
			throw new ServletException(e.getLocalizedMessage(), e);
		}
	}

	private void delegate(HttpServletRequest request,
			HttpServletResponse response) throws JAXBException, IOException,
			DatatypeConfigurationException {
		String clientName= request.getUserPrincipal().getName();
		Unmarshaller u = PayloadHelper.getUnmarshaller();
		Object o = u.unmarshal(request.getInputStream());
		//PayloadUtil.printRequest(o);
		if (o instanceof OadrRequestEvent) {
			if( ((OadrRequestEvent) o).getEiRequestEvent().getVenID().equals(clientName)){
				handleRequest((OadrRequestEvent) o, request, response);
			}else{
				response.sendError(401);
			}
		} else if (o instanceof OadrCreatedEvent) {
			if( ((OadrCreatedEvent) o).getEiCreatedEvent().getVenID().equals(clientName)){
				handleRequest((OadrCreatedEvent) o, request, response);
			}else{
				response.sendError(401);
			}
		} else {
			handleRequest(o, response);
		}
	}
	
	
	private void handleRequest(Object o, HttpServletResponse response)
			throws JAXBException, IOException {
		String result = "UNSUPPORTED OPERATION: \n\n";
		if (o != null) {
			result = result + o.toString();
			PrintWriter out = response.getWriter();
			out.println(result);
		}

	}

	private void marshallResponse(Object o, HttpServletResponse response)
			throws JAXBException, IOException {
		
		response.setContentType("text/xml;charset=utf-8");
		response.setCharacterEncoding("UTF-8");
		
		Marshaller marshaller = PayloadHelper.getMarshaller();
		marshaller.marshal(o, response.getWriter());

	}

	private void handleRequest(OadrCreatedEvent payload,
			HttpServletRequest request, HttpServletResponse response)
			throws JAXBException, IOException {

		PayloadHandler ph = new PayloadHandler();
		OadrResponse oadrResponse = ph.getOadrResponse(payload);
		//PayloadUtil.printResponse(oadrResponse);
		marshallResponse(oadrResponse, response);
	}

	private void handleRequest(OadrRequestEvent payload,
			HttpServletRequest request, HttpServletResponse response)
			throws JAXBException, IOException, DatatypeConfigurationException {
		PayloadHandler ph = new PayloadHandler();
		OadrDistributeEvent oadrDistributeEvent = ph
				.getOadrDistributeEvent(payload);
		//PayloadUtil.printResponse(oadrDistributeEvent);
		marshallResponse(oadrDistributeEvent, response);
	}

}
