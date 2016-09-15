package com.akuacom.pss2.drw;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remote;

public interface UploadConfManager {
	
 	@Remote
    public interface R extends UploadConfManager {}
	@Local
	public interface L extends UploadConfManager {}
	
	void batchInsert(List<String[]> result);
	void clearLocation();
	void clearZipcode();
	void loadSlap();
	void loadAbank();
	void loadSubstation();
	void loadSlapZip();
	void loadAbankZip();
	void loadSubZip();

	void dropTemp();
	void createTemp();
	
	long checkActiveEvents();
}
