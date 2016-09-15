<?xml version="1.0" encoding="UTF-8" ?>

<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" xmlns:f="http://java.sun.com/jsf/core" xmlns:a4j="http://richfaces.org/a4j" xmlns:rich="http://richfaces.org/rich" xmlns:h="http://java.sun.com/jsf/html" version="2.1">

	<jsp:directive.page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />

	<head>
		<jsp:include page="/jsp/head.jsp" />
	</head>

	<f:view>
	   <f:phaseListener type="com.akuacom.pss2.richsite.BeforeRenderPhaseListener"/>
	   
		<jsp:include page="/jsp/header_program.jsp" />
		<h:form id="form">
			<a4j:region>
				
				<rich:spacer height="10px" width="700px" />


				<!--  <div class="programInfoPanel">  -->
                <rich:panel>
					<div class="programInfo_header">
                      <div class="programInfoSubTitle">
                        <h:outputLabel value="#{programConfigureDataModel.titleOfTheUI}" />
                      </div>
                    </div>

					<rich:messages layout="table"
                                   globalOnly="false"
                                   infoClass="global-message-info"
                                   warnClass="global-message-warn"
                                   errorClass="global-message-error"
                                   fatalClass="global-message-fatal"/>

                    <rich:tabPanel switchType="server" styleClass="" >
						<rich:tab  label="Main Program Properties"
                                   switchType="server"
                                   action="#{programConfigureDataModel.saveFlagActionEnabled}">
							<jsp:include page="/jsp/program/configure/program_property_layout.jsp" />
						</rich:tab>
						<rich:tab label="Configure Signals"
                                  switchType="server"
                                  action="#{programConfigureDataModel.saveFlagActionEnabled}">
							<jsp:include page="/jsp/program/configure/program_signal_layout.jsp" />
						</rich:tab>
                        <rich:tab label="Configure Price"
                                  rendered="#{programConfigureDataModel.displayFlagRTP}"
                                  switchType="server"
                                  action="#{programConfigureDataModel.saveFlagActionDisabled}"
                                  >
                            <jsp:include page="/jsp/program/configure/rtpLayout.jsp" />
                        </rich:tab>


                        <rich:tab label="Seasons / Holidays"
                                  switchType="server"
                                  action="#{programConfigureDataModel.saveFlagActionDisabled}">
                           <jsp:include page="/jsp/program/seasonLayout.jsp" />
                        </rich:tab>
                        

                        <rich:tab label="Rules" id="rulesTab"
                                  switchType="client"
                                  action="#{programConfigureDataModel.saveFlagActionDisabled}">
                                  <h:form   id="Rules-Form" >
                                  <h:panelGroup styleClass="program_property_content" layout="block" >

                                        <table width="500" border="0"><tr><td align="right">
                                                <a4j:commandLink
                                                        action="#{rules.newRuleAction}"
                                                        immediate="true"
                                                        oncomplete="#{rich:component('editRuleWindow')}.show()"
                                                        value=""
                                                        rendered="#{ProgramDataModel.programRuleEnabled}">
                                                        <h:graphicImage value="/images/layout/add_device.gif" />
                                                  </a4j:commandLink>                                                 
                                                        
                                         </td></tr></table>


                                        <rich:dataTable id="Config-Rules"
                                                        value="#{rules.jsfRules}"
                                                        var="element" width="500"
                                                        styleClass="event-rich-table ">

                                             <rich:column width="60">


                                                 <h:selectBooleanCheckbox value="#{element.delete}"
                                                                          title="Check for Selection"
                                                                          rendered="#{ProgramDataModel.programRuleEnabled}">

                                                         </h:selectBooleanCheckbox>

                                                         </rich:column>
                                                                    <rich:column width="60">
                                                                        <f:facet name="header">
                                                                            <h:outputText value="Start"  title="Start"/>
                                                                        </f:facet>
                                                                        <h:outputText value="#{element.rule.start}">
                                                                            <f:convertDateTime pattern="HH:mm" />
                                                                        </h:outputText>
                                                                    </rich:column>
                                                                    <rich:column width="70">
                                                                        <f:facet name="header">
                                                                            <h:outputText value="End Time"  title="End Time"/>
                                                                        </f:facet>
                                                                        <h:outputText value="#{element.rule.end}">
                                                                            <f:convertDateTime pattern="HH:mm" />
                                                                        </h:outputText>
                                                                    </rich:column>
                                                                    <rich:column width="80">
                                                                        <f:facet name="header">
                                                                            <h:outputText value="Mode"  title="Mode"/>
                                                                        </f:facet>
                                                                        <h:outputText value="#{element.rule.mode}" />
                                                                    </rich:column>
                                                                    <rich:column width="80">
                                                                        <f:facet name="header">
                                                                            <h:outputText value="Variable"  title="Variable"/>
                                                                        </f:facet>
                                                                        <h:outputText value="#{element.rule.variable}"
                                                                            rendered="#{!element.variableValueEditable}"/>
                                                                    </rich:column>
                                                                    <rich:column width="80">
                                                                        <f:facet name="header">
                                                                            <h:outputText value="Operator"  title="Operator"/>
                                                                        </f:facet>
                                                                        <h:outputText value="#{element.operator}" />
                                                                    </rich:column>
                                                                    <rich:column width="80">
                                                                        <f:facet name="header">
                                                                            <h:outputText value="Value"  title="Value" />
                                                                        </f:facet>
                                                                        <h:outputText value="#{element.rule.value}"
                                                                            rendered="#{!element.variableValueEditable}"/>
                                                                    </rich:column>

                                        </rich:dataTable>

                                    
                                          <h:commandButton
                                                        value="Delete "
                                                        title="Delete Rule"
                                                        action="#{rules.deleteRulesAction}"
                                                        disabled="#{!ProgramDataModel.programRuleEnabled}">

                                          </h:commandButton>


                                      <rich:spacer width="8px" />


                                            <h:commandButton
                                                value="Up"
                                                title="Sorting Up"
                                                 action="#{rules.sortRulesUpAction}"
                                                >

                                             </h:commandButton>


                                             <h:commandButton
                                                value="Down"
                                                title="Sorting down"
                                                action="#{rules.sortRulesDownAction}"
                                                >
                                     
                                             </h:commandButton>


                                             <h:commandButton
                                                value="Save"
                                                title="Save"
                                                action="#{rules.saveRulesAction}"
                                                disabled="#{!ProgramDataModel.programRuleEnabled}">

                                             </h:commandButton>



                                    </h:panelGroup>

                          </h:form>
                                    <rich:modalPanel
                                            showWhenRendered="false"
                                            id="editRuleWindow"
                                            autosized="false"
                                            keepVisualState="false"
                                            width="400"
                                            height="500"
                                            >

                                            <h:form   id="editeRulesForm" >

                                                <rich:messages layout="table" globalOnly="false"
                                                   infoClass="global-message-info" warnClass="global-message-warn"
                                                   errorClass="global-message-error" fatalClass="global-message-fatal" />

                                                  <rich:panel styleClass="panel-style">
                                                                <h:panelGrid columns="2" styleClass="panel-style">
                                                                <h:outputLabel for="start" value="Start Time (hh:mm):" style="font-weight:bold"/>
                                                                <h:panelGroup>
                                                                    <h:inputText id="start"
                                                                    value="#{rule.rule.start}"
                                                                    required="true"
                                                                    requiredMessage="Start Time is required"
                                                                    label="#{rule.rule.start}"
                                                                    title="Start Time"
                                                                    converterMessage="Start Time must be of the form hh:mm (e.g. 12:34)">
                                                                    <f:convertDateTime pattern="HH:mm"/>
                                                                    </h:inputText>
                                                                    <h:message for="start" errorClass="message-error" />
                                                                </h:panelGroup>

                                                                <h:outputLabel for="end" value="End Time (hh:mm):" style="font-weight:bold"/>
                                                                <h:panelGroup>
                                                                    <h:inputText id="end"
                                                                    value="#{rule.rule.end}"
                                                                    required="true"
                                                                    requiredMessage="End Time is required"
                                                                    label="#{rule.rule.end}"
                                                                    title="End Time"
                                                                    converterMessage="End Time must be of the form hh:mm (e.g. 12:34)">
                                                                    <f:convertDateTime pattern="HH:mm"/>
                                                                    </h:inputText>
                                                                    <h:message for="end" errorClass="message-error" />
                                                                </h:panelGroup>

                                                              

                                                                <h:outputLabel for="selectOneMenu_mode" value="Mode:" style="font-weight:bold"/>

                                                                <h:selectOneMenu id="selectOneMenu_mode" value="#{rule.rule.mode}"
                                                                title="Mode">
                                                                <f:selectItems value="#{rule.availableModes}"/>
                                                                   <a4j:support event="onchange"
                                                                        reRender="variable,value,end,start"
                                                                        action="#{rule.modeAction}"
                                                                        />
                                                                </h:selectOneMenu>


                                                                <h:outputLabel for="variable" value="Variable:" style="font-weight:bold"/>
                                                                <h:selectOneMenu id="variable" value="#{rule.rule.variable}"
                                                                disabled="#{rule.variableValueEditable}"
                                                                title="Variable">
                                                                <f:selectItems value="#{rule.availableVariables}"/>
                                                                </h:selectOneMenu>

                                                                <h:outputLabel for="name" value="Operator:" style="font-weight:bold"/>
                                                                <h:selectOneMenu id="name" value="#{rule.operator}" title="Operator">
                                                                <f:selectItems value="#{rule.availableOperators}"/>
                                                                    <a4j:support event="onchange"
                                                                        reRender="variable,value,end,start"
                                                                        action="#{rule.operatorAction}"
                                                                        />
                                                                </h:selectOneMenu>


                                                                <h:outputLabel for="value" value="Value:" style="font-weight:bold"/>
                                                                <h:panelGroup>
                                                                <h:inputText id="value" value="#{rule.rule.value}"
                                                                required="true" label="#{rule.rule.value}"
                                                                disabled="#{rule.variableValueEditable}"
                                                                title="Value"/>
                                                                <h:message for="value" errorClass="errors" />
                                                                </h:panelGroup>
                                                                </h:panelGrid>

                                                                <div id="fm-submitr">
                                                                <a4j:commandButton
                                                                    value="Create Rule"
                                                                    title="Create Rule"
                                                                    immediate="true"
                                                                    action="#{rules.createRuleAction}"
                                                                    oncomplete="#{rich:component('editRuleWindow')}.hide();"
                                                                    reRender="Config-Rules"
                                                                    >

                                                                 </a4j:commandButton>


                                                                <rich:spacer width="5px" />
                                                                <a4j:commandButton
                                                                    value="Cancel"
                                                                    title="Cancel"
                                                                    immediate="true"
                                                                    action="#{rules.cancelRuleAction}"
                                                                    oncomplete="#{rich:component('editRuleWindow')}.hide();"
                                                                    >
                                                                 </a4j:commandButton>


                                                                </div>
                                                            </rich:panel>

                                                    </h:form>
                                                </rich:modalPanel>

                       </rich:tab>

					</rich:tabPanel>

				</rich:panel>

			    <jsp:include page="/jsp/program/programConfigureModalPanels.jsp" />
                <jsp:include page="/jsp/footer.jsp" />
			</a4j:region>
		</h:form>
		
	</f:view>

</jsp:root>