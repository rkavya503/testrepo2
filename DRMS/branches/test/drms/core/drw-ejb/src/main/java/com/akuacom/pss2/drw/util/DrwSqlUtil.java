package com.akuacom.pss2.drw.util;

import java.util.Collections;
import java.util.List;

import com.akuacom.pss2.drw.value.EventValue;

public class DrwSqlUtil {
	
	public static String ACTIVE_CONDITION_STATEMENT=" e.startTime<=NOW() and (ed.actualEndTime is null or ed.actualEndTime > NOW()) ";
	//public static String SCHEDULED_CONDITION_STATEMENT=" e.startTime > NOW() and (e.issuedTime< NOW() or e.issuedTime is null)";
	public static String SCHEDULED_CONDITION_STATEMENT=" e.startTime > NOW()";
	public static String getSQL_DRW_COMMON_EVENT_TEMPLATE(boolean isActive){		
		StringBuilder builder=new StringBuilder();
		builder.append("  select CONCAT(tmp.eventID,CAST(tmp.startTime as char),IF(tmp.endTime is not null, CAST(tmp.endTime as char),'NULL'))  as eventKey,tmp.* from ");
		builder.append(" ( ");
		builder.append(" select eventEntity.*, ze.block,ze.cityName,ze.countyName,ze.zipCode from ");
		builder.append(" ( ");
		builder.append(" select ");
		builder.append(" p.utilityName as utilityProgramName, p.programClass, p.longProgramName, p.name as product, "); 
		builder.append(" e.uuid as eventID ,");
		builder.append(" ed.uuid as eventDetailID,ed.locationID,ed.allLocationType,");
		builder.append(" e.startTime,e.issuedTime issueTime, ");
		builder.append(" IF(ed.actualEndTime is not null, ed.actualEndTime, ed.estimatedEndTime) as endTime, ");  
		builder.append(" IF( ed.estimatedEndTime is null and ed.actualEndTime is null,'1','0') as tbdFlag "); 
		builder.append(" from event e, event_detail ed, program p ");
		builder.append(" where p.programClass = ${programClass} and p.product = e.product ");
		builder.append(" and e.product in ${product} and e.uuid = ed.event_uuid ");
		builder.append(" and ");
		if(isActive){
			builder.append(ACTIVE_CONDITION_STATEMENT);	
		}else{
			builder.append(SCHEDULED_CONDITION_STATEMENT);
		}
		//builder.append(" group by CONCAT(e.uuid,CAST(e.startTime as char),IF(endTime is not null, CAST(endTime as char),'NULL')) ");
		builder.append(" ) eventEntity, event_detail eDetail, zipcode_entry ze");
		builder.append(" where eDetail.uuid = eventEntity.eventDetailID and eDetail.uuid = ze.eventDetail_uuid ");
		builder.append(" [and ze.zipCode in ${zipCodes}] [and ze.cityName=${city}] [and ze.countyName=${county}] ");
		builder.append(" ) tmp left join location l on tmp.locationID = l.id order by programClass, product, startTime, endTime ");
		return builder.toString();
	}
	
	public static String getSQL_KML_BLOCK_TEMPLATE(){		
		StringBuilder builder=new StringBuilder();
		builder.append(" select distinct l.block as blockNumber,l.number as abankNumber,lk.kml from location_kml lk,location l ");
		builder.append(" where lk.number = l.number and locationType = 'ABank' and l.block in ${blocks}  ");
		builder.append(" order by blockNumber ");
		
		return builder.toString();
	}
	
	
	public static String getSQL_DRW_COMMON_EVENT_TEMPLATE_BY_LEGEND(boolean isActive){		
		StringBuilder builder=new StringBuilder();
		builder.append("  select CONCAT(CAST(tmp.startTime as char),IF(tmp.endTime is not null, CAST(tmp.endTime as char),'NULL'))  as eventKey,tmp.* from ");
		builder.append(" ( ");
		builder.append(" select eventEntity.*, ze.block,ze.cityName,ze.countyName,ze.zipCode from ");
		builder.append(" ( ");
		builder.append(" select ");
		builder.append(" p.utilityName as utilityProgramName, p.programClass, p.longProgramName, p.name as product, "); 
		builder.append(" ed.uuid as eventDetailID,ed.locationID,ed.allLocationType,");
		builder.append(" e.startTime,e.issuedTime issueTime, ");
		builder.append(" IF(ed.actualEndTime is not null, ed.actualEndTime, ed.estimatedEndTime) as endTime, ");  
		builder.append(" IF( ed.estimatedEndTime is null and ed.actualEndTime is null,'1','0') as tbdFlag "); 
		builder.append(" from event e, event_detail ed, program p ");
		builder.append(" where p.product = e.product and e.uuid = ed.event_uuid ");
		builder.append(" and ed.uuid in ${eventDetailIDs} ");
		builder.append(" ) eventEntity, event_detail eDetail, zipcode_entry ze");
		builder.append(" where eDetail.uuid = eventEntity.eventDetailID and eDetail.uuid = ze.eventDetail_uuid ");
		builder.append(" ) tmp left join location l on tmp.locationID = l.id order by programClass, product, startTime, endTime ");
		return builder.toString();
	}
	public static void sortEventList(List<EventValue> aItems) {
		if(aItems!=null){
			EventValueComparator comparator = new EventValueComparator();
			
			Collections.sort(aItems, comparator);
		}
	}
	
	public static String getSQL_DRW_CBP_EVENT_TEMPLATE(boolean isActive){		
		StringBuilder builder=new StringBuilder();
		builder.append("  select CONCAT(product,CAST(tmp.startTime as char),IF(tmp.endTime is not null, CAST(tmp.endTime as char),'NULL'))  as eventKey,tmp.* from ");
		builder.append(" ( ");
		builder.append(" select eventEntity.*, ze.block,ze.cityName,ze.countyName,ze.zipCode from ");
		builder.append(" ( ");
		builder.append(" select ");
		builder.append(" p.utilityName as utilityProgramName, p.programClass, p.longProgramName, p.name as product, "); 
		builder.append(" e.uuid as eventID ,");
		builder.append(" ed.uuid as eventDetailID,ed.locationID,ed.allLocationType,");
		builder.append(" e.startTime,e.issuedTime issueTime, ");
		builder.append(" IF(ed.actualEndTime is not null, ed.actualEndTime, ed.estimatedEndTime) as endTime, ");  
		builder.append(" IF( ed.estimatedEndTime is null and ed.actualEndTime is null,'1','0') as tbdFlag "); 
		builder.append(" from event e, event_detail ed, program p ");
		builder.append(" where p.programClass = ${programClass} and p.product = e.product ");
		builder.append(" and e.product in ${product} and e.uuid = ed.event_uuid ");
		builder.append(" and ");
		if(isActive){
			builder.append(ACTIVE_CONDITION_STATEMENT);	
		}else{
			builder.append(SCHEDULED_CONDITION_STATEMENT);
		}
		//builder.append(" group by CONCAT(e.uuid,CAST(e.startTime as char),IF(endTime is not null, CAST(endTime as char),'NULL')) ");
		builder.append(" ) eventEntity, event_detail eDetail, zipcode_entry ze");
		builder.append(" where eDetail.uuid = eventEntity.eventDetailID and eDetail.uuid = ze.eventDetail_uuid ");
		builder.append(" [and ze.zipCode in ${zipCodes}] [and ze.cityName=${city}] [and ze.countyName=${county}] ");
		builder.append(" ) tmp left join location l on tmp.locationID = l.id order by programClass, product, startTime, endTime ");
		return builder.toString();
	}
	public static String getSQL_DRW_CBP_EVENT_TEMPLATE_BY_LEGEND(boolean isActive){		
		StringBuilder builder=new StringBuilder();
		builder.append("  select CONCAT(product,CONCAT(CAST(startTime as char),IF(endTime is not null, CAST(endTime as char),'NULL')))  as eventKey ,tmp.* from ");
		builder.append(" ( ");
		builder.append(" select eventEntity.*, ze.block,ze.cityName,ze.countyName,ze.zipCode from ");
		builder.append(" ( ");
		builder.append(" select ");
		builder.append(" p.utilityName as utilityProgramName, p.programClass, p.longProgramName, p.name as product, "); 
		builder.append(" e.uuid as eventID ,");
		builder.append(" ed.uuid as eventDetailID,ed.locationID,ed.allLocationType,");
		builder.append(" e.startTime,e.issuedTime issueTime, ");
		builder.append(" IF(ed.actualEndTime is not null, ed.actualEndTime, ed.estimatedEndTime) as endTime, ");  
		builder.append(" IF( ed.estimatedEndTime is null and ed.actualEndTime is null,'1','0') as tbdFlag "); 
		builder.append(" from event e, event_detail ed, program p ");
		builder.append(" where p.product = e.product and e.uuid = ed.event_uuid ");
		builder.append(" and ed.uuid in ${eventDetailIDs} ");
		builder.append(" ) eventEntity, event_detail eDetail, zipcode_entry ze");
		builder.append(" where eDetail.uuid = eventEntity.eventDetailID and eDetail.uuid = ze.eventDetail_uuid ");
		builder.append(" ) tmp left join location l on tmp.locationID = l.id order by programClass, product, startTime, endTime ");
		return builder.toString();
	}
}
