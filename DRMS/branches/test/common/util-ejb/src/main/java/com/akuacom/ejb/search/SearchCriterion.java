/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.ejb.search.SearchCriterion.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved.
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.ejb.search;

import java.io.Serializable;


/**
 * The Class SearchCriterion.
 */
public class SearchCriterion implements Serializable 
{
    
    private String fieldName;
	
	private String operator;
	
	private Serializable fieldValue;
	
	private String joinOper = null;

    /**
     * Instantiates a new search criterion.
     */
    public SearchCriterion()
    {
    }

    /**
     * Instantiates a new search criterion.
     * 
     * @param fieldName the field name
     * @param operator the operator
     * @param fieldValue the field value
     */
    public SearchCriterion(String fieldName, String operator, Serializable fieldValue)
    {
        if (fieldName != null && ! fieldName.equalsIgnoreCase(""))
		{
			this.fieldName = fieldName;
			this.fieldValue = fieldValue;
			this.operator = operator;
			if (operator.equalsIgnoreCase("like"))
			{
				this.operator = "like";
				this.fieldValue = "%" + fieldValue + "%";
			}

		}
    }

    /**
     * Gets the field name.
     * 
     * @return the field name
     */
    public String getFieldName()
    {
        return fieldName;
    }

    /**
     * Sets the field name.
     * 
     * @param fieldName the new field name
     */
    public void setFieldName(String fieldName)
    {
        this.fieldName = fieldName;
    }

    /**
     * Gets the operator.
     * 
     * @return the operator
     */
    public String getOperator()
    {
        return operator;
    }

    /**
     * Sets the operator.
     * 
     * @param operator the new operator
     */
    public void setOperator(String operator)
    {
        this.operator = operator;
    }

    /**
     * Gets the field value.
     * 
     * @return the field value
     */
    public Object getFieldValue()
    {
        return fieldValue;
    }

    /**
     * Sets the field value.
     * 
     * @param fieldValue the new field value
     */
    public void setFieldValue(Serializable fieldValue)
    {
        this.fieldValue = fieldValue;
    }

    /**
     * Gets the join operator.
     * 
     * @return the join operator
     */
    public String getJoinOperator()
    {
        return joinOper;
    }

    /**
     * Sets the join operator.
     * 
     * @param joinOper the new join operator
     */
    public void setJoinOperator(String joinOper)
    {
        this.joinOper = joinOper;
    }
}
