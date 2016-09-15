package com.honeywell.dras.vtn.util;

import com.honeywell.dras.openadr2.payloads.b.ObjectFactory;

public class FactoryHelper {
	
	public static com.honeywell.dras.openadr2.payloads.b.ObjectFactory getOadrObjectFactory(){
		ObjectFactory of = new ObjectFactory();
		return of;
	}
	
	public static org.oasis_open.docs.ns.energyinterop._201110.ObjectFactory getEIObjectFactory(){
		org.oasis_open.docs.ns.energyinterop._201110.ObjectFactory of = new org.oasis_open.docs.ns.energyinterop._201110.ObjectFactory();
		return of;
	}
	
	public static org.oasis_open.docs.ns.energyinterop._201110.payloads.ObjectFactory getEIPayloadsObjectFactory(){
		org.oasis_open.docs.ns.energyinterop._201110.payloads.ObjectFactory of = new org.oasis_open.docs.ns.energyinterop._201110.payloads.ObjectFactory();
		return of;
	}

	public static ietf.params.xml.ns.icalendar_2.ObjectFactory getCalObjectFactory(){
		ietf.params.xml.ns.icalendar_2.ObjectFactory of = new ietf.params.xml.ns.icalendar_2.ObjectFactory();
		return of;
	}
	
	public static ietf.params.xml.ns.icalendar_2_0.stream.ObjectFactory getCalStreamObjectFactory(){
		ietf.params.xml.ns.icalendar_2_0.stream.ObjectFactory of = new ietf.params.xml.ns.icalendar_2_0.stream.ObjectFactory();
		return of;
	}
	
	public static org.oasis_open.docs.ns.emix._2011._06.power.ObjectFactory getEmixPowerObjectFactory(){
		org.oasis_open.docs.ns.emix._2011._06.power.ObjectFactory of = new org.oasis_open.docs.ns.emix._2011._06.power.ObjectFactory();
		return of;
	}
	
	public static org.oasis_open.docs.ns.emix._2011._06.ObjectFactory getEmixObjectFactory(){
		org.oasis_open.docs.ns.emix._2011._06.ObjectFactory of = new org.oasis_open.docs.ns.emix._2011._06.ObjectFactory();
		return of;
	}
	
	public static org.w3._2005.atom.ObjectFactory getAtomObjectFactory(){
		org.w3._2005.atom.ObjectFactory of = new org.w3._2005.atom.ObjectFactory();
		return of;
	}
	
}



