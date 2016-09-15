
package com.akuacom.pss2.drw.jsf;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import com.akuacom.pss2.core.ValidationException;
import com.akuacom.pss2.drw.constant.DRWConstants;
import com.akuacom.pss2.drw.core.Program;
import com.akuacom.pss2.drw.jsf.event.BaseEventGroupBackingBean;
import com.akuacom.pss2.drw.jsf.event.RTPEventGroupBackingBean;
import com.akuacom.pss2.drw.model.EventCache;
import com.akuacom.pss2.drw.model.RTPEventDataModel;
import com.akuacom.pss2.drw.service.impl.EventManagerImpl;
import com.akuacom.pss2.drw.service.impl.StateMachineManagerImpl;
import com.akuacom.utils.DateUtil;


public class HistoryUIBackingBean implements Serializable {

	private static final long serialVersionUID = -3052943798984569813L;
	
	/** history SAI/CBP/DBP/DRC events wrapper backing bean */
	
	private BaseEventGroupBackingBean historyBaseEvents = new BaseEventGroupBackingBean();
	
//	/** history SDP events wrapper backing bean */
//	private SDPEventGroupBackingBean historySDPEvents = new SDPEventGroupBackingBean();
//	
//	/** history BIP events wrapper backing bean */
//	private BIPEventGroupBackingBean historyBIPEvents = new BIPEventGroupBackingBean();
//	
//	/** history API events wrapper backing bean */
//	private APIEventGroupBackingBean historyAPIEvents = new APIEventGroupBackingBean();
	
	/** history RTP events wrapper backing bean */
	private RTPEventGroupBackingBean historyRTPEvents = new RTPEventGroupBackingBean(new ArrayList<RTPEventDataModel>());
	
	/** current select program class */
	private String currentSelectProgram;
	
	/** current select long program name */
	private String currentSelectProduct;
	
	/** search start date */
	private Date startDate;
	
	/** search end date */
	private Date endDate;
	
	/** search restrict begin date */
	private Date restrictDateBegin;
	
	/** search restrict end date */
	private Date restrictDateEnd;
	
	/** search zip code */
	private String zipCode;
	
	/** program class items */
	private List<SelectItem> programItems;

	/** program long name items */
	private List<SelectItem> productItems;

	/** history search results total size */
	private int historyEventsTotalSize = 0;

	/** current scroller page */
	private int scrollerPage = 1;

	/** history search results total results */
	private int totalResults = 0;

	/** current paging result index from */
	private int resultIndexFrom = 0;

	/** current paging result index to */
	private int resultIndexTo = 0;
	
	private boolean datascrollerDispalyFlag = true;
	private boolean exportCSVDispalyFlag = true;

	private String dateFormat;
	
	private boolean zipcodeFlag= false;
	
	/** history RTP events flag */
	private boolean historyRTPEventsFlag = false;

	/** history result visible flag */
	private boolean historyResultsVisibleFlag = false;

	/** history result validate error flag */
	private boolean historyResultsValidateErrorFlag = false;
	
	private String displaySearchDataScroller = "";
	
	/** history event search title */
	private String historyEventSearchTitle = "";
	
	private boolean blockColumnDisplayFlag = false;
	
	public void initialize(){
		initializeHistoryEventSearch();
	}

	
	/**
	 * Function for initialize the event search component content
	 */
	public void initializeHistoryEventSearch() {

		programItems = new ArrayList<SelectItem>();
		programItems.add(new SelectItem("Select", "Select"));
		programItems.add(new SelectItem(DRWConstants.HISTORY_PROGRAM_DISPLAY_NAME_API,DRWConstants.HISTORY_PROGRAM_DISPLAY_NAME_API));
		programItems.add(new SelectItem(DRWConstants.HISTORY_PROGRAM_DISPLAY_NAME_CBP,DRWConstants.HISTORY_PROGRAM_DISPLAY_NAME_CBP));
		programItems.add(new SelectItem(DRWConstants.HISTORY_PROGRAM_DISPLAY_NAME_DBP,DRWConstants.HISTORY_PROGRAM_DISPLAY_NAME_DBP));
		if(DRWConstants.DRC_ENABLE){
			programItems.add(new SelectItem(DRWConstants.HISTORY_PROGRAM_DISPLAY_NAME_DRC,DRWConstants.HISTORY_PROGRAM_DISPLAY_NAME_DRC));	
		}
		programItems.add(new SelectItem(DRWConstants.HISTORY_PROGRAM_DISPLAY_NAME_RTP,DRWConstants.HISTORY_PROGRAM_DISPLAY_NAME_RTP));
		programItems.add(new SelectItem(DRWConstants.HISTORY_PROGRAM_DISPLAY_NAME_SAI,DRWConstants.HISTORY_PROGRAM_DISPLAY_NAME_SAI));
//		programItems.add(new SelectItem(DRWConstants.PROGRAM_CLASS_NAME_SDP_C,DRWConstants.PROGRAM_CLASS_NAME_SDP_C));
//		programItems.add(new SelectItem(DRWConstants.PROGRAM_CLASS_NAME_SDP_R,DRWConstants.PROGRAM_CLASS_NAME_SDP_R));
		programItems.add(new SelectItem(DRWConstants.HISTORY_PROGRAM_DISPLAY_NAME_SDP,DRWConstants.HISTORY_PROGRAM_DISPLAY_NAME_SDP));
		programItems.add(new SelectItem(DRWConstants.HISTORY_PROGRAM_DISPLAY_NAME_SPD,DRWConstants.HISTORY_PROGRAM_DISPLAY_NAME_SPD));
		programItems.add(new SelectItem(DRWConstants.HISTORY_PROGRAM_DISPLAY_NAME_BIP,DRWConstants.HISTORY_PROGRAM_DISPLAY_NAME_BIP));
		
		productItems = new ArrayList<SelectItem>();
		productItems.add(new SelectItem("Select", "Select"));

		historyRTPEventsFlag = false;
		historyResultsVisibleFlag = false;
		historyResultsValidateErrorFlag = false;
	}
	
	public void programNameChange(ValueChangeEvent event) {
		if ((event.getOldValue() == null) || (!event.getOldValue().equals(event.getNewValue()))) {
			String programValue = event.getNewValue().toString();
			if(!StateMachineManagerImpl.getInstance().isGetProgramsSuccessFlag()){
				List<Program> programList = EventManagerImpl.getInstance().retrieveAllPrograms();
				EventCache.getInstance().setProgramsCache(programList);
			}
			productItems = new ArrayList<SelectItem>();
			productItems.add(new SelectItem("Select", "Select"));
			productItems.add(new SelectItem("All", "All"));
			zipcodeFlag = false;
			if (DRWConstants.HISTORY_PROGRAM_DISPLAY_NAME_API.equalsIgnoreCase(programValue)) {
				zipcodeFlag = true;
				for (String programLongName : EventCache.getInstance().getProductAPIList()) {
					productItems.add(new SelectItem(programLongName,programLongName));
				}
			}
			if (DRWConstants.HISTORY_PROGRAM_DISPLAY_NAME_BIP.equalsIgnoreCase(programValue)) {
				//DRMS-7508
				zipcodeFlag = true;
				productItems.add(new SelectItem("BIP","BIP"));
//				for (String programLongName : EventCache.getInstance().getProductBIPList()) {
//					productItems.add(new SelectItem(programLongName,programLongName));
//				}
			}
			if (DRWConstants.HISTORY_PROGRAM_DISPLAY_NAME_CBP.equalsIgnoreCase(programValue)) {
				zipcodeFlag = true;
				for (String programLongName : EventCache.getInstance().getProductCBPList()) {
					productItems.add(new SelectItem(programLongName,programLongName));
				}
			}
			if (DRWConstants.HISTORY_PROGRAM_DISPLAY_NAME_DBP.equalsIgnoreCase(programValue)) {
				for (String programLongName : removeDuplicateElements(EventCache.getInstance().getProductDBPList())) {
					productItems.add(new SelectItem(programLongName,programLongName));
				}
			}
			if (DRWConstants.HISTORY_PROGRAM_DISPLAY_NAME_DRC.equalsIgnoreCase(programValue)) {
				for (String programLongName :EventCache.getInstance().getProductDRCList()) {
					productItems.add(new SelectItem(programLongName,programLongName));
				}
			}
			if (DRWConstants.HISTORY_PROGRAM_DISPLAY_NAME_RTP.equalsIgnoreCase(programValue)) {
				productItems.add(new SelectItem("RTP","RTP"));
			}
			if (DRWConstants.HISTORY_PROGRAM_DISPLAY_NAME_SAI.equalsIgnoreCase(programValue)) {
				productItems.add(new SelectItem("Residential","Residential"));
				productItems.add(new SelectItem("Commercial","Commercial"));
			}
			if (DRWConstants.HISTORY_PROGRAM_DISPLAY_NAME_SDP.equalsIgnoreCase(programValue)) {
				zipcodeFlag = true;
				for (String programLongName : EventCache.getInstance().getProductSDPList()) {
					productItems.add(new SelectItem(programLongName,programLongName));
				}
			}
			if (DRWConstants.HISTORY_PROGRAM_DISPLAY_NAME_SPD.equalsIgnoreCase(programValue)) {
				productItems.add(new SelectItem("SPD","SPD"));
			}
		}
	}
	
	private List<String> removeDuplicateElements(List<String> input){
		List<String> result = new ArrayList<String>();
		Set<String> set = new HashSet<String>();
		for(String key:input){
			set.add(key);
		}
		for(String key:set){
			result.add(key);
		}
		return result;
	}
	
	public void resetSearchPageParameters(){
		historyResultsVisibleFlag = true;
		historyResultsValidateErrorFlag = false;
		datascrollerDispalyFlag = true;
		setDisplaySearchDataScroller("");
		exportCSVDispalyFlag = true;
		totalResults=0;
		resultIndexFrom = 0;
		resultIndexTo = 0;
		historyEventsTotalSize = 0;
		scrollerPage = 1;
	}
	public String searchHistoryEvent() {
		try {
			resetSearchPageParameters();
			
			if (getValidationErrorSize() > 0) {
				historyRTPEventsFlag = false;
				historyResultsVisibleFlag = false;
				historyResultsValidateErrorFlag = true;
				
				datascrollerDispalyFlag = false;
				setDisplaySearchDataScroller("none");
				exportCSVDispalyFlag = false;
				
				return "";
			}

			List<ValidationException> validationErrors = EventManagerImpl.getInstance().searchHistoryEvent(this);
			if (validationErrors.size() > 0) {
				historyRTPEventsFlag = false;
				historyResultsVisibleFlag = false;
				historyResultsValidateErrorFlag = true;
				FacesContext facesContext = FacesContext.getCurrentInstance();
				for (ValidationException e : validationErrors) {
					if(e.getMessage().equalsIgnoreCase("VALIDATION_ERROR_INPUT_PARAMETER_EMPTY")){
						facesContext.addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please select a Product", "Please select a Product"));
						facesContext.addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please select a Program", "Please select a Program"));
					}else{
						facesContext.addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage()));
					}
					
				}
			}else{
				if(totalResults>0){
					exportCSVDispalyFlag = true;
					if(totalResults<=50){
						datascrollerDispalyFlag = false;
						setDisplaySearchDataScroller("none");
					}else{
						datascrollerDispalyFlag = true;
						setDisplaySearchDataScroller("");
					}
				}else{
					exportCSVDispalyFlag = false;
					datascrollerDispalyFlag = false;
					setDisplaySearchDataScroller("none");
				}
			}
		} catch (Exception e) {
			historyRTPEventsFlag = false;
			historyResultsVisibleFlag = false;
			historyResultsValidateErrorFlag = true;
			datascrollerDispalyFlag = false;
			setDisplaySearchDataScroller("none");
			exportCSVDispalyFlag = false;
			FacesContext facesContext = FacesContext.getCurrentInstance();
			facesContext.addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage()));
		}
		return "";
	}	
	public String clearAll() {
		initializeHistoryEventSearch();
		resetSearchPageParameters();
		startDate=null;
		endDate=null;
		zipCode="";
		currentSelectProgram="";
		currentSelectProduct="";
		historyResultsVisibleFlag=false;
		zipcodeFlag=false;
		return "";
	}	
	public String addValidationErrorMessage(String errorMessage) {
		historyRTPEventsFlag = false;
		historyResultsVisibleFlag = false;
		historyResultsValidateErrorFlag = true;
		FacesContext facesContext = FacesContext.getCurrentInstance();
		facesContext.addMessage(null, new FacesMessage(
				FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));
		return "";
	}

	public int getValidationErrorSize() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		Iterator<FacesMessage> messages = facesContext.getMessages();
		int result = 0;
		while (messages.hasNext()) {
			messages.next();
			result++;
		}
		return result;
	}
	
	public void exportCSV_EventHistory_Normal() throws IOException {

		EventManagerImpl.getInstance().exportCSV_EventHistory_Normal(this);

	}

	public void exportCSV_EventHistory_RTP() throws IOException {
		EventManagerImpl.getInstance().exportCSV_EventHistory_RTP(this);
	}
	public void validateInputCalendar(FacesContext context,
			UIComponent component, Object value) throws Exception {

	}
	/**
	 * @return the historyBaseEvents
	 */
	public BaseEventGroupBackingBean getHistoryBaseEvents() {
		return historyBaseEvents;
	}

	/**
	 * @param historyBaseEvents the historyBaseEvents to set
	 */
	public void setHistoryBaseEvents(BaseEventGroupBackingBean historyBaseEvents) {
		this.historyBaseEvents = historyBaseEvents;
	}

//	/**
//	 * @return the historySDPEvents
//	 */
//	public SDPEventGroupBackingBean getHistorySDPEvents() {
//		return historySDPEvents;
//	}
//
//	/**
//	 * @param historySDPEvents the historySDPEvents to set
//	 */
//	public void setHistorySDPEvents(SDPEventGroupBackingBean historySDPEvents) {
//		this.historySDPEvents = historySDPEvents;
//	}
//
//	/**
//	 * @return the historyBIPEvents
//	 */
//	public BIPEventGroupBackingBean getHistoryBIPEvents() {
//		return historyBIPEvents;
//	}
//
//	/**
//	 * @param historyBIPEvents the historyBIPEvents to set
//	 */
//	public void setHistoryBIPEvents(BIPEventGroupBackingBean historyBIPEvents) {
//		this.historyBIPEvents = historyBIPEvents;
//	}
//
//	/**
//	 * @return the historyAPIEvents
//	 */
//	public APIEventGroupBackingBean getHistoryAPIEvents() {
//		return historyAPIEvents;
//	}
//
//	/**
//	 * @param historyAPIEvents the historyAPIEvents to set
//	 */
//	public void setHistoryAPIEvents(APIEventGroupBackingBean historyAPIEvents) {
//		this.historyAPIEvents = historyAPIEvents;
//	}

	/**
	 * @return the historyRTPEvents
	 */
	public RTPEventGroupBackingBean getHistoryRTPEvents() {
		return historyRTPEvents;
	}

	/**
	 * @param historyRTPEvents the historyRTPEvents to set
	 */
	public void setHistoryRTPEvents(RTPEventGroupBackingBean historyRTPEvents) {
		this.historyRTPEvents = historyRTPEvents;
	}

	/**
	 * @return the currentSelectProgram
	 */
	public String getCurrentSelectProgram() {
		return currentSelectProgram;
	}

	/**
	 * @param currentSelectProgram the currentSelectProgram to set
	 */
	public void setCurrentSelectProgram(String currentSelectProgram) {
		this.currentSelectProgram = currentSelectProgram;
	}

	/**
	 * @return the currentSelectProduct
	 */
	public String getCurrentSelectProduct() {
		return currentSelectProduct;
	}

	/**
	 * @param currentSelectProduct the currentSelectProduct to set
	 */
	public void setCurrentSelectProduct(String currentSelectProduct) {
		this.currentSelectProduct = currentSelectProduct;
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
		if(startDate!=null){
			startDate = DateUtil.getStartOfDay(startDate);	
		}
		
		this.startDate = startDate;
	}

	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		if(endDate!=null){
			endDate = DateUtil.getEndOfDay(endDate);	
		}
		
		return endDate;
	}

	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return the restrictDateBegin
	 */
	public Date getRestrictDateBegin() {
		return restrictDateBegin;
	}

	/**
	 * @param restrictDateBegin the restrictDateBegin to set
	 */
	public void setRestrictDateBegin(Date restrictDateBegin) {
		this.restrictDateBegin = restrictDateBegin;
	}

	/**
	 * @return the restrictDateEnd
	 */
	public Date getRestrictDateEnd() {
		return restrictDateEnd;
	}

	/**
	 * @param restrictDateEnd the restrictDateEnd to set
	 */
	public void setRestrictDateEnd(Date restrictDateEnd) {
		this.restrictDateEnd = restrictDateEnd;
	}

	/**
	 * @return the zipCode
	 */
	public String getZipCode() {
		return zipCode;
	}

	/**
	 * @param zipCode the zipCode to set
	 */
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	/**
	 * @return the programItems
	 */
	public List<SelectItem> getProgramItems() {
		return programItems;
	}

	/**
	 * @param programItems the programItems to set
	 */
	public void setProgramItems(List<SelectItem> programItems) {
		this.programItems = programItems;
	}

	/**
	 * @return the productItems
	 */
	public List<SelectItem> getProductItems() {
		return productItems;
	}

	/**
	 * @param productItems the productItems to set
	 */
	public void setProductItems(List<SelectItem> productItems) {
		this.productItems = productItems;
	}

	/**
	 * @return the historyEventsTotalSize
	 */
	public int getHistoryEventsTotalSize() {
		return historyEventsTotalSize;
	}

	/**
	 * @param historyEventsTotalSize the historyEventsTotalSize to set
	 */
	public void setHistoryEventsTotalSize(int historyEventsTotalSize) {
		this.historyEventsTotalSize = historyEventsTotalSize;
	}

	/**
	 * @return the scrollerPage
	 */
	public int getScrollerPage() {
		return scrollerPage;
	}

	/**
	 * @param scrollerPage the scrollerPage to set
	 */
	public void setScrollerPage(int scrollerPage) {
		this.scrollerPage = scrollerPage;
	}

	/**
	 * @return the totalResults
	 */
	public int getTotalResults() {
		return totalResults;
	}

	/**
	 * @param totalResults the totalResults to set
	 */
	public void setTotalResults(int totalResults) {
		this.totalResults = totalResults;
	}

	/**
	 * @return the resultIndexFrom
	 */
	public int getResultIndexFrom() {
		return resultIndexFrom;
	}

	/**
	 * @param resultIndexFrom the resultIndexFrom to set
	 */
	public void setResultIndexFrom(int resultIndexFrom) {
		this.resultIndexFrom = resultIndexFrom;
	}

	/**
	 * @return the resultIndexTo
	 */
	public int getResultIndexTo() {
		return resultIndexTo;
	}

	/**
	 * @param resultIndexTo the resultIndexTo to set
	 */
	public void setResultIndexTo(int resultIndexTo) {
		this.resultIndexTo = resultIndexTo;
	}

	/**
	 * @return the datascrollerDispalyFlag
	 */
	public boolean isDatascrollerDispalyFlag() {
		return datascrollerDispalyFlag;
	}

	/**
	 * @param datascrollerDispalyFlag the datascrollerDispalyFlag to set
	 */
	public void setDatascrollerDispalyFlag(boolean datascrollerDispalyFlag) {
		this.datascrollerDispalyFlag = datascrollerDispalyFlag;
	}

	/**
	 * @return the exportCSVDispalyFlag
	 */
	public boolean isExportCSVDispalyFlag() {
		return exportCSVDispalyFlag;
	}

	/**
	 * @param exportCSVDispalyFlag the exportCSVDispalyFlag to set
	 */
	public void setExportCSVDispalyFlag(boolean exportCSVDispalyFlag) {
		this.exportCSVDispalyFlag = exportCSVDispalyFlag;
	}

	/**
	 * @return the dateFormat
	 */
	public String getDateFormat() {
		return dateFormat;
	}

	/**
	 * @param dateFormat the dateFormat to set
	 */
	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}
	/**
	 * @return the historyRTPEventsFlag
	 */
	public boolean isHistoryRTPEventsFlag() {
		return historyRTPEventsFlag;
	}
	/**
	 * @param historyRTPEventsFlag the historyRTPEventsFlag to set
	 */
	public void setHistoryRTPEventsFlag(boolean historyRTPEventsFlag) {
		this.historyRTPEventsFlag = historyRTPEventsFlag;
	}
	/**
	 * @return the historyResultsVisibleFlag
	 */
	public boolean isHistoryResultsVisibleFlag() {
		return historyResultsVisibleFlag;
	}
	/**
	 * @param historyResultsVisibleFlag the historyResultsVisibleFlag to set
	 */
	public void setHistoryResultsVisibleFlag(boolean historyResultsVisibleFlag) {
		this.historyResultsVisibleFlag = historyResultsVisibleFlag;
	}
	/**
	 * @return the historyResultsValidateErrorFlag
	 */
	public boolean isHistoryResultsValidateErrorFlag() {
		return historyResultsValidateErrorFlag;
	}
	/**
	 * @param historyResultsValidateErrorFlag the historyResultsValidateErrorFlag to set
	 */
	public void setHistoryResultsValidateErrorFlag(
			boolean historyResultsValidateErrorFlag) {
		this.historyResultsValidateErrorFlag = historyResultsValidateErrorFlag;
	}
	/**
	 * @return the displaySearchDataScroller
	 */
	public String getDisplaySearchDataScroller() {
		return displaySearchDataScroller;
	}
	/**
	 * @param displaySearchDataScroller the displaySearchDataScroller to set
	 */
	public void setDisplaySearchDataScroller(String displaySearchDataScroller) {
		this.displaySearchDataScroller = displaySearchDataScroller;
	}
	/**
	 * @return the historyEventSearchTitle
	 */
	public String getHistoryEventSearchTitle() {
		return historyEventSearchTitle;
	}
	/**
	 * @param historyEventSearchTitle the historyEventSearchTitle to set
	 */
	public void setHistoryEventSearchTitle(String historyEventSearchTitle) {
		this.historyEventSearchTitle = historyEventSearchTitle;
	}
	/**
	 * @return the blockColumnDisplayFlag
	 */
	public boolean isBlockColumnDisplayFlag() {
		return blockColumnDisplayFlag;
	}
	/**
	 * @param blockColumnDisplayFlag the blockColumnDisplayFlag to set
	 */
	public void setBlockColumnDisplayFlag(boolean blockColumnDisplayFlag) {
		this.blockColumnDisplayFlag = blockColumnDisplayFlag;
	}
	/**
	 * @param zipcodeFlag the zipcodeFlag to set
	 */
	public void setZipcodeFlag(boolean zipcodeFlag) {
		this.zipcodeFlag = zipcodeFlag;
	}
	/**
	 * @return the zipcodeFlag
	 */
	public boolean isZipcodeFlag() {
		return zipcodeFlag;
	}
	
	
	
}
