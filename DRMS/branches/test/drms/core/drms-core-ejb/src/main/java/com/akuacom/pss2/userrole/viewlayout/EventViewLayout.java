package com.akuacom.pss2.userrole.viewlayout;

public interface EventViewLayout extends ViewLayout {
	public boolean getCanDeleteEvent();
    public void setCanDeleteEvent(boolean value);
    public boolean getCanOptOutOfEvent();
    public void setCanOptOutOfEvent(boolean value);
    public boolean getCanDeleteDemoEvent() ;
	public void setCanDeleteDemoEvent(boolean value);


}
