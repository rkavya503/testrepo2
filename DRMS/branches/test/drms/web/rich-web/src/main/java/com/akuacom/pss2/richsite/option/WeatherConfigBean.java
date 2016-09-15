package com.akuacom.pss2.richsite.option;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import com.akuacom.ejb.client.EJB3Factory;
import com.akuacom.pss2.weather.ForecastConfig;
import com.akuacom.pss2.weather.WeatherManager;
import com.akuacom.pss2.weather.uk.UKCityCache;
import com.akuacom.pss2.weather.uk.UKCityCacheBean;

public class WeatherConfigBean implements Serializable{
	private static WeatherManager manager = EJB3Factory.getBean(WeatherManager.class);
	private static final long serialVersionUID = 3317004124260726059L;
	private static final String USA="US";
	private static final String AUS="AUS";
	private static final String UK="UK";
	
    public WeatherConfigBean(){
    	reloadWeatherConfig();
	}
	
    private void reloadWeatherConfig(){
//		WeatherManager manager = EJB3Factory.getBean(WeatherManager.class);
		ForecastConfig config = manager.getConfig();		
		// Give a default value for country 
		if(config==null){
			country =USA;
		}else{
			this.setCountry(config.getCountry());
			this.setCity(config.getCity());
			this.setState(config.getState());
			this.setZipcode(config.getZipcode());
		}
		
		if(USA.equalsIgnoreCase(country)){
			showUs = true;
			showAus = false;
			showUk = false;
		}else if(AUS.equalsIgnoreCase(country)){
			showUs = false;
			showAus = true;
			showUk = false;
		}else if(UK.equalsIgnoreCase(country)){
			showUs = false;
			showAus = false;
			showUk = true;
			initUKWeatherConfigInfo(config);
			this.setCity("");//To remove the AUS city display on the weather config page. 
			this.setZipcode("");//To remove the US zipcode display on the weather config page. 
		}
		
		initCountries();	
		initStates();
    }
    
	public void save(){
//		WeatherManager manager = EJB3Factory.getBean(WeatherManager.class);
		ForecastConfig config = new ForecastConfig();
		  
		if(USA.equalsIgnoreCase(this.getCountry())){
			  config.setCountry(this.getCountry());
			  config.setCity(null);
			  config.setState(null);
			  config.setZipcode(this.getZipcode());
		}else if(AUS.equalsIgnoreCase(this.getCountry())){
			  config.setCountry(this.getCountry());
			  config.setCity(this.getCity());
			  config.setState(this.getState());
			  config.setZipcode(null);
		}else if(UK.equalsIgnoreCase(this.getCountry())){
			  config.setCountry(UK);
			  config.setCity(displayCityNameOfUK);
			  if(selectedUKCity!=null&&(!selectedUKCity.equalsIgnoreCase(""))){
				  UKCityCacheBean bean =UKCityCache.getInstance().findCity(selectedUKCity).get(0);
				  if(bean!=null&&bean.getId()!=null){
					  config.setZipcode(bean.getId());  
				  }else{
					  config.setZipcode(null);
				  }
			  }
			  config.setState(null);
		}
		  
		manager.saveConfig(config);
		
		FacesContext facesContext = FacesContext.getCurrentInstance();
		FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "Configuration successfully saved...", null);
		facesContext.addMessage(null, facesMessage);
		reloadWeatherConfig();
    }
	public void searchUKCity(){
		if(ukCitySearch!=null&&(!ukCitySearch.equalsIgnoreCase(""))){
			List<UKCityCacheBean> result = UKCityCache.getInstance().findCity(ukCitySearch);
			searchUKCityResult = new ArrayList();
			for(UKCityCacheBean bean : result){
				String name = bean.getName();
				if(name!=null&&(!name.equalsIgnoreCase(""))){
					SelectItem option = new SelectItem(name, name);
					searchUKCityResult.add(option);
				}
			}
		}
    }    
	public void update(){
		if(USA.equalsIgnoreCase(country)){
			showUs = true;
			showAus = false;
			showUk=false;
			if(this.zipcode==null||this.zipcode.equalsIgnoreCase("")){
				this.setZipcode("90001");
			}
		}else if(AUS.equalsIgnoreCase(country)){
			showUs = false;
			showAus = true;
			showUk=false;
		}else if(UK.equalsIgnoreCase(country)){
			showUs = false;
			showAus = false;
			showUk=true;
		}
       
	}
  
	private void initStates() {
		states = new ArrayList();
		SelectItem option = new SelectItem("NSW", "New South Wales");
		states.add(option);
		option = new SelectItem("Vic", "Victoria");
		states.add(option);	
		option = new SelectItem("QLD", "Queensland");
		states.add(option);	
		option = new SelectItem("SA", "South Australia");
		states.add(option);	
		option = new SelectItem("WA", "Western Australia");
		states.add(option);	
		option = new SelectItem("Tas", "Tasmania");
		states.add(option);	
		option = new SelectItem("NT", "Northern Territory");
		states.add(option);
	}

	private void initCountries() {
		countries = new ArrayList();
		SelectItem option = new SelectItem(USA, "United States");
		countries.add(option);
		option = new SelectItem(AUS, "Australia");
		countries.add(option);
		option = new SelectItem(UK, "Britain");
		countries.add(option);
	}
	
    private List countries;
	private List states;
	private String country;
	private String zipcode;
	private String city;
	private String state;
	private boolean showAus;
	private boolean showUs;
	private boolean showUk;
	private String ukCitySearch;
	private String selectedUKCity;
	private List searchUKCityResult;


	public List getCountries() {
		return countries;
	}

	public void setCountries(List countries) {
		this.countries = countries;
	}

	public List getStates() {
		return states;
	}

	public void setStates(List states) {
		this.states = states;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public boolean isShowAus() {
		return showAus;
	}

	public void setShowAus(boolean showAus) {
		this.showAus = showAus;
	}

	public boolean isShowUs() {
		return showUs;
	}

	public void setShowUs(boolean showUs) {
		this.showUs = showUs;
	}

	/**
	 * @return the showUk
	 */
	public boolean isShowUk() {
		return showUk;
	}

	/**
	 * @param showUk the showUk to set
	 */
	public void setShowUk(boolean showUk) {
		this.showUk = showUk;
	}

	/**
	 * @return the ukCitySearch
	 */
	public String getUkCitySearch() {
		return ukCitySearch;
	}

	/**
	 * @param ukCitySearch the ukCitySearch to set
	 */
	public void setUkCitySearch(String ukCitySearch) {
		this.ukCitySearch = ukCitySearch;
	}

	/**
	 * @return the selectedUKCity
	 */
	public String getSelectedUKCity() {
		return selectedUKCity;
	}

	/**
	 * @param selectedUKCity the selectedUKCity to set
	 */
	public void setSelectedUKCity(String selectedUKCity) {
		this.selectedUKCity = selectedUKCity;
	}

	/**
	 * @return the searchUKCityResult
	 */
	public List getSearchUKCityResult() {
		if(searchUKCityResult==null){
			searchUKCityResult = new ArrayList();
		}
		return searchUKCityResult;
	}

	/**
	 * @param searchUKCityResult the searchUKCityResult to set
	 */
	public void setSearchUKCityResult(List searchUKCityResult) {
		this.searchUKCityResult = searchUKCityResult;
	}
	/**
	 * DRMS-7666
	 */
	private void initUKWeatherConfigInfo(ForecastConfig config){
		if(config!=null){
			displayCityNameOfUK = config.getCity();
			String forecastID = config.getZipcode();
			String forecastName = UKCityCache.getInstance().getForecastName(forecastID);
			String observationName = UKCityCache.getInstance().getObservationName(forecastID);
			setSelectedForecastName(forecastName);
			setMatchedObservationName(observationName);
		}
	}
	private String displayCityNameOfUK;
	private String selectedForecastName;
	private String matchedObservationName;

	/**
	 * @return the displayCityNameOfUK
	 */
	public String getDisplayCityNameOfUK() {
		if(displayCityNameOfUK==null){
			displayCityNameOfUK="";
		}
		return displayCityNameOfUK;
	}



	/**
	 * @param displayCityNameOfUK the displayCityNameOfUK to set
	 */
	public void setDisplayCityNameOfUK(String displayCityNameOfUK) {
		this.displayCityNameOfUK = displayCityNameOfUK;
	}



	/**
	 * @return the selectedForecastName
	 */
	public String getSelectedForecastName() {
		if(selectedForecastName==null){
			selectedForecastName="";
		}
		return selectedForecastName;
	}



	/**
	 * @param selectedForecastName the selectedForecastName to set
	 */
	public void setSelectedForecastName(String selectedForecastName) {
		this.selectedForecastName = selectedForecastName;
	}



	/**
	 * @return the matchedObservationName
	 */
	public String getMatchedObservationName() {
		if(matchedObservationName==null){
			matchedObservationName="";
		}
		return matchedObservationName;
	}



	/**
	 * @param matchedObservationName the matchedObservationName to set
	 */
	public void setMatchedObservationName(String matchedObservationName) {
		this.matchedObservationName = matchedObservationName;
	}
	
}
