package com.akuacom.pss2.facdash;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.akuacom.ejb.client.EJB3Factory;
import com.akuacom.pss2.program.Program;
import com.akuacom.pss2.program.ProgramManager;
import com.akuacom.pss2.program.participant.ProgramParticipant;
import com.akuacom.pss2.system.SystemManager;


public class CBPUtil {
    public static final String CBP14DO="CBP 1-4 DO";
    public static final String CBP26DO="CBP 2-6 DO";
    public static final String CBP48DO="CBP 4-8 DO";
    public static final String CBP14DA="CBP 1-4 DA";
    public static final String CBP26DA="CBP 2-6 DA";
    public static final String CBP48DA="CBP 4-8 DA";
    
    private static Map<String,List<String>> cbpGroup = new HashMap<String,List<String>>();   
    
	public static String convertProgramDispalyName(String in) {
		if(CBP14DO.equalsIgnoreCase(in)||CBP26DO.equalsIgnoreCase(in)||CBP48DO.equalsIgnoreCase(in)
				||CBP14DA.equalsIgnoreCase(in)||CBP26DA.equalsIgnoreCase(in)||CBP48DA.equalsIgnoreCase(in)){
			return "CBP";
		}
		return in;
	}
	
	public static boolean equalsProgramName(String in1, String in2){
		if(in1.equalsIgnoreCase(in2)) return true;
		
		if(CBP14DO.equalsIgnoreCase(in1)||CBP26DO.equalsIgnoreCase(in1)||CBP48DO.equalsIgnoreCase(in1)
				||CBP14DA.equalsIgnoreCase(in1)||CBP26DA.equalsIgnoreCase(in1)||CBP48DA.equalsIgnoreCase(in1)){
			if(CBP14DO.equalsIgnoreCase(in2)||CBP26DO.equalsIgnoreCase(in2)||CBP48DO.equalsIgnoreCase(in2)
					||CBP14DA.equalsIgnoreCase(in2)||CBP26DA.equalsIgnoreCase(in2)||CBP48DA.equalsIgnoreCase(in2)){
				return true;
			}
		}
		
		return false;
	}
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
	public static JSFClientProgram getCBPJSFClientProgram(List<JSFClientProgram> input){
		for(JSFClientProgram instance:input){
			if(instance.getConsolidationProgramName().equalsIgnoreCase("CBP")){
				return instance;
			}
		}
		return null;
	}

	public static List<JSFClientProgram> revertList(List<JSFClientProgram> inputAfterConsolidation,List<JSFClientProgram> inputBeforeConsolidation){
		if(!isEnableCBPConsolidation()){
			return inputAfterConsolidation;
		}
		List<JSFClientProgram> result = new ArrayList<JSFClientProgram>();
		if(containsJSFClientProgram(inputAfterConsolidation,"CBP")){
			JSFClientProgram pp = getCBPJSFClientProgram(inputAfterConsolidation);
			for(JSFClientProgram instance:inputBeforeConsolidation){
				String key = instance.getProgramName();
				if(CBPUtil.getCbpGroup().get("CBP").contains(key)){
					instance.setParticipating(pp.isParticipating());
					result.add(instance);
				}else{
					result.add(instance);
				}
			}
		}else{
			return inputAfterConsolidation;
		}
		return result;
	}
	public static List<JSFClientProgram> transferList(List<JSFClientProgram> input){
		if(!isEnableCBPConsolidation()){
			return input;
		}
		List<JSFClientProgram> result = new ArrayList<JSFClientProgram>();
		List<JSFClientProgram> relativedClientParticipant = new ArrayList<JSFClientProgram>();
		for(JSFClientProgram instance:input){
			String key = instance.getProgramName();
			if(CBPUtil.getCbpGroup().get("CBP").contains(key)){
				if(!containsJSFClientProgram(result,key)){
            		result.add(instance);
            	}
				relativedClientParticipant.add(instance);
            }else{
            	result.add(instance);
            }
		}
		for(JSFClientProgram instance:input){
			instance.setRelativedClientParticipant(relativedClientParticipant);
		}
		return result;
	}
	public static boolean containsJSFClientProgram(List<JSFClientProgram> input,String programName){
		boolean result = false;
		for(JSFClientProgram instance:input){
			String pName = instance.getProgramName();
			if(CBPUtil.getCbpGroup().get("CBP").contains(pName)){
				result = true;
				return result;
			}
		}
		return result;
	}
	private static ProgramManager pm;

	public static ProgramManager getPm() {
		if(pm==null){
			pm = EJB3Factory.getBean(ProgramManager.class);
		}
		return pm;
	}
	static Boolean consolidationFlag = null;
	public static boolean isEnableCBPConsolidation(){
		if(consolidationFlag!=null){
			return consolidationFlag;
		}
		boolean result = false;
		List<Program> list = getPm().getAllPrograms();
		for(Program program:list){
			String programName = program.getProgramName();
			String programGroup = program.getProgramGroup();
			if(CBPUtil.getCbpGroup().get("CBP").contains(programName)){
				if("CBP".equalsIgnoreCase(programGroup)){
					result = true;
					consolidationFlag = true;
					return result;
				}
			}
		}
		consolidationFlag = false;
		return result;
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
	
	public static List<JSFParticipantProgram> getNonConsolidationPrograms(List<JSFParticipantProgram> inputList){
		List<JSFParticipantProgram> result =new ArrayList<JSFParticipantProgram>();
		for(JSFParticipantProgram instance:inputList){
			String key = instance.getProgramName();
			if(CBPUtil.getCbpGroup().get("CBP").contains(key)){
				result.add(instance);
			}
		}
		return result;
	}
	
	public static List<JSFParticipantProgram> transferJSFParticipantProgram(List<JSFParticipantProgram> input){
		if(!isEnableCBPConsolidation()){
			return input;
		}
		List<JSFParticipantProgram> result = new ArrayList<JSFParticipantProgram>();
		List<JSFParticipantProgram> relativedClientParticipant = new ArrayList<JSFParticipantProgram>();
		for(JSFParticipantProgram instance:input){
			String key = instance.getProgramName();
			if(CBPUtil.getCbpGroup().get("CBP").contains(key)){
				if(!containsJSFParticipantProgram(result,key)){
            		result.add(instance);
            	}
				relativedClientParticipant.add(instance);
            }else{
            	result.add(instance);
            }
		}
		for(JSFParticipantProgram instance:input){
			instance.setRelativedClientParticipant(relativedClientParticipant);
		}
		return result;
	}
	public static boolean containsJSFParticipantProgram(List<JSFParticipantProgram> input,String programName){
		boolean result = false;
		for(JSFParticipantProgram instance:input){
			String pName = instance.getProgramName();
			if(CBPUtil.getCbpGroup().get("CBP").contains(pName)){
				result = true;
				return result;
			}
		}
		return result;
	}
	public static List<AggregationTreeNode> transferAggregationTreeNodeList(List<AggregationTreeNode> input){
		if(!isEnableCBPConsolidation()){
			return input;
		}
		List<AggregationTreeNode> result = new ArrayList<AggregationTreeNode>();
		for(AggregationTreeNode instance:input){
			String programName = instance.getProgramName();
			String participantName = instance.getParticipantName();
			if(CBPUtil.getCbpGroup().get("CBP").contains(programName)){
				if(!containsAggregationTreeNode(result,programName,participantName)){
            		result.add(instance);
            	}
            }else{
            	result.add(instance);
            }
		}
		return result;
	}
	public static boolean containsAggregationTreeNode(List<AggregationTreeNode> input,String programName,String participantName){
		boolean result = false;
		for(AggregationTreeNode instance:input){
			String pName = instance.getProgramName();
			String paName = instance.getParticipantName();
			if(CBPUtil.getCbpGroup().get("CBP").contains(pName)||"CBP".equalsIgnoreCase(pName)){
				if(paName.equalsIgnoreCase(participantName)){
					result = true;
					return result;	
				}
			}
		}
		return result;
	}
}
