package com.akuacom.pss2.userrole;

import javax.ejb.Stateless;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.akuacom.pss2.userrole.layoutbuilder.ViewLayoutBuilder;


@Stateless
public class ViewLayoutBuilderFactoryBean  implements ViewLayoutBuilderFactory.L,ViewLayoutBuilderFactory.R{

	private ViewLayoutBuilder builder = null;
	@Override
	public ViewLayoutBuilder getLayoutBuilder(String layoutBuilderBeanName) {
		try {
			this.builder = (ViewLayoutBuilder) new InitialContext().lookup("pss2/"+layoutBuilderBeanName+"/local");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return builder;
	}

}
