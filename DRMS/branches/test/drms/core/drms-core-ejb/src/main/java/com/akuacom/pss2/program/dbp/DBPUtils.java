// $Revision$ $Date$
package com.akuacom.pss2.program.dbp;

import java.util.*;

import org.openadr.dras.eventinfo.EventInfoInstance;
import org.openadr.dras.eventinfo.EventInfoValue;
import org.openadr.dras.utilitydrevent.UtilityDREvent;

import com.akuacom.pss2.event.Event;
import com.akuacom.pss2.event.TimeBlock;
import com.akuacom.pss2.program.bidding.BidBlock;

public class DBPUtils
{
    /**
     * Gets the event bid blocks for this event given the template (just h,m,s) 
     * in the program.
     * 
     * @param program the program
     * @param event the event
     * 
     * @return the event bid blocks
     */
    public static List<EventBidBlock> getEventBidBlocks(DBPProgram program, DBPEvent event) {
        final List<EventBidBlock> results = new ArrayList<EventBidBlock>();
        final Set<BidBlock> blocks = program.getBidConfig().getBidBlocks();
        Date startTime = event.getStartTime();
        Date endTime = event.getEndTime();
        for (BidBlock bidBlock : blocks) {
            if (bidBlock.getStartTimeH() >= startTime.getHours()
                    && bidBlock.getEndTimeH() <= endTime.getHours()) {
                final EventBidBlock timeBlock = new EventBidBlock();
                timeBlock.setEvent(event);
                timeBlock.setStartTime(bidBlock.getStartTimeH() * 100 + bidBlock.getStartTimeM());
                timeBlock.setEndTime(bidBlock.getEndTimeH() * 100 + bidBlock.getEndTimeM());
                results.add(timeBlock);
            }
        }
        return results;
    }

    public static List<EventBidBlock> getEventBidBlocks(
    	Event event, UtilityDREvent utilityDREvent)
    {
        final List<EventBidBlock> results = new ArrayList<EventBidBlock>();

        EventInfoInstance eventInfo =
    		utilityDREvent.getEventInformation().getEventInfoInstance().get(0);

    	// build an array of block end times
    	int[] endTimes = new int[eventInfo.getValues().getValue().size()];
    	int count = 0;

    	for(EventInfoValue value: eventInfo.getValues().getValue())
    	{
    		if(count > 0)
    		{
    			endTimes[count-1] = value.getStartTime().intValue();
    		}
    		count++;
    	}
    	// the last ending time is the end time of the event
    	endTimes[count-1] = eventInfo.getEndTime().intValue();

	    count = 0;
    	for(EventInfoValue value: eventInfo.getValues().getValue())
    	{

            GregorianCalendar blockStartCal = new GregorianCalendar();
    		blockStartCal.setTime(event.getStartTime());
    		blockStartCal.add(Calendar.SECOND, value.getStartTime().intValue());
    		GregorianCalendar blockEndCal = new GregorianCalendar();
    		blockEndCal.setTime(event.getStartTime());
    		blockEndCal.add(Calendar.SECOND, endTimes[count]);

            final EventBidBlock timeBlock = new EventBidBlock();
            timeBlock.setEvent(event);
            timeBlock.setStartTime(blockStartCal.get(
            	Calendar.HOUR_OF_DAY) * 100 + blockStartCal.get(Calendar.MINUTE));
            timeBlock.setEndTime(blockEndCal.get(
            	Calendar.HOUR_OF_DAY) * 100 + blockEndCal.get(Calendar.MINUTE));
            results.add(timeBlock);
            count++;
        }
        return results;
    }

    public static TimeBlock getTimeBlock(BidBlock bb) {
        final TimeBlock tb = new TimeBlock();
        tb.setStartHour(bb.getStartTimeH());
        tb.setStartMinute(bb.getStartTimeM());
        tb.setStartSecond(0);
        tb.setEndHour(bb.getEndTimeH());
        tb.setEndMinute(bb.getEndTimeM());
        tb.setEndSecond(0);
        return tb;
    }

    public static TimeBlock[] getTimeBlocks(Set<BidBlock> set) {
        final TimeBlock[] timeBlocks = new TimeBlock[set.size()];
        int i = 0;
        for (BidBlock bidBlock : set) {
            timeBlocks[i++] = getTimeBlock(bidBlock);
        }
        return timeBlocks;
    }
}
