package com.akuacom.pss2.richsite.event;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.openadr.dras.utilitydrevent.UtilityDREvent;

import com.akuacom.ejb.client.EJB3Factory;
import com.akuacom.pss2.core.ErrorUtil;
import com.akuacom.pss2.program.dbp.DBPEvent;
import com.akuacom.pss2.program.dbp.DBPProgramManager;
import com.akuacom.pss2.program.sceftp.MessageUtil;
import com.akuacom.pss2.richsite.event.dbp.DBPNoBidEventDataModel;
import com.akuacom.pss2.util.LogUtils;

public class EventDBPNoBidScheduleDispatcher extends AbstractEventScheduleDispatcher{
	
	private static final long serialVersionUID = -3864006958963444083L;
	private static final Logger log = Logger.getLogger(EventDBPNoBidScheduleDispatcher.class.getName());

	/**
	 * Function for get JSF presentation layer request and handler
	 * 
	 * @param eventDataModel
	 * @param manager
	 * @return
	 */
	public String dispatchEvent(EventDataModel eventDataModel,EventDataModelManager manager) {
		try {
			if (eventDataModel instanceof DBPNoBidEventDataModel) {
				File file = ((DBPNoBidEventDataModel) eventDataModel).getFiles().get(0);
				String filename=((DBPNoBidEventDataModel) eventDataModel).getUploadFileName();
				InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
				BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
				StringBuilder sb = new StringBuilder();
				String line;
				while ((line = in.readLine()) != null) {
					sb.append(line);
					sb.append('\n');
				}
				
				DBPProgramManager programManager = EJB3Factory.getLocalBean(DBPProgramManager.class);
				programManager.createEvent(filename, sb.toString());
				
				eventDataModel.setRenewFlag(true);
				manager.setFlag_GoToParent(true);
				return manager.goToEventDisplayListPage();
			} 
		} catch (IOException e) {
			manager.addMsgError("Failed to read upload file: "+e.getMessage());
			log.error(LogUtils.createExceptionLogEntry("DBP DA", LogUtils.CATAGORY_EVENT, e));
		} catch (Exception e) {
			
			manager.addMsgError("Failed to create DBP event: ");
			List<String> messages=MessageUtil.getDisplayMessage(e);
			for (String message:messages) {
				manager.addMsgError(message);
			}
		}

		eventDataModel.setRenewFlag(false);
		return "success";
	}
	
	/**
	 * Function for save event to Database
	 * 
	 * @param eventDataModel
	 * @param manager
	 * @return
	 */
	public String submitToDB(EventDataModel eventDataModel,EventDataModelManager manager) {
		String programName ="";
		try{
			if (eventDataModel instanceof DBPNoBidEventDataModel) {
				final DBPEvent event = ((DBPNoBidEventDataModel)eventDataModel).getEvent();
				final UtilityDREvent uEvent = ((DBPNoBidEventDataModel)eventDataModel).getUtilityDREvent();
	            final Date date = new Date();
	            event.setIssuedTime(date);
	            event.setReceivedTime(date);
	            programName = event.getProgramName();
				manager.getEventManager().createEvent(event.getProgramName(), event, uEvent);
				addEventLog(true, programName, getWelcomeName(), null);
				eventDataModel.setRenewFlag(true);
				manager.setFlag_GoToParent(true);
				return manager.goToEventDisplayListPage();
			}else{
				throw new Exception("Not pass the right event data object");
			}
		}catch (Exception e) {
			final String s = ErrorUtil.getErrorMessage(e);
			manager.addMsgError(s);
//			log.error("pss2.event.create.creationError: " + s);
			addEventLog(false, programName, getWelcomeName(), e);
			return "success";
		}
	}
}