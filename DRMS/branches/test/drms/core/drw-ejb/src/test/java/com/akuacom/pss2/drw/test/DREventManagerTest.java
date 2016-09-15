package com.akuacom.pss2.drw.test;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.junit.Ignore;

import com.akuacom.pss2.drw.DREventManager;
import com.akuacom.pss2.drw.core.Event;
import com.akuacom.pss2.drw.core.EventDetail;
import com.akuacom.pss2.drw.core.Location;
import com.akuacom.utils.lang.DateUtil;
/**
 * This is a temporary integration testing solution used for development debuging
 * How to use this:
 * 1.Comment out the @Ignore annotation
 * 2.add jbossall-client.jar(jboss-5.1.0.GA\client\jboss-client.jar) into classpath, 
 *   if you meet the following exception: java.lang.ClassNotFoundException: 
 *    org.jnp.interfaces.NamingContextFactory 
 *3.Since a location record was used in this test case, you may need to created it in database manually
 *    id:1 | name:| abank1 | type:ABank | number:1
 * @author E499390
 *
 */
@Ignore
public class DREventManagerTest {
	DREventManager eventManager;
		
		private DREventManager getEvtManager(){
			if(eventManager==null) {
				eventManager = ServiceLocator.findHandler(DREventManager.class,
						"dr-pro/DREventManager/remote");
			}
			
			return eventManager;
		}
	 @Test
	    public void testCreateEvent() {
		 Event event = new Event();
			event.setStartTime(new Date());
			event.setComment("Test");
			event.setProgramName("SDP");
//			event.setRate("APS");
//			event.setAllABank(false);
//			event.setAllSLAP(false);
//			event.setAllSubstation(false);
			Set<EventDetail> createdDetails=new HashSet<EventDetail>();
			com.akuacom.pss2.drw.core.Location location = new Location();
			location.setName("abank1");
			location.setNumber("1");
			location.setType("ABank");
			location.setID(1);
			EventDetail detail=new EventDetail();
			detail.setLocation(location);
			detail.setEstimatedEndTime(DateUtil.add(new Date(), Calendar.DATE, 5));
			detail.setLastModifiedTime(new Date());
			detail.setEvent(event);
			createdDetails.add(detail);
			Set<EventDetail> eventDetails=event.getDetails();
			eventDetails.addAll(createdDetails);
			
			getEvtManager().createEvent(event);
	    }
}
