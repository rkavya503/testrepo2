package com.akuacom.pss2.userrole.viewlayout;

public interface ParticipantViewLayout extends ViewLayout {
	
	public boolean isCanCreateParticipant();

	public void setCanCreateParticipant(boolean canCreateParticipant);

	public boolean isCanDeleteParticipant();

	public void setCanDeleteParticipant(boolean canDeleteParticipant);	
	
	public boolean isCanSearchPaticipants();

	public void setCanSearchPaticipants(boolean canSearchPaticipants);

	public boolean isCanExportParticipants();

	public void setCanExportParticipants(boolean canExportParticipants);

	public boolean isCanClearParticipants();

	public void setCanClearParticipants(boolean canClearParticipants);

	public boolean isCanDeleteChecked();

	public void setCanDeleteChecked(boolean canDeleteChecked);

	public boolean isAggregationEnabled();

	public void setAggregationEnabled(boolean aggregationEnabled);
	
	public boolean isEditParticipant();

	public void setEditParticipant(boolean editParticipant);

	public boolean isViewParticipant();

	public void setViewParticipant(boolean viewParticipant);
	
	public boolean isCanUserAddClient();

	public void setCanUserAddClient(boolean canUserAddClient);
	
	public boolean isSce();
}
