package com.akuacom.pss2.facdash;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlInputText;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;

import org.junit.Test;
import org.openadr.dras.eventinfo.EventInfoInstance;
import org.openadr.dras.utilitydrevent.UtilityDREvent;
import org.openadr.dras.utilitydrevent.UtilityDREvent.EventInformation;

import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.program.testProgram.TestProgram;
import com.akuacom.pss2.web.event.DemoEvent;
import com.akuacom.pss2.web.event.JSFDemoEventInfo;
import com.akuacom.utils.lang.DateUtil;
import org.junit.Ignore;

public class JSFTestDemoEventTest  {
	
		@Ignore 
	public void testDefaultValues(){
		
		JSFTestEvent demoEvent = new JSFTestEvent(false){
			public List<String> getEnabledSignalTypes(){
				return Arrays.asList("mode","price","bid");
			}
		};
		
		Date current =demoEvent.getCurrentClientTime();
		
		assertEquals(DateUtil.minuteOffset(current, demoEvent.getStart()),
				JSFTestEvent.NOTIFICATION_OFFSET+JSFTestEvent.DEFAULT_NOTICE);
		
		assertEquals(DateUtil.minuteOffset(demoEvent.getNotification(),demoEvent.getStart()),
				JSFTestEvent.DEFAULT_NOTICE);
		
		assertEquals(DateUtil.minuteOffset(demoEvent.getStart(),demoEvent.getEnd()),
				JSFTestEvent.DEFAULT_DURATION);
		
		assertEquals(demoEvent.getEvents().size(),5);
	}
	
	protected JSFTestEvent createJSFDemoEvent(final int hour,final int min,int notice,int duration,final String signalTypes[]){
		//current  notification  start end
		//-------  ------------  ---   ----
		// 14:20   14:30         14:35 14:40
		JSFTestEvent demoEvent = new JSFTestEvent(false) {
			public Date getCurrentClientTime(){
				//xxxx.xx.x  14:20
				final Calendar dDate =  Calendar.getInstance();
				dDate.setTime(new Date());
				dDate.set(Calendar.HOUR_OF_DAY,hour);
				dDate.set(Calendar.MINUTE,min);
				dDate.set(Calendar.SECOND,0);
				dDate.set(Calendar.MILLISECOND, 0);
				return dDate.getTime();
			}
			
			public List<String> getEnabledSignalTypes(){
				return Arrays.asList(signalTypes);
				//return new String[]{"mode","price","bid"};
			}
			
			protected void reportError(String msg){
				//DO nothing, just to isolate unit test from jsf runtime
			}
			
			final String name ="sce";
			
			
			protected Participant createParticipant(String name){
				Participant part= new Participant();
				part.setUUID(UUID.randomUUID().toString());
				part.setParticipantName(name);
				part.setAccountNumber("1234");
				part.setClient(false);
				return part;
			}
			
			protected Participant createClient(Participant part,String name){
				Participant client= new Participant();
				client.setUUID(UUID.randomUUID().toString());
				client.setAccountNumber(part.getParticipantName());
				client.setClient(false);
				client.setParticipantName(name);
				return client;
				
			}
			
			protected JSFParticipant getParticipant(){
				return new JSFParticipant(name){
					private Participant part;
					
					public void refresh(){
						
					}
					
					public Participant getParticipant(){
						if(part==null)
							part=createParticipant("part1");
						return part;
					}
					
					public Set<Participant>  getClients(){
						return new HashSet(Arrays.asList(
							new Participant[]{
								createClient(getParticipant(),"part1.client1")	
							}
						));
					}
				};
			}
		};
		
		demoEvent.setNotice(notice);
		demoEvent.setDuration(duration);
		
		//demo Event instances 
		
		return demoEvent;
	}
	
	
		@Ignore
	public void testUpdateStartTimeStr(){
		//current  notification  start end
		//-------  ------------  ----  ---
		// 14:20   14:23         14:28 14:38
		JSFTestEvent demoEvent = createJSFDemoEvent(14,20,5,10,new String[]{"mode","price","bid"});
		
		assertEquals("14:28",demoEvent.getStartTimeStr());
		
		//change start time
		demoEvent.setStartTimeStr("14:30");
		demoEvent.startTimeChange(new ValueChangeEvent(new HtmlInputText(),"14:28","14:30"));
		
		demoEvent.updateModel();
		
		//check the time
		assertEquals("14:30",demoEvent.getStartTimeStr());
		assertEquals("14:25",demoEvent.getNotificationTimeStr());
		assertEquals("14:40",demoEvent.getEndTimeStr());
		assertEquals(5,demoEvent.getNotice());
		assertEquals(10,demoEvent.getDuration());
	}
	
		@Ignore
	public void testUpdateNotice(){
		//default configuration
		//idx    phase         time   offset    mode      price     bid
		//---    -----------   -----  ------    -----     ---
		// 0     FAR           14:23   0
		// 1     near          14:24   1
		// 2     start         14:28   5        moderate    1.0       2.0   
		// 3     start         14:33   10       high        1.0       2.0   
		// 4     end(NONE)     14:38   15
		
		JSFTestEvent demoEvent = createJSFDemoEvent(14,20,5,10,new String[]{"mode","price","bid"});
		
		assertEquals("14:28",demoEvent.getStartTimeStr());
		assertEquals(5,demoEvent.getEvents().size());
		
		assertEquals(demoEvent.getEvents().get(0).getDateTime(),demoEvent.getNotification());
		assertEquals(demoEvent.getEvents().get(2).getDateTime(),demoEvent.getStart());
		assertEquals(demoEvent.getEvents().get(demoEvent.getEvents().size()-1).getDateTime(),demoEvent.getEnd());
		
		//change start time
		demoEvent.setNotice(6);
		demoEvent.noticeTimeChange(new ValueChangeEvent(new HtmlInputText(),5,6));
		demoEvent.updateModel();
		
		//idx    phase         time   offset    mode      price     bid
		//---    -----------   -----  ------    -----     ---
		// 0     FAR           14:22   0
		// 1     near          14:23   1
		// 2     start         14:28   6         moderate    0.0       0.0   
		// 3     event1        14:33   13        high        0.0       0.0   
		// 4     end(NONE)     14:38   18
		
		//check the time
		assertEquals("14:28",demoEvent.getStartTimeStr());
		assertEquals("14:22",demoEvent.getNotificationTimeStr());
		assertEquals("14:38",demoEvent.getEndTimeStr());
		assertEquals(6,demoEvent.getNotice());
		assertEquals(10,demoEvent.getDuration());
		assertEquals(5,demoEvent.getEvents().size());
		
		assertEquals(demoEvent.getEvents().get(0).getDateTime(),demoEvent.getNotification());
		assertEquals(demoEvent.getEvents().get(2).getDateTime(),demoEvent.getStart());
		assertEquals(demoEvent.getEvents().get(demoEvent.getEvents().size()-1).getDateTime(),demoEvent.getEnd());
		
		//minutes roll-over
		demoEvent.setNotice(36);
		demoEvent.noticeTimeChange(new ValueChangeEvent(new HtmlInputText(),6,35));
		demoEvent.updateModel();
		
		assertEquals("14:28",demoEvent.getStartTimeStr());
		assertEquals("13:52",demoEvent.getNotificationTimeStr());
		assertEquals("14:38",demoEvent.getEndTimeStr());
		assertEquals(36,demoEvent.getNotice());
		assertEquals(10,demoEvent.getDuration());
		
		assertEquals(demoEvent.getEvents().get(0).getDateTime(),demoEvent.getNotification());
		assertEquals(demoEvent.getEvents().get(2).getDateTime(),demoEvent.getStart());
		assertEquals(demoEvent.getEvents().get(demoEvent.getEvents().size()-1).getDateTime(),demoEvent.getEnd());
	}
	
	@Ignore
	public void testUpdateNotification(){
		//current  notification  start end
		//-------  ------------  ----  ---
		// 14:20   14:23         14:28 14:38
		JSFTestEvent demoEvent = createJSFDemoEvent(14,20,5,10,new String[]{"mode","price","bid"});
		
		assertEquals("14:28",demoEvent.getStartTimeStr());
		
		//change start time
		demoEvent.setNotificationTimeStr("14:24");
		demoEvent.notificationTimeChange(new ValueChangeEvent(new HtmlInputText(),3,4));
		
		demoEvent.updateModel();
		
		//check the time
		assertEquals("14:28",demoEvent.getStartTimeStr());
		assertEquals("14:24",demoEvent.getNotificationTimeStr());
		assertEquals("14:38",demoEvent.getEndTimeStr());
		assertEquals(4,demoEvent.getNotice());
		assertEquals(10,demoEvent.getDuration());
		
		/*0: notification time
		  1: near time
		  2: start time*/		
		assertEquals(demoEvent.getEvents().get(0).getDateTime(),demoEvent.getNotification());
		assertEquals(demoEvent.getEvents().get(2).getDateTime(),demoEvent.getStart());
		assertEquals(demoEvent.getEvents().get(demoEvent.getEvents().size()-1).getDateTime(),demoEvent.getEnd());
		
	}
	
	@Ignore
	public void testUpdateDuration(){
		//current  notification  start end
		//-------  ------------  ----  ---
		// 14:20   14:23         14:28 14:38
		JSFTestEvent demoEvent = createJSFDemoEvent(14,20,5,10,new String[]{"mode","price","bid"});
		
		assertEquals("14:28",demoEvent.getStartTimeStr());
		
		//change duration
		demoEvent.setDuration(6);
		demoEvent.durationTimeChange(new ValueChangeEvent(new HtmlInputText(),5,6));
		
		demoEvent.updateModel();
		
		//check time
		assertEquals("14:28",demoEvent.getStartTimeStr());
		assertEquals("14:23",demoEvent.getNotificationTimeStr());
		assertEquals("14:34",demoEvent.getEndTimeStr());
		assertEquals(5,demoEvent.getNotice());
		assertEquals(6,demoEvent.getDuration());
		
		assertEquals(demoEvent.getEvents().get(0).getDateTime(),demoEvent.getNotification());
		assertEquals(demoEvent.getEvents().get(2).getDateTime(),demoEvent.getStart());
		assertEquals(demoEvent.getEvents().get(demoEvent.getEvents().size()-1).getDateTime(),demoEvent.getEnd());
		
		
		//change duration to make minute roll-over
		demoEvent.setDuration(26);
		demoEvent.durationTimeChange(new ValueChangeEvent(new HtmlInputText(),6,26));
		demoEvent.updateModel();
		
		assertEquals("14:28",demoEvent.getStartTimeStr());
		assertEquals("14:23",demoEvent.getNotificationTimeStr());
		assertEquals("14:54",demoEvent.getEndTimeStr());
		assertEquals(5,demoEvent.getNotice());
		assertEquals(26,demoEvent.getDuration());
		
		assertEquals(demoEvent.getEvents().get(0).getDateTime(),demoEvent.getNotification());
		assertEquals(demoEvent.getEvents().get(2).getDateTime(),demoEvent.getStart());
		assertEquals(demoEvent.getEvents().get(demoEvent.getEvents().size()-1).getDateTime(),demoEvent.getEnd());
	}
	
	
		@Ignore
	public void testUpdateEndTime(){
		//current  notification  start end
		//-------  ------------  ----  ---
		// 14:20   14:23         14:28 14:38
		JSFTestEvent demoEvent = createJSFDemoEvent(14,20,5,10,new String[]{"mode","price","bid"});
		
		assertEquals(demoEvent.getStartTimeStr(),"14:28");
		
		//change end time
		demoEvent.setEndTimeStr("14:41");
		demoEvent.endTimeChange(new ValueChangeEvent(new HtmlInputText(),"14:38","14:41"));
		
		demoEvent.updateModel();
		
		//check the time
		assertEquals("14:28",demoEvent.getStartTimeStr());
		assertEquals("14:23",demoEvent.getNotificationTimeStr());
		assertEquals("14:41",demoEvent.getEndTimeStr());
		assertEquals(5,demoEvent.getNotice());
		assertEquals(13,demoEvent.getDuration());
		
		assertEquals(demoEvent.getEvents().get(0).getDateTime(),demoEvent.getNotification());
		assertEquals(demoEvent.getEvents().get(2).getDateTime(),demoEvent.getStart());
		assertEquals(demoEvent.getEvents().get(demoEvent.getEvents().size()-1).getDateTime(),demoEvent.getEnd());
		
		
		//change end time, minute roll-over
		demoEvent.setEndTimeStr("15:01");
		demoEvent.endTimeChange(new ValueChangeEvent(new HtmlInputText(),"14:41","15:01"));
		demoEvent.updateModel();
		
		assertEquals("14:28",demoEvent.getStartTimeStr());
		assertEquals("14:23",demoEvent.getNotificationTimeStr());
		assertEquals("15:01",demoEvent.getEndTimeStr());
		assertEquals(5,demoEvent.getNotice());
		assertEquals(33,demoEvent.getDuration());
	}
	
	
	
		@Ignore
	public void testAddEvent(){
		//idx    phase         time   offset    mode      price     bid
		//---    -----------   -----  ------    -----     ---
		// 0     notification  14:30   0
		// 1     near          14:34   4
		// 2     start         14:35   5        normal    1.0       2.0  
		// 3     event2        14:36   7         moderate  3.0       4.0
		// 5     end           14:40   10
		
		JSFTestEvent demoEvent = createJSFDemoEvent(14,20,5,10,new String[]{"mode","price","bid"});
		UIComponent component = new HtmlInputText();
		
		JSFDemoEventInfo startEvent = demoEvent.getEvents().get(2);
		
		component.getAttributes().put("evtid", startEvent.getId());
		demoEvent.addSingalEntry(new ActionEvent (component));
		
		assertEquals(6, demoEvent.getEvents().size());
		
		JSFDemoEventInfo evt1 = demoEvent.getEvents().get(3);
		assertEquals(2, DateUtil.minuteOffset(startEvent.getDateTime(),evt1.getDateTime()));
		assertEquals(7, evt1.getOffsetFromNotification());
		
	}
	
	
		@Ignore
	public void testToUtilityDREvent(){
		JSFTestEvent demoEvent = createJSFDemoEvent(14,20,5,5,new String[]{"mode","price","bid"});
		//idx    phase         time    offset  mode      price     bid
		//---    -----------   -----   ------  ------    -----     ---
		// -     current       14:20     
		// 0     notification  14:30     0
		// 1     near          14:34     4
		// 2     start         14:35     5      normal    1.0       2.0  
		// 3     event2        14:36     7      moderate  3.0       4.0
		// 4     event3        14:37     8      high      5.0       6.0
		// 5     end           14:40     10
		JSFDemoEventInfo startEvent = demoEvent.getEvents().get(2);
		
		UIComponent component = new HtmlInputText();
		component.getAttributes().put("evtid", startEvent.getId());
		demoEvent.addSingalEntry(new ActionEvent (component));
		JSFDemoEventInfo event2 = demoEvent.getEvents().get(3);
		
		component.getAttributes().put("evtid", event2.getId());
		demoEvent.addSingalEntry(new ActionEvent (component));
		JSFDemoEventInfo event3 = demoEvent.getEvents().get(4);
		
		startEvent.setSignalValue("mode", 1.0);
		startEvent.setSignalValue("price",1.0);
		startEvent.setSignalValue("bid",2.0);
		
		event2.setSignalValue("mode",2.0);
		event2.setSignalValue("price",3.0);
		event2.setSignalValue("bid", 4.0);
		

		event3.setSignalValue("mode",3.0);
		event3.setSignalValue("price",5.0);
		event3.setSignalValue("bid", 6.0);
		
		
		UtilityDREvent drevent = demoEvent.toUtilityDREvent();
		
		assertNotNull(drevent.getEventIdentifier());
		assertEquals(TestProgram.PROGRAM_NAME,drevent.getProgramName());
		
		EventInformation information=drevent.getEventInformation();
		assertEquals(3,information.getEventInfoInstance().size());
		
		//mode 
		//mode is the second record in event info instance
		EventInfoInstance modeEvtInfo = information.getEventInfoInstance().get(0);
		
		assertEquals(DemoEvent.getSingalNameInOpenAdr("mode"),
					modeEvtInfo.getEventInfoTypeName());
		
		assertEquals(4,modeEvtInfo.getValues().getValue().size());
//		assertEquals(1.0,modeEvtInfo.getValues().getValue().get(0).getValue(),0.0001);
		assertEquals(0.0,modeEvtInfo.getValues().getValue().get(0).getStartTime(),0.0001);
		
//		assertEquals(2.0,modeEvtInfo.getValues().getValue().get(1).getValue(),0.0001);
		assertEquals(120.0,modeEvtInfo.getValues().getValue().get(1).getStartTime(),0.0001);
		
//		assertEquals(3.0,modeEvtInfo.getValues().getValue().get(2).getValue(),0.0001);
		assertEquals(180.0,modeEvtInfo.getValues().getValue().get(2).getStartTime(),0.0001);
		
		//price is the second record
		EventInfoInstance priceEvtInfo = information.getEventInfoInstance().get(1);
		assertEquals(DemoEvent.getSingalNameInOpenAdr("price"),
				priceEvtInfo.getEventInfoTypeName());
		
		assertEquals(4,priceEvtInfo.getValues().getValue().size());
//		assertEquals(1.0,priceEvtInfo.getValues().getValue().get(0).getValue(),0.0001);
		assertEquals(0.0,priceEvtInfo.getValues().getValue().get(0).getStartTime(),0.0001);
		
//		assertEquals(3.0,priceEvtInfo.getValues().getValue().get(1).getValue(),0.0001);
		assertEquals(120.0,priceEvtInfo.getValues().getValue().get(1).getStartTime(),0.0001);
		
//		assertEquals(5.0,priceEvtInfo.getValues().getValue().get(2).getValue(),0.0001);
		assertEquals(180.0,priceEvtInfo.getValues().getValue().get(2).getStartTime(),0.0001);
		
		//bid is the third record
		EventInfoInstance bidEvtInfo = information.getEventInfoInstance().get(2);
		assertEquals(DemoEvent.getSingalNameInOpenAdr("bid"),
				bidEvtInfo.getEventInfoTypeName());
		
		assertEquals(4,bidEvtInfo.getValues().getValue().size());
//		assertEquals(2.0,bidEvtInfo.getValues().getValue().get(0).getValue(),0.0001);
		assertEquals(0.0,bidEvtInfo.getValues().getValue().get(0).getStartTime(),0.0001);
		
//		assertEquals(4.0,bidEvtInfo.getValues().getValue().get(1).getValue(),0.0001);
		assertEquals(120.0,bidEvtInfo.getValues().getValue().get(1).getStartTime(),0.0001);
		
//		assertEquals(6.0,bidEvtInfo.getValues().getValue().get(2).getValue(),0.0001);
		assertEquals(180.0,bidEvtInfo.getValues().getValue().get(2).getStartTime(),0.0001);
	}
	

	@Ignore
	public void testToUtilityDREventWithAllSignalTypesEnabled(){
		JSFTestEvent demoEvent = createJSFDemoEvent(14,20,5,5,
					new String[]{"mode","price","bid","price2","cpp_price"});
		//idx    phase         time    offset  mode      price     bid   price2  cpp_price 
		//---    -----------   -----   -----   ------    -----     ---   ------  --------
		// -     current       14:20    
		// 0     notification  14:30    0
		// 1     near          14:34    4
		// 2     start         14:35    5      normal    1.0       2.0   2.1      2.2
		// 3     event2        14:36    7      moderate  3.0       4.0   4.1      4.2
		// 4     event3        14:37    8      high      5.0       6.0   6.1      6.2
		// 5     end           14:40    10
		JSFDemoEventInfo startEvent = demoEvent.getEvents().get(2);
		
		UIComponent component = new HtmlInputText();
		component.getAttributes().put("evtid", startEvent.getId());
		demoEvent.addSingalEntry(new ActionEvent (component));
		JSFDemoEventInfo event2 = demoEvent.getEvents().get(3);
		
		component.getAttributes().put("evtid", event2.getId());
		demoEvent.addSingalEntry(new ActionEvent (component));
		JSFDemoEventInfo event3 = demoEvent.getEvents().get(4);
		
		startEvent.setSignalValue("mode", 1.0);
		startEvent.setSignalValue("price",1.0);
		startEvent.setSignalValue("bid",2.0);
		startEvent.setSignalValue("price2",2.1);
		startEvent.setSignalValue("cpp_price",2.2);
		

		event2.setSignalValue("mode", 2.0);
		event2.setSignalValue("price",3.0);
		event2.setSignalValue("bid",4.0);
		event2.setSignalValue("price2",4.1);
		event2.setSignalValue("cpp_price",4.2);
		
		event3.setSignalValue("mode", 3.0);
		event3.setSignalValue("price",5.0);
		event3.setSignalValue("bid",6.0);
		event3.setSignalValue("price2",6.1);
		event3.setSignalValue("cpp_price",6.2);
		
		
		UtilityDREvent drevent = demoEvent.toUtilityDREvent();
		
		assertNotNull(drevent.getEventIdentifier());
		assertEquals(TestProgram.PROGRAM_NAME,drevent.getProgramName());
		
		EventInformation information=drevent.getEventInformation();
		assertEquals(5,information.getEventInfoInstance().size());
		
		//mode is the first record
		EventInfoInstance modeEvtInfo = information.getEventInfoInstance().get(0);
		
		assertEquals(DemoEvent.getSingalNameInOpenAdr("mode"),
					modeEvtInfo.getEventInfoTypeName());
		
		assertEquals(4,modeEvtInfo.getValues().getValue().size());
//		assertEquals(1.0,modeEvtInfo.getValues().getValue().get(0).getValue(),0.0001);
		assertEquals(0.0,modeEvtInfo.getValues().getValue().get(0).getStartTime(),0.0001);
		
//		assertEquals(2.0,modeEvtInfo.getValues().getValue().get(1).getValue(),0.0001);
		assertEquals(120.0,modeEvtInfo.getValues().getValue().get(1).getStartTime(),0.0001);
		
//		assertEquals(3.0,modeEvtInfo.getValues().getValue().get(2).getValue(),0.0001);
		assertEquals(180.0,modeEvtInfo.getValues().getValue().get(2).getStartTime(),0.0001);
		
		//price is the second record
		EventInfoInstance priceEvtInfo = information.getEventInfoInstance().get(1);
		assertEquals(DemoEvent.getSingalNameInOpenAdr("price")
		              ,priceEvtInfo.getEventInfoTypeName());
		assertEquals(4,priceEvtInfo.getValues().getValue().size());
//		assertEquals(1.0,priceEvtInfo.getValues().getValue().get(0).getValue(),0.0001);
		assertEquals(0.0,priceEvtInfo.getValues().getValue().get(0).getStartTime(),0.0001);
		
//		assertEquals(3.0,priceEvtInfo.getValues().getValue().get(1).getValue(),0.0001);
		assertEquals(120.0,priceEvtInfo.getValues().getValue().get(1).getStartTime(),0.0001);
		
//		assertEquals(5.0,priceEvtInfo.getValues().getValue().get(2).getValue(),0.0001);
		assertEquals(180.0,priceEvtInfo.getValues().getValue().get(2).getStartTime(),0.0001);
		
		//bid is the third record
		EventInfoInstance bidEvtInfo = information.getEventInfoInstance().get(2);
		assertEquals(DemoEvent.getSingalNameInOpenAdr("bid")
		                 ,bidEvtInfo.getEventInfoTypeName());
		
		assertEquals(4,bidEvtInfo.getValues().getValue().size());
//		assertEquals(2.0,bidEvtInfo.getValues().getValue().get(0).getValue(),0.0001);
		assertEquals(0.0,bidEvtInfo.getValues().getValue().get(0).getStartTime(),0.0001);
		
//		assertEquals(4.0,bidEvtInfo.getValues().getValue().get(1).getValue(),0.0001);
		assertEquals(120.0,bidEvtInfo.getValues().getValue().get(1).getStartTime(),0.0001);
		
//		assertEquals(6.0,bidEvtInfo.getValues().getValue().get(2).getValue(),0.0001);
		assertEquals(180.0,bidEvtInfo.getValues().getValue().get(2).getStartTime(),0.0001);
		
		
		//price 2
		EventInfoInstance price2EvtInfo = information.getEventInfoInstance().get(3);
		assertEquals(DemoEvent.getSingalNameInOpenAdr("price2")
		              ,price2EvtInfo.getEventInfoTypeName());
		
		assertEquals(4,price2EvtInfo.getValues().getValue().size());
//		assertEquals(2.1,price2EvtInfo.getValues().getValue().get(0).getValue(),0.0001);
		assertEquals(0.0,price2EvtInfo.getValues().getValue().get(0).getStartTime(),0.0001);
		
//		assertEquals(4.1,price2EvtInfo.getValues().getValue().get(1).getValue(),0.0001);
		assertEquals(120.0,price2EvtInfo.getValues().getValue().get(1).getStartTime(),0.0001);
		
//		assertEquals(6.1,price2EvtInfo.getValues().getValue().get(2).getValue(),0.0001);
		assertEquals(180.0,price2EvtInfo.getValues().getValue().get(2).getStartTime(),0.0001);
		
		
		//cpp_price
		EventInfoInstance cppEventInfo = information.getEventInfoInstance().get(4);
		assertEquals(DemoEvent.getSingalNameInOpenAdr("cpp_price")
		                 ,cppEventInfo.getEventInfoTypeName());
		
		assertEquals(4,cppEventInfo.getValues().getValue().size());
//		assertEquals(2.2,cppEventInfo.getValues().getValue().get(0).getValue(),0.0001);
		assertEquals(0.0,cppEventInfo.getValues().getValue().get(0).getStartTime(),0.0001);
		
//		assertEquals(4.2,cppEventInfo.getValues().getValue().get(1).getValue(),0.0001);
		assertEquals(120.0,cppEventInfo.getValues().getValue().get(1).getStartTime(),0.0001);
		
//		assertEquals(6.2,cppEventInfo.getValues().getValue().get(2).getValue(),0.0001);
		assertEquals(180.0,cppEventInfo.getValues().getValue().get(2).getStartTime(),0.0001);
		
		
	}
	
}
