package com.akuacom.pss2.drw;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;
import org.jboss.ejb3.annotation.LocalBinding;
import org.jboss.ejb3.annotation.RemoteBinding;

import com.akuacom.jdbc.ColumnAsFeatureFactory;
import com.akuacom.jdbc.ListConverter;
import com.akuacom.jdbc.SQLBuilder;
import com.akuacom.jdbc.SQLLoader;
import com.akuacom.pss2.drw.core.LocationKmlEntry;
import com.akuacom.pss2.drw.core.Program;
import com.akuacom.pss2.drw.eao.LocationKmlEAO;
import com.akuacom.pss2.drw.exception.DuplicatedKeyException;
import com.akuacom.pss2.drw.value.LocationKmlStatus;

@Stateless	
@LocalBinding(jndiBinding="dr-pro/LocationKmlManager/local")
@RemoteBinding(jndiBinding="dr-pro/LocationKmlManager/remote")
public class LocationKmlManagerBean implements LocationKmlManager.R, LocationKmlManager.L {
	
	private static final Logger log = Logger.getLogger(CFEventManagerBean.class);
	
	@EJB
	LocationKmlEAO.L lkEAO;
	@EJB
	DrwSQLExecutor.L drwSqlExecutor;

	@Override
	public void merge(LocationKmlEntry entry) {
		// need to add a query method
	
		List<LocationKmlEntry> fetchedList = lkEAO.getLocationKmlByNum(entry.getNumber());
		if(fetchedList!=null&&!fetchedList.isEmpty()) {
			// case of exist record, just update it.
			LocationKmlEntry entity = fetchedList.iterator().next();
			entity.setKml(entry.getKml());
			lkEAO.update(entity);
		}else{
			// case of new record, insert it
			try {
				lkEAO.create(entry);
			} catch (DuplicatedKeyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void delete(String type, String number) {
		String sqltemplate=" DELETE FROM location_kml WHERE locationType=${type}  AND number =${number} ;";
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("type", type);
		params.put("number", number);
		
		try {
			String sql = SQLBuilder.buildNamedParameterSQL(sqltemplate, params);
			drwSqlExecutor.execute(sql, params);
		}catch(Exception e){
			log.debug(e);
			throw new EJBException(e);
		}
		
	}

	@Override
	public void insert(LocationKmlEntry entry) {
		try {
			lkEAO.create(entry);
		} catch (DuplicatedKeyException e) {
			e.printStackTrace();
		}
		
	}
	
	@Override
	public List<LocationKmlStatus> getAllLocationKmlStatus() {
		List<LocationKmlStatus> result=null;
		
		String sqlTempalte = null;
		try {
			 sqlTempalte =SQLLoader.loadSQLFromFile(LocationKmlManagerBean.class, "getLocationKMLStatus.sql");
		} catch (IOException e) {
			
		} 
		
		Map<String,Object> params = new HashMap<String,Object>();

		try {
			String sql = SQLBuilder.buildNamedParameterSQL(sqlTempalte, params);
			result = drwSqlExecutor.doNativeQuery(sql, params, new ListConverter<LocationKmlStatus>(new ColumnAsFeatureFactory<LocationKmlStatus>(LocationKmlStatus.class)));
		}catch(Exception e){
			log.debug(e);
			throw new EJBException(e);
		}

		return result==null?Collections.EMPTY_LIST:result;
	}

}
