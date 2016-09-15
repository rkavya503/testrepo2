/*
* www.akuacom.com - Automating Demand Response
*
* com.akuacom.pss2.facdash.ProgramTable.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved.
* Redistribution and use in source and binary forms, with or without modification, is prohibited.
*
*/
package com.akuacom.pss2.facdash;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import javax.annotation.Resource;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerService;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import com.akuacom.ejb.client.EJB3Factory;
import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.core.ErrorResourceUtil;
import com.akuacom.pss2.event.participant.EventParticipant;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.participant.ParticipantManager;
import com.akuacom.pss2.program.participant.ProgramParticipant;
import com.akuacom.pss2.program.participant.ProgramParticipantAggregationManager;
import com.akuacom.pss2.program.participant.ProgramParticipantAggregationManagerBean;
import com.akuacom.pss2.program.participant.ProgramParticipantManager;
import com.akuacom.pss2.program.testProgram.TestProgram;
import com.akuacom.pss2.system.SystemManager;
import com.akuacom.pss2.system.property.PSS2Features;
import com.akuacom.pss2.util.DrasRole;
import com.akuacom.pss2.util.UserType;
import com.akuacom.pss2.web.aggregation.AggregationTree;

import java.io.Serializable;

/**
* The Class ProgramTable.
*/
public class ProgramTable implements Serializable {


        /** The program name. */
        private String programName;

        /** The programs. */
        private List<JSFParticipantProgram> programs;

        private String optStatus = "In";

        private boolean inEvent;

		private ArrayList nonConsolidationPrograms;
		
		private boolean dayOfAdjustment ;
        
        //private static final Logger log = Logger.getLogger(JSFClient.class.getName());
		
		private boolean enableOptOut=true;
		
		private Date optOutUntil=new Date();
		 	

		/**
		 * @return the optOutUntil
		 */
		public Date getOptOutUntil() {
			return optOutUntil;
		}

		/**
		 * @param optOutUntil the optOutUntil to set
		 */
		public void setOptOutUntil(Date optOutUntil) {
			this.optOutUntil = optOutUntil;
		}

		/**
		 * @return the enableOptOut
		 */
		public boolean isEnableOptOut() {						
			return enableOptOut;
			
		}

		/**
		 * @param enableOptOut the enableOptOut to set
		 */
		public void setEnableOptOut(boolean enableOptOut) {
			this.enableOptOut = enableOptOut;
		}

		/**
        * Instantiates a new program table.
        */
        public ProgramTable(){
        }

        public String getProgramName() {
            return programName;
        }

        public void setProgramName(String programName) {
            this.programName = programName;
        }

        public boolean isInEvent() {
            return inEvent;
        }

        public void setInEvent(boolean inEvent) {
            this.inEvent = inEvent;
        }
        
        public boolean isDayOfAdjustment() {
			return dayOfAdjustment;
		}

		public void setDayOfAdjustment(boolean dayOfAdjustment) {
			this.dayOfAdjustment = dayOfAdjustment;
		}

        /**
        * Gets the programs.
        *
        * @return the programs
        */
	public List<JSFParticipantProgram> getPrograms() {
        FDUtils.getHeader1().setVIEW_TABLE_NAME("programs");
		ParticipantManager participantManager = EJBFactory
				.getBean(ParticipantManager.class);
		programs = new ArrayList<JSFParticipantProgram>();
		
		/*
		 * Enable opt-out changes
		 */
		String userRole=getUserRole();
		String role = System.getProperty("com.honeywell.roles.optOut");		
		StringTokenizer st = new StringTokenizer(role,",");  
		
		if(userRole.equalsIgnoreCase("FacilityManager")) {
			boolean isAggregator=checkIsAggregator(participantManager);
			while (st.hasMoreTokens()) {  
		        String nextElemnt=st.nextToken();	
		        if(null != nextElemnt && (nextElemnt.equalsIgnoreCase("AGGREGATOR") || nextElemnt.equalsIgnoreCase("CUSTOMER")) ){		        	
		        	if(nextElemnt.equalsIgnoreCase("AGGREGATOR") && isAggregator){
			        	 this.enableOptOut=false;
			        	 break;
			         }else if(nextElemnt.equalsIgnoreCase("CUSTOMER") && !isAggregator){
			        	 this.enableOptOut=false;
			        	 break;
			         }
		        	
		        }		        
		         
		     }		
			
		}else{
			
			while (st.hasMoreTokens()) {  
		        String nextElemnt=st.nextToken();	        
		         if(null!= nextElemnt && nextElemnt.equalsIgnoreCase(userRole)){
		        	 this.enableOptOut=false;
		        	 break;
		         }
		     }
		}
		
		
		for (ProgramParticipant programParticipant : participantManager
				.getParticipant(FDUtils.getParticipantName())
				.getProgramParticipants()) {
			JSFProgram program = FDUtils.getJSFProgram();
            if (program != null && !program.getName().equals(AggregationTree.NO_PROGRAM) 
                    && !program.getName().equalsIgnoreCase(TestProgram.PROGRAM_NAME)) {
                	if(programParticipant.getProgramName().equalsIgnoreCase(FDUtils.getJSFProgram().getName()) ){
                		JSFParticipantProgram jsfProgram = new JSFParticipantProgram(programParticipant);
	                    if (jsfProgram.getEventsString().isEmpty())
	                        this.inEvent = false;
	                    else
	                        this.inEvent = true;
	                    if (programParticipant.getOptStatus() == 1)
	                        jsfProgram.setOptStatus("Off");
	                    
	                    else
	                        jsfProgram.setOptStatus("In");
	                    
	                    if (programParticipant.getApplyDayOfBaselineAdjustment() ==1)
	                        jsfProgram.setDayOfAdjustment(true);
	                    else
	                    	jsfProgram.setDayOfAdjustment(false);
	                    
	
	                    programs.add(jsfProgram);
                	}
            }else{
                 JSFParticipantProgram jsfProgram = new JSFParticipantProgram(programParticipant);
                 if (!jsfProgram.getProgramName().equalsIgnoreCase(TestProgram.PROGRAM_NAME)){
                    if (jsfProgram.getEventsString().isEmpty())
                        this.inEvent = false;
                    else
                        this.inEvent = true;
                    if (programParticipant.getOptStatus() == 1){
                    	jsfProgram.setOptStatus("Off");
                    	if(null !=programParticipant.getOptOutUntil()){
                    		jsfProgram.setOptOutUntil(programParticipant.getOptOutUntil());
                    		jsfProgram.setOptedOutFlag(true);
                    	}
                        
                    }
                    	
                    
                        
                    else{
                        jsfProgram.setOptStatus("In"); 
                    	if(!enableOptOut){
                    		jsfProgram.setEnableCalOptOut(false);
                    	}else if(enableOptOut){
                    		
                    	}                   		
                    	
                    }
                    if (programParticipant.getApplyDayOfBaselineAdjustment() ==1)
                        jsfProgram.setDayOfAdjustment(true);
                    else
                    	jsfProgram.setDayOfAdjustment(false);

                    programs.add(jsfProgram);
                 }
            }
        }
		nonConsolidationPrograms = new ArrayList(programs);
		programs = CBPUtil.transferJSFParticipantProgram(programs);
		
		
		return programs;
	}
	
	
        private boolean checkIsAggregator(ParticipantManager participantManager) {
        	
        	Principal userPrincipal = FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal();
			String uid = userPrincipal.getName();
			Participant participant = participantManager.getParticipant(uid);
			
			ProgramParticipantAggregationManager aggMan = (ProgramParticipantAggregationManager)EJBFactory.getBean(ProgramParticipantAggregationManagerBean.class);
	           int aggCount = 0;
	           for (ProgramParticipant ppp : participant.getProgramParticipants()) {
	                aggCount += aggMan.getDescendants(ppp).size();
	                if(aggCount > 1){
	                	break;
	                }
				}          
	         
	           
	           if (aggCount > 0 && !participant.getUserType().equals(UserType.SIMPLE) && !participant.getInstaller()) {
	        	   return true;
	           }	           
			
			return false;
		}

		private String getUserRole() {
        	String userRole=null;
    		
    		if (FacesContext.getCurrentInstance().getExternalContext()
    				.isUserInRole(DrasRole.Admin.toString())) {
    			userRole = DrasRole.Admin.toString();
    		} else if (FacesContext.getCurrentInstance().getExternalContext()
    				.isUserInRole(DrasRole.Operator.toString())) {
    			userRole = DrasRole.Operator.toString();
    		}else if (FacesContext.getCurrentInstance().getExternalContext()
    				.isUserInRole(DrasRole.FacilityManager.toString())) {
    			userRole = DrasRole.FacilityManager.toString();
    			
    		}else if (FacesContext.getCurrentInstance().getExternalContext()
    				.isUserInRole(DrasRole.UtilityOperator.toString())) {
    			userRole = DrasRole.UtilityOperator.toString();
    		}else if (FacesContext.getCurrentInstance().getExternalContext()
    				.isUserInRole(DrasRole.Readonly.toString())) {
    			userRole = DrasRole.Readonly.toString();
    			
    		} else  {
    			userRole = DrasRole.Dispatcher.toString();    		
    		}
    		
    		return userRole;
    	
		}

		public String getOptStatus() {
            return optStatus;
        }

        public void setOptStatus(String optStatus) {
            this.optStatus = optStatus;
        }



        /**
        * Edits the constraints action.
        *
        * @return the string
        */
        public String editConstraintsAction()
        {
                for(JSFParticipantProgram program: programs)
                {
                        if(program.getProgramName().equals(programName))
                        {
                                FDUtils.setJSFParticipantProgram(program);
                                program.editContstraints();
                                break;
                        }
                }
          return "editConstraints";
        }

        /**
        * Edits the constraints listener.
        *
        * @param e the e
        */
        public void editConstraintsListener(ActionEvent e)
        {
        programName = e.getComponent().getAttributes().
         get("programName").toString();
        }

        /**
        * Edits the bids action.
        *
        * @return the string
        */
        public String editBidsAction()
        {
                FDUtils.setBids(new Bids(programName, "", false));
                return "editBids";
        }

        /**
        * Edits the bids listener.
        *
        * @param e the e
        */
        public void editBidsListener(ActionEvent e)
        {
        programName = e.getComponent().getAttributes().
         get("programName").toString();
        }



        private static Map<String,Object> optValues;
        static{
            optValues = new LinkedHashMap<String,Object>();
            optValues.put("Opt-in", "In"); //label, value
            optValues.put("Opt-out", "Off");
        }

        public Map<String,Object> getOptValues() {
            return optValues;
        }

        
        private void checkForEvent(){
            ParticipantManager participantManager = EJBFactory.getBean(
                ParticipantManager.class);

            Set<EventParticipant> epList =
            participantManager.getParticipant(FDUtils.getParticipantName())
                .getEventParticipants();
			if (epList != null)
			{
				for (EventParticipant ep : epList)
				{
					if (this.programName.equals(ep.getEvent().getProgramName()))
					{
                        if (this.getOptStatus().equals("Off")){
                               FDUtils.addMsgInfo(
                                "This will opt you out from future events" +
                                " If you want to opt-out from the current event, go to event tab and click opt out button");
                            break;
                        }
					}
				}
			}
        }

    public void updateOptControl(){
          JSFParticipant jsfParticipant = FDUtils.getJSFParticipant();
          ProgramParticipantManager programParticipantManager = EJBFactory.getBean(
                 ProgramParticipantManager.class);
          
          
          if(CBPUtil.isEnableCBPConsolidation()&&CBPUtil.isCBPConsolidationProgram(this.programName)){
        	  List<JSFParticipantProgram> result = CBPUtil.getNonConsolidationPrograms(nonConsolidationPrograms);
        	  for(JSFParticipantProgram instance: result){
        		  String key = instance.getProgramName();
        		  updateOpt(programParticipantManager,jsfParticipant,key);  
        	  }
          }else{
        	  updateOpt(programParticipantManager,jsfParticipant,this.programName);
          }
    }
    
    public void updateDayOfAdjustmentControl(){
        JSFParticipant jsfParticipant = FDUtils.getJSFParticipant();
        ProgramParticipantManager programParticipantManager = EJBFactory.getBean(
               ProgramParticipantManager.class);
        updateDayOfAdjustment(programParticipantManager,jsfParticipant,this.programName);
       
  }
	
    private void updateOpt(ProgramParticipantManager programParticipantManager,JSFParticipant jsfParticipant,String programName){
    	 // update participant 
        ProgramParticipant pp =  programParticipantManager.getProgramParticipant(programName,
               jsfParticipant.getName(), false);

        int status = this.getOptStatus().equals("Off") ? 1 : 0;
        pp.setOptStatus(status);
        if(null!=this.getOptStatus() && this.getOptStatus().equals("Off")){
        	if(null ==this.optOutUntil){
            	pp.setOptOutUntil(new Date());
            }else{
            	pp.setOptOutUntil(this.optOutUntil);
            }        	
        
        	
        } else{
        	pp.setOptOutUntil(null);
        }
        
        
        programParticipantManager.updateProgramParticipant(pp.getProgramName(), pp.getParticipantName(), false, pp);
        
        // update clients
        List<ProgramParticipant> clientParts = programParticipantManager
        	.getProgramParticipantsByParent(programName, jsfParticipant.getName(), true);
        
        for (ProgramParticipant cp : clientParts) {
      	  cp.setOptStatus(status);
      	if(null!=this.getOptStatus() && this.getOptStatus().equals("Off")){
        	if(null ==this.optOutUntil){
        		cp.setOptOutUntil(new Date());
            }else{
            	cp.setOptOutUntil(this.optOutUntil);
            }
        }else{
        	cp.setOptOutUntil(null);
        }
      	
           programParticipantManager.updateProgramParticipant(cp.getProgramName(), cp.getParticipantName(), true, cp);
        }
        this.optOutUntil=new Date();
        this.checkForEvent(); 
    }
    
    private void updateDayOfAdjustment(ProgramParticipantManager programParticipantManager,JSFParticipant jsfParticipant,String programName){
      	 // update participant 
          ProgramParticipant pp =  programParticipantManager.getProgramParticipant(programName,
                 jsfParticipant.getName(), false);

          int dayOfAdjustment =  this.dayOfAdjustment ? 1:0;
          pp.setApplyDayOfBaselineAdjustment(dayOfAdjustment);
          programParticipantManager.updateProgramParticipant(pp.getProgramName(), pp.getParticipantName(), false, pp);
          
          // update clients
          List<ProgramParticipant> clientParts = programParticipantManager
          	.getProgramParticipantsByParent(programName, jsfParticipant.getName(), true);
          
          for (ProgramParticipant cp : clientParts) {
        	  cp.setApplyDayOfBaselineAdjustment(dayOfAdjustment);
              programParticipantManager.updateProgramParticipant(cp.getProgramName(), cp.getParticipantName(), true, cp);
          }

          
      }
	/**
	 * Edit Demand Limiting Participant Settings Action.
	 * 
	 * @return the string
	 */
	public String demandLimitingParticipantSettingsAction()
	{
		return "demandLimitingParticipantSettings";
	}
	
	/**
	 * Edit Demand Limiting Participant Settings Action.
	 * 
	 * @return the string
	 */
	public String demandLimitingDashboardAction()
	{
		return "demandLimitingDashboard";
	}
	
	/**
	 * Launch Demand Limiting Mock Meter.
	 * 
	 * @return the string
	 */
	public String demandLimitingMockMeterAction()
	{
		return "demandLimitingMockMeter";
	}
	
	

}
