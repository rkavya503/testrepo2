package com.akuacom.jdbc;

import static org.junit.Assert.assertEquals;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.easymock.EasyMock;
import org.junit.Test;

import com.akuacom.utils.DateUtil;

public class NamedParameterSQLTest {

	protected static String singleWhitespace(String str){
		str= str.replaceAll("\\s{2,}", " ");
		return str.trim();
	}
	
	protected void assertSQLEquals(String sql1,String sql2){
		assertEquals(singleWhitespace(sql1),singleWhitespace(sql2));
	}
	
	@Test
	public void testParameterBinding() throws Exception {
		//String sqltemplate = SQLLoader.loadSQLFromFile(NamedParameterSQLTest.class,"resource/clientStatus.sql");
		String sqltemplate =
				"select * from client_status where 1=1 and [participantName in ${participantName}] and startTime < ${startTime}" 
			  + " and endTime <${endTime} and [clientName in ${participantName}] ${orderBy} ${range}";
		
		//parameterized sql 
		//select * from client_status where 1=1 and participantName in (?,?,?) and startTime < ? " 
		// " and endTime < ?  and clientName in (?,?,?) order by participantName desc limie 1:50";
		
		
		Map<String,Object> params = new HashMap<String,Object>();
		Date startDate = DateUtil.stripTime(new Date());
		Date endDate = DateUtil.endOfDay(new Date());
		params.put("startTime", startDate);
		params.put("endTime", endDate);
		params.put("participantName",Arrays.asList("p1","p2","p3"));
		params.put("orderBy", new SQLWord("order by participantName desc"));
		params.put("range", new SQLWord("limit 1:50"));
		
		String sql = SQLBuilder.buildNamedParameterSQL(sqltemplate, params);
		
		assertSQLEquals("select * from client_status where 1=1 and participantName in ${participantName} and startTime < ${startTime}" 
		  + " and endTime <${endTime} and clientName in ${participantName} ${orderBy} ${range}", sql);
		
		Connection mockConnection =  EasyMock.createMock(Connection.class);
		PreparedStatement mockps = EasyMock.createMock(PreparedStatement.class);
		EasyMock.expect(mockConnection.prepareStatement(EasyMock.isA(String.class))).andReturn(mockps).anyTimes();
		
		mockps.setString(1,"p1");
		EasyMock.expectLastCall().times(1);
		mockps.setString(2,"p2");
		EasyMock.expectLastCall().times(1);
		mockps.setString(3,"p3");
		EasyMock.expectLastCall().times(1);
		
		mockps.setTimestamp(4,JavaTypes.dateToSqlTimeStamp(startDate));
		EasyMock.expectLastCall().times(1);
		
		mockps.setTimestamp(5,JavaTypes.dateToSqlTimeStamp(endDate));
		EasyMock.expectLastCall().times(1);
		
		mockps.setString(6,"p1");
		EasyMock.expectLastCall().times(1);
		mockps.setString(7,"p2");
		EasyMock.expectLastCall().times(1);
		mockps.setString(8,"p3");
		EasyMock.expectLastCall().times(1);
		
		EasyMock.replay(mockConnection);
		EasyMock.replay(mockps);
		
		//ready, go replay
		NamedParameterStatement ns = new NamedParameterStatement(sql,params,mockConnection);
		//two occurrences of participantName 
		assertEquals(ns.getIndexes("participantName")[0],1); //start from 1
		assertEquals(ns.getIndexes("participantName")[1],6); //start from 6
		
		assertEquals(ns.getIndexes("startTime")[0],4);
		assertEquals(ns.getIndexes("endTime")[0],5);
		assertEquals(ns.getIndexes("orderBy"),null);
		
		ns.bindParameters();
		
		EasyMock.verify(mockConnection);
		EasyMock.verify(mockps);
		
	}
	
}
