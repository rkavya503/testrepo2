<%@ taglib uri="/WEB-INF/displaytag.tld" prefix="display" %>
<%@ taglib prefix="html" uri="http://struts.apache.org/tags-html" %>
<%@ taglib prefix="bean" uri="http://struts.apache.org/tags-bean" %>
<%@ taglib uri="/WEB-INF/tld/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/tld/fmt.tld" prefix="fmt"%>

<div id="subpage_report_dbp_event_creation">
	<%@include file="../log/log-sub-tabs.jsp"%>
</div>

<p></p>
<!-- link calendar resources -->
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/tcal.css" />
	<script type="text/javascript" src="<%=request.getContextPath()%>/tcal.js"></script> 
<html:form method="post" action="/dbpEventCreationReport" styleId="fm-form">
    <input type="hidden" name="dispatch" value="list"/>
    <fieldset>
        <legend>DBP Event Creation</legend>
        <div class="fm-req">          
            <label for="startDate">
                Start Date:
            </label>
            <div id="startDate">
                <html:text property="startDate" size="40" readonly="true" styleClass="tcal"/>                
            </div>
            
            <label for="endDate">
                End Date:
            </label>
                        <div id="endDate">
                <html:text property="endDate" size="40" readonly="true" styleClass="tcal"/>
            </div>
        </div>
        <input type="submit" value="Search"/>
    </fieldset>
        <div class="tablestyle">
        <display:table name="DBPEventCreationForm.dbpEventCreationList" id="dbpEventCreation" pagesize="20" cellspacing="0" cellpadding="0"
            requestURI="dbpEventCreationReport.do" sort="list" export="true">
            <display:caption title="" media="html">DBP Event Creation History</display:caption>
            <display:column title="Date" sortable="true">
                <bean:write name="dbpEventCreation" property="date" format="${applicationScope.dateFimeFormat}"/>
            </display:column>
            <display:column title="Auto Creation" property="autoCreation" sortable="true"/>
            <display:column title="Success" property="status" sortable="false"/>
            <display:column title="File Name" property="fileName" sortable="false"/>
            <display:column title="Description" property="description" sortable="false"/>
            <display:setProperty name="paging.banner.item_name" value="Entry"/>
            <display:setProperty name="paging.banner.items_name" value="Entries"/>
        </display:table>
    </div>
</html:form>