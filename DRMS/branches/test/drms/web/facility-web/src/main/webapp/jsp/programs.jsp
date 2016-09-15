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
                
                <script type="text/javascript">
                
 					function disablementFunction(day){
 						
						//enable 6 months from today
						var flag = true;
						var dayDisable = new Date();
						var x = 6; //6 months 						
						dayDisable.setMonth(dayDisable.getMonth() + x);
						var currentDate = new Date();
						if ((currentDate.getTime() - day.date.getTime() >= 0) || day.date>dayDisable)
						{
							
							flag = false;
						}				
						
						// disable when return false
						return flag;
				}
 					
				function disabledClassesProv(day){
					if (disablementFunction(day)) return 'applyStyle';
						
				}
 					
                </script>
                
               
                <style>
					.applyStyle{					
 					background-color:#B0E0E6;
					}
				</style>
                
                
                
                <body>
                        <div id="frame">
                                <h:form id="navForm">
                                        <jsp:include page="header.jsp" />

                                        <rich:panel styleClass="content-panel" id="content-panel">

                                        <rich:messages layout="table" globalOnly="true"
                                        infoClass="global-message-info" warnClass="global-message-warn"
                                        errorClass="global-message-error" fatalClass="global-message-fatal"/>
                                     
                                        <rich:dataTable id="programs" rows="20" 
                                                        value="#{programs.programs}" var="aProgram"
                                                        reRender="ds" styleClass="content-table">
                                                        <rich:column width="25%" sortBy="#{aProgram.consolidationProgramName}">
                                                                <f:facet name="header">
                                                                        <h:outputText value="Name"  title="Name"/>
                                                                </f:facet>
                                                                <h3>
                                                                <h:outputText value="#{aProgram.consolidationProgramName}" />
                                                                </h3>
                                                        </rich:column>
                                                        <rich:column width="25%"  sortBy="#{aProgram.clientsString}">
                                                                <f:facet name="header">
                                                                        <h:outputText value="Clients"  title="Clients"/>
                                                                </f:facet>
                                                                <h:outputText value="#{aProgram.clientsString}" />
                                                        </rich:column>
                                                        <rich:column width="25%"  sortBy="#{aProgram.eventsString}">
                                                                <f:facet name="header">
                                                                        <h:outputText value="Events"  title="Events"/>
                                                                </f:facet>
                                                                <h:outputText value="#{aProgram.eventsString}" />
                                                        </rich:column>
                                          <rich:column width="25%" >

                                                <f:facet name="header">
                                                        <h:outputText value="Actions"  title="Actions"/>
                                                </f:facet>
                                                <div>
                                              
                                                </div>
                                            <div>
                                              
                                                </div>
                                            <h:selectOneRadio
                                                    rendered="#{header1.sceOptOutFlag}"
                                                    onclick="#{rich:component('calender')}.hide();"
                                                    id="optStatus"
                                                    value="#{aProgram.optStatus}"
                                                    styleClass="radio-buttons"
                                                   >
                                                  <a4j:support
                                                            event="onclick"  
                                                                                                                                                                                                                                   
                                                            oncomplete="#{rich:component('calender')}.show()"
                                                            ajaxSingle="true"
                                                            >
                                                            <f:setPropertyActionListener
                                                                value="#{aProgram.programName}"
                                                                target="#{programs.programName}"
                                                            />
                                                             <f:setPropertyActionListener
                                                                value="#{aProgram.optStatus}"
                                                                target="#{programs.optStatus}"
                                                            />                                                            
                                                            
                                                   </a4j:support>
                                                    <f:selectItem itemLabel="Opt-in" itemValue="In" />
                                                    <f:selectItem itemLabel="Opt-out" itemValue="Off" itemDisabled="#{programs.enableOptOut}" />
                                                                                          
                                            </h:selectOneRadio>
                                        
                                        <rich:modalPanel id="calender">  
                                        
                                       <h:outputLabel   value="Opt Out Until: " styleClass="form-field" rendered="#{aProgram.optStatus == 'In'}"/>                                       
                                       <rich:calendar id="optDate" value="#{aProgram.optOutUntil}" label="Opted Out Until:"										
												required="true" requiredMessage="Start Date is required" 
												rendered="#{aProgram.optStatus == 'In'}"
										isDayEnabled="disablementFunction" dayStyleClass="disabledClassesProv" todayControlMode="select">
										<a4j:support event="onchanged" ajaxSingle="true"> 
											<f:setPropertyActionListener
                                                                value="#{aProgram.optOutUntil}"
                                                                target="#{programs.optOutUntil}"
                                              />										
										</a4j:support>										
										</rich:calendar>
										
										
            <rich:panel id="messages"  >            
                <h:outputText value="You are about to change the program opt-status ...  "  />
             </rich:panel>
				<table border="0">
								<tr>
									<td>
										<h3>
											 <a4j:commandButton  id="yes" value="Yes" reRender="programs"
																action="#{programs.updateOptControl}"
																oncomplete="#{rich:component('calender')}.hide();">
												   <a4j:support
																action="#{programs.updateOptControl}"
																event="onchange"
																oncomplete="#{rich:component('calender')}.show()"
																ajaxSingle="true"
																>
																<f:setPropertyActionListener
																	value="#{aProgram.programName}"
																	target="#{programs.programName}"
																/>
																 <f:setPropertyActionListener
																	value="#{aProgram.optStatus}"
																	target="#{programs.optStatus}"
																/>
																<f:setPropertyActionListener
																	value="#{aProgram.optOutUntil}"
																	target="#{programs.optOutUntil}"
																/>
															
												   </a4j:support>			  
											</a4j:commandButton>
										</h3>
									</td>
									<td>
										<h3>
											<a4j:commandButton reRender="programs" id="no" value="No" oncomplete="#{rich:component('calender')}.hide();"/>
										</h3>
									</td>
								</tr>
							</table>
										</rich:modalPanel> 
										
										<h:outputLabel   value="Opted Out Until: " styleClass="form-field" rendered="#{aProgram.optedOutFlag == true}"/>
										<h:outputLabel   value="#{aProgram.optOutUntil}" styleClass="form-field" rendered="#{aProgram.optedOutFlag == true}">	
										<f:convertDateTime type="date" pattern="MMM-dd-yyyy"/>
										</h:outputLabel>

							<table border="0">
								<tr>
									<td>
										<h:panelGroup  layout="block"  rendered="#{aProgram.customRulesEnabled}" >
										<h3>
											<h:commandButton value="Constraints"
												action="#{programs.editConstraintsAction}"
												actionListener="#{programs.editConstraintsListener}"
												rendered="#{aProgram.customRulesEnabled}">
												<f:attribute name="programName" value="#{aProgram.programName}" />
											</h:commandButton>
										</h3>
										</h:panelGroup>
									</td>
									<td>
										<h:panelGroup  layout="block"  rendered="#{aProgram.bid}" >
										<h3>
											<h:commandButton value="Bids"
                                                            action="#{programs.editBidsAction}"
                                                            actionListener="#{programs.editBidsListener}"
                                                            rendered="#{aProgram.bid}">
                                                            <f:attribute name="programName" value="#{aProgram.programName}"/>
                                            </h:commandButton>
										</h3>
										</h:panelGroup>
									</td>
									 <td>
										<h:panelGroup  layout="block"  rendered="#{aProgram.bid}" >
										<h3>
											<h:selectBooleanCheckbox  value="#{aProgram.dayOfAdjustment}" 
												  onclick="#{rich:component('panelDayOfAdjustment')}.hide();"
                                                    id="DayOfAdjustment">
                                                    <a4j:support
                                                            event="onclick"
                                                            oncomplete="#{rich:component('panelDayOfAdjustment')}.show()"
                                                            ajaxSingle="true"
                                                            >
                                                            <f:setPropertyActionListener
                                                                value="#{aProgram.programName}"
                                                                target="#{programs.programName}"
                                                            />
                                                             <f:setPropertyActionListener
                                                                value="#{aProgram.dayOfAdjustment}"
                                                                target="#{programs.dayOfAdjustment}"
                                                            />
                                                   </a4j:support>  
								 			</h:selectBooleanCheckbox>
								 			<h:outputText value="Apply Day-of Baseline Adjustment" />
								 			
                                           
										</h3>
										</h:panelGroup>
									</td>
								</tr>
							</table>
							
                            <br/><rich:spacer height="10px">
                            	
                            </rich:spacer>
							<a4j:commandLink value="Demand Limiting Settings" immediate="true"
								action="#{programs.demandLimitingParticipantSettingsAction}"
								rendered="#{aProgram.demandLimitingSettingsActive}" >
								<f:attribute name="programName" value="#{aProgram.programName}" />
								<f:attribute name="participantName"
									value="#{aProgram.participantName}" />
							</a4j:commandLink>
                                            <rich:spacer width="20px" />
                                                    
							<a4j:commandLink value="Demand Limiting Dashboard" immediate="true"
								action="#{programs.demandLimitingDashboardAction}"
								rendered="#{aProgram.demandLimitingSettingsActive}" target="_blank">
								<f:attribute name="programName" value="#{aProgram.programName}" />
								<f:attribute name="participantName"
									value="#{aProgram.participantName}" />
							</a4j:commandLink>
                                           <rich:spacer width="20px" />
							<a4j:commandLink value="Mock Meter" immediate="true"
								action="#{programs.demandLimitingMockMeterAction}"
								rendered="#{aProgram.demandLimitingMockMeterActive}">
								<f:attribute name="programName" value="#{aProgram.programName}" />
								<f:attribute name="participantName"
									value="#{aProgram.participantName}" />
							</a4j:commandLink>
						</rich:column>

                        <f:facet name="footer">
                            <rich:datascroller id="ds"></rich:datascroller>
                        </f:facet>
                    </rich:dataTable>

                    </rich:panel>
            </h:form>

 <rich:modalPanel id="panelDayOfAdjustment"
                            autosized="false"
                            keepVisualState="false"
                            width="315"
                            height="150">
    <h:form id="adjustmentForm">
            <rich:panel id="messages"  >
                <h:outputText value="You are about to change the program Day-of Baseline Adjustment ...  "  />
             </rich:panel>
				<table border="0">
								<tr>
									<td>
										<h3>
											 <a4j:commandButton  id="yes" value="Yes" reRender="programs"
																action="#{programs.updateDayOfAdjustmentControl}"
																oncomplete="#{rich:component('panelDayOfAdjustment')}.hide();">
												   <a4j:support
																action="#{programs.updateDayOfAdjustmentControl}"
																event="onchange"
																oncomplete="#{rich:component('panelDayOfAdjustment')}.show()"
																ajaxSingle="true"
																>
																<f:setPropertyActionListener
																	value="#{aProgram.programName}"
																	target="#{programs.programName}"
																/>
																 <f:setPropertyActionListener
																	value="#{aProgram.dayOfAdjustment}"
																	target="#{programs.dayOfAdjustment}"
																/>
												
												   </a4j:support>
											</a4j:commandButton>
										</h3>
									</td>
									<td>
										<h3>
											<a4j:commandButton reRender="programs" id="no" value="No" oncomplete="#{rich:component('panelDayOfAdjustment')}.hide();"/>
										</h3>
									</td>
								</tr>
							</table>
     </h:form>
</rich:modalPanel>


                        <jsp:include page="footer.jsp" />
                        </div>
                </body>
        </f:view>
</html>