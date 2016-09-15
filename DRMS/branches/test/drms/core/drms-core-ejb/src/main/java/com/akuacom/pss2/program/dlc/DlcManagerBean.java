/**
 * 
 */
package com.akuacom.pss2.program.dlc;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import com.akuacom.utils.DateUtil;
import org.apache.log4j.Logger;

import scala.actors.threadpool.Arrays;

import com.akuacom.accmgr.ws.Role;
import com.akuacom.accmgr.ws.User;
import com.akuacom.accmgr.wsclient.AccMgrWSClient;
import com.akuacom.ejb.client.EJB3Factory;
import com.akuacom.pss2.client.ClientEAO;
import com.akuacom.pss2.client.ClientManager;
import com.akuacom.pss2.contact.Contact;
import com.akuacom.pss2.contact.ContactManager;
import com.akuacom.pss2.core.ErrorUtil;
import com.akuacom.pss2.core.ProgramValidationMessage;
import com.akuacom.pss2.email.NotificationMethod;
import com.akuacom.pss2.email.NotificationParametersVO;
import com.akuacom.pss2.email.Notifier;
import com.akuacom.pss2.event.Event;
import com.akuacom.pss2.event.EventManager;
import com.akuacom.pss2.event.participant.EventParticipant;
import com.akuacom.pss2.history.CustomerReportManager;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.participant.ParticipantEAO;
import com.akuacom.pss2.participant.ParticipantManager;
import com.akuacom.pss2.program.dlc.customers.Customer;
import com.akuacom.pss2.program.dlc.customers.Customers;
import com.akuacom.pss2.program.participant.ProgramParticipant;
import com.akuacom.pss2.program.participant.ProgramUserBaseEAO;
import com.akuacom.pss2.util.DrasRole;
import com.akuacom.pss2.util.Environment;
import com.akuacom.pss2.util.LogUtils;

/**
 * the class DlcManagerBean
 */
@Stateless
public class DlcManagerBean implements DlcManager.L, DlcManager.R {
	
    private static final Logger log = Logger.getLogger(DlcManagerBean.class);

    private static final String DEFAULT_PASSWORD="Test_1234";
    private static final String DEFAULT_CLIENT="client1";
    private static final Pattern pattern = Pattern.compile("\\w*");

    @EJB
    ParticipantEAO.L participantEAO;
    @EJB
    ClientEAO.L clientEAO;
    @EJB
    ProgramUserBaseEAO.L userEAO;
    @EJB
    ParticipantManager.L participantManager;
    @EJB
    ClientManager.L clientManager;
    @EJB
    CustomerReportManager.L customerReportManager;
    @EJB
    Notifier.L notifier;
    @EJB
    EventManager.L eventManager;

    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public void createParticipant(Customers customers) {
    	List<Customer> invalid=new ArrayList<Customer>();
    	List<Customer> created=new ArrayList<Customer>();
        Map<String, ProgramValidationMessage> errorMap = new HashMap<String, ProgramValidationMessage>();

    	try {

	        for (Customer customer:customers.getCustomer()) {
                // validate service id
	        	if (customer.getServiceId()==null || customer.getServiceId().trim().length()==0
	        			|| customer.getParticipantName()==null || customer.getParticipantName().trim().length()==0) {
	        		invalid.add(customer);
	        		continue;
	        	}

                ProgramValidationMessage message = validateCustomer(customer);
                if (message != null) {
                    errorMap.put(customer.getServiceId(), message);
                    invalid.add(customer);
                    continue;
                }
                
                if(checkDuplicatedParticipant(customer.getParticipantName(), customer.getServiceId())) {
                    errorMap.put(customer.getServiceId(), 
                    		new ProgramValidationMessage("participantName", "Duplicated participant name or account number exists in the system"));
                    invalid.add(customer);
                    continue;
                }

                Participant part=new Participant();
	        	part.setParticipantName(customer.getParticipantName());
	        	part.setAccountNumber(customer.getServiceId());
	        	part.setClient(false);

	        	String clientName=customer.getClientName();
	        	if (clientName==null || clientName.trim().length()==0)
	        		clientName=part.getParticipantName()+"."+DEFAULT_CLIENT;
	        	Participant client=new Participant();
	        	client.setParticipantName(clientName);
	        	client.setAccountNumber(clientName);
	        	client.setClient(true);
	        	client.setParent(part.getParticipantName());
	        	
	        	String partpwd=customer.getParticipantPassword();
	        	if (partpwd==null || partpwd.trim().length()==0)
	        		partpwd=DEFAULT_PASSWORD;
	
	        	String clientpwd=customer.getClientPassword();
	        	if (clientpwd==null || clientpwd.trim().length()==0)
	        		clientpwd=DEFAULT_PASSWORD;
	
	        	if (createParticipant(part, partpwd.toCharArray(), client, clientpwd.toCharArray()))
	        		created.add(customer);
	        	else
	        		invalid.add(customer);
	        }
	        
	        sendNotification("Viridity: customers creation completed", "created", created, invalid, errorMap);
    	}catch(Exception e) {
    		sendNotification("Viridity: failed to create customers", ErrorUtil.getErrorMessage(e));
    		throw new EJBException(e);
    	}
    }

    private boolean checkDuplicatedParticipant(String partName, String serviceId) {
        final boolean duplicated;

        Participant part = participantEAO.findParticipantOnlyByName(partName, false);
        duplicated = part != null || participantEAO.checkAccount(serviceId);    // check account returns true if account exists

        return duplicated;
    }

    private ProgramValidationMessage validateCustomer(Customer customer) {
        // validate customer's participant name
        String participantName = customer.getParticipantName().trim();
        if (participantName.length() > 20) {
            return new ProgramValidationMessage(
                    "participantName", "Participant name can't be longer than 20 characters");
        }
        Matcher m1 = pattern.matcher(participantName);
        if (!m1.matches()) {
            return new ProgramValidationMessage(
                    "participantName", "Illegal character(s) in participant name");
        }

        ProgramValidationMessage message = validatePassword(customer.getParticipantPassword());
        if (message != null) {
            String replace = message.getDescription().replace("New password", "Participant password");
            message.setDescription(replace);
            return message;
        }

        // validate customer's client name
        // DRMS-7440: added null check before trimming.
        String clientName = customer.getClientName();
        clientName = clientName == null ? null : clientName.trim();
        // DRMS-7431: client name is optional, so it's ok to be blank.
        if (clientName != null && clientName.length() > 0) {
            String[] array = clientName.split("\\.");
            if (array.length != 2) {
                return new ProgramValidationMessage(
                        "clientName", "Client name must have one and only one dot");
            }
            if (!array[0].equals(participantName)) {
                return new ProgramValidationMessage(
                        "clientName", "Client name should start with participant name follow by a dot and the client id");
            }
            String name = array[1];
            if (name.length() > 20) {
                return new ProgramValidationMessage(
                        "clientName", "Client name can't be longer than 20 characters");
            }
            Matcher m2 = pattern.matcher(name);
            if (!m2.matches()) {
                return new ProgramValidationMessage(
                        "clientName", "Illegal character(s) in client name");
            }
        }

        message = validatePassword(customer.getClientPassword());
        if (message != null) {
            String replace = message.getDescription().replace("New password", "Client password");
            message.setDescription(replace);
            return message;
        }

        return null;
    }

    private ProgramValidationMessage validatePassword(String password) {
        if (password != null && password.length() > 0) {
            if (password.length() < 9) {
                return new ProgramValidationMessage("participantPassword", "New password must have 9 or more characters.");
            }
            if (password.length() > 24) {
                return new ProgramValidationMessage("participantPassword", "New password must be less than 24 characters.");
            }
            if (!password.matches(".*[A-Z].*")) {// at least one upper case required
                return new ProgramValidationMessage("participantPassword", "New password must have at least one upper case character (A-Z).");
            }
            if (!password.matches(".*[a-z].*")) {// at least one lower case required
                return new ProgramValidationMessage("participantPassword", "New password must have at least one lower case character (a-z).");
            }
            if (!password.matches(".*\\d.*")) {// at least one digit required
                return new ProgramValidationMessage("participantPassword", "New password must have at least one digital character (0-9).");
            }
            if (!password.matches(".*[\\._\\-/].*")) {// at least one special character required
                return new ProgramValidationMessage("participantPassword", "New password must have at least one of the following special character (._-/)");
            }
            if (password.matches(".*[\\W&&[^\\._\\-/]].*")) {
                return new ProgramValidationMessage("participantPassword", "New password has illegal character(s), only the following characters are allowed (A-Za-z1-9._-/)");
            }
        }
        return null;
    }

    public boolean createParticipant(Participant part, char[] partpwd, 
    		Participant client, char[] clientpwd) {
    	boolean created=false;
    	try {
	        participantEAO.createParticipant(part);
	        createUser(partpwd, part.getParticipantName(), getParticipantRoles());
	        userEAO.createProgramParticipant(DlcProgramEJBBean.PROGRAM_NAME, part.getParticipantName(), false);
	        
	        clientEAO.createClient(client);
	        createClientUser(clientpwd, client.getParticipantName());
			reportClientStatus(client.getUUID(), false, null, new Date(), client.getParticipantName());
	        
	    	userEAO.createProgramParticipant(DlcProgramEJBBean.PROGRAM_NAME, client.getParticipantName(), true);
	    	created=true;
    	}catch(Exception e) {
    		removeUser(part.getParticipantName());
    		removeClientUser(client.getParticipantName());
    		
    		log.info(LogUtils.createLogEntry("Viridity DLC", "Viridity Customer CRUD", "can not create participant "+part.getParticipantName(), null));
    		log.debug(LogUtils.createExceptionLogEntry("Viridity DLC", "Viridity Customer CRUD", e));
    	}
    	return created;
    }
 
    @Override
    public void removeParticipant(String serviceId){
        List<Participant> parts = participantEAO.findParticipantsByAccounts(Arrays.asList(new String[]{serviceId}));
        if (parts!=null && parts.size()>0) {
	        List<EventParticipant> eps=participantEAO.findEventParticipants(parts.get(0).getParticipantName());
	        if (eps==null || eps.size()==0) {
	        	participantManager.removeParticipant(parts.get(0).getParticipantName());
	        	sendNotification("Viridity: deleted customer successfully", "Customer service ID: "+serviceId);
	        	return;
	        }
        }
        String message="Customer does not exist or is enrolling in an event and cannot be deleted. " +
        		"Customer service ID: "+serviceId;
    	sendNotification("Viridity: failed to delete customer", message);
    }
    
    
    @Override
    public void removeParticipants(List<String> serviceIds){
    	List<String> invalids=new ArrayList<String>();
    	List<String> removed=new ArrayList<String>();
    	try {
	        for (String service : serviceIds) {
	            List<Participant> parts = participantEAO.findParticipantsByAccounts(Arrays.asList(new String[]{service}));
	            if (parts==null || parts.size()==0) {
	            	invalids.add(service);
	            	continue;
	            }
	            List<EventParticipant> eps=participantEAO.findEventParticipants(parts.get(0).getParticipantName());
	            if (eps!=null && eps.size()>0) {
	            	invalids.add(service);
	            	continue;
	            }
	            participantManager.removeParticipant(parts.get(0).getParticipantName());
	        	removed.add(service);
	        }
	        sendDeleteNotification(removed, invalids);
    	}catch(Exception e) {
    		sendNotification("Viridity: failed to delete customers", ErrorUtil.getErrorMessage(e));
    		throw new EJBException(e);
    	}
    }

    public void sendNotification(String subject, String message){
		log.info(LogUtils.createLogEntry("Viridity DLC", "Viridity Customer CRUD", subject, message));
		
		sendDRASOperatorEventNotification(subject, message,
				NotificationMethod.getInstance(), new NotificationParametersVO(), "Viridity DLC", notifier);
    }
    
    public void sendDeleteNotification(List<String> valids, List<String> invalids){
        String subject="Viridity: deleted customers successfully";
		StringBuilder content=new StringBuilder();
		content.append("The following customers are deleted: ");
		for (String serviceID:valids) {
			content.append(serviceID);
			content.append(", ");
		}
		content.append("\n");
		content.append("The following customers do not exist or are invalid and can not be deleted: ");
		for (String serviceID:invalids) {
			content.append(serviceID);
			content.append(", ");
		}
        
		log.info(LogUtils.createLogEntry("Viridity DLC", "Viridity Customer CRUD", subject, content.toString()));
		
		sendDRASOperatorEventNotification(subject, content.toString(),
                NotificationMethod.getInstance(), new NotificationParametersVO(), "Viridity DLC", notifier);

    }

    // TODO: update title to cover more scenarios
	public void sendNotification(String subject, String operation, List<Customer> valids, List<Customer> invalids, Map<String, ProgramValidationMessage> errorMap) {

        StringBuilder content = new StringBuilder();
        if (valids.size() > 0) {
            content.append("The following customers were ");
            content.append(operation);
            content.append(": ");
            for (Customer customer : valids) {
                content.append(customer.getServiceId());
                content.append(", ");
            }
            content.append("\n\n");
        } else {
            content.append("No customer were ").append(operation).append(".\n\n");
        }

        if (invalids.size() > 0) {
            content.append("The following customers were invalid and couldn't be ");
            content.append(operation);
            content.append(":\n\n");
            for (Customer customer : invalids) {
                content.append("Service ID: ");
                content.append(customer.getServiceId()).append("\n");
                content.append(" Participant Name: ");
                content.append(customer.getParticipantName()).append("\n");
                content.append(" Participant Password: ");
                content.append(customer.getParticipantPassword()).append("\n");
                content.append(" Client Name: ");
                content.append(customer.getClientName()).append("\n");
                content.append(" Client Password: ");
                content.append(customer.getClientPassword()).append("\n");

                String serviceId = customer.getServiceId();
                ProgramValidationMessage message = errorMap.get(serviceId);
                if (message != null) {
                    content.append(" Error message: ").append(message.getDescription()).append("\n");
                }

                content.append("\n");
            }
        }

        log.info(LogUtils.createLogEntry("Viridity DLC", "Viridity Customer CRUD", subject, content.toString()));

        sendDRASOperatorEventNotification(subject, content.toString(),
                NotificationMethod.getInstance(), new NotificationParametersVO(), "Viridity DLC", notifier);
    }
    
    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public void updateParticipant(Customers customers) {
    	List<Customer> updated=new ArrayList<Customer>();
        Map<String, ProgramValidationMessage> errorMap = new HashMap<String, ProgramValidationMessage>();

    	try {
	        for (Customer customer:customers.getCustomer()) {
	        	if (customer.getServiceId()==null || customer.getServiceId().trim().length()==0
	        			|| customer.getParticipantName()==null || customer.getParticipantName().trim().length()==0) {
	        		continue;
	        	}

                ProgramValidationMessage message = validateCustomer(customer);
                if (message != null) {
                    errorMap.put(customer.getServiceId(), message);
                    continue;
                }

                Participant part=new Participant();
	        	part.setAccountNumber(customer.getServiceId());
	        	part.setParticipantName(customer.getParticipantName());
	        	part.setClient(false);
	
	        	Participant client=new Participant();
	    		client.setParticipantName(customer.getClientName());
	        	client.setClient(true);
	        	client.setParent(part.getParticipantName());
	        	
	        	char[] partpwd=null;
	        	if (customer.getParticipantPassword()!=null && customer.getParticipantPassword().trim().length()!=0)
	        		partpwd=customer.getParticipantPassword().toCharArray();
	
	        	char[] clientpwd=null;
	        	if (customer.getClientPassword()!=null && customer.getClientPassword().trim().length()!=0)
	        		clientpwd=customer.getClientPassword().toCharArray();
	        	
	        	// check if servide ID exists 
		        List<Participant> parts = participantEAO.findParticipantsByAccounts(Arrays.asList(new String[]{part.getAccountNumber()}));
		        if (parts==null || parts.size()==0) {
		        	errorMap.put(customer.getServiceId(), new ProgramValidationMessage("serviceID", "the participant with the service ID does not exist"));
		        	continue;
		        }
	        	
                if (updateParticipant(part, partpwd, client, clientpwd, parts.get(0))) {
                    updated.add(customer);
                } else {
                    errorMap.put(customer.getServiceId(), new ProgramValidationMessage(
                            "participantName", "Update failed. Possible reason: customer in active event (signal level is moderate or high) or internal error."));
                }
	        }
	        
	    	List<Customer> invalid=new ArrayList<Customer>();
	        if (updated.size()!=customers.getCustomer().size()) {
	        	invalid.addAll(customers.getCustomer());
	        	invalid.removeAll(updated);
	        }

	        sendNotification("Viridity: customers update completed", "updated", updated, invalid, errorMap);
    	}catch(Exception e) {
    		sendNotification("Viridity: failed to update customers", ErrorUtil.getErrorMessage(e));
    		throw new EJBException(e);
    	}
    }

    
    public boolean updateParticipant(Participant part, char[] partpwd, 
    		Participant client, char[] clientpwd, Participant updated) {
    	boolean updateflag=false;
    	try {
//	        List<Participant> parts = participantEAO.findParticipantsByAccounts(Arrays.asList(new String[]{part.getAccountNumber()}));
//	    	if (parts!=null && parts.size()>0) {
//	        	Participant updated=parts.get(0);
            String parentName = updated.getParticipantName();
            Date now = new Date();
            Date startTime = DateUtil.stripTime(now);
            Date endTime = DateUtil.endOfDay(now);
            ArrayList<String> list = new ArrayList<String>();
            list.add(parentName);
            List<Event> allEvents = eventManager.findByParticipantAndDate(startTime, endTime, list);
            // assuming any active events will prevent participant update
            if (allEvents != null && allEvents.size() > 0) {
                return false;
            }
            if (part.getParticipantName() != null && !part.getParticipantName().equals(updated.getParticipantName())) {
                char[] ppwd = partpwd;
                if (ppwd == null || ppwd.length == 0)
                    ppwd = DEFAULT_PASSWORD.toCharArray();

                char[] cpwd = clientpwd;
                if (cpwd == null || cpwd.length == 0)
                    cpwd = DEFAULT_PASSWORD.toCharArray();

                if (client.getParticipantName() == null || client.getParticipantName().trim().length() == 0)
                    client.setParticipantName(part.getParticipantName() + "." + DEFAULT_CLIENT);

                participantManager.removeParticipant(parentName);
                createParticipant(part, ppwd, client, cpwd);
            } else {
                if (partpwd != null && partpwd.length != 0)
                    participantManager.setParticipantPassword(updated.getUUID(), new String(partpwd));

                List<Participant> updatedClients = clientEAO.getClientsAllInfoByParent(parentName);
                if (updatedClients != null && updatedClients.size() > 0) {
                    Participant updatedClient = updatedClients.get(0);
                    if (client.getParticipantName() != null && client.getParticipantName().trim().length() != 0
                            && !updatedClient.getParticipantName().equals(client.getParticipantName())) {

                        char[] cpwd = clientpwd;
                        if (cpwd == null || cpwd.length == 0)
                            cpwd = DEFAULT_PASSWORD.toCharArray();

                        clientManager.removeClient(updatedClient.getParticipantName());
                        clientEAO.createClient(client);
                        createClientUser(cpwd, client.getParticipantName());
                        reportClientStatus(client.getUUID(), false, null, new Date(), client.getParticipantName());

                        userEAO.createProgramParticipant(DlcProgramEJBBean.PROGRAM_NAME, client.getParticipantName(), true);

                    } else {
                        if (clientpwd != null && clientpwd.length != 0)
                            participantManager.setParticipantPassword(updatedClient.getUUID(), new String(clientpwd));
                    }
                }
            }
            updateflag = true;
//	    	}
        }catch(Exception e) {
			log.info(LogUtils.createLogEntry("Viridity DLC", "Viridity Customer CRUD", 
					"can not update participant with account number "+part.getAccountNumber(), null));
			log.debug(LogUtils.createExceptionLogEntry("Viridity DLC", "Viridity Customer CRUD", e));
		}

    	return updateflag;
    }
    
    
    
    private void removeUser(String userName) {
        AccMgrWSClient accmgrClient = new AccMgrWSClient();
        accmgrClient.initialize();
        
        User user;
		try {
			user = accmgrClient.getAccmgr().getUserByName("PSS2", userName);
		} catch (Exception ex) {
			user = null;
		}
		if (user != null) {
			accmgrClient.getAccmgr().removeUser(user.getId());
		}
    }
    
    private void removeClientUser(String clientName){
		AccMgrWSClient accmgrClient = new AccMgrWSClient();
		accmgrClient.initialize();
		User user;
		try {
			user = accmgrClient.getAccmgr().getUserByName("CLIENT", clientName);
		} catch (Exception ex) {
			user = null;
		}
		if (user != null) {
			accmgrClient.getAccmgr().removeUser(user.getId());
		}
    }
    
    private void createClientUser(char[] password, String clientName) {
        AccMgrWSClient accmgrClient = new AccMgrWSClient();
        accmgrClient.initialize();
        User user = new User();
        user.setId(clientName);
        user.setDomainname("CLIENT");
        user.setUsername(clientName);
        user.setPassword(new String(password));
        Role role = new Role();
        role.setRolename("PSS2WS");
        user.getRoles().add(role);
        user.setEmail(clientName);
        user.setStatus("ACTIVE");
        accmgrClient.getAccmgr().createUser("CLIENT", user);
    }
    
    private void createUser(char[] password, String userName,
            List<Role> participantRoles) {
        AccMgrWSClient accmgrClient = new AccMgrWSClient();
        accmgrClient.initialize();
        User user = new User();
        user.setId(userName);
        user.setDomainname("PSS2");
        user.setUsername(userName);
        user.setPassword(new String(password));
        user.getRoles().addAll(participantRoles);
        user.setEmail(userName);
        user.setStatus("ACTIVE");
        accmgrClient.getAccmgr().createUser("PSS2", user);
    }

    private List<Role> getParticipantRoles() {
        final ArrayList<Role> list = new ArrayList<Role>();
        list.add(generateRole(DrasRole.FacilityManager.toString()));
        list.add(generateRole("website"));
        list.add(generateRole("PSS2WS"));
        return list;
    }

    private Role generateRole(String rolename) {
        Role role = new Role();
        role.setRolename(rolename);
        return role;
    }

	private void reportClientStatus(String client_uuid, boolean online, Date time, Date lastContact, String clientName){
		if (online)
			customerReportManager.reportOnlineStatus(client_uuid, time, lastContact, clientName);
		else
			customerReportManager.reportOfflineStatus(client_uuid, lastContact, clientName);
	}
	
    public synchronized void sendDRASOperatorEventNotification(
            String subject, String content, NotificationMethod method,
            NotificationParametersVO params, String programName, Notifier notifier) {
        ContactManager cm = EJB3Factory.getLocalBean(ContactManager.class);

        List<Contact> sendList = cm.getOperatorContacts();
        if (sendList.size() > 0) {
        	notifier.sendNotification(sendList, "operator", subject, content,
                    method, params, Environment.isAkuacomEmailOnly(),
                    programName);
        }
    }
    
    /**
     * The approach is like this: fetch all participant/clients. Set up a map
     * for client with participant name as key. This assumes participant only
     * has one client. Otherwise, the result is not defined. Then, go through
     * participants, and set up Customer object for return.
     *
     * NOTE: This call uses memory to trade for multiple db lookup.
     */
    @Override
    public List<Customer> findParticipant(List<String> serviceIds) {
        ArrayList<Customer> list = new ArrayList<Customer>();

        List<Participant> participants = participantEAO.findAllBare();

        Map<String, Participant> map = new HashMap<String, Participant>();
        for (Participant client : participants) {
            if (client.isClient()) {
                map.put(client.getParent(), client);
            }
        }

        for (Participant participant : participants) {
            if (!participant.isClient()) {
                if (serviceIds == null || serviceIds.size() == 0
                        || serviceIds.contains(participant.getAccountNumber())) {
                    Customer o = new Customer();
                    o.setServiceId(participant.getAccountNumber());
                    String participantName = participant.getParticipantName();
                    o.setParticipantName(participantName);
                    Participant client = map.get(participantName);
                    o.setClientName(client == null ? null : client.getParticipantName());
                    list.add(o);
                }
            }
        }

        return list;
    }

    @Override
    public Customer findParticipant(String serviceId) {
        List<Participant> participants = participantEAO.findByAccount(serviceId);
        Participant p = null;
        if (participants != null && participants.size() > 0) {
            p = participants.get(0);
        }
        if (p == null) {
            return null;
        }

        List<Participant> clients = clientEAO.findClientsByParticipant(p.getParticipantName());
        Customer result = new Customer();
        result.setServiceId(serviceId);
        result.setParticipantName(p.getParticipantName());
        if (clients != null && clients.size() > 0) {
            result.setClientName(clients.get(0).getParticipantName());
        }
        return result;
    }

	@Override
	public ProgramParticipant updateProgramParticipant(ProgramParticipant value) {
		return userEAO.updateProgramParticipant(value);
	}

	@Override
	public ProgramParticipant findRtpStrategyByProgAndPartiForClient(
			String programName, String participantName, boolean client) {
		ProgramParticipant p =  userEAO.findRtpStrategyByProgAndPartiForClient(programName, participantName, client);
		p.getRtpStrateges().size();
		return p;
	}
	
	

}
