// $Revision: 1.1 $ $Date: 2000/01/01 00:00:00 $
package com.akuacom.pss2.facdash;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.akuacom.ejb.client.EJB3Factory;
import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.core.ErrorUtil;
import com.akuacom.pss2.core.ProgramValidationException;
import com.akuacom.pss2.core.ProgramValidationMessage;
import com.akuacom.pss2.core.SignalLevelMapper;
import com.akuacom.pss2.core.ValidationException;
import com.akuacom.pss2.event.EventManager;
import com.akuacom.pss2.event.TimeBlock;
import com.akuacom.pss2.event.participant.EventParticipant;
import com.akuacom.pss2.event.participant.EventParticipantManager;
import com.akuacom.pss2.program.Program;
import com.akuacom.pss2.program.ProgramManager;
import com.akuacom.pss2.program.bidding.BiddingProgramManager;
import com.akuacom.pss2.program.dbp.BidEntry;
import com.akuacom.pss2.program.dbp.DBPEvent;
import com.akuacom.pss2.program.dbp.DBPProgram;
import com.akuacom.pss2.program.dbp.DBPUtils;
import com.akuacom.pss2.util.ModeSignal;

public class Bids
{
	String title;
	String programName;
	String eventName;
	boolean current;
	private Bid[] bids;
	private boolean displayActive;
    private boolean readOnly;
    private boolean dayOfAdjustment;
    private boolean displayBaslineAdjustment;
	public Bids()
	{
	}

	public Bids(String programName, String eventName, boolean current)
	{
		this.programName = programName;
		this.eventName = eventName;
		this.current = current;
		loadBids();
		if(!eventName.isEmpty()){
			displayBaslineAdjustment = true;
			onload();
		}
	}
	
	/**
	 * Load bids.
	 */
	public void loadBids()
	{
		ProgramManager programManager = EJBFactory.getLocalBean(ProgramManager.class);

        Program program = programManager.getProgramWithLoadBid(programName);
		if(program instanceof DBPProgram)
		{
			DBPProgram dbpProgram = (DBPProgram)program;
			BiddingProgramManager biddingProgramManager = 
				EJB3Factory.getLocalBean(BiddingProgramManager.class);
			List<BidEntry> bidEntries;
            DBPEvent event = null;
            if(current)
			{
                EventManager eventManager = EJBFactory.getBean(EventManager.class);
                event = (DBPEvent)eventManager.getEvent(programName, eventName);
                bidEntries = biddingProgramManager.getCurrentBid(
					programName, eventName, FDUtils.getParticipantName(), false);
                readOnly = biddingProgramManager.isBidAccepted(programName, event, FDUtils.getParticipantName(), false)
                        || new Date().after(event.getDrasRespondBy());
			}
			else
			{
				bidEntries = biddingProgramManager.getDefaultBid(
					programName, FDUtils.getParticipantName(), false);				
                readOnly = false;
			}
			
			TimeBlock[] timeBlocks = DBPUtils.getTimeBlocks(dbpProgram.getBidConfig().getBidBlocks());
			if(programName.startsWith("PeakChoice"))
			{
				displayActive = false;
				// display a single bid block that spans the entire timeframe
				TimeBlock timeBlock = new TimeBlock();
				timeBlock.setStartHour(timeBlocks[0].getStartHour());
				timeBlock.setStartMinute(timeBlocks[0].getStartMinute());
				timeBlock.setStartSecond(timeBlocks[0].getStartSecond());
				if(timeBlocks[timeBlocks.length-1].getEndHour() == 24)
				{
					timeBlock.setEndHour(23);
					timeBlock.setEndMinute(59);
					timeBlock.setEndSecond(59);
				} 
				else
				{
					timeBlock.setEndHour(timeBlocks[timeBlocks.length-1].getEndHour());
					timeBlock.setEndMinute(timeBlocks[timeBlocks.length-1].getEndMinute());
					timeBlock.setEndSecond(timeBlocks[timeBlocks.length-1].getEndSecond());
				}
				// all bids must be the same, so just use the first one
				bids = new Bid[1];
				bids[0] = new Bid();
    			bids[0].setTimeBlock(
    				SignalLevelMapper.getTimeBlock(timeBlock));
    			bids[0].setReductionKW(bidEntries.get(0).getReductionKW());
    			bids[0].setLevel(ModeSignal.getLevelString(
    				bidEntries.get(0).getPriceLevel()));	
			}
			else
			{
				displayActive = true;
                if (event != null) {
                    Date startTime = event.getStartTime();
                    long start = startTime.getTime();
                    Date endTime = event.getEndTime();
                    long end = endTime.getTime();
                    int size = (int) ((end - start) / 3600 / 1000);
                    bids = new Bid[size];
                    int i = 0;
                    for (TimeBlock timeBlock : timeBlocks)
                    {
                        for(BidEntry bidEntry: bidEntries)
                        {
                            TimeBlock bidTimeBlock = new TimeBlock(
                                    bidEntry.getBlockStart(), bidEntry.getBlockEnd());
                            if(bidTimeBlock.compareTo(timeBlock) == 0 &&
                                    !bidEntry.getBlockStart().before(startTime) &&
                                    !bidEntry.getBlockEnd().after(endTime))
                            {
                                bids[i] = new Bid();
                                bids[i].setTimeBlock(
                                        SignalLevelMapper.getTimeBlock(timeBlock));
                                bids[i].setReductionKW(bidEntry.getReductionKW());
                                bids[i].setLevel(ModeSignal.getLevelString(
                                        bidEntry.getPriceLevel()));
                                bids[i].setActive(bidEntry.isActive());
                                i++;
                            }
                        }
                    }
                } else {
                    bids = new Bid[bidEntries.size()];
                    int i = 0;
                    for (TimeBlock timeBlock : timeBlocks)
                    {
                        for(BidEntry bidEntry: bidEntries)
                        {
                            TimeBlock bidTimeBlock = new TimeBlock(
                                    bidEntry.getBlockStart(), bidEntry.getBlockEnd());
                            if(bidTimeBlock.compareTo(timeBlock) == 0)
                            {
                                bids[i] = new Bid();
                                bids[i].setTimeBlock(
                                        SignalLevelMapper.getTimeBlock(timeBlock));
                                bids[i].setReductionKW(bidEntry.getReductionKW());
                                bids[i].setLevel(ModeSignal.getLevelString(
                                        bidEntry.getPriceLevel()));
                                bids[i].setActive(bidEntry.isActive());
                            }
                        }
                        i++;
                    }
                }
			}
		} 
	}

	/**
	 * Save bids action.
	 * 
	 * @return the string
	 */
	public String saveBidsAction()
	{
		try 
		{
			BiddingProgramManager biddingProgramManager =
				EJB3Factory.getLocalBean(BiddingProgramManager.class);
			List<BidEntry> bidEntries;
            DBPEvent event = null;
            if(current)
			{
                EventManager eventManager = EJBFactory.getBean(EventManager.class);
                event = (DBPEvent)eventManager.getEvent(programName, eventName);
                if(biddingProgramManager.isBidAccepted(
                    programName, event, FDUtils.getParticipantName(), false))
                {
                    FDUtils.addMsgError("Bids were accepted prior to saving - click cancel to exit");
                    return null;
                }
                
				bidEntries = biddingProgramManager.getCurrentBid(programName,
					eventName, FDUtils.getParticipantName(), false);
			}
			else
			{
				bidEntries = biddingProgramManager.getDefaultBid(programName,
					FDUtils.getParticipantName(), false);
			}
			if(programName.startsWith("PeakChoice"))
			{
				// copy the one ui object into each bid entry
	        	for(BidEntry bidEntry: bidEntries)
	        	{
        			bidEntry.setReductionKW(bids[0].getReductionKW());
        			bidEntry.setPriceLevel(
        				ModeSignal.getLevelValue(bids[0].getLevel()));
        			bidEntry.setActive(true);
	        	}
			}
			else
			{
                for (BidEntry bidEntry : bidEntries) {
                    for (Bid bid : bids) {
                        String start = bid.getTimeBlock().substring(0, 2); // timeblock format: 12:00-13:00
                        if(start.startsWith("0")){
                        	start = StringUtils.stripStart(start, "0");
                        }
                        if (start.equals(bidEntry.getBlockStart().getHours() + "")) {
                            bidEntry.setReductionKW(bid.getReductionKW());
                            bidEntry.setPriceLevel(ModeSignal.getLevelValue(bid.getLevel()));
                            bidEntry.setActive(bid.isActive());
                        }
                    }
                }
			}
			if(current)
			{
		        biddingProgramManager.setCurrentBid(programName, eventName,
                        FDUtils.getParticipantName(), false, bidEntries, false);
			}
			else
			{
		        biddingProgramManager.setDefaultBid(programName,
                        FDUtils.getParticipantName(), false, bidEntries);
			}
		}
		catch (Exception e)
		{
			ValidationException ve = ErrorUtil.getValidationException(e);
			if (ve != null)
			{
				FDUtils.addMsgError(ve.getLocalizedMessage());
			}
			else
			{
                ProgramValidationException pve = ErrorUtil.getProgramValidationException(e);
                if (pve != null) {
                    List<ProgramValidationMessage> errors = pve.getErrors();
                    for (ProgramValidationMessage pvm : errors) {
                        FDUtils.addMsgError(pvm.getParameterName() + ", " + pvm.getDescription());
                    }
                } else {
                    FDUtils.addMsgError("Internal error");
                }
			}
			return null;
		}

		if(current)
		{
			return "saveCurrentBids";
		}
		else
		{
			return "saveDefaultBids";			
		}
	}
	
/*	public void updateDayOfAdjustmentControl(){
		//System.out.println(this.dayOfAdjustment);
        JSFParticipant jsfParticipant = FDUtils.getJSFParticipant();
        EventParticipantManager eventParticipantManager = EJB3Factory.getBean(EventParticipantManager.class);
        updateDayOfAdjustment(eventParticipantManager,jsfParticipant,this.eventName);
       
  }
	
	private void updateDayOfAdjustment(EventParticipantManager eventParticipantManager,JSFParticipant jsfParticipant,String eventName){
	   	 // update participant 
	       EventParticipant ep = eventParticipantManager.getEventParticipant(eventName, jsfParticipant.getName(), false);

	       int dayOfAdjustment =  this.dayOfAdjustment ? 1:0;
	       ep.setApplyDayOfBaselineAdjustment(dayOfAdjustment);
	       eventParticipantManager.updateEventParticipant(eventName, ep.getParticipant().getParticipantName(), false, ep);
	       
	   }*/

	/**
	 * Cancel bids action.
	 * 
	 * @return the string
	 */
	public String cancelBidsAction()
	{
		if(current)
		{
			return "cancelCurrentBids";
		}
		else
		{
			return "cancelDefaultBids";			
		}
	}
	
	/**
	 * Gets the bids.
	 * 
	 * @return the bids
	 */
	public Bid[] getBids()
	{
		return bids;
	}

	public boolean isDisplayActive()
	{
		return displayActive;
	}

	public String getTitle()
	{
		StringBuilder sb = new StringBuilder();
		if(current)
		{
			sb.append("Current \n");
		}
		else
		{
			sb.append("Default \n");
		}
		sb.append("Bids for Program ");
		sb.append(programName);
		sb.append(" for Participant ");
		sb.append(FDUtils.getParticipantName());
		
		return sb.toString();
	}

    public boolean isReadOnly() {
        return readOnly;
    }
    
    private void onload(){
    	JSFParticipant jsfParticipant = FDUtils.getJSFParticipant();
    	EventParticipantManager eventParticipantManager = EJB3Factory.getBean(EventParticipantManager.class);
    	 EventParticipant ep = eventParticipantManager.getEventParticipant(eventName, jsfParticipant.getName(), false);
        if(ep.getApplyDayOfBaselineAdjustment() ==1)
        	this.setDayOfAdjustment(true);
        else 
        	this.setDayOfAdjustment(false);
    }
    
    public boolean isDayOfAdjustment() {
		 return this.dayOfAdjustment;
	}

	public void setDayOfAdjustment(boolean dayOfAdjustment) {
		this.dayOfAdjustment = dayOfAdjustment;
	}
	
	public boolean isDisplayBaslineAdjustment() {
		return displayBaslineAdjustment;
	}
	
	public void setDayOfAdjustmentControl(){
    	JSFParticipant jsfParticipant = FDUtils.getJSFParticipant();
    	EventParticipantManager eventParticipantManager = EJB3Factory.getBean(EventParticipantManager.class);
    	 EventParticipant ep = eventParticipantManager.getEventParticipant(eventName, jsfParticipant.getName(), false);
        if(ep.getApplyDayOfBaselineAdjustment() ==1)
        	this.setDayOfAdjustment(true);
        else 
        	this.setDayOfAdjustment(false);
    }
}
