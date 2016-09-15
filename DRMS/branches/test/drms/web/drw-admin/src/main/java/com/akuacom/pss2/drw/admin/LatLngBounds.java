package com.akuacom.pss2.drw.admin;

public class LatLngBounds {
	
	public LatLngBounds(){
		this.north = Double.NaN;
		this.south = Double.NaN;
		this.west = Double.NaN;
		this.east = Double.NaN;
	}

	//min latitude
	private Double north;
	//max latitude
	private Double south;
	//min longitude
	private Double west;
	//max longitude
	private Double east;
	/**
	 * 
	 * @param x longitude
	 * @param y latitude
	 */
	public void extend(Double x, Double y){
		if(west.isNaN()||west>x){
			west = x;
		}
		
		if(east.isNaN()||east<x){
			east = x;
		}
		
		if(north.isNaN()||north<y){
			north = y;
		}
		
		if(south.isNaN()||south>y){
			south = y;
		}
	}
	public void extend(Object x, Object y){
		if(x==null||y==null) return;
		Double xd = Double.valueOf(x.toString());
		Double yd = Double.valueOf(y.toString());
		
		if(yd.isNaN()||xd.isNaN()) return;
		
		extend(xd, yd);
		
	}
	
	public String getJsonString(){
		if(west.isNaN()||east.isNaN()||north.isNaN()||south.isNaN()) return "";
		
		StringBuilder jsonString = new StringBuilder();
		jsonString.append("new google.maps.LatLngBounds(");
		jsonString.append("new google.maps.LatLng("+south+","+west+")");
		jsonString.append(",");
		jsonString.append("new google.maps.LatLng("+north+","+east+")");
		jsonString.append(")");
		
		return jsonString.toString();
	}
	
//	LatLngBounds(sw?:LatLng, ne?:LatLng)
//	Creates a LatLngBounds object that is a rectangle with the inputted points as its southwest and northeast corners.
//  new google.maps.LatLngBounds()
	
//	LatLng(lat:number,
//			lng:number, noWrap?:boolean)
//			Creates a LatLng object. The order of latitude and longitude is
//			always latitude first and longitude second. If noWrap is set to true, it
//			will take the passed coordinates as is; otherwise, it will force the
//			latitude to lie between -90 and +90 degrees and longitude between
//			-180 and +180 degrees.
// 
}
