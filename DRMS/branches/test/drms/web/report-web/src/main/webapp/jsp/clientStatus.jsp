
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://akuacom.com/richext" prefix="richext"%>

<h:panelGroup layout="block" id="client-status-block">
	<rich:tabPanel  selectedTab="#{report.clientStatus.selectedTab}" switchType="ajax" >
		<rich:tab  labelWidth="140px" status ="waitStatus"  label="Client Offline Statistics" id="clientOfflineStatisticsTab"
			oncomplete="$('ss-tab-header').focus();">
			<f:facet name="label">
				<h:outputText  escape="false" 
					value="<h3><a id='ss-tab-header' tabindex='0' onkeydown='if((window.event|| event).keyCode==13) {_findClickHandler(this)()};'>Client Offline Statistics<span/></a></h3>"/>
			</f:facet>
			<jsp:include page="offlineStatistics.jsp" />   
		</rich:tab>
		<rich:tab labelWidth="140px" status ="waitStatus"  label="Client Offline Instance" id="clientOfflineInstanceTab"
			oncomplete="$('ins-tab-header').focus();">
			<f:facet name="label">
				<h:outputText  escape="false" 
					value="<h3><a id='ins-tab-header' tabindex='0' onkeydown='if((window.event|| event).keyCode==13) {_findClickHandler(this)()};'>Client Offline Instance<span/></a></h3>"/>
			</f:facet>
			<jsp:include page="offlineInstance.jsp" />
		</rich:tab>
	</rich:tabPanel>
</h:panelGroup>


