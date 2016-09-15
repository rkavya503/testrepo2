package com.akuacom.pss2.richsite.program.configure;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.faces.model.SelectItem;

import org.openadr.dras.akuautilityprogram.AkuaBidConfig;

import com.akuacom.ejb.client.EJB3Factory;
import com.akuacom.pss2.program.Program;
import com.akuacom.pss2.program.ProgramManager;
import com.akuacom.pss2.program.ProgramManagerBean;
import com.akuacom.pss2.program.signal.ProgramSignal;
import com.akuacom.pss2.richsite.program.configure.bid.BidConfigureValidator;
import com.akuacom.pss2.richsite.program.configure.mode.ModeConfigureValidator;
import com.akuacom.pss2.richsite.program.configure.signal.SignalConfigureValidator;
import com.akuacom.pss2.richsite.program.configure.signal.SignalDefDataModel;
import com.akuacom.pss2.richsite.util.AkuacomJSFUtil;
import com.akuacom.pss2.signal.SignalDef;
import com.akuacom.pss2.signal.SignalManager;

public class ProgramConfigureDataModelManagerImpl implements ProgramConfigureDataModelManager,Serializable{

	/** ProgramManager instance reference*/
	private ProgramManager programManager = EJB3Factory.getBean(ProgramManagerBean.class);

	/** className for DBPProgramEJB*/
	private static final String className_DBPProgramEJB = "com.akuacom.pss2.program.dbp.DBPProgramEJB";

	/** className for DBPBidProgramEJB*/
	private static final String className_DBPBidProgramEJB ="com.akuacom.pss2.program.dbp.DBPBidProgramEJB";

	/** className for DBPNoBidProgramEJB*/
	private static final String className_DBPNoBidProgramEJB ="com.akuacom.pss2.program.dbp.DBPNoBidProgramEJB";

	/**
	 * Function for save program configuration into DB
	 * @param model	ProgramConfigureDataModel instance
	 * @throws Exception
	 */
	public void saveProgramConfigure(ProgramConfigureDataModel model) throws Exception{
		try{
			//judge the operation is ADD or UPDATE...
			String operationFlag = model.getOperationFlag();
			if(operationFlag.equalsIgnoreCase(ProgramConfigureDataModel.OPERATION_FLAG_ADD)){
				//validate data
				validateData(model,true);
                //construct the program entity with valid data
				Program program = buildData(model,true);

				if(program!=null){
					//save program entity into database
					addData(program);
				}
			}
			//comments out below codes by Frank, no longer used
			
//			else if(operationFlag.equalsIgnoreCase(ProgramConfigureDataModel.OPERATION_FLAG_UPDATE)){
//				//updateProgram(model);
//
//				//validate data
//				validateData(model,false);
//				//construct the program entity with valid data
//				Program program = buildData(model,false);
//				if(program!=null){
//					//save program entity into database
//					updateData(program);
//				}
//			}
		}catch(Exception e){
			//Add error message into JSF FacesMessage object
//			AkuacomJSFUtil.addErrorMessage(e.getMessage());
			//Throw exception to where it should be handle
			throw e;
		}
	}
	/**
	 * Function for get program configuration object from DB
	 * @param model ProgramConfigureDataModel instance
	 */
	public void getProgramConfigure(ProgramConfigureDataModel model){
		//Retrieve program entity from database
		Program program = getProgram(model);
		//Set program reference into model
		model.setProgram(program);
	}

//	/**
//	 * Function for build ProgramConfigureDataModel object attributes by program entity
//	 * Should be invoked after get program from database
//	 * @param model
//	 * @param program
//	 */
//	public void buildProgramConfigure(ProgramConfigureDataModel model,Program program){
//		model.setMaxIssueTimeString(program.getMaxIssueTimeH()+":"+program.getMaxIssueTimeM());
//		model.setMinStartTimeString(program.getMinStartTimeH()+":"+program.getMinStartTimeM());
//		model.setMaxStartTimeString(program.getMaxStartTimeH()+":"+program.getMaxStartTimeM());
//		model.setMinEndTimeString(program.getMinEndTimeH()+":"+program.getMinEndTimeM());
//		model.setMaxEndTimeString(program.getMaxEndTimeH()+":"+program.getMaxEndTimeM());
//		model.setPendingTimeDBEString(program.getPendingTimeDBEH()+":"+program.getPendingTimeDBEM());
//        if (program.getAutoRepeatTimeOfDay() != null){
//            model.setAutoRepeatTimeOfDayStr(program.getAutoRepeatTimeOfDay().toString());
//        }else{
//            model.setAutoRepeatTimeOfDayStr("");
//        }
//	}

	/**
	 * Function for build ProgramConfigureDataModel instance
	 * @param model
	 * @return
	 */
	public Program buildData(ProgramConfigureDataModel model,boolean addFlag){
		Program program;
		if(addFlag){
			program = model.getProgram();
			program.setState(Program.PROGRAM_ACTIVE);
		}else{
			//Retrieve program entity which will be build from database
			program = getProgram(model);
		}

		if(program!=null&&model!=null&&(!model.getProgram().getProgramName().equalsIgnoreCase(""))){
			String selectedProgramTemplate = model.getSelectedProgramTemplate();
			String currentProgramSechdulePage = model.getCurrentProgramSechdulePage();
			String currentProgramValidationClass =model.getCurrentProgramValidationClass();
//			String className = program.getClassName();
			String className = selectedProgramTemplate;
			//Retrieve bid info if program base on DBP/DBP no bid and build program entity
			if(className.equalsIgnoreCase(className_DBPProgramEJB)||className.equalsIgnoreCase(className_DBPBidProgramEJB)||className.equalsIgnoreCase(className_DBPNoBidProgramEJB)){
//				program = buildBid(model,program);
			}
			//Retrieve program basic info and build program entity
			program = buildProgram(model,program);
			program.setClassName(selectedProgramTemplate);
			program.setUiScheduleEventString(currentProgramSechdulePage);
			program.setValidatorClass(currentProgramValidationClass);
           
            if (AkuacomJSFUtil.getProgram().getRules() != null){

             program.setRules(AkuacomJSFUtil.getProgram().getRules());

            }
            
			//Retrieve program signal info and build program entity
			program = buildSignal(model,program);

			//Retrieve program mode info and build program entity
            //program = buildMode(model,program);
		}


		return program;
	}

	/**
	 * function for retrieve program entity from database using  ProgramConfigureDataModel instance
	 * @param model ProgramConfigureDataModel instance
	 * @return program instance
	 */
	public Program getProgram(ProgramConfigureDataModel model){
		if(model!=null&&(!model.getProgramName().equalsIgnoreCase(""))){
			Program program = programManager.findProgramPerfByProgramName(model.getProgramName().trim());
			return program;
		}else if(model!=null&&(!model.getProgram().getProgramName().equalsIgnoreCase(""))){
			Program program = programManager.findProgramPerfByProgramName(model.getProgram().getProgramName().trim());
			return program;
		}else{
			return null;
		}
	}

	/**
	 * function for parse String value for time, get the hour string value
	 * @param timeString	String value for time
	 * @return hour string value
	 */
	public String getHourFromModelTimeString(String timeString){
		if(timeString.contains(":")){
			String[] time =timeString.split(":");
			return time[0];
		}else{
			return "0";
		}
	}

	/**
	 * function for parse String value for time, get the minute string value
	 * @param timeString	String value for time
	 * @return minute string value
	 */
	public String getMinFromModelTimeString(String timeString){
		if(timeString.contains(":")){
			String[] time =timeString.split(":");
			if(time.length>1){
				return time[1];
			}
		}
		return "0";
	}

    public String getSecFromModelTimeString(String timeString){
		if(timeString.contains(":")){
			String[] time =timeString.split(":");
			if(time.length>2){
				return time[2];
			}
		}
		return "0";
	}


    public Date getDateFromModelTimeString(String timeString){
        Date d = new Date();
        if (timeString.isEmpty()) return null;

		if(timeString.contains(":")){
			String[] time =timeString.split(":");
			if(time.length>1){
				d.setHours(Integer.valueOf(time[0]));
                d.setMinutes(Integer.valueOf(time[1]));
                //if (time[2] != null)
                   // d.setSeconds(Integer.valueOf(time[2]));
			}
		}

		return d;
	}
	/**
	 * Private function for validate ProgramConfigureDataModel data
	 * This function is the first step before save program entity
	 * @param model ProgramConfigureDataModel instance
	 * @param addFlag add or update operation flag, true is add , false is update
	 */
	private void validateData(ProgramConfigureDataModel model,boolean addFlag){
		//validate program
		validateProgram(model,addFlag);
		//validate signal
		validateSignal(model,addFlag);
		//validate bid
		validateBid(model,addFlag);
		//validate mode
		validateMode(model,addFlag);
	}
	/**
	 * Private function for validate signal
	 * @param model ProgramConfigureDataModel instance
	 */
	private void validateSignal(ProgramConfigureDataModel model,boolean addFlag){
		if(model!=null){
			SignalConfigureValidator.signalValidation(model.getSignalConfigureDataModel());
		}
	}

	/**
	 * Private function for validate bid
	 * @param model ProgramConfigureDataModel instance
	 */
	private void validateBid(ProgramConfigureDataModel model,boolean addFlag){
		if(model!=null){
			BidConfigureValidator.bidValidation(model.getBidConfigureDataModel());
		}
	}

	/**
	 * Private function for validate mode
	 * @param model ProgramConfigureDataModel instance
	 */
	private void validateMode(ProgramConfigureDataModel model,boolean addFlag){
		if(model!=null){
			ModeConfigureValidator.modeValidation(model.getModeConfigureDataModel());
		}
	}

	/**
	 * Private function for validate program
	 * @param model
	 */
	private void validateProgram(ProgramConfigureDataModel model,boolean addFlag){
		ProgramConfigureValidator.programValidation(model,addFlag,this);
		ProgramConfigureValidator.programPriorityValidation(model,addFlag,programManager);
		ProgramConfigureValidator.maxEndTimeStringValidation(model, this);
		ProgramConfigureValidator.maxIssueTimeStringValidation(model,this);
		ProgramConfigureValidator.maxStartTimeStringValidation(model,this);
		ProgramConfigureValidator.minEndTimeStringValidation(model,this);
		ProgramConfigureValidator.minStartTimeStringValidation(model,this);
		ProgramConfigureValidator.pendingTimeDBEStringValidation(model, this);
        ProgramConfigureValidator.autoRepeatTimeOfDayStringValidation(model, this);
	}

	/**
	 * Private function construct program entity
	 * @param model	ProgramConfigureDataModel instance
	 * @param program Program instance
	 * @return	Program instance
	 */
	private Program buildProgram(ProgramConfigureDataModel model,Program program){

//		program.setLongProgramName(model.getProgram().getLongProgramName());
//		program.setProgramClass(model.getProgram().getProgramClass());
        if (this.getDateFromModelTimeString(model.getAutoRepeatTimeOfDayStr()) != null){
             program.setAutoRepeatTimeOfDay(this.getDateFromModelTimeString(model.getAutoRepeatTimeOfDayStr()));
        }else{
             program.setAutoRepeatTimeOfDay(null);
        }
		program.setProgramName(model.getProgram().getProgramName().trim());
		program.setClassName(model.getProgram().getClassName());
		program.setUtilityProgramName(model.getProgram().getUtilityProgramName());
		program.setSecondaryUtilityName(model.getProgram().getSecondaryUtilityName());
		program.setPriority(model.getProgram().getPriority());
		program.setValidatorClass(model.getProgram().getValidatorClass());

		if(program.getValidatorConfigFile()==""){
			program.setValidatorConfigFile("");
		}
		program.setUiScheduleEventString(model.getProgram().getUiScheduleEventString());
		program.setUiConfigureProgramString("");
		// Intention this attribute is transient and it can be not fetch into database or get from database
//		program.setOperatorEMails(model.getProgram().getOperatorEMails());

		program.setMinIssueToStartM(model.getProgram().getMinIssueToStartM());
		program.setMustIssueBDBE(model.getProgram().isMustIssueBDBE());

		program.setMaxIssueTimeH(Integer.parseInt(getHourFromModelTimeString(model.getMaxIssueTimeString())));
		program.setMaxIssueTimeM(Integer.parseInt(getMinFromModelTimeString(model.getMaxIssueTimeString())));

		program.setMinStartTimeH(Integer.parseInt(getHourFromModelTimeString(model.getMinStartTimeString())));
		program.setMinStartTimeM(Integer.parseInt(getMinFromModelTimeString(model.getMinStartTimeString())));

		program.setMaxStartTimeH(Integer.parseInt(getHourFromModelTimeString(model.getMaxStartTimeString())));
		program.setMaxStartTimeM(Integer.parseInt(getMinFromModelTimeString(model.getMaxStartTimeString())));

		program.setMinEndTimeH(Integer.parseInt(getHourFromModelTimeString(model.getMinEndTimeString())));
		program.setMinEndTimeM(Integer.parseInt(getMinFromModelTimeString(model.getMinEndTimeString())));

		program.setMaxEndTimeH(Integer.parseInt(getHourFromModelTimeString(model.getMaxEndTimeString())));
		program.setMaxEndTimeM(Integer.parseInt(getMinFromModelTimeString(model.getMaxEndTimeString())));

		program.setPendingTimeDBEH(Integer.parseInt(getHourFromModelTimeString(model.getPendingTimeDBEString())));
		program.setPendingTimeDBEM(Integer.parseInt(getMinFromModelTimeString(model.getPendingTimeDBEString())));

		program.setMinDurationM(model.getProgram().getMinDurationM());
		program.setMaxDurationM(model.getProgram().getMaxDurationM());
		program.setNotificationParam1(model.getProgram().getNotificationParam1());

		program.setManualCreatable(model.getProgram().isManualCreatable());
		
		program.setDefaultDuration(model.getProgram().getDefaultDuration());

		return program;
	}

	/**
	 * Private function for construct bid
	 * @param model ProgramConfigureDataModel instance
	 * @param program Program instance
	 * @return	Program instance
	 */
	private Program buildBid(ProgramConfigureDataModel model,Program program){
		if(model!=null&&program!=null){
			AkuaBidConfig bc = model.getBidConfigureDataModel().getAkuaBidConfig();

			program.getBidConfig().setAcceptTimeoutPeriodM(bc.getAcceptTimeoutPeriodM());
			program.getBidConfig().setDefaultBidKW(bc.getDefaultBidKW());
			program.getBidConfig().setDrasBidding(bc.isDrasBidding());
			program.getBidConfig().setDrasRespondByPeriodM(bc.getDrasRespondByPeriodM());
			program.getBidConfig().setMinBidKW(bc.getMinBidKW());
			program.getBidConfig().setMinConsectutiveBlocks(bc.getMinConsectutiveBlocks());

			program.getBidConfig().setRespondByTimeH(Integer.parseInt(getHourFromModelTimeString(model.getBidConfigureDataModel().getRespondByTimeString())));
			program.getBidConfig().setRespondByTimeM(Integer.parseInt(getMinFromModelTimeString(model.getBidConfigureDataModel().getRespondByTimeString())));

			Set<com.akuacom.pss2.program.bidding.BidBlock> bidBlocks = new HashSet<com.akuacom.pss2.program.bidding.BidBlock>();

			List<SelectItem> bidBlocksSelectItems = model.getBidConfigureDataModel().getBidBlocksSelectItems();

			for(int i=0;i<bidBlocksSelectItems.size();i++){
				SelectItem item = bidBlocksSelectItems.get(i);

				com.akuacom.pss2.program.bidding.BidBlock tb = new com.akuacom.pss2.program.bidding.BidBlock();

				String startTimeString =model.getBidConfigureDataModel().getStartTimeString(item);
				String endTimeString =model.getBidConfigureDataModel().getEndTimeString(item);

				tb.setEndTimeH(Integer.parseInt(getHourFromModelTimeString(startTimeString)));
				tb.setEndTimeM(Integer.parseInt(getMinFromModelTimeString(startTimeString)));
				tb.setStartTimeH(Integer.parseInt(getHourFromModelTimeString(endTimeString)));
				tb.setStartTimeM(Integer.parseInt(getMinFromModelTimeString(endTimeString)));

                bidBlocks.add(tb);
			}

			program.getBidConfig().setBidBlocks(bidBlocks);
		}

		return program;
	}

	/**
	 * Private function for construct signal
	 * @param model	ProgramConfigureDataModel instance
	 * @param program Program instance
	 * @return	Program instance
	 */
	@SuppressWarnings("unused")
	@Deprecated
	private Program buildSignalOld(ProgramConfigureDataModel model,Program program){
		if(model!=null&&program!=null){
			List<SelectItem> programSignalSelectItems = model.getSignalConfigureDataModel().getProgramSignalSelectItems();

			SignalManager signalManager = EJB3Factory.getBean(SignalManager.class);

	        Set<ProgramSignal> signals = program.getSignals();
	        Set<ProgramSignal> set = new HashSet<ProgramSignal>();

	        List<String> signalStrings = new ArrayList<String>();

	        for(int i =0;i<programSignalSelectItems.size();i++){
	        	String signalString = programSignalSelectItems.get(i).getLabel();
	        	signalStrings.add(signalString);
	        }
	        for (String signalString : signalStrings) {
	            final SignalDef signalDef = signalManager.getSignal(signalString);
	            ProgramSignal s = null;
	            if(signals != null) {
	            	for (ProgramSignal signal : signals) {
		                if (signal.getSignalDef().equals(signalDef)) {
		                    s = signal;
		                    break;
		                }
		            }
		        }
	            if (s == null) {
	                s = new ProgramSignal();
	                s.setSignalDef(signalDef);
	                s.setProgram(program);
	            }
	            set.add(s);
	        }

	        program.setSignals(set);
		}
		return program;
	}

	/**
	 * Private function for construct signal
	 * @param model	ProgramConfigureDataModel instance
	 * @param program Program instance
	 * @return	Program instance
	 */
	private Program buildSignal(ProgramConfigureDataModel model,Program program){
		if(model!=null&&program!=null){
			List<SignalDefDataModel> signalDefDataModelList = model.getSignalConfigureDataModel().getSignalDefDataModelList();

	        Set<ProgramSignal> set = new HashSet<ProgramSignal>();

	        List<String> signalStrings = new ArrayList<String>();

	        for(int i =0;i<signalDefDataModelList.size();i++){
	        	if(signalDefDataModelList.get(i).isSelect()){
	        		String signalString = signalDefDataModelList.get(i).getSignalDef().getSignalName();
		        	signalStrings.add(signalString);
	        	}
	        }
            set = updateSignals(program, signalStrings);
            program.setSignals(set);
           
		}
		return program;
	}

	/**
	 * Private function for update signals
	 * @param program
	 * @param signalStrings
	 * @return
	 */
	private static Set<ProgramSignal> updateSignals(Program program, List<String> signalStrings) {
        SignalManager signalManager = EJB3Factory.getLocalBean(SignalManager.class);
        Set<ProgramSignal> set = new HashSet<ProgramSignal>();
        List<SignalDef> signals = signalManager.getSignals(signalStrings);
        for (SignalDef signalDef : signals) {
            ProgramSignal s = null;
            if (s == null) {
                s = new ProgramSignal();
                s.setSignalDef(signalDef);
                s.setProgram(program);
            }
            set.add(s);
        }
        return set;
    }

	/**
	 * Private function for construct mode
	 * @param model	ProgramConfigureDataModel instance
	 * @param program	Program instance
	 * @return	Program instance
	 */
	private Program buildMode(ProgramConfigureDataModel model,Program program){
		return program;
	}

	/**
	 * Private function for save program entity
	 * @param program	Program instance
	 */
	private void updateData(Program program){
		programManager.updateProgram(program);
	}
	private void addData(Program program) throws Exception{
		programManager.createProgram(program);
	}
	@Override
	public void saveMainProgram(ProgramConfigureDataModel model)
			throws Exception {//currently only works for update
		try{
		
			//validate data
			validateProgram(model,false);
			//construct the program entity with valid data
			Program program = buildProgramPerf(model);
			if(program!=null){
				//save program entity into database
				updateData(program);
			}
		}catch(Exception e){
			//Add error message into JSF FacesMessage object
//			AkuacomJSFUtil.addErrorMessage(e.getMessage());
			//Throw exception to where it should be handle
			throw e;
		}
		
	}
	@Override
	public Program buildProgramPerf(ProgramConfigureDataModel model) {
		Program program;
		program = getProgram(model);//program with contacts

		if(program!=null&&model!=null&&(!model.getProgram().getProgramName().equalsIgnoreCase(""))){
			String selectedProgramTemplate = model.getSelectedProgramTemplate();
			String currentProgramSechdulePage = model.getCurrentProgramSechdulePage();
			String currentProgramValidationClass =model.getCurrentProgramValidationClass();
			//Retrieve program basic info and build program entity
			program = buildProgram(model,program);
			program.setClassName(selectedProgramTemplate);
			program.setUiScheduleEventString(currentProgramSechdulePage);
			program.setValidatorClass(currentProgramValidationClass);
                      
		}

		return program;
	}
	@Override
	public void saveProgramSignalsConfigure(ProgramConfigureDataModel model)
			throws Exception {

		//validate signal
		validateSignal(model,false);
		//construct the program entity with valid data
		Program program;
		//Retrieve program entity which will be build from database
		program = getProgram(model);
		
		//Retrieve program signal info and build program entity
		program = buildSignal(model,program);
		if(program!=null){
			//save program entity into database
			updateData(program);
		}
		
	}




}
