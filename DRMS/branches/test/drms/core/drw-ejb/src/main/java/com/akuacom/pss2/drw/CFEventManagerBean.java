/**
 * 
 */
package com.akuacom.pss2.drw;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;
import org.jboss.ejb3.annotation.LocalBinding;
import org.jboss.ejb3.annotation.RemoteBinding;

import scala.actors.threadpool.Arrays;

import com.akuacom.jdbc.CellConverter;
import com.akuacom.jdbc.ColumnAsFeatureFactory;
import com.akuacom.jdbc.ColumnAsObjectFactory;
import com.akuacom.jdbc.ColumnsAsListConverter;
import com.akuacom.jdbc.ListConverter;
import com.akuacom.jdbc.MasterDetailFactory;
import com.akuacom.jdbc.SQLBuilder;
import com.akuacom.jdbc.SQLBuilderException;
import com.akuacom.jdbc.SQLLoader;
import com.akuacom.jdbc.SimpleListConverter;
import com.akuacom.pss2.drw.core.DRWebsiteProperty;
import com.akuacom.pss2.drw.core.Location;
import com.akuacom.pss2.drw.core.Program;
import com.akuacom.pss2.drw.eao.DRWebsitePropertyEAO;
import com.akuacom.pss2.drw.eao.ProgramEAO;
import com.akuacom.pss2.drw.util.AdminProduct;
import com.akuacom.pss2.drw.util.DrwSqlUtil;
import com.akuacom.pss2.drw.value.AlertValue;
import com.akuacom.pss2.drw.value.EventValue;
import com.akuacom.pss2.drw.value.LocationValue;
import com.akuacom.pss2.drw.value.WeatherValue;
import com.akuacom.utils.DateUtil;

/**
 * the class DREventManagerBean
 */
@Stateless
@LocalBinding(jndiBinding="dr-pro/CFEventManager/local")
@RemoteBinding(jndiBinding="dr-pro/CFEventManager/remote")
public class CFEventManagerBean implements CFEventManager.L, CFEventManager.R {

	
    private static final Logger log = Logger.getLogger(CFEventManagerBean.class);

    private static final String[] drwProgramClasses={"SDP", "API", "BIP"};
    private static final String PROGRAM_CLASS_SDP="SDP";
    private static final String PROGRAM_CLASS_BIP="BIP";
    private static final String PROGRAM_CLASS_API="API";
    
    private static final String[] sdpCommericalRate={AdminProduct.APS.name(), "APS-E"};
//    private static final String[] sdpCommericalRate={AdminProduct.APS.name(), AdminProduct.APS_E.name()};
    private static final String[] sdpResidentialRate={AdminProduct.SDP.name()};
    private static final String[] sdpProducts={"Residential", "Commercial Base", "Commercial Enhanced"};
    
	public static final String RTP_AGRICULTURAL_PROGRAM="RTP";

	
	@EJB
	ProgramEAO.L programEAO;
	@EJB
	DRWebsitePropertyEAO.L websitePropertyEAO;
    @EJB
    DrwSQLExecutor.L drwSqlExecutor;
    @EJB
    Pss2SQLExecutor.L pss2SqlExecutor;
	@EJB
    mobimsgSQLExecutor.L mobimsgSQLExecutor;
	
	@Override
	public List<Program> getAllProgram() {
		List<Program> result=new ArrayList<Program>();
		result.addAll(programEAO.getAll(Program.class.getName()));
		
		String sqltemplate= " SELECT name, utilityProgramName, programClass, longProgramName " +
							" FROM program WHERE programType!=${demoProgram} and programType!=${clientTestProgram} " +
							" and programClass IS NOT NULL and state = 1 ";
		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("demoProgram", "DemoProgram");
		params.put("clientTestProgram", "TestProgram");

		try {
			String sql = SQLBuilder.buildNamedParameterSQL(sqltemplate, params);
			List<Program> pros = pss2SqlExecutor.doNativeQuery(sql, params, new ListConverter<Program>(new ColumnAsFeatureFactory<Program>(Program.class)));
			if (pros!=null) 
				result.addAll(pros);
		}catch(Exception e){
			log.debug(e);
			throw new EJBException(e);
		}

		return result;
	}

	@Override
	public List<EventValue> getActiveEvent(List<String> programClass, boolean commercial) {
		List<EventValue> result=new ArrayList<EventValue>();
		List<String> drwPrograms=new ArrayList<String>();

		List<String> programs=new ArrayList<String>();
		programs.addAll(programClass);
		
		if (programs.contains(PROGRAM_CLASS_SDP)) {
			if (commercial) {
//				result.addAll(getSDPActiveEvent(PROGRAM_CLASS_SDP, true));
				result.addAll(getSDPEvent2013(PROGRAM_CLASS_SDP, true,true));
			} else {
//				result.addAll(getSDPActiveEvent(PROGRAM_CLASS_SDP, false));
				result.addAll(getSDPEvent2013(PROGRAM_CLASS_SDP, false,true));
			}
			programs.remove(PROGRAM_CLASS_SDP);
		}
		
		if (programs.contains(PROGRAM_CLASS_BIP)) {
			result.addAll(getBIPActiveEvent(PROGRAM_CLASS_BIP));
			result.addAll(getBIP2013ActiveEvent());
			
//			result.addAll(getBIPActiveEvent(PROGRAM_CLASS_BIP));
			
			programs.remove(PROGRAM_CLASS_BIP);
		}
		
		if (programs.contains(PROGRAM_CLASS_API)) {
			drwPrograms.add(PROGRAM_CLASS_API);
//			result.addAll(getDRWActiveEvent(drwPrograms));
			result.addAll(getAPIActiveEvent(drwPrograms));
			programs.remove(PROGRAM_CLASS_API);
		}
		
		if (programs.size()>0)
			result.addAll(getPSS2ActiveEvent(programs));
		
		return result;
	}

	@Override
	public List<EventValue> getScheduledEvent(List<String> programClass, boolean commercial) {
		List<EventValue> result=new ArrayList<EventValue>();
		List<String> drwPrograms=new ArrayList<String>();
		
		List<String> programs=new ArrayList<String>();
		programs.addAll(programClass);
		if (programs.contains(PROGRAM_CLASS_SDP)) {
			if (commercial) {
				//result.addAll(getSDPScheduledEvent(PROGRAM_CLASS_SDP, true));
				result.addAll(getSDPEvent2013(PROGRAM_CLASS_SDP, true,false));
			} else {
				//result.addAll(getSDPScheduledEvent(PROGRAM_CLASS_SDP, false));
				result.addAll(getSDPEvent2013(PROGRAM_CLASS_SDP, false,false));
			}
			programs.remove(PROGRAM_CLASS_SDP);
		}

		if (programs.contains(PROGRAM_CLASS_BIP)) {
			result.addAll(getBIPScheduledEvent(PROGRAM_CLASS_BIP));
			result.addAll(getBIP2013ScheduledEvent());
			
//			result.addAll(getBIPScheduledEvent(PROGRAM_CLASS_BIP));
//			result.addAll(getBIP2013ScheduledEvent());
			programs.remove(PROGRAM_CLASS_BIP);
		}

		if (programs.contains(PROGRAM_CLASS_API)) {
			drwPrograms.add(PROGRAM_CLASS_API);
			//result.addAll(getDRWScheduledEvent(drwPrograms));
			result.addAll(getAPIScheduledEvent(drwPrograms));
			programs.remove(PROGRAM_CLASS_API);
		}
		
		if (programs.size()>0)
			result.addAll(getPSS2ScheduledEvent(programs));
		
		return result;
	}
	
	@Override
	public List<EventValue> getHistoryEvent(String programClass, String product,
			Date start, Date end, List<String> zipCodes) {
		List<EventValue> result=new ArrayList<EventValue>();
		
		Date startTime = null;
		if (start!=null) startTime=DateUtil.getStartOfDay(start);
		Date endTime=null;
		if (end!=null) endTime=DateUtil.getEndOfDay(end);
		
		if(endTime!=null&&!endTime.before(new Date())){
			endTime = new Date();
		}
		
		if (isDRWProgram(programClass)) {
			result=getDRWHistoryEvent(programClass, product, start, endTime, zipCodes);
		} else {
			if (programClass.equals("CPP")) {
				SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
				try {
					Date pointDate=format.parse("2011-01-01");
					if (product == null){
						result.addAll(getPss2SAIHistoryEvent(programClass, "SAI", "Commercial", startTime, endTime));
						if (pointDate.getTime()<endTime.getTime()) {
							if (pointDate.getTime()>startTime.getTime())
								startTime = pointDate;
							result.addAll(getPss2SAIHistoryEvent(programClass, "SAI", "Residential", startTime, endTime));
						}
					} else if (product.equals("Residential")) {
						if (pointDate.getTime()<endTime.getTime()) {
							if (pointDate.getTime()>startTime.getTime())
								startTime = pointDate;
							result.addAll(getPss2SAIHistoryEvent(programClass, "SAI", "Residential", startTime, endTime));
						} else {
							throw new EJBException("No residential data exists before Jan 01, 2011");
						}
					} else {
						result=getPss2SAIHistoryEvent(programClass, "SAI", "Commercial", startTime, endTime);
					}
				} catch (ParseException e) {
					log.error(e);
				}
					
			}else if(programClass.equals("CBP")){
				//1 ->Retrieve data from drwebsite database
				List<EventValue> resultInDrw=getDRWHistoryEvent(programClass, product, startTime, endTime,zipCodes);
				//2 ->Retrieve data from pss2 database
				List<EventValue> resultInPss2 = new ArrayList<EventValue>();
				if(zipCodes!=null&&zipCodes.size()>0){
					//do nothing
				}else{
					resultInPss2=getPss2HistoryEvent(programClass, product, startTime, endTime);	
				}
				
				//3 ->Filter the same data
				Map<String,EventValue> map = new TreeMap<String,EventValue>();
				for(EventValue ev:resultInDrw){
					map.put(ev.getEventKey(), ev);
				}
				for(EventValue ev:resultInPss2){
					if(!map.containsKey(ev.getEventKey())){
						map.put(ev.getEventKey(), ev);
					}
				}
				Collection<EventValue> list =(Collection) map.values();
				for(EventValue value :list){
					result.add(value);
				}
			}
			else {
				result=getPss2HistoryEvent(programClass, product, startTime, endTime);
			}
		}

		return result;
	}

	@Override
	public List<EventValue> getListView(List<String> products, String county, String city, List<String> zipCodes) {
		List<EventValue> result=new ArrayList<EventValue>();
		
		List<String> searchSdpProducts=new ArrayList<String>();
		List<String> searchDrwProducts=new ArrayList<String>();
		for (String product:products) {
			if (Arrays.asList(sdpProducts).contains(product))
				searchSdpProducts.add(product);
			else
				searchDrwProducts.add(product);
		}
		
		// only for SDP program
		if (searchSdpProducts.size()>0) {
			String sqltemplate = getListViewSDPSQLTemplate();
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("products", products);
			params.put("county", county);
			params.put("city", city);
			params.put("zipCodes", zipCodes);
			
			result.addAll(getLocationEntry(sqltemplate, params));
		}
		//for BIP2013 programs
		boolean bipFlag = false;
		for (String product:products) {
			if(product.contains("BIP2013")){
				bipFlag = true;
			}
		}
		if(bipFlag){
			String sqltemplate=" SELECT 	CONCAT(tmp.eventID,IF(l.type IS NOT NULL, l.type ,'SLAP')) AS eventKey,	tmp.* FROM( " +
			" SELECT ea.*, ec.block,ec.zipCode, ec.cityName, ec.countyName FROM ( select e.uuid as eventID,ed.uuid as eventDtailID,  " +
			" 'Time-of-Use Base Interruptible Program' as utilityProgramName,'Time-of-Use Base Interruptible Program' as longProgramName,'BIP2013' as programClass, 'BIP2013' as product, e.startTime,e.issuedTime issueTime, IF(ed.actualEndTime, ed.actualEndTime, ed.estimatedEndTime) as endTime,  " +
			" ed.locationID,	ed.allLocationType from event e, event_detail ed  " +
			" where programName = 'BIP2013' and e.uuid=ed.event_uuid   " +
			" and e.startTime<=NOW() and (ed.actualEndTime is null or ed.actualEndTime > NOW()) ) ea,  " +
			" event_detail eed, zipcode_entry ec WHERE eed.uuid=ea.eventDtailID AND eed.uuid=ec.eventDetail_uuid  " +
			" [AND ec.zipCode IN ${zipCodes}] [AND ec.cityName=${city}] [AND ec.countyName=${county}]  " +
			" ) tmp LEFT JOIN location l ON tmp.locationID = l.id  " +
			" ORDER BY programClass, product, startTime, endTime, zipCode, cityName, countyName ";
			
			
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("county", county);
			params.put("city", city);
			params.put("zipCodes", zipCodes);
			
			result.addAll(getLocationAndBlockEntry(sqltemplate, params));
		}else{
			// only for API program
			if (searchDrwProducts.size()>0) {
//				String sqltemplate= " SELECT ea.*, ec.zipCode, ec.cityName, ec.countyName FROM " +
//									" ( SELECT p.utilityName AS utilityProgramName, p.programClass, p.longProgramName, p.name AS product, " +
//									" e.startTime, IF(ed.actualEndTime, ed.actualEndTime, ed.estimatedEndTime) AS endTime, " +
//									" ed.uuid AS eventKey " +
//									" FROM event e, event_detail ed, program p " +
//									" WHERE p.name IN ${products} AND p.product=e.product AND e.uuid=ed.event_uuid  " +
//									" AND e.startTime<=NOW() AND (ed.actualEndTime IS NULL OR ed.actualEndTime > NOW()) ) ea, " +
//									" event_detail eed, zipcode_entry ec " +
//									" WHERE eed.uuid=ea.eventKey AND eed.uuid=ec.eventDetail_uuid " +
//									" [AND ec.zipCode IN ${zipCodes}] [AND ec.cityName=${city}] [AND ec.countyName=${county}] " +
//									" ORDER BY programClass, product, startTime, zipCode, cityName, countyName ";
				
				String sqltemplate=" SELECT 	CONCAT(tmp.eventID,IF(l.type IS NOT NULL, l.type ,'SLAP')) AS eventKey,	tmp.* FROM( " +
				" SELECT ea.*, ec.block,ec.zipCode, ec.cityName, ec.countyName FROM ( SELECT e.uuid AS eventID,ed.uuid AS eventDtailID, p.utilityName AS utilityProgramName,  " +
				" p.programClass, p.longProgramName, p.name AS product, e.startTime,e.issuedTime issueTime, IF(ed.actualEndTime, ed.actualEndTime, ed.estimatedEndTime) AS endTime,  " +
				" ed.locationID,	ed.allLocationType FROM event e, event_detail ed, program p  " +
				" WHERE p.programClass IN ('API') AND p.product=e.product AND e.uuid=ed.event_uuid   " +
				" AND e.startTime<=NOW() AND (ed.actualEndTime IS NULL OR ed.actualEndTime > NOW()) ) ea,  " +
				" event_detail eed, zipcode_entry ec WHERE eed.uuid=ea.eventDtailID AND eed.uuid=ec.eventDetail_uuid  " +
				" [AND ec.zipCode IN ${zipCodes}] [AND ec.cityName=${city}] [AND ec.countyName=${county}]  " +
				" ) tmp LEFT JOIN location l ON tmp.locationID = l.id  " +
				" ORDER BY programClass, product, startTime, endTime, zipCode, cityName, countyName ";
				
				
				Map<String,Object> params = new HashMap<String,Object>();
				params.put("products", searchDrwProducts);
				params.put("county", county);
				params.put("city", city);
				params.put("zipCodes", zipCodes);
				
//				result.addAll(getLocationEntry(sqltemplate, params));
				result.addAll(getLocationAndBlockEntry(sqltemplate, params));
			}
		}

		
		return result;
	}

	@Override
	public List<WeatherValue> getHistoryTems(String programName, Date startDate, Date endDate) {
    	String sqltemplate = " select distinct p.name, ws.date, ws.high from program_rtp_config p, " +
    						 " (select DATE_ADD(w.date, INTERVAL 1 DAY)  as date, w.high, pro.uuid as program_uuid, case when exists (select * from season_config sc " +
    						 " where sc.startDate<=DATE_ADD(w.date, INTERVAL 1 DAY) and sc.endDate>=DATE_ADD(w.date, INTERVAL 1 DAY) and " +
    						 " sc.program_uuid=pro.uuid and sc.name!='WINTER' and sc.name!='SUMMER') " +
    						 "  then 'WEEKEND'" +
    						 " when (weekday(DATE_ADD(w.date, INTERVAL 1 DAY)) = 5 or weekday(DATE_ADD(w.date, INTERVAL 1 DAY)) = 6) then 'WEEKEND'" +
    						 " else s.name end as seasonName " +
    						 " from season_config s, noaa_weather w, program pro" +
    						 " where s.startDate<=DATE_ADD(w.date, INTERVAL 1 DAY) and s.endDate>=DATE_ADD(w.date, INTERVAL 1 DAY) and s.program_uuid=pro.uuid" +
    						 " and pro.name= ${programName}" +
    						 " and (s.name='WINTER' or s.name='SUMMER') " +
    						 " [and w.date >=${start}] [and w.date <=${end}) ws] " +
    						 " where p.program_uuid = ws.program_uuid" +
    						 " and p.seasonName=ws.seasonName and p.startTemperature <= ws.high " +
    						 " and (p.endTemperature > ws.high or p.endTemperature=0) order by ws.date desc";
    	 
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("programName", programName);
		params.put("start", com.akuacom.utils.lang.DateUtil.getDate(startDate,-1));
		params.put("end", com.akuacom.utils.lang.DateUtil.getDate(endDate,-1));
		
		Map<String, String> columnToFeatureMap=new HashMap<String, String>();
		columnToFeatureMap.put("name", "pricingCategory");
		columnToFeatureMap.put("high", "temperature");
		
		List<WeatherValue> result=getWeather(sqltemplate, params, columnToFeatureMap);
		return result;
	}

	@Override
	public List<WeatherValue> getForcast(Date date) {
		List<WeatherValue> value=new ArrayList<WeatherValue>();
		
		Map<Date, Double> map=getForcastTemp(date);
		for (Date key: map.keySet()) {
			WeatherValue weather=new WeatherValue();
			weather.setDate(key);
			weather.setTemperature(map.get(key));
			weather.setPricingCategory(getCategory(RTP_AGRICULTURAL_PROGRAM, key, map.get(key)));
			value.add(weather);
		}
			
		return value;
	}

	@Override
	public List<String> getCountyName(){
		List<String> counties=new ArrayList<String>();
		
		String sqltemplate=" SELECT DISTINCT countyName FROM zipcode WHERE countyName IS NOT NULL ORDER BY countyName ASC ";
		Map<String,Object> params = new HashMap<String,Object>();
		
		try {
			String sql = SQLBuilder.buildNamedParameterSQL(sqltemplate, params);
			counties = drwSqlExecutor.doNativeQuery(sql, params, new SimpleListConverter<String>(String.class));
		}catch(Exception e){
			log.debug(e);
			throw new EJBException(e);
		}
		
		return counties;
	}
	@Override
	public List<String> getCityName(){
		List<String> cities=new ArrayList<String>();
		
		String sqltemplate=" SELECT DISTINCT cityName FROM zipcode WHERE cityName IS NOT NULL AND cityName !='' ORDER BY cityName ASC ";
		Map<String,Object> params = new HashMap<String,Object>();
		
		try {
			String sql = SQLBuilder.buildNamedParameterSQL(sqltemplate, params);
			cities = drwSqlExecutor.doNativeQuery(sql, params, new SimpleListConverter<String>(String.class));
		}catch(Exception e){
			log.debug(e);
			throw new EJBException(e);
		}
		
		return cities;
	}
	@Override
	public List<String> getCity(String countyName){
		List<String> cities=new ArrayList<String>();
		
		String sqltemplate=" SELECT DISTINCT cityName FROM zipcode WHERE countyName=${countyName} ORDER BY cityName ASC ";
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("countyName", countyName);
		
		try {
			String sql = SQLBuilder.buildNamedParameterSQL(sqltemplate, params);
			cities = drwSqlExecutor.doNativeQuery(sql, params, new SimpleListConverter<String>(String.class));
		}catch(Exception e){
			log.debug(e);
			throw new EJBException(e);
		}
		
		return cities;
	}

	private Map<Date, Double> getForcastTemp(Date date) {
		Map<Date, Double> temps=new TreeMap<Date, Double>();
		Date today=DateUtil.getStartOfDay(date);
		Date yesterday = new Date(today.getTime() - DateUtil.MSEC_IN_DAY);

		String sqltemplate=" SELECT high FROM noaa_weather WHERE date = ${yesterday} ";
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("yesterday", yesterday);
		
		try {
			String sql = SQLBuilder.buildNamedParameterSQL(sqltemplate, params);
			Double todayHigh = pss2SqlExecutor.doNativeQuery(sql,params, new CellConverter<Double>(Double.class));
			
			if (todayHigh==null)
				throw new EJBException("no today's high temprature available");
			
			temps.put(today, todayHigh);
			
			sqltemplate=" SELECT forecastHigh0, forecastHigh1, forecastHigh2, forecastHigh3, forecastHigh4 " +
						" FROM noaa_weather WHERE date = ${today} ";
			
			params = new HashMap<String,Object>();
			params.put("today", today);
			
			sql = SQLBuilder.buildNamedParameterSQL(sqltemplate, params);
			List<Double> forcastTemps= pss2SqlExecutor.doNativeQuery(sql, params, 
					new ColumnsAsListConverter<Double>(Double.class));
			if (forcastTemps==null || forcastTemps.size()==0) 
				throw new EJBException("no forecast temprature available");
			
			Date tomorrow =  new Date(today.getTime() + DateUtil.MSEC_IN_DAY);
			for (Double temp:forcastTemps) {
				temps.put(tomorrow, temp);
				tomorrow = new Date(tomorrow.getTime() + DateUtil.MSEC_IN_DAY);
			}
			
		} catch (SQLBuilderException e) {
			log.debug(e);
			throw new EJBException(e);
		} catch (SQLException e) {
			log.debug(e);
			throw new EJBException(e);
		}

		return temps;
	}
	
	private String getCategory(String programName, Date date, Double temp) {
		String sqltemplate = " SELECT DISTINCT prc.name FROM " +
							 " ( SELECT seasonName FROM " +
							 " ( SELECT DISTINCT 'WEEKEND' AS seasonName FROM season_config s, program p " +
							 " WHERE (s.startDate<= ${date} " +
							 " AND s.endDate>= ${date} AND s.name!='SUMMER' AND s.name!='WINTER' " +
							 " AND p.name=${programName} AND p.uuid=s.program_uuid) " +
							 "  OR (WEEKDAY(${date})=5 OR WEEKDAY(${date})=6) " +
							 " UNION " +
							 " SELECT s.name FROM season_config s, program p WHERE s.startDate<= ${date} " +
							 " AND p.name=${programName} AND p.uuid=s.program_uuid" +
							 " AND s.endDate>= ${date} AND (s.name='SUMMER' OR s.name='WINTER') ) a " +
							 " LIMIT 0,1 ) sn, program_rtp_config prc, program p " +
							 " WHERE sn.seasonName=prc.seasonName AND (prc.startTemperature <= ${temp} OR prc.startTemperature=0)" +
							 " AND (prc.endTemperature > ${temp} OR prc.endTemperature=0)" +
							 " AND p.name=${programName} AND p.uuid=prc.program_uuid";
		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("programName", programName);
		params.put("date", date);
		params.put("temp", temp);

		String category;
		try {
			String sql = SQLBuilder.buildNamedParameterSQL(sqltemplate, params);
			category = pss2SqlExecutor.doNativeQuery(sql,params, new CellConverter<String>(String.class));
		}catch(Exception e){
			log.debug(e);
			throw new EJBException(e);
		}

		return category;
	}
	
	private boolean isDRWProgram(String programClass) {
		return Arrays.asList(drwProgramClasses).contains(programClass);
	}
	
	private List<EventValue> getDRWHistoryEvent(String programClass, String product,
			Date start, Date end, List<String> zipCodes){
		
		if (programClass!=null&&programClass.equalsIgnoreCase("SDP")) {
			return getSDPHistoryEvent(programClass, product, start, end, zipCodes);
		}
		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("programClass", programClass);
		params.put("product", product);
		params.put("start", start);
		params.put("end", end);
		
		String sqltemplate;
		List<EventValue> result = new ArrayList<EventValue>();
		
		if(programClass!=null&&programClass.equalsIgnoreCase("CBP")){
			sqltemplate=	" SELECT DISTINCT e.eventName, p.longProgramName, p.programClass, p.utilityName AS utilityProgramName, p.name AS product, " +
			" e.startTime, ed.actualEndTime as endTime,c.block, " +
			" CONCAT(e.product,CONCAT(CAST(e.startTime as char),IF(ed.actualEndTime is not null, CAST(ed.actualEndTime as char),'NULL')))  as eventKey   " +
			" FROM event e, event_detail ed, program p, zipcode_entry c " +
			" WHERE e.uuid=ed.event_uuid [AND p.programClass=${programClass}] [AND p.name=${product}] " +
			" AND e.product=p.product " +
			" [AND e.startTime >= ${start}] [AND ed.actualEndTime <= ${end}] " +
			" AND c.eventDetail_uuid=ed.uuid " +
			" ORDER BY product, startTime DESC, endTime DESC";
			result= getEventBlockEntry(sqltemplate, params);
		}
		else if(programClass!=null&&programClass.equalsIgnoreCase("API")){
			sqltemplate=	" SELECT DISTINCT p.longProgramName, p.programClass, p.utilityName AS utilityProgramName, p.name AS product, " +
			" e.startTime, ed.actualEndTime as endTime,c.block, " +
			" CONCAT(CAST(e.startTime AS CHAR),IF(ed.actualEndTime IS NOT NULL, CAST(ed.actualEndTime AS CHAR),'NULL'))  AS eventKey   " +
			" FROM event e, event_detail ed, program p, zipcode_entry c " +
			" WHERE e.uuid=ed.event_uuid [AND p.programClass=${programClass}] [AND p.name=${product}] " +
			" AND e.product=p.product " +
			" [AND e.startTime >= ${start}] [AND ed.actualEndTime <= ${end}] " +
			" AND c.eventDetail_uuid=ed.uuid " +
			" ORDER BY product, startTime DESC, endTime DESC";
			result= getEventBlockEntry(sqltemplate, params);
		}else if(programClass!=null&&programClass.equalsIgnoreCase("BIP")){
			sqltemplate=	" SELECT DISTINCT CONCAT(CAST(e.startTime AS CHAR),IF(ed.actualEndTime IS NOT NULL, CAST(ed.actualEndTime AS CHAR),'NULL'))  AS eventKey, " +
			" p.longProgramName, p.programClass, p.utilityName AS utilityProgramName, p.name AS product, " +
			" e.startTime, ed.actualEndTime as endTime,ed.blockNames AS block " +
			" FROM event e, event_detail ed, program p " +
			" WHERE e.uuid=ed.event_uuid [AND p.programClass=${programClass}] " +
			" AND e.product=p.product " +
			" [AND e.startTime >= ${start}] [AND ed.actualEndTime <= ${end}] " +
			" ORDER BY startTime DESC, endTime DESC";
			
			
			List<EventValue> oldBIP = getEventBlockEntry(sqltemplate, params);
							
			sqltemplate=	" SELECT DISTINCT CONCAT(CAST(e.startTime AS CHAR),IF(ed.actualEndTime IS NOT NULL, CAST(ed.actualEndTime AS CHAR),'NULL'))  AS eventKey, " +
			" 'Time-of-Use Base Interruptible Program' as longProgramName, 'BIP' as programClass, 'Time-of-Use Base Interruptible Program' as utilityProgramName,'BIP' as product, " +
			" e.startTime, ed.actualEndTime as endTime,c.block " +
			" from event e,event_detail ed,zipcode_entry c  " +
			" where e.programName = 'BIP' and e.product = 'BIP2013' and e.uuid = ed.event_uuid " +
			" [AND e.startTime >= ${start}] [AND ed.actualEndTime <= ${end}] " +
			" AND c.eventDetail_uuid=ed.uuid " +
			" ORDER BY startTime DESC, endTime DESC";
			
			List<EventValue> newBIP = getEventBlockEntry(sqltemplate, params);
			
			Map<String,EventValue> map = new TreeMap<String,EventValue>();
			for(EventValue ev:newBIP){
				map.put(ev.getEventKey(), ev);
			}
			for(EventValue ev:oldBIP){
				if(!map.containsKey(ev.getEventKey())){
					map.put(ev.getEventKey(), ev);
				}
			}
			Collection<EventValue> list =(Collection) map.values();
			for(EventValue value :list){
				result.add(value);
			}
		}
		
		result = filterHistory(result,zipCodes);
		return result;
	}
	private List<EventValue> filterHistory(List<EventValue> input,List<String> zipCodes){
		List<EventValue> result = new ArrayList<EventValue>();
		List<EventValue> others = new ArrayList<EventValue>();
		if (zipCodes!=null && zipCodes.size()>0) {
			Date compareStart =DateUtil.parse("2012-12-31 23:59:59", "yyyy-MM-dd HH:mm:ss");
			for(EventValue ev:input){
				Date startTime =ev.getStartTime();
				if(startTime.before(compareStart)){
					result.add(ev);
				}else{
					others.add(ev);
				}
			}
			String sqltemplate=" select distinct block  from zipcode where zipCode in ${zipCodes}";
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("zipCodes", zipCodes);
			try {
				String sql = SQLBuilder.buildNamedParameterSQL(sqltemplate, params);
				List<String> blocks = drwSqlExecutor.doNativeQuery(sql,params,new SimpleListConverter<String>(String.class));
				
				for(EventValue ev:others){
					boolean flag = false;
					List<String> bList = ev.getBlocks();
						for(String block:bList){
							for(String b:blocks){
								if(b!=null&&(!b.equalsIgnoreCase(""))){
									if(b.equalsIgnoreCase(block)){
										flag = true;
										break;
									}
								}
							}
							if(flag){
								break;
							}
						}
					if(flag){
						result.add(ev);
					}
				}
			}catch(Exception e){
				log.error(e);
			}
		}else{
			result = input;
		}
		return result;
	}
	private List<EventValue> getSDPHistoryEvent(String programClass, String product,
			Date start, Date end, List<String> zipCodes){
		List<EventValue> result=new ArrayList<EventValue>();
		
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date pointDate=format.parse("2012-01-01");
			if (product == null){
				result.addAll(doGetSDPHistoryEvent(programClass, product, start, end, zipCodes));
				if (pointDate.getTime()>start.getTime()) {
					Date endTime=end;
					if (pointDate.getTime()<end.getTime())
						endTime=pointDate;

					result.addAll(doGetSDPRHistoryEvent("APS", start, endTime, zipCodes, "Residential Base"));
					result.addAll(doGetSDPRHistoryEvent("APS-E", start, endTime, zipCodes, "Residential Enhanced"));
				}
			} else if (product.equals("Residential")) {
				result.addAll(doGetSDPHistoryEvent(programClass, product, start, end, zipCodes));
				if (pointDate.getTime()>start.getTime()) {
					Date endTime=end;
					if (pointDate.getTime()<end.getTime())
						endTime=pointDate;

					result.addAll(doGetSDPRHistoryEvent("APS", start, endTime, zipCodes, "Residential Base"));
					result.addAll(doGetSDPRHistoryEvent("APS-E", start, endTime, zipCodes, "Residential Enhanced"));
				}
			} else {
				result.addAll(doGetSDPHistoryEvent(programClass, product, start, end, zipCodes));
			}
		} catch (ParseException e) {
			log.error(e);
		}
		return result;
	}
	
	private List<EventValue> doGetSDPRHistoryEvent(String product, Date start, Date end, List<String> zipCodes, String display){
		StringBuilder builder=new StringBuilder();
		builder.append(" SELECT DISTINCT ed.uuid,  ${display} AS product, e.startTime, ed.actualEndTime as endTime,");
		builder.append(" CONCAT( CONCAT(p.name,CAST(e.startTime AS CHAR)),IF(ed.actualEndTime IS NOT NULL, CAST(ed.actualEndTime AS CHAR),'NULL'))  AS eventKey, ed.blockNames AS block    ");
		builder.append(" FROM event e, event_detail ed, program p ");
		if (zipCodes!=null && zipCodes.size()>0)
			builder.append(", zipcode_entry c ");
		builder.append(" WHERE e.uuid=ed.event_uuid ");
		builder.append(" AND e.product=p.product");
		builder.append(" [AND e.product=${product}] ");
		builder.append(" [AND e.startTime >= ${start}] [AND ed.actualEndTime <= ${end}] ");
		if (zipCodes!=null && zipCodes.size()>0) {
			builder.append(" [AND c.zipCode IN ${zipCodes}] ");
			builder.append(" AND c.eventDetail_uuid=ed.uuid ");
		}
		
		builder.append("  ORDER BY product, startTime DESC, endTime DESC");

		Map<String,Object> params = new HashMap<String,Object>();
		params.put("display", display);
		params.put("product", product);
		params.put("start", start);
		params.put("end", end);
		if (zipCodes!=null && zipCodes.size()>0)
			params.put("zipCodes", zipCodes);

		//return getDRWEvent(builder.toString(), params);
		return getEventBlockEntry(builder.toString(), params);
	}

	private List<EventValue> doGetSDPHistoryEvent(String programClass, String product,
			Date start, Date end, List<String> zipCodes){
		StringBuilder builder=new StringBuilder();
		builder.append(" SELECT DISTINCT ed.uuid, p.longProgramName, p.programClass, p.utilityName AS utilityProgramName, p.name AS product, ");
		builder.append("  e.startTime, ed.actualEndTime as endTime, ");
		builder.append("  CONCAT( CONCAT(p.name,CAST(e.startTime AS CHAR)),IF(ed.actualEndTime IS NOT NULL, CAST(ed.actualEndTime AS CHAR),'NULL'))  AS eventKey, ed.blockNames AS block    ");
		builder.append(" FROM event e, event_detail ed, program p ");
		if (zipCodes!=null && zipCodes.size()>0)
			builder.append(", zipcode_entry c ");
		builder.append(" WHERE e.uuid=ed.event_uuid [AND p.programClass=${programClass}] [AND p.name = ${product}] ");
		builder.append(" AND e.product=p.product ");
		builder.append(" [AND e.startTime >= ${start}] [AND ed.actualEndTime <= ${end}] ");
		if (zipCodes!=null && zipCodes.size()>0) {
			builder.append(" [AND c.zipCode IN ${zipCodes}] ");
			builder.append(" AND c.eventDetail_uuid=ed.uuid ");
		}
		builder.append("  ORDER BY product, startTime DESC, endTime DESC");

		Map<String,Object> params = new HashMap<String,Object>();
		params.put("programClass", programClass);
		params.put("product", product);
		params.put("start", start);
		params.put("end", end);
		if (zipCodes!=null && zipCodes.size()>0)
			params.put("zipCodes", zipCodes);

		//return getDRWEvent(builder.toString(), params);
		return getEventBlockEntry(builder.toString(), params);
	}

	
	private List<EventValue> getPss2SAIHistoryEvent(String programClass, String program, String product,
			Date start, Date end){
		String sqltemplate=	" SELECT p.longProgramName, p.programClass, p.utilityProgramName, ${product} AS product, e.startTime, e.endTime " +
							" FROM history_event e, program p" +
							" WHERE e.programName=p.name [AND p.programClass=${programClass}] [AND e.programName=${program}] " +
							" [AND e.startTime >= ${start}] [AND e.endTime <= ${end}] AND e.cancelled=${cancelled} " +
							" ORDER BY product, startTime DESC, endTime DESC";
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("programClass", programClass);
		params.put("product", product);
		params.put("program", program);
		params.put("start", start);
		params.put("end", end==null?null:DateUtil.getEndOfDay(end));
		params.put("cancelled", false);

		return getPSS2Event(sqltemplate, params);
	}
	
	private List<EventValue> getPss2HistoryEvent(String programClass, String product,
			Date start, Date end){
		String distinct="";
		if (programClass.equals("DBP"))
			distinct ="DISTINCT";
		
		String sqltemplate=	" SELECT " + distinct +
							" e.eventName, p.longProgramName, p.programClass, p.utilityProgramName, p.name AS product, e.startTime, e.endTime, " +
							" CONCAT(p.name,CONCAT(CAST(e.startTime as char),IF(e.endTime is not null, CAST(e.endTime as char),'NULL')))  as eventKey "+
							" FROM history_event e, program p" +
							" WHERE e.programName=p.name [AND p.programClass=${programClass}] [AND e.programName=${product}] " +
							" [AND e.startTime >= ${start}] [AND e.endTime <= ${end}] AND e.cancelled=${cancelled} " +
							" ORDER BY product, startTime DESC, endTime DESC";
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("programClass", programClass);
		params.put("product", product);
		params.put("start", start);
		params.put("end", end==null?null:DateUtil.getEndOfDay(end));
		params.put("cancelled", false);

		return getPSS2Event(sqltemplate, params);
	}
	
	// only for API
	// API displays all events with block per location
	private List<EventValue> getDRWActiveEvent(List<String> programClass){
		String sqltemplate= " SELECT ea.*, ec.block FROM " +
				" ( SELECT p.utilityName AS utilityProgramName, p.programClass, p.longProgramName, p.name AS product, " +
				" e.startTime,e.issuedTime issueTime, IF(ed.actualEndTime, ed.actualEndTime, ed.estimatedEndTime) AS endTime, " +
				" ed.uuid AS eventKey " +
				" FROM event e, event_detail ed, program p " +
				" WHERE p.programClass IN ${programClass} AND p.product=e.product AND e.uuid=ed.event_uuid  " +
				" AND e.startTime<=NOW() AND (ed.actualEndTime IS NULL OR ed.actualEndTime > NOW()) ) ea, " +
				" event_detail eed, zipcode_entry ec " +
				" WHERE eed.uuid=ea.eventKey AND eed.uuid=ec.eventDetail_uuid " +
				" ORDER BY programClass, product, startTime, endTime ";
		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("programClass", programClass);

		return getEventBlockEntry(sqltemplate, params);
	}
	
	private List<EventValue> getAPIActiveEvent(List<String> programClass){
		String sqltemplate=
			" SELECT 	CONCAT(tmp.eventID,IF(l.type IS NOT NULL, l.type ,'SLAP')) AS eventKey,	tmp.* FROM(" +
			" SELECT ea.*, ec.block FROM ( SELECT e.uuid AS eventID,ed.uuid AS eventDtailID, p.utilityName AS utilityProgramName, " +
			" p.programClass, p.longProgramName, p.name AS product, e.startTime,e.issuedTime issueTime, IF(ed.actualEndTime, ed.actualEndTime, ed.estimatedEndTime) AS endTime, " +
			" ed.locationID,	ed.allLocationType FROM event e, event_detail ed, program p " +
			" WHERE p.programClass IN ${programClass} AND p.product=e.product AND e.uuid=ed.event_uuid  " +
			" AND e.startTime<=NOW() AND (ed.actualEndTime IS NULL OR ed.actualEndTime > NOW()) ) ea, " +
			" event_detail eed, zipcode_entry ec WHERE eed.uuid=ea.eventDtailID AND eed.uuid=ec.eventDetail_uuid " +
			"  ) tmp LEFT JOIN location l ON tmp.locationID = l.id ORDER BY programClass, product, startTime, endTime ";
		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("programClass", programClass);

		return getEventBlockEntry(sqltemplate, params);
	}
	
	private List<EventValue> getBIP2013ActiveEvent(){
		String sqltemplate=
			" select 	CONCAT(tmp.eventID,IF(l.type is not null, l.type ,'SLAP')) as eventKey,	tmp.* from( " +
			" select ea.*, ec.block from (   select e.uuid as eventID,ed.uuid as eventDtailID,  " +
			" 'Time-of-Use Base Interruptible Program' as utilityProgramName,'Time-of-Use Base Interruptible Program' as longProgramName,'BIP2013' as programClass, 'BIP2013' as product, e.startTime,e.issuedTime issueTime, IF(ed.actualEndTime, ed.actualEndTime, ed.estimatedEndTime) as endTime, " +
			" ed.locationID,	ed.allLocationType from event e, event_detail ed " +
			" where programName = 'BIP2013' and e.uuid=ed.event_uuid  " +
			" and e.startTime<=NOW() and (ed.actualEndTime is null or ed.actualEndTime > NOW()) ) ea, " +
			" event_detail eed, zipcode_entry ec WHERE eed.uuid=ea.eventDtailID AND eed.uuid=ec.eventDetail_uuid " +
			"  ) tmp LEFT JOIN location l ON tmp.locationID = l.id ORDER BY programClass, product, startTime, endTime ";
		
		Map<String,Object> params = new HashMap<String,Object>();

		return getEventBlockEntry(sqltemplate, params);
	}
	private List<EventValue> getBIP2013ScheduledEvent(){
		String sqltemplate=
			" select 	CONCAT(tmp.eventID,IF(l.type is not null, l.type ,'SLAP')) as eventKey,	tmp.* from( " +
			" select ea.*, ec.block from (   select e.uuid as eventID,ed.uuid as eventDtailID,  " +
			" 'Time-of-Use Base Interruptible Program' as utilityProgramName,'Time-of-Use Base Interruptible Program' as longProgramName,'BIP2013' as programClass, 'BIP2013' as product, e.startTime,e.issuedTime issueTime, IF(ed.actualEndTime, ed.actualEndTime, ed.estimatedEndTime) as endTime, " +
			" ed.locationID,	ed.allLocationType from event e, event_detail ed " +
			" where programName = 'BIP2013' and e.uuid=ed.event_uuid  " +
			" and e.startTime > NOW() ) ea, " +
			" event_detail eed, zipcode_entry ec WHERE eed.uuid=ea.eventDtailID AND eed.uuid=ec.eventDetail_uuid " +
			"  ) tmp LEFT JOIN location l ON tmp.locationID = l.id ORDER BY programClass, product, startTime, endTime ";
		
		Map<String,Object> params = new HashMap<String,Object>();

		return getEventBlockEntry(sqltemplate, params);
	}
	// only for API
	// API displays all events with block per location
	private List<EventValue> getDRWScheduledEvent(List<String> programClass){
		String sqltemplate= " SELECT ea.*, ec.block FROM " +
				" ( SELECT p.utilityName AS utilityProgramName, p.programClass, p.longProgramName, p.name AS product, " +
				" e.startTime,e.issuedTime issueTime, IF(ed.actualEndTime, ed.actualEndTime, ed.estimatedEndTime) AS endTime, " +
				" ed.uuid AS eventKey " +
				" FROM event e, event_detail ed, program p " +
				" WHERE p.programClass IN ${programClass} AND p.product=e.product AND e.uuid=ed.event_uuid  " +
				" AND e.startTime > NOW() ) ea, " +
				" event_detail eed, zipcode_entry ec " +
				" WHERE eed.uuid=ea.eventKey AND eed.uuid=ec.eventDetail_uuid " +
				" ORDER BY programClass, product, startTime, endTime ";

		Map<String,Object> params = new HashMap<String,Object>();
		params.put("programClass", programClass);

		return getEventBlockEntry(sqltemplate, params);
	}
	private List<EventValue> getAPIScheduledEvent(List<String> programClass){
		String sqltemplate=
			" SELECT 	CONCAT(tmp.eventID,IF(l.type IS NOT NULL, l.type ,'SLAP')) AS eventKey,	tmp.* FROM(" +
			" SELECT ea.*, ec.block FROM ( SELECT e.uuid AS eventID,ed.uuid AS eventDtailID, p.utilityName AS utilityProgramName, " +
			" p.programClass, p.longProgramName, p.name AS product, e.startTime,e.issuedTime issueTime, IF(ed.actualEndTime, ed.actualEndTime, ed.estimatedEndTime) AS endTime, " +
			" ed.locationID,	ed.allLocationType FROM event e, event_detail ed, program p " +
			" WHERE p.programClass IN ${programClass} AND p.product=e.product AND e.uuid=ed.event_uuid  " +
			" AND e.startTime > NOW() ) ea, " +
			" event_detail eed, zipcode_entry ec WHERE eed.uuid=ea.eventDtailID AND eed.uuid=ec.eventDetail_uuid " +
			"  ) tmp LEFT JOIN location l ON tmp.locationID = l.id ORDER BY programClass, product, startTime, endTime ";
		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("programClass", programClass);

		return getEventBlockEntry(sqltemplate, params);
	}	
	
	private List<EventValue> getEventDispatch(String sqltemplate, Map<String,Object> params){
		List<EventValue> result=new ArrayList<EventValue>();
		try {
			String sql = SQLBuilder.buildNamedParameterSQL(sqltemplate, params);
			
			MasterDetailFactory<EventValue, LocationValue> factory = 
					new MasterDetailFactory<EventValue, LocationValue>(
						new ColumnAsFeatureFactory<EventValue>(EventValue.class, "eventKey"),
						new ColumnAsFeatureFactory<LocationValue>(LocationValue.class)) {

					private static final long serialVersionUID = 6951017266263011396L;
					public void buildUp(EventValue master, LocationValue detail) {
						master.getLocationMap().put(detail.getEventKey(), detail);
						if (detail.getDispatchType()!=null && !master.getDispatchTypes().contains(detail.getDispatchType())) {
							master.getDispatchTypes().add(detail.getDispatchType());
						}
						if (detail.getDispatchLocation()!=null && !master.getDispatchLocations().contains(detail.getDispatchLocation())) {
							master.getDispatchLocations().add(detail.getDispatchLocation());
						}
					}
				};

			result = (List<EventValue>) drwSqlExecutor.doNativeQuery(sql, params, new ListConverter<EventValue>(factory));
		}catch(Exception e){
			log.debug(e);
			throw new EJBException(e);
		}
		
		return result;
	}

	private List<EventValue> getEventBlockEntry(String sqltemplate, Map<String,Object> params){
		List<EventValue> result=new ArrayList<EventValue>();
		try {
			String sql = SQLBuilder.buildNamedParameterSQL(sqltemplate, params);
			
			MasterDetailFactory<EventValue, LocationValue> factory = 
					new MasterDetailFactory<EventValue, LocationValue>(
						new ColumnAsFeatureFactory<EventValue>(EventValue.class, "eventKey"),
						new ColumnAsFeatureFactory<LocationValue>(LocationValue.class)) {

					private static final long serialVersionUID = 6951017266263011396L;
					public void buildUp(EventValue master, LocationValue detail) {
						master.getLocationMap().put(detail.getEventKey(), detail);
						if (detail.getBlock()!=null && !master.getBlocks().contains(detail.getBlock())) {
							master.getBlocks().add(detail.getBlock());
						}
						if (detail.getEventID()!=null && !master.getEventIDList().contains(detail.getEventID())) {
							master.getEventIDList().add(detail.getEventID());
						}
					}
				};

			result = (List<EventValue>) drwSqlExecutor.doNativeQuery(sql, params, new ListConverter<EventValue>(factory));
		}catch(Exception e){
			log.debug(e);
			throw new EJBException(e);
		}
		
		return result;
	}
	
	//only for SDP
	//only one row: earliest start time & latest end time
	private List<EventValue> getSDPActiveEvent(String sdpProgramClass, boolean commercial) {
		String sqltemplate=" SELECT p.utilityName as utilityProgramName, p.programClass, p.longProgramName, " +
				" p.name as product, MIN(e.startTime) AS startTime,MIN(e.issuedTime) AS issueTime, " +
				" IF(MAX(ed.estimatedEndTime) IS NULL OR MAX(ed.actualEndTime)>=MAX(ed.estimatedEndTime), MAX(ed.actualEndTime), MAX(ed.estimatedEndTime)) AS endTime, " +
				" MAX(IF( ed.estimatedEndTime IS NULL AND ed.actualEndTime IS NULL,'1','0')) AS tbdFlag " +
				" FROM event e, event_detail ed, program p" +
				" WHERE p.programClass =${programClass} AND p.product=e.product " +
				" AND e.product IN ${product} AND e.uuid=ed.event_uuid " +
				" AND e.startTime<=NOW() AND (ed.actualEndTime IS NULL OR ed.actualEndTime > NOW()) " +
				" GROUP BY product " +
				" ORDER BY programClass, product, startTime, endTime";
		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("programClass", sdpProgramClass);
		if (commercial)
			params.put("product", Arrays.asList(sdpCommericalRate));
		else
			params.put("product", Arrays.asList(sdpResidentialRate));

		return getDRWEvent(sqltemplate, params);
	}

	//only for SDP
	//only one row: earliest start time & latest end time
	private List<EventValue> getSDPScheduledEvent(String sdpProgramClass, boolean commercial) {
		String sqltemplate=" SELECT p.utilityName as utilityProgramName, p.programClass, p.longProgramName, " +
				" p.name as product, MIN(e.startTime) AS startTime,MIN(e.issuedTime) AS issueTime, " +
				" IF(MAX(ed.estimatedEndTime) IS NULL OR MAX(ed.actualEndTime)>=MAX(ed.estimatedEndTime), MAX(ed.actualEndTime), MAX(ed.estimatedEndTime)) AS endTime " +
				" FROM event e, event_detail ed, program p" +
				" WHERE p.programClass =${programClass} AND p.product=e.product " +
				" AND e.product IN ${product} AND e.uuid=ed.event_uuid " +
				" AND e.startTime > NOW() " +
				" GROUP BY product " +
				" ORDER BY programClass, product, startTime, endTime";
		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("programClass", sdpProgramClass);
		if (commercial)
			params.put("product", Arrays.asList(sdpCommericalRate));
		else
			params.put("product", Arrays.asList(sdpResidentialRate));
		
		return getDRWEvent(sqltemplate, params);
	}
	@Override
	public List<EventValue> getPSS2ActiveEvent(List<String> programClass) {
		String sqltemplate= " SELECT e.uuid as uuid, p.longProgramName, p.programClass, p.utilityProgramName, " +
							" p.name AS product, e.startTime, e.endTime, e.issuedTime issueTime " +
							" FROM event e, program p " +
							" WHERE p.programClass IN ${programClass} AND e.programName=p.name AND e.status =\"ACTIVE\" " +
							" ORDER BY startTime, endTime, programClass, product";
		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("programClass", programClass);
		
		return getPSS2Event(sqltemplate, params);
	}
	@Override
	public List<EventValue> getPSS2ScheduledEvent(List<String> programClass) {
		String sqltemplate= " SELECT e.uuid as uuid, p.longProgramName, p.programClass, p.utilityProgramName, " +
							" p.name AS product, e.startTime,e.issuedTime issueTime, e.endTime " +
							" FROM event e, program p " +
							//" WHERE p.programClass IN ${programClass} and (e.issuedTime< NOW() or e.issuedTime is null) AND e.programName=p.name AND e.status !=\"ACTIVE\" " +
							" WHERE p.programClass IN ${programClass} AND e.programName=p.name AND e.status !=\"ACTIVE\" " +
							" ORDER BY startTime, endTime, programClass, product ";
		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("programClass", programClass);
		
		return getPSS2Event(sqltemplate, params);
	}

	private List<EventValue> getLocationEntry(String sqltemplate, Map<String,Object> params){
		List<EventValue> result=new ArrayList<EventValue>();
		try {
			String sql = SQLBuilder.buildNamedParameterSQL(sqltemplate, params);
			
			MasterDetailFactory<EventValue, LocationValue> factory = 
					new MasterDetailFactory<EventValue, LocationValue>(
						new ColumnAsFeatureFactory<EventValue>(EventValue.class, "eventKey"),
						new ColumnAsFeatureFactory<LocationValue>(LocationValue.class)) {

					private static final long serialVersionUID = 6951017266263011396L;
					public void buildUp(EventValue master, LocationValue detail) {
						master.getLocationMap().put(detail.getEventKey(), detail);
						if (detail.getZipCode()!=null && !master.getZipCodes().contains(detail.getZipCode()))
							master.getZipCodes().add(detail.getZipCode());
						if (detail.getCountyName() !=null && !master.getCounties().contains(detail.getCountyName()))
							master.getCounties().add(detail.getCountyName());
						if (detail.getCityName() !=null && !master.getCities().contains(detail.getCityName()))
							master.getCities().add(detail.getCityName());
					}
				};

			result = (List<EventValue>) drwSqlExecutor.doNativeQuery(sql, params,
					new ListConverter<EventValue>(factory));
		}catch(Exception e){
			log.debug(e);
			throw new EJBException(e);
		}
		
		return result;
	}
	@Override
	public List<EventValue> getLocationAndBlockEntry(String sqltemplate, Map<String,Object> params){
		List<EventValue> result=new ArrayList<EventValue>();
		try {
			String sql = SQLBuilder.buildNamedParameterSQL(sqltemplate, params);
			
			MasterDetailFactory<EventValue, LocationValue> factory = 
					new MasterDetailFactory<EventValue, LocationValue>(
						new ColumnAsFeatureFactory<EventValue>(EventValue.class, "eventKey"),
						new ColumnAsFeatureFactory<LocationValue>(LocationValue.class)) {

					private static final long serialVersionUID = 6951017266263011396L;
					public void buildUp(EventValue master, LocationValue detail) {
						master.getLocationMap().put(detail.getEventKey(), detail);
						if (detail.getZipCode()!=null && !master.getZipCodes().contains(detail.getZipCode()))
							master.getZipCodes().add(detail.getZipCode());
						if (detail.getCountyName() !=null && !master.getCounties().contains(detail.getCountyName()))
							master.getCounties().add(detail.getCountyName());
						if (detail.getCityName() !=null && !master.getCities().contains(detail.getCityName()))
							master.getCities().add(detail.getCityName());
						if (detail.getBlock()!=null && !master.getBlocks().contains(detail.getBlock())) {
							master.getBlocks().add(detail.getBlock());
						}
						if (detail.getEventID()!=null && !master.getEventIDList().contains(detail.getEventID())) {
							master.getEventIDList().add(detail.getEventID());
						}
					}
				};

			result = (List<EventValue>) drwSqlExecutor.doNativeQuery(sql, params,
					new ListConverter<EventValue>(factory));
		}catch(Exception e){
			log.debug(e);
			throw new EJBException(e);
		}
		
		return result;
	}
	private List<EventValue> getDRWEvent(String sqltemplate, Map<String,Object> params){
		List<EventValue> result=new ArrayList<EventValue>();
		try {
			String sql = SQLBuilder.buildNamedParameterSQL(sqltemplate, params);
			result = drwSqlExecutor.doNativeQuery(sql,params, new ListConverter<EventValue>(
					new ColumnAsFeatureFactory<EventValue>(EventValue.class)));
		}catch(Exception e){
			log.debug(e);
			throw new EJBException(e);
		}
		
		return result;
	}

	private List<WeatherValue> getWeather(String sqltemplate, Map<String,Object> params, Map<String,String> columnToFeatureMap) {
		List<WeatherValue> result=new ArrayList<WeatherValue>();
		try {
			String sql = SQLBuilder.buildNamedParameterSQL(sqltemplate, params);
			result = pss2SqlExecutor.doNativeQuery(sql,params, new ListConverter<WeatherValue>(
					new ColumnAsFeatureFactory<WeatherValue>(WeatherValue.class, columnToFeatureMap)));
		}catch(Exception e){
			log.debug(e);
			throw new EJBException(e);
		}
		
		return result;
	}
	
	private List<EventValue> getPSS2Event(String sqltemplate, Map<String,Object> params){
		List<EventValue> result=new ArrayList<EventValue>();
		try {
			String sql = SQLBuilder.buildNamedParameterSQL(sqltemplate, params);
			result = pss2SqlExecutor.doNativeQuery(sql,params, new ListConverter<EventValue>(
					new ColumnAsFeatureFactory<EventValue>(EventValue.class)));
		}catch(Exception e){
			log.debug(e);
			throw new EJBException(e);
		}
		
		return result;
	}

	// BIP has blocks
	private List<EventValue> getBIPActiveEvent(String programClass) {
		String sqltemplate=
				" SELECT CONCAT(CAST(e.startTime AS CHAR),IF(ed.actualEndTime IS NOT NULL, CAST(ed.actualEndTime AS CHAR),'NULL')) as eventKey, " +
				"p.utilityName AS utilityProgramName, p.programClass, p.longProgramName, p.name AS product, " +
				" e.startTime,e.issuedTime issueTime, IF(ed.actualEndTime, ed.actualEndTime, ed.estimatedEndTime) AS endTime, " +
				" ed.blockNames AS block " +
				" FROM event e, event_detail ed, program p " +
				" WHERE p.programClass = ${programClass} AND p.product=e.product " +
				" AND e.uuid=ed.event_uuid " +
				" AND e.startTime<=NOW() AND (ed.actualEndTime IS NULL OR ed.actualEndTime > NOW()) " +
				" ORDER BY product, startTime, endTime";
		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("programClass", programClass);

		//return this.getDRWEvent(sqltemplate, params);
		return getEventBlockEntry(sqltemplate, params);
	}
	
	// BIP has blocks
	private List<EventValue> getBIPScheduledEvent(String programClass) {
		String sqltemplate=
				" SELECT CONCAT(CAST(e.startTime AS CHAR),IF(ed.actualEndTime IS NOT NULL, CAST(ed.actualEndTime AS CHAR),'NULL')) as eventKey, " +
				" p.utilityName AS utilityProgramName, p.programClass, p.longProgramName, p.name AS product, " +
				" e.startTime,e.issuedTime issueTime, IF(ed.actualEndTime, ed.actualEndTime, ed.estimatedEndTime) AS endTime, " +
				" ed.blockNames AS block " +
				" FROM event e, event_detail ed, program p " +
				" WHERE p.programClass = ${programClass} AND p.product=e.product " +
				" AND e.uuid=ed.event_uuid " +
				" AND e.startTime > NOW() " +
				" ORDER BY product, startTime, endTime";
		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("programClass", programClass);

		//return getDRWEvent(sqltemplate, params);
		return getEventBlockEntry(sqltemplate, params);
	}

	@Override
	/**
	 * Retrieve KML for all active events of  API program
	 */
	public List<String> getKML4API() {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT COUNT(ed.uuid)																");
		sql.append(" FROM event ev, event_detail ed, program pg 										");
		sql.append(" WHERE pg.programClass='API' AND pg.product=ev.product 								");
		sql.append(" AND ev.uuid=ed.event_uuid   														");
		sql.append(" AND ev.startTime<=NOW() AND (ed.actualEndTime IS NULL OR ed.actualEndTime > NOW()) ");
		sql.append(" AND ed.allLocationType IS NOT NULL													");
		
		int count = 0;
		try {
			count = drwSqlExecutor.doNativeQuery(sql.toString(),
			new CellConverter<Integer>(Integer.class));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if(count>0){
			return getKML(" SELECT kml FROM location_kml lk where lk.number = 'ALL' ");
		}
		
		sql.setLength(0);// clear the current StringBuilder
        sql.append("  SELECT kml FROM location_kml lk INNER JOIN  											");
        sql.append("  (SELECT DISTINCT(lt.ID) id, lt.type locationType, lt.number number  					");
        sql.append("  FROM event ev, event_detail ed, program pg  , location lt		  						");
        sql.append("  WHERE pg.programClass='API' AND pg.product=ev.product  		  						");
        sql.append("  AND ev.uuid=ed.event_uuid   	  														");
        sql.append("  AND ev.startTime<=NOW() AND (ed.actualEndTime IS NULL OR ed.actualEndTime > NOW())    ");
        sql.append("  AND ed.locationID = lt.id ) loc  														");
        sql.append("  ON loc.number = lk.number AND loc.locationType = lk.locationType  					");
        
        return getKML(sql.toString());
	}
	@Override
	/**
	 * Retrieve KML for all active events of  API program
	 */
	public List<String> getKML4BIP() {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT COUNT(ed.uuid)																");
		sql.append(" FROM event ev, event_detail ed														");
		sql.append(" WHERE ev.programName = 'BIP' 													");
		sql.append(" AND ev.uuid=ed.event_uuid   														");
		sql.append(" AND ev.startTime<=NOW() AND (ed.actualEndTime IS NULL OR ed.actualEndTime > NOW()) ");
		sql.append(" AND ed.allLocationType IS NOT NULL													");
		
		int count = 0;
		try {
			count = drwSqlExecutor.doNativeQuery(sql.toString(),
			new CellConverter<Integer>(Integer.class));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if(count>0){
			return getKML(" SELECT kml FROM location_kml lk where lk.number = 'ALL' ");
		}
		
		sql.setLength(0);// clear the current StringBuilder
        sql.append("  SELECT kml FROM location_kml lk INNER JOIN  											");
        sql.append("  (SELECT DISTINCT(lt.ID) id, lt.type locationType, lt.number number  					");
        sql.append("  FROM event ev, event_detail ed, location lt		  									");
        sql.append("  where ev.programName = 'BIP' and ev.uuid=ed.event_uuid  		  					");
        sql.append("  AND ev.startTime<=NOW() AND (ed.actualEndTime IS NULL OR ed.actualEndTime > NOW())    ");
        sql.append("  AND ed.locationID = lt.id ) loc  														");
        sql.append("  ON loc.number = lk.number AND loc.locationType = lk.locationType  					");
        
        return getKML(sql.toString());
	}
	@Override
	/**
	 * Retrieve KML for all active events of  API program
	 */
	public List<String> getKML4CBP() {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT COUNT(ed.uuid)																");
		sql.append(" FROM event ev, event_detail ed														");
		sql.append(" WHERE ev.programName = 'CBP' 													");
		sql.append(" AND ev.uuid=ed.event_uuid   														");
		sql.append(" AND ev.startTime<=NOW() AND (ed.actualEndTime IS NULL OR ed.actualEndTime > NOW()) ");
		sql.append(" AND ed.allLocationType IS NOT NULL													");
		
		int count = 0;
		try {
			count = drwSqlExecutor.doNativeQuery(sql.toString(),
			new CellConverter<Integer>(Integer.class));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if(count>0){
			return getKML(" SELECT kml FROM location_kml lk where lk.number = 'ALL' ");
		}
		
		sql.setLength(0);// clear the current StringBuilder
        sql.append("  SELECT kml FROM location_kml lk INNER JOIN  											");
        sql.append("  (SELECT DISTINCT(lt.ID) id, lt.type locationType, lt.number number  					");
        sql.append("  FROM event ev, event_detail ed, location lt		  									");
        sql.append("  where ev.programName = 'CBP' and ev.uuid=ed.event_uuid  		  					");
        sql.append("  AND ev.startTime<=NOW() AND (ed.actualEndTime IS NULL OR ed.actualEndTime > NOW())    ");
        sql.append("  AND ed.locationID = lt.id ) loc  														");
        sql.append("  ON loc.number = lk.number AND loc.locationType = lk.locationType  					");
        
        return getKML(sql.toString());
	}
	@Override
	/**
	 * Retrieve KML for all active events of  SDP Commercial program
	 */
	public List<String> getKML4SDPC() {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT COUNT(ed.uuid) 																	");
		sql.append(" FROM event ev, event_detail ed, program pg 											");
		sql.append(" WHERE pg.programClass='SDP' AND pg.product=ev.product									");
		sql.append(" AND ev.product IN ('APS-E','APS')														");
		sql.append(" AND ev.uuid=ed.event_uuid  															");
		sql.append(" AND ev.startTime<=NOW() AND (ed.actualEndTime IS NULL OR ed.actualEndTime > NOW())		");
		sql.append(" AND ed.allLocationType IS NOT NULL														");
		
		int count = 0;
		try {
			count = drwSqlExecutor.doNativeQuery(sql.toString(),
			new CellConverter<Integer>(Integer.class));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if(count>0){
			return getKML(" SELECT kml FROM location_kml lk where lk.number = 'ALL' ");
		}
		
		sql.setLength(0);// clear the current StringBuilder
        sql.append("  SELECT kml FROM location_kml lk INNER JOIN  											");
        sql.append("  (SELECT DISTINCT(lt.ID) id, lt.type locationType, lt.number number  					");
        sql.append("  FROM event ev, event_detail ed, program pg  , location lt		  						");
        sql.append("  WHERE pg.programClass='SDP' AND pg.product=ev.product    								");
        sql.append("  AND ev.product IN ('APS-E','APS')  													");
        sql.append("  AND ev.uuid=ed.event_uuid     														");
        sql.append("  AND ev.startTime<=NOW() AND (ed.actualEndTime IS NULL OR ed.actualEndTime > NOW()) 	");
        sql.append("  AND ed.locationID = lt.id ) loc   													");
        sql.append("  ON loc.number = lk.number AND loc.locationType = lk.locationType  					");
	
        return getKML(sql.toString());
	}

	@Override
	/**
	 * Retrieve KML for all active events of  SDP Residential program
	 */
	public List<String> getKML4SDPR() {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT COUNT(ed.uuid)																	");
		sql.append(" FROM event ev, event_detail ed, program pg												");
		sql.append(" WHERE pg.programClass='SDP' AND pg.product=ev.product									");
		sql.append(" AND ev.product ='SDP'																	");
		sql.append(" AND ev.uuid=ed.event_uuid 																");
		sql.append(" AND ev.startTime<=NOW() AND (ed.actualEndTime IS NULL OR ed.actualEndTime > NOW())		");
		sql.append(" AND ed.allLocationType IS NOT NULL														");
		
		int count = 0;
		try {
			count = drwSqlExecutor.doNativeQuery(sql.toString(),
			new CellConverter<Integer>(Integer.class));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if(count>0){
			return getKML(" SELECT kml FROM location_kml lk where lk.number = 'ALL' ");
		}
     
		sql.setLength(0);// clear the current StringBuilder
        sql.append("  SELECT kml FROM location_kml lk INNER JOIN  											");
        sql.append("  (SELECT DISTINCT(lt.ID) id, lt.type locationType, lt.number number  					");
        sql.append("  FROM event ev, event_detail ed, program pg, location lt		  						");
        sql.append("  WHERE pg.programClass='SDP' AND pg.product=ev.product    								");
        sql.append("  AND ev.product ='SDP'  																");
        sql.append("  AND ev.uuid=ed.event_uuid     														");
        sql.append("  AND ev.startTime<=NOW() AND (ed.actualEndTime IS NULL OR ed.actualEndTime > NOW()) 	");
        sql.append("  AND ed.locationID = lt.id ) loc   													");
        sql.append("  ON loc.number = lk.number AND loc.locationType = lk.locationType  					");
        
        return getKML(sql.toString());
	}
	
	private List<String> getKML(String sqltemplate){
		List<String> result=new ArrayList<String>();
		try {
			String sql = SQLBuilder.buildNamedParameterSQL(sqltemplate, new HashMap<String,Object>());
			result = drwSqlExecutor.doNativeQuery(sql, new ListConverter<String>(
					new ColumnAsObjectFactory<String>(String.class,"kml")));
		}catch(Exception e){
			log.debug(e);
			throw new EJBException(e);
		}
		
		return result;
	}
	
	
	@Override
	public DRWebsiteProperty getDRWebsitePropertyByPropertyName(String propertyName) {
		try {
			DRWebsiteProperty result = websitePropertyEAO.findByPropertyName(propertyName);
			return result;
		}catch(Exception e){
			log.debug(e);
			throw new EJBException(e);
		}
	}
	@Override
	public List<EventValue> getKioskEvent(boolean isActive){
		List<EventValue> result=new ArrayList<EventValue>();
		List<EventValue> drwResult=new ArrayList<EventValue>();
		List<EventValue> pss2Result=new ArrayList<EventValue>();
		List<String> pss2Programs=new ArrayList<String>();
		pss2Programs.add("CPP");
		pss2Programs.add("DBP");
		pss2Programs.add("DRC");
		pss2Programs.add("SPD");
		try {
			if(isActive){
				//retrieve active events
				
				drwResult.addAll(getDRWKioskActiveEvent());
//				drwResult.addAll(getDRWKioskBIP2013ActiveEvent());
				pss2Result.addAll(getPSS2ActiveEvent(pss2Programs));
				
				
			}else{
				//retrieve scheduled events
				drwResult.addAll(getDRWKioskScheduledEvent());
//				drwResult.addAll(getDRWKioskBIP2013ScheduledEvent());
				pss2Result.addAll(getPSS2ScheduledEvent(pss2Programs));
			}
		}catch(Exception e){
			log.debug(e);
			throw new EJBException(e);
		}
		
		result.addAll(drwResult);
		result.addAll(pss2Result);
		
		return result;
	}
	

	private List<EventValue> getDRWKioskActiveEvent() {
		StringBuffer sql = new StringBuffer();
		sql.append("  SELECT 																				");
		sql.append("  IF(ea.allLocationType IS NOT NULL, ea.allLocationType ,l.type) AS dispatchType,								");
		sql.append("  IF(ea.allLocationType IS NOT NULL, 'ALL', l.name) AS dispatchLocation,					");
		sql.append("  CONCAT(product, CAST(startTime AS CHAR),IF(endTime IS NOT NULL, CAST(endTime AS CHAR) ,'TBD')) AS eventKey,				");
		sql.append("  ea.*																					");
		sql.append("  FROM																					");
		sql.append("  (																						");
		sql.append("  SELECT DISTINCT																		");
		sql.append("  p.programClass, 																		");
		sql.append("  p.longProgramName, 																	");
		sql.append("  p.name AS product,																	");
//		sql.append("  e.uuid AS event_uuid, 																");
//		sql.append("  ed.uuid AS event_detail_uuid,															");
		sql.append("  e.startTime,																			");
		sql.append("  e.issuedTime issueTime,																");
		sql.append("  IF(ed.actualEndTime, ed.actualEndTime, ed.estimatedEndTime) AS endTime,				");
		sql.append("  ed.locationID,																		");
		sql.append("  ed.allLocationType,																	");
		sql.append("  ed.blockNames																			");
		sql.append("  FROM event e,event_detail ed,program p												");
		sql.append("  WHERE e.startTime<=NOW() AND (ed.actualEndTime IS NULL OR ed.actualEndTime > NOW()) 	");
		sql.append("  AND p.product=e.product AND e.uuid=ed.event_uuid 										");
		sql.append("  ) ea LEFT JOIN location l ON ea.locationID = l.id										");
		sql.append("  ORDER BY startTime, endTime,programClass, product ;									");
		
		Map<String,Object> params = new HashMap<String,Object>();

		return this.getEventDispatch(sql.toString(), params);
	}
	private List<EventValue> getDRWKioskBIP2013ActiveEvent() {
		StringBuffer sql = new StringBuffer();
		sql.append("  SELECT 																				");
		sql.append("  IF(l.type IS NOT NULL, l.type ,'Block') AS dispatchType,								");
		sql.append("  IF(l.name IS NOT NULL, l.name, ea.blockNames) AS dispatchLocation,					");
		sql.append("  ea.*																					");
		sql.append("  FROM																					");
		sql.append("  (																						");
		sql.append("  SELECT DISTINCT																		");
		sql.append("  'BIP2013' as programClass, 																		");
		sql.append("  'Time-of-Use Base Interruptible Program' as longProgramName, 																	");
		sql.append("  'BIP2013' as product,																	");
		sql.append("  e.uuid AS event_uuid, 																");
		sql.append("  ed.uuid AS event_detail_uuid,															");
		sql.append("  e.startTime,																			");
		sql.append("  e.issuedTime issueTime,																			");
		sql.append("  IF(ed.actualEndTime, ed.actualEndTime, ed.estimatedEndTime) AS endTime,				");
		sql.append("  ed.locationID,																		");
		sql.append("  ed.allLocationType,																	");
		sql.append("  ed.blockNames																			");
		sql.append("  FROM event e,event_detail ed												");
		sql.append("  WHERE e.startTime<=NOW() AND (ed.actualEndTime IS NULL OR ed.actualEndTime > NOW()) 	");
		sql.append("  AND e.programName = 'BIP2013' AND e.uuid=ed.event_uuid 										");
		sql.append("  ) ea LEFT JOIN location l ON ea.locationID = l.id										");
		sql.append("  ORDER BY startTime, endTime,programClass, product ;									");
		
		Map<String,Object> params = new HashMap<String,Object>();

		return this.getDRWEvent(sql.toString(), params);
	}
	
	private List<EventValue> getDRWKioskScheduledEvent() {
		StringBuffer sql = new StringBuffer();
		sql.append("  SELECT 																				");
		sql.append("  IF(ea.allLocationType IS NOT NULL, ea.allLocationType ,l.type) AS dispatchType,								");
		sql.append("  IF(ea.allLocationType IS NOT NULL, 'ALL', l.name) AS dispatchLocation,					");
//		sql.append("  IF(l.type IS NOT NULL, l.type ,'Block') AS dispatchType,								");
//		sql.append("  IF(l.name IS NOT NULL, l.name,  ea.blockNames) AS dispatchLocation,					");
		sql.append("  CONCAT(product, CAST(startTime AS CHAR),IF(endTime IS NOT NULL, CAST(endTime AS CHAR) ,'TBD')) AS eventKey,				");
		sql.append("  ea.*																					");
		sql.append("  FROM																					");
		sql.append("  (																						");
		sql.append("  SELECT DISTINCT																		");
		sql.append("  p.programClass, 																		");
		sql.append("  p.longProgramName, 																	");
		sql.append("  p.name AS product,																	");
//		sql.append("  e.uuid AS event_uuid, 																");
//		sql.append("  ed.uuid AS event_detail_uuid,															");
		sql.append("  e.startTime,																			");
		sql.append("  e.issuedTime issueTime,																");
		sql.append("  IF(ed.actualEndTime, ed.actualEndTime, ed.estimatedEndTime) AS endTime,				");
		sql.append("  ed.locationID,																		");
		sql.append("  ed.allLocationType,																	");
		sql.append("  ed.blockNames																			");
		sql.append("  FROM event e,event_detail ed,program p												");
		//sql.append("  WHERE e.startTime > NOW() and (e.issuedTime< NOW() or e.issuedTime is null) 		 	");
		sql.append("  WHERE e.startTime > NOW()  		 	");
		sql.append("  AND (ed.actualEndTime IS NULL OR ed.actualEndTime > NOW()) 							");
		sql.append("  AND p.product=e.product AND e.uuid=ed.event_uuid 										");
		sql.append("  ) ea LEFT JOIN location l ON ea.locationID = l.id										");
		sql.append("  ORDER BY startTime, endTime,programClass, product ;									");
		
		Map<String,Object> params = new HashMap<String,Object>();

		return this.getEventDispatch(sql.toString(), params);
	}
	private List<EventValue> getDRWKioskBIP2013ScheduledEvent() {
		StringBuffer sql = new StringBuffer();
		sql.append("  SELECT 																				");
		sql.append("  IF(l.type IS NOT NULL, l.type ,'Block') AS dispatchType,								");
		sql.append("  IF(l.name IS NOT NULL, l.name,  ea.blockNames) AS dispatchLocation,					");
		sql.append("  ea.*																					");
		sql.append("  FROM																					");
		sql.append("  (																						");
		sql.append("  SELECT DISTINCT																		");
		sql.append("  'BIP2013' as programClass, 																		");
		sql.append("  'Time-of-Use Base Interruptible Program' as longProgramName, 																	");
		sql.append("  'BIP2013' as product,																	");
		sql.append("  e.uuid AS event_uuid, 																");
		sql.append("  ed.uuid AS event_detail_uuid,															");
		sql.append("  e.startTime,																			");
		sql.append("  e.issuedTime issueTime,																");
		sql.append("  IF(ed.actualEndTime, ed.actualEndTime, ed.estimatedEndTime) AS endTime,				");
		sql.append("  ed.locationID,																		");
		sql.append("  ed.allLocationType,																	");
		sql.append("  ed.blockNames																			");
		sql.append("  FROM event e,event_detail ed												");
		sql.append("  WHERE e.startTime > NOW() AND (ed.actualEndTime IS NULL OR ed.actualEndTime > NOW()) 	");
		sql.append("  AND e.programName = 'BIP2013' AND e.uuid=ed.event_uuid 										");
		sql.append("  ) ea LEFT JOIN location l ON ea.locationID = l.id										");
		sql.append("  ORDER BY startTime, endTime,programClass, product ;									");
		
		Map<String,Object> params = new HashMap<String,Object>();

		return this.getDRWEvent(sql.toString(), params);
	}
	private String getListViewSDPSQLTemplate(){
		String sqlTempalte = null;
		try {
			 sqlTempalte =SQLLoader.loadSQLFromFile(CFEventManagerBean.class, "SDPListView.sql");
		} catch (IOException e) {
			
		} 
		if(sqlTempalte!=null){
			
		}

		return sqlTempalte;
		 
	}
	private String getListViewSDPSQLTemplateScheduled(){
		String sqlTempalte = null;
		try {
			 sqlTempalte =SQLLoader.loadSQLFromFile(CFEventManagerBean.class, "ScheduledSDPListView.sql");
		} catch (IOException e) {
			
		} 
		if(sqlTempalte!=null){
			
		}

		return sqlTempalte;
		 
	}	
	@Override
	public List<String> getAllBlock() {
		List<String> result=new ArrayList<String>();
		String sqltemplate= " select distinct block from location where block is not null order by block " ;
		Map<String,Object> params = new HashMap<String,Object>();

		try {
			String sql = SQLBuilder.buildNamedParameterSQL(sqltemplate, params);
			result = drwSqlExecutor.doNativeQuery(sql, new ListConverter<String>(new ColumnAsObjectFactory<String>(String.class,"block")));
		}catch(Exception e){
			log.debug(e);
			throw new EJBException(e);
		}

		return result;
	}
	
	
	@Override
	public List<EventValue> getListViewScheduled(List<String> products, String county, String city, List<String> zipCodes) {
		List<EventValue> result=new ArrayList<EventValue>();
		
		List<String> searchSdpProducts=new ArrayList<String>();
		List<String> searchDrwProducts=new ArrayList<String>();
		for (String product:products) {
			if (Arrays.asList(sdpProducts).contains(product))
				searchSdpProducts.add(product);
			else
				searchDrwProducts.add(product);
		}
		
		// only for SDP program
		if (searchSdpProducts.size()>0) {
			String sqltemplate = getListViewSDPSQLTemplateScheduled();
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("products", products);
			params.put("county", county);
			params.put("city", city);
			params.put("zipCodes", zipCodes);
			
			result.addAll(getLocationEntry(sqltemplate, params));
		}
		//for BIP2013 programs
		boolean bipFlag = false;
		for (String product:products) {
			if(product.contains("BIP2013")){
				bipFlag = true;
			}
		}
		if(bipFlag){
			String sqltemplate=" SELECT 	CONCAT(tmp.eventID,IF(l.type IS NOT NULL, l.type ,'SLAP')) AS eventKey,	tmp.* FROM( " +
			" SELECT ea.*, ec.block,ec.zipCode, ec.cityName, ec.countyName FROM ( select e.uuid as eventID,ed.uuid as eventDtailID,  " +
			" 'Time-of-Use Base Interruptible Program' as utilityProgramName,'Time-of-Use Base Interruptible Program' as longProgramName,'BIP2013' as programClass, 'BIP2013' as product, e.startTime,e.issuedTime issueTime, IF(ed.actualEndTime, ed.actualEndTime, ed.estimatedEndTime) as endTime,  " +
			" ed.locationID,	ed.allLocationType from event e, event_detail ed  " +
			" where programName = 'BIP2013' and e.uuid=ed.event_uuid   " +
			" and e.startTime > NOW() ) ea,  " +
			" event_detail eed, zipcode_entry ec WHERE eed.uuid=ea.eventDtailID AND eed.uuid=ec.eventDetail_uuid  " +
			" [AND ec.zipCode IN ${zipCodes}] [AND ec.cityName=${city}] [AND ec.countyName=${county}]  " +
			" ) tmp LEFT JOIN location l ON tmp.locationID = l.id  " +
			" ORDER BY programClass, product, startTime, endTime, zipCode, cityName, countyName ";
			
			
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("county", county);
			params.put("city", city);
			params.put("zipCodes", zipCodes);
			
			result.addAll(getLocationAndBlockEntry(sqltemplate, params));
		}else{
			// only for API program
			if (searchDrwProducts.size()>0) {

				String sqltemplate=" SELECT 	CONCAT(tmp.eventID,IF(l.type IS NOT NULL, l.type ,'SLAP')) AS eventKey,	tmp.* FROM( " +
				" SELECT ea.*, ec.block,ec.zipCode, ec.cityName, ec.countyName FROM ( SELECT e.uuid AS eventID,ed.uuid AS eventDtailID, p.utilityName AS utilityProgramName,  " +
				" p.programClass, p.longProgramName, p.name AS product, e.startTime,e.issuedTime issueTime, IF(ed.actualEndTime, ed.actualEndTime, ed.estimatedEndTime) AS endTime,  " +
				" ed.locationID,	ed.allLocationType FROM event e, event_detail ed, program p  " +
				" WHERE p.programClass IN ('API') AND p.product=e.product AND e.uuid=ed.event_uuid   " +
				" AND e.startTime > NOW() ) ea,  " +
				" event_detail eed, zipcode_entry ec WHERE eed.uuid=ea.eventDtailID AND eed.uuid=ec.eventDetail_uuid  " +
				" [AND ec.zipCode IN ${zipCodes}] [AND ec.cityName=${city}] [AND ec.countyName=${county}]  " +
				" ) tmp LEFT JOIN location l ON tmp.locationID = l.id  " +
				" ORDER BY programClass, product, startTime, endTime, zipCode, cityName, countyName ";
				
				
				Map<String,Object> params = new HashMap<String,Object>();
				params.put("products", searchDrwProducts);
				params.put("county", county);
				params.put("city", city);
				params.put("zipCodes", zipCodes);
				
//				result.addAll(getLocationEntry(sqltemplate, params));
				result.addAll(getLocationAndBlockEntry(sqltemplate, params));
			}
		}

		
		return result;
	}
	
	@Override
	public List<Location> getLocationEntity() {
		String sqltemplate= " select distinct * from location l where l.type in('SLAP','ABank') order by block;";
	
		Map<String,Object> params = new HashMap<String,Object>();

		return getDRWLocation(sqltemplate, params);
	}
	
	private List<Location> getDRWLocation(String sqltemplate, Map<String,Object> params){
		List<Location> result=new ArrayList<Location>();
		try {
			String sql = SQLBuilder.buildNamedParameterSQL(sqltemplate, params);
			result = drwSqlExecutor.doNativeQuery(sql,params, new ListConverter<Location>(
					new ColumnAsFeatureFactory<Location>(Location.class)));
		}catch(Exception e){
			log.debug(e);
			throw new EJBException(e);
		}
		
		return result;
	}
	
	//only for SDP
	//only one row: earliest start time & latest end time
	private List<EventValue> getSDPEvent2013(String sdpProgramClass, boolean commercial,boolean isActive) {
		String sqltemplate= DrwSqlUtil.getSQL_DRW_COMMON_EVENT_TEMPLATE(isActive);
		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("programClass", sdpProgramClass);
		if (commercial){
			params.put("product", Arrays.asList(sdpCommericalRate));
		}
		else{
			params.put("product", Arrays.asList(sdpResidentialRate));
		}
		return getLocationAndBlockEntry(sqltemplate, params);
	}
//--------------------------------------------------------------------------------------------------------------	
	public List<EventValue> getDrEvent2013(List<String> programClass, boolean commercial,boolean isActive) {
		List<EventValue> result=new ArrayList<EventValue>();
		List<String> programs=new ArrayList<String>();
		programs.addAll(programClass);
		
		if (programs.contains(PROGRAM_CLASS_SDP)) {
			result.addAll(getDrwEvent2013(PROGRAM_CLASS_SDP,commercial,isActive));
			programs.remove(PROGRAM_CLASS_SDP);
		}
		
		if (programs.contains(PROGRAM_CLASS_BIP)) {
			result.addAll(getDrwEvent2013(PROGRAM_CLASS_BIP,commercial,isActive));
			programs.remove(PROGRAM_CLASS_BIP);
		}
		
		if (programs.contains(PROGRAM_CLASS_API)) {
			result.addAll(getDrwEvent2013(PROGRAM_CLASS_API,commercial,isActive));
			programs.remove(PROGRAM_CLASS_API);
		}
		
		if (programs.size()>0){
			result.addAll(getPSS2ActiveEvent(programs));
		}
			
		
		return result;
	}
	

	private List<EventValue> getDrwEvent2013(String programClass, boolean commercial,boolean isActive) {
		String sqltemplate= DrwSqlUtil.getSQL_DRW_COMMON_EVENT_TEMPLATE(isActive);
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("programClass", programClass);
		if(programClass.contains(PROGRAM_CLASS_SDP)){
			if (commercial){
				params.put("product", Arrays.asList(sdpCommericalRate));
			}
			else{
				params.put("product", Arrays.asList(sdpResidentialRate));
			}
		}
		if(programClass.contains(PROGRAM_CLASS_BIP)){
			List<String> list = new ArrayList<String>();
			list.add("BIP");
			
			params.put("product",list);
		}
		if(programClass.contains(PROGRAM_CLASS_API)){
			List<String> list = new ArrayList<String>();
			list.add("API");
			params.put("product",list);
		}
		return getLocationAndBlockEntry(sqltemplate, params);
	}
	
	
	
	@Override
	public List<String> getKML4Block(List<String> blocks) {
		String sqltemplate= DrwSqlUtil.getSQL_KML_BLOCK_TEMPLATE();
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("blocks", blocks);
        return getKML(sqltemplate);
	}
	
	
	
	@SuppressWarnings("unchecked")
	@Override
	/**
	 * Retrieve KML for all active events of  SDP Residential program
	 */
	public List<String> getKML4EventDetails(String eventDetailIds) {
		if(eventDetailIds==null || eventDetailIds.trim().length()==0) return new ArrayList<String>();
		
		List<String> uuids = new ArrayList<String>();
		String[] eds = eventDetailIds.split(",");
		for(String value:eds){
			uuids.add(value);
		}
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT COUNT(*) FROM event_detail WHERE UUID in ${p_eventDetailIds}  AND locationID IS NULL AND allLocationType='SLAP' ");
		Map params = new HashMap<String,Object>();
		params.put("p_eventDetailIds", uuids);
		
		int count = 0;
		try {
			count = drwSqlExecutor.doNativeQuery(sb.toString(),params,
			new CellConverter<Integer>(Integer.class));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if(count>0){
			return getKML(" SELECT kml FROM location_kml lk where lk.number = 'ALL' ");
		}
		
		sb.setLength(0);// clear the current StringBuilder
		
		sb.append("  SELECT kml FROM location lc,event_detail ed ,location_kml lk  						");
		sb.append("  WHERE lc.ID=ed.locationID															");
		sb.append("  AND lc.number=lk.number AND lk.locationType=lc.type	  							");
		sb.append("  AND ed.uuid in  ${p_eventDetailIds}  												");
        
        List<String> result=new ArrayList<String>();
		try {
			String sql = SQLBuilder.buildNamedParameterSQL(sb.toString(),params );
			result = drwSqlExecutor.doNativeQuery(sql, params,new ListConverter<String>(
					new ColumnAsObjectFactory<String>(String.class,"kml")));
		}catch(Exception e){
			log.debug(e);
			throw new EJBException(e);
		}
		
		return result;
	}
	public List<String> getKML4EventDetails(String eventDetailIds,boolean consolidationSLAP) {
		if(!consolidationSLAP){
			return getKML4EventDetails(eventDetailIds);
		}else{
			if(eventDetailIds==null || eventDetailIds.trim().length()==0) return new ArrayList<String>();
			
			List<String> uuids = new ArrayList<String>();
			String[] eds = eventDetailIds.split(",");
			for(String value:eds){
				uuids.add(value);
			}
			StringBuilder sb = new StringBuilder();
			sb.append(" SELECT distinct(locationID) from event_detail where UUID in ${p_eventDetailIds} order by locationID ");
			Map params = new HashMap<String,Object>();
			params.put("p_eventDetailIds", uuids);
			List list = new ArrayList();
			try {
				list = drwSqlExecutor.doNativeQuery(sb.toString(),params,
				new SimpleListConverter<Integer>(Integer.class));
			} catch (SQLException e) {
				e.printStackTrace();
			}
			if(list.size()==6){
				List<Integer> list1 = new ArrayList<Integer>();
				list1.add(1);
				list1.add(2);
				list1.add(3);
				list1.add(4);
				list1.add(5);
				list1.add(6);
				if(list1.equals(list)){
					return getKML(" SELECT kml FROM location_kml lk where lk.number = 'ALL' ");
				}
			}
			return getKML4EventDetails(eventDetailIds);
		}
		
	}	
	public static void main(String args[]){
		Date compareStart =DateUtil.parse("2012-12-31 23:59:59", "yyyy-MM-dd HH:mm:ss");
		
		System.out.println(compareStart);
		
		List<Integer> list1 = new ArrayList<Integer>();
		list1.add(1);
		list1.add(2);
		list1.add(3);
		list1.add(4);
		list1.add(5);
		list1.add(6);
		List<Integer> list2 = new ArrayList<Integer>();
		list2.add(1);
		list2.add(2);
		list2.add(3);
		list2.add(4);
		list2.add(5);
		list2.add(6);
		
		System.out.println(list1.equals(list2));
	}
	
	@Override
	public List<EventValue> getPSS2ScheduledEvent4Mobile(List<String> programClass) {
		String sqltemplate= " SELECT e.uuid as uuid, p.longProgramName, p.programClass, p.utilityProgramName, " +
							" p.name AS product, e.startTime, e.endTime, e.issuedTime issueTime " +
							" FROM event e, program p " +
							" WHERE p.programClass IN ${programClass}  AND e.programName=p.name AND e.status !=\"ACTIVE\" " +
							" ORDER BY startTime, endTime, programClass, product ";
		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("programClass", programClass);
		
		return getPSS2Event(sqltemplate, params);
	}

	@Override
	public Date getWatherForeCastModifidDate() 
	{
		String sqltemplate=" SELECT modifiedtime FROM forecast_weather ";
		Map<String,Object> params = new HashMap<String,Object>();
		Date modifiedDate = null;
		try {
			String sql = SQLBuilder.buildNamedParameterSQL(sqltemplate, params);
			List<Date> result = pss2SqlExecutor.doNativeQuery(sql, params, new SimpleListConverter<Date>(Date.class));
//			System.out.println("size = "+dates.size());
			if(result.size() > 0)
			{
				modifiedDate = result.get(0);
			}
		}catch(Exception e){
			log.debug(e);
			throw new EJBException(e);
		}
		
		return modifiedDate;
	}
	
	@Override
	 public List<AlertValue> getAlertHistory(String deviceKey){
		 log.info(" In CFEventManagerBean.getAlertHistory(deviceKey) ");
		 List<AlertValue> result= new ArrayList<AlertValue>();
		 
		 String sqltemplate = "SELECT uuid, deviceKey, message, alertTime, programName, product, startTime, endTime from Alert_History where deviceKey = ${deviceKey} and DATE(alertTime) > curdate() - interval 1 year order by alertTime desc";
		  Map<String,Object> params = new HashMap<String, Object>();
		  params.put("deviceKey", deviceKey);
		  
		  try{
			  String sql = SQLBuilder.buildNamedParameterSQL(sqltemplate, params);
			  result = mobimsgSQLExecutor.doNativeQuery(sql,params, new ListConverter<AlertValue>(
						new ColumnAsFeatureFactory<AlertValue>(AlertValue.class)));
			  
			 }
		  catch(Exception e){
			  log.error("Exception in CFEventManagerBean.getAlertHistory "+e.getMessage());
			  throw new EJBException(e);
		  }
		 
		 return result;
	 }

}
