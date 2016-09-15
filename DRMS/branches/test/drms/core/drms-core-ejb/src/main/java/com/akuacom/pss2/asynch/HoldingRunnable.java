package com.akuacom.pss2.asynch;

import java.util.Date;
import java.util.Map;

/**
 *A <tt> HoldingRunnable</tt> is a task to be executed, normally in a thread other than caller's thread.
 *
 *<p>
 * see {@link HoldingQueue}
 */
public interface HoldingRunnable extends Runnable {
	
	String STS_RUNNING="RUNNING";
	String STS_PENDDING="PENDDING";
	
	/** 
	 * id of the task, only task with same id can be merged together
	 **/
	String getId();
	void setId(String id);
	
	/**
	 * the minimal time frame for the task will be held  
	 */
	long getMinHold();
	void setMinHold(long timeFrame);
	
	/**
	 * the max time frame for the task will be held  
	 */
	long getMaxHold();
	void setMaxHold(long maxHold);
	
	/**
	 * the creation time for the task, the difference between current time and creation time
	 * is stay time of the task
	 */
	Date getCreationTime();
	void setCreationTime(Date date);
	
	/**
	 * the time for last updated. The different between current time and last updated time is 
	 * time of holding
	 */
	Date getLastUpdate();
	void setLastUpdate(Date date);
	
	
	/** merge this task with another one**/
	Map<String,Object> getMergeSQLParam();
	String getMergeSQLTempleate();
	
	String getStatus();
	void setStatus(String status);
	
	int getPriority();
	void setPriority(int priority);
	
	
	AsynchRunable getRunnable();
	void setRunnable(AsynchRunable runnable);
	
}
