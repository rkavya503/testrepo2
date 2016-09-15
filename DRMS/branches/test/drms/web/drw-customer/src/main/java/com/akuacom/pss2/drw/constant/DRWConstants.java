
package com.akuacom.pss2.drw.constant;

public class DRWConstants {
	public static final String LOCATION_TYPE_ALL="ALL";
	public static final String LOCATION_TYPE_SLAP="SLAP";
	public static final String LOCATION_TYPE_ABANK="ABANK";
	public static final String LOCATION_TYPE_SUBSTATION="SUBSTATION";
	
	public static final String ACTION_ACTIVATED="ACTIVATED";
	public static final String ACTION_CONTINUED="CONTINUED";
	public static final String ACTION_TERMINATED="TERMINATED";
	public static final String ACTION_SCHEDULED="SCHEDULED";
	public static final String ACTION_DELETE="DELETE";
    
	public static String HISTORY_PROGRAM_DISPLAY_NAME_API="Agricultural & Pumping Interruptible Program (AP-I)";
	public static String HISTORY_PROGRAM_DISPLAY_NAME_CBP="Capacity Bidding Program (CBP)";
	public static String HISTORY_PROGRAM_DISPLAY_NAME_DBP="Demand Bidding Program (DBP)";
	public static String HISTORY_PROGRAM_DISPLAY_NAME_RTP="Real Time Pricing (RTP)";
	public static String HISTORY_PROGRAM_DISPLAY_NAME_SAI="Summer Advantage Incentive (SAI, also known as Critical Peak Pricing, CPP)";
	public static String HISTORY_PROGRAM_DISPLAY_NAME_SDP="Summer Discount Plan (SDP)";
	public static String HISTORY_PROGRAM_DISPLAY_NAME_SPD="Save Power Day (SPD)";
	public static String HISTORY_PROGRAM_DISPLAY_NAME_BIP="Base Interruptible Program (BIP)";
	public static String HISTORY_PROGRAM_DISPLAY_NAME_DRC="Demand Response Contracts";
	
	
	public static String PROGRAM_CLASS_NAME_CBP="Capacity Bidding";
	public static String PROGRAM_CLASS_NAME_CBP_TITLE="Capacity Bidding Program";
	public static String PROGRAM_CLASS_NAME_DBP="Demand Bidding";
	public static String PROGRAM_CLASS_NAME_DRC="Demand Response Contracts";
	public static String PROGRAM_CLASS_NAME_RTP="Real Time Pricing";
	public static String PROGRAM_CLASS_NAME_SDP="Summer Discount Plan";
	public static String PROGRAM_CLASS_NAME_BIP="Base Interruptiple Program";
	public static String PROGRAM_CLASS_NAME_BIP_TITLE="Base Interruptible Program (BIP)";
	public static String PROGRAM_CLASS_NAME_API="Agriculture/Pumping Interruptiple Program";
	public static String PROGRAM_CLASS_NAME_API_TITLE="Agricultural & Pumping Interruptible Program (AP-I)";
	public static String PROGRAM_CLASS_NAME_SAI="Summer Advantage Incentive";
	
	public static String PROGRAM_CLASS_NAME_SAI_TITLE="Summer Advantage Incentive(SAI, also known as Critical Peak Pricing)";
	
	public static String PROGRAM_CLASS_NAME_SDP_R="Summer Discount Plan (SDP) - Residential Air Conditioning Plan";
	public static String PROGRAM_CLASS_NAME_SDP_C="Summer Discount Plan (SDP) - Commercial";
	
	public static final String RTP_PROGRAM_NAME="RTP";
	
	public static String PROGRAM_RTP="RTP";
	public static String PROGRAM_CBP="CBP";
	public static String PROGRAM_DBP="DBP";
	public static String PROGRAM_DRC="DRC";
	public static String PROGRAM_SDP="SDP";
	public static String PROGRAM_SPD="SPD";
	public static String PROGRAM_BIP="BIP";
	public static String PROGRAM_BIP_TOU="TOU-BIP";
	public static String PROGRAM_API="API";
	public static String PROGRAM_SAI="SAI";
	public static String PROGRAM_CPP="CPP";
	public static boolean DRC_ENABLE=false; 
	
	public static String DATE_FORMATE="MM/dd/yyyy";
	

	public static String convertClassNameToProgram(String programClassNameDisplay){
		if(programClassNameDisplay.equalsIgnoreCase(HISTORY_PROGRAM_DISPLAY_NAME_CBP)){
			return PROGRAM_CBP;
		}
		if(programClassNameDisplay.equalsIgnoreCase(HISTORY_PROGRAM_DISPLAY_NAME_DBP)){
			return PROGRAM_DBP;
		}
		if(programClassNameDisplay.equalsIgnoreCase(HISTORY_PROGRAM_DISPLAY_NAME_DRC)){
			return PROGRAM_DRC;
		}
		if(programClassNameDisplay.equalsIgnoreCase(HISTORY_PROGRAM_DISPLAY_NAME_RTP)){
			return PROGRAM_RTP;
		}
		if(programClassNameDisplay.equalsIgnoreCase(HISTORY_PROGRAM_DISPLAY_NAME_SAI)){
			return PROGRAM_CPP;
		}
		if(programClassNameDisplay.equalsIgnoreCase(HISTORY_PROGRAM_DISPLAY_NAME_SDP)){
			return PROGRAM_SDP;
		}
		if(programClassNameDisplay.equalsIgnoreCase(HISTORY_PROGRAM_DISPLAY_NAME_BIP)){
			return PROGRAM_BIP;
		}
		if(programClassNameDisplay.equalsIgnoreCase(HISTORY_PROGRAM_DISPLAY_NAME_API)){
			return PROGRAM_API;
		}
		if(programClassNameDisplay.equalsIgnoreCase(HISTORY_PROGRAM_DISPLAY_NAME_SPD)){
			return PROGRAM_SPD;
		}
		return "";
	}
	
	public static String convertProgramNameToClassDisplayName(String programName){
		if(programName.equalsIgnoreCase(PROGRAM_CBP)){
			return PROGRAM_CLASS_NAME_CBP;
		}
		if(programName.equalsIgnoreCase(PROGRAM_DBP)){
			return PROGRAM_CLASS_NAME_DBP;
		}
		if(programName.equalsIgnoreCase(PROGRAM_DRC)){
			return PROGRAM_CLASS_NAME_DRC;
		}
		if(programName.equalsIgnoreCase(PROGRAM_RTP)){
			return PROGRAM_CLASS_NAME_RTP;
		}
		if(programName.equalsIgnoreCase(PROGRAM_SAI)){
			return PROGRAM_CLASS_NAME_SAI;
		}
		if(programName.equalsIgnoreCase(PROGRAM_SDP)){
			return PROGRAM_CLASS_NAME_SDP;
		}
		if(programName.equalsIgnoreCase(PROGRAM_BIP)){
			return PROGRAM_CLASS_NAME_BIP;
		}
		if(programName.equalsIgnoreCase(PROGRAM_API)){
			return PROGRAM_CLASS_NAME_API;
		}
		return "";
	}
	public static String PRODUCT_API_ABB="A";
	public static String PRODUCT_BIP_ABB="B";
	public static String PRODUCT_CBP_ABB="P";
	public static String PRODUCT_SDP_R_ABB="R";
	public static String PRODUCT_SDP_C_ABB="C";	
	public static String PRODUCT_API="AP-I";
	public static String PRODUCT_SDP_R="Residential";
	public static String PRODUCT_SDP_C="Commercial";
	public static String PRODUCT_SDP_C_B="Commercial Base";
	public static String PRODUCT_SDP_C_E="Commercial Enhanced";
	
	public static final String VIEW_LIST_URL="/dr.website/scepr-event-listview.jsf";
	
	
	public static final String NAME_DRSESSION_BEAN="drSession";
	
	public static final String EVENT_ICON_FOLDER="images/icons/";
	
	public static final String EVENT_ICON_SAI="i.gif";
	public static final String EVENT_ICON_DRC="r.gif";
	public static final String EVENT_ICON_DBP="d.gif";
	public static final String EVENT_ICON_CBP="c.gif";
	public static final String EVENT_ICON_BIP="b2013.gif";
	public static final String EVENT_ICON_API="a.gif";
	public static final String EVENT_ICON_SDPC="s_p.gif";
	public static final String EVENT_ICON_SDPR="sV2.gif";
	public static final String EVENT_ICON_SPD="SPD.png";
	
	
	public static final String SCEC="SCEC";
	public static final String SCEN="SCEN";
	public static final String SCNW="SCNW";
	public static final String SCEW="SCEW";
	public static final String SCHD="SCHD";
	public static final String SCLD="SCLD";
	public static final String BLOCK="BLOCK";
	
	public static final String PRODUCT_CBP_DO_14="CBP 1-4 DO";
	public static final String PRODUCT_CBP_DO_26="CBP 2-6 DO";
	public static final String PRODUCT_CBP_DO_48="CBP 4-8 DO";
	public static final String PRODUCT_CBP_DA_14="CBP 1-4 DA";
	public static final String PRODUCT_CBP_DA_26="CBP 2-6 DA";
	public static final String PRODUCT_CBP_DA_48="CBP 4-8 DA";
}
