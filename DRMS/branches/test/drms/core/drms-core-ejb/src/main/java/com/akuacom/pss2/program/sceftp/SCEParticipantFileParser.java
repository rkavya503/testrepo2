/**
 * 
 */
package com.akuacom.pss2.program.sceftp;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.akuacom.pss2.core.ProgramValidationException;
import com.akuacom.pss2.core.ProgramValidationMessage;

/**
 * the class SCEParticipantFileParser
 * 
 */
public class SCEParticipantFileParser {

	private static final String DATE_TIME_FORMAT="MM/dd/yyyy";
	private static final String PARTICIPATION_NO="NO";
	
	private String filename;
	List<SCEParticipantEntry> partEntryList=new ArrayList<SCEParticipantEntry>();
	List<String> accounts=new ArrayList<String>();
	
	List<ProgramValidationMessage> messages=new ArrayList<ProgramValidationMessage>();
	
	public SCEParticipantFileParser(String fileString, String filename) {
		this.filename=filename;
		parse(fileString);
	}
	
	public void validation() throws ProgramValidationException {
		if (this.partEntryList.size()==0) {
			String message="No available information exists in the participant information file " + filename;
			ProgramValidationException exception=new ProgramValidationException();
			List<ProgramValidationMessage> errors=new ArrayList<ProgramValidationMessage>();
			errors.add(new ProgramValidationMessage("FileStructureError", message));
			errors.addAll(messages);
			exception.setErrors(errors);
			throw exception;
		}
	}

	protected void parse(String fileString){
		StringBuilder regx=new StringBuilder();
		
		regx.append("\\s*([^,]*\\S)\\s*,");  //service account number
		regx.append("((\"[^\"]*\")?|[^,]*),");  //customer name
		regx.append("\\s*([^,]*\\S)?\\s*,");  //service street address
		regx.append("\\s*([^,]*\\S)?\\s*,");  //service city name
		regx.append("\\s*([^,]*\\S)?\\s*,");  //zip
		regx.append("\\s*([^,]*\\S)?\\s*,");  //service plan
		regx.append("\\s*([^,]*\\S)?\\s*,");  //abank
		regx.append("\\s*([^,]*\\S)?\\s*,");  //SLap
		regx.append("\\s*([^,]*\\S)?\\s*,");  //Pnode
		regx.append("\\s*(\\d{1,2}/\\d{1,2}/\\d{4})?\\s*,");  // rate effective date
		regx.append("\\s*([^,]*\\S)?\\s*,");  //DBP participant
		regx.append("\\s*([^,]*\\S)?\\s*,");  //SDP participant
		regx.append("\\s*([^,]*\\S)?\\s*,");  //CPP participant
		regx.append("\\s*([^,]*\\S)?\\s*,");  //BIP participant
		regx.append("\\s*([^,]*\\S)?\\s*,");  //API participant
		regx.append("\\s*([^,]*\\S)?\\s*,");  //SLRP participant
		regx.append("\\s*([^,]*\\S)?\\s*,");  //DRC participant
		regx.append("\\s*([^,]*\\S)?\\s*,");  //Auto DR participant
		regx.append("\\s*([^,]*\\S)?\\s*,");  //RTP participant
		regx.append("\\s*([^,]*\\S)?\\s*,");  //RTP Voltag
		regx.append("\\s*([^,]*\\S)?\\s*,");  //Direct Access Participant
		regx.append("\\s*([^,]*\\S)?\\s*,");  //CBP participant
		regx.append("((\"[^\"]*\")?|[^,]*),"); //BCD Rep		
		regx.append("\\s*((\"\\d{1,2}/\\d{1,2}/\\d{4}\")|(\\d{1,2}/\\d{1,2}/\\d{4}))?\\s*,"); //Auto DR Profile Start Date
//		regx.append("\\s*(\\d{1,2}/\\d{1,2}/\\d{4})?\\s*,*"); //Auto DR Profile Start Date
		regx.append("\\s*([^,]*\\S)?\\s*,");  //substation
		regx.append("\\s*([^,]*\\S)?\\s*,");  //block number
		regx.append("\\s*([^,]*\\S)?\\s*,*");  //program option
		SimpleDateFormat format=new SimpleDateFormat(DATE_TIME_FORMAT);
		
		Pattern p = Pattern.compile("(^.*$)",Pattern.MULTILINE);
   		Matcher m = p.matcher(fileString);
   		
		String row=null;

		while(m.find()){
			row=m.group(0);
			
			Pattern p1 = Pattern.compile(regx.toString());
			Matcher m1 = p1.matcher(row);
			
			if(m1.find()) {
				try {
					SCEParticipantEntry entry=new SCEParticipantEntry();
					entry.setServiceAccount(m1.group(1));
					entry.setCustomerName(m1.group(2));
					entry.setServiceStreetAddress(m1.group(4));
					entry.setServiceCityName(m1.group(5));
					entry.setZip(m1.group(6));
					entry.setServicePlan(m1.group(7));
					entry.setaBank(m1.group(8));
					entry.setSlap(m1.group(9));
					entry.setPnode(m1.group(10));
					
					if (m1.group(11)!=null && m1.group(11).trim().length()!=0)
						entry.setRateEffectiveDate(format.parse(m1.group(11)));
					
					entry.setDbpParticipant(isParticipation(m1.group(12)));
					entry.setSdpParticipant(isParticipation(m1.group(13)));
					entry.setCppParticipant(isParticipation(m1.group(14)));
					entry.setBipParticipant(isParticipation(m1.group(15)));
					
					
					entry.setApiParticipant(isParticipation(m1.group(16)));
					entry.setSlrpParticipant(isParticipation(m1.group(17)));
					
					entry.setDrcParticipant(isParticipation(m1.group(18)));
					if (entry.isDrcParticipant())
						entry.setDrcProgram(m1.group(18));
					
					entry.setAutoDRParticipant(isParticipation(m1.group(19)));
					
					entry.setRtpParticipant(isParticipation(m1.group(20)));
					if (m1.group(21)!=null && !m1.group(21).isEmpty())
						entry.setRtpVoltag(Double.valueOf(m1.group(21)));
					
					entry.setDirectAccessParticipant(isParticipation(m1.group(22)));
	
					entry.setCbpParticipant(isParticipation(m1.group(23)));
					
					//entry.setBcdRepName(m1.group(24));
					entry.setBcdRepName(m1.group(24).replaceAll("\"", ""));
					  
					if (m1.group(26)!=null && m1.group(26).trim().length()!=0){
						entry.setAutoDrProfileStartDate(format.parse(m1.group(26).replaceAll("\"", "")));
					}else{
						entry.setAutoDrProfileStartDate(null);
					}
					entry.setSubstation(m1.group(29));
					entry.setBlockNumber(m1.group(30));
					String programOptionString = m1.group(31);
					int programOption=0;
					if(("15".equalsIgnoreCase(programOptionString)||"30".equalsIgnoreCase(programOptionString))){
						programOption=Integer.valueOf(programOptionString);
					}
					entry.setProgramOption(programOption);
					
//					if (entry.isCbpParticipant())
//						entry.setCbpProgram(m1.group(16));
					
//					System.out.println();
//		        	System.out.println("Find, group count "+m1.groupCount());
//		        	for(int i = 0;i<m1.groupCount()+1;i++){
//		        		System.out.println("G[" +i +"]="+m1.group(i));
//		        	}
					
					if (contains(entry.getServiceAccount())) {
						String desc="Duplicated service account number ("+entry.getServiceAccount()+") exists, reject row data: "+row;
						String para="DuplicatedAccountNumber";
						addMessage(desc, para);
					} else {
						partEntryList.add(entry);
						accounts.add(entry.getServiceAccount());
					}
				} catch (ParseException e) {
					String desc="Incorrect row data: "+row;
					String para="DateFormatError";
					addMessage(desc, para);
				} catch(NumberFormatException e){
					String desc="Incorrect row data: "+row;
					String para="NumberFormatError";
					addMessage(desc, para);
				}
			}
		}
		
	}
	
	private boolean contains(String account){
		for (String a:accounts) {
			if (a.compareToIgnoreCase(account)==0)
				return true;
		}
		
		return false;
	}

	public Set<SCEParticipantEntry> getSortedSet(){
		Set<SCEParticipantEntry> entrySet=new TreeSet<SCEParticipantEntry>(
				new Comparator<SCEParticipantEntry>() {
					@Override
					public int compare(SCEParticipantEntry o1, SCEParticipantEntry o2) {
						return o1.getServiceAccount().compareToIgnoreCase(o2.getServiceAccount());
					}
				});
		
		entrySet.addAll(partEntryList);
		
		return entrySet;
	}
	
	public boolean isParticipation(String participation){
		if (participation==null || participation.isEmpty())
			return false;
		if (participation.toUpperCase().equals(PARTICIPATION_NO))
			return false;
		
		return true;
	}

	private void addMessage(String description, String parameterName){
        ProgramValidationMessage error = new ProgramValidationMessage();
        error.setDescription(description);
        error.setParameterName(parameterName);
        messages.add(error);
	}

	public static void main(String[] args) {
		try {
			File file=new File("C:\\Users\\e508342\\Desktop\\721\\2.csv");
			InputStream inputStream;
				inputStream = new BufferedInputStream(new FileInputStream(file));
			BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = in.readLine()) != null) {
				sb.append(line);
				sb.append('\n');
			}

			SCEParticipantFileParser parser=new SCEParticipantFileParser(sb.toString(), "test.csv");
			
			System.out.println(parser.getFilename());
			System.out.println(parser.getMessages());
 
			for (SCEParticipantEntry entry:parser.getPartEntryList()) {
				System.out.println();
				
				System.out.println("Service Account:          "+entry.getServiceAccount());
				System.out.println("Customer Name:            "+entry.getCustomerName());
				System.out.println("Service Street Address:   "+entry.getServiceStreetAddress());
				System.out.println("Service City Name:        "+entry.getServiceCityName());
				System.out.println("Zip:                      "+entry.getZip());
				System.out.println("Abank:                    "+entry.getaBank());
				System.out.println("Slap:                     "+entry.getSlap());
				
				System.out.println("Service Plan:             "+entry.getServicePlan());
				System.out.println("Rate Effective Date:      "+entry.getRateEffectiveDate());
				System.out.println("DirectAccessParticipant:  "+entry.isDirectAccessParticipant());
				
				System.out.println("DBP Participant:          "+entry.isDbpParticipant());
				System.out.println("SDP Participant:          "+entry.isSdpParticipant());
				System.out.println("CPP Participant:          "+entry.isCppParticipant());
				System.out.println("BIP Participant:          "+entry.isBipParticipant());
				System.out.println("CBP Participant:          "+entry.isCbpParticipant());
				System.out.println("API Participant:          "+entry.isApiParticipant());
				System.out.println("SLRP Participant:         "+entry.isSlrpParticipant());
				System.out.println("DRC Participant:          "+entry.isDrcParticipant());
				System.out.println("DRC Program:              "+entry.getDrcProgram());
				
				System.out.println("ADR Program:              "+entry.isAutoDRParticipant());
				System.out.println("RTP Program:              "+entry.isRtpParticipant());
				
				System.out.println("Utitity Programs:         "+entry.getUtilityPrograms());
				System.out.println("Programs option:         "+entry.getProgramOption());
				System.out.println("Substation:         "+entry.getSubstation());
				System.out.println("Block Number:         "+entry.getBlockNumber());
			}
			
			
//			System.out.println(parser.getStartTime());
//			System.out.println(parser.getEndTime());
//			System.out.println(System.currentTimeMillis()-now);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public List<SCEParticipantEntry> getPartEntryList() {
		return partEntryList;
	}
	public void setPartEntryList(List<SCEParticipantEntry> partEntryList) {
		this.partEntryList = partEntryList;
	}
	public List<ProgramValidationMessage> getMessages() {
		return messages;
	}
	public void setMessages(List<ProgramValidationMessage> messages) {
		this.messages = messages;
	}
	public List<String> getAccounts() {
		return accounts;
	}
	public void setAccounts(List<String> accounts) {
		this.accounts = accounts;
	}
}
