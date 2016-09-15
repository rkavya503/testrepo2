// $Revision: 1.1 $ $Date: 2000/01/01 00:00:00 $
package com.akuacom.pss2.richsite;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.richfaces.event.UploadEvent;
import org.richfaces.model.UploadItem;

import com.kanaeki.firelog.util.FireLogEntry;

public abstract class Import
{

	private static final Logger log = Logger.getLogger(ParticipantImport.class
		.getName());

	protected enum Column
	{
		RECORDS("record"), CLIENT_NAME("Client"), CLIENT_PASSWORD(
			"Client Password"), PARTICIPANT_NAME("Participant"),
		PARTICIPANT_PASSWORD("Participant Password"), ACCOUNT_NUMBER(
			"Account #"), TYPE("Type"), AGGREGATOR_FLAG("Aggregator"),
		TEST_FLAG("Test Participant"), PROGRAMS("Programs"), CLIENT_CONFIG(
			"Client Config"), CLIENT_PARTICIPATION("Client Participation"),
		PREMISE_NUMBER("Premise Number"), ENROLLED_DATE("Enrolled Date"),
		START_DATE("Start Date"), DEACTIVATE_DATE("Deactivate Date"), COMMENTS(
			"Comments"), CONTACT_CLIENT_NAME("Client"), NAME("Name"), ADDRESS(
			"Contact"), CONTACT_TYPE("Type"), COMMS_NOTIFICATIONS("Comms"),
		EVENT_NOTIFICATIONS("EventNotification"), OFF_SEASON("OffSeason"),
		ON_SEASON("OnSeason"), THRESHOLD("Threshold");

		private String columnName;

		Column(String columnName)
		{
			this.columnName = columnName;
		}

		public String getColumnName()
		{
			return columnName;
		}
	}

	protected enum Error
	{
		ILLEGAL_CHARACTER("illegal character"),
		ALREADY_EXISTS("already exists"),
		DATE_FORMAT("must be a date (mm/dd/yyyy)"),
		ALREADY_ACCOUNT("already used as an account number"),
		CANT_BE_SAME("can't be same as client name"),
		TYPE_LEGAL_VALUES("must be 'AUTO' or 'MANUAL'"),
		DOESNT_EXIST("doesn't exist"),
		CANT_COEXIST("can't coexists"),
		BAD_FORMAT("invalid format"),
		BAD_CONTACT_TYPE("must be 'EMAIL','VOICE','FAX',or 'PAGE'"),
		BAD_NOTIFICATION_TYPE(
			"must be 'ALL NOTIFICATIONS', 'TRATEGY INITIATED NOTIFICATIONS', or 'NO NOTIFICATIONS'"),
		POSITIVE("must be positive"), 
		NUMBER_FORMAT("must be a number"),
		EMAIL_ONLY("only EMAIL types are allowed"), 
		EMPTY("can't be empty"), 
		X("must be 'X' or empty"), 
		INTERNAL("internal error (see logs)");
		private String errorMessage;

		Error(String errorMessage)
		{
			this.errorMessage = errorMessage;
		}

		String getErrorMessage()
		{
			return errorMessage;
		}
	}

	protected enum UploadStatus
	{
		IDLE, CHOOSING, RUNNING, CANCELLED, COMPLETE
	}

	protected static final DateFormat dateFormat = new SimpleDateFormat(
		"MM/dd/yyyy");

	protected List<String> fileUploadOutput;
	protected String parseLine;
	protected int lineNum;
	protected int totalLineNum;
	protected UploadStatus uploadStatus;
	protected java.io.File importFile;
	protected boolean validateOnly;
	protected String lineName;

	private List<String> list1 = new ArrayList<String>();
	private List<String> list2 = new ArrayList<String>();

	public void fileUploadListener(UploadEvent event) throws Exception
	{
		UploadItem item = event.getUploadItem();

		importFile = item.getFile();
		uploadStatus = UploadStatus.RUNNING;
	}

	protected abstract List<String> parseFile(File participantFile,
		boolean validateOnly, List<String> list1, List<String> list2)
		throws FileNotFoundException;

	protected abstract boolean is2Lists();

	protected abstract String getObjectName();
	
	protected abstract String getList1Name();
	
	protected abstract String getList2Name();

	public void fileChoosenAction()
	{
		try
		{
			List<String> errors = parseFile(importFile, true, list1, list2);
			if (errors.size() != 0)
			{
				fileUploadOutput = errors;
				if (uploadStatus == UploadStatus.CANCELLED)
				{
					fileUploadOutput
						.add("CANCELLED: Not processed, errors occurred");
				}
				else
				{
					fileUploadOutput
						.add("FAILURE: Not processed because of errors");
				}
				log.error(createLogEntry("import failed", getObjectName()
					+ "Import", fileUploadOutput, null, null));
			}
			else
			{
				errors = parseFile(importFile, false, list1, list2);
				if (errors.size() != 0)
				{
					fileUploadOutput = errors;
					if (uploadStatus == UploadStatus.CANCELLED)
					{
						fileUploadOutput
							.add("CANCELLED: Partially processed, errors occurred");
					}
					else
					{
						fileUploadOutput
							.add("WARNING: Processed, errors occurred");
					}
				}
				else
				{
					fileUploadOutput = new ArrayList<String>();
					if (uploadStatus == UploadStatus.CANCELLED)
					{
						fileUploadOutput
							.add("CANCELLED: Partially processed, no errors");
					}
					else
					{
						fileUploadOutput.add("SUCCESS: Processed, no errors");
					}
				}
				fileUploadOutput.add(getList1Name() + " created: "
					+ list1.size());
				if(is2Lists())
				{
					fileUploadOutput.add(getList2Name() + " created: "
						+ list2.size());
				}
				log.warn(createLogEntry("import succeeded",
					getObjectName() + " Import", fileUploadOutput,
					list1, list2));
			}
		}
		catch (FileNotFoundException e)
		{
			log.error(createLogEntry("import failed: file not found",
				getObjectName() + " Import", fileUploadOutput, list1,
				list2));
		}
		lineNum = totalLineNum + 1;
		uploadStatus = UploadStatus.COMPLETE;
	}

	public void addMessage(List<String> messages, int lineNum, Column column,
		Error error, String value)
	{
		messages.add(lineNumber(lineNum) + "Error in " + column.getColumnName()
			+ ": " + error.getErrorMessage() + ": " + value);
	}

	public void addMessage(List<String> messages, int lineNum, Column column,
		String error, String value)
	{
		messages.add(lineNumber(lineNum) + "Error in " + column.getColumnName()
			+ ": " + error + ": " + value);
	}

	public boolean validateLength(String token, int maxLength, Column column,
		List<String> messages, int lineNum)
	{
		if (token.length() > maxLength)
		{
			addMessage(messages, lineNum, column, "too long [max length: "
				+ maxLength + "]", token);
			return false;
		}
		return true;
	}

	public boolean validateNotEmpty(String token, Column column,
		List<String> messages, int lineNum)
	{
		if (token.length() == 0)
		{
			addMessage(messages, lineNum, column, Error.EMPTY, token);
			return false;
		}
		return true;
	}

	public boolean validateX(String token, Column column,
		List<String> messages, int lineNum)
	{
		if (token.length() > 0 && !"X".equalsIgnoreCase(token))
		{
			addMessage(messages, lineNum, column, Error.X, token);
			return false;
		}
		return true;
	}

	public boolean validateDate(String token, Column column,
		List<String> messages, int lineNum)
	{
		if (token.length() == 0)
		{
			return true;
		}

		try
		{
			Date d = dateFormat.parse(token);
		}
		catch (ParseException e)
		{
			addMessage(messages, lineNum, column, Error.DATE_FORMAT, token);
			return false;
		}
		return true;
	}

	public boolean validateDouble(String token, Column column,
		List<String> messages, int lineNum)
	{
		try
		{
			double d = Double.parseDouble(token);
			if (d < 0.0)
			{
				addMessage(messages, lineNum, column, Error.POSITIVE, token);
				return false;
			}
		}
		catch (NumberFormatException e)
		{
			addMessage(messages, lineNum, column, Error.NUMBER_FORMAT, token);
			return false;
		}
		return true;
	}

	public boolean validateInteger(String token, Column column,
		List<String> messages, int lineNum)
	{
		try
		{
			int i = Integer.parseInt(token);
			if (i < 0)
			{
				addMessage(messages, lineNum, column, Error.POSITIVE, token);
				return false;
			}
		}
		catch (NumberFormatException e)
		{
			addMessage(messages, lineNum, column, Error.NUMBER_FORMAT, token);
			return false;
		}
		return true;
	}

	public String lineNumber(int lineNum)
	{
		return "[" + lineNum + "] ";
	}

	public FireLogEntry createLogEntry(String description, String category,
		List<String> messages, List<String> objects1, List<String> objects2)
	{
		FireLogEntry logEntry = new FireLogEntry();
		logEntry.setUserParam1("");
		logEntry.setCategory(category);
		logEntry.setDescription(description);
		StringBuilder sb = new StringBuilder();
		for (String message : messages)
		{
			sb.append(message);
			sb.append("\n");
		}
		if (objects1 != null)
		{
			sb.append("\ncreated:\n");
			for (String object : objects1)
			{
				sb.append(object);
				sb.append("\n");
			}
		}
		if(is2Lists())
		{
			if (objects2 != null)
			{
				sb.append("\ncreated:\n");
				for (String object : objects2)
				{
					sb.append(object);
					sb.append("\n");
				}
			}
		}
		logEntry.setLongDescr(sb.toString());
		return logEntry;
	}

	public void fileUploadStartAction()
	{
		log.info("file upload started");
		lineNum = 0;
		totalLineNum = 0;
		uploadStatus = UploadStatus.CHOOSING;
	}

	public String fileUploadDoneAction()
	{
		log.info("file upload done");
		uploadStatus = UploadStatus.IDLE;
		fileUploadOutput = new ArrayList<String>();
		
		return "fileUploadDone";
	}

	public void fileUploadCancelAction()
	{
		log.info("file upload cancelled");
		uploadStatus = UploadStatus.CANCELLED;
	}

	public boolean isUploadInProgress()
	{
		if (uploadStatus == UploadStatus.COMPLETE)
		{
			return false;
		}
		else
		{
			return true;
		}
	}

	public boolean isFileUploadDisabled()
	{
		return uploadStatus == UploadStatus.CANCELLED;
	}

	public boolean isRenderCancel1()
	{
		if (uploadStatus == UploadStatus.CHOOSING)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public boolean isRunning()
	{
		return uploadStatus == UploadStatus.RUNNING ? true : false;
	}

	public List<String> getFileUploadOutput()
	{
		return fileUploadOutput;
	}

	protected String parseToken(char delimiter)
	{
		String returnString;

		int index = parseLine.indexOf(delimiter);
		if (index >= 0)
		{
			returnString = parseLine.substring(0, index).trim();
			parseLine = parseLine.substring(index + 1);
		}
		else
		{
			returnString = parseLine.trim();
		}
		return returnString;
	}

	public String getPass()
	{
		if (validateOnly)
		{
			return "Validating: " + lineName;
		}
		else
		{
			return "Uploading: " + lineName;
		}
	}

	public Long getLineNum()
	{
		return Long.valueOf(lineNum);
	}

	public java.io.File getImportFile()
	{
		return importFile;
	}

	public void setImportFile(java.io.File importFile)
	{
		this.importFile = importFile;
	}

	public int getTotalLineNum()
	{
		return totalLineNum;
	}

}
