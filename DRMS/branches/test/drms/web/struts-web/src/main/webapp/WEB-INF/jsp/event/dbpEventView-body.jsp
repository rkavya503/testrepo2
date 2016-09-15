<%@ taglib uri="/WEB-INF/displaytag.tld" prefix="display" %>
<%@ taglib prefix="html" uri="http://struts.apache.org/tags-html" %>
<%@ taglib prefix="bean" uri="http://struts.apache.org/tags-bean" %>
<%@ taglib prefix="logic" uri="http://struts.apache.org/tags-logic" %>
<%@ taglib prefix="c" uri="/WEB-INF/tld/c.tld" %>
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
                <td>RFB issued at:</td>
                <td>
                    <bean:write name="event" property="issuedTime" format="MM/dd/yy"/>
                    <span class="time"><bean:write name="event" property="issuedTime" format="HH:mm:ss"/></span>
                </td>
            </tr>
            <tr>
                <td>Comm Dev must Respond By:</td>
                <td>
                    <bean:write name="event" property="drasRespondBy" format="MM/dd/yy"/>
                    <span class="time"><bean:write name="event" property="drasRespondBy" format="HH:mm:ss"/></span>
                </td>
            </tr>
            <tr>
                <td>Operator must Respond By:</td>
                <td>
                    <bean:write name="event" property="respondBy" format="MM/dd/yy"/>
                    <span class="time"><bean:write name="event" property="respondBy" format="HH:mm:ss"/></span>
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
<html:form action="/dbpEventDetail">
    <input name='dispatch' type='hidden' value=""/>
    <html:hidden property="programName"/>
    <html:hidden property="eventName"/>
<div class="tablestyle">
    <display:table name="currentBids" id="bid" cellspacing="0" cellpadding="0"
            requestURI="dbpEventDetail.do" sort="list" defaultsort="1">
        <display:caption>
            Current Bid Status
        </display:caption>
        <display:column title="Comm Dev" sortable="true">
            <bean:write name="bid" property="participantName"/>
        </display:column>
        <display:column title="Account#">
            <bean:write name="bid" property="accountNumber"/>&nbsp;
        </display:column>
        <logic:iterate name="blockTimes" id="blockTime" indexId="i">
            <display:column title="${blockTime}" property="reductions[${i}]"/>
        </logic:iterate>
        <display:column title="Accepted" property="bidStatus"/>
        <display:column title="">
            <input name="biddings" type="checkbox" value="<bean:write name="bid" property="participantName"/>"/>
        </display:column>
        <display:footer>
            <td colspan="2">Total kW</td>
            <logic:iterate name="reductionTotals" id="reductionTotal">
                <td><bean:write name="reductionTotal"/></td>
            </logic:iterate>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
        </display:footer>
    </display:table>
</div>
<div id="fm-submit" class="fm-req">
    <input type="submit" value="Send Bids" onclick="this.form.dispatch.value='sendBids'"/>
    <input type="submit" value="Accept" onclick="this.form.dispatch.value='accept'"/>
    <input type="submit" value="Accept All" onclick="this.form.dispatch.value='acceptAll'"/>
    <input type="submit" value="Reject" onclick="this.form.dispatch.value='reject'"/>
    <input type="submit" value="Reject All" onclick="this.form.dispatch.value='rejectAll'"/>
    <input type="submit" value="Next State" onclick="this.form.dispatch.value='nextState'"/>
</div>
<p/>
<div class="tablestyle">
    <display:table name="clients" id="client" cellspacing="0" cellpadding="0"
                   requestURI="dbpNoBidEventDetail.do" sort="list" defaultsort="1">
        <display:caption>Clients</display:caption>
        <display:column title="Client">
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
</html:form>