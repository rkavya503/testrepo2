<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
      "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<html lang="en-US" xml:lang="en-US">
<f:view>
    <head>
    <jsp:include page="head.jsp" />
    </head>
    <body>
    <h:form id="navForm">
        <jsp:include page="header.jsp" />

        <a4j:keepAlive beanName="ClientEventSignals" />
        <rich:panel>
            <f:facet name="header">
                <h:outputText value="Signal Entries for Event: #{ClientEventSignals.eventName}, Program: #{ClientEventSignals.programName}, Client: #{ClientEventSignals.participantName}" />
            </f:facet>
            <rich:dataTable id="signals"
                            value="#{ClientEventSignals.clientSignalEntries}"
                            var="signal" 
                            width="100%" 
                            styleClass="event-rich-table ">

                <rich:spacer height="30px" width="700px" />
                <rich:column sortBy="#{signal.time}" sortOrder="ASCENDING">
                    <f:facet name="header">
                        <h:outputText value="Time" escape="false"/>
                    </f:facet>
                    <h:outputText value="#{signal.time}">
                        <f:convertDateTime pattern="#{applicationScope.shortDateTimeFormat}" type="both"/>
                    </h:outputText>
                </rich:column>
                <rich:column sortBy="#{signal.parentSignal.signalDef.signalName}">
                    <f:facet name="header">
                        <h:outputText value="Signal" escape="false" />
                    </f:facet>
                    <h:outputText value="#{signal.parentSignal.signalDef.signalName}" />
                </rich:column>
                <rich:column sortBy="#{signal.valueAsString}">
                    <f:facet name="header">
                        <h:outputText value="Value" escape="false" />
                    </f:facet>
                    <h:outputText value="#{signal.valueAsString}" />
                </rich:column>
            </rich:dataTable>
        </rich:panel>
    </h:form>
    <jsp:include page="footer.jsp" />
    </body>
</f:view>
</html>
