package com.akuacom.pss2.drw.eao;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.akuacom.pss2.drw.core.LocationKmlEntry;

public interface LocationKmlEAO extends BaseEAO<LocationKmlEntry>{
	@Remote
    public interface R extends LocationKmlEAO {}
    @Local
    public interface L extends LocationKmlEAO {}
	    
	List<LocationKmlEntry> getLocationKmlByNum(String num);

}
