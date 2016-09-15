package com.akuacom.pss2.richsite.program.configure.rtp;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import org.openadr.dras.akuartpconfig.AkuaRTPConfig;
import org.openadr.dras.akuartpconfig.ListOfRTPConfigs;

import com.akuacom.ejb.client.EJB3Factory;
import com.akuacom.pss2.program.scertp.RTPConfig;
import com.akuacom.pss2.program.scertp.SCERTPProgramManager;
import com.akuacom.pss2.program.scertp.SCERTPProgramManagerBean;
import com.akuacom.pss2.util.DateTool;

public class RTPConfigureDataModelManagerImpl 
	implements RTPConfigureDataModelManager, Serializable {
	private SCERTPProgramManager scertpprogramManager = EJB3Factory.getLocalBean(SCERTPProgramManagerBean.class);
	
	/**
	 * Function for save rtp csv file from presentation layer data into database
	 * @param model
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public String saveRTPFileIntoDB(RTPConfigureDataModel model,File file) throws IOException{
		if(model.getProgramConfigureDataModel()!=null&&model.getProgramConfigureDataModel().getProgramName()!=null){
			if("com.akuacom.pss2.program.scertp2013.SCERTPProgramEJB2013".equalsIgnoreCase(model.getProgramConfigureDataModel().getProgram().getClassName())){
				return SCERTPImporter.processUploadedFile2013(file, model.getProgramConfigureDataModel().getProgramName());	
				
			}
			return SCERTPImporter.processUploadedFile(file, model.getProgramConfigureDataModel().getProgramName());	
		}else{
			return "";
		}
		
	}
	
	/**
	 * Function for get RTPConfig object from database
	 * @param model
	 * @throws Exception
	 */
	public void getRTPConfigs(RTPConfigureDataModel model) throws Exception{
		if(model.getProgramConfigureDataModel()!=null&&model.getProgramConfigureDataModel().getProgramName()!=null){
			List<RTPConfig> scs = scertpprogramManager.findRTPConfigsByProgramId(model.getProgramConfigureDataModel().getProgram().getUUID());
			ListOfRTPConfigs list = new ListOfRTPConfigs();

			for (RTPConfig prop : scs) {
				AkuaRTPConfig c = getRTPConfig(prop);
				if (c != null)
					list.getPriceTable().add(c);
			}
			
			if(list.getPriceTable().size()==0){
				model.setRtpNotExistWarnFlag(true);
				model.setWarningMessage(RTPConfigureDataModel.WARNING_NO_RTP);
			}else{
				model.setRtpNotExistWarnFlag(false);
//				RTPForm form = new RTPForm(this);
//				form.setProgramName(programName);
//				form.buildRTPForm(list);
//				setRtpForm(form);
				
				model.setList(list);
				model.buildRTPForm();
			}
		}
	}
	private AkuaRTPConfig getRTPConfig(RTPConfig prop) throws Exception {
		AkuaRTPConfig aprop = new AkuaRTPConfig();
		aprop.setId(prop.getUUID());
		aprop.setName(prop.getName());
		aprop.setStartTime(DateTool.converDateToXMLGregorianCalendar(prop
				.getStartTime()));
		aprop.setEndTime(DateTool.converDateToXMLGregorianCalendar(prop
				.getEndTime()));
		aprop.setStartTemperature(prop.getStartTemperature());
		aprop.setEndTemperature(prop.getEndTemperature());
		aprop.setRate(prop.getRate());
		aprop.setSeasonName(prop.getSeasonName());
		return aprop;
	}
}
