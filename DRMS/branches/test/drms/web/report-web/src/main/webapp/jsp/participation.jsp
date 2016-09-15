
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://akuacom.com/richext" prefix="richext"%>

<h:panelGroup layout="block" id="event-participation-block">

	<richext:dataTable id="eventParticipantDataTable" status="waitStatus"  width="100%" styleClass="event-rich-table"
			value="#{report.participation.eventParticipations}" var="part" rows="15">
			<rich:column sortable="true" sortBy="#{clientName}">
                <f:facet name="header">
                    <h:outputText value="Client" escape="false"/>
                </f:facet>				
				<h:outputText escape="false" value="<h3 class='heading-level3'> #{part.clientName} </h3>"> </h:outputText>
			</rich:column>
			<rich:column sortable="true" sortBy="#{programName}">
				<f:facet name="header">
                    <h:outputText value="Program" escape="false"  />
                </f:facet>
				<h:outputText value="#{part.programName}"/>				
			</rich:column> 
			<rich:column sortable="true" sortBy="#{eventName}">
                <f:facet name="header">
                    <h:outputText value="Event" escape="false"/>
                </f:facet>
				<h:outputText value="#{part.eventName}"/>
			</rich:column>
			<rich:column sortable="true" sortBy="#{eventDate}">
				<f:facet name="header">
                    <h:outputText value="Date" escape="false"  />
                </f:facet>
				<h:outputText value="#{part.eventDate}">
					<f:convertDateTime pattern="#{report.dateFormat}"/>
				</h:outputText>
			</rich:column> 
			<!-- will sort by two columns, first percentage and  then participation-->
			<rich:column sortable="true" sortBy="#{percentage.participation}">
                <f:facet name="header">
                    <h:outputText value="Participation" escape="false"/>
                </f:facet>
				<h:outputText value="#{part.status}"/>
			</rich:column>
			<f:facet name="footer">
				<rich:datascroller></rich:datascroller>
			</f:facet>
    </richext:dataTable>	
	<br/>
	<h:panelGroup layout="block" >
		<h3><h:commandButton  value="Export Data" title="Export Data" 
			action="#{report.participation.exportAction}" 
			disabled="#{report.participation.exportDisable}"
			style="border: 1px solid #2BB0FF;"/></h3>
	</h:panelGroup>	
	
	<h:outputText value="No participation data found" styleClass="not-found-msg" rendered="#{report.participation.eventParticipations.totalRowCount==0}"/>
</h:panelGroup>


