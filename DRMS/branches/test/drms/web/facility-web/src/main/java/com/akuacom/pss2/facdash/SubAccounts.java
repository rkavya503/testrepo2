// $Revision$ $Date$
package com.akuacom.pss2.facdash;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJBException;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.core.ErrorUtil;
import com.akuacom.pss2.participant.ParticipantManager;
import com.akuacom.pss2.subaccount.SubAccount;


public class SubAccounts implements Serializable  {
    
	/** The accounts. */
	private List<JSFAccount> accounts = new ArrayList<JSFAccount>();

	/** The account name. */
	private String accountName;
	
	public SubAccounts()
	{
		ParticipantManager participantManager =
			EJBFactory.getBean(ParticipantManager.class);

		accounts = new ArrayList<JSFAccount>();
		List<SubAccount> subAccounts =
			participantManager.getSubAccounts(FDUtils.getParticipantName());
		for (SubAccount sceAccount : subAccounts)
		{
			accounts.add(new JSFAccount(sceAccount));
		}
	}

	public void update()
	{
		ParticipantManager participantManager =
			EJBFactory.getBean(ParticipantManager.class);

		// accounts
		List<SubAccount> sceAccounts = new ArrayList<SubAccount>();
		for (JSFAccount account : accounts)
		{
			sceAccounts.add(account.parseAccount());
		}
		participantManager.updateSubAccounts(sceAccounts, FDUtils
			.getParticipantName());
	}

	/**
	 * Edits the accounts action.
	 * 
	 * @return the string
	 */
	public String editAccountAction()
	{
		for (JSFAccount account : accounts)
		{
			if (account.getName().equals(accountName))
			{
				account.setEdit(true);
				FDUtils.setJSFAccount(account);
				break;
			}
		}
		return "editAccount";
	}

	/**
	 * Delete accounts action.
	 * 
	 * @return the string
	 */
	public String deleteAccountsAction()
	{
		Iterator<JSFAccount> i = accounts.iterator();
		while (i.hasNext())
		{
			JSFAccount account = i.next();
			if (account.isSelected())
			{
				i.remove();
			}
		}
		update();
		return "deleteAccounts";
	}

	/**
	 * Creates the account action.
	 * 
	 * @return the string
	 */
	public String newAccountAction()
	{
		FDUtils.setJSFAccount(new JSFAccount());
		return "newAccount";
	}

	private boolean validateDates()
	{
		if(FDUtils.getJSFAccount().getEnrollmentDate() == null &&
			FDUtils.getJSFAccount().getStartDate() != null)
		{
			FDUtils.addMsgError("Start Date can only be set if Enrollment Date is set");
			return false;
		}	
		
		if (FDUtils.getJSFAccount().getEnrollmentDate() != null) {
			Date end = com.akuacom.utils.lang.DateUtil.endOfDay(new Date());
			if (FDUtils.getJSFAccount().getEnrollmentDate().after(end)) {
				FDUtils.addMsgError("Enrollment Date can not be in the future.");
				return false;
			}
		}
		
		if(FDUtils.getJSFAccount().getEnrollmentDate() != null &&
			FDUtils.getJSFAccount().getStartDate() != null &&
			FDUtils.getJSFAccount().getStartDate().before(
			FDUtils.getJSFAccount().getEnrollmentDate()))
		{
			FDUtils.addMsgError("Start Date must be on or after Enrollment Date");
			return false;
		}	
		
		if(FDUtils.getJSFAccount().getStartDate() == null &&
			FDUtils.getJSFAccount().getDeactiveDate() != null)
		{
			FDUtils.addMsgError("Deactivate Date can only be set if Start Date is set");
			return false;
		}		
		if(FDUtils.getJSFAccount().getStartDate() != null &&
			FDUtils.getJSFAccount().getDeactiveDate() != null &&
			FDUtils.getJSFAccount().getDeactiveDate().before(
			FDUtils.getJSFAccount().getStartDate()))
		{
			FDUtils.addMsgError("Deactivate Date must be on or after Start Date");
			return false;
		}		
		return true;
	}
	
	/**
	 * Creates the account action.
	 * 
	 * @return the string
	 */
	public String createAccountAction()
	{
		if(!validateDates())
		{
			return null;
		}
		accounts.add(FDUtils.getJSFAccount());
		
		try {
			update();
		} catch (EJBException e) {
			FDUtils.addMsgError(ErrorUtil.getErrorMessage(e));	
			Iterator<JSFAccount> i = accounts.iterator();
			while (i.hasNext())
			{
				JSFAccount account = i.next();
				if (account == FDUtils.getJSFAccount())
				{
					i.remove();
				}
			}
			return null;
		}
		return "createAccount";
	}

	/**
	 * Update account action.
	 * 
	 * @return the string
	 */
	public String updateAccountAction()
	{
		if(!validateDates())
		{
			return null;
		}		
		update();
		return "updateAccount";
	}

	/**
	 * Account listen.
	 * 
	 * @param e
	 *            the e
	 */
	public void accountListen(ActionEvent e)
	{
		FacesContext ctx = FacesContext.getCurrentInstance();
		accountName =
			(String) ctx.getExternalContext().getRequestParameterMap().get(
				"accountName");
	}

	/**
	 * Cancel account action.
	 * 
	 * @return the string
	 */
	public String cancelAccountAction()
	{
		return "cancelAccount";
	}

	/**
	 * Gets the accounts.
	 * 
	 * @return the accounts
	 */
	public List<JSFAccount> getAccounts()
	{
		return accounts;
	}

	/**
	 * Sets the accounts.
	 * 
	 * @param accounts
	 *            the new accounts
	 */
	public void setAccounts(List<JSFAccount> accounts)
	{
		this.accounts = accounts;
	}
}
