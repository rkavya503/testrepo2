package com.akuacom.pss2.richsite.gridpoint;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.akuacom.pss2.data.gridpoint.GridPointConfiguration;
import com.akuacom.pss2.grippoint.wsclient.AuthClient;
import com.akuacom.pss2.grippoint.wsclient.AuthResponseBean;
import com.akuacom.pss2.grippoint.wsclient.RetrieveDataClient;
import com.akuacom.pss2.grippoint.wsclient.RetrieveResponseBean;
import com.akuacom.pss2.richsite.FDUtils;
import com.akuacom.utils.lang.DateUtil;

public class GridPointConfigureBackingBean {
	/** The serialVersionUID */
	private static final long serialVersionUID = 3831750127717274233L;
	//---------------------------------------------------attributes---------------------------------------------------------
	/** The log */
	private static final Logger log = Logger.getLogger(GridPointConfigureBackingBean.class);
	
	/** The GridPointConfiguration instance*/
	private GridPointConfiguration dataModel = new GridPointConfiguration();
	
	/** The GridPointConfiguration instance timeInterval attribute string value for validation purpose*/
	private String timeIntervalString;
	
	/** The GridPointConfiguration instance fixScopeValue attribute string value for validation purpose*/
	private String fixScopeValueString;
	
	/** The GridPointConfiguration instance dateBackScope attribute string value for validation purpose*/
	private String dateBackScopeString;
	
	/** The Authentication results */
	private List<String> authenticationResults = new ArrayList<String>();
	
	/** The retrieve data results */
	private List<GridPointRetrieveDataBackingBean> retrieveDataResults = new ArrayList<GridPointRetrieveDataBackingBean>();
	
	/** The test site id */
	private String testSiteId;
	
	/** The test end time  */
	private String testEndTimeString;

   /** The GridPointConfiguration instance fixScopeEnabled attribute string value for validation purpose*/
	private boolean fixScopeEnabled;

    private boolean lastValid;

    private String controlState;
	
	
	//---------------------------------------------------business logic---------------------------------------------------------

	private boolean setup = true;
	public GridPointConfigureBackingBean(){
		retrieveConfigureDataModel();
	}
	
	public void retrieveConfigureDataModel(){
		GridPointConfiguration dataModel = GridPointConfigureManager.getInstance().retrieveConfigureDataModel();
		if(dataModel == null){
			setSetup(false);
			setupValidate();
		}else{
			setDataModel(dataModel);
			initializeConfigureDataModel();	
		}
	}
	
	private boolean setupValidate(){
		if(!setup){
			log.info(GridPointConfigureBackingBeanValidator.ERROR_MESSAGE_RETRIEVE_NO_RECORD_CONFIGURATION);
			FDUtils.addMsgError(GridPointConfigureBackingBeanValidator.ERROR_MESSAGE_RETRIEVE_NO_RECORD_CONFIGURATION);
		}
		return setup;
	}
	
	public void mergeConfigureDataModel(){
		boolean validateFlag = GridPointConfigureBackingBeanValidator.validate(this);
		if(!validateFlag||!setupValidate()){
			return;
		}
		buildDataModel();
		GridPointConfigureManager.getInstance().mergeConfigureDataModel(dataModel);
		FDUtils.addMsgInfo(GridPointConfigureBackingBeanValidator.SUCCESS_MESSAGE_SAVE_CONFIGURATION);
	}
	
	private void buildDataModel(){
		if(dataModel!=null){
			dataModel.setTimeInterval(Integer.parseInt(this.getTimeIntervalString()));
			dataModel.setDateBackScope(Integer.parseInt(this.getDateBackScopeString()));

            dataModel.setFixScopeValue(Integer.parseInt(this.getFixScopeValueString()));
            if (this.controlState.equals("fixed")){
                 dataModel.setFixScopeEnabled(true); 
            } else {
                 dataModel.setFixScopeEnabled(false); 
            }
          }
	}
	
	public synchronized void testAuthntication(){
		boolean validateFlag = GridPointConfigureBackingBeanValidator.validate(this);
		if(!validateFlag||!setupValidate()){
			return;
		}
		buildDataModel();
		authenticationResults.clear();
		AuthClient authenticationInvoker = new AuthClient("", dataModel.getAuthenticationURL(), "");

		try {
			AuthResponseBean bean = new AuthResponseBean(authenticationInvoker.authentication(dataModel.getUsername(), dataModel.getPassword()));
			authenticationResults.add(bean.getLoginResult());
		} catch (Exception e) {
			log.error("Test Grid Point Authentication failed! The error message is : "+e.getMessage());
			FDUtils.addMsgError("Test Grid Point Authentication failed! The error message is : "+e.getMessage());
			log.error(e);
		}
	}
    
    public static Date getDate(Date date, int intervalMinutes) {
        if (date != null) {
            long diff = intervalMinutes *60 * 1000;
            return new Date(date.getTime() + diff);
        } else {
            return date;
        }
    }
    
     public synchronized void testRetrieveData(){
		boolean validateFlag = GridPointConfigureBackingBeanValidator.validate(this);
		if(!validateFlag||!setupValidate()){
			return;
		}
		if(StringUtils.isEmpty(this.getTestSiteId())){
			FDUtils.addMsgError(GridPointConfigureBackingBeanValidator.ERROR_MESSAGE_SITE_ID_EMPTY);
			return;
		}
		buildDataModel();
		AuthResponseBean bean=null;
		try {
			AuthClient authenticationInvoker = new AuthClient("", dataModel.getAuthenticationURL(), "");
			bean = new AuthResponseBean(authenticationInvoker.authentication(dataModel.getUsername(), dataModel.getPassword()));
			
		} catch (Exception e) {
			log.info(GridPointConfigureBackingBeanValidator.ERROR_MESSAGE_TEST_AUTHENTICATION+e.getMessage());
			FDUtils.addMsgError(GridPointConfigureBackingBeanValidator.ERROR_MESSAGE_TEST_AUTHENTICATION+e.getMessage());
			return;
		}
		Date endTime;
		Date startTime;
		String startTimeString="";
		String endTimeString="";
		try{
			if(dataModel.getFixScopeEnabled()){
				endTime= DateUtil.parseStringToDate(getTestEndTimeString(), DateUtil.dateTimeFormatter());
				startTime = getDate(endTime, 0-dataModel.getFixScopeValue());
				startTimeString = DateUtil.format(startTime,GridPointConfigureBackingBeanValidator.timeFormatPattern);
				endTimeString = DateUtil.format(endTime, GridPointConfigureBackingBeanValidator.timeFormatPattern);
			}else{
				endTime= new Date();
				startTime = DateUtil.getDate(endTime, 0-dataModel.getDateBackScope());
				startTimeString = DateUtil.format(startTime, GridPointConfigureBackingBeanValidator.timeFormatPattern);
				endTimeString = DateUtil.format(endTime, GridPointConfigureBackingBeanValidator.timeFormatPattern);
			}
		}catch(Exception e){
			FDUtils.addMsgError(GridPointConfigureBackingBeanValidator.ERROR_MESSAGE_TIME_PATTERN);
			return;
		}
		
		if((bean!=null)&&("ok".equals(bean.getLoginResult()))&&bean.hasCookies()){
			RetrieveDataClient retrieveDataInvoker = new RetrieveDataClient("", dataModel.getRetrieveDataURL(), "");
			SOAPEnvelope resEnvelope;
			try {
				resEnvelope = retrieveDataInvoker.retrieveData(bean.getCookie(), this.getTestSiteId(), startTimeString, endTimeString, "false");
				RetrieveResponseBean resBean = new RetrieveResponseBean(resEnvelope);
				if(resBean.isSuccessful()){
					Map<Date, Double> map = resBean.getMap();
					buildRetrieveData(map);
//					System.out.println(map);
				}else{
//					System.out.println(resBean.getMeesage());
					FDUtils.addMsgError(resBean.getMeesage());
				}
			} catch (Exception e) {
				log.info(GridPointConfigureBackingBeanValidator.ERROR_MESSAGE_TEST_RETRIEVE_DATA+e.getMessage());
				FDUtils.addMsgError(GridPointConfigureBackingBeanValidator.ERROR_MESSAGE_TEST_RETRIEVE_DATA+e.getMessage());
			}
		}else{
			FDUtils.addMsgError(GridPointConfigureBackingBeanValidator.ERROR_MESSAGE_AUTHENTICATION_FIRST);
		}
	}
	
	private void buildRetrieveData(Map<Date, Double> map){
		retrieveDataResults.clear();
		
		Set<Date> keySet = map.keySet();
		Iterator<Date> i = keySet.iterator();
		while(i.hasNext()){
			Date key = (Date) i.next();
			Double value =map.get(key);
			GridPointRetrieveDataBackingBean bean = new GridPointRetrieveDataBackingBean(key,value);
			retrieveDataResults.add(bean);
		}
	}
	
	
	private void initializeConfigureDataModel(){
		if(dataModel.getAuthenticationURL()==null||dataModel.getAuthenticationURL().equalsIgnoreCase("")){
			dataModel.setAuthenticationURL("https://admtools-admview.admmicro.net/ADMTools/auth/wslogin.asmx");
		}
		if(dataModel.getUsername()==null||dataModel.getUsername().equalsIgnoreCase("")){
			dataModel.setUsername("Honeywell");
		}
		if(dataModel.getPassword()==null||dataModel.getPassword().equalsIgnoreCase("")){
			dataModel.setPassword("hwell$123");
		}
		if(dataModel.getRetrieveDataURL()==null||dataModel.getRetrieveDataURL().equalsIgnoreCase("")){
			dataModel.setRetrieveDataURL("https://admtools-admview.admmicro.net/ADMTools/admview/admdata.asmx");
		}
		if(dataModel.getTimeInterval()==null){
			dataModel.setTimeInterval(15);
			this.setTimeIntervalString("15");
		}else{
			this.setTimeIntervalString(String.valueOf(dataModel.getTimeInterval()));
		}
		if(dataModel.getFixScopeEnabled()==null){
			dataModel.setFixScopeEnabled(true);
		}
		if(dataModel.getFixScopeValue()==null){
			dataModel.setFixScopeValue(1440);
			this.setFixScopeValueString("1440");
		}else{
			this.setFixScopeValueString(String.valueOf(dataModel.getFixScopeValue()));
		}
		if(dataModel.getDateBackScope()==null){
			dataModel.setDateBackScope(14);
			this.setDateBackScopeString("14");
		}else{
			this.setDateBackScopeString(String.valueOf(dataModel.getDateBackScope()));
		}

        if (dataModel.getFixScopeEnabled()==null){
            dataModel.setFixScopeEnabled(true);
              this.setControlState("fixed");
        }else{
            if (dataModel.getFixScopeEnabled()){
                 this.setControlState("fixed");
            } else {
               this.setControlState("lastValid");
            }

        }
	}
	
	//--------------------------------------------Getters and Setters-----------------------------------------------

	public GridPointConfiguration getDataModel() {
		return dataModel;
	}

	public void setDataModel(GridPointConfiguration dataModel) {
		this.dataModel = dataModel;
	}

	public List<String> getAuthenticationResults() {
		return authenticationResults;
	}

	public void setAuthenticationResults(List<String> authenticationResults) {
		this.authenticationResults = authenticationResults;
	}

	public String getTimeIntervalString() {
		return timeIntervalString;
	}

	public void setTimeIntervalString(String timeIntervalString) {
		this.timeIntervalString = timeIntervalString;
	}

	public String getFixScopeValueString() {
		return fixScopeValueString;
	}

	public void setFixScopeValueString(String fixScopeValueString) {
		this.fixScopeValueString = fixScopeValueString;
	}

    public boolean isFixScopeEnabled() {
        return fixScopeEnabled;
    }

    public void setFixScopeEnabled(boolean fixScopeEnabled) {
        this.fixScopeEnabled = fixScopeEnabled;
    }

    public boolean isLastValid() {
        return lastValid;
    }

    public void setLastValid(boolean lastValid) {
        this.lastValid = lastValid;
    }


	public String getDateBackScopeString() {
		return dateBackScopeString;
	}

	public void setDateBackScopeString(String dateBackScopeString) {
		this.dateBackScopeString = dateBackScopeString;
	}
	
	public String getTestSiteId() {
		return testSiteId;
	}

	public void setTestSiteId(String testSiteId) {
		this.testSiteId = testSiteId;
	}

	public String getTestEndTimeString() {
		return testEndTimeString;
	}

	public void setTestEndTimeString(String testEndTimeString) {
		this.testEndTimeString = testEndTimeString;
	}
	
	public List<GridPointRetrieveDataBackingBean> getRetrieveDataResults() {
		return retrieveDataResults;
	}

	public void setRetrieveDataResults(
			List<GridPointRetrieveDataBackingBean> retrieveDataResults) {
		this.retrieveDataResults = retrieveDataResults;
	}

	public void setSetup(boolean setupFlag) {
		this.setup = setupFlag;
	}

	public boolean isSetup() {
		return setup;
	}
        /**
     * Gets the control state.
     *
     * @return the control state
     */
    public String getControlState() {
        return controlState;
    }

    /**
     * Sets the control state.
     *
     * @param controlState
     *            the new control state
     */
    public void setControlState(String controlState) {
        this.controlState = controlState;
    }

}
