package com.akuacom.drms.pdp.eventstate;

import java.net.URL;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import org.openadr.dras.pdp.eventstate.EventInfoInstance;
import org.openadr.dras.pdp.eventstate.EventState;
import org.openadr.dras.pdp.eventstate.ListOfEventState;
import org.openadr.dras.pdp.eventstate.SimpleClientEventData;
import org.openadr.dras.pdp.eventstate.SmartClientDREventData;

public class PdpEventStateUnmarshallerTest {

	/**
	 * Tests PDP Event State Unmarshaller AutoPdpEven126
	 * @throws Exception Failure
	 */
	@Test public void testUnmarshalAutoPdpEven126() throws Exception {
		URL eventsFile = Thread.currentThread().getContextClassLoader().getResource("pdp_auto_even_12_6.xml");
		ListOfEventState es = PdpEventStateUnmarshaller.getInstance().unmarshal(eventsFile);
		Assert.assertNotNull(es);
		List<EventState> eventList = es.getEventStates();
		Assert.assertNotNull(eventList);
		Assert.assertTrue(eventList.size() == 1);
		EventState event1 = eventList.get(0);
		Assert.assertNotNull(event1);
		Assert.assertEquals(event1.getProgramName(), "PDP - Auto PDP Even 12-6");
		Assert.assertEquals(event1.getEventIdentifier(), "100830-160614");
		SimpleClientEventData s = event1.getSimpleDRModeData();
		Assert.assertNotNull(s);
		Assert.assertEquals(s.getEventStatus(), "FAR");
		Assert.assertEquals(s.getOperationModeValue(), "NORMAL");
		SmartClientDREventData d = event1.getDrEventData();
		Assert.assertNotNull(d);
		Assert.assertNotNull(d.getStartTime());
		List<EventInfoInstance> eList = d.getEventInfoInstances();
		Assert.assertTrue(eList.size() == 1);
		EventInfoInstance e = eList.get(0);
		Assert.assertEquals(e.getEventInfoTypeID().getValue(), "PRICE_RELATIVE");
		Assert.assertEquals(e.getEventInfoName(), "cpp_price");
	}
	

	/**
	 * Tests PDP Event State Unmarshaller AutoPdpEven26
	 * @throws Exception Failure
	 */
	@Test public void testUnmarshalAutoPdpEven26() throws Exception {
		URL eventsFile = Thread.currentThread().getContextClassLoader().getResource("pdp_auto_even_2_6.xml");
		ListOfEventState es = PdpEventStateUnmarshaller.getInstance().unmarshal(eventsFile);
		Assert.assertNotNull(es);
		List<EventState> eventList = es.getEventStates();
		Assert.assertNotNull(eventList);
		Assert.assertTrue(eventList.size() == 1);
		EventState event1 = eventList.get(0);
		Assert.assertNotNull(event1);
		Assert.assertEquals(event1.getProgramName(), "PDP - Auto PDP Even 2-6");
		Assert.assertEquals(event1.getEventIdentifier(), "100830-161854");
		SimpleClientEventData s = event1.getSimpleDRModeData();
		Assert.assertNotNull(s);
		Assert.assertEquals(s.getEventStatus(), "FAR");
		Assert.assertEquals(s.getOperationModeValue(), "NORMAL");
		SmartClientDREventData d = event1.getDrEventData();
		Assert.assertNotNull(d);
		Assert.assertNotNull(d.getStartTime());
		List<EventInfoInstance> eList = d.getEventInfoInstances();
		Assert.assertTrue(eList.size() == 1);
		EventInfoInstance e = eList.get(0);
		Assert.assertEquals(e.getEventInfoTypeID().getValue(), "PRICE_RELATIVE");
		Assert.assertEquals(e.getEventInfoName(), "cpp_price");
	}


	
	/**
	 * Tests PDP Event State Unmarshaller AutoPdpOdd126
	 * @throws Exception Failure
	 */
	@Test public void testUnmarshalAutoPdpOdd126() throws Exception {
		URL eventsFile = Thread.currentThread().getContextClassLoader().getResource("pdp_auto_odd_12_6.xml");
		ListOfEventState es = PdpEventStateUnmarshaller.getInstance().unmarshal(eventsFile);
		Assert.assertNotNull(es);
		List<EventState> eventList = es.getEventStates();
		Assert.assertNotNull(eventList);
		Assert.assertTrue(eventList.size() == 1);
		EventState event1 = eventList.get(0);
		Assert.assertNotNull(event1);
		Assert.assertEquals(event1.getProgramName(), "PDP - Auto PDP Odd 12-6");
		Assert.assertEquals(event1.getEventIdentifier(), "100830-161206");
		SimpleClientEventData s = event1.getSimpleDRModeData();
		Assert.assertNotNull(s);
		Assert.assertEquals(s.getEventStatus(), "FAR");
		Assert.assertEquals(s.getOperationModeValue(), "NORMAL");
		SmartClientDREventData d = event1.getDrEventData();
		Assert.assertNotNull(d);
		Assert.assertNotNull(d.getStartTime());
		List<EventInfoInstance> eList = d.getEventInfoInstances();
		Assert.assertTrue(eList.size() == 1);
		EventInfoInstance e = eList.get(0);
		Assert.assertEquals(e.getEventInfoTypeID().getValue(), "PRICE_RELATIVE");
		Assert.assertEquals(e.getEventInfoName(), "cpp_price");
	}

	

	/**
	 * Tests PDP Event State Unmarshaller AutoPdpOdd26
	 * @throws Exception Failure
	 */
	@Test public void testUnmarshalAutoPdpOdd26() throws Exception {
		URL eventsFile = Thread.currentThread().getContextClassLoader().getResource("pdp_auto_odd_2_6.xml");
		ListOfEventState es = PdpEventStateUnmarshaller.getInstance().unmarshal(eventsFile);
		Assert.assertNotNull(es);
		List<EventState> eventList = es.getEventStates();
		Assert.assertNotNull(eventList);
		Assert.assertTrue(eventList.size() == 1);
		EventState event1 = eventList.get(0);
		Assert.assertNotNull(event1);
		Assert.assertEquals(event1.getProgramName(), "PDP - Auto PDP Odd 2-6");
		Assert.assertEquals(event1.getEventIdentifier(), "100830-160928");
		SimpleClientEventData s = event1.getSimpleDRModeData();
		Assert.assertNotNull(s);
		Assert.assertEquals(s.getEventStatus(), "FAR");
		Assert.assertEquals(s.getOperationModeValue(), "NORMAL");
		SmartClientDREventData d = event1.getDrEventData();
		Assert.assertNotNull(d);
		Assert.assertNotNull(d.getStartTime());
		List<EventInfoInstance> eList = d.getEventInfoInstances();
		Assert.assertTrue(eList.size() == 1);
		EventInfoInstance e = eList.get(0);
		Assert.assertEquals(e.getEventInfoTypeID().getValue(), "PRICE_RELATIVE");
		Assert.assertEquals(e.getEventInfoName(), "cpp_price");
	}


	/**
	 * Tests PDP Event State Unmarshaller AutoPdpUnl126
	 * @throws Exception Failure
	 */
	@Test public void testUnmarshalAutoPdpUnl126() throws Exception {
		URL eventsFile = Thread.currentThread().getContextClassLoader().getResource("pdp_auto_unlimited_12_6.xml");
		ListOfEventState es = PdpEventStateUnmarshaller.getInstance().unmarshal(eventsFile);
		Assert.assertNotNull(es);
		List<EventState> eventList = es.getEventStates();
		Assert.assertNotNull(eventList);
		Assert.assertTrue(eventList.size() == 1);
		EventState event1 = eventList.get(0);
		Assert.assertNotNull(event1);
		Assert.assertEquals(event1.getProgramName(), "PDP - Auto PDP Unlimited 12-6");
		Assert.assertEquals(event1.getEventIdentifier(), "100830-160218");
		SimpleClientEventData s = event1.getSimpleDRModeData();
		Assert.assertNotNull(s);
		Assert.assertEquals(s.getEventStatus(), "FAR");
		Assert.assertEquals(s.getOperationModeValue(), "NORMAL");
		SmartClientDREventData d = event1.getDrEventData();
		Assert.assertNotNull(d);
		Assert.assertNotNull(d.getStartTime());
		List<EventInfoInstance> eList = d.getEventInfoInstances();
		Assert.assertTrue(eList.size() == 1);
		EventInfoInstance e = eList.get(0);
		Assert.assertEquals(e.getEventInfoTypeID().getValue(), "PRICE_RELATIVE");
		Assert.assertEquals(e.getEventInfoName(), "cpp_price");
	}
	

	
	/**
	 * Tests PDP Event State Unmarshaller AutoPdpUnl26
	 * @throws Exception Failure
	 */
	@Test public void testUnmarshalAutoPdpUnl26() throws Exception {
		URL eventsFile = Thread.currentThread().getContextClassLoader().getResource("pdp_auto_unlimited_2_6.xml");
		ListOfEventState es = PdpEventStateUnmarshaller.getInstance().unmarshal(eventsFile);
		Assert.assertNotNull(es);
		List<EventState> eventList = es.getEventStates();
		Assert.assertNotNull(eventList);
		Assert.assertTrue(eventList.size() == 1);
		EventState event1 = eventList.get(0);
		Assert.assertNotNull(event1);
		Assert.assertEquals(event1.getProgramName(), "PDP - Auto PDP Unlimited 2-6");
		Assert.assertEquals(event1.getEventIdentifier(), "100830-155401");
		SimpleClientEventData s = event1.getSimpleDRModeData();
		Assert.assertNotNull(s);
		Assert.assertEquals(s.getEventStatus(), "FAR");
		Assert.assertEquals(s.getOperationModeValue(), "NORMAL");
		SmartClientDREventData d = event1.getDrEventData();
		Assert.assertNotNull(d);
		Assert.assertNotNull(d.getStartTime());
		List<EventInfoInstance> eList = d.getEventInfoInstances();
		Assert.assertTrue(eList.size() == 1);
		EventInfoInstance e = eList.get(0);
		Assert.assertEquals(e.getEventInfoTypeID().getValue(), "PRICE_RELATIVE");
		Assert.assertEquals(e.getEventInfoName(), "cpp_price");
	}
	
	
	
	
}
