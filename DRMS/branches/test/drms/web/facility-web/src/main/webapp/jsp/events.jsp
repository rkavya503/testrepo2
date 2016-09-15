<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
      "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j" %>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich" %>

<html lang="en-US" xml:lang="en-US">
<f:view>
    <head>
        <jsp:include page="head.jsp"/>
        <script type="text/javascript">
        	function handleEvent(){
        		document.getElementById("dialogTitle").focus();
        	}
        </script>
    </head>
    <body>
    <div id="frame">
        <h:form id="navForm">
            <jsp:include page="header.jsp"/>
            <rich:panel id="eventPanel" styleClass="content-panel">
                <a4j:region>
                    <a4j:keepAlive beanName="events"/>
					
                    <rich:dataTable id="events" rows="20" value="#{events.events}"
                                    var="eventItem" reRender="ds" styleClass="content-table">
                        <rich:column width="14%" sortBy="#{eventItem.name}">

                            <f:facet name="header">
                                <h:outputText value="Name" title="Name"/>
                            </f:facet>
                            <h3><h:outputText value="#{eventItem.name}"/></h3>
                        </rich:column>
                        <rich:column width="14%" sortBy="#{eventItem.programName}">
                            <f:facet name="header">
                                <h:outputText value="Program" title="Program"/>
                            </f:facet>
                            <h:outputText value="#{eventItem.programName}"/>
                        </rich:column>
                        <rich:column width="12%" sortBy="#{eventItem.clientsString}">
                            <f:facet name="header">
                                <h:outputText value="Clients" title="Clients"/>
                            </f:facet>
                            <h:outputText value="#{eventItem.clientsString}"/>
                        </rich:column>
                        <rich:column width="14%" sortBy="#{eventItem.remainingTime}" id="rtime">
                            <f:facet name="header">
                                <h:outputText value="Remaining Time" title="Remaining Time"/>
                            </f:facet>
                            <h:outputText value="#{eventItem.remainingTime}" id="txtTime"/>
                        </rich:column>
                        <rich:column width="10%" sortBy="#{eventItem.start}" sortOrder="ASCENDING">
                            <f:facet name="header">
                                <h:outputText value="Start" title="Start"/>
                            </f:facet>
                            <h:outputText value="#{eventItem.start}">
                                <f:convertDateTime pattern="#{applicationScope.shortDateTimeFormat}"/>
                            </h:outputText>
                        </rich:column>
                        <rich:column width="10%" sortBy="#{eventItem.end}">
                            <f:facet name="header">
                                <h:outputText value="End" title="End"/>
                            </f:facet>
                            <h:outputText value="#{eventItem.end}">
                                <f:convertDateTime pattern="#{applicationScope.shortDateTimeFormat}"/>
                            </h:outputText>
                        </rich:column>

                        <rich:column width="10%" sortBy="#{eventItem.status}">
                            <f:facet name="header">
                                <h:outputText value="Status" title="Status"/>
                            </f:facet>
                            <h:outputText value="#{eventItem.status}"/>
                        </rich:column>

                        <rich:column width="30%">
                            <f:facet name="header">
                                <h:outputText value="Actions" title="Actions"/>
                            </f:facet>
							<table border="0">
								<tr>
									<td>
										<h:panelGroup layout="block"  rendered="true" >
										<h3>
											<a4j:commandButton oncomplete="#{rich:component('panel')}.show();handleEvent();"
															   reRender="panel" value="Opt-out"
															   rendered="true"
															   disabled="#{!events.canOptOutOfEvent or !events.eventOptOutEnabled}">
												<f:setPropertyActionListener
														value="#{eventItem.name}"
														target="#{events.eventName}"
														/>
											</a4j:commandButton>
										</h3>
										</h:panelGroup>
									</td>
										<td>
											<h:panelGroup layout="block"  rendered="false" >
											<h3>
												<h:commandButton value="Opt-in" action="#{events.optInAction}"
																 actionListener="#{events.editBidsListener}"
																 rendered="false">
													<f:attribute name="eventName" value="#{eventItem.name}"/>
												</h:commandButton>
											</h3>
											</h:panelGroup>
										</td>	
									</tr>
									<tr>
										<td>
											<h:panelGroup layout="block"  rendered="#{eventItem.bid}" >
											<h3>
												<h:commandButton value="Bids" action="#{events.editBidsAction}"
																 actionListener="#{events.editBidsListener}"
																 rendered="#{eventItem.bid}">
													<f:attribute name="eventName" value="#{eventItem.name}"/>
												</h:commandButton>
											</h3>
											</h:panelGroup>
									   </td>
										<td>
											<h:panelGroup styleClass="noWrapContent"  layout="block"  id="baselineAjustment" rendered="true" >
											<h3>
												<h:selectBooleanCheckbox  disabled="#{eventItem.disableDayAdjustment}" value="#{eventItem.dayOfAdjustment}" onclick="#{rich:component('panelDayOfAdjustment')}.hide();"
	                                                    id="DayOfAdjustment">
	                                                    <a4j:support
	                                                            event="onclick"
	                                                            oncomplete="#{rich:component('panelDayOfAdjustment')}.show()"
	                                                            ajaxSingle="true"
	                                                            >
																<f:setPropertyActionListener
																	value="#{eventItem.name}"
																	target="#{events.eventName}"
																/>
	                                                             <f:setPropertyActionListener
	                                                                value="#{eventItem.dayOfAdjustment}"
	                                                                target="#{events.dayOfAdjustment}"
	                                                            />
	                                                   </a4j:support>  
									 			</h:selectBooleanCheckbox>
									 			<h:outputText value="Apply Day-of Baseline Adjustment" />
											</h3>
											</h:panelGroup>
										</td>
									</tr>
							</table>
                        </rich:column>
                        <f:facet name="footer">
                            <rich:datascroller id="ds"></rich:datascroller>
                        </f:facet>
                    </rich:dataTable>
                </a4j:region>
            </rich:panel>
        </h:form>

		 <rich:modalPanel id="panelDayOfAdjustment"
		                            autosized="false"
		                            keepVisualState="false"
		                            width="315"
		                            height="150">
		    <h:form id="adjustmentForm">
		            <rich:panel id="messages1"  >
		                <h:outputText value="You are about to change the program Day-of Baseline Adjustment ...  "  />
		             </rich:panel>
						<table border="0">
										<tr>
											<td>
												<h3>
													 <a4j:commandButton  id="yes" value="Yes" reRender="baselineAjustment"
																		action="#{events.updateDayOfAdjustmentControl}"
																		oncomplete="#{rich:component('panelDayOfAdjustment')}.hide();" >
														   <a4j:support
																		action="#{events.updateDayOfAdjustmentControl}"
																		event="onchange"
																		oncomplete="#{rich:component('panelDayOfAdjustment')}.show()"
																		ajaxSingle="true"
																		>
																		 <f:setPropertyActionListener
																			value="#{eventItem.dayOfAdjustment}"
																			target="#{events.dayOfAdjustment}"
																		/>
																		<f:setPropertyActionListener
																			value="#{eventItem.name}"
																			target="#{events.eventName}"
																		/>
														   </a4j:support>
													</a4j:commandButton>
												</h3>
											</td>
											<td>
												<h3>
													<a4j:commandButton  id="no" value="No" reRender="baselineAjustment"
													    action="#{events.setDayOfAdjustmentControl}"
														oncomplete="#{rich:component('panelDayOfAdjustment')}.hide();">
														<a4j:support 
														    action="#{events.setDayOfAdjustmentControl}"
															event="onchange"
															oncomplete="#{rich:component('panelDayOfAdjustment')}.show()"
															ajaxSingle="true">
														</a4j:support>
													</a4j:commandButton>
												</h3>
											</td>
										</tr>
									</table>
		     </h:form>
		</rich:modalPanel>		
        <rich:modalPanel id="panel" autosized="false"
                         keepVisualState="false" width="315" height="150">
            <h:form id="deleteForm">
             	<a id="dialogTitle" href="javascript:void(0);" style="font-weight: bold;">Confirmation dialog</a>
                <rich:panel id="messages">
	                
	                <!-- <input type="text" id="test" value="test"/> -->
	                <a id="confirmBox" href="javascript:void(0);">
	                    <h:outputText value="Are you sure you want to opt-out from event: "/>
	                    <h:outputText value="#{events.eventName} ">
	                    </h:outputText>
	                </a>
	               <!-- </a> -->
	                <h:inputHidden id="pName" value="#{events.eventName}"/>
                </rich:panel>
							<table border="0">
								<tr>
									<td>
										<h3>
											<a4j:commandButton reRender="events"
															   id="yes" value="Yes" action="#{events.optOutAction}"
															   oncomplete="#{rich:component('panel')}.hide();">
												<f:setPropertyActionListener
														value="#{events.eventName}"
														target="#{events.eventName}"/>
											</a4j:commandButton>
										</h3>
									</td>
									<td>
										<h3>
											<a4j:commandButton id="no" value="No" oncomplete="#{rich:component('panel')}.hide();"/>
										</h3>
									</td>
								</tr>
							</table>				   
            </h:form>
        </rich:modalPanel>
		

        <jsp:include page="footer.jsp"/>
    </div>
    </body>
</f:view>
</html>