
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://akuacom.com/richext" prefix="richext"%>

<h:panelGroup layout="block" >
	<richext:dataTable status="waitStatus" id="statistics" value="#{report.clientStatus.summary.offlineStatistics}" var="item" rows="15" width="95%">
		<rich:column sortBy="#{participantName}" width="20%"> 
			<f:facet name="header">
					<h:outputText value="Client" escape="false" />
				</f:facet>
			<h:outputText escape="false" value="<h4 class='heading-level4'> #{item.participantName} </h4>"> </h:outputText>
		</rich:column>			
		<rich:column sortBy="#{times}" width="20%"> 
			<f:facet name="header">
				
					<h:outputText value="# Times" escape="false" />
				
			</f:facet>
			<h:outputText value="#{item.times}">
				 <f:convertNumber  groupingUsed="true"/>
			</h:outputText>
		</rich:column>			
		<rich:column sortBy="#{totalTime}" width="20%"> 
			<f:facet name="header">
				
					<h:outputText value="Total Time (min)" escape="false" />
				
			</f:facet>
			<h:outputText value="#{item.totalTime}">
				<f:convertNumber  groupingUsed="true"/>
			</h:outputText>
		</rich:column>			
		<rich:column sortBy="#{timePercent}" width="20%"> 
			<f:facet name="header">
				
					<h:outputText value="% Time" escape="false" />
				
			</f:facet>
			<h:outputText value="#{100*item.timePercent}">
				 <f:convertNumber  maxFractionDigits="1"/>
			</h:outputText>
		</rich:column>			
		<rich:column sortBy="#{duringEvent}" width="20%"> 
			<f:facet name="header">
				
					<h:outputText value="% During Events" escape="false" />
				
			</f:facet>
			
			<h:outputText value="No Event" rendered="#{item.duringEvent==null}">
			</h:outputText>
	
			<h:outputText value="#{100*item.duringEvent}" rendered="#{item.duringEvent!=null}">
				 <f:convertNumber  maxFractionDigits="1"/>
			</h:outputText>
		</rich:column>			

		<f:facet name="footer">
			<rich:datascroller
                limitToList="true"
                immediate="true"
                rendered="true"
				status="waitStatus"
                for="statistics"/>		
		</f:facet>
	</richext:dataTable>
	
	<br/>
	
	<h:panelGroup layout="block" >
		<h4><h:commandButton id="statisticsButton" value="Export Data"
			title="Export Searched Client Offline Statistics Data"
			action="#{report.clientStatus.summary.exportAction}"
			disabled="#{report.clientStatus.summary.offlineStatistics.totalRowCount<=0}"
			style="border: 1px solid #2BB0FF;" /></h4>
	</h:panelGroup>	
	
	<h:outputText value="No Offline statistics found" styleClass="not-found-msg" 
		rendered="#{report.clientStatus.summary.offlineStatistics.totalRowCount==0}"/>
</h:panelGroup>

