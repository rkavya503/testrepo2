/**
 * 
 */
package com.akuacom.pss2.program.testProgram;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.akuacom.pss2.event.EventTiming;
import com.akuacom.pss2.program.Program;

/**
 *
 */
@Entity
@DiscriminatorValue("TestProgram")
public class TestProgram extends Program {

	private static final long serialVersionUID = 3635043185523290376L;
	public static final String PROGRAM_NAME="Client Test";

	@Override
    public Program getNewInstance() {
        return new TestProgram();
    }

	@Override
    public long getPendingLeadMS(EventTiming eventTiming) {
		if(eventTiming.getNearTime() != null)
		{
	        return eventTiming.getStartTime().getTime() - 
	        	eventTiming.getNearTime().getTime();
		}
		else
		{
			return super.getPendingLeadMS(eventTiming);
		}
    }

    /* (non-Javadoc)
     * @see com.akuacom.pss2.core.model.Program#toString()
     */
    @Override
    public String toString()
    {
        StringBuilder rv = new StringBuilder("Client Test: ");
        rv.append(super.toString());
        return rv.toString();
   }
}
