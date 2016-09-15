package com.akuacom.pss2.userrole.viewlayout;

public interface ClientViewLayout extends ViewLayout {

	public boolean isCanDeleteClient();

	public void setCanDeleteClient(boolean canDeleteClient);

	public boolean isCanSearchClient();

	public void setCanSearchClient(boolean canSearchClient);

	public boolean isCanExport();

	public void setCanExport(boolean canExport);

	public boolean isCanClearClient();

	public void setCanClearClient(boolean canClearClient);

	public boolean isCanExportContacts();

	public void setCanExportContacts(boolean canExportContacts);
	
	public boolean isSearchInResults();

	public void setSearchInResults(boolean searchInResults);
	
	public boolean isDeleteCheckBoxDisable();

	public void setDeleteCheckBoxDisable(boolean deleteCheckBoxDisable);

}
