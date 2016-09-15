<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://akuacom.com/richext" prefix="richext"%>


<h:form id="confirmForm">
<a4j:keepAlive beanName="bip2013HistoryEvents"/>

<rich:panel styleClass="wizard-title-container">
    <span class="wizard-title">BIP - History Events</span>
</rich:panel> 
<div  style="margin-left:22px">

<rich:spacer height="10px" width="50px"/>
<table width="900px">
    <tr>
        <td>
                <h:panelGrid styleClass="filter-form-block" columns="3" id="reject-filter-block" style="width:100%">
                
                <h:panelGroup>
                    <h:graphicImage id="requiredName" alt="required" title="required"  url="/images/layout/required.jpg" />
                    <h:outputText style="text-align: left;font-size:11px"   value="From:"/>
                    <a4j:region>
                    <rich:calendar id="startTime"
                          value="#{bip2013HistoryEvents.startTime}"  
                          datePattern="MM-d-yyyy"
                          popup="true">
                    </rich:calendar>
                    </a4j:region>
                    <h:outputText style="text-align: left;font-size:11px"   value="To:"/>
                    <a4j:region>
                    <rich:calendar id="endTime"
                          value="#{bip2013HistoryEvents.endTime}"  
                          datePattern="MM-d-yyyy"
                          popup="true">
                    </rich:calendar>
                    </a4j:region>     
                </h:panelGroup>  
                <h:panelGroup >
                   
                 </h:panelGroup> 
                <h:panelGroup >
                   
                 </h:panelGroup> 
                
                <!-- ***************************second row***************************************** -->             
                
                <h:panelGroup >
                    <h:outputText style="text-align: left;font-size:11px" value="Dispatch Location  : " />
                    <h:inputText  value="#{bip2013HistoryEvents.locationName}" style="border: 1px solid;" />
                </h:panelGroup>
                
                <h:panelGroup >
					<h:outputText style="text-align: left;font-size:11px"  value="Dispatch Types : " />
					<h:selectOneMenu  style="padding-left:5px;border: 1px solid;" value="#{bip2013HistoryEvents.dispatchTypeFilter}">
                         <f:selectItem itemValue="ALL" itemLabel="ALL" />
						 <f:selectItem itemValue="ABank" itemLabel="ABank" />
                         <f:selectItem itemValue="District" itemLabel="District" />
						 <f:selectItem itemValue="SLAP" itemLabel="SLAP" />
						 <f:selectItem itemValue="Substation" itemLabel="Substation" />
                    </h:selectOneMenu>
                </h:panelGroup>
                
                <h:panelGroup>
                       <a4j:commandButton id="apply-select-filter" value="Go" styleClass="cmd-button" style="width:auto;"
                              action="#{bip2013HistoryEvents.applyFilter}" title="filter in history events"
                              limitToList="true"
                              reRender="msg-block,confirmParticipantList"/> 
                         <h:commandButton
                                value="     Export    " title="Export Result"
                                action="#{bip2013HistoryEvents.exportExcel}"
                                style="border: 1px solid #2BB0FF;" />    
                </h:panelGroup>
                
                <!-- ***************************third row***************************************** -->
                
              
                </h:panelGrid>
        </td>
    </tr>
    <tr>
        <td id="enrolled-table-col">            
            <richext:treeTable id="confirmParticipantList" value="#{bip2013HistoryEvents.locationProvider}" var="item" rows="10000"  selectionMode="multiple"
                    onSelection="_selChange($('enrolled-table-col'),#{rich:element('deleteButton')});" height="380px" width="100%">
                
                <richext:treeColumn width="1px">
                </richext:treeColumn>
                 <rich:column sortBy="#{event.issuedTime}" width="110px" >
                        <f:facet name="header">
                               <h:outputText value="Issue Date&Time" escape="false"/>
                           </f:facet>               
                        <h:outputText value="#{item.event.issuedTime}">
                            <f:convertDateTime pattern="MM/dd/yyyy HH:mm:ss" />
                        </h:outputText>
                </rich:column>
                <rich:column sortBy="#{event.startTime}" width="110px" >
                        <f:facet name="header">
                               <h:outputText value="Start Date&Time" escape="false"/>
                           </f:facet>               
                        <h:outputText value="#{item.event.startTime}">
                            <f:convertDateTime pattern="MM/dd/yyyy HH:mm:ss" />
                        </h:outputText>
                </rich:column>
                <rich:column sortBy="#{actualEndTime}" width="110px" >
                        <f:facet name="header">
                               <h:outputText value="End Date&Time" escape="false"/>
                           </f:facet>               
                        <h:outputText value="#{item.actualEndTime!=null?item.actualEndTime:'-'}">
                            <f:convertDateTime pattern="MM/dd/yyyy HH:mm:ss" />
                        </h:outputText>
                </rich:column>
                <rich:column width="60px"  >
                        <f:facet name="header">
                               <h:outputText value="Dispatch<br/>Type" escape="false"/>
                           </f:facet>               
                        <h:outputText value="#{item.allLocationType==null?item.location.type:item.allLocationType}"></h:outputText>
                </rich:column>
                <rich:column width="65px" >
                        <f:facet name="header">
                               <h:outputText value="Location#" escape="false"/>
                           </f:facet>               
                        <h:outputText value="#{item.allLocationType==null?item.location.number:'-'}" title="#{item.allLocationType==null?item.location.number:'-'}" />                                                    
                </rich:column>
                <rich:column width="100px"  >
                        <f:facet name="header">
                               <h:outputText value="Dispatch Location" escape="false"/>
                           </f:facet>               
                        <h:outputText value="#{item.allLocationType==null?item.location.name:'All'}" title="#{item.allLocationType==null?item.location.name:'All'}">
                            <richext:subStringConverterTag length="20"></richext:subStringConverterTag>
                         </h:outputText>
                </rich:column>
                <rich:column  width="340px" >
                        <f:facet name="header">
                               <h:outputText value="Comments" escape="false"/>
                           </f:facet>               
                        <h:outputText value="#{item.event.comment}" title="#{item.event.comment}"><richext:subStringConverterTag length="80"></richext:subStringConverterTag></h:outputText>
                </rich:column>
            </richext:treeTable>
        </td>
        
    </tr>
</table>
<rich:spacer height="10px" width="50px"/>
</div>
<rich:panel styleClass="wizard-button-container" style="border-top: 1px solid #DDDDDD" > 
    <a4j:commandButton id="deleteButton" value="Delete" styleClass="wizard-btn"
            style="margin-left:15px"
            oncomplete="#{rich:component('comfirmpanel')}.show();"
            title="Delete"  
            limitToList="true" reRender="comfirmpanel,event-creation-wizard"/>
            
    <a4j:commandButton value="Exit" styleClass="wizard-btn" 
            action="#{bip2013HistoryEvents.doExit}" title="Exit"
            limitToList="true" reRender="event-creation-wizard"/>
</rich:panel>
</h:form>