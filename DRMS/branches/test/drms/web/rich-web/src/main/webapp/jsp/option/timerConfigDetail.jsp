<!doctype html public "-//w3c//dtd html 4.0 transitional//en">
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>

<html>

<f:view>
    <head>
		<title>Timer Config Detail</title>
		<jsp:include page="../head.jsp" />

    </head>

    <body>
	<jsp:include page="../header_options.jsp" />
		<h:form>
			<a4j:keepAlive beanName="timerConfigBackingBean" />
			<h:panelGroup layout="block">
				<h:panelGrid columns="2" styleClass="event-panel-grid" style="display:">
					Timer Name:
					<h:panelGroup>
						<h:outputText value="#{timerConfigBackingBean.currentTimerConfig.name}" />
					</h:panelGroup>
					Invoke Time: 
					<h:panelGroup>
						<h:selectOneMenu value="#{timerConfigBackingBean.searchHour}">
							<f:selectItems value="#{timerConfigBackingBean.hourList}"/>
						</h:selectOneMenu>
						:
						<h:selectOneMenu value="#{timerConfigBackingBean.searchMin}">
							<f:selectItems value="#{timerConfigBackingBean.minList}"/>
						</h:selectOneMenu>
					</h:panelGroup>
					
				</h:panelGrid>			
				<h:panelGroup layout="block" rendered="#{!timerConfigBackingBean.normalTypeFlag}">
					<h:panelGrid columns="2" styleClass="event-panel-grid" style="display:">
						Start Month:
						<h:panelGroup>
							<h:inputText value="#{timerConfigBackingBean.currentTimerConfig.startMonth}" />
						</h:panelGroup>
						Start Day: 
						<h:panelGroup>
							<h:inputText value="#{timerConfigBackingBean.currentTimerConfig.startDay}" />
						</h:panelGroup>
						End Month:
						<h:panelGroup>
							<h:inputText value="#{timerConfigBackingBean.currentTimerConfig.endMonth}" />
						</h:panelGroup>
						End Day: 
						<h:panelGroup>
							<h:inputText value="#{timerConfigBackingBean.currentTimerConfig.endDay}" />
						</h:panelGroup>
					</h:panelGrid>		
				</h:panelGroup>
				
				<h:commandButton value="Update" title="Update" action="#{timerConfigBackingBean.updateSingleAction}" />	
				
			</h:panelGroup>
			
		</h:form>
		<!--------------------------------------------------- Client Information Table End  --------------------------------------------------->
		<jsp:include page="../footer.jsp" />
    </body>
</f:view>
</html>