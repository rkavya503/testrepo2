// $Revision: 1.1 $ $Date: 2000/01/01 00:00:00 $
package com.akuacom.pss2.event;

import java.io.Serializable;
import java.util.Date;

public class EventTimingImpl implements EventTiming, Serializable
{
    /**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 2996221342201657504L;

	/** The issued time. */
    private Date issuedTime;
    
	/** The start time. */
    private Date startTime;
    
    /** The end time. */
    private Date endTime;
    
    /** The received time. */
    private Date receivedTime;

    /** The near time. */
    private Date nearTime;

    public Date getNearTime()
	{
		return nearTime;
	}

	public void setNearTime(Date nearTime)
	{
		this.nearTime = nearTime;
	}

	public Date getIssuedTime()
	{
		return issuedTime;
	}

	public void setIssuedTime(Date issuedTime)
	{
		this.issuedTime = issuedTime;
	}

	public Date getStartTime()
	{
		return startTime;
	}

	public void setStartTime(Date startTime)
	{
		this.startTime = startTime;
	}

	public Date getEndTime()
	{
		return endTime;
	}

	public void setEndTime(Date endTime)
	{
		this.endTime = endTime;
	}

	public Date getReceivedTime()
	{
		return receivedTime;
	}

	public void setReceivedTime(Date receivedTime)
	{
		this.receivedTime = receivedTime;
	}


}
