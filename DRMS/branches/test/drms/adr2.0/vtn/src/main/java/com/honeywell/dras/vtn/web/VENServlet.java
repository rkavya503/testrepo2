package com.honeywell.dras.vtn.web;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.oasis_open.docs.ns.energyinterop._201110.EiEventType;
import org.oasis_open.docs.ns.energyinterop._201110.EventDescriptorType;

import com.honeywell.dras.vtn.api.event.CreatedEventImpl;
import com.honeywell.dras.vtn.api.event.ResponseImpl;
import com.honeywell.dras.openadr2.eipayloads.OadrCreatedEvent;
import com.honeywell.dras.openadr2.eipayloads.OadrDistributeEvent;
import com.honeywell.dras.openadr2.eipayloads.OadrDistributeEvent.OadrEvent;
import com.honeywell.dras.openadr2.eipayloads.OadrResponse;
import com.honeywell.dras.openadr2.eipayloads.PayloadHelper;
import com.honeywell.dras.payload.PayloadGenerator;
import com.honeywell.dras.vtn.api.OptType;

public class VENServlet extends HttpServlet {

	/**
	 * Serial Version ID
	 */
	private static final long serialVersionUID = 7456355762734971490L;

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
			HttpServletResponse response) throws JAXBException, IOException {
		Unmarshaller u = PayloadHelper.getUnmarshaller();
		Object o = u.unmarshal(request.getInputStream());
//		System.out.println("^^^^^^^^ VEN ");
//		PayloadUtil.printRequest(o);
		if (o instanceof OadrDistributeEvent) {
			handleRequest((OadrDistributeEvent) o, request, response);
		} else if(o instanceof OadrResponse){
			handleRequest((OadrResponse) o, request, response);
		} else{
			handleRequest(o, response);
		}

	}
	
	private void handleRequest(Object o, HttpServletResponse response)
			throws JAXBException, IOException {
		String result = "UNSUPPORTED OPERATION: \n\n";
		if(o != null){
			result = result + o.toString();
			PrintWriter out = response.getWriter();
			out.println(result);

		}
		
	}

	
	private void handleRequest(OadrResponse payload,
			HttpServletRequest request, HttpServletResponse response)
			throws JAXBException, IOException {

		if (payload != null) {
			String result = "SUCCESS - RESPONSE CODE 200";
			PrintWriter out = response.getWriter();
			out.println(result);
		}
			
		}

	private void marshallResponse(Object o, HttpServletResponse response)
			throws JAXBException, IOException {
		Marshaller marshaller = PayloadHelper.getMarshaller();
		marshaller.marshal(o, response.getWriter());

	}

	private void handleRequest(OadrDistributeEvent payload,
			HttpServletRequest request, HttpServletResponse response)
			throws JAXBException, IOException {

		if (payload != null) {
			String result = "SUCCESS - RESPONSE CODE 200";
			PrintWriter out = response.getWriter();
			out.println(result);
			
			
			CreatedEventImpl createdEvent = new CreatedEventImpl();
			createdEvent.setVenId(Constants.VENID);
			
			ResponseImpl r = new ResponseImpl();
			r.setResponseCode("200");
			String requestID = payload.getRequestID();
			r.setRequestId(requestID);
			createdEvent.setResponse(r);

			
			List<com.honeywell.dras.vtn.api.EventResponse> eventResponseList = new ArrayList<com.honeywell.dras.vtn.api.EventResponse>();
			List<OadrEvent> el = payload.getOadrEvent();
			if (el != null && !el.isEmpty()) {
				for(OadrEvent oEvent : el){
					EiEventType eiEvent = oEvent.getEiEvent();
					EventDescriptorType eventDescriptor = eiEvent.getEventDescriptor();
					if (eventDescriptor != null) {
						com.honeywell.dras.vtn.api.impl.EventResponseImpl e1 = new com.honeywell.dras.vtn.api.impl.EventResponseImpl();
						e1.setDescription(eventDescriptor.getVtnComment());
						e1.setEventID(eventDescriptor.getEventID());
						e1.setOptType(OptType.OPT_IN);
						e1.setRequestID(requestID);
						e1.setResponseCode("200");
						eventResponseList.add(e1);
					}
					
					
				}
			}
			
			
			createdEvent.setEventResponseList(eventResponseList );
			
			String vtnID = payload.getVtnID();


//			System.out.println("*********** Request Details:  Request ID - "
//					+ requestID +  " Vtn ID - "
//					+ vtnID + "\n");
//			

			
			
			
			
			
			PayloadGenerator pg = new PayloadGenerator();
			OadrCreatedEvent ce = pg.getOadrCreatedEvent(createdEvent);
			
			System.out.println("^^^^^^^^ VEN ");
//			PayloadUtil.printRequest(ce);

			URLConnection connection = null;
			InputStream is = null;
			String endPoint = Constants.VTN_END_POINT;
			try {
				connection = new URL(endPoint).openConnection();
				connection.setDoOutput(true);
				Marshaller marshaller = PayloadHelper.getMarshaller();
				marshaller.marshal(ce, connection.getOutputStream());
				is = connection.getInputStream();
				
				Unmarshaller u = PayloadHelper.getUnmarshaller();
				OadrResponse j = (OadrResponse) u.unmarshal(is);
//				System.out.println("^^^^^^^^ VEN ");
//				PayloadUtil.printResponse(j);
				
			} catch(Exception e){
				e.printStackTrace();
			}finally {
				try {
					if (is != null) {
						is.close();
					}

				} catch (Exception e) {
				}
			}

		}

	}

}
