<!doctype html public "-//w3c//dtd html 4.0 transitional//en">
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://akuacom.com/richext" prefix="richext"%>

<h:panelGroup layout="block" >

	<richext:dataTable id="instances" status="waitStatus" value="#{report.clientStatus.instance.offlineInstance}" var="item" rows="15" width="95%">
		<rich:column sortBy="#{participantName}" sortable="true">
               <f:facet name="header">
                   <h:outputText value="Client" escape="false"/>
               </f:facet>				
			<h:outputText/>
			<h:outputText value="#{item.participantName}"></h:outputText>
		</rich:column>
		<rich:column sortBy="#{startTime}" sortable="true" >
			<f:facet name="header">
                   <h:outputText value="Date / Time Offline" escape="false"  />
               </f:facet>
			<h:outputText/>
			<h:outputText value="#{item.startTime}"><f:convertDateTime pattern="#{report.dateTimeFormat}"/></h:outputText>
		</rich:column> 
		<rich:column sortBy="#{duration}" sortable="true" >
               <f:facet name="header">
                   <h:outputText value="Duration (min)" escape="false"/>
               </f:facet>
			<h:outputText />
			<h:outputText value="#{item.duration}">
				<f:convertNumber  groupingUsed="true"/>
			</h:outputText>	
		</rich:column>

		<f:facet name="footer">
			<rich:datascroller
                limitToList="true"
                immediate="true"
                rendered="true"
				status="waitStatus"
                for="instances"/>		
		</f:facet>
    </richext:dataTable>

	<BR>

	<h:panelGroup layout="block" >
		<h:commandButton id="instanceButton" value="Export Data"
			title="Export Searched Client Offline Instance Data"
			action="#{report.clientStatus.instance.exportAction}"
			disabled="#{report.clientStatus.instance.offlineInstance.totalRowCount<=0}"
			style="border: 1px solid #2BB0FF;" />
	</h:panelGroup>	
	<h:outputText value="No offline Instances found" styleClass="not-found-msg" 
		rendered="#{report.clientStatus.instance.offlineInstance.totalRowCount==0}"/>
		
</h:panelGroup>

