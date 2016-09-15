package com.akuacom.pss2.rtp.ftp;

import java.util.List;

import javax.ejb.EJBException;
import javax.ejb.Stateless;

import com.akuacom.pss2.rtp.ftp.RTPFTPConfiguration;

@Stateless
public class RTPFTPConfigurationEAOBean extends RTPFTPConfigurationGenEAOBean implements RTPFTPConfigurationEAO.L, RTPFTPConfigurationEAO.R {

	public RTPFTPConfigurationEAOBean() {
		super(RTPFTPConfiguration.class);
	}

	@Override
	public RTPFTPConfiguration getRTPFTPConfiguration() {
		RTPFTPConfiguration rtpFTPConfig = new RTPFTPConfiguration();
		try {

			List<RTPFTPConfiguration> all = findAll();
			
			if (!all.isEmpty()) {
				rtpFTPConfig = all.get(0);
			} 
		} 
		catch (Exception e) {
			throw new EJBException("ERROR_RTPFTPConfiguration_GET", e);
		}
		
		return rtpFTPConfig;
	}

	@Override
	public void saveRTPFTPConfiguration(RTPFTPConfiguration rtpFTPConfiguration) {
		super.set(rtpFTPConfiguration);
	}
}
