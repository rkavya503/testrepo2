// $Revision: 1.1 $ $Date: 2000/01/01 00:00:00 $
package com.akuacom.pss2.facdash;

import javax.faces.event.ActionEvent;

import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.event.EventManager;

public class Status
{
	/** The program name. */
	private String eventName;

	/**
	 * Adding event opt-out listener.
	 * 
	 * @param e the e
	 */
	public void optoutListener(ActionEvent e)
	{
		eventName =
			e.getComponent().getAttributes().get("eventName").toString();
	}

	/**
	 * opt-out action.
	 * 
	 * @return the string
	 */
	public String optoutAction()
	{
		EventManager em = EJBFactory.getBean(EventManager.class);
		em.removeParticipantFromEvent(eventName, FDUtils.getParticipantName());

		return "optout";
	}
}
