<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
  <a4j:region >
  <script type="text/javascript">
	//<![CDATA[
      A4J.AJAX.onExpired = function(loc, expiredMsg){
        if(window.confirm("Due to inactivity, your session has timed out. Please log in again.")){
            loc = "../pss2.website/Login";
            return loc
            } else {
             return false;
            }
        }
		
	//]]>
  </script>
 </a4j:region>

 <jsp:include page="/jsp/logo.jsp" />
        
<%-- Aggregation Tree BEG --%>
 <rich:modalPanel   id="aggModelPanel" keepVisualState="false" height="550" width="366">
   <f:facet name="header">
       <h:outputText value="Aggregated Participants" />
   </f:facet>
   
   <h:form id="aggForm">
       <rich:panel style="width:340px;height:420px;overflow:auto;display:block">
       <f:facet name="header">
           <h:outputText value="Select a Program Participant" />
       </f:facet>
           <rich:tree id="aggTreeView"
                      value="#{aggTree.multiTreeNode}" var="aggNode" 
                      ajaxSubmitSelection="true"
                      reRender="selectedParticipant,switch"
                      nodeSelectListener="#{aggTree.processSelection}">
                       
               <rich:treeNode acceptedTypes="#{aggNode}" >
                   <h:outputText value="#{aggNode}" />
               </rich:treeNode>
           </rich:tree>
       </rich:panel>
       <rich:spacer height="10px" />
       <h:panelGrid columns="2">
       <a4j:commandButton id="cancel" value="Cancel" 
                          oncomplete="#{rich:component('aggModelPanel')}.hide();"/>
       <a4j:commandButton id="switch" value="Switch" 
                          action="#{header1.navigateAction}"
                          immediate="true"
                          disabled="#{'' ==  aggTree.selectedParticipantName}"
                          oncomplete="#{rich:component('aggModelPanel')}.hide();">
         <a4j:actionparam name="switchName" 
                          value="#{aggTree.selectedParticipantName}" 
                          assignTo="#{header1.switchingParticipant}"/>
         <a4j:actionparam name="switchProgram"
                          value="#{aggTree.parentProgram}"
                          assignTo="#{header1.swtichingProgram}"
                          />
       </a4j:commandButton>
       </h:panelGrid>
       <rich:spacer height="10px" />
       <h:outputText value="Switch To:"/>
       <rich:spacer width="5px" />
       <h:outputText escape="false" 
                     value="#{aggTree.aggDisplay}" 
                     style="font-weight:bold"
                     id="selectedParticipant" />
   </h:form>
 </rich:modalPanel>
 <%-- Aggregation Tree END --%>
