<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://akuacom.com/richext" prefix="richext"%>
<a4j:keepAlive beanName = "drKiosk"/>
<h:panelGroup layout="block" id="scheduledEventsPanelGroup">
      <div class="resultGridHeaderBg">Scheduled Events <span class="datetime"><h:outputText id="outputText_ScheduledEventsTime" value="#{drFrame.currentSystemTime}"/></span></div>
      <div class="resultGridBodyBg">
        <div class="dataWrapper">
			<h:panelGroup layout="block" rendered="#{drKiosk.scheduledEventsGroup.eventEmptyFlag}">
					<table class="KioskNoEventsDateTable">
						<tr>
						  <td><img src="images/icons/NoEventIcon.png" alt="no events in progress" align="middle" class="NoEvenIcon" /> No Events Are Currently Scheduled</td>
						</tr>
					</table>	
			</h:panelGroup>
			<h:panelGroup layout="block" rendered="#{!drKiosk.scheduledEventsGroup.eventEmptyFlag}">
				<rich:scrollableDataTable 
				rowKeyVar="rkv" 
				frozenColCount="0" 
				height="96%" 
				width="100%"
                id="scheduledEventList" 
				rows="0" 
				styleClass="EvtKioskTable" 
				rowClasses="EvtKioskTableCell,EvtKioskTableCell_nobg"
                value="#{drKiosk.scheduledEventsGroup.events}" 
				var="item" 
				sortMode="single"
                >
						
						<rich:column sortable="false" styleClass="rich-table-cell-first" width="300px">
							<f:facet name="header" >
								<h:outputText value="Program" escape="false" />
							</f:facet>
							<h:panelGroup layout="block">
							<h:graphicImage value="#{item.iconLocation}" styleClass="headlineIcon2">
							</h:graphicImage>
							</h:panelGroup>
							
							<h:panelGroup layout="block">
							<h:outputText  value="#{item.program}">
							</h:outputText>
							</h:panelGroup>
						</rich:column>
						<rich:column sortable="false" width="140px">
							<f:facet name="header" >
								<h:outputText value="Product" escape="false" />
							</f:facet>
							<h:outputText  value="#{item.product}">
							</h:outputText>
						</rich:column>
						<rich:column sortable="false" width="80px">
							<f:facet name="header" >
								<h:outputText value="Dispatch<br/>Type" escape="false" />
							</f:facet>
							<h:outputText  value="#{item.dispatchType}">
							</h:outputText>
						</rich:column>
						<rich:column sortable="false" width="100px">
							<f:facet name="header" >
								<h:outputText value="Dispatch<br/>Location" escape="false" />
							</f:facet>
							<h:outputText  value="#{item.dispatchLocation}">
							</h:outputText>
						</rich:column>
						<rich:column sortable="false" width="60px">
							<f:facet name="header" >
								<h:outputText value="Start<br/>Date" escape="false" />
							</f:facet>
							<h:outputText  value="#{item.startDate}">
							</h:outputText>
						</rich:column>
						<rich:column sortable="false" width="60px">
							<f:facet name="header">
								<h:outputText value="End<br/>Date" escape="false"  />
							</f:facet>
							<h:outputText  value="#{item.endDate}">
							</h:outputText>
						</rich:column>
						<rich:column sortable="false" width="60px">
							<f:facet name="header">
								<h:outputText value="Start<br/>Time" escape="false"  />
							</f:facet>
							<h:outputText  value="#{item.startTime}">
							</h:outputText>
						</rich:column>
						<rich:column sortable="false" styleClass="rich-table-cell-last" width="60px">
							<f:facet name="header">
								<h:outputText value="End<br/>Time" escape="false"  />
							</f:facet>
							<h:outputText  value="#{item.endTime}">
							</h:outputText>
						</rich:column>
                
				</rich:scrollableDataTable>		
			</h:panelGroup>
          
        </div>
        <div class="clearfix"></div>
      </div>
      <div class="resultGridFooterBgScheduled"></div>
</h:panelGroup>		  