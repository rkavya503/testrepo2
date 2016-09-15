package com.akuacom.pss2.userrole.viewlayout;

public interface ClientRuleViewLayout extends ViewLayout {

	public boolean isDeleteCheckboxEnable();

	public void setDeleteCheckboxEnable(boolean deleteCheckboxEnable);

	public boolean isCanAddRule();

	public void setCanAddRule(boolean canAddRule);

	public boolean isCanSaveRule();

	public void setCanSaveRule(boolean canSaveRule);

	public boolean isCanDeleteRule();

	public void setCanDeleteRule(boolean canDeleteRule);

	public boolean isCanGoUpRule();

	public void setCanGoUpRule(boolean canGoUpRule);

	public boolean isCanGoDownRule();

	public void setCanGoDownRule(boolean canGoDownrule);
	
}
