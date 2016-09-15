/**
 * 
 */
package com.akuacom.pss2.richsite.event.creation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.akuacom.utils.drw.SCE_LOCATION_SLAP;

/**
 * the class ProgramConverter
 */
public class ProgramConverter {
	
	public static boolean isUploadCBPProgram(String programName) {
		if (ProgramConverter.getMapping().containsValue(programName) && programName.startsWith("CBP"))
			return true;

		return false;
	}
	
	public static Map<String, String> getMapping(){
		Map<String, String> mapping=new HashMap<String, String>();
		mapping.put("CBP-SCE DO 1-4 Hour", "CBP 1-4 DO");
		mapping.put("CBP-SCE DO 2-6 Hour", "CBP 2-6 DO");
		mapping.put("CBP-SCE DO 4-8 Hour", "CBP 4-8 DO");
		mapping.put("CBP-SCE DA 1-4 Hour", "CBP 1-4 DA");
		mapping.put("CBP-SCE DA 2-6 Hour", "CBP 2-6 DA");
		mapping.put("CBP-SCE DA 4-8 Hour", "CBP 4-8 DA");
		mapping.put("DRC-ENERDO DO 1-4 Hour", "DRC-ENODO");
		mapping.put("DRC-CEGDA DA 1-4 Hour", "DRC-CEGDA");
		mapping.put("DRC-CEGDO DO 1-4 Hour", "DRC-CEGDO");
		mapping.put("DRC-NAPPDO DO 1-4 Hour", "DRC-NAPDO");
		mapping.put("DRC-ECIDA DA 1-4 Hour", "DRC-ECIDA");
		
		return mapping;
	}
	
	// get DRAS program name from APX program & product
	public static String getProgram(String program, String product){
		return getMapping().get(program+" "+product);
	}

	public static List<String> getSLAPList() {
		List<String> slaps=new ArrayList<String>();
		
		for (SCE_LOCATION_SLAP slap:SCE_LOCATION_SLAP.values())
			slaps.add(slap.name());
		
		return slaps;
	}
}
