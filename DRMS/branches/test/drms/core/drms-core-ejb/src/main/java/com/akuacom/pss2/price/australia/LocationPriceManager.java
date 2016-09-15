package com.akuacom.pss2.price.australia;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.akuacom.pss2.timer.TimerManager;

public interface LocationPriceManager extends TimerManager {
	
	@Remote
    public interface R extends LocationPriceManager {}
    @Local
    public interface L extends LocationPriceManager {}
    
	public PriceRecord getPrice();
}
