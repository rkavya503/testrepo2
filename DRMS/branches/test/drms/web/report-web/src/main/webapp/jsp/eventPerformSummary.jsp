
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://akuacom.com/richext" prefix="richext"%>


<h:panelGroup layout="block" > 

	<br/>
	<h:outputText value="Performance Summary for Selected Event: #{report.events.selectedEventName}" styleClass="form-field" ></h:outputText>
	
<br/><br/>
	<richext:treeTable status="waitStatus" width="100%" height="350px" id="performSummary" value="#{report.events.reportPerformanceTree}" var="item" >       		
		<richext:treeColumn width="20%">
			<f:facet name="header">
				<a4j:region>
					<h:outputText value="Entity" escape="false"  />
				</a4j:region>
			</f:facet>
			
			<h4>
			<h:outputText id="name" value="#{item.participantName}" rendered="#{!item.client}" />
			<a4j:commandLink value="#{item.participantName}"
				status="waitStatus"
                action="#{report.events.eventSignalAction}"
                reRender="reportEventChildPage, reportSignalDetailTable" rendered="#{item.client}">
                
                <f:param name="selectedClientName" value="#{item.participantName}" />
            </a4j:commandLink>
			</h4>
							
		</richext:treeColumn>

		<rich:column width="8%">
			<f:facet name="header">
				<a4j:region>
					<h:outputText value="Program" escape="false"  />
				</a4j:region>
			</f:facet>
			<h:outputText value="#{item.programName}" />
		</rich:column>

		<rich:column width="15%">
			<f:facet name="header">
				<a4j:region>
					<h:outputText value="Event" escape="false"  />
				</a4j:region>
			</f:facet>
			<h:outputText value="#{item.eventName}" />
		</rich:column>

		<rich:column width="15%">
			<f:facet name="header">
				<a4j:region>
					<h:outputText value="Start" escape="false"  />
				</a4j:region>
			</f:facet>
			<h:outputText value="#{item.startTime}" ><f:convertDateTime pattern="#{report.dateTimeFormat}"/></h:outputText>
		</rich:column>

		<rich:column width="15%">
			<f:facet name="header">
				<a4j:region>
					<h:outputText value="End" escape="false"  />
				</a4j:region>
			</f:facet>
			<h:outputText value="#{item.endTime}" ><f:convertDateTime pattern="#{report.dateTimeFormat}"/></h:outputText>
		</rich:column>

		<rich:column rendered="#{reportFramework.usageEnable}">
			<f:facet name="header">
				<a4j:region>
					<h:outputText value="Avg Shed" escape="false"  />
				</a4j:region>
			</f:facet>
			<h:outputText value="#{item.avgShed}" />
		</rich:column>

		<rich:column rendered="#{reportFramework.usageEnable}">
			<f:facet name="header">
				<a4j:region>
					<h:outputText value="Total Shed" escape="false"  />
				</a4j:region>
			</f:facet>
			<h:outputText value="#{item.totalShed}" />
		</rich:column>

		<rich:column rendered="#{report.usageEnable}">
			<f:facet name="header">
				<a4j:region>
					<h:outputText value="Avg Shed" escape="false"  />
				</a4j:region>
			</f:facet>
			<h:outputText value="#{item.avgShed}" rendered="#{!item.client}" />
			<h:outputText value="" rendered="#{item.client}"/>
		</rich:column>

		<rich:column rendered="#{report.usageEnable}">
			<f:facet name="header">
				<a4j:region>
					<h:outputText value="Total Shed" escape="false"  />
				</a4j:region>
			</f:facet>
			<h:outputText value="#{item.totalShed}" rendered="#{!item.client}"/>
			<h:outputText value="" rendered="#{item.client}"/>
		</rich:column>
	</richext:treeTable>
	<br/>
	<h:panelGroup layout="block" >
		<h4><h:commandButton  value="Export Data" title="Export Data" 
			action="#{report.events.exportEventPerformanceAction}" 
			disabled="#{report.events.exportPerformanceDisable}"
			style="border: 1px solid #2BB0FF;"/></h4>
	</h:panelGroup>		
</h:panelGroup>

