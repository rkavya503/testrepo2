<!doctype html public "-//w3c//dtd html 4.0 transitional//en">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>

				<h:panelGroup styleClass="program_property_content" layout="block" >
					<table id="programBasicAttributesTable" width="400px">
                       <%--
                          <tr>
							<td>
                            <h:commandButton value="     Stop Timer    " title="Stop Timer" action="#{programConfigureDataModel.stopTimerAction}" style="border: 1px solid #2BB0FF;" />
                        	</td>
						</tr>
                        <tr>
							<td>
                            <h:outputLabel for="Weather Info"  value="#{programConfigureDataModel.weatherTimerInfo}" styleClass="program_edit_label"/>
							</td>
						</tr>      
                      --%>
                        <tr>
							<td>
								<h:outputLabel for="inputText_repeatTimeOfDayTxt"  value="Repeat Event Time" styleClass="program_edit_label"/>
							</td>
							<td>
                            <h:inputText id="inputText_repeatTimeOfDayTxt" styleClass="inputText_program_property" required="false"  value="#{programConfigureDataModel.autoRepeatTimeOfDayStr}" readonly="#{!ProgramDataModel.programPropsEnabled}"/>
							</td>
						</tr>

						<tr>
							<td>
								<h:outputLabel for="selectOneMenu_programTemplate"  value="Program Template" styleClass="program_edit_label"/>
							</td>
							<td>
								<h:selectOneMenu id="selectOneMenu_programTemplate"
									disabled="#{!programConfigureDataModel.programTemplateEditableFlag or !ProgramDataModel.programPropsEnabled}" value="#{programConfigureDataModel.selectedProgramTemplate}"
									onkeydown="if((event || window.event).keyCode==13) {this.onchange();return false}"
									valueChangeListener="#{programConfigureDataModel.programTemplateChange}">                     
									<f:selectItems value="#{programConfigureDataModel.programTemplateList}"/> 
									<a4j:support event="onchange" reRender="inputText_uiScheduleEventString" ajaxSingle="true" limitToList="true"/> 
								</h:selectOneMenu>
							</td>
						</tr>						
						
						<tr>
							<td>
								<h:outputLabel for="inputText_program_programName"  value="*Program Name" styleClass="program_edit_label"/>
							</td>
							<td>
								<h:inputText id ="inputText_program_programName" readonly="#{!programConfigureDataModel.programNameEditableFlag or !ProgramDataModel.programPropsEnabled}" required="true"  requiredMessage=" Program name is required " styleClass="inputText_program_property" value="#{programConfigureDataModel.program.programName}" />
							</td>
						</tr>
						
						<tr>
							<td>
								<h:outputLabel for="inputText_utilityName"  value="Utility Name" styleClass="program_edit_label"/>
							</td>
							<td>
								<h:inputText id="inputText_utilityName" styleClass="inputText_program_property" value="#{programConfigureDataModel.program.utilityProgramName}" readonly="#{!ProgramDataModel.programPropsEnabled}"/>
							</td>
						</tr>

						<tr>
							<td>
								<h:outputLabel for="inputText_utilityName"  value="Secondary Utility Name" styleClass="program_edit_label"/>
							</td>
							<td>
								<h:inputText id="inputText_secondaryUtilityName" styleClass="inputText_program_property" value="#{programConfigureDataModel.program.secondaryUtilityName}" readonly="#{!ProgramDataModel.programPropsEnabled}"/>
							</td>
						</tr>

						
						<tr>
							<td>
								<h:outputLabel for="selectBooleanCheckbox_manullyCreatable"  value="Manually Creatable" styleClass="program_edit_label"/>
							</td>
							<td>
								<h:selectBooleanCheckbox  id="selectBooleanCheckbox_manullyCreatable" value="#{programConfigureDataModel.program.manualCreatable}" disabled="#{!ProgramDataModel.programPropsEnabled}"/>
							</td>
						</tr>
						
						<tr>
							<td>
								<h:outputLabel for="priority_inputText"  value="*Priority" styleClass="program_edit_label"/>
							</td>
							<td>
								<h:inputText id="priority_inputText" required="true" requiredMessage=" priority is required " styleClass="inputText_program_property" value="#{programConfigureDataModel.program.priority}" readonly="#{!ProgramDataModel.programPropsEnabled}"/>
							</td>
							<td>
								
							</td>
							
						</tr>
						
						<tr>
							<td>
								<h:outputLabel for="inputText_minIssueToStart"  value="*Min Issue to Start(min)" styleClass="program_edit_label"/>
							</td>
							<td>
								<h:inputText id="inputText_minIssueToStart" styleClass="inputText_program_property" required="true" requiredMessage=" Min issue to start is required " value="#{programConfigureDataModel.program.minIssueToStartM}" readonly="#{!ProgramDataModel.programPropsEnabled}" />
							</td>
						</tr>
						
						<tr>
							<td>
								<h:outputLabel for="inputText_maxIssueTime"  value="*Max Issue Time" styleClass="program_edit_label"/>
							</td>
							<td>
								
								<h:inputText id="inputText_maxIssueTime" styleClass="inputText_program_property" required="true"  requiredMessage=" Max issue time is required " value="#{programConfigureDataModel.maxIssueTimeString}" readonly="#{!ProgramDataModel.programPropsEnabled}" />
							</td>
						</tr>
						
						<tr>
							<td>
								<h:outputLabel for="selectBooleanCheckbox_BDBE"  value="Must Issue BDBE" styleClass="program_edit_label"/>
							</td>
							<td>
								<h:selectBooleanCheckbox id="selectBooleanCheckbox_BDBE" value="#{programConfigureDataModel.program.mustIssueBDBE}"/>
							</td>
						</tr>
						<tr>
							<td>
								<h:outputLabel for="inputText_defaultDuration"  value="Default Duration" styleClass="program_edit_label" rendered="#{programConfigureDataModel.setDefaultDuration}"/>
							</td>
							<td>
								<h:inputText id="inputText_defaultDuration" styleClass="inputText_program_property" value="#{programConfigureDataModel.program.defaultDuration}" rendered="#{programConfigureDataModel.setDefaultDuration}"/>
							</td>
						</tr>
						
					</table>			
				</h:panelGroup>
				
				<h:panelGroup styleClass="program_property_content" layout="block" >
					<table id="programBasicAttributesTable" width="400px">
						<tr>
							<td>
								<h:outputLabel for="inputText_minStartTime"  value="*Min Start Time" styleClass="program_edit_label"/>
							</td>
							<td>
								
								<h:inputText id="inputText_minStartTime" styleClass="inputText_program_property" required="true"  requiredMessage=" Min start time is required " value="#{programConfigureDataModel.minStartTimeString}" validator="#{programConfigureDataModel.validateMinStartTime}" readonly="#{!ProgramDataModel.programPropsEnabled}"/>
							</td>
						</tr>
						
						<tr>
							<td>
								<h:outputLabel for="inputText_minEndTime"  value="*Min End Time" styleClass="program_edit_label"/>
							</td>
							<td>
								
								<h:inputText id="inputText_minEndTime" styleClass="inputText_program_property" required="true"  requiredMessage=" Min end time is required " value="#{programConfigureDataModel.minEndTimeString}" validator="#{programConfigureDataModel.validateMinEndTime}" readonly="#{!ProgramDataModel.programPropsEnabled}"/>
							</td>
						</tr>
						
						<tr>
							<td>
								<h:outputLabel for="inputText_maxStartTime"  value="*Max Start Time" styleClass="program_edit_label"/>
							</td>
							<td>
								
								<h:inputText id="inputText_maxStartTime" styleClass="inputText_program_property" required="true"  requiredMessage=" Max start time is required " value="#{programConfigureDataModel.maxStartTimeString}" validator="#{programConfigureDataModel.validateMaxStartTime}" readonly="#{!ProgramDataModel.programPropsEnabled}"/>
							</td>
						</tr>
						
						<tr>
							<td>
								<h:outputLabel for="inputText_maxEndTime"  value="*Max End Time" styleClass="program_edit_label"/>
							</td>
							<td>
								
								<h:inputText id="inputText_maxEndTime" styleClass="inputText_program_property" required="true"  requiredMessage=" Max end time is required " value="#{programConfigureDataModel.maxEndTimeString}" validator="#{programConfigureDataModel.validateMaxndTime}" readonly="#{!ProgramDataModel.programPropsEnabled}"/>
							</td>
						</tr>
						
						<tr>
							<td>
								<h:outputLabel for="inputText_minDurationMinutes"  value="*Min Duration Minutes" styleClass="program_edit_label"/>
							</td>
							<td>
								<h:inputText id="inputText_minDurationMinutes" styleClass="inputText_program_property" required="true"  requiredMessage=" Min duration minutes is required " value="#{programConfigureDataModel.program.minDurationM}" readonly="#{!ProgramDataModel.programPropsEnabled}">
									<f:validateDoubleRange minimum="1"/>
								</h:inputText>
							</td>
						</tr>
						
						<tr>
							<td>
								<h:outputLabel for="inputText_maxDurationMinutes"  value="*Max Duration Minutes" styleClass="program_edit_label"/>
							</td>
							<td>
								<h:inputText id="inputText_maxDurationMinutes" styleClass="inputText_program_property" required="true"  requiredMessage=" Max duration minutes is required " value="#{programConfigureDataModel.program.maxDurationM}" readonly="#{!ProgramDataModel.programPropsEnabled}"/>
							</td>
						</tr>
						
						<tr>
							<td>
								<h:outputLabel for="inputText_DBE"  value="*Pending Time DBE" styleClass="program_edit_label"/>
							</td>
							<td>
								
								<h:inputText id="inputText_DBE" styleClass="inputText_program_property" required="true"  requiredMessage=" Pending time DBE is required " value="#{programConfigureDataModel.pendingTimeDBEString}" readonly="#{!ProgramDataModel.programPropsEnabled}" />
							</td>
						</tr>
						
						<tr>
							<td>
								<h:outputLabel for="inputText_notificationParameter"  value="Notification Parameter" styleClass="program_edit_label"/>
							</td>
							<td>
								<h:inputText id="inputText_notificationParameter" styleClass="inputText_program_property" value="#{programConfigureDataModel.program.notificationParam1}" readonly="#{!ProgramDataModel.programPropsEnabled}" />
							</td>
						</tr>
						
						<tr>
							<td>
								<h:outputLabel for="inputText_uiScheduleEventString"  value="*Event Schedule Page" styleClass="program_edit_label"/>
							</td>
							<td>
								<h:inputText id="inputText_uiScheduleEventString" required="true"  requiredMessage=" UI schedule event string is required "  readonly="true" styleClass="inputText_program_property" value="#{programConfigureDataModel.currentProgramSechdulePage}" />
							</td>
						</tr>
					</table>		
				</h:panelGroup>
				
				<div style="clear: both"></div>
                    
                    <div class="programEditSeasonButtonStyle">
                    <a4j:commandButton 
                     title="Save program configuration" action="#{programConfigureDataModel.saveMainProgram}"  disabled="#{!ProgramDataModel.programPropsEnabled}" value="Save" immediate="false"/>
                     
                     <a4j:commandButton
                                       value="Cancel" action="#{programConfigureDataModel.dispatchToProgramPage}" immediate="true" title="Cancel Submit"/>
                    </div>  

