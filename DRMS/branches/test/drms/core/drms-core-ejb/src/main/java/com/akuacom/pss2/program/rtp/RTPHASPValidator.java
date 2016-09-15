// $Revision: 1.4 $ $Date: 2010-04-14 19:40:30 $
package com.akuacom.pss2.program.rtp;

import java.util.Date;

import com.akuacom.pss2.core.ProgramValidationMessage;
import com.akuacom.pss2.signal.SignalEntry;

public class RTPHASPValidator extends RTPValidator
{
	protected void validateStartTime()
	{
		// no check for start time before issue time

		if(getMinStartCal().getTimeInMillis() > getStartCal().getTimeInMillis() ||
			getMaxStartCal().getTimeInMillis() < getStartCal().getTimeInMillis())
		{
			ProgramValidationMessage error = new ProgramValidationMessage();
			error.setDescription("start time must be between " +
				program.getMinStartTimeH() + ":" + program.getMinStartTimeM() +
				" and " +
				program.getMaxStartTimeH() + ":" + program.getMaxStartTimeM());
			error.setParameterName("startTime");
			getErrors().add(error);
		}
	}

	@Override
    protected void validateSignalEntryTimes(SignalEntry signalEntry)
    {
		// check for times older than the last 60 seconds
        // and if it's dirty or not.
//        final long earliestActiveLimit = getNowMS() - TIME_BUFFER_MS;
        final Date entryTime = signalEntry.getTime();
//        if(!signalEntry.getExpired() && entryTime.getTime() < earliestActiveLimit) {
//            addError(getErrors(), "signal entry time in the past", signalEntry.toString());
//        }
        
        final Date startTime = getEvent().getStartTime();
        final Date endTime = getEvent().getEndTime();
        final Date issueTime = getEvent().getIssuedTime();

        if (entryTime.after(endTime)) {
            addError(getErrors(), "entry signal beyond event duration", signalEntry.toString());
        }
        if ("pending".equals(signalEntry.getParentSignal().getSignalDef().getSignalName())) {
            if (entryTime.before(issueTime)) { 
            	// TODO: what should we do regarding pending signal?
                addError(getErrors(), "entry signal beyond event duration", signalEntry.toString());
            }
        } else {
            if (entryTime.before(startTime)) {
                addError(getErrors(), "entry signal beyond event duration", signalEntry.toString());
            }
        }
    }
}
