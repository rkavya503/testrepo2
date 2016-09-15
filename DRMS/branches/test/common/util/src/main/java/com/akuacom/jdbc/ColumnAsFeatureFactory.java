package com.akuacom.jdbc;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * A <code>ColumnAsFeatureFactory</code> will create a pojo using its default constructor, and then 
 * map each column in <code> java.sql.Result</code> into one property in pojo. 
 * 
 * <p>
 * <li> Firstly Use column label, see {@link java.sql.ResultSetMetaDatat} 
 * 	as the key to find property name in {@link #columnToFeatureMap} if it is not null
 * </li>
 * <li> if No mapped property name for the column label, then use column label as property name 
 * </li>
 * <li> if No mapped property name found or can not assign column value to the property,
 *      this column will be ignored
 * </li>
 */
public class ColumnAsFeatureFactory<T> implements BeanFactory<T>, Serializable {

	private static final long serialVersionUID = -1527311248918426186L;

	private Class<T> type;
	
	private Map<String,String> columnToFeatureMap;
	
	private Constructor<T> constructor;
	
	private String[] keyColumns;
	
	private List<Integer> columns;

	private List<Method> setters;
	
	public static <F> ColumnAsFeatureFactory<F> make(Class<F> type){
		return new ColumnAsFeatureFactory<F>(type);
	}
	
	public static <F> ColumnAsFeatureFactory<F> make(Class<F> type, Map<String,String> columnToFeatureMap){
		return new ColumnAsFeatureFactory<F>(type,columnToFeatureMap);
	}
	
	public static <F> ColumnAsFeatureFactory<F> make(Class<F> type, Map<String,String> columnToFeatureMap,String... keyColumns){
		return new ColumnAsFeatureFactory<F>(type,columnToFeatureMap,keyColumns);
	}
	
	public static <F> ColumnAsFeatureFactory<F> make(Class<F> type,String... keyColumns){
		return new ColumnAsFeatureFactory<F>(type,keyColumns);
	}
	
	public ColumnAsFeatureFactory(Class<T> type, Map<String,String> columnToFeatureMap){
		this.type= type;
		this.columnToFeatureMap = columnToFeatureMap;
	}
	
	public ColumnAsFeatureFactory(Class<T> type, Map<String,String> columnToFeatureMap,String... keyColumns){
		this(type,columnToFeatureMap);
		this.keyColumns  = keyColumns;
	}
	
	public ColumnAsFeatureFactory(Class<T> type){
		this.type = type;
	}
	
	public ColumnAsFeatureFactory(Class<T> type,String... keyColumns){
		this(type);
		this.keyColumns =keyColumns;
	}
	
	@Override
	public T createInstance(ResultSet rs) {
		try{
			return	doCreateInstance(rs);
		}catch(Exception e){
			throw new BeanCreationException(e);
		}
	}
	
	protected T doCreateInstance(ResultSet rs) throws Exception{
		T instance = null;
		for(String key:this.getKeyColumns()){
			if(rs.getObject(key)==null) return null;
		}
		
		if(columns==null){
			Method writeMethod  = null;
			String featureName = null;
			instance = newInstance();
			columns = new ArrayList<Integer>();
			setters = new ArrayList<Method>();
			
			if(columnToFeatureMap==null)	columnToFeatureMap = Collections.emptyMap();
			ResultSetMetaData metadata =rs.getMetaData();
			for(int i =1  ; i<= metadata.getColumnCount();i++){
				String colName = metadata.getColumnLabel(i);
				featureName = columnToFeatureMap.get(colName);
				if(featureName==null) featureName = colName;
				writeMethod = getSetter(type,featureName);
				if(writeMethod!=null){
					Object value = rs.getObject(i);
					doSetProperty(instance,writeMethod,value);
					//cache for later use
					columns.add(i);
					setters.add(writeMethod);
				}
			}
			return instance;
		}else{
			instance = newInstance();
			for(int i = 0; i< columns.size();i++){
				Object value = rs.getObject(columns.get(i));
				doSetProperty(instance,setters.get(i),value);
			}
			return instance;
		}
	}
	
	protected void doSetProperty(Object instance,Method writeMethod,Object value){
		Class<?> expectType = null;
		Class<?> actualType = null;
		try{
			expectType = writeMethod.getParameterTypes()[0];
			if(value!=null){
				actualType = value.getClass();
				if(!JavaTypes.isAutoBoxing(actualType, expectType) && !expectType.isInstance(value)){
					value = JavaTypes.forceCast(value, expectType);
				}
				writeMethod.invoke(instance, value);
			}else{
				if(expectType.isPrimitive()){
					//force a default value if null
					switch(JavaTypes.getTypeId(expectType)){
					case JavaTypes.T_BOOLEAN:
						value =false;
						break;
					case JavaTypes.T_CHAR:
						value = '\0';
						break;
					case JavaTypes.T_FLOAT:
					case JavaTypes.T_BYTE:
					case JavaTypes.T_DOUBLE:
					case JavaTypes.T_INT:
					case JavaTypes.T_LONG:
					case JavaTypes.T_SHORT:
						value =0;
						break;
					}
				}
				writeMethod.invoke(instance, value);
			}
		}catch(Exception e){
			String msg ="";
			if(instance==null){
				msg = "can not initialize "+type.getName();
			}else{
				msg =" can not set property: "+type.getName()+"."+writeMethod.getName();
			}
			if(writeMethod!=null){
				msg +=" expect type "+expectType +", actual type "+actualType;
			}
			throw new BeanCreationException(msg,e);
		}
	}
	
	public static Method getSetter(Class<?> type,String featureName){
		Method methods[] =type.getMethods();
		String setter = "set"+capName(featureName);
		for(Method method:methods){
			if(method.getName().equals(setter)
					&& method.getParameterTypes().length==1
				    && method.getReturnType().getName().equals("void")
				    && Modifier.isPublic(method.getModifiers())) {
				return method;
			}
		}
		return null;
	}
	
	public static String capName(String name){
	    if (name.length() == 0)
	      return name;
	    else
	      return name.substring(0,1).toUpperCase() + name.substring(1);
	}
	
	public static String uncapName(String name){
	    if (name.length() == 0)
	      return name;
	    else
	      return name.substring(0,1).toLowerCase() + name.substring(1);
	}
	
	protected T newInstance() throws Exception{
		if(constructor==null){
			try {
				constructor = type.getConstructor();
			} catch (Exception e) {
				throw new BeanCreationException("No constructor wihtout a parameter found",e);
			}
		}
		return constructor.newInstance();
	}

	public String[] getKeyColumns() {
		if(this.keyColumns!=null)
			return keyColumns;
		else
			return new String[] {};
	}
	
	
}
