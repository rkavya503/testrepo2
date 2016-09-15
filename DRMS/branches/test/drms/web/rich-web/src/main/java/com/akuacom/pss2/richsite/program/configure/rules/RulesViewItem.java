/*
 * www.akuacom.com - Automating Demand Response
 *
 * com.akuacom.pss2.facdash.JSFRule.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved.
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.richsite.program.configure.rules;

import com.akuacom.pss2.program.ProgramRule;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import com.akuacom.pss2.rule.Rule;
import com.akuacom.pss2.rule.Rule.Operator;
import com.akuacom.pss2.richsite.util.AkuacomJSFUtil;

/**
 * The Class JSFRule.
 */
public class RulesViewItem implements Serializable
{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * The Enum Variable.
	 */
	private enum Variable {/** The price. */
    price, /** The bid. */
     bid};

    /** The rule. */
	private ProgramRule rule;

	/** The delete. */
	private boolean delete;

   	/** The rule variable support by the program. */
	private List<String> ruleVariables;

	/**
	 * Instantiates a new jSF rule.
	 */
	public RulesViewItem()
	{
		rule = new ProgramRule();
		try
		{
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
            rule.setStart(simpleDateFormat.parse("00:00"));
			rule.setEnd(simpleDateFormat.parse("23:59"));
			rule.setMode(Rule.Mode.MODERATE);
			rule.setVariable(Variable.price.toString());
			rule.setOperator(Rule.Operator.ALWAYS);
			rule.setNotifyAction(false);
			rule.setSignalAction(false);
			rule.setThreshold(Rule.Threshold.NA);
			rule.setValue(0.1);
			rule.setSource(Rule.Source.PROGRAM.getDescription());
		}
		catch (ParseException e)
		{
            e.printStackTrace();
			// TODO: what should we do here?
		}
	}

	/**
	 * Instantiates a new jSF rule.
	 *
	 * @param rule the rule
	 */
	public RulesViewItem(ProgramRule rule){
		this.rule = rule;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		return delete + "_" +
			new SimpleDateFormat("HH:mm").format(rule.getStart()) + "_" +
			new SimpleDateFormat("HH:mm").format(rule.getEnd()) + "_" +
			rule.getMode() + "_" +
			rule.getVariable() + "_" +
			rule.getOperator().description() + "_" +
			rule.getValue();
	}

	public String operatorAction()
	{
		return null;
	}

   public String modeAction()
	{
		return null;
	}


	/**
	 * Gets the available modes.
	 *
	 * @return the available modes
	 */
	public List<SelectItem> getAvailableModes(){

		List<SelectItem> availableModes = new ArrayList<SelectItem>();
		for(Rule.Mode operator: Rule.Mode.values())
		{
			availableModes.add(new SelectItem(operator));
		}
		return availableModes;
	}


    /**
	 * Gets the available operators.
	 *
	 * @return the available operators
	 */
	public List<SelectItem> getAvailableOperators(){

    	List<SelectItem> availableOperators = new ArrayList<SelectItem>();
        if (AkuacomJSFUtil.getProgram()!=null&&AkuacomJSFUtil.getProgram().getRules() != null){

            com.akuacom.pss2.program.ProgramManager programManager =
                com.akuacom.pss2.core.EJBFactory
                    .getBean(com.akuacom.pss2.program.ProgramManager.class);
            ruleVariables = programManager.getProgramRuleVariables(AkuacomJSFUtil.getProgram().getProgramName());

            if (ruleVariables.size() == 0)
            {
                availableOperators.add(new SelectItem(Rule.Operator.ALWAYS
                    .description()));
            }
            else
            {
                for (Rule.Operator operator : Rule.Operator.values())
                {
                    availableOperators.add(new SelectItem(operator.description()));
                }
            }
        }
		return availableOperators;
	}

	/**
	 * Gets the available variables.
	 *
	 * @return the available variables
	 */
	public List<SelectItem> getAvailableVariables()
	{
           com.akuacom.pss2.program.ProgramManager programManager =
                com.akuacom.pss2.core.EJBFactory
                    .getBean(com.akuacom.pss2.program.ProgramManager.class);
            List<SelectItem> availableVariables = new ArrayList<SelectItem>();
           	if (AkuacomJSFUtil.getProgram()!=null&&AkuacomJSFUtil.getProgram().getRules() != null) {
                    ruleVariables = programManager.getProgramRuleVariables(AkuacomJSFUtil.getProgram().getProgramName());
                   

                    for (String variable :  ruleVariables)
                    {
                    availableVariables.add(new SelectItem(variable));
                    }
            } 
		 return availableVariables;
	}

    /**
	 * Edits the rules.
	 */
	public void editCustomRules()
	{
      if (AkuacomJSFUtil.getProgram()!=null&&AkuacomJSFUtil.getProgram().getRules() != null){
		com.akuacom.pss2.program.ProgramManager programManager =
			com.akuacom.pss2.core.EJBFactory
				.getBean(com.akuacom.pss2.program.ProgramManager.class);
		ruleVariables = programManager.getProgramRuleVariables(AkuacomJSFUtil.getProgram().getProgramName());
        }


	}
	/**
	 * Gets the rule.
	 *
	 * @return the rule
	 */
	public ProgramRule getRule()
	{
		return rule;
	}

	/**
	 * Sets the rule.
	 *
	 * @param rule the new rule
	 */
	public void setRule(ProgramRule rule)
	{
		this.rule = rule;
	}

	/**
	 * Checks if is delete.
	 *
	 * @return true, if is delete
	 */
	public boolean isDelete()
	{
		return delete;
	}

	/**
	 * Sets the delete.
	 *
	 * @param delete the new delete
	 */
	public void setDelete(boolean delete)
	{
		this.delete = delete;
	}

	/**
	 * Gets the operator.
	 *
	 * @return the operator
	 */
	public String getOperator()
	{
		return rule.getOperator().description();
	}

	/**
	 * Sets the operator.
	 *
	 * @param jsfOperator the new operator
	 */
	public void setOperator(String jsfOperator)
	{
		for(Rule.Operator operator: Rule.Operator.values())
		{
			if(jsfOperator.equals(operator.description()))
			{
				rule.setOperator(operator);
				return;
			}
		}
	}

	/**
	 * @return the variableValueEditable
	 */
	public boolean isVariableValueEditable()
	{
		if(rule.getOperator() == Operator.ALWAYS)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

    public List<String> getRuleVariables() {
        return ruleVariables;
    }

    public void setRuleVariables(List<String> ruleVariables) {
        this.ruleVariables = ruleVariables;
    }




	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + (delete ? 1231 : 1237);
		result = prime * result + ((rule == null) ? 0 : rule.hashCode());

		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{

     /*
        if (this.getRule().getUUID().equalsIgnoreCase( ((RulesViewItem)obj).getRule().getUUID())){
              return true;
        } else {
                return false;
        }
       */

		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final RulesViewItem other = (RulesViewItem) obj;
		if (delete != other.delete)
			return false;
		if (rule == null)
		{
			if (other.rule != null)
				return false;
		}
		else if (!rule.equals(other.rule))
			return false;

		return true;



    }


}
