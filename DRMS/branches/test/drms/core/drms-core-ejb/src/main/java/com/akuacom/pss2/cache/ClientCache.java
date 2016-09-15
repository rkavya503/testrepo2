package com.akuacom.pss2.cache;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import com.akuacom.pss2.event.ClientConversationState;

public class ClientCache {

    private Cache cache;

    // singleton
    private static final ClientCache instance = new ClientCache();
    private ClientCache() {
        CacheManager manager = CacheManager.create();
        cache = manager.getCache("clientConversationStateCache");
    }

    public static ClientCache getInstance() {
        return instance;
    }

    // getter, setter
    public void putClientConversationState(Integer id, ClientConversationState clientConversationState) {
        cache.put(new Element(id, clientConversationState));
    }

    public ClientConversationState getClientConversationState(Integer id) {
        Element element = cache.get(id);
        if (element != null) {
            return (ClientConversationState) element.getValue();
        } else {
            return null;
        }
    }

    public void removeClientConversationState(Integer id) {
        cache.remove(id);
    }
}
