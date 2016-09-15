package com.akuacom.pss2.facdash;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.participant.ParticipantManager;
import com.akuacom.pss2.participant.ParticipantRtpStrategy;
import com.akuacom.pss2.program.ProgramManager;
import com.akuacom.pss2.program.dlc.DlcManager;
import com.akuacom.pss2.program.participant.ProgramParticipant;
import com.akuacom.pss2.program.participant.ProgramParticipantManager;
import com.akuacom.pss2.program.scertp.RTPConfig;
import com.akuacom.pss2.program.scertp.RTPShedStrategyEntry;
import com.akuacom.pss2.program.scertp2013.SCERTPProgramManager2013;

public class SceRtp2013BackingBean implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private ProgramParticipant clientParticipant;
	
	private List<Participant> descendantClients;
	
	private List<RTPShedStrategyEntry> rtpStrategies;
	
	/** The time block string. */
	private String timeBlockString;
	
	private String[] temperatureCategories = new String[9];
	
	public SceRtp2013BackingBean(ProgramParticipant clientParticipant){
		this.clientParticipant = clientParticipant;
		
		initStrategy(clientParticipant);
	}


	private void initStrategy(ProgramParticipant clientParticipant) {
		SCERTPProgramManager2013 programManager = EJBFactory.getBean(SCERTPProgramManager2013.class);
		List<RTPConfig> categories = programManager.findRtpCategoryByProgramName(clientParticipant.getProgramName());
		for(int i=0;i<categories.size();i++){
			temperatureCategories[i] = String.valueOf(categories.get(i).getStartTemperature());
		}
		
		Set<ParticipantRtpStrategy> entries =  clientParticipant.getRtpStrateges();
		String[] header = {"EXTREMELY HOT SUMMER WEEKDAY", "VERY HOT SUMMER WEEKDAY","HOT SUMMER WEEKDAY",
							"MODERATE SUMMER WEEKDAY","MILD SUMMER WEEKDAY","HIGH COST WINTER WEEKDAY",
							"LOW COST WINTER WEEKDAY","HIGH COST WEEKEND","LOW COST WEEKEND"};
						
        String[][] matrix = new String[9][25];
        for(ParticipantRtpStrategy strategy : entries){
        	int index = indexOf(header, strategy.getName());
        	
        	matrix[index][24] = strategy.getName();
        	matrix[index][0] = strategy.getValue0();
        	matrix[index][1] = strategy.getValue1();
        	matrix[index][2] = strategy.getValue2();
        	matrix[index][3] = strategy.getValue3();
        	matrix[index][4] = strategy.getValue4();
        	matrix[index][5] = strategy.getValue5();
        	matrix[index][6] = strategy.getValue6();
        	matrix[index][7] = strategy.getValue7();
        	matrix[index][8] = strategy.getValue8();
        	matrix[index][9] = strategy.getValue9();
        	matrix[index][10] = strategy.getValue10();
        	matrix[index][11] = strategy.getValue11();
        	matrix[index][12] = strategy.getValue12();
        	matrix[index][13] = strategy.getValue13();
        	matrix[index][14] = strategy.getValue14();
        	matrix[index][15] = strategy.getValue15();
        	matrix[index][16] = strategy.getValue16();
        	matrix[index][17] = strategy.getValue17();
        	matrix[index][18] = strategy.getValue18();
        	matrix[index][19] = strategy.getValue19();
        	matrix[index][20] = strategy.getValue20();
        	matrix[index][21] = strategy.getValue21();
        	matrix[index][22] = strategy.getValue22();
        	matrix[index][23] = strategy.getValue23();
        	
        }
        
        rtpStrategies = new ArrayList<RTPShedStrategyEntry>();
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                    "H:mm.ss.SSS");
        for (int i = 0; i < 24; i++) {
        	RTPShedStrategyEntry entry = new RTPShedStrategyEntry();
            try {
				entry.setStartTime(simpleDateFormat.parse(i
				        + ":00.0.000"));
			} catch (ParseException e) {
				e.printStackTrace();
			}
            try {
				entry.setEndTime(simpleDateFormat.parse(i
				        + ":59.59.999"));
			} catch (ParseException e) {
				e.printStackTrace();
			}
            
            entry.setValue0(matrix[0][i]);
            entry.setValue1(matrix[1][i]);
            entry.setValue2(matrix[2][i]);
            entry.setValue3(matrix[3][i]);
            entry.setValue4(matrix[4][i]);
            entry.setValue5(matrix[5][i]);
            entry.setValue6(matrix[6][i]);
            entry.setValue7(matrix[7][i]);
            entry.setValue8(matrix[8][i]);
            
            rtpStrategies.add(entry);
        }
	}

	
	private static int indexOf (String[] array, String value) {
		if(array==null||array.length==0||value==null) return -1;
		
		for(int i=0; i<array.length; i++){
			if(value.equalsIgnoreCase(array[i])){
				return i;
			}
		}
		
		return -1;
	}

	

	public List<RTPShedStrategyEntry> getRtpStrategies() {
		return rtpStrategies;
	}

	public void setRtpStrategies(List<RTPShedStrategyEntry> rtpStrategies) {
		this.rtpStrategies = rtpStrategies;
	}
	
	
	public String saveRtpShedStrategiesAction()
	{
		 String[][] matrix = new String[9][25];
		//need to be sorted?
		int index = 0; 
		for(RTPShedStrategyEntry entry : rtpStrategies){
        	matrix[0][index] = entry.getValue0();
        	matrix[1][index] = entry.getValue1();
        	matrix[2][index] = entry.getValue2();
        	matrix[3][index] = entry.getValue3();
        	matrix[4][index] = entry.getValue4();
        	matrix[5][index] = entry.getValue5();
        	matrix[6][index] = entry.getValue6();
        	matrix[7][index] = entry.getValue7();
        	matrix[8][index] = entry.getValue8();
	        	
        	index++;
		}

		Set<ParticipantRtpStrategy> entries =  clientParticipant.getRtpStrateges();
		String[] header = {"EXTREMELY HOT SUMMER WEEKDAY", "VERY HOT SUMMER WEEKDAY","HOT SUMMER WEEKDAY",
							"MODERATE SUMMER WEEKDAY","MILD SUMMER WEEKDAY","HIGH COST WINTER WEEKDAY",
							"LOW COST WINTER WEEKDAY","HIGH COST WEEKEND","LOW COST WEEKEND"};
		
		for(ParticipantRtpStrategy entry : entries){
			int indexHeader = indexOf(header, entry.getName());
			entry.setValue0(matrix[indexHeader][0]);
			entry.setValue1(matrix[indexHeader][1]);
			entry.setValue2(matrix[indexHeader][2]);
			entry.setValue3(matrix[indexHeader][3]);
			entry.setValue4(matrix[indexHeader][4]);
			entry.setValue5(matrix[indexHeader][5]);
			
			entry.setValue6(matrix[indexHeader][6]);
			entry.setValue7(matrix[indexHeader][7]);
			entry.setValue8(matrix[indexHeader][8]);
			entry.setValue9(matrix[indexHeader][9]);
			entry.setValue10(matrix[indexHeader][10]);
			
			entry.setValue11(matrix[indexHeader][11]);
			entry.setValue12(matrix[indexHeader][12]);
			entry.setValue13(matrix[indexHeader][13]);
			entry.setValue14(matrix[indexHeader][14]);
			entry.setValue15(matrix[indexHeader][15]);
			
			entry.setValue16(matrix[indexHeader][16]);
			entry.setValue17(matrix[indexHeader][17]);
			entry.setValue18(matrix[indexHeader][18]);
			entry.setValue19(matrix[indexHeader][19]);
			entry.setValue20(matrix[indexHeader][20]);
			
			entry.setValue21(matrix[indexHeader][21]);
			entry.setValue22(matrix[indexHeader][22]);
			entry.setValue23(matrix[indexHeader][23]);
			
		}
		
		 ParticipantManager participantManager = EJBFactory
		            .getBean(ParticipantManager.class);
		 
		 DlcManager manager =  EJBFactory
		            .getBean(DlcManager.class);
		 Participant client = participantManager.getParticipant(clientParticipant.getParticipant().getParticipantName(), clientParticipant.getParticipant().isClient());
		 
		 ProgramParticipantManager programParticipantManager = EJBFactory
	                .getBean(ProgramParticipantManager.class);
		 ProgramParticipant pp = programParticipantManager
                 .getClientProgramParticipants(clientParticipant.getProgramName(), clientParticipant.getParticipantName(), true);
		 clientParticipant = pp;
		 
		 clientParticipant.setRtpStrateges(entries);
		 
		 manager.updateProgramParticipant(clientParticipant);
		 // cascade update
		 ProgramManager programManager =  EJBFactory
		            .getBean(ProgramManager.class);
		 List<Participant> ps = this.getDescendantClients();
		 List<String> names = new ArrayList<String>();
		 for(Participant p : ps){
			 if(p.getParticipantName().equals(clientParticipant.getParticipantName())) continue;
			 names.add(p.getParticipantName());
		 }
		 List<ProgramParticipant> pps = programManager.findProgramClientByProgramAndParticipant(clientParticipant.getProgramName(), names);
		 
		 for(ProgramParticipant cp: pps){
			 entries =  cp.getRtpStrateges();
			 for(ParticipantRtpStrategy entry : entries){
					int indexHeader = indexOf(header, entry.getName());
					entry.setValue0(matrix[indexHeader][0]);
					entry.setValue1(matrix[indexHeader][1]);
					entry.setValue2(matrix[indexHeader][2]);
					entry.setValue3(matrix[indexHeader][3]);
					entry.setValue4(matrix[indexHeader][4]);
					entry.setValue5(matrix[indexHeader][5]);
					
					entry.setValue6(matrix[indexHeader][6]);
					entry.setValue7(matrix[indexHeader][7]);
					entry.setValue8(matrix[indexHeader][8]);
					entry.setValue9(matrix[indexHeader][9]);
					entry.setValue10(matrix[indexHeader][10]);
					
					entry.setValue11(matrix[indexHeader][11]);
					entry.setValue12(matrix[indexHeader][12]);
					entry.setValue13(matrix[indexHeader][13]);
					entry.setValue14(matrix[indexHeader][14]);
					entry.setValue15(matrix[indexHeader][15]);
					
					entry.setValue16(matrix[indexHeader][16]);
					entry.setValue17(matrix[indexHeader][17]);
					entry.setValue18(matrix[indexHeader][18]);
					entry.setValue19(matrix[indexHeader][19]);
					entry.setValue20(matrix[indexHeader][20]);
					
					entry.setValue21(matrix[indexHeader][21]);
					entry.setValue22(matrix[indexHeader][22]);
					entry.setValue23(matrix[indexHeader][23]);
					
				}
			 cp.setRtpStrateges(entries);
			 manager.updateProgramParticipant(cp);
		 }
       
		
		tempEntry=null;
		timeBlockString=null;
		dispalyModel="View";
		
		for(RTPShedStrategyEntry entry: rtpStrategies)
		{
			entry.setSelected(Boolean.FALSE);
				
		}
		
		return "done";
	}
	
	
	public String cancelRtpShedStrategiesAction()
	{
		setDispalyModel(null);
		initStrategy(clientParticipant);
		return "done";
	}
	
	
	//**************
	/**
	 * Gets the available types.
	 * 
	 * @return the available types
	 */
	public List<SelectItem> getAvailableTypes()
	{
		List<SelectItem> types = new ArrayList<SelectItem>();
		types.add(new SelectItem("View"));
		types.add(new SelectItem("Edit"));
		
		return types;
	}
	
	/**
	 * Type listener.
	 * 
	 * @param e the e
	 */
	public void typeListener(ActionEvent e)
	{
		initStrategy(clientParticipant);
	}
	private Boolean viewModel;
	
	public Boolean getViewModel() {
		return !"Edit".equals(dispalyModel);
	}

	private String dispalyModel;

	public String getDispalyModel() {
		return dispalyModel==null?"View":dispalyModel;
	}

	public void setDispalyModel(String dispalyModel) {
		this.dispalyModel = dispalyModel;
	}

	public String[] getTemperatureCategories() {
		return temperatureCategories;
	}

	public void setTemperatureCategories(String[] temperatureCategories) {
		this.temperatureCategories = temperatureCategories;
	}
	
	public String getProgramClassName() {
		return this.clientParticipant.getProgram().getClassName();
	}
	
	
	/**
	 * Gets the time block string.
	 * 
	 * @return the time block string
	 */
	public String getTimeBlockString()
	{
		return timeBlockString;
	}

	/**
	 * Sets the time block string.
	 * 
	 * @param timeBlockString the new time block string
	 */
	public void setTimeBlockString(String timeBlockString)
	{
		this.timeBlockString = timeBlockString;
	}
	
	private RTPShedStrategyEntry tempEntry;
	public String copyEntryAction()
	{
		for(RTPShedStrategyEntry entry: rtpStrategies)
		{
			if(entry.getTimeBockString().equals(timeBlockString))
			{
				tempEntry = entry;
				entry.setSelected(Boolean.TRUE);
			
			}else{
				entry.setSelected(Boolean.FALSE);
			}
		}
		return null;
	}
	
	/**
	 * Paste entry action.
	 * 
	 * @return the string
	 */
	public String pasteEntryAction()
	{
		if(tempEntry==null){
			return null;
			//TODO: an alert message is need
		}
		for(RTPShedStrategyEntry entry: rtpStrategies)
		{
			if(entry.getTimeBockString().equals(timeBlockString))
			{				
				entry.setValue0(tempEntry.getValue0());
				entry.setValue1(tempEntry.getValue1());
				entry.setValue2(tempEntry.getValue2());
				entry.setValue3(tempEntry.getValue3());
				entry.setValue4(tempEntry.getValue4());
				entry.setValue5(tempEntry.getValue5());
				entry.setValue6(tempEntry.getValue6());
				entry.setValue7(tempEntry.getValue7());
				entry.setValue8(tempEntry.getValue8());
				break;
			}
		}
		
		return null;
	}
	
	public List<Participant> getDescendantClients() {
		return descendantClients;
	}


	public void setDescendantClients(List<Participant> descendantClients) {
		this.descendantClients = descendantClients;
	}


}
