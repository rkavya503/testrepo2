package com.akuacom.pss2.weatherInfo;

import java.io.InputStream;
import java.util.HashMap;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class WeatherXMLReaderDomImpl implements WeatherXMLReader{

	public WeatherXMLReaderDomImpl() {
	}


	public  void parserNDFDgenResponse(HashMap<String, Object> map, InputStream is) {

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = null;
		Document document = null;
		try {

			db = dbf.newDocumentBuilder();
			document = db.parse(is);
			
			Element root = document.getDocumentElement();
			NodeList nodes = root.getChildNodes();
			Node param = null;
			for (int i = 0; i < nodes.getLength(); i++) {
				Node node = nodes.item(i);
				if (node.getNodeType() == 1 && node.getNodeName().equals("data")) {
					//System.out.println(node.getNodeName());

					NodeList list = node.getChildNodes();

					for (int j = 0; j < list.getLength(); j++) {
						if (list.item(j).getNodeType() == 1 && list.item(j).getNodeName().equals("parameters")) {
							param = list.item(j);
							break;
						}
					}

				}

			}

			NodeList values = param.getChildNodes();

			for (int i = 0; i < values.getLength(); i++) {
				if (values.item(i).getNodeName().equals("conditions-icon")) {
					map.put("conditions-icon", (values.item(i).getChildNodes().item(3)).getTextContent());
					break;
				}
			}

			for (int i = 0; i < values.getLength(); i++) {
				if (values.item(i).getNodeName().equals("temperature") && values.item(i).getAttributes().getNamedItem("type").getNodeValue().equals("apparent")) {
					map.put("apparent", values.item(i).getChildNodes().item(3).getTextContent());

					break;
				}
			}

			for (int i = 0; i < values.getLength(); i++) {
				if (values.item(i).getNodeName().equals("direction") && values.item(i).getAttributes().getNamedItem("type").getNodeValue().equals("wind")) {
					map.put("wind", values.item(i).getChildNodes().item(3).getTextContent());
					break;
				}
			}

			for (int i = 0; i < values.getLength(); i++) {
				if (values.item(i).getNodeName().equals("humidity") && values.item(i).getAttributes().getNamedItem("type").getNodeValue().equals("relative")) {
					map.put("humidity", values.item(i).getChildNodes().item(3).getTextContent());
					break;
				}
			}

		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		} catch (ParserConfigurationException e) {
			System.out.println(e.getMessage());
		} catch (SAXException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		} finally {
			document = null;
		}
	}
	
	public  void parserNDFDgenByDayResponse(HashMap<String, Object> map, InputStream is) {

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = null;
		Document document = null;
		try {

			db = dbf.newDocumentBuilder();
			document = db.parse(is);//db.parse(fileName);
			
			Element root = document.getDocumentElement();
			NodeList nodes = root.getChildNodes();
			Node param = null;
			for (int i = 0; i < nodes.getLength(); i++) {
				Node node = nodes.item(i);
				// ELEMENT_NODE
				if (node.getNodeType() == 1 && node.getNodeName().equals("data")) {

					NodeList list = node.getChildNodes();

					for (int j = 0; j < list.getLength(); j++) {
						if (list.item(j).getNodeType() == 1 && list.item(j).getNodeName().equals("parameters")) {
							param = list.item(j);
							break;
						}
					}

				}

			}

			NodeList values = param.getChildNodes();

			for (int i = 0; i < values.getLength(); i++) {
				if (values.item(i).getNodeName().equals("conditions-icon")) {
					NodeList children = values.item(i).getChildNodes();
					List<String> icons = new ArrayList<String>();
					for (int j = 0; j < children.getLength(); j++) {
						Node child = children.item(j);
						if(1==child.getNodeType()&&"icon-link".equals(child.getNodeName())){
							icons.add(child.getTextContent());
						}
					}
					
					map.put("conditions-icon", icons);
					break;
				}
			}

			for (int i = 0; i < values.getLength(); i++) {
				if (values.item(i).getNodeName().equals("temperature") && values.item(i).getAttributes().getNamedItem("type").getNodeValue().equals("maximum")) {
					NodeList children = values.item(i).getChildNodes();
					
					List<String> maxs = new ArrayList<String>();
					for (int j = 0; j < children.getLength(); j++) {
						Node child = children.item(j);
						if(1==child.getNodeType()&&"value".equals(child.getNodeName())){
							maxs.add(child.getTextContent());
						}
					}
					
					map.put("maximum", maxs);
					
					break;
				}
			}
			for (int i = 0; i < values.getLength(); i++) {
				if (values.item(i).getNodeName().equals("temperature") && values.item(i).getAttributes().getNamedItem("type").getNodeValue().equals("minimum")) {
					NodeList children = values.item(i).getChildNodes();
					List<String> minx = new ArrayList<String>();
					for (int j = 0; j < children.getLength(); j++) {
						Node child = children.item(j);
						if(1==child.getNodeType()&&"value".equals(child.getNodeName())){
							minx.add(child.getTextContent());
						}
					}
					map.put("minimum", minx);

					break;
				}
			}
			
			
			for (int i = 0; i < values.getLength(); i++) {
				if (values.item(i).getNodeName().equals("weather")) {
					NodeList children = values.item(i).getChildNodes();
					List<String> minx = new ArrayList<String>();
					for (int j = 0; j < children.getLength(); j++) {
						Node child = children.item(j);
						if(1==child.getNodeType()&&"weather-conditions".equals(child.getNodeName())){
							minx.add(child.getAttributes().getNamedItem("weather-summary").getNodeValue());
						}
					}
					map.put("weather-conditions", minx);

					break;
				}
			}
			
			for (int i = 0; i < values.getLength(); i++) {
				if (values.item(i).getNodeName().equals("direction") && values.item(i).getAttributes().getNamedItem("type").getNodeValue().equals("wind")) {
					map.put("wind", values.item(i).getChildNodes().item(3).getTextContent());
					break;
				}
			}


		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		} catch (ParserConfigurationException e) {
			System.out.println(e.getMessage());
		} catch (SAXException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		} finally {
			document = null;
		}
	}

	public  void parserLatLonListZipCodeResponse(HashMap<String, String> map, InputStream is) {

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = null;
		Document document = null;
		try {

			db = dbf.newDocumentBuilder();
			document = db.parse(is);//db.parse(fileName);
			
			Element root = document.getDocumentElement();
			NodeList nodes = root.getChildNodes();
			for (int i = 0; i < nodes.getLength(); i++) {
				Node node = nodes.item(i);
				if (node.getNodeType() == 1 && node.getNodeName().equals("latLonList")) {
					String value = node.getTextContent();
					String [] values = value.split(",");
					
					map.put("latitude", values[0]);
					map.put("longitude", values[1]);

				}

			}

		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		} catch (ParserConfigurationException e) {
			System.out.println(e.getMessage());
		} catch (SAXException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		} finally {
			document = null;
		}
	}
	
}
