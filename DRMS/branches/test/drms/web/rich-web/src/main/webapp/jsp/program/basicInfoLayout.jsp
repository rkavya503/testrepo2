<!doctype html public "-//w3c//dtd html 4.0 transitional//en">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<div>

	<table id="programBasicAttributesTable" width="600px">
						
						
						<tr>
							<td>
								<h:outputLabel for=""  value="Program Name" styleClass="program_edit_label"/>
							</td>
							<td>
								<h:inputText readonly="true" styleClass="" value="#{programConfigureDataModel.program.programName}" />
							</td>
						</tr>
						
						<tr>
							<td>
								<h:outputLabel for=""  value="Utility Name" styleClass="program_edit_label"/>
							</td>
							<td>
								<h:inputText  styleClass="" value="#{programConfigureDataModel.program.utilityProgramName}" />
							</td>
						</tr>
						<tr>
							<td>
								<h:outputLabel for=""  value="Long Program Name" styleClass="program_edit_label"/>
							</td>
							<td>
								<h:inputText  styleClass="" value="#{programConfigureDataModel.program.longProgramName}" />
							</td>
						</tr>
						<tr>
							<td>
								<h:outputLabel for=""  value="Program Class" styleClass="program_edit_label"/>
							</td>
							<td>
								<h:inputText  styleClass="" value="#{programConfigureDataModel.program.programClass}" />
							</td>
						</tr>
						<tr>
							<td>
								<h:outputLabel for=""  value="Test Program" styleClass="program_edit_label"/>
							</td>
							<td>
								<h:selectBooleanCheckbox value="#{programConfigureDataModel.program.testProgram}"/>
							</td>
						</tr>
						
						<tr>
							<td>
								<h:outputLabel for=""  value="Repeat Events" styleClass="program_edit_label"/>
							</td>
							<td>
								<h:selectBooleanCheckbox value="#{programConfigureDataModel.program.repeatingEvents}"/>
							</td>
						</tr>
						
						<tr>
							<td>
								<h:outputLabel for=""  value="Manually Creatable" styleClass="program_edit_label"/>
							</td>
							<td>
								<h:selectBooleanCheckbox value="#{programConfigureDataModel.program.manualCreatable}"/>
							</td>
						</tr>
						
						<tr>
							<td>
								<h:outputLabel for=""  value="Priority" styleClass="program_edit_label"/>
							</td>
							<td>
								<h:inputText id="priority_inputText" styleClass="" value="#{programConfigureDataModel.program.priority}" />
							</td>
							<td>
								
							</td>
							
						</tr>
						
						<tr>
							<td>
								<h:outputLabel for=""  value="Operator Emails" styleClass="program_edit_label"/>
							</td>
							<td>
								<h:inputText styleClass="" value="#{programConfigureDataModel.program.operatorEMails}" />
							</td>
						</tr>
						
						<tr>
							<td>
								<h:outputLabel for=""  value="Min Issue to Start(min)" styleClass="program_edit_label"/>
							</td>
							<td>
								<h:inputText styleClass="" value="#{programConfigureDataModel.program.minIssueToStartM}" />
							</td>
						</tr>
						
						<tr>
							<td>
								<h:outputLabel for=""  value="Max Issue Time" styleClass="program_edit_label"/>
							</td>
							<td>
								
								<h:inputText styleClass="" value="#{programConfigureDataModel.maxIssueTimeString}" />
							</td>
						</tr>
						
						<tr>
							<td>
								<h:outputLabel for=""  value="Must Issue BDBE" styleClass="program_edit_label"/>
							</td>
							<td>
								<h:selectBooleanCheckbox value="#{programConfigureDataModel.program.mustIssueBDBE}"/>
							</td>
						</tr>
						
						<tr>
							<td>
								<h:outputLabel for=""  value="Min Start Time" styleClass="program_edit_label"/>
							</td>
							<td>
								
								<h:inputText styleClass="" value="#{programConfigureDataModel.minStartTimeString}" />
							</td>
						</tr>
						
						<tr>
							<td>
								<h:outputLabel for=""  value="Min End Time" styleClass="program_edit_label"/>
							</td>
							<td>
								
								<h:inputText styleClass="" value="#{programConfigureDataModel.minEndTimeString}" />
							</td>
						</tr>
						
						<tr>
							<td>
								<h:outputLabel for=""  value="Max Start Time" styleClass="program_edit_label"/>
							</td>
							<td>
								
								<h:inputText styleClass="" value="#{programConfigureDataModel.maxStartTimeString}" />
							</td>
						</tr>
						
						<tr>
							<td>
								<h:outputLabel for=""  value="Max End Time" styleClass="program_edit_label"/>
							</td>
							<td>
								
								<h:inputText styleClass="" value="#{programConfigureDataModel.maxEndTimeString}" />
							</td>
						</tr>
						
						<tr>
							<td>
								<h:outputLabel for=""  value="Min Duration Minutes" styleClass="program_edit_label"/>
							</td>
							<td>
								<h:inputText styleClass="" value="#{programConfigureDataModel.program.minDurationM}" />
							</td>
						</tr>
						
						<tr>
							<td>
								<h:outputLabel for=""  value="Max Duration Minutes" styleClass="program_edit_label"/>
							</td>
							<td>
								<h:inputText styleClass="" value="#{programConfigureDataModel.program.maxDurationM}" />
							</td>
						</tr>
						
						<tr>
							<td>
								<h:outputLabel for=""  value="Pending Time DBE" styleClass="program_edit_label"/>
							</td>
							<td>
								
								<h:inputText styleClass="" value="#{programConfigureDataModel.pendingTimeDBEString}" />
							</td>
						</tr>
						
						<tr>
							<td>
								<h:outputLabel for=""  value="Start Date" styleClass="program_edit_label"/>
							</td>
							<td>
								<rich:calendar value="#{programConfigureDataModel.startDate}" popup="true">
								</rich:calendar>
							</td>
						</tr>
						
						<tr>
							<td>
								<h:outputLabel for=""  value="End Date" styleClass="program_edit_label"/>
							</td>
							<td>
								<rich:calendar value="#{programConfigureDataModel.endDate}" popup="true">
								</rich:calendar>
							</td>
						</tr>
						
						<tr>
							<td>
								<h:outputLabel for=""  value="Notification Parameter" styleClass="program_edit_label"/>
							</td>
							<td>
								<h:inputText styleClass="" value="#{programConfigureDataModel.program.notificationParam1}" />
							</td>
						</tr>
						
						<tr>
							<td>
								<h:outputLabel for=""  value="Implementation Class" styleClass="program_edit_label"/>
							</td>
							<td>
								<h:inputText readonly="true" styleClass="inputText_programClassName" value="#{programConfigureDataModel.program.className}" />
							</td>
						</tr>
						
						<tr>
							<td>
								<h:outputLabel for=""  value="Validator Class" styleClass="program_edit_label"/>
							</td>
							<td>
								<h:inputText readonly="true" styleClass="inputText_programValidatorClassName" value="#{programConfigureDataModel.program.validatorClass}" />
							</td>
						</tr>
						
						<tr>
							<td>
								<h:outputLabel for=""  value="Event Schedule Page" styleClass="program_edit_label"/>
							</td>
							<td>
								<h:inputText readonly="true" styleClass="inputText_programSchedulePage" value="#{programConfigureDataModel.program.uiScheduleEventString}" />
							</td>
						</tr>
	</table>
</div>
