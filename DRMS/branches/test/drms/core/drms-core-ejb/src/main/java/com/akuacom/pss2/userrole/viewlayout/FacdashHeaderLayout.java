package com.akuacom.pss2.userrole.viewlayout;

public interface FacdashHeaderLayout extends ViewLayout{
	
	public void setEnableClientOptionTab(boolean enableClientOptionTab);
	public boolean isEnableClientOptionTab(); 
	
	public boolean isEnableClientTestTab() ;
	public void setEnableClientTestTab(boolean enableClientTestTab) ;
	
	public boolean isEnableCompleteInstallation();
	public void setEnableCompleteInstallation(boolean enableCompleteInstallation);
}
