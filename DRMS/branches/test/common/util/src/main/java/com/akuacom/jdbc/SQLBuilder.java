package com.akuacom.jdbc;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.akuacom.jdbc.SearchConstraint.ORDER;

/**
 *  <tt>SQLBuilder</tt> can build a templated sql which is not a qualified sql statement to a  sql statement
 *  <p>
 *  Usually in report query, sql statement is dynamic, it is determined by how many parameters are used and which parameters? 
 *  SQL builder can replace parameter name with correct values and also removed that conditions if specified parameter name
 *  can not be found in parameter map.
 *  <p>
 *  Another benefit of <tt>SQLBuilder </tt> is that it can process parameter and add single quotations as required for you. 
 *   
 *  For example 
 *  <PRE>
 *    String sqltemplate = "select * from participant where 1=1 [and participantId in ${partIds}] [[and name like ${participantName}] [ ${orderBy}";
 *    Map<String,Object> params = new HashMap<String,Object>();
 *    String qualifiedSQL= SQLBuilder.buildSQL(str, params);
 *    //qualifiedSQL is: "select * from participant where 1=1";
 *  	
 *    List<Integer> ids = Arrays.asList(1,2,3,4);
 *    params.put("partIds", ids);
 *    qualifiedSQL= SQLBuilder.buildSQL(str, params);
 *    //qualifiedSQL now is: select * from participant where 1=1 and participantId in (1,2,3,4)"
 *      
 *    params.put("participantName", "part1%");
 *    qualifiedSQL= SQLBuilder.buildSQL(str, params);
 *    //qualifiedSQL now is: select * from participant where 1=1 and participantId in (1,2,3,4) and name like 'part1%'
 *    
 *    SQLWord word = new SQLWord("order by name desc");
 *    params.put("orderBy", word);
 *    qualifiedSQL= SQLBuilder.buildSQL(str, params);
 *    //qualifiedSQL now is: select * from participant where 1=1 and participantId in (1,2,3,4) and name like 'part1%' order by name desc
 *  </PRE>
 */
public class SQLBuilder {
	
	private static final String SINGLE_QUOTATION_MARK="'";
	
	//match [ and p.participantName = ${participantName}]  or p.participantName = ${participantName}
	private static Pattern p  = Pattern.compile("(\\[[^\\]]*)?(\\$\\{\\s*(\\w+)\\s*\\})([^\\[]*\\])?", Pattern.MULTILINE);
	
	
	
    /**
	 * @param sqltemplate the sqltemplate which can be built into different qualified sql 
	 * 		  statement according to parameters
	 * @param parameters the parameter name and value pairs
	 * @return qualified sql statement
	 * @throws SQLBuilderException if illegal template 
	 */
	public static String buildSQL(String sqltemplate,Map<String,Object> parameters)
								throws SQLBuilderException{
		//example: select * from participant where 1=1 [and name =  ${participantName}] and status = ${status}"; 
		Matcher m = p.matcher(sqltemplate);
        StringBuffer sb = new StringBuffer();
        while (m.find()){
        	//groups 
        	//   0 [and name =  ${participantName}] 
        	//   1 [and name =   or null 
        	//   2 ${participantName} 
        	//   3 participantName
        	//   4 ] or null
        	String start = m.group(1); 
        	String paramName = m.group(3); 
        	String end = m.group(4); 
        	boolean containsParam = parameters.containsKey(paramName);
        	Object paramValue =null;
        	if(containsParam){
        		paramValue = parameters.get(paramName);
        		paramValue = formatParameter(paramValue);
        	}
        		
        	if(start!=null && end == null || start==null && end!=null){
        		throw new SQLBuilderException("unbalanced [ and ] -" + m.group(0));
        	}
        	if(start!=null){
        		if(paramValue!=null){
        			String replacement =m.group(1);
        			replacement= replacement.substring(1);
        			replacement+= paramValue;
        			replacement+=end.substring(0,end.length()-1);
        			m.appendReplacement(sb, replacement);
        		}else{
        			//removed this parameter
        			m.appendReplacement(sb,"");
        		}
        	}else{	
        		//0 ${status}
        		//1 null
        		//2 ${status}
        		//3 status
        		//4 null
        		m.appendReplacement(sb,""+paramValue);
        	}
        }
        m.appendTail(sb);
        return sb.toString();
	}
	
	private static int[] concat(int[] a, int b){
		if(a==null){
			return new int[]{b};
		}
		int c[] = new int[a.length+1];
		System.arraycopy(a, 0, c, 0, a.length);
		c[a.length] = b;
		return c;
	}
	
	private static void appendLocation(Map<String,int[]> map,String key,int location){
		int o[] = map.get(key);
		map.put(key, concat(o,location));
	}
	
	public static String buildNamedParameterSQL(String sqltemplate, Map<String,Object> parameters)
			throws SQLBuilderException{
		Matcher m = p.matcher(sqltemplate);
        StringBuffer sb = new StringBuffer();
		
        //build parameterized sql, sql statement with ? for parameters
		//example: select * from participant where 1=1 [and name =  ${participantName}] 
		//and status = ${status}"; 
        while (m.find()){
        	//groups 
        	//   0 [and name =  ${participantName}] or null
        	//   1 [and name =   or null 
        	//   2 ${participantName} 
        	//   3 participantName
        	//   4 ] or null
        	String start = m.group(1); 
        	String paramName = m.group(3); 
        	String end = m.group(4); 
        	boolean containsParam = parameters.containsKey(paramName);
        	Object paramValue =null;
        	if(containsParam){
        		paramValue = parameters.get(paramName);
        	}
        	
        	if(start!=null && end == null || start==null && end!=null){
        		throw new SQLBuilderException("unbalanced [ and ] -" + m.group(0));
        	}
        	
        	//check optional parameters
        	if(start!=null){
        		if(paramValue==null){
        			//remove optional parameters
            		m.appendReplacement(sb,"");
        		}else{
        			String replacement =m.group(1);
        			replacement= replacement.substring(1);
        			replacement+= ("\\${"+paramName+"}");
        			replacement+=end.substring(0,end.length()-1);
        			m.appendReplacement(sb, replacement);
        		}
        	}
        }
        m.appendTail(sb);
        return sb.toString();
	}
	/**
	 * @param sql
	 * @param parameters
	 * @return
	 */
	public static ParameterizedSQL parseNamedPamameterSQL(String sqltemplate, Map<String,Object> parameters)
				throws SQLBuilderException{
		
		Map<String,int[]> parameterIndex = new HashMap<String, int[]>();
	
		//build prepared sql, sql statement with ? for parameters
		//example: select * from participant where 1=1 [and name =  ${participantName}] 
		//and status = ${status}"; 
		Matcher m = p.matcher(sqltemplate);
        StringBuffer sb = new StringBuffer();
        int i = 0;
        while (m.find()){
        	//groups 
        	//   0 [and name =  ${participantName}] or null
        	//   1 [and name =   or null 
        	//   2 ${participantName} 
        	//   3 participantName
        	//   4 ] or null
        	String start = m.group(1); 
        	String paramName = m.group(3); 
        	String end = m.group(4); 
        	boolean containsParam = parameters.containsKey(paramName);
        	Object paramValue =null;
        	if(containsParam){
        		paramValue = parameters.get(paramName);
        	}
        	
        	if(start!=null && end == null || start==null && end!=null){
        		throw new SQLBuilderException("unbalanced [ and ] -" + m.group(0));
        	}
        	
        	if(start!=null){
        		if(paramValue!=null){
        			String replacement =m.group(1);
        			replacement= replacement.substring(1);
        			if(paramValue instanceof NonParameterizable){
        				replacement += ((NonParameterizable<?>)paramValue).getParameterizedReplacement();
        				int count = ((NonParameterizable<?>)paramValue).getParamCount(paramName);
        				if(count >0){
        					appendLocation(parameterIndex,paramName,++i);
        					i+= (count-1);
        				}
        			}else if(JavaTypes.isCollection(paramValue) || JavaTypes.isArray(paramValue)){
        				int size = JavaTypes.sizeOfCollectionOrArray(paramValue);
        				replacement+= replacementOfMany(size);
        				appendLocation(parameterIndex,paramName,++i);
        				i+=(size-1);
        			}else{
        				replacement+= "?";
        				//remember index of this parameter
	        			appendLocation(parameterIndex,paramName,++i);
        			}
        			replacement+=end.substring(0,end.length()-1);
        			m.appendReplacement(sb, replacement);
        			
        		}else{
        			//removed this parameter
        			m.appendReplacement(sb,"");
        		}
        	}else{	
        		//0 ${status}
        		//1 null
        		//2 ${status}
        		//3 status
        		//4 null
        		String replacement ="";
        		if(paramValue instanceof NonParameterizable){
    				replacement = ((NonParameterizable<?>)paramValue).getParameterizedReplacement();
    				int count = ((NonParameterizable<?>)paramValue).getParamCount(paramName);
    				if(count >0){
    					appendLocation(parameterIndex,paramName,++i);
    					i+= (count-1);
    				}
        		}else if(JavaTypes.isCollection(paramValue) || JavaTypes.isArray(paramValue)){
    				int size = JavaTypes.sizeOfCollectionOrArray(paramValue);
    				replacement+= replacementOfMany(size);
    				appendLocation(parameterIndex,paramName,++i);
    				i+=(size-1);
    			}else{
    				replacement= "?";
    				appendLocation(parameterIndex,paramName,++i);
    			}
        		m.appendReplacement(sb,replacement);
        	}
        }
        m.appendTail(sb);
	    return new ParameterizedSQL(sb.toString(),parameterIndex);
	}
	
	private static String replacementOfMany(int size){
		String str="(";
		for(int i=0;i<size;i++){
			if(i==0)
				str+="?";
			else
				str+=",?";
		}
		return str+")";
	}
	
	private static String quotatized(String value){
		return SINGLE_QUOTATION_MARK+value+SINGLE_QUOTATION_MARK;
	}
	
	public static Object formatParameter(Object value) throws SQLBuilderException{
		if(value==null) return "null";
		
		if(JavaTypes.isNumber(value) ){
			return value;
		}
		
		Class<?> type = value.getClass();
		int typeId = JavaTypes.getTypeId(type);
		switch(typeId){
		case JavaTypes.T_BOOLEAN:
		case JavaTypes.T_JAVA_LANG_BOOLEAN:
			return value;
		case JavaTypes.T_JAVA_LANG_STRING:	
			return quotatized((String) value);
		
		case JavaTypes.T_JAVA_UTIL_DATE:
		case JavaTypes.T_JAVA_SQL_DATE:
		case JavaTypes.T_JAVA_SQL_TIME:
		case JavaTypes.T_JAVA_SQL_TIMESTAMP:
			Date date = (Date) value;
			//TODO? 
			return quotatized(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date));
		}
		
		if(SQLSegment.class.isInstance(value)){
			return ((SQLSegment)value).format();
		}
		
		if(JavaTypes.isCollection(value)){
			String str = null;
			int i = 0;
			for (Object val:(Collection<?>)value){
				if(i ==0)
					str="("+formatParameter(val);
				else
					str+=","+formatParameter(val);
				i++;
			}
			if(str!=null) str+=")";
			return str;
		}
		
		if(JavaTypes.isArray(value)){
			String str = null;
			int i = 0;
			for (Object val:(Object[])value){
				if(i ==0)
					str="("+formatParameter(val);
				else
					str+=","+formatParameter(val);
				i++;
			}
			if(str!=null) str+=")";
			return str;
		}
		
		if(JavaTypes.isBlob(value)){
			return value;
		}
		
		throw new SQLBuilderException("can not format parameter "+value);
	}
	
	public static String getDerivedKey(Map<String,Object> parameters){
		String key ="";
		for(String param: parameters.keySet()){
			Object value = parameters.get(param);
			if(value instanceof NonParameterizable){
				key +="_"+ ((NonParameterizable<?>)value).getParamKey(param);
			}else if(JavaTypes.isCollection(value)){
				key+="_"+param +"-" +((Collection<?>)value).size();
			}else if (JavaTypes.isArray(value)){
				key+="_"+param+"-"+ ((Object[])value).length;
			}
			key+="_"+param;
		}
		return key;
	}
	
	
	public static SQLWord getMySqlLimit(SearchConstraint sc){
		if(sc==null) return null;
		if(sc.getRowCount()==-1)
			return null;
		else{
			int start = sc.getStartRow();
			SQLWord limitWord = new SQLWord(" LIMIT " + start + ", " + sc.getRowCount());
			return limitWord;
		}
	}
	
	public static SQLWord getOrderBy(SearchConstraint sc){
		if(sc==null || sc.getOrderColumns()==null || sc.getOrderColumns().size()==0)
			return null;
		String orderBy =null;
		for(String column:sc.getOrderColumns()){
			ORDER order = sc.getOrder(column);
			if(orderBy==null){
				orderBy = "ORDER BY "+column +" "+order;
			}else{
				orderBy += ","+column +" "+order;
			}
		}
		return new SQLWord(orderBy);
	}
		
}
