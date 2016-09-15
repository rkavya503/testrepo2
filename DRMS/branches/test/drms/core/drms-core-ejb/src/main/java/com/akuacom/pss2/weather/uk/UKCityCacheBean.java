package com.akuacom.pss2.weather.uk;

import org.dom4j.Element;

public class UKCityCacheBean {
	public String id;
	public String name;
	public String latitude;
	public String longitude;
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the latitude
	 */
	public String getLatitude() {
		return latitude;
	}
	/**
	 * @param latitude the latitude to set
	 */
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	/**
	 * @return the longitude
	 */
	public String getLongitude() {
		return longitude;
	}
	/**
	 * @param longitude the longitude to set
	 */
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	
	public static UKCityCacheBean parse(Element e){
		UKCityCacheBean bean = new UKCityCacheBean();
		bean.setId(e.attribute("id").getStringValue());
		bean.setLatitude(e.attribute("latitude").getStringValue());
		bean.setLongitude(e.attribute("longitude").getStringValue());
		bean.setName(e.attribute("name").getStringValue());
		return bean;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		if(obj instanceof UKCityCacheBean){
			UKCityCacheBean bean = (UKCityCacheBean) obj;
			if(bean.getName().equalsIgnoreCase(getName())&&bean.getLongitude().equalsIgnoreCase(getLongitude())&&bean.getLatitude().equalsIgnoreCase(getLatitude())){
				return true;
			}
		}
		return false;
	}
	
}
