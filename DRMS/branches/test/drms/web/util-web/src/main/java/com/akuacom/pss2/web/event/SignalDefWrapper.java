package com.akuacom.pss2.web.event;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.faces.model.SelectItem;

import com.akuacom.pss2.signal.SignalDef;
import com.akuacom.pss2.signal.SignalLevelDef;

public class SignalDefWrapper implements Serializable {

	SignalDefWrapper(SignalDef signalDef){
		this.signalDef=signalDef;
	}
	private static final long serialVersionUID = 6738922647465493814L;
	
	private List<SelectItem> signalLevelsSelectItem;
    private List<SelectItem> signalsLevelsSelectItem;
	private double selectValue;
	private SignalDef signalDef;
	private String signalDefName;
	public void setSignalLevelsSelectItem(List<SelectItem> signalLevelsSelectItem) {
		this.signalLevelsSelectItem = signalLevelsSelectItem;
	}

    public void setSignalsLevelsSelectItem(List<SelectItem> signalsLevelsSelectItem) {
        this.signalsLevelsSelectItem = signalsLevelsSelectItem;
    }

	public List<SelectItem> getSignalLevelsSelectItem() {
		List<SelectItem> availableTypes = new ArrayList<SelectItem>();
		if(signalDef==null){
			signalLevelsSelectItem=availableTypes;
			return signalLevelsSelectItem;
		}	
		Set<SignalLevelDef> signalLevels = signalDef.getSignalLevels();
		Iterator<SignalLevelDef> i =signalLevels.iterator();
		int index=0;
		while(i.hasNext()){
			SignalLevelDef signalLevelDef =i.next();
			String levelName = signalLevelDef.getStringValue();
			availableTypes.add(new SelectItem(index,levelName));
			index++;
		}
		signalLevelsSelectItem=availableTypes;
		return signalLevelsSelectItem;
	}

    	public List<SelectItem> getSignalLevelsSelectItems() {
		List<SelectItem> availableTypes = new ArrayList<SelectItem>();
		if(signalDef==null){
			signalLevelsSelectItem=availableTypes;
			return signalLevelsSelectItem;
		}
		Set<SignalLevelDef> signalLevels = signalDef.getSignalLevels();
		Iterator<SignalLevelDef> i =signalLevels.iterator();
		int index=0;
		while(i.hasNext()){
			SignalLevelDef signalLevelDef =i.next();
			String levelName = signalLevelDef.getStringValue();

            if (index ==0)
                availableTypes.add(new SelectItem(index,"Moderate"));
            if (index ==1)
                availableTypes.add(new SelectItem(index,"High"));
            if (index ==2)
                availableTypes.add(new SelectItem(index,"Normal"));

			index++;
		}
		signalLevelsSelectItem=availableTypes;
		return signalLevelsSelectItem;
	}

       public List<SelectItem> getSignalsLevelsSelectItem() {

		List<SelectItem> availableTypes = new ArrayList<SelectItem>();
		if(signalDef==null){
			signalLevelsSelectItem=availableTypes;
			return signalLevelsSelectItem;
		}

		Set<SignalLevelDef> signalLevels = signalDef.getSignalLevels();
		Iterator<SignalLevelDef> i =signalLevels.iterator();
		int index=1;
		while(i.hasNext()){
			SignalLevelDef signalLevelDef =i.next();
			String levelName = signalLevelDef.getStringValue();

           if (index == 1){
                availableTypes.add(new SelectItem(3,"High"));
            }
            if (index ==3){
                availableTypes.add(new SelectItem(1,"Normal"));
            }
            if (index == 2){
                availableTypes.add(new SelectItem(2,"Moderate"));
            }
           
        
			index++;
		}
   
		signalLevelsSelectItem=availableTypes;
		return signalLevelsSelectItem;
	}
	
	public double getModeValue(){
		if(signalDef.getType().equalsIgnoreCase("LOAD_LEVEL")){
			for(SelectItem selectItem : signalLevelsSelectItem){
 				double value =Double.parseDouble(selectItem.getValue().toString());
				if(value==selectValue){
					String label = selectItem.getLabel();
					if(label.equalsIgnoreCase("Normal")){
						return 1.0;
					}if(label.equalsIgnoreCase("Moderate")){
						return 2.0;
					}if(label.equalsIgnoreCase("High")){
						return 3.0;
					}
				}
			}
		}
		return selectValue;
	}
	
	public void setSignalDef(SignalDef signalDef) {
		this.signalDef = signalDef;
	}

	public SignalDef getSignalDef() {
		
		return signalDef;
	}

	public void setSelectValue(double selectValue) {
		this.selectValue = selectValue;
	}

	public double getSelectValue() {
		return selectValue;
	}

	public void setSignalDefName(String signalDefName) {
		this.signalDefName = signalDefName;
	}

	public String getSignalDefName() {
		signalDefName=signalDef.getSignalName();
		return signalDefName;
	}

}
