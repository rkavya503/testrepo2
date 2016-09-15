/*
 * www.akuacom.com - Automating Demand Response
 *
 * com.akuacom.pss2.facdash.RuleTable.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved.
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.facdash;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.ajax4jsf.component.UIDataAdaptor;
import org.apache.log4j.Logger;
import org.richfaces.component.UIExtendedDataTable;
import org.richfaces.model.selection.SimpleSelection;

import com.akuacom.pss2.program.participant.ProgramParticipantRule;
import com.akuacom.pss2.rule.Rule;
import com.akuacom.pss2.userrole.ViewBuilderManager;
import com.akuacom.pss2.userrole.viewlayout.ClientRuleViewLayout;



/**
 * The Class RuleTable.
 */
public class RuleTable implements ClientRuleViewLayout, Serializable {
    /** The Constant log. */
    private static final Logger log = Logger.getLogger(RuleTable.class.getName());

	/** The jsf rules. */
	private List<JSFRule> jsfRules = new ArrayList<JSFRule>();

	/** The selected rules. */
	private Set<JSFRule> selectedRules = new HashSet<JSFRule>();
    private SimpleSelection selection = new SimpleSelection();
    private List<Object> convertedSelection;
    private UIExtendedDataTable table;


	/**
	 * Instantiates a new rule table.
	 */
	public RuleTable()
	{
		buildViewLayout();
	}
	/**
	 * Instantiates a new rule table.
	 *
	 * @param rules the rules
	 */
	public RuleTable(List<ProgramParticipantRule> rules)
	{   buildViewLayout();  
        for(ProgramParticipantRule rule: rules)
		{
            if(rule.getSource().equals(Rule.Source.CUSTOM.getDescription()))
            {
                jsfRules.add(new JSFRule(rule));
            }
		}
		Collections.sort(jsfRules, new JSFRuleComparator());
	}

	/**
	 * New rule action.
	 *
	 * @return the string
	 */
	public String newRuleAction()
	{
		FDUtils.setJSFRule(new JSFRule());
		return "newRule";
	}

	/**
	 * Creates the rule action.
	 *
	 * @return the string
	 */
	public String createRuleAction()
	{
		jsfRules.add(FDUtils.getJSFRule());
		return "createRule";
	}

	/**
	 * Cancel rule action.
	 *
	 * @return the string
	 */
	public String cancelRuleAction()
	{
		return "cancelRule";
	}

	/**
	 * Delete rules action.
	 *
	 * @return the string
	 */
	public String deleteRulesAction()
	{
		Iterator<JSFRule> i = jsfRules.iterator();
		while(i.hasNext())
		{
			JSFRule rule = i.next();
			if(rule.isDelete())
			{
				i.remove();
			}
		}
		return "deleteRules";
	}


       public String sortRulesUpAction() {
            Iterator<JSFRule> i = jsfRules.iterator();
            boolean sort = true;
            while(i.hasNext())
            {
                JSFRule rule = i.next();
                if(rule.isDelete() &&  sort)
                {
                     sort = false;
                     if (rule.getRule().getSortOrder().intValue() != 0){
                          this.sortRulesUpAction(rule.getRule().getSortOrder().intValue());
                     }else{
                            FDUtils.addMsgError("This is the first rule");
                     }

                }else{
                    //FDUtils.addMsgError("You need to select only one rule to sort");
                }
            }
            Collections.sort(jsfRules, new JSFRuleComparator());
            return "";
        }

       private void sortRulesUpAction(int sort){
           Iterator<JSFRule> i = jsfRules.iterator();
            while(i.hasNext())
            {
                JSFRule rule = i.next();
                if(rule.getRule().getSortOrder().intValue() == sort-1)
                {
                     rule.getRule().setSortOrder(rule.getRule().getSortOrder()+1);
                }else if (rule.getRule().getSortOrder().intValue() == sort){
                     rule.getRule().setSortOrder(sort-1);
                }
            }
        }


       public String sortRulesDownAction()
        {
            Iterator<JSFRule> i = jsfRules.iterator();
            boolean sort = true;
            while(i.hasNext())
            {
                JSFRule rule = i.next();
                if(rule.isDelete() &&  sort)
                {
                     if (rule.getRule().getSortOrder().intValue() < jsfRules.size()-1){
                         sort = false;
                         this.sortRulesDownAction(rule.getRule().getSortOrder().intValue());
                     }else{
                         FDUtils.addMsgError("This is the last rule");
                     }
                }else{
                    //FDUtils.addMsgError("You need to select only one rule to sort");
                }
            }
            Collections.sort(jsfRules, new JSFRuleComparator());
            return "";
        }

         private void sortRulesDownAction(int sort){
           Iterator<JSFRule> i = jsfRules.iterator();
            while(i.hasNext())
            {
                JSFRule rule = i.next();
                if(rule.getRule().getSortOrder().intValue() == sort+1)
                {
                     rule.getRule().setSortOrder(rule.getRule().getSortOrder()-1);
                }else if (rule.getRule().getSortOrder().intValue() == sort){
                     rule.getRule().setSortOrder(sort+1);
                }
            }
        }


     /*** can be used to capture the selected row in the table ****/
     /** Start ***************************************************/
        private UIDataAdaptor findUIDataAdaptor(UIComponent component) {
             UIComponent result = component;
             while (result != null) {
             if (result instanceof UIDataAdaptor) {
             return (UIDataAdaptor) result;
             } else {
             result = result.getParent();
             }
         }
         return null;
         }


         private List<Object> convertSelection(UIDataAdaptor dataAdaptor) {
             List<Object> result = new ArrayList<Object>();
             FacesContext facesContext = FacesContext.getCurrentInstance();
             Object rowKey = dataAdaptor.getRowKey();

             try {
             if (selection != null) {
                 SimpleSelection simpleSelection = (SimpleSelection) selection;
                 for (Iterator<Object> keys = simpleSelection.getKeys(); keys.hasNext();) {
                 Object nextKey = keys.next();

                 dataAdaptor.setRowKey(facesContext, nextKey);
                 result.add(dataAdaptor.getRowData());
                 }
                 }
             } finally {
             try {
                 dataAdaptor.setRowKey(facesContext, rowKey);
             } catch (Exception e) {
                 facesContext.getExternalContext().log(e.getMessage(), e);
             }
         }

         return result;
         }

        public void printSelection(ActionEvent actionEvent) {
             convertedSelection = (List<Object>) convertSelection(findUIDataAdaptor(actionEvent.getComponent()));
              Iterator it = convertedSelection.iterator();
                while(it.hasNext()){
                    //log.error("++++" + (it.next()).toString());
                }
           }


        public UIExtendedDataTable getTable() {
            return table;
        }

        public void setTable(UIExtendedDataTable table) {
            this.table = table;
        }
   /** End ***************************************************/

	/**
	 * Save rules action.
	 *
	 * @return the string
	 */
	public String saveRulesAction()
	{
        List<ProgramParticipantRule> rules = new ArrayList<ProgramParticipantRule>();
        for (JSFRule jsfRule : jsfRules)
		{
			rules.add(jsfRule.getRule());
		}
		FDUtils.getJSFClientProgram().updateRules(rules, Rule.Source.CUSTOM);

		return "saveRules";
	}

	/**
	 * Cancel rules action.
	 *
	 * @return the string
	 */
	public String cancelRulesAction()
	{
		return "cancelRules";
	}

	/**
	 * Gets the selected rules.
	 *
	 * @return the selected rules
	 */
	public Set<JSFRule> getSelectedRules()
	{
		return selectedRules;
	}

	/**
	 * Sets the selected rules.
	 *
	 * @param selectedRules the new selected rules
	 */
	public void setSelectedRules(Set<JSFRule> selectedRules)
	{
		this.selectedRules = selectedRules;
	}

	/**
	 * Gets the jsf rules.
	 *
	 * @return the jsf rules
	 */
	public List<JSFRule> getJsfRules()
	{
		return jsfRules;
	}

	/**
	 * Sets the jsf rules.
	 *
	 * @param jsfRules the new jsf rules
	 */
	public void setJsfRules(List<JSFRule> jsfRules)
	{
		this.jsfRules = jsfRules;
	}
	
	
	private boolean deleteCheckboxEnable;
	private boolean canAddRule;
	private boolean canSaveRule;
	private boolean canDeleteRule;
	private boolean canGoUpRule;
	private boolean canGoDownRule;


	public boolean isDeleteCheckboxEnable() {
		return deleteCheckboxEnable;
	}

	public void setDeleteCheckboxEnable(boolean deleteCheckboxEnable) {
		this.deleteCheckboxEnable = deleteCheckboxEnable;
	}

	public boolean isCanAddRule() {
		return canAddRule;
	}

	public void setCanAddRule(boolean canAddRule) {
		this.canAddRule = canAddRule;
	}

	public boolean isCanSaveRule() {
		return canSaveRule;
	}

	public void setCanSaveRule(boolean canSaveRule) {
		this.canSaveRule = canSaveRule;
	}

	public boolean isCanDeleteRule() {
		return canDeleteRule;
	}

	public void setCanDeleteRule(boolean canDeleteRule) {
		this.canDeleteRule = canDeleteRule;
	}

	public boolean isCanGoUpRule() {
		return canGoUpRule;
	}

	public void setCanGoUpRule(boolean canGoUpRule) {
		this.canGoUpRule = canGoUpRule;
	}
	
	public boolean isCanGoDownRule() {
		return canGoDownRule;
	}
	public void setCanGoDownRule(boolean canGoDownRule) {
		this.canGoDownRule = canGoDownRule;
	}
	private void buildViewLayout(){
        try {
        	getViewBuilderManager().buildClientRuleViewLayout(this);
        } catch (NamingException e) {                // log exception

        }
	}

	private ViewBuilderManager getViewBuilderManager() throws NamingException{
       return (ViewBuilderManager) new InitialContext().lookup("pss2/ViewBuilderManagerBean/local");
	}	
}
