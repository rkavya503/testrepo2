package com.akuacom.pss2.richsite.option;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.akuacom.ejb.client.EJB3Factory;
import com.akuacom.pss2.contact.Contact;
import com.akuacom.pss2.contact.ContactManager;
import com.akuacom.pss2.util.DrasRole;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import com.akuacom.pss2.userrole.ViewBuilderManager;
import com.akuacom.pss2.userrole.viewlayout.EventViewLayout;
import com.akuacom.pss2.userrole.viewlayout.JSFContactLayout;

public class JSFContactBackingBean implements Serializable, JSFContactLayout {

	private static final long serialVersionUID = 8155573046623766786L;
	
	private boolean userAuthorized = false;
	
	private List<Contact> contacts = new ArrayList<Contact>();
	private static ContactManager contactManager = EJB3Factory.getBean(ContactManager.class);
	public JSFContactBackingBean(){
		retrieveData();
		buildViewLayout();
	}
	
	private void retrieveData(){
		if(contactManager!=null){
			contacts = contactManager.getOperatorContacts();
		}
	}
	public void updateContactAction(){
		contactManager.updateCoreContacts(contacts);
		retrieveData();
	}

	/**
	 * @return the contacts
	 */
	public List<Contact> getContacts() {
		return contacts;
	}

	/**
	 * @param contacts the contacts to set
	 */
	public void setContacts(List<Contact> contacts) {
		this.contacts = contacts;
	}

	/**
	 * @return the contactManager
	 */
	public static ContactManager getContactManager() {
		return contactManager;
	}

	/**
	 * @param contactManager the contactManager to set
	 */
	public static void setContactManager(ContactManager contactManager) {
		JSFContactBackingBean.contactManager = contactManager;
	}
	
	public boolean isUserAuthorized() {
		return userAuthorized;
	}

	public void setUserAuthorized(boolean userAuthorized) {
		this.userAuthorized = userAuthorized;
	}
	
	private void buildViewLayout(){

		try 
		{
			getViewBuilderManager().buildJSFContactLayout(this);

		} catch (NamingException e) 
		{
			// log exception
		}

	}

	private ViewBuilderManager getViewBuilderManager() throws NamingException{

		return (ViewBuilderManager) new InitialContext().lookup("pss2/ViewBuilderManagerBean/local");

	}
}