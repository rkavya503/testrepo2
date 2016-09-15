<%@ taglib prefix="bean" uri="http://struts.apache.org/tags-bean" %>
<%@ taglib prefix="html" uri="http://struts.apache.org/tags-html" %>
<%@ taglib uri="/WEB-INF/displaytag.tld" prefix="display" %>
<div class="tablestyle">
    <table>
        <caption>Pending Event:</caption>
        <tbody>
            <tr>
                <td>Program Name:</td>
                <td>
                    <bean:write name="event" property="programName"/>
                </td>
            </tr>
            <tr>
                <td>Event Name:</td>
                <td>
                    <bean:write name="event" property="eventName"/>
                </td>
            </tr>
            <tr>
                <td>Issued at:</td>
                <td>
                    <bean:write name="event" property="issuedTime" format="MM/dd/yy"/>
                    <span class="time"><bean:write name="event" property="issuedTime" format="HH:mm:ss"/></span>
                </td>
            </tr>
            <tr>
                <td>Starts at:</td>
                <td>
                    <bean:write name="event" property="startTime" format="MM/dd/yy"/>
                    <span class="time"><bean:write name="event" property="startTime" format="HH:mm:ss"/></span>
                </td>
            </tr>
            <tr>
                <td>Ends at:</td>
                <td>
                    <bean:write name="event" property="endTime" format="MM/dd/yy"/>
                    <span class="time"><bean:write name="event" property="endTime" format="HH:mm:ss"/></span>
                </td>
            </tr>
            <tr>
                <td>Status:</td>
                <td>
                    <bean:write name="event" property="state"/>
                </td>
            </tr>
        </tbody>
    </table>
</div>
<div class="tablestyle">
    <display:table name="clients" id="client" cellspacing="0" cellpadding="0"
                   requestURI="cppEventDetail.do" sort="list" defaultsort="1">
        <display:caption>Clients</display:caption>
        <display:column title="Client" sortable="true">
            <html:link action="/signalList" paramId="participantName"
                paramName="client" paramProperty="participantName">
                <html:param name="programName" value="${event.programName}" />
                <html:param name="eventName" value="${event.eventName}" />
                <bean:write name="client" property="participantName" />
            </html:link>
        </display:column>
        <display:column title="Participant" property="parent"/>
    </display:table>
</div>
