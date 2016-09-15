package com.akuacom.pss2.customer.baseline;

public interface ProgressUpdater {
	
	void progress(int progress);
	
	boolean isAllDone();
	
	double getDonePercentage();
}
