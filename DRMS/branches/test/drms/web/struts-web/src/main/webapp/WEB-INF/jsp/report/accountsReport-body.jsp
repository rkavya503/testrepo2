<%@ taglib uri="/WEB-INF/displaytag.tld" prefix="display" %>
<%@ taglib prefix="html" uri="http://struts.apache.org/tags-html" %>
<%@ taglib prefix="bean" uri="http://struts.apache.org/tags-bean" %>
<%@ taglib uri="/WEB-INF/tld/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/tld/fmt.tld" prefix="fmt"%>

<div id="subpage_report_account">
	<%@include file="../log/log-sub-tabs.jsp"%>
</div>

<p></p>

<html:form method="post" action="/accountsReport" styleId="submitQuery">
    <div class="tablestyle">

    <display:table name="AccountsReportForm.accounts" id="account" pagesize="1000" cellspacing="0" cellpadding="0"
        requestURI="accountsReport.do" sort="list" defaultsort="1" export="false">

        <display:caption title="" media="html">Accounts Info</display:caption>
        <display:column title="Participant" property="participantName" sortable="true" sortProperty="participantName" media="html" group="1"/>
        <display:column title="Participant" property="participantName" sortable="true" sortProperty="participantName" media="csv"/>
        <display:column title="Account Number" property="accountNumber"   sortable="true" sortProperty="accountNumber"/>
        <display:column title="Parent" property="subAccount"   sortable="true" sortProperty="subAccount"/>
        <display:column title="Premise Number" property="premiseNumber"   sortable="true" sortProperty="premiseNumber"/>
        <display:column title="Start Date"   sortable="true" sortProperty="startDate">
            <bean:write name="account" property="startDate" format="${applicationScope.dateFormat}"/>
        </display:column>
        <display:column title="End Date"   sortable="true" sortProperty="endDate">
            <bean:write name="account" property="endDate" format="${applicationScope.dateFormat}"/>
        </display:column>
        <display:column title="Programs" property="programNames" group="2"   sortable="true" sortProperty="programNames"/>
        <display:column title="Active" property="active"   sortable="true" sortProperty="active"/>
        <display:setProperty name="export.csv.filename" value="accounts.csv"/>
        <display:setProperty name="paging.banner.item_name" value="Account"/>
        <display:setProperty name="paging.banner.items_name" value="Accounts"/>
    </display:table>

    </div> <!--  Table Style -->

</html:form>

<a href="export.do?dispatch=exportAccountInfo"/>
    <f:verbatim> Export to Excel </f:verbatim>
</a>		
