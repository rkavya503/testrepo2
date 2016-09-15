package com.akuacom.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * <tt>HierarchyFactory</tt> supports creation of a object graph based on its sub factories and 
 * the relation ship between them. For example
 * 
 * <PRE>
 * Map<String,BeanFactory<?>> factories = new HashMap<String,BeanFactory<?>>();
 * factories.put("/",new ColumnAsFeatureFactory<DataSource>(DataSource.class,"dataSourceId"));
 * factories.put("/dataSet",new ColumnAsFeatureFactory<DataSet>(DataSet.class,"dataSourceId","dataSetId"));
 * factories.put("/dataSet/dataEntry",new ColumnAsFeatureFactory<DataEntry>(DataEntry.class));
 *		
 * HierarchyFactory<DataSource> factory= new HierarchyFactory<DataSource>(factories){
 * private static final long serialVersionUID = 2468387495125385719L;
 *	@Override
 *		public void buildUp(Object parent, Object child, String path) {
 *			if(path.equals("/dataSet")){
 *				DataSource source = (DataSource) parent;
 *				DataSet set = (DataSet)child;
 *				source.addDataSet(set.getDataSetId(), set);
 *			}else if(path.equals("/dataSet/dataEntry")){
 *				DataSet set = (DataSet)parent;
 *				DataEntry entry = (DataEntry)child;
 *				set.getDataEntries().add(entry);
 *			}
 *		}
 *	};
 *		
 *	ListConverter<DataSource> converter = new ListConverter<DataSource>(factory);
 *	List<DataSource> dataSources =converter.convert(rs);
 *  //return result - top level object is DataSource
 *  //and each DataSource object contains multiple DataSet Object
 *  //and each DataSet Object contains muitiple DataEntry object
 *	</PRE>
 *   
 */

public abstract class HierarchyFactory<E> implements BeanFactory<E> {
	
	public static final String ROOT= "/";
	
	private static final long serialVersionUID = 4223679071064224004L;
	
	private Map<Integer,Object> createdObjects;
	
	private int maxDepth = 0;
	
	private static final int INT_NO_OBJECT = -1;
	
	private boolean ordered = false;
	
	private String paths[];
	
	private boolean leaves[];
	
	//object will be return for current row
	private int currentKeys[];
	
	//object key values for current row in ResultSet
	private Object currentValues[];
	
	private BeanFactory<?> beanFactory[];
	
	private boolean newInstance[];
	
	private Map<String,Integer> pathIndex;
	
	public HierarchyFactory(Map<String,BeanFactory<?>> factories){
		if(!ordered)
			createdObjects   = new HashMap<Integer,Object>();
		
		int size = factories.keySet().size();
		paths = new String [ size];
		currentValues = new Object[size];
		leaves		  = new boolean[size];
		beanFactory   = new BeanFactory[size];
		currentKeys   = new int[size];
		newInstance   = new boolean[size];
		pathIndex     = new HashMap<String,Integer>(size);
		
		newInstance[size-1] = true;
		
		int i =0;
		for(String path:factories.keySet()){
			int depth = depthOf(path);
			if(depth>maxDepth){
				maxDepth = depth;
			}
			paths[i++] = path;
		}
		paths = sortPath(paths);
		
		for(i = 0; i<size; i++){
			boolean leaf = true;
			for(int j = i+1; j<paths.length ; j++){
				if(paths[j].startsWith(paths[i])){
					leaf = false;
				}
			}
			pathIndex.put(paths[i], i);
			beanFactory[i] = factories.get(paths[i]);
			leaves[i] = leaf;
		}
	}
	
	public HierarchyFactory(Map<String,BeanFactory<?>> subfactories, boolean ordered){
		this(subfactories);
		this.ordered = ordered;
	}
	
	@Override
	public E createInstance(ResultSet rs) throws SQLException {
		E result = null;
		for(int i = 0 ; i< paths.length ; i++){
			String path = paths[i];
			int hashCode = getHashCode(i,rs);
			currentValues[i]  = getInstance(i,hashCode,rs);
			if( (leaves[i] || newInstance[i]) && !path.equals(ROOT)){
				Object parent = currentValues[pathIndex.get(parentOf(path))];
				Object kid = currentValues[i];
				if(parent!=null && kid!=null){
					buildUp(parent,kid,path);
				}
			}
		}
		if(newInstance[0]){
			result= (E) currentValues[0];
		}
		return result;
	}
	
	abstract public void buildUp(Object parent,Object child,String path);
	
	protected Object getCurrent(String path){
		return currentValues[pathIndex.get(path)];
	}
	
	private int getHashCode(int index,ResultSet rs) throws SQLException{
		if(leaves[index] && beanFactory[index].getKeyColumns().length == 0){
			return 0;
		}
		
		String path = paths[index];
		HashCodeBuilder hb = new HashCodeBuilder();
		BeanFactory<?> subfactory = beanFactory[index];
		for(String key: subfactory.getKeyColumns()){
			Object keyobj =rs.getObject(key);
			//NULL key value which means no object create for this row
			if(keyobj==null) return INT_NO_OBJECT;
			hb.append(keyobj);
		}
		hb.append("___"+path);
		int hashCode = hb.toHashCode();
		return hashCode;
	}
	
	protected Object getInstance(int index,int hashCode,ResultSet rs) throws SQLException{
		Object obj = null;
		if(this.leaves[index]){
			obj = beanFactory[index].createInstance(rs);
			return obj;
		}else{
			if(hashCode==currentKeys[index]){
				obj = currentValues[index];
			}else{
				currentKeys[index]= hashCode;
			}
		
			if(obj==null && !ordered){
				obj=createdObjects.get(hashCode);
			}
			if(obj==null){
				//not exist, create one
				BeanFactory<?> factory = beanFactory[index];
				obj = factory.createInstance(rs);
				newInstance[index] =true;
				if(!ordered){
					createdObjects.put(hashCode,obj);
				}
			}else{
				newInstance[index] =false;
			}
			return obj;
		}
	}
	
	private static int depthOf(String path){
		if(path==null) return 0;
		path = path.trim();
		if(!path.startsWith("/")){
			throw new IllegalArgumentException("path must start with /");
		}
		if(path.equals("/"))
			return 0;
		
		if(path.endsWith("/")){
			throw new IllegalArgumentException("path must not end with /");
		}
		path = path.substring(1,path.length());
		
		return path.split("\\/").length;
	}
	
	private static String parentOf(String path){
		int depth = depthOf(path);
		switch(depth){
			case 0: return null;
			case 1: return ROOT;
			default:
				int i = path.lastIndexOf('/');
				return path.substring(0,i);
		}
	}
	
	public String[] getKeyColumns() {
		return beanFactory[0].getKeyColumns();
	}
	
	protected static String[]  sortPath(String[] paths){
		 Arrays.sort(paths, new Comparator<String>(){
			@Override
			public int compare(String o1, String o2) {
				int depth1 = depthOf(o1);
				int depth2= depthOf(o2);
				if(depth1!=depth2){
					return depth1- depth2;
				}else{
					return o1.compareTo(o2);
				}
			}
		});
		return paths;
	}
}
