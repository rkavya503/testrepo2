package com.akuacom.jdbc;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.easymock.EasyMock;
import org.junit.Test;

import com.akuacom.jdbc.SampleBeans.SimpleBean;
import com.akuacom.jdbc.SampleBeans.WEEK_DATE;

public class ColumnAsFeatureFactoryTest {
	
	@Test
	public void testCreateInstance() throws SQLException{
		ResultSet rs =  EasyMock.createMock(ResultSet.class);
		ResultSetMetaData metaData = EasyMock.createMock(ResultSetMetaData.class);
		int colCount = 11;
		// result set
		//columns
		// 1          2           3         4         5         6          7          8            9          10         11
		// boolValue boolObject longValue longObject intValue intObject stringValue doubleValue doubleObject bigDecimal  dateValue 
		// true      FALSE     1000       LONG(1001)  100    Integer(101) "testStr"   100.001     100.002     1000.0001   2011-4-28 01:02:03
		
		//map each column to SimpleBean's property with same name
		
		rs.getMetaData();
		EasyMock.expectLastCall().andReturn(metaData).anyTimes();
		
		metaData.getColumnCount();
		EasyMock.expectLastCall().andReturn(colCount).anyTimes();
		
		EasyMock.expect(metaData.getColumnLabel(1)).andReturn("boolValue").anyTimes();
		EasyMock.expect(metaData.getColumnLabel(2)).andReturn("boolObject").anyTimes();
		EasyMock.expect(metaData.getColumnLabel(3)).andReturn("longValue").anyTimes();
		EasyMock.expect(metaData.getColumnLabel(4)).andReturn("longObject").anyTimes();
		EasyMock.expect(metaData.getColumnLabel(5)).andReturn("intValue").anyTimes();
		EasyMock.expect(metaData.getColumnLabel(6)).andReturn("intObject").anyTimes();
		EasyMock.expect(metaData.getColumnLabel(7)).andReturn("stringValue").anyTimes();
		EasyMock.expect(metaData.getColumnLabel(8)).andReturn("doubleValue").anyTimes();
		EasyMock.expect(metaData.getColumnLabel(9)).andReturn("doubleObject").anyTimes();
		EasyMock.expect(metaData.getColumnLabel(10)).andReturn("bigDecimal").anyTimes();
		EasyMock.expect(metaData.getColumnLabel(11)).andReturn("dateValue").anyTimes();
		
		EasyMock.expect(rs.getObject(1)).andReturn(true);
		EasyMock.expect(rs.getObject(2)).andReturn(Boolean.FALSE);
		EasyMock.expect(rs.getObject(3)).andReturn(1000L);
		EasyMock.expect(rs.getObject(4)).andReturn(new Long(1001));
		EasyMock.expect(rs.getObject(5)).andReturn(100);
		EasyMock.expect(rs.getObject(6)).andReturn(new Integer(101));
		EasyMock.expect(rs.getObject(7)).andReturn("testStr");
		EasyMock.expect(rs.getObject(8)).andReturn(100.001);
		EasyMock.expect(rs.getObject(9)).andReturn(new Double(100.002));
		EasyMock.expect(rs.getObject(10)).andReturn(new BigDecimal("1000.001"));
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-DD hh:MM:SS");
		Date date = null;
		try {
			date = format.parse("2011-04-28 01:02:03");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		EasyMock.expect(rs.getObject(11)).andReturn(date);
		
		EasyMock.replay(rs);
		EasyMock.replay(metaData);
		
		ColumnAsFeatureFactory<SimpleBean> factory= new ColumnAsFeatureFactory<SimpleBean>(SimpleBean.class);
		SimpleBean bean = factory.createInstance(rs);
		
		assertNotNull(bean);                                //column
		assertTrue(bean.isBoolValue());                     //1
		assertFalse(bean.getBoolObject());                  //2
		assertEquals(1000L,bean.getLongValue());            //3
		assertEquals(1001L,bean.getLongObject().longValue());//4
		assertEquals(100,bean.getIntValue());               //5 
		assertEquals(101,bean.getIntObject().intValue());   //6
		assertEquals("testStr",bean.getStringValue());      //7
		assertEquals(100.001,bean.getDoubleValue(),4);      //8
		assertEquals(100.002,bean.getDoubleObject().doubleValue(),4);   //9
		assertEquals(new BigDecimal("1000.001"),bean.getBigDecimal());  //10
		assertEquals(date,bean.getDateValue());            //11
		
		EasyMock.verify(rs);
		EasyMock.verify(metaData);
		
	}
	
	@Test
	public void testCreateInstanceWithParameterMap() throws SQLException{
		ResultSet rs =  EasyMock.createMock(ResultSet.class);
		ResultSetMetaData metaData = EasyMock.createMock(ResultSetMetaData.class);
		int colCount = 4;
		// result set
		//columns longValue1-> longValue  stringValue1->stringValue
		// 1         2           3          4 
		// intValue  boolObject longValue1   stringValue1
		// 101        FALSE     1000L         "testStr"
		
		rs.getMetaData();
		EasyMock.expectLastCall().andReturn(metaData).anyTimes();
		
		metaData.getColumnCount();
		EasyMock.expectLastCall().andReturn(colCount).anyTimes();
		
		EasyMock.expect(metaData.getColumnLabel(1)).andReturn("intValue").anyTimes();
		EasyMock.expect(metaData.getColumnLabel(2)).andReturn("boolObject").anyTimes();
		EasyMock.expect(metaData.getColumnLabel(3)).andReturn("longValue1").anyTimes();
		EasyMock.expect(metaData.getColumnLabel(4)).andReturn("stringValue1").anyTimes();
		
		EasyMock.expect(rs.getObject(1)).andReturn(101);
		EasyMock.expect(rs.getObject(2)).andReturn(Boolean.TRUE);
		EasyMock.expect(rs.getObject(3)).andReturn(1000L);
		EasyMock.expect(rs.getObject(4)).andReturn("testStr");
		
		EasyMock.replay(rs);
		EasyMock.replay(metaData);
		
		Map<String,String> params = new HashMap<String,String>();
		params.put("stringValue1", "stringValue");
		params.put("longValue1",   "longValue");
		ColumnAsFeatureFactory<SimpleBean> factory= new ColumnAsFeatureFactory<SimpleBean>(SimpleBean.class,params);
		SimpleBean bean = factory.createInstance(rs);
		
		assertNotNull(bean);                                  //column
		assertEquals(101,bean.getIntValue());     			  //1
		assertTrue(bean.getBoolObject());                     //2
		assertEquals(1000L,bean.getLongValue());              //3
		assertEquals("testStr",bean.getStringValue());        //4
		
		EasyMock.verify(rs);
		EasyMock.verify(metaData);
	}
	
	
	@Test
	public void testCreateInstanceWithAutoBoxing() throws SQLException{
		ResultSet rs =  EasyMock.createMock(ResultSet.class);
		ResultSetMetaData metaData = EasyMock.createMock(ResultSetMetaData.class);
		int colCount = 4;
		// result set
		// Column:           1         2           3          4 
		// column Label:     intValue  boolObject longValue   doubleObject
		// column value:     101        FALSE     1000L       1000.001
		// bean   property:  intObject  boolValue longObject  doubleValue
		
		rs.getMetaData();
		EasyMock.expectLastCall().andReturn(metaData).anyTimes();
		
		metaData.getColumnCount();
		EasyMock.expectLastCall().andReturn(colCount).anyTimes();
		
		EasyMock.expect(metaData.getColumnLabel(1)).andReturn("intValue").anyTimes();
		EasyMock.expect(metaData.getColumnLabel(2)).andReturn("boolObject").anyTimes();
		EasyMock.expect(metaData.getColumnLabel(3)).andReturn("longValue").anyTimes();
		EasyMock.expect(metaData.getColumnLabel(4)).andReturn("doubleObject").anyTimes();
		
		EasyMock.expect(rs.getObject(1)).andReturn(101);
		EasyMock.expect(rs.getObject(2)).andReturn(Boolean.TRUE);
		EasyMock.expect(rs.getObject(3)).andReturn(1000L);
		EasyMock.expect(rs.getObject(4)).andReturn(new Double(1000.001));
		
		EasyMock.replay(rs);
		EasyMock.replay(metaData);
		
		Map<String,String> params = new HashMap<String,String>();
		params.put("intValue", "intObject");
		params.put("boolObject",   "boolValue");
		params.put("longValue", "longObject");
		params.put("doubleObject",   "doubleValue");
		
		ColumnAsFeatureFactory<SimpleBean> factory= new ColumnAsFeatureFactory<SimpleBean>(SimpleBean.class,params);
		SimpleBean bean = factory.createInstance(rs);
		
		assertNotNull(bean);                                  //column
		assertEquals(101,bean.getIntObject().intValue());     //1
		assertTrue(bean.isBoolValue());                       //2
		assertEquals(1000L,bean.getLongObject().longValue()); //3
		assertEquals(1000.001,bean.getDoubleValue(),4);       //4
		
		EasyMock.verify(rs);
		EasyMock.verify(metaData);
	}
	
	@Test
	public void testCreateInstanceWithAutoTypeConversion() throws SQLException{
		ResultSet rs =  EasyMock.createMock(ResultSet.class);
		ResultSetMetaData metaData = EasyMock.createMock(ResultSetMetaData.class);
		int colCount = 6;
		// result set
		// Column:           1         2          3           4            5           6         
		// column Label:     intValue  longValue  doubleValue bigDecimal   enumStr     ordinal
		// column value:     101       1000L      1000.001    10000.002    "MON"(MON)  2 (TUE)
		// bean   property:  longValue intValue   bigDecimal  doubleValue  weekDate    weekDate1
		
		rs.getMetaData();
		EasyMock.expectLastCall().andReturn(metaData).anyTimes();
		
		metaData.getColumnCount();
		EasyMock.expectLastCall().andReturn(colCount).anyTimes();
		
		EasyMock.expect(metaData.getColumnLabel(1)).andReturn("intValue").anyTimes();
		EasyMock.expect(metaData.getColumnLabel(2)).andReturn("longValue").anyTimes();
		EasyMock.expect(metaData.getColumnLabel(3)).andReturn("doubleValue").anyTimes();
		EasyMock.expect(metaData.getColumnLabel(4)).andReturn("bigDecimal").anyTimes();
		EasyMock.expect(metaData.getColumnLabel(5)).andReturn("enumStr").anyTimes();
		EasyMock.expect(metaData.getColumnLabel(6)).andReturn("ordinal").anyTimes();
		
		EasyMock.expect(rs.getObject(1)).andReturn(101);
		EasyMock.expect(rs.getObject(2)).andReturn(1000L);
		EasyMock.expect(rs.getObject(3)).andReturn(1000.001);
		EasyMock.expect(rs.getObject(4)).andReturn(new BigDecimal("10000.002"));
		EasyMock.expect(rs.getObject(5)).andReturn("MON");
		EasyMock.expect(rs.getObject(6)).andReturn(2);
		
		
		EasyMock.replay(rs);
		EasyMock.replay(metaData);
		
		Map<String,String> params = new HashMap<String,String>();
		params.put("intValue", "longValue");
		params.put("longValue",   "intValue");
		params.put("doubleValue", "bigDecimal");
		params.put("bigDecimal",   "doubleValue");
		params.put("enumStr",   "weekDate");
		params.put("ordinal",   "weekDate2");
		
		
		ColumnAsFeatureFactory<SimpleBean> factory= new ColumnAsFeatureFactory<SimpleBean>(SimpleBean.class,params);
		SimpleBean bean = factory.createInstance(rs);
		 
		assertNotNull(bean);                                           //column
		assertEquals(101L,bean.getLongValue());                        //1
		assertEquals(1000,bean.getIntValue());                         //2
		assertEquals(new BigDecimal("1000.001"),bean.getBigDecimal()); //3
		assertEquals(10000.002,bean.getDoubleValue(),4);       	       //4
		assertEquals(WEEK_DATE.MON,bean.getWeekDate());
		assertEquals(WEEK_DATE.TUE,bean.getWeekDate2());
		
		EasyMock.verify(rs);
		EasyMock.verify(metaData);
		
	}
}
