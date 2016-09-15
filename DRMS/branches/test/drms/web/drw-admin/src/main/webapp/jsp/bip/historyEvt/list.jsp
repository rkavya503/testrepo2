<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://akuacom.com/richext" prefix="richext"%>


<h:form id="confirmForm">
<a4j:keepAlive beanName="bipHistoryEvents"/>

<rich:panel styleClass="wizard-title-container">
    <span class="wizard-title">BIP - History Events(Legacy Data Before 2013)</span>
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
                          value="#{bipHistoryEvents.startTime}"  
                          datePattern="MM-d-yyyy"
                          popup="true">
                    </rich:calendar>
                    </a4j:region>
                    <h:outputText style="text-align: left;font-size:11px"   value="To:"/>
                    <a4j:region>
                    <rich:calendar id="endTime"
                          value="#{bipHistoryEvents.endTime}"  
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
                    <table>
                        <tr>
                            <td><h:outputText style="text-align: left;font-size:11px" value="Blocks : " /></td>
                            <td>
                                <h:selectManyCheckbox value="#{bipHistoryEvents.block}" id="block_radio" styleClass="selfDefinedFontRadio">
                                    <f:selectItems value="#{bipHistoryEvents.blockList}"/>
                                </h:selectManyCheckbox>
                            </td>
                        </tr>
                    </table>

                </h:panelGroup>
                 <h:panelGroup >
					<h:outputText style="text-align: left;font-size:11px"  value="Products : " />
					<h:selectOneMenu  style="padding-left:5px;border: 1px solid;" value="#{bipHistoryEvents.productFilter}">
                         <f:selectItem itemValue="ALL" itemLabel="ALL" />
						 <f:selectItem itemValue="BIP" itemLabel="BIP" />
                         <f:selectItem itemValue="BIP-15" itemLabel="BIP-15" />
						 <f:selectItem itemValue="BIP-30" itemLabel="BIP-30" />
						 <f:selectItem itemValue="I6-30" itemLabel="I6-30" />
                    </h:selectOneMenu>
                </h:panelGroup>            
                
                <h:panelGroup>
                       <a4j:commandButton id="apply-select-filter" value="Go" styleClass="cmd-button" style="width:auto;"
                              action="#{bipHistoryEvents.applyFilter}" title="filter in history events"
                              limitToList="true"
                              reRender="msg-block,confirmParticipantList"/> 
                         <h:commandButton
                                value="     Export    " title="Export Result"
                                action="#{bipHistoryEvents.exportExcel}"
                                style="border: 1px solid #2BB0FF;" />    
                </h:panelGroup>
                
                <!-- ***************************third row***************************************** -->
                
              
                </h:panelGrid>
        </td>
    </tr>
    <tr>
          <td id="enrolled-table-col">    
            
            <richext:treeTable id="confirmParticipantList" value="#{bipHistoryEvents.locationProvider}" var="item" rows="10000"  selectionMode="multiple"
                     onSelection="_selChange($('enrolled-table-col'),#{rich:element('deleteButton')});" height="380px" width="100%">
                
                <richext:treeColumn width="5px"  >
                        
                </richext:treeColumn>
                <rich:column sortBy="#{event.startTime}" width="15%" >
                        <f:facet name="header">
                               <h:outputText value="Start Date&Time" escape="false"/>
                           </f:facet>               
                        <h:outputText value="#{item.event.startTime}"><f:convertDateTime pattern="MM/dd/yyyy HH:mm:ss" /></h:outputText>
                </rich:column>
                <rich:column sortBy="#{actualEndTime}" width="15%" >
                        <f:facet name="header">
                               <h:outputText value="End Date&Time" escape="false"/>
                           </f:facet>               
                        <h:outputText value="#{item.actualEndTime!=null?item.actualEndTime:'-'}"><f:convertDateTime pattern="MM/dd/yyyy HH:mm:ss" /></h:outputText>
                </rich:column>              
                
                <rich:column  sortable="false">
                        <f:facet name="header">
                               <h:outputText value="Product" escape="false"/>
                           </f:facet>               
                        <h:outputText value="#{item.event.product}"></h:outputText>
                </rich:column>
                <rich:column sortBy="#{blockNames}"  >
                        <f:facet name="header">
                               <h:outputText value="Blocks" escape="false"/>
                           </f:facet>               
                        <h:outputText value="#{item.blockNames}"></h:outputText>
                </rich:column>
                <rich:column  width="40%">
                        <f:facet name="header">
                               <h:outputText value="Comments" escape="false"/>
                           </f:facet>               
                        <h:outputText value="#{item.event.comment}" title="#{item.event.comment}"><richext:subStringConverterTag length="50"></richext:subStringConverterTag></h:outputText>
                </rich:column>
            </richext:treeTable>
        </td>
        
    </tr>
</table>
<rich:spacer height="10px" width="50px"/>
</div>
<rich:panel styleClass="wizard-button-container" style="border-top: 1px solid #DDDDDD"> 
        <a4j:commandButton id="deleteButton" value="Delete" styleClass="wizard-btn"
            style="margin-left:15px"
            oncomplete="#{rich:component('comfirmpanel')}.show();"
            title="Delete"  
            limitToList="true" reRender="comfirmpanel,event-creation-wizard"/>
            
    <a4j:commandButton value="Exit" styleClass="wizard-btn" 
            action="#{bipHistoryEvents.doExit}" title="Exit"
            limitToList="true" reRender="event-creation-wizard"/>
</rich:panel>
</h:form>