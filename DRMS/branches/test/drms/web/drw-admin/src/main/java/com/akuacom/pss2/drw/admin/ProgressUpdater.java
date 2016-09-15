package com.akuacom.pss2.drw.admin;

public interface ProgressUpdater {
	
	void progress(int progress);
	
	boolean isAllDone();
	
	double getDonePercentage();
}
