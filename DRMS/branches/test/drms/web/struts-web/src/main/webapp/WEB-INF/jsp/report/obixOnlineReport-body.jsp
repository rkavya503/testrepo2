<%@ taglib uri="/WEB-INF/displaytag.tld" prefix="display" %>
<%@ taglib prefix="html" uri="http://struts.apache.org/tags-html" %>
<%@ taglib prefix="bean" uri="http://struts.apache.org/tags-bean" %>
<%@ taglib uri="/WEB-INF/tld/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/tld/fmt.tld" prefix="fmt"%>

<div id="subpage_report_obixOnline">
	<%@include file="../log/log-sub-tabs.jsp"%>
</div>

<p></p>

<html:form method="post" action="/obixOnlineReport" styleId="submitQuery">
    <div class="tablestyle">

    <display:table name="ObixOnlineReportForm.records" id="account" pagesize="1000" cellspacing="0" cellpadding="0"
        requestURI="obixOnlineReport.do" sort="list" defaultsort="1" export="false">

        <display:caption title="" media="html">Obix Communication Report</display:caption>
        <display:column title="Participant" property="participantName" group="1"  sortable="true" sortProperty="participantName"/>
        <display:column title="Account Number" property="accountId" sortable="true" sortProperty="accountId"/>
        <display:column title="Obix Last Contact" sortable="true" sortProperty="lastContact">
            <bean:write name="account" property="lastContact" format="${applicationScope.dateTimeFormat}"/>
        </display:column>
        <display:column title="Obix Comm Status" property="status"   sortable="true" sortProperty="status"/>
        <display:setProperty name="paging.banner.item_name" value="Obix Comm"/>
        <display:setProperty name="paging.banner.items_name" value="Obix Comms"/>
    </display:table>

    </div> <!--  Table Style -->

</html:form>
