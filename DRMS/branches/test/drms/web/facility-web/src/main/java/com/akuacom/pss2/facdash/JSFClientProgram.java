/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.facdash.JSFClientProgram.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.facdash;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import com.akuacom.ejb.client.EJB3Factory;
import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.core.SignalLevelMapper;
import com.akuacom.pss2.event.TimeBlock;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.participant.ParticipantManager;
import com.akuacom.pss2.participant.ParticipantRtpStrategy;
import com.akuacom.pss2.program.bidding.BiddingProgramManager;
import com.akuacom.pss2.program.cpp.CPPProgram;
import com.akuacom.pss2.program.cpp.CPPShedStrategy;
import com.akuacom.pss2.program.cpp.CPPUtils;
import com.akuacom.pss2.program.dbp.DBPProgram;
import com.akuacom.pss2.program.dbp.DBPUtils;
import com.akuacom.pss2.program.participant.ProgramParticipant;
import com.akuacom.pss2.program.participant.ProgramParticipantManager;
import com.akuacom.pss2.program.participant.ProgramParticipantRule;
import com.akuacom.pss2.program.rtp.RTPProgram;
import com.akuacom.pss2.program.scertp.RTPConfig;
import com.akuacom.pss2.program.scertp.RTPShedStrategy;
import com.akuacom.pss2.program.scertp.RTPShedStrategyEntry;
import com.akuacom.pss2.program.scertp.SCERTPProgram;
import com.akuacom.pss2.program.scertp.SCERTPUtils;
import com.akuacom.pss2.program.scertp2013.SCERTPProgramManager2013;
import com.akuacom.pss2.rule.Rule;
import com.akuacom.pss2.system.SystemManager;
import com.akuacom.pss2.system.property.PSS2Features;

/**
 * The Class JSFClientProgram.
 */
public class JSFClientProgram implements Serializable
{
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The name. */
	private ProgramParticipant clientParticipant;
	private List<JSFClientProgram> relativedClientParticipant = new ArrayList<JSFClientProgram>();
	/** The participating. */
	private boolean participating;

	/** The bid mapping exist. */
	private boolean bidMappingExist;

	/** The bid mappings. */
	private BidMapping[] bidMappings;

	private List<CPPShedStrategy> cppShedStrategies;

	/** The events string. */
	private String eventsString;

        private String programLabel;

	/** The rtp shed strategies exist. */
	private boolean cppShedStrategiesExist;

	/** The rtp shed strategies exist. */
	private boolean rtpShedStrategiesExist;

	/** The forecast exist. */
	private boolean forecastExist;

	/** The rule variable support by the program. */
	private List<String> ruleVariables;

	/** can the client be edited to participate in program */
	private boolean activated;
	
	private SceRtp2013BackingBean scertp2013BackingBean;
	
	private List<Participant> descendantClients;
	
	
	
	public List<Participant> getDescendantClients() {
		return descendantClients;
	}

	public void setDescendantClients(List<Participant> descendantClients) {
		this.descendantClients = descendantClients;
	}

	public void setActivated(boolean activated) {
		this.activated = activated;
	}

        public String getProgramLabel() {
              if (this.participating) this.programLabel = "Remove";
              else this.programLabel = "Enroll";
         return programLabel;
        }

        public void setProgramLabel(String programLabel) {
            this.programLabel = programLabel;
        }


	/**
	 * Instantiates a new jSF client program.
	 */
	public JSFClientProgram()
	{
	}

	/**
	 * Instantiates a new jSF client program.
	 *
     * @param clientParticipant client participant
     */

	public JSFClientProgram(ProgramParticipant clientParticipant, boolean isForcastEnabled,PSS2Features features)
	{
		init(clientParticipant,isForcastEnabled,features);
	}
	
	private void init(ProgramParticipant clientParticipant,boolean isForcastEnabled,PSS2Features features){    	

        this.clientParticipant = clientParticipant;
		loadBidMapping();
		if (clientParticipant.getProgram() instanceof SCERTPProgram)
		{
			this.rtpShedStrategiesExist = true;
			if (isForcastEnabled)
				this.forecastExist = true;
			
			//
			if(clientParticipant.getProgram().getClassName().equals("com.akuacom.pss2.program.scertp2013.SCERTPProgramEJB2013")){
				
				scertp2013BackingBean = new SceRtp2013BackingBean(clientParticipant);
				
			}
		}
		else if(clientParticipant.getProgram() instanceof RTPProgram)
		{
    		this.rtpShedStrategiesExist = true;            
			this.forecastExist = false;
        }
		else if(clientParticipant.getProgram() instanceof CPPProgram)
		{
			this.cppShedStrategiesExist = true;
			cppShedStrategies = CPPUtils.parseRules((CPPProgram)clientParticipant.getProgram(), 
				getRules(clientParticipant));
		}	
		
		if(features.isAggBatchUpdateEnabled()){
			com.akuacom.pss2.program.ProgramManager programManager =
					com.akuacom.pss2.core.EJBFactory
						.getBean(com.akuacom.pss2.program.ProgramManager.class);
			descendantClients = programManager.getDescendantClients(clientParticipant.getParticipant().getParent(), getConsolidationProgramName());
			boolean inList = false;
			for(Participant p:descendantClients){
				if(p.getParticipantName().equalsIgnoreCase(clientParticipant.getParticipantName())){
					inList = true;
					break;
				}
			}
			if(!inList){
				if(descendantClients==null) descendantClients = new ArrayList<Participant>();
				Participant client = new Participant();
				client.setUUID(clientParticipant.getParticipant().getUUID());
				client.setParticipantName(clientParticipant.getParticipantName());
				descendantClients.add(client);//add current selected client
			}			
			if(scertp2013BackingBean!=null)
			{
				scertp2013BackingBean.setDescendantClients(descendantClients);
			}
		}else{
			if(descendantClients==null) descendantClients = new ArrayList<Participant>();
			Participant client = new Participant();
			client.setUUID(clientParticipant.getParticipant().getUUID());
			client.setParticipantName(clientParticipant.getParticipantName());
			descendantClients.add(client);//add current selected client
		}
	}
	
	
    private List<ProgramParticipantRule> getRules(ProgramParticipant pgrmPart){
    	if (pgrmPart.getProgramParticipantRules() != null)
		{
			List<ProgramParticipantRule> rules = new ArrayList<ProgramParticipantRule>();
			for(ProgramParticipantRule rule: pgrmPart.getProgramParticipantRules())
			{
				rules.add(rule);
			}
			Collections.sort(rules, new Rule.SortOrderComparator());
			return rules;
		}
		else
		{
			return new ArrayList<ProgramParticipantRule>();
		}
    }	
	
	
	
	/**
	 * Load bids.
     */
	private void loadBidMapping()
	{
		if (clientParticipant.getProgram() instanceof DBPProgram)
		{
            bidMappingExist = true;

			final DBPProgram dbpProgram = (DBPProgram) clientParticipant.getProgram();
            final TimeBlock[] timeBlocks = DBPUtils.getTimeBlocks(dbpProgram.getBidConfig().getBidBlocks());

            BiddingProgramManager bpm = EJBFactory.getBean(BiddingProgramManager.class);
			final Map<String, List<String>> map = bpm.getLevelMap(
				clientParticipant.getProgramName(), clientParticipant.getParticipantName(), true);

			bidMappings = new BidMapping[timeBlocks.length];
            int i = 0;
            for (TimeBlock timeBlock : timeBlocks)
            {
                final String timeBlockString = SignalLevelMapper.getTimeBlock(timeBlock);
                bidMappings[i] = new BidMapping();
                bidMappings[i].setTimeBlock(timeBlockString);
                // if no client specific map, use default map defined in program.
                if (map == null)
                {
                    bidMappings[i].setNormal("x");
                    bidMappings[i].setModerate("x");
                    bidMappings[i].setHigh("1");
                }
                // use client's map
                else
                {
                    final List<String> rule = map.get(timeBlockString);
                    if (rule == null) {
                        bidMappings[i].setNormal("x");
                        bidMappings[i].setModerate("x");
                        bidMappings[i].setHigh("1");
                    } else {
                        bidMappings[i].setNormal(rule.get(0));
                        bidMappings[i].setModerate(rule.get(1));
                        bidMappings[i].setHigh(rule.get(2));
                    }
                }
                i++;
            }
		}
	}


	/**
	 * Save bid mapping action.
	 * 
	 * @return the string
	 */
	public String saveBidMappingAction()
	{
		BiddingProgramManager access =
			EJBFactory.getBean(BiddingProgramManager.class);
		final Map<String, List<String>> result =
			new HashMap<String, List<String>>();

		for (BidMapping mapping : bidMappings)
		{
			if(!validateBidMapping(mapping))
			{
				return null;
			}
			List<String> rules = new ArrayList<String>(3);
			String moderate = mapping.getModerate();
			String high = mapping.getHigh();
			String normal = "x";
			if("x".equalsIgnoreCase(moderate)&&"x".equalsIgnoreCase(high)){
				normal = "1";
			}
			rules.add(normal);
			rules.add(moderate);
			rules.add(high);
			result.put(mapping.getTimeBlock(), rules);
		}
		List<Participant> all = this.getDescendantClients();
		for(Participant p : all){
			access.setLevelMap(clientParticipant.getProgramName(), 
					p.getParticipantName(), true, result);
		}
		
		return "saveBidMapping";
	}

	/**
	 * Cancel bid mapping action.
	 * 
	 * @return the string
	 */
	public String cancelBidMappingAction()
	{
        this.loadBidMapping();
		return "cancelBidMapping";
	}

	/**
	 * 
	 * @return the string
	 */
	public String cancelCPPShedStrategiesAction()
	{
		return "cancelCPPShedStrategies";
	}

	public List<SelectItem> getCppShedStrategyAvailableModes()
	{
		List<SelectItem> availableModes = new ArrayList<SelectItem>();

	    SystemManager systemManager = EJB3Factory.getBean(SystemManager.class);
	    boolean specialEnabled = systemManager.getPss2Features().isSpecialModeEnabled();


        for(CPPShedStrategy.ShedMode operator: CPPShedStrategy.ShedMode.values())
	    {
            if(!specialEnabled && operator == CPPShedStrategy.ShedMode.SPECIAL)
            {
                continue;
            } else  if(operator == CPPShedStrategy.ShedMode.DEFAULT) {
                continue;
            }
            availableModes.add(new SelectItem(operator));
		}		
		return availableModes;
	}

	/**
	 * Validate bid mapping.
	 * 
	 * @param mapping the mapping
     * @return boolean
	 */
	private boolean validateBidMapping(BidMapping mapping)
	{
		boolean valid = true;
		
		// ActionErrors errors = new ActionErrors();
//		final double normal = extractValue(mapping.getNormal());
		String moderateString = mapping.getModerate();
		String highString = mapping.getHigh();
		String normalString = "x";
		if("x".equalsIgnoreCase(moderateString)&&"x".equalsIgnoreCase(highString)){
			normalString = "1";
		}
		final double normal = extractValue(normalString);
		final double moderate = extractValue(mapping.getModerate());
		final double high = extractValue(mapping.getHigh());

		// todo add more validation rules here
		List<Double> validRules = new ArrayList<Double>();

		final String timeBlock = mapping.getTimeBlock();
		if (normal == -2)
		{
			FDUtils.addMsgInfo("Invalid rule NORMAL for time block: "+ timeBlock);
			valid = false;
		}
		else if (normal > -1)
		{
			validRules.add(normal);
		}

		if (moderate == -2)
		{
			FDUtils.addMsgInfo("Invalid rule MODERATE for time block: "+ timeBlock);
			valid = false;
		}
		else if (moderate > -1)
		{
			validRules.add(moderate);
		}

		if (high == -2)
		{
			FDUtils.addMsgInfo("Invalid rule HIGH for time block: "+ timeBlock);
			valid = false;
		}
		else if (high > -1)
		{
			validRules.add(high);
		}

		if (validRules.size() == 0)
		{
			FDUtils.addMsgInfo("No rule: "+ timeBlock);
			valid = false;
		}
		else
		{
			if (validRules.get(0) != 1)
			{
				FDUtils.addMsgInfo("Base rule greater than one: " + timeBlock);
				valid = false;
			}
			if (validRules.size() > 1)
			{
				for (int i = 1; i < validRules.size(); i++)
				{
					if (validRules.get(i - 1) >= validRules.get(i))
					{
						FDUtils.addMsgInfo("Must be in ascending order: "+ timeBlock);
						valid = false;
					}
				}
			}
		}
		
		return valid;
	}

	/**
	 * Extract value.
	 * 
	 * @param s the s
	 * 
	 * @return the int
	 */
	private double extractValue(String s)
	{
		double t;
		try
		{
			t = new Double(s);
			if (t < 1)
			{
				t = -2;
			}
		}
		catch (NumberFormatException e)
		{
			if ("x".equals(s))
			{
				t = -1;
			}
			else
			{
				t = -2;
			}
		}
		return t;
	}

	/**
	 * Edits the rules.
	 */
	public void editCustomRules()
	{
		com.akuacom.pss2.program.ProgramManager programManager =
			com.akuacom.pss2.core.EJBFactory
				.getBean(com.akuacom.pss2.program.ProgramManager.class);
		ruleVariables = programManager.getProgramRuleVariables(
			clientParticipant.getProgramName());

		ProgramParticipantManager prgmPartManager =
                EJBFactory.getBean(ProgramParticipantManager.class);
		FDUtils.setRuleTable(new RuleTable(
                prgmPartManager.getRules(FDUtils.getJSFClient().getName(),
                clientParticipant.getProgramName())));
	}
			
    /**
	 * Edits the SCE RTP Shed Strategies.
	 */
	public void editRTPShedStrategies()
	{
		ProgramParticipantManager prgmPartManager =
                EJBFactory.getBean(ProgramParticipantManager.class);
		FDUtils.setRTPShedStrategies(new RTPShedStrategies(SCERTPUtils.parseRules(
            prgmPartManager.getRules(
            FDUtils.getJSFClient().getName(),
            clientParticipant.getProgramName()))));
	}

	/**
	 * Edits the SCE RTP Shed Strategies.
	 */
	public void viewForecast()
	{
	if(clientParticipant.getProgram().getClassName().equals("com.akuacom.pss2.program.scertp2013.SCERTPProgramEJB2013")){
		//FDUtils.setSCERTPForecast(new SCERTPForecast2013(clientParticipant.getProgramName()));
		FDUtils.setSCERTPForecast2013(new SCERTPForecast2013(clientParticipant.getProgramName()));
	}else{
		FDUtils.setSCERTPForecast(new SCERTPForecast(clientParticipant.getProgramName()));
	}
		
		
	}

	/**
	 * Gets the available operators.
	 * 
	 * @return the available operators
	 */
	public List<SelectItem> getAvailableOperators()
	{
		List<SelectItem> availableOperators = new ArrayList<SelectItem>();
		if (ruleVariables.size() == 0)
		{
			availableOperators.add(new SelectItem(Rule.Operator.ALWAYS
				.description()));
		}
		else
		{
			for (Rule.Operator operator : Rule.Operator.values())
			{
				availableOperators.add(new SelectItem(operator.description()));
			}
		}
		return availableOperators;
	}

	/**
	 * Gets the available variables.
	 * 
	 * @return the available variables
	 */
	public List<SelectItem> getAvailableVariables()
	{
		List<SelectItem> availableVariables = new ArrayList<SelectItem>();
		for (String variable : ruleVariables)
		{
			availableVariables.add(new SelectItem(variable));
		}
		return availableVariables;
	}

	/**
	 * Update rules.
	 * 
	 * @param rules the jsf rules
     * @param source source
     */
	public void updateRules(List<ProgramParticipantRule> rules,
        Rule.Source source)
	{
		ProgramParticipantManager prgmPartManager =
                EJBFactory.getBean(ProgramParticipantManager.class);
		
		List<Participant> allclients = this.getDescendantClients();
		for(Participant p: allclients){
			if(CBPUtil.isEnableCBPConsolidation()&&CBPUtil.isCBPConsolidationProgram(clientParticipant.getProgramName())){
				for(JSFClientProgram instance:relativedClientParticipant){
					prgmPartManager.addRules(p.getParticipantName(),instance.getProgramName(),rules, source);
				}
			}else{
				prgmPartManager.addRules(p.getParticipantName(),clientParticipant.getProgramName(),rules, source);	
			}
		}
       
	}

	public String saveCPPShedStrategiesAction()
	{
		updateRules(CPPUtils.parseCPPShedStrategies(cppShedStrategies),
            Rule.Source.CPP_SHED_STRATEGY);

		return "saveCPPShedStrategies";
	}

	/**
	 * Update rules.
	 * 
     * @param jsfStrategies shed strategies
     *
     * @return the string
	 */
	public String updateRTPShedStrategies(List<JSFRTPShedStrategy> jsfStrategies)
	{
		// downcast list for rmi
		List<RTPShedStrategy> stratagies = new ArrayList<RTPShedStrategy>();
		for(JSFRTPShedStrategy jsfStartegy: jsfStrategies)
		{
			stratagies.add(jsfStartegy.getStrategy());
		}
		updateRules(SCERTPUtils.parseRTPShedStrategies(stratagies),
            Rule.Source.SCERTP_SHED_STRATEGY);

		return "saveStrategies";
	}

	/**
	 * Gets the name.
	 * 
	 * @return the name
	 */
	public String getProgramName()
	{	
		return clientParticipant.getProgramName();
	}
	public String getConsolidationProgramName()
	{
		if(CBPUtil.isEnableCBPConsolidation()){
			String key = clientParticipant.getProgramName();
			if(CBPUtil.getCbpGroup().get("CBP").contains(key)){
				return "CBP";
            }else{
            	return key;
            }
		}else{
			return clientParticipant.getProgramName();
		}
	}

	/**
	 * Checks if is participating.
	 * 
	 * @return true, if is participating
	 */
	public boolean isParticipating()
	{
		return participating;
	}

	/**
	 * Sets the participating.
	 * 
	 * @param participating the new participating
	 */
	public void setParticipating(boolean participating)
	{
		this.participating = participating;
	}

	/**
	 * Checks if is bid mapping exist.
	 * 
	 * @return true, if is bid mapping exist
	 */
	public boolean isBidMappingExist()
	{
		return bidMappingExist;
	}

	/**
	 * Sets the bid mapping exist.
	 * 
	 * @param bidMappingExist the new bid mapping exist
	 */
	public void setBidMappingExist(boolean bidMappingExist)
	{
		this.bidMappingExist = bidMappingExist;
	}

	/**
	 * Gets the bid mappings.
	 * 
	 * @return the bid mappings
	 */
	public BidMapping[] getBidMappings()
	{
		return bidMappings;
	}

	/**
	 * Sets the bidmappings.
	 * 
	 * @param bidMappings the new bidmappings
	 */
	public void setBidmappings(BidMapping[] bidMappings)
	{
		this.bidMappings = bidMappings;
	}

	/**
	 * Gets the events string.
	 * 
	 * @return the events string
	 */
	public String getEventsString()
	{
		return eventsString;
	}

	/**
	 * Sets the events string.
	 * 
	 * @param eventsString the new events string
	 */
	public void setEventsString(String eventsString)
	{
		this.eventsString = eventsString;
	}

	/**
	 * Checks if is rtp shed strategies exist.
	 * 
	 * @return true, if is rtp shed strategies exist
	 */
	public boolean isRtpShedStrategiesExist()
	{
		return rtpShedStrategiesExist;
	}

	/**
	 * Sets the rtp shed strategies exist.
	 * 
	 * @param rtpShedStrategiesExist the new rtp shed strategies exist
	 */
	public void setRtpShedStrategiesExist(boolean rtpShedStrategiesExist)
	{
		this.rtpShedStrategiesExist = rtpShedStrategiesExist;
	}

	/**
	 * @return the ruleVariables
	 */
	public List<String> getRuleVariables()
	{
		return ruleVariables;
	}

	public boolean isForecastExist()
	{
		return forecastExist;
	}

	public boolean isCppShedStrategiesExist()
	{
		return cppShedStrategiesExist;
	}

	public void setCppShedStrategiesExist(boolean cppShedStrategiesExist)
	{
		this.cppShedStrategiesExist = cppShedStrategiesExist;
	}

	public List<CPPShedStrategy> getCppShedStrategies()
	{
		return cppShedStrategies;
	}

	public void setShedStrategies(List<CPPShedStrategy> cppShedStrategies)
	{
		this.cppShedStrategies = cppShedStrategies;
	}

	public boolean isActivated() {
		return activated;
	}

	public SceRtp2013BackingBean getScertp2013BackingBean() {
		return scertp2013BackingBean;
	}

	public void setScertp2013BackingBean(SceRtp2013BackingBean scertp2013BackingBean) {
		this.scertp2013BackingBean = scertp2013BackingBean;
	}

	/**
	 * @return the relativedClientParticipant
	 */
	public List<JSFClientProgram> getRelativedClientParticipant() {
		return relativedClientParticipant;
	}

	/**
	 * @param relativedClientParticipant the relativedClientParticipant to set
	 */
	public void setRelativedClientParticipant(
			List<JSFClientProgram> relativedClientParticipant) {
		this.relativedClientParticipant = relativedClientParticipant;
	}
	
	

}
