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

public class ClientPollingRESTUsecase extends AbstractUseCase {
	
	private String clientName;
	private String password;
	
	public ClientPollingRESTUsecase(String clientName, String password) {
		this.clientName = clientName;
		this.password = password;
	}

	@Override
	public Object runCase() throws Exception {
		ClientPollingState res = new ClientPollingState();
		String endPoint = "http://localhost:8080/RestClientWS/nossl/rest";
		
		try
		{
			URLConnection connection = new URL(endPoint).openConnection();
			
			String drasClientID = clientName;
			String plain = drasClientID + ":" + password;
	        String enocoded = new sun.misc.BASE64Encoder().encode(plain.getBytes());
	        connection.setRequestProperty("Authorization", "Basic " + enocoded);

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

			// parse xml 
	        String eventStatus = "NONE"; // default value
	        String operationModeValue = "NORMAL"; // default value;
	        String eventStateId = "-1"; // default value;
	        String eventIdentifier = "NOT_SET"; // default value;
	        String eventModNumber = "NOT_SET"; // default value;
	        String programName = "NOT_SET"; // default value;

	        Element root = document.getDocumentElement();
	        NodeList eventStateChildren = root.getChildNodes();
	        if(eventStateChildren.getLength() > 0)
	        {
				NamedNodeMap children = root.getAttributes();
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
				
		        for(int i = 0; i < eventStateChildren.getLength(); i++)
		        {
		        	Node eventStateChild = eventStateChildren.item(i);
		        	if(eventStateChild.getNodeName().equals("p:simpleDRModeData"))
		        	{
		        		NodeList simpleChildren = eventStateChild.getChildNodes();
				        for(int j = 0; j < simpleChildren.getLength(); j++)
				        {
				        	Node simpleChild = simpleChildren.item(j);        		
				        	if(simpleChild instanceof Element)
				        	{
				        		Element childElement = (Element)simpleChild;
				        		if(childElement.getFirstChild() instanceof Text)
				        		{
					        		Text textNode = (Text)childElement.getFirstChild();
					        		String text = textNode.getData().trim();
					        		String tagName = childElement.getTagName();
					        		if(tagName.equals("p:EventStatus"))
					        		{
					        			eventStatus = text;
					        		} 
					        		else if(tagName.equals("p:OperationModeValue"))
					        		{
					        			operationModeValue = text;
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
	        res.setEventStatus(eventStatus);
	        res.setOperationMode(operationModeValue);
            
			// send the EventStateConfirmation
			// the data here is dummied - it should really be copied out of the 
			// EventState
            
			endPoint = endPoint + "Confirm";
			connection = new URL(endPoint).openConnection();
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
