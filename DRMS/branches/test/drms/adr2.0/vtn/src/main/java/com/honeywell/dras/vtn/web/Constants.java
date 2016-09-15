package com.honeywell.dras.vtn.web;

/**
 * Constants
 * @author sunil
 *
 */
public class Constants {
	
	public static final String HEATING_EVENT_TAG = "HEATING_EVENT";
	
	public static final String VENID = "HONEYWELL.AKUACOM.VEN.1002";
	
	public static final String VTN_END_POINT = "http://localhost:8080/vtn/OpenADR2/Simple/EiEvent";
	
	public static final String VTNID = "HONEYWELL.AKUACOM.VTN.1002";

	public static final String VEN_END_POINT = "http://localhost:8080/vtn/OpenADR2/Simple/EiEventClient";
	
	//public static final String DEFAULT_MARKET_CONTEXT = "HONEYWELL.AKUACOM.MARKET.CHINA";
	public static final String DEFAULT_MARKET_CONTEXT = "MarketContext1";

	
	public static final int VEN_TIMEZONE_OFFSET = -180;
	//private static final int TIMEZONE_OFFSET = -180; // Boston
	public static final int TIMEZONE_OFFSET = -180; // SF
	
	public static final String VTN_COMMENT = "HONEYWELL AKUACOM OPENADR 2.0 VTN";
	
	public static final String REQUEST_ID_PREFIX = "AKUACOM.7.REQ:";
	
	public static final long POLL_FREQ = 60 * 1000; // 1 minute
	public static final boolean SUPPORTS_OPENADR_1 = true;
	public static final boolean SUPPORTS_OPENADR_2A = true;
	public static final boolean SUPPORTS_OPENADR_2B = true;
	public static final boolean SUPPORTS_OBIX = true;	
	public static final String PROFILEOBIX = "OBIX" ;	
	public static final String PROFILE1 = "1.0a" ;	
	public static final String PROFILE2A = "2.0a" ;
	public static final String PROFILE2B = "2.0b" ;
	public static final String simpleHttp = "simpleHttp" ;
	public static final String xmpp = "xmpp" ;

}
