package com.akuacom.pss2.drw.jsf.event.factory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.akuacom.pss2.drw.jsf.event.listview.AbstractEventGroupListView;
import com.akuacom.pss2.drw.model.BaseEventDataModel;
import com.akuacom.pss2.drw.model.BaseEventDataModelCache;
import com.akuacom.pss2.drw.model.EventCache;
import com.akuacom.pss2.drw.model.KmlCache;
import com.akuacom.pss2.drw.model.ListViewEventDataModel;
import com.akuacom.pss2.drw.util.DRWUtil;
import com.akuacom.pss2.drw.value.EventLegend;

public class ListViewGroupEventFactory {
	public static final String KML_KEY="KML";
	

	public static AbstractEventGroupListView createListViewEventGroup_API(){
		AbstractEventGroupListView cache = new AbstractEventGroupListView(){
			@Override
			public List<BaseEventDataModel> retrieveEvents(){
				List<BaseEventDataModel> results = new ArrayList<BaseEventDataModel>();
				Set keySet = new TreeSet();
				if(getListView()!=null){
					if(getListView().getDisplayActiveFlag()){
						 keySet = getListView().getSelectedItems(new TreeSet(), getListView().getActiveEventLegend().getApiItems());
						 results = EventCache.getInstance().getActApiEvents().getValueList(keySet);
					}else{
						 keySet = getListView().getSelectedItems(new TreeSet(), getListView().getScheEventLegend().getApiItems());
						 results = EventCache.getInstance().getScheApiEvents().getValueList(keySet);
					}
				}
				results = this.getListView().filter(results,this.getListView().getLastSearchCounty(), this.getListView().getLastSearchCity(), this.getListView().getLastSearchZipCode());
				return results;
			}
			@Override
			public boolean isEventEmptyFlag() {
				List result = null;
				if(getListView()!=null){
					if(getListView().getDisplayActiveFlag()){
						result = getListView().getActiveEventLegend().getApiItems();
					}else{
						result = getListView().getScheEventLegend().getApiItems();
					}
				}
				if(result!=null&&result.size()>0){
					return false;
				}else{
					return true;
				}
			}
		};
		return cache;
	}
	
	public static AbstractEventGroupListView createListViewEventGroup_BIP(){
		AbstractEventGroupListView cache = new AbstractEventGroupListView(){
			@Override
			public List<BaseEventDataModel> retrieveEvents(){
				List<BaseEventDataModel> results = new ArrayList<BaseEventDataModel>();
				Set keySet = new TreeSet();
				if(getListView()!=null){
					if(getListView().getDisplayActiveFlag()){
						 keySet = getListView().getSelectedItems(new TreeSet(), getListView().getActiveEventLegend().getBipItems());
						 results = EventCache.getInstance().getActBipEvents().getValueList(keySet);
					}else{
						 keySet = getListView().getSelectedItems(new TreeSet(), getListView().getScheEventLegend().getBipItems());
						 results = EventCache.getInstance().getScheBipEvents().getValueList(keySet);
					}
				}
				results = this.getListView().filter(results,this.getListView().getLastSearchCounty(), this.getListView().getLastSearchCity(), this.getListView().getLastSearchZipCode());
				return results;
			}
			@Override
			public boolean isEventEmptyFlag() {
				List result = null;
				if(getListView()!=null){
					if(getListView().getDisplayActiveFlag()){
						result = getListView().getActiveEventLegend().getBipItems();
					}else{
						result = getListView().getScheEventLegend().getBipItems();
					}
				}
				if(result!=null&&result.size()>0){
					return false;
				}else{
					return true;
				}
			}
		};
		return cache;
	}
	
	public static AbstractEventGroupListView createListViewEventGroup_SDPR(){
		AbstractEventGroupListView cache = new AbstractEventGroupListView(){
			@Override
			public List<BaseEventDataModel> retrieveEvents(){
				List<BaseEventDataModel> results = new ArrayList<BaseEventDataModel>();
				Set keySet = new TreeSet();
				if(getListView()!=null){
					if(getListView().getDisplayActiveFlag()){
						 keySet = getListView().getSelectedItems(new TreeSet(), getListView().getActiveEventLegend().getSdprItems());
						 results = EventCache.getInstance().getActSdprEvents().getValueList(keySet);
					}else{
						 keySet = getListView().getSelectedItems(new TreeSet(), getListView().getScheEventLegend().getSdprItems());
						 results = EventCache.getInstance().getScheSdprEvents().getValueList(keySet);
					}
				}
				results = this.getListView().filter(results,this.getListView().getLastSearchCounty(), this.getListView().getLastSearchCity(), this.getListView().getLastSearchZipCode());
				return results;
			}
			@Override
			public boolean isEventEmptyFlag() {
				List result = null;
				if(getListView()!=null){
					if(getListView().getDisplayActiveFlag()){
						result = getListView().getActiveEventLegend().getSdprItems();
					}else{
						result = getListView().getScheEventLegend().getSdprItems();
					}
				}
				if(result!=null&&result.size()>0){
					return false;
				}else{
					return true;
				}
			}
		};
		return cache;
	}
	
	public static AbstractEventGroupListView createListViewEventGroup_SDPC(){
		AbstractEventGroupListView cache = new AbstractEventGroupListView(){
			@Override
			public List<BaseEventDataModel> retrieveEvents(){
				List<BaseEventDataModel> results = new ArrayList<BaseEventDataModel>();
				Set keySet = new TreeSet();
				if(getListView()!=null){
					if(getListView().getDisplayActiveFlag()){
						 keySet = getListView().getSelectedItems(new TreeSet(), getListView().getActiveEventLegend().getSdpcItems());
						 results = EventCache.getInstance().getActSdpcEvents().getValueList(keySet);
					}else{
						 keySet = getListView().getSelectedItems(new TreeSet(), getListView().getScheEventLegend().getSdpcItems());
						 results = EventCache.getInstance().getScheSdpcEvents().getValueList(keySet);
					}
				}
				results = this.getListView().filter(results,this.getListView().getLastSearchCounty(), this.getListView().getLastSearchCity(), this.getListView().getLastSearchZipCode());
				return results;
			}
			@Override
			public boolean isEventEmptyFlag() {
				List result = null;
				if(getListView()!=null){
					if(getListView().getDisplayActiveFlag()){
						result = getListView().getActiveEventLegend().getSdpcItems();
					}else{
						result = getListView().getScheEventLegend().getSdpcItems();
					}
				}
				if(result!=null&&result.size()>0){
					return false;
				}else{
					return true;
				}
			}
		};
		return cache;
	}
	
	public static AbstractEventGroupListView createListViewEventGroup_CBP(){
		AbstractEventGroupListView cache = new AbstractEventGroupListView(){
			@Override
			public List<BaseEventDataModel> retrieveEvents(){
				List<BaseEventDataModel> results = new ArrayList<BaseEventDataModel>();
				Set keySet = new TreeSet();
				if(getListView()!=null){
					if(getListView().getDisplayActiveFlag()){
						 keySet = getListView().getSelectedItems(new TreeSet(), getListView().getActiveEventLegend().getCbpItems());
						 results = EventCache.getInstance().getActCbpEvents().getValueList(keySet);
					}else{
						 keySet = getListView().getSelectedItems(new TreeSet(), getListView().getScheEventLegend().getCbpItems());
						 results = EventCache.getInstance().getScheCbpEvents().getValueList(keySet);
					}
				}
				results = this.getListView().filter(results,this.getListView().getLastSearchCounty(), this.getListView().getLastSearchCity(), this.getListView().getLastSearchZipCode());
				return results;
			}
			@Override
			public boolean isEventEmptyFlag() {
				List result = null;
				if(getListView()!=null){
					if(getListView().getDisplayActiveFlag()){
						result = getListView().getActiveEventLegend().getCbpItems();
					}else{
						result = getListView().getScheEventLegend().getCbpItems();
					}
				}
				if(result!=null&&result.size()>0){
					return false;
				}else{
					return true;
				}
			}
		};
		return cache;
	}
}
