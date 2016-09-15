<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
    xmlns:f="http://java.sun.com/jsf/core"
    xmlns:a4j="http://richfaces.org/a4j"
    xmlns:rich="http://richfaces.org/rich"
    xmlns:h="http://java.sun.com/jsf/html" version="2.1">
    <jsp:directive.page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />

    <h:form>
        <a4j:outputPanel  ajaxRendered="true" id="tablePrentPanel">
            <rich:dataTable
                id="participants"
                value="#{EventDetailDataModel.allParticipantsInEvent}"
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
                    <h:outputText value="#{item.participantRegistedShed}">
                        <f:convertNumber pattern="#0.0"/>
                    </h:outputText>
                </rich:column>
                 <rich:column sortBy="#{item.participantAvilableShed}">
                    <f:facet name="header">
                        <h:outputText value="Available Shed (KW)" escape="false"/>
                    </f:facet>
                    <h:outputText value="#{item.participantAvilableShed}">
                        <f:convertNumber pattern="#0.0"/>
                    </h:outputText>
                </rich:column>
                 <rich:column rendered="#{EventDetailDataModel.eventOptIn}">
                    <f:facet name="header">
                        <h:outputText value="Action" escape="false"/>
                    </f:facet>
					<a4j:commandButton oncomplete="#{rich:component('panel')}.show();"
									   reRender="panel,tablePrentPanel" value="Opt-out"
									   rendered="true"
									   disabled="#{!EventDetailDataModel.canParticipantOptOutOfEvent}">
						<f:setPropertyActionListener
								value="#{item}"
								target="#{EventDetailDataModel.optoutPart}"/>
					</a4j:commandButton>
                </rich:column>
            </rich:dataTable>
            
        </a4j:outputPanel>
    </h:form>

    <rich:modalPanel id="panel" autosized="false"
                     keepVisualState="false" width="315" height="150">
        <h:form id="optoutForm">
         	<a id="dialogTitle" href="javascript:void(0);" style="font-weight: bold;">Confirmation dialog</a>
            <rich:panel id="messages">
             
             <a id="confirmBox" href="javascript:void(0);">
                 <h:outputText value="Are you sure you want to opt-out participant: "/>
                 <h:outputText value="#{EventDetailDataModel.optoutPart.participant.participantName} ">
                 </h:outputText>
             </a>
            </rich:panel>
			<table border="0">
				<tr>
					<td>
						<h3>
							<a4j:commandButton reRender="tablePrentPanel"
											   id="yes" value="Yes" action="#{EventDetailDataModel.optOutAction}"
											   oncomplete="#{rich:component('panel')}.hide();">
							</a4j:commandButton>
						</h3>
					</td>
					<td>
						<h3>
							<a4j:commandButton id="no" value="No" oncomplete="#{rich:component('panel')}.hide();"/>
						</h3>
					</td>
				</tr>
			</table>				   
        </h:form>
    </rich:modalPanel>

</jsp:root>