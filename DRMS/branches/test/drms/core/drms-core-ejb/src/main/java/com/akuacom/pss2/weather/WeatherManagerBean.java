package com.akuacom.pss2.weather;

import java.util.Date;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.commons.lang.StringUtils;

import com.akuacom.ejb.DuplicateKeyException;
import com.akuacom.ejb.EntityNotFoundException;

@Stateless
public class WeatherManagerBean implements WeatherManager.R,WeatherManager.L {
	private static Logger log = Logger.getLogger(WeatherManagerBean.class.getSimpleName());
	@EJB
	private ForecastWeatherGenEAO.L eao;
	@EJB
	private ForecastWmoGenEAO.L wmoEao;
	@EJB
	private ForecastConfigGenEAO.L configEao;
	@EJB
	private WeatherCategoryGenEAO.L mappingEao;

	@Override
	public void saveWeather(ForecastWeather entity) {
		if(entity==null||entity.getCity()==null) return;
		
		ForecastWeather vo = eao.findByCity(entity.getCity());
		if(vo==null){
			//Insert if not exist
			try {
				eao.create(entity);
			} catch (DuplicateKeyException e) {
				log.warning("saveWeather error...Msg="+ e.getMessage());
			}
			return;
		}
		
		//update an existed record
		if(!StringUtils.isBlank(entity.getTemp())){
			vo.setTemp(entity.getTemp());
		}
		if(!StringUtils.isBlank(entity.getHum())){
			vo.setHum(entity.getHum());
		}
		if(!StringUtils.isBlank(entity.getWeather_0())){
			vo.setWeather_0(entity.getWeather_0());
		}
		if(!StringUtils.isBlank(entity.getWeather_1())){
			vo.setWeather_1(entity.getWeather_1());
		}
		if(!StringUtils.isBlank(entity.getWeather_icon_0())){
			vo.setWeather_icon_0(entity.getWeather_icon_0());
		}
		if(!StringUtils.isBlank(entity.getWeather_icon_1())){
			vo.setWeather_icon_1(entity.getWeather_icon_1());
		}
		if(!StringUtils.isBlank(entity.getMax_0())){
			vo.setMax_0(entity.getMax_0());
		}
		if(!StringUtils.isBlank(entity.getMax_1())){
			vo.setMax_1(entity.getMax_1());
		}
		if(!StringUtils.isBlank(entity.getMin_0())){
			vo.setMin_0(entity.getMin_0());
		}
		if(!StringUtils.isBlank(entity.getMin_1())){
			vo.setMin_1(entity.getMin_1());
		}
		if(!StringUtils.isBlank(entity.getUnit())){
			vo.setUnit(entity.getUnit());
		}
		
		vo.setModifier("anonymous");
		vo.setModifiedTime(new Date());
		
		try {
			eao.update(vo);
		} catch (EntityNotFoundException e) {
			log.warning("saveWeather error...Msg="+ e.getMessage());
		}

	}

	@Override
	public void saveWmoMapping(ForecastWmo entity) {
		
		ForecastWmo vo = wmoEao.findByCity(entity.getCity());
		if(vo==null){
			//Insert if not exist
			try {
				wmoEao.create(entity);
			} catch (DuplicateKeyException e) {
				log.warning("saveWmoMapping error...Msg="+ e.getMessage());
			}
			return;
		}
		
		// Update an existed record
		vo.setCity(entity.getCity());
		vo.setWmo(entity.getWmo());
		vo.setModifier("anonymous");
		vo.setModifiedTime(new Date());

		try {
			wmoEao.update(vo);
		} catch (EntityNotFoundException e) {
			log.warning("saveWmoMapping error...Msg="+ e.getMessage());
		}
		
	}

	@Override
	public ForecastWmo getWmoByCity(String city) {
		return wmoEao.findByCity(city);
	}

	@Override
	public void saveConfig(ForecastConfig entity) {
		ForecastConfig vo = configEao.findConfig();
		if(vo==null){
			//Insert if not exist
			try {
				configEao.create(entity);
			} catch (DuplicateKeyException e) {
				log.warning("saveConfig error...Msg="+ e.getMessage());
			}
			return;
		}
		
		// Update an existed record
		vo.setCountry(entity.getCountry());
		vo.setZipcode(entity.getZipcode());
		vo.setState(entity.getState());
		vo.setCity(entity.getCity());

		try {
			configEao.update(vo);
		} catch (EntityNotFoundException e) {
			log.warning("saveConfig error...Msg="+ e.getMessage());
		}
		
		
	}

	@Override
	public ForecastConfig getConfig() {
		ForecastConfig config =  configEao.findConfig();
		if(config!=null) return config;
		
		//don't need to add a synchronized block, since following codes are used very rarely.
		config = new ForecastConfig();
		config.setCountry("US");
		config.setZipcode("90001");
		try {
			configEao.create(config);
		} catch (DuplicateKeyException e) {
			log.warning("getConfig error...Msg="+ e.getMessage());
		}
		
		return configEao.findConfig();
	}

	@Override
	public ForecastWeather getWeatherByCity(String city) {
		ForecastWeather vo = eao.findByCity(city);
		if(vo==null) vo = new ForecastWeather();
		
		return vo;
	}

	@Override
	public ForecastWeather getWeather() {
		ForecastConfig vo = configEao.findConfig();
		if("US".equalsIgnoreCase(vo.getCountry())){
			// USA get weather by zipcode
			return getWeatherByCity(vo.getZipcode());
		}
		if("AUS".equalsIgnoreCase(vo.getCountry())){
			//Australia get weather by cityname
			return getWeatherByCity(vo.getCity());
		}
		if("UK".equalsIgnoreCase(vo.getCountry())){
			// USA get weather by zipcode
			return getWeatherByCity(vo.getCity());
		}
		return new ForecastWeather();
	}

	@Override
	public String getWeatherIconMapping(String des) {
		if(des==null) return null;
		
		WeatherCategory mapping =  mappingEao.findByType(des);
		
		if(mapping!=null&&mapping.getIcon()!=null) return mapping.getIcon();
		
		WeatherCategory cate = WeatherUtils.getIconBytype(des);
		if(mapping==null) {
			cate.setDescription(des);
			cate.setFixed(false);
			try {
				mappingEao.create(cate);
			} catch (DuplicateKeyException e) {
				log.warning("getWeatherIconMapping error...Msg="+ e.getMessage());
			}
		}
		
		return cate.getIcon();
	}

	@Override
	public WeatherCategory getWeatherIconFromDB(String type) {
		if(type==null) return null;
		
		return mappingEao.findByType(type);
	}
	

}
