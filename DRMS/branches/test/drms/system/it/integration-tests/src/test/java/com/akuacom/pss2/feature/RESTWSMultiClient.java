package com.akuacom.pss2.feature;

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
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

public class RESTWSMultiClient extends Thread
{
    String clientName;
    public RESTWSMultiClient(String name)
    {
        clientName = name;
    }

    public void run()
    {

        try
		{
			String endPoint = "http://localhost:8080/RestClientWS/nossl/rest";

            while(FeatureFixture.POLLING )
            {
            URLConnection connection = new URL(endPoint).openConnection();

	        String plain = clientName + ":Test_1234";
	        String enocoded = new sun.misc.BASE64Encoder().encode(plain.getBytes());
	        connection.setRequestProperty("Authorization", "Basic " + enocoded);


            // create the dom
			DocumentBuilder builder =
	        	DocumentBuilderFactory.newInstance().newDocumentBuilder();
	        Document document = builder.parse(connection.getInputStream());

			// print the xml for debug purposes
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			DOMSource source = new DOMSource(document);
			StringWriter sw = new StringWriter();
			StreamResult result = new StreamResult(sw);
			transformer.transform(source, result);
			System.out.println(sw.toString());

			// parse xml
	        String eventStatus = "NONE"; // default value
	        String operationModeValue = "NORMAL"; // default value;
	        Element root = document.getDocumentElement();
	        NodeList eventStateChildren = root.getChildNodes();
	        if(eventStateChildren.getLength() == 0)
	        {
	        	System.out.println("No Event");
	        }
	        else
	        {
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
	        Thread.currentThread().sleep(60000);
            }
            
        }
		catch (Exception e)
		{
			e.printStackTrace();
		}
    }

    public static void main(String[] args)
    {
    	

    }
}