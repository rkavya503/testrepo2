package com.akuacom.pss2.drw.admin;

import java.io.Serializable;

public class LocationKmlBackingBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
	public LocationKmlBackingBean(){
		super();
	}
	

	private LocationKmlModel locationKmls = null;
	
	public LocationKmlModel getLocationKmls() {
		if( locationKmls==null)
			locationKmls = new LocationKmlModel();
		return locationKmls;
	}

}



