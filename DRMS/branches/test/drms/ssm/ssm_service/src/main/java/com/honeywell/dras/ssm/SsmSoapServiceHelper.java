package com.honeywell.dras.ssm;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.xml.ws.WebServiceContext;

import org.apache.log4j.Logger;

import com.akuacom.ejb.client.EJB3Factory;
import com.akuacom.pss2.client.ClientManager;
import com.akuacom.pss2.contact.Contact;
import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.core.ErrorUtil;
import com.akuacom.pss2.core.ValidationException;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.participant.ParticipantEAO;
import com.akuacom.pss2.participant.ParticipantManager;
import com.akuacom.pss2.participant.contact.ContactEventNotificationType;
import com.akuacom.pss2.participant.contact.ParticipantContact;
import com.akuacom.pss2.program.ProgramManager;
import com.akuacom.pss2.program.participant.ProgramParticipant;
import com.akuacom.pss2.program.participant.ProgramParticipantAggregationManager;
import com.akuacom.pss2.program.participant.ProgramParticipantAggregationManagerBean;
import com.akuacom.pss2.program.participant.ProgramParticipantManager;
import com.akuacom.pss2.system.SystemManager;
import com.akuacom.pss2.system.property.PSS2Features;
import com.akuacom.pss2.util.EncryptUtil;
import com.akuacom.pss2.util.UserType;
import com.honeywell.dras.ssm.api.request.EnrollAggregatorRequest;
import com.honeywell.dras.ssm.api.request.EnrollCustomerRequest;
import com.honeywell.dras.ssm.api.request.ParticipantRequest;
import com.honeywell.dras.ssm.api.request.data.AggregatorEnrollmentData;
import com.honeywell.dras.ssm.api.request.data.CustomerEnrollmentData;
import com.honeywell.dras.ssm.api.response.EnrollAggregatorResponse;
import com.honeywell.dras.ssm.api.response.EnrollCustomerResponse;
import com.honeywell.dras.ssm.api.response.GetProgramResponse;
import com.honeywell.dras.ssm.api.response.ParticipantResponse;


public class SsmSoapServiceHelper {

	private static final String PRIVATEKEY = "ssmprivate.key";

	private ProgramParticipantAggregationManager ppAggregationManager;
	public static final String SHED_TYPE_ADVANCED="ADVANCED";
	private Logger logger = Logger.getLogger(this.getClass());
	WebServiceContext wsctx;
	private List<String> userList;
	private List<String> passList;
	private String username;
	private String password;
	
	public SsmSoapServiceHelper(){
	}
	
	public GetProgramResponse getProgramList(){
		GetProgramResponse response = new GetProgramResponse();
		ProgramManager programManager =  getProgramManager();
		if(null == programManager){
			return response;
		}
		response.setProgramList(programManager.getPrograms());
		return response;
	}
	
	public EnrollCustomerResponse enrollCustomerInDras(
			EnrollCustomerRequest request) {
		EnrollCustomerResponse response = new EnrollCustomerResponse();
		try {
		    EncryptUtil passwordEncoder = new EncryptUtil();
		    InputStream inputStream = null;
		    inputStream = SsmSoapServiceHelper.class.getClassLoader().getResourceAsStream(PRIVATEKEY);
			ObjectInputStream keyinputStream = null;
		    keyinputStream = new ObjectInputStream(inputStream);
			final PrivateKey privateKey = (PrivateKey) keyinputStream.readObject();
			response.setError(false);
			validateCustomerEnrollmentRequest(request);
			createParticipant(request.getCustomerEnrollmentData(), privateKey, passwordEncoder);
		} catch (IOException ioe) {
			response.setError(true);
			logger.error("Exception on decryption : " + ioe.getMessage());
			String errMsg = getParticipantCreationErrorMsg(ioe);
			response.setErrorMessage(errMsg);
		} catch (Exception e) {
			response.setError(true);
			String errMsg = getParticipantCreationErrorMsg(e);
			response.setErrorMessage(errMsg);
		}
		
		return response;
	}
	
	public EnrollAggregatorResponse enrollAggregatorInDras(EnrollAggregatorRequest request){
		EnrollAggregatorResponse response = new EnrollAggregatorResponse();
		response.setError(false);
		try {
			validateAggregatorEnrollmentRequest(request);
			List<String> participantList = createCustomerForAggregator(request.getAggregatorEnrollmentData());
			boolean aggregatorExists = isParticipantExist(request.getAggregatorEnrollmentData().getCustomerName());
			logger.info("[START OF AGGRIGATION]");
			if ( !aggregatorExists ) {
				removeParticipants(participantList);
				response.setError(true);
				response.setErrorMessage("Enrollment type is existing aggregator but does not exist in DRAS");
			} else {
				getParticipantManager().createAggregatorHierarchy(request.getAggregatorEnrollmentData());
			}
			aggregatorExists = isParticipantExist(request.getAggregatorEnrollmentData().getCustomerName());
			if ( !aggregatorExists ) {
				removeParticipants(participantList);
				response.setError(true);
				response.setErrorMessage("Enrollment type is existing aggregator but does not exist in DRAS");
			}
			logger.info("Is Aggregator participant exist in DRAS : " + aggregatorExists);
			logger.info("[END OF AGGRIGATION]");
		} catch (Exception e) {
			response.setError(true);
			String errMsg = getParticipantCreationErrorMsg(e);
			response.setErrorMessage(errMsg);
		}
		return response;
	}
	
	public ParticipantResponse checkAccountNumberExist(ParticipantRequest request) {
		ParticipantResponse response = new ParticipantResponse();
		response.setAccountNumberExist(isAccountNoExist(request.getAccountNumber()));
		return response;
	}
	
	public ParticipantResponse checkParticipantExist(ParticipantRequest request) {
		ParticipantResponse response = new ParticipantResponse();
		
		if ( request.isReqIdExists() ) {
			response.setParticipantExist(isPartWithReqIdExist(request.getParticipantName()));
		} else {
			response.setParticipantExist(isParticipantExist(request.getParticipantName()));
		}
		return response;
	}
	
	public ParticipantResponse checkAggregationEnrollmentStatus(ParticipantRequest request) {
		ParticipantResponse response = new ParticipantResponse();
		response.setParticipantExist(isParticipantExist(request.getParticipantName()));
		return response;
	}
	
	private void validateAggregatorEnrollmentRequest(EnrollAggregatorRequest request)
			throws Exception {
		if (null == request || null == request.getAggregatorEnrollmentData()) {
			throw new ValidationException("Request object/data should not be null");
		}
		AggregatorEnrollmentData enrollmentData = request.getAggregatorEnrollmentData();
		if(null == enrollmentData.getChilds() || 0== enrollmentData.getChilds().size()){
			throw new ValidationException("Aggregator can not be enrolled without child ");
		}
		validateAggregatorEnrollmentData(enrollmentData);
		ValidateAggregationProgram(enrollmentData);
	}
	
	private void validateAggregatorEnrollmentData(AggregatorEnrollmentData aggEnrollmentData) throws Exception{
		CustomerEnrollmentData enrollmentData = aggEnrollmentData.getAggregatorEnrollmentInfo();
		validateCustomerName(enrollmentData.getCustomerName());
		List<CustomerEnrollmentData> childs = aggEnrollmentData.getChilds();
		for(CustomerEnrollmentData cusData : childs){
			validateCustomerEnrollmentData(cusData);
		}
		if(aggEnrollmentData.getIsNew()){
			return;
		}
		if(!isParticipantExist(aggEnrollmentData.getCustomerName())){
			throw new ValidationException("Enrollment type is existing aggregator but does not exist in DRAS");
		}
	}
	private void ValidateAggregationProgram(AggregatorEnrollmentData aggEnrollmentData) throws Exception{
			Participant participant = getParticipantDetails(aggEnrollmentData.getCustomerName());
			List<String> aggPrograms = new ArrayList<String>();
			ProgramParticipant programParticipant = null;
			Set<ProgramParticipant> programParticipantSet = participant.getProgramParticipants();
			Iterator<ProgramParticipant> iter = programParticipantSet.iterator();
			
			while (iter.hasNext()) {
				programParticipant = iter.next();
				aggPrograms.add(programParticipant.getProgramName());
			}
		     List<CustomerEnrollmentData> childs = aggEnrollmentData.getChilds();
		     for(CustomerEnrollmentData chData : childs){
		    	 if(Collections.disjoint(aggPrograms, chData.getProgramList())){
		    		 String name = chData.getCustomerName();
		    		 String aggName = aggEnrollmentData.getCustomerName();
		    		 String errMsg = "Customer : "+name +" is not participating in aggregator : "+aggName+"'s program";
		    		 throw new ValidationException(errMsg);
		    	 }
		    	 
		     }
	}
	private List<String> createCustomerForAggregator(AggregatorEnrollmentData aggEnrollmentData) throws Exception {
//		List<String> participantList = new ArrayList<String>();
//		try{
		List<String> participantList = getParticipantManager().enrollAggregatorInDras(aggEnrollmentData);
//			getParticipantManager().enrollAggregatorClientInDras(aggEnrollmentData);
//			getParticipantManager().enrollAggregatorUserInDras(aggEnrollmentData);
//		} catch (Exception exc) {
//			logger.error("Exception on creating aggregation:: Participant List already created: " + participantList);
//			removeCustomerCreatedOnAggregator(participantList);
//			logger.error("Exception on creating aggregation:: Participant List already created: " + participantList);
//			throw new ValidationException("Aggregation was unsuccessful, please try again later");
//		}
		return participantList;
	}
	
/*	private void removeCustomerCreatedOnAggregator(List<String> participantList) {
		try {
			logger.info("Rolling back the participant created :: Participant List : " + participantList);
			List<String> tempParticipantList = new ArrayList<String>(participantList);
			for(String participantName : participantList){
				removeParticipant(participantName);
				tempParticipantList.remove(participantName);
			}
			logger.info("Rolled back the participant created :: participantList :: " + participantList+" unsuccessful roll back participants : " + tempParticipantList);
		} catch (Exception exe) {
			logger.error("Exception on removing " + exe.getMessage());
		}
	}*/
	
	private void validateCustomerEnrollmentRequest(EnrollCustomerRequest request)
			throws Exception {
		if (null == request || null == request.getCustomerEnrollmentData()) {
			throw new ValidationException("Request object/data should not be null");
		}
		CustomerEnrollmentData enrollmentData = request
				.getCustomerEnrollmentData();
		/*validateCustomerName(enrollmentData.getCustomerName());
		validateProgram(enrollmentData.getPrograms());
		validateAccountNumber(enrollmentData.getAccountNumber());
		validatePassword(enrollmentData.getPassword());*/
		validateCustomerEnrollmentData(enrollmentData);
	}
	
	private void validateCustomerEnrollmentData(CustomerEnrollmentData enrollmentData) throws Exception{
		validateCustomerName(enrollmentData.getCustomerName());
//		validateProgram(enrollmentData.getPrograms());
		validateAccountNumber(enrollmentData.getAccountNumber());
		validatePassword(enrollmentData.getPassword());
	}

	private void validateCustomerName(String customerName) throws Exception {
		if(null == customerName || !customerName.equals(customerName)){
			throw new ValidationException("Customer can not be enrolled without name");
		}
		if(!customerName.equals(specialCharValidator(customerName,true))){
			throw new ValidationException("Customer can not be enrolled with name containing special chars");
		}
	}
	private void validateProgram(String[] programs) throws Exception {
		if(null == programs || 0 == programs.length){
			throw new ValidationException("Customer can not be enrolled without program");
		}
	}
	private void validateAccountNumber(String accNumber) throws Exception {
		if(null == accNumber || accNumber.isEmpty()){
			throw new ValidationException("Customer can not be enrolled without account number");
		}
//		try {
//			Integer.parseInt(accNumber);
//		} catch (Exception e) {
//			throw new ValidationException("Invalid account number");
//		}

	}
	private void validatePassword(String password) throws Exception {
		if(null == password || password.isEmpty()){
			throw new ValidationException("Customer can not be enrolled without password");
		}

	}
	private boolean isParticipantExist(String participantName){
		boolean participantExist = false;
		Participant participant = getParticipantDetails(participantName);
		if(null != participant){
			participantExist = true;
		}
		return participantExist;
	}
	
	private boolean isPartWithReqIdExist(String participantName) {
		boolean participantExist = false;
		Participant participant = getParticipantDetails(participantName);
		
		if(null != participant){
			String requestId = participant.getRequestId();
			if ( requestId != null && !requestId.isEmpty() ) {
				participantExist = true;
			}
		}
		return participantExist;
	}
	
	private boolean isAccountNoExist(String accountNumber){
		ParticipantEAO participantEAO = EJB3Factory
				.getLocalBean(ParticipantEAO.class);
		boolean found = participantEAO.checkAccount(accountNumber);
		return found;
	}
	

	public ParticipantResponse getAllAccountNos(ParticipantRequest request) {
		ParticipantResponse response = new ParticipantResponse();
		List<String> accountList = request.getServiceAccList();
		boolean found = false;
		ParticipantEAO participantEAO = EJB3Factory
				.getLocalBean(ParticipantEAO.class);
		List<Participant> partList = participantEAO.findParticipantsByAccounts(accountList);
		
		if ( partList.size() > 0 ) {
			found = true;
		}
		response.setAccountNumberExist(found);
		return response;
	}
	
	public ParticipantResponse getAllAccNoAndPartName(ParticipantRequest request) {
		ParticipantResponse response = new ParticipantResponse();
		List<String> participantList = new ArrayList<String>();
		List<String> accountNumberList = new ArrayList<String>();
		ParticipantEAO participantEAO = EJB3Factory
				.getLocalBean(ParticipantEAO.class);
		List<Participant> partList = participantEAO.getAllParticipants();
		
		for (Participant participant : partList) {
			participantList.add(participant.getParticipantName());
			accountNumberList.add(participant.getAccountNumber());
		}
		response.setAccountNumberList(accountNumberList);
		response.setParticipantList(participantList);
		return response;
	}
	
	private Participant getParticipantDetails(String participantName) {
		Participant participant = getParticipantManager().getParticipant(participantName);
		return participant;
	}
	
	public ParticipantResponse getAllPartNames(ParticipantRequest request) {
		ParticipantResponse response = new ParticipantResponse();
		List<String> participantList = request.getParticipantList();
		boolean found = false;
		List<String> partList = getParticipantManager().getParticipants();
		partList.retainAll(participantList);
		if ( partList.size() > 0 ) {
			found = true;
		}
		response.setParticipantExist(found);
		return response;
	}

	private void createParticipant(CustomerEnrollmentData enrollmentData, PrivateKey privateKey, EncryptUtil passwordEncoder) throws Exception {
		String partPwd = enrollmentData.getPassword();
		String clientPwd = enrollmentData.getClientPassword();
		String decryptPPwd = passwordEncoder.decrypt(partPwd, privateKey).trim();
		String decryptCPwd = passwordEncoder.decrypt(clientPwd, privateKey).trim();
		String[] programs = enrollmentData.getPrograms();
		String name = enrollmentData.getParticipantName();
		String accNum = enrollmentData.getAccountNumber();
		Participant p = new Participant();
		p.setUser(name);
		p.setAccountNumber(accNum);
		p.setClientOfflineNotificationEnable(false);
		p.setOptOutClientOfflineNotification(false);
		p.setClientOfflineNotificationAggEnable(false);
		p.setThresholdsSummer(24);
		p.setThresholdsUnSummer(168);
		PSS2Features features = getSystemManager().getPss2Features();
		p.setDefaultEventOptoutEnabled(true);
		p.setPartEventOptoutEnabled(features.isEventOptoutEnabled());
		p.setEnrollmentDate(new Date());
		p.setCustomerName(enrollmentData.getCustomerName());
		p.setUserType(UserType.ADVANCED);
		p.setShedType("SIMPLE");
		p.setShedPerHourKW(Double.parseDouble("0.0"));
		p.setRequestId(enrollmentData.getRequestId());
	
		getParticipantManager().createParticipant(p, decryptPPwd.toCharArray(), programs);
		createClient(enrollmentData, decryptCPwd);
		//createClientContact(enrollmentData, name);
	}
	
	public void rollbackParticipants(ParticipantRequest request){
		List<String> participantList = request.getParticipantList();
		removeParticipants(participantList);
	}
	
	private void removeParticipants(List<String> participants) {
		try {
			getParticipantManager().removeCustomerCreatedOnAggregator(participants);	
		} catch(Exception exc) {
			logger.error("Exception : " + exc.getMessage());
		}
	}
	
	private void createClient(CustomerEnrollmentData enrollmentData, String decryptCPwd){
		String participantName = enrollmentData.getParticipantName();
		String clientName = participantName+enrollmentData.getClientName();
		Participant client = new Participant();
		client.setClient(true);
		client.setParent(participantName);
		client.setParticipantName(clientName);
		client.setCustomerName(enrollmentData.getCustomerName());
		client.setType((byte) 0);
		ParticipantEAO participantEAO = EJB3Factory
				.getLocalBean(ParticipantEAO.class);
		boolean found = participantEAO.checkAccount(clientName);
		if (found) {
			String message = "ERROR_CLIENT_NAME_DUPLICATED";

			//log.warn(LogUtils.createLogEntry("", "", message, null));
			throw new ValidationException(message);
		}
		getClientManager().createClient(client, decryptCPwd.toCharArray());
		createClientContact(enrollmentData, clientName);
		
	}
	private String specialCharValidator(String name,boolean isPart){
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
	
	private String getParticipantCreationErrorMsg(Exception excp){
		String errMsg = "";
		ValidationException ve = ErrorUtil.getValidationException(excp);
		if(null != ve){
			errMsg = ve.getLocalizedMessage();
		}else{
			errMsg = ErrorUtil.getErrorMessage(excp);
			if (errMsg.equalsIgnoreCase("internal error")){
				errMsg = "Approval process was unsuccessful, please try again later.";
			}
		}
		return errMsg;
	}
	
	private ParticipantManager getParticipantManager(){
		ParticipantManager participantManager = EJB3Factory.getBean(ParticipantManager.class);
		return participantManager;
		
	}
	private ProgramManager getProgramManager(){
		ProgramManager programManager = EJB3Factory.getBean(ProgramManager.class);
		return programManager;
	}
	private SystemManager getSystemManager(){
		SystemManager systemManager = EJB3Factory.getBean(SystemManager.class);
		return systemManager;
	}
	
	private ClientManager getClientManager(){
		ClientManager cm = EJB3Factory.getLocalBean(ClientManager.class);
		return cm;
	}
	private ProgramParticipantManager getProgramParticipantManager(){
		ProgramParticipantManager progParticipantManager = EJB3Factory.getLocalBean(ProgramParticipantManager.class);
		return progParticipantManager;
	}
	private ProgramParticipantAggregationManager getAggregationManager() {
		if (ppAggregationManager == null) {
			ppAggregationManager = (ProgramParticipantAggregationManager) EJBFactory.getBean(ProgramParticipantAggregationManagerBean.class);
		}
		return ppAggregationManager;
	}

	private void createClientContact(CustomerEnrollmentData enrollmentData, String clientName){
	
		Participant clientDetails = getClientManager().getClientWithContacts(clientName);
			if (clientDetails != null && clientDetails.getContacts() != null)
			{
				for (ParticipantContact contact : clientDetails.getContacts())
				{
					if (contact.getAddress().equals(enrollmentData.getCcEmail1())
						&& contact.getType().equals("Email"))
					{
						continue;
					}
				}
			}

			ParticipantContact contact = null;
			Set<ParticipantContact> contacts = new HashSet<ParticipantContact>();
			
			/*contact = new ParticipantContact();
			contact.setDescription(enrollmentData.getCcName1());
			contact.setAddress(enrollmentData.getCcPhone1());;
			contact.setType(Contact.PHONE_NUMBER);
			contact.setCommNotification(true);
			contact.setEventNotification(ContactEventNotificationType.NotNormalNotification);
			contact.setOffSeasonNotiHours(Double.parseDouble("8"));
			contact.setOnSeasonNotiHours(Double.parseDouble("1"));
			contact.setMsgThreshold(Integer.parseInt("10"));
			
			//contacts.add(contact);
			clientDetails.getContacts().add(contact);
			contact.setParticipant(clientDetails);
			getClientManager().updateClient(clientDetails);
*/
			contact = new ParticipantContact();
			contact.setDescription(enrollmentData.getCcName1());
			contact.setAddress(enrollmentData.getCcEmail1());;
			contact.setType(Contact.EMAIL_ADDRESS);
			contact.setCommNotification(true);
			contact.setEventNotification(ContactEventNotificationType.NotNormalNotification);
			contact.setOffSeasonNotiHours(Double.parseDouble("8"));
			contact.setOnSeasonNotiHours(Double.parseDouble("1"));
			contact.setMsgThreshold(Integer.parseInt("10"));
			contact.setDefaultMsgThreshold(true);
			
			clientDetails.getContacts().add(contact);
			contact.setParticipant(clientDetails);
			getClientManager().updateClient(clientDetails);
			
			
			//contacts.add(contact);
			
			/*contact = new ParticipantContact();
			contact.setDescription(enrollmentData.getCcName2());
			contact.setAddress(enrollmentData.getCcPhone2());;
			contact.setType(Contact.PHONE_NUMBER);
			contact.setCommNotification(true);
			contact.setEventNotification(ContactEventNotificationType.NotNormalNotification);
			contact.setOffSeasonNotiHours(Double.parseDouble("8"));
			contact.setOnSeasonNotiHours(Double.parseDouble("1"));
			contact.setMsgThreshold(Integer.parseInt("10"));
			//contacts.add(contact);
			
			clientDetails.getContacts().add(contact);
			contact.setParticipant(clientDetails);
			getClientManager().updateClient(clientDetails);

*/			
			if (enrollmentData.getCcName2() != null && !enrollmentData.getCcName2().isEmpty() ) {
				if (enrollmentData.getCcEmail2() != null && !enrollmentData.getCcName2().isEmpty() ) {
					contact = new ParticipantContact();
					contact.setDescription(enrollmentData.getCcName2());
					contact.setAddress(enrollmentData.getCcEmail2());
					contact.setType(Contact.EMAIL_ADDRESS);
					contact.setCommNotification(true);
					contact.setEventNotification(ContactEventNotificationType.NotNormalNotification);
					contact.setOffSeasonNotiHours(Double.parseDouble("8"));
					contact.setOnSeasonNotiHours(Double.parseDouble("1"));
					contact.setMsgThreshold(Integer.parseInt("10"));
					contact.setDefaultMsgThreshold(true);
					//contacts.add(contact);
					clientDetails.getContacts().add(contact);
					contact.setParticipant(clientDetails);
					getClientManager().updateClient(clientDetails);
				}
			}
	}

	public List<String> getUserList() {
		return userList;
	}

	public void setUserList(List<String> userList) {
		this.userList = userList;
	}

	public List<String> getPassList() {
		return passList;
	}

	public void setPassList(List<String> passList) {
		this.passList = passList;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
}
