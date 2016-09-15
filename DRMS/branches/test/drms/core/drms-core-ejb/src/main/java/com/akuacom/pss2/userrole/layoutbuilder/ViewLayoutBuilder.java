package com.akuacom.pss2.userrole.layoutbuilder;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.akuacom.pss2.userrole.viewlayout.ViewLayout;


public interface ViewLayoutBuilder {
	
	@Remote
	public interface R extends ViewLayoutBuilder {}

	@Local
	public interface L extends ViewLayoutBuilder {}
	
	public void build(ViewLayout layout);

}
