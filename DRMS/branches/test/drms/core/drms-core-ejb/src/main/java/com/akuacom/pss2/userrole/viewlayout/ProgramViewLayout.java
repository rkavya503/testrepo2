package com.akuacom.pss2.userrole.viewlayout;

public interface ProgramViewLayout extends ViewLayout{
	public boolean isProgramEditEnabled() ;
	public void setProgramEditEnabled(boolean isProgramEditEnabled);

	public boolean isProgramSelectEnabled() ;
	public void setProgramSelectEnabled(boolean isProgramSelectEnabled);

	public boolean isProgramCloneEnabled() ;
	public void setProgramCloneEnabled(boolean isProgramCloneEnabled);

	public boolean isProgramAddEnabled();
	public void setProgramAddEnabled(boolean isProgramAddEnabled);

	public boolean isProgramRemEnabled() ;
	public void setProgramRemEnabled(boolean isProgramRemEnabled);

	public boolean isProgramActnEnabled();
	public void setProgramActnEnabled(boolean isProgramActnEnabled);
	   
	public boolean isProgramPropsEnabled();
	public void setProgramPropsEnabled(boolean isProgramActnEnabled);
	
	public boolean isProgramSignalEnabled();
	public void setProgramSignalEnabled(boolean isProgramSignalEnabled);
	
	public boolean isProgramSeasonEnabled();
	public void setProgramSeasonEnabled(boolean isProgramSeasonEnabled);
	
	public boolean isProgramRuleEnabled();
	public void setProgramRuleEnabled(boolean isProgramRuleEnabled);

	public boolean isProgramAddEventEnabled();
	public void setProgramAddEventEnabled(boolean isProgramAddEventEnabled);

	public boolean isProgramRtpEnabled();
	public void setProgramRtpEnabled(boolean isProgramRtpEnabled);
	
	public boolean isProgramExpEnabled();
	public void setProgramExpEnabled(boolean isProgramExpEnabled);
}
