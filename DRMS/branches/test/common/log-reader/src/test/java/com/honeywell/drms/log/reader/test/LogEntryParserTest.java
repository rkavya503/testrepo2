package com.honeywell.drms.log.reader.test;

import java.text.SimpleDateFormat;
import java.util.regex.Pattern;

import junit.framework.TestCase;

import org.apache.log4j.Priority;
import org.junit.Test;

import com.honeywell.drms.log.reader.LogEntryParser;
import com.kanaeki.firelog.util.FireLogEntry;

public class LogEntryParserTest extends TestCase {
	
	protected static SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss','SSS");
	
	protected static Pattern SERVER_PATTERN = Pattern.compile("((.*\\\\server\\\\)(\\w*))(\\\\tmp\\\\.*)");

	@Test
	public void testParseLoginOneLine(){
		LogEntryParser parser = new LogEntryParser();
		String log ="2011-11-04 13:49:11,408 INFO  [org.apache.struts.chain.commands.servlet.CreateAction] (http-0.0.0.0-8080-4) Initialize action of type: com.akuacom.pss2.web.session.LogoutAction";
		
		assertTrue(parser.isNewRecordEntry(log));
		
		//parse log and convert it into FireLogEntry
		FireLogEntry entry = parser.parse(log);
		assertEquals(dateFormatter.format(entry.getLogDate()),"2011-11-04 13:49:11,408");
		assertEquals(entry.getLogLevel(),Priority.INFO_INT);
		assertEquals(entry.getCategory(),"org.apache.struts.chain.commands.servlet.CreateAction");
		assertEquals(entry.getDescription(),"(http-0.0.0.0-8080-4) Initialize action of type: com.akuacom.pss2.web.session.LogoutAction");
		
		log ="2011-11-04 13:49:11,408 INFO  [CreateAction] (http-0.0.0.0-8080-4) Initialize action of type: com.akuacom.pss2.web.session.LogoutAction";
		entry = parser.parse(log);
		assertEquals(dateFormatter.format(entry.getLogDate()),"2011-11-04 13:49:11,408");
		assertEquals(entry.getLogLevel(),Priority.INFO_INT);
		assertEquals(entry.getCategory(),"CreateAction");
		assertEquals(entry.getDescription(),"(http-0.0.0.0-8080-4) Initialize action of type: com.akuacom.pss2.web.session.LogoutAction");
	}
	
	@Test
	public void testParseCustomFields(){
		LogEntryParser parser = new LogEntryParser();
		String log ="2011-11-04 13:49:11,408 INFO  [org.apache.struts.chain.commands.servlet.CreateAction] e${i:A0d10111cd084a078495ddecd8c511cd;p:DEMO;u:a;d:xxxxxx yyy}(http-0.0.0.0-8080-4) Initialize action of type: com.akuacom.pss2.web.session.LogoutAction";
		assertTrue(parser.isNewRecordEntry(log));
		//parse log and convert it into FireLogEntry
		FireLogEntry entry = parser.parse(log);
		assertEquals(dateFormatter.format(entry.getLogDate()),"2011-11-04 13:49:11,408");
		assertEquals(entry.getLogLevel(),Priority.INFO_INT);
		assertEquals(entry.getCategory(),"org.apache.struts.chain.commands.servlet.CreateAction");
		assertEquals(entry.getDescription(),"xxxxxx yyy");
		assertEquals(entry.getLongDescr(),"(http-0.0.0.0-8080-4) Initialize action of type: com.akuacom.pss2.web.session.LogoutAction");
		assertEquals(entry.getUserName(),"a");
		assertEquals(entry.getUserParam1(),"DEMO");
		assertEquals(entry.getUserParam3(),"A0d10111cd084a078495ddecd8c511cd");
		
		
		log="2011-12-01 11:53:07,787 INFO  [NamingHelper] e${i:70d10111cd084a078495ddecd8c511bc} JNDI InitialContext properties:{java.naming.factory.initial=org.jnp.interfaces.NamingContextFactory, java.naming.factory.url.pkgs=org.jboss.naming:org.jnp.interfaces}";
		assertTrue(parser.isNewRecordEntry(log));
		//parse log and convert it into FireLogEntry
		entry = parser.parse(log);
		assertEquals(dateFormatter.format(entry.getLogDate()),"2011-12-01 11:53:07,787");
		assertEquals(entry.getLogLevel(),Priority.INFO_INT);
		assertEquals(entry.getUserParam3(),"70d10111cd084a078495ddecd8c511bc");
		
	}
	
	@Test 
	public void testParseLogMultiLine(){
		String log="2011-11-14 13:29:04,131 ERROR [com.akuacom.ejb.client.EJB3Factory] (EJB-Timer-1321237648528[target=jboss.j2ee:ear=pss2.ear,jar=drms-core-ejb-7.1-SNAPSHOT.jar,name=SystemManagerBean,service=EJB3])  Failed to get EJB remote interface for: ContactManager"
		+"\njavax.naming.NameNotFoundException: ContactManagerBean not bound"
		+"\nat org.jnp.server.NamingServer.getBinding(NamingServer.java:771)"
		+"\nat org.jnp.server.NamingServer.getBinding(NamingServer.java:779)"
		+"\nat org.jnp.server.NamingServer.getObject(NamingServer.java:785)"
		+"\nat org.jnp.server.NamingServer.lookup(NamingServer.java:396)"
		+"\nat org.jnp.server.NamingServer.lookup(NamingServer.java:399)"
		+"\nat sun.reflect.GeneratedMethodAccessor297.invoke(Unknown Source)"
		+"\nat sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:25";
		
		LogEntryParser parser = new LogEntryParser();
		assertTrue(parser.isNewRecordEntry(log));
		//parse log and convert it into FireLogEntry
		FireLogEntry entry = parser.parse(log);
		assertEquals(dateFormatter.format(entry.getLogDate()),"2011-11-14 13:29:04,131");
		assertEquals(entry.getLogLevel(),Priority.ERROR_INT);
		assertEquals(entry.getCategory(),"com.akuacom.ejb.client.EJB3Factory");
		String desc = "(EJB-Timer-1321237648528[target=jboss.j2ee:ear=pss2.ear,jar=drms-core-ejb-7.1-SNAPSHOT.jar,name=SystemManagerBean,service=EJB3])  Failed to get EJB remote interface for: ContactManager";
		
		assertEquals(entry.getDescription(),desc.substring(0,128));
		assertEquals(
				"(EJB-Timer-1321237648528[target=jboss.j2ee:ear=pss2.ear,jar=drms-core-ejb-7.1-SNAPSHOT.jar,name=SystemManagerBean,service=EJB3])  Failed to get EJB remote interface for: ContactManager"
		+"\njavax.naming.NameNotFoundException: ContactManagerBean not bound"
		+"\nat org.jnp.server.NamingServer.getBinding(NamingServer.java:771)"
		+"\nat org.jnp.server.NamingServer.getBinding(NamingServer.java:779)"
		+"\nat org.jnp.server.NamingServer.getObject(NamingServer.java:785)"
		+"\nat org.jnp.server.NamingServer.lookup(NamingServer.java:396)"
		+"\nat org.jnp.server.NamingServer.lookup(NamingServer.java:399)"
		+"\nat sun.reflect.GeneratedMethodAccessor297.invoke(Unknown Source)"
		+"\nat sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:25",
		entry.getLongDescr());
	}
	
}
