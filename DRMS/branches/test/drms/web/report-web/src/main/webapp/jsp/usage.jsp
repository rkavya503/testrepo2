<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://akuacom.com/richext" prefix="richext"%>
<%@ page import="com.akuacom.utils.BuildProperties" %> 

<%
    BuildProperties buildProperties = new BuildProperties();
%>

<h:panelGroup layout="block" id="usage-block" >
	<richext:dataTable id="reports" value="#{report.usage.reports}" var="p" rows="15" width="95%">
		<f:facet name="header4">
			<h:outputText value="Entire Day " />
			<f:facet name="header">
				<rich:columnGroup>
					<rich:column colspan="1">
					</rich:column>
					<rich:column colspan="2">
						<h:outputText value="Baseline" />
					</rich:column>
					<rich:column colspan="2">
						<h:outputText value="Telemetry" />
					</rich:column>
					<rich:column colspan="2">
						<h:outputText value="Shed" />
					</rich:column>
				</rich:columnGroup>
			</f:facet>
		</f:facet>
		<rich:column width="100px" colspan="1" styleClass="center-column" >
			<h:outputText value="#{p.catalog}"
				style="font-weight:bold" />
		</rich:column>
		<rich:column width="100px" styleClass="right-column" colspan="1">
			<f:facet name="header">
				<h:outputText value="Avg(KW)" title="Avg" />
			</f:facet>
			<h:outputText id="dayAvgBase" value="#{p.baseAvg}" >
				 <f:convertNumber pattern = "#,##0.00"/>
			</h:outputText>
		</rich:column>				
		<rich:column width="100px" colspan="1" styleClass="right-column">
			<f:facet name="header">
				<h:outputText value="Total(KWH)" title="total" />
			</f:facet>
			<h:outputText id="dayTotalBase" value="#{p.baseTotal}" >
				<f:convertNumber pattern = "#,##0.00"/>
			</h:outputText>
		</rich:column>
		<rich:column width="100px"  styleClass="right-column">
			<f:facet name="header">
				<h:outputText value="Avg(KW)" title="Avg" />
			</f:facet>
			<h:outputText id="dayAvgActual" value="#{p.actualAvg}" >
				<f:convertNumber pattern = "#,##0.00"/>
			</h:outputText>
		</rich:column>				
		<rich:column width="100px" styleClass="right-column">
			<f:facet name="header">
				<h:outputText value="Total(KWH)" title="total" />
			</f:facet>
			<h:outputText id="dayTotalActual"
				value="#{p.actualTotal}" >
				 <f:convertNumber pattern = "#,##0.00"/>
			</h:outputText>
		</rich:column>
		<rich:column width="100px" styleClass="right-column">
			<f:facet name="header">
				<h:outputText value="Avg(KW)" title="Avg" />
			</f:facet>
			<h:outputText id="dayAvgShed" value="#{p.shedAvg}" >
			 <f:convertNumber pattern = "#,##0.00"/>
			</h:outputText>
		</rich:column>
		<rich:column width="100px"  styleClass="right-column">
			<f:facet name="header">
				<h:outputText value="Total(KWH)" title="total" />
			</f:facet>
			<h:outputText id="dayTotalShed" value="#{p.shedTotal}" >
			 <f:convertNumber pattern = "#,##0.00"/>
			</h:outputText>
		</rich:column>
	</richext:dataTable>
	
	<rich:spacer></rich:spacer>
	<br/>
	<br/>
	<rich:panel styleClass="content-panel">
		<script type="text/javascript" src="swfobject.js"></script>
		<script type="text/javascript">     
			var flashvars = {
			  pdWsURL: "/opt.report/ws/OperatorReportUsageDataServicerBean",
			  participants:"<h:outputText value="#{report.usage.participants}" />",
			  participantName:"<h:outputText value="#{report.usage.participants}" />",
			  plotLabel: "",
			  hideControls: "true",
			  dataSet1: "Baseline,Baseline",
			  dataSet2: "Demand,Demand",
			  selectedDate: "<h:outputText value="#{report.searchCriteria.startDate}" ><f:convertDateTime pattern='yyyy-MM-dd' /></h:outputText>"
			};          
			var params = {};            
            params.wmode = "transparent"; 
            
			swfobject.embedSWF("DRASFlexReportUsage.swf", "usageCurve", "98%", "430", "9.0.0", null, flashvars, params, null);
		</script>		
		<div id="usageCurve"></div>	
	</rich:panel>	
	<br/>	
	
    <h:panelGroup layout="block" rendered="#{report.usage.aggregator}"> 
    <h:outputText value="#{report.usage.contributedInfo}" >     
    </h:outputText>
    <a4j:commandLink            
            immediate="true"
            oncomplete="#{rich:component('editRuleWindow')}.show()"
            value="More details"
            >           
      </a4j:commandLink>   
    </h:panelGroup>

	<h:panelGroup layout="block" >
		
							<table border="0">
								<tr>
									<td>
										<h3>
											<h:commandButton value="Export Data"  title="Export Data" action="#{report.usage.exportAction}" 
												disabled="#{report.usage.exportDisable}"
												style="border: 1px solid #2BB0FF;"/>
										</h3>
									</td>
								</tr>
							</table>	
		<h:outputText value="No usage related data found" styleClass="not-found-msg" 
			rendered="#{report.usage.reports.totalRowCount==0}"/>
	</h:panelGroup>	
	<rich:modalPanel showWhenRendered="false" id="editRuleWindow" autosized="true" keepVisualState="false" width="400" >
	
	  <rich:panel styleClass="panel-style">
	                
	
	                <div id="fm-submitr">   
	                <br/>  
	                <h:outputText value="Contributing participants: "></h:outputText>
	                <h:outputText value="#{report.usage.contributedPaticipantNamesString}" >
	                </h:outputText>
	                
	                <br/>   
	                <h:outputText value="Non Contributing participants: "></h:outputText>
	                <h:outputText value="#{report.usage.nonPaticipantNamesString}" >
	                </h:outputText>
	                <br/>
	                * indicates a participant not attend this activity 
	                <br/>               
	                <a4j:commandButton
	                    value="Close"
	                    title="Close"
	                    immediate="true"                    
	                    oncomplete="#{rich:component('editRuleWindow')}.hide();"
	                    >
	                 </a4j:commandButton>
	
	
	                </div>
	            </rich:panel>
	
	    
	</rich:modalPanel>
</h:panelGroup>

	


