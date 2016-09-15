package com.akuacom.pss2.nssettings;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;

@Stateless
public class NSSettingsManagerBean implements NSSettingsManager.R,
        NSSettingsManager.L {

    @EJB
    NSSettingsEAO.L nsSettingsEAO;

    @Override
    public NSSettings getNSSettings() {

        NSSettings nsSettings;

        try {
            nsSettings = nsSettingsEAO.getNSSettings();
        } catch (Exception e) {
            throw new EJBException("ERROR_NSSettingsEntity_GET", e);
        }
        return nsSettings;
    }

    @Override
    public void saveNSSettings(NSSettings nssettings) {

        try {
            nsSettingsEAO.saveNSSettings(nssettings);
        } catch (Exception e) {
            throw new EJBException("ERROR_NSSettingsEntity_SAVE", e);
        }

    }
}
