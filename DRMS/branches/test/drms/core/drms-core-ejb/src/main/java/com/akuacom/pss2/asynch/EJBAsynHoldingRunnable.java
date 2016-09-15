package com.akuacom.pss2.asynch;

public class EJBAsynHoldingRunnable extends BasetHoldingRunnable implements AsynchRunable {
	
	private static final long serialVersionUID = -2503173857345308364L;

	private transient Runnable wrapper;
	
	public EJBAsynHoldingRunnable(String id,long minHold,long maxHold,Class<?> ejbInterface, String method,
			 Class<?>[] argumentTypes,Object[] arguments) {
		super(new EJBAsynchRunable(ejbInterface,method,argumentTypes,arguments),id, minHold,maxHold);
	}

	public EJBAsynHoldingRunnable(AsynchRunable runnable, String id, long minHold,
			long maxHold) {
		super(runnable, id, minHold, maxHold);
	}
	
	@Override
	public Runnable getWrapper() {
		return wrapper;
	}

	@Override
	public void setWrapper(Runnable runnable) {
		this.wrapper= runnable;
	}
	
}
