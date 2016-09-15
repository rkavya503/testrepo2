<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://akuacom.com/richext" prefix="richext"%>

<rich:panel id="scriptPanel">
    <script type="text/javascript">
    var $j=jQuery.noConflict();
    
    function handleKeyPress(event){
        if(event.keyCode == 13){
            if(event.preventDefault){
                event.preventDefault();
            }else{
                event.returnValue = false;
            }
            
            if(event.stopPropagation){
                event.stopPropagation();
            }else{
                event.cancelBubble = true;
            }
            var button = document.getElementById('participant-form:apply-reject-filter');
            $j(button).click();
        }
    }
    
    function _selChange(wholeTable,button){
        var eles =wholeTable.getElementsByClassName('checked');
        if(eles.length>0){
            button.removeAttribute("disabled");
        }else{
            button.setAttribute("disabled",true);
        }
    }
    
    function showConfirm(){
        var seletedSize = 0;
        seletedSize = '<h:outputText value="#{evtAdvisor.eventModel.locationSelection.seletedLocationSize}" />';
        if(seletedSize>0){
            // show pop up
            document.getElementById('comfirmpanel').component.show();
        }
    }
</script>
</rich:panel> 
<h:form id="selected-part-form">
<a4j:status id="waitStatus2" forceId="true"
                onstart="javascript:Richfaces.showModalPanel('waitModalPanel1');"
                onstop="javascript:Richfaces.hideModalPanel('waitModalPanel1');" />
<rich:panel styleClass="wizard-title-container">
    <span class="wizard-title"> SDP - Select Dispatch Locations </span>
</rich:panel> 
<a4j:keepAlive beanName="localSelection"/>
<rich:panel styleClass="content-panel" id ="participant-selection-panel" style="margin-left:10px">    
    <richext:set beanName="localSelection" value="#{evtAdvisor.eventModel.locationSelection}"/>
    <richext:set beanName="selectFilter" value="#{localSelection.selectedProvider.filter}"/>
    <table width="920px" class="filter-table">
        <tr><td>
            <table width="100%">
                <tr>
                    <td align="left">                                                  
                        <a4j:commandButton value="Remove Selected" id="remove-selected-btn"  style="width:120px" styleClass="cmd-button"
                            action="#{localSelection.removeParticipants}" title="Remove selected locations from enrolled list"   
                            oncomplete="#{rich:element('remove-selected-btn')}.setAttribute('disabled',true);"                              
                            limitToList="true" 
                            reRender="scriptPanel,participantList,enroll-more-button,enroll-all-button"/>
                         <rich:spacer height="20px" width="20px"/>
                        <a4j:commandButton type="button" id="enroll-more-button" value="Enroll Locations" styleClass="cmd-button"
                            onclick="#{rich:component('select-participants-dlg')}.show();"
                            disabled="#{localSelection.slapAll}"
                            title="Add More Locations"                               
                            limitToList="true" reRender="participant-rejection-panel"/>            
                         <rich:spacer height="20px" width="20px"/>   
                  
                        <a4j:commandButton type="button" id="enroll-all-button" value="Enroll All" styleClass="cmd-button"
                            action="#{localSelection.addAll}" 
                            oncomplete="showConfirm()"                                                            
                            disabled="#{localSelection.slapAll}"
                            reRender="participantList, enroll-more-button,enroll-all-button,participant-rejection-panel,msg-block"
                            title="Enroll All"
                            status="waitStatus2"
                            limitToList="true"/>    
                    </td>
                </tr>
            </table>
        </td></tr>
        <tr><td align="left">
            <table width="100%">
            <tr><td id="enrolled-table-col" colspan="3">
            <richext:treeTable id="participantList" value="#{localSelection.selectedProvider}" var="item" rows="10000" 
                    height="320px" width="100%" selectionMode="multiple"
                    onSelection="_selChange($('enrolled-table-col'),#{rich:element('remove-selected-btn')});">
                <richext:treeColumn width="20px">
                </richext:treeColumn>                
                <rich:column id="name" >
                        <f:facet name="header">
                               <h:outputText value="Location Name" escape="false"/>
                           </f:facet>               
                        <h:outputText value="#{item.name}"></h:outputText>
                </rich:column>
                <rich:column id="account" width="45%">
                        <f:facet name="header">
                               <h:outputText value="Location#" escape="false"/>
                           </f:facet>               
                        <h:outputText value="#{item.number}"></h:outputText>
                </rich:column>
            </richext:treeTable>
            </td></tr>
            </table>
        </td></tr>
    </table>
</rich:panel>
    <jsp:include page="buttonsOnPartSelPage.jsp"/>
</h:form>
<a4j:region>
    <rich:modalPanel id="select-participants-dlg" keepVisualState="false" height="520" width="770">
        <f:facet name="header">
                <h:outputText styleClass="dlg-title" value="Enroll Dispatch Locations"/>
        </f:facet>
        <f:facet name="controls">
            <h:graphicImage value="/images/close.jpg" style="cursor:pointer" onclick="Richfaces.hideModalPanel('select-participants-dlg')"/>
        </f:facet>
        <h:form  id="participant-form"> 
        
        <a4j:status id="waitStatus1" forceId="true"
                onstart="javascript:Richfaces.showModalPanel('waitModalPanel1');"
                onstop="javascript:Richfaces.hideModalPanel('waitModalPanel1');" />
        
        
        <rich:panel styleClass="content-panel" id ="participant-rejection-panel">
        <table width="720px" class="filter-table">
            <tr><td>                
                
                <h:panelGrid styleClass="filter-form-block" columns="3" id="reject-filter-block" style="width:100%">
                    <richext:set beanName="rejectFilter" value="#{localSelection.rejectedProvider.filter}"/>
                    <!-- first row -->
                    <h:panelGroup >
                        <h:graphicImage id="requiredRates" alt="required" title="required"  url="/images/layout/required.jpg" />
                        <h:outputText style="text-align: left;font-size:11px"   value="Dispatch Type:"/>                            
                    </h:panelGroup>  
                    <h:panelGroup >
                     <a4j:region>
                        <h:selectOneRadio value="#{rejectFilter.dispatchBy}" styleClass="selfDefinedFontRadio" id="dispatchType" >
                            <a4j:support  event="onclick" action="#{localSelection.locationValueChangeListener}"
                             reRender="reject-filter-block, rejectedParticipantList"  status="waitStatus1"/>
                            <f:selectItems value="#{rejectFilter.dispatchByList}"/>
                        </h:selectOneRadio>
                     </a4j:region>
                    </h:panelGroup>  
                    <h:panelGroup >
                    </h:panelGroup>
                    <!-- row for shed -->
                    <h:outputText style="text-align: left;font-size:11px"   value="Filter by:"/>    
                    <h:selectOneRadio value="#{rejectFilter.filterBy}" styleClass="selfDefinedFontRadio" id="filterType" >
                    <a4j:support  event="onchange" reRender="reject-filter-block"/>
                            <f:selectItems value="#{rejectFilter.filterByList}"/>
                        </h:selectOneRadio>
                    <h:panelGroup >
                        <h:inputText id="filterValue" onkeypress="handleKeyPress(event);" value="#{rejectFilter.searchByValue}" style="margin-left:10px" styleClass="form-field"  />
                        
                        <a4j:commandButton id="apply-reject-filter" value="Filter" style="width:120px"  styleClass="dlg-button"
                              action="#{localSelection.rejectedProvider.applyFilter}"
                              title="Filter locations in list"
                               status="waitStatus1"
                              limitToList="true" reRender="rejectedParticipantList"/>
                              
                        <rich:spacer height="20px" width="20px"/>  
                        
                        <a4j:commandButton id="apply-clear-filter" value="Clear" style="width:120px"  styleClass="dlg-button"
                              action="#{localSelection.rejectedProvider.clearFilter}"
                              title="Clear button for filter on search"
                               status="waitStatus1"
                              limitToList="true" reRender="reject-filter-block, rejectedParticipantList"/>
                    </h:panelGroup>  
                    
                    
                </h:panelGrid>
            </td></tr>

            <tr><td>
                <table width="100%">
                    
                    <tr>
                        <td colspan="3" id="rejected-part-col" align="right">
                            <richext:treeTable id="rejectedParticipantList" 
                                value="#{localSelection.rejectedProvider}" var="item" 
                                    rows="10000" height="300px" width="100%" selectionMode="multiple"
                                   >
                                    
                                 <richext:treeColumn>
                                   
                                </richext:treeColumn>
                                <rich:column sortBy="#{name}" sortable="false" width="50%">
                                    <f:facet name="header">
                                           <h:outputText value="Location Name" escape="false"/>
                                       </f:facet>               
                                    <h:outputText/>
                                    <h:outputText value="#{item.name}"></h:outputText>
                                </rich:column>
                                <rich:column sortBy="#{number}" sortable="false" width="45%">
                                    <f:facet name="header">
                                       <h:outputText value="Location#" escape="false"/>
                                   </f:facet>               
                                    <h:outputText/>
                                    <h:outputText value="#{item.number}"></h:outputText>
                                </rich:column>
                            
                            </richext:treeTable>
                        </td>
                    </tr>
                </table>
            <td></tr>
            <tr>
                <td align="right">
                <a4j:commandButton value="Enroll & Close" id="enroll-close-btn"
                                title="add checked locations to selection list and close current dialog"
                                styleClass="dlg-button"
                                action="#{localSelection.addParticipants}"  style="width:100px"
                                limitToList="true"
                                status="waitStatus1"
                                oncomplete="#{rich:component('select-participants-dlg')}.hide();"
                                reRender="scriptPanel,participant-rejection-panel,participantList,enroll-more-button,msg-block">
                            </a4j:commandButton>
                 <rich:spacer height="20px" width="20px"/>  
                <a4j:commandButton value="Enroll Selected" id="enroll-selected-btn"
                                title="add checked locations to selection list "
                                styleClass="dlg-button"
                                action="#{localSelection.addParticipants}"  style="width:100px"
                                limitToList="true"
                                status="waitStatus1"
                                reRender="scriptPanel,participant-rejection-panel,participantList,enroll-more-button,msg-block">
                            </a4j:commandButton>
                    <rich:spacer height="20px" width="20px"/>               
                 <a4j:commandButton id="part_cancel" value="Cancel"  styleClass="dlg-button" style="width:100px"
                              oncomplete="#{rich:component('select-participants-dlg')}.hide();"/>
                </td>
            </tr>
        </table>
        </rich:panel>
        
        
        </h:form> 
    </rich:modalPanel>
</a4j:region>
 <rich:modalPanel id="waitModalPanel1" autosized="true" width="200"
    height="120" moveable="false" resizeable="false">
    <f:facet name="header">
        <h:outputText value="Processing" />
    </f:facet>
    <h:outputText value="Please wait..." />
    <center>
        <h:graphicImage value="/images/ajax-loader.gif" />
    </center>
</rich:modalPanel>

<rich:modalPanel id="comfirmpanel" autosized="false"
    keepVisualState="false" width="315" height="150">
    <h:form onsubmit="" id="deleteForm">
        <rich:panel id="messages">
            <h:outputText value="Do you want to replace your current selections with All?" />
        </rich:panel>
          
        <a4j:commandButton id="yes" value="Yes"
            reRender="participantList, enroll-more-button,enroll-all-button,participant-rejection-panel" status="waitStatus2"
            action="#{localSelection.slapValueChanged}"
            oncomplete="#{rich:component('comfirmpanel')}.hide();">
        </a4j:commandButton>
        <a4j:commandButton id="no" value="No"
            oncomplete="#{rich:component('comfirmpanel')}.hide();" />
    </h:form>
</rich:modalPanel>
