package com.akuacom.jdbc;

import static org.junit.Assert.assertEquals;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.easymock.EasyMock;
import org.junit.Test;

public class SimpleConverterTest {

	@Test
	public void testSimpleListConverter() throws SQLException{
		ResultSet rs =  EasyMock.createMock(ResultSet.class);
		ResultSetMetaData metaData = EasyMock.createMock(ResultSetMetaData.class);
		int colCount = 1;
		int rowCount = 3;
		// result set
		// Column:           1       
		// column Label:     strVar
		// values            a,b,c
		EasyMock.expect(rs.next()).andReturn(true).times(3);
		EasyMock.expect(rs.next()).andReturn(false).times(1);
		
		rs.getMetaData();
		EasyMock.expectLastCall().andReturn(metaData).anyTimes();
		
		metaData.getColumnCount();
		EasyMock.expectLastCall().andReturn(colCount).anyTimes();
		
		EasyMock.expect(metaData.getColumnLabel(1)).andReturn("strValue").anyTimes();
		
		EasyMock.expect(rs.getObject(1)).andReturn("a").times(1);
		EasyMock.expect(rs.getObject(1)).andReturn("b").times(1);
		EasyMock.expect(rs.getObject(1)).andReturn("c").times(1);
		
		EasyMock.replay(rs);
		EasyMock.replay(metaData);
		
		List<String> list = new ArrayList<String>();
		SimpleListConverter<String> converter = new SimpleListConverter<String>(String.class);
		
		list =converter.convert(rs);
		
		assertEquals(rowCount,list.size());
			
		assertEquals("a",list.get(0));
		assertEquals("b",list.get(1));
		assertEquals("c",list.get(2));
		
		EasyMock.verify(rs);
		EasyMock.verify(metaData);
	}
	
	
	@Test
	public void testSimpleListConverterForColumn2() throws SQLException{
		ResultSet rs =  EasyMock.createMock(ResultSet.class);
		ResultSetMetaData metaData = EasyMock.createMock(ResultSetMetaData.class);
		int colCount = 2;
		int rowCount = 3;
		// result set
		// Column:           1        2 
		// column Label:     strVar   strVal2
		// values            a,b,c    A,B,C 
		EasyMock.expect(rs.next()).andReturn(true).times(3);
		EasyMock.expect(rs.next()).andReturn(false).times(1);
		
		rs.getMetaData();
		EasyMock.expectLastCall().andReturn(metaData).anyTimes();
		
		metaData.getColumnCount();
		EasyMock.expectLastCall().andReturn(colCount).anyTimes();
		
		EasyMock.expect(metaData.getColumnLabel(1)).andReturn("strVal2").anyTimes();
		
		EasyMock.expect(rs.getObject(2)).andReturn("A").times(1);
		EasyMock.expect(rs.getObject(2)).andReturn("B").times(1);
		EasyMock.expect(rs.getObject(2)).andReturn("C").times(1);
		
		EasyMock.replay(rs);
		EasyMock.replay(metaData);
		
		List<String> list = new ArrayList<String>();
		SimpleListConverter<String> converter = new SimpleListConverter<String>(String.class,2);
		
		list =converter.convert(rs);
		
		assertEquals(rowCount,list.size());
			
		assertEquals("A",list.get(0));
		assertEquals("B",list.get(1));
		assertEquals("C",list.get(2));
		
		EasyMock.verify(rs);
		EasyMock.verify(metaData);
	}
}
