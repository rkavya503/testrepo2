<%@ taglib uri="/WEB-INF/displaytag.tld" prefix="display" %>
<%@ taglib prefix="html" uri="http://struts.apache.org/tags-html" %>
<%@ taglib prefix="bean" uri="http://struts.apache.org/tags-bean" %>
<%@ taglib uri="/WEB-INF/tld/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/tld/fmt.tld" prefix="fmt"%>

<div id="subpage_report_event_signals">
	<%@include file="../log/log-sub-tabs.jsp"%>
</div>

<p></p>

<html:form method="post" action="/eventSignalReport" styleId="submitQuery">
    <div class="tablestyle">

    <display:table name="EventSignalReportForm.signals" id="signal" pagesize="20" cellspacing="0" cellpadding="0"
        requestURI="eventSignalReport.do" sort="list" defaultsort="3" export="true">

        <display:caption title="" media="html">
            Signals <bean:write name="EventSignalReportForm" property="programName"/> for Event: <bean:write name="EventSignalReportForm" property="eventName"/>
        </display:caption>
        <display:column title="Client" property="participantName" sortable="true"/>
        <display:column title="Event Date">
            <bean:write name="signal" property="signalTime" format="MM/dd/yyyy"/>
        </display:column>
        <display:column title="Event Time">
            <bean:write name="signal" property="signalTime" format="HH:mm:ss"/>
        </display:column>
        <display:column title="Calculated Signal" property="signalLevel" sortable="true"/>
        <display:column title="Actual Signal" property="actualLevel" sortable="true"/>
        <display:setProperty name="paging.banner.item_name" value="Signal"/>
        <display:setProperty name="paging.banner.items_name" value="Signals"/>
        
         <!-- Li Fei added on 20100715 for export program name to Excel. Refer to EventSignalReportAction.java for the definition of fileName -->
        <display:setProperty name="export.excel.filename" value="${fileName}" />
        
    </display:table>

    </div> <!--  Table Style -->

</html:form>

