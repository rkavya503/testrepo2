<!doctype html public "-//w3c//dtd html 4.0 transitional//en">
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>

<h:panelGroup layout="block">
			
		<h:panelGrid columns="1" styleClass="event-panel-grid" style="display:">
				<h:panelGroup>
					<h:outputText value="Report Generate Time: " style="fontsize:12px"/>
					<h:panelGroup>
						<h:selectOneMenu value="#{timerConfigBackingBean.reportConfig.invokeHour}" disabled="#{!timerConfigBackingBean.clientReportOfflineConfigurationEnabled}" >
							<f:selectItems value="#{timerConfigBackingBean.hourList}"/>
						</h:selectOneMenu>
						:
						<h:selectOneMenu value="#{timerConfigBackingBean.reportConfig.invokeMin}" disabled="#{!timerConfigBackingBean.clientReportOfflineConfigurationEnabled}">
							<f:selectItems value="#{timerConfigBackingBean.minList}"/>
						</h:selectOneMenu>
					</h:panelGroup>
				</h:panelGroup>
				<h:commandButton action="#{timerConfigBackingBean.updateReportAction}" value="Save" rendered = "#{timerConfigBackingBean.clientReportOfflineConfigurationEnabled}"/>
		</h:panelGrid>		
</h:panelGroup>
