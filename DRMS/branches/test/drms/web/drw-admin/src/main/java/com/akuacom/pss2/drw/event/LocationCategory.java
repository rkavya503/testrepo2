package com.akuacom.pss2.drw.event;

import java.util.ArrayList;
import java.util.List;

import com.akuacom.pss2.drw.core.Location;

public class LocationCategory extends Location {
	
	public LocationCategory(String categoryName, List<Location> locations){
		super.setName(categoryName);
		this.contents = locations;
	}
	
	public LocationCategory(){
		
	}
	
	public LocationCategory(String categoryName, boolean isAll){
		super.setName(categoryName);
	}
	
	private static final long serialVersionUID = 1925870839709708941L;

	private List<Location> contents;
	
	public List<Location> getContents() {
		if(contents==null)
			contents = new ArrayList<Location>();
		return contents;
	}

	public void setContents(List<Location> contents) {
		this.contents = contents;
	}
	
	@Override
	public String getNumber() {
		return "";
	}
	@Override
	public String getName() {
		return "Dispatch Type: "+super.getName();
	}
	
}
