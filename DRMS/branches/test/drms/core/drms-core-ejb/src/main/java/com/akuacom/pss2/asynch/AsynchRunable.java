package com.akuacom.pss2.asynch;

import java.io.Serializable;

public interface AsynchRunable extends Serializable,Runnable {
	
	Runnable getWrapper();
	void setWrapper(Runnable runnable);
}
