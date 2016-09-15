package com.akuacom.pss2.web.common;

import java.io.Serializable;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;

import com.akuacom.pss2.system.SystemManager;
import com.akuacom.pss2.system.property.PSS2Properties;

public class Config implements Serializable {

	private static final long serialVersionUID = -7413080352684141176L;

	private String logo;
	private String title;
	private String alt;
	private String link;
	private String copyright;
	private String utilityName;
	
	public Config(){
		SystemManager systemManager=getSystemManager();
		PSS2Properties pss2 = systemManager.getPss2Properties();
		title=pss2.getUtilityDisplayName();
		alt=pss2.getUtilityDisplayName();
		link=pss2.getContactURLLink();
		copyright=pss2.getCopyright();
		utilityName=pss2.getUtilityName();
		
		logo=utilLogo(utilityName);
		if (logo==null)
			logo=pss2.getLogo();
	}
	
	public SystemManager getSystemManager() {
		return Config.findHandler(SystemManager.class, "pss2/SystemManagerBean/remote");
	}
	
	public String getHostName() {
		ExternalContext context = FacesContext.getCurrentInstance()
				.getExternalContext();
		HttpServletRequest request = (HttpServletRequest) context.getRequest();
		String hostName = request.getServerName();
		return hostName;
	}

	public String getLogo(){
		return logo;
	}
	
	public String getTitle(){
		return title;
	}
	
	public String getAlt(){
		return alt;
	}
	
	public String getLink(){
		return link;
	}
	
	public String getCopyright(){
		return copyright;
    }

	
	private String utilLogo(String utilName) {
		String imgName = null;
		@SuppressWarnings("unused")
		String linkName = null;
		// TODO: We need to refactor this out to a properties file
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
		} else if ((utilName.contains("teda"))
				|| (utilName.equalsIgnoreCase("teda"))) {
			imgName = "teda_logo.gif";
			linkName = "http://www.investteda.org/";
		} else if ((utilName.contains("viridity"))
				|| (utilName.equalsIgnoreCase("viridity"))) {
			imgName = "viridity.jpg";
			linkName = "http://viridityenergy.com/";
//		} else {
//			imgName = "default.png";
//			imgName = "http://www.honeywell.com";
		}
		return imgName;
	}

	public static <T> T findHandler(Class<T> ServiceType, String serviceName){
		try{
			 Context namingContext=new InitialContext();
			 @SuppressWarnings("unchecked")
			 T ret = (T)namingContext.lookup(serviceName);
             return ret;
		}catch(Throwable e){
			e.printStackTrace();
			return null;
		}
	}
}
