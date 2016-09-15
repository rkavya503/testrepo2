<!doctype html public "-//w3c//dtd html 4.0 transitional//en">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<div>
	<table width="360px">
						<!--Row 1-->
						<tr>
							<td>
								<h:outputLabel for=""  value="Mode Transitions" styleClass="program_edit_label"/>
							</td>
							<td>
								<h:selectManyMenu id="programModeTransitionsSelectItemsMenu" style="width:100px;height:100px">
									<f:selectItems value="#{programConfigureDataModel.modeConfigureDataModel.programModeTransitionsSelectItems}"/>
								</h:selectManyMenu>
							</td>
						</tr>
						<!--Row 2-->
						<tr>
							<td>
								<h:outputLabel for=""  value="Time" styleClass="program_edit_label"/>
							</td>
							<td>
								<h:inputText styleClass="" value="#{programConfigureDataModel.modeConfigureDataModel.timeString}" />
							</td>
							<td>
								<h:outputLabel for=""  value="hh:MM i.e. 13:00" styleClass="program_edit_label"/>
							</td>
						</tr>
						<!--Row 3-->
						<tr>
							<td>
								<h:outputLabel for=""  value="Signal" styleClass="program_edit_label"/>
							</td>
							<td>
								<h:selectOneMenu value="#{programConfigureDataModel.modeConfigureDataModel.currentSelectItem}">                     
									<f:selectItems value="#{programConfigureDataModel.modeConfigureDataModel.definedModeSignalLevelDefSelectItems}"/> 
								</h:selectOneMenu>
							</td>
							<td>
								<a4j:commandButton value="Add" alt="Add select mode transition into program" action="#{programConfigureDataModel.modeConfigureDataModel.addDisplayProgramModeTransition}"
									title="Add select mode transition into program"
									reRender="programModeTransitionsSelectItemsMenu">
								</a4j:commandButton>
								<a4j:commandButton value="Clear" alt="Clear program mode transitions" action="#{programConfigureDataModel.modeConfigureDataModel.clearDisplayProgramModeTransitions}"
									title="Clear program mode transitions"
									reRender="programModeTransitionsSelectItemsMenu">
								</a4j:commandButton>
							</td>
						</tr>
	</table>
</div>
