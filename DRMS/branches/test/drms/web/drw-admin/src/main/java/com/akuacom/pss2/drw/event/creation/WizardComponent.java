package com.akuacom.pss2.drw.event.creation;

import java.util.HashMap;
import java.util.Map;

import com.akuacom.pss2.drw.admin.FDUtils;
import com.akuacom.pss2.drw.event.creation.CommonValidator.MSG;

public abstract class WizardComponent{
	//TODO: when need validate?
	
	public WizardComponent(){
		installValidators();
	}
	private Wizard wizard;
	private Map<String,CommonValidator> validators;
	
	abstract protected void installValidators();
	/** sub class to override to provide a different wizard for event creation **/
	abstract protected  Wizard createWizard();
	
	public String getActivePage() {
		return getWizard().getActivePage();
	}

	public void setActivePage(String activePage) {
		this.getWizard().setActivePage(activePage);
	}
	
	public void nextPage() {
		CommonValidator val = getValidators().get(getActivePage());
		if(val!=null){
			MSG msg=val.validate(this);
			report(msg);
			if(msg!=null && msg.type==CommonValidator.MSG_ERROR)
				return;
		}
		getWizard().nextPage();
	}
	
	public void goToPage(String page){
		CommonValidator val = getValidators().get(getActivePage());
		if(val!=null){
			MSG msg=val.validate(this);
			report(msg);
			if(msg!=null && msg.type==CommonValidator.MSG_ERROR)
				return;
		}
		getWizard().goToPage(page);
	}
	
	public void backPage() {
		getWizard().backPage();
	}
	
	
	public Wizard getWizard(){
		if(this.wizard==null)
			wizard = createWizard();
		return wizard;
	}
	
	public Map<String,CommonValidator> getValidators(){
		if(validators==null){
			validators = new HashMap<String,CommonValidator>();
		}
		return validators;
	}
	
	protected void registerValidator(String page,CommonValidator validator){
		getValidators().put(page, validator);
	}
	
	protected void report(MSG msg){
		if(msg!=null){
			switch(msg.type){
			case CommonValidator.MSG_ERROR:
				FDUtils.addMsgError(msg.body);
				break;
			case CommonValidator.MSG_INFO:
				FDUtils.addMsgInfo(msg.body);
				break;
			case CommonValidator.MSG_WARN:
				FDUtils.addMsgWarn(msg.body);
				break;
			}
		}
	}

}
