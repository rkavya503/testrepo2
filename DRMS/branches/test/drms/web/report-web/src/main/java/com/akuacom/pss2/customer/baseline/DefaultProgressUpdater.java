package com.akuacom.pss2.customer.baseline;

public class DefaultProgressUpdater implements ProgressUpdater{

	private int progress = 0;
	private int progressMax; 
	
	public DefaultProgressUpdater(int progressMax){
		this.progressMax = progressMax;
	}
	
	public DefaultProgressUpdater(long progressMax){
		this.progressMax = (int) progressMax;
	}
	
	@Override
	public synchronized void progress(int progress) {
		this.progress +=progress;
	}
	
	public int getProgress() {
		return progress;
	}

	@Override
	public boolean isAllDone() {
		return progress == progressMax;
	}
	
	@Override
	public double getDonePercentage() {
		return 100* progress / progressMax;
	}
}
