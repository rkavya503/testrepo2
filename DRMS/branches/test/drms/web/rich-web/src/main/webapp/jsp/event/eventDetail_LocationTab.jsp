<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
    xmlns:f="http://java.sun.com/jsf/core"
    xmlns:a4j="http://richfaces.org/a4j"
    xmlns:rich="http://richfaces.org/rich"
    xmlns:richext="http://akuacom.com/richext"
    xmlns:h="http://java.sun.com/jsf/html" version="2.1">
    <jsp:directive.page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />
  
  <script>
      function _selChange(wholeTable,button,canEndEvent){
        var eles =wholeTable.getElementsByClassName('checked');
        if(canEndEvent){
        if(eles.length>0){
            button.removeAttribute("disabled");
        }else{
            button.setAttribute("disabled",true);
        }
      }
    }
    
     function copyForms(){
        jQuery('#hiddenForms .rich-calendar-input').val(jQuery('.endTimeForm .rich-calendar-input').val());
        jQuery('#hiddenForms .endHour').val(jQuery('.endTimeForm .endHour').val());
        jQuery('#hiddenForms .endMin').val(jQuery('.endTimeForm .endMin').val());
        jQuery('#hiddenForms .endSec').val(jQuery('.endTimeForm .endSec').val());
      }
      
      function _endEvent(){
        jQuery('.action_end_event').click();
      }
     
           
    
  </script>
     <a4j:outputPanel  ajaxRendered="true" id="eventLocationPanel">
    <h:form id="listForm">
        <DIV id="hiddenForms" style="display:none">
                <h:commandButton action="#{EventDetailDataModel.locationModel.endEvent}" styleClass="action_end_event" />
				
                <rich:calendar value="#{EventDetailDataModel.locationModel.endDate}" id="endDateCalendar1" disabled="#{EventDetailDataModel.locationModel.endImmeidate}"
                       popup="true"
                       styleClass="endDate"
                     
                       showApplyButton="false" datePattern="#{applicationScope.dateFormat}" cellWidth="24px"
                       cellHeight="22px" inputStyle="width:80px;margin-left:5px;"
                       enableManualInput="true"
                 />
                 <h:selectOneMenu value="#{EventDetailDataModel.locationModel.endHour}" styleClass="endHour" disabled="#{EventDetailDataModel.locationModel.endImmeidate}">
                      <f:selectItems value="#{EventDetailDataModel.locationModel.hourList}"/>
                 </h:selectOneMenu>
                 <h:selectOneMenu value="#{EventDetailDataModel.locationModel.endMin}" styleClass="endMin" disabled="#{EventDetailDataModel.locationModel.endImmeidate}">
                    <f:selectItems value="#{EventDetailDataModel.locationModel.minList}"/>
                 </h:selectOneMenu>
                  <h:selectOneMenu value="#{EventDetailDataModel.locationModel.endSec}" styleClass="endSec" disabled="#{EventDetailDataModel.locationModel.endImmeidate}">
                    <f:selectItems value="#{EventDetailDataModel.locationModel.secList}"/>
                  </h:selectOneMenu>
        </DIV>
         <div style="margin:6px;overflow:auto">
        <div style="float:left">
        
         </div>
         <div style="float:left;margin-left:400px">
           <a4j:commandButton id="end-selected-btn" styleClass="endEvtBtn" disabled="#{EventDetailDataModel.canEndEventForLocation} }"
              onclick="jQuery('.endEvtName').val('#{EventDetailDataModel.locationModel.eventName}');  #{rich:component('end_event_dialog')}.show();return false;"
                  value="End Event">
            </a4j:commandButton>
            <script>
              $(jQuery('.endEvtBtn').attr('disabled','true'));
            </script>
         </div>             
        </div>
        
        <div id="locationTable" style="clear:both">
        <richext:treeTable id="loclist" value="#{EventDetailDataModel.locationModel.locationProvider}" var="item" rows="10000" 
                    height="420px" width="800px" selectionMode="multiple"
                    onSelection="_selChange($('locationTable'),#{rich:element('end-selected-btn')},#{EventDetailDataModel.canEndEventForLocation});" >
             <richext:treeColumn sortBy="#{locationNumber}"  width="20%">
                <f:facet name="header">
                    <h:outputText value="Location Number" escape="false"  />
                </f:facet>
                <h:outputText  value="#{item.locationNumber}">
                </h:outputText>
            </richext:treeColumn>
            <rich:column sortBy="#{locationName}" width="30%">
              <f:facet name="header">
                  <h:outputText value="Location Name" escape="false"/>
              </f:facet>
              <h:outputText value="#{item.locationName}" />
            </rich:column>
           <rich:column sortBy="#{locationType}" width="20%">
              <f:facet name="header">
                  <h:outputText value="Location Type" escape="false"/>
              </f:facet>
              <h:outputText value="#{item.locationType}">
              </h:outputText>
            </rich:column>
           <rich:column sortBy="#{endTime}" width="30%">
              <f:facet name="header">
                  <h:outputText value="End time" escape="false"/>
              </f:facet>
              <h:outputText value="#{item.endTime}">
                 <f:convertDateTime pattern="#{applicationScope.shortDateTimeFormat}"/>
              </h:outputText>
            </rich:column>
          </richext:treeTable>
            
          </div>
    </h:form>
    </a4j:outputPanel>
    
    <rich:modalPanel id="end_event_dialog" keepVisualState="false" height="300" width="450">
         <f:facet name="header">
              <h:outputText styleClass="dlg-title" value="End Event"/>
          </f:facet>
          <f:facet name="controls">
              <h:graphicImage value="/images/close.jpg" style="cursor:pointer" onclick="Richfaces.hideModalPanel('end_event_dialog')"/>
          </f:facet>
          <h:form>
             <h:panelGroup layout="block" id="enddlgmain" style="overflow:auto;font-size:12px">
              <rich:messages layout="table" globalOnly="true" id="msg-block"
              infoClass="global-message-info" warnClass="global-message-warn"
              errorClass="global-message-error" fatalClass="global-message-fatal"/>
              
              <h:inputText  styleClass="endEvtName" value="#{EventDetailDataModel.locationModel.event.eventName}" style="display:none"/>
              <SPAN style="clear:both;margin-top:10px;width:auto">
                <h:outputText value="Terminate at:" style="clear:both;margin-top:5px;float:left"/>
                <span style="float:left" class="endTimeForm">
                  <rich:calendar value="#{EventDetailDataModel.locationModel.endDate}" id="endDateCalendar" disabled="#{EventDetailDataModel.locationModel.endImmeidate}"
                         popup="true"
                         styleClass="endDate"
                         required="true"
                         requiredMessage="end date is required"
                         showApplyButton="false" datePattern="#{applicationScope.dateFormat}" cellWidth="24px"
                         cellHeight="22px" inputStyle="width:80px;margin-left:5px;"
                         enableManualInput="true"
                   />
                   <h:selectOneMenu value="#{EventDetailDataModel.locationModel.endHour}" styleClass="endHour" disabled="#{EventDetailDataModel.locationModel.endImmeidate}">
                        <f:selectItems value="#{EventDetailDataModel.locationModel.hourList}"/>
                   </h:selectOneMenu>
                   : 
                   <h:selectOneMenu value="#{EventDetailDataModel.locationModel.endMin}" styleClass="endMin" disabled="#{EventDetailDataModel.locationModel.endImmeidate}">
                      <f:selectItems value="#{EventDetailDataModel.locationModel.minList}"/>
                   </h:selectOneMenu>
                     :
                    <h:selectOneMenu value="#{EventDetailDataModel.locationModel.endSec}" styleClass="endSec" disabled="#{EventDetailDataModel.locationModel.endImmeidate}">
                      <f:selectItems value="#{EventDetailDataModel.locationModel.secList}"/>
                    </h:selectOneMenu>
                  </span>
               </SPAN>
               
               <DIV style="clear:both;margin-top:50px;overflow:auto">
				<a4j:commandButton id="endEventButton" style="float:right;margin:5px" onclick="copyForms();Richfaces.hideModalPanel('end_event_dialog'); _endEvent();return false;" value="End Event"/>
                
                <button type="button" id="cancelBtn" style="float:right;margin:5px" onclick="Richfaces.hideModalPanel('end_event_dialog')">Cancel
                </button>
              </DIV>
          </h:panelGroup>
           </h:form>
        </rich:modalPanel>
		
</jsp:root>