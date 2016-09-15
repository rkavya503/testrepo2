<!doctype html public "-//w3c//dtd html 4.0 transitional//en">
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>

<html>

<f:view>
    <head>
    
    <jsp:include page="../head.jsp" />
    </head>
    <body>
    <h:form>
        <jsp:include page="../header_scoreCard.jsp" />
        
        <a4j:keepAlive beanName="scorecard" />
        
        <a4j:status id="waitStatus" forceId="true"
                onstart="javascript:Richfaces.showModalPanel('waitModalPanel');"
                onstop="javascript:Richfaces.hideModalPanel('waitModalPanel');" />
        
		<rich:panel styleClass="about-content-panel; font:normal 9px arial">
           
		<rich:messages layout="table" globalOnly="false"
		               infoClass="global-message-info" warnClass="global-message-warn"
		               errorClass="global-message-error" fatalClass="global-message-fatal" />
		
		   
            <f:facet name="header">
                <h:outputText value="Scorecard" />
            </f:facet>

                <rich:panel style="width:50%">
                <f:facet name="header">
                    <h:outputText value="Create Event Scorecard by Program" />
                </f:facet>
                
                <h:panelGrid id="searchgrid" columns="1">
                    <h:panelGroup>
                        <h:outputText value="Program" />
                    </h:panelGroup>
                    <h:panelGroup>
                        <h:selectOneMenu id="programClass" 
                                         style="width:200px"
                                         value="#{scorecard.programClass}" >
                            <f:selectItems value="#{scorecard.availableProgramClasses}"/>
                        </h:selectOneMenu>
                    </h:panelGroup>
                    <h:panelGroup>
                        <rich:spacer height="10"/>
                        <h:outputText value="Event Date Range" />
                    </h:panelGroup>
                    <h:panelGroup>
                        <h:panelGrid columns="2">
                            <h:outputLabel value="From" style="text-align:left"/>
                            <h:outputLabel value="To" style="text-align:left"/>
                            <rich:calendar id="start" popup="true" value="#{scorecard.startDate}" 
                            				datePattern="#{scorecard.dateFormat}"/>
                            <rich:calendar id="end" popup="true" value="#{scorecard.endDate}" 
                            				datePattern="#{scorecard.dateFormat}"/>
                        </h:panelGrid>
                    </h:panelGroup>
                    <h:panelGroup>
                        <rich:spacer height="10"/>
                        <a4j:commandButton value="Search" 
                                           action="#{scorecard.searchHistoryAction}"
                                           status="waitStatus"
                                           reRender="searchresults" />
                    </h:panelGroup>
                </h:panelGrid>
            </rich:panel>
				
            <rich:spacer height="20"/>
                
            <rich:dataTable id="searchresults" 
                            rows="20"
                            value="#{scorecard.eventList}" 
                            width="80%"
                            var="event">
                <f:facet name="header">
                    <h:outputText value="Event Results" />
                </f:facet>
                <rich:column width="25%" sortBy="#{event.programName}" >
                    <f:facet name="header">
                        <h:outputText value="Program"/>
                    </f:facet>
                    <h:outputText value="#{event.programName}" />
                </rich:column>
                <rich:column width="25%" sortBy="#{event.event.eventName}">
                    <f:facet name="header">
                        <h:outputText value="Event" />
                    </f:facet>
                    <h:outputText value="#{event.event.eventName}" />
                </rich:column>
                <rich:column width="25%" sortBy="#{event.event.startTime}" sortOrder="DESCENDING">
                    <f:facet name="header">
                        <h:outputText value="Date / Time" />
                    </f:facet>
                    <h:outputText value="#{event.event.startTime}">
                    <f:convertDateTime pattern="#{scorecard.dateFormat}"/>
                    </h:outputText>
                    <h:outputText value=" " />
                    <h:outputText value="#{event.event.startTime}">
                    <f:convertDateTime pattern="HH:mm"/>
                    </h:outputText>
                    <h:outputText value=" - " />
                    <h:outputText value="#{event.event.endTime}" >
                    <f:convertDateTime pattern="HH:mm"/>
                    </h:outputText>
                </rich:column>
                <rich:column width="25%">
                    <f:facet name="header">
                        <h:outputText value="Scorecard"/>
                    </f:facet>
                    <h:outputLink 
                      value="/opt.report/jasperReport?reportName=scorecard&amp;eventName=#{event.event.eventName}&amp;startTime=#{scorecard.startDateAsTime}&amp;endTime=#{scorecard.endDateAsTime}">
                        <f:verbatim>View Scorecard</f:verbatim>
                    </h:outputLink>
                </rich:column>
                <f:facet name="footer">
                    <rich:datascroller immediate="true" id="result_scroller" for="searchresults" maxPages="50" />
                </f:facet>
            </rich:dataTable>
        </rich:panel>
        <br />
        <br />
        <jsp:include page="../footer.jsp" />
        
        <rich:modalPanel id="waitModalPanel" autosized="true" width="200"
            height="120" moveable="false" resizeable="false">
            <f:facet name="header">
                <h:outputText value="Processing" />
            </f:facet>
            <h:outputText value="Please wait..." />
            <center>
                <h:graphicImage value="/images/ajax-loader.gif" />
            </center>
        </rich:modalPanel>
        
    </h:form>
    </body>
</f:view>
</html>