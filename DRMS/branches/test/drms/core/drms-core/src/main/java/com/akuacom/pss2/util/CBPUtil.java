
package com.akuacom.pss2.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.akuacom.pss2.program.Program;
import com.akuacom.pss2.program.participant.ProgramParticipant;

public class CBPUtil {
    public static final String CBP14DO="CBP 1-4 DO";
    public static final String CBP26DO="CBP 2-6 DO";
    public static final String CBP48DO="CBP 4-8 DO";
    public static final String CBP14DA="CBP 1-4 DA";
    public static final String CBP26DA="CBP 2-6 DA";
    public static final String CBP48DA="CBP 4-8 DA";
    
    private static Map<String,List<String>> cbpGroup = new HashMap<String,List<String>>();    
    /**
	 * @return the cbpGroup
	 */
	public static Map<String, List<String>> getCbpGroup() {
		if(!cbpGroup.containsKey("CBP")){
			cbpGroup.put("CBP", new ArrayList<String>());
			cbpGroup.get("CBP").add(CBP14DO);
			cbpGroup.get("CBP").add(CBP26DO);
			cbpGroup.get("CBP").add(CBP48DO);
			cbpGroup.get("CBP").add(CBP14DA);
			cbpGroup.get("CBP").add(CBP26DA);
			cbpGroup.get("CBP").add(CBP48DA);
		}
		return cbpGroup;
	}
	
	public static List<String> transferList(List<String> input){
		List<String> result = new ArrayList<String>();
		for(String key:input){
			if(CBPUtil.getCbpGroup().get("CBP").contains(key)){
            	if(!result.contains("CBP")){
            		result.add("CBP");
            	}
            }else{
            	result.add(key);
            }
		}
		return result;
	}
	public static List<String> revertList(List<String> input){
		List<String> result = new ArrayList<String>();
		for(String key:input){
			if("CBP".equalsIgnoreCase(key)){
				if(!result.contains(CBP14DO)){
            		result.add(CBP14DO);
            		result.add(CBP26DO);
            		result.add(CBP48DO);
            		result.add(CBP14DA);
            		result.add(CBP26DA);
            		result.add(CBP48DA);
            	}
			}else{
            	result.add(key);
            }
		}
		return result;
	}
	public static ProgramParticipant getFirstCBPProgramParticipant(List<ProgramParticipant> input){
		for(ProgramParticipant instance:input){
			String key = instance.getProgramName();
			if(CBPUtil.getCbpGroup().get("CBP").contains(key)){
            	return instance;
            }
		}
		return null;
	}
	public static List<ProgramParticipant> transferProgramParticipant(List<ProgramParticipant> input){
		List<ProgramParticipant> result = new ArrayList<ProgramParticipant>();
		for(ProgramParticipant instance:input){
			String key = instance.getProgramName();
			if(CBPUtil.getCbpGroup().get("CBP").contains(key)){
            	if(!containsProgramParticipant(result,"CBP")){
            		instance.setProgramName("CBP");
            		result.add(instance);
            	}
            }else{
            	result.add(instance);
            }
		}
		return result;
	}
	public static List<ProgramParticipant> transferProgramParticipantWithParticipant(List<ProgramParticipant> input){
		List<ProgramParticipant> result = new ArrayList<ProgramParticipant>();
		for(ProgramParticipant instance:input){
			String key = instance.getProgramName();
			String participantName = instance.getParticipantName();
			if(CBPUtil.getCbpGroup().get("CBP").contains(key)){
            	if(!containsProgramParticipantWithParticipant(result,"CBP",participantName)){
            		instance.setProgramName("CBP");
            		result.add(instance);
            	}
            }else{
            	result.add(instance);
            }
		}
		return result;
	}
	public static List<ProgramParticipant> revertProgramParticipant(List<ProgramParticipant> inputInView,List<ProgramParticipant> inputInDB){
		List<ProgramParticipant> result = new ArrayList<ProgramParticipant>();
		if(containsProgramParticipant(inputInView,"CBP")){
			ProgramParticipant pp = getCBPProgramParticipant(inputInView);
			for(ProgramParticipant instance:inputInDB){
				String key = instance.getProgramName();
				if(CBPUtil.getCbpGroup().get("CBP").contains(key)){
					pp.setProgramName(key);
					result.add(pp);
				}else{
					result.add(instance);
				}
			}
				
		}
		
		return result;
	}
	
	public static boolean containsProgramParticipant(List<ProgramParticipant> input,String programName){
		boolean result = false;
		for(ProgramParticipant instance:input){
			if(instance.getProgramName().equalsIgnoreCase(programName)){
				result = true;
				return result;
			}
		}
		return result;
	}
	public static boolean containsProgramParticipantWithParticipant(List<ProgramParticipant> input,String programName,String participantName){
		boolean result = false;
		for(ProgramParticipant instance:input){
			String pName = instance.getParticipantName();
			if(instance.getProgramName().equalsIgnoreCase(programName)&&pName.equalsIgnoreCase(participantName)){
				result = true;
				return result;
			}
		}
		return result;
	}
	public static ProgramParticipant getCBPProgramParticipant(List<ProgramParticipant> input){
		for(ProgramParticipant instance:input){
			if(instance.getProgramName().equalsIgnoreCase("CBP")){
				return instance;
			}
		}
		return null;
	}
	public static boolean isCBPConsolidationProgram(String programName){
		if("CBP".equalsIgnoreCase(programName)){
			return true;
		}else{
			if(CBPUtil.getCbpGroup().get("CBP").contains(programName)){
				return true;
			}else{
				return false;
			}
		}
	}

	/**
	 * @param totalParticipantListWithoutConsolidation
	 * @param participantName
	 * @return
	 */
	public static List<ProgramParticipant> revertProgramParticipantWithParticipant(
			List<ProgramParticipant> totalParticipantListWithoutConsolidation,
			String participantName) {
		List<ProgramParticipant> result = new ArrayList<ProgramParticipant>();
		for(ProgramParticipant instance:totalParticipantListWithoutConsolidation){
			String pName = instance.getParticipantName();
			String programName = instance.getProgramName();
			if(pName.equalsIgnoreCase(participantName)){
				if(CBPUtil.getCbpGroup().get("CBP").contains(programName)||"CBP".equalsIgnoreCase(programName)){
					result.add(instance);	
				}
			}
		}
		return result;
	}
	public static ProgramParticipant getProgramParticipant(List<ProgramParticipant> list,ProgramParticipant input){
		for(ProgramParticipant instance:list){
			if(instance.getUUID().equalsIgnoreCase(input.getUUID())){
				return instance;
			}
		}
		return null;
	}
	
	public static boolean isEnableCBPConsolidation(List<Program> list){
		boolean result = false;
		for(Program program:list){
			String programName = program.getProgramName();
			String programGroup = program.getProgramGroup();
			if(CBPUtil.getCbpGroup().get("CBP").contains(programName)){
				if("CBP".equalsIgnoreCase(programGroup)){
					result = true;
					cbpConsolidationFlag = true;
					return result;
				}
			}
		}
		cbpConsolidationFlag = false;
		return result;
	}
	private static Boolean cbpConsolidationFlag = null;
	public static Boolean isEnableCBPConsolidation(){
		return cbpConsolidationFlag;
	}
}
