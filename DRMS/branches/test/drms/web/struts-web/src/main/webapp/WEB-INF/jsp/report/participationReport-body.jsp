<%@ taglib uri="/WEB-INF/displaytag.tld" prefix="display" %>
<%@ taglib prefix="html" uri="http://struts.apache.org/tags-html" %>
<%@ taglib prefix="bean" uri="http://struts.apache.org/tags-bean" %>
<%@ taglib uri="/WEB-INF/tld/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/tld/fmt.tld" prefix="fmt"%>

<div id="subpage_report_event_participation">
	<%@include file="../log/log-sub-tabs.jsp"%>
</div>

<p></p>

<html:form method="post" action="/eventParticipationReport" styleId="fm-form">
    <fieldset>
        <legend>Event Participation</legend>
        <div class="fm-req">
            <label for="eventDate">
                Event Date:
            </label>
            <div id="eventDate">
                <html:text property="eventDate" size="40"/>
                <script type="text/javascript" src="<%=request.getContextPath()%>/CalendarPopup.js"></script>
                <script type=text/javascript>
                    var cal = new CalendarPopup();
                </script>
                <img align='middle' border='0' hspace='0' vspace='0'
                     src='<%=request.getContextPath()%>/secure/images/cal.gif'
                     id="EVENT_START_DATE_t" style="cursor:pointer;"
                     onclick="cal.select(document.forms['EventParticipationForm'].eventDate,'EVENT_START_DATE_t','${applicationScope.dateFormat}');"/>
            </div>
            <label for="programName">
                Program Name:
            </label>
            <div id="programName">
                <html:select property="program">
                    <html:option value="--"/>
                    <html:optionsCollection property="programs" label="name"/>
                </html:select>
            </div>
            <input type="submit" value="Search"/>
        </div>
    </fieldset>
    <div class="tablestyle">
        <display:table name="EventParticipationForm.participationList" id="participation" pagesize="20" cellspacing="0" cellpadding="0"
            requestURI="eventParticipationReport.do" sort="list" export="true">
            <display:caption title="" media="html">Participation Details</display:caption>
            <display:column title="Client" property="participantName" sortable="true"/>
            <display:column title="Entry Time" sortable="true">
                <bean:write name="participation" property="entryTime" format="MM/dd/yyyy HH:mm"/>
            </display:column>
            <display:column title="Event Name" property="eventName" sortable="true"/>
            <display:column title="Program Name" property="programName" sortable="true"/>
            <display:column title="Participation" property="reason" sortable="true"/>
            <display:column title="Type" property="type" sortable="true"/>
            <display:setProperty name="paging.banner.item_name" value="Entry"/>
            <display:setProperty name="paging.banner.items_name" value="Entries"/>
        </display:table>
    </div>
</html:form>