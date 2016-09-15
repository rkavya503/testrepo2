/**
 * 
 */
package com.akuacom.pss2.program.sceftp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.apache.log4j.Logger;

import com.akuacom.common.exception.AppServiceException;
import com.akuacom.ejb.client.EJB3Factory;
import com.akuacom.pss2.cache.EventStateCacheHelper;
import com.akuacom.pss2.contact.Contact;
import com.akuacom.pss2.contact.ContactManager;
import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.core.ProgramValidationException;
import com.akuacom.pss2.core.ProgramValidationMessage;
import com.akuacom.pss2.email.NotificationMethod;
import com.akuacom.pss2.email.NotificationParametersVO;
import com.akuacom.pss2.email.Notifier;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.participant.ParticipantEAO;
import com.akuacom.pss2.program.dbp.SCEFTPClient;
import com.akuacom.pss2.system.SystemManager;
import com.akuacom.pss2.system.SystemManagerBean;
import com.akuacom.pss2.util.Environment;
import com.akuacom.pss2.util.LogUtils;

/**
 * the class SCEFTPManagerBean
 *
 */
@Stateless
public class SCEFTPManagerBean implements SCEFTPManager.L, SCEFTPManager.R {

    private static final Logger log = Logger.getLogger(SCEFTPManagerBean.class);

    @EJB
    Notifier.L notifier;
    @EJB
    ParticipantEAO.L participantEAO;
    private static final int RETURN_SUCCESS = 0;
    private static final int RETURN_FAILTURE = 1;
    private static final int RETURN_FTP_CONNECT_FAILTURE = 2;

    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public int process(SCEFTPConfig config, boolean lastScan){
    	SystemManager systemManager = EJBFactory
				.getBean(SystemManagerBean.class);
    	int result=RETURN_FAILTURE;
		SimpleDateFormat format=new SimpleDateFormat(config.getFilenameTemplate());
		String filename=format.format(new Date());

        try {
        	String fileString=getRemoteFile(config, filename);
			if (fileString==null || fileString.trim().length()==0) {
				if (fileString!=null) {
					String message="Empty file " + filename;
	        		sendNotifications(message);   
	        		return result;
				}
				
				if (!config.getUpload() && lastScan && config.getRequired()) {
					String message="No available file exists from " + config.getScanStartTime()+" to "+config.getScanEndTime();
	        		sendNotifications(message);        		
				}
        		return result;
			}
			
			SCEParticipantFileParser parser=new SCEParticipantFileParser(fileString, filename);
			parser.validation();
			
			processParserFile(parser);
			result=RETURN_SUCCESS;

        } catch(AppServiceException e) {
			if (!config.getConnErrorNotified()) {
				if(systemManager.getPss2Features().isProductionServer()){
					sendNotification("FTP server isn't reachable(SCE Participant Info Upload)", e.getMessage());
				} else {
					sendNotification("FTP server isn't reachable(SCE2 Participant Info Upload)", e.getMessage());	
				}
				result=RETURN_FTP_CONNECT_FAILTURE;
			}
		} catch (ProgramValidationException e) {
			sendExceptionNotifications(e, filename);
		} catch(Exception e) {
			sendExceptionNotifications(e, filename);
		}
    	
        return result;
    }
    @Override
	public void processParserFile(SCEParticipantFileParser parser){
    	
		// active participants exist both in file and dras 
    	List<Participant> partsInFile=participantEAO.findInAccounts(parser.getAccounts());
		// active participants exist in dras but not in file 
    	List<Participant> partsNotInFile=participantEAO.findNotInAccounts(parser.getAccounts());
    	// inactive participants exist in file 
    	List<String> inactiveParts=participantEAO.getInactiveAccount(parser.getAccounts());
    	
    	List<SCEParticipantEntry> partsNotInDRAS=new ArrayList<SCEParticipantEntry>();
    	
    	List<SCEParticipantProgram> programChanged=new ArrayList<SCEParticipantProgram>();
    	List<SCEParticipantProgram> noProgramChanged=new ArrayList<SCEParticipantProgram>();
    	List<Participant> updatedList=new ArrayList<Participant>();
    	
    	List<Participant> partsMissAbank=new ArrayList<Participant>();
    	List<Participant> partsMissSubstation=new ArrayList<Participant>();
    	List<Participant> partsMissSlap=new ArrayList<Participant>();
    	List<Participant> partsMissBlockNumber=new ArrayList<Participant>();
    	List<Participant> partsMissProgramOption=new ArrayList<Participant>();
    	
    	int index=0;
    	boolean missingFromFile=true;
    	for (SCEParticipantEntry entry: parser.getSortedSet()) {
			if (index < partsInFile.size()) {
				Participant part = partsInFile.get(index);
	    		if (0==entry.getServiceAccount().compareToIgnoreCase(part.getAccountNumber())) {
	    			SCEParticipantProgram pp=new SCEParticipantProgram(part, entry);
	    			updatedList.add(pp.getParticipant());
	    			
	    			if (pp.isChanged())
	    				programChanged.add(pp);
	    			else
	    				noProgramChanged.add(pp);
	    			
	    			index++;
	    			missingFromFile=false;
	    			
	    			
	    			if(pp.isBIP()){
	    				String abank = part.getABank();
	    				String blockNumber = entry.getBlockNumber();
	    				int programOption = entry.getProgramOption();
	    				if(abank==null||abank.trim().equalsIgnoreCase("")){partsMissAbank.add(part);}
	    				if(blockNumber==null||blockNumber.trim().equalsIgnoreCase("")){partsMissBlockNumber.add(part);}
	    				if(!(programOption==15||programOption==30)){
	    					partsMissProgramOption.add(part);
	    				}
	    			}
	    			if(pp.isAPI()){
	    				String slap = part.getSlap();
	    				String abank = part.getABank();
	    				String blockNumber = entry.getBlockNumber();
	    				
	    				if(slap==null||slap.trim().equalsIgnoreCase("")){partsMissSlap.add(part);}
	    				if(abank==null||abank.trim().equalsIgnoreCase("")){partsMissAbank.add(part);}
	    				if(blockNumber==null||blockNumber.trim().equalsIgnoreCase("")){partsMissBlockNumber.add(part);}
	    			}
	    			if(pp.isSDP()){
	    				String slap = part.getSlap();
	    				String abank = part.getABank();
	    				String substation = part.getSubstation();
	    				String blockNumber = entry.getBlockNumber();
	    				
	    				if(slap==null||slap.trim().equalsIgnoreCase("")){partsMissSlap.add(part);}
	    				if(abank==null||abank.trim().equalsIgnoreCase("")){partsMissAbank.add(part);}
	    				if(blockNumber==null||blockNumber.trim().equalsIgnoreCase("")){partsMissBlockNumber.add(part);}
	    				if(substation==null||substation.trim().equalsIgnoreCase("")){partsMissSubstation.add(part);}
	    			}
	    			
	    		}
			}
			if (missingFromFile) { 
				if (!inactiveParts.contains(entry.getServiceAccount()))
					partsNotInDRAS.add(entry);
			} else {
				missingFromFile=true;
			}
    	}

    	updateParticipants(updatedList);
    	
    	sendParticipantUpdateNotifications(noProgramChanged, programChanged, partsNotInDRAS, partsNotInFile, parser.getFilename(),partsMissSlap,partsMissAbank,partsMissSubstation,partsMissBlockNumber,partsMissProgramOption);
    }

    
    
    private void updateParticipants(List<Participant> parts){
    	participantEAO.update(parts);
    }
	
    protected String getRemoteFile(SCEFTPConfig config, String filename) throws AppServiceException{
    	SCEFTPClient ftpClient=null;
    	String fileString;
    	try {
	    	ftpClient=new SCEFTPClient(config.getHost(), config.getPort(), config.getUsername(), config.getPassword());
	    	ftpClient.setFilename(filename);
	    	ftpClient.setBackupPath(config.getBackupPath());
	
	    	ftpClient.connect();
	    	fileString=ftpClient.getFileContents();
	    	
	    	if (fileString!=null)
	    		ftpClient.backupEventFile(fileString);
    	}finally{
			if (ftpClient!=null)
				ftpClient.close();
    	}
    	return fileString;
    }
    
    @Override
    public void sendNotifications(String content){
    	String subject="Failed to update participant information";

		log.info(LogUtils.createLogEntry("", "SCE Participant Upload From FTP drop", subject, content));
    	sendNotifications(subject, content, null, null);
	}
    @Override
    public void sendNotification(String subject, String content){
		sendNotifications(subject, content, null, null);
    }

    @Override
	public void sendExceptionNotifications(Exception e, String filename){
		StringBuilder subject=new StringBuilder();
		subject.append("Failed to update the participant file " + filename); 
		
		StringBuilder content=new StringBuilder();
		if (e instanceof ProgramValidationException) {
			for (ProgramValidationMessage msg:((ProgramValidationException)e).getErrors()) {
				content.append(msg.getParameterName());
				content.append(": ");
				content.append(msg.getDescription());
				content.append("\n");
			}
		} else {
			content.append(MessageUtil.getErrorMessage(e));
		}

		log.info(LogUtils.createLogEntry("", "SCE Participant Upload From FTP drop", subject.toString(), content.toString()));
		sendNotifications(subject.toString(), content.toString(), null, null);
	}
	
    /**
     * Formats a set of strings for display 
     * (Used instead of default Set<String> .toString output
     * @param strings
     * @return 
     */
    private String formatStringList(Set<String> strings) {
        StringBuilder retval = new StringBuilder();
        if (strings != null && strings.size() > 0) {
            int idx = 0;
            for (String str : strings) {
                retval.append(str);
                ++idx;
                if (idx < strings.size()) {
                    retval.append(", ");
                }
            }
        }
        return retval.toString();
    }
    
	public void sendParticipantUpdateNotifications(List<SCEParticipantProgram> programNonUpdatedParts, 
			List<SCEParticipantProgram> programUpdatedParts, List<SCEParticipantEntry> partsNotInDRAS,
			List<Participant> partsNotInFile, String filename,List<Participant> partsMissSlap,List<Participant> partsMissAbank,
			List<Participant> partsMissSubstation,List<Participant> partsMissBlock,List<Participant> partsMissProgramOption){
		StringBuilder subject=new StringBuilder();
		subject.append("Participant File " + filename);
		subject.append(" has been processed");
		StringBuilder content=new StringBuilder();
		
		content.append("Participant Name,Account Number,Auto DR Profile Start Date, Program in File,Program in DRAS,Discrepancy\n");
		
		SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
		
		for (SCEParticipantProgram pp: programNonUpdatedParts) {
			if (pp.isClientDiscrepancy()) {
	            content.append("\"");
				content.append(pp.getParticipant().getParticipantName());
				content.append("\",\"");
				content.append(pp.getParticipant().getAccountNumber());
				content.append("\",\"");
				if (pp.getParticipant().getAutoDrProfileStartDate() !=null)
					content.append(format.format(pp.getParticipant().getAutoDrProfileStartDate()));					
				content.append("\",\"");
				content.append(formatStringList(pp.getProgramInFile()));
				content.append("\",\"");
				content.append(formatStringList(pp.getClientPrograms()));
				content.append("\",Client Program Discrepancy\n");
			}
		}
		
		for (SCEParticipantProgram pp: programUpdatedParts) {
            content.append("\"");
			content.append(pp.getParticipant().getParticipantName());
			content.append("\",\"");
			content.append(pp.getParticipant().getAccountNumber());
			content.append("\",\"");
			if (pp.getParticipant().getAutoDrProfileStartDate() !=null)
				content.append(format.format(pp.getParticipant().getAutoDrProfileStartDate()));
			content.append("\",\"");
			content.append(formatStringList(pp.getProgramInFile()));
			content.append("\",\"");
			content.append(formatStringList(pp.getProgramInDras()));
			content.append("\",Program Discrepancy\n");
		}

		for (SCEParticipantEntry entry: partsNotInDRAS) {
			if (!entry.isRtpParticipant()) {
                content.append("\"");
                content.append(entry.getCustomerName());                
    			content.append("\",\"");
				content.append(entry.getServiceAccount());
				content.append("\",\"");
				if (entry.getAutoDrProfileStartDate() !=null)
					content.append(format.format(entry.getAutoDrProfileStartDate()));
				content.append("\",,,Participants missing from DRAS\n");
			}
		}

		for (Participant p: partsNotInFile) {
            content.append("\"");
			content.append(p.getParticipantName());
			content.append("\",\"");
			content.append(p.getAccountNumber());
			content.append("\",\"");
			if (p.getAutoDrProfileStartDate() !=null)
				content.append(format.format(p.getAutoDrProfileStartDate()));
			content.append("\",,,");
			content.append("Customer Missing from Report\n");
		}
		for (Participant p: partsMissSlap) {
            content.append("\"");
			content.append(p.getParticipantName());
			content.append("\",\"");
			content.append(p.getAccountNumber());
			content.append("\",\"");
			if (p.getAutoDrProfileStartDate() !=null)
				content.append(format.format(p.getAutoDrProfileStartDate()));
			content.append("\",,,");
			content.append("Participants Missing SLAP value from Report\n");
		}
		for (Participant p: partsMissAbank) {
            content.append("\"");
			content.append(p.getParticipantName());
			content.append("\",\"");
			content.append(p.getAccountNumber());
			content.append("\",\"");
			if (p.getAutoDrProfileStartDate() !=null)
				content.append(format.format(p.getAutoDrProfileStartDate()));
			content.append("\",,,");
			content.append("Participants Missing ABANK value from Report\n");
		}
		for (Participant p: partsMissSubstation) {
            content.append("\"");
			content.append(p.getParticipantName());
			content.append("\",\"");
			content.append(p.getAccountNumber());
			content.append("\",\"");
			if (p.getAutoDrProfileStartDate() !=null)
				content.append(format.format(p.getAutoDrProfileStartDate()));
			content.append("\",,,");
			content.append("Participants Missing SUBSTATION value from Report\n");
		}
		for (Participant p: partsMissBlock) {
            content.append("\"");
			content.append(p.getParticipantName());
			content.append("\",\"");
			content.append(p.getAccountNumber());
			content.append("\",\"");
			if (p.getAutoDrProfileStartDate() !=null)
				content.append(format.format(p.getAutoDrProfileStartDate()));
			content.append("\",,,");
			content.append("Participants Missing BLOCK NUMBER value from Report\n");
		}
		for (Participant p: partsMissProgramOption) {
            content.append("\"");
			content.append(p.getParticipantName());
			content.append("\",\"");
			content.append(p.getAccountNumber());
			content.append("\",\"");
			if (p.getAutoDrProfileStartDate() !=null)
				content.append(format.format(p.getAutoDrProfileStartDate()));
			content.append("\",,,");
			content.append("Participants Missing PROGRAM OPTION value from Report\n");
		}

		log.info(LogUtils.createLogEntry("", "SCE Participant Upload From FTP drop", subject.toString(), content.toString()));
		sendNotifications(subject.toString(), "", "DRAS Discrepancy Report.csv", content.toString());
	}
	
    protected void sendNotifications(String subject, String content, String attachFilename, String attachFileContent){
		sendDRASOperatorEventNotification(subject, content, attachFilename, attachFileContent,
				NotificationMethod.getInstance(), new NotificationParametersVO(), "", notifier);
	}
	
    public static synchronized void sendDRASOperatorEventNotification(
            String subject, String content, String attachFilename, String attachFileContent, NotificationMethod method,
            NotificationParametersVO params, String programName, Notifier notifier) {
        ContactManager cm = EJB3Factory.getLocalBean(ContactManager.class);

        EventStateCacheHelper cache = EventStateCacheHelper.getInstance();
		List<Contact> contacts =  cache.getEscacheforoperatorcontacts();
        if(contacts.isEmpty()){
        	contacts = cm.getOperatorContacts();
        	cache.setEscacheforoperatorcontacts("OperatorContacts", contacts);
        }
        List<Contact> sendList = new ArrayList<Contact>();

        for (Contact c : contacts) {
        	if (c.eventNotificationOn()) {
                sendList.add(c);
            }
        }

        if (sendList.size() > 0) {
        	notifier.sendNotification(sendList, "operator", subject, content,attachFilename, attachFileContent,
                    method, params, Environment.isAkuacomEmailOnly(),
                    programName);
        }
    }

}
