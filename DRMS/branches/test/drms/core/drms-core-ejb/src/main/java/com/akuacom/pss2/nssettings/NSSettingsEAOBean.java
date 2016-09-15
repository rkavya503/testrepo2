package com.akuacom.pss2.nssettings;

import java.util.List;

import javax.ejb.EJBException;
import javax.ejb.Stateless;

import com.akuacom.ejb.BaseEAOBean;
import com.akuacom.pss2.email.MessageDispatcher;

/**
 * Default implementation of NSSettingsManager
 * 
 * @author Li Fei
 * 
 *         Initial date Jun 01, 2010
 * @see MessageDispatcher for the manager of this.
 */
@Stateless
public class NSSettingsEAOBean extends BaseEAOBean<NSSettings> implements
        NSSettingsEAO.R, NSSettingsEAO.L {

    public NSSettingsEAOBean() {
        super(NSSettings.class);
    }

    @Override
    public NSSettings getNSSettings() {

        NSSettings nsSettingsEntity;

        try {
            List<NSSettings> all = getAll();
            if (all.isEmpty()) {
				// TODO move business logic to manager DRMS-2060 ... should this
				// be a default declared somewhere else?
				nsSettingsEntity = new NSSettings();
				nsSettingsEntity.setUUID("0");
				nsSettingsEntity.setFilterStatus(0);
				nsSettingsEntity.setMsgThreshold(0);
				nsSettingsEntity.setFrequency(1440);
				nsSettingsEntity.setDuration(1440);
				nsSettingsEntity.setMsgExpireTime(1440);
				nsSettingsEntity.setCleanMsgHour(0);
				nsSettingsEntity.setCleanMsgMinute(0);
            } else {
                nsSettingsEntity = all.get(0);
            }

        } catch (Exception e) {
            throw new EJBException("ERROR_NSSettings_GET", e);
        }
        return nsSettingsEntity;
    }

    @Override
    public void saveNSSettings(NSSettings nssettings) {
        super.set(nssettings);
    }

}
