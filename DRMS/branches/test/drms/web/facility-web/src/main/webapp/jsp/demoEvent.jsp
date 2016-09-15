<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
      "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://akuacom.com/richext" prefix="richext"%>
<html lang="en-US" xml:lang="en-US">
	<f:view>
		<a4j:keepAlive beanName="demoEvent" />
		<head>
			<jsp:include page="head.jsp" />
             <script type="text/javascript">
                function resetNotification(){
                    document.forms[1].elements[2].value =  new Date().getHours()+":"+new Date().getMinutes();
                    //document.forms[0].elements[2].disabled = true;
                    //document.forms[1].elements[4].disabled = true;
                }
             </script>
		 </head>			
		 <body id="DemoEventsPane">
			<div id="frame">
				<h:form id="navForm" style="margin:0px">
						<jsp:include page="header.jsp" />
				</h:form>
				<div id="wizard">	
					<div id="eventPage">
						
						
						<rich:panel styleClass="content-panel">
							<h:panelGroup id="messageForm-block">
								<h:form id="messageForm">
									<h:messages id="message" layout="table" showDetail="true" showSummary="false"
									infoClass="global-message-info" warnClass="global-message-warn" 
									errorClass="global-message-error" fatalClass="global-message-fatal"/>
								</h:form>
							</h:panelGroup>	
						<h:form id="eventForm">
						<table class="panel-style"> 
						<tr>
							<td> 
								<h3 style="margin-bottom:10px; font-weight:bold">
								  Run a Client Test 
								</h3>
							</td>
							<td>
							</td>
						</tr>
						<tr>
							<td valign="top" colspan="2" align="left">
								<richext:simpleTogglePanel switchType="client" label="Client Test Details" headingLevel="h4" opened="false">
								<table>
								<tr> 
								<td align="left" valign="top">
									<div style="width:360px">
										<jsp:include page="demoEvtTiming.jsp"/>
									</div>
								</td>
								<td valign="top">
									<jsp:include page="demoEvtSignals.jsp"/>
								</td>
								</tr></table>
								</richext:simpleTogglePanel>
							</td>
						</tr>
						
						<tr>	
							<td colspan="2" align="left">
								<rich:dataTable id="clients"
									value="#{demoEvent.participantClients}" var="pclient"
										styleClass="content-table" captionStyle="font-size:11px;font-weight:bold" >
									<f:facet name="caption">
										<h:outputText value="Select Clients" />
							        </f:facet>
									<rich:column width="1%">
										<f:facet name="header">
											<h:outputText value="Choose clients" title="Choose clients" />
										</f:facet>
										<h:selectBooleanCheckbox id="chooseClients" value="#{pclient.selected}"  />
									</rich:column>
									<rich:column width="14%">
										<f:facet name="header">
											<h:outputText value="Name" title="Name" />
										</f:facet>
										<h:outputLabel value="#{pclient.participantName}" for="chooseClients" ></h:outputLabel>
									</rich:column>
								</rich:dataTable>
							</td>
						</tr>
						<tr>
							<td colspan="2" align="left" id="fm-submit">
							  <div>
								<table border="0">
									<tr>
										<td>
											<h3>
												<a4j:commandButton value="Select All"  title="Select All participants" actionListener="#{demoEvent.selectAllAction}" reRender="clients"/>
											</h3>
										</td>
										<td>
											<h3>
												<a4j:commandButton value="Select None" title="Select None participants" actionListener="#{demoEvent.deselectAllAction}" reRender="clients"/>
											</h3>
										</td>
										<td>
											<h3>
												<a4j:commandButton  type="submit" value="Start Client Test" title="Start Client Test" action="#{demoEvent.issueEventAction}" reRender="messageForm-block"/>
											</h3>
										</td>
									</tr>
								</table>
							  </div>
                              <div>
                                  <br/><br/>
                                  <hr/>
                                   <h:outputText value="#{demoEvent.disclimar}" />
                              </div>
							</td>
						</tr>

	                    </table>
						</h:form>
						</rich:panel>
					
						
						
					</div> <!-- end of event info page -->
				</div><!-- end of event info page -->
				<jsp:include page="footer.jsp" />
			</div>
		</body>
	</f:view>
</html>
