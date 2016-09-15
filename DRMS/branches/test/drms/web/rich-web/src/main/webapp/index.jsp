<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:f="http://java.sun.com/jsf/core"
	xmlns:a4j="http://richfaces.org/a4j"
	xmlns:rich="http://richfaces.org/rich"
	xmlns:h="http://java.sun.com/jsf/html" version="2.1">
	<jsp:directive.page language="java"
		contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />
        <head>
              <jsp:include page="/jsp/head.jsp" />
        </head>

<f:view>
	<h:form>

                <jsp:include page="/jsp/header.jsp" />
                <rich:spacer height="20px" width="700px" />
                <rich:panel id="messages"  >
                             <h:outputLink value="/pss2.website/commDevDetail.do?dispatch=create">
                                                 <f:verbatim> Add |  </f:verbatim>
                             </h:outputLink>
                </rich:panel>

		<rich:dataTable id="participants" value="#{participantDataModel}"
			var="item" rows="10">

			<rich:column >
                                     <f:facet name="header">
                                	<a4j:region>
                                            <h:outputText value="Name" escape="false"  />
                                             <br />
                                            <h:inputText  id="NameFilter"
                                                         value="#{participantDataModel.name}"
                                                         >
                                                <a4j:support event="onchange"  reRender="participants"
                                                         action="#{participantDataModel.NameFilterAction}"
                                                     />
                                             </h:inputText>
                                        </a4j:region>
                                </f:facet>
				<h:outputText id="name" value="#{item.name}" />
			</rich:column>
			<rich:column>
				 <f:facet name="header">
                                	<a4j:region>
                                            <h:outputText value="Account #" escape="false"  />
                                             <br />
                                            <h:inputText  id="accountFilter"
                                                          value="#{participantDataModel.accountNumber}"
                                                         >
                                                <a4j:support event="onchange"  reRender="participants"
                                                             action="#{participantDataModel.accountFilterAction}"
                                                     />
                                             </h:inputText>
                                        </a4j:region>
                                </f:facet>
				<h:outputText  value="#{item.accountNumber}">
				</h:outputText>
			</rich:column>


                           <rich:column width="100px" >
                                    <f:facet name="header">
                                        <h:outputText value="Programs" escape="false"  />
                                    </f:facet>
                                    <h:outputText value="#{item.programsParticipation}" />
                            </rich:column>


                            <rich:column width="100px" >
                                    <f:facet name="header">
                                            <h:outputText value="Event Status" />
                                    </f:facet>
                                    <h:outputText value="#{item.eventPartcipationStatus}" />
                            </rich:column>




                               <rich:column width="80px" >
                                    <f:facet name="header">
                                            <h:outputText value="Edit" />
                                    </f:facet>
                                     <h:outputLink value="/pss2.website/commDevDetail.do?dispatch=edit&amp;userName=#{item.name}">
                                          <f:verbatim> Edit </f:verbatim>
                                    </h:outputLink>

                            </rich:column>


                              <rich:column width="80px" >
                                     <f:facet name="header">
                                            <h:outputText value="Delete"  />
                                    </f:facet>
                                    <a4j:commandLink oncomplete="#{rich:component('panel')}.show()"
                                    reRender="panel" value="Delete">
                                    <f:setPropertyActionListener  value="#{item.name}"
                                      target="#{participantDataModel.selectedParticipant}"  />
                                    </a4j:commandLink>
                             </rich:column>

                               <rich:column width="80px" >
                                    <f:facet name="header">
                                            <h:outputText value="Dashboard" />
                                    </f:facet>
                                     <h:outputLink value="/facdash/jsp/clients.jsf?user=#{item.name}&amp;uid=#{param['uid']}">
                                          <f:verbatim> # </f:verbatim>
                                    </h:outputLink>
                              </rich:column>

			<f:facet name="footer">
				<rich:datascroller for="participants" maxPages="10" />
			</f:facet>




        </rich:dataTable>
</h:form>
    <rich:modalPanel id="panel" autosized="false"
                            keepVisualState="false" width="315" height="150">
            <h:form id="deleteForm">
                    <rich:panel id="messages"  >
                        <h:outputText value="Are you sure you want to delete Participant: "/>
                        <h:outputText value="#{participantDataModel.selectedParticipant} ">
                        </h:outputText>
                    </rich:panel>
                    <a4j:commandButton  id="yes" value="Yes"
                                       action="#{participantDataModel.deleteAction}"
                                       oncomplete="#{rich:component('panel')}.hide();">


                    </a4j:commandButton>
                    <a4j:commandButton id="no" value="No"
                            oncomplete="#{rich:component('panel')}.hide();"/>
             </h:form>
         </rich:modalPanel>
       <jsp:include page="/jsp/footer.jsp" />

</f:view>
</jsp:root>