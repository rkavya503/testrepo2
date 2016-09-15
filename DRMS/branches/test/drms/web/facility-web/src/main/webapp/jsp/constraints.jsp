<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
      "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<html lang="en-US" xml:lang="en-US">
	<f:view>
		<head>
			<jsp:include page="head.jsp" />
		</head>
		<body>
			<h:form id="navForm">
				<jsp:include page="header.jsp" />

				<rich:panel styleClass="content-panel">
            
                    <f:facet name="header">
						<h:outputText
                            value="Constraints for Participant #{header1.welcomeName} in Program #{participantProgram.programName}" />
					</f:facet>
              

					<h:panelGrid columns="2" styleClass="panel-style">
						<h:outputText value="Event Active Window:" styleClass="form-field"/>
						<h:selectOneRadio value="#{constraints.constraint.activeAction}" 
							styleClass="radio-buttons" disabled="#{!header1.editConstraints}">
							<f:selectItems value="#{constraints.availableActiveActions}"/>
						</h:selectOneRadio>
						<h:outputLabel for="minActive" value="Minimum (hh:mm):" styleClass="form-field"/>
						<h:panelGroup>
							<h:inputText id="minActive" value="#{constraints.constraint.minActive}"
								required="true" 
								requiredMessage="Minimum is required"
								label="#{constraints.constraint.minActive}"
								converterMessage="Minimum must be of the form hh:mm (e.g. 12:34)" disabled="#{!header1.editConstraints}">
								<f:convertDateTime pattern="HH:mm" />
							</h:inputText>
							<h:message for="minActive" errorClass="message-error" />
						</h:panelGroup>
						<h:outputLabel for="maxActive" value="Maximum (hh:mm):" styleClass="form-field"/>
						<h:panelGroup>
							<h:inputText id="maxActive" value="#{constraints.constraint.maxActive}"
								required="true" 
								requiredMessage="Maximum is required"
								label="#{constraints.constraint.maxActive}"
								converterMessage="Maximum must be of the form hh:mm (e.g. 12:34)" disabled="#{!header1.editConstraints}">
								<f:convertDateTime pattern="HH:mm" />
							</h:inputText>
							<h:message for="maxActive" errorClass="message-error" />
						</h:panelGroup>
	
						<h:outputText value="Event Notification Window:" styleClass="form-field"/>
						<h:selectOneRadio value="#{constraints.constraint.notifyAction}" 
							styleClass="radio-buttons" disabled="#{!header1.editConstraints}">
							<f:selectItems value="#{constraints.availableNotifyActions}"/>
						</h:selectOneRadio>
						<h:outputLabel for="minNotify" value="Minimum (hh:mm):" styleClass="form-field"/>
						<h:panelGroup>
							<h:inputText id="minNotify" value="#{constraints.constraint.minNotify}"
								required="true" 
								requiredMessage="Minimum is required"
								label="#{constraints.constraint.minNotify}"
								converterMessage="Minimum must be of the form hh:mm (e.g. 12:34)" disabled="#{!header1.editConstraints}">
								<f:convertDateTime pattern="HH:mm" />
							</h:inputText>
							<h:message for="minNotify" errorClass="message-error" />
						</h:panelGroup>
						<h:outputLabel for="maxNotify" value="Maximum (hh:mm):" styleClass="form-field"/>
						<h:panelGroup>
							<h:inputText id="maxNotify" value="#{constraints.constraint.maxNotify}"
								required="true" 
								requiredMessage="Maximum is required"
								label="#{constraints.constraint.maxNotify}"
								converterMessage="Maximum must be of the form hh:mm (e.g. 12:34)" disabled="#{!header1.editConstraints}">
								<f:convertDateTime pattern="HH:mm" />
							</h:inputText>
							<h:message for="maxNotify" errorClass="message-error" />
						</h:panelGroup>
	
						<h:outputText value="Event Duration Window:" styleClass="form-field"/>
						<h:selectOneRadio value="#{constraints.constraint.durationAction}" 
							styleClass="radio-buttons" disabled="#{!header1.editConstraints}">
							<f:selectItems value="#{constraints.availableDurationActions}"/>
						</h:selectOneRadio>
						<h:outputLabel for="minDuration" value="Minimum (hh:mm):" styleClass="form-field" />
						<h:panelGroup>
							<h:inputText id="minDuration" value="#{constraints.constraint.minDuration}"
								required="true" 
								requiredMessage="Minimum is required"
								label="#{constraints.constraint.minNotify}"
								converterMessage="Minimum must be of the form hh:mm (e.g. 12:34)" disabled="#{!header1.editConstraints}">
								<f:convertDateTime pattern="HH:mm" />
							</h:inputText>
							<h:message for="minDuration" errorClass="message-error" />
						</h:panelGroup>
						<h:outputLabel for="maxDuration" value="Maximum (hh:mm):" styleClass="form-field"/>
						<h:panelGroup>
							<h:inputText id="maxDuration" value="#{constraints.constraint.maxDuration}"
								required="true" 
								requiredMessage="Maximum is required"
								label="#{constraints.constraint.maxNotify}"
								converterMessage="Maximum must be of the form hh:mm (e.g. 12:34)" disabled="#{!header1.editConstraints}">
								<f:convertDateTime pattern="HH:mm" />
							</h:inputText>
							<h:message for="maxDuration" errorClass="message-error" />
						</h:panelGroup>
	
						<!-- TODO: put this back in when core supports consecutive days 
						<h:outputText value="Consecutive Days:" styleClass="form-field"/>
						<h:selectOneRadio value="#{constraints.constraint.consecutiveAction}" 
							styleClass="radio-buttons">
							<f:selectItems value="#{constraints.availableConsecutiveActions}"/>
						</h:selectOneRadio>
						<h:outputLabel for="maxConsecutiveD" value="Maximum:" style="font-weight:bold"/>
						<h:panelGroup>
							<h:inputText id="maxConsecutiveD"
								value="#{constraints.constraint.maxConsecutiveD}" required="true"
								label="#{constraints.constraint.maxConsecutiveD}" />
							<h:message for="maxConsecutiveD" errorClass="message-error" />
						</h:panelGroup> -->
					</h:panelGrid>
	
					<rich:spacer height="10px" />

					<rich:panel>
						<f:facet name="header">
							<h:outputText value="Opt Out Dates" />
						</f:facet>

						<h:panelGrid columns="2" styleClass="panel-style">
							<a4j:outputPanel id="selectedDate" layout="block">
		                    	<rich:calendar  
		                    		value="#{constraints.selectedDate}"
			                        popup="false"
			                        cellWidth="24px" 
			                        cellHeight="22px" 
			                        datePattern="#{applicationScope.dateFormat}"
			                        style="width:200px">
			                        <a4j:support event="onchanged"
											ajaxSingle="true"/>
								</rich:calendar>
		           			</a4j:outputPanel>

							<rich:panel style="height:200px;">
								<rich:dataTable id="jsfInvalidDates" rows="5"
									value="#{constraints.jsfInvalidDates}" var="date" 
									reRender="ds" width="200px" style="margin:0px">
									<rich:column width="5%">
										<h:selectBooleanCheckbox value="#{date.delete}">
											<a4j:support event="onclick"
											ajaxSingle="true" disabled="#{!header1.editConstraints}"/>
										</h:selectBooleanCheckbox>
									</rich:column>
									<rich:column width="95%">
										<f:facet name="header">
											<h:outputText value="Date" />
										</f:facet>
										<h:outputText value="#{date.invalidDate.invalidDate}">								
											<f:convertDateTime pattern="#{applicationScope.shortDateTimeFormat}"/>
										</h:outputText>
									</rich:column>
						            <f:facet name="footer">
						                <rich:datascroller id="ds"></rich:datascroller>
						            </f:facet>
								</rich:dataTable>		
							</rich:panel>

							<h:panelGroup>							
								
								<table border="0">
									<tr>
										<td>
											<h4>
												<h:commandButton value="Add Date" title="Add Date" action="#{constraints.addDateAction}" immediate="true" disabled="#{!header1.editConstraints}"/>
											</h4>
										</td>
									</tr>
								</table>		
								<h:outputText value="#{constraints.addDateError}" 
									styleClass="message-error"/>
							</h:panelGroup>							

							<h:panelGroup>	
								<table border="0">
									<tr>
										<td>
											<h4>
												<h:commandButton value="Delete Date" title="Delete Date" action="#{constraints.deleteDateAction}" immediate="true" disabled="#{!header1.editConstraints}"/>
											</h4>
										</td>
									</tr>
								</table>		
							</h:panelGroup>
						</h:panelGrid>
					</rich:panel>
					
					<rich:spacer height="10px" />

					<div id="fm-submit">
						
							<table border="0">
								<tr>
									<td>
										<h3>
											<h:commandButton value="Update Constraints" title="Update Constraints" action="#{participantProgram.updateConstraintsAction}" disabled="#{!header1.editConstraints}" style="opacity : #{header1.editConstraints ? 1 : 0.65}"/>
										</h3>
									</td>
									<td>
										<h3>
											<h:commandButton value="Cancel Constraints" title="Cancel Constraints" action="#{participantProgram.cancelConstraintsAction}" immediate="true"/>
										</h3>
									</td>
								</tr>
							</table>	
					</div>
	
				</rich:panel>
			</h:form>

			<jsp:include page="footer.jsp" />
		</body>
	</f:view>
</html>
