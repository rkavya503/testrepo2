/**
 * 
 */
package com.akuacom.pss2.notifications.usecasetest;


/**
 * @author e333812
 *
 */
public class FindNotificationsUseCase extends AbstractNotificationUseCase {

    private String subject = null;
    
    public String getSubject() {
		return subject;
	}
    
    public void setSubject(String subject) {
		this.subject = subject;
	}

	public FindNotificationsUseCase() {
		this(null);
	}

	public FindNotificationsUseCase(String subject) {
		super();
		if (subject != null) {
			String utilityName = this.getSystemManager().getPss2Properties()
					.getUtilityDisplayName();
			this.subject = utilityName + " " + subject;
		} else {
			this.subject = subject;
		}
	}

	/* (non-Javadoc)
	 * @see com.akuacom.pss2.usecasetest.cases.AbstractUseCase#runCase()
	 */
	@Override
	public Object runCase() throws Exception {
		return getEmailManager().getMessageBySubject(subject);
	}

}
