
package com.akuacom.pss2.customer.report;

import com.akuacom.pss2.system.SystemManager;
import com.akuacom.pss2.system.property.CoreProperty;
import java.io.IOException;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.ajax4jsf.model.DataVisitor;
import org.ajax4jsf.model.Range;
import org.ajax4jsf.model.SerializableDataModel;

import com.akuacom.pss2.util.DrasRole;

public class Footer extends SerializableDataModel {
  String utility;
  boolean newsEnabled = false;
  boolean participantsMapViewEnabled = false;
  boolean participantsUploadEnabled = false;
  boolean participantNotesEnabled = false;
  boolean participantInfoEnabled = false;

  String hostName;
  boolean admin;
  boolean operator;


  public Footer(){
      this.setUtility(utilLogo(this.getUtilityName()));
      this.newsEnabled = this.getCoreAccess("feature.news");
      this.participantsMapViewEnabled = this.getCoreAccess("feature.participantsMapView");
      this.participantsUploadEnabled = this.getCoreAccess("feature.participantsUpload");
      this.participantNotesEnabled = this.getCoreAccess("feature.participantNotes");
      this.participantInfoEnabled = this.getCoreAccess("feature.participantInfo");

      ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
      HttpServletRequest request = (HttpServletRequest) context.getRequest();
      if (request.isUserInRole(DrasRole.Admin.toString())){
                this.admin = true;
      }if (request.isUserInRole(DrasRole.Operator.toString())){
                this.operator = true;
      }
  }

	private SystemManager systemManager;
	public SystemManager getSystemManager() {
		if(systemManager==null)
			systemManager =ServiceLocator.findHandler(SystemManager.class,
					"pss2/SystemManagerBean/remote");
		return systemManager;
	}

    public boolean getCoreAccess(String coreValue){
    	if(systemManager==null){
        	systemManager = getSystemManager();
    	}
           boolean flag= false;
           for(CoreProperty corp : systemManager.getAllProperties()){
        	   if (corp.getPropertyName().equalsIgnoreCase(coreValue))
                  flag =corp.isBooleanValue();
           }
       return flag;
    }

   public String getUtilityName(){
	   if(systemManager==null){
       		systemManager = getSystemManager();
	   }
           String uName= "";
           for(CoreProperty corp : systemManager.getAllProperties()){
        	   if (corp.getPropertyName().equalsIgnoreCase("utilityName"))
                   uName =corp.getStringValue();
           }
       return uName;
    }



    public String getUtility() {
      return utility;
    }


    public void setUtility(String utility) {
        this.utility = utility;
    }

    public boolean isNewsEnabled() {
        return newsEnabled;
    }

    public void setNewsEnabled(boolean newsEnabled) {
        this.newsEnabled = newsEnabled;
    }

    public boolean isParticipantsMapViewEnabled() {
        return this.participantsMapViewEnabled;
    }

    public void setParticipantsMapViewEnabled(boolean participantsMapViewEnabled) {
        this.participantsMapViewEnabled = participantsMapViewEnabled;
    }

    public String getHostName() {
                ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                HttpServletRequest request = (HttpServletRequest) context.getRequest();
                hostName = request.getServerName();
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }
    public boolean isParticipantsUploadEnabled() {
    	return participantsUploadEnabled;
    }



    public void setParticipantsUploadEnabled(boolean participantsUploadEnabled) {
    	this.participantsUploadEnabled = participantsUploadEnabled;
    }



    public boolean isParticipantNotesEnabled() {
    	return participantNotesEnabled;
    }



    public void setParticipantNotesEnabled(boolean participantNotesEnabled) {
    	this.participantNotesEnabled = participantNotesEnabled;
    }



    public boolean isParticipantInfoEnabled() {
    	return participantInfoEnabled;
    }



    public void setParticipantInfoEnabled(boolean participantInfoEnabled) {
    	this.participantInfoEnabled = participantInfoEnabled;
    }


    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public boolean isOperator() {
        return operator;
    }

    public void setOperator(boolean operator) {
        this.operator = operator;
    }

 
   private String utilLogo(String utilName){
        
		String imgName = "";
		String linkName = "";
      	//TODO: We need to refactor this out to a properties file
		if ((utilName.contains("pge")) || (utilName.equalsIgnoreCase("pge"))) {
			imgName = "pge-logo.gif";
			linkName = "http://www.pge.com";
		} else if ((utilName.contains("sce"))
				|| (utilName.equalsIgnoreCase("sce"))) {
			imgName = "sce_logo.gif";
			linkName = "http://www.sce.com/";
		} else if ((utilName.contains("nrcan"))
				| (utilName.equalsIgnoreCase("nrcan"))) {
			imgName = "nrcan-logo.gif";
			linkName = "http://www.nrcan-rncan.gc.ca/com/";
		} else if ((utilName.contains("sdg"))
				|| (utilName.equalsIgnoreCase("sdg"))) {
			imgName = "sdge-logo.gif";
			linkName = "http://www.sdge.com";
		} else if ((utilName.contains("clp"))
				|| (utilName.equalsIgnoreCase("clp"))) {
			imgName = "clp.png";
			linkName = "https://www.clpgroup.com/Pages/home.aspx";
		} else if ((utilName.contains("hyw"))
				|| (utilName.equalsIgnoreCase("hyw"))) {
			imgName = "hyw.png";
			linkName = "http://www.honeywell.com";
		} else if ((utilName.contains("akua"))
				|| (utilName.equalsIgnoreCase("akua"))) {
			imgName = "akua.png";
			linkName = "http://www.akuacom.com";
		} else if ((utilName.contains("cot"))
				|| (utilName.equalsIgnoreCase("cot"))) {
			imgName = "cot.png";
			linkName = "http://www.talgov.com/you/electric/index.cfm";
		} else if ((utilName.contains("duke"))
				|| (utilName.equalsIgnoreCase("duke"))) {
			imgName = "duke.gif";
			linkName = "http://www.duke-energy.com/";
		} else if ((utilName.contains("smud"))
				|| (utilName.equalsIgnoreCase("smud"))) {
			imgName = "smud.gif";
			linkName = "http://www.smud.org";
		} else if ((utilName.contains("erm"))
				|| (utilName.equalsIgnoreCase("erm"))) {
			imgName = "erm.jpg";
			linkName = "http://www.ermpower.com.au";
		} else if ((utilName.contains("ergon"))
				|| (utilName.equalsIgnoreCase("ergon"))) {
			imgName = "ergon.jpg";
			linkName = "http://www.ergon.com.au/";
		} else if ((utilName.contains("sgepri"))
				|| (utilName.equalsIgnoreCase("sgepri"))) {
			imgName = "sgepri.jpg";
			linkName = "http://www.sgcc.com.cn/";
		} else if ((utilName.contains("agl"))
				|| (utilName.equalsIgnoreCase("agl"))) {
			imgName = "agl.gif";
			linkName = "http://www.agl.com.au/";
		} else if ((utilName.contains("tata"))
				|| (utilName.equalsIgnoreCase("tata"))) {
			imgName = "tata.gif";
			linkName = "http://www.tatapower.com/";
		} else if ((utilName.contains("sse"))
				|| (utilName.equalsIgnoreCase("sse"))) {
			imgName = "sse.jpg";
			linkName = "http://www.scottish-southern.co.uk";
		} else if ((utilName.contains("tpnz"))
				|| (utilName.equalsIgnoreCase("tpnz"))) {
			imgName = "tpnz.gif";
			linkName = "http://www.transpower.co.nz/";
		} else if ((utilName.contains("tva"))
				|| (utilName.equalsIgnoreCase("tva"))) {
			imgName = "tva.png";
			linkName = "http://www.tva.gov/";
		} else if ((utilName.contains("cps"))
				|| (utilName.equalsIgnoreCase("cps"))) {
			imgName = "cps.jpg";
			linkName = "http://www.cpsenergy.com/";
		} else if ((utilName.contains("bhi"))
				|| (utilName.equalsIgnoreCase("bhi"))) {
			imgName = "bhi.jpg";
			linkName = "http://www.burlingtonhydro.com/";
		} else if ((utilName.contains("lhi"))
				|| (utilName.equalsIgnoreCase("lhi"))) {
			imgName = "lhi.gif";
			linkName = "http://www.londonhydro.com/";
		} else if ((utilName.contains("nbpower"))
				|| (utilName.equalsIgnoreCase("nbpower"))) {
			imgName = "nbpower.jpg";
			linkName = "http://www.nbpower.com/";
		} else if ((utilName.contains("thes"))
				|| (utilName.equalsIgnoreCase("thes"))) {
			imgName = "thes.jpg";
			linkName = "http://www.torontohydro.com";
		} else if ((utilName.contains("powerstream"))
				|| (utilName.equalsIgnoreCase("powerstream"))) {
			imgName = "powerstream.gif";
			linkName = "http://www.powerstream.ca";
		} else if ((utilName.contains("njbpu"))
				|| (utilName.equalsIgnoreCase("njbpu"))) {
			imgName = "njbpu.gif";
			linkName = "http://www.bpu.state.nj.us/";
		} else if ((utilName.contains("coned"))
				|| (utilName.equalsIgnoreCase("coned"))) {
			imgName = "coned.jpg";
			linkName = "http://www.coned.com/";
		} else if ((utilName.contains("peco"))
				|| (utilName.equalsIgnoreCase("peco"))) {
			imgName = "peco.gif";
			linkName = "http://www.peco.com/";
		} else if ((utilName.contains("aec"))
				|| (utilName.equalsIgnoreCase("aec"))) {
			imgName = "aec.jpg";
			linkName = "http://www.aep.com/";
		} else if ((utilName.contains("xcel"))
				|| (utilName.equalsIgnoreCase("xcel"))) {
			imgName = "xcel.jpg";
			linkName = "http://www.xcelenergy.com";
		} else if ((utilName.contains("cms"))
				|| (utilName.equalsIgnoreCase("cms"))) {
			imgName = "cms.jpg";
			linkName = "http://www.consumersenergy.com/";
		} else if ((utilName.contains("gre"))
				|| (utilName.equalsIgnoreCase("gre"))) {
			imgName = "gre.gif";
			linkName = "http://www.greatriverenergy.com/";
		} else if ((utilName.contains("lge"))
				|| (utilName.equalsIgnoreCase("lge"))) {
			imgName = "lge.jpg";
			linkName = "http://www.lgeenergy.com/";
		} else if ((utilName.contains("bge"))
				|| (utilName.equalsIgnoreCase("bge"))) {
			imgName = "bge.jpg";
			linkName = "http://www.bge.com";
		} else if ((utilName.contains("dvp"))
				|| (utilName.equalsIgnoreCase("dvp"))) {
			imgName = "dvp.jpg";
			linkName = "http://www.dom.com/";
		} else if ((utilName.contains("ladwp"))
				|| (utilName.equalsIgnoreCase("ladwp"))) {
			imgName = "ladwp.jpg";
			linkName = "http://www.ladwp.com";
		} else if ((utilName.contains("meco"))
				|| (utilName.equalsIgnoreCase("meco"))) {
			imgName = "meco.gif";
			linkName = "http://www.mauielectric.com";
		} else if ((utilName.contains("heco"))
				|| (utilName.equalsIgnoreCase("heco"))) {
			imgName = "heco.gif";
			linkName = "http://www.heco.com";
		} else if ((utilName.contains("austin"))
				|| (utilName.equalsIgnoreCase("austin"))) {
			imgName = "austin.jpg";
			linkName = "http://www.austinenergy.com/";
		} else if ((utilName.contains("kcpl"))
				|| (utilName.equalsIgnoreCase("kcpl"))) {
			imgName = "kcpl.gif";
			linkName = "http://www.kcpl.com/";
		} else if ((utilName.contains("cpe"))
				|| (utilName.equalsIgnoreCase("cpe"))) {
			imgName = "cpe.jpg";
			linkName = "http://www.centerpointenergy.com";
		} else if ((utilName.contains("oncor"))
				|| (utilName.equalsIgnoreCase("oncor"))) {
			imgName = "oncor.jpg";
			linkName = "http://www.oncor.com/";
		} else if ((utilName.contains("sask"))
				|| (utilName.equalsIgnoreCase("sask"))) {
			imgName = "sask.jpg";
			linkName = "http://www.saskpower.com/";
		} else if ((utilName.contains("bch"))
				|| (utilName.equalsIgnoreCase("bch"))) {
			imgName = "bch.jpg";
			linkName = "http://www.bchydro.com/";
		} else if ((utilName.contains("manitoba"))
				|| (utilName.equalsIgnoreCase("manitoba"))) {
			imgName = "manitoba.jpg";
			linkName = "http://www.hydro.mb.ca/";
		}else if ((utilName.contains("teda"))
					|| (utilName.equalsIgnoreCase("teda"))) {
				imgName = "teda_logo.gif";
				linkName = "http://www.investteda.org/";
		}else if ((utilName.contains("viridity"))
					|| (utilName.equalsIgnoreCase("viridity"))) {
				imgName = "viridity.jpg";
				linkName = "http://viridityenergy.com/";
		} else {
			imgName = "default.png";
			imgName = "http://www.honeywell.com";
		}
		return imgName;
    }

    @Override
    public void update() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setRowKey(Object o) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object getRowKey() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void walk(FacesContext fc, DataVisitor dv, Range range, Object o) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isRowAvailable() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getRowCount() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object getRowData() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getRowIndex() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setRowIndex(int i) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object getWrappedData() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setWrappedData(Object o) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

   




}
