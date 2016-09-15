package com.akuacom.pss2.richsite.program.configure;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.apache.log4j.Logger;

import com.akuacom.ejb.client.EJB3Factory;
import com.akuacom.pss2.program.Program;
import com.akuacom.pss2.program.dl.DemandLimitingProgram;
import com.akuacom.pss2.richsite.program.configure.bid.BidConfigureDataModel;
import com.akuacom.pss2.richsite.program.configure.factory.ProgramConfigureFactory;
import com.akuacom.pss2.richsite.program.configure.mode.ModeConfigureDataModel;
import com.akuacom.pss2.richsite.program.configure.rtp.RTPConfigureDataModel;
import com.akuacom.pss2.richsite.program.configure.rules.RulesConfigureDataModel;
import com.akuacom.pss2.richsite.program.configure.season.SeasonConfigureDataModel;
import com.akuacom.pss2.richsite.program.configure.signal.SignalConfigureDataModel;
import com.akuacom.pss2.richsite.util.AkuacomJSFUtil;
import com.akuacom.pss2.system.SystemManager;
import com.akuacom.pss2.util.DrasRole;


public class ProgramConfigureDataModel implements Serializable {



	private static final long serialVersionUID = 5960548789729826992L;
	/** Program template constants */
	public static final String PROGRAM_TEMPLATE_LABEL_CPP = "cpp.CPPProgramEJB";
	public static final String PROGRAM_TEMPLATE_LABEL_DBP_NO_BID = "dbp.DBPNoBidProgramEJB";
	public static final String PROGRAM_TEMPLATE_LABEL_DBP = "dbp.DBPBidProgramEJB";
	public static final String PROGRAM_TEMPLATE_LABEL_RTP = "scertp.SCERTPProgramEJB";
	public static final String PROGRAM_TEMPLATE_LABEL_RTP2013 = "scertp2013.SCERTPProgramEJB2013";
	public static final String PROGRAM_TEMPLATE_LABEL_DEMO = "demo.DemoProgramEJB";
	public static final String PROGRAM_TEMPLATE_LABEL_CBP = "cbp.CBPProgramEJB";

	/** Program implementation class constants */
	public static final String PROGRAM_IMPLEMENTATION_CLASS_CPP = "com.akuacom.pss2.program.cpp.CPPProgramEJB";
	public static final String PROGRAM_IMPLEMENTATION_CLASS_DBP_NO_BID = "com.akuacom.pss2.program.dbp.DBPNoBidProgramEJB";
	public static final String PROGRAM_IMPLEMENTATION_CLASS_DBP = "com.akuacom.pss2.program.dbp.DBPBidProgramEJB";
	public static final String PROGRAM_IMPLEMENTATION_CLASS_ISSUE = "com.akuacom.pss2.program.cpp.CPPProgramEJB";
	public static final String PROGRAM_IMPLEMENTATION_CLASS_DEMO = "com.akuacom.pss2.program.demo.DemoProgramEJB";
	public static final String PROGRAM_IMPLEMENTATION_CLASS_TEST = "com.akuacom.pss2.program.testProgram.TestProgramEJB";
	public static final String PROGRAM_IMPLEMENTATION_CLASS_RTP = "com.akuacom.pss2.program.scertp.SCERTPProgramEJB";
	public static final String PROGRAM_IMPLEMENTATION_CLASS_RTP2013 = "com.akuacom.pss2.program.scertp2013.SCERTPProgramEJB2013";
	public static final String PROGRAM_IMPLEMENTATION_CLASS_CBP = "com.akuacom.pss2.program.cbp.CBPProgramEJB";

	/** Program UI schedule page constants */
	public static final String PROGRAM_SCHEDULE_PAGE_CPP = "CPPSchedulePage";
	public static final String PROGRAM_SCHEDULE_PAGE_DBP_NO_BID = "DBPNoBidSchedulePage";
	public static final String PROGRAM_SCHEDULE_PAGE_DBP = "DBPSchedulePage";
	public static final String PROGRAM_SCHEDULE_PAGE_ISSUE = "IssueSchedulePage";
	public static final String PROGRAM_SCHEDULE_PAGE_DEMO = "DemoSchedulePage";
	public static final String PROGRAM_SCHEDULE_PAGE_TEST = "TestSchedulePage";
	public static final String PROGRAM_SCHEDULE_PAGE_RTP_VERSION_6_2 = "ButtonOnlySchedulePage";
	public static final String PROGRAM_SCHEDULE_PAGE_RTP_VERSION_6_6 = "SCERTPSchedulePage";

	/** Program validation class constants */
	public static final String PROGRAM_VALIDATION_CLASS_CPP = "com.akuacom.pss2.program.cpp.CPPValidator";
	public static final String PROGRAM_VALIDATION_CLASS_DBP_NO_BID = "com.akuacom.pss2.program.dbp.DBPNoBidValidator";
	public static final String PROGRAM_VALIDATION_CLASS_DBP = "com.akuacom.pss2.program.dbp.DBPBidValidator";
	public static final String PROGRAM_VALIDATION_CLASS_ISSUE = "com.akuacom.pss2.program.cpp.CPPValidator";
	public static final String PROGRAM_VALIDATION_CLASS_DEMO = "com.akuacom.pss2.program.cpp.CPPValidator";
	public static final String PROGRAM_VALIDATION_CLASS_TEST = "com.akuacom.pss2.program.cpp.CPPValidator";
	public static final String PROGRAM_VALIDATION_CLASS_RTP = "com.akuacom.pss2.program.scertp.SCERTPValidator";

	/** Defined program page address for faces-config.xml*/
	public static final String ProgramPageUrl = "ProgramPageUrl";

	/** Program add operation flag */
	public static final String OPERATION_FLAG_ADD = "ADD";

	/** Program update operation flag */
	public static final String OPERATION_FLAG_UPDATE = "UPDATE";

	/** Program add title */
	public static final String TITLE_PROGRAM_ADD = "Create Program";

	/** Program update title */
	public static final String TITLE_PROGRAM_UPDATE = "Edit Program";
	/** Program update title */
	public static final String TITLE_PROGRAM_View = "View Program";
	/** Program add page URL */
	public static final String ProgramAddPageUrl = "ProgramAddPageUrl";

	/** Program update page URL */
	public static final String ProgramUpdatePageUrl = "ProgramUpdatePageUrl";

	/** Demand Limiting Program settings URI */
	public static final String DEMAND_LIMITING_SETTINGS_URI = "demandLimitingSettings";

	/** The log */
	private static final Logger log = Logger.getLogger(ProgramConfigureDataModel.class.getName());

	/** ProgramConfigureDataModelManager reference */
	private ProgramConfigureDataModelManager manager =ProgramConfigureFactory.getInstance().getProgramConfigureDataModelManager();

	/** ADD operation boolean flag*/
	private boolean addFlag=true;

	/** The renewFlag. */
	private boolean renewFlag = true;

	/** SignalConfigureDataModel instance reference*/
	private SignalConfigureDataModel signalConfigureDataModel;

	/** ModeConfigureDataModel instance reference*/
	private ModeConfigureDataModel modeConfigureDataModel;

	/** BidConfigureDataModel instance reference*/
	private BidConfigureDataModel bidConfigureDataModel;

	/** SeasonConfigureDataModel instance reference*/
	private SeasonConfigureDataModel seasonConfigureDataModel;

	/** RTPConfigureDataModel instance reference*/
	private RTPConfigureDataModel rtpConfigureDataModel;

    /** RulesDataModel instance reference*/
	private RulesConfigureDataModel ruleConfigureDataModel;

	/** Program operate flag */
	private String operationFlag=OPERATION_FLAG_ADD; //ADD or UPDATE

	/** Program template list */
	private List<SelectItem> programTemplateList = new ArrayList<SelectItem>();

	/** Current selected program template .The value is selectItem value, it's program class, the display value is program template label*/
	private String selectedProgramTemplate;

	/** Current selected program schedule page */
	private String currentProgramSechdulePage="CPPSchedulePage";

	/** Current selected program validation class */
	private String currentProgramValidationClass=PROGRAM_VALIDATION_CLASS_CPP;

	/**Program template edit flag, in the program add page it should be true, otherwise it should be false*/
	private boolean programTemplateEditableFlag = true;

	/**Program name edit flag, in the program add page it should be true, otherwise it should be false*/
	private boolean programNameEditableFlag=true;

	/** Program entity reference*/
	private Program program;

	/** program name*/
	private String programName="";

	/** maxIssueTime use for build up program attributes maxIssueTimeH and maxIssueTimeM*/
	private String maxIssueTimeString="";

	/** minStartTime use for build up program attributes minStartTimeH and minStartTimeM*/
	private String minStartTimeString="";

	/** maxStartTime use for build up program attributes maxStartTimeH and maxStartTimeM*/
	private String maxStartTimeString="";

	/** minEndTime use for build up program attributes minEndTimeH and minEndTimeM*/
	private String minEndTimeString="";

	/** maxEndTime use for build up program attributes maxEndTimeH and maxEndTimeM*/
	private String maxEndTimeString="";

	/** pendingTimeDBE use for build up program attributes pendingTimeDBEH and pendingTimeDBEM*/
	private String pendingTimeDBEString="";

	/** should the tab for RTP pricing be displayed */
	private boolean displayFlagRTP = false;

	private String titleOfTheUI=TITLE_PROGRAM_ADD;

	private boolean setDefaultDuration=false;
	
    //private String weatherTimerInfo;

    private Date autoRepeatTimeOfDay = new Date();
    private String autoRepeatTimeOfDayStr ;

    private boolean saveFlag = true;

    private String dateFormat;
    
	// ---------------------------------------------Business logic function--------------------------------------

	/**
	 * Function for save program operation
	 */
	public String saveProgramConfigure(){
		try{
			manager.saveProgramConfigure(this);

			return dispatchToProgramPage();
		}catch(Exception e){
			setRenewFlag(false);
			AkuacomJSFUtil.addErrorMessage(e.getMessage());
			return "";
		}
	}
	
	/**
	 * Function for save main program properties  operation
	 */
	public String saveMainProgram(){
		String operationFlag = this.getOperationFlag();
		if(operationFlag.equalsIgnoreCase(ProgramConfigureDataModel.OPERATION_FLAG_ADD)){
			
			return saveProgramConfigure();
		}
		
		try{
			manager.saveMainProgram(this);

			return dispatchToProgramPage();
		}catch(Exception e){
			setRenewFlag(false);
			AkuacomJSFUtil.addErrorMessage(e.getMessage());
			return "";
		}
	}
	
	/**
	 * Function for save program operation
	 */
	public String saveProgramSignalsConfigure(){
		String operationFlag = this.getOperationFlag();
		if(operationFlag.equalsIgnoreCase(ProgramConfigureDataModel.OPERATION_FLAG_ADD)){
			
			return saveProgramConfigure();
		}
		
		try{
			manager.saveProgramSignalsConfigure(this);

			return dispatchToProgramPage();
		}catch(Exception e){
			setRenewFlag(false);
			AkuacomJSFUtil.addErrorMessage(e.getMessage());
			return "";
		}
	}

	/**
	 * Function for dispatch to program page
	 * This function will be called at cancel or complete an the program create and edit operation.
	 */
	public String dispatchToProgramPage(){
		return ProgramPageUrl;
	}

	/**
	 * Function for get request and dispatch to program create or update page
	 * @return
	 */
	public String dispatchPage(){
        SystemManager systemManager = EJB3Factory.getBean(SystemManager.class);
        dateFormat=systemManager.getPss2Features().getDateFormat();
		
		String operationFlag = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("operationFlag");
		if(operationFlag.equalsIgnoreCase(OPERATION_FLAG_ADD)){

			initializeProgramAdd();

			return ProgramAddPageUrl;
		}else if(operationFlag.equalsIgnoreCase(OPERATION_FLAG_UPDATE)){

			initializeProgramUpdate();
			if ((this.getProgram() != null) && (this.getProgram() instanceof DemandLimitingProgram))
				return DEMAND_LIMITING_SETTINGS_URI;
			else
				return ProgramUpdatePageUrl;
		}

		return "";
	}
	/**
	 * Function for initialize the program add operation data model
	 */
	private void initializeProgramAdd(){
		this.setProgramName("");
		this.setProgram(null);
		setOperationFlag("ADD");
		addFlag=true;
		programTemplateEditableFlag = true;
		programNameEditableFlag = true;
		programTemplateList = new ArrayList<SelectItem>();
		programTemplateList.add(new SelectItem(PROGRAM_IMPLEMENTATION_CLASS_CPP,PROGRAM_TEMPLATE_LABEL_CPP));
		programTemplateList.add(new SelectItem(PROGRAM_IMPLEMENTATION_CLASS_DBP_NO_BID,PROGRAM_TEMPLATE_LABEL_DBP_NO_BID));
		programTemplateList.add(new SelectItem(PROGRAM_IMPLEMENTATION_CLASS_DBP,PROGRAM_TEMPLATE_LABEL_DBP));
		programTemplateList.add(new SelectItem(PROGRAM_IMPLEMENTATION_CLASS_DEMO,PROGRAM_TEMPLATE_LABEL_DEMO));
		programTemplateList.add(new SelectItem(PROGRAM_IMPLEMENTATION_CLASS_RTP,PROGRAM_TEMPLATE_LABEL_RTP));
		programTemplateList.add(new SelectItem(PROGRAM_IMPLEMENTATION_CLASS_RTP2013,PROGRAM_TEMPLATE_LABEL_RTP2013));
		programTemplateList.add(new SelectItem(PROGRAM_IMPLEMENTATION_CLASS_CBP,PROGRAM_TEMPLATE_LABEL_CBP));
		this.setTitleOfTheUI(TITLE_PROGRAM_ADD);

		setRtpConfigureDataModel(null);

		getSignalConfigureDataModel().buildModel(operationFlag);

        this.setRuleConfigureDataModel(null);
        AkuacomJSFUtil.setProgram(new Program());
	}

	/**
	 * Function for initialize the program update operation data model
	 */
	private void initializeProgramUpdate(){
		String programName = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("programName");
		setProgramName(programName);
		this.setProgram(null);
		setOperationFlag("UPDATE");
		addFlag=false;
		titleOfTheUI=TITLE_PROGRAM_UPDATE;
		FacesContext facesContext = FacesContext.getCurrentInstance();

	   	if (facesContext.getExternalContext().isUserInRole(DrasRole.Operator.toString()))
	   	{
	   		titleOfTheUI=TITLE_PROGRAM_View;
	   	}
		
		programTemplateEditableFlag = false;
		programNameEditableFlag = false;
		selectedProgramTemplate="";
		currentProgramSechdulePage="";
		currentProgramValidationClass="";
		manager.getProgramConfigure(this);
		buildProgramConfigure(this.getProgram());//page1 program
		//getSignalConfigureDataModel().buildModel(operationFlag);//page2 signal connect to db  build at the construct time
		//getModeConfigureDataModel().buildModel();//?no longer used
		//getBidConfigureDataModel().buildModel();//?no longer used
        

		setRtpConfigureDataModel(null);//rtp
		
		this.setSeasonConfigureDataModel(null);
		this.setSignalConfigureDataModel(null);

		setCurrentProgramValidationClass(program.getValidatorClass());
		setCurrentProgramSechdulePage(program.getUiScheduleEventString());
		setSelectedProgramTemplate(program.getClassName());

		String templateLabel = getProgramTemplage(program.getClassName());
		programTemplateList = new ArrayList<SelectItem>();
		programTemplateList.add(0,new SelectItem(program.getClassName(),templateLabel));
        AkuacomJSFUtil.setProgram(this.getProgram());
        AkuacomJSFUtil.setNeedRuleRefresh(new Boolean(true));

        //getSeasonConfigureDataModel().buildModel();//season build at the construct time
	}


	/**
	 * Function for program template changed
	 * Listen the attribute of program template and change relative other attributes
	 * @param event
	 */
	public void programTemplateChange(ValueChangeEvent event){
		this.setRenewFlag(false);
		if((event.getOldValue()==null)||(!event.getOldValue().equals(event.getNewValue()))){
			setSelectedProgramTemplate(event.getNewValue().toString());
			setCurrentProgramSechdulePage(getProgramSchedulePage(getSelectedProgramTemplate()));
			setCurrentProgramValidationClass(getProgramValidationClass(getSelectedProgramTemplate()));
		}
	}

	public void programTemplateChange(String programClass){
		this.setRenewFlag(false);
		setSelectedProgramTemplate(programClass);
		setCurrentProgramSechdulePage(getProgramSchedulePage(getSelectedProgramTemplate()));
		setCurrentProgramValidationClass(getProgramValidationClass(getSelectedProgramTemplate()));
		if(operationFlag.equalsIgnoreCase(OPERATION_FLAG_ADD)){

		}else if(operationFlag.equalsIgnoreCase(OPERATION_FLAG_UPDATE)){

		}
	}



	/**
	 * Function for get suitable program validation class base on the program template
	 * @param programTemplate
	 * @return
	 */
	private String getProgramValidationClass(String programTemplate){
		if(PROGRAM_IMPLEMENTATION_CLASS_CPP.equalsIgnoreCase(programTemplate)){
			return PROGRAM_VALIDATION_CLASS_CPP;
		}else if(PROGRAM_IMPLEMENTATION_CLASS_DBP_NO_BID.equalsIgnoreCase(programTemplate)){
			return PROGRAM_VALIDATION_CLASS_DBP_NO_BID;
		}else if(PROGRAM_IMPLEMENTATION_CLASS_DBP.equalsIgnoreCase(programTemplate)){
			return PROGRAM_VALIDATION_CLASS_DBP;
		}else if(PROGRAM_IMPLEMENTATION_CLASS_RTP.equalsIgnoreCase(programTemplate)){
			return PROGRAM_VALIDATION_CLASS_RTP;
		}else if(PROGRAM_IMPLEMENTATION_CLASS_DEMO.equalsIgnoreCase(programTemplate)){
			return PROGRAM_VALIDATION_CLASS_DEMO;
		}else{
			return PROGRAM_VALIDATION_CLASS_CPP;
		}
	}

	/**
	 * Function for get suitable program schedule page base on the program template
	 * @param programTemplate
	 * @return
	 */
	private String getProgramSchedulePage(String programTemplate){
		if(PROGRAM_IMPLEMENTATION_CLASS_CPP.equalsIgnoreCase(programTemplate)){
			return PROGRAM_SCHEDULE_PAGE_CPP;
		}else if(PROGRAM_IMPLEMENTATION_CLASS_DBP_NO_BID.equalsIgnoreCase(programTemplate)){
			return PROGRAM_SCHEDULE_PAGE_DBP_NO_BID;
		}else if(PROGRAM_IMPLEMENTATION_CLASS_DBP.equalsIgnoreCase(programTemplate)){
			return PROGRAM_SCHEDULE_PAGE_DBP;
		}else if(PROGRAM_IMPLEMENTATION_CLASS_RTP.equalsIgnoreCase(programTemplate)){
			return PROGRAM_SCHEDULE_PAGE_RTP_VERSION_6_6;
		}else if(PROGRAM_IMPLEMENTATION_CLASS_DEMO.equalsIgnoreCase(programTemplate)){
			return PROGRAM_SCHEDULE_PAGE_DEMO;
		}else{
			return PROGRAM_SCHEDULE_PAGE_CPP;
		}
	}
	/**
	 * Function for get suitable program template base on the program class
	 * @param programClass
	 * @return
	 */
	private String getProgramTemplage(String programClass){
		if(PROGRAM_IMPLEMENTATION_CLASS_CPP.equalsIgnoreCase(programClass)){
			return PROGRAM_TEMPLATE_LABEL_CPP;
		}else if(PROGRAM_IMPLEMENTATION_CLASS_DBP_NO_BID.equalsIgnoreCase(programClass)){
			return PROGRAM_TEMPLATE_LABEL_DBP_NO_BID;
		}else if(PROGRAM_IMPLEMENTATION_CLASS_DBP.equalsIgnoreCase(programClass)){
			return PROGRAM_TEMPLATE_LABEL_DBP;
		}else if(PROGRAM_IMPLEMENTATION_CLASS_RTP.equalsIgnoreCase(programClass)){
			return PROGRAM_TEMPLATE_LABEL_RTP;
		}else if(PROGRAM_IMPLEMENTATION_CLASS_DEMO.equalsIgnoreCase(programClass)){
			return PROGRAM_TEMPLATE_LABEL_DEMO;
		}else if(PROGRAM_IMPLEMENTATION_CLASS_TEST.equalsIgnoreCase(programClass)){
			return PROGRAM_TEMPLATE_LABEL_DEMO;
		}else if(PROGRAM_IMPLEMENTATION_CLASS_ISSUE.equalsIgnoreCase(programClass)){
			return PROGRAM_TEMPLATE_LABEL_CPP;
		}else if(PROGRAM_IMPLEMENTATION_CLASS_RTP2013.equalsIgnoreCase(programClass)){
			return PROGRAM_TEMPLATE_LABEL_RTP2013;
		}else if(PROGRAM_IMPLEMENTATION_CLASS_CBP.equalsIgnoreCase(programClass)){
			return PROGRAM_TEMPLATE_LABEL_CBP;
		}else{
			return PROGRAM_TEMPLATE_LABEL_CPP;
		}
	}

	// ---------------------------------------------Getter and Setter--------------------------------

	public boolean isRenewFlag() {
		return renewFlag;
	}
	public void setRenewFlag(boolean renewFlag) {
		this.renewFlag = renewFlag;
	}


	public boolean isDisplayFlagRTP() {
		displayFlagRTP = this.getSelectedProgramTemplate() != null && this.getSelectedProgramTemplate().indexOf("RTP") > -1;
		return displayFlagRTP;
	}

	public void setDisplayFlagRTP(boolean displayFlagRTP) {
		this.displayFlagRTP = displayFlagRTP;
	}

	public SignalConfigureDataModel getSignalConfigureDataModel() {
		if(signalConfigureDataModel==null){
			signalConfigureDataModel = new SignalConfigureDataModel(this);
		}
		return signalConfigureDataModel;
	}
	public void setSignalConfigureDataModel(
			SignalConfigureDataModel signalConfigureDataModel) {
		this.signalConfigureDataModel = signalConfigureDataModel;
	}
	public ModeConfigureDataModel getModeConfigureDataModel() {
		if(modeConfigureDataModel==null){
			modeConfigureDataModel = new ModeConfigureDataModel(this);
		}
		return modeConfigureDataModel;
	}

	public void setModeConfigureDataModel(
			ModeConfigureDataModel modeConfigureDataModel) {
		this.modeConfigureDataModel = modeConfigureDataModel;
	}
	public BidConfigureDataModel getBidConfigureDataModel() {
		if(bidConfigureDataModel==null){
			bidConfigureDataModel = new BidConfigureDataModel(this);
		}
		return bidConfigureDataModel;
	}

	public void setBidConfigureDataModel(BidConfigureDataModel bidConfigureDataModel) {
		this.bidConfigureDataModel = bidConfigureDataModel;
	}
	public SeasonConfigureDataModel getSeasonConfigureDataModel() {
		if(seasonConfigureDataModel==null){
			seasonConfigureDataModel = new SeasonConfigureDataModel(this);
		}
		return seasonConfigureDataModel;
	}

	public void setSeasonConfigureDataModel(
			SeasonConfigureDataModel seasonConfigureDataModel) {
		this.seasonConfigureDataModel = seasonConfigureDataModel;
	}

	public RTPConfigureDataModel getRtpConfigureDataModel() {
		if(rtpConfigureDataModel == null){
			rtpConfigureDataModel = new RTPConfigureDataModel(this);
		}
		return rtpConfigureDataModel;
	}

	public void setRtpConfigureDataModel(RTPConfigureDataModel rtpConfigureDataModel) {
		this.rtpConfigureDataModel = rtpConfigureDataModel;
	}

    public RulesConfigureDataModel getRuleConfigureDataModel() {
        if (ruleConfigureDataModel == null)
            ruleConfigureDataModel = new RulesConfigureDataModel(this);
        return ruleConfigureDataModel;
    }

    public void setRuleConfigureDataModel(RulesConfigureDataModel ruleConfigureDataModel) {
        this.ruleConfigureDataModel = ruleConfigureDataModel;
    }

	public static Logger getLog() {
		return log;
	}
	public Program getProgram() {

		if(program==null){
			program = new Program();
			program.setMinIssueToStartM(0);
			program.setMaxIssueTimeH(23);
			program.setMaxIssueTimeM(59);
			program.setMinStartTimeH(0);
			program.setMinStartTimeM(0);
			program.setMinEndTimeH(0);
			program.setMinEndTimeM(0);
			program.setMaxStartTimeH(23);
			program.setMaxStartTimeM(59);
			program.setMaxEndTimeH(23);
			program.setMaxEndTimeM(59);
			program.setMinDurationM(1);
			program.setMaxDurationM(1440);
			program.setPendingTimeDBEH(21);
			program.setPendingTimeDBEM(0);
            program.setAutoRepeatTimeOfDay(this.getAutoRepeatTimeOfDay());
			buildProgramConfigure(program);
		}
		return program;
	}
	
	public void buildProgramConfigure(Program program){
		setMaxIssueTimeString(program.getMaxIssueTimeH()+":"+program.getMaxIssueTimeM());
		setMinStartTimeString(program.getMinStartTimeH()+":"+program.getMinStartTimeM());
		setMaxStartTimeString(program.getMaxStartTimeH()+":"+program.getMaxStartTimeM());
		setMinEndTimeString(program.getMinEndTimeH()+":"+program.getMinEndTimeM());
		setMaxEndTimeString(program.getMaxEndTimeH()+":"+program.getMaxEndTimeM());
		setPendingTimeDBEString(program.getPendingTimeDBEH()+":"+program.getPendingTimeDBEM());
        if (program.getAutoRepeatTimeOfDay() != null){
            setAutoRepeatTimeOfDayStr(program.getAutoRepeatTimeOfDay().toString());
        }else{
            setAutoRepeatTimeOfDayStr("");
        }
        
        if (program.getClassName()==null || 
        		(program.getClassName() !=null && (program.getClassName().equals("com.akuacom.pss2.program.demo.DemoProgramEJB") 
        		|| program.getClassName().equals("com.akuacom.pss2.program.testProgram.TestProgramEJB")))) {
        	setDefaultDuration=true;
        } else {
        	setDefaultDuration=false;
        }
	}
	
	public void setProgram(Program program) {
		this.program = program;
	}
	public String getProgramName() {
		return programName;
	}
	public void setProgramName(String programName) {
		this.programName = programName;
	}
	public String getMaxIssueTimeString() {
		return maxIssueTimeString;
	}

	public void setMaxIssueTimeString(String maxIssueTimeString) {
		this.maxIssueTimeString = maxIssueTimeString;
	}

	public String getMinStartTimeString() {
		return minStartTimeString;
	}

	public void setMinStartTimeString(String minStartTimeString) {
		this.minStartTimeString = minStartTimeString;
	}

	public String getMaxStartTimeString() {
		return maxStartTimeString;
	}

	public void setMaxStartTimeString(String maxStartTimeString) {
		this.maxStartTimeString = maxStartTimeString;
	}

	public String getMinEndTimeString() {
		return minEndTimeString;
	}

	public void setMinEndTimeString(String minEndTimeString) {
		this.minEndTimeString = minEndTimeString;
	}

	public String getMaxEndTimeString() {
		return maxEndTimeString;
	}

	public void setMaxEndTimeString(String maxEndTimeString) {
		this.maxEndTimeString = maxEndTimeString;
	}

	public String getPendingTimeDBEString() {
		return pendingTimeDBEString;
	}

	public void setPendingTimeDBEString(String pendingTimeDBEString) {
		this.pendingTimeDBEString = pendingTimeDBEString;
	}

	public List<SelectItem> getProgramTemplateList() {

		return programTemplateList;
	}

	public void setProgramTemplateList(List<SelectItem> programTemplateList) {
		this.programTemplateList = programTemplateList;
	}

	public void setSelectedProgramTemplate(String selectedProgramTemplate) {
		this.selectedProgramTemplate = selectedProgramTemplate;
	}

	public String getSelectedProgramTemplate() {
		return selectedProgramTemplate;
	}


	public void setCurrentProgramSechdulePage(String currentProgramSechdulePage) {
		this.currentProgramSechdulePage = currentProgramSechdulePage;
	}


	public String getCurrentProgramSechdulePage() {
		return currentProgramSechdulePage;
	}

	public void setOperationFlag(String operationFlag) {
		this.operationFlag = operationFlag;
	}

	public String getOperationFlag() {
		return operationFlag;
	}

	public void setCurrentProgramValidationClass(
			String currentProgramValidationClass) {
		this.currentProgramValidationClass = currentProgramValidationClass;
	}

	public String getCurrentProgramValidationClass() {
		return currentProgramValidationClass;
	}

	public void setProgramTemplateEditableFlag(boolean programTemplateEditableFlag) {
		this.programTemplateEditableFlag = programTemplateEditableFlag;
	}

	public boolean isProgramTemplateEditableFlag() {
		return programTemplateEditableFlag;
	}

	public void setProgramNameEditableFlag(boolean programNameEditableFlag) {
		this.programNameEditableFlag = programNameEditableFlag;
	}

	public boolean isProgramNameEditableFlag() {
		return programNameEditableFlag;
	}

	public void setTitleOfTheUI(String titleOfTheUI) {
		this.titleOfTheUI = titleOfTheUI;
	}

	public String getTitleOfTheUI() {
		return titleOfTheUI;
	}

	public void setAddFlag(boolean addFlag) {
		this.addFlag = addFlag;
	}

	public boolean isAddFlag() {
		return addFlag;
	}

    /*
    public String getWeatherTimerInfo() {
            SystemManager systemManager = EJB3Factory.getBean(SystemManager.class);
            SCERTPProgramEJB programEJB = (SCERTPProgramEJB)systemManager.lookupProgramBean(this.getProgramName());
            weatherTimerInfo = programEJB.getTimersInfo().toString();

            if (weatherTimerInfo.equals("SCERTP_WEATHER")){

            }
        return weatherTimerInfo;
    }

    public void setWeatherTimerInfo(String weatherTimerInfo) {

        this.weatherTimerInfo = weatherTimerInfo;
    }
    */

    public Date getAutoRepeatTimeOfDay() {
        autoRepeatTimeOfDay = new Date();
        return autoRepeatTimeOfDay;
    }

    public void setAutoRepeatTimeOfDay(Date autoRepeatTimeOfDay) {
        this.autoRepeatTimeOfDay = autoRepeatTimeOfDay;
    }

    public String getAutoRepeatTimeOfDayStr() {
        return autoRepeatTimeOfDayStr;
    }

    public void setAutoRepeatTimeOfDayStr(String autoRepeatTimeOfDayStr) {
        this.autoRepeatTimeOfDayStr = autoRepeatTimeOfDayStr;
    }

    public void validateMinStartTime(FacesContext context, UIComponent toValidate, Object value) {
        if (programName != null) {
            if ("PeakChoice - BestEffort1-7".equals(programName)) {
                String startTime = (String) value;
                if (!"13:00".equals(startTime)) {
                    ((UIInput) toValidate).setValid(false);
                    FacesMessage message = new FacesMessage("Invalid min start time. It has to be 13:00");
                    context.addMessage(toValidate.getClientId(context), message);
                }
            }
        }
    }

    public void validateMinEndTime(FacesContext context, UIComponent toValidate, Object value) {
        if (programName != null) {
            if ("PeakChoice - BestEffort1-7".equals(programName)) {
                String startTime = (String) value;
                if (!"19:00".equals(startTime)) {
                    ((UIInput) toValidate).setValid(false);
                    FacesMessage message = new FacesMessage("Invalid min end time. It has to be 19:00");
                    context.addMessage(toValidate.getClientId(context), message);
                }
            }
        }
    }

    public void validateMaxStartTime(FacesContext context, UIComponent toValidate, Object value) {
        if (programName != null) {
            if ("PeakChoice - BestEffort1-7".equals(programName)) {
                String startTime = (String) value;
                if (!"13:00".equals(startTime)) {
                    ((UIInput) toValidate).setValid(false);
                    FacesMessage message = new FacesMessage("Invalid max start time. It has to be 13:00");
                    context.addMessage(toValidate.getClientId(context), message);
                }
            }
        }
    }

    public void validateMaxndTime(FacesContext context, UIComponent toValidate, Object value) {
        if (programName != null) {
            if ("PeakChoice - BestEffort1-7".equals(programName)) {
                String startTime = (String) value;
                if (!"19:00".equals(startTime)) {
                    ((UIInput) toValidate).setValid(false);
                    FacesMessage message = new FacesMessage("Invalid max end time. It has to be 19:00");
                    context.addMessage(toValidate.getClientId(context), message);
                }
            }
        }
    }

     public void saveFlagActionDisabled(){
		this.saveFlag = false;
	}

    public void saveFlagActionEnabled(){
		this.saveFlag = true;
	}
    public boolean isSaveFlag() {
        return saveFlag;
    }

    public void setSaveFlag(boolean saveFlag) {
        this.saveFlag = saveFlag;
    }

	public String getDateFormat() {
		return dateFormat;
	}

	public boolean isSetDefaultDuration() {
		return setDefaultDuration;
	}

	public void setSetDefaultDuration(boolean setDefaultDuration) {
		this.setDefaultDuration = setDefaultDuration;
	}
}
