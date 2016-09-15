package com.akuacom.pss2.facdash;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.faces.context.FacesContext;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;

import com.akuacom.jsf.model.AbstractTreeContentProvider;
import com.akuacom.jsf.model.TreeContentProvider;
import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.participant.ParticipantManager;
import com.akuacom.pss2.participant.ParticipantManagerBean;
import com.akuacom.pss2.program.ProgramManager;
import com.akuacom.pss2.program.ProgramManagerBean;
import com.akuacom.pss2.program.participant.ProgramParticipant;
import com.akuacom.pss2.program.participant.ProgramParticipantAggregationManager;
import com.akuacom.pss2.program.participant.ProgramParticipantAggregationManagerBean;
import com.akuacom.pss2.program.participant.TreeNodeVo;

public class AggregationTree implements Serializable {

	private static final long serialVersionUID = -5386319818137004809L;
	private static final Logger log = Logger.getLogger(AggregationTree.class.getName());
	
	private ProgramParticipantAggregationManager aggMan = null;
	
	
//    private Header1 headerBean; // +setter
	
	public Header1 getHeaderBean() {
			FacesContext context = FacesContext.getCurrentInstance();
			Header1 header = (Header1)context.getApplication()
		          .evaluateExpressionGet(context, "#{header1}", Header1.class);
			
			return header;
	}
	
	
	private com.akuacom.pss2.web.aggregation.AggregationTree legacyAggTree; // +setter
		
	public com.akuacom.pss2.web.aggregation.AggregationTree getLegacyAggTreeBean() {
			FacesContext context = FacesContext.getCurrentInstance();
			com.akuacom.pss2.web.aggregation.AggregationTree tree = (com.akuacom.pss2.web.aggregation.AggregationTree)context.getApplication()
		          .evaluateExpressionGet(context, "#{aggTree}", com.akuacom.pss2.web.aggregation.AggregationTree.class);
			legacyAggTree = tree;
		return legacyAggTree;
	}
	public AggregationTree(){
		needRetriveData = true;
		setCurrentParticipant(FDUtils.getParticipantName());
		if(null==FDUtils.getJSFProgram()){
			setCurrentProgram("");
		}else{
			setCurrentProgram(FDUtils.getJSFProgram().getName());
		}
	}
	
	private boolean needRetriveData = false;
	
	private ProgramParticipantAggregationManager getAggregationManager() {
		if (aggMan == null) {
			aggMan = (ProgramParticipantAggregationManager) EJBFactory.getBean(ProgramParticipantAggregationManagerBean.class);
		}
		
		return aggMan;
	}

	/** event performance tree table content provider*///  TreeContentProvider
	private AbstractTreeContentProvider<AggregationTreeNode> aggTree = 
		new AbstractTreeContentProvider<AggregationTreeNode>(){

		private List<AggregationTreeNode> contents;
		private int totalRowCount = -1; //-1 indicates no search executed
		@Override
		public List<AggregationTreeNode> getChildren(AggregationTreeNode parent) {
			if (!hasChildren(parent)) {
				return Collections.emptyList();
			}
			List<AggregationTreeNode> contents = parent.getContents();
			contents = CBPUtil.transferAggregationTreeNodeList(contents);
			return contents;
		}

		@Override
		public boolean hasChildren(AggregationTreeNode parent) {
			return !parent.isLeafNode();
		}
		
		@Override
		public List<AggregationTreeNode> getContents() {
			contents = CBPUtil.transferAggregationTreeNodeList(contents);
			return contents;
		}
		
		@Override
		public int getTotalRowCount() {
			return totalRowCount;
		}
		
		@Override
		public String getRowStyleClass(AggregationTreeNode row) {
			String style="";
			if(getCurrentParticipant().equalsIgnoreCase(row.getParticipantName())&&getCurrentProgram().equalsIgnoreCase(row.getProgramName())){
				style = "row-current ";
			}
			if(row.getParticipantName()==null||row.getParticipantName().trim().length()==0){
				return style+" row-category";//set a specific style for program row
			}
//			if(row.isRootNode()){
//				return style+" row-root";
//			}else if(!row.isLeafNode()){
//				return style+" row-category";
//			}
		
			return style;
		}
		
		@Override
		public boolean isEagerLoad(AggregationTreeNode node){
			return true;
			
		}
		
		@Override
		public boolean isAutoExpand(AggregationTreeNode node){
			return true;
		}
		
		@Override
		public void updateModel() {
			try {
				if(needRetriveData){
					List<AggregationTreeNode> result = new ArrayList<AggregationTreeNode>();
					ParticipantManager partMan = (ParticipantManager)EJBFactory.getBean(ParticipantManagerBean.class);
					// get the parent participant
					Participant part = partMan.getParticipant(getHeaderBean().getLoginParticipant(), false);//just get participant and related programs
					if (part == null) {
						return;
					}
					
					AggregationTreeNode root = new AggregationTreeNode();
					
					root.setParticipantName(part.getParticipantName());
					root.setAccountNo(part.getAccountNumber());
					root.setSecondaryAccountNo(part.getSecondaryAccountNumber());
					root.setRootNode(true);
					
					List<AggregationTreeNode> branch0 = new ArrayList<AggregationTreeNode>();
					if(getHeaderBean().isDisplayAggregation()){
						for (ProgramParticipant pp : part.getProgramParticipants()) {
							// get program
							AggregationTreeNode child1 = new AggregationTreeNode();
							child1.setProgramName(pp.getProgramName());

							//retrieve participant by program
							ProgramManager progMan = (ProgramManager)EJBFactory.getBean(ProgramManagerBean.class);
//							Program program = progMan.getProgramWithParticipants(pp.getProgramName());
							
							//get branch for each program, every branch is a subTree
							
							List<TreeNodeVo> subTree = progMan.getChildren(pp.getParticipantName(), pp.getProgramName());
							//subTree is retrieved in order, parents are always before kids.
							List<AggregationTreeNode> branch11 = new ArrayList<AggregationTreeNode>();
							
							for(TreeNodeVo node : subTree){
								//parent is root
								if(root.getParticipantName().equalsIgnoreCase(node.getParentName())){
									AggregationTreeNode treeNode = new AggregationTreeNode();
									treeNode.setParticipantName(node.getParticipantName());
									treeNode.setProgramName(node.getProgramName());
									treeNode.setAccountNo(node.getAccountNumber());
									treeNode.setSecondaryAccountNo(node.getSecondaryAccountNo());
									treeNode.setContents(new ArrayList<AggregationTreeNode>());
									treeNode.setLeafNode(true);
									
									branch11.add(treeNode);
								}else{
									//Since subTree is retrieved in order, parents are always before kids. parents are created and saved in List(brach11) already.
									AggregationTreeNode parent = getEntryByKey(branch11, node.getParentName());
									if(parent==null){
										log.error("Tree build failure...");
									}
									AggregationTreeNode treeNode = new AggregationTreeNode();
									treeNode.setParticipantName(node.getParticipantName());
									treeNode.setProgramName(node.getProgramName());
									treeNode.setContents(new ArrayList<AggregationTreeNode>());
									treeNode.setLeafNode(true);
									
									parent.getContents().add(treeNode);
									parent.setLeafNode(false);
									
								}
							}
							
							sortFunction(branch11);
							child1.setContents(branch11);
							if(branch11.isEmpty()){
								child1.setLeafNode(true);
							}else{
								child1.setLeafNode(false);
								
							}
							branch0.add(child1);
						}
					}
					
					sortFunction(branch0);
					root.setContents(branch0);
					if(branch0.isEmpty()){
						root.setLeafNode(true);
					}else{
						root.setLeafNode(false);
					}
					result.add(root);
					this.contents = result;
					sort(contents, getConstraint());
					totalRowCount = contents.size();
					needRetriveData = false;
				}
				
			} catch (Exception e) {
				log.error(e);
			}
		}
	};
	
	
	private static AggregationTreeNode getEntryByKey(List<AggregationTreeNode> tree, String key){
		if(tree==null) return null;
		for(AggregationTreeNode node:tree){
			if(key.equalsIgnoreCase(node.getParticipantName())){
				return node;
			}
		}
		
		return null;
	}
	
	public void sortFunction(List<AggregationTreeNode> contents){
		final boolean ascendent=true;
		final String columnName="participantName";
	
		Collections.sort(contents, new Comparator<AggregationTreeNode>(){
			@Override
			public int compare(AggregationTreeNode obj1, AggregationTreeNode obj2) {
				if(ascendent)
					return doCompare(obj1, obj2,columnName);
				else
					return doCompare(obj2, obj1,columnName);
			}
		});
	
	}
	
	private static final int LESSER = -1;
	private static final int EQUAL = 0;
	private static final int GREATER = 1;
	protected int doCompare(AggregationTreeNode row1, AggregationTreeNode row2,String property){
		//  alternate to actual value comparison 
		if (null == row1 && null != row2) {
			return LESSER;
		} else if (null != row1 && null == row2) {
			return GREATER;
		} else if (null == row1 && null == row2) {
			return EQUAL;		
		}

		int response = EQUAL;
		try {
			Object v1 = (null == property) ? row1 : getValue(row1,property);
			Object v2 = (null == property) ? row2 : getValue(row2,property);

			if (v1 instanceof Comparable && v2 instanceof Comparable) {
				response = (((Comparable)v1).compareTo((Comparable)v2));
			} else {
				response = (v1==null ? "" : v1.toString()).compareTo(v2==null ? "" : v2.toString());
			}
		} catch (Exception e) {
			log.error("Exception occurred while comparing", e);
			response = EQUAL;
		}
		
		return response;
	}
	
	/**
	 * Return the value of the (possibly nested) property of the specified name, for the specified bean, with no type conversions
	 * @param obj - a {@link java.lang.Object}
	 * @return object - a {@link java.lang.Object} - return of given method
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws NoSuchMethodException
	 * @throws NoSuchFieldException 
	 * @throws SecurityException 
	 */
	private Object getValue(Object bean, String name) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		if(name==null||bean==null) return bean;
		
		return PropertyUtils.getNestedProperty(bean, name);
	}
	
	public TreeContentProvider<AggregationTreeNode> getAggTree() {
		return aggTree;
	}
	public void setAggTree(
			AbstractTreeContentProvider<AggregationTreeNode> reportPerformanceTree) {
		this.aggTree = reportPerformanceTree;
	}	
	
	public void rowSelected() {
		if(aggTree.getFirstSelected().getParticipantName()!=null){
			setCurrentParticipant(aggTree.getFirstSelected().getParticipantName());
			setCurrentProgram(aggTree.getFirstSelected().getProgramName());
			 
			getHeaderBean().switchAggregationTree(aggTree.getFirstSelected().getParticipantName(), aggTree.getFirstSelected().getProgramName());
			 
			String root = getHeaderBean().getParentParticipant();
			String aggDisplay = "";
			if(getCurrentParticipant().equalsIgnoreCase(root)){
				aggDisplay = "Root";
			}else{
				aggDisplay ="Root:"+aggTree.getFirstSelected().getProgramName()+":"+aggTree.getFirstSelected().getParticipantName();
			}
			 
			getLegacyAggTreeBean().setAggDisplay(aggDisplay);
		 }
	 }
	
	public void rowSwitchAction(){
		FacesContext ctx = FacesContext.getCurrentInstance();  
		String selectedParticipant = (String)ctx.getExternalContext().getRequestParameterMap().get("selectedParticipantName");
		String selectedProgram = (String)ctx.getExternalContext().getRequestParameterMap().get("selectedProgramName");
		selectedProgram = selectedProgram==null?"":selectedProgram;
		
		setCurrentParticipant(selectedParticipant);
		setCurrentProgram(selectedProgram);
		 
		getHeaderBean().switchAggregationTree(selectedParticipant, selectedProgram);
		 
		String root = getHeaderBean().getParentParticipant();
		String aggDisplay = "";
		if(getCurrentParticipant().equalsIgnoreCase(root)){
			aggDisplay = "Root";
		}else{
			aggDisplay ="Root:"+CBPUtil.convertProgramDispalyName(selectedProgram)+":"+selectedParticipant;
		}
		 
		getLegacyAggTreeBean().setAggDisplay(aggDisplay);
		
	}
	
	
//	setSelectedClientName((String)ctx.getExternalContext().getRequestParameterMap().get("selectedClientName"));
//	<f:param name="selectedParticipantName" value="#{item.participantName}" />
//	<f:param name="selectedProgramName" value="#{item.programName}" />
	 
	private String currentParticipant;
	
	private String currentProgram;
	
	public String getCurrentProgram() {
		return currentProgram==null?"":currentProgram;
	}
	public void setCurrentProgram(String currentProgram) {
		this.currentProgram = currentProgram;
	}
	public String getCurrentParticipant() {
		return currentParticipant;
	}
	public void setCurrentParticipant(String currentParticipant) {
		if(currentParticipant!=null){
			this.currentParticipant = currentParticipant;
		}
	}

}
