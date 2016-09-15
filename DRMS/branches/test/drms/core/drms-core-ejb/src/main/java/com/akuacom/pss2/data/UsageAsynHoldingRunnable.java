package com.akuacom.pss2.data;

import java.util.Date;
import java.util.Map;

import com.akuacom.pss2.asynch.AsynchRunable;
import com.akuacom.pss2.asynch.BasetHoldingRunnable;
import com.akuacom.pss2.core.EJBFactory;

public class UsageAsynHoldingRunnable extends BasetHoldingRunnable implements AsynchRunable{
	
	private static final long serialVersionUID = 6371549878328912515L;
	
	private transient Runnable wrapper;
	
	public UsageAsynHoldingRunnable(String id, int minHold, int maxHold,final String theDataSourceId,
			final Date theDate, final boolean baselineUpdate){
		super(id,minHold,maxHold);
		setRunnable(new AsynchRunable(){
				private static final long serialVersionUID = 1L;
				private transient BasetHoldingRunnable wrapper;
				private boolean updateBaseline;
				
				public void run() {
					DataManager dataManager  = EJBFactory.getBean(DataManager.class);
					dataManager.onUsageChange(theDataSourceId, theDate,isUpdataBaseline());
				}
				public boolean isUpdataBaseline(){
					return updateBaseline;
				}
				
				@Override
				public Runnable getWrapper() {
					return this.wrapper;
				}
				
				@Override
				public void setWrapper(Runnable runnable) {
					this.wrapper = (BasetHoldingRunnable) runnable;
					this.updateBaseline = wrapper.isBool1();
				}
				
			});
		this.setUpdateBaseline(baselineUpdate);
	}
	
	public boolean isUpdateBaseline() {
		return isBool1();
	}

	@Override
	public String getMergeSQLTempleate() {
		return MERGE_SQL_TEMPLATE;
	}

	
	@Override
	public Map<String, Object> getMergeSQLParam() {
		Map<String,Object> params= super.getMergeSQLParam();
		params.put("updateBaseLine", isUpdateBaseline());
		return params;
	}
	
	public void setUpdateBaseline(boolean updateBaseline) {
		setBool1(updateBaseline);
	}
	
	private static final String  MERGE_SQL_TEMPLATE= 
		"INSERT INTO asyn_task(id, creationTime,lastUpdate, minHold,maxHold,runnable,bool1)" 
		+" VALUES (${id}, NOW(),NOW(),${minHold}, ${maxHold},${runnable},${updateBaseLine}) " 
		+" ON DUPLICATE KEY UPDATE lastUpdate= GREATEST(NOW(),lastUpdate),bool1= (bool1 OR ${updateBaseLine}) ";

	@Override
	public Runnable getWrapper() {
		return wrapper;
	}

	@Override
	public void setWrapper(Runnable runnable) {
		this.wrapper= runnable;
	}
	
}
