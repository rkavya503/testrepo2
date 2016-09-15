package com.akuacom.pss2.participant.contact;

public enum ContactEventNotificationType {
	FullNotification("All Notifications"),
	NotNormalNotification("Strategy Initiated Notifications"),
	NoNotification("No Notifications");
	
	private final String description;

	ContactEventNotificationType(String desc)
	{
		this.description = desc;
	}

	public String description()
	{
		return this.description;
	}
}
