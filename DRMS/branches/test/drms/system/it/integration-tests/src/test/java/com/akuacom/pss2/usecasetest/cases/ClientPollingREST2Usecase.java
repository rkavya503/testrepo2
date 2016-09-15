package com.akuacom.pss2.usecasetest.cases;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import com.akuacom.pss2.usecasetest.tests.ClientPollingState;

public class ClientPollingREST2Usecase extends AbstractUseCase {
	
	private String clientName;
	private String password;
	
	public ClientPollingREST2Usecase(String clientName, String password) {
		this.clientName = clientName;
		this.password = password;
	}

	@Override
	public Object runCase() throws Exception {
		ClientPollingState res = new ClientPollingState();
		String endPoint = "http://localhost:8080/RestClientWS/nossl/rest2";
		String confirmEndPoint = "http://localhost:8080/RestClientWS/nossl/restConfirm";
		
		try {
			URLConnection connection = new URL(endPoint).openConnection();
			
			String drasClientID = clientName;
	        String plain = drasClientID + ":" + password;
	        String enocoded = new sun.misc.BASE64Encoder().encode(plain.getBytes());
	        connection.setRequestProperty("Authorization", "Basic " + enocoded);

	        // For clarity, in this example we are going to use a DOM parser to
			// process the XML. A production system should use another method
			// like a SAX parser.
	        // create the dom
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
	        Document document = builder.parse(connection.getInputStream()); 
	        
			// print the xml for debug purposes
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			DOMSource source = new DOMSource(document);
			StringWriter sw = new StringWriter();
			StreamResult result = new StreamResult(sw);
			transformer.transform(source, result);			
			//System.out.println(sw.toString());

			// parse xml 
	        String eventStatus = "NONE"; // default value
	        String operationModeValue = "NOT_SET"; // default value;
	        String eventStateId = "-1"; // default value;
	        String eventIdentifier = "NOT_SET"; // default value;
	        String eventModNumber = "NOT_SET"; // default value;
	        String programName = "NOT_SET"; // default value;
	        
	        Element root = document.getDocumentElement();
	        NodeList eventStateChildren = root.getChildNodes();
	        if(eventStateChildren.getLength() > 0)
	        {
		        for(int h = 0; h < eventStateChildren.getLength(); h++)
				{
					Node listChild = eventStateChildren.item(h);
					if(listChild.getNodeName().equals("p:eventStates"))
					{
						// get needed attributes
						NamedNodeMap children = listChild.getAttributes();
						for (int x = 0; x < children.getLength(); x++) {
							Node listChildAttribute = children.item(x);
							if (listChildAttribute.getNodeName().equals("eventStateID")) {
								eventStateId = listChildAttribute.getNodeValue();
							}else if (listChildAttribute.getNodeName().equals("eventIdentifier")) {
								eventIdentifier = listChildAttribute.getNodeValue();
							} else if (listChildAttribute.getNodeName().equals("eventModNumber")) {
								eventModNumber = listChildAttribute.getNodeValue();
							} else if (listChildAttribute.getNodeName().equals("programName")) {
								programName = listChildAttribute.getNodeValue();
							}
						}
						
						for(int i = 0; i < listChild.getChildNodes().getLength(); i++)
						{
							Node eventStateChild = listChild.getChildNodes().item(i);
							if (eventStateChild.getNodeName().equals("p:simpleDRModeData"))
							{
								NodeList simpleChildren =
									eventStateChild.getChildNodes();
								for(int j = 0; j < simpleChildren.getLength(); j++)
								{
									Node simpleChild = simpleChildren.item(j);
									if(simpleChild instanceof Element)
									{
										Element childElement =
											(Element) simpleChild;
										if(childElement.getFirstChild() instanceof Text)
										{
											Text textNode =
												(Text) childElement
													.getFirstChild();
											String text =
												textNode.getData().trim();
											String tagName =
												childElement.getTagName();
											if(tagName.equals("p:EventStatus"))
											{
												eventStatus = text;
											}
											else if(tagName
												.equals("p:OperationModeValue"))
											{
												operationModeValue = text;
											}
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
            //System.out.println("EventStatus = " + eventStatus);
            //System.out.println("OperationModeValue = " + operationModeValue);
	        res.setEventStatus(eventStatus);
	        res.setOperationMode(operationModeValue);
            
			// send the EventStateConfirmation
			// Data must match the data the DRAS sent in the EventState
			// EventState            
			connection = new URL(confirmEndPoint).openConnection();
	        connection.setRequestProperty("Authorization", "Basic " + enocoded);
	        connection.setDoOutput(true);
			StringBuffer sb = new StringBuffer();
			
			sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			sb.append(" <p:eventStateConfirmation currentTime=\"31.0\" drasClientID=\"").append(drasClientID).append("\"");
			sb.append(" eventIdentifier=\"").append(eventIdentifier);
			sb.append("\" eventModNumber=\"").append(eventModNumber);
			sb.append("\" eventStateID=\"").append(eventStateId).append("\"");
			sb.append(" operationModeValue=\"").append(operationModeValue);
			sb.append("\" optInStatus=\"true\" programName=\"").append(programName).append("\"");
			sb.append(" schemaVersion=\"\" xmlns:p=\"http://www.openadr.org/DRAS/EventState\"");
			sb.append(" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"");
			sb.append(" xsi:schemaLocation=\"http://openadr.lbl.gov/src/1/EventState.xsd\">");
			sb.append("</p:eventStateConfirmation>");
			connection.getOutputStream().write(sb.toString().getBytes());
			String rv =  new BufferedReader(new InputStreamReader(connection.getInputStream())).readLine();
			res.setConfirmationStatus(rv);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return res;
	}
}
