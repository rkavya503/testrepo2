package com.akuacom.pss2.userrole;

import javax.ejb.Local;
import javax.ejb.Remote;
import com.akuacom.pss2.userrole.layoutbuilder.ViewLayoutBuilder;

public interface ViewLayoutBuilderFactory {
	
	@Remote
	public interface R extends ViewLayoutBuilderFactory {}

	@Local
	public interface L extends ViewLayoutBuilderFactory {}
	
	public ViewLayoutBuilder getLayoutBuilder(String layoutBuilderName) ;

}
