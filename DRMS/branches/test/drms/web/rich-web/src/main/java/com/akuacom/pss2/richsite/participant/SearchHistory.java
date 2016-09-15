package com.akuacom.pss2.richsite.participant;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.akuacom.common.Condition;
import com.akuacom.common.Condition.OPERATOR;

public class SearchHistory implements Serializable{

	private static final long serialVersionUID = -6396925776618218531L;
	
	public final static String  PARAM_CONFLICT="param_value_confliction_";
	
	public final static Integer INT_PARAM_CONFLICT =Integer.MIN_VALUE;
	
	
	private List<Condition> conditions;
	
	private int maxWorkingIndex = -1;
	
	public final static int SEARCH_ALL =-2;
	
	public SearchHistory(){
		conditions = new ArrayList<Condition>();
	}
	
	public int getMaxWorkingIndex(){
		return this.maxWorkingIndex;
	}
	
	public void setMaxWorkingIndex(int idx){
		this.maxWorkingIndex = idx;
	}
	
	public void resetMaxWorkingIndex(){
		maxWorkingIndex = -1;
	}
	
	public void searchAll(){
		this.maxWorkingIndex = SEARCH_ALL;
	}
	
	public List<Condition> getWorkingConditions() {
		if(maxWorkingIndex==-1)
			return conditions;
		else if(maxWorkingIndex==SEARCH_ALL )
			return Collections.emptyList();
		else
			return conditions.subList(0, maxWorkingIndex+1);
	}
	
	//for jsf page used only 
	public List<ConditionWrapper> getIndexedConditions(){
		List<ConditionWrapper> cons = new ArrayList<ConditionWrapper>(conditions.size());
		int i = 0;
		for(Condition con:conditions){
			cons.add(new ConditionWrapper(con,i++));
		}
		return cons;
	}
	
	public void appendToHistory(Condition condition){
		Condition filter =(Condition) condition.clone();
		if(!conditions.contains(filter)){
			conditions.add(filter);
		}
		this.setMaxWorkingIndex(conditions.size()-1);
	}
	
	/**
	 *  return possible values in a list
	 */
	public List<String> getCandiateParameterValues(String param, OPERATOR opt) throws CriteriaContradictionException{
		Set<String> values = new HashSet<String>();
		for(Condition c:getWorkingConditions()){
			if(c.getParam().equals(param)){
				if(c.getOpterator()==opt){
					values.add((String)c.getValue());
				}
			}
		}
		return new ArrayList<String>(values);
	}
	
	public void clear(){
		conditions.clear();
		resetMaxWorkingIndex();
	}

	public String getStrParamValue(String param,OPERATOR opt) throws CriteriaContradictionException{
		List<Condition> workingConditions =getWorkingConditions(); 
		Set<String> values = new HashSet<String>();
		for(Condition c:workingConditions){
			if(c.getParam().equals(param) && c.getOpterator()==opt){
				values.add(c.paramAsString(param));
			}
		}
		if(opt==OPERATOR.EQUAL){
			if(values.size()==1)
				return values.iterator().next();
			if(values.size()>1)
				throw new CriteriaContradictionException();
		}else if(opt==OPERATOR.START_WITH) {
			return consolidateLeadingCharacters(new ArrayList<String>(values));
		}else
			//TODO
			throw new RuntimeException("Not supported operation exception");
		return null;
	}
	
	public Integer getIntegerParamValue(String param,OPERATOR opt) throws CriteriaContradictionException{
		Set<Integer> values = new HashSet<Integer>();
		for(Condition c:getWorkingConditions()){
			if(c.getParam().equals(param) && c.getOpterator()==opt){
				values.add(c.paramAsInteger(param));
			}
		}
		if(opt==OPERATOR.EQUAL){
			if(values.size()==1)
				return values.iterator().next();
			if(values.size()>1)
				throw new CriteriaContradictionException();
		}else{
			//TODO
			throw new RuntimeException("Not supported operation exception");
		}
		return null;
	}
	
	public Object getParamValue(String param,OPERATOR opt) throws CriteriaContradictionException{
		List<Object> values = new ArrayList<Object>();
		for(Condition c:getWorkingConditions()){
			if(c.getParam().equals(param) && opt==c.getOpterator()){
				values.add(c.paramAsObject(param));
			}
		}
		if(opt==OPERATOR.EQUAL){
			if(values.size()==1)
				return values.get(0);
			else if(values.size()>1)
				throw new CriteriaContradictionException();
		}else{
			//TODO
			throw new RuntimeException("Not supported operation exception");
		}
		return null;
	}
	
	protected static String consolidateLeadingCharacters(List<String> params) 
					throws  CriteriaContradictionException{
		if (params == null || params.isEmpty())
			return null;
		Collections.sort(params);
		String p = params.get(0);
		
		
			for (int i = 1; i < params.size(); i++) {
				// 1--*X*
				if(p.length()>0&&p.endsWith("*")&&p.startsWith("*")){
					
				}
				String next = params.get(i);
				//1.1 --XX* --> XX*
				if(next.contains(p)){
					p=next;
				}
				// -- YX*
				//1.2 --Y*
				//1.3 --*Y
				//1.4 --*Y*
			}
		
		//2--*X
		//3--*X*
		
		
		
		for (int i = 1; i < params.size(); i++) {
			String next = params.get(i);
			String compare = p.replaceAll("\\*", "");
			if (next.startsWith(compare)) {
				p = next;
			} else
				throw new CriteriaContradictionException();
		}
		if (p == null || p.trim().length() == 0)
			return null;
		return p;
	}
	
	protected static boolean emptyStr(String str) {
		return str == null || str.isEmpty() || str.trim().length() == 0;
	}
	
	public static class ConditionWrapper {
		Condition condition;
		public int sequence;
		
		public ConditionWrapper(Condition condition, int sequence){
			this.condition = condition;
			this.sequence = sequence;
		}
		
		public int getSequence() {
			return sequence;
		}
		
		public Object getValue() {
			return condition.getValue();
		}

		public OPERATOR getOpterator() {
			return condition.getOpterator();
		}

		public String getParam() {
			return condition.getParam();
		}

		public String getLabel() {
			return condition.getLabel();
		}
	}

	
	public static void main(String args[]){
		String a ="X*";
		String b ="YXX*";
		System.out.println(b.contains(a));
	}
}
