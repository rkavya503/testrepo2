<!doctype html>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<f:loadBundle basename="com.akuacom.pss2.richsite.OperatorDashboard_en"
	var="msg" />
<html>
<f:view>
	<head>
<script src="<%=request.getContextPath()%>/js/uncraphael.js"></script>
<script src="<%=request.getContextPath()%>/js/util.js"></script>
<script src="<%=request.getContextPath()%>/js/widgets.js"></script>
<script src="<%=request.getContextPath()%>/js/usagedata.js"></script>
<script
	src="https://maps-api-ssl.google.com/maps/api/js?v=3&sensor=false"
	type="text/javascript"></script>
<script src="http://www.google.com/jsapi"></script>
<title>Prototype:  Operator Dashboard with Raphael and Google Maps</title>


<jsp:include page="../../head.jsp" />


	</head>
<h:outputText value="#{odProgramManager.activeProgramsJson}" escape="false" /> 
	<body>
		<h:form id="opdashForm">
			<jsp:include page="../../header_opdash.jsp" />
			<rich:panel>
				<!-- program status -->
				<div id="programStatus" style="width: 100%; height: 180px">
					<f:facet name="header">
						<h:outputText value="#{msg.programStatus}" />
					</f:facet>
					<div
						style="position: absolute; left: 0; width: 550px; margin-left: 100px;">
						<rich:dataTable id="programStatus"
							rows="#{odProgramManager.rowCount}" border="1" cellpadding="5"
							cellspacing="3" value="#{odProgramManager.activePrograms}"
							var="odProgramVar">
							<rich:column width="25%">
								<f:facet name="header">
									<h:outputText value="#{msg.programs}" style="font-size: small" />
								</f:facet>
								<div style="text-align: center">
									<h:outputText value="#{odProgramVar.programName}" />
								</div>
							</rich:column>
							<rich:column width="20%">
								<f:facet name="header">
									<h:outputLabel value="#{msg.availableShed}"
										style="font-size: small" />
								</f:facet>
								<table>
									<tr>
										<!-- shed gauge -->
										<td><h:outputText value="#{odProgramVar.shedGaugeDiv}"
												escape="false" /> <h:outputText escape="false"
												value="#{odProgramVar.shedGaugeScript}" />
										</td>
										<td><h:outputText
												value="#{odProgramVar.availableShedFmt} kW"
												style="font-size: 8pt" />
										</td>
									</tr>
									<tr>
										<td colspan="2"><h:outputText
												value="#{odProgramVar.totalShedFmt} " style="font-size: 6pt" />
											<h:outputText value=" kW Total Registered Shed"
												style="font-size: 6pt" /></td>
									</tr>
								</table>
							</rich:column>
							<rich:column width="15%">
								<f:facet name="header">
									<h:outputLabel value="#{msg.offlineClients}"
										style="font-size: small" />
								</f:facet>
								<div style="text-align: center">
									<h:outputText value="#{odProgramVar.offlineClients}" />
								</div>
							</rich:column>
							<rich:column width="15%">
								<f:facet name="header">
									<h:outputLabel value="#{msg.issueEvent}"
										style="font-size: small" />
								</f:facet>
								<div style="text-align: center">
									<h:commandButton action="#{odProgramManager.callEvent}"
										title="#{msg.callEvent}" value="#{odProgram.programName}"
										image="/images/callEvent.png">
										<f:setPropertyActionListener
											target="#{odProgramManager.selectedProgramName}"
											value="#{odProgramVar.programName}" />
									</h:commandButton>
								</div>
							</rich:column>
						</rich:dataTable>
					</div>
				</div>
			</rich:panel>
			<rich:tabPanel id="tabPanel" width="100%" height="400"
				rendered="#{not empty odProgramManager.activePrograms}" switchType="client">
				<c:forEach items="#{odProgramManager.activePrograms}" var="odp">
					<rich:tab name="#{odp.programName}">
						<f:facet name="label">
							<h:panelGrid columns="1">
								<h:outputText value="#{odp.programName}" />
							</h:panelGrid>
						</f:facet>
						<table border="1" cellPadding="0" cellSpacing="0" width="100%">
							<tr>
								<td width="30%" align="center"><h:outputText
										value="Program Participants" style="font-size: 120%" /> <br />
									<h:outputText value="#{odp.mapDiv}" escape="false" style="width: 300px, height: 300px"/> 
									<h:outputText										escape="false" value="#{odp.mapScript }" />
								<td width="70%">
									<table border="1" cellPadding="0" cellSpacing="0" width="100%">
										<tr>
											<td align="center" style="width: 25%; height: 50%">
												<table>
													<tr>
														<td align="center" style="font-size: 120%">Active
															Events</td>
													</tr>
													<c:forEach items="#{odp.currentEvents}" var="curEv">
														<tr>
															<td align="center" style="font-size: 120%"><h:outputText
																	value="#{curEv.date}" /></td>
														</tr>
														<tr>
															<!-- event gauge -->
															<td align="center"><h:outputText escape="false"
																	value="#{curEv.gaugeDiv}" /> <h:outputText
																	escape="false" value="#{curEv.gaugeScript}" />
															</td>
														</tr>
													</c:forEach>
												</table></td>
											<td align="center" style="width: 25%; height: 50%">
												<table>
													<tr>
														<td align="center"
															style="vertical-align: text-top; font-size: 120%">Event
															Potential</td>
													</tr>
													<tr>	
														<!-- large shed gauge -->
 														<td><h:outputText value="#{odp.largeShedGaugeDiv}" escape="false" /> 
														 <h:outputText escape="false" value="#{odp.largeShedGaugeScriptIdx}" />
														</td>
													</tr>
													<tr>
														<td align="center"><h:outputText
																value="#{odp.availableShedFmt} kW Available Shed"
																style="font-size: 8pt" />
														</td>
													</tr>
													<tr>
														<td align="center"><h:outputText
																value="#{odp.totalShedFmt} kW Total Registered Shed"
																style="font-size: 9pt" /></td>
													</tr>
												</table></td>
											<td align="center" style="width: 25%; height: 50%">
												<table>
													<tr>
														<td align="center" style="font-size: 120%">Offline
															Clients</td>
													</tr>
													<tr>
														<td>
														<h:outputText escape="false" value="#{odp.offlineDiv}"  />
														<h:outputText escape="false" value="#{odp.offlineScript}" />
														</td>
													</tr>
													<div style="text-align: center">
													</div>
													<tr>
														<td align="center" style="font-size: 120%">Opt Outs</td>
													</tr>
														<tr>
															<td> 
																	
															<h:outputText escape="false" value="#{odp.optOutDiv}"  />
															<h:outputText escape="false" value="#{odp.optOutScript}" />

															</td>
														</tr>
												</table>
											<td align="center" style="width: 25%; height: 50%">
												<table>
													<tr>
														<td align="center" style="font-size: 120%">Pending
															Events</td>
													</tr>
													<c:forEach items="#{odp.pendingEvents}" var="pendEv">
														<tr>
															<td align="center" style="font-size: 120%"><h:outputText
																	value="#{pendEv.date}" /></td>
														</tr>
														<tr>
															<!-- pending event widget -->
															<td align="center"><h:outputText escape="false"
																	value="#{pendEv.gaugeDiv}" /> <h:outputText
																	escape="false" value="#{pendEv.gaugeScript}" />
															</td>
														</tr>
													</c:forEach>
												</table></td>
										</tr>
										<tr>
											<td colspan="4" align="center" style="height: 50%">
												<table>
													<tr>
														<td width="30%">
															<table>
																<tr>
																	<td align="center">Program Summary</td>
																</tr>
																<tr>
																	<td align="center" style="font-size: 8px">Real-Time
																		Energy</td>
																</tr>
																<tr>
																	<td align="center"><h:outputText escape="false"
																			value="#{odp.chartLegendDiv}" /> <h:outputText
																			escape="false" value="#{odp.legend}" />
																	</td>
																</tr>
															</table>
														</td>
														<td width="70%"><h:outputText escape="false" value="#{odp.chartDiv}" /> 
																<h:outputText escape="false" value="#{odp.usageChartIdx}" />
														</td>
													</tr>
												</table>
											</td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
					</rich:tab>
				</c:forEach>
			</rich:tabPanel>
			<h:outputText value="No active search results."
				style="font-style:italic"
				rendered="#{empty odProgramManager.activePrograms}" />

		</h:form>

		<jsp:include page="../../footer.jsp" />

	</body>
</f:view>
</html>
