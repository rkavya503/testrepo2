/**
 * 
 */
package com.akuacom.pss2.program.sceftp;

import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 */
@Stateless
public class SCEFTPConfigEAOBean extends SCEFTPConfigGenEAOBean implements SCEFTPConfigEAO.L, SCEFTPConfigEAO.R {

	/* (non-Javadoc)
	 * @see com.akuacom.pss2.program.sceftp.SCEFTPConfigEAO#updateFTPConfig(java.lang.String, int, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public int updateFTPConfig(String host, int port, String username,
			String password, String configName) {
		
		Query q = em.createQuery("update SCEFTPConfig c set c.host=:host, c.port=:port, c.username=:username, c.password=:password where c.configName<>:configName");
		q.setParameter("host", host);
		q.setParameter("port", port);
		q.setParameter("username", username);
		q.setParameter("password", password);
		q.setParameter("configName", configName);
		int updated=q.executeUpdate();
		
		return updated;
	}

}
