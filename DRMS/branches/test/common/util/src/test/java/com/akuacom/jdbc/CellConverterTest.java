package com.akuacom.jdbc;


import static org.junit.Assert.assertEquals;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.easymock.EasyMock;
import org.junit.Test;


public class CellConverterTest {

	@Test
	public void testCellConverter() throws SQLException{
		ResultSet rs =  EasyMock.createMock(ResultSet.class);
		ResultSetMetaData metaData = EasyMock.createMock(ResultSetMetaData.class);
		int colCount = 1;
		
		//map each column to SimpleBean's property with same name
		EasyMock.expect(rs.next()).andReturn(true).times(1);
		//EasyMock.expect(rs.next()).andReturn(false).times(2);
		
		rs.getMetaData();
		EasyMock.expectLastCall().andReturn(metaData).anyTimes();
		
		metaData.getColumnCount();
		EasyMock.expectLastCall().andReturn(colCount).anyTimes();
		
		EasyMock.expect(metaData.getColumnLabel(1)).andReturn("count (*)").anyTimes();
		EasyMock.expect(rs.getObject(1)).andReturn(11);
		
		EasyMock.replay(rs);
		EasyMock.replay(metaData);
		
		CellConverter<Long> converter= new CellConverter<Long>(Long.class);
		
		long size =converter.convert(rs);
		assertEquals(11L, size);
		
		EasyMock.verify(rs);
		EasyMock.verify(metaData);
	}
	
}
