package com.akuacom.pss2.drw;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.adapters.XmlAdapter;

class MapAdapter extends XmlAdapter<MapElements[], Map<String, Long>> {
    public MapElements[] marshal(Map<String, Long> arg0) throws Exception {
        MapElements[] mapElements = new MapElements[arg0.size()];
        int i = 0;
        for (Map.Entry<String, Long> entry : arg0.entrySet())
            mapElements[i++] = new MapElements(entry.getKey(), entry.getValue());

        return mapElements;
    }

    public Map<String, Long> unmarshal(MapElements[] arg0) throws Exception {
        Map<String, Long> r = new HashMap<String, Long>();
        for (MapElements mapelement : arg0)
            r.put(mapelement.key, mapelement.value);
        return r;
    }
}