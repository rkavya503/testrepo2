// $Revision: 1.1 $ $Date: 2000/01/01 00:00:00 $
package com.akuacom.pss2.program.dbp;

import java.util.Date;

import com.akuacom.pss2.event.EventTiming;

public interface DBPEventTiming extends EventTiming
{
	public Date getRespondBy();

	public void setRespondBy(Date respondBy);
}
