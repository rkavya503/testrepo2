package com.akuacom.pss2.drw;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.akuacom.pss2.drw.core.LocationKmlEntry;
import com.akuacom.pss2.drw.value.LocationKmlStatus;

public interface LocationKmlManager {
	@Remote
    public interface R extends LocationKmlManager {}
    @Local
    public interface L extends LocationKmlManager {}
    
    void merge(LocationKmlEntry entry);
    
    void delete(String type, String number);
    
    void insert(LocationKmlEntry entry);
    
    List<LocationKmlStatus> getAllLocationKmlStatus();

}
