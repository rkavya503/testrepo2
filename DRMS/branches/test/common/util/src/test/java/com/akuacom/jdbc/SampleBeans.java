package com.akuacom.jdbc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SampleBeans {

	public static enum WEEK_DATE {
		SUN, MON, TUE, WED, THU, FRI, SAT
	}

	public static class SimpleBean {

		private boolean boolValue;
		private Boolean boolObject;
		private long longValue;
		private Long longObject;
		private int intValue;
		private Integer intObject;
		private short shortValue;
		private Short shortObject;
		private double doubleValue;
		private Double doubleObject;
		private float floatValue;
		private Float floatObject;
		private byte byteValue;
		private Byte byteObject;
		private String stringValue;
		private Date dateValue;
		private BigDecimal bigDecimal;
		private WEEK_DATE weekDate;
		private WEEK_DATE weekDate2;

		public boolean isBoolValue() {
			return boolValue;
		}

		public void setBoolValue(boolean boolValue) {
			this.boolValue = boolValue;
		}

		public Boolean getBoolObject() {
			return boolObject;
		}

		public void setBoolObject(Boolean boolObject) {
			this.boolObject = boolObject;
		}

		public long getLongValue() {
			return longValue;
		}

		public void setLongValue(long longValue) {
			this.longValue = longValue;
		}

		public Long getLongObject() {
			return longObject;
		}

		public void setLongObject(Long longObject) {
			this.longObject = longObject;
		}

		public int getIntValue() {
			return intValue;
		}

		public void setIntValue(int intValue) {
			this.intValue = intValue;
		}

		public Integer getIntObject() {
			return intObject;
		}

		public void setIntObject(Integer intObject) {
			this.intObject = intObject;
		}

		public short getShortValue() {
			return shortValue;
		}

		public void setShortValue(short shortValue) {
			this.shortValue = shortValue;
		}

		public Short getShortObject() {
			return shortObject;
		}

		public void setShortObject(Short shortObject) {
			this.shortObject = shortObject;
		}

		public double getDoubleValue() {
			return doubleValue;
		}

		public void setDoubleValue(double doubleValue) {
			this.doubleValue = doubleValue;
		}

		public Double getDoubleObject() {
			return doubleObject;
		}

		public void setDoubleObject(Double doubleObject) {
			this.doubleObject = doubleObject;
		}

		public float getFloatValue() {
			return floatValue;
		}

		public void setFloatValue(float floatValue) {
			this.floatValue = floatValue;
		}

		public Float getFloatObject() {
			return floatObject;
		}

		public void setFloatObject(Float floatObject) {
			this.floatObject = floatObject;
		}

		public byte getByteValue() {
			return byteValue;
		}

		public void setByteValue(byte byteValue) {
			this.byteValue = byteValue;
		}

		public Byte getByteObject() {
			return byteObject;
		}

		public void setByteObject(Byte byteObject) {
			this.byteObject = byteObject;
		}

		public String getStringValue() {
			return stringValue;
		}

		public void setStringValue(String stringValue) {
			this.stringValue = stringValue;
		}

		public Date getDateValue() {
			return dateValue;
		}

		public void setDateValue(Date dateValue) {
			this.dateValue = dateValue;
		}

		public BigDecimal getBigDecimal() {
			return bigDecimal;
		}

		public void setBigDecimal(BigDecimal bigDecimal) {
			this.bigDecimal = bigDecimal;
		}

		public WEEK_DATE getWeekDate() {
			return weekDate;
		}

		public void setWeekDate(WEEK_DATE weekDate) {
			this.weekDate = weekDate;
		}

		public WEEK_DATE getWeekDate2() {
			return weekDate2;
		}

		public void setWeekDate2(WEEK_DATE weekDate2) {
			this.weekDate2 = weekDate2;
		}

		@Override
		public String toString() {
			return "SampleBean [boolValue=" + boolValue + ", boolObject="
					+ boolObject + ", longValue=" + longValue + ", longObject="
					+ longObject + ", intValue=" + intValue + ", intObject="
					+ intObject + ", shortValue=" + shortValue
					+ ", shortObject=" + shortObject + ", doubleValue="
					+ doubleValue + ", doubleObject=" + doubleObject
					+ ", floatValue=" + floatValue + ", floatObject="
					+ floatObject + ", byteValue=" + byteValue
					+ ", byteObject=" + byteObject + ", stringValue="
					+ stringValue + ", dateValue=" + dateValue
					+ ", bigDecimal=" + bigDecimal + ", weekDate=" + weekDate
					+ ", weekDate2=" + weekDate2 + "]";
		}
	}
	
	
	//master/detail (one to many) relationship
	public static class Order {
		private int orderId ;
		private String customer;
		private Date date;
		private List<Item> items = new ArrayList<Item>();
		public int getOrderId() {
			return orderId;
		}
		public void setOrderId(int orderId) {
			this.orderId = orderId;
		}
		public String getCustomer() {
			return customer;
		}
		public void setCustomer(String customer) {
			this.customer = customer;
		}
		public Date getDate() {
			return date;
		}
		public void setDate(Date date) {
			this.date = date;
		}
		public List<Item> getItems() {
			return items;
		}
		public void setItems(List<Item> items) {
			this.items = items;
		}
	}
	
	public static class Item {
		private String productName;
		private int amount;
		private Order order;
		
		public Order getOrder() {
			return order;
		}
		public void setOrder(Order order) {
			this.order = order;
		}
		public String getProductName() {
			return productName;
		}
		public void setProductName(String productName) {
			this.productName = productName;
		}
		public int getAmount() {
			return amount;
		}
		public void setAmount(int amount) {
			this.amount = amount;
		}
	}
	
}
