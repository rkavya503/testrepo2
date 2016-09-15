package com.akuacom.pss2.program.dbp;

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
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.akuacom.pss2.core.ProgramValidationException;
import com.akuacom.pss2.core.ProgramValidationMessage;
import com.akuacom.pss2.program.dbp.SCEDBPBidEntry.BidBlock;

/**
 * the class SCEDBPEventFileParser
 * 
 */
public class SCEDBPEventFileParser {
	
	private static final String DATE_TIME_FORMAT="yyyy-MM-dd HH:mm:ss";
	private static final long MSEC_IN_HOUR=60*60*1000;
	
	private String filename;
	private Date startTime;
	private Date endTime;
	
	List<SCEDBPBidEntry> bidEntryList=new ArrayList<SCEDBPBidEntry>();
	
	List<ProgramValidationMessage> messages=new ArrayList<ProgramValidationMessage>();
	
	public SCEDBPEventFileParser(String fileString, String filename) throws ProgramValidationException{
		this.filename=filename;
		parse(fileString);
		validation();
	}
	
	public void validation() throws ProgramValidationException{
		if (startTime==null || endTime==null || this.bidEntryList==null || this.bidEntryList.size()==0) {
			String message="No available information in the DBP bid file " + filename;
			ProgramValidationException exception=new ProgramValidationException();
			List<ProgramValidationMessage> errors=new ArrayList<ProgramValidationMessage>();
			errors.add(new ProgramValidationMessage("EventCreation", message));
			errors.addAll(messages);
			exception.setErrors(errors);
			throw exception;
		}
	}
	
	private void addMessage(String description, String parameterName){
        ProgramValidationMessage error = new ProgramValidationMessage();
        error.setDescription(description);
        error.setParameterName(parameterName);
        messages.add(error);
	}
	
	protected void parse(String fileString) {
		StringBuilder regx=new StringBuilder();
		regx.append("([^,]*),");
		regx.append("([^,]*),");  //service account number
		regx.append("([^,]*),");  
		regx.append("([^,]*),");  
		regx.append("([^,]*),");  
		regx.append("([^,]*),");  // event ID
		regx.append("\\s*(\\d{4}-\\d{1,2}-\\d{1,2})\\s*,");  // event date
		regx.append("([^,]*),");  
		regx.append("\\s*(\\d{1,2}[.:]\\d{2}[.:]\\d{2})\\s*,");  // bid start time
		regx.append("\\s*(\\d{1,2}[.:]\\d{2}[.:]\\d{2})\\s*,");  // bid end time
		regx.append("\\s*([^,\\s]*)\\s*,");  // bid quantity
		regx.append("(.*)");  
		
		String row=null;
		SCEDBPBidEntry bidEntry=null;
		
		String eventDate=null;
		String startTime=null;
		String endTime=null;
		String servAcctNum=null;
		Double bidQty=null;
    	SimpleDateFormat format=new SimpleDateFormat(DATE_TIME_FORMAT);
		
		Pattern p2 = Pattern.compile("(^.*$)",Pattern.MULTILINE);
   		Matcher m2 = p2.matcher(fileString);
		while(m2.find()){
			row=m2.group(0);
			
			Pattern p3 = Pattern.compile(regx.toString());
			Matcher m3 = p3.matcher(row);
			
			if(m3.find()) {
				if (eventDate==null)
					eventDate=m3.group(7);
				
				startTime=m3.group(9).replace(".", ":");
				endTime=m3.group(10).replace(".", ":");
				servAcctNum=m3.group(2);
				if (servAcctNum!=null)
					servAcctNum=servAcctNum.trim();
				
				bidQty=Double.valueOf(m3.group(11)==null?"0":m3.group(11));
				
	        	try {
					Date bidStartTime=format.parse(eventDate+" "+startTime);
					Date bidEndTime=format.parse(eventDate+" "+endTime);
					long bidPeriod=bidEndTime.getTime()-bidStartTime.getTime();
					
					if (bidPeriod != MSEC_IN_HOUR)
						continue;
    				
    				if (bidEntry==null ||
    						!bidEntry.getServiceAccountNumber().equals(servAcctNum)) {
    					if (bidEntry !=null) {
    						if (validateBidBlock(bidEntry)) {
    							bidEntryList.add(bidEntry);
    							
    		    				if (this.startTime==null)
    		    					this.startTime=bidEntry.getBidBlocks().get(0).getBidStartTime();
    		    				
        						if (this.endTime==null)
        							this.endTime=bidEntry.getBidBlocks().get(bidEntry.getBidBlocks().size()-1).getBidEndTime();
    						}
    					}
    					
    					bidEntry=new SCEDBPBidEntry();
    					bidEntry.setServiceAccountNumber(servAcctNum);
    				}
    				
    				BidBlock block=bidEntry.new BidBlock();
    				block.setBidStartTime(bidStartTime);
    				block.setBidEndTime(bidEndTime);
    				block.setStartTime(startTime);
    				block.setEndTime(endTime);
    				block.setBidQuantity(bidQty);
    				
    				bidEntry.getBidBlocks().add(block);
				} catch (ParseException e) {
					String message="Failed to parse the event date & time.";
					addMessage(message, row);
				} catch (NumberFormatException e){
					String message="Bid quantity is not a correct number.";
					addMessage(message, row);
				} 
			} 
		}
		if (bidEntry !=null) {
			if (validateBidBlock(bidEntry)) {
				bidEntryList.add(bidEntry);
				if (this.startTime==null)
					this.startTime=bidEntry.getBidBlocks().get(0).getBidStartTime();
				
				if (this.endTime==null)
					this.endTime=bidEntry.getBidBlocks().get(bidEntry.getBidBlocks().size()-1).getBidEndTime();
			}
		}
	}
	
	protected boolean validateBidBlock(SCEDBPBidEntry entry){
		boolean correct=true;
		
		List<SCEDBPBidEntry.BidBlock> blocks=entry.getBidBlocks();
		double totalQtyPerAcct=0;
    	for (int i=0; i<blocks.size(); i++) {
			totalQtyPerAcct=totalQtyPerAcct+blocks.get(i).getBidQuantity();
    		
            // validate the bid blocks are contiguous
    		if ((i<blocks.size()-1) 
    				&& blocks.get(i).getBidEndTime().compareTo(blocks.get(i+1).getBidStartTime())!=0) {
                StringBuffer desc = new StringBuffer();
                desc.append("Bid blocks ");
                desc.append(blocks.get(i).getStartTime());
                desc.append(" - ");
                desc.append(blocks.get(i).getEndTime());
                desc.append(" and ");
                desc.append(blocks.get(i+1).getStartTime());
                desc.append(" - ");
                desc.append(blocks.get(i+1).getEndTime());
                desc.append(" for account number ");
                desc.append(entry.getServiceAccountNumber());
                desc.append("  are not contiguous");
                addMessage(desc.toString(), "BidBlockError");
                
                correct=false;
                continue;
    		}
    	}

		if (totalQtyPerAcct==0.0) {
			// validate zero bid
			String message="zero bid for account number " + entry.getServiceAccountNumber();
			addMessage(message, "AccountNumberError");
			correct=false;
		}
		
		return correct;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			File file=new File("D:\\ProjectAccess\\Template\\DBP_BID_REPORT_20110718.csv");
			InputStream inputStream;
				inputStream = new BufferedInputStream(new FileInputStream(file));
			BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = in.readLine()) != null) {
				sb.append(line);
				sb.append('\n');
			}

			long now =System.currentTimeMillis();
			SCEDBPEventFileParser parser=new SCEDBPEventFileParser(sb.toString(), "test.csv");
			System.out.println(parser.getStartTime());
			System.out.println(parser.getEndTime());
			System.out.println(System.currentTimeMillis()-now);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ProgramValidationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
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
	public List<SCEDBPBidEntry> getBidEntryList() {
		return bidEntryList;
	}
	public List<ProgramValidationMessage> getMessages() {
		return messages;
	}
	public void setMessages(List<ProgramValidationMessage> messages) {
		this.messages = messages;
	}
}
