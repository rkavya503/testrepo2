package com.honeywell.dras.openadr2b.eipayloads;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.honeywell.dras.openadr2.payloads.b.ObjectFactory;

public class PayloadHelper {
	private static JAXBContext jc = null;

	private static ObjectFactory factory = null;

	private static long UID = 82595127686589L;

	public static JAXBContext getJAXBContext() throws JAXBException {
		if (jc == null) {
			jc = JAXBContext
					.newInstance(
							com.honeywell.dras.openadr2.payloads.b.ObjectFactory.class,
							ietf.params.xml.ns.icalendar_2.ObjectFactory.class,
							ietf.params.xml.ns.icalendar_2_0.stream.ObjectFactory.class,
							net.opengis.gml._3.ObjectFactory.class,
							org.naesb.espi.ObjectFactory.class,
							org.oasis_open.docs.ns.emix._2011._06.ObjectFactory.class,
							org.oasis_open.docs.ns.emix._2011._06.power.ObjectFactory.class,
							org.oasis_open.docs.ns.emix._2011._06.siscale.ObjectFactory.class,
							org.oasis_open.docs.ns.energyinterop._201110.ObjectFactory.class,
							org.oasis_open.docs.ns.energyinterop._201110.payloads.ObjectFactory.class,
							org.w3._2005.atom.ObjectFactory.class,
							un.unece.uncefact.codelist.standard._5.iso42173a._2010_04_07.ObjectFactory.class);
		}
		return jc;
	}

	public static Marshaller getMarshaller() throws JAXBException {
		return getJAXBContext().createMarshaller();

	}

	public static Unmarshaller getUnmarshaller() throws JAXBException {
		return getJAXBContext().createUnmarshaller();
	}

	public static ObjectFactory getObjectFactory() throws JAXBException {
		if (factory == null) {
			factory = new ObjectFactory();
		}
		return factory;
	}

	public static String getUID() {
		return "" + UID++;
	}
}
