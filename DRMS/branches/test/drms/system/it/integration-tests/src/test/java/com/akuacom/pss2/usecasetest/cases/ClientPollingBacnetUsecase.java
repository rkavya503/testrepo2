package com.akuacom.pss2.usecasetest.cases;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.ws.BindingProvider;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import services.web.bacnet.Bacnet;
import services.web.bacnet.BacnetPortType;

import com.akuacom.pss2.usecasetest.tests.ClientPollingState;

public class ClientPollingBacnetUsecase extends AbstractUseCase {
	
	private String clientName;
	private String password;
	
	public ClientPollingBacnetUsecase(String clientName, String password) {
		this.clientName = clientName;
		this.password = password;
	}

	@Override
	public Object runCase() throws Exception {
		ClientPollingState res = new ClientPollingState();
		String endPoint = "http://localhost:8080/DRASClientWS/nossl/bacnet";
		
		try {
         	Authenticator.setDefault(new BacnetSimpleAuthenticator());

         	BacnetPortType service = new Bacnet(new URL(endPoint + "?wsdl"),
        		new QName("urn:bacnet-web-services", "bacnet")).getBacnet();

         	// force the endpoint since what comes back from the server isn't correct
			final BindingProvider bp = (BindingProvider) service;
			bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endPoint);

			// get the EventState
			String value = service.getValue("", "/DRAS/EventState");

            // create the dom
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();

            byte[] bytes = value.getBytes();
            InputStream is = new ByteArrayInputStream(bytes);

            Document document = builder.parse(is);

			// parse xml
	        String eventStatus = "NONE"; // default value
	        String operationModeValue = "NORMAL"; // default value;
	        String eventStateId = "-1"; // default value;
	        String eventIdentifier = "NOT_SET"; // default value;
	        String eventModNumber = "NOT_SET"; // default value;
	        String programName = "NOT_SET"; // default value;

	        Element root = document.getDocumentElement();
	        NodeList children = root.getChildNodes();
	        if(children.getLength() == 0)
	        {
	        	System.out.println("No Event");
	        }
	        else
	        {
		        for(int i = 0; i < children.getLength(); i++)
		        {
		        	Node child = children.item(i);
		        	if(child.getNodeName().equals("Sequence"))
		        	{
		        		NodeList seqChildren = child.getChildNodes();
				        for(int j = 0; j < seqChildren.getLength(); j++)
				        {
							Node sepChild = seqChildren.item(j);

							NamedNodeMap children2 = sepChild.getAttributes();
							if (children2 != null)
								for (int h = 0; h < children2.getLength(); h++) {
									Node listChild = children2.item(h);
									if (listChild == null
											|| listChild.getNodeValue() == null
											|| listChild.getNodeName() == null)
										continue;
									if (listChild.getNodeValue().equals("eventStateID")) {
										eventStateId = children2.item(h + 1).getNodeValue();
										break;
									}
									else if (listChild.getNodeValue().equals("eventIdentifier")) {
										eventIdentifier = children2.item(h + 1).getNodeValue();
										break;
									}
									else if (listChild.getNodeValue().equals("eventModNumber")) {
										eventModNumber = children2.item(h + 1).getNodeValue();
										break;
									}
									else if (listChild.getNodeValue().equals("programName")) {
										programName = children2.item(h + 1).getNodeValue();
										break;
									}
								}

				        	if(sepChild.getNodeName().equals("Sequence"))
		        	        {
                                NodeList seqGrandChildred = sepChild.getChildNodes();
                                for(int k = 0; k < seqGrandChildred.getLength(); k++)
                                {
                                    Node seqGrandChild = seqGrandChildred.item(k);
                                    if(seqGrandChild.getNodeName().equals("Enumerated"))
                                    {
                                        boolean isStatus = false;
                                        boolean isMode = false;
                                        NamedNodeMap attMap = seqGrandChild.getAttributes();
                                        Node node = attMap.getNamedItem("name");
                                        if(node.getTextContent().equals("EventStatus"))
                                        {
                                            isStatus = true;
                                        }
                                        if(node.getTextContent().equals("OperationModeValue"))
                                        {
                                            isMode = true;
                                        }

                                        if(isStatus)
                                        {
                                            eventStatus = attMap.getNamedItem("value").getTextContent();
                                        }
                                        if(isMode)
                                        {
                                            operationModeValue = attMap.getNamedItem("value").getTextContent();
                                        }
                                    }
                                }
                            }
				        }
		        	}

                }
	        }
            // do something with the result. here we just print the two values.
	        // in a real client, this is where relays would be controlled or
	        // a signal of some sort would be sent the the EMCS controller.
//            System.out.println("EventStatus = " + eventStatus);
//            System.out.println("OperationModeValue = " + operationModeValue);
//            System.out.println("EventStateId = " + eventStateId);
//            System.out.println("EventIdentifier = " + eventIdentifier);
//            System.out.println("EventModNumber = " + eventModNumber);
//            System.out.println("ProgramName = " + programName);
            
            res.setEventStatus(eventStatus);
            res.setOperationMode(operationModeValue);
            

            // send the EventStateConfirmation
			// the data here is dummied - it should really be copied out of the
			// EventState
			StringBuffer sb = new StringBuffer();
			sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			sb.append("<CSML xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"");
			sb.append(" xsi:noNamespaceSchemaLocation=\"file:DR-034A-17 Schema.xsd\">");
			sb.append("<Sequence name=\"eventStateConfirmation\"");
			sb.append(" type=\"0-DRAS-EventStateConfirmation-2008-1\">");
			sb.append("<String name=\"programName\" value=\"").append(programName).append("\" />");
			sb.append("<Unsigned name=\"eventModNumber\" value=\"").append(eventModNumber).append("\" />");
			sb.append("<String name=\"eventIdentifier\" value=\"").append(eventIdentifier).append("\" />");
			sb.append("<String name=\"drasClientID\" value=\"").append(clientName).append("\" />");
			sb.append("<Unsigned name=\"eventStateID\" value=\"").append(eventStateId).append("\" />");
			sb.append("<String name=\"schemaVersion\" value=\"0.9\" />");
			sb.append("<Enumerated name=\"operationModeValue\" value=\"").append(operationModeValue).append("\" />");
			sb.append("<Boolean name=\"optInStatus\" value=\"true\" />");
			sb.append("<Real name=\"currentTime\" value=\"31.0\" />");
			sb.append("</Sequence>");
			sb.append("</CSML>");
			String rv = service.setValue("", "/DRAS/EventStateConfirmation", sb.toString());
			
			res.setConfirmationStatus(rv);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
		
		return res;
	}
	
    class BacnetSimpleAuthenticator extends Authenticator {
		public PasswordAuthentication getPasswordAuthentication()
		{
			return new PasswordAuthentication(clientName, password.toCharArray());
		}
	}
}
