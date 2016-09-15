package com.akuacom.pss2.drw.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import java.util.TreeMap;

import com.akuacom.pss2.drw.util.DRWUtil;
import com.akuacom.pss2.drw.value.EventLegend;
import com.akuacom.pss2.drw.value.EventValue;
public abstract class BaseEventDataModelCache implements Observer {
	
	Map<String,BaseEventDataModel> value;
	public Map<String,BaseEventDataModel> getValue() {
		return value;
	}

	public void setValue(Map<String,BaseEventDataModel> value) {
		this.value = value;
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		//act on the update
		refreshValue();
	}
	
	protected abstract void refreshValue();
	
	protected void generateValue(List<EventLegend> legends){
		Map<String,BaseEventDataModel> cache = new TreeMap<String,BaseEventDataModel>(); 
		for(EventLegend le: legends){
			String key = le.getEventKey();
			
			List<EventValue> result = DRWUtil.getDREvent2013Manager().getDrwEvents(le.getEventlocations());
			if(result.size()>0){
				BaseEventDataModel model=new BaseEventDataModel(result.get(0));
				cache.put(key, model);
			}
		}
		this.setValue(cache);
	}
	
	public List<BaseEventDataModel> getValueList(){
		List<BaseEventDataModel> result =new ArrayList<BaseEventDataModel>();
		if(value!=null){
			Iterator<String> i = value.keySet().iterator();
			while(i.hasNext()){
				String key =  i.next();
				BaseEventDataModel model = value.get(key);
				model.setKey(key);
				result.add(model);
			}
		}
		DRWUtil.sortEventModelList(result);
		return result;
	}
	public List<BaseEventDataModel> getValueList(Set keySet){
		List<BaseEventDataModel> result =new ArrayList<BaseEventDataModel>();
		if(value!=null){
			Iterator<String> i = keySet.iterator();
			while(i.hasNext()){
				Object key =  i.next();
				result.add(value.get(key));
			}
		}
		DRWUtil.sortEventModelList(result);
		return result;
	}
	
}
