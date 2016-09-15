<!doctype html public "-//w3c//dtd html 4.0 transitional//en">
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>

<html>

<style>

</style>
<f:view>
    <head>
		<title>Client Shed Strategy</title>
		<jsp:include page="../head.jsp" />

    </head>

    <body>
		<h:form id="form">
			<jsp:include page="../header_reports.jsp" />
			
			<a4j:keepAlive beanName="clientShedStrategyTableBackingBean" />
			<rich:dataTable width="100%" style="width:1200px;">
				<f:facet name="header">
					<rich:columnGroup>
						<rich:column  style="text-align:left;font-size: 15px; " colspan="9">
							<h:outputText id="title" value="Client Shed Strategy" />
						</rich:column>
					</rich:columnGroup>
				</f:facet>
			</rich:dataTable>
			
			<rich:dataTable id="headerGrid" width="100%" style="width:1200px;">
				<f:facet name="caption">
					<rich:columnGroup>
						<rich:column id="queryRegion">
							<h:outputText value="Filter By Program Name: " style="font-family: Arial, Verdana, sans-serif;font-size: 12px;font-weight: bold;"/>
						
							
							<h:selectOneMenu  	id="filterOptions" 
												style="padding-left:5px;border: 1px solid;"
												value="#{clientShedStrategyTableBackingBean.searchProgramName}" >
								<f:selectItems value="#{clientShedStrategyTableBackingBean.programItems}"/> 	
							</h:selectOneMenu>
							
							<a4j:commandButton  id="FilterButton" type="submit" value="   Search   " title="Search Client Shed Strategy"  
												action="#{clientShedStrategyTableBackingBean.searchFilterAction}" 
												style="border: 1px solid #2BB0FF;"
												reRender="reportTable">
							</a4j:commandButton>
							<br />
							
						</rich:column>
					</rich:columnGroup>
				</f:facet>
			</rich:dataTable>
			<rich:panel id="reportTable">
				<rich:dataTable id="shedStrategyTable" value="#{clientShedStrategyTableBackingBean.shedStrategyList}" 
							var="item" style="width:1200px;"
							rendered="#{!clientShedStrategyTableBackingBean.rtpProgram}">	 		
					<rich:column >
						<f:facet name="header"><h:outputText value="Client" escape="false" /></f:facet>
						<h:outputText value="#{item.client}" />
					</rich:column>
					<rich:column >
						<f:facet name="header"><h:outputText value="Program" escape="false" /></f:facet>
						<h:outputText value="#{item.program}" />
					</rich:column>
					<rich:column >
						<f:facet name="header"><h:outputText value="Enabled" escape="false" /></f:facet>
						<h:outputText value="#{item.enroll}" />
					</rich:column>
					<rich:column >
						<f:facet name="header"><h:outputText value="Participant" escape="false" /></f:facet>
						<h:outputText value="#{item.participant}" />
					</rich:column>
					<rich:column >
						<f:facet name="header"><h:outputText value="Account<br/>Number" escape="false" /></f:facet>
						<h:outputText value="#{item.account}" />
					</rich:column>
					<rich:column >
						<f:facet name="header"><h:outputText value="Parent" escape="false" /></f:facet>
						<h:outputText value="#{item.parent}" />
					</rich:column>
					<rich:column >
						<f:facet name="header"><h:outputText value="Season" escape="false" /></f:facet>
						<h:outputText value="#{item.season}" />
					</rich:column>
					<rich:column >
						<f:facet name="header"><h:outputText value="End Time" escape="false" /></f:facet>
						<h:outputText value="#{item.startTime}" />
					</rich:column>
					<rich:column >
						<f:facet name="header"><h:outputText value="Moderate" escape="false" /></f:facet>
						<h:outputText value="#{item.moderateValue}" />
					</rich:column>
					<rich:column >
						<f:facet name="header"><h:outputText value="High" escape="false" /></f:facet>
						<h:outputText value="#{item.highValue}" />
					</rich:column>
				</rich:dataTable>
				
				<rich:dataTable id="rtpShedStrategyTable" value="#{clientShedStrategyTableBackingBean.rtpShedStrategyList}" 
							var="item" style="width:1200px;"
							rendered="#{clientShedStrategyTableBackingBean.rtpProgram}">	 		
					<rich:column >
						<f:facet name="header"><h:outputText value="Client" escape="false" /></f:facet>
						<h:outputText value="#{item.client}" />
					</rich:column>
					<rich:column >
						<f:facet name="header"><h:outputText value="Program" escape="false" /></f:facet>
						<h:outputText value="#{item.program}" />
					</rich:column>
					<rich:column >
						<f:facet name="header"><h:outputText value="Enabled" escape="false" /></f:facet>
						<h:outputText value="#{item.enroll}" />
					</rich:column>
					<rich:column >
						<f:facet name="header"><h:outputText value="Participant" escape="false" /></f:facet>
						<h:outputText value="#{item.participant}" />
					</rich:column>
					<rich:column >
						<f:facet name="header"><h:outputText value="Account<br/>Number" escape="false" /></f:facet>
						<h:outputText value="#{item.account}" />
					</rich:column>
					<rich:column >
						<f:facet name="header"><h:outputText value="Parent" escape="false" /></f:facet>
						<h:outputText value="#{item.parent}" />
					</rich:column>
					<rich:column >
						<f:facet name="header"><h:outputText value="End Time" escape="false" /></f:facet>
						<h:outputText value="#{item.startTime}" />
					</rich:column>
					<rich:column >
						<f:facet name="header"><h:outputText value="EXTREMELY HOT<br/>SUMMER<br/>WEEKDAY" escape="false" /></f:facet>
						<h:outputText value="#{item.extremelyHotSummerWeekday}" />
					</rich:column>
					<rich:column >
						<f:facet name="header"><h:outputText value="VERY HOT<br/>SUMMER<br/>WEEKDAY" escape="false" /></f:facet>
						<h:outputText value="#{item.veryHostSummerWeekday}" />
					</rich:column>
					<rich:column >
						<f:facet name="header"><h:outputText value="HOT SUMMER<br/>WEEKDAY" escape="false" /></f:facet>
						<h:outputText value="#{item.hotSummerWeekday}" />
					</rich:column>
					<rich:column >
						<f:facet name="header"><h:outputText value="MODERATE<br/>SUMMER<br/>WEEKDAY" escape="false" /></f:facet>
						<h:outputText value="#{item.moderateSummerWeekday}" />
					</rich:column>
					<rich:column >
						<f:facet name="header"><h:outputText value="MILD SUMMER<br/>WEEKDAY" escape="false" /></f:facet>
						<h:outputText value="#{item.mildSummerWeekday}" />
					</rich:column>
					<rich:column >
						<f:facet name="header"><h:outputText value="HIGH COST<br/>WINTER WEEKDAY" escape="false" /></f:facet>
						<h:outputText value="#{item.highCostWinterWeekday}" />
					</rich:column>
					<rich:column >
						<f:facet name="header"><h:outputText value="LOW COST<br/>WINTER WEEKDAY" escape="false" /></f:facet>
						<h:outputText value="#{item.lowCostWinterWeekday}" />
					</rich:column>
					<rich:column >
						<f:facet name="header"><h:outputText value="HIGH COST<br/>WEEKEND" escape="false" /></f:facet>
						<h:outputText value="#{item.highCostWeekend}" />
					</rich:column>
					<rich:column >
						<f:facet name="header"><h:outputText value="LOW COST<br/>WEEKEND" escape="false" /></f:facet>
						<h:outputText value="#{item.lowCostWeekend}" />
					</rich:column>
				</rich:dataTable>
				
			<h:panelGroup layout="block" rendered="#{clientShedStrategyTableBackingBean.resultEmptyFlag}">	
						<h:outputText value="Nothing found to display" />					
			</h:panelGroup>	
			</rich:panel>
			<br/>

			<br/>
			<h:commandLink 	value="Export To Excel" title="Export To Excel" action="#{clientShedStrategyTableBackingBean.exportToExcel}" />
			<jsp:include page="../footer.jsp" />
		</h:form>
    </body>
</f:view>
</html>