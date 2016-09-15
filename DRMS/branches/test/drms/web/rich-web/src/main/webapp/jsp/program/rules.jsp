<!doctype html public "-//w3c//dtd html 4.0 transitional//en">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<html lang="en-US" xml:lang="en-US">
	<f:view>
		<head>
				<jsp:include page="/jsp/head.jsp" />
		</head>
		<body>
			<div id="frame">
				<h:form id="navForm">
					<rich:panel styleClass="content-panel">

                    <rich:dataTable width="100%">
                        <f:facet name="header4">
                            <f:facet name="header">
                                <rich:columnGroup>
                                    <rich:column style="text-align:left;font-size: 15px;">
                                        <h:outputText value="" /></rich:column>

                                    <rich:column colspan="1" width="200px">

                                        <a4j:commandLink
                                            action="#{rules.newRuleAction}"
                                            immediate="true"
                                            oncomplete="#{rich:component('editRuleModal2')}.show()"
                                            value=""
                                            >
                                            <h:graphicImage value="/images/layout/add_device.gif" />

                                        </a4j:commandLink>


                              <a4j:commandButton
									value="Delete"
									title="Delete"
									immediate="true"
									action="#{rules.deleteRulesAction}"
                                    >
                                   
                                 </a4j:commandButton>

                                    </rich:column>
                                </rich:columnGroup>

                            </f:facet>
                        </f:facet>
                    </rich:dataTable>
             
                           <rich:extendedDataTable
                                             height="300px" width="100%"
                                             id="ConfigRules"
                                             value="#{rules.jsfRules}"
                                             var="element"  >

                                 <rich:column width="60">
									<h:selectBooleanCheckbox value="#{element.delete}" title="Check for Selection"/>
									<a4j:support event="onchange"  action="#{rules.deleteRulesAction}"  reRender=""  />
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

                         </rich:extendedDataTable>
               

							<rich:spacer height="10px" />
							<div id="fm-submit">
								 <%--<h:commandButton value="New Rule" title="New Rule"
                                    /> --%>
								<rich:spacer width="5px" />

							<%--<h:commandButton value="Save Rules" title="Save Rules"
									action="#{rules.saveRulesAction}" />

                            <a4j:commandButton
                                value="Save"
                                title="Save"
                                immediate="true"
                                action="#{rules.saveRulesAction}"
                              
                            />--%>

								<rich:spacer width="5px" />
								 <%--<h:commandButton value="Cancel" title="Cancel"
									action="#{rules.cancelRulesAction}" />
                               <h:commandButton value="Delete Rule" title="Delete Rule"
									action="#{rules.deleteRulesAction}" />--%>

								 <a4j:commandButton
									value="Delete"
									title="Delete"
									immediate="true"
									action="#{rules.deleteRulesAction}"				  
                                    >
                                        <a4j:support  event="onclick"
                                                      action="#{rules.deleteRulesAction}"
                                                      reRender="ConfigRules"
                                        />
                                 </a4j:commandButton>

                                 <h:commandButton  value="delete 2"
                                                   title="delete"
                                                   action="#{rules.deleteRulesAction}"
                                                 >

                                 </h:commandButton>
                                 <a4j:commandButton
									value="Delete"
									title="Delete"
									immediate="false"
									action="#{rules.deleteRulesAction}"
								>
                                <a4j:support  event="onclick" action="#{rules.deleteRulesAction}"
                               reRender=",ConfigRules"
                               />
                                 </a4j:commandButton>

								<rich:spacer width="5px" />

                                <a4j:commandButton  value="Up" title="up"
                                                    action="#{rules.sortRulesUpAction}"
												    immediate="true"
                                                    reRender="ConfigRules"
                                                    >
                                </a4j:commandButton>

                                <a4j:commandButton value="Down" title="up"
                                                   action="#{rules.sortRulesDownAction}"
											 	   immediate="true"
													>
                                 <a4j:support event="onclick" action="#{rules.sortRulesDownAction}"
                                                reRender="ConfigRules"
                                    />
                                </a4j:commandButton>
                              
							</div>
                      
					</rich:panel>
				</h:form>


   <rich:modalPanel
        showWhenRendered="false"
        id="editRuleModal2"
        autosized="false"
        keepVisualState="false"
        width="600"
        height="500"
        >

        <h:form   id="editeRuleForm2" >
            <center><br><br><br>
                    <h:outputLabel for="selectOneMenu_mode" value="Mode:" style="font-weight:bold"/>

                    <h:selectOneMenu id="selectOneMenu_mode2" value="#{rule.rule.mode}"
                    title="Mode">
                    <f:selectItems value="#{rule.availableModes}"/>
                    </h:selectOneMenu>

                <a4j:commandButton
                    value="Create Rule"
                    title="Create Rule"
                    immediate="true"
                    action="#{rules.createRuleAction}"
                    oncomplete="#{rich:component('editRuleModal2')}.hide();"
                    >
                     <a4j:support  event="oncomplete" action="#{rules.createRuleAction}"
                       reRender="parentTable,ConfigRules"
                   />
                 </a4j:commandButton>
                             <a4j:commandButton
                    value="Create Rule"
                    title="no ajax"
                    immediate="true"
                    action="#{rules.createRuleAction}"
                    oncomplete="#{rich:component('editRuleModal2')}.hide();"
                    reRender="parentTable,ConfigRules"
                    >

                 </a4j:commandButton>
                 <a4j:commandButton
                    value="Create Rule"
                    title="onChange"
                    immediate="true"
                    action="#{rules.createRuleAction}"
                    oncomplete="#{rich:component('editRuleModal2')}.hide();"
                    >
                     <a4j:support  event="onchange" action="#{rules.createRuleAction}"
                       reRender="parentTable,ConfigRules"
                   />
                 </a4j:commandButton>
                           <a4j:commandButton
                    value="Create Rule"
                    title="immediate"
                    immediate="false"
                    action="#{rules.createRuleAction}"
                    oncomplete="#{rich:component('editRuleModal2')}.hide();"
                    >
                     <a4j:support  event="onclick" action="#{rules.createRuleAction}"
                       reRender="parentTable,ConfigRules"
                   />
                 </a4j:commandButton>




            </center>
        </h:form>
    </rich:modalPanel>


<rich:modalPanel
        showWhenRendered="false"
        id="editRuleModal"
        autosized="false"
        keepVisualState="false"
        width="600"
        height="500"
        >

        <h:form   id="editeRuleForm" >
            <center><br><br><br>

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
                            </h:selectOneMenu>
                            <%--
                            <h:outputLabel for="variable" value="Variable:" style="font-weight:bold"/>
                            <h:selectOneMenu id="variable" value="#{rule.rule.variable}"
                            disabled="#{rule.variableValueEditable}"
                            title="Variable">
                            <f:selectItems value="#{rule.availableVariables}"/>
                            </h:selectOneMenu>
                            --%>
                            <h:outputLabel for="name" value="Operator:" style="font-weight:bold"/>
                            <h:selectOneMenu id="name" value="#{rule.operator}" title="Operator">
                            <f:selectItems value="#{rule.availableOperators}"/>
                            <a4j:support event="onchange"
                            reRender="variable, value"
                            action="#{rule.operatorAction}"/>
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

                            <div id="fm-submit">
                            <a4j:commandButton
                                value="Create Rule"
                                title="Create Rule"
                                immediate="true"
                                action="#{rules.createRuleAction}"
                                oncomplete="#{rich:component('editRuleModal')}.hide();"
                            >
                             <a4j:support  event="oncomplete" action="#{rules.createRuleAction}"
                               reRender="parentTable,ConfigRules"
                               />
                             </a4j:commandButton>


                            <%-- another way of submitting the form --%>



                            <rich:spacer width="5px" />
                            <a4j:commandButton
                                value="Cancel"
                                title="Cancel"
                                immediate="true"
                                action="#{rules.cancelRuleAction}"
                                oncomplete="#{rich:component('editRuleModal')}.hide();"
                                >
                                </a4j:commandButton>
                          
                            
                            </div>
						</rich:panel>

                    </center>
                </h:form>
            </rich:modalPanel>


        </div>
    </body>

	</f:view>
</html>
