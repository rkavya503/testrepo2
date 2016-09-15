// $Revision: 1.1 $ $Date: 2000/01/01 00:00:00 $
package com.akuacom.pss2.event;

import java.util.Date;

public interface EventTiming
{
    public Date getIssuedTime();

	public void setIssuedTime(Date issuedTime);

	public Date getStartTime();

	public void setStartTime(Date startTime);

	public Date getEndTime();

	public void setEndTime(Date endTime);

	public Date getReceivedTime();

	public void setReceivedTime(Date receivedTime);

	public Date getNearTime();

	public void setNearTime(Date nearTime);
}
