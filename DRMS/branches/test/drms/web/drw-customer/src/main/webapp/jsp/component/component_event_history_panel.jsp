<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://akuacom.com/richext" prefix="richext"%>
<script type="text/javascript">

function searchComplete(){

    var obj = document.getElementById("eventHistoryForm:message").firstChild.firstChild.firstChild.firstChild;
    if(obj){
        obj.focus();
    }
    
}
</script>
<h:form id="eventHistoryForm">
	<a4j:poll id="poll" interval="30000" reRender="poll,outputText_systemTime"/>
	<a4j:keepAlive beanName="drFrame" />
	<a4j:region>	
	<h:panelGroup id = "historyMainPanel" layout="block" >
		 <div class="ConventEventLeft">
          
          <div class="EventResultWrapper" style="clear:both">
            <div class="EventResultTitleBg">
              <div class="EventResultTitle"><h:outputText id="outputText_systemTime" value="Updated: #{drFrame.currentSystemTime}"/></div>
            </div>
            <div class="EventResultContDiv2">
              <div style="margin-left:20px; margin-bottom:30px; font-size:12px;"><h:outputText value="#{drTextContext.copyrightHistoryEventSearchTitle}" /></div>
              <div style="margin-left:20px; margin-right:15px; margin-bottom:15px;">
              	<div class="searchTitleStyle">
                  <h:outputLabel value="Program" for="selectOneMenu_program"/>
                </div>
				<h:selectOneMenu 	styleClass="selectDropdown" style="width:460px;"
									title="program selection"
									id="selectOneMenu_program"
									value="#{drFrame.historyUIBackingBean.currentSelectProgram}"
									onkeydown="if((event || window.event).keyCode==13) {this.onchange();return false}"
									valueChangeListener="#{drFrame.historyUIBackingBean.programNameChange}">                     
									<f:selectItems value="#{drFrame.historyUIBackingBean.programItems}"/> 
									<a4j:support event="onchange" reRender="selectOneMenu_product,zipcodePanelGroup" ajaxSingle="true" limitToList="true"/> 
				</h:selectOneMenu>
				
              </div>
              <div style="float:left; margin-left:20px; margin-right:15px;">
                <div class="searchTitleStyle">
					<h:outputLabel value="Product Type" for="selectOneMenu_product"/>
				</div>
				
				<h:selectOneMenu 	styleClass="selectDropdown"
									title="product type selection"
									id="selectOneMenu_product" 
									value="#{drFrame.historyUIBackingBean.currentSelectProduct}">                     
									<f:selectItems value="#{drFrame.historyUIBackingBean.productItems}"/> 
				</h:selectOneMenu> 
              </div>
			  <h:panelGroup layout="block" id="zipcodePanelGroup">
              <div style="float:left;">
                <div class="searchTitleStyle">
                   <h:outputLabel value="Search ZIP Code" for="inputText_zipcode" rendered="#{drFrame.historyUIBackingBean.zipcodeFlag}"/> 
                </div>
				<h:inputText value="#{drFrame.historyUIBackingBean.zipCode}" id="inputText_zipcode" styleClass="zipTextBox"  rendered="#{drFrame.historyUIBackingBean.zipcodeFlag}"/>
              </div>
			  </h:panelGroup>
              <div class="clearfix"></div>
              <div style="clear:both; margin:10px 10px 10px 20px;">
              	<div class="searchTitleStyle">
                    Date Range
                </div>
                
				<div style="float:left; margin-right:10px;">					
					<jsp:include page="component_event_history_calendar.jsp" />
				</div>
				<a4j:commandButton  style="float:left;vertical-align:middle;" 
                                title="Search" 
                                alt="Search" 
                                action ="#{drFrame.historyUIBackingBean.searchHistoryEvent}" 
                                image="images/buttons/search.jpg"
                                reRender="contentPanel,warningPanel"
                                ignoreDupResponses="true"
                                limitToList="true"
                                requestDelay="200"
                                oncomplete="searchComplete()">
            
                 </a4j:commandButton>
				 
				 <a4j:commandButton id="clearButton"  
									reRender="historyMainPanel" 
									style="float:none;vertical-align:middle;padding-left: 10px;" 
									title="Clear All" 
									alt="Clear All" 
									action ="#{drFrame.historyUIBackingBean.clearAll}" 
									image="images/buttons/clearAll2.jpg">
				</a4j:commandButton>
              </div>
              <div class="clearfix" style="padding-bottom:20px;"></div>
			  
			  <div>
			  <h:panelGroup id = "warningPanel" layout="block" >
			  <h:panelGroup layout="block" rendered="#{drFrame.historyUIBackingBean.historyResultsValidateErrorFlag}"> 
					<div class="evtsHistryNotice">
						<div class="evtsHistryNoticebox_upper"></div>    
						<div class="evtsHistryNoticebox">
							
							<h:messages 	id="message" 
										globalOnly="false"
										layout="table"
										infoClass="global-message-info" 
										warnClass="global-message-warn" 
										errorClass="global-message-error" 
										fatalClass="global-message-fatal"/>
							
						</div> 
						<div class="evtsHistryNoticebox_lower"></div>
					</div> 
				</h:panelGroup>	
				</h:panelGroup>
				</div>
				<h:panelGroup id = "contentPanel" layout="block" >
					<h:panelGroup layout="block" id="eventHistoryResultsPanelGroup" rendered="#{drFrame.historyUIBackingBean.historyResultsVisibleFlag}">
						<jsp:include page="component_event_history_RTP.jsp" />	
						<jsp:include page="component_event_history_General.jsp" />					  
					</h:panelGroup>	
				</h:panelGroup>	
				
				
				
              <div class="clearfix"></div>
            </div>
            <div class="EventResultFooterBg"></div>
          </div>
        </div>
	</h:panelGroup>	
	</a4j:region>
</h:form>		

 
 