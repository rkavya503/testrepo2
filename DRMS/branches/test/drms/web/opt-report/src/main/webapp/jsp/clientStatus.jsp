<!doctype html public "-//w3c//dtd html 4.0 transitional//en">
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://akuacom.com/richext" prefix="richext"%>

<h:panelGroup layout="block" id="client-status-block">
	<rich:tabPanel  selectedTab="#{report.clientStatus.selectedTab}" switchType="ajax" >
		<rich:tab  labelWidth="140px" status ="waitStatus"  label="Client Offline Statistics" id="clientOfflineStatisticsTab">
			<jsp:include page="offlineStatistics.jsp" />   
		</rich:tab>
		<rich:tab labelWidth="140px" status ="waitStatus"  label="Client Offline Instance" id="clientOfflineInstanceTab">
			<jsp:include page="offlineInstance.jsp" />
		</rich:tab>
	</rich:tabPanel>
	
</h:panelGroup>

