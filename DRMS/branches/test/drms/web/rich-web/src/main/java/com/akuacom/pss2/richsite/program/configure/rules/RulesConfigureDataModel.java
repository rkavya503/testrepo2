
package com.akuacom.pss2.richsite.program.configure.rules;




import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.ajax4jsf.component.UIDataAdaptor;
import org.apache.log4j.Logger;
import org.richfaces.component.UIExtendedDataTable;
import org.richfaces.model.selection.SimpleSelection;

import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.program.Program;
import com.akuacom.pss2.program.ProgramManager;
import com.akuacom.pss2.program.ProgramManagerBean;
import com.akuacom.pss2.program.ProgramRule;
import com.akuacom.pss2.richsite.program.configure.ProgramConfigureDataModel;
import com.akuacom.pss2.richsite.util.AkuacomJSFUtil;
import com.akuacom.pss2.rule.Rule;



/**
 * The Class RuleTable.
 */
public class RulesConfigureDataModel extends ProgramConfigureDataModel implements Serializable {


    /** The Constant log. */
    private static final Logger log = Logger.getLogger(RulesConfigureDataModel.class.getName());
    private static ProgramManager pm = (ProgramManager)  EJBFactory.getBean(ProgramManagerBean.class);

	/** The jsf rules. */
	private List<RulesViewItem> jsfRules = new ArrayList<RulesViewItem>();

	/** The selected rules. */
	private Set<RulesViewItem> selectedRules = new HashSet<RulesViewItem>();
    private SimpleSelection selection = new SimpleSelection();
    private List<Object> convertedSelection;
    private UIExtendedDataTable table;
    private Set<ProgramRule> rulesModel = new HashSet<ProgramRule>();

   /** ProgramConfigureDataModel reference */
	private ProgramConfigureDataModel programConfigureDataModel;

    private boolean saveFlag = false;

    public RulesConfigureDataModel() {
         super();
         getRuleModel();
   }

	public void getRuleModel() {
		if (AkuacomJSFUtil.getProgram()!=null&&AkuacomJSFUtil.getProgram().getProgramName() != null) {
			Program p =pm.getProgramWithRules(AkuacomJSFUtil.getProgram().getProgramName());
			Set<ProgramRule> prs = p.getRules();
			for (ProgramRule rule : prs) {
				if (rule.getSource().equals(
						Rule.Source.PROGRAM.getDescription())) {
					jsfRules.add(new RulesViewItem(rule));
				}
			}
			Collections.sort(jsfRules, new RulesItemComparator());
		}
	}

    /**
	 * Constructor
	 * @param programConfigureDataModel ProgramConfigureDataModel instance
	 */
	 public RulesConfigureDataModel(ProgramConfigureDataModel programConfigureDataModel){
		super();
		this.programConfigureDataModel=programConfigureDataModel;
		Program p =pm.getProgramWithRules(programConfigureDataModel.getProgramName());
        rulesModel = p.getRules();

        for(ProgramRule rule: rulesModel )
		{
            if(rule.getSource().equals(Rule.Source.PROGRAM.getDescription()))
            {
                jsfRules.add(new RulesViewItem(rule));
            }
		}
		Collections.sort(jsfRules, new RulesItemComparator());
     }

	public void clearData() {
		jsfRules.clear();
	    
	    getRuleModel();
	}

	/**
	 * New rule action.
	 *
	 * @return the string
	 */
	public String newRuleAction()
	{
		AkuacomJSFUtil.setJSFRule(new RulesViewItem());
		return "newRule";
	}

	/**
	 * Creates the rule action.
	 *
	 * @return the string
	 */
	public String createRuleAction(){

        RulesViewItem newRule =  new RulesViewItem();
        newRule = AkuacomJSFUtil.getJSFRule();

        newRule.getRule().setSortOrder(jsfRules.size());

        jsfRules.add(newRule);
 
        for (RulesViewItem jsfRule : jsfRules) {
                jsfRule.getRule().setSource(Rule.Source.PROGRAM.getDescription());
                rulesModel.add(jsfRule.getRule());
         }


        AkuacomJSFUtil.getProgram().setRules(rulesModel);
        AkuacomJSFUtil.setJSFRule(new RulesViewItem());


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
		Iterator<RulesViewItem> i = jsfRules.iterator();
		while(i.hasNext())
		{
			RulesViewItem rule = i.next();
			if(rule.isDelete())
			{
				i.remove();
			}
		}
        this.setJsfRules(jsfRules);
		return "deleteRules";
	}


       public String sortRulesUpAction() {

            Iterator<RulesViewItem> i = jsfRules.iterator();
            boolean sort = true;
            while(i.hasNext())
            {

                RulesViewItem rule = i.next();
                if(rule.isDelete() &&  sort)
                {
                     sort = false;
                     if (rule.getRule().getSortOrder().intValue() != 0){
                          this.sortRulesUpAction(rule.getRule().getSortOrder().intValue());
                     }else{
                            AkuacomJSFUtil.addMsgError("This is the first rule");
                     }

                }else{
                    //AkuacomJSFUtil.addMsgError("You need to select only one rule to sort");
                }
            }
            Collections.sort(jsfRules, new RulesItemComparator());
            return "";
        }

       private void sortRulesUpAction(int sort){
           Iterator<RulesViewItem> i = jsfRules.iterator();
            while(i.hasNext())
            {
                RulesViewItem rule = i.next();
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
            Iterator<RulesViewItem> i = jsfRules.iterator();
            boolean sort = true;
            while(i.hasNext())
            {
                RulesViewItem rule = i.next();
                if(rule.isDelete() &&  sort)
                {
                     if (rule.getRule().getSortOrder().intValue() < jsfRules.size()-1){
                         sort = false;
                         this.sortRulesDownAction(rule.getRule().getSortOrder().intValue());
                     }else{
                         AkuacomJSFUtil.addMsgError("This is the last rule");
                     }
                }else{
                    //AkuacomJSFUtil.addMsgError("You need to select only one rule to sort");
                }
            }
            Collections.sort(jsfRules, new RulesItemComparator());
            return "";
        }

         private void sortRulesDownAction(int sort){
           Iterator<RulesViewItem> i = jsfRules.iterator();
            while(i.hasNext())
            {
                RulesViewItem rule = i.next();
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

         /*
        public void printSelection(ActionEvent actionEvent) {
             convertedSelection = (List<Object>) convertSelection(findUIDataAdaptor(actionEvent.getComponent()));
              Iterator it = convertedSelection.iterator();
                while(it.hasNext()){
                    //log.error("++++" + (it.next()).toString());
                }
           }
           */

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
	public String saveRulesAction(){

        Set<ProgramRule> rulesModelSave = new HashSet<ProgramRule>();
        for (RulesViewItem jsfRule : jsfRules) {
            jsfRule.getRule().setSource(Rule.Source.PROGRAM.getDescription());
            rulesModelSave.add(jsfRule.getRule());
        }

       
        AkuacomJSFUtil.getProgram().setRules(rulesModelSave);
        Program p =pm.getProgramWithRules(AkuacomJSFUtil.getProgram().getProgramName());

         
           Set<ProgramRule> prTemp =  (Set<ProgramRule>) p.getRules();
           Set<ProgramRule> rulesCopy = new HashSet<ProgramRule>();
           Set<ProgramRule> rulesToSave = new HashSet<ProgramRule>();
         
            for(ProgramRule pRule : prTemp){
                   rulesCopy.add(pRule);
                   pRule.setProgram(p);
            }
            prTemp.removeAll(rulesCopy);
            p.setRules(prTemp);
            pm.updateProgram(p);
 
            for(ProgramRule rModel : rulesModelSave){
                   rModel.setProgram(p);
                   rulesToSave.add(rModel);
            }
            p.setRules(rulesToSave);
            pm.updateProgram(p);

		return "saveRules";
	}

    public void saveFlagAction(){
		this.saveFlag = true;
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
	public Set<RulesViewItem> getSelectedRules()
	{
		return selectedRules;
	}

	/**
	 * Sets the selected rules.
	 *
	 * @param selectedRules the new selected rules
	 */
	public void setSelectedRules(Set<RulesViewItem> selectedRules)
	{
		this.selectedRules = selectedRules;
	}

	/**
	 * Gets the jsf rules.
	 *
	 * @return the jsf rules
	 */
	public List<RulesViewItem> getJsfRules()
	{
		Boolean needRefresh = AkuacomJSFUtil.getNeedRuleRefresh();
		if (needRefresh != null && needRefresh.booleanValue()) {
			AkuacomJSFUtil.setNeedRuleRefresh(new Boolean(false));
			clearData();
		}
		
		return jsfRules;
	}

	/**
	 * Sets the jsf rules.
	 *
	 * @param jsfRules the new jsf rules
	 */
	public void setJsfRules(List<RulesViewItem> jsfRules)
	{
		this.jsfRules = jsfRules;
	}

    public Set<ProgramRule> getRulesModel() {
        return rulesModel;
    }

    public void setRulesModel(Set<ProgramRule> rulesModel) {
        this.rulesModel = rulesModel;
    }

	public ProgramConfigureDataModel getProgramConfigureDataModel() {
		return programConfigureDataModel;
	}

	public void setProgramConfigureDataModel(
			ProgramConfigureDataModel programConfigureDataModel) {
		this.programConfigureDataModel = programConfigureDataModel;
	}

    public boolean isSaveFlag() {
        return saveFlag;
    }

    public void setSaveFlag(boolean saveFlag) {
        this.saveFlag = saveFlag;
    }


}
