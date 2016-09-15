<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
    xmlns:f="http://java.sun.com/jsf/core"
    xmlns:a4j="http://richfaces.org/a4j"
    xmlns:rich="http://richfaces.org/rich"
    xmlns:h="http://java.sun.com/jsf/html" version="2.1">
    <jsp:directive.page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />

    <h:form>
        <a4j:outputPanel  ajaxRendered="true" id="optoutPartPanel">
            <rich:dataTable
                id="optOutParticipants"
                value="#{EventDetailDataModel.allOptOutParticipantsInEvent}"
                var="item"
                width="100%"
                styleClass="event-rich-table ">

            <rich:spacer height="30px" width="700px" />
                <rich:column sortBy="#{item.participant.participantName}" sortOrder="ASCENDING">
                    <f:facet name="header">
                        <h:outputText value="Participant" escape="false"  />
                    </f:facet>
                    <h:outputText  value="#{item.participant.participantName}">
                    </h:outputText>
                </rich:column>
                <rich:column sortBy="#{item.participant.accountNumber}">
                    <f:facet name="header">
                        <h:outputText value="Account#" escape="false"/>
                    </f:facet>
                    <h:outputText value="#{item.participant.accountNumber}" />
                </rich:column>
                <rich:column sortBy="#{item.participantRegistedShed}">
                    <f:facet name="header">
                        <h:outputText value="Registered Shed (KW)" escape="false"/>
                    </f:facet>
                    <h:outputText value="#{item.participantRegistedShed}" />
                </rich:column>
                 <rich:column sortBy="#{item.participantAvilableShed}">
                    <f:facet name="header">
                        <h:outputText value="Available Shed (KW)" escape="false"/>
                    </f:facet>
                    <h:outputText value="#{item.participantAvilableShed}" />
                </rich:column>
            </rich:dataTable>
        </a4j:outputPanel>

    </h:form>
</jsp:root>