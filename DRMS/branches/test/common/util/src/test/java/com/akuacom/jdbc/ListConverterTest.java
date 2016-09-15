package com.akuacom.jdbc;

import static org.junit.Assert.*;

import java.lang.reflect.Proxy;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.easymock.EasyMock;
import org.junit.Test;

import com.akuacom.jdbc.SampleBeans.Item;
import com.akuacom.jdbc.SampleBeans.Order;
import com.akuacom.jdbc.SampleBeans.SimpleBean;

public class ListConverterTest {
	
	@Test
	public void testListConverter() throws SQLException{
		ResultSet rs =  EasyMock.createMock(ResultSet.class);
		ResultSetMetaData metaData = EasyMock.createMock(ResultSetMetaData.class);
		int colCount = 4;
		int rowCount = 3;
		// result set
		// Column:           1         2           3          4 
		// column Label:     intValue  boolValue longValue   doubleValue
		// bean   property:  intValue  boolValue longValue   doubleValue
		// row1     		 101       TRUE      1001L       1001.001
		// row2              102       FALSE     1002L       1002.001
		// row3     		 103       TRUE      1003L       1003.001
		
		EasyMock.expect(rs.next()).andReturn(true).times(3);
		EasyMock.expect(rs.next()).andReturn(false).times(1);
		
		rs.getMetaData();
		EasyMock.expectLastCall().andReturn(metaData).anyTimes();
		
		metaData.getColumnCount();
		EasyMock.expectLastCall().andReturn(colCount).anyTimes();
		
		EasyMock.expect(metaData.getColumnLabel(1)).andReturn("intValue").anyTimes();
		EasyMock.expect(metaData.getColumnLabel(2)).andReturn("boolValue").anyTimes();
		EasyMock.expect(metaData.getColumnLabel(3)).andReturn("longValue").anyTimes();
		EasyMock.expect(metaData.getColumnLabel(4)).andReturn("doubleValue").anyTimes();
		
		for(int i = 0; i<rowCount; i++){
			int intValue = 101+i;
			boolean bool = (i%2 ==0);
			long longValue = 1001+i;
			double doubleValue = 1001.001+i;
			
			EasyMock.expect(rs.getObject(1)).andReturn(intValue).times(1);
			EasyMock.expect(rs.getObject(2)).andReturn(bool).times(1);
			EasyMock.expect(rs.getObject(3)).andReturn(longValue).times(1);
			EasyMock.expect(rs.getObject(4)).andReturn(doubleValue).times(1);
		}
		
		EasyMock.replay(rs);
		EasyMock.replay(metaData);
		
		
		List<SimpleBean> list = new ArrayList<SimpleBean>();
		ColumnAsFeatureFactory<SimpleBean> factory= new ColumnAsFeatureFactory<SimpleBean>(SimpleBean.class);
		ListConverter<SimpleBean> converter = new ListConverter<SimpleBean>(factory);
		
		list =converter.convert(rs);
		
		assertEquals(3,list.size());
		SimpleBean bean1 = list.get(0);
		assertEquals(101,bean1.getIntValue());
		assertTrue(bean1.isBoolValue());
		assertEquals(1001,bean1.getLongValue());
		assertEquals(1001.001,bean1.getDoubleValue(),3);
		
		
		SimpleBean bean3 = list.get(2);
		assertEquals(103,bean3.getIntValue());
		assertTrue(bean3.isBoolValue());
		assertEquals(1003,bean3.getLongValue());
		assertEquals(1003.001,bean3.getDoubleValue(),3);
		
		EasyMock.verify(rs);
		EasyMock.verify(metaData);
	}
	
	@Test
	public void testListConverterWithMasterDetailConverter() throws SQLException{
		
		int colCount = 4;
		// result set
		// Column:           1              2              3                  4 
		// column Label:     orderId       customer        productName        amount
		// bean   property:  Order.orderId Order.customer  item.productName   item.amount
		// row1-1     		 101           Mark            pen                10
		// row1-2            101           Mark            flower             15
		// row2-1            102           Rose            shoes              20
		// row2-2     		 102           Rose            book               25
		
		//two object - Order (master) and Item (detail)
		
		//map each column to SimpleBean's property with same name
		Object[][] data = {
		// Column:           1              2              3                  4 
		// column Label:     orderId       customer        productName        amount
		// bean property:  Order.orderId Order.customer  item.productName   item.amount
			              {101,        	"Mark",   		"pen", 				   10  },
			              {101,        	"Mark",   		"flower",  		       15  }, 
			              {102,        	"Rose",   		"shoes",  			   20  },
			              {102,        	"Rose",   		"book",                25  },
		};
		String columns[]= {"orderId","customer","productName","amount"};
		ResultSetMetaData metaData = EasyMock.createMock(ResultSetMetaData.class);
		ResultSet rs = (ResultSet) Proxy.newProxyInstance(getClass().getClassLoader(),
				new Class[]{ResultSet.class},
				new MockResultSet(metaData,data,columns));
		
		EasyMock.expect(metaData.getColumnCount()).andReturn(colCount).anyTimes();
		for(int i = 0;i<columns.length;i++){
			EasyMock.expect(metaData.getColumnLabel(i+1)).andReturn(columns[i]).anyTimes();
		}
		
		EasyMock.replay(metaData);
		
		MasterDetailFactory<Order,Item> factory = new MasterDetailFactory<Order,Item>(
		          new ColumnAsFeatureFactory<Order>(Order.class,"orderId"),
		          new ColumnAsFeatureFactory<Item>(Item.class)){
			private static final long serialVersionUID = 1L;
			
			public void buildUp(Order order, Item item){
		  	    if(order.getItems()==null){
		  		    order.setItems(new ArrayList<Item>());
		  	    }
		        order.getItems().add(item);
		        item.setOrder(order);
		      }
		};
		ListConverter<Order> converter = new ListConverter<Order>(factory);
		List<Order> list = converter.convert(rs);
		
		assertEquals(2,list.size());
		Order order1 = list.get(0);
		assertEquals(2,order1.getItems().size());
		assertEquals(101,order1.getOrderId());
		assertEquals("Mark",order1.getCustomer());
		
		Item item11= order1.getItems().get(0);
		Item item12= order1.getItems().get(1);
		
		assertEquals(order1,item11.getOrder());
		assertEquals("pen",item11.getProductName());
		assertEquals(10,item11.getAmount());
		
		assertEquals(order1,item12.getOrder());
		assertEquals("flower",item12.getProductName());
		assertEquals(15,item12.getAmount());
		
		Order order2 = list.get(1);
		assertEquals(2,order2.getItems().size());
		assertEquals(102,order2.getOrderId());
		assertEquals("Rose",order2.getCustomer());
		
		Item item21= order2.getItems().get(0);
		Item item22= order2.getItems().get(1);
		
		assertEquals(order2,item21.getOrder());
		assertEquals("shoes",item21.getProductName());
		assertEquals(20,item21.getAmount());
		
		assertEquals(order2,item22.getOrder());
		assertEquals("book",item22.getProductName());
		assertEquals(25,item22.getAmount());
		
		EasyMock.verify(metaData);
	}
	
	
	@Test
	public void testListConverterWithMasterDetailConverterOrdered() throws SQLException{
		
		int colCount = 4;
		// result set
		// Column:           1              2              3                  4 
		// column Label:     orderId       customer        productName        amount
		// bean   property:  Order.orderId Order.customer  item.productName   item.amount
		// row1-1     		 101           Mark            pen                10
		// row1-2            101           Mark            flower             15
		// row2-1            102           Rose            shoes              20
		// row2-2     		 102           Rose            book               25
		
		//two object - Order (master) and Item (detail)
		
		//map each column to SimpleBean's property with same name
		Object[][] data = {
		// Column:           1              2              3                  4 
		// column Label:     orderId       customer        productName        amount
		// bean property:  Order.orderId Order.customer  item.productName   item.amount
			              {101,        	"Mark",   		"pen", 				   10  },
			              {101,        	"Mark",   		"flower",  		       15  }, 
			              {102,        	"Rose",   		"shoes",  			   20  },
			              {102,        	"Rose",   		"book",                25  },
		};
		String columns[]= {"orderId","customer","productName","amount"};
		ResultSetMetaData metaData = EasyMock.createMock(ResultSetMetaData.class);
		ResultSet rs = (ResultSet) Proxy.newProxyInstance(getClass().getClassLoader(),
				new Class[]{ResultSet.class},
				new MockResultSet(metaData,data,columns));
		
		EasyMock.expect(metaData.getColumnCount()).andReturn(colCount).anyTimes();
		for(int i = 0;i<columns.length;i++){
			EasyMock.expect(metaData.getColumnLabel(i+1)).andReturn(columns[i]).anyTimes();
		}
		
		EasyMock.replay(metaData);
		
		MasterDetailFactory<Order,Item> factory = new MasterDetailFactory<Order,Item>(
		          new ColumnAsFeatureFactory<Order>(Order.class,"orderId"),
		          new ColumnAsFeatureFactory<Item>(Item.class),true){
			private static final long serialVersionUID = 1L;
			
			public void buildUp(Order order, Item item){
		  	    if(order.getItems()==null){
		  		    order.setItems(new ArrayList<Item>());
		  	    }
		        order.getItems().add(item);
		        item.setOrder(order);
		      }
		};
		ListConverter<Order> converter = new ListConverter<Order>(factory);
		List<Order> list = converter.convert(rs);
		
		assertEquals(2,list.size());
		Order order1 = list.get(0);
		assertEquals(2,order1.getItems().size());
		assertEquals(101,order1.getOrderId());
		assertEquals("Mark",order1.getCustomer());
		
		Item item11= order1.getItems().get(0);
		Item item12= order1.getItems().get(1);
		
		assertEquals(order1,item11.getOrder());
		assertEquals("pen",item11.getProductName());
		assertEquals(10,item11.getAmount());
		
		assertEquals(order1,item12.getOrder());
		assertEquals("flower",item12.getProductName());
		assertEquals(15,item12.getAmount());
		
		Order order2 = list.get(1);
		assertEquals(2,order2.getItems().size());
		assertEquals(102,order2.getOrderId());
		assertEquals("Rose",order2.getCustomer());
		
		Item item21= order2.getItems().get(0);
		Item item22= order2.getItems().get(1);
		
		assertEquals(order2,item21.getOrder());
		assertEquals("shoes",item21.getProductName());
		assertEquals(20,item21.getAmount());
		
		assertEquals(order2,item22.getOrder());
		assertEquals("book",item22.getProductName());
		assertEquals(25,item22.getAmount());
		
		EasyMock.verify(metaData);
	}
}
