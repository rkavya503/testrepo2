<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://akuacom.com/richext" prefix="richext"%>
<h:panelGroup id="listViewLegendPanel" layout="block" style="disabled:true">
      <div class="sideHeaderBg">Legend</div>
      <div class="sideMiddleBg">
      <div class="sideFilterItem">
            <div class="sideFCheckbox">
			<h:panelGroup layout="block" rendered="#{drListView.displayActiveFlag}">
                <h:selectBooleanCheckbox value="#{drListView.activeEventLegend.sdprSelected}" id="sdp-r"  >
                    <a4j:support event="onclick" onsubmit="checkAllHandler(this,'form:sdprLegendPanelGroup'),disableAllCheckBox()" oncomplete="eventPanelDisplay('form:SDPRPanel',this);enableAllCheckBox();searchCompleteAction();" action="#{drListView.checkBoxChangeListener}" reRender="SDPRRefreshPanel"/>
                </h:selectBooleanCheckbox>
			</h:panelGroup>
			<h:panelGroup layout="block" rendered="#{!drListView.displayActiveFlag}">
                <h:selectBooleanCheckbox value="#{drListView.scheEventLegend.sdprSelected}" id="sdp-rs"  >
                    <a4j:support event="onclick" onsubmit="checkAllHandler(this,'form:sdprLegendPanelGroup'),disableAllCheckBox()" oncomplete="eventPanelDisplay('form:SDPRPanel',this);enableAllCheckBox();searchCompleteAction();" action="#{drListView.checkBoxChangeListener}" reRender="SDPRRefreshPanel"/>
                </h:selectBooleanCheckbox>
			</h:panelGroup>				
            </div>

          <div class="sicon"><img src="images/icons/sV2.gif" alt="Summer discount plan for residential" align="middle" /></div>
          <div class="sFtext">Summer Discount Plan (SDP) - Residential</div>
        </div>
		<div class="activeCheckBoxesHolderList">
		<h:panelGroup id="sdprLegendPanelGroup" layout="block">	
			<h:panelGroup id="SDPREventsLegendPanel" layout="block" rendered="#{drListView.displayActiveFlag}">
				<rich:dataList var="event" value="#{drListView.activeEventLegend.sdprItems}" rows="100" style="list-style-type:none;">
					<h:selectBooleanCheckbox value="#{event.selected}" id="sdp-rdetail" styleClass="mapform:sdp-r" style="vertical-align: middle; margin: 0px; margin-right:10px;">
						<a4j:support event="onclick" onsubmit="checkEventHandler(this,'form:sdp-r','form:SDPREventsLegendPanel');disableAllCheckBox()" oncomplete="enableAllCheckBox();searchCompleteAction();eventPanelDisplayByID('form:SDPRPanel','form:sdp-r')"  reRender="SDPRRefreshPanel">
								</a4j:support>						
					</h:selectBooleanCheckbox>
					<h:inputHidden id="sdp-rdetail-value" value="#{event.eventKey}" />
					<h:outputText  value="#{event.label}"></h:outputText>
				</rich:dataList>
			</h:panelGroup>
			
			
			<h:panelGroup id="scheSDPREventsLegendPanel" layout="block" rendered="#{!drListView.displayActiveFlag}" >
				<rich:dataList var="event" value="#{drListView.scheEventLegend.sdprItems}" rows="100" style="list-style-type:none;">
					<h:selectBooleanCheckbox value="#{event.selected}" id="sdp-rdetail" styleClass="mapform:sdp-r" style="vertical-align: middle; margin: 0px; margin-right:10px;">
						<a4j:support event="onclick" onsubmit="checkEventHandler(this,'form:sdp-rs','form:scheSDPREventsLegendPanel');disableAllCheckBox()" oncomplete="enableAllCheckBox();searchCompleteAction();eventPanelDisplayByID('form:SDPRPanel','form:sdp-rs')"  reRender="SDPRRefreshPanel">
								</a4j:support>	
					</h:selectBooleanCheckbox>
					<h:inputHidden id="sdp-rdetail-value" value="#{event.eventKey}" />
					<h:outputText  value="#{event.label}"></h:outputText>
				</rich:dataList>
			</h:panelGroup>
		</h:panelGroup>		
        </div>
        <!--  -->
		
		<!-- SDPC LEGEND-->
        <div class="sideFilterItem">
            <div class="sideFCheckbox">
			<h:panelGroup layout="block" rendered="#{drListView.displayActiveFlag}">
                <h:selectBooleanCheckbox value="#{drListView.activeEventLegend.sdpcSelected}" id="sdp-c" >
                    <a4j:support event="onclick" onsubmit="checkAllHandler(this,'form:sdpcLegendPanelGroup'),disableAllCheckBox()" oncomplete="eventPanelDisplay('form:SDPCPanel',this);enableAllCheckBox();searchCompleteAction();" action="#{drListView.checkBoxChangeListener}" reRender="SDPCRefreshPanel"/>
                </h:selectBooleanCheckbox>
			</h:panelGroup>
			<h:panelGroup layout="block" rendered="#{!drListView.displayActiveFlag}">
                <h:selectBooleanCheckbox value="#{drListView.scheEventLegend.sdpcSelected}" id="sdp-cs" >
                    <a4j:support event="onclick" onsubmit="checkAllHandler(this,'form:sdpcLegendPanelGroup'),disableAllCheckBox()" oncomplete="eventPanelDisplay('form:SDPCPanel',this);enableAllCheckBox();searchCompleteAction();" action="#{drListView.checkBoxChangeListener}" reRender="SDPCRefreshPanel"/>
                </h:selectBooleanCheckbox>
			</h:panelGroup>			
            </div>
          <div class="sicon"><img src="images/icons/s_p.gif" alt="Summer discount plan for commercial" align="middle" /></div>
          <div class="sFtext">Summer Discount Plan (SDP) - Commercial</div>
        </div>
		<div class="activeCheckBoxesHolderList">
		<h:panelGroup id="sdpcLegendPanelGroup" layout="block">
			<h:panelGroup id="SDPCEventsLegendPanel" layout="block" rendered="#{drListView.displayActiveFlag}">
				<rich:dataList var="event" value="#{drListView.activeEventLegend.sdpcItems}" rows="100" style="list-style-type:none;">
					<h:selectBooleanCheckbox value="#{event.selected}" id="sdp-cdetail" styleClass="mapform:sdp-c" style="vertical-align: middle; margin: 0px; margin-right:10px;">
						<a4j:support event="onclick" onsubmit="checkEventHandler(this,'form:sdp-c','form:sdpcLegendPanelGroup');disableAllCheckBox()" oncomplete="enableAllCheckBox();searchCompleteAction();eventPanelDisplayByID('form:SDPCPanel','form:sdp-c')"  reRender="SDPCRefreshPanel">
								</a4j:support>
					</h:selectBooleanCheckbox>
					<h:inputHidden id="sdp-cdetail-value" value="#{event.eventKey}" />
					<h:outputText  value="#{event.label}"></h:outputText>
				</rich:dataList>
			</h:panelGroup>		
			
			<h:panelGroup id="scheSDPCEventsLegendPanel" layout="block" rendered="#{!drListView.displayActiveFlag}">
				<rich:dataList var="event" value="#{drListView.scheEventLegend.sdpcItems}" rows="100" style="list-style-type:none;">
					<h:selectBooleanCheckbox value="#{event.selected}" id="sdp-cdetail" styleClass="mapform:sdp-c" style="vertical-align: middle; margin: 0px; margin-right:10px;">
						<a4j:support event="onclick" onsubmit="checkEventHandler(this,'form:sdp-cs','form:scheSDPCEventsLegendPanel');disableAllCheckBox()" oncomplete="enableAllCheckBox();searchCompleteAction();eventPanelDisplayByID('form:SDPCPanel','form:sdp-cs')"  reRender="SDPCRefreshPanel">
								</a4j:support>	
					</h:selectBooleanCheckbox>
					<h:inputHidden id="sdp-cdetail-value" value="#{event.eventKey}" />
					<h:outputText  value="#{event.label}"></h:outputText>
				</rich:dataList>
			</h:panelGroup>		
        </h:panelGroup>	
		</div>                                                                                                               
            <div class="sideFilterItem">
              <div class="sideFCheckbox">
			  <h:panelGroup layout="block" rendered="#{drListView.displayActiveFlag}">
              <h:selectBooleanCheckbox id="api" value="#{drListView.activeEventLegend.apiSelected}" >
					<a4j:support event="onclick" onsubmit="checkAllHandler(this,'form:apiLegendPanelGroup'),disableAllCheckBox()" oncomplete="eventPanelDisplay('form:APIPanel',this);enableAllCheckBox();searchCompleteAction();" action="#{drListView.checkBoxChangeListener}" reRender="APIRefreshPanel"/>
               </h:selectBooleanCheckbox>
			   </h:panelGroup>
			   <h:panelGroup layout="block" rendered="#{!drListView.displayActiveFlag}">
              <h:selectBooleanCheckbox id="apis" value="#{drListView.scheEventLegend.apiSelected}" >
					<a4j:support event="onclick" onsubmit="checkAllHandler(this,'form:apiLegendPanelGroup'),disableAllCheckBox()" oncomplete="eventPanelDisplay('form:APIPanel',this);enableAllCheckBox();searchCompleteAction();" action="#{drListView.checkBoxChangeListener}" reRender="APIRefreshPanel"/>
               </h:selectBooleanCheckbox>
			   </h:panelGroup>
              </div>
              <div class="sicon"><img src="images/icons/a.gif" alt="Agriculture and pumping interruptible program " align="absmiddle" /></div>
              <div class="sFtext">Agricultural &amp; Pumping Interruptible Program (AP-I)</div>
            </div>
			<div class="activeCheckBoxesHolderList">
				<h:panelGroup id="apiLegendPanelGroup" layout="block">
					<h:panelGroup id="APIEventsLegendPanel" layout="block" rendered="#{drListView.displayActiveFlag}">
						<rich:dataList var="event" value="#{drListView.activeEventLegend.apiItems}" rows="100" style="list-style-type:none;">
							<h:selectBooleanCheckbox value="#{event.selected}" id="api-rdetail" styleClass="mapform:api" style="vertical-align: middle; margin: 0px; margin-right:10px;">
								<a4j:support event="onclick" onsubmit="checkEventHandler(this,'form:api','form:APIEventsLegendPanel');disableAllCheckBox()" oncomplete="enableAllCheckBox();searchCompleteAction();eventPanelDisplayByID('form:APIPanel','form:api')"  reRender="APIRefreshPanel">
								</a4j:support>	
							</h:selectBooleanCheckbox>
							<h:inputHidden id="api-rdetail-value" value="#{event.eventKey}" />
							<h:outputText  value="#{event.label}"></h:outputText>
						</rich:dataList>
					</h:panelGroup>		
					
					<h:panelGroup id="scheAPIEventsLegendPanel" layout="block" rendered="#{!drListView.displayActiveFlag}">
						<rich:dataList var="event" value="#{drListView.scheEventLegend.apiItems}" rows="100" style="list-style-type:none;">
							<h:selectBooleanCheckbox value="#{event.selected}" id="api-rdetail" styleClass="mapform:api" style="vertical-align: middle; margin: 0px; margin-right:10px;">
								<a4j:support event="onclick" onsubmit="checkEventHandler(this,'form:apis','form:scheAPIEventsLegendPanel');disableAllCheckBox()" oncomplete="enableAllCheckBox();searchCompleteAction();eventPanelDisplayByID('form:APIPanel','form:apis')"  reRender="APIRefreshPanel">
								</a4j:support>	
							</h:selectBooleanCheckbox>
							<h:inputHidden id="api-rdetail-value" value="#{event.eventKey}" />
							<h:outputText  value="#{event.label}"></h:outputText>
						</rich:dataList>
					</h:panelGroup>
				</h:panelGroup>					
			</div>           
			<!-- -->
			<div class="sideFilterItem">
              <div class="sideFCheckbox">
			  <h:panelGroup layout="block" rendered="#{drListView.displayActiveFlag}">
              <h:selectBooleanCheckbox id="cbp" value="#{drListView.activeEventLegend.cbpSelected}" >
					<a4j:support event="onclick" onsubmit="checkAllHandler(this,'form:cbpLegendPanelGroup'),disableAllCheckBox()" oncomplete="eventPanelDisplay('form:CBPPanel',this);enableAllCheckBox();searchCompleteAction();" action="#{drListView.checkBoxChangeListener}" reRender="CBPRefreshPanel"/>
               </h:selectBooleanCheckbox>
			   </h:panelGroup>
			   <h:panelGroup layout="block" rendered="#{!drListView.displayActiveFlag}">
				<h:selectBooleanCheckbox id="cbps" value="#{drListView.scheEventLegend.cbpSelected}" >
					<a4j:support event="onclick" onsubmit="checkAllHandler(this,'form:cbpLegendPanelGroup'),disableAllCheckBox()" oncomplete="eventPanelDisplay('form:CBPPanel',this);enableAllCheckBox();searchCompleteAction();" action="#{drListView.checkBoxChangeListener}" reRender="CBPRefreshPanel"/>
				</h:selectBooleanCheckbox>
			   </h:panelGroup>
              </div>
              <div class="sicon"><img src="images/icons/c.gif" alt="Capacity Bidding Program " align="absmiddle" /></div>
              <div class="sFtext">Capacity Bidding Program (CBP)</div>
            </div>
			<div class="activeCheckBoxesHolderList">
				<h:panelGroup id="cbpLegendPanelGroup" layout="block">
					<h:panelGroup id="CBPEventsLegendPanel" layout="block" rendered="#{drListView.displayActiveFlag}">
						<rich:dataList var="event" value="#{drListView.activeEventLegend.cbpItems}" rows="100" style="list-style-type:none;">
							<h:selectBooleanCheckbox value="#{event.selected}" id="cbp-rdetail" styleClass="mapform:cbp" style="vertical-align: middle; margin: 0px; margin-right:10px;">
								<a4j:support event="onclick" onsubmit="checkEventHandler(this,'form:cbp','form:CBPEventsLegendPanel');disableAllCheckBox()" oncomplete="enableAllCheckBox();searchCompleteAction();eventPanelDisplayByID('form:CBPPanel','form:cbp')"  reRender="CBPRefreshPanel">
								</a4j:support>	
							</h:selectBooleanCheckbox>
							<h:inputHidden id="api-rdetail-value" value="#{event.eventKey}" />
							<h:outputText  value="#{event.label}"></h:outputText>
						</rich:dataList>
					</h:panelGroup>		
					
					<h:panelGroup id="scheCBPEventsLegendPanel" layout="block" rendered="#{!drListView.displayActiveFlag}">
						<rich:dataList var="event" value="#{drListView.scheEventLegend.cbpItems}" rows="100" style="list-style-type:none;">
							<h:selectBooleanCheckbox value="#{event.selected}" id="cbp-rdetail" styleClass="mapform:cbp" style="vertical-align: middle; margin: 0px; margin-right:10px;">
								<a4j:support event="onclick" onsubmit="checkEventHandler(this,'form:cbps','form:scheCBPEventsLegendPanel');disableAllCheckBox()" oncomplete="enableAllCheckBox();searchCompleteAction();eventPanelDisplayByID('form:CBPPanel','form:cbps')"  reRender="CBPRefreshPanel">
								</a4j:support>	
							</h:selectBooleanCheckbox>
							<h:inputHidden id="api-rdetail-value" value="#{event.eventKey}" />
							<h:outputText  value="#{event.label}"></h:outputText>
						</rich:dataList>
					</h:panelGroup>
				</h:panelGroup>					
			</div>           
			<!-- -->
			 <div class="sideFilterItem">
              <div class="sideFCheckbox">
			  <h:panelGroup layout="block" rendered="#{drListView.displayActiveFlag}">
				<h:selectBooleanCheckbox id="bip" value="#{drListView.activeEventLegend.bipSelected}" >
					<a4j:support event="onclick" onsubmit="checkAllHandler(this,'form:bipLegendPanelGroup'),disableAllCheckBox()" oncomplete="eventPanelDisplay('form:BIPPanel',this);enableAllCheckBox();searchCompleteAction();" action="#{drListView.checkBoxChangeListener}" reRender="BIPRefreshPanel"/>
				</h:selectBooleanCheckbox>
			   </h:panelGroup>
			   <h:panelGroup layout="block" rendered="#{!drListView.displayActiveFlag}">
				<h:selectBooleanCheckbox id="bips" value="#{drListView.scheEventLegend.bipSelected}" >
					<a4j:support event="onclick" onsubmit="checkAllHandler(this,'form:bipLegendPanelGroup'),disableAllCheckBox()" oncomplete="eventPanelDisplay('form:BIPPanel',this);enableAllCheckBox();searchCompleteAction();" action="#{drListView.checkBoxChangeListener}" reRender="BIPRefreshPanel"/>
				</h:selectBooleanCheckbox>
			   </h:panelGroup>
              </div>
              <div class="sicon"><img src="images/icons/b2013.gif" alt="Time-of-Use Base Interruptible Program (TOU-BIP) " align="middle" /></div>
			  <div class="sFtext">Base Interruptible Program (BIP)</div>
            </div>
			<div class="activeCheckBoxesHolderList">
			<h:panelGroup id="bipLegendPanelGroup" layout="block">
				<h:panelGroup id="BIPEventsLegendPanel" layout="block" rendered="#{drListView.displayActiveFlag}">
					<rich:dataList var="event" value="#{drListView.activeEventLegend.bipItems}" rows="100" style="list-style-type:none;">
						<h:selectBooleanCheckbox value="#{event.selected}" id="bip-rdetail" styleClass="mapform:bip" style="vertical-align: middle; margin: 0px; margin-right:10px;">
							<a4j:support event="onclick" onsubmit="checkEventHandler(this,'form:bip','form:BIPEventsLegendPanel');disableAllCheckBox()" oncomplete="enableAllCheckBox();searchCompleteAction();eventPanelDisplayByID('form:BIPPanel','form:bip')"  reRender="BIPRefreshPanel">
								</a4j:support>
						</h:selectBooleanCheckbox>
						<h:inputHidden id="bip-rdetail-value" value="#{event.eventKey}" />
						<h:outputText  value="#{event.label}"></h:outputText>
					</rich:dataList>
				</h:panelGroup>			
				
				<h:panelGroup id="scheBIPEventsLegendPanel" layout="block" rendered="#{!drListView.displayActiveFlag}">
					<rich:dataList var="event" value="#{drListView.scheEventLegend.bipItems}" rows="100" style="list-style-type:none;">
						<h:selectBooleanCheckbox value="#{event.selected}" id="bip-rdetail" styleClass="mapform:bip" style="vertical-align: middle; margin: 0px; margin-right:10px;">
							<a4j:support event="onclick" onsubmit="checkEventHandler(this,'form:bips','form:scheBIPEventsLegendPanel');disableAllCheckBox()" oncomplete="enableAllCheckBox();searchCompleteAction();eventPanelDisplayByID('form:BIPPanel','form:bips')"  reRender="BIPRefreshPanel">
								</a4j:support>
						</h:selectBooleanCheckbox>
						<h:inputHidden id="bip-rdetail-value" value="#{event.eventKey}" />
						<h:outputText  value="#{event.label}"></h:outputText>
					</rich:dataList>
				</h:panelGroup>		
			</h:panelGroup>	
			</div>
			
            <div class="clearfix"></div>
      </div>
</h:panelGroup>	  