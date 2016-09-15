/*
 * www.akuacom.com - Automating Demand Response
 *
 * com.akuacom.pss2.web.commdev.CommDevDetailForm.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved.
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.web.commdev;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.akuacom.pss2.contact.Contact;
import com.akuacom.pss2.program.ProgramManager;
import com.akuacom.pss2.program.dl.DemandLimitingProgram;
import com.akuacom.pss2.program.participant.ProgramParticipant;
import com.akuacom.pss2.program.testProgram.TestProgram;
import com.akuacom.pss2.system.SystemManager;
import com.akuacom.pss2.system.property.PSS2Features;
import com.akuacom.pss2.util.CBPUtil;
import com.akuacom.pss2.util.DrasRole;
import com.akuacom.pss2.util.UserType;
import com.akuacom.pss2.web.util.EJBFactory;
import com.akuacom.utils.Tag;

/**
 * The Class CommDevDetailForm.
 */
public class CommDevDetailForm extends ActionForm {

    private static final long serialVersionUID = 3284388741093235457L;

    /**
     * The user name.
     */
    private String userName;

    /**
     * The password.
     */
    private String password;

    /**
     * The password2.
     */
    private String password2;

    /**
     * The account number.
     */
    private String accountNumber;
    
    private String secondaryAccountNumber;
    private boolean secondaryAccountNumberEnabled;

    /**
     * The type.
     */
    private String type;

    /**
     * The first name.
     */
    private String firstName;

    /**
     * The last name.
     */
    private String lastName;

    /**
     * The contact1.
     */
    private String contact1;

    /**
     * The contact2.
     */
    private String contact2;

    /**
     * The contact3.
     */
    private String contact3 = "";

    /**
     * The contact4.
     */
    private String contact4;

    /**
     * The contact type1.
     */
    private String contactType1;

    /**
     * The contact type2.
     */
    private String contactType2;

    /**
     * The contact type3.
     */
    private String contactType3;

    /**
     * The contact type4.
     */
    private String contactType4;

    /**
     * The feedback.
     */
    private boolean feedback;

    /**
     * The meter id.
     */
    private String meterId;

    /**
     * The address.
     */
    private String address;

    /**
     * The grid location.
     */
    private String gridLocation;

    /**
     * The latitude.
     */
    private double latitude;

    /**
     * The longitude.
     */
    private double longitude;

    /**
     * The shed per hour kw.
     */
    private double shedPerHourKW;

    /**
     * The programs.
     */
    private String[] programs;

    /**
     * The external contacts.
     */
    private List<Contact> externalContacts;

    /**
     * The admin user.
     */
    private boolean adminUser;

    /**
     * feature selects
     */
    private boolean featureLocation;
    private boolean featureFeedback;
    private boolean featureShedInfo;
    private boolean featureParticipantsMapView;
    private boolean featureParticipantsUpload;
    private boolean featureParticipantNotes;
    private boolean featureParticipantInfo;
    private boolean featureDemandLimiting;
    private boolean featureClientOfflineNotification;
    
    private boolean activated;
    private boolean dataEnabler;
    private boolean installer;

    /**
     * Aggregator
     */
    private boolean aggregator;

    /**
     * The types.
     */
    private List<Tag> types = null;

    /**
     * The contact types.
     */
    private List<Tag> contactTypes = null;

    /**
     * The all programs.
     */
    private List<Tag> allPrograms = null;

    /**
     * The Clients Configration.
     */
    private List<ProgramParticipant> clientsConfig = null;

    /**
     * The participant confirmation message
     */
    private String passwordConf = "";

    private String userType;

    private List<UserType> userTypeList = new ArrayList<UserType>();

    private boolean usageData;

    private boolean enableDataService;

    private boolean useDefaultFeatureValue;
    private boolean partEventOptoutEnabled;
    private boolean defaultEventOptoutEnabled;
    private boolean retained;
    private boolean userRoleEnabled;

    /* (non-Javadoc)
    * @see org.apache.struts.action.ActionForm#validate(org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
    */

    public boolean isUserRoleEnabled() {
		return userRoleEnabled;
	}

	public void setUserRoleEnabled(boolean userRoleEnabled) {
		this.userRoleEnabled = userRoleEnabled;
	}

	public boolean isRetained() {
		return retained;
	}

	public void setRetained(boolean retained) {
		this.retained = retained;
	}

	private String customerName;
    private String serviceStreetAddress;
    private String serviceCityName;
    private String zip;
    private String aBank;
    private String slap;
    private String pNode;
    private String servicePlan;
    private Date rateEffectiveDate;
    private String rateEffectiveDateStr;
    private Boolean directAccessParticipant = false;
    private Boolean testParticipant = false;
    private Boolean customer = false;
    private Boolean nonAutoDR = false;
    private Boolean uisActive = false;
    private String notes;
    private Date notesLastUpdate;
    private String notesLastUpdateStr;
    private Boolean demandLimiting = false;
    private String notesAuthor;

    // subaccount fields
    private String premiseNumber;
    private Date enrolledDate;
    private String enrolledDateStr;
    private Date startDate;
    private String startDateStr;
    private Date deactivateDate;
    private String deactivateDateStr;
    private String comments;

    //GridPoint component fields
    private String siteID;
    private String serviceProvider;
    
    private String bcdRepName;
	private Date autoDrProfileStartDate;
	private String autoDrProfileStartDateStr;
	
	private String[] selectedItems = {}; 
	private String[] items = {"Aggregator","Test Participant","Customer","Non-Auto DR"}; //Aggregator aggregator, Test Participant testParticipant, Customer, Non-Auto DR
	
	private boolean optOutClientOfflineNotification;
	private boolean clientOfflineNotificationEnable;
	private boolean clientOfflineNotificationAggEnable;
	private String thresholdsSummer;
	private String thresholdsUnSummer;

	private boolean enableCBPConsolidation;
	
	private int programOption;
	private String substation;
	private String blockNumber;
    /**
	 * @return the programOption
	 */
	public int getProgramOption() {
		return programOption;
	}

	/**
	 * @param programOption the programOption to set
	 */
	public void setProgramOption(int programOption) {
		this.programOption = programOption;
	}

	public String[] getSelectedItems() {
    	//
		return selectedItems;
	}

	public String[] getItems() {
		return items;
	}


	public void setSelectedItems(String[] selectedItems) {
		this.selectedItems = selectedItems;
	}

	public void setItems(String[] items) {
		this.items = items;
	}

	public ActionErrors validate(ActionMapping actionMapping, HttpServletRequest request) {
        ActionErrors errors = null;


        String dispatch = request.getParameter("dispatch");
        if ("save".equals(dispatch)) {

            errors = new ActionErrors();
            if (userName == null || userName.length() == 0) {
                errors.add("username", new ActionMessage("pss2.commDev.create.username"));
            } else if (userName.length() > 20) {
                errors.add("username", new ActionMessage("pss2.commDev.create.username.toolong"));
            }
            if (accountNumber != null && accountNumber.length() > 128) {
                errors.add("accountNumber", new ActionMessage("pss2.commDev.create.accountNumber"));
            }            
            if(applicationId != null && applicationId.length() > 128) {
            	errors.add("applicationId", new ActionMessage("pss2.commDev.create.applicationId"));
            }
            if (secondaryAccountNumber != null && secondaryAccountNumber.length() > 128) {
                errors.add("secondaryAccountNumber", new ActionMessage("pss2.commDev.create.accountNumber"));//FIXME: error message frank
            }
            if (password == null || password.length() == 0 || !password.equals(password2)) {
                errors.add("password", new ActionMessage("pss2.commDev.create.password"));
            } else {
                if (password.length() < 9) {
                    ActionMessage message = new ActionMessage("pss2.options.password.length");
                    errors.add("password.length", message);
                }
                if (password.length() > 24) {
                    ActionMessage message = new ActionMessage("pss2.options.password.toolong");
                    errors.add("password.toolong", message);
                }
                if (!password.matches(".*[A-Z].*")) {// at least one upper case required
                    ActionMessage message = new ActionMessage("pss2.options.password.upper");
                    errors.add("password.upper", message);
                }
                if (!password.matches(".*[a-z].*")) {// at least one lower case required
                    ActionMessage message = new ActionMessage("pss2.options.password.lower");
                    errors.add("password.lower", message);
                }
                if (!password.matches(".*\\d.*")) {// at least one digit required
                    ActionMessage message = new ActionMessage("pss2.options.password.digit");
                    errors.add("password.digit", message);
                }
                if (!password.matches(".*[\\._\\-/].*")) {// at least one special character required
                    ActionMessage message = new ActionMessage("pss2.options.password.nonword.clir");
                    errors.add("password.nonword", message);
                }
                if (password.matches(".*[\\W&&[^\\._\\-/]].*")) {
                    ActionMessage message = new ActionMessage("pss2.options.password.illegal.clir");
                    errors.add("password.nonword", message);
                }
            }
            try {
                if ((rateEffectiveDateStr != null) && (!rateEffectiveDateStr.isEmpty()))
                    rateEffectiveDate = new SimpleDateFormat("M/d/y").parse(rateEffectiveDateStr);
                else
                    rateEffectiveDate = null;
            } catch (ParseException e) {
                errors.add("pss2.commDev.edit.rateEffectiveDate.invalid",
                        new ActionMessage("pss2.commDev.edit.rateEffectiveDate.invalid",
                                rateEffectiveDateStr, "MM/dd/yyyy"));
            }


        } else if ("reset".equals(dispatch)) {

            errors = new ActionErrors();
            /*
            if (userName == null || userName.length() == 0) {
                errors.add("username", new ActionMessage("pss2.commDev.create.username"));
            }
            */
            if (accountNumber != null && accountNumber.length() > 128) {
                errors.add("accountNumber", new ActionMessage("pss2.commDev.create.accountNumber"));
            }
            if(applicationId != null && applicationId.length() > 128) {
            	errors.add("applicationId", new ActionMessage("pss2.commDev.create.applicationId"));
            }
            if (secondaryAccountNumber != null && secondaryAccountNumber.length() > 128) {
                errors.add("secondaryAccountNumber", new ActionMessage("pss2.commDev.create.accountNumber"));//FIXME:Frank error message
            }
            if (password == null || password.length() == 0 || !password.equals(password2)) {
                errors.add("password", new ActionMessage("pss2.commDev.create.password"));
            } else {
                if (password.length() < 9) {
                    ActionMessage message = new ActionMessage("pss2.options.password.length");
                    errors.add("password.length", message);
                }
                if (password.length() > 24) {
                    ActionMessage message = new ActionMessage("pss2.options.password.toolong");
                    errors.add("password.toolong", message);
                }
                if (!password.matches(".*[A-Z].*")) {// at least one upper case required
                    ActionMessage message = new ActionMessage("pss2.options.password.upper");
                    errors.add("password.upper", message);
                }
                if (!password.matches(".*[a-z].*")) {// at least one lower case required
                    ActionMessage message = new ActionMessage("pss2.options.password.lower");
                    errors.add("password.lower", message);
                }
                if (!password.matches(".*\\d.*")) {// at least one digit required
                    ActionMessage message = new ActionMessage("pss2.options.password.digit");
                    errors.add("password.digit", message);
                }
                if (!password.matches(".*[\\._\\-/].*")) {// at least one special character required
                    ActionMessage message = new ActionMessage("pss2.options.password.nonword.clir");
                    errors.add("password.nonword", message);
                }
                if (password.matches(".*[\\W&&[^\\._\\-/]].*")) {
                    ActionMessage message = new ActionMessage("pss2.options.password.illegal.clir");
                    errors.add("password.nonword", message);
                }
            }
        }


        // TODO: add validation for address, gridLocation, latitude, and longitude
        /*
              if (errors.isEmpty()) {
                  errors = null;
              }
              */

        return errors;
    }

    /* (non-Javadoc)
     * @see org.apache.struts.action.ActionForm#reset(org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
     */
    @Override
    public void reset(ActionMapping actionMapping, HttpServletRequest httpServletRequest) {
        super.reset(actionMapping, httpServletRequest);
        setAdminUser(httpServletRequest.isUserInRole(DrasRole.Admin.toString())
                || httpServletRequest.isUserInRole(DrasRole.Operator.toString()));
        SystemManager systemManager = EJBFactory.getBean(SystemManager.class);
        PSS2Features pss2Features = systemManager.getPss2Features();
	   	this.setSecondaryAccountNumberEnabled(pss2Features.isSecondaryAccountEnabled());
	   	this.setApplicationIdEnabled(pss2Features.isApplicationIdEnabled());
    }

    public boolean isUsageData() {
        return usageData;
    }

    public void setUsageData(boolean usageData) {
        this.usageData = usageData;
    }

    public boolean isInstaller() {
        return installer;
    }

    public void setInstaller(boolean installer) {
        this.installer = installer;
    }

    /**
     * Gets the account number.
     *
     * @return the account number
     */
    public String getAccountNumber() {
        return accountNumber;
    }

    /**
     * Sets the account number.
     *
     * @param accountNumber the new account number
     */
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    /**
     * Gets the contact1.
     *
     * @return the contact1
     */
    public String getContact1() {
        return contact1;
    }

    /**
     * Sets the contact1.
     *
     * @param contact1 the new contact1
     */
    public void setContact1(String contact1) {
        this.contact1 = contact1;
    }

    /**
     * Gets the contact2.
     *
     * @return the contact2
     */
    public String getContact2() {
        return contact2;
    }

    /**
     * Sets the contact2.
     *
     * @param contact2 the new contact2
     */
    public void setContact2(String contact2) {
        this.contact2 = contact2;
    }

    /**
     * Gets the contact3.
     *
     * @return the contact3
     */
    public String getContact3() {
        return contact3;
    }

    /**
     * Sets the contact3.
     *
     * @param contact3 the new contact3
     */
    public void setContact3(String contact3) {
        this.contact3 = contact3;
    }

    /**
     * Gets the contact4.
     *
     * @return the contact4
     */
    public String getContact4() {
        return contact4;
    }

    /**
     * Sets the contact4.
     *
     * @param contact4 the new contact4
     */
    public void setContact4(String contact4) {
        this.contact4 = contact4;
    }

    /**
     * Gets the contact type1.
     *
     * @return the contact type1
     */
    public String getContactType1() {
        return contactType1;
    }

    /**
     * Sets the contact type1.
     *
     * @param contactType1 the new contact type1
     */
    public void setContactType1(String contactType1) {
        this.contactType1 = contactType1;
    }

    /**
     * Gets the contact type2.
     *
     * @return the contact type2
     */
    public String getContactType2() {
        return contactType2;
    }

    /**
     * Sets the contact type2.
     *
     * @param contactType2 the new contact type2
     */
    public void setContactType2(String contactType2) {
        this.contactType2 = contactType2;
    }

    /**
     * Gets the contact type3.
     *
     * @return the contact type3
     */
    public String getContactType3() {
        return contactType3;
    }

    /**
     * Sets the contact type3.
     *
     * @param contactType3 the new contact type3
     */
    public void setContactType3(String contactType3) {
        this.contactType3 = contactType3;
    }

    /**
     * Gets the contact type4.
     *
     * @return the contact type4
     */
    public String getContactType4() {
        return contactType4;
    }

    /**
     * Sets the contact type4.
     *
     * @param contactType4 the new contact type4
     */
    public void setContactType4(String contactType4) {
        this.contactType4 = contactType4;
    }

    /**
     * Gets Password confirmation status.
     *
     * @return passwordRestConfirmation
     */
    public String getPasswordConf() {
        return passwordConf;
    }

    /**
     * Sets the Password confirmation status.
     *
     * @param passwordConf password conf
     */
    public void setPasswordConf(String passwordConf) {
        this.passwordConf = passwordConf;
    }

    /**
     * Gets the first name.
     *
     * @return the first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name.
     *
     * @param firstName the new first name
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Gets the last name.
     *
     * @return the last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name.
     *
     * @param lastName the new last name
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Gets the password2.
     *
     * @return the password2
     */
    public String getPassword2() {
        return password2;
    }

    /**
     * Sets the password2.
     *
     * @param password2 the new password2
     */
    public void setPassword2(String password2) {
        this.password2 = password2;
    }

    /**
     * Gets the password.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password.
     *
     * @param password the new password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets the programs.
     *
     * @return the programs
     */
    public String[] getPrograms() {
        if (programs == null) {
            return new String[]{};
        }
        return programs;
    }

    /**
     * Sets the programs.
     *
     * @param programs the new programs
     */
    public void setPrograms(String[] programs) {
        this.programs = programs;
    }

    /**
     * Gets the user name.
     *
     * @return the user name
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Sets the user name.
     *
     * @param userName the new user name
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Gets the type.
     *
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the type.
     *
     * @param type the new type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Checks if is feedback.
     *
     * @return true, if is feedback
     */
    public boolean isFeedback() {
        return feedback;
    }

    /**
     * Sets the feedback.
     *
     * @param feedback the new feedback
     */
    public void setFeedback(boolean feedback) {
        this.feedback = feedback;
    }

    /**
     * Gets the meter id.
     *
     * @return the meter id
     */
    public String getMeterId() {
        return meterId;
    }

    /**
     * Sets the meter id.
     *
     * @param meterId the new meter id
     */
    public void setMeterId(String meterId) {
        this.meterId = meterId;
    }

    /**
     * Gets the address.
     *
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets the address.
     *
     * @param address the new address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Gets the grid location.
     *
     * @return the grid location
     */
    public String getGridLocation() {
        return gridLocation;
    }

    /**
     * Sets the grid location.
     *
     * @param gridLocation the new grid location
     */
    public void setGridLocation(String gridLocation) {
        this.gridLocation = gridLocation;
    }

    /**
     * Gets the latitude.
     *
     * @return the latitude
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Sets the latitude.
     *
     * @param latitude the new latitude
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * Gets the longitude.
     *
     * @return the longitude
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Sets the longitude.
     *
     * @param longitude the new longitude
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    /**
     * Gets the shed per hour kw.
     *
     * @return the shed per hour kw
     */
    public double getShedPerHourKW() {
        return shedPerHourKW;
    }

    /**
     * Sets the shed per hour kw.
     *
     * @param shedPerHourKW the new shed per hour kw
     */
    public void setShedPerHourKW(double shedPerHourKW) {
        this.shedPerHourKW = shedPerHourKW;
    }

    /**
     * Gets the contact types.
     *
     * @return the contact types
     */
    public List<Tag> getContactTypes() {
        if (contactTypes == null) {
            contactTypes = new ArrayList<Tag>();
            Tag t1 = new Tag("Email Address", "Email Address");
            contactTypes.add(t1);
            Tag t2 = new Tag("Fax Number", "Fax Number");
            contactTypes.add(t2);
            Tag t3 = new Tag("Phone Number", "Phone Number");
            contactTypes.add(t3);
            Tag t4 = new Tag("Pager", "Pager");
            contactTypes.add(t4);
        }
        return contactTypes;
    }

    /**
     * Gets the types.
     *
     * @return the types
     */
    public List<Tag> getTypes() {
        if (types == null) {
            types = new ArrayList<Tag>();
            Tag t1 = new Tag("AUTO", "AUTO");
            types.add(t1);
            Tag t2 = new Tag("MANUAL", "MANUAL");
            types.add(t2);
        }
        return types;
    }

    /**
     * Gets the external contacts.
     *
     * @return the external contacts
     */
    public List<Contact> getExternalContacts() {
        return externalContacts;
    }

    /**
     * Sets the external contacts.
     *
     * @param externalContacts the new external contacts
     */
    public void setExternalContacts(List<Contact> externalContacts) {
        this.externalContacts = externalContacts;
    }

    /**
     * Gets the all programs.
     *
     * @return the all programs
     */

    public List<Tag> getAllPrograms() {
        allPrograms = new ArrayList<Tag>();
        List<String> programs;
        if (this.programs != null) {
            programs = Arrays.asList(this.programs);
        } else {
            programs = new ArrayList<String>();
        }

        List<String> allList = EJBFactory.getBean(ProgramManager.class).getPrograms();
        if(this.enableCBPConsolidation){
        	allList = CBPUtil.transferList(allList);
        }
        for (String program : allList) {
            if ((!program.equals(TestProgram.PROGRAM_NAME)) && (!(program.equals(DemandLimitingProgram.PROGRAM_NAME) && !isFeatureDemandLimiting()))) {
                Tag tag = new Tag();
                tag.setName(program);
                tag.setValue(Boolean.toString(programs.contains(program)));
                allPrograms.add(tag);
            }
        }
        return allPrograms;
    }



    /**
     * Sets the clientsConfig.
     */
    public List<ProgramParticipant> getClientsConfig() {
        return clientsConfig;
    }

    /**
     * Sets the all Clients Config.
     *
     * @param clientsConfig the new clientsConfig
     */
    public void setClientsConfig(List<ProgramParticipant> clientsConfig) {
        this.clientsConfig = clientsConfig;
    }


    /**
     * Checks if is admin user.
     *
     * @return true, if is admin user
     */
    public boolean isAdminUser() {
        return adminUser;
    }

    /**
     * Sets the admin user.
     *
     * @param adminUser the new admin user
     */
    public void setAdminUser(boolean adminUser) {
        this.adminUser = adminUser;
    }

    public boolean isFeatureLocation() {
        return featureLocation;
    }

    public void setFeatureLocation(boolean featureLocation) {
        this.featureLocation = featureLocation;
    }

    public boolean isFeatureFeedback() {
        return featureFeedback;
    }

    public void setFeatureFeedback(boolean featureFeedback) {
        this.featureFeedback = featureFeedback;
    }

    public boolean isFeatureShedInfo() {
        return featureShedInfo;
    }

    public void setFeatureShedInfo(boolean featureShedInfo) {
        this.featureShedInfo = featureShedInfo;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {

        this.activated = activated;
    }

    public boolean isDataEnabler() {
        return dataEnabler;
    }


    public void setDataEnabler(boolean dataEnabler) {
        this.dataEnabler = dataEnabler;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public List<UserType> getUserTypeList() {
        Collections.addAll(userTypeList, UserType.values());

        return userTypeList;
    }

    public void setUserTypeList(List<UserType> userTypeList) {
        this.userTypeList = userTypeList;
    }

    public boolean isAggregator() {
        return aggregator;
    }

    public void setAggregator(boolean aggregator) {
        this.aggregator = aggregator;
    }


    public boolean isFeatureParticipantsMapView() {
        return featureParticipantsMapView;
    }

    public void setFeatureParticipantsMapView(boolean featureParticipantsMapView) {
        this.featureParticipantsMapView = featureParticipantsMapView;
    }


    public boolean isFeatureParticipantsUpload() {
        return featureParticipantsUpload;
    }

    public void setFeatureParticipantsUpload(boolean featureParticipantsUpload) {
        this.featureParticipantsUpload = featureParticipantsUpload;
    }

    public boolean isFeatureParticipantNotes() {
        return featureParticipantNotes;
    }

    public void setFeatureParticipantNotes(boolean featureParticipantNotes) {
        this.featureParticipantNotes = featureParticipantNotes;
    }

    public boolean isFeatureParticipantInfo() {
        return featureParticipantInfo;
    }

    public void setFeatureParticipantInfo(boolean featureParticipantInfo) {
        this.featureParticipantInfo = featureParticipantInfo;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getServiceStreetAddress() {
        return serviceStreetAddress;
    }

    public void setServiceStreetAddress(String serviceStreetAddress) {
        this.serviceStreetAddress = serviceStreetAddress;
    }

    public String getServiceCityName() {
        return serviceCityName;
    }

    public void setServiceCityName(String serviceCityName) {
        this.serviceCityName = serviceCityName;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getABank() {
        return aBank;
    }

    public void setABank(String aBank) {
        this.aBank = aBank;
    }

    public String getSlap() {
        return slap;
    }

    public void setSlap(String slap) {
        this.slap = slap;
    }

    public String getPNode() {
        return pNode;
    }

    public void setPNode(String pNode) {
        this.pNode = pNode;
    }

    public String getServicePlan() {
        return servicePlan;
    }

    public void setServicePlan(String servicePlan) {
        this.servicePlan = servicePlan;
    }

    public Date getRateEffectiveDate() {
        return rateEffectiveDate;
    }

    public void setRateEffectiveDate(Date rateEffectiveDate) {
        this.rateEffectiveDate = rateEffectiveDate;
    }

    public Boolean getDirectAccessParticipant() {
        return directAccessParticipant;
    }

    public void setDirectAccessParticipant(Boolean directAccessParticipant) {
        this.directAccessParticipant = directAccessParticipant;
    }

    public void setUisActive(Boolean uisActive) {
        this.uisActive = uisActive;
    }

    public Boolean getUisActive() {
        return uisActive;
    }

    public void setContactTypes(List<Tag> contactTypes) {
        this.contactTypes = contactTypes;
    }

    public void setTestParticipant(Boolean testParticipant) {
        this.testParticipant = testParticipant;
    }

    public Boolean getTestParticipant() {
        return testParticipant;
    }

    public void setRateEffectiveDateStr(String rateEffectiveDateStr) {
        this.rateEffectiveDateStr = rateEffectiveDateStr;
    }

    public String getRateEffectiveDateStr() {
        return rateEffectiveDateStr;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Boolean getDemandLimiting() {
        return demandLimiting;
    }

    public void setDemandLimiting(Boolean demandLimiting) {
        this.demandLimiting = demandLimiting;
    }

    public void setFeatureDemandLimiting(boolean featureDemandLimiting) {
        this.featureDemandLimiting = featureDemandLimiting;
    }

    public boolean isFeatureDemandLimiting() {
        return featureDemandLimiting;
    }

    public void setNotesLastUpdate(Date notesLastUpdate) {
        this.notesLastUpdate = notesLastUpdate;
    }

    public Date getNotesLastUpdate() {
        return notesLastUpdate;
    }

    public void setNotesLastUpdateStr(String notesLastUpdateStr) {
        this.notesLastUpdateStr = notesLastUpdateStr;
    }

    public String getNotesLastUpdateStr() {
        return notesLastUpdateStr;
    }

    public void setNotesAuthor(String notesAuthor) {
        this.notesAuthor = notesAuthor;
    }

    public String getNotesAuthor() {
        return notesAuthor;
    }

    public Boolean getDisplayParticipantNotesUpdateInfo() {
        return (notesLastUpdateStr != null) && (notesLastUpdateStr.length() > 0) && (notesAuthor != null) && (notesAuthor.length() > 0);
    }

    public String getPremiseNumber() {
        return premiseNumber;
    }

    public void setPremiseNumber(String premiseNumber) {
        this.premiseNumber = premiseNumber;
    }

    public Date getEnrolledDate() {
        return enrolledDate;
    }

    public void setEnrolledDate(Date enrolledDate) {
        this.enrolledDate = enrolledDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getDeactivateDate() {
        return deactivateDate;
    }

    public void setDeactivateDate(Date deactivateDate) {
        this.deactivateDate = deactivateDate;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getEnrolledDateStr() {
        return enrolledDateStr;
    }

    public void setEnrolledDateStr(String enrolledDateStr) {
        this.enrolledDateStr = enrolledDateStr;
    }

    public String getStartDateStr() {
        return startDateStr;
    }

    public void setStartDateStr(String startDateStr) {
        this.startDateStr = startDateStr;
    }

    public String getDeactivateDateStr() {
        return deactivateDateStr;
    }

    public void setDeactivateDateStr(String deactivateDateStr) {
        this.deactivateDateStr = deactivateDateStr;
    }

    public String getSiteID() {
        return siteID;
    }

    public void setSiteID(String siteID) {
        this.siteID = siteID;
    }

    public String getServiceProvider() {
        return serviceProvider;
    }

    public void setServiceProvider(String serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    public void setEnableDataService(boolean enableDataService) {
        this.enableDataService = enableDataService;
    }

    public boolean isEnableDataService() {
        return enableDataService;
    }

	public boolean isUseDefaultFeatureValue() {
		return useDefaultFeatureValue;
	}

	public void setUseDefaultFeatureValue(boolean useDefaultFeatureValue) {
		this.useDefaultFeatureValue = useDefaultFeatureValue;
	}

	public boolean isPartEventOptoutEnabled() {
		return partEventOptoutEnabled;
	}

	public void setPartEventOptoutEnabled(boolean partEventOptoutEnabled) {
		this.partEventOptoutEnabled = partEventOptoutEnabled;
	}

	public boolean isDefaultEventOptoutEnabled() {
		return defaultEventOptoutEnabled;
	}

	public void setDefaultEventOptoutEnabled(boolean defaultEventOptoutEnabled) {
		this.defaultEventOptoutEnabled = defaultEventOptoutEnabled;
	}

	public String getBcdRepName() {
		return bcdRepName;
	}

	public void setBcdRepName(String bcdRepName) {
		this.bcdRepName = bcdRepName;
	}

	public Date getAutoDrProfileStartDate() {
		return autoDrProfileStartDate;
	}

	public void setAutoDrProfileStartDate(Date autoDrProfileStartDate) {
		this.autoDrProfileStartDate = autoDrProfileStartDate;
	}

	public String getAutoDrProfileStartDateStr() {
		return autoDrProfileStartDateStr;
	}

	public void setAutoDrProfileStartDateStr(String autoDrProfileStartDateStr) {
		this.autoDrProfileStartDateStr = autoDrProfileStartDateStr;
	}

	public Boolean getCustomer() {
		return customer;
	}

	public void setCustomer(Boolean customer) {
		this.customer = customer;
	}

	public Boolean getNonAutoDR() {
		return nonAutoDR;
	}

	public void setNonAutoDR(Boolean nonAutoDR) {
		this.nonAutoDR = nonAutoDR;
	}

	public String getSecondaryAccountNumber() {
		return secondaryAccountNumber;
	}

	public void setSecondaryAccountNumber(String secondaryAccountNumber) {
		this.secondaryAccountNumber = secondaryAccountNumber;
	}

	public boolean isSecondaryAccountNumberEnabled() {
		return secondaryAccountNumberEnabled;
	}

	public void setSecondaryAccountNumberEnabled(
			boolean secondaryAccountNumberEnabled) {
		this.secondaryAccountNumberEnabled = secondaryAccountNumberEnabled;
	}

	/**
	 * @return the featureClientOfflineNotification
	 */
	public boolean isFeatureClientOfflineNotification() {
		return featureClientOfflineNotification;
	}

	/**
	 * @param featureClientOfflineNotification the featureClientOfflineNotification to set
	 */
	public void setFeatureClientOfflineNotification(
			boolean featureClientOfflineNotification) {
		this.featureClientOfflineNotification = featureClientOfflineNotification;
	}

	/**
	 * @return the optOutClientOfflineNotification
	 */
	public boolean isOptOutClientOfflineNotification() {
		return optOutClientOfflineNotification;
	}

	/**
	 * @param optOutClientOfflineNotification the optOutClientOfflineNotification to set
	 */
	public void setOptOutClientOfflineNotification(
			boolean optOutClientOfflineNotification) {
		this.optOutClientOfflineNotification = optOutClientOfflineNotification;
	}

	/**
	 * @return the clientOfflineNotificationEnable
	 */
	public boolean isClientOfflineNotificationEnable() {
		return clientOfflineNotificationEnable;
	}

	/**
	 * @param clientOfflineNotificationEnable the clientOfflineNotificationEnable to set
	 */
	public void setClientOfflineNotificationEnable(
			boolean clientOfflineNotificationEnable) {
		this.clientOfflineNotificationEnable = clientOfflineNotificationEnable;
	}



	/**
	 * @return the thresholdsSummer
	 */
	public String getThresholdsSummer() {
		return thresholdsSummer;
	}

	/**
	 * @param thresholdsSummer the thresholdsSummer to set
	 */
	public void setThresholdsSummer(String thresholdsSummer) {
		this.thresholdsSummer = thresholdsSummer;
	}

	/**
	 * @return the thresholdsUnSummer
	 */
	public String getThresholdsUnSummer() {
		return thresholdsUnSummer;
	}

	/**
	 * @param thresholdsUnSummer the thresholdsUnSummer to set
	 */
	public void setThresholdsUnSummer(String thresholdsUnSummer) {
		this.thresholdsUnSummer = thresholdsUnSummer;
	}

	/**
	 * @return the clientOfflineNotificationAggEnable
	 */
	public boolean isClientOfflineNotificationAggEnable() {
		return clientOfflineNotificationAggEnable;
	}

	/**
	 * @param clientOfflineNotificationAggEnable the clientOfflineNotificationAggEnable to set
	 */
	public void setClientOfflineNotificationAggEnable(
			boolean clientOfflineNotificationAggEnable) {
		this.clientOfflineNotificationAggEnable = clientOfflineNotificationAggEnable;
	}

	/**
	 * @param allPrograms the allPrograms to set
	 */
	public void setAllPrograms(List<Tag> allPrograms) {
		this.allPrograms = allPrograms;
	}

	/**
	 * @param enableCBPConsolidation
	 */
	public void setFeatureCBDConsolidation(boolean enableCBPConsolidation) {
		// TODO Auto-generated method stub
		this.enableCBPConsolidation = enableCBPConsolidation;
	}

	/**
	 * @return the enableCBPConsolidation
	 */
	public boolean isEnableCBPConsolidation() {
		return enableCBPConsolidation;
	}

	/**
	 * @param enableCBPConsolidation the enableCBPConsolidation to set
	 */
	public void setEnableCBPConsolidation(boolean enableCBPConsolidation) {
		this.enableCBPConsolidation = enableCBPConsolidation;
	}

	/**
	 * @return the substation
	 */
	public String getSubstation() {
		return substation;
	}

	/**
	 * @param substation the substation to set
	 */
	public void setSubstation(String substation) {
		this.substation = substation;
	}

	/**
	 * @return the blockNumber
	 */
	public String getBlockNumber() {
		return blockNumber;
	}

	/**
	 * @param blockNumber the blockNumber to set
	 */
	public void setBlockNumber(String blockNumber) {
		this.blockNumber = blockNumber;
	}
	
	private String applicationId;
	private boolean applicationIdEnabled;

	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	public boolean isApplicationIdEnabled() {
		return applicationIdEnabled;
	}

	public void setApplicationIdEnabled(boolean applicationIdEnabled) {
		this.applicationIdEnabled = applicationIdEnabled;
	}		
}
