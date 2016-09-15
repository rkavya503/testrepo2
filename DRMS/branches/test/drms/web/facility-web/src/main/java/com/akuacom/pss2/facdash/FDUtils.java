/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.facdash.FDUtils.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */

package com.akuacom.pss2.facdash;

import java.io.IOException;
import java.security.Principal;
import java.util.Iterator;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.system.SystemManager;
import com.akuacom.pss2.system.SystemManagerBean;
import com.akuacom.pss2.web.aggregation.AggregationTree;

/**
 * The Class FDUtils.
 */
public class FDUtils
{
	
	/**
	 * Gets the participant name.
	 * 
	 * @return the participant name
	 */
	static public String getParticipantName()
	{
		JSFParticipant participant =
			(JSFParticipant) FacesContext.getCurrentInstance()
				.getExternalContext().getSessionMap().get("participant");
		return participant.getName();
	}
	
	/**
	 * Adds the msg info.
	 * 
	 * @param message the message
	 */
	static public void addMsgInfo(String message) {
        addMsgInfo(null, message);
	}

	static public void addMsgInfo(String forTarget, String message) {
        addMsg(forTarget, message, FacesMessage.SEVERITY_INFO);
	}

    /**
   	 * Adds the msg error.
   	 *
   	 * @param message
   	 *            the message
   	 */
   	static public void addMsgError(String message) {
        addMsgError(null, message);
   	}

   	static public void addMsgError(String forTarget, String message) {
   		addMsg(forTarget, message, FacesMessage.SEVERITY_ERROR);
   	}

    private static void addMsg(String forTarget, String message, FacesMessage.Severity severity) {
        FacesContext fc = FacesContext.getCurrentInstance();
        if (!isDuplicateMessage(forTarget, message)) {
            fc.addMessage(forTarget, new FacesMessage(severity, message, message));
        }
    }

    private static boolean isDuplicateMessage(String forTarget, String message) {
        FacesContext fc = FacesContext.getCurrentInstance();
        boolean flag = false;
        if (forTarget != null && !"".equals(forTarget)) {
            Iterator<FacesMessage> it = fc.getMessages(forTarget);
            while (it.hasNext()) {
                String detail = it.next().getDetail();
                if (detail.equalsIgnoreCase(message)) {
                    flag = true;
                }
            }
        } else {
            Iterator<FacesMessage> it = fc.getMessages();
            while (it.hasNext()) {
                String detail = it.next().getDetail();
                if (detail.equalsIgnoreCase(message)) {
                    flag = true;
                }
            }
        }

        return flag;
    }

    public static void clearMsg(String forTarget) {
        FacesContext fc = FacesContext.getCurrentInstance();
        Iterator<FacesMessage> iterator = fc.getMessages(forTarget);
        while (iterator.hasNext()) {
            iterator.next();
            iterator.remove();
        }
    }

	/**
	 * Gets the header1.
	 * 
	 * @return the header1
	 */
	static public Header1 getHeader1()
	{
		return (Header1)FacesContext.getCurrentInstance().
				getExternalContext().getSessionMap().get("header1");
	}

	/**
	 * Sets the header1.
	 * 
	 * @param header1 the new header1
	 */
	static public void setHeader1(Header1 header1)
	{
		FacesContext.getCurrentInstance().
				getExternalContext().getSessionMap().put("header1", header1);
	}

	/**
	 * Gets the jSF participant.
	 * 
	 * @return the jSF participant
	 */
	static public JSFParticipant getJSFParticipant()
	{
		return (JSFParticipant)FacesContext.getCurrentInstance().
				getExternalContext().getSessionMap().get("participant");
	}

	/**
	 * Sets the jSF participant.
	 * 
	 * @param participant the new jSF participant
	 */
	static public void setJSFParticipant(JSFParticipant participant)
	{
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
			.put("participant", participant);
	}

	/**
	 * Gets the program table.
	 * 
	 * @return the program table
	 */
	static public ProgramTable getProgramTable()
	{
		return (ProgramTable)FacesContext.getCurrentInstance().
				getExternalContext().getSessionMap().get("programs");
	}

	/**
	 * Sets the program table.
	 * 
	 * @param programs the new program table
	 */
	static public void setProgramTable(ProgramTable programs)
	{
		FacesContext.getCurrentInstance().
				getExternalContext().getSessionMap().put("programs", programs);
	}

	/**
	 * Gets the forecast table.
	 * 
	 * @return the forecast table
	 */
	static public ForecastTable getForecastTable()
	{
		return (ForecastTable)FacesContext.getCurrentInstance().
				getExternalContext().getSessionMap().get("forecasts");
	}

	/**
	 * Sets the forecast table.
	 * 
	 * @param forecasts the new forecast table
	 */
	static public void setForecastTable(ForecastTable forecasts)
	{
		FacesContext.getCurrentInstance().
				getExternalContext().getSessionMap().put("forecasts", forecasts);
	}
	
	/**
	 * Gets the event table.
	 * 
	 * @return the event table
	 */
	static public EventTable getEventTable()
	{
		return (EventTable)FacesContext.getCurrentInstance().
				getExternalContext().getSessionMap().get("events");
	}

	/**
	 * Sets the event table.
	 * 
	 * @param events the new event table
	 */
	static public void setEventTable(EventTable events)
	{
		FacesContext.getCurrentInstance().
				getExternalContext().getSessionMap().put("events", events);
	}
	
	/**
	 * Gets the jSF client.
	 * 
	 * @return the jSF client
	 */
	static public JSFClient getJSFClient()
	{
		JSFClient jsfClinet = (JSFClient)FacesContext.getCurrentInstance().
		getExternalContext().getSessionMap().get("client");		
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		if(externalContext.isUserInRole("Admin") || externalContext.isUserInRole("Operator")){
			jsfClinet.setAdminOrOperator(true);
		}		
		return jsfClinet;
	/*	return (JSFClient)FacesContext.getCurrentInstance().
				getExternalContext().getSessionMap().get("client");*/
		
	}

	/**
	 * Sets the jSF client.
	 * 
	 * @param client the new jSF client
	 */
	static public void setJSFClient(JSFClient client)
	{
		FacesContext.getCurrentInstance().
				getExternalContext().getSessionMap().put("client", client);
	}



    static public void setJSFProgram(JSFProgram program)
	{
		FacesContext.getCurrentInstance().
				getExternalContext().getSessionMap().put("program", program);
	}


    static public JSFProgram  getJSFProgram()
	{
		return (JSFProgram)FacesContext.getCurrentInstance().
				getExternalContext().getSessionMap().get("program");
	}
    

    static public void setJSFInstaller(Boolean installer)
	{
		FacesContext.getCurrentInstance().
				getExternalContext().getSessionMap().put("installer", installer);
	}


    static public Boolean  getJSFInstaller()
	{
		return (Boolean)FacesContext.getCurrentInstance().
				getExternalContext().getSessionMap().get("installer");
	}


	/**
	 * Gets the jSF participant program.
	 * 
	 * @return the jSF participant program
	 */
	static public JSFParticipantProgram getJSFParticipantProgram()
	{
		return (JSFParticipantProgram)FacesContext.getCurrentInstance().
				getExternalContext().getSessionMap().get("participantProgram");
	}

	/**
	 * Sets the jSF participant program.
	 * 
	 * @param participantProgram the new jSF participant program
	 */
	static public void setJSFParticipantProgram(
		JSFParticipantProgram participantProgram)
	{
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
			.put("participantProgram", participantProgram);
	}

	/**
	 * Gets the jSF client program.
	 * 
	 * @return the jSF client program
	 */
	static public JSFClientProgram getJSFClientProgram()
	{
		return (JSFClientProgram)FacesContext.getCurrentInstance().
				getExternalContext().getSessionMap().get("clientProgram");
	}

	/**
	 * Sets the jSF client program.
	 * 
	 * @param clientProgram the new jSF client program
	 */
	static public void setJSFClientProgram(JSFClientProgram clientProgram)
	{
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
			.put("clientProgram", clientProgram);
	}
	
	/**
	 * Gets the jSF client program.
	 * 
	 * @return the jSF client event
	 */
	static public JSFClientProgram getJSFClientEvent()
	{
		return (JSFClientProgram)FacesContext.getCurrentInstance().
				getExternalContext().getSessionMap().get("clientEvent");
	}

	/**
	 * Sets the jSF client program.
	 * 
	 * @param clientEvent the new jSF client event
	 */
	static public void setJSFClientEvent(JSFClientEvent clientEvent)
	{
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
			.put("clientEvent", clientEvent);
	}

	static public JSFEvent getJSFEvent()
	{
		return (JSFEvent)FacesContext.getCurrentInstance().
				getExternalContext().getSessionMap().get("event");
	}

	static public void setJSFEvent(JSFEvent event)
	{
		FacesContext.getCurrentInstance().
				getExternalContext().getSessionMap().put("event", event);
	}
	
	static public ClientTable getClientTable()
	{
		return (ClientTable)FacesContext.getCurrentInstance().
				getExternalContext().getSessionMap().get("clients");
	}

	/**
	 * Sets the client table.
	 * 
	 * @param clients the new client table
	 */
	static public void setClientTable(ClientTable clients)
	{
		FacesContext.getCurrentInstance().
				getExternalContext().getSessionMap().put("clients", clients);
	}
	
	/**
	 * Gets the rule table.
	 * 
	 * @return the rule table
	 */
	static public RuleTable getRuleTable()
	{
		return (RuleTable)FacesContext.getCurrentInstance().
				getExternalContext().getSessionMap().get("rules");
	}

	/**
	 * Sets the rule table.
	 * 
	 * @param rules the new rule table
	 */
	static public void setRuleTable(RuleTable rules)
	{
		FacesContext.getCurrentInstance().
				getExternalContext().getSessionMap().put("rules", rules);
	}
	
	static public EventRuleTable getEventRuleTable()
	{
		return (EventRuleTable)FacesContext.getCurrentInstance().
				getExternalContext().getSessionMap().get("eventRules");
	}

	static public void setEventRuleTable(EventRuleTable eventRules)
	{
		FacesContext.getCurrentInstance().
				getExternalContext().getSessionMap().put("eventRules", eventRules);
	}
	
	static public JSFRule getJSFRule()
	{
		return (JSFRule)FacesContext.getCurrentInstance().
				getExternalContext().getSessionMap().get("rule");
	}

	/**
	 * Sets the jSF rule.
	 * 
	 * @param rule the new jSF rule
	 */
	static public void setJSFRule(JSFRule rule)
	{
		FacesContext.getCurrentInstance().
				getExternalContext().getSessionMap().put("rule", rule);
	}
	
	static public JSFEventRule getJSFEventRule()
	{
		return (JSFEventRule)FacesContext.getCurrentInstance().
				getExternalContext().getSessionMap().get("eventRule");
	}

	static public void setJSFEventRule(JSFEventRule eventRule)
	{
		FacesContext.getCurrentInstance().
				getExternalContext().getSessionMap().put("eventRule", eventRule);
	}
	
	static public JSFConstraints getJSFConstraints()
	{
		return (JSFConstraints)FacesContext.getCurrentInstance().
				getExternalContext().getSessionMap().get("constraints");
	}

	/**
	 * Sets the jSF constraints.
	 * 
	 * @param constraints the new jSF constraints
	 */
	static public void setJSFConstraints(JSFConstraints constraints)
	{
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
			.put("constraints", constraints);
	}
	
	/**
	 * Gets the about.
	 * 
	 * @return the about
	 */
	static public About getAbout()
	{
		return (About)FacesContext.getCurrentInstance().
				getExternalContext().getRequestMap().get("about");
	}

	/**
	 * Sets the about.
	 * 
	 * @param about the new about
	 */
	static public void setAbout(About about)
	{
		FacesContext.getCurrentInstance().
				getExternalContext().getRequestMap().put("about", about);
	}
	
	/**
	 * Gets the options.
	 * 
	 * @return the options
	 */
	static public Options getOptions()
	{
		return (Options)FacesContext.getCurrentInstance().
				getExternalContext().getSessionMap().get("options");
	}

	/**
	 * Sets the options.
	 * 
	 * @param options the new options
	 */
	static public void setOptions(Options options)
	{
		FacesContext.getCurrentInstance().
				getExternalContext().getSessionMap().put("options", options);
	}
	
	/**
	 * Gets the jSF contact.
	 * 
	 * @return the jSF contact
	 */
	static public JSFContact getJSFContact()
	{
		return (JSFContact)FacesContext.getCurrentInstance().
				getExternalContext().getSessionMap().get("contact");
	}

	/**
	 * Sets the jSF contact.
	 * 
	 * @param contact the new jSF contact
	 */
	static public void setJSFContact(JSFContact contact)
	{
		FacesContext.getCurrentInstance().
				getExternalContext().getSessionMap().put("contact", contact);
	}
	
	/**
	 * Gets the jSF account.
	 * 
	 * @return the jSF account
	 */
	static public JSFAccount getJSFAccount()
	{
		return (JSFAccount)FacesContext.getCurrentInstance().
				getExternalContext().getSessionMap().get("account");
	}

	/**
	 * Sets the jSF account.
	 * 
	 * @param account the new jSF account
	 */
	static public void setJSFAccount(JSFAccount account)
	{
		FacesContext.getCurrentInstance().
				getExternalContext().getSessionMap().put("account", account);
	}
	
	/**
	 * Gets the rTP shed strategies.
	 * 
	 * @return the rTP shed strategies
	 */
	static public RTPShedStrategies getRTPShedStrategies()
	{
		return (RTPShedStrategies)FacesContext.getCurrentInstance().
				getExternalContext().getSessionMap().get("rtpShedStrategies");
	}

	/**
	 * Sets the rTP shed strategies.
	 * 
	 * @param rtpShedStrategies the new rTP shed strategies
	 */
	static public void setRTPShedStrategies(RTPShedStrategies rtpShedStrategies)
	{
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
			.put("rtpShedStrategies", rtpShedStrategies);
	}
	
	/**
	 * Gets the rTP shed strategy.
	 * 
	 * @return the rTP shed strategy
	 */
	static public JSFRTPShedStrategy getRTPShedStrategy()
	{
		return (JSFRTPShedStrategy)FacesContext.getCurrentInstance().
				getExternalContext().getSessionMap().get("rtpShedStrategy");
	}

	/**
	 * Sets the rTP shed strategy.
	 * 
	 * @param rtpShedStrategy the new rTP shed strategy
	 */
	static public void setRTPShedStrategy(JSFRTPShedStrategy rtpShedStrategy)
	{
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
			.put("rtpShedStrategy", rtpShedStrategy);
	}	

	/**
	 * Gets the sce rtp forecast.
	 * 
	 * @return the sce rtp forecast
	 */
	static public SCERTPForecast getSCERTPForecast()
	{
		return (SCERTPForecast)FacesContext.getCurrentInstance().
				getExternalContext().getSessionMap().get("sceRTPForecast");
	}

	/**
	 * Sets the sce rtp forecast.
	 * 
	 * @param sceRTPForecast the sce rtp forecast
	 */
	static public void setSCERTPForecast(SCERTPForecast sceRTPForecast)
	{
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
			.put("sceRTPForecast", sceRTPForecast);
	}
	
	/**
	 * Gets the sce rtp forecast.
	 * 
	 * @return the sce rtp forecast
	 */
	static public SCERTPForecast2013 getSCERTPForecast2013()
	{
		return (SCERTPForecast2013)FacesContext.getCurrentInstance().
				getExternalContext().getSessionMap().get("sceRTPForecast2013");
	}

	/**
	 * Sets the sce rtp forecast.
	 * 
	 * @param sceRTPForecast the sce rtp forecast
	 */
	static public void setSCERTPForecast2013(SCERTPForecast2013 sceRTPForecast)
	{
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
			.put("sceRTPForecast2013", sceRTPForecast);
	}

	/**
	 * Gets the sce rtp forecast.
	 * 
	 * @return the sce rtp forecast
	 */
	static public SubAccounts getSubAccounts()
	{
		return (SubAccounts)FacesContext.getCurrentInstance().
				getExternalContext().getSessionMap().get("subAccounts");
	}

	/**
	 * Sets the sce rtp forecast.
	 * 
	 * @param subAccounts the sce rtp forecast
	 */
	static public void setSubAccounts(SubAccounts subAccounts)
	{
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
			.put("subAccounts", subAccounts);
	}

	/**
	 * Gets the bids.
	 * 
	 * @return the bids
	 */
	static public Bids getBids()
	{
		return (Bids)FacesContext.getCurrentInstance().
				getExternalContext().getSessionMap().get("bids");
	}

	/**
	 * Sets the bids.
	 * 
	 * @param bids bids
	 */
	static public void setBids(Bids bids)
	{
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
			.put("bids", bids);
	}
    static public void reDirect(String url) throws IOException{
         HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().
                    getExternalContext().getResponse();
         response.sendRedirect(url);
    }
	static public AggregationTree getAggregationTree()
	{
		return (AggregationTree)FacesContext.getCurrentInstance().
				getExternalContext().getSessionMap().get("aggTree");
	}

	/**
	 * Sets the bids.
	 * 
	 * @param aggregationTree aggregationTree
	 */
	static public void setAggregationTree(AggregationTree aggregationTree)
	{
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
			.put("aggTree", aggregationTree);
	}
	
	static public boolean isTelemetryTomorrowEnabled(){
		SystemManager systemManager = EJBFactory.getBean(SystemManagerBean.class);
		return systemManager.getPss2Features().isFeatureTelemetryTomorrowEnabled();
	}
}












