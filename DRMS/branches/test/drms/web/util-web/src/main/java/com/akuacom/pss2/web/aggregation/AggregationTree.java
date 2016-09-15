package com.akuacom.pss2.web.aggregation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.ajax4jsf.context.AjaxContext;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang.StringUtils;
import org.richfaces.component.UITree;
import org.richfaces.component.UITreeNode;
import org.richfaces.component.html.HtmlTree;
import org.richfaces.event.DropEvent;
import org.richfaces.event.NodeExpandedEvent;
import org.richfaces.event.NodeSelectedEvent;
import org.richfaces.model.TreeNode;
import org.richfaces.model.selection.Selection;
import org.richfaces.model.selection.SimpleSelection;

import com.akuacom.ejb.client.EJB3Factory;
import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.participant.ParticipantManager;
import com.akuacom.pss2.participant.ParticipantManagerBean;
import com.akuacom.pss2.program.Program;
import com.akuacom.pss2.program.ProgramManager;
import com.akuacom.pss2.program.ProgramManagerBean;
import com.akuacom.pss2.program.participant.ProgramParticipant;
import com.akuacom.pss2.program.participant.ProgramParticipantAggregationManager;
import com.akuacom.pss2.program.participant.ProgramParticipantAggregationManagerBean;
import com.akuacom.pss2.program.participant.ProgramParticipantManager;
import com.akuacom.pss2.program.participant.ProgramParticipantManagerBean;
import com.akuacom.pss2.system.SystemManager;
import com.akuacom.pss2.system.property.PSS2Features;
import com.akuacom.pss2.util.CBPUtil;
import com.akuacom.pss2.web.MsgUtils;

/**
 * Backing bean to configure program based aggregation.  Adds children to a parent
 * based on the currently selected parent program
 *
 */
public class AggregationTree implements Serializable{
	private static final long serialVersionUID = -3284293763406702331L;
	
	static final private String NO_MATCH = "No matches";
	static final public String NO_PROGRAM = "Program";
	static final private String AGG_ROOT = "Root";

	/** root node */
	private AggregationTreeNode<String> aggTreeRoot = new AggregationTreeNode<String>();
	/** the entire tree */
	private AggregationTreeNode<String> aggTreeNodes = new AggregationTreeNode<String>();
	/** flag that participant parent info has changed */
	private boolean needToLoadTree = true;
	/** every program participant for every program the parent participant is in */
	private List<ProgramParticipant> totalParticipantList = new ArrayList<ProgramParticipant>();
	/** the possible child participants for display for the current program */
	private ArrayList<String> availableParticipantList = new ArrayList<String>();
	/** current parent participant */
	private String currParticipant = null;
	/** parent participant */
	private String parentParticipant = "";
	/** current program of the programs which the parent participant belongs to */
	private String currProgram = NO_PROGRAM;
	/** program for use in UI */
	private String parentProgram = null;
	/** program name for children - normally parent of leaf */
	private String displayProgram = NO_PROGRAM;
	/** selected node display name */
	private String selectedParticipantName = "";
	/** participant to de-aggregate */
	private String removingParticipant = "";
	/** are multiple levels being displayed for facdash or just one for agg builder */
	private boolean multiLevelMode = false;
	/** facdash display of aggregation level */
	private String aggDisplay = AGG_ROOT;
	/** The the actual tree component the tree tag binds to 
	 *  Use tag: binding="#{aggTree.tree}" 
	 */
	private HtmlTree tree;
	
	PSS2Features features;
	
	// for multiselect
	private Selection selection = new SimpleSelection();
	
	private ProgramParticipantAggregationManager aggMan = null;

    private int AggPartCount;
    
    private String treeError;
    private ProgramParticipant cbpProgramParticipant;
    private String cbpProgramName;
    
	private ProgramParticipantAggregationManager getAggregationManager() {
		if (aggMan == null) {
			aggMan = (ProgramParticipantAggregationManager) EJBFactory.getBean(ProgramParticipantAggregationManagerBean.class);
		}
		
		return aggMan;
	}

	public void loadTree() {
		// this is called when the participant tab is first displayed
		// so we need to handle uninitialized case
		availableParticipantList.add(NO_MATCH);

		if (currParticipant == null) {
			return;
		}

		ParticipantManager partMan = (ParticipantManager)EJBFactory.getBean(ParticipantManagerBean.class);
		ProgramManager progMan = (ProgramManager)EJBFactory.getBean(ProgramManagerBean.class);

		aggTreeRoot = new AggregationTreeNode<String>();
		aggTreeNodes = new AggregationTreeNode<String>();

		// get the parent participant
		Participant part = partMan.getParticipant(currParticipant, false);
		if (part == null) {
			return;
		}
		// NOTE! if you change this you MUST update Header1.setSwitchingParticipant
        //String rootCount = "  (" + getAggCount(part) + ")";
		aggTreeRoot.setData(currParticipant);
		aggTreeNodes.addChild(0, aggTreeRoot);

		// load program nodes
		totalParticipantList.clear();
		totalParticipantListWithoutConsolidation.clear();
		boolean first = true;
		Set<ProgramParticipant> ppSet = part.getProgramParticipants();
		List<String> programNames = new ArrayList<String>();
		for (ProgramParticipant pp :ppSet) {
			programNames.add(pp.getProgramName());
		}
		List<Program> programs = progMan.getProgramsWithParticipants(programNames);
		for(Program p : programs){
			for (ProgramParticipant totPP : p.getProgramParticipants()) {
				if (totPP == null || totPP.getParticipant().isClient()) {
					continue;
				}
				totalParticipantListWithoutConsolidation.add(totPP);
			}
		}
		List<ProgramParticipant> ppList = new ArrayList<ProgramParticipant>(ppSet);
		if(isEnableCBPConsolidation()){
			cbpProgramParticipant = CBPUtil.getFirstCBPProgramParticipant(ppList);
			if(cbpProgramParticipant!=null){
				cbpProgramName=cbpProgramParticipant.getProgramName();
				ppList = CBPUtil.transferProgramParticipant(ppList);	
			}
		}
		for (ProgramParticipant pp :ppList) {
			// get program
			AggregationTreeNode<String> child1 = new AggregationTreeNode<String>();
			child1.setData(pp.getProgramName());
			String programName = pp.getProgramName();
			Program program = null;
			if(isEnableCBPConsolidation()&&CBPUtil.isCBPConsolidationProgram(programName)){
				if(cbpProgramParticipant!=null){
					program = progMan.getProgramWithParticipants(cbpProgramName);
				}else{
					program = progMan.getProgramWithParticipants(CBPUtil.CBP14DO);
				}
			}else{
				program = progMan.getProgramWithParticipants(programName);	
			}
			if(program==null){
				return;
			}
			for (ProgramParticipant totPP : program.getProgramParticipants()) {
				if (totPP == null || totPP.getParticipant().isClient()) {
					continue;
				}
				totalParticipantList.add(totPP);
				
				if (first) {
					first = false;
					// this could be first data load or vars are already set
					if (NO_PROGRAM.equals(currProgram)) {
						if (NO_PROGRAM.equals(displayProgram)) {
							setCurrProgram(pp.getProgramName());
							displayProgram = this.getCurrProgram();
						} else {
							setCurrProgram(displayProgram);
						}
					}
				}
			}

			// get aggregated participants
			addLevel(pp, child1);

//			aggTreeRoot.addChild(child1.getData(), child1);
			//DRMS-3970
			aggTreeRoot.addChild(child1.getIndentifyNumber(), child1);
		}
		loadParticipantList();
		needToLoadTree = false;
		Date ed = new Date();
	}
	

	
	/**
	 * Recursively called to add sub levels of aggregation
	 * @param pp
	 * @param parent
	 */
	private void addLevel(ProgramParticipant pp, AggregationTreeNode<String> parent) {
		String programName = pp.getProgramName();
		Set<ProgramParticipant> childPP = null;
		if(isEnableCBPConsolidation()&&CBPUtil.isCBPConsolidationProgram(programName)){
//			ProgramParticipant instance = CBPUtil.getProgramParticipant(totalParticipantListWithoutConsolidation, pp);
//			if(instance!=null){
//				pp=instance;
//			}
			if(cbpProgramParticipant!=null){
				pp=cbpProgramParticipant;
				pp.setProgramName(cbpProgramName);
			}
		}
		
		childPP = getAggregationManager().getDescendants(pp);

		if (childPP == null) {
			return;
		}

		ArrayList<AggregationTreeNode<String>> sorted = new ArrayList<AggregationTreeNode<String>>();
		for (ProgramParticipant cPP : childPP) {
			AggregationTreeNode<String> subChild1 = new AggregationTreeNode<String>();
			subChild1.setData(cPP.getParticipantName());
			sorted.add(subChild1);

			// recursive call
			if (multiLevelMode) {
				addLevel(cPP, subChild1);
			}
		}
		
		Collections.sort(sorted);
		
		for (AggregationTreeNode<String> node : sorted) {
//			parent.addChild(node.getData(), node);
			//DRMS-3970
			parent.addChild(node.getIndentifyNumber(), node);
		}
	}

	private void loadParticipantList() {
		availableParticipantList.clear();
        Set<String> availableParticipantTree = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
        List<ProgramParticipant> ppList = getTotalParticipantList();
		for (ProgramParticipant pp : ppList) {
			if (pp.getProgramName().equals(getCurrProgram()) 
					&& !currParticipant.equals(pp.getParticipantName())
					&& !currentChild(pp.getParticipantName())) {
				//availableParticipantList.add(pp.getParticipantName());
                availableParticipantTree.add(pp.getParticipantName());
			}
		}

		if (availableParticipantTree.isEmpty()) {
			availableParticipantList.add(NO_MATCH);
			//DRMS-6474 Even the list is empty, the logic should also keep the current program status.
			//setCurrProgram(NO_PROGRAM);
		} else {
            for (String p: availableParticipantTree){
                availableParticipantList.add(p);
            }
		}
	}
	//DRMS-6474
	private void loadParticipantList(boolean onlyDissociateNodesFlag){
		loadParticipantList();
		if(onlyDissociateNodesFlag){
			TreeNode<String> programTree =getAggregationTreeChild(aggTreeRoot,getCurrProgram());
			List<String> programTreeNodes = getTreeChildIterator(programTree);
			if(availableParticipantList!=null&&programTreeNodes!=null){
				availableParticipantList = (ArrayList<String>) ListUtils.subtract(availableParticipantList, programTreeNodes);	
			}
		}else{
		}
	}
	
	/**
	 * @return the totalParticipantList
	 */
	public List<ProgramParticipant> getTotalParticipantList() {
		if(isEnableCBPConsolidation()){
			totalParticipantList = CBPUtil.transferProgramParticipantWithParticipant(totalParticipantList);	
		}
		return totalParticipantList;
	}
	private List<ProgramParticipant> totalParticipantListWithoutConsolidation = new ArrayList<ProgramParticipant>();
	//DRMS-6474
	/**
	 * Separate this function with loadParticipantList main for follow the Open - Close principle
	 */
	public void showParticipantList(){
		availableParticipantList.clear();
        Set<String> availableParticipantTree = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
        List<ProgramParticipant> ppList = getTotalParticipantList();
		for (ProgramParticipant pp : ppList) {
			if (pp.getProgramName().equals(getCurrProgram()) && !currParticipant.equals(pp.getParticipantName())){
                availableParticipantTree.add(pp.getParticipantName());
			}
		}

		if (availableParticipantTree.isEmpty()) {
			availableParticipantList.add(NO_MATCH);
		} else {
            for (String p: availableParticipantTree){
                availableParticipantList.add(p);
            }
		}
	}
	public boolean currentChild(String participantName) {
		TreeNode<String> node = null;
		
//		TreeNode<String> root = aggTreeRoot.getChild(getCurrProgram());
		TreeNode<String> root =getAggregationTreeChild(aggTreeRoot,getCurrProgram());
		if (root != null) {
//			node = root.getChild(participantName);
			node = getAggregationTreeChild(root,participantName);
		}
		
		return node != null;
	}
	private TreeNode<String> getAggregationTreeChild(TreeNode<String> root,String data){
		Iterator<Map.Entry<Object, TreeNode<String>>> i = root.getChildren();
		while(i.hasNext()){
			Entry<Object, TreeNode<String>> entry = i.next();
			TreeNode<String> value = entry.getValue();
			String compareData = value.getData();
			if(data.equalsIgnoreCase(compareData)){
				return value;
			}
		}
		return null;
	}
	
	private List<String> getTreeChildIterator(TreeNode<String> tree){
		List<String> result = new ArrayList<String>();
		Iterator<Map.Entry<Object, TreeNode<String>>> i = tree.getChildren();
		while(i.hasNext()){
			Entry<Object, TreeNode<String>> entry = i.next();
			TreeNode<String> value = entry.getValue();
			String data = value.getData();
			result.add(data);
		}
		return result;
	}
	
	/**
	 * Called after processExapnsion and processSelection (except on open when it
	 * is only called).
	 * @param tree
	 * @return
	 */
	public Boolean adviseNodeSelected(UITree tree) {
		if (!multiLevelMode) {
			if (tree != null) {
				String rowKey = tree.getRowKey().toString(); 
				int level = StringUtils.countMatches(rowKey, ":");
				if (level == 0) {
					//DRMS-3970
					//return rowKey.equals(this.displayProgram);
					String compareProgram = (String) tree.getRowData();
					return compareProgram.equals(this.displayProgram);
				} else {
					//DRMS-4572
					return null;
				}
			}
			
		}
		return null;  // selected mode does not change
	}
	
	/**
	 * The tree expand expand listener method.
	 */
	public void processExpansion(NodeExpandedEvent nodeExpandedEvent) {
		// get the source or the component who fired this event.
		HtmlTree tree = (HtmlTree) nodeExpandedEvent.getComponent();
		String rowKey = tree.getRowKey().toString();  // CPP:p1
		int level = StringUtils.countMatches(rowKey, ":");
		if (level == 0) {
			selectedParticipantName = "";
			//DRMS-3970
			this.setCurrProgram((String) tree.getRowData());
//			this.setCurrProgram(rowKey);
			this.setDisplayProgram(this.getCurrProgram());
			this.loadParticipantList();
		}
	}

	
    	public void processSelection(NodeSelectedEvent event) {
		HtmlTree tree = (HtmlTree) event.getComponent();
        String rowKey = "";
        boolean onlyDissociateNodesFlag = false;
        if (tree.getRowKey() != null){
                rowKey = tree.getRowKey().toString();  // 0:CPP:p1

                int level = StringUtils.countMatches(rowKey, ":");
//                setCurrProgram((String) tree.getRowData());
                                
                if (multiLevelMode) {   
                	// facdash
                	//root-participant:program:node-participant
                    if (level == 0) {
                    	//clicked on a root participant in facdash
                        selectedParticipantName = (String)tree.getRowData();
                        aggDisplay = AGG_ROOT;
                        this.setParentProgram(NO_PROGRAM);
                        this.setCurrProgram(NO_PROGRAM);
                    } else if(level ==1){
                        // clicked on a program in facdash
                    	//root-participant:program
                        selectedParticipantName = "";
                        aggDisplay = "";
                		
                		int loc = rowKey.indexOf(":");
                		String indentify =rowKey.substring(loc+1, rowKey.length());
                		if(indentify != null && indentify.length() > 0){
                   		 AggregationTreeNode<String> programNode = getAggregationTreeNode(indentify,aggTreeRoot);
                            if(programNode!=null){
                            	this.setParentProgram(NO_PROGRAM);
                            	this.setCurrProgram(programNode.getData());
                            	this.setDisplayProgram(programNode.getData());
                            }
                		}
                    } else if(level>1){
                    	// clicked on a node participant in facdash
                    	//root-participant:program:node-participant
                    	selectedParticipantName = (String)tree.getRowData();
                        aggDisplay = rowKey.replaceFirst("0:", "Root:");
                        int loc = rowKey.indexOf(":", 2);
                        if (loc > -1) {
//                            String prog = rowKey.substring(2, loc);
//                            if (prog != null && prog.length() > 0) {
//                                this.setParentProgram(prog);
//                            }
                        	//DRMS-6985
                        	String indentify = rowKey.substring(2, loc);
                        	if(indentify != null && indentify.length() > 0){
                        		 AggregationTreeNode<String> programNode = getAggregationTreeNode(indentify,aggTreeRoot);
                                 if(programNode!=null){
                                 	this.setParentProgram(programNode.getData());
                                 	this.setCurrProgram(programNode.getData());
                                 	this.setDisplayProgram(programNode.getData());
                                 }
                        	}
                        }
                    }
                } else {
                	//program:node-participant
                	if(level ==0){
                		//program 
                		String indentify = rowKey;
                		if(indentify != null && indentify.length() > 0){
                   		 AggregationTreeNode<String> programNode = getAggregationTreeNode(indentify,aggTreeRoot);
                            if(programNode!=null){
                            	this.setCurrProgram(programNode.getData());
                            	this.setDisplayProgram(programNode.getData());
                            }
                		}
                		onlyDissociateNodesFlag = true;
                        selectedParticipantName = "";
                	}else if(level>0){
                		//node-participant
                		selectedParticipantName = (String)tree.getRowData();
                        int loc = rowKey.indexOf(":");
                        
                        //DRMS-3970
                        String indentify = rowKey.substring(0, loc);
                        if(indentify != null && indentify.length() > 0){
	                        AggregationTreeNode<String> programNode = getAggregationTreeNode(indentify,aggTreeRoot);
	                        if(programNode!=null){
	                        	this.setCurrProgram(programNode.getData());
	                        	this.setDisplayProgram(programNode.getData());
	                        }
                        }
                        
                	}
                }
                loadParticipantList(onlyDissociateNodesFlag);
        
         }else{
            
             this.treeError = "Error Navigating the tree, please re-navigate";
             MsgUtils.addMsgError(treeError);    
        }
        
        
	}
    	
    private AggregationTreeNode<String> getAggregationTreeNode(String rowKey,AggregationTreeNode<String> tree){
    	AggregationTreeNode<String> result = null;
    	String indentify = String.valueOf(tree.getIndentifyNumber());
    	if(indentify.equalsIgnoreCase(rowKey)){
    		result = tree;
    		return result;
    	}else{
    		Iterator<Entry<Object, TreeNode<String>>> i = tree.getChildren();
    		while(i.hasNext()){
    			Entry<Object, TreeNode<String>> entry = i.next();
    			AggregationTreeNode<String> child = (AggregationTreeNode<String>) entry.getValue();
    			result = getAggregationTreeNode(rowKey,child);
    			if(result!=null){
    				break;
    			}
    		}
    	}
    	return result;
    }
    
	public void dropListener(DropEvent dropEvent) {
		if (this.currProgram.equals(dropEvent.getDragType())) {
			if (selection.size() > 1) {
				if (!NO_MATCH.equals((String) dropEvent.getDragValue())) {
					Iterator<Object> it = selection.getKeys();
					while (it.hasNext()) {
						Integer key = (Integer)it.next();
						String partName = this.getAvailableParticipantList().get(key.intValue());
						aggregateParticipant(partName);
					}
					
					((SimpleSelection)selection).clear();
				}
			} else {
				if (!NO_PROGRAM.equals((String) dropEvent.getDragValue())) {
					aggregateParticipant((String) dropEvent.getDragValue());
				}
			}
		
			// force render of affected components
			// resolve drag destination attributes
		    UITreeNode destNode = (dropEvent.getSource() instanceof UITreeNode) ? (UITreeNode) dropEvent.getSource() : null;
		    UITree destTree = destNode != null ? destNode.getUITree() : null;
	
		    // resolve drag source attributes
		    UIComponent srcComp = null;
		    if (dropEvent.getDraggableSource() instanceof UIComponent) {
		    	UIComponent comp = (UIComponent)dropEvent.getDraggableSource();
		    	srcComp = comp.findComponent("availableParts");
		    }
		    
		    AjaxContext ac = AjaxContext.getCurrentInstance();
		    // Add destination tree to reRender
		    try {
		        ac.addComponentToAjaxRender(destTree);
		        if (srcComp != null) {
		        	ac.addComponentToAjaxRender(srcComp);
		        }
		    } catch (Exception e) {
		        System.err.print(e.getMessage());
		    }
		}
	}
	
	private void aggregateParticipant(String participantName) {
		ProgramParticipantManager progPartMan = (ProgramParticipantManager) EJBFactory
				.getBean(ProgramParticipantManagerBean.class);
		if(isEnableCBPConsolidation()&&CBPUtil.isCBPConsolidationProgram(currProgram)){
			aggregateParticipantMerge(CBPUtil.CBP14DO,participantName,progPartMan);
			aggregateParticipantMerge(CBPUtil.CBP26DO,participantName,progPartMan);
			aggregateParticipantMerge(CBPUtil.CBP48DO,participantName,progPartMan);
			aggregateParticipantMerge(CBPUtil.CBP14DA,participantName,progPartMan);
			aggregateParticipantMerge(CBPUtil.CBP26DA,participantName,progPartMan);
			aggregateParticipantMerge(CBPUtil.CBP48DA,participantName,progPartMan);
		}else{
			aggregateParticipantMerge(currProgram,participantName,progPartMan);
		}
		
	}
	private void aggregateParticipantMerge(String currProgram,String participantName,ProgramParticipantManager progPartMan){
		ProgramParticipant parentPP = progPartMan.getProgramParticipant(currProgram, currParticipant, false);
		ProgramParticipant childPP = progPartMan.getProgramParticipant(currProgram, participantName, false);

		if (parentPP == null || childPP == null) {
			return;
		}

		needToLoadTree = true;

		if (getAggregationManager().isDescendant(childPP, parentPP)) {
			// Need to break link before adding to
			getAggregationManager().removeParent(parentPP);
			parentPP = progPartMan.getProgramParticipant(currProgram, currParticipant, false);
			childPP = progPartMan.getProgramParticipant(currProgram, participantName, false);
		} 
		getAggregationManager().addChild(parentPP, childPP);
	}
	
	

	public TreeNode<String> getTreeNode() {
		multiLevelMode = false;
		if (needToLoadTree) {
			loadTree();
		}

		//return aggTreeNodes;
		return getAggTreeRoot(); // makes the tree look expanded
	}
	
	public TreeNode<String> getMultiTreeNode() {
		multiLevelMode = true;
		if (needToLoadTree) {
			loadTree();
		}

		return aggTreeNodes;
		//return aggTreeRoot; // makes the tree look expanded
	}

	public AggregationTreeNode<String> getAggTreeRoot() {
		if(isEnableCBPConsolidation()){
			
		}
		return aggTreeRoot;
	}

	public void setAggTreeRoot(AggregationTreeNode<String> aggTreeRoot) {
		this.aggTreeRoot = aggTreeRoot;
	}

	public AggregationTreeNode<String> getAggTreeNodes() {
		return aggTreeNodes;
	}

	public void setAggTreeNodes(AggregationTreeNode<String> aggTreeNodes) {
		this.aggTreeNodes = aggTreeNodes;
	}

	public ArrayList<String> getAvailableParticipantList() {
		return availableParticipantList;
	}

	public void setAvailableParticipantList(
			ArrayList<String> availableParticipantList) {
		this.availableParticipantList = availableParticipantList;
	}

	public String getCurrParticipant() {
		return currParticipant;
	}

	public void setCurrParticipant(String currParticipant) {
		needToLoadTree = true; // reset tree
		this.currParticipant = currParticipant;
	}

	public String getCurrProgram() {
		return currProgram;
	}

	public void setCurrProgram(String currProgram) {
		this.currProgram = currProgram;
	}

	public String getParentProgram() {
		if (parentProgram == null || NO_PROGRAM.equals(parentProgram)) {
			parentProgram = this.getCurrProgram();
		}
		return parentProgram;
	}

	public void setParentProgram(String parentProgram) {
		this.parentProgram = parentProgram;
	}

	public String getDisplayProgram() {
		return displayProgram;
	}

	public void setDisplayProgram(String displayProgram) {
		this.displayProgram = displayProgram;
	}

	public String getSelectedParticipantName() {
		return selectedParticipantName;
	}

	public void setSelectedParticipantName(String selectedParticipantName) {
		this.selectedParticipantName = selectedParticipantName;
	}

	public String getParentParticipant() {
		return parentParticipant;
	}

	public void setParentParticipant(String parentParticipant) {
		this.parentParticipant = parentParticipant;
		setCurrParticipant(parentParticipant);
	}
	
	public void setLoadParticipant(String parentParticipant) {
		setAggDisplay(AGG_ROOT);
		setParentParticipant(parentParticipant);
		setCurrProgram(NO_PROGRAM);
		setDisplayProgram(NO_PROGRAM);
		selectedParticipantName = "";
	}
	

	public String getAggDisplay() {
		
		if(getFeatures().isAggBatchUpdateEnabled()){

			return aggDisplay;
		}
		ExternalContext context = 
			FacesContext.getCurrentInstance().getExternalContext();
		HttpServletRequest request =
			(HttpServletRequest) context.getRequest();
		if (request.getParameter("aggDisplayMode") != null && request.getParameter("aggDisplayMode").equals("reset")) {
			this.setAggDisplay(AGG_ROOT);
			return aggDisplay;
		}
		//DRMS-7581
		int level = StringUtils.countMatches(aggDisplay, ":");
		if(level>0){
			String aggDisplayString=AGG_ROOT;
			String[] result = StringUtils.split(aggDisplay, ":");	
			for(int i=1;i<result.length;i++){
				String indentify = result[i];
				AggregationTreeNode<String> programNode = getAggregationTreeNode(indentify,aggTreeRoot);
				if(programNode==null){
					return aggDisplay;
				}
				String data = programNode.getData();
				aggDisplayString = aggDisplayString+":"+data;
	    	}
			aggDisplay = aggDisplayString;
		}
		 

	    	
		 
		return aggDisplay;
	}


    public int getAggPartCount() {
        return AggPartCount;
    }

    public void setAggPartCount(int AggPartCount) {
        this.AggPartCount = AggPartCount;
    }

    public int getAggCount(Participant part){

          int aggCount = 0;
          StringBuilder programsSB = new StringBuilder();
          for (ProgramParticipant ppp : part.getProgramParticipants()) {
                aggCount += getAggregationManager().getDescendants(ppp).size();
				programsSB.append(ppp.getProgramName() + " ");
			}
        return aggCount;
    }
	public void setAggDisplay(String aggDisplay) {
		this.aggDisplay = aggDisplay;
	}

	public String getRemovingParticipant() {
		return removingParticipant;
	}

	public void setRemovingParticipant(String removingParticipant) {
		this.removingParticipant = removingParticipant;
		List<ProgramParticipant> ppList = getTotalParticipantList();
		for (ProgramParticipant pp : ppList) {
			if (pp.getProgramName().equals(this.getDisplayProgram()) 
					&& removingParticipant.equals(pp.getParticipantName())) {
				
				if(isEnableCBPConsolidation()&&CBPUtil.isCBPConsolidationProgram(pp.getProgramName())){
					String participantName = pp.getParticipantName();
					List<ProgramParticipant> list = CBPUtil.revertProgramParticipantWithParticipant(totalParticipantListWithoutConsolidation, participantName);
					for(ProgramParticipant instance : list){
						getAggregationManager().removeParent(instance);		
					}
				}else{
					getAggregationManager().removeParent(pp);	
				}
				
				
				selectedParticipantName = "";
				loadTree();
				
				break;
			}
		}
	}

	public HtmlTree getTree() {
		return tree;
	}

	public void setTree(HtmlTree tree) {
		this.tree = tree;
	}

	public Selection getSelection() {
		return selection;
	}

	public void setSelection(Selection selection) {
		this.selection = selection;
	}
    
    public String getTreeError() {
        return treeError;
    }

    public void setTreeError(String treeError) {
        this.treeError = treeError;
    }
    
    public PSS2Features getFeatures() {
    	if(features==null){
    		SystemManager systemManager = EJB3Factory.getLocalBean(SystemManager.class);
    		features = systemManager.getPss2Features();
    	}
		return features;
	}
    private static ProgramManager pm;

	public static ProgramManager getPm() {
		if(pm==null){
			pm = EJB3Factory.getBean(ProgramManager.class);
		}
		return pm;
	}
	public boolean isEnableCBPConsolidation(){
		Boolean result = CBPUtil.isEnableCBPConsolidation();
		if(result==null){
			result = CBPUtil.isEnableCBPConsolidation(getPm().getAllPrograms());
		}
		return result;
	}

}
