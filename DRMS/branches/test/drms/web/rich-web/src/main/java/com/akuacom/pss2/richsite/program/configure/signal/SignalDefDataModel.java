package com.akuacom.pss2.richsite.program.configure.signal;

import java.io.Serializable;

import com.akuacom.pss2.signal.SignalDef;

public class SignalDefDataModel implements Serializable{

	private static final long serialVersionUID = -7079379412848195953L;

	public SignalDefDataModel(){
		super();
		select = true;
	}
	public SignalDefDataModel(SignalDef signalDef, Boolean select){
		super();
		this.signalDef = signalDef;
		this.select = select;

	}
	private SignalDef signalDef;
	private boolean select;
	
	public void setSignalDef(SignalDef signalDef) {
		this.signalDef = signalDef;
	}
	public SignalDef getSignalDef() {
		return signalDef;
	}
	public void setSelect(boolean select) {
		this.select = select;
	}
	public boolean isSelect() {
		return select;
	}
	
	
}
