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
        <jsp:include page="/jsp/header_events.jsp" />
			<h:form id="listForm">
            <rich:spacer height="20px" width="700px" />
            <a4j:keepAlive beanName="EventListDataModel" />
            <rich:messages layout="table" globalOnly="false"
                           infoClass="global-message-info"
                           warnClass="global-message-warn"
                           errorClass="global-message-error"
                           fatalClass="global-message-fatal"
             />
            <rich:extendedDataTable  activeRowKey="status" enableContextMenu="false"  title="Active Events"
                            id="events"
                            value="#{EventListDataModel.events}" 
                            var="event"
                            styleClass="content-table-narrow"
                            width="900"
                            >

                <f:facet name="header4">
                    <f:facet name="header">
                        <rich:columnGroup>
                            <rich:column
                                style="text-align:left;font-size: 15px;"
                                colspan="7">
                                <h:outputText value="Active or scheduled events" />
                            </rich:column>
                            <rich:column colspan="1">
                                <a4j:commandLink oncomplete="#{rich:component('panel')}.show()"
                                                 reRender="panel" value="" rendered="#{EventListDataModel.canDeleteEvent}">
                                    <h:graphicImage value="/images/layout/delete_device.gif" />
                                </a4j:commandLink>
                            </rich:column>
                        </rich:columnGroup>
                    </f:facet>
                </f:facet>
                
                <rich:column width="10%" >
                    <f:facet name="header">
                        <a4j:region>
                            <h:outputText value="Delete" escape="false" />
                            <br />
                        </a4j:region>
                    </f:facet>
                    <h:selectBooleanCheckbox value="#{event.deleted}" title="Delete Event" disabled="#{not EventListDataModel.canDeleteEvent}" />
                </rich:column>

                    <rich:column width="25%" sortBy="#{event.eventName}"
                             sortOrder="ASCENDING">
                    <f:facet name="header">
                        <a4j:region>
                            <h:outputText value="Event Name" escape="false" />
                            <br />
                        </a4j:region>
                    </f:facet>
                    <h:outputLink value="/pss2.utility/jsp/event/eventDetail.jsf?eventName=#{event.eventName}&amp;programName=#{event.programName}">
                        <h:outputText id="name" value="#{event.eventName}" />
                    </h:outputLink>
                </rich:column>
                
                <rich:column sortBy="#{event.programName}" >
                    <f:facet name="header">
                        <a4j:region>
                            <h:outputText value="Program" escape="false" />
                            <br />
                        </a4j:region>
                    </f:facet>
                    <h:outputText id="prog" value="#{event.programName}" />
                </rich:column>

                <rich:column sortBy="#{event.issuedTime}" sortOrder="ASCENDING" >
                    <f:facet name="header">
                        <a4j:region>
                            <h:outputText value="Notification" escape="false" />
                            <br />
                        </a4j:region>
                    </f:facet>
                    <h:outputText id="notification" value="#{event.issuedTime}">
	                    <f:convertDateTime pattern="#{applicationScope.shortDateTimeFormat}"/>
                    </h:outputText>
                </rich:column>
                <rich:column sortBy="#{event.startTime}" sortOrder="ASCENDING">
                    <f:facet name="header">
                        <a4j:region>
                            <h:outputText value="Start" escape="false" />
                            <br />
                        </a4j:region>
                    </f:facet>

                    <h:outputText id="start" value="#{event.startTime}">
	                    <f:convertDateTime pattern="#{applicationScope.shortDateTimeFormat}"/>
                    </h:outputText>
                </rich:column>
                <rich:column sortBy="#{event.endTime}" sortOrder="ASCENDING">
                    <f:facet name="header">
                        <a4j:region>
                            <h:outputText value="End" escape="false" />
                            <br />
                        </a4j:region>
                    </f:facet>

                    <h:outputText id="end" value="#{event.endTime}">
                    	                <f:convertDateTime pattern="#{applicationScope.shortDateTimeFormat}"/>
                    </h:outputText>
                    
                </rich:column>
                <rich:column  >
                    <f:facet name="header">
                        <a4j:region>
                            <h:outputText value="Status" escape="false" />
                            <br />
                        </a4j:region>
                    </f:facet>

                    <h:outputText id="status" value="#{event.state}" />
                </rich:column>
                <rich:column width="100px">
                    <f:facet name="header">
                        <a4j:region>
                            <h:outputText value="Action" escape="false" />
                            <br />
                        </a4j:region>
                    </f:facet>
                    <h:panelGroup rendered="#{event.manualTerminate}">
                           <a4j:commandButton onclick="jQuery('.endEvtName').val('#{event.eventName}'); #{rich:component('end_event_dialog')}.show();return false;"
                                                 value="End Event"
                                                 disabled="#{not EventListDataModel.canDeleteEvent}" reRender="end_event_dialog,panel,listForm">
                           </a4j:commandButton>
                    </h:panelGroup>
                    <h:panelGroup rendered="#{event.demoEvent}">
                           <a4j:commandButton onclick="jQuery('.demoEvtName').val('#{event.eventName}'); #{rich:component('panel_delete_demo_event')}.show();return false;"
                                                 value="DeleteEvent"
                                                 disabled="#{not EventListDataModel.canDeleteDemoEvent}">
                                                 
                           </a4j:commandButton>
                    </h:panelGroup>
                </rich:column>
            </rich:extendedDataTable>
      </h:form>
        
        <h:outputLink value="/pss2.website/export.do?dispatch=exportEvents">
            <f:verbatim> Export to Excel </f:verbatim>
        </h:outputLink>
        
        <rich:modalPanel id="panel" autosized="false"
            keepVisualState="false" width="315" height="150">
            <h:form onsubmit="" id="deleteForm">
                <rich:panel id="messages">
                    <h:outputText value="The selected event(s) will be deleted: " />
                    <h:outputText value="#{EventListDataModel.selectedPrograms} "/>
                </rich:panel>
                <a4j:commandButton id="yes" value="Yes"
                    reRender="events"
                    disabled="#{EventListDataModel.noneSelected}"
                    action="#{EventListDataModel.eventDeleteAction}"
                    onclick="this.disabled=true"
                    oncomplete="this.disabled=false;#{rich:component('panel')}.hide();">
                </a4j:commandButton>
                <a4j:commandButton id="no" value="No"
                    oncomplete="#{rich:component('panel')}.hide();" />
            </h:form>
        </rich:modalPanel>
        
        <rich:modalPanel id="panel_delete_demo_event" autosized="false"
            keepVisualState="false" width="315" height="150">
            <h:form onsubmit="" id="deleteDemoEveForm">
            <h:inputText  styleClass="demoEvtName" value="#{EventListDataModel.demoEventNameToDelete}" style="display:none"/>
                <rich:panel id="messages">
                    <h:outputText value="The selected event(s) will be deleted: " />
                    <h:outputText value="#{EventListDataModel.selectedPrograms} "/>
                </rich:panel>
                <a4j:commandButton id="yes" value="Yes"
                    reRender="events"
                    action="#{EventListDataModel.deleteDemoEvent}"
                    onclick="this.disabled=true"
                    oncomplete="this.disabled=false;#{rich:component('panel_delete_demo_event')}.hide();">
                </a4j:commandButton>
                <a4j:commandButton id="no" value="No"
                    oncomplete="#{rich:component('panel')}.hide();" />
            </h:form>
        </rich:modalPanel>
        
      
        <rich:modalPanel id="end_event_dialog" domElementAttachment="form" keepVisualState="false" height="300" width="450">
           <f:facet name="header">
                <h:outputText styleClass="dlg-title" value="End Event"/>
            </f:facet>
            <h:form>
	            <f:facet name="controls">
	            <h:commandLink action="#{EventListDataModel.close}">
	                <h:graphicImage value="/images/close.jpg" style="cursor:pointer" onclick="Richfaces.hideModalPanel('end_event_dialog')"/>
	            </h:commandLink>
	            </f:facet>
	             <h:panelGroup layout="block" id="enddlgmain" style="overflow:auto;font-size:12px">
	              <rich:messages layout="table" globalOnly="true" id="msg-block"
	              infoClass="global-message-info" warnClass="global-message-warn"
	              errorClass="global-message-error" fatalClass="global-message-fatal"/>
	              
	              <h:inputText  styleClass="endEvtName" value="#{EventListDataModel.eventName}" style="display:none"/>
	              <h:selectBooleanCheckbox value="#{EventListDataModel.endImmeidate}" id="endImmediate" style="float:left;">
	                <a4j:support event="onchange" reRender="enddlgmain" />
	               </h:selectBooleanCheckbox>
	              <h:outputLabel for='endImmediate' style="width:auto"> Terminate Immediately</h:outputLabel>
	              <SPAN style="clear:both;margin-top:10px;width:auto">
	                <h:outputText value="Terminate at:" style="clear:both;margin-top:5px;float:left"/>
	                <span style="float:left">
	                  <rich:calendar value="#{EventListDataModel.endDate}" id="endDateCalendar" disabled="#{EventListDataModel.endImmeidate}"
	                         popup="true"
	                         showApplyButton="false" datePattern="#{applicationScope.dateFormat}" cellWidth="24px"
	                         cellHeight="22px" inputStyle="width:80px;margin-left:5px;"
	                         enableManualInput="true"
	                   />
	                  
	                   <h:selectOneMenu value="#{EventListDataModel.endHour}" disabled="#{EventListDataModel.endImmeidate}">
	                        <f:selectItems value="#{EventListDataModel.hourList}"/>
	                   </h:selectOneMenu>
	                   : 
	                   <h:selectOneMenu value="#{EventListDataModel.endMin}" disabled="#{EventListDataModel.endImmeidate}">
	                      <f:selectItems value="#{EventListDataModel.minList}"/>
	                   </h:selectOneMenu>
	                     :
	                    <h:selectOneMenu value="#{EventListDataModel.endSec}" disabled="#{EventListDataModel.endImmeidate}">
	                      <f:selectItems value="#{EventListDataModel.secList}"/>
	                    </h:selectOneMenu>
	                  </span>
	               </SPAN>
	               
	               <DIV style="clear:both;margin-top:50px;overflow:auto">
	                <a4j:commandButton id="endEventButton" style="float:right;margin:5px" action="#{EventListDataModel.endEvent}" value="End Event" 
	                  reRender="end_event_dialog,panel,listForm">
	                </a4j:commandButton>
	                
	                <a4j:commandButton id="cancelBtn" style="float:right;margin:5px" value="Cancel" action="#{EventListDataModel.reset}" ajaxSingle="true" oncomplete="Richfaces.hideModalPanel('end_event_dialog')" reRender="end_event_dialog,enddlgmain,panel,listForm">
	                </a4j:commandButton>
	                
	
	              </DIV>
	            </h:panelGroup>
              
           </h:form>
        </rich:modalPanel>
        
        <a4j:outputPanel ajaxRendered="true">
          <script>
                if(jQuery('.rich-messages-label').size()>0){
                   Richfaces.showModalPanel('end_event_dialog');
                }
          </script>
        </a4j:outputPanel>
     <jsp:include page="/jsp/footer.jsp" />
    </f:view>
</jsp:root>