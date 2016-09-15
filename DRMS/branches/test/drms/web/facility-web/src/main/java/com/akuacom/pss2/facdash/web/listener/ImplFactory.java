package com.akuacom.pss2.facdash.web.listener;


public class ImplFactory {
    private static ImplFactory s_instance = null;
    
    private static final String USA = "US";
    private static final String AUSTRALIA = "AUS";
    private static final String UK = "UK";

    public static ImplFactory instance() {
        if (null == ImplFactory.s_instance) {
            ImplFactory.s_instance = new ImplFactory();
        }
        return ImplFactory.s_instance;
    }
    
    private ImplFactory(){
    	
    }

    public WeatheRretriever getWeatherRetriever(String country) {
        if (country == null || country.isEmpty()) {
        	country = USA;
        }
        
        if(country.equalsIgnoreCase(USA)){
        	return new UsWeatheRretriever();
        }
        
        if(country.equalsIgnoreCase(AUSTRALIA)){
        	return new AusWeatheRretriever();
        }
        if(country.equalsIgnoreCase(UK)){
        	return new UkWeatheRretriever();
        }
        return new UsWeatheRretriever();

    }


}
