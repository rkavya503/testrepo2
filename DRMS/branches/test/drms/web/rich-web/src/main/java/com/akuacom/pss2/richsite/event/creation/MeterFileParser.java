/**
 * 
 */
package com.akuacom.pss2.richsite.event.creation;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.program.Program;
import com.akuacom.pss2.program.ProgramManager;
import com.akuacom.pss2.query.NativeQueryManager;
import com.akuacom.pss2.system.SystemManager;

/**
 * the class MeterFileParser
 */
public class MeterFileParser implements Serializable {

	private static final long serialVersionUID = 3839002286356930802L;

    private static final Logger log = Logger.getLogger(MeterFileParser.class.getName());

	private static final String EVENT_MONTH_FORMAT="yyyy-MM";

	private String programName;
	private Date month;
	private Date startTime;
	private Date endTime;
	private String programClass;
	private List<String> locations=new ArrayList<String>();
	
	private List<String> accounts=new ArrayList<String>();
	private List<String> validAccounts=new ArrayList<String>();
	private List<String> invalidAccounts=new ArrayList<String>();
	
	List<String> errors=new ArrayList<String>();
	List<String> warnings=new ArrayList<String>();

	public MeterFileParser(){
	}
	
	public MeterFileParser(String file, String programName, Date startTime, Date endTime) {
		this.programName=programName;
		this.startTime=startTime;
		this.endTime=endTime;
		
		parse(file);
		validate();
	}
	
	public void parseLocation(String file, String programName) {
		this.programName=programName;
		
		parse(file);
	}
	
	public void parse(String file){
		StringBuilder regx=new StringBuilder();
		regx.append("\\s*(\\d{4}-\\d{1,2})\\s*,");  //month yyyy-MM (1)
		regx.append("\\s*([^,]*\\S)\\s*,");  //program (2) 
		regx.append("\\s*([^,]*\\S)\\s*,");  //participant ID (3)
		regx.append("((\"[^\"]*\")?|[^,]*),");  //participant (4)(5)
		regx.append("\\s*([^,]*\\S)\\s*,");  //location (6)
		regx.append("\\s*([^,]*\\S)\\s*,");  //product (7)
		regx.append("\\s*([^,]*\\S)\\s*,");  //load type
		regx.append("\\s*([^,]*\\S)\\s*,");  //MPP
		regx.append("\\s*([^,]*\\S)\\s*,");  //Meter Site Name
		regx.append("\\s*([^,]*\\S)\\s*,");  //Reference ID
		regx.append("\\s*([^,]*\\S)\\s*,");  //Service Account (12)
		regx.append("\\s*([^,]*\\S)\\s*,*");  //Baseline Option
		
		ProgramManager pm = EJBFactory.getBean(ProgramManager.class);
		if(pm!=null){
			this.setProgramClass(pm.getProgramClassByName(this.programName));
		}
		SystemManager sm = EJBFactory.getBean(SystemManager.class);
		boolean useSecondaryUtilityName=sm.getPss2Features().isUseSecondaryUtilityNameForAPX();
		
		SimpleDateFormat format=new SimpleDateFormat(EVENT_MONTH_FORMAT);
		
		Pattern p = Pattern.compile("(^.*$)",Pattern.MULTILINE);
   		Matcher m = p.matcher(file);
   		
		String row=null;
		List<String> slaps=ProgramConverter.getSLAPList();

		while(m.find()){
			row=m.group(0);
			
			Pattern p1 = Pattern.compile(regx.toString());
			Matcher m1 = p1.matcher(row);
			
			if(m1.find()) {
				try {
					String month=m1.group(1);
					String program=m1.group(2);
					String product=m1.group(7);
					String serviceAccount=m1.group(12);
					
					String location=m1.group(6);
					
					String convertedProgram=null;
					if (useSecondaryUtilityName) {
						Program prog=pm.getBySecondaryUtilityName(program+" "+product);
						if (prog!=null){
							convertedProgram= prog.getProgramName();
						}else{
							convertedProgram=ProgramConverter.getProgram(program, product);	
						}
					} else {
						convertedProgram=ProgramConverter.getProgram(program, product);
					}
					
					if (convertedProgram!=null && programName.equals(convertedProgram)) {
						if (this.month==null)
							this.month=format.parse(month);
						this.accounts.add(serviceAccount);
						if (slaps.contains(location) && !locations.contains(location))
							locations.add(location);
					}
				} catch(ParseException e){
					log.error(e);
				}
			}
		}
	}
	
	public void validate() {
		SimpleDateFormat format=new SimpleDateFormat(EVENT_MONTH_FORMAT);
		try {
			if (this.month!=null && (format.parse(format.format(startTime)).getTime()!=this.month.getTime() ||
					format.parse(format.format(startTime)).getTime()!=this.month.getTime())) {
				String desc="Event start time or end time does not match the Month (" + format.format(month) 
						+ ") defined in the meter file";
				errors.add(desc);
			}
			
//			if (ProgramConverter.isUploadCBPProgram(programName)||"CBP".equalsIgnoreCase(this.getProgramClass()) ) {
//				if(this.locations.size()==0){
//					String desc="No available locations";
//					errors.add(desc);	
//				}
//			}

		} catch (ParseException e) {
			log.error(e);
		}
		
		if (errors.size()==0) {
			validateServiceAccount();
		}
	}
	
	public void validateServiceAccount(){
		NativeQueryManager nativeQuery=EJBFactory.getBean(NativeQueryManager.class);
		for (String account:accounts) {
			try {
				int exist=nativeQuery.validateAccount(programName, account);
				if (exist>0) 
					validAccounts.add(account);
				else 
					invalidAccounts.add(account);
			} catch (SQLException e) {
				log.error(e.getMessage(), e);
			}
		}
	}
	
	public static void main(String[] args) {
		try {
			File file=new File("C:\\Users\\e333812\\Desktop\\Monthly Meter Nomination-1.csv");
			InputStream inputStream;
				inputStream = new BufferedInputStream(new FileInputStream(file));
			BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = in.readLine()) != null) {
				sb.append(line);
				sb.append('\n');
			}
			inputStream.close();
			
			SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date start=format.parse("2013-04-20 12:00:00");
			Date end=format.parse("2013-04-20 16:00:00");
			
			MeterFileParser parser=new MeterFileParser(sb.toString(), "CBP 1-4 DA", start, end);
			System.out.println(parser.getMonth());
			System.out.println(parser.getAccounts().size());
			System.out.println(parser.getAccounts().toString());
			
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public String getProgramName() {
		return programName;
	}

	public void setProgramName(String programName) {
		this.programName = programName;
	}

	public Date getMonth() {
		return month;
	}

	public void setMonth(Date month) {
		this.month = month;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public List<String> getValidAccounts() {
		return validAccounts;
	}

	public void setValidAccounts(List<String> validAccounts) {
		this.validAccounts = validAccounts;
	}

	public List<String> getErrors() {
		return errors;
	}

	public void setErrors(List<String> errors) {
		this.errors = errors;
	}

	public List<String> getAccounts() {
		return accounts;
	}

	public void setAccounts(List<String> accounts) {
		this.accounts = accounts;
	}

	public List<String> getWarnings() {
		return warnings;
	}

	public void setWarnings(List<String> warnings) {
		this.warnings = warnings;
	}

	public List<String> getLocations() {
		return locations;
	}

	public void setLocations(List<String> locations) {
		this.locations = locations;
	}

	public List<String> getInvalidAccounts() {
		return invalidAccounts;
	}

	public void setInvalidAccounts(List<String> invalidAccounts) {
		this.invalidAccounts = invalidAccounts;
	}

	/**
	 * @return the programClass
	 */
	public String getProgramClass() {
		return programClass;
	}

	/**
	 * @param programClass the programClass to set
	 */
	public void setProgramClass(String programClass) {
		this.programClass = programClass;
	}
	
}
