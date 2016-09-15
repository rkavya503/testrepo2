package com.akuacom.drms.pdp.eventstate;

import java.io.InputStream;
import java.net.URL;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

import org.openadr.dras.pdp.eventstate.ListOfEventState;

/**
 * Unmarshalls Events from xml to JAXB object
 * @author Sunil
 *
 */




/**
 * Unmarshalls Events from xml to JAXB object
 * @author Sunil
 *
 */
public class PdpEventStateUnmarshaller {

	/**
	 * Singletom Instance
	 */
	private static PdpEventStateUnmarshaller instance;
	
	/**
	 * Schema file
	 */
	private static final String SCHEMA_RESOURCE = "pdp-event-state.xsd";
	
	/**
	 * Schema
	 */
	private Schema schema = null;
	
	/**
	 * JAXB Context
	 */
	private JAXBContext jaxbContext = null;

	
	public PdpEventStateUnmarshaller() throws JAXBException, SAXException{
		SchemaFactory schemaFactory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
		URL schemaUrl = Thread.currentThread().getContextClassLoader().getResource(SCHEMA_RESOURCE);
		schema = schemaFactory.newSchema(schemaUrl);
		this.jaxbContext = JAXBContext.newInstance(ListOfEventState.class);		
	}
	/**
	 * Unmarshals events xml file to equivalent JAXB object
	 * @param xmlFile
	 * @return
	 */
	public ListOfEventState unmarshal(URL xmlFile) throws JAXBException{
		return (ListOfEventState)createUnmarshaller().unmarshal(xmlFile);
	}

	/**
	 * Unmarshal from an input stream
	 * @param is input stream
	 * @return ListOfEventState
	 * @throws JAXBException Failure
	 */
	public ListOfEventState unmarshal(InputStream is) throws JAXBException{
		return (ListOfEventState)createUnmarshaller().unmarshal(is);
	}
	
	/**
	 * Get a singletom instance of unmarshaller
	 * @return PdpEventStateUnmarshaller
	 */
	public static PdpEventStateUnmarshaller getInstance() throws JAXBException, SAXException{
		if(instance == null){
			instance = new PdpEventStateUnmarshaller();
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
