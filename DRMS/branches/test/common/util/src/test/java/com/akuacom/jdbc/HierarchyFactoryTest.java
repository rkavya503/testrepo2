package com.akuacom.jdbc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Proxy;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.easymock.EasyMock;
import org.junit.Test;



public class HierarchyFactoryTest {
	
	public static class DataSource{
		private String dataSourceId;
		private String dataSourceName;
		private Map<String,DataSet> dateSets = new HashMap<String,DataSet>();
		private List<DataEntry> dataEntries = new ArrayList<DataEntry>();
		
		public String getDataSourceId() {
			return dataSourceId;
		}
		public void setDataSourceId(String dataSourceId) {
			this.dataSourceId = dataSourceId;
		}
		
		public String getDataSourceName() {
			return dataSourceName;
		}
		public void setDataSourceName(String dataSourceName) {
			this.dataSourceName = dataSourceName;
		}
		public Map<String, DataSet> getDateSets() {
			return dateSets;
		}
		public void addDataSet(String setId, DataSet dset){
			dateSets.put(setId, dset);
		}
		public List<DataEntry> getDataEntries() {
			return dataEntries;
		}
		public void setDataEntries(List<DataEntry> dataEntries) {
			this.dataEntries = dataEntries;
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((dataSourceId == null) ? 0 : dataSourceId.hashCode());
			return result;
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			DataSource other = (DataSource) obj;
			if (dataSourceId == null) {
				if (other.dataSourceId != null)
					return false;
			} else if (!dataSourceId.equals(other.dataSourceId))
				return false;
			return true;
		}
		@Override
		public String toString() {
			return "DataSource [dataSourceId=" + dataSourceId
					+ ", dataSourceName=" + dataSourceName + "]";
		}
	}
	
	public static class DataSet {
		private String dataSetId;
		private String dataSetName;
		
		private Map<String,DataSource> dataSource = new HashMap<String,DataSource>();
		private List<DataEntry> dataEntries = new ArrayList<DataEntry>();
		
		public String getDataSetId() {
			return dataSetId;
		}
		public void setDataSetId(String dataSetId) {
			this.dataSetId = dataSetId;
		}
		public String getDataSetName() {
			return dataSetName;
		}
		public void setDataSetName(String dataSetName) {
			this.dataSetName = dataSetName;
		}
		
		
		public Map<String, DataSource> getDataSource() {
			return dataSource;
		}
		public void addDataSource(String sourceId, DataSource dsource){
			dataSource.put(sourceId, dsource);
		}
		
		public List<DataEntry> getDataEntries() {
			return dataEntries;
		}
		public void setDataEntries(List<DataEntry> dataEntries) {
			this.dataEntries = dataEntries;
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((dataSetId == null) ? 0 : dataSetId.hashCode());
			return result;
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			DataSet other = (DataSet) obj;
			if (dataSetId == null) {
				if (other.dataSetId != null)
					return false;
			} else if (!dataSetId.equals(other.dataSetId))
				return false;
			return true;
		}
		@Override
		public String toString() {
			return "DataSet [dataSetId=" + dataSetId + ", dataSetName="
					+ dataSetName + "]";
		}
		
	}
	
	public static class DataEntry {
		private String dataEntryId;
		private String time;
		private String dataSetName;
		private DataSet dataSet;
		private DataSource dataSource;
		
		public String getDataEntryId() {
			return dataEntryId;
		}
		public void setDataEntryId(String dataEntryId) {
			this.dataEntryId = dataEntryId;
		}
		
		public String getTime() {
			return time;
		}
		public void setTime(String time) {
			this.time = time;
		}
		
		public String getDataSetName() {
			return dataSetName;
		}
		public void setDataSetName(String dataSetName) {
			this.dataSetName = dataSetName;
		}
		
		public DataSet getDataSet() {
			return dataSet;
		}
		public void setDataSet(DataSet dataSet) {
			this.dataSet = dataSet;
		}
		public DataSource getDataSource() {
			return dataSource;
		}
		public void setDataSource(DataSource dataSource) {
			this.dataSource = dataSource;
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((dataEntryId == null) ? 0 : dataEntryId.hashCode());
			return result;
		}
		
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			DataEntry other = (DataEntry) obj;
			if (dataEntryId == null) {
				if (other.dataEntryId != null)
					return false;
			} else if (!dataEntryId.equals(other.dataEntryId))
				return false;
			return true;
		}
		@Override
		public String toString() {
			return "DataEntry [dataEntryId=" + dataEntryId + ", time=" + time
					+ ", dataSetName=" + dataSetName + "]";
		}
	}
	
	
	@Test
	public void testDataSourceDataEntry() throws SQLException{
		int colCount = 6;
		//map each column to SimpleBean's property with same name
		Object[][] data = {
			//1              2       3            4            5               6
			//dataEntryId   time     dataSetId  dataSetName   dataSourceId  dataSourceName
			{"e1",        "12:00",   "type1",  "forecast",   "source1",    "participant1"},
			{"e2",        "12:15",   "type1",  "forecast",   "source1",    "participant1"},
			{"e3",        "12:00",   "type2",  "usage",      "source1",    "participant1"},
			{"e4",        "12:15",   "type2",  "usage",      "source1",    "participant1"},
			{"e5",        "12:00",   "type1",  "forecast",   "source2",    "participant2"},
			{"e6",        "12:15",   "type1",  "forecast",   "source2",    "participant2"},
			{"e7",        "12:00",   "type2",  "usage",      "source2",    "participant2"},
			{"e8",        "12:15",   "type2",  "usage",      "source2",    "participant2"},
		};
		String columns[]= {"dataEntryId","time","dataSetId","dataSetName","dataSourceId","dataSourceName"};
		ResultSetMetaData metaData = EasyMock.createMock(ResultSetMetaData.class);
		ResultSet rs = (ResultSet) Proxy.newProxyInstance(getClass().getClassLoader(),
				new Class[]{ResultSet.class},
				new MockResultSet(metaData,data,columns));
		
		EasyMock.expect(metaData.getColumnCount()).andReturn(colCount).anyTimes();
		for(int i = 0;i<columns.length;i++){
			EasyMock.expect(metaData.getColumnLabel(i+1)).andReturn(columns[i]).anyTimes();
		}
		
		EasyMock.replay(metaData);
		
		Map<String,BeanFactory<?>> factories = new HashMap<String,BeanFactory<?>>();
		factories.put("/",new ColumnAsFeatureFactory<DataSource>(DataSource.class,"dataSourceId"));
		factories.put("/dataEntry",new ColumnAsFeatureFactory<DataEntry>(DataEntry.class,"dataEntryId"));
		
		HierarchyFactory<DataSource> factory= new HierarchyFactory<DataSource>(factories){
			private static final long serialVersionUID = 2468387495125385719L;
			@Override
			public void buildUp(Object parent, Object child, String path) {
				if(path.equals("/dataEntry")){
					DataSource source = (DataSource) parent;
					DataEntry entry = (DataEntry)child;
					source.getDataEntries().add(entry);
				}
			}
		};
		
		ListConverter<DataSource> converter = new ListConverter<DataSource>(factory);
		List<DataSource> dataSources =converter.convert(rs);
		// |-Participant1
		// |           |-e1,12:00,forecast
		// |           |-e2,12:15,forecast
		// |           |-e3,12:00,usage
		// |           |-e4,12:05,usage
		// |-Participant2    
		// |           |-e5,12:00,forecast
		// |           |-e6,12:15,forecast
		// |           |-e7,12:00,usage
		// |           |-e8,12:05,usage
		
		assertEquals(2,dataSources.size());
		//participant1
		DataSource source1 = dataSources.get(0);
		assertEquals("participant1",source1.getDataSourceName());
		assertEquals(4,source1.getDataEntries().size());
		
		DataEntry entry1 = source1.getDataEntries().get(0);
		assertEquals("e1",entry1.getDataEntryId());
		assertEquals("12:00",entry1.getTime());
		assertEquals("forecast",entry1.getDataSetName());
		
		DataEntry entry2 = source1.getDataEntries().get(1);
		assertEquals("e2",entry2.getDataEntryId());
		assertEquals("12:15",entry2.getTime());
		assertEquals("forecast",entry2.getDataSetName());
		
		DataEntry entry3 = source1.getDataEntries().get(2);
		assertEquals("e3",entry3.getDataEntryId());
		assertEquals("12:00",entry3.getTime());
		assertEquals("usage",entry3.getDataSetName());
		
		DataEntry entry4 = source1.getDataEntries().get(3);
		assertEquals("e4",entry4.getDataEntryId());
		assertEquals("12:15",entry4.getTime());
		assertEquals("usage",entry4.getDataSetName());
		
		//participant2
		DataSource source2 = dataSources.get(1);
		assertEquals("participant2",source2.getDataSourceName());
		assertEquals(4,source2.getDataEntries().size());
		
		DataEntry entry5 = source2.getDataEntries().get(0);
		assertEquals("e5",entry5.getDataEntryId());
		assertEquals("12:00",entry5.getTime());
		assertEquals("forecast",entry5.getDataSetName());
		
		DataEntry entry6 = source2.getDataEntries().get(1);
		assertEquals("e6",entry6.getDataEntryId());
		assertEquals("12:15",entry6.getTime());
		assertEquals("forecast",entry6.getDataSetName());
		
		DataEntry entry7 = source2.getDataEntries().get(2);
		assertEquals("e7",entry7.getDataEntryId());
		assertEquals("12:00",entry7.getTime());
		assertEquals("usage",entry7.getDataSetName());
		
		DataEntry entry8 = source2.getDataEntries().get(3);
		assertEquals("e8",entry8.getDataEntryId());
		assertEquals("12:15",entry8.getTime());
		assertEquals("usage",entry8.getDataSetName());
	}
	
	@Test
	public void testDataSourceDataSetDataEntryNonOrderedRows() throws SQLException{
		int colCount = 6;
		//map each column to SimpleBean's property with same name
		Object[][] data = {
			//1              2       3            4            5               6
			//dataEntryId   time     dataSetId  dataSetName   dataSourceId  dataSourceName
			{"e1",        "12:00",   "type1",  "forecast",   "source1",    "participant1"},
			{"e7",        "12:00",   "type2",  "usage",      "source2",    "participant2"},
			{"e3",        "12:00",   "type2",  "usage",      "source1",    "participant1"},
			{"e2",        "12:15",   "type1",  "forecast",   "source1",    "participant1"},
			{"e4",        "12:15",   "type2",  "usage",      "source1",    "participant1"},
			{"e5",        "12:00",   "type1",  "forecast",   "source2",    "participant2"},
			{"e6",        "12:15",   "type1",  "forecast",   "source2",    "participant2"},
			{"e8",        "12:15",   "type2",  "usage",      "source2",    "participant2"},
		};
		String columns[]= {"dataEntryId","time","dataSetId","dataSetName","dataSourceId","dataSourceName"};
		ResultSetMetaData metaData = EasyMock.createMock(ResultSetMetaData.class);
		ResultSet rs = (ResultSet) Proxy.newProxyInstance(getClass().getClassLoader(),
				new Class[]{ResultSet.class},
				new MockResultSet(metaData,data,columns));
		
		EasyMock.expect(metaData.getColumnCount()).andReturn(colCount).anyTimes();
		for(int i = 0;i<columns.length;i++){
			EasyMock.expect(metaData.getColumnLabel(i+1)).andReturn(columns[i]).anyTimes();
		}
		
		EasyMock.replay(metaData);
		
		Map<String,BeanFactory<?>> factories = new HashMap<String,BeanFactory<?>>();
		factories.put("/",new ColumnAsFeatureFactory<DataSource>(DataSource.class,"dataSourceId"));
		factories.put("/dataSet",new ColumnAsFeatureFactory<DataSet>(DataSet.class,"dataSourceId","dataSetId"));
		factories.put("/dataSet/dataEntry",new ColumnAsFeatureFactory<DataEntry>(DataEntry.class));
		
		HierarchyFactory<DataSource> factory= new HierarchyFactory<DataSource>(factories){
			private static final long serialVersionUID = 2468387495125385719L;
			@Override
			public void buildUp(Object parent, Object child, String path) {
				if(path.equals("/dataSet")){
					DataSource source = (DataSource) parent;
					DataSet set = (DataSet)child;
					source.addDataSet(set.getDataSetId(), set);
				}else if(path.equals("/dataSet/dataEntry")){
					DataSet set = (DataSet)parent;
					DataEntry entry = (DataEntry)child;
					set.getDataEntries().add(entry);
				}
			}
		};
		
		ListConverter<DataSource> converter = new ListConverter<DataSource>(factory);
		List<DataSource> dataSources =converter.convert(rs);
		
		// |-Participant1
		// |		   |-forecast
		// |           			 |-e1,12:00
		// |                     |-e2,12:15
		// |           |-usage
		// |           			 |-e3,12:00
		// |                     |-e4,12:15
		// |-Participant2    
		// |           |-forecast
		// |           			 |-e5,12:00
		// |                     |-e6,12:15
		// |           |-usage	
		// |           			 |-e7,12:00
		// |                     |-e8,12:15
		
		//participant1
		assertEquals(2,dataSources.size());
		DataSource source1 = dataSources.get(0);
		assertEquals("participant1",source1.getDataSourceName());
		assertEquals(2,source1.dateSets.size());
		//forecast
		DataSet set1 = source1.getDateSets().get("type1");
		assertEquals("forecast",set1.getDataSetName());
		assertEquals(2,set1.getDataEntries().size());
		
		DataEntry entry1 = set1.getDataEntries().get(0);
		assertEquals("e1",entry1.getDataEntryId());
		assertEquals("12:00",entry1.getTime());
		DataEntry entry2 = set1.getDataEntries().get(1);
		assertEquals("e2",entry2.getDataEntryId());
		assertEquals("12:15",entry2.getTime());
		
		//usage
		DataSet set2 = source1.getDateSets().get("type2");
		assertEquals("usage",set2.getDataSetName());
		assertEquals(2,set2.getDataEntries().size());
		
		DataEntry entry3 = set2.getDataEntries().get(0);
		assertEquals("e3",entry3.getDataEntryId());
		assertEquals("12:00",entry3.getTime());
		DataEntry entry4 = set2.getDataEntries().get(1);
		assertEquals("e4",entry4.getDataEntryId());
		assertEquals("12:15",entry4.getTime());
		
		//participant2
		DataSource source2 = dataSources.get(1);
		assertEquals("participant2",source2.getDataSourceName());
		assertEquals(2,source2.dateSets.size());
		
		//forecast
		set1 = source2.getDateSets().get("type1");
		assertEquals("forecast",set1.getDataSetName());
		assertEquals(2,set1.getDataEntries().size());
		DataEntry entry5 = set1.getDataEntries().get(0);
		assertEquals("e5",entry5.getDataEntryId());
		assertEquals("12:00",entry5.getTime());
		DataEntry entry6 = set1.getDataEntries().get(1);
		assertEquals("e6",entry6.getDataEntryId());
		assertEquals("12:15",entry6.getTime());
		
		
		set2 = source2.getDateSets().get("type2");
		assertEquals("usage",set2.getDataSetName());
		assertEquals(2,set2.getDataEntries().size());
		DataEntry entry7 = set2.getDataEntries().get(0);
		assertEquals("e7",entry7.getDataEntryId());
		assertEquals("12:00",entry7.getTime());
		DataEntry entry8 = set2.getDataEntries().get(1);
		assertEquals("e8",entry8.getDataEntryId());
		assertEquals("12:15",entry8.getTime());
		
		EasyMock.verify(metaData);
	}
	
	@Test
	public void testDataSetDataSourceDataEntry() throws SQLException{
		int colCount = 6;
		//map each column to SimpleBean's property with same name
		Object[][] data = {
			//1              2       3            4            5               6
			//dataEntryId   time     dataSetId  dataSetName   dataSourceId  dataSourceName
			{"e1",        "12:00",   "type1",  "forecast",   "source1",    "participant1"},
			{"e2",        "12:15",   "type1",  "forecast",   "source1",    "participant1"},
			{"e3",        "12:00",   "type2",  "usage",      "source1",    "participant1"},
			{"e4",        "12:15",   "type2",  "usage",      "source1",    "participant1"},
			{"e5",        "12:00",   "type1",  "forecast",   "source2",    "participant2"},
			{"e6",        "12:15",   "type1",  "forecast",   "source2",    "participant2"},
			{"e7",        "12:00",   "type2",  "usage",      "source2",    "participant2"},
			{"e8",        "12:15",   "type2",  "usage",      "source2",    "participant2"},
			{"e9",        "12:30",   "type2",  "usage",      "source2",    "participant2"},
			{"e10",       "12:45",   "type2",  "usage",      "source2",    "participant2"},
			{"e11",       "13:00",   "type2",  "usage",      "source2",    "participant2"},
		};
		
		String columns[]= {"dataEntryId","time","dataSetId","dataSetName","dataSourceId","dataSourceName"};
		
		ResultSetMetaData metaData = EasyMock.createMock(ResultSetMetaData.class);
		ResultSet rs = (ResultSet) Proxy.newProxyInstance(getClass().getClassLoader(),
				new Class[]{ResultSet.class},
				new MockResultSet(metaData,data,columns));
		
		
		EasyMock.expect(metaData.getColumnCount()).andReturn(colCount).anyTimes();
		for(int i = 0;i<columns.length;i++){
			EasyMock.expect(metaData.getColumnLabel(i+1)).andReturn(columns[i]).anyTimes();
		}
		
		EasyMock.replay(metaData);
		
		Map<String,BeanFactory<?>> factories = new HashMap<String,BeanFactory<?>>();
		factories.put("/",new ColumnAsFeatureFactory<DataSet>(DataSet.class,"dataSetId"));
		factories.put("/dataSource",new ColumnAsFeatureFactory<DataSource>(DataSource.class,"dataSourceId","dataSetId"));
		factories.put("/dataSource/dataEntry",new ColumnAsFeatureFactory<DataEntry>(DataEntry.class));
		
		HierarchyFactory<DataSet> factory= new HierarchyFactory<DataSet>(factories){
			private static final long serialVersionUID = 2468387495125385719L;
			@Override
			public void buildUp(Object parent, Object child, String path) {
				if(path.equals("/dataSource")){
					DataSet set = (DataSet) parent;
					DataSource source = (DataSource)child;
					set.addDataSource(source.getDataSourceId(), source);
				}else if(path.equals("/dataSource/dataEntry")){
					DataSource source = (DataSource)parent;
					DataEntry entry = (DataEntry)child;
					source.getDataEntries().add(entry);
				}
			}
		};
		ListConverter<DataSet> converter = new ListConverter<DataSet>(factory);
		List<DataSet> datasets =converter.convert(rs);
		// |-forecast
		// |		|-participant1
		// |           			 |-e1,12:00
		// |                     |-e2,12:15
		// |        |-participant2
	    // |           		     |-e5,12:00	
		// |                     |-e6,12:15
		// |-usage    
		// |        |-participant1
		// |           			 |-e3,12:00
		// |                     |-e4,12:15
		// |        |-participant2	
		// |           		     |-e7,12:00	
		// |                     |-e8,12:15
		
		//forecast
		assertEquals(2,datasets.size());
		DataSet set1 = datasets.get(0);
		assertEquals("forecast",set1.getDataSetName());
		
		assertEquals(2,set1.dataSource.size());
		
		DataSource source1 = set1.getDataSource().get("source1");
		assertEquals("participant1",source1.getDataSourceName());
		assertEquals(2,source1.getDataEntries().size());
		DataEntry entry1 = source1.getDataEntries().get(0);
		assertEquals("e1",entry1.getDataEntryId());
		assertEquals("12:00",entry1.getTime());
		DataEntry entry2 = source1.getDataEntries().get(1);
		assertEquals("e2",entry2.getDataEntryId());
		assertEquals("12:15",entry2.getTime());
		
		DataSource source2 =set1.getDataSource().get("source2");
		assertEquals("participant2",source2.getDataSourceName());
		assertEquals(2,source2.getDataEntries().size());
		DataEntry entry3 = source2.getDataEntries().get(0);
		assertEquals("e5",entry3.getDataEntryId());
		assertEquals("12:00",entry3.getTime());
		DataEntry entry4 = source2.getDataEntries().get(1);
		assertEquals("e6",entry4.getDataEntryId());
		assertEquals("12:15",entry4.getTime());
		
		//usage
		assertEquals(2,datasets.size());
		DataSet set2 = datasets.get(1);
		assertEquals("usage",set2.getDataSetName());
		
		assertEquals(2,set2.dataSource.size());
		
		source1 = set2.getDataSource().get("source1");
		assertEquals("participant1",source1.getDataSourceName());
		assertEquals(2,source1.getDataEntries().size());
		DataEntry entry5 = source1.getDataEntries().get(0);
		assertEquals("e3",entry5.getDataEntryId());
		assertEquals("12:00",entry5.getTime());
		DataEntry entry6 = source1.getDataEntries().get(1);
		assertEquals("e4",entry6.getDataEntryId());
		assertEquals("12:15",entry6.getTime());
		
		source2 =set2.getDataSource().get("source2");
		assertEquals("participant2",source2.getDataSourceName());
		assertEquals(5,source2.getDataEntries().size());
		DataEntry entry7 = source2.getDataEntries().get(0);
		assertEquals("e7",entry7.getDataEntryId());
		assertEquals("12:00",entry7.getTime());
		DataEntry entry8 = source2.getDataEntries().get(1);
		assertEquals("e8",entry8.getDataEntryId());
		assertEquals("12:15",entry8.getTime());
		
		EasyMock.verify(metaData);
	}
	
	@Test
	public void testDataSourceDataSetDataEntryWithOrderedRows() throws SQLException{
		int colCount = 6;
		//map each column to SimpleBean's property with same name
		Object[][] data = {
			//1              2       3            4            5               6
			//dataEntryId   time     dataSetId  dataSetName   dataSourceId  dataSourceName
			{"e1",        "12:00",   "type1",  "forecast",   "source1",    "participant1"},
			{"e2",        "12:15",   "type1",  "forecast",   "source1",    "participant1"},
			{"e3",        "12:00",   "type2",  "usage",      "source1",    "participant1"},
			{"e4",        "12:15",   "type2",  "usage",      "source1",    "participant1"},
			{"e5",        "12:00",   "type1",  "forecast",   "source2",    "participant2"},
			{"e6",        "12:15",   "type1",  "forecast",   "source2",    "participant2"},
			{"e7",        "12:00",   "type2",  "usage",      "source2",    "participant2"},
			{"e8",        "12:15",   "type2",  "usage",      "source2",    "participant2"},
			{"e9",        "12:30",   "type2",  "usage",      "source2",    "participant2"},
			{"e10",       "12:45",   "type2",  "usage",      "source2",    "participant2"},
			{"e11",       "13:00",   "type2",  "usage",      "source2",    "participant2"},
		};
		
		String columns[]= {"dataEntryId","time","dataSetId","dataSetName","dataSourceId","dataSourceName"};
		
		ResultSetMetaData metaData = EasyMock.createMock(ResultSetMetaData.class);
		ResultSet rs = (ResultSet) Proxy.newProxyInstance(getClass().getClassLoader(),
				new Class[]{ResultSet.class},
				new MockResultSet(metaData,data,columns));
		
		
		EasyMock.expect(metaData.getColumnCount()).andReturn(colCount).anyTimes();
		for(int i = 0;i<columns.length;i++){
			EasyMock.expect(metaData.getColumnLabel(i+1)).andReturn(columns[i]).anyTimes();
		}
		
		EasyMock.replay(metaData);
		
		Map<String,BeanFactory<?>> factories = new HashMap<String,BeanFactory<?>>();
		factories.put("/",new ColumnAsFeatureFactory<DataSource>(DataSource.class,"dataSourceId"));
		factories.put("/dataSet",new ColumnAsFeatureFactory<DataSet>(DataSet.class,"dataSourceId","dataSetId"));
		factories.put("/dataSet/dataEntry",new ColumnAsFeatureFactory<DataEntry>(DataEntry.class));
		
		HierarchyFactory<DataSource> factory= new HierarchyFactory<DataSource>(factories){
			private static final long serialVersionUID = 2468387495125385719L;
			@Override
			public void buildUp(Object parent, Object child, String path) {
				if(path.equals("/dataSet")){
					DataSource source = (DataSource) parent;
					DataSet set = (DataSet)child;
					source.addDataSet(set.getDataSetId(), set);
				}else if(path.equals("/dataSet/dataEntry")){
					DataSet set = (DataSet)parent;
					DataEntry entry = (DataEntry)child;
					set.getDataEntries().add(entry);
				}
			}
		};
		
		ListConverter<DataSource> converter = new ListConverter<DataSource>(factory);
		List<DataSource> dataSources =converter.convert(rs);
		
		// |-Participant1
		// |		   |-forecast
		// |           			 |-e1,12:00
		// |                     |-e2,12:15
		// |           |-usage
		// |           			 |-e3,12:00
		// |                     |-e4,12:15
		// |-Participant2    
		// |           |-forecast
		// |           			 |-e5,12:00
		// |                     |-e6,12:15
		// |           |-usage	
		// |           			 |-e7,12:00
		// |                     |-e8,12:15
		
		//participant1
		assertEquals(2,dataSources.size());
		DataSource source1 = dataSources.get(0);
		assertEquals("participant1",source1.getDataSourceName());
		assertEquals(2,source1.dateSets.size());
		//forecast
		DataSet set1 = source1.getDateSets().get("type1");
		assertEquals("forecast",set1.getDataSetName());
		assertEquals(2,set1.getDataEntries().size());
		
		DataEntry entry1 = set1.getDataEntries().get(0);
		assertEquals("e1",entry1.getDataEntryId());
		assertEquals("12:00",entry1.getTime());
		DataEntry entry2 = set1.getDataEntries().get(1);
		assertEquals("e2",entry2.getDataEntryId());
		assertEquals("12:15",entry2.getTime());
		
		//usage
		DataSet set2 = source1.getDateSets().get("type2");
		assertEquals("usage",set2.getDataSetName());
		assertEquals(2,set2.getDataEntries().size());
		
		DataEntry entry3 = set2.getDataEntries().get(0);
		assertEquals("e3",entry3.getDataEntryId());
		assertEquals("12:00",entry3.getTime());
		DataEntry entry4 = set2.getDataEntries().get(1);
		assertEquals("e4",entry4.getDataEntryId());
		assertEquals("12:15",entry4.getTime());
		
		//participant2
		DataSource source2 = dataSources.get(1);
		assertEquals("participant2",source2.getDataSourceName());
		assertEquals(2,source2.dateSets.size());
		
		//forecast
		set1 = source2.getDateSets().get("type1");
		assertEquals("forecast",set1.getDataSetName());
		assertEquals(2,set1.getDataEntries().size());
		DataEntry entry5 = set1.getDataEntries().get(0);
		assertEquals("e5",entry5.getDataEntryId());
		assertEquals("12:00",entry5.getTime());
		DataEntry entry6 = set1.getDataEntries().get(1);
		assertEquals("e6",entry6.getDataEntryId());
		assertEquals("12:15",entry6.getTime());
		
		set2 = source2.getDateSets().get("type2");
		assertEquals("usage",set2.getDataSetName());
		assertEquals(5,set2.getDataEntries().size());
		DataEntry entry7 = set2.getDataEntries().get(0);
		assertEquals("e7",entry7.getDataEntryId());
		assertEquals("12:00",entry7.getTime());
		DataEntry entry8 = set2.getDataEntries().get(1);
		assertEquals("e8",entry8.getDataEntryId());
		assertEquals("12:15",entry8.getTime());
		
		EasyMock.verify(metaData);
	}
	
	@Test
	public void testMultipleLeaves() throws SQLException{
		int colCount = 6;
		//map each column to SimpleBean's property with same name
		Object[][] data = {
			//1              2       3            4            5               6
			//dataEntryId   time     dataSetId  dataSetName   dataSourceId  dataSourceName
			{"e1",        "12:00",   "type1",  "forecast",   "source1",    "participant1"},
			{"e2",        "12:15",   "type1",  "forecast",   "source1",    "participant1"},
			{"e3",        "12:00",   "type2",  "usage",      "source1",    "participant1"},
			{"e4",        "12:15",   "type2",  "usage",      "source1",    "participant1"},
			{"e5",        "12:00",   "type1",  "forecast",   "source2",    "participant2"},
			{"e6",        "12:15",   "type1",  "forecast",   "source2",    "participant2"},
			{"e7",        "12:00",   "type2",  "usage",      "source2",    "participant2"},
			{"e8",        "12:15",   "type2",  "usage",      "source2",    "participant2"},
			{"e9",        "12:30",   "type2",  "usage",      "source2",    "participant2"},
			{"e10",       "12:45",   "type2",  "usage",      "source2",    "participant2"},
			{"e11",       "13:00",   "type2",  "usage",      "source2",    "participant2"},
		};
		
		String columns[]= {"dataEntryId","time","dataSetId","dataSetName","dataSourceId","dataSourceName"};
		
		ResultSetMetaData metaData = EasyMock.createMock(ResultSetMetaData.class);
		ResultSet rs = (ResultSet) Proxy.newProxyInstance(getClass().getClassLoader(),
				new Class[]{ResultSet.class},
				new MockResultSet(metaData,data,columns));
		
		
		EasyMock.expect(metaData.getColumnCount()).andReturn(colCount).anyTimes();
		for(int i = 0;i<columns.length;i++){
			EasyMock.expect(metaData.getColumnLabel(i+1)).andReturn(columns[i]).anyTimes();
		}
		
		EasyMock.replay(metaData);
		
		Map<String,BeanFactory<?>> factories = new HashMap<String,BeanFactory<?>>();
		factories.put("/",new ColumnAsFeatureFactory<DataEntry>(DataEntry.class,"dataSourceId","dataSetId","time"));
		factories.put("/dataSet",new ColumnAsFeatureFactory<DataSet>(DataSet.class,"dataSetId"));
		factories.put("/dataSource",new ColumnAsFeatureFactory<DataSource>(DataSource.class,"dataSourceId"));
		
		HierarchyFactory<DataEntry> factory= new HierarchyFactory<DataEntry>(factories){
			private static final long serialVersionUID = 2468387495125385719L;
			@Override
			public void buildUp(Object parent, Object child, String path) {
				if(path.equals("/dataSet")){
					DataEntry entry = (DataEntry) parent;
					DataSet set = (DataSet)child;
					entry.setDataSet(set);
					
				}else if(path.equals("/dataSource")){
					DataEntry entry = (DataEntry)parent;
					DataSource source = (DataSource)child;
					entry.setDataSource(source);
				}
			}
		};
		
		ListConverter<DataEntry> converter = new ListConverter<DataEntry>(factory);
		List<DataEntry> dataEntries =converter.convert(rs);
		
		assertEquals(11,dataEntries.size());
		//entry1
		DataEntry entry1 = dataEntries.get(0);
		assertEquals("e1",entry1.getDataEntryId());
		assertEquals("12:00",entry1.getTime());
		
		DataSource source1 = entry1.getDataSource();
		assertEquals("participant1",source1.getDataSourceName());
		DataSet set1 = entry1.getDataSet();
		assertEquals("forecast",set1.getDataSetName());
		
		//entry11
		DataEntry entry11 = dataEntries.get(10);
		assertEquals("e11",entry11.getDataEntryId());
		assertEquals("13:00",entry11.getTime());
		
		DataSource source2 = entry11.getDataSource();
		assertEquals("participant2",source2.getDataSourceName());
		DataSet set2 = entry11.getDataSet();
		assertEquals("usage",set2.getDataSetName());
		
		EasyMock.verify(metaData);
	}
	
	@Test
	public void testMultipleLeavesWithNull() throws SQLException{
		int colCount = 6;
		//map each column to SimpleBean's property with same name
		Object[][] data = {
			//1              2       3            4            5               6
			//dataEntryId   time     dataSetId  dataSetName   dataSourceId  dataSourceName
			{"e1",        "12:00",   "type1",  "forecast",   null,          "participant1"},
			{"e2",        "12:15",   "type1",  "forecast",   "source1",    "participant1"},
			{"e3",        "12:00",   "type2",  "usage",      "source1",    "participant1"},
			{"e4",        "12:15",   "type2",  "usage",      "source1",    "participant1"},
			{"e5",        "12:00",   "type1",  "forecast",   "source2",    "participant2"},
			{"e6",        "12:15",   "type1",  "forecast",   "source2",    "participant2"},
			{"e7",        "12:00",   "type2",  "usage",      "source2",    "participant2"},
			{"e8",        "12:15",   "type2",  "usage",      "source2",    "participant2"},
			{"e9",        "12:30",   "type2",  "usage",      "source2",    "participant2"},
			{"e10",       "12:45",   "type2",  "usage",      "source2",    "participant2"},
			{"e11",       "13:00",   "type2",  "usage",      "source2",    "participant2"},
		};
		
		String columns[]= {"dataEntryId","time","dataSetId","dataSetName","dataSourceId","dataSourceName"};
		
		ResultSetMetaData metaData = EasyMock.createMock(ResultSetMetaData.class);
		ResultSet rs = (ResultSet) Proxy.newProxyInstance(getClass().getClassLoader(),
				new Class[]{ResultSet.class},
				new MockResultSet(metaData,data,columns));
		
		
		EasyMock.expect(metaData.getColumnCount()).andReturn(colCount).anyTimes();
		for(int i = 0;i<columns.length;i++){
			EasyMock.expect(metaData.getColumnLabel(i+1)).andReturn(columns[i]).anyTimes();
		}
		
		EasyMock.replay(metaData);
		
		Map<String,BeanFactory<?>> factories = new HashMap<String,BeanFactory<?>>();
		factories.put("/",new ColumnAsFeatureFactory<DataEntry>(DataEntry.class,"dataEntryId"));
		factories.put("/dataSet",new ColumnAsFeatureFactory<DataSet>(DataSet.class,"dataSetId"));
		factories.put("/dataSource",new ColumnAsFeatureFactory<DataSource>(DataSource.class,"dataSourceId"));
		
		HierarchyFactory<DataEntry> factory= new HierarchyFactory<DataEntry>(factories,true){
			private static final long serialVersionUID = 2468387495125385719L;
			@Override
			public void buildUp(Object parent, Object child, String path) {
				if(path.equals("/dataSet")){
					DataEntry entry = (DataEntry) parent;
					DataSet set = (DataSet)child;
					entry.setDataSet(set);
					
				}else if(path.equals("/dataSource")){
					DataEntry entry = (DataEntry)parent;
					DataSource source = (DataSource)child;
					entry.setDataSource(source);
				}
			}
		};
		
		ListConverter<DataEntry> converter = new ListConverter<DataEntry>(factory);
		List<DataEntry> dataEntries =converter.convert(rs);
		
		assertEquals(11,dataEntries.size());
		//entry1
		DataEntry entry1 = dataEntries.get(0);
		assertEquals("e1",entry1.getDataEntryId());
		assertEquals("12:00",entry1.getTime());
		
		DataSource source1 = entry1.getDataSource();
		assertTrue(source1==null);
		DataSet set1 = entry1.getDataSet();
		assertEquals("forecast",set1.getDataSetName());
		
		//entry11
		DataEntry entry11 = dataEntries.get(10);
		assertEquals("e11",entry11.getDataEntryId());
		assertEquals("13:00",entry11.getTime());
		
		DataSource source2 = entry11.getDataSource();
		assertEquals("participant2",source2.getDataSourceName());
		DataSet set2 = entry11.getDataSet();
		assertEquals("usage",set2.getDataSetName());
		
		EasyMock.verify(metaData);
	}
	
	@Test
	public void testMultipleLeavesWithSimpleAtt() throws SQLException{
		int colCount = 6;
		String columns[]= {"uuid","participantName","account","programName","eventId","childParticipant"};
		
		//map each column to SimpleBean's property with same name
		Object[][] data = {
			//1           2                  3          4            5               6
			//uuid        participantName    account    programName  eventId        childName
			{"1",        "p1",   			"account1",  "DEMO",  	 "demo_evt1",   "p2(demo-p1/p2)"},
			{"1",        "p1",              "account1",  "DEMO",   	 "cpp_evt1",    "p2(demo-p1/p2)"},
			{"1",        "p1",        	      "account1",  "CPP",      "demo_evt1",   "p2(cpp-p1/p2)"},
			{"1",        "p1",              "account1",  "CPP",      "cpp_evt1",    "p2(cpp-p1/p2)"},
			{"2",        "p2",              "account2",  "DEMO",     "demo_evt1",   "p3(demo-p2/p3)"},
			{"2",        "p2",              "account2",  "DEMO",     "cpp_evt1",    "p3(demo-p2/p3)"},
			{"2",        "p2",              "account2",  "CPP",      "demo_evt1",   "p3(cpp-p2/p3)"},
			{"2",        "p2",              "account2",  "CPP",      "cpp_evt1",    "p3(cpp-p2/p3)"},
		};
		
		ResultSetMetaData metaData = EasyMock.createMock(ResultSetMetaData.class);
		ResultSet rs = (ResultSet) Proxy.newProxyInstance(getClass().getClassLoader(),
				new Class[]{ResultSet.class},
				new MockResultSet(metaData,data,columns));
		
		
		EasyMock.expect(metaData.getColumnCount()).andReturn(colCount).anyTimes();
		for(int i = 0;i<columns.length;i++){
			EasyMock.expect(metaData.getColumnLabel(i+1)).andReturn(columns[i]).anyTimes();
		}
		EasyMock.replay(metaData);
		
		
		Map<String,BeanFactory<?>> factories = new HashMap<String,BeanFactory<?>>();
		factories.put("/", ColumnAsFeatureFactory.make(ParticipantOverview.class,"uuid"));
		factories.put("/program",ColumnAsObjectFactory.make(String.class, "programName","uuid","programName"));
		factories.put("/program/aggregation",ColumnAsObjectFactory.make(String.class, "childParticipant","uuid","childParticipant"));
		HierarchyFactory<ParticipantOverview> factory= new HierarchyFactory<ParticipantOverview>(factories,true){
			private static final long serialVersionUID = -582937705196076890L;
			@Override
			public void buildUp(Object parent, Object child, String path) {
				if(path.equals("/program")){
					//ParticipantOverview p = (ParticipantOverview) parent;
					//String  program =  (String)child;
					//p.get
				}
				else if(path.equals("/program/aggregation")){
					ParticipantOverview p =(ParticipantOverview) getCurrent("/");
					String  program =  (String)parent;
					String childParticipant =(String)child;
					if(childParticipant!=null){
						p.addAggregation(program, childParticipant);
					}
				}
			}
		};
		
		ListConverter<ParticipantOverview> converter = new ListConverter<ParticipantOverview>(factory);
		List<ParticipantOverview> parts =converter.convert(rs);
		 
		assertEquals(2,parts.size());
		ParticipantOverview p1 = parts.get(0);
		assertEquals("CPP,DEMO",p1.getProgramNames());
		assertEquals(2,p1.getPrograms().keySet().size());
		assertEquals(1,p1.getPrograms().get("DEMO").size());
		assertEquals("p2(demo-p1/p2)",p1.getPrograms().get("DEMO").get(0));
	}
	
	
	public static class ParticipantOverview {
		
		private String uuid;
		private String participantName;
		private String account;
		
		private String nonOptOutEvents;
		private int aggregationCount;
		private Map<String,List<String>> programs;
		
		public String getProgramNames() {
			List<String> programs = Arrays.asList(getPrograms().keySet().toArray(new String[0]));
			Collections.sort(programs);
			String result ="";
			for(String p: programs){
				result=result.concat(","+p);
			}
			if(result.equals(""))
				return null;
			else
				return result.substring(1);
		}
		
		public String getParticipantName() {
			return participantName;
		}
		public void setParticipantName(String participantName) {
			this.participantName = participantName;
		}
		public String getAccount() {
			return account;
		}
		public void setAccount(String account) {
			this.account = account;
		}
		
		public String getNonOptOutEvents() {
			return nonOptOutEvents;
		}
		
		public void setNonOptOutEvents(String nonOptOutEvents) {
			this.nonOptOutEvents = nonOptOutEvents;
		}
		
		public int getAggregationCount() {
			return aggregationCount;
		}
		
		public void setAggregationCount(int aggregationCount) {
			this.aggregationCount = aggregationCount;
		}
		public String getUuid() {
			return uuid;
		}
		public void setUuid(String uuid) {
			this.uuid = uuid;
		}
		
		public Map<String,List<String>> getPrograms() {
			if(programs==null)
				programs = new HashMap<String,List<String>>();
			return programs;
		}
		
		public void addAggregation(String programName,String participant){
			Map<String,List<String>> programs = this.getPrograms();
			List<String> parts = programs.get(programName);
			if(parts==null){
				parts = new ArrayList<String>();
				programs.put(programName, parts);
			}
			if(participant==null) return;
			if(!parts.contains(participant)){
				parts.add(participant);
			}
		}
		
	}
}
