<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:f="http://java.sun.com/jsf/core"
	xmlns:a4j="http://richfaces.org/a4j"
	xmlns:rich="http://richfaces.org/rich"
	xmlns:richext="http://akuacom.com/richext"
	xmlns:h="http://java.sun.com/jsf/html" version="2.1">
	<jsp:directive.page language="java"
		contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />
        <head>
              <jsp:include page="/jsp/head.jsp" />
        </head>

<f:view>
    <jsp:include page="/jsp/header_program.jsp" />
	<h:form>	
        <rich:spacer height="20px" width="700px" />
    
        <a4j:keepAlive beanName="ProgramDataModel" />
       	<rich:messages layout="table" globalOnly="false"
		               infoClass="global-message-info" warnClass="global-message-warn"
		               errorClass="global-message-error" fatalClass="global-message-fatal" />

        <richext:dataTable  title="Programs" id="programs" 
                            value="#{ProgramDataModel}" 
                            var="p"
                            styleClass="content-table-narrow"
                            width="70%">
        	<f:facet name="header4" >
                           <h:outputText  value="Programs" />
                           <f:facet name="header">
                           <rich:columnGroup>

                               <rich:column  style="text-align:left;font-size: 15px;" colspan="5">
                                    <h:outputText value="Programs" />
                               </rich:column>
                                    
                               <rich:column style="text-align:right;" colspan="2">
									<a4j:commandLink action="#{programConfigureDataModel.dispatchPage}"  disabled="#{!ProgramDataModel.programAddEnabled}" 
										rendered="#{ProgramDataModel.programAddEnabled}">
										<h:graphicImage  value="/images/layout/add_device.gif" />
										<f:param name="operationFlag" value="ADD" />					
									</a4j:commandLink>
									  <h:outputText  value="|" rendered="#{ProgramDataModel.programAddEnabled}"/>
                                    <a4j:commandLink oncomplete="#{rich:component('panel')}.show()"
                                                  reRender="panel"
                                                  value="" disabled="#{!ProgramDataModel.programAddEnabled}" 
										rendered="#{ProgramDataModel.programAddEnabled}">
                                                  <h:graphicImage value="/images/layout/delete_device.gif" />
                                    </a4j:commandLink>
                               </rich:column>
                           </rich:columnGroup>
                       </f:facet>
            </f:facet>
       <rich:column>
			<f:facet name="header">
	                    <a4j:region>
	                         <h:outputText value="Select" escape="false"  />
	                         <br />
	                    </a4j:region>
			</f:facet>
	        <h:selectBooleanCheckbox
	                  disabled="#{(!p.disabled) or (!ProgramDataModel.programAddEnabled)}"
	                  value="#{p.deleted}"
	                  title=" Program to Delete"
	                  >
	        </h:selectBooleanCheckbox>
	
	        <h:outputText rendered="false"  value="Delete" >
			</h:outputText>
   		</rich:column>
           <rich:column sortBy="#{programName}" sortable="true">
                     <f:facet name="header">
                        <a4j:region>
                         <h:outputText value="Program Name" escape="false"  />
                         <br />
                        </a4j:region>
                    </f:facet>
				<h:outputText id="name" value="#{p.programName}" />
			</rich:column>

           <rich:column sortBy="#{priority}" sortable="true">
                     <f:facet name="header">
                        <a4j:region>
                         <h:outputText value="Priority" escape="false"  />
                         <br />
                        </a4j:region>
                    </f:facet>
				<h:outputText id="priority" value="#{p.priority}" />
			</rich:column>
        <rich:column>
		<f:facet name="header">
                    <a4j:region>
                         <h:outputText value="Clone" escape="false"  />
                         <br />
                    </a4j:region>
			</f:facet>
                  <h:outputLink value="/pss2.website/program.do?dispatch=cloneEdit&amp;programName=#{p.programName}"
                  	disabled="#{!p.clonable or !ProgramDataModel.programAddEnabled}">
                      <f:verbatim> Clone  </f:verbatim>
                   </h:outputLink>
	 </rich:column>

      <rich:column rendered="#{ProgramDataModel.programAddEnabled}">
		<f:facet name="header">
                    <a4j:region >
                         <h:outputText value="Edit" escape="false"  />
                         <br />
                    </a4j:region>
				</f:facet>
                    <a4j:commandLink rendered="#{ProgramDataModel.programAddEnabled}" value="Edit" action="#{programConfigureDataModel.dispatchPage}">
						<f:param name="operationFlag" value="UPDATE" />	
						<f:param name="programName" value="#{p.programName}" />								
					</a4j:commandLink>
      </rich:column>
		   
	    <rich:column rendered="#{ProgramDataModel.operatorUser or !ProgramDataModel.programAddEnabled}">
        <f:facet name="header">
                    <a4j:region >
                         <h:outputText value="View" escape="false"  />
                         <br />
                    </a4j:region>
                </f:facet>
                    <a4j:commandLink rendered="#{ProgramDataModel.operatorUser or !ProgramDataModel.programAddEnabled}" value="View" action="#{programConfigureDataModel.dispatchPage}">
                        <f:param name="operationFlag" value="UPDATE" /> 
                        <f:param name="programName" value="#{p.programName}" />                             
                    </a4j:commandLink>
           </rich:column>
           
           <rich:column width="130">
				<f:facet name="header">
                    <a4j:region>
                         <h:outputText value="Action" escape="false"  />
                         <br />
                    </a4j:region>
				</f:facet>
				
                   <h:outputLink value="/pss2.website/dispatchEventDetail.do?dispatch=create&amp;programName=#{p.programName}" 
                   disabled="#{!p.eventCreatable or !ProgramDataModel.programAddEventEnabled}" rendered="#{p.className!='DemoProgramEJB'}">
                                         <f:verbatim> Add event  </f:verbatim>
                   </h:outputLink>
                   
                   <h:outputLink value="/pss2.website/dispatchEventDetail.do?dispatch=create&amp;programName=#{p.programName}" 
                   disabled="#{!ProgramDataModel.operatorUser and !ProgramDataModel.programAddEventEnabled}" rendered="#{p.className=='DemoProgramEJB'}">
                                         <f:verbatim> Add event  </f:verbatim>
                   </h:outputLink>
                   
                   
				   <h:outputText escape="false" value="&lt;br&gt;" rendered="#{p.programName=='BIP'}"/>
				   <h:outputLink value="/drw.admin/jsp/bip2013/historyEvt/historyEvents.jsf?programName=BIP" 
                   rendered="#{p.className=='BIPProgramEJB'}" target="_blank">
                                         <f:verbatim> View History </f:verbatim>
                   </h:outputLink>
				   
				   <h:outputText escape="false" value="&lt;br&gt;" rendered="#{p.createDRWebsiteEvent or !ProgramDataModel.programAddEnabled}"/>
                   <h:outputLink value="jsp/evtcreate/drwebsite/event.jsf?programName=#{p.programName}" rendered="#{p.createDRWebsiteEvent}" disabled="#{!ProgramDataModel.programAddEventEnabled}">
                                         <f:verbatim> Add DR Website Event  </f:verbatim>
                   </h:outputLink>
		   </rich:column>

           <rich:column width="190">
				<f:facet name="header">
                    <a4j:region>
                         <h:outputText value="Participation [P:C]" escape="false"  />
                         <br />
                    </a4j:region>
				</f:facet>
                   <h:outputLink value="participant.jsf?program=#{p.programName}" disabled="#{!p.participantViewable or !ProgramDataModel.programAddEnabled}">
                                         <f:verbatim>  Participants   </f:verbatim>
                   </h:outputLink>
                         [
                          <h:outputText  value="#{p.participantsCount}" id="pCount" />
                         ]

                          :
                    <h:outputLink value="clients.jsf?program=#{p.programName}" disabled="#{!p.participantViewable or !ProgramDataModel.programAddEnabled}">
                                         <f:verbatim>  Clients   </f:verbatim>
                    </h:outputLink>
                          [
                          <h:outputText  value="#{p.clientsCount}" id="cCount"  />
                          ]
            </rich:column>
        </richext:dataTable>
        <h:commandLink value="Export to Excel" action="#{ProgramDataModel.exportHtmlTableToExcel}" rendered="#{ProgramDataModel.programExpEnabled}"/>

	</h:form>
                
        <rich:modalPanel id="panel" autosized="false"
                       keepVisualState="false" width="315" height="150">
           <h:form onsubmit="" id="deleteForm">
               <rich:panel id="messages"  >
                   <h:outputText value="The selected program(s) will be deleted "/>
               </rich:panel>
               <a4j:commandButton id="yes" value="Yes" reRender="programs"
                                  action="#{ProgramDataModel.programDeleteAction}"
                                  oncomplete="#{rich:component('panel')}.hide();">
               </a4j:commandButton>
               <a4j:commandButton id="no" value="No"
                       oncomplete="#{rich:component('panel')}.hide();"/>
           </h:form>
       </rich:modalPanel>
   <jsp:include page="/jsp/footer.jsp" />
</f:view>
</jsp:root>