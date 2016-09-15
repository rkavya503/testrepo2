package com.akuacom.pss2.BIPEvent;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.Test;


import com.akuacom.pss2.event.BIPEventUtil;
import com.akuacom.pss2.event.Event;
import com.akuacom.utils.DateUtil;

public class BIPEventTest {

	@Test
	public void testCase1(){
		Event event = new Event();
		//event.setEndTime(DateUtil.parse("2014-02-14 12:00:00", BIPEventUtil.DATE_FORMAT));
		List<String> locations = Arrays.asList("5011","5012","2201");
		event.setLocations(locations);
		
		Map<String,Date> times=BIPEventUtil.getAllEventLocationAndEndTime(event);
		System.out.println(times.get("5011"));
		System.out.println(times.get("5012"));
		
		BIPEventUtil.updateEventLocationStr(event, Arrays.asList("5011","5012"),
				DateUtil.parse("2014-02-14 14:00:00", BIPEventUtil.DATE_FORMAT));
		
		times=BIPEventUtil.getAllEventLocationAndEndTime(event);
		System.out.println(times.get("5011"));
		System.out.println(times.get("5012"));
		System.out.println(times.get("2201"));
		
		System.out.println(BIPEventUtil.getBiggestEndTime(event));
	}
	
}
