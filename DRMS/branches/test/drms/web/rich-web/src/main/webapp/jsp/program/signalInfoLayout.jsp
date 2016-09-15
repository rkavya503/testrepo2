<!doctype html public "-//w3c//dtd html 4.0 transitional//en">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<div>
	
	<table width="360px">
						
						<tr>
							<td width="20px">
								<h:outputLabel for=""  value="Signals" styleClass="program_edit_label_signal"/>
							</td>
							<td>
								<h:selectManyMenu id="programSignalSelectItemsMenu" style="width:100px;height:100px">
									<f:selectItems value="#{programConfigureDataModel.signalConfigureDataModel.programSignalSelectItems}"/>
								</h:selectManyMenu>
							</td>
						</tr>
						
						<tr>
							<td>
								
							</td>
							<td>
								<h:selectOneMenu value="#{programConfigureDataModel.signalConfigureDataModel.currentSelectSignal}">                     
									<f:selectItems value="#{programConfigureDataModel.signalConfigureDataModel.definedSignalSelectItems}"/> 
								</h:selectOneMenu>
							</td>
							<td>
								<a4j:commandButton value="Add" alt="Add select signal into program" action="#{programConfigureDataModel.signalConfigureDataModel.addDisplayProgramSignal}"
									title="Add select signal into program"
									reRender="programSignalSelectItemsMenu">
								</a4j:commandButton>
								<!--<h:commandButton value="Add" action="#{programConfigureDataModel.signalConfigureDataModel.addDisplayProgramSignal}" title="Add select signal into program"/>-->
							</td>
							<td>
								<a4j:commandButton value="Clear" alt="Clear program signals" action="#{programConfigureDataModel.signalConfigureDataModel.clearDisplayProgramSignals}"
									title="Clear program signals"
									reRender="programSignalSelectItemsMenu">
								</a4j:commandButton>
								<!--<h:commandButton value="Clear" action="#{programConfigureDataModel.signalConfigureDataModel.clearDisplayProgramSignals}" title="Clear program signals"/>-->
							</td>
						</tr>
	</table>							
</div>
