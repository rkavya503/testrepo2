
package com.akuacom.pss2.drw;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;
import org.jboss.ejb3.annotation.LocalBinding;
import org.jboss.ejb3.annotation.RemoteBinding;

import scala.actors.threadpool.Arrays;

import com.akuacom.jdbc.CellConverter;
import com.akuacom.jdbc.ColumnAsFeatureFactory;
import com.akuacom.jdbc.ListConverter;
import com.akuacom.jdbc.MasterDetailFactory;
import com.akuacom.jdbc.SQLBuilder;
import com.akuacom.jdbc.SQLLoader;
import com.akuacom.jdbc.SimpleListConverter;
import com.akuacom.pss2.drw.constants.DrwConstants;
import com.akuacom.pss2.drw.core.Program;
import com.akuacom.pss2.drw.util.DrwSqlUtil;
import com.akuacom.pss2.drw.value.BlockValue;
import com.akuacom.pss2.drw.value.EventLoaction;
import com.akuacom.pss2.drw.value.EventValue;
import com.akuacom.pss2.drw.value.EventVo;
import com.akuacom.pss2.drw.value.LocationValue;
import com.akuacom.pss2.drw.value.SlapBlockRange;
import com.akuacom.utils.DateUtil;
import com.akuacom.pss2.drw.value.EventLegend;
@Stateless
@LocalBinding(jndiBinding="dr-pro/DREvent2013Manager/local")
@RemoteBinding(jndiBinding="dr-pro/DREvent2013Manager/remote")
public class DREvent2013ManagerBean implements DREvent2013Manager.L, DREvent2013Manager.R {
	
	private static final String[] drwProgramClasses={"SDP", "API", "BIP","CBP"};
	private static final Logger log = Logger.getLogger(DREvent2013ManagerBean.class);
	@EJB
	CFEventManager.L cfEventManager;
	@EJB
    DrwSQLExecutor.L drwSqlExecutor;
	@EJB
	Pss2SQLExecutor.L pss2SQLExecutor;
	@Override
	public List<EventValue> getEvents(List<String> programClass,boolean isCommercial, boolean isActive){
		List<EventValue> result = new ArrayList<EventValue>();
		List<String> programs=new ArrayList<String>();
		programs.addAll(programClass);
		
		if (programs.contains(DrwConstants.PROGRAM_CLASS_SDP)) {
			result.addAll(getDrwEvent2013(DrwConstants.PROGRAM_CLASS_SDP,isCommercial,isActive));
			programs.remove(DrwConstants.PROGRAM_CLASS_SDP);
		}
		
		if (programs.contains(DrwConstants.PROGRAM_CLASS_BIP)) {
			result.addAll(getDrwEvent2013(DrwConstants.PROGRAM_CLASS_BIP,isCommercial,isActive));
			programs.remove(DrwConstants.PROGRAM_CLASS_BIP);
		}
		
		if (programs.contains(DrwConstants.PROGRAM_CLASS_API)) {
			result.addAll(getDrwEvent2013(DrwConstants.PROGRAM_CLASS_API,isCommercial,isActive));
			programs.remove(DrwConstants.PROGRAM_CLASS_API);
		}
		
		if (programs.size()>0){
			if(isActive){
				result.addAll(cfEventManager.getPSS2ActiveEvent(programs));	
			}else{
				result.addAll(cfEventManager.getPSS2ScheduledEvent(programs));
			}
		}
		return result;
	}
	@Override
	public List<EventValue> getHistoryEvents(String programClass, String product,Date start, Date end, List<String> zipCodes){
		List<EventValue> result = new ArrayList<EventValue>();
		
		Date startTime = null;
		if (start!=null) {
			startTime=DateUtil.getStartOfDay(start);
		}
		Date endTime=null;
		if (end!=null) {
			endTime=DateUtil.getEndOfDay(end);
		}
		
		if(endTime!=null&&!endTime.before(new Date())){
			endTime = new Date();
		}
		
		if (isDRWProgram(programClass)) {
			result=getDRWHistoryEvent(programClass, product, start, endTime, zipCodes);
		} else {
			return cfEventManager.getHistoryEvent(programClass, product, startTime, endTime, zipCodes);
		}

		return result;
	}
	
	
	
	
	
	
	private List<EventValue> getDrwEvent2013(String programClass, boolean commercial,boolean isActive) {
		String sqltemplate= DrwSqlUtil.getSQL_DRW_COMMON_EVENT_TEMPLATE(isActive);
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("programClass", programClass);
		if(programClass.contains(DrwConstants.PROGRAM_CLASS_SDP)){
			if (commercial){
				List<String> list = new ArrayList<String>();
				list.add(DrwConstants.PRODUCT_SDP_APS);
				list.add(DrwConstants.PRODUCT_SDP_APS_E);
				params.put("product", list);
			}
			else{
				List<String> list = new ArrayList<String>();
				list.add(DrwConstants.PRODUCT_SDP_SDP);
				params.put("product", list);
			}
		}
		if(programClass.contains(DrwConstants.PROGRAM_CLASS_BIP)){
			List<String> list = new ArrayList<String>();
			list.add(DrwConstants.PRODUCT_BIP_OLD);
			list.add(DrwConstants.PRODUCT_BIP_2013);
			params.put("product",list);
		}
		if(programClass.contains(DrwConstants.PROGRAM_CLASS_API)){
			List<String> list = new ArrayList<String>();
			list.add(DrwConstants.PRODUCT_API);
			params.put("product",list);
		}
		
		if(programClass.contains(DrwConstants.PROGRAM_CLASS_CBP)){
			sqltemplate= DrwSqlUtil.getSQL_DRW_CBP_EVENT_TEMPLATE(isActive);
			List<String> list = new ArrayList<String>();
			list.add(DrwConstants.PRODUCT_CBP_DA_14);
			list.add(DrwConstants.PRODUCT_CBP_DA_26);
			list.add(DrwConstants.PRODUCT_CBP_DA_48);
			list.add(DrwConstants.PRODUCT_CBP_DO_14);
			list.add(DrwConstants.PRODUCT_CBP_DO_26);
			list.add(DrwConstants.PRODUCT_CBP_DO_48);
			params.put("product",list);
		}
		List<EventValue> result = cfEventManager.getLocationAndBlockEntry(sqltemplate, params);
		DrwSqlUtil.sortEventList(result);
		return result;
		
	}
	private List<EventValue> getDRWHistoryEvent(String programClass, String product,Date start, Date end, List<String> zipCodes){
		String sqltemplate= DrwSqlUtil.getSQL_DRW_COMMON_EVENT_TEMPLATE(true);
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("programClass", programClass);
		return cfEventManager.getLocationAndBlockEntry(sqltemplate, params);
	}
	private boolean isDRWProgram(String programClass) {
		return Arrays.asList(drwProgramClasses).contains(programClass);
	}
	
	@Override
	public List<BlockValue> getKMLS(List<String> abanksNumber){
		List<BlockValue> result = new ArrayList<BlockValue>();
		List<BlockValue> tmp = new ArrayList<BlockValue>();
		Map<String,List<BlockValue>> map = new HashMap<String,List<BlockValue>>();
		String sqltemplate= DrwSqlUtil.getSQL_KML_BLOCK_TEMPLATE();
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("blocks", abanksNumber);
		try {
			String sql = SQLBuilder.buildNamedParameterSQL(sqltemplate, params);
			tmp = drwSqlExecutor.doNativeQuery(sql,params, new ListConverter<BlockValue>(
					new ColumnAsFeatureFactory<BlockValue>(BlockValue.class)));
			for(BlockValue entity: tmp){
				String abankNumber = entity.getAbankNumber();
				if(map.containsKey(abankNumber)){
					map.get(abankNumber).add(entity);
				}else{
					List<BlockValue> list = new ArrayList<BlockValue>();
					list.add(entity);
					map.put(abankNumber, list);
				}
			}
			Set<String> key = map.keySet();
			Iterator<String> keyI = key.iterator();
			while(keyI.hasNext()){
				String abankNumber = keyI.next();
				List<BlockValue> kmls = map.get(abankNumber);
				BlockValue entity = new BlockValue();
				entity.setAbankNumber(abankNumber);
				for(int i = 0;i<kmls.size();i++){
					BlockValue bv = kmls.get(i);
					if(i==0){
						entity.setBlockNumber(bv.getBlockNumber());
					}
					entity.getKmls().add(bv.getKml());
				}
				result.add(entity);
			}
		}catch(Exception e){
			log.debug(e);
			throw new EJBException(e);
		}
		
		
		return result;
	}
	private String getListViewSDPSQLTemplate(String name){
		String sqlTempalte = null;
		try {
			 sqlTempalte =SQLLoader.loadSQLFromFile(DREvent2013ManagerBean.class, name);
		} catch (IOException e) {
			
		} 
		if(sqlTempalte!=null){
			
		}

		return sqlTempalte;
		 
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<EventLegend> getActiveEvents(String programName, List<String> products) {
		List<EventLegend> result=new ArrayList<EventLegend>();
		try {
			Map parms = new HashMap();
			parms.put("pram_programName", programName);
			parms.put("pram_products", products);
			if(programName.equalsIgnoreCase("CBP")){
				String sql = SQLBuilder.buildNamedParameterSQL(getListViewSDPSQLTemplate("loadCBPEvents.sql"), parms);
				
				result = drwSqlExecutor.doNativeQuery(sql,parms, new ListConverter<EventLegend>(
						new ColumnAsFeatureFactory<EventLegend>(EventLegend.class)));
			}else{
				String sql = SQLBuilder.buildNamedParameterSQL(getListViewSDPSQLTemplate("loadEvents.sql"), parms);
				
				result = drwSqlExecutor.doNativeQuery(sql,parms, new ListConverter<EventLegend>(
						new ColumnAsFeatureFactory<EventLegend>(EventLegend.class)));	
			}
			
		}catch(Exception e){
			throw new EJBException(e);
		}
		
		return result;
	}
	@Override
	public List<EventLegend> getScheduleEvents(String programName,
			List<String> products) {
		List<EventLegend> result=new ArrayList<EventLegend>();
		try {
			Map parms = new HashMap();
			parms.put("pram_programName", programName);
			parms.put("pram_products", products);
			if(programName.equalsIgnoreCase("CBP")){
				String sql = SQLBuilder.buildNamedParameterSQL(getListViewSDPSQLTemplate("loadCBPScheduleEvents.sql"), parms);
				
				result = drwSqlExecutor.doNativeQuery(sql,parms, new ListConverter<EventLegend>(
						new ColumnAsFeatureFactory<EventLegend>(EventLegend.class)));
			}else{
			String sql = SQLBuilder.buildNamedParameterSQL(getListViewSDPSQLTemplate("loadScheduleEvents.sql"), parms);
			
			result = drwSqlExecutor.doNativeQuery(sql,parms, new ListConverter<EventLegend>(
					new ColumnAsFeatureFactory<EventLegend>(EventLegend.class)));
			}
		}catch(Exception e){
			throw new EJBException(e);
		}
		
		return result;
	}
	@Override
	public List<EventValue> getDrwEvents(String eventDetailIds){
		List<String> uuids = new ArrayList<String>();
		String[] eds = eventDetailIds.split(",");
		for(String value:eds){
			uuids.add(value);
		}
		String sqltemplate= DrwSqlUtil.getSQL_DRW_COMMON_EVENT_TEMPLATE_BY_LEGEND(true);
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("eventDetailIDs", uuids);
		List<EventValue> result = cfEventManager.getLocationAndBlockEntry(sqltemplate, params);
		DrwSqlUtil.sortEventList(result);
		return result;
	}
	@Override
	public List<EventValue> getDrwCBPEvents(String eventDetailIds){
		List<String> uuids = new ArrayList<String>();
		String[] eds = eventDetailIds.split(",");
		for(String value:eds){
			uuids.add(value);
		}
		String sqltemplate= DrwSqlUtil.getSQL_DRW_CBP_EVENT_TEMPLATE_BY_LEGEND(true);
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("eventDetailIDs", uuids);
		List<EventValue> result = cfEventManager.getLocationAndBlockEntry(sqltemplate, params);
		DrwSqlUtil.sortEventList(result);
		return result;
	}
	@Override
	public boolean scheduledEventsNeedUpdate(){
		boolean result = false;
		StringBuilder sql = new StringBuilder();
		sql.append(" select COUNT(*) from event where issuedTime > NOW() and issuedTime < DATE_ADD(NOW(), interval 1 minute) ");

		int count_drw = 0;
		int count_pss2 = 0;
		try {
			count_drw = drwSqlExecutor.doNativeQuery(sql.toString(),
			new CellConverter<Integer>(Integer.class));
			count_pss2 = pss2SQLExecutor.doNativeQuery(sql.toString(),
			new CellConverter<Integer>(Integer.class));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		log.debug("PSS2 scheduled events issued time reached count is :"+count_pss2);
		log.debug("DRW scheduled events issued time reached count is :"+count_drw);
		if(count_drw>0||count_pss2>0){
			result = true;
		}
		return result;
	}
	@Override
	public boolean activeEventsNeedUpdate() {
		boolean result = false;
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT COUNT(*) FROM event WHERE issuedTime >= startTime and startTime <=NOW()");

		int count = 0;
		try {
			count = drwSqlExecutor.doNativeQuery(sql.toString(),
			new CellConverter<Integer>(Integer.class));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if(count>0){
			result = true;
		}
		return result;
	}
	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<EventVo> getActiveEventDetails(String programName,
			List<String> products) {
		List<EventVo> result = new ArrayList<EventVo>();
		try {
			Map parms = new HashMap();
			parms.put("pram_programName", programName);
			parms.put("pram_products", products);

			String sql = SQLBuilder.buildNamedParameterSQL(
					getListViewSDPSQLTemplate("load_active_events.sql"), parms);

			MasterDetailFactory<EventVo, EventLoaction> factory = new MasterDetailFactory<EventVo, EventLoaction>(
					new ColumnAsFeatureFactory<EventVo>(EventVo.class, "uuid"),
					new ColumnAsFeatureFactory<EventLoaction>(
							EventLoaction.class, "zipCode", "cityName",
							"countyName"), true) {
				private static final long serialVersionUID = 1L;

				public void buildUp(EventVo master, EventLoaction detail) {
					if (detail != null) {
						master.getLocations().add(detail);
					}
				}
			};

			result = drwSqlExecutor.doNativeQuery(sql, parms,
					new ListConverter<EventVo>(factory));

		} catch (Exception e) {
			throw new EJBException(e);
		}

		return result;
	}
	@Override
	public List<EventVo> getScheduleEventDetails(String programName,
			List<String> products) {
		List<EventVo> result = new ArrayList<EventVo>();
		try {
			Map parms = new HashMap();
			parms.put("pram_programName", programName);
			parms.put("pram_products", products);

			String sql = SQLBuilder.buildNamedParameterSQL(
					getListViewSDPSQLTemplate("load_schedule_events.sql"),
					parms);

			MasterDetailFactory<EventVo, EventLoaction> factory = new MasterDetailFactory<EventVo, EventLoaction>(
					new ColumnAsFeatureFactory<EventVo>(EventVo.class, "uuid"),
					new ColumnAsFeatureFactory<EventLoaction>(
							EventLoaction.class, "zipCode", "cityName",
							"countyName"), true) {
				private static final long serialVersionUID = 1L;

				public void buildUp(EventVo master, EventLoaction detail) {
					if (detail != null) {
						master.getLocations().add(detail);
					}
				}
			};

			result = drwSqlExecutor.doNativeQuery(sql, parms,
					new ListConverter<EventVo>(factory));

		} catch (Exception e) {
			throw new EJBException(e);
		}

		return result;
	}
	@Override
	public List<EventVo> getDREvents(List<String> programClass, boolean isActive) {
		List<EventValue> result = new ArrayList<EventValue>();
		List<String> programs=new ArrayList<String>();
		programs.addAll(programClass);
			if(isActive){
				result.addAll(cfEventManager.getPSS2ActiveEvent(programs));	
			}else{
				result.addAll(cfEventManager.getPSS2ScheduledEvent(programs));
			}
			
		List<EventVo> results = new ArrayList<EventVo>();
		for(EventValue ev:result){
			EventVo vo = new EventVo();
			vo.setUuid(ev.getUuid());
			vo.setProgram(ev.getLongProgramName());
			vo.setProduct(ev.getProduct());
			vo.setStartTime(ev.getStartTime());
			vo.setEndTime(ev.getEndTime());
			vo.setActive(isActive);
			
			results.add(vo);
		}
		return results;
	}
	@Override
	public List<Program> getAllProgram() {
		List<Program> result=new ArrayList<Program>();
		String sqltemplate= "select distinct utilityName, programClass,irProgram from program order by utilityName";
		
		Map<String,Object> params = new HashMap<String,Object>();

		try {
			String sql = SQLBuilder.buildNamedParameterSQL(sqltemplate, params);
			List<Program> pros = drwSqlExecutor.doNativeQuery(sql, params, new ListConverter<Program>(new ColumnAsFeatureFactory<Program>(Program.class)));
			if (pros!=null) 
				result.addAll(pros);
		}catch(Exception e){
			log.debug(e);
			throw new EJBException(e);
		}

		return result;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<String> getProducts(String programName,boolean isIRProgram){
		List<String> results=new ArrayList<String>();
		String sqltemplate="";
		if(isIRProgram){
			sqltemplate= "select distinct productDisplayName from program where programClass = ${programClass} order by productDisplayName";
		}else{
			sqltemplate= "select distinct longProgramName from program where programClass = ${programClass}";
		}
		Map params = new HashMap();
		params.put("programClass", programName);
		try {
			String sql = SQLBuilder.buildNamedParameterSQL(sqltemplate, params);
			if(isIRProgram){
				results = drwSqlExecutor.doNativeQuery(sql, params, new SimpleListConverter<String>(String.class));
			}else{
				results = pss2SQLExecutor.doNativeQuery(sql, params, new SimpleListConverter<String>(String.class));
			}
		}catch(Exception e){
			log.debug(e);
			throw new EJBException(e);
		}
		return results;
	}
	
	
	@Override
	public List<String> getBlocksForSlap(String number){
		List<String> results=new ArrayList<String>();
		String sqltemplate="SELECT block FROM location WHERE parentID = (SELECT id FROM location WHERE number=${number}) ORDER BY CONVERT(block, SIGNED INTEGER)";
		Map params = new HashMap();
		params.put("number", number);
		try {
			String sql = SQLBuilder.buildNamedParameterSQL(sqltemplate, params);
			results = drwSqlExecutor.doNativeQuery(sql, params, new SimpleListConverter<String>(String.class));
		}catch(Exception e){
			log.debug(e);
			throw new EJBException(e);
		}
		return results;
	}

	@Override
	public SlapBlockRange getSlapBlockRange(String number) {
		List<SlapBlockRange> results=new ArrayList<SlapBlockRange>();
		String sqltemplate="";
		sqltemplate= "SELECT CAST(parentID AS CHAR) id, MIN(CONVERT(block, SIGNED INTEGER)) 'minValue', MAX(CONVERT(block, SIGNED INTEGER)) 'maxValue' FROM location WHERE parentID=(SELECT id FROM location WHERE number=${number}) ";
		Map params = new HashMap();
		params.put("number", number);
		try {
			String sql = SQLBuilder.buildNamedParameterSQL(sqltemplate, params);
				results = drwSqlExecutor.doNativeQuery(sql, params,new ListConverter<SlapBlockRange>(new ColumnAsFeatureFactory<SlapBlockRange>(SlapBlockRange.class)));
		}catch(Exception e){
			log.debug(e);
			throw new EJBException(e);
		}
		return results.isEmpty()?new SlapBlockRange():results.get(0);
	}
	@Override
	public List<String> getAllSlaps() {
		List<String> results=new ArrayList<String>();
		String sqltemplate="";
			sqltemplate= "SELECT number FROM location WHERE TYPE='SLAP'";
		Map params = new HashMap();
		try {
			String sql = SQLBuilder.buildNamedParameterSQL(sqltemplate, params);
				results = drwSqlExecutor.doNativeQuery(sql, params, new SimpleListConverter<String>(String.class));
		}catch(Exception e){
			log.debug(e);
			throw new EJBException(e);
		}
		return results;
	}
	@Override
	public String getSlap4Block(String number) {
		List<String> results=new ArrayList<String>();
		String sqltemplate="";
			sqltemplate= "SELECT number FROM location WHERE id=(SELECT parentID FROM location WHERE block=${number})";
			Map params = new HashMap();
			params.put("number", number);
		try {
			String sql = SQLBuilder.buildNamedParameterSQL(sqltemplate, params);
				results = drwSqlExecutor.doNativeQuery(sql, params, new SimpleListConverter<String>(String.class));
		}catch(Exception e){
			log.debug(e);
			throw new EJBException(e);
		}
		if(results!=null&&!results.isEmpty()) return results.get(0);
		
		return "NULL";
	}
	
	
	
	
	@Override
	public List<EventVo> getDREvents4Mobile(List<String> programClass, boolean isActive) {
		List<EventValue> result = new ArrayList<EventValue>();
		List<String> programs=new ArrayList<String>();
		programs.addAll(programClass);
			if(isActive){
				result.addAll(cfEventManager.getPSS2ActiveEvent(programs));	
			}else{
				result.addAll(cfEventManager.getPSS2ScheduledEvent4Mobile(programs));
			}
			
		List<EventVo> results = new ArrayList<EventVo>();
		for(EventValue ev:result){
			EventVo vo = new EventVo();
			vo.setUuid(ev.getUuid());
			vo.setProgram(ev.getLongProgramName());
			vo.setProduct(ev.getProduct());
			vo.setStartTime(ev.getStartTime());
			vo.setEndTime(ev.getEndTime());
			vo.setActive(isActive);
			vo.setIssueTime(ev.getIssueTime());
			
			results.add(vo);
		}
		return results;
	}
}
