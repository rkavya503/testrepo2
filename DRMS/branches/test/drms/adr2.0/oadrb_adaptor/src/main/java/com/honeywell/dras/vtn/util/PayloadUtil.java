package com.honeywell.dras.vtn.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;

import com.honeywell.dras.openadr2b.eipayloads.PayloadHelper;
import com.honeywell.dras.openadr2.payloads.b.OadrCancelOptType;
import com.honeywell.dras.openadr2.payloads.b.OadrCancelPartyRegistrationType;
import com.honeywell.dras.openadr2.payloads.b.OadrCancelReportType;
import com.honeywell.dras.openadr2.payloads.b.OadrCanceledOptType;
import com.honeywell.dras.openadr2.payloads.b.OadrCanceledPartyRegistrationType;
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

/**
 * Helper class
 * @author sunil
 *
 */
public class PayloadUtil {
	
	
	public static String convertStreamToString(InputStream is)
			throws IOException {
		if (is != null) {
			Writer writer = new StringWriter();

			char[] buffer = new char[1024];
			try {
				Reader reader = new BufferedReader(new InputStreamReader(is,
						"UTF-8"));
				int n;
				while ((n = reader.read(buffer)) != -1) {
					writer.write(buffer, 0, n);
				}
			} finally {
				is.close();
			}
			return writer.toString();
		} else {
			return "";
		}
	}

	public static void printResponse(InputStream i) throws JAXBException,
			IOException {
		String p = convertStreamToString(i);
		System.out.println("********* RESPONSE PAYLOAD ***********");
		System.out.println(p);
		System.out.println("\n######################################\n");

	}

	public static void printResponse(Object o) throws JAXBException {
		Marshaller marshaller = PayloadHelper.getMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		System.out.println("********* RESPONSE PAYLOAD ***********");

		marshallToStdOut(o, marshaller);
		System.out.println("\n######################################\n");

	}

	
	private static void marshallToStdOut(Object o, Marshaller marshaller) throws JAXBException{
		
		
		
		
		OadrPayload oadrPayload = new OadrPayload();
		OadrSignedObject signedObject = new OadrSignedObject();
		
		
		if(o instanceof OadrDistributeEventType){
			signedObject.setOadrDistributeEvent((OadrDistributeEventType)o);
		}else if(o instanceof OadrRequestEventType){
			signedObject.setOadrRequestEvent((OadrRequestEventType)o);

		}else if(o instanceof OadrCreatedEventType){
			signedObject.setOadrCreatedEvent((OadrCreatedEventType)o);

		}else if(o instanceof OadrResponseType){
			signedObject.setOadrResponse((OadrResponseType)o);

		}else if(o instanceof OadrQueryRegistrationType){
			signedObject.setOadrQueryRegistration((OadrQueryRegistrationType)o);

		}else if(o instanceof OadrCreatedPartyRegistrationType){
			signedObject.setOadrCreatedPartyRegistration((OadrCreatedPartyRegistrationType)o);

		}else if(o instanceof OadrCreatePartyRegistrationType){
			signedObject.setOadrCreatePartyRegistration((OadrCreatePartyRegistrationType)o);

		}else if(o instanceof OadrCancelPartyRegistrationType){
			signedObject.setOadrCancelPartyRegistration((OadrCancelPartyRegistrationType)o);

		}else if(o instanceof OadrCanceledPartyRegistrationType){
			signedObject.setOadrCanceledPartyRegistration((OadrCanceledPartyRegistrationType)o);

		}else if(o instanceof OadrPollType){
			signedObject.setOadrPoll((OadrPollType)o);

		}else if(o instanceof OadrRegisterReportType){
			signedObject.setOadrRegisterReport((OadrRegisterReportType)o);

		}else if(o instanceof OadrRegisteredReportType){
			signedObject.setOadrRegisteredReport((OadrRegisteredReportType)o);

		}else if(o instanceof OadrUpdateReportType){
			signedObject.setOadrUpdateReport((OadrUpdateReportType)o);

		}else if(o instanceof OadrUpdatedReportType){
			signedObject.setOadrUpdatedReport((OadrUpdatedReportType)o);

		}else if(o instanceof OadrCreateReportType){
			signedObject.setOadrCreateReport((OadrCreateReportType)o);

		}else if(o instanceof OadrCreateOptType){
			signedObject.setOadrCreateOpt((OadrCreateOptType)o);

		}else if(o instanceof OadrCreatedOptType){
			signedObject.setOadrCreatedOpt((OadrCreatedOptType)o);

		}else if(o instanceof OadrCancelOptType){
			signedObject.setOadrCancelOpt((OadrCancelOptType)o);

		}else if(o instanceof OadrCanceledOptType){
			signedObject.setOadrCanceledOpt((OadrCanceledOptType)o);

		}else if(o instanceof OadrCreatedReportType){
			signedObject.setOadrCreatedReport((OadrCreatedReportType)o);

		}else if (o instanceof OadrCancelReportType) {
			signedObject.setOadrCancelReport((OadrCancelReportType) o);

		}else if(o instanceof OadrRequestReregistrationType){
			signedObject.setOadrRequestReregistration((OadrRequestReregistrationType)o);

		}else if(o instanceof OadrPayload){
			oadrPayload = (OadrPayload)o;
			signedObject = oadrPayload.getOadrSignedObject();

		}else if(o instanceof OadrSignedObject){
			signedObject = ((OadrSignedObject)o);

		}
	
		
		oadrPayload.setOadrSignedObject(signedObject );
		marshaller.marshal(oadrPayload, System.out);	
		

		
		
	}
	
	public static void printRequest(Object o) throws JAXBException {
		Marshaller marshaller = PayloadHelper.getMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		System.out.println("********* REQUEST PAYLOAD ***********");
		
		marshallToStdOut(o, marshaller);
		System.out.println("\n######################################\n");

	}

}
