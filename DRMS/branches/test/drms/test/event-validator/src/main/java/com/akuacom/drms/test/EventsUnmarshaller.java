package com.akuacom.drms.test;

import java.io.InputStream;
import java.net.URL;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

import com.akuacom.pss2.event.validator.Events;



/**
 * Unmarshalls Events from xml to JAXB object
 * @author Sunil
 *
 */
public class EventsUnmarshaller {

	/**
	 * Singletom Instance
	 */
	private static EventsUnmarshaller instance;
	
	/**
	 * Schema file
	 */
	private static final String SCHEMA_RESOURCE = "event-validator.xsd";
	
	/**
	 * Schema
	 */
	private Schema schema = null;
	
	/**
	 * JAXB Context
	 */
	private JAXBContext jaxbContext = null;

	
	public EventsUnmarshaller() throws JAXBException, SAXException{
		SchemaFactory schemaFactory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
		URL schemaUrl = Thread.currentThread().getContextClassLoader().getResource(SCHEMA_RESOURCE);
		schema = schemaFactory.newSchema(schemaUrl);
		this.jaxbContext = JAXBContext.newInstance(Events.class);		
	}
	/**
	 * Unmarshals events xml file to equivalent JAXB object
	 * @param xmlFile
	 * @return
	 */
	public Events unmarshal(URL xmlFile) throws JAXBException{
		return (Events)createUnmarshaller().unmarshal(xmlFile);
	}

	/**
	 * Unmarshal from an input stream
	 * @param is input stream
	 * @return Events
	 * @throws JAXBException Failure
	 */
	public Events unmarshal(InputStream is) throws JAXBException{
		return (Events)createUnmarshaller().unmarshal(is);
	}
	
	/**
	 * Get a singletom instance of unmarshaller
	 * @return EventsUnmarshaller
	 */
	public static EventsUnmarshaller getInstance() throws JAXBException, SAXException{
		if(instance == null){
			instance = new EventsUnmarshaller();
		}
		return instance;
	}

	/**
	 * Creates a unmarshaller
	 * @return Unmarshaller
	 * @throws JAXBException Failure
	 */
    private Unmarshaller createUnmarshaller() throws JAXBException {
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        unmarshaller.setSchema(schema);
        return unmarshaller;
    }
	
	
}
