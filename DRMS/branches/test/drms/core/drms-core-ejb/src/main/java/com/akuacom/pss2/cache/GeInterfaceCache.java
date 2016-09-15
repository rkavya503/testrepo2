package com.akuacom.pss2.cache;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

public class GeInterfaceCache {

    private Cache cache;
    public static final String CACHE_ID = "cache_id";

    // singleton
    private static final GeInterfaceCache instance = new GeInterfaceCache();
    private GeInterfaceCache() {
        CacheManager manager = CacheManager.create();
        cache = manager.getCache("geInterfaceCache");
    }

    public static GeInterfaceCache getInstance() {
        return instance;
    }

    // getter, setter
    public void putRunningInterval(Integer interval) {
        cache.put(new Element(CACHE_ID, interval));
    }

    public Integer getRunningState() {
        Element element = cache.get(CACHE_ID);
        if (element != null) {
            return (Integer) element.getValue();
        } else {
            return 0;
        }
    }

    public void removeRunningState() {
        cache.remove(CACHE_ID);
    }
}
