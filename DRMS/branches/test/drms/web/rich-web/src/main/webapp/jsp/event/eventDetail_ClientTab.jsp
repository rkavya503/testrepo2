<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
    xmlns:f="http://java.sun.com/jsf/core"
    xmlns:a4j="http://richfaces.org/a4j"
    xmlns:rich="http://richfaces.org/rich"
    xmlns:h="http://java.sun.com/jsf/html" version="2.1">
    <jsp:directive.page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />

        <h:form>
        <a4j:outputPanel  ajaxRendered="true" id="clientPanel">
            <rich:dataTable
                id="clients"
                value="#{EventDetailDataModel.allClientsInEvent}"
                var="client"
                width="100%"
                styleClass="event-rich-table ">

            <rich:spacer height="30px" width="700px" />
                <rich:column sortBy="#{client.clientName}" sortOrder="ASCENDING">
                    <f:facet name="header">
                        <h:outputText value="Client" escape="false"  />
                    </f:facet>
                    <h:outputText  value="#{client.clientName}">
                    </h:outputText>
                </rich:column>
                <rich:column sortBy="#{client.parentName}">
                    <f:facet name="header">
                        <h:outputText value="Participant" escape="false"  />
                    </f:facet>
                    <h:outputText  value="#{client.parentName}">
                    </h:outputText>
                </rich:column>
                <rich:column sortBy="#{client.type}">
                    <f:facet name="header">
                        <h:outputText value="Type" escape="false"/>
                    </f:facet>
                    <h:outputText value="#{client.typeString}" />
                </rich:column>
                <rich:column>
                    <f:facet name="header">
                        <h:outputText value="Mode" escape="false"/>
                    </f:facet>
                    <h:outputText value="#{client.mode}" />
                </rich:column>
                <rich:column sortBy="#{client.lastContact}">
                    <f:facet name="header">
                        <h:outputText value="Last Contact" escape="false"/>
                    </f:facet>
                    <h:outputText value="#{client.lastContact}">
                    				<f:convertDateTime pattern="#{applicationScope.shortDateTimeFormat}"/>
                    </h:outputText>
                </rich:column>
                <rich:column>
                    <f:facet name="header">
                        <h:outputText value="Event Status" escape="false"/>
                    </f:facet>
                    <h:outputText value="#{client.eventStatus}" />
                </rich:column>
                <rich:column >
                    <f:facet name="header">
                        <h:outputText value="Actions" escape="false"/>
                    </f:facet>
                    <h:outputLink value="/facdash/jsp/clientEventSignals.jsf?user=#{client.parentName}&amp;fromEvents=true&amp;client=#{client.clientName}&amp;eventName=#{EventDetailDataModel.event.eventName}&amp;aggDisplayMode=reset">
                        <f:verbatim>View Signals</f:verbatim>
                    </h:outputLink>
                </rich:column>
            </rich:dataTable>
        </a4j:outputPanel>
        </h:form>
</jsp:root>