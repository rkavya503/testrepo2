/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.utilopws.stubs.PMapper.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.utilopws.stubs;

import com.sun.xml.bind.marshaller.NamespacePrefixMapper;

/**
 * The Class PMapper.
 */
public class PMapper extends NamespacePrefixMapper
{
	
	/* (non-Javadoc)
	 * @see com.sun.xml.bind.marshaller.NamespacePrefixMapper#getPreferredPrefix(java.lang.String, java.lang.String, boolean)
	 */
	@Override
	public String getPreferredPrefix(String namespaceUri,
		String suggestion, boolean requirePrefix)
	{
		if(namespaceUri.equals("http://www.w3.org/2001/XMLSchema-instance"))
		{
			return "xsi";
		}
		else if(namespaceUri.equals("http://www.openadr.org/DRAS/EventState"))
		{
			return "p";
		}
		return suggestion;
	}
}
