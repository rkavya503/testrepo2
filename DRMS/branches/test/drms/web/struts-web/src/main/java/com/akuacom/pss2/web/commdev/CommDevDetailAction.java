/*
 * www.akuacom.com - Automating Demand Response
 *
 * com.akuacom.pss2.web.commdev.CommDevDetailAction.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved.
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.web.commdev;

import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.actions.DispatchAction;

import com.akuacom.accmgr.wsclient.AccMgrWSClient;
import com.akuacom.ejb.client.EJB3Factory;
import com.akuacom.pss2.core.ErrorUtil;
import com.akuacom.pss2.core.ValidationException;
import com.akuacom.pss2.data.DataManager;
import com.akuacom.pss2.data.PDataSource;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.participant.ParticipantManager;
import com.akuacom.pss2.program.Program;
import com.akuacom.pss2.program.ProgramManager;
import com.akuacom.pss2.program.participant.ProgramParticipant;
import com.akuacom.pss2.program.participant.ProgramParticipantManager;
import com.akuacom.pss2.system.SystemManager;
import com.akuacom.pss2.system.SystemManagerBean;
import com.akuacom.pss2.system.property.CoreProperty;
import com.akuacom.pss2.system.property.PSS2Features;
import com.akuacom.pss2.util.CBPUtil;
import com.akuacom.pss2.util.DrasRole;
import com.akuacom.pss2.util.EncryptUtil;
import com.akuacom.pss2.util.UserType;
import com.akuacom.pss2.web.login.LoginServlet;
import com.akuacom.pss2.web.tabs.TabsAction;
import com.akuacom.pss2.web.uohome.UOProgramAction;
import com.akuacom.utils.Tag;


/**
 * The Class CommDevDetailAction.
 */
@SuppressWarnings({"UnusedDeclaration"})
public class CommDevDetailAction extends DispatchAction {
	private static final String COOKIE_PUBLICKEY = "ssmpublic.key";
	
    /**
     * Cancel.
     *
     * @param actionMapping the action mapping
     * @param actionForm the action form
     * @param request the request
     * @param response the response
     *
     * @return the action forward
     *
     * @throws Exception the exception
     */
    public ActionForward cancel(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        /* do nothing but to return to parent */
        if (TabsAction.isDemo()) {
            return actionMapping.findForward("parentMap");
        } else {
            return actionMapping.findForward("parent");
        }
    }

    /**
     * Creates the.
     *
     * @param actionMapping the action mapping
     * @param actionForm the action form
     * @param request the request
     * @param response the response
     *
     * @return the action forward
     *
     * @throws Exception the exception
     */
    public ActionForward viewarq(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {

		// ----  SSO implementation START
		
		Cookie[] allCookies = request.getCookies();
		Cookie loginCookie = null;
		boolean createCookie = false;

		EncryptUtil cookieEncoder = new EncryptUtil();
		InputStream pubinputStream = null;
		pubinputStream = LoginServlet.class.getClassLoader().getResourceAsStream(COOKIE_PUBLICKEY);
		
		// get cookies
		if (allCookies != null) {
			for (int i = 0; i < allCookies.length; i++) {
				if (allCookies[i].getName().equals("DRASID")) {
					loginCookie = allCookies[i];
				}
			}

			if (loginCookie == null || loginCookie.getValue() == null) {
				createCookie = true;
			} else {
				
			}
		} else {
			// else remember to create cookie if authorisation succeeds
			createCookie = true;
		}
		

		String userId = request.getRemoteUser();
		String email = "admin@akua.com";
		String userRole = null;
		String tokenCreationTime = null;
		
		if (request.isUserInRole(DrasRole.Admin.toString())) {
			userRole = DrasRole.Admin.toString();
		} else if (request.isUserInRole(DrasRole.Operator.toString())) {
			userRole = DrasRole.Operator.toString();
		}else if (request.isUserInRole(DrasRole.FacilityManager.toString())) {
			userRole = DrasRole.FacilityManager.toString();
		}else if (request.isUserInRole(DrasRole.UtilityOperator.toString())) {
			userRole = DrasRole.UtilityOperator.toString();
		}else if (request.isUserInRole(DrasRole.Readonly.toString())) {
			userRole = DrasRole.Readonly.toString();
		} else  {
			userRole = DrasRole.Dispatcher.toString();
		}
		
		if (createCookie)
			loginCookie = new Cookie("DRASID", "");
		
		boolean setCookie = true;
		tokenCreationTime = getCreationDate();
		StringBuffer newValue = null;
		String enNewValue = null;
		if (setCookie) {
			newValue = new StringBuffer(userId)
			.append(":").append(email).append(":").append(
					userRole).append(":").append(tokenCreationTime).append(":");

			enNewValue = cookieEncoder.encrypt(newValue.toString(), pubinputStream).toString();

		// set to cookie after encryption
		loginCookie.setValue(enNewValue);
		//Restrict the cookie to be secure and accessed over https only
		loginCookie.setPath("/;HttpOnly");
		loginCookie.setSecure(true);
		response.addCookie(loginCookie);
		//request.getSession().setAttribute("DRASID", enNewValue);
		}
		

    	
    	
		String viewARQUrl = "";
		viewARQUrl = "/ssm/showViewArq";

    	response.sendRedirect(viewARQUrl+"?uid="+enNewValue);
        return null;
    }

    
    /**
     * redirect to SSMARQ.
     *
     * @param actionMapping the action mapping
     * @param actionForm the action form
     * @param request the request
     * @param response the response
     *
     * @return the action forward
     *
     * @throws Exception the exception
     */
    public ActionForward create(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	
        return actionMapping.findForward("success");
    }
    
    /*
     * Retrieve comm dev info for population.
     */
    /**
     * Edits the.
     *
     * @param actionMapping the action mapping
     * @param actionForm the action form
     * @param request the request
     * @param response the response
     *
     * @return the action forward
     *
     * @throws Exception the exception
     */
    public ActionForward edit(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {

        final CommDevDetailForm form = (CommDevDetailForm)actionForm;

        SystemManager systemManager = EJB3Factory.getBean(SystemManager.class);
        PSS2Features pss2Features = systemManager.getPss2Features();
        final String userName = form.getUserName();
        ParticipantManager participantManager = EJB3Factory.getBean(ParticipantManager.class);
        final Participant p = participantManager.getParticipant(userName);
        List<String> programs = participantManager.getProgramsForParticipant(userName, false);
        
        if(isEnableCBPConsolidation()){
        	programs = CBPUtil.transferList(programs);
        }
        
        form.setAccountNumber(p.getAccountNumber());
        form.setApplicationId(p.getApplicationId());
        form.setSecondaryAccountNumber(p.getSecondaryAccountNumber());
        form.setType(p.getTypeString());
        form.setFirstName(p.getFirstName());
        form.setLastName(p.getLastName());
        form.setFeedback(p.isFeedback());
        form.setMeterId(p.getMeterId());
        form.setAddress(p.getAddress());
        form.setGridLocation(p.getGridLocation());
        form.setLatitude(p.getLatitude());
        form.setLongitude(p.getLongitude());
        form.setShedPerHourKW(p.getShedPerHourKW());
        form.setExternalContacts(p.getExternalContactsAsContactModels());
        form.setActivated(p.getActivated().booleanValue());
        form.setDataEnabler(p.getDataEnabler().booleanValue());
        form.setInstaller(p.getInstaller().booleanValue());
        form.setAggregator(p.getAggregator().booleanValue());
        form.setRetained(p.getRetained());
        
        form.setCustomerName(p.getCustomerName());
        form.setServiceStreetAddress(p.getServiceStreetAddress());
        form.setServiceCityName(p.getServiceCityName());
        form.setZip(p.getZip());
        form.setABank(p.getABank());
        form.setSlap(p.getSlap());
        form.setPNode(p.getPNode());
        form.setProgramOption(p.getProgramOption());
        form.setSubstation(p.getSubstation());
        form.setBlockNumber(p.getBlockNumber());
        form.setServicePlan(p.getServicePlan());

       
        String dateFormat= pss2Features.getDateFormat();

        form.setRateEffectiveDate(p.getRateEffectiveDate());
        if (form.getRateEffectiveDate() != null)
        	form.setRateEffectiveDateStr(new SimpleDateFormat(dateFormat).format(form.getRateEffectiveDate()).toString());
        else
        	form.setRateEffectiveDateStr("");

        form.setDirectAccessParticipant(p.getDirectAccessParticipant());
        form.setTestParticipant(p.getTestParticipant());        
        form.setUisActive(p.getUisActive());

        form.setNotes(p.getNotes());
        form.setNotesLastUpdate(p.getNotesLastUpdate());
        form.setNotesAuthor(p.getNotesAuthor());
        if (form.getNotesLastUpdate() != null)
        	form.setNotesLastUpdateStr(DateFormat.getInstance().format(form.getNotesLastUpdate()));
        else
        	form.setNotesLastUpdateStr("");

        
        form.setDemandLimiting(p.getDemandLimiting());

        form.setPremiseNumber(p.getPremiseNumber());
        
        form.setEnrolledDate(p.getEnrollmentDate());

        if (form.getEnrolledDate() != null)
        	form.setEnrolledDateStr(new SimpleDateFormat(dateFormat).format(form.getEnrolledDate()).toString());
        else
        	form.setEnrolledDateStr("");

        form.setStartDate(p.getStartDate());
        if (form.getStartDate() != null)
        	form.setStartDateStr(new SimpleDateFormat(dateFormat).format(form.getStartDate()).toString());
        else
        	form.setStartDateStr("");

        form.setDeactivateDate(p.getEndDate());
        if (form.getDeactivateDate() != null)
        	form.setDeactivateDateStr(new SimpleDateFormat(dateFormat).format(form.getDeactivateDate()).toString());
        else
        	form.setDeactivateDateStr("");

        form.setComments(p.getComment());
        
        form.setUserType((p.getUserType().toString()));
        form.setUsageData(this.getCoreAccess("feature.usage"));
        form.setEnableDataService(this.getCoreAccess("feature.enableDataService"));
        setFeatures(form, pss2Features);

        form.setPrograms(programs.toArray(new String[programs.size()]));
        final boolean isUO =  request.isUserInRole(DrasRole.UtilityOperator.toString());
        final boolean isReadOnly =  request.isUserInRole(DrasRole.Readonly.toString());
        final boolean isDispatcher =  request.isUserInRole(DrasRole.Dispatcher.toString());
        if(isReadOnly || isDispatcher) {
        	form.setUserRoleEnabled(true);
        }
        form.setUseDefaultFeatureValue(p.getDefaultEventOptoutEnabled().booleanValue());
        form.setPartEventOptoutEnabled(p.getPartEventOptoutEnabled());
        form.setDefaultEventOptoutEnabled(pss2Features.isEventOptoutEnabled());
        
        form.setAutoDrProfileStartDate(p.getAutoDrProfileStartDate());
        if (form.getAutoDrProfileStartDate() != null)
        	form.setAutoDrProfileStartDateStr(new SimpleDateFormat(dateFormat).format(form.getAutoDrProfileStartDate()).toString());
        else
        	form.setAutoDrProfileStartDateStr("");
        form.setBcdRepName(p.getBcdRepName());
        
        form.setNonAutoDR(p.getNonAutoDR());
        form.setCustomer(p.getCustomer());
        
        String[] items = new String[4];
    	int index = 0;
    	if(form.isAggregator()){
    		items[index++] = "Aggregator";
    	}    	
    	if(form.getTestParticipant()){
    		items[index++] = "Test Participant";
    	}
    	if(form.getCustomer()){
    		items[index++] = "Customer";
    	}
    	if(form.getNonAutoDR()){
    		items[index++] = "Non-Auto DR";
    	}
    	form.setSelectedItems(items);
    	
        
        PDataSource dataSource = null;
        DataManager dataManager = EJB3Factory.getBean(DataManager.class);
        dataSource = dataManager.getDataSourceByNameAndOwner("meter1", p.getParticipantName()) ;
        
        if(dataSource==null){
        	form.setServiceProvider("");
        	form.setSiteID("");
        }else{
        	form.setServiceProvider(dataSource.getServiceProvider());
        	form.setSiteID(dataSource.getSiteID());
        }
        
        //participant client offline notification setting
        form.setClientOfflineNotificationEnable(p.getClientOfflineNotificationEnable());
        form.setClientOfflineNotificationAggEnable(p.getClientOfflineNotificationAggEnable());
        form.setOptOutClientOfflineNotification(p.getOptOutClientOfflineNotification());
        form.setThresholdsSummer(String.valueOf(p.getThresholdsSummer()));
        form.setThresholdsUnSummer(String.valueOf(p.getThresholdsUnSummer()));
       
        List<ProgramParticipant> ppList = participantManager.getProgramParticpantsForClientConfig(p.getParticipantName(),false);
        if(isEnableCBPConsolidation()){
        	ppList = CBPUtil.transferProgramParticipant(ppList);
        }
        if(isUO) {
            form.setAllPrograms(getMyPrograms(programs, request));
            form.setClientsConfig(ppList);

            return actionMapping.findForward("view");
        } else {
            form.setAllPrograms(getAllPrograms(programs));
            form.setClientsConfig(ppList);

            return actionMapping.findForward("edit");
        }
        

    }

    private void setFeatures(CommDevDetailForm form, PSS2Features pss2Features) {
        form.setFeatureParticipantsMapView(this.getCoreAccess("feature.participantsMapView"));
        form.setFeatureParticipantInfo(this.getCoreAccess("feature.participantInfo"));
        form.setFeatureParticipantNotes(this.getCoreAccess("feature.participantNotes"));
        form.setFeatureParticipantsUpload(this.getCoreAccess("feature.participantsUpload"));
        form.setFeatureDemandLimiting(this.getCoreAccess("feature.demandLimiting"));

        form.setFeatureFeedback(pss2Features.isFeatureFeedbackEnabled());
        form.setFeatureLocation(pss2Features.isFeatureLocationEnabled());
        form.setFeatureShedInfo(pss2Features.isFeatureShedInfoEnabled());
        form.setFeatureClientOfflineNotification(pss2Features.isClientOfflineNotificationEnabled());
        form.setFeatureCBDConsolidation(isEnableCBPConsolidation());
    }


    public ActionForward editPassword(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
//        final CommDevDetailForm form = (CommDevDetailForm)actionForm;
//        final String userName = form.getUserName();
//        final boolean isUO = request.isUserInRole(DrasRole.UtilityOperator.toString());
       return actionMapping.findForward("editPassword");
    }

    public boolean getCoreAccess(String coreValue){
    	   SystemManager systemManager = EJB3Factory.getBean(SystemManagerBean.class);
           boolean flag= false;
           for(CoreProperty corp : systemManager.getAllProperties()){
        	   if (corp.getPropertyName().equalsIgnoreCase(coreValue))
                  flag =corp.isBooleanValue();
           }
       return flag;
    }

    /**
     * Gets the all programs.
     *
     * @param activeList the active list
     *
     * @return the all programs
     */
    private List<Tag> getAllPrograms(List<String> activeList) {
        List<Tag> results = new ArrayList<Tag>();

        List<String> allList = EJB3Factory.getBean(com.akuacom.pss2.program.ProgramManager.class).getPrograms();
        
        SystemManager systemManager = EJB3Factory.getBean(SystemManager.class);
        PSS2Features pss2Features = systemManager.getPss2Features();
        if(isEnableCBPConsolidation()){
        	allList = CBPUtil.transferList(allList);
        }
        
        for (String program : allList) {
            Tag tag = new Tag();
            tag.setName(program);
            tag.setValue(Boolean.toString(activeList.contains(program)));
            results.add(tag);
        }

        return results;
    }

    /**
     * Gets the my programs.
     *
     * @param activeList the active list
     * @param request the request
     *
     * @return the my programs
     */
    private List<Tag> getMyPrograms(List<String> activeList, HttpServletRequest request) {
        List<Tag> results = new ArrayList<Tag>();

        //List<String> allList = EJBFactory.getProgramManager().getPrograms();
        String[] myprograms = UOProgramAction.getMyListInTag(request, "UO_PROGRAMS");

        for (String program : myprograms) {
            Tag tag = new Tag();
            tag.setName(program);
            tag.setValue(Boolean.toString(activeList.contains(program)));
            results.add(tag);
        }

        return results;
    }


    public static String specialCharValidator(String name,boolean isPart){
        String[]  sChar = {"*","!","@","#","$","%","^","&","*","()","+","[]","{}",":",";","'",".","/","<>","?","~","`","*"};
                if (!isPart) {
                    name = name.replaceAll("\\s", "");
                }
                for (int i=0;i<sChar.length; i++){
                    if (name.contains(sChar[i])){
                           name = name.replaceAll("\\"+sChar[i], "");   
                    }
                }
        return name;
    }

    /**
     * Save.
     *
     * @param actionMapping the action mapping
     * @param actionForm the action form
     * @param request the request
     * @param response the response
     *
     * @return the action forward
     *
     * @throws Exception the exception
     */
    public ActionForward save(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        CommDevDetailForm form = (CommDevDetailForm)actionForm;
        Participant p = new Participant();
        String userName = form.getUserName();
        if ((userName != null) && (!userName.isEmpty()) && (userName.equals(specialCharValidator(userName,true)))) {
        	userName = specialCharValidator(userName,true);
        	form.setUserName(userName);
        	form.setUsageData(this.getCoreAccess("feature.usage"));
        	form.setEnableDataService(this.getCoreAccess("feature.enableDataService"));

        	p.setUser(userName);

        	if(form.getAccountNumber() == null || form.getAccountNumber().isEmpty()) {
        		p.setAccountNumber(null);
        	} else {
        		String accNum = form.getAccountNumber();
        		// TODO: this shuld be added back in some way but with a different 
        		// character set
        		// accNum = this.specialCharValidator(accNum, false);
        		p.setAccountNumber(accNum);
        	}
        	
        	if(form.getApplicationId() == null || form.getApplicationId().isEmpty()) {
        		p.setApplicationId(null);
        	} else {
        		String appId = form.getApplicationId();
        		appId = specialCharValidator(appId, false);
        		p.setApplicationId(appId);        		
        	}
        	if(!StringUtils.isEmpty(form.getSecondaryAccountNumber())){
        		p.setSecondaryAccountNumber(form.getSecondaryAccountNumber());
            }

        	//participant client offline notification setting
        	p.setClientOfflineNotificationEnable(false);
        	p.setOptOutClientOfflineNotification(false);
        	p.setClientOfflineNotificationAggEnable(false);
        	p.setThresholdsSummer(24);
        	p.setThresholdsUnSummer(168);
        	
        	final ParticipantManager participantManager = EJB3Factory.getBean(ParticipantManager.class);
        	try {
        		String[] programs = form.getPrograms();
        		SystemManager systemManager = EJB3Factory.getBean(SystemManager.class);
				PSS2Features features = systemManager.getPss2Features();
				p.setDefaultEventOptoutEnabled(true);
				p.setPartEventOptoutEnabled(features.isEventOptoutEnabled());

        		participantManager.createParticipant(p, form.getPassword().toCharArray(), programs);
            
        	} catch (Exception e) {

        		ValidationException ve = ErrorUtil.getValidationException(e);
        		ActionErrors errors = new ActionErrors();
        		ActionMessage error;
        		if (ve != null) {
        			error = new ActionMessage(ve.getLocalizedMessage(), false);
        		} else {
        			error = new ActionMessage(ErrorUtil.getErrorMessage(e), false);
        		}
        		errors.add("participantValidation", error);
        		addErrors(request, errors);
        		return actionMapping.findForward("success");
        	}

        	return edit(actionMapping, actionForm, request, response);
        } else {
    		ActionErrors errors = new ActionErrors();
    		errors.add("pss2.commDev.name.illegal.chars", new ActionMessage("pss2.commDev.name.illegal.chars"));
    		addErrors(request, errors);
    		return actionMapping.findForward("success");
        }
    }



    /**
     * Update.
     *
     * @param actionMapping the action mapping
     * @param actionForm the action form
     * @param request the request
     * @param response the response
     *
     * @return the action forward
     *
     * @throws Exception the exception
     */
    public ActionForward reset(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {

        final CommDevDetailForm form = (CommDevDetailForm)actionForm;
        final String userName = form.getUserName();
        ParticipantManager participantManager = EJB3Factory.getBean(ParticipantManager.class);
        final Participant p = participantManager.getParticipant(userName);
        try {
            if (!form.getPassword().equals("null")){
                participantManager.setParticipantPassword(p.getUUID(), form.getPassword() );
                form.setPasswordConf("Successfuly Updated");
            }
        } catch (Exception e){

            ValidationException ve = ErrorUtil.getValidationException(e);
            ActionErrors errors = new ActionErrors();
            ActionMessage error;
            if (ve != null) {
                error = new ActionMessage(ve.getLocalizedMessage(), false);
            } else {
                error = new ActionMessage(ErrorUtil.getErrorMessage(e), false);
            }
            errors.add("participantValidation", error);
            addErrors(request, errors);
            form.setPasswordConf("Erorr Restting Password ");

            return actionMapping.findForward("parent");
        }

        if(TabsAction.isDemo()) {
            return actionMapping.findForward("parent");
        } else {
            return actionMapping.findForward("parent");
        }
    }



    public ActionForward update(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	
    	
    	if(!validateThreshold((CommDevDetailForm)actionForm)){
    		final CommDevDetailForm form = (CommDevDetailForm)actionForm;

            SystemManager systemManager = EJB3Factory.getBean(SystemManager.class);
            PSS2Features pss2Features = systemManager.getPss2Features();
            
            form.setUsageData(this.getCoreAccess("feature.usage"));
            form.setEnableDataService(this.getCoreAccess("feature.enableDataService"));
            setFeatures(form, pss2Features);

    		 ValidationException ve = new ValidationException("Threshold should be a valid positive number.");
             ActionErrors errors = new ActionErrors();
             ActionMessage error;
             error = new ActionMessage(ve.getLocalizedMessage(), false);
             errors.add("participantValidation", error);
             addErrors(request, errors);
             return actionMapping.findForward("edit");
    	}
    	
        SystemManager systemManager = EJB3Factory.getBean(SystemManager.class);
        PSS2Features pss2Features = systemManager.getPss2Features();
        String dateFormat= pss2Features.getDateFormat();

        CommDevDetailForm form = (CommDevDetailForm)actionForm;
        String accountNumber = form.getAccountNumber();
        // validate account number
        if (accountNumber != null && accountNumber.length() > 128) {
            ActionErrors errors = new ActionErrors();
            errors.add("accountNumber", new ActionMessage("pss2.commDev.create.accountNumber"));
            addErrors(request, errors);
            setFeatures(form, pss2Features);
            return actionMapping.findForward("edit");
        }
       
        String applicationId = form.getApplicationId();
        if(applicationId != null && applicationId.length() > 128) {
        	ActionErrors errors = new ActionErrors();
        	 errors.add("applicationId", new ActionMessage("pss2.commDev.create.applicationId"));
             addErrors(request, errors);
             setFeatures(form, pss2Features);
             return actionMapping.findForward("edit");
        }

        String userName = form.getUserName();
        form.setUsageData(this.getCoreAccess("feature.usage"));
        form.setEnableDataService(this.getCoreAccess("feature.enableDataService"));
        ParticipantManager participantManager = EJB3Factory.getBean(ParticipantManager.class);
        ProgramParticipantManager programParticipantManager = EJB3Factory.getBean(ProgramParticipantManager.class);
    	DataManager dataManager = EJB3Factory.getBean(DataManager.class);

        final Participant p = participantManager.getParticipant(userName);
        final AccMgrWSClient accmgrClient = new AccMgrWSClient();
        accmgrClient.initialize();

        String[] clientConfigs=request.getParameterValues("clientConfig");
        List<String> configList = null;

        boolean isClient = false;
        if (clientConfigs != null){
                isClient = true;
            configList = Arrays.asList(clientConfigs);
        }

        
        String accNum = accountNumber;
        // TODO: this should be added back in some way but with a different 
        // character set
        // accNum = this.specialCharValidator(accNum, false);
        p.setAccountNumber(accNum);
        if(applicationId == null || applicationId.isEmpty()){
        	p.setApplicationId(null);
        } else {
        	p.setApplicationId(applicationId);
        }
        if(!StringUtils.isEmpty(form.getSecondaryAccountNumber())){
        	 p.setSecondaryAccountNumber(form.getSecondaryAccountNumber());
        }else{
        	p.setSecondaryAccountNumber(null);
        }

        p.setType("MANUAL".equalsIgnoreCase(form.getType())? Participant.TYPE_MANUAL : Participant.TYPE_AUTO);
        p.setFirstName(form.getFirstName());
        p.setLastName(form.getLastName());

        p.setFeedback(form.isFeedback());
        p.setMeterId(form.getMeterId());

        p.setAddress(form.getAddress());
        p.setGridLocation(form.getGridLocation());
        p.setLatitude(form.getLatitude());
        p.setLongitude(form.getLongitude());
        
        
        p.setCustomerName(form.getCustomerName());
        p.setServiceStreetAddress(form.getServiceStreetAddress());
        p.setServiceCityName(form.getServiceCityName());
        p.setZip(form.getZip());
        p.setABank(form.getABank());
        p.setSlap(form.getSlap());
        p.setProgramOption(form.getProgramOption());
        p.setBlockNumber(form.getBlockNumber());
        p.setSubstation(form.getSubstation());
        p.setPNode(form.getPNode());
        p.setServicePlan(form.getServicePlan());

        if ((p.getNotes() != null) && (form.getNotes() != null)) {
        	if (!p.getNotes().equals(form.getNotes())) {
        		p.setNotesLastUpdate(new Date());
        		p.setNotesAuthor(request.getRemoteUser());
        	}
        } else if ((p.getNotes() == null) && (form.getNotes() != null)) {
        	p.setNotesLastUpdate(new Date());
        	p.setNotesAuthor(request.getRemoteUser());
        }
        p.setNotes(form.getNotes());
        p.setDemandLimiting(form.getDemandLimiting());

        p.setPremiseNumber(form.getPremiseNumber());
        p.setDefaultEventOptoutEnabled(Boolean.valueOf(form.isUseDefaultFeatureValue()));
        if (p.getDefaultEventOptoutEnabled())
        	p.setPartEventOptoutEnabled(form.isDefaultEventOptoutEnabled());
        else
        	p.setPartEventOptoutEnabled(Boolean.valueOf(form.isPartEventOptoutEnabled()));
        if (p.getRetained())
        	p.setRetained(form.isRetained());
        else
        	p.setRetained(Boolean.valueOf(form.isRetained()));


        try {
            if (form.getEnrolledDateStr() != null)
                    form.setEnrolledDate(new SimpleDateFormat( dateFormat ).parse(form.getEnrolledDateStr()));
                else
                    form.setEnrolledDate(null);
            } catch (Exception e) {
                //Validation phase will catch the format error
            }
   
        p.setEnrollmentDate(form.getEnrolledDate());

         try {
        	if (form.getStartDateStr() != null)
        		form.setStartDate(new SimpleDateFormat( dateFormat ).parse(form.getStartDateStr()));
        	else
        		form.setStartDate(null);
        } catch (Exception e) {
        	//Validation phase will catch the format error
        }
        p.setStartDate(form.getStartDate());
        
        p.setEndDate(form.getDeactivateDate());
        try {
        	if (form.getDeactivateDateStr() != null)
        		form.setDeactivateDate(new SimpleDateFormat( dateFormat).parse(form.getDeactivateDateStr()));
        	else
        		form.setDeactivateDate(null);
        } catch (Exception e) {
        	//Validation phase will catch the format error
        }
        p.setEndDate(form.getDeactivateDate());


        p.setComment(form.getComments());
 
        try {
        	if (form.getRateEffectiveDateStr() != null)
        		form.setRateEffectiveDate(new SimpleDateFormat( dateFormat ).parse(form.getRateEffectiveDateStr()));
        	else
        		form.setRateEffectiveDate(null);
        } catch (Exception e) {
        	//Validation phase will catch the format error
        }
        p.setRateEffectiveDate(form.getRateEffectiveDate());
        
        
        p.setDirectAccessParticipant(form.getDirectAccessParticipant());
      
        //p.setUisActive(form.getUisActive());        
        
        p.setActivated(Boolean.valueOf(form.isActivated()));

        p.setDataEnabler(Boolean.valueOf(form.isDataEnabler()));
        if ((form.getDemandLimiting() != null) && (form.getDemandLimiting()))
        	p.setDataEnabler(true);
        
        
        p.setInstaller(Boolean.valueOf(form.isInstaller()));

        p.setBcdRepName(form.getBcdRepName());
        p.setAutoDrProfileStartDate(form.getAutoDrProfileStartDate());
        String[] selected = form.getSelectedItems(); //{"Aggregator","Test Participant","Customer","Non-Auto DR"};
        Boolean isAggregaor = Boolean.FALSE, isTestparticipant = Boolean.FALSE, isCustomer = Boolean.FALSE, isNonAutoDr = Boolean.FALSE;
        for(String item : selected){
        	if("Aggregator".equalsIgnoreCase(item)){
        		isAggregaor = Boolean.TRUE;
        	}else if("Test Participant".equalsIgnoreCase(item)){
        		isTestparticipant = Boolean.TRUE;
        	}else if ("Customer".equalsIgnoreCase(item)){
        		isCustomer = Boolean.TRUE;
        	}else if ("Non-Auto DR".equalsIgnoreCase(item)){
        		isNonAutoDr = Boolean.TRUE;
        	}
        }
        p.setAggregator(isAggregaor);
        p.setTestParticipant(isTestparticipant);
        p.setCustomer(isCustomer);
        p.setNonAutoDR(isNonAutoDr);
        
         if (form.getUserType().equals("ADVANCED")){
              p.setUserType(UserType.ADVANCED);
          }else if (form.getUserType().equals("SIMPLE")){
              p.setUserType(UserType.SIMPLE);
          }

//        p.setShedPerHourKW(form.getShedPerHourKW());
        String shedType = p.getShedType();
        if(shedType==null||shedType.equalsIgnoreCase("")){
        	p.setShedType("SIMPLE");
        }
        p.setClient(false);
        
      //participant client offline notification setting
        if(this.getCoreAccess("feature.clientOfflineNotificationEnable")){
        	p.setClientOfflineNotificationEnable(Boolean.valueOf(form.isClientOfflineNotificationEnable()));
        	p.setClientOfflineNotificationAggEnable(Boolean.valueOf(form.isClientOfflineNotificationAggEnable()));
        	p.setOptOutClientOfflineNotification(Boolean.valueOf(form.isOptOutClientOfflineNotification()));
        	p.setThresholdsSummer(Integer.valueOf(form.getThresholdsSummer()));
        	p.setThresholdsUnSummer(Integer.valueOf(form.getThresholdsUnSummer()));	
        }
        
        PDataSource dataSource = null;

		try {
			String[] programs = form.getPrograms();
	        if(isEnableCBPConsolidation()){
	        	List<String> list = Arrays.asList(programs);
	        	list = CBPUtil.revertList(list);
	        	programs= list.toArray(new String[list.size()]);
				if(configList!=null){
					configList=CBPUtil.revertList(configList);
				}
	        }
	        if(form.isApplicationIdEnabled()){
	        	if(form.getApplicationId() == null || form.getApplicationId().isEmpty()){
	        		ProgramManager programManager = EJB3Factory.getBean(ProgramManager.class);
	        		for(String prog: programs){
	        			Program programOnly = programManager.getProgramOnly(prog);
	        			if(programOnly != null)  {
	        				String programName = programOnly.getClassName();
	        				if(programName != null && programName.equalsIgnoreCase("com.akuacom.pss2.program.dbp.DBPBidProgramEJB")){
	        					ActionErrors errors = new ActionErrors();
	        		        	errors.add("editApplicationId", new ActionMessage("pss2.commDev.edit.applicationId"));
	        		            addErrors(request, errors);
	        		            setFeatures(form, pss2Features);
	        		            return actionMapping.findForward("edit");	        					
	        				}
	        			}
		        	}
	        	}
	        }
	        
			participantManager.updateParticipant(p, programs);
			if ((programs != null)) {
				for (ProgramParticipant ppc : participantManager
						.getProgramParticpantsForClientConfig(
								p.getParticipantName(), false)) {
					if (configList == null) {
						// log.error("+++++ REMOVE ALL ++ "
						// +ppc.getProgramName() );
						ppc.setClientConfig(0);
						programParticipantManager.updateProgramParticipant(
								ppc.getProgramName(), p.getParticipantName(),
								false, ppc);
					} else if (configList.contains(ppc.getProgramName())) {
						// log.error("+++++ IN THE LIST ++ "
						// +ppc.getProgramName() );
						ppc.setClientConfig(1);
						programParticipantManager.updateProgramParticipant(
								ppc.getProgramName(), p.getParticipantName(),
								false, ppc);
					} else {
						// log.error("+++++ OUT THE LIST ++ " +
						// ppc.getProgramName());
						ppc.setClientConfig(0);
						programParticipantManager.updateProgramParticipant(
								ppc.getProgramName(), p.getParticipantName(),
								false, ppc);
					}
				}
			}

            if (form.isDataEnabler()){     			
                   try{
                            dataSource = dataManager.getDataSourceByNameAndOwner("meter1", p.getParticipantName()) ;
                            dataSource.setEnabled(true);
                            dataSource.setServiceProvider(form.getServiceProvider());
                            dataSource.setSiteID(form.getSiteID());
                            dataManager.updatePDataSource(dataSource);
                    }catch(Exception e){

                    }

                    if (dataSource == null ){
                            dataSource = new PDataSource();
                            dataSource.setEnabled(true);
                            dataSource.setName("meter1");
                            dataSource.setOwnerID(p.getParticipantName());
                            dataSource.setServiceProvider(form.getServiceProvider());
                            dataSource.setSiteID(form.getSiteID());
                            dataManager.createPDataSource(dataSource);
                    }
             }else{

                try{
                            dataSource = dataManager.getDataSourceByNameAndOwner("meter1", p.getParticipantName()) ;
                            dataSource.setEnabled(false);
                            dataManager.updatePDataSource(dataSource);
                        }catch(Exception e){
                            
                        }
                }
            
 

        } catch (Exception e){
            ValidationException ve = ErrorUtil.getValidationException(e);
            ActionErrors errors = new ActionErrors();
            ActionMessage error;
            if (ve != null) {
                error = new ActionMessage(ve.getLocalizedMessage(), false);
            } else {
                error = new ActionMessage(ErrorUtil.getErrorMessage(e), false);
            }
            errors.add("participantValidation", error);
            addErrors(request, errors);
            return actionMapping.findForward("edit");
        }

        if(TabsAction.isDemo()) {
            return actionMapping.findForward("parentMap");
        } else {
            return actionMapping.findForward("parent");
        }
    }
    
    private boolean validateThreshold(CommDevDetailForm form){
    	
    	if(!this.getCoreAccess("feature.clientOfflineNotificationEnable")){
    		return true;
    	}
    	
    	
    	String summerThreshold = form.getThresholdsSummer();
    	String winterThreshold = form.getThresholdsUnSummer();
    	
    	boolean result = true;
		try{
			int st = Integer.valueOf(summerThreshold);
			if(st<0){
				throw new Exception();
			}
		}catch(Exception e){
			result = false;
		}
		try{
			int st = Integer.valueOf(winterThreshold);
			if(st<0){
				throw new Exception();
			}
		}catch(Exception e){
			result = false;
		}
		return result;
    }
    private static ProgramManager pm;

	public static ProgramManager getPm() {
		if(pm==null){
			pm = EJB3Factory.getBean(ProgramManager.class);
		}
		return pm;
	}
	public boolean isEnableCBPConsolidation(){
		return CBPUtil.isEnableCBPConsolidation(getPm().getAllPrograms());
	}

	/**
	 * Method getCreationDate. This provide the creation date of the SSO token and it gets added to the token, which later used to check its validity.
	 * 
	 * @return String
	 */
	public String getCreationDate(){
		String dtToday;
		SimpleDateFormat todayFormat = new SimpleDateFormat("MMddyyyyHHmmss");
		todayFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
		
		Date date = new Date();
		dtToday = todayFormat.format(date);
		return dtToday;
	}
	
}
