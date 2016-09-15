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

import org.apache.log4j.Logger;

import com.akuacom.pss2.client.ClientManager;
import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.core.ProgramEJB;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.participant.ParticipantManager;
import com.akuacom.pss2.program.Program;
import com.akuacom.pss2.program.ProgramManager;
import com.akuacom.pss2.program.matrix.ProgramMatrixTrig;
import com.akuacom.pss2.program.participant.ProgramParticipant;
import com.akuacom.pss2.program.participant.ProgramParticipantManager;
import com.akuacom.pss2.userrole.ViewBuilderManager;
import com.akuacom.pss2.userrole.viewlayout.ParticipantImportLayout;
import com.akuacom.pss2.util.LogUtils;
import com.akuacom.pss2.util.PSS2Util;

public class ParticipantImport extends Import implements ParticipantImportLayout
{
	private static final Logger log = Logger.getLogger(ParticipantImport.class
		.getName());

	public ParticipantImport(){
		buildViewLayout();
	}
	@Override
	protected boolean is2Lists()
	{
		return true;
	}

	@Override
	protected String getObjectName()
	{
		return "Participant";
	}
	
	@Override
	protected String getList1Name()	{
		return "Participants";
	}

	
	@Override
	protected String getList2Name()	{
		return "Clients";
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
			list2.clear();

			BufferedReader br =
				new BufferedReader(new FileReader(participantFile));

			final ParticipantManager participantManager =
				EJBFactory.getBean(ParticipantManager.class);

			final ProgramParticipantManager programParticipantManager =
				EJBFactory.getBean(ProgramParticipantManager.class);

			final ProgramManager programManager =
				EJBFactory.getBean(ProgramManager.class);

			final ClientManager clientManager =
				EJBFactory.getBean(ClientManager.class);

			ProgramMatrixTrig pmt = programManager.getProgramMatrixTrig();

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
			while ((parseLine = br.readLine()) != null
				&& uploadStatus != UploadStatus.CANCELLED)
			{
				lineNum++;
				if (parseLine.trim().length() == 0)
				{
					continue;
				}
				String originalLine = new String(parseLine);

				lineName = "";
				String clientName = "";

				try
				{
					clientName = parseToken(',');
					String clientPassword = parseToken(',');
					if (clientName.length() > 0)
					{
						if (!PSS2Util.isLegalName(clientName))
						{
							addMessage(messages, lineNum, Column.CLIENT_NAME,
								Error.ILLEGAL_CHARACTER, clientName);
						}
						List<String> passwordErrors = new ArrayList<String>();
						PSS2Util.validatePassword(clientPassword,
							clientPassword, false, passwordErrors);
						for (String error : passwordErrors)
						{
							addMessage(messages, lineNum,
								Column.CLIENT_PASSWORD, error.substring(13),
								clientPassword);
						}
					}

					lineName = parseToken(',');
					if (!PSS2Util.isLegalName(lineName))
					{
						addMessage(messages, lineNum, Column.PARTICIPANT_NAME,
							Error.ILLEGAL_CHARACTER, lineName);
					}

					validateLength(lineName, 20,
						Column.PARTICIPANT_NAME, messages, lineNum);

					boolean participantCreateError = false;
					try
					{
						Participant p =
							participantManager.getParticipant(lineName);
						if (p != null)
						{
							participantCreateError = true;
						}
					}
					catch (Exception e)
					{
						participantCreateError = true;
					}
					if (participantCreateError)
					{
						addMessage(messages, lineNum, Column.PARTICIPANT_NAME,
							Error.ALREADY_EXISTS, lineName);
						if (!validateOnly)
						{
							continue;
						}
					}

					String participantPassword = parseToken(',');
					List<String> passwordErrors = new ArrayList<String>();
					PSS2Util.validatePassword(participantPassword,
						participantPassword, false, passwordErrors);
					for (String error : passwordErrors)
					{
						addMessage(messages, lineNum,
							Column.PARTICIPANT_PASSWORD, error.substring(13),
							participantPassword);
					}

					if (clientName.length() > 0)
					{
						// client name exists
						boolean clientCreateError = false;
						try
						{
							Participant p =
								clientManager.getClientOnly(lineName + "."
									+ clientName);
							if (p != null)
							{
								clientCreateError = true;
							}
						}
						catch (Exception e)
						{
							clientCreateError = true;
						}
						if (clientCreateError)
						{
							addMessage(messages, lineNum, Column.CLIENT_NAME,
								Error.ALREADY_EXISTS, clientName);
							if (!validateOnly)
							{
								continue;
							}
						}

						// client name isn't in use as account number
						try
						{
							participantManager
								.getByAccount(lineName + "."
									+ clientName, true);
							addMessage(messages, lineNum,
								Column.PARTICIPANT_NAME, Error.ALREADY_ACCOUNT,
								lineName + "." + clientName);
							if (!validateOnly)
							{
								continue;
							}
						}
						catch (Exception e)
						{
							// this is ok
						}
					}

					String accountNumber = parseToken(',');
					// account number already exists
					try
					{
						participantManager
							.getByAccount(accountNumber, false);
						addMessage(messages, lineNum, Column.ACCOUNT_NUMBER,
							Error.ALREADY_EXISTS, accountNumber);
						if (!validateOnly)
						{
							continue;
						}
					}
					catch (Exception e)
					{
						// this is ok
					}
					// account number isn't same as client name
					if (accountNumber
						.equals(lineName + "." + clientName))
					{
						addMessage(messages, lineNum, Column.ACCOUNT_NUMBER,
							Error.CANT_BE_SAME, accountNumber);
					}

					String type = parseToken(',');
					if (clientName.length() > 0)
					{
						// type is legal
						int clientNameLength = 20;
						if ("AUTO".equalsIgnoreCase(type))
						{
							clientNameLength = 20;
						}
						else if ("MANUAL".equalsIgnoreCase(type))
						{
							clientNameLength = 24;
						}
						else
						{
							addMessage(messages, lineNum, Column.TYPE,
								Error.TYPE_LEGAL_VALUES, type);
						}
						// client name is legal length
						validateLength(clientName, clientNameLength,
							Column.CLIENT_NAME, messages, lineNum);
					}

					// aggregator is legal
					String aggregator = parseToken(',');
					validateX(aggregator, Column.AGGREGATOR_FLAG, messages,
						lineNum);

					// test is legal
					String test = parseToken(',');
					validateX(test, Column.TEST_FLAG, messages, lineNum);

					String programsString;
					if (parseLine.charAt(0) == '\"')
					{
						parseLine = parseLine.substring(1);
						int index = parseLine.indexOf('\"');
						programsString = parseLine.substring(0, index).trim();
						parseLine = parseLine.substring(index + 1);
						index = parseLine.indexOf(',');
						parseLine = parseLine.substring(index + 1);
					}
					else
					{
						programsString = parseToken(',');
					}
					String[] tmpProgramNames = programsString.split(",");
					List<String> programNames = new ArrayList<String>();
					for (String programName : tmpProgramNames)
					{
						if (programName.trim().length() > 0)
						{
							programNames.add(programName.trim());
						}
					}
					// programs exists
					for (String programName : programNames)
					{
						try
						{
							Program p = programManager.getProgramOnly(programName);
							if (p == null)
							{
								addMessage(messages, lineNum, Column.PROGRAMS,
									Error.DOESNT_EXIST, programName);
								if (!validateOnly)
								{
									continue;
								}
							}
						}
						catch (Exception e)
						{
							addMessage(messages, lineNum, Column.PROGRAMS,
								Error.DOESNT_EXIST, programName);
							if (!validateOnly)
							{
								continue;
							}
						}
					}
					// programs coexists in matrix
					for (String programName1 : programNames)
					{
						for (String programName2 : programNames)
						{
							if (!programName1.equals(programName2))
							{
								if (!pmt.coexistByNames(programName1,
									programName2))
								{
									addMessage(messages, lineNum,
										Column.PROGRAMS, Error.CANT_COEXIST,
										programName1 + " and " + programName2);
									if (!validateOnly)
									{
										continue;
									}
								}
							}
						}
					}

					String clientConfig = parseToken(',');
					validateX(aggregator, Column.CLIENT_CONFIG, messages,
						lineNum);

					String clientParticipation = parseToken(',');
					validateX(test, Column.CLIENT_PARTICIPATION, messages,
						lineNum);

					String premiseNumber = parseToken(',');
					validateLength(premiseNumber, 255, Column.PREMISE_NUMBER,
						messages, lineNum);

					String enrolledDate = parseToken(',');
					validateDate(enrolledDate, Column.ENROLLED_DATE, messages,
						lineNum);

					String startDate = parseToken(',');
					validateDate(startDate, Column.START_DATE, messages,
						lineNum);

					String deactivateDate = parseToken(',');
					validateDate(deactivateDate, Column.DEACTIVATE_DATE,
						messages, lineNum);

					String comments = parseLine.trim();

					if (!validateOnly)
					{
						Participant participant = new Participant();
						participant.setParticipantName(lineName);
						participant.setAccountNumber(accountNumber);
						participant.setAggregator("X"
							.equalsIgnoreCase(aggregator));
						participant.setTestParticipant("X"
							.equalsIgnoreCase(test));
						participant.setPremiseNumber(premiseNumber);
						if(enrolledDate.length() > 0)
						{
							participant.setEnrollmentDate(dateFormat.parse(enrolledDate));
						}
						if (startDate.length() > 0)
						{
							participant.setStartDate(dateFormat
								.parse(startDate));
						}
						if (deactivateDate.length() > 0)
						{
							participant.setEndDate(dateFormat
								.parse(deactivateDate));
						}
						participant.setComment(comments);
						try
						{
							participantManager.createParticipant(participant,
								participantPassword.toCharArray(), null);
							list1.add(lineName);
						}
						catch (EJBException e)
						{
							// participant exists - this is ok
						}
						for (String programName : programNames)
						{
							ProgramEJB programEJB =
								programManager.lookupProgramBean(programName
									.trim());
							Program program =
								programManager.getProgramOnly(programName.trim());
							programParticipantManager.addParticipantToProgram(
								program, participant, false, programEJB);
							ProgramParticipant programParticipant =
								programParticipantManager
									.getProgramParticipant(programName.trim(),
										lineName, false);
							programParticipant.setClientConfig("X"
								.equalsIgnoreCase(clientConfig) ? 1 : 0);
							programParticipantManager.updateProgramParticipant(
								programName.trim(), lineName, false,
								programParticipant);
						}

						if (clientName.length() != 0)
						{
							Participant client = new Participant();
							String fullClientName =
								lineName + "." + clientName;
							client.setParticipantName(fullClientName);
							client.setAccountNumber(fullClientName);
							client.setParent(lineName);
							client.setClient(true);
							if ("MANUAL".equalsIgnoreCase(type))
							{
								client.setType((byte) 2);
							}
							else
							{
								client.setType((byte) 0);
							}
							clientManager.createClient(client,
								clientPassword.toCharArray());
							list2.add(fullClientName);
							for (String programName : programNames)
							{
								ProgramParticipant pp =
									programParticipantManager
										.getClientProgramParticipants(
											programName, fullClientName, true);
								if ("X".equalsIgnoreCase(clientParticipation))
								{
									pp.setState(ProgramParticipant.PROGRAM_PART_ACTIVE);
								}
								else
								{
									pp.setState(ProgramParticipant.PROGRAM_PART_DELETED);
								}
								programParticipantManager
									.updateProgramParticipant(programName,
										fullClientName, true, pp);
							}
						}
					}

				}
				catch (Exception e)
				{
					addMessage(messages, lineNum, Column.RECORDS,
						Error.INTERNAL, "");
					log.error(LogUtils.createExceptionLogEntry("",
						"Participant Import", e));
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
	private boolean importModalCancel1;
	private boolean importModalCancel2;
	private boolean importModalDone;
	
	
	
	public boolean isFileUploadEnabled() {
		return fileUploadEnabled;
	}
	public void setFileUploadEnabled(boolean fileUploadEnabled) {
		this.fileUploadEnabled = fileUploadEnabled;
	}	
	
	public boolean isImportModalCancel1() {
		return importModalCancel1;
	}
	public void setImportModalCancel1(boolean importModalCancel1) {
		this.importModalCancel1 = importModalCancel1;
	}
	public boolean isImportModalCancel2() {
		return importModalCancel2;
	}
	public void setImportModalCancel2(boolean importModalCancel2) {
		this.importModalCancel2 = importModalCancel2;
	}
	public boolean isImportModalDone() {
		return importModalDone;
	}
	public void setImportModalDone(boolean importModalDone) {
		this.importModalDone = importModalDone;
	}
	private void buildViewLayout(){
        try {
        	getViewBuilderManager().buildParticipantImportLayout(this);
        } catch (NamingException e) {                // log exception

        }
	}

	private ViewBuilderManager getViewBuilderManager() throws NamingException{
       return (ViewBuilderManager) new InitialContext().lookup("pss2/ViewBuilderManagerBean/local");
	}

}
