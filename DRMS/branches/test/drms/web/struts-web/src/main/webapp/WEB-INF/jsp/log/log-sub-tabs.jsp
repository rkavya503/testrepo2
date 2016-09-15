<%@ page import="com.akuacom.pss2.util.DrasRole" %>
<%@ page import="com.akuacom.pss2.core.EJBFactory" %>
<%@ page import="com.akuacom.pss2.system.SystemManager" %>
<%@ page import="com.akuacom.pss2.system.property.PSS2Features" %>
<div id="subnav">
    <ul>
        <%
            final PSS2Features pss2features = EJBFactory.getBean(SystemManager.class).getPss2Features();
			boolean shedStrategyFlag = pss2features.isShedStrategyExportEnabled();
            request.setAttribute("pss2features", pss2features);
			request.setAttribute("shedStrategyFlag", shedStrategyFlag);
            request.setAttribute("admin", request.isUserInRole(DrasRole.Admin.toString()));
			request.setAttribute("operator", request.isUserInRole(DrasRole.Operator.toString()));
        %>
        <bean:define id="features" name="pss2features" scope="request"/>
		<bean:define id="flag" name="shedStrategyFlag" scope="request"/>
        
        <li><a href="logList.do" id="subnav_report_logs">Logs</a></li>
        <c:if test="${features.reportAccountInfoEnabled}">
        <li><a href="accountsReport.do" id="subnav_report_account">Account Info</a></li>
        </c:if>
		<c:if test="${features.reportClientInfoEnabled}">
        <li><a href="/pss2.utility/jsp/reports/clientInfo.jsf" id="subnav_report_client">Client Info</a></li>
        </c:if>
		<c:if test="${flag}">
        <li><a href="/pss2.utility/jsp/reports/clientShedStrategy.jsf" id="subnav_report_client_shed">Client Shed Strategy</a></li>
        </c:if>
        <c:if test="${features.usageEnabled}">
        <li><a href="obixOnlineReport.do" id="subnav_report_obixOnline">Obix Comms</a></li>
        </c:if>
        <c:if test="${features.SCEDBPEventAutoCreationEnabled}">
        <li><a href="dbpEventCreationReport.do" id="subnav_report_dbp_event_creation">DBP Event Creation</a></li>
        </c:if>
        <c:if test="${features.reportEMailEnabled && requestScope.admin}">
		<li><a href="emails.do" id="subnav_report_emails">Emails</a></li>
        </c:if>
        <c:if test="${features.weatherEnabled}">
            <li><a href="weatherReport.do" id="subnav_report_weather">Weather</a></li>
        </c:if>
		<li><a href="/pss2.utility/jsp/reports/clientTestEmailReport.jsf" id="subnav_report_clientTestEmailReport">Client Test Email Report</a></li>
		
		<c:if test="${features.enableUndeliveredEmail}">
			<li><a href="/pss2.utility/jsp/reports/undeliveredEmailReport.jsf" id="subnav_report_undeliveredEmailReport">Undelivered Email Report</a></li>
		</c:if>
		<!-- operator report tab -->
		<li><a href="/opt.report/jsp/framework.jsf" id="operator_Report">Operator Report</a></li>
		<c:if test="${features.clientOfflineReportEnabled && (requestScope.admin || requestScope.operator) }">
			<li><a href="/pss2.utility/jsp/reports/clientOfflineReport.jsf" id="subnav_report_clientOfflineReport">Client Offline Daily Report</a></li>
        </c:if>
		<c:if test="${features.communicationEmailEnabled && (requestScope.admin || requestScope.operator) }">
			<li><a href="/pss2.utility/jsp/reports/communicationEmailReport.jsf" id="subnav_report_communicationEmailReport">Communication Emails</a></li>
        </c:if>
    </ul>
</div>
