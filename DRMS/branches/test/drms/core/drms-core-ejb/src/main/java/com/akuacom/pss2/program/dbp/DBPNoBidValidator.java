// $Revision$ $Date$
package com.akuacom.pss2.program.dbp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;

import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.core.ProgramValidationException;
import com.akuacom.pss2.core.ProgramValidationMessage;
import com.akuacom.pss2.core.ProgramValidator;
import com.akuacom.pss2.core.ValidationException;
import com.akuacom.pss2.event.Event;
import com.akuacom.pss2.event.EventInfo;
import com.akuacom.pss2.event.EventManager;
import com.akuacom.pss2.program.bidding.BidConfig;
import com.kanaeki.firelog.util.FireLogEntry;

public class DBPNoBidValidator extends ProgramValidator
{
	/** The Constant log. */
	private static final Logger log = Logger.getLogger(DBPNoBidValidator.class);	
	 
    /**
	 * confirm bids are valid
	 * 
	 * things to check:
	 * - minimum consecutive blocks
	 * - all bids must be contiguous
	 * - minimum/maximum bid.
	 * 
	 * @param program the program
	 * @param bids the bids
	 * 
	 * @throws ProgramValidationException the program validatation exception
	 */
	public void validateDefaultBids(DBPProgram programWithBidConfig, List<BidEntry> bids) 
	throws ProgramValidationException
	{
		List<ProgramValidationMessage> errors = 
			new ArrayList<ProgramValidationMessage>();

        Collections.sort(bids);
		int count = 0;
		int contiguousCount = 0;
		int notActiveCount = 0;
		boolean contiguousBlock = false;
        final BidConfig config = programWithBidConfig.getBidConfig();
		for(BidEntry entry: bids)
		{
            if(entry.isActive())
			{
				if(contiguousBlock)
				{
					errors.add(new ProgramValidationMessage("bids.reductionKW", "all bid blocks must be contiguous" ));
					break;
					
				}
				contiguousCount++;
				if(entry.getReductionKW() < config.getMinBidKW())
				{
					errors.add(new ProgramValidationMessage("bid " + getTimeBlock(entry) +
						"'s reductionKW", "value (" + entry.getReductionKW() +
						") is below the minimum (" + config.getMinBidKW() +
						")" ));			
				}
			}
			else
			{
				if(contiguousCount > 0)
				{
					contiguousBlock = true;
				}
				if(contiguousCount > 0 && contiguousCount < 
					config.getMinConsectutiveBlocks())
				{
					errors.add(new ProgramValidationMessage("bids.reductionKW", "must have a minimum of " + 
							config.getMinConsectutiveBlocks() +
							" consecutive bid blocks" ));	
					break;
				}
				contiguousCount = 0;
				notActiveCount++;
			}
			count++;
		}
		 if(notActiveCount == count || notActiveCount == count-1)
		 {
			 errors.add(new ProgramValidationMessage("bids.reductionKW", "must have a minimum of " + 
						config.getMinConsectutiveBlocks() +
						" consecutive bid blocks" ));
		 }
		if(errors.size() > 0)
		{
			FireLogEntry logEntry = new FireLogEntry();
			logEntry.setUserParam1(program.getProgramName());
			logEntry.setDescription("bid validation failed");
			logEntry.setLongDescr(errors.toString() + "\n" + program + "\n" + 
				bids);
			log.warn(logEntry);
			ProgramValidationException error = 
				new ProgramValidationException();
			error.setErrors(errors);
			throw error;
		}
	}

    public void validateCurrentBids(DBPProgram programWithBidConfig, List<BidEntry> bids) throws ProgramValidationException {
        final Date now = new Date();
        for (BidEntry entry : bids) {
            if (entry.getBlockStart().getTime() <= now.getTime()) {
                throw new ValidationException("Bid can not be changed after event starts.");
            }
        }
        validateDefaultBids(programWithBidConfig, bids);
    }

    public static String getTimeBlock(BidEntry timeBlock) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        return simpleDateFormat.format(timeBlock.getBlockStart()) + "-" +
        	simpleDateFormat.format(timeBlock.getBlockEnd());
    }
    
    @Override
    protected void validateAgainstExistingEvents() {
    	
    	com.akuacom.pss2.program.ProgramManager programManager = 
    			EJBFactory.getBean(com.akuacom.pss2.program.ProgramManager.class);
    		EventManager pmb =
    			EJBFactory.getBean(EventManager.class);
    		for(EventInfo eventInfo: 
    			programManager.getEventsForProgram(program.getProgramName()))
    		{
    			Event existingEvent = pmb.getEvent(program.getProgramName(), 
    				eventInfo.getEventName());
    			if (!event.getEventName().equals(existingEvent.getEventName())) { /* DRMS-2633 temporary fix(?)*/
                    // if there is overlap
                    if((event.getStartTime().getTime() >= existingEvent.getStartTime().getTime() &&
                        event.getStartTime().getTime() < existingEvent.getEndTime().getTime()) ||
                        (event.getEndTime().getTime() > existingEvent.getStartTime().getTime() &&
                        event.getEndTime().getTime() <= existingEvent.getEndTime().getTime()) ||
                        (event.getStartTime().getTime() < existingEvent.getStartTime().getTime() &&
                        event.getEndTime().getTime() >= existingEvent.getEndTime().getTime()))
                    {
                        ProgramValidationMessage error = new ProgramValidationMessage();
                        error.setDescription("event overlaps with existing event: " +
                            existingEvent.getEventName());
                        error.setParameterName("startTime");
                        errors.add(error);
                    }
                }       			
    		}
    	
       /* final com.akuacom.pss2.event.EventEAO eventEAO = EJBFactory
                .getBean(com.akuacom.pss2.event.EventEAOBean.class);
        
        boolean existing=false;
    	List<Event> events=eventEAO.findEventOnlyByProgramName(program.getProgramName());
    	if (events!=null && events.size()>0) {
    		for (Event event:events) {
    			if (event.getEventStatus()!=EventStatus.ACTIVE) {
    				existing=true;
    				break;
    			}
    		}
    	}
    	
    	if (existing) {
            ProgramValidationMessage error = new ProgramValidationMessage();
            error.setDescription("No event created due to one or more scheduled events exist in the system");
            error.setParameterName("existingEvent");
            errors.add(error);
    	}*/
    }
}
