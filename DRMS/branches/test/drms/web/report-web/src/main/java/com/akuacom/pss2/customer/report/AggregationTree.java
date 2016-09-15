package com.akuacom.pss2.customer.report;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.ajax4jsf.context.AjaxContext;
import org.apache.commons.lang.StringUtils;
import org.richfaces.component.UITree;
import org.richfaces.component.UITreeNode;
import org.richfaces.component.html.HtmlTree;
import org.richfaces.event.DropEvent;
import org.richfaces.event.NodeExpandedEvent;
import org.richfaces.event.NodeSelectedEvent;
import org.richfaces.model.TreeNode;

import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.history.HistoryReportManager;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.participant.ParticipantManager;
import com.akuacom.pss2.program.Program;
import com.akuacom.pss2.program.ProgramManager;
import com.akuacom.pss2.program.participant.ProgramParticipant;
import com.akuacom.pss2.program.participant.ProgramParticipantAggregationManager;
import com.akuacom.pss2.program.participant.ProgramParticipantManager;
import javax.ejb.EJB;

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
	private ArrayList<ProgramParticipant> totalParticipantList = new ArrayList<ProgramParticipant>();
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
	
	private ProgramParticipantAggregationManager aggMan = null;
	private ParticipantManager partMan;
	private ProgramManager progMan;
	private ProgramParticipantManager progPartMan;
	
    private int AggPartCount;
	private ProgramParticipantAggregationManager getAggregationManager() {
		return this.getAggMan();
	}
	
	public void loadTree() {
		availableParticipantList.add(NO_MATCH);

		if (currParticipant == null) {
			return;
		}

		aggTreeRoot = new AggregationTreeNode<String>();
		aggTreeNodes = new AggregationTreeNode<String>();
		
		// get the parent participant
		Participant part =getPartMan().getParticipant(currParticipant, false);
		if (part == null) {
			return;
		}
		
		// NOTE! if you change this you MUST update Header1.setSwitchingParticipant
        String rootCount = "  (" + getAggCount(part) + ")";
		aggTreeRoot.setData(currParticipant);
		aggTreeNodes.addChild(0, aggTreeRoot);

		// load program nodes
		totalParticipantList.clear();
		boolean first = true;

		for (ProgramParticipant pp : part.getProgramParticipants()) {
			// get program
			AggregationTreeNode<String> child1 = new AggregationTreeNode<String>();
			child1.setData(pp.getProgramName());

			Program program = getProgMan().getProgramWithParticipants(pp.getProgramName());
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

			aggTreeRoot.addChild(child1.getData(), child1);
		}

		loadParticipantList();
		needToLoadTree = false;
	}

	/**
	 * Recursively called to add sub levels of aggregation
	 * @param pp
	 * @param parent
	 */
	private void addLevel(ProgramParticipant pp, AggregationTreeNode<String> parent) {
		Set<ProgramParticipant> childPP = getAggregationManager().getDescendants(pp);

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
			parent.addChild(node.getData(), node);
		}
	}

	private void loadParticipantList() {
		availableParticipantList.clear();
        Set<String> availableParticipantTree = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
		for (ProgramParticipant pp : totalParticipantList) {
			if (pp.getProgramName().equals(getCurrProgram()) 
					&& !currParticipant.equals(pp.getParticipantName())
					&& !currentChild(pp.getParticipantName())) {
				//availableParticipantList.add(pp.getParticipantName());
                availableParticipantTree.add(pp.getParticipantName());
			}
		}

		if (availableParticipantTree.isEmpty()) {
			availableParticipantList.add(NO_MATCH);
			setCurrProgram(NO_PROGRAM);
		} else {
            for (String p: availableParticipantTree){
                availableParticipantList.add(p);
            }
		}
	}
	
	public boolean currentChild(String participantName) {
		TreeNode<String> node = null;
		
		TreeNode<String> root = aggTreeRoot.getChild(getCurrProgram());
		if (root != null) {
			node = root.getChild(participantName);
		}
		
		return node != null;
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
					return rowKey.equals(this.displayProgram);
				} else {
					return false;
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
			this.setCurrProgram(rowKey);
			this.setDisplayProgram(this.getCurrProgram());
			this.loadParticipantList();
		}
	}

	public void processSelection(NodeSelectedEvent event) {
		HtmlTree tree = (HtmlTree) event.getComponent();
		String rowKey = tree.getRowKey().toString();  // 0:CPP:p1
		int level = StringUtils.countMatches(rowKey, ":");
		setCurrProgram((String) tree.getRowData());
		
		if (multiLevelMode) {   // facdash
			if (level == 0) {
				selectedParticipantName = (String)tree.getRowData();
				aggDisplay = AGG_ROOT;
			} else if (level != 1) {
				selectedParticipantName = (String)tree.getRowData();
				aggDisplay = rowKey.replaceFirst("0:", "Root:");
			} else {
				// clicked on a program in facdash
				selectedParticipantName = "";
				aggDisplay = "";
			} 
			if (level > 1) {
				int loc = rowKey.indexOf(":", 2);
				if (loc > -1) {
					String prog = rowKey.substring(2, loc);
					if (prog != null && prog.length() > 0) {
						this.setParentProgram(prog);
					}
				}
			}
			if (level == 0) {
				this.setParentProgram(NO_PROGRAM);
				this.setCurrProgram(NO_PROGRAM);
			}
		} else {
			if (level != 0) {
				selectedParticipantName = (String)tree.getRowData();
				int loc = rowKey.indexOf(":");
				this.setDisplayProgram(rowKey.substring(0, loc));
			} else {
				selectedParticipantName = "";
				this.setDisplayProgram(this.getCurrProgram());
			} 
		}
		loadParticipantList();
	}

	public void dropListener(DropEvent dropEvent) {
		if (this.currProgram.equals(dropEvent.getDragType()) && !NO_PROGRAM.equals((String) dropEvent.getDragValue())) {
			ProgramParticipant parentPP = getProgPartMan().getProgramParticipant(currProgram, currParticipant, false);
			ProgramParticipant childPP =getProgPartMan().getProgramParticipant(currProgram, (String) dropEvent.getDragValue(), false);

			if (parentPP == null || childPP == null) {
				return;
			}
			needToLoadTree = true;
			
			if (getAggregationManager().isDescendant(childPP, parentPP)) {
				// Need to break link before adding to
				getAggregationManager().removeParent(parentPP);
				parentPP =getProgPartMan().getProgramParticipant(currProgram, currParticipant, false);
				childPP = getProgPartMan().getProgramParticipant(currProgram, (String) dropEvent.getDragValue(), false);
			} 
			getAggregationManager().addChild(parentPP, childPP);
			
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

	public TreeNode<String> getTreeNode() {
		multiLevelMode = false;
		if (needToLoadTree) {
			loadTree();
		}

		//return aggTreeNodes;
		return aggTreeRoot; // makes the tree look expanded
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
		ExternalContext context = 
			FacesContext.getCurrentInstance().getExternalContext();
		HttpServletRequest request =
			(HttpServletRequest) context.getRequest();
		if (request.getParameter("aggDisplayMode") != null && request.getParameter("aggDisplayMode").equals("reset")) {
			this.setAggDisplay(AGG_ROOT);
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
		
		for (ProgramParticipant pp : totalParticipantList) {
			if (pp.getProgramName().equals(this.getDisplayProgram()) 
					&& removingParticipant.equals(pp.getParticipantName())) {
				getAggregationManager().removeParent(pp);
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
	
	public ProgramParticipantAggregationManager getAggMan() {
		if(aggMan==null)
			aggMan =EJBFactory.getBean(ProgramParticipantAggregationManager.class);
		return aggMan;
//		if(aggMan==null)
//			aggMan =ServiceLocator.findHandler(ProgramParticipantAggregationManager.class,
//					"pss2/ProgramParticipantAggregationManagerBean/remote");
//		return aggMan;
	}
	
	public ParticipantManager getPartMan() {
		if(partMan==null)
			partMan =EJBFactory.getBean(ParticipantManager.class);
//		if( partMan==null)
//			partMan= ServiceLocator.findHandler(ParticipantManager.class, 
//					"pss2/ParticipantManagerBean/remote");
		
		return partMan;
	}

	public ProgramManager getProgMan() {
		if(progMan==null)
			progMan =EJBFactory.getBean(ProgramManager.class);
//		if(progMan == null)
//			progMan=ServiceLocator.findHandler(ProgramManager.class, "pss2/ProgramManagerBean/remote");
		return progMan;
	}

	public ProgramParticipantManager getProgPartMan() {
		if(progPartMan==null)
			progPartMan =EJBFactory.getBean(ProgramParticipantManager.class);
//		if(progPartMan==null)
//			progPartMan = ServiceLocator.findHandler(ProgramParticipantManager.class, "pss2/ProgramParticipantManagerBean/remote");
		return progPartMan;
	}
}
