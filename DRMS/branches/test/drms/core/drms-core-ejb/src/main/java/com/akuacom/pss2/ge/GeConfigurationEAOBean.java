package com.akuacom.pss2.ge;

import javax.ejb.Stateless;
import javax.persistence.Query;

@Stateless
public class GeConfigurationEAOBean  extends GeConfigurationGenEAOBean implements GeConfigurationEAO.L,GeConfigurationEAO.R {

	@Override
	public int updateFTPConfig(String programName, String url, int shortInterval,
			int longInterval) {
		Query q = em.createQuery("update GeConfiguration c set c.programName=:programName, c.url=:url, c.shortInterval=:shortInterval, c.longInterval=:longInterval where c.configName=:configName");
		q.setParameter("programName", programName);
		q.setParameter("url", url);
		q.setParameter("shortInterval", shortInterval);
		q.setParameter("longInterval", longInterval);
		q.setParameter("configName", "ge_conf");
		int updated=q.executeUpdate();
		
		return updated;
	}

}
