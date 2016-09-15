package com.akuacom.pss2.core;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.openadr.dras.eventstate.ListOfEventStates;

import com.akuacom.pss2.client.ClientManager;
import com.akuacom.pss2.event.Event;
import com.akuacom.pss2.event.EventManager;
import com.akuacom.pss2.event.TimeBlock;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.participant.ParticipantManager;
import com.akuacom.pss2.program.ProgramManager;
import com.akuacom.pss2.program.cpp.CPPShedStrategy;
import com.akuacom.pss2.program.cpp.CPPShedStrategy.ShedMode;
import com.akuacom.pss2.program.cpp.CPPUtils;
import com.akuacom.pss2.program.participant.ProgramParticipant;
import com.akuacom.pss2.program.participant.ProgramParticipantManager;
import com.akuacom.pss2.program.participant.ProgramParticipantRule;
import com.akuacom.pss2.rule.Rule.Mode;
import com.akuacom.pss2.util.EventState;

public class EventSignalClient {


	public static void main(String[] args) {

            Properties properties = new Properties();
            properties.put("java.naming.factory.initial","org.jnp.interfaces.NamingContextFactory");
            properties.put("java.naming.factory.url.pkgs","=org.jboss.naming:org.jnp.interfaces");
            properties.put("java.naming.provider.url","localhost:1099");
            properties.put("jnp.socket.Factory", "org.jnp.interfaces.TimedSocketFactory");

            String name = "ahmedGhali.c1";

            Context context;
             try {
                    context = new InitialContext(properties);
                    System.out.println("THINKING ABOUT IT ..");
                    ProgramManager programBean = (ProgramManager) context.lookup("pss2/ProgramManagerBean/remote");
                    ParticipantManager participantBean = (ParticipantManager) context.lookup("pss2/ParticipantManagerBean/remote");
                    ClientManager clientBean = (ClientManager) context.lookup("pss2/ClientManagerBean/remote");

                    /****** Set CPP Shed-Strategy ******/
                    String participantName = "test1.c1";
                    String participantAcc = "0909096";
                   // String clientName = "test1.c1";
                    String programName = "CPP";
                    String[] programs = {"CPP"};

                    /*** Create Participant ***/
                        //Participant participant = createParticipant(context,participantName,participantAcc);
                        // System.out.println("+++ Participant Name " + participant.getParticipantName());
                         //participant.getParticipantName()
                    /*** Add Participant to program **/
                       // programBean.addParticipantToProgram(programName,participantName , false);
                    /*** Create a client for this participant **/
                       // String clientName = createClient(context,participant,"c1");
                       // System.out.println("+++ Client Name " + clientName);
                    /*** Extract signal ***********************/
                   //createRemoveEvent(context);
                    extractSingals(context,participantName);


                    /*** Create an event for this participant/client **/
                    /*** Create shedStrategy for this client **/
                    //List<ProgramParticipantRuleEAO> rules = prepairCPPShedStrategy();
                    //createCPPShedStrategy(context,programName,clientName,rules);

             System.out.println("+++++++ WORKED.");

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            }
        }


        public static String createClient(Context context,Participant participant,String client) throws NamingException{
               ParticipantManager participantBean = (ParticipantManager) context.lookup("pss2/ParticipantManagerBean/remote");
               Participant c = participantBean.getParticipant(participant.getParticipantName(), false);
                    //clientBean.createClient( partcipantVO , chars);
                   // Participant c = ParticipantUtil
                             //   .getParicipantInstance(TestUtil
                             //   .generateRandomInt(ParticipantManagerTest.PARTIPANT_USER_MAX_LENGTH));
                    ClientManager clientBean = (ClientManager) context.lookup("pss2/ClientManagerBean/remote");

                    //Participant c = new Participant();
                    char[] chars = {'1','2','3','a','p','p','l','e','_','0','0'};
                    String parentName = participant.getParticipantName();
                    c.setParent(parentName);
                    c.setClient(true);
                    String clientUser = parentName + "." + client ;
                    c.setParticipantName(clientUser);
                    c.setAccountNumber(clientUser);
                    clientBean.createClient(c, chars);

               return clientBean.getClient(c.getParticipantName()).getParticipantName();
        }

        public static Participant createParticipant(Context context,String name,String accNum) throws NamingException{
             Participant p = new Participant();
                            p.setFirstName("ahmed");
                            p.setLastName("Ghali");
                            p.setParticipantName(name);
                            p.setActivated(true);
                            p.setAccountNumber(accNum);
                         ParticipantManager participantBean = (ParticipantManager) context.lookup("pss2/ParticipantManagerBean/remote");
                         participantBean.createParticipant(p, "".toCharArray(), null);

               return participantBean.getParticipant(name, false);

        }

        public static List prepairCPPShedStrategy(){
             List<ProgramParticipantRule> rules = null;
              List<CPPShedStrategy> cppShedStrategies = new ArrayList<CPPShedStrategy>();

            try{
                   /***** First time block ********************/
                    CPPShedStrategy strategy1 = new CPPShedStrategy() ;
                    TimeBlock timeBlock = new TimeBlock();
                            timeBlock.setStartHour(14);
                            timeBlock.setStartMinute(00);
                            timeBlock.setStartSecond(00);
                            /*******End of Start time********/
                            timeBlock.setEndHour(15);
                            timeBlock.setEndMinute(00);
                            timeBlock.setEndSecond(00);
                            strategy1.setMode(ShedMode.HIGH);
                            strategy1.setRuleMode(Mode.HIGH);
                            strategy1.setTimeBlock(timeBlock);
                    /*******End of time Block****************/
                   /***** Second time block ********************/
                    CPPShedStrategy strategy2 = new CPPShedStrategy() ;
                    TimeBlock timeBlock2 = new TimeBlock();
                            timeBlock2.setStartHour(15);
                            timeBlock2.setStartMinute(00);
                            timeBlock2.setStartSecond(00);
                            /*******End of Start time********/
                            timeBlock2.setEndHour(16);
                            timeBlock2.setEndMinute(00);
                            timeBlock2.setEndSecond(00);
                            strategy2.setMode(ShedMode.MODERATE);
                            strategy2.setRuleMode(Mode.MODERATE);
                            strategy2.setTimeBlock(timeBlock2);
                    /*******End of time Block****************/
                               /***** Second time block ********************/
                    CPPShedStrategy strategy3 = new CPPShedStrategy() ;
                    TimeBlock timeBlock3 = new TimeBlock();
                            timeBlock3.setStartHour(16);
                            timeBlock3.setStartMinute(00);
                            timeBlock3.setStartSecond(00);
                            /*******End of Start time********/
                            timeBlock3.setEndHour(17);
                            timeBlock3.setEndMinute(00);
                            timeBlock3.setEndSecond(00);
                            strategy3.setMode(ShedMode.HIGH);
                            strategy3.setRuleMode(Mode.HIGH);
                            strategy3.setTimeBlock(timeBlock3);
                    /*******End of time Block****************/
                    cppShedStrategies.add(strategy1);
                    cppShedStrategies.add(strategy2);
                    cppShedStrategies.add(strategy3);

                    rules = CPPUtils.parseCPPShedStrategies(cppShedStrategies);

            }catch(Exception ex){
                 System.out.println("xx Error in preparing CPP Shed Strategy");
                ex.printStackTrace();
            }

            return rules;
        }
        public static String createCPPShedStrategy(Context context,String programName,String clientName,List<ProgramParticipantRule> rules ){
               try{
                    ProgramManager programBean = (ProgramManager) context.lookup("pss2/ProgramManagerBean/remote");
                    ProgramParticipantManager programParticipantManager = (ProgramParticipantManager) context.lookup("pss2/ProgramParticipantManager/remote");
                    
                    ProgramParticipant prgmPart =programParticipantManager.getClientProgramParticipants(programName, clientName, true);

                    if (prgmPart == null){
                        System.out.println("+++ progmPart NULL");
                    }else{
                        System.out.println("+++ progmPart " + prgmPart.getParticipantName());
                    }
                    if (prgmPart.getProgramParticipantRules() == null){
                        System.out.println("+++ in getRules NULL");
			prgmPart.setProgramParticipantRules(new HashSet<ProgramParticipantRule>());
                    }else{
                        System.out.println("+++ in getRules NOT NULL");
                        prgmPart.getProgramParticipantRules().clear();
                    }

                            int index = 0;
                            for (ProgramParticipantRule rule : rules)
                            {
                                    rule.setSortOrder(index++);
                                      System.out.println("++++ Rule>> Start:" + rule.getStart());
                                      System.out.println("++++ Rule>> End:" + rule.getEnd());
                                      System.out.println("++++ Rule>> Mode:" + rule.getMode());
                                    prgmPart.getProgramParticipantRules().add(rule);

                            }

                            programParticipantManager.updateProgramParticipant(programName, clientName, true, prgmPart);
            }catch(Exception ex){
                System.out.println("xx Error in creating CPP Shed Strategy");
                ex.printStackTrace();
            }
         return "ShedWorked";
        }


      private static Event generateEvent() {

            Event event = new Event();
            event.setProgramName("CPP");
            Date now = new Date();
            event.setEventName("TestCPP4");

            event.setIssuedTime(now);
            GregorianCalendar calender = new GregorianCalendar();
                calender.setTime(now);
                calender.add(Calendar.DAY_OF_MONTH, 0);
                calender.set(Calendar.HOUR_OF_DAY, 14);
                calender.set(Calendar.MINUTE, 0);
                calender.set(Calendar.SECOND, 0);
                calender.set(Calendar.MILLISECOND, 0);

            event.setStartTime(calender.getTime());
            calender.set(Calendar.HOUR_OF_DAY, 18);
            event.setEndTime(calender.getTime());

        event.setReceivedTime(now);
        return event;
    }

      public static void createRemoveEvent(Context context) throws NamingException{
            EventManager eventBean = (EventManager) context.lookup("pss2/EventManagerBean/remote");

            Event event = generateEvent();
                eventBean.createEvent("CPP", event);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // it's ok
            }

            //eventBean.removeEvent("CPP_EVENT", "TEST Event");
            //programManager.removeParticipantFromProgram(CPP_TEST, p0.getUser(), false);
            //pm.removeParticipant(p0.getUser());
      }




      public static void writeLog(StringBuilder feed){

          try {
                FileWriter outFile = new FileWriter("C:\\Users\\ahmed\\Desktop\\log.txt");
                PrintWriter out = new PrintWriter(outFile);
                out.println(feed);

           out.close();
          } catch (IOException e){
              e.printStackTrace();
         }

      }

	public static String extractSingals(Context context, String name) throws NamingException{
            try{
                StringBuilder eventsSB = new StringBuilder();
                while(true){
                    System.out.println(new Date());
                    /**** Start of Extracting the signal ****************************/
                             ClientManager clientBean = (ClientManager) context.lookup("pss2/ClientManagerBean/remote");
                             System.out.println("+++++ client Status: " + clientBean.getClientStatus(name) );
                             List<EventState> eventStates = clientBean.getClientEventStates(name, false);

                              System.out.println("*** Client: " + name);
                               eventsSB.append("");
                               eventsSB.append(new Date() + "--");
                           if(eventStates != null) {
                                ListOfEventStates listOfEventStates = new org.openadr.dras.eventstate.ListOfEventStates();
                                for(EventState eventState: eventStates)   {
                                    eventsSB.append(eventState.getProgramName() + "--");
                                    eventsSB.append(eventState.getStartTime() + "--");
                                    eventsSB.append(eventState.getEndTime() + "--");
                                    eventsSB.append(eventState.getEventStatus().toString() + "--");
                                    eventsSB.append(eventState.getOperationModeValue().name().toString() + "--");
                           eventsSB.append(System.getProperty("line.separator"));
                           eventsSB.append("============================================");
                           eventsSB.append(System.getProperty("line.separator"));

                                    System.out.println("**** Program: " + eventState.getProgramName());
                                    System.out.println("**** Start: " + eventState.getStartTime());
                                    System.out.println("**** End: " + eventState.getEndTime());
                                    System.out.println("**** Status: " + eventState.getEventStatus().toString());
                                    System.out.println("**** Op Mode: " + eventState.getOperationModeValue().name().toString());
                            System.out.println(" ======================================== ");
                                //listOfEventStates.getEventStates().add(ClientUtil.parseEventState(name,eventState));
                                }
                               // String eventStatesXML = ClientUtil.eventStatesToXML(listOfEventStates);
                         }
                         writeLog(eventsSB);
                 /**** End of Extracting the signal ****************************/
                    Thread.sleep(60 * 1000);
                }
            }catch(InterruptedException e) {
                e.printStackTrace();
            }

            return "extractSignalWorked";
        }


}
