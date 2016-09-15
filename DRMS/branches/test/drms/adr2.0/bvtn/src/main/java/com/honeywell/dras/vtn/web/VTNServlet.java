package com.honeywell.dras.vtn.web;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.DatatypeConfigurationException;

import org.jboss.logging.Logger;

import com.honeywell.dras.openadr2b.eipayloads.PayloadHelper;
import com.honeywell.dras.openadr2.payloads.b.OadrCancelOptType;
import com.honeywell.dras.openadr2.payloads.b.OadrCancelPartyRegistrationType;
import com.honeywell.dras.openadr2.payloads.b.OadrCancelReportType;
import com.honeywell.dras.openadr2.payloads.b.OadrCanceledOptType;
import com.honeywell.dras.openadr2.payloads.b.OadrCanceledPartyRegistrationType;
import com.honeywell.dras.openadr2.payloads.b.OadrCanceledReportType;
import com.honeywell.dras.openadr2.payloads.b.OadrCreateOptType;
import com.honeywell.dras.openadr2.payloads.b.OadrCreatePartyRegistrationType;
import com.honeywell.dras.openadr2.payloads.b.OadrCreateReportType;
import com.honeywell.dras.openadr2.payloads.b.OadrCreatedEventType;
import com.honeywell.dras.openadr2.payloads.b.OadrCreatedOptType;
import com.honeywell.dras.openadr2.payloads.b.OadrCreatedPartyRegistrationType;
import com.honeywell.dras.openadr2.payloads.b.OadrCreatedReportType;
import com.honeywell.dras.openadr2.payloads.b.OadrDistributeEventType;
import com.honeywell.dras.openadr2.payloads.b.OadrPayload;
import com.honeywell.dras.openadr2.payloads.b.OadrPollType;
import com.honeywell.dras.openadr2.payloads.b.OadrQueryRegistrationType;
import com.honeywell.dras.openadr2.payloads.b.OadrRegisterReportType;
import com.honeywell.dras.openadr2.payloads.b.OadrRegisteredReportType;
import com.honeywell.dras.openadr2.payloads.b.OadrRequestEventType;
import com.honeywell.dras.openadr2.payloads.b.OadrRequestReregistrationType;
import com.honeywell.dras.openadr2.payloads.b.OadrResponseType;
import com.honeywell.dras.openadr2.payloads.b.OadrSignedObject;
import com.honeywell.dras.openadr2.payloads.b.OadrUpdateReportType;
import com.honeywell.dras.openadr2.payloads.b.OadrUpdatedReportType;
import com.honeywell.dras.payload.PayloadGenerator;
import com.honeywell.dras.payload.PojoGenerator;
import com.honeywell.dras.vtn.api.event.CreatedEvent;
import com.honeywell.dras.vtn.api.event.DistributeEvent;
import com.honeywell.dras.vtn.api.event.RequestEvent;
import com.honeywell.dras.vtn.api.opt.CancelOpt;
import com.honeywell.dras.vtn.api.opt.CanceledOpt;
import com.honeywell.dras.vtn.api.opt.CreateOpt;
import com.honeywell.dras.vtn.api.opt.CreatedOpt;
import com.honeywell.dras.vtn.api.poll.Poll;
import com.honeywell.dras.vtn.api.poll.PollResponse;
import com.honeywell.dras.vtn.api.registration.CancelPartyRegistration;
import com.honeywell.dras.vtn.api.registration.CanceledPartyRegistration;
import com.honeywell.dras.vtn.api.registration.CreatePartyRegistration;
import com.honeywell.dras.vtn.api.registration.CreatedPartyRegistration;
import com.honeywell.dras.vtn.api.registration.QueryRegistration;
import com.honeywell.dras.vtn.api.registration.ReRegistration;
import com.honeywell.dras.vtn.api.report.CancelReport;
import com.honeywell.dras.vtn.api.report.CanceledReport;
import com.honeywell.dras.vtn.api.report.CreateReport;
import com.honeywell.dras.vtn.api.report.CreatedReport;
import com.honeywell.dras.vtn.api.report.RegisterReport;
import com.honeywell.dras.vtn.api.report.RegisteredReport;
import com.honeywell.dras.vtn.api.report.UpdateReport;
import com.honeywell.dras.vtn.api.report.UpdatedReport;
import com.honeywell.dras.vtn.dras.service.VtnDrasServiceException;
import com.honeywell.dras.vtn.util.PayloadUtil;

/**
 * VTN
 * 
 * @author sunil
 * 
 */
public class VTNServlet extends HttpServlet {

	/**
	 * Serial id
	 */
	private static final long serialVersionUID = -2202680220316911860L;
	
	private static final String SSL_CLIENT_S_DN = "SSL_CLIENT_S_DN";
	private static final String SSL_CLIENT_I_DN = "SSL_CLIENT_I_DN";
	private static final String SSL_SERVER_S_DN_OU = "SSL_SERVER_S_DN_OU";
	private static final String SSL_CLIENT_VERIFY = "SSL_CLIENT_VERIFY";
	

	/*@EJB
	private VTNHelper.L vtnHelper;*/
	
	private VTNHelper vtnHelper = new VTNHelperBean();
	
	private Logger log = Logger.getLogger(VTNServlet.class);
	
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
		Unmarshaller u = PayloadHelper.getUnmarshaller();
		Object o = u.unmarshal(request.getInputStream());

		if (o instanceof OadrPayload) {

			OadrPayload oadrPayload = (OadrPayload) o;
			OadrSignedObject signedObject = ((OadrPayload) o)
					.getOadrSignedObject();
			if (signedObject != null) {
				o = signedObject.getOadrCanceledOpt();
				if (o == null) {
					o = signedObject.getOadrCanceledPartyRegistration();
				}
				if (o == null) {
					o = signedObject.getOadrCanceledReport();
				}
				if (o == null) {
					o = signedObject.getOadrCancelOpt();
				}
				if (o == null) {
					o = signedObject.getOadrCancelPartyRegistration();
				}
				if (o == null) {
					o = signedObject.getOadrCancelReport();
				}
				if (o == null) {
					o = signedObject.getOadrCreatedEvent();
				}
				if (o == null) {
					o = signedObject.getOadrCreatedOpt();
				}
				if (o == null) {
					o = signedObject.getOadrCreatedPartyRegistration();
				}
				if (o == null) {
					o = signedObject.getOadrCreatedReport();
				}
				if (o == null) {
					o = signedObject.getOadrCreateOpt();
				}
				if (o == null) {
					o = signedObject.getOadrCreatePartyRegistration();
				}
				if (o == null) {
					o = signedObject.getOadrCreateReport();
				}
				if (o == null) {
					o = signedObject.getOadrDistributeEvent();
				}
				if (o == null) {
					o = signedObject.getOadrPoll();
				}
				if (o == null) {
					o = signedObject.getOadrQueryRegistration();
				}
				if (o == null) {
					o = signedObject.getOadrRegisteredReport();
				}
				if (o == null) {
					o = signedObject.getOadrRegisterReport();
				}
				if (o == null) {
					o = signedObject.getOadrRequestEvent();
				}
				if (o == null) {
					o = signedObject.getOadrRequestReregistration();
				}
				if (o == null) {
					o = signedObject.getOadrResponse();
				}
				if (o == null) {
					o = signedObject.getOadrUpdatedReport();
				}
				if (o == null) {
					o = signedObject.getOadrUpdateReport();
				}
				if (o == null) {
					o = signedObject.getOadrCanceledReport();
				}
			}

		}

		// JAXBElement<Object> jbo = (JAXBElement<Object>) o;
		// o = jbo.getValue();

		System.out.println("==== VTN ");
		PayloadUtil.printRequest(o);
		if (o instanceof OadrRequestEventType) {
			handleRequest((OadrRequestEventType) o, request, response);
		} else if (o instanceof OadrCreatedEventType) {
			handleRequest((OadrCreatedEventType) o, request, response);
		} else if (o instanceof OadrCreatePartyRegistrationType) {
			handleRequest((OadrCreatePartyRegistrationType) o, request,
					response);
		} else if (o instanceof OadrCancelPartyRegistrationType) {
			handleRequest((OadrCancelPartyRegistrationType) o, request,
					response);
		} else if (o instanceof OadrPollType) {
			handleRequest((OadrPollType) o, request, response);
		} else if (o instanceof OadrRegisterReportType) {
			handleRequest((OadrRegisterReportType) o, request, response);
		} else if (o instanceof OadrUpdateReportType) {
			handleRequest((OadrUpdateReportType) o, request, response);
		} else if (o instanceof OadrCreateOptType) {
			handleRequest((OadrCreateOptType) o, request, response);
		} else if (o instanceof OadrCancelOptType) {
			handleRequest((OadrCancelOptType) o, request, response);
		} else if (o instanceof OadrCreatedReportType) {
			handleRequest((OadrCreatedReportType) o, request, response);
		} else if (o instanceof OadrCreateReportType) {
			handleRequest((OadrCreateReportType) o, request, response);
		} else if (o instanceof OadrQueryRegistrationType) {
			handleRequest((OadrQueryRegistrationType) o, request, response);
		} else if (o instanceof OadrCanceledPartyRegistrationType) {
			handleRequest((OadrCanceledPartyRegistrationType) o, request,
					response);
		} else if (o instanceof OadrRegisteredReportType) {
			handleRequest((OadrRegisteredReportType) o, request, response);
		} else if (o instanceof OadrResponseType) {
			handleRequest((OadrResponseType) o, request, response);
		} else if (o instanceof OadrCanceledReportType) {
			handleRequest((OadrCanceledReportType) o, request, response);
		} else {
			handleRequest(o, response);
		}

	}

	private void handleRequest(OadrCreateReportType payload,
			HttpServletRequest request, HttpServletResponse response)
			throws JAXBException, IOException {

		if (payload != null) {

			PayloadGenerator pg = new PayloadGenerator();
			CreatedReport cReport = null;
			PojoGenerator pojoGenerator = null;
			if(request.getHeader(SSL_CLIENT_S_DN) != null)
			{
				System.out.println("1111111111111 OadrCreateReportType");
				pojoGenerator = new PojoGenerator(request.getHeader(SSL_CLIENT_S_DN));
			}
			else
			{
				System.out.println("222222222222 OadrCreateReportType");
				pojoGenerator = new PojoGenerator(SSL_CLIENT_S_DN);
			}
			
			CreateReport createReport = pojoGenerator.getCreateReport(payload);
			try {
				cReport = vtnHelper.getVtnDrasService().createReport(createReport);
			} catch (VtnDrasServiceException e) {
				log.error("Exception in createReport on VTNServlet!!! "+e);
			}
			OadrCreatedReportType createdReport = pg
					.getOadrCreatedReportType(cReport);
			PayloadUtil.printResponse(createdReport);
			marshallResponse(createdReport, response);

		}
	}

	private void handleRequest(OadrCanceledReportType payload,
			HttpServletRequest request, HttpServletResponse response)
			throws JAXBException, IOException {
		if (payload != null) {
			PayloadGenerator pg = new PayloadGenerator();
			com.honeywell.dras.vtn.api.common.Response rr = null;
			PojoGenerator pojoGenerator = null;
			if(request.getHeader(SSL_CLIENT_S_DN) != null)
			{
				System.out.println("1111111111111 OadrCanceledReportType");
				pojoGenerator = new PojoGenerator(request.getHeader(SSL_CLIENT_S_DN));
			}
			else
			{
				System.out.println("222222222222 OadrCanceledReportType");
				pojoGenerator = new PojoGenerator(SSL_CLIENT_S_DN);
			}
			
			CanceledReport canceledReport = pojoGenerator.getCanceledReport(payload);
			try {
				rr = vtnHelper.getVtnDrasService().canceledReport(canceledReport);
			} catch (VtnDrasServiceException e) {
				log.error("Exception in canceledReport on VTNServlet!!! "+e);
			}
			OadrResponseType oadrResponse  = pg.getOadrResponse(rr);
			PayloadUtil.printResponse(oadrResponse);
			marshallResponse(oadrResponse, response);
		}

	}

	private void handleRequest(OadrResponseType o, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		// String result = "SUCCESS \n\n";
		// PrintWriter out = response.getWriter();
		// out.println(result);

	}

	private void handleRequest(OadrRegisteredReportType payload,
			HttpServletRequest request, HttpServletResponse response)
			throws JAXBException, IOException {

		if (payload != null) {

			PayloadGenerator pg = new PayloadGenerator();
			com.honeywell.dras.vtn.api.common.Response rr = null;
			PojoGenerator pojoGenerator = null;
			if(request.getHeader(SSL_CLIENT_S_DN) != null)
			{
				System.out.println("1111111111111 OadrRegisteredReportType");
				pojoGenerator = new PojoGenerator(request.getHeader(SSL_CLIENT_S_DN));
			}
			else
			{
				System.out.println("222222222222 OadrRegisteredReportType");
				pojoGenerator = new PojoGenerator(SSL_CLIENT_S_DN);
			}
			RegisteredReport registeredReport = pojoGenerator.getRegisteredReport(payload);
			try {
				rr = vtnHelper.getVtnDrasService().registeredReport(registeredReport);
			} catch (VtnDrasServiceException e) {
				log.error("Exception in registeredReport on VTNServlet!!! "+e);
			}
			OadrResponseType oadrResponse  = pg.getOadrResponse(rr);
			PayloadUtil.printResponse(oadrResponse);
			marshallResponse(oadrResponse, response);
		}

	}

	private void handleRequest(OadrCanceledPartyRegistrationType payload,
			HttpServletRequest request, HttpServletResponse response)
			throws JAXBException, IOException {

		if (payload != null) {

			PayloadGenerator pg = new PayloadGenerator();
			com.honeywell.dras.vtn.api.common.Response rr = null;
			PojoGenerator pojoGenerator = null;
			if(request.getHeader(SSL_CLIENT_S_DN) != null)
			{
				System.out.println("1111111111111 OadrCanceledPartyRegistrationType");
				pojoGenerator = new PojoGenerator(request.getHeader(SSL_CLIENT_S_DN));
			}
			else
			{
				System.out.println("222222222222 OadrCanceledPartyRegistrationType");
				pojoGenerator = new PojoGenerator(SSL_CLIENT_S_DN);
			}
			CanceledPartyRegistration canceledPartyRegistration = pojoGenerator.getCanceledPartyRegistration(payload);
			try {
				rr = vtnHelper.getVtnDrasService().canceledPartyRegistration(canceledPartyRegistration);
			} catch (VtnDrasServiceException e) {
				log.error("Exception in canceledPartyRegistration on VTNServlet!!! "+e);
			}
			OadrResponseType oadrResponse  = pg.getOadrResponse(rr);
			PayloadUtil.printResponse(oadrResponse);
			marshallResponse(oadrResponse, response);
			
		}

	}

	private void handleRequest(OadrCreatedReportType payload,
			HttpServletRequest request, HttpServletResponse response)
			throws JAXBException, IOException {

		if (payload != null) {

			PayloadGenerator pg = new PayloadGenerator();
			com.honeywell.dras.vtn.api.common.Response rr = null;
			PojoGenerator pojoGenerator = null;
			if(request.getHeader(SSL_CLIENT_S_DN) != null)
			{
				System.out.println("1111111111111 OadrCreatedReportType");
				pojoGenerator = new PojoGenerator(request.getHeader(SSL_CLIENT_S_DN));
			}
			else
			{
				System.out.println("222222222222 OadrCreatedReportType");
				pojoGenerator = new PojoGenerator(SSL_CLIENT_S_DN);
			}
			CreatedReport createdReport = pojoGenerator.getCreatedReport(payload);
			try {
				rr = vtnHelper.getVtnDrasService().createdReport(createdReport);
			} catch (VtnDrasServiceException e) {
				log.error("Exception in createdReport on VTNServlet!!! "+e);
			}
			OadrResponseType oadrResponse  = pg.getOadrResponse(rr);
			PayloadUtil.printResponse(oadrResponse);
			marshallResponse(oadrResponse, response);			

		}

	}

	private void handleRequest(OadrCancelOptType payload,
			HttpServletRequest request, HttpServletResponse response)
			throws JAXBException, IOException {

		if (payload != null) {

			PayloadGenerator pg = new PayloadGenerator();
			CanceledOpt rr = null;
			PojoGenerator pojoGenerator = null;
			if(request.getHeader(SSL_CLIENT_S_DN) != null)
			{
				System.out.println("1111111111111 OadrCancelOptType");
				pojoGenerator = new PojoGenerator(request.getHeader(SSL_CLIENT_S_DN));
			}
			else
			{
				System.out.println("222222222222 OadrCancelOptType");
				pojoGenerator = new PojoGenerator(SSL_CLIENT_S_DN);
			}
			CancelOpt cancelOpt = pojoGenerator.getCancelOpt(payload);
			try {
				rr = vtnHelper.getVtnDrasService().cancelOpt(cancelOpt);
			} catch (VtnDrasServiceException e) {
				log.error("Exception in cancelOpt on VTNServlet!!! "+e);
			}
			OadrCanceledOptType oadrResponse  = pg.getOadrCanceledOptType(rr);
			PayloadUtil.printResponse(oadrResponse);
			marshallResponse(oadrResponse, response);			
		}

	}

	private void handleRequest(OadrCreateOptType payload,
			HttpServletRequest request, HttpServletResponse response)
			throws JAXBException, IOException {

		if (payload != null) {
			
			PayloadGenerator pg = new PayloadGenerator();
			CreatedOpt rr = null;
			PojoGenerator pojoGenerator = null;
			if(request.getHeader(SSL_CLIENT_S_DN) != null)
			{
				System.out.println("1111111111111 OadrCreateOptType");
				pojoGenerator = new PojoGenerator(request.getHeader(SSL_CLIENT_S_DN));
			}
			else
			{
				System.out.println("222222222222 OadrCreateOptType");
				pojoGenerator = new PojoGenerator(SSL_CLIENT_S_DN);
			}
			CreateOpt createOpt = pojoGenerator.getCreateOpt(payload);
			try {
				rr = vtnHelper.getVtnDrasService().createOpt(createOpt);
			} catch (VtnDrasServiceException e) {
				log.error("Exception in createOpt on VTNServlet!!! "+e);
			}
			
			OadrCreatedOptType oadrResponse = pg.getOadrCreatedOptType(rr);
			PayloadUtil.printResponse(oadrResponse);
			marshallResponse(oadrResponse, response);		

		}

	}

	private void handleRequest(OadrUpdateReportType payload,
			HttpServletRequest request, HttpServletResponse response)
			throws JAXBException, IOException {

		if (payload != null) {

			PayloadGenerator pg = new PayloadGenerator();
			UpdatedReport rr = null;
			PojoGenerator pojoGenerator = null;
			if(request.getHeader(SSL_CLIENT_S_DN) != null)
			{
				System.out.println("1111111111111 OadrUpdateReportType");
				pojoGenerator = new PojoGenerator(request.getHeader(SSL_CLIENT_S_DN));
			}
			else
			{
				System.out.println("222222222222 OadrUpdateReportType");
				pojoGenerator = new PojoGenerator(SSL_CLIENT_S_DN);
			}
			UpdateReport updateReport = pojoGenerator.getUpdateReport(payload);
			try {
				rr = vtnHelper.getVtnDrasService().updateReport(updateReport);
			} catch (VtnDrasServiceException e) {
				log.error("Exception in updateReport on VTNServlet!!! "+e);
			}
			OadrUpdatedReportType oadrResponse = pg
					.getOadrUpdatedReportType(rr);
			PayloadUtil.printResponse(oadrResponse);
			marshallResponse(oadrResponse, response);		

		}

	}

	private void handleRequest(OadrRegisterReportType payload,
			HttpServletRequest request, HttpServletResponse response)
			throws JAXBException, IOException, DatatypeConfigurationException {

		if (payload != null) {

			PayloadGenerator pg = new PayloadGenerator();
			RegisteredReport rr = null;
			PojoGenerator pojoGenerator = null;
			if(request.getHeader(SSL_CLIENT_S_DN) != null)
			{
				System.out.println("1111111111111 OadrRegisterReportType");
				pojoGenerator = new PojoGenerator(request.getHeader(SSL_CLIENT_S_DN));
			}
			else
			{
				System.out.println("222222222222 OadrRegisterReportType");
				pojoGenerator = new PojoGenerator(SSL_CLIENT_S_DN);
			}
			RegisterReport registerReport = pojoGenerator.getRegisterReport(payload);
			try {
				rr = vtnHelper.getVtnDrasService().registerReport(registerReport);
			} catch (VtnDrasServiceException e) {
				log.error("Exception in registerReport on VTNServlet!!! "+e);
			}			
			OadrRegisteredReportType oadrResponse = pg
					.getOadrRegisteredReportType(rr);
			PayloadUtil.printResponse(oadrResponse);
			marshallResponse(oadrResponse, response);		
			
		}

	}

	private void handleRequest(OadrPollType payload,
			HttpServletRequest request, HttpServletResponse response)
			throws JAXBException, IOException, DatatypeConfigurationException {

		if (payload != null) {
			
			PollResponse resp = null;
			PojoGenerator pojoGenerator = null;
			if(request.getHeader(SSL_CLIENT_S_DN) != null)
			{
				System.out.println("1111111111111 OadrPollType");
				pojoGenerator = new PojoGenerator(request.getHeader(SSL_CLIENT_S_DN));
			}
			else
			{
				System.out.println("222222222222 OadrPollType");
				pojoGenerator = new PojoGenerator(SSL_CLIENT_S_DN);
			}
			Poll poll = pojoGenerator.getPoll(payload);
			try {
				resp = vtnHelper.getVtnDrasService().poll(poll);
			} catch (VtnDrasServiceException e) {
				log.error("Exception in poll on VTNServlet!!! "+e);
			}

			PayloadGenerator pg = new PayloadGenerator();

			Object oadrResponse = null;
			if (resp == null) {
				com.honeywell.dras.vtn.api.common.Response rr = new com.honeywell.dras.vtn.api.common.Response();
				rr.setRequestId("AKUACOM DRAS 8 - ERROR PROCESSING POLL");
				rr.setResponseCode("403");
				rr.setResponseDescription("POLL FAILED.  Contact VTN Operator for details");
				rr.setSchemaVersion(payload.getSchemaVersion());
				rr.setVenId(payload.getVenID());
				OadrResponseType o = pg.getOadrResponse(rr);
				oadrResponse = o;
			} else if(null != resp.getReRegistration()){
				ReRegistration rr = (ReRegistration)resp.getReRegistration();
				OadrRequestReregistrationType o = pg
						.getOadrRequestReregistrationType(rr);
				oadrResponse = o;
			}else if(null != resp.getCancelPartyRegistration()){
				CancelPartyRegistration rr = (CancelPartyRegistration)resp.getCancelPartyRegistration();
				OadrCancelPartyRegistrationType o = pg
						.getOadrCancelPartyRegistrationType(rr );
				oadrResponse = o;
			}else if(null != resp.getCancelReport()){
				CancelReport rr = (CancelReport)resp.getCancelReport();
				OadrCancelReportType o = pg.getOadrCancelReportType(rr);
				oadrResponse = o;
			}else if(null != resp.getCreateReport()){
				CreateReport rr = (CreateReport)resp.getCreateReport();
				OadrCreateReportType o = pg.getOadrCreateReportType(rr);
				oadrResponse = o;
			}else if(null != resp.getRegisterReport()){
				RegisterReport rr = (RegisterReport)resp.getRegisterReport();
				OadrRegisterReportType o = pg
						.getOadrRegisterReportType(rr);
				oadrResponse = o;
			}else if(null != resp.getDistributeEvent()){
				DistributeEvent rr = (DistributeEvent)resp.getDistributeEvent();
				OadrDistributeEventType o = pg
						.getOadrDistributeEvent(rr);
				oadrResponse = o;
			}else if(null != resp.getResponse()){
				com.honeywell.dras.vtn.api.common.Response rr = (com.honeywell.dras.vtn.api.common.Response)resp.getResponse();
				OadrResponseType o = pg.getOadrResponse(rr);
				oadrResponse = o;
			}

			PayloadUtil.printResponse(oadrResponse);
			marshallResponse(oadrResponse, response);		

			}

		}


	private void handleRequest(OadrCancelPartyRegistrationType payload,
			HttpServletRequest request, HttpServletResponse response)
			throws JAXBException, IOException {

		if (payload != null) {

			PayloadGenerator pg = new PayloadGenerator();
			CanceledPartyRegistration rr = null;
			PojoGenerator pojoGenerator = null;
			if(request.getHeader(SSL_CLIENT_S_DN) != null)
			{
				System.out.println("1111111111111 OadrCancelPartyRegistrationType");
				pojoGenerator = new PojoGenerator(request.getHeader(SSL_CLIENT_S_DN));
			}
			else
			{
				System.out.println("222222222222 OadrCancelPartyRegistrationType");
				pojoGenerator = new PojoGenerator(SSL_CLIENT_S_DN);
			}
			CancelPartyRegistration cancelPartyRegistration = pojoGenerator.getCancelPartyRegistration(payload);
			try {
				rr = vtnHelper.getVtnDrasService().cancelPartyRegistration(cancelPartyRegistration);
			} catch (VtnDrasServiceException e) {
				log.error("Exception in cancelPartyRegistration on VTNServlet!!! "+e);
			}
			OadrCanceledPartyRegistrationType oadrResponse = pg
					.getOadrCanceledPartyRegistrationType(rr);
			PayloadUtil.printResponse(oadrResponse);
			marshallResponse(oadrResponse, response);		
			
			
		}
	}


	private void handleRequest(OadrCreatePartyRegistrationType payload,
			HttpServletRequest request, HttpServletResponse response)
			throws JAXBException, IOException {

		if (payload != null) {

			log.debug(">>>>>>>>The values from the soap header are: \n");
			if(request.getHeader(SSL_CLIENT_S_DN) != null)
			{
				
			}
			else
			{
				System.out.println("Request header is nullll");
			}
//			log.debug(request.getHeader(SSL_CLIENT_I_DN));
//			log.debug(request.getHeader(SSL_SERVER_S_DN_OU));
//			log.debug(request.getHeader(SSL_CLIENT_VERIFY));
			
			PayloadGenerator pg = new PayloadGenerator();
			CreatedPartyRegistration rr = null;
			PojoGenerator pojoGenerator = new PojoGenerator(SSL_CLIENT_S_DN);
			CreatePartyRegistration createPartyRegistration = pojoGenerator.getCreatePartyRegistration(payload);
			System.out.println("Hanle party Registration request %%%%%%%%%%%%%%% ");
			try {
				rr = vtnHelper.getVtnDrasService().createPartyRegistration(createPartyRegistration);
			} catch (VtnDrasServiceException e) {
				log.error("Exception in createPartyRegistration on VTNServlet!!! "+e);
			}
			OadrCreatedPartyRegistrationType oadrResponse = pg
					.getOadrCreatedPartyRegistrationType(rr);
			PayloadUtil.printResponse(oadrResponse);
			marshallResponse(oadrResponse, response);		
			

		}

	}

	private void handleRequest(OadrQueryRegistrationType payload,
			HttpServletRequest request, HttpServletResponse response)
			throws JAXBException, IOException {

		if (payload != null) {

			PayloadGenerator pg = new PayloadGenerator();
			CreatedPartyRegistration rr = null;
			PojoGenerator pojoGenerator = null;
			if(request.getHeader(SSL_CLIENT_S_DN) != null)
			{
				System.out.println("1111111111111 OadrQueryRegistrationType");
				pojoGenerator = new PojoGenerator(request.getHeader(SSL_CLIENT_S_DN));
			}
			else
			{
				System.out.println("222222222222 OadrQueryRegistrationType");
				pojoGenerator = new PojoGenerator(SSL_CLIENT_S_DN);
			}
			QueryRegistration queryRegistration = pojoGenerator.getQueryRegistration(payload);
			try {
				rr = vtnHelper.getVtnDrasService().queryRegistration(queryRegistration);
			} catch (VtnDrasServiceException e) {
				log.error("Exception in queryRegistration on VTNServlet!!! "+e);
			}
			OadrCreatedPartyRegistrationType oadrResponse = pg
					.getOadrCreatedPartyRegistrationType(rr);

			PayloadUtil.printResponse(oadrResponse);
			marshallResponse(oadrResponse, response);		

		}

	}

	private void handleRequest(Object o, HttpServletResponse response)
			throws JAXBException, IOException {
		String result = "UNSUPPORTED OPERATION: \n\n";
		PrintWriter out = response.getWriter();
		out.println(result);
	}

	private void marshallResponse(Object o, HttpServletResponse response)
			throws JAXBException, IOException {
		Marshaller marshaller = PayloadHelper.getMarshaller();
		//marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

		OadrPayload oadrPayload = new OadrPayload();
		OadrSignedObject signedObject = new OadrSignedObject();

		if (o instanceof OadrDistributeEventType) {
			signedObject.setOadrDistributeEvent((OadrDistributeEventType) o);
		} else if (o instanceof OadrRequestEventType) {
			signedObject.setOadrRequestEvent((OadrRequestEventType) o);

		} else if (o instanceof OadrCreatedEventType) {
			signedObject.setOadrCreatedEvent((OadrCreatedEventType) o);

		} else if (o instanceof OadrResponseType) {
			signedObject.setOadrResponse((OadrResponseType) o);

		} else if (o instanceof OadrQueryRegistrationType) {
			signedObject
					.setOadrQueryRegistration((OadrQueryRegistrationType) o);

		} else if (o instanceof OadrCreatedPartyRegistrationType) {
			signedObject
					.setOadrCreatedPartyRegistration((OadrCreatedPartyRegistrationType) o);

		} else if (o instanceof OadrCreatePartyRegistrationType) {
			signedObject
					.setOadrCreatePartyRegistration((OadrCreatePartyRegistrationType) o);

		} else if (o instanceof OadrCancelPartyRegistrationType) {
			signedObject
					.setOadrCancelPartyRegistration((OadrCancelPartyRegistrationType) o);

		} else if (o instanceof OadrCanceledPartyRegistrationType) {
			signedObject
					.setOadrCanceledPartyRegistration((OadrCanceledPartyRegistrationType) o);

		} else if (o instanceof OadrPollType) {
			signedObject.setOadrPoll((OadrPollType) o);

		} else if (o instanceof OadrRegisterReportType) {
			signedObject.setOadrRegisterReport((OadrRegisterReportType) o);

		} else if (o instanceof OadrRegisteredReportType) {
			signedObject.setOadrRegisteredReport((OadrRegisteredReportType) o);

		} else if (o instanceof OadrUpdateReportType) {
			signedObject.setOadrUpdateReport((OadrUpdateReportType) o);

		} else if (o instanceof OadrUpdatedReportType) {
			signedObject.setOadrUpdatedReport((OadrUpdatedReportType) o);

		} else if (o instanceof OadrCreateReportType) {
			signedObject.setOadrCreateReport((OadrCreateReportType) o);

		} else if (o instanceof OadrCreateOptType) {
			signedObject.setOadrCreateOpt((OadrCreateOptType) o);

		} else if (o instanceof OadrCreatedOptType) {
			signedObject.setOadrCreatedOpt((OadrCreatedOptType) o);

		} else if (o instanceof OadrCancelOptType) {
			signedObject.setOadrCancelOpt((OadrCancelOptType) o);

		} else if (o instanceof OadrCanceledOptType) {
			signedObject.setOadrCanceledOpt((OadrCanceledOptType) o);

		} else if (o instanceof OadrCreatedReportType) {
			signedObject.setOadrCreatedReport((OadrCreatedReportType) o);

		} else if (o instanceof OadrCancelReportType) {
			signedObject.setOadrCancelReport((OadrCancelReportType) o);

		} else if (o instanceof OadrRequestReregistrationType) {
			signedObject
					.setOadrRequestReregistration((OadrRequestReregistrationType) o);

		} else if (o instanceof OadrPayload) {
			oadrPayload = (OadrPayload) o;
			signedObject = oadrPayload.getOadrSignedObject();

		} else if (o instanceof OadrSignedObject) {
			signedObject = ((OadrSignedObject) o);

		}

		oadrPayload.setOadrSignedObject(signedObject);
		marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");

		response.setContentType("application/xml");
		response.setHeader("Content-Encoding", "UTF-8");
		response.setHeader("Connection", "Keep-Alive");

		ByteArrayOutputStream buff = new ByteArrayOutputStream();
		PrintWriter writer = new PrintWriter(buff);

		// marshaller.marshal(oadrPayload, response.getWriter());
		marshaller.marshal(oadrPayload, writer);

		response.setContentLength(buff.size());
		buff.writeTo(response.getOutputStream());

	}

	private void handleRequest(OadrCreatedEventType payload,
			HttpServletRequest request, HttpServletResponse response)
			throws JAXBException, IOException {

		if (payload != null) {

			PayloadGenerator pg = new PayloadGenerator();
			com.honeywell.dras.vtn.api.common.Response rr = null;
			PojoGenerator pojoGenerator = null;
			if(request.getHeader(SSL_CLIENT_S_DN) != null)
			{
				System.out.println("1111111111111 OadrCreatedEventType");
				pojoGenerator = new PojoGenerator(request.getHeader(SSL_CLIENT_S_DN));
			}
			else
			{
				System.out.println("222222222222 OadrCreatedEventType");
				pojoGenerator = new PojoGenerator();
			}
			CreatedEvent createdEvent = pojoGenerator.getCreatedEvent(payload);
			try {
				rr = vtnHelper.getVtnDrasService().createdEvent(createdEvent);
			} catch (VtnDrasServiceException e) {
				log.error("Exception in createdEvent on VTNServlet!!! "+e);
			}
			OadrResponseType oadrResponse = pg.getOadrResponse(rr);

			PayloadUtil.printResponse(oadrResponse);
			marshallResponse(oadrResponse, response);		


		}

	}

	private void handleRequest(OadrRequestEventType payload,
			HttpServletRequest request, HttpServletResponse response)
			throws JAXBException, IOException, DatatypeConfigurationException {

		if (payload != null) {

			PayloadGenerator pg = new PayloadGenerator();
			DistributeEvent rr = null;
			PojoGenerator pojoGenerator = null;
			if(request.getHeader(SSL_CLIENT_S_DN) != null)
			{
				System.out.println("1111111111111 OadrRequestEventType ");
				pojoGenerator = new PojoGenerator(request.getHeader(SSL_CLIENT_S_DN));
			}
			else
			{
				System.out.println("222222222222 OadrRequestEventType");
				pojoGenerator = new PojoGenerator();
			}
			RequestEvent requestEvent = pojoGenerator.getRequestEvent(payload);
			try {
				rr = vtnHelper.getVtnDrasService().requestEvent(requestEvent);
			} catch (VtnDrasServiceException e) {
				log.error("Exception in requestEvent on VTNServlet!!! "+e);
			}
			OadrDistributeEventType oadrResponse = pg.getOadrDistributeEvent(rr);

			PayloadUtil.printResponse(oadrResponse);
			marshallResponse(oadrResponse, response);		

		}

	}

}
