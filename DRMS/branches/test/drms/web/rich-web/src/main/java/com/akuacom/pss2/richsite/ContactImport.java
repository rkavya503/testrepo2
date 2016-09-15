// $Revision: 1.1 $ $Date: 2000/01/01 00:00:00 $
package com.akuacom.pss2.richsite;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJBException;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.commons.validator.EmailValidator;
import org.apache.log4j.Logger;

import com.akuacom.ejb.client.EJB3Factory;
import com.akuacom.pss2.client.ClientManager;
import com.akuacom.pss2.contact.Contact;
import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.participant.contact.ContactEventNotificationType;
import com.akuacom.pss2.participant.contact.ParticipantContact;
import com.akuacom.pss2.system.SystemManager;
import com.akuacom.pss2.system.property.PSS2Features;
import com.akuacom.pss2.userrole.ViewBuilderManager;
import com.akuacom.pss2.userrole.viewlayout.ClientImportLayout;
import com.akuacom.pss2.util.LogUtils;

public class ContactImport extends Import implements ClientImportLayout
{
	private static final Logger log = Logger.getLogger(ContactImport.class
		.getName());

	public ContactImport(){
		buildViewLayout();
	}
	
	@Override
	protected boolean is2Lists()
	{
		return false;
	}

	@Override
	protected String getObjectName()
	{
		return "Contacts";
	}
	
	@Override
	protected String getList1Name()	{
		return "Contact";
	}

	
	@Override
	protected String getList2Name()	{
		return "";
	}


	@Override
	protected List<String> parseFile(File participantFile,
		boolean validateOnly, List<String> list1, List<String> list2)
		throws FileNotFoundException
	{
		List<String> messages = new ArrayList<String>();
		this.validateOnly = validateOnly;

		try
		{
			list1.clear();

			BufferedReader br =
				new BufferedReader(new FileReader(participantFile));

			final ClientManager clientManager =
				EJBFactory.getBean(ClientManager.class);

			SystemManager systemManager =
				EJB3Factory.getLocalBean(SystemManager.class);
			PSS2Features features = systemManager.getPss2Features();

			if (validateOnly)
			{
				// read header
				br.readLine();
				totalLineNum = 0;
				while ((parseLine = br.readLine()) != null)
				{
					totalLineNum++;
					if (parseLine.trim().length() == 0)
					{
						continue;
					}
				}
				br = new BufferedReader(new FileReader(participantFile));
			}

			// read header
			String header = br.readLine();
			// TODO: validate header?
			lineNum = 0;
			while ((parseLine = br.readLine()) != null)
			{
				lineNum++;
				if (parseLine.trim().length() == 0)
				{
					continue;
				}
				String originalLine = new String(parseLine);

				String clientName = "";

				try
				{
					clientName = parseToken(',');
					// client name exists
					boolean clientExistsFlag = false;
					try
					{
						Participant p = clientManager.getClientOnly(clientName);
						if (p != null)
						{
							clientExistsFlag = true;
						}
					}
					catch (Exception e)
					{
					}
					if (!clientExistsFlag)
					{
						addMessage(messages, lineNum,
							Column.CONTACT_CLIENT_NAME,
							Error.DOESNT_EXIST, clientName);
					}

					String name = parseToken(',');
					validateLength(name, 255,
						Column.NAME, messages, lineNum);
					validateNotEmpty(name, Column.NAME, messages, lineNum);
					
					String address = parseToken(',');

					String type = parseToken(',');
					
					lineName = "client:" + clientName + ", name:"
							+ name + ", address:" + address + ", type:" + type;
							
					String parsedType = Contact.EMAIL_ADDRESS;
					if (type.equalsIgnoreCase("EMAIL"))
					{
						if (!EmailValidator.getInstance().isValid(address))
						{
							addMessage(messages, lineNum,
								Column.ADDRESS,
								Error.BAD_FORMAT, address);
						}
						parsedType = Contact.EMAIL_ADDRESS;
					}
					else
					{
						if (!features.isFeatureExtendedNotificationEnabled())
						{
							addMessage(messages, lineNum,
								Column.CONTACT_TYPE,
								Error.EMAIL_ONLY, type);
						}
						else
						{
							if ("VOICE".equalsIgnoreCase(type))
							{
								parsedType = Contact.PHONE_NUMBER;
							}
							else if ("FAX".equalsIgnoreCase(type))
							{
								parsedType = Contact.FAX_NUMBER;
							}
							else if ("PAGE".equalsIgnoreCase(type))
							{
								parsedType = Contact.PAGER;
							}
							else
							{
								addMessage(messages, lineNum,
									Column.CONTACT_TYPE,
									Error.BAD_CONTACT_TYPE, type);
							}
						}
					}

					// commsNotification is legal
					String commsNotification = parseToken(',');
					validateX(commsNotification,
						Column.COMMS_NOTIFICATIONS, messages,
						lineNum);

					// commsNotification is legal
					String eventNotification = parseToken(',');
					ContactEventNotificationType parsedEventNotification =
						ContactEventNotificationType.FullNotification;
					if ("ALL NOTIFICATIONS".equalsIgnoreCase(eventNotification))
					{
						parsedEventNotification =
							ContactEventNotificationType.FullNotification;
					}
					else if ("STRATEGY INITIATED NOTIFICATIONS"
						.equalsIgnoreCase(eventNotification))
					{
						parsedEventNotification =
							ContactEventNotificationType.NotNormalNotification;
					}
					else if ("NO NOTIFICATIONS"
						.equalsIgnoreCase(eventNotification))
					{
						parsedEventNotification =
							ContactEventNotificationType.NoNotification;
					}
					else
					{
						addMessage(messages, lineNum,
							Column.EVENT_NOTIFICATIONS,
							Error.BAD_NOTIFICATION_TYPE,
							eventNotification);
					}

					String offSeason = parseToken(',');
					validateDouble(offSeason,
						Column.OFF_SEASON, messages, lineNum);

					String onSeason = parseToken(',');
					validateDouble(onSeason,
						Column.ON_SEASON, messages, lineNum);

					String threshold = parseToken(',');
					validateInteger(threshold,
						Column.THRESHOLD, messages, lineNum);

					Participant client = clientManager.getClientWithContacts(clientName);
					if (client != null && client.getContacts() != null)
					{
						for (ParticipantContact contact : client.getContacts())
						{
							if (contact.getAddress().equals(address)
								&& contact.getType().equals(parsedType))
							{
								addMessage(messages, lineNum,
									Column.ADDRESS,
									Error.ALREADY_EXISTS, address
										+ "(" + type + ")");
								if (!validateOnly)
								{
									continue;
								}
							}
						}
					}

					if (!validateOnly)
					{
						ParticipantContact contact = new ParticipantContact();
						contact.setDescription(name);
						contact.setAddress(address);
						contact.setType(parsedType);
						contact.setCommNotification("X"
							.equalsIgnoreCase(commsNotification));
						contact.setEventNotification(parsedEventNotification);
						contact.setOffSeasonNotiHours(Double
							.parseDouble(offSeason));
						contact.setOnSeasonNotiHours(Double
							.parseDouble(onSeason));
						contact.setMsgThreshold(Integer.parseInt(threshold));
						client.getContacts().add(contact);
						contact.setParticipant(client);
						clientManager.updateClient(client);
						list1.add(lineName);
					}
				}
				catch (EJBException e)
				{
					addMessage(messages, lineNum,
						Column.RECORDS, Error.INTERNAL,
						"");
					log.error(LogUtils.createExceptionLogEntry("",
						"Contacts Import", e));
					continue;
				}
			}
		}
		catch (IOException e)
		{
			// TODO: something reasonable
		}
		return messages;
	}
	
	private boolean fileUploadEnabled;
	private boolean importModalDone;

	public boolean isFileUploadEnabled() {
		return fileUploadEnabled;
	}

	public void setFileUploadEnabled(boolean fileUploadEnabled) {
		this.fileUploadEnabled = fileUploadEnabled;
	}

	public boolean isImportModalDone() {
		return importModalDone;
	}

	public void setImportModalDone(boolean importModalDone) {
		this.importModalDone = importModalDone;
	}
	
	private void buildViewLayout(){
        try {
        	getViewBuilderManager().buildClientImportLayout(this);
        } catch (NamingException e) {                // log exception

        }
	}

	private ViewBuilderManager getViewBuilderManager() throws NamingException{
       return (ViewBuilderManager) new InitialContext().lookup("pss2/ViewBuilderManagerBean/local");
	}
	
}
