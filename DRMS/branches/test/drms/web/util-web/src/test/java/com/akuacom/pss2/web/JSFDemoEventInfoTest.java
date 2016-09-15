
package com.akuacom.pss2.web;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.Date;

import org.junit.Test;

import com.akuacom.pss2.web.event.JSFDemoEventInfo;

public class JSFDemoEventInfoTest {
	
	@Test
	public void testConstruction(){
		JSFDemoEventInfo evtinfo 
			= new JSFDemoEventInfo(new Date(),3,"Active(Start)", 
					Arrays.asList("bid","mode","price"));
	
		//verify default values
		assertEquals(0,(int)evtinfo.getSignalValue("bid"));
		assertEquals(1.0,evtinfo.getSignalValue("mode"),0.00001);
		assertEquals(0.0,evtinfo.getSignalValue("price"),0.00001);
		
		//verify setters and getters
		evtinfo.setSignalValue("bid", 2.0);
		assertEquals(2,(int)evtinfo.getSignalValue("bid"));
	}
	
	@Test
	public void testNextEvent(){
		JSFDemoEventInfo evtinfo 
			= new JSFDemoEventInfo(new Date(),3,"Active(Start)", 
				Arrays.asList("bid","mode","price"));
		
		evtinfo.setSignalValue("bid", 1.0);
		evtinfo.setSignalValue("mode", 3.0);
		evtinfo.setSignalValue("price",4.0);
		
		JSFDemoEventInfo next =JSFDemoEventInfo.nextEvent(evtinfo,10);
		
		assertEquals(1,(int)evtinfo.getSignalValue("bid"));
		assertEquals(3,(int)evtinfo.getSignalValue("mode"));
		assertEquals(4,(int)evtinfo.getSignalValue("price"));
		
		assertEquals(1,(int)next.getSignalValue("bid"));
		assertEquals(3,(int)next.getSignalValue("mode"));
		assertEquals(4,(int)next.getSignalValue("price"));
		
		assertNotNull(next.getId());
		
		evtinfo.setSignalValue("bid", 100);
		next.setSignalValue("bid", 200);
		
		
		assertEquals(100,(int)evtinfo.getSignalValue("bid"));
		assertEquals(200,(int)next.getSignalValue("bid"));
	}
	
}
