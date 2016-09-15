package com.akuacom.pss2.drw.jsf;

import java.io.Serializable;

import com.akuacom.pss2.drw.CFEventManager;
import com.akuacom.pss2.drw.constant.DRWTextContextConstants;
import com.akuacom.pss2.drw.core.DRWebsiteProperty;
import com.akuacom.pss2.drw.util.DRWUtil;

public class TextContextBackingBean implements Serializable {
	
	
	private static final long serialVersionUID = 3621469740178081290L;
	/** DRC copyright data */
	private String copyrightDRC;
	
	/** Table header: date of usage */
	private String tblcolDateOfUsage;
	
	/** RTP forecast subtitle */
	private String forecastSubtitleRTP;
	
	/** RTP forecast copyright */
	private String copyrightRTPForecast;
	
	/** RTP history search copyright */
	private String copyrightHistoryRTPSearch;
	
	/** RTP copyright data */
	private String copyrightRTP;

	/** SCE copyright data */
	private String copyrightSCE;

	/** Active event title copyright data */
	private String copyrightActiveEventTitle;

	/** History event title copyright data */
	private String copyrightHistoryEventTitle;

	/** Active DBP event copyright data */
	private String copyrightActiveEventDBP;

	/** History search title copyright data */
	private String copyrightHistoryEventSearchTitle;
	
	/** Event Status home page title */
	private String titleEventStatusPage;
	
	/** History page title */
	private String titleEventHistoryPage;
	
	/** RTP Active:Forecasted Applicable Temperature for  */
	private String titleEventStatusRTP;
	
	/** Contact No */
	private String contactNo;
	
	/** Table header: Program Name */
	private String tblcolProgramName;
	
	/** Table header: Affected Regions */
	private String tblcolRegions;
	
	/** Table header: Date */
	private String tblcolDate="";
	
	/** Table header: Start Time */
	private String tblcolStartTime;
	
	/** Table header: End Time */
	private String tblcolEndTime;
	
	/** Table header: Temperature */
	private String tblcolTemperature;
	
	/** Table header: Pricing Category */
	private String tblcolPricingCategory;
	
	/** Table header: Pricing Category */
	private String tblcolPricingCategoryHistory;
	
	private String contactInfoSpdResi; // SPD Residential
	private String contactInfoSdpResi; // SDP Residential 
	private String contactInfoSaiResi; // SAI Residential 
	private String contactInfoApiCom; // API Commercial
	private String contactInfoDbpCom; // DBP Commercial
	private String contactInfoSaiCom; // SAI Commercial 
	private String contactInfoSdpCom; // SDP Commercial
	private String contactInfoBipCom; // TOU-BIP Commercial
	
	private String sdpScheduledMessage;
	

	
	public TextContextBackingBean(){
	}
	
	public String getCopyrightRTP() {
		if (copyrightRTP == null || copyrightRTP ==""){
			DRWebsiteProperty dRWebsiteProperty = getManager().getDRWebsitePropertyByPropertyName(DRWTextContextConstants.COPYRIGHT_RTP);
			copyrightRTP = dRWebsiteProperty.getTextValue();
		}
		return copyrightRTP;
	}

	public String getCopyrightSCE() {
		if (copyrightSCE == null || copyrightSCE ==""){
			DRWebsiteProperty dRWebsiteProperty = getManager().getDRWebsitePropertyByPropertyName(DRWTextContextConstants.COPYRIGHT_SCE);
			copyrightSCE = dRWebsiteProperty.getTextValue();
		}
		return copyrightSCE;
	}

	public String getCopyrightActiveEventTitle() {
		if (copyrightActiveEventTitle == null || copyrightActiveEventTitle ==""){
			DRWebsiteProperty dRWebsiteProperty = getManager().getDRWebsitePropertyByPropertyName(DRWTextContextConstants.COPYRIGHT_ACTIVE_EVENTS_TITLE);
			copyrightActiveEventTitle = dRWebsiteProperty.getTextValue();
		}
		return copyrightActiveEventTitle;
	}

	public String getCopyrightHistoryEventTitle() {
		if (copyrightHistoryEventTitle == null || copyrightHistoryEventTitle ==""){
			DRWebsiteProperty dRWebsiteProperty = getManager().getDRWebsitePropertyByPropertyName(DRWTextContextConstants.COPYRIGHT_HISTORY_EVENTS_TITLE);
			copyrightHistoryEventTitle = dRWebsiteProperty.getTextValue();
		}
		return copyrightHistoryEventTitle;
	}

	public String getCopyrightActiveEventDBP() {
		if (copyrightActiveEventDBP == null || copyrightActiveEventDBP ==""){
			DRWebsiteProperty dRWebsiteProperty = getManager().getDRWebsitePropertyByPropertyName(DRWTextContextConstants.COPYRIGHT_ACTIVE_EVENTS_DBP);
			copyrightActiveEventDBP = dRWebsiteProperty.getTextValue();
		}
		return copyrightActiveEventDBP;
	}

	public String getCopyrightHistoryEventSearchTitle() {
		if (copyrightHistoryEventSearchTitle == null || copyrightHistoryEventSearchTitle ==""){
			DRWebsiteProperty dRWebsiteProperty = getManager().getDRWebsitePropertyByPropertyName(DRWTextContextConstants.COPYRIGHT_HISTORY_EVENTS_SEARCH_TITLE);
			copyrightHistoryEventSearchTitle = dRWebsiteProperty.getTextValue();
		}
		return copyrightHistoryEventSearchTitle;
	}

	public String getTitleEventStatusPage() {
		if (titleEventStatusPage == null || titleEventStatusPage ==""){
			DRWebsiteProperty dRWebsiteProperty = getManager().getDRWebsitePropertyByPropertyName(DRWTextContextConstants.TITLE_EVENT_STATUS_PAGE);
			titleEventStatusPage = dRWebsiteProperty.getTextValue();
		}
		return titleEventStatusPage;
	}
	
	public String getTitleEventHistoryPage() {
		if (titleEventHistoryPage == null || titleEventHistoryPage ==""){
			DRWebsiteProperty dRWebsiteProperty = getManager().getDRWebsitePropertyByPropertyName(DRWTextContextConstants.TITLE_EVENT_HISTORY_PAGE);
			titleEventHistoryPage = dRWebsiteProperty.getTextValue();
		}
		return titleEventHistoryPage;
	}

	public String getTitleEventStatusRTP() {
		if (titleEventStatusRTP == null || titleEventStatusRTP ==""){
			DRWebsiteProperty dRWebsiteProperty = getManager().getDRWebsitePropertyByPropertyName(DRWTextContextConstants.TITLE_EVENT_STATUS_RTP);
			titleEventStatusRTP = dRWebsiteProperty.getTextValue();
		}
		return titleEventStatusRTP;
	}

	public String getContactNo() {
		if (contactNo == null || contactNo ==""){
			DRWebsiteProperty dRWebsiteProperty = getManager().getDRWebsitePropertyByPropertyName(DRWTextContextConstants.CONTACT_NO);
			contactNo = dRWebsiteProperty.getTextValue();
		}
		return contactNo;
	}

	public String getTblcolProgramName() {
		if (tblcolProgramName == null || tblcolProgramName ==""){
			DRWebsiteProperty dRWebsiteProperty = getManager().getDRWebsitePropertyByPropertyName(DRWTextContextConstants.TBLCOL_PROGRAM_NAME);
			tblcolProgramName = dRWebsiteProperty.getTextValue();
		}
		return tblcolProgramName;
	}

	public String getTblcolRegions() {
		if (tblcolRegions == null || tblcolRegions ==""){
			DRWebsiteProperty dRWebsiteProperty = getManager().getDRWebsitePropertyByPropertyName(DRWTextContextConstants.TBLCOL_REGIONS);
			tblcolRegions = dRWebsiteProperty.getTextValue();
		}
		return tblcolRegions;
	}

	public String getTblcolDate() {
		if (tblcolDate == null || tblcolDate ==""){
			DRWebsiteProperty dRWebsiteProperty = getManager().getDRWebsitePropertyByPropertyName(DRWTextContextConstants.TBLCOL_DATE);
			tblcolDate = dRWebsiteProperty.getTextValue();
		}
		return tblcolDate;
	}

	public String getTblcolStartTime() {
		if (tblcolStartTime == null || tblcolStartTime ==""){
			DRWebsiteProperty dRWebsiteProperty = getManager().getDRWebsitePropertyByPropertyName(DRWTextContextConstants.TBLCOL_START_TIME);
			tblcolStartTime = dRWebsiteProperty.getTextValue();
		}
		return tblcolStartTime;
	}

	public String getTblcolEndTime() {
		if (tblcolEndTime == null || tblcolEndTime ==""){
			DRWebsiteProperty dRWebsiteProperty = getManager().getDRWebsitePropertyByPropertyName(DRWTextContextConstants.TBLCOL_END_TIME);
			tblcolEndTime = dRWebsiteProperty.getTextValue();
		}
		return tblcolEndTime;
	}

	public String getTblcolTemperature() {
		if (tblcolTemperature == null || tblcolTemperature ==""){
			DRWebsiteProperty dRWebsiteProperty = getManager().getDRWebsitePropertyByPropertyName(DRWTextContextConstants.TBLCOL_TEMPERATURE);
			tblcolTemperature = dRWebsiteProperty.getTextValue();
		}
		return tblcolTemperature;
	}

	
	public String getTblcolPricingCategory() {
		if (tblcolPricingCategory == null || tblcolPricingCategory ==""){
			DRWebsiteProperty dRWebsiteProperty = getManager().getDRWebsitePropertyByPropertyName(DRWTextContextConstants.TBLCOL_PRICE_CATEGORY);
			tblcolPricingCategory = dRWebsiteProperty.getTextValue();
		}
		return tblcolPricingCategory;
	}	

	public String getCopyrightDRC() {
		if (copyrightDRC == null || copyrightDRC ==""){
			DRWebsiteProperty dRWebsiteProperty = getManager().getDRWebsitePropertyByPropertyName(DRWTextContextConstants.COPYRIGHT_DRC);
			copyrightDRC = dRWebsiteProperty.getTextValue();
		}
		return copyrightDRC;
	}

	public String getTblcolDateOfUsage() {
		if (tblcolDateOfUsage == null || tblcolDateOfUsage ==""){
			DRWebsiteProperty dRWebsiteProperty = getManager().getDRWebsitePropertyByPropertyName(DRWTextContextConstants.TBLCOL_Date_Of_Usage);
			tblcolDateOfUsage = dRWebsiteProperty.getTextValue();
		}
		return tblcolDateOfUsage;
	}

	public String getForecastSubtitleRTP() {
		if (forecastSubtitleRTP == null || forecastSubtitleRTP ==""){
			DRWebsiteProperty dRWebsiteProperty = getManager().getDRWebsitePropertyByPropertyName(DRWTextContextConstants.RTPFORECAST_SUBTITLE);
			forecastSubtitleRTP = dRWebsiteProperty.getTextValue();
		}
		return forecastSubtitleRTP;
	}

	public String getCopyrightRTPForecast() {
		if (copyrightRTPForecast == null || copyrightRTPForecast ==""){
			DRWebsiteProperty dRWebsiteProperty = getManager().getDRWebsitePropertyByPropertyName(DRWTextContextConstants.COPYRIGHT_RTPFORECAST);
			copyrightRTPForecast = dRWebsiteProperty.getTextValue();
		}
		return copyrightRTPForecast;
	}

	public String getCopyrightHistoryRTPSearch() {
		if (copyrightHistoryRTPSearch == null || copyrightHistoryRTPSearch ==""){
			DRWebsiteProperty dRWebsiteProperty = getManager().getDRWebsitePropertyByPropertyName(DRWTextContextConstants.COPYRIGHT_HISTORY_RTP_SEARCH);
			copyrightHistoryRTPSearch = dRWebsiteProperty.getTextValue();
		}
		return copyrightHistoryRTPSearch;
	}

	public void setTblcolPricingCategoryHistory(
			String tblcolPricingCategoryHistory) {
		this.tblcolPricingCategoryHistory = tblcolPricingCategoryHistory;
	}

	public String getTblcolPricingCategoryHistory() {
		if (tblcolPricingCategoryHistory == null || tblcolPricingCategoryHistory ==""){
			DRWebsiteProperty dRWebsiteProperty = getManager().getDRWebsitePropertyByPropertyName(DRWTextContextConstants.TBLCOL_PRICE_CATEGORY_HISTORY);
			tblcolPricingCategoryHistory = dRWebsiteProperty.getTextValue();
		}
		return tblcolPricingCategoryHistory;
	}

	private static CFEventManager manager;
	public CFEventManager getManager(){
		if(manager == null){
			manager = DRWUtil.getCFEventManager();
		}
		return manager;	 
	}

	public String getContactInfoSdpResi() {
		if (contactInfoSdpResi == null || contactInfoSdpResi ==""){
			DRWebsiteProperty dRWebsiteProperty = getManager().getDRWebsitePropertyByPropertyName(DRWTextContextConstants.CONTACT_INFO_SDP_RESI);
			contactInfoSdpResi = dRWebsiteProperty.getTextValue();
		}
		return contactInfoSdpResi;
	}

	public String getContactInfoSaiResi() {
		if (contactInfoSaiResi == null || contactInfoSaiResi ==""){
			DRWebsiteProperty dRWebsiteProperty = getManager().getDRWebsitePropertyByPropertyName(DRWTextContextConstants.CONTACT_INFO_SAI_RESI);
			contactInfoSaiResi = dRWebsiteProperty.getTextValue();
		}
		return contactInfoSaiResi;
	}

	public String getContactInfoApiCom() {
		if (contactInfoApiCom == null || contactInfoApiCom ==""){
			DRWebsiteProperty dRWebsiteProperty = getManager().getDRWebsitePropertyByPropertyName(DRWTextContextConstants.CONTACT_INFO_API_COM);
			contactInfoApiCom = dRWebsiteProperty.getTextValue();
		}
		return contactInfoApiCom;
	}

	public String getContactInfoDbpCom() {
		if (contactInfoDbpCom == null || contactInfoDbpCom ==""){
			DRWebsiteProperty dRWebsiteProperty = getManager().getDRWebsitePropertyByPropertyName(DRWTextContextConstants.CONTACT_INFO_DBP_COM);
			contactInfoDbpCom = dRWebsiteProperty.getTextValue();
		}
		return contactInfoDbpCom;
	}

	public String getContactInfoSaiCom() {
		if (contactInfoSaiCom == null || contactInfoSaiCom ==""){
			DRWebsiteProperty dRWebsiteProperty = getManager().getDRWebsitePropertyByPropertyName(DRWTextContextConstants.CONTACT_INFO_SAI_COM);
			contactInfoSaiCom = dRWebsiteProperty.getTextValue();
		}
		return contactInfoSaiCom;
	}

	public String getContactInfoSdpCom() {
		if (contactInfoSdpCom == null || contactInfoSdpCom ==""){
			DRWebsiteProperty dRWebsiteProperty = getManager().getDRWebsitePropertyByPropertyName(DRWTextContextConstants.CONTACT_INFO_SDP_COM);
			contactInfoSdpCom = dRWebsiteProperty.getTextValue();
		}
		return contactInfoSdpCom;
	}

	public String getContactInfoBipCom() {
		if (contactInfoBipCom == null || contactInfoBipCom ==""){
			DRWebsiteProperty dRWebsiteProperty = getManager().getDRWebsitePropertyByPropertyName(DRWTextContextConstants.CONTACT_INFO_BIP_COM);
			contactInfoBipCom = dRWebsiteProperty.getTextValue();
		}
		return contactInfoBipCom;
	}
	 private String disclaimerSDPResidential;

	    public String getDisclaimerSDPResidential() {
	        if (disclaimerSDPResidential == null || disclaimerSDPResidential ==""){
	      			DRWebsiteProperty dRWebsiteProperty = getManager().getDRWebsitePropertyByPropertyName("DISCLAIMER_SDP_RESIDENTIAL");
	            disclaimerSDPResidential = dRWebsiteProperty.getTextValue();
	      		}
	        return disclaimerSDPResidential;
	    }

	    private String disclaimerSDPCommercial;

	    public String getDisclaimerSDPCommercial() {
	        if (disclaimerSDPCommercial == null || disclaimerSDPCommercial ==""){
	            DRWebsiteProperty dRWebsiteProperty = getManager().getDRWebsitePropertyByPropertyName("DISCLAIMER_SDP_COMMERCIAL");
	            disclaimerSDPCommercial = dRWebsiteProperty.getTextValue();
	        }
	        return disclaimerSDPCommercial;
	    }

	    private String disclaimerAPICommercial;

	    public String getDisclaimerAPICommercial() {
	        if (disclaimerAPICommercial == null || disclaimerAPICommercial ==""){
	            DRWebsiteProperty dRWebsiteProperty = getManager().getDRWebsitePropertyByPropertyName("DISCLAIMER_API_COMMERCIAL");
	            disclaimerAPICommercial = dRWebsiteProperty.getTextValue();
	        }
	        return disclaimerAPICommercial;
	    }

	    private String disclaimerBIPCommercial;

	    public String getDisclaimerBIPCommercial() {
	        if (disclaimerBIPCommercial == null || disclaimerBIPCommercial ==""){
	            DRWebsiteProperty dRWebsiteProperty = getManager().getDRWebsitePropertyByPropertyName("DISCLAIMER_BIP_COMMERCIAL");
	            disclaimerBIPCommercial = dRWebsiteProperty.getTextValue();
	        }
	        return disclaimerBIPCommercial;
	    }

		/**
		 * @param contactInfoSpdResi the contactInfoSpdResi to set
		 */
		public void setContactInfoSpdResi(String contactInfoSpdResi) {
			this.contactInfoSpdResi = contactInfoSpdResi;
		}

		/**
		 * @return the contactInfoSpdResi
		 */
		public String getContactInfoSpdResi() {
			if (contactInfoSpdResi == null || contactInfoSpdResi ==""){
      			DRWebsiteProperty dRWebsiteProperty = getManager().getDRWebsitePropertyByPropertyName(DRWTextContextConstants.CONTACT_INFO_SPD_COM);
      			contactInfoSpdResi = dRWebsiteProperty.getTextValue();
      		}
        return contactInfoSpdResi;
		}

		/**
		 * @return the sdpScheduledMessage
		 */
		public String getSdpScheduledMessage() {
			if (sdpScheduledMessage == null || sdpScheduledMessage ==""){
      			DRWebsiteProperty dRWebsiteProperty = getManager().getDRWebsitePropertyByPropertyName(DRWTextContextConstants.DBP_SCHEDULE_INFO);
      			sdpScheduledMessage = dRWebsiteProperty.getTextValue();
      		}
			return sdpScheduledMessage;
		}

}
