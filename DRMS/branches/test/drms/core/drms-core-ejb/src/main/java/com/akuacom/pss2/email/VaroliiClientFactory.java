package com.akuacom.pss2.email;

import java.util.List;

import com.akuacom.ejb.client.EJB3Factory;
import com.akuacom.pss2.system.property.CoreProperty;
import com.akuacom.pss2.system.property.CorePropertyEAO;
import com.akuacom.pss2.system.property.PSS2Features;
import com.akuacom.pss2.system.property.PSS2Properties;

public class VaroliiClientFactory {
    public static MBOemClient getVaroliiClient() {
        // get utility name
        CorePropertyEAO corePropertyEAO = EJB3Factory.getBean(CorePropertyEAO.class);
        List<CoreProperty> coreProperties = corePropertyEAO.getAll();
        PSS2Properties properties = new PSS2Properties(coreProperties);
        String utilityName = properties.getUtilityName();

        // only returns non-null value if it's recognized clients
        MBOemClient client = null;
        if ("pge".equals(utilityName)) {
            client = new MBOemClient(); // client is defaulted to pge configuration.
        } else if ("heco".equals(utilityName)) {
            client = new MBOemClient();
            client.setDomain("heco");
            // if it's production instance, use prod config for hostname.
            PSS2Features features = new PSS2Features(coreProperties);
            Boolean productionServer = features.isProductionServer();
            if (productionServer) {
                VaroliiProperties varoliiProperties = VaroliiProperties.getInstance();
                client.setHostname(varoliiProperties.getHostName());
            }
        }
        return client;
    }
}
