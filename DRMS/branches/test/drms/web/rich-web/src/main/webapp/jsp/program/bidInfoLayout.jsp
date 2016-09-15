<!doctype html public "-//w3c//dtd html 4.0 transitional//en">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<div>
	<table width="450px">
						<!--Row 1-->
						<tr>
							<td>
								<h:outputLabel for=""  value="Min Bid(KW)" styleClass="program_edit_label"/>
							</td>
							<td>
								<h:inputText styleClass="" value="#{programConfigureDataModel.bidConfigureDataModel.akuaBidConfig.minBidKW}" />
							</td>
						</tr>
						<!--Row 2-->
						<tr>
							<td>
								<h:outputLabel for=""  value="Default Bid(KW)" styleClass="program_edit_label"/>
							</td>
							<td>
								<h:inputText styleClass="" value="#{programConfigureDataModel.bidConfigureDataModel.akuaBidConfig.defaultBidKW}" />
							</td>
						</tr>
						<!--Row 3-->
						<tr>
							<td>
								<h:outputLabel for=""  value="Min Consetutive Blocks" styleClass="program_edit_label"/>
							</td>
							<td>
								<h:inputText styleClass="" value="#{programConfigureDataModel.bidConfigureDataModel.akuaBidConfig.minConsectutiveBlocks}" />
							</td>
						</tr>
						<!--Row 4-->
						<tr>
							<td>
								<h:outputLabel for=""  value="Is Dras Biding" styleClass="program_edit_label"/>
							</td>
							<td>
								<h:selectBooleanCheckbox value="#{programConfigureDataModel.bidConfigureDataModel.akuaBidConfig.drasBidding}"/>
							</td>
						</tr>
						<!--Row 5-->
						<tr>
							<td>
								<h:outputLabel for=""  value="RespondBy Time" styleClass="program_edit_label"/>
							</td>
							<td>
								<h:inputText styleClass="" value="#{programConfigureDataModel.bidConfigureDataModel.respondByTimeString}" />
							</td>
						</tr>
						<!--Row 6-->
						<tr>
							<td>
								<h:outputLabel for=""  value="Dras RespondBy Period(min)" styleClass="program_edit_label"/>
							</td>
							<td>
								<h:inputText styleClass="" value="#{programConfigureDataModel.bidConfigureDataModel.akuaBidConfig.drasRespondByPeriodM}" />
							</td>
						</tr>
						<!--Row 7-->
						<tr>
							<td>
								<h:outputLabel for=""  value="Accept RespondBy Period(min)" styleClass="program_edit_label"/>
							</td>
							<td>
								<h:inputText styleClass="" value="#{programConfigureDataModel.bidConfigureDataModel.akuaBidConfig.acceptTimeoutPeriodM}" />
							</td>
						</tr>
						<!--Row 8-->
						<tr>
							<td>
								<h:outputLabel for=""  value="Bid Blocks" styleClass="program_edit_label"/>
							</td>
							<td>
								<h:selectManyMenu id="programBidBlocksSelectItemsMenu" style="width:100px;height:100px">
									<f:selectItems value="#{programConfigureDataModel.bidConfigureDataModel.bidBlocksSelectItems}"/>
								</h:selectManyMenu>
							</td>
						</tr>
						<!--Row 9-->
						<tr>
							<td>
								<h:outputLabel for=""  value="Start Time" styleClass="program_edit_label"/>
							</td>
							<td>
								<h:inputText styleClass="" value="#{programConfigureDataModel.bidConfigureDataModel.startTimeString}" />
							</td>
						</tr>
						<!--Row 10-->
						<tr>
							<td>
								<h:outputLabel for=""  value="End Time" styleClass="program_edit_label"/>
							</td>
							<td>
								<h:inputText styleClass="" value="#{programConfigureDataModel.bidConfigureDataModel.endTimeString}" />
							</td>
						</tr>
	</table>
	<div style="text-align: center;">        
								<a4j:commandButton value="Add" alt="Add select bid block into program" action="#{programConfigureDataModel.bidConfigureDataModel.addDisplayBidBlocks}"
									title="Add select bid block into program"
									reRender="programBidBlocksSelectItemsMenu">
								</a4j:commandButton>
								<a4j:commandButton value="Clear" alt="Clear program bid blocks" action="#{programConfigureDataModel.bidConfigureDataModel.clearDisplayBidBlocks}"
									title="Clear program bid blocks"
									reRender="programBidBlocksSelectItemsMenu">
								</a4j:commandButton>
	</div>
</div>
