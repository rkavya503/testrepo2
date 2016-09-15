package com.akuacom.jdbc;

import static org.junit.Assert.assertEquals;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;


public class SQLBuilderTest {
	
	protected static String singleWhitespace(String str){
		str= str.replaceAll("\\s{2,}", " ");
		return str.trim();
	}
	
	@Test
	public void testSingleWhiteplace(){
		String str1=" select * from   participant   where participantId = 'a'";
		String str2="select * from participant   where participantId   = 'a'";
		assertEquals(singleWhitespace(str1),singleWhitespace(str2));
	}
	
	protected void assertSQLEquals(String sql1,String sql2){
		assertEquals(singleWhitespace(sql1),singleWhitespace(sql2));
	}
	
	
	@Test
	public void buildSQLTest() throws SQLBuilderException{
	   String str = "select * from participant where 1=1 [and name like ${participantName}] and status = ${status}"; 
	   Map<String,Object> params = new HashMap<String,Object>();
	   params.put("status", 1);
	   
	   String qualifiedSQL= SQLBuilder.buildSQL(str, params);
	   assertSQLEquals("select * from participant where 1=1 and status = 1",qualifiedSQL);
	   
	   params.put("participantName", "part%");
	   qualifiedSQL= SQLBuilder.buildSQL(str, params);
	   assertSQLEquals("select * from participant where 1=1 and name like 'part%' and status = 1",
			   qualifiedSQL);
	}
	
	@Test
	public void buildSQLRemoveWhere() throws SQLBuilderException {
		String str = "select * from participant [where ${needWhere} ] [name like ${participantName}] ";
		
		Map<String,Object> params = new HashMap<String,Object>();
		String qualifiedSQL= SQLBuilder.buildSQL(str, params);
		assertSQLEquals("select * from participant",qualifiedSQL);
		
		params.put("needWhere",EmptyWord.getInstance());
		params.put("participantName","part1%");
		
		qualifiedSQL= SQLBuilder.buildSQL(str, params);
		assertSQLEquals("select * from participant where name like 'part1%'",qualifiedSQL);
	}
	
	@Test
	public void buildSQLWithOrderClauseTest() throws SQLBuilderException{
		String str = "select * from participant where 1=1 [and name like ${participantName}] and status = ${status} [ ${orderBy} ]";
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("status", 1);
		String qualifiedSQL= SQLBuilder.buildSQL(str, params);
		assertSQLEquals("select * from participant where 1=1 and status = 1",qualifiedSQL);
		
		SQLWord word = new SQLWord("order by participantName desc");
		params.put("orderBy", word);
		qualifiedSQL= SQLBuilder.buildSQL(str, params);
		assertSQLEquals("select * from participant where 1=1 and status = 1 order by participantName desc",qualifiedSQL);
		
		params.put("participantName", "part%");
		qualifiedSQL= SQLBuilder.buildSQL(str, params);
		assertSQLEquals("select * from participant where 1=1 and name like 'part%' and status = 1 order by participantName desc",qualifiedSQL);
	}
	
	
	@Test
	public void buildSQLWithListParameter() throws SQLBuilderException {
		
		String str = "select * from participant where 1=1 [and participantId in ${partIds}] ";
		
		Map<String,Object> params = new HashMap<String,Object>();
		
		String qualifiedSQL= SQLBuilder.buildSQL(str, params);
		assertSQLEquals("select * from participant where 1=1",qualifiedSQL);
		
		List<Integer> ids = Arrays.asList(1,2,3,4);
		params.put("partIds", ids);
		qualifiedSQL= SQLBuilder.buildSQL(str, params);
		assertSQLEquals("select * from participant where 1=1 and participantId in (1,2,3,4)",qualifiedSQL);
		
		params.put("partIds", Collections.emptyList());
		qualifiedSQL= SQLBuilder.buildSQL(str, params);
		assertSQLEquals("select * from participant where 1=1",qualifiedSQL);
	}
	
	
	@Test
	public void buildSQLWithDateParameter() throws SQLBuilderException, ParseException {
		String str = "select * from participant where 1=1 [and created >= ${date}] ";
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = format.parse("2011-04-28 01:02:03");
		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("date", date);
		
		String qualifiedSQL= SQLBuilder.buildSQL(str, params);
		assertSQLEquals("select * from participant where 1=1 and created >= '2011-04-28 01:02:03'",qualifiedSQL);
	}
	
	@Test
	public void testParameterizableSQL() throws SQLBuilderException, ParseException {
		
		String str = "select * from participant where 1=1 [and name like ${participantName}] and status = ${status}";
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("status", 1);
		
		ParameterizedSQL sql=SQLBuilder.parseNamedPamameterSQL(str, params);
		
		assertSQLEquals("select * from participant where 1=1  and status = ?",sql.getSqlStatement());
		assertEquals(1,sql.getParameterIndex().get("status").length);
		assertEquals(1,sql.getParameterIndex().get("status")[0]);
		
		params.put("participantName", "2");
		
		sql=SQLBuilder.parseNamedPamameterSQL(str, params);
		
		assertSQLEquals("select * from participant where 1=1 and name like ? and status = ?",sql.getSqlStatement());
		
		assertEquals(1,sql.getParameterIndex().get("participantName").length);
		assertEquals(1,sql.getParameterIndex().get("participantName")[0]);
		assertEquals(1,sql.getParameterIndex().get("status").length);
		assertEquals(2,sql.getParameterIndex().get("status")[0]);
		
		
		str = "select * from participant where 1=1 and address <> ${status} [and name like ${participantName}] " +
				"and status = ${status}";
		
		sql=SQLBuilder.parseNamedPamameterSQL(str, params);
		
		assertSQLEquals("select * from participant where 1=1 and address <> ? and name like ? and status = ?",
				sql.getSqlStatement());
		
		assertEquals(1,sql.getParameterIndex().get("participantName").length);
		assertEquals(2,sql.getParameterIndex().get("participantName")[0]);
		
		assertEquals(2,sql.getParameterIndex().get("status").length);
		assertEquals(1,sql.getParameterIndex().get("status")[0]);
		assertEquals(3,sql.getParameterIndex().get("status")[1]);
	}
	
	@Test
	public void testParameterizableSQLWithListParameter() throws SQLBuilderException, ParseException {
		String str = "select * from participant where status in ${status} [and name like ${participantName}] ";
		Map<String,Object> params = new HashMap<String,Object>();
		
		params.put("status", Arrays.asList("A","B","C"));
		params.put("participantName", "p%");
		
		ParameterizedSQL sql=SQLBuilder.parseNamedPamameterSQL(str, params);
		assertSQLEquals("select * from participant where status in (?,?,?) and name like ?",sql.getSqlStatement());
		assertEquals(1,sql.getParameterIndex().get("status").length);
		assertEquals(1,sql.getParameterIndex().get("status")[0]);
		
		assertEquals(1,sql.getParameterIndex().get("participantName").length);
		assertEquals(4,sql.getParameterIndex().get("participantName")[0]);
		
		str = "select * from participant where name like ${participantName} [ and status in ${status}]  ";
		
		sql= SQLBuilder.parseNamedPamameterSQL(str, params);
		assertSQLEquals("select * from participant where name like ? and status in (?,?,?)",sql.getSqlStatement());
		
	}
	
	@Test
	public void testParseNamedParameterSQL() throws SQLBuilderException, ParseException {
		String str = "select * from participant where status in ${status} [and name like ${participantName}]  ${orderBy}";
		Map<String,Object> params = new HashMap<String,Object>();
		
		params.put("status", Arrays.asList("A","B","C"));
		params.put("participantName", "p%");
		params.put("orderBy", new SQLWord("order by participant desc"));
		
		ParameterizedSQL sql=SQLBuilder.parseNamedPamameterSQL(str, params);
		assertSQLEquals("select * from participant where status in (?,?,?) and name like ? order by participant desc ",sql.getSqlStatement());
		
		assertEquals(2,sql.getParameterIndex().keySet().size());
	}
	
	@Test
	public void testBuildNamedParameterSQL() throws SQLBuilderException, ParseException {
		String str = "select * from participant where status in ${status} [and name like ${participantName}]  [${orderBy}]";
		Map<String,Object> params = new HashMap<String,Object>();
		
		params.put("status", Arrays.asList("A","B","C"));
		//params.put("participantName", "p%");
		params.put("orderBy", new SQLWord("order by participant desc"));
		
		assertSQLEquals("select * from participant where status in ${status} ${orderBy}",
				SQLBuilder.buildNamedParameterSQL(str, params));
		
	}
	
	
}
