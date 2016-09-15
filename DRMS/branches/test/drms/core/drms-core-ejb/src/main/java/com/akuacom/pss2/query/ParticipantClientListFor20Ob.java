/**
 * 
 */
package com.akuacom.pss2.query;

/**
 * @author e835162
 *
 */
public class ParticipantClientListFor20Ob {
	
	private String parentName;
	private String parentAccount;
	private String parentApplicationID;
	private String clientName;
	public ParticipantClientListFor20Ob(){
		
	}
	public ParticipantClientListFor20Ob(String parentName,String parentAccount,String parentApplicationID,String clientName){
		this.parentName = parentName;
		this.parentAccount = parentAccount;
		this.parentApplicationID = parentApplicationID;
		this.clientName = clientName;
	}
	public String getParentName() {
		return parentName;
	}
	public void setParentName(String parentName) {
		this.parentName = parentName;
	}
	public String getParentAccount() {
		return parentAccount;
	}
	public void setParentAccount(String parentAccount) {
		this.parentAccount = parentAccount;
	}
	public String getParentApplicationID() {
		return parentApplicationID;
	}
	public void setParentApplicationID(String parentApplicationID) {
		this.parentApplicationID = parentApplicationID;
	}
	public String getClientName() {
		return clientName;
	}
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	

}
