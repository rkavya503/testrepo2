package com.akuacom.pss2.grippoint.wsclient;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XPathQueryUtil {
	
	private static XPathFactory factory = XPathFactory.newInstance();
	private static XPath xpath = factory.newXPath();
	
	public static NodeList parse2Nodes(String XPathExpression, String context) throws Exception{
		XPathExpression expr = xpath.compile(XPathExpression);
		Document doc = XPathQueryUtil.getDocument(context);
		Object result = expr.evaluate(doc, XPathConstants.NODESET);
		
		return (NodeList) result;
	}

	public static Document getDocument() {
		InputStream stream= null;
		try {
			stream= XPathQueryUtil.class.getResourceAsStream("empty.xml");
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(stream);
			return doc;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}finally{
			try {
				stream.close();
			} catch (IOException e) {
			}
		}
	}
	
	public static Document getDocumentByName(String xmlFile) {
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(xmlFile);
			return doc;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Document getDocument(String xmlContext) {
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(new ByteArrayInputStream(xmlContext.getBytes()));
			return doc;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static String getAttribute(Node node, String att) throws Exception {
		if (node instanceof Element) {
			Element element = (Element) node;
			Node n = element.getElementsByTagName(att).item(0);
			if (n != null)
				return n.getTextContent();
		}
		return null;
	}
	
}
