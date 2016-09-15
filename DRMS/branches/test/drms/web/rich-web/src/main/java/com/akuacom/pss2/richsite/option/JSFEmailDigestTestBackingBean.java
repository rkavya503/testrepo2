package com.akuacom.pss2.richsite.option;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.akuacom.ejb.client.EJB3Factory;
import com.akuacom.pss2.email.ClientTestEmailManager;
import com.akuacom.pss2.email.MessageEntity;
import com.akuacom.utils.DateUtil;

import com.akuacom.pss2.email.JSFClientTestEmailReportEntity;
//import com.akuacom.pss2.program.interruptible.InterruptibleProgramManager;
import com.akuacom.pss2.system.SystemManager;
public class JSFEmailDigestTestBackingBean implements Serializable{

	private static ClientTestEmailManager clientTestEmailManager = EJB3Factory.getBean(ClientTestEmailManager.class);
//	private static InterruptibleProgramManager interruptibleProgramManager = EJB3Factory.getBean(InterruptibleProgramManager.class);
	private static SystemManager systemManager = EJB3Factory.getBean(SystemManager.class);
	private static final long serialVersionUID = -9140763882155882896L;
	private List<JSFClientTestEmailReportEntity> results= new ArrayList<JSFClientTestEmailReportEntity>();
	private Date startDate;
	private Date endDate;
	
	private String fileName;
	private String fileContext;
	
	public void autoDispatchAction(){
//		interruptibleProgramManager.dispatch(fileName, fileContext);
	}
	
	public void digestAction(){
		if(startDate!=null&&endDate!=null){
			startDate = DateUtil.getStartOfDay(startDate);
			endDate = DateUtil.getEndOfDay(endDate);
			clientTestEmailManager.digestClientTestEmail(startDate, endDate);
//			retrieveDigestAction();
		}
	}

	public void digestAction2(){
		int interval = (int) systemManager.getPss2Properties().getClientTestEmailConsolidationInterval();
		if(interval>90||interval<1){
			interval = 1;
		}
		Date from = com.akuacom.utils.lang.DateUtil.getDate(DateUtil.getStartOfDay(new Date()), -interval);
		Date to = com.akuacom.utils.lang.DateUtil.getDate(DateUtil.getEndOfDay(new Date()), -1);
		clientTestEmailManager.digestClientTestEmail(from, to);
	}
	
	public void retrieveDigestAction(){
		results.clear();
		if(startDate!=null&&endDate!=null){
			startDate = DateUtil.getStartOfDay(startDate);
			endDate = DateUtil.getEndOfDay(endDate);
			List<MessageEntity> messages = clientTestEmailManager.findDigestMessage(startDate, endDate, null);
			for(MessageEntity message:messages){
				JSFClientTestEmailReportEntity instance = JSFClientTestEmailReportEntity.transfer(message);
				results.add(instance);
			}
		}
	}

	/**
	 * @return the clientTestEmailManager
	 */
	public static ClientTestEmailManager getClientTestEmailManager() {
		return clientTestEmailManager;
	}

	/**
	 * @param clientTestEmailManager the clientTestEmailManager to set
	 */
	public static void setClientTestEmailManager(
			ClientTestEmailManager clientTestEmailManager) {
		JSFEmailDigestTestBackingBean.clientTestEmailManager = clientTestEmailManager;
	}

	/**
	 * @return the results
	 */
	public List<JSFClientTestEmailReportEntity> getResults() {
		return results;
	}

	/**
	 * @param results the results to set
	 */
	public void setResults(List<JSFClientTestEmailReportEntity> results) {
		this.results = results;
	}

	/**
	 * @return the startDate
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return the fileContext
	 */
	public String getFileContext() {
		return fileContext;
	}

	/**
	 * @param fileContext the fileContext to set
	 */
	public void setFileContext(String fileContext) {
		this.fileContext = fileContext;
	}

}
