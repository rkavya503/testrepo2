package com.akuacom.pss2.drw.model.factory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.akuacom.pss2.drw.model.BaseEventDataModel;
import com.akuacom.pss2.drw.model.BaseEventDataModelCache;
import com.akuacom.pss2.drw.model.EventCache;
import com.akuacom.pss2.drw.model.KmlCache;
import com.akuacom.pss2.drw.util.DRWUtil;
import com.akuacom.pss2.drw.value.EventLegend;
import com.akuacom.pss2.drw.value.EventValue;

public class EventCacheFactory {
	public static final String KML_KEY="KML";
	
	public static KmlCache createKMLCache_PRODUCT_SDPR(){
		KmlCache cache = new KmlCache(){
			@Override
			protected void refreshValue() {
				Map kmlCache = new HashMap();
				List<String> apiKMl = DRWUtil.getCFEventManager().getKML4SDPR();
				kmlCache.put(KML_KEY, apiKMl);
				this.setValue(kmlCache);
			}
		};
		return cache;
	}
	public static KmlCache createKMLCache_PRODUCT_SDPC(){
		KmlCache cache = new KmlCache(){
			@Override
			protected void refreshValue() {
				Map kmlCache = new HashMap(); 
				
				List<String> apiKMl = DRWUtil.getCFEventManager().getKML4SDPC();
				kmlCache.put(KML_KEY, apiKMl);
				this.setValue(kmlCache);
			}
		};
		return cache;
	}
	public static KmlCache createKMLCache_PRODUCT_API(){
		KmlCache cache = new KmlCache(){
			@Override
			protected void refreshValue() {
				Map kmlCache = new HashMap(); 
				
				List<String> apiKMl = DRWUtil.getCFEventManager().getKML4API();
				kmlCache.put(KML_KEY, apiKMl);
				this.setValue(kmlCache);
			}
		};
		return cache;
	}
	public static KmlCache createKMLCache_PRODUCT_BIP(){
		KmlCache cache = new KmlCache(){
			@Override
			protected void refreshValue() {
				Map kmlCache = new HashMap(); 
				
				List<String> apiKMl = DRWUtil.getCFEventManager().getKML4BIP();
				kmlCache.put(KML_KEY, apiKMl);
				this.setValue(kmlCache);
			}
		};
		return cache;
	}
	public static KmlCache createKMLCache_PRODUCT_CBP(){
		KmlCache cache = new KmlCache(){
			@Override
			protected void refreshValue() {
				Map kmlCache = new HashMap(); 
				
				List<String> apiKMl = DRWUtil.getCFEventManager().getKML4CBP();
				kmlCache.put(KML_KEY, apiKMl);
				this.setValue(kmlCache);
			}
		};
		return cache;
	}
	public static KmlCache createKMLCache_ACTIVE_SDPR(){
		KmlCache cache = new KmlCache(){
			@Override
			protected void refreshValue() {
				List<EventLegend> legends = EventCache.getInstance().getActSDPResiEventLegends().getEventLegends();
				generateValue(legends);
			}
		};
		return cache;
	}
	public static KmlCache createKMLCache_ACTIVE_SDPC(){
		KmlCache cache = new KmlCache(){
			@Override
			protected void refreshValue() {
				List<EventLegend> legends = EventCache.getInstance().getActSDPComeEventLegends().getEventLegends();
				generateValue(legends);
			}
		};
		return cache;
	}
	public static KmlCache createKMLCache_ACTIVE_API(){
		KmlCache cache = new KmlCache(){
			@Override
			protected void refreshValue() {
				List<EventLegend> legends = EventCache.getInstance().getActAPIEventLegends().getEventLegends();
				generateValue(legends);
			}
		};
		return cache;
	}
	public static KmlCache createKMLCache_ACTIVE_BIP(){
		KmlCache cache = new KmlCache(){
			@Override
			protected void refreshValue() {
				List<EventLegend> legends = EventCache.getInstance().getActBIPEventLegends().getEventLegends();
				generateValue(legends);
			}
		};
		return cache;
	}
	public static KmlCache createKMLCache_ACTIVE_CBP(){
		KmlCache cache = new KmlCache(){
			@Override
			protected void refreshValue() {
				List<EventLegend> legends = EventCache.getInstance().getActCBPEventLegends().getEventLegends();
				generateValue(legends);
			}
		};
		cache.setConsolidationSLAP(true);
		return cache;
	}	

	public static KmlCache createKMLCache_SCHEDULED_SDPR(){
		KmlCache cache = new KmlCache(){
			@Override
			protected void refreshValue() {
				List<EventLegend> legends = EventCache.getInstance().getScheSDPResiEventLegends().getEventLegends();
				generateValue(legends);
			}
		};
		return cache;
	}
	public static KmlCache createKMLCache_SCHEDULED_SDPC(){
		KmlCache cache = new KmlCache(){
			@Override
			protected void refreshValue() {
				List<EventLegend> legends = EventCache.getInstance().getScheSDPComeEventLegends().getEventLegends();
				generateValue(legends);
			}
		};
		return cache;
	}
	public static KmlCache createKMLCache_SCHEDULED_API(){
		KmlCache cache = new KmlCache(){
			@Override
			protected void refreshValue() {
				List<EventLegend> legends = EventCache.getInstance().getScheAPIEventLegends().getEventLegends();
				generateValue(legends);
			}
		};
		return cache;
	}
	public static KmlCache createKMLCache_SCHEDULED_BIP(){
		KmlCache cache = new KmlCache(){
			@Override
			protected void refreshValue() {
				List<EventLegend> legends = EventCache.getInstance().getScheBIPEventLegends().getEventLegends();
				generateValue(legends);
			}
		};
		return cache;
	}
	public static KmlCache createKMLCache_SCHEDULED_CBP(){
		KmlCache cache = new KmlCache(){
			@Override
			protected void refreshValue() {
				List<EventLegend> legends = EventCache.getInstance().getScheCBPEventLegends().getEventLegends();
				generateValue(legends);
			}
		};
		cache.setConsolidationSLAP(true);
		return cache;
	}
	public static BaseEventDataModelCache createBaseEventCache_ACTIVE_SDPR(){
		BaseEventDataModelCache cache = new BaseEventDataModelCache(){
			@Override
			protected void refreshValue() {
				List<EventLegend> legends = EventCache.getInstance().getActSDPResiEventLegends().getEventLegends();
				generateValue(legends);
			}
		};
		return cache;
	}
	public static BaseEventDataModelCache createBaseEventCache_ACTIVE_SDPC(){
		BaseEventDataModelCache cache = new BaseEventDataModelCache(){
			@Override
			protected void refreshValue() {
				List<EventLegend> legends = EventCache.getInstance().getActSDPComeEventLegends().getEventLegends();
				generateValue(legends);
			}
		};
		return cache;
	}
	public static BaseEventDataModelCache createBaseEventCache_ACTIVE_BIP(){
		BaseEventDataModelCache cache = new BaseEventDataModelCache(){
			@Override
			protected void refreshValue() {
				List<EventLegend> legends = EventCache.getInstance().getActBIPEventLegends().getEventLegends();
				generateValue(legends);
			}
		};
		return cache;
	}
	public static BaseEventDataModelCache createBaseEventCache_ACTIVE_API(){
		BaseEventDataModelCache cache = new BaseEventDataModelCache(){
			@Override
			protected void refreshValue() {
				List<EventLegend> legends = EventCache.getInstance().getActAPIEventLegends().getEventLegends();
				generateValue(legends);
			}
		};
		return cache;
	}
	public static BaseEventDataModelCache createBaseEventCache_ACTIVE_CBP(){
		BaseEventDataModelCache cache = new BaseEventDataModelCache(){
			@Override
			protected void refreshValue() {
				List<EventLegend> legends = EventCache.getInstance().getActCBPEventLegends().getEventLegends();
//				generateValue(legends);
				Map<String,BaseEventDataModel> cache = new TreeMap<String,BaseEventDataModel>(); 
				for(EventLegend le: legends){
					String key = le.getEventKey();
					
					List<EventValue> result = DRWUtil.getDREvent2013Manager().getDrwCBPEvents(le.getEventlocations());
					if(result.size()>0){
						BaseEventDataModel model=new BaseEventDataModel(result.get(0));
						cache.put(key, model);
					}
				}
				this.setValue(cache);
			}
		};
		return cache;
	}
	public static BaseEventDataModelCache createBaseEventCache_SCHEDULED_SDPR(){
		BaseEventDataModelCache cache = new BaseEventDataModelCache(){
			@Override
			protected void refreshValue() {
				List<EventLegend> legends = EventCache.getInstance().getScheSDPResiEventLegends().getEventLegends();
				generateValue(legends);
			}
		};
		return cache;
	}
	public static BaseEventDataModelCache createBaseEventCache_SCHEDULED_SDPC(){
		BaseEventDataModelCache cache = new BaseEventDataModelCache(){
			@Override
			protected void refreshValue() {
				List<EventLegend> legends = EventCache.getInstance().getScheSDPComeEventLegends().getEventLegends();
				generateValue(legends);
			}
		};
		return cache;
	}
	public static BaseEventDataModelCache createBaseEventCache_SCHEDULED_BIP(){
		BaseEventDataModelCache cache = new BaseEventDataModelCache(){
			@Override
			protected void refreshValue() {
				List<EventLegend> legends = EventCache.getInstance().getScheBIPEventLegends().getEventLegends();
				generateValue(legends);
			}
		};
		return cache;
	}
	public static BaseEventDataModelCache createBaseEventCache_SCHEDULED_API(){
		BaseEventDataModelCache cache = new BaseEventDataModelCache(){
			@Override
			protected void refreshValue() {
				List<EventLegend> legends = EventCache.getInstance().getScheAPIEventLegends().getEventLegends();
				generateValue(legends);
			}
		};
		return cache;
	}
	public static BaseEventDataModelCache createBaseEventCache_SCHEDULED_CBP(){
		BaseEventDataModelCache cache = new BaseEventDataModelCache(){
			@Override
			protected void refreshValue() {
				List<EventLegend> legends = EventCache.getInstance().getScheCBPEventLegends().getEventLegends();
//				generateValue(legends);
				Map<String,BaseEventDataModel> cache = new TreeMap<String,BaseEventDataModel>(); 
				for(EventLegend le: legends){
					String key = le.getEventKey();
					
					List<EventValue> result = DRWUtil.getDREvent2013Manager().getDrwCBPEvents(le.getEventlocations());
					if(result.size()>0){
						BaseEventDataModel model=new BaseEventDataModel(result.get(0));
						cache.put(key, model);
					}
				}
				this.setValue(cache);
			}
		};
		return cache;
	}
}
