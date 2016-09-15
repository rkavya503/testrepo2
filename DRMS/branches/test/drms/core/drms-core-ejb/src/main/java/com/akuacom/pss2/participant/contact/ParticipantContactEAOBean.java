package com.akuacom.pss2.participant.contact;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;

import com.akuacom.ejb.BaseEAOBean;
import com.akuacom.ejb.EntityNotFoundException;
import com.akuacom.jdbc.CellConverter;
import com.akuacom.jdbc.ColumnAsFeatureFactory;
import com.akuacom.jdbc.ListConverter;
import com.akuacom.jdbc.SQLBuilder;
import com.akuacom.pss2.Pss2SQLExecutor;
import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.data.PDataSet;
import com.akuacom.pss2.nssettings.NSSettings;
import com.akuacom.pss2.nssettings.NSSettingsManager;
import com.akuacom.pss2.participant.UserEAOBean;
import com.akuacom.pss2.util.LogUtils;

@Stateless
public class ParticipantContactEAOBean extends BaseEAOBean<ParticipantContact> implements ParticipantContactEAO.R, ParticipantContactEAO.L {

	/** The Constant log. */
    private static final Logger log = Logger.getLogger(UserEAOBean.class);
    
    @EJB
	Pss2SQLExecutor.L sqlExecutor;
	
	public ParticipantContactEAOBean() {
		super(ParticipantContact.class);
	}

	@Override
	public int getMsgThreshold(String contactUuid) {
		int msgThreshold = -1;
		try {
			ParticipantContact contact=getById(contactUuid);
			if (contact != null) {
				NSSettingsManager NSSettingsManager = EJBFactory.getBean(NSSettingsManager.class);
				if (NSSettingsManager != null) {
					NSSettings settings = NSSettingsManager.getNSSettings();
					if (settings != null) {
						if (contact.getDefaultMsgThreshold()) {
							msgThreshold = settings.getMsgThreshold();
						} else {
							msgThreshold = contact.getMsgThreshold();
						}
					}
				}
			} 
		} catch (EntityNotFoundException e) {
			// we send msgThreshold = -1;
		}
		return msgThreshold;
	}	

	@Override
	public ParticipantContact updateParticipant(ParticipantContact participantContact){
		try {
			if (participantContact != null) {
				NSSettingsManager NSSettingsManager = EJBFactory.getBean(NSSettingsManager.class);
				NSSettings settings = NSSettingsManager.getNSSettings();
				if ((participantContact.getDefaultMsgThreshold()) && (settings!=null)) {
					participantContact.setMsgThreshold(settings.getMsgThreshold());
				}
			}

			return update(participantContact);
		} catch (Exception e) {
	        String message = "error updating participantContact " + participantContact.getDescription();
	        log.debug(message, e);
	        throw new EJBException(message, e);
	    }
	}

	@Override
	public void persistParticipantContact(ParticipantContact pc, String rootClientUuid, List<String> participant_uuids) {
		StringBuffer sqltemplate = new StringBuffer(); 
		sqltemplate.append("  INSERT INTO `participant_contact`       \n");
		sqltemplate.append("  (`uuid`, `creationTime`, `type`, `address`, `description`, `commNotification`, `eventNotification`, `offSeasonNotiHours`, `onSeasonNotiHours`,       \n");
		sqltemplate.append("   `offlineError`, `external`, `participant_uuid`, `msgThreshold`, `defaultMsgThreshold`, `demandLimitingNotifications`, `rateLimitExceededNotifications`, 	    \n");
		sqltemplate.append("  `rateLimitExceededNotificationInterval`, `rateLimitExceededNotificationLast`, `demandModerateNotifications`, `demandModerateNotificationInterval`, 		\n");
		sqltemplate.append("  `demandModerateNotificationLast`, `demandHighNotifications`, `demandHighNotificationInterval`, `demandHighNotificationLast`, `demandNormalNotifications`,        \n");
		sqltemplate.append("  `demandNormalNotificationInterval`, `demandNormalNotificationLast`, `demandWarningNotifications`, `demandWarningNotificationInterval`,        \n");
		sqltemplate.append("  `demandWarningNotificationLast`)       \n");
		sqltemplate.append("  VALUES(REPLACE(UUID(), '-', ''),    \n");
		sqltemplate.append("  ${pram_creationTime}, ${pram_type}, ${pram_address}, ${pram_description}, ${pram_commNotification}, ${pram_eventNotification}, ${pram_offSeasonNotiHours},${pram_onSeasonNotiHours},       \n");
		sqltemplate.append("  ${pram_offlineError}, ${pram_external}, ${pram_participant_uuid}, ${pram_msgThreshold}, ${pram_defaultMsgThreshold}, ${pram_demandLimitingNotifications}, ${pram_rateLimitExceededNotifications}, 	    \n");
		sqltemplate.append("  ${pram_rateLimitExceededNotificationInterval}, ${pram_rateLimitExceededNotificationLast}, ${pram_demandModerateNotifications}, ${pram_demandModerateNotificationInterval}, 		\n");
		sqltemplate.append("  ${pram_demandModerateNotificationLast}, ${pram_demandHighNotifications}, ${pram_demandHighNotificationInterval}, ${pram_demandHighNotificationLast}, ${pram_demandNormalNotifications},        \n");
		sqltemplate.append("  ${pram_demandNormalNotificationInterval}, ${pram_demandNormalNotificationLast}, ${pram_demandWarningNotifications}, ${pram_demandWarningNotificationInterval},        \n");
		sqltemplate.append("  ${pram_demandWarningNotificationLast})       \n");
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("pram_creationTime", pc.getCreationTime());
		params.put("pram_type", pc.getType());
		params.put("pram_address", pc.getAddress());
		params.put("pram_description", pc.getDescription());
		params.put("pram_commNotification", pc.isCommNotification());
		
		params.put("pram_eventNotification", pc.getEventNotification().toString());
		params.put("pram_offSeasonNotiHours", pc.getOffSeasonNotiHours());
		params.put("pram_onSeasonNotiHours", pc.getOnSeasonNotiHours());
		params.put("pram_offlineError", pc.isOfflineError());
		params.put("pram_external", pc.getExternal());
		
		params.put("pram_msgThreshold", pc.getMsgThreshold());
		params.put("pram_defaultMsgThreshold", pc.getDefaultMsgThreshold());
		params.put("pram_demandLimitingNotifications", pc.getDemandLimitingNotifications());
		params.put("pram_rateLimitExceededNotifications", pc.getRateLimitExceededNotifications());
		
		params.put("pram_rateLimitExceededNotificationInterval", pc.getRateLimitExceededNotificationInterval());
		params.put("pram_rateLimitExceededNotificationLast", pc.getRateLimitExceededNotificationLast());
		params.put("pram_demandModerateNotifications", pc.getDemandModerateNotifications());
		params.put("pram_demandModerateNotificationInterval", pc.getDemandModerateNotificationInterval());
		params.put("pram_demandModerateNotificationLast", pc.getDemandModerateNotificationLast());
		
		params.put("pram_demandHighNotifications", pc.getDemandHighNotifications());
		params.put("pram_demandHighNotificationInterval", pc.getDemandHighNotificationInterval());
		params.put("pram_demandHighNotificationLast", pc.getDemandHighNotificationLast());
		params.put("pram_demandNormalNotifications", pc.getDemandNormalNotifications());
		params.put("pram_demandNormalNotificationInterval", pc.getDemandNormalNotificationInterval());
		
		params.put("pram_demandNormalNotificationLast", pc.getDemandNormalNotificationLast());
		params.put("pram_demandWarningNotifications", pc.getDemandWarningNotifications());
		params.put("pram_demandWarningNotificationInterval", pc.getDemandWarningNotificationInterval());
		params.put("pram_demandWarningNotificationLast", pc.getDemandWarningNotificationLast());
		
		params.put("pram_participant_uuid", rootClientUuid);
		
		String sql = null;
		try {
			sql = SQLBuilder.buildNamedParameterSQL(sqltemplate.toString(), params);
			int result = sqlExecutor.execute(sql, params);
		} catch (Exception e) {
			throw new EJBException(e);
		}
		
		List<ParticipantContact> pcs = this.getParticipantContacts(rootClientUuid);
		Boolean exists = false;
		for (ParticipantContact c : pcs) {
    		if (c != null && c.getAddress() != null && c.getDescription() != null) {
    			if (c.getAddress().equalsIgnoreCase(pc.getAddress()) && 
    					c.getDescription().equals(pc.getDescription())) {
    				exists = true;
    				break;
    			}
    		}
    	}
		if(!exists){
			pcs.add(pc);
		}
		
		List<String> part_uuids = new ArrayList<String>();
		if(participant_uuids!=null&&!participant_uuids.isEmpty()){
			for(String p : participant_uuids){
				if(!rootClientUuid.equalsIgnoreCase(p)){
					part_uuids.add(p);
				}
			}
		}
		removeParticipantContact(part_uuids);
		persistParticipantContacts(pcs, part_uuids);
		
	}


	@Override
	public void removeParticipantContact(String address, String desc,
			List<String> participant_uuids) { 
		// TODO Auto-generated method stub
			StringBuffer sqltemplate = new StringBuffer(); 
			sqltemplate.append(" DELETE FROM participant_contact WHERE address=${pram_address} AND description=${pram_description} AND participant_uuid IN ${parm_participant_uuids};      \n");
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("pram_address", address);
			params.put("pram_description", desc);
			params.put("parm_participant_uuids",participant_uuids);
			
			String sql = null;
			try {
				sql = SQLBuilder.buildNamedParameterSQL(sqltemplate.toString(), params);
				int result = sqlExecutor.execute(sql, params);
			} catch (Exception e) {
				throw new EJBException(e);
			}
	}

	@Override
	public void updateParticipantContact(ParticipantContact pc, String orig_type, 
			String orig_address, String orig_desc, List<String> participant_uuids) {
		StringBuffer sqltemplate = new StringBuffer(); 
		sqltemplate.append(" UPDATE participant_contact SET type=${pram_type} , address=${pram_address_new}  , description=${pram_description_new} ,    		 							\n");
		sqltemplate.append(" commNotification=${pram_commNotification}, eventNotification=${pram_eventNotification},    										 		\n");
		sqltemplate.append(" offSeasonNotiHours=${pram_offSeasonNotiHours}, onSeasonNotiHours=${pram_onSeasonNotiHours} ,   						 					\n");
		sqltemplate.append(" defaultMsgThreshold=${pram_defaultMsgThreshold}, msgThreshold=${pram_msgThreshold} ,   							 						\n");
		sqltemplate.append(" demandLimitingNotifications=${pram_demandLimitingNotifications}, rateLimitExceededNotifications=${pram_rateLimitExceededNotifications},    \n");
		sqltemplate.append(" demandModerateNotifications=${pram_demandModerateNotifications}, demandHighNotifications=${pram_demandHighNotifications},    		 		\n");
		sqltemplate.append(" demandNormalNotifications=${pram_demandNormalNotifications}, demandWarningNotifications=${pram_demandWarningNotifications}    		 		\n");
		sqltemplate.append(" WHERE type=${pram_type_orig} AND address=${pram_address_orig}  AND description=${pram_description_orig} AND participant_uuid IN ${parm_participant_uuids};     		\n");
		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("pram_type", pc.getType());
		params.put("pram_address_new", pc.getAddress());
		params.put("pram_description_new", pc.getDescription());
		params.put("pram_commNotification", pc.isCommNotification());
		params.put("pram_eventNotification", pc.getEventNotification().toString());
		params.put("pram_offSeasonNotiHours", pc.getOffSeasonNotiHours());
		params.put("pram_onSeasonNotiHours", pc.getOnSeasonNotiHours());
		params.put("pram_msgThreshold", pc.getMsgThreshold());
		params.put("pram_defaultMsgThreshold", pc.getDefaultMsgThreshold());
		params.put("pram_demandLimitingNotifications", pc.getDemandLimitingNotifications());
		params.put("pram_rateLimitExceededNotifications", pc.getRateLimitExceededNotifications());
		params.put("pram_demandModerateNotifications", pc.getDemandModerateNotifications());
		params.put("pram_demandHighNotifications", pc.getDemandHighNotifications());
		params.put("pram_demandNormalNotifications", pc.getDemandNormalNotifications());
		params.put("pram_demandWarningNotifications", pc.getDemandWarningNotifications());
		params.put("pram_type_orig", orig_type);
		params.put("pram_address_orig", orig_address);
		params.put("pram_description_orig", orig_desc);
		params.put("parm_participant_uuids",participant_uuids);
		
		String sql = null;
		try {
			sql = SQLBuilder.buildNamedParameterSQL(sqltemplate.toString(), params);
			int result = sqlExecutor.execute(sql, params);
			
			System.out.println("######Update Result: "+ result);
			
		} catch (Exception e) {
			throw new EJBException(e);
		}
		
	}

	@Override
	public int countParticipantContact(String address, String desc,
			String participant_uuid) {
		// TODO Auto-generated method stub
		StringBuffer sqltemplate = new StringBuffer(); 
		sqltemplate.append("  SELECT COUNT(*) counts FROM participant_contact        \n");
		sqltemplate.append(" WHERE address=${pram_address}  AND description=${pram_description} AND participant_uuid = ${parm_participant_uuid};     		\n");
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("pram_address", address);
		params.put("pram_description", desc);
		params.put("parm_participant_uuid", participant_uuid);
		
		String sql = null;
		int count = 0;
		try {
			sql = SQLBuilder.buildNamedParameterSQL(sqltemplate.toString(), params);
			count = sqlExecutor.doNativeQuery(sql,params,new CellConverter<Integer>(Integer.class));
			
		} catch (Exception e) {
			throw new EJBException(e);
		}
		return count;
	}
	
	@Override
	public List<ParticipantContact> getParticipantContacts(String uuid){
		List<ParticipantContact> results = null;
		StringBuffer sqltemplate = new StringBuffer(); 
		sqltemplate.append(" SELECT `type`,`address`,`description`, `commNotification`, `eventNotification`, `offSeasonNotiHours`, `onSeasonNotiHours`,       \n");
		sqltemplate.append("    `offlineError`, `external`, `participant_uuid`, `msgThreshold`, `defaultMsgThreshold`, `demandLimitingNotifications`, `rateLimitExceededNotifications`,        \n");
		sqltemplate.append("    `rateLimitExceededNotificationInterval`, `rateLimitExceededNotificationLast`, `demandModerateNotifications`, `demandModerateNotificationInterval`,        \n");
		sqltemplate.append("    `demandModerateNotificationLast`, `demandHighNotifications`, `demandHighNotificationInterval`, `demandHighNotificationLast`, `demandNormalNotifications`,         \n");
		sqltemplate.append("    `demandNormalNotificationInterval`, `demandNormalNotificationLast`, `demandWarningNotifications`, `demandWarningNotificationInterval`, `demandWarningNotificationLast`      \n");
		sqltemplate.append("  FROM `participant_contact`       \n");
		sqltemplate.append("  WHERE participant_uuid= ${parm_participant_uuid};          \n");
		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("parm_participant_uuid", uuid);
		try {
		String sql=SQLBuilder.buildNamedParameterSQL(sqltemplate.toString(), params);
		results = sqlExecutor.doNativeQuery(sql,params,
				new ListConverter<ParticipantContact>(new ColumnAsFeatureFactory<ParticipantContact>(ParticipantContact.class)));
		}catch(Exception e) {
			log.debug(LogUtils.createExceptionLogEntry(null, null, e));
		}
		
		return results==null?new ArrayList<ParticipantContact>():results;
	}
	
	
	@Override
	public void removeParticipantContact(List<String> participant_uuids) { 
		if(null==participant_uuids||participant_uuids.isEmpty()) return;
		// TODO Auto-generated method stub
			StringBuffer sqltemplate = new StringBuffer(); 
			sqltemplate.append(" DELETE FROM participant_contact WHERE participant_uuid IN ${parm_participant_uuids};");
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("parm_participant_uuids",participant_uuids);
			
			String sql = null;
			try {
				sql = SQLBuilder.buildNamedParameterSQL(sqltemplate.toString(), params);
				int result = sqlExecutor.execute(sql, params);
			} catch (Exception e) {
				throw new EJBException(e);
			}
	}

	@Override
	public void persistParticipantContacts(List<ParticipantContact> pcs,
			List<String> participant_uuids) {
		if(null==participant_uuids||participant_uuids.isEmpty()) return;
		StringBuffer sqltemplate = new StringBuffer(); 
		sqltemplate.append("  INSERT INTO `participant_contact`       \n");
		sqltemplate.append("  (`uuid`, `creationTime`, `type`, `address`, `description`, `commNotification`, `eventNotification`, `offSeasonNotiHours`, `onSeasonNotiHours`,       \n");
		sqltemplate.append("   `offlineError`, `external`, `participant_uuid`, `msgThreshold`, `defaultMsgThreshold`, `demandLimitingNotifications`, `rateLimitExceededNotifications`, 	    \n");
		sqltemplate.append("  `rateLimitExceededNotificationInterval`, `rateLimitExceededNotificationLast`, `demandModerateNotifications`, `demandModerateNotificationInterval`, 		\n");
		sqltemplate.append("  `demandModerateNotificationLast`, `demandHighNotifications`, `demandHighNotificationInterval`, `demandHighNotificationLast`, `demandNormalNotifications`,        \n");
		sqltemplate.append("  `demandNormalNotificationInterval`, `demandNormalNotificationLast`, `demandWarningNotifications`, `demandWarningNotificationInterval`,        \n");
		sqltemplate.append("  `demandWarningNotificationLast`)       \n");
		sqltemplate.append("  VALUES(REPLACE(UUID(), '-', ''),    \n");
		sqltemplate.append("  ${pram_creationTime}, ${pram_type}, ${pram_address}, ${pram_description}, ${pram_commNotification}, ${pram_eventNotification}, ${pram_offSeasonNotiHours},${pram_onSeasonNotiHours},       \n");
		sqltemplate.append("  ${pram_offlineError}, ${pram_external}, ${pram_participant_uuid}, ${pram_msgThreshold}, ${pram_defaultMsgThreshold}, ${pram_demandLimitingNotifications}, ${pram_rateLimitExceededNotifications}, 	    \n");
		sqltemplate.append("  ${pram_rateLimitExceededNotificationInterval}, ${pram_rateLimitExceededNotificationLast}, ${pram_demandModerateNotifications}, ${pram_demandModerateNotificationInterval}, 		\n");
		sqltemplate.append("  ${pram_demandModerateNotificationLast}, ${pram_demandHighNotifications}, ${pram_demandHighNotificationInterval}, ${pram_demandHighNotificationLast}, ${pram_demandNormalNotifications},        \n");
		sqltemplate.append("  ${pram_demandNormalNotificationInterval}, ${pram_demandNormalNotificationLast}, ${pram_demandWarningNotifications}, ${pram_demandWarningNotificationInterval},        \n");
		sqltemplate.append("  ${pram_demandWarningNotificationLast})       \n");
		Map<String,Object> params = new HashMap<String,Object>();
		for(ParticipantContact c:pcs){
			params = new HashMap<String,Object>();
			params.put("pram_creationTime", c.getCreationTime());
			params.put("pram_type", c.getType());
			params.put("pram_address", c.getAddress());
			params.put("pram_description", c.getDescription());
			params.put("pram_commNotification", c.isCommNotification());
			
			params.put("pram_eventNotification", c.getEventNotification().toString());
			params.put("pram_offSeasonNotiHours", c.getOffSeasonNotiHours());
			params.put("pram_onSeasonNotiHours", c.getOnSeasonNotiHours());
			params.put("pram_offlineError", c.isOfflineError());
			params.put("pram_external", c.getExternal());
			
			params.put("pram_msgThreshold", c.getMsgThreshold());
			params.put("pram_defaultMsgThreshold", c.getDefaultMsgThreshold());
			params.put("pram_demandLimitingNotifications", c.getDemandLimitingNotifications());
			params.put("pram_rateLimitExceededNotifications", c.getRateLimitExceededNotifications());
			
			params.put("pram_rateLimitExceededNotificationInterval", c.getRateLimitExceededNotificationInterval());
			params.put("pram_rateLimitExceededNotificationLast", c.getRateLimitExceededNotificationLast());
			params.put("pram_demandModerateNotifications", c.getDemandModerateNotifications());
			params.put("pram_demandModerateNotificationInterval", c.getDemandModerateNotificationInterval());
			params.put("pram_demandModerateNotificationLast", c.getDemandModerateNotificationLast());
			
			params.put("pram_demandHighNotifications", c.getDemandHighNotifications());
			params.put("pram_demandHighNotificationInterval", c.getDemandHighNotificationInterval());
			params.put("pram_demandHighNotificationLast", c.getDemandHighNotificationLast());
			params.put("pram_demandNormalNotifications", c.getDemandNormalNotifications());
			params.put("pram_demandNormalNotificationInterval", c.getDemandNormalNotificationInterval());
			
			params.put("pram_demandNormalNotificationLast", c.getDemandNormalNotificationLast());
			params.put("pram_demandWarningNotifications", c.getDemandWarningNotifications());
			params.put("pram_demandWarningNotificationInterval", c.getDemandWarningNotificationInterval());
			params.put("pram_demandWarningNotificationLast", c.getDemandWarningNotificationLast());
			for(String uuid:participant_uuids){
				params.remove("pram_participant_uuid");
				params.put("pram_participant_uuid", uuid);
				
				try {
					String sql = SQLBuilder.buildNamedParameterSQL(sqltemplate.toString(), params);
					int result = sqlExecutor.execute(sql, params);
				} catch (Exception e) {
					throw new EJBException(e);
				}
			}// end of for(String uuid:participant_uuids)
		}
		
	}

}