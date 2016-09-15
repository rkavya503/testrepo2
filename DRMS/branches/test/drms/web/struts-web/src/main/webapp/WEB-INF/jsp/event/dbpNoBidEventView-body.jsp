<%@ taglib uri="/WEB-INF/displaytag.tld" prefix="display"%>
<%@ taglib prefix="html" uri="http://struts.apache.org/tags-html"%>
<%@ taglib prefix="bean" uri="http://struts.apache.org/tags-bean"%>
<%@ taglib prefix="logic" uri="http://struts.apache.org/tags-logic"%>
<%@ taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<div class="tablestyle">
	<table>
		<caption>
			Pending Event:
		</caption>
		<tbody>
			<tr>
				<td>
					Program Name:
				</td>
				<td>
					<bean:write name="event" property="programName" />
				</td>
			</tr>
			<tr>
				<td>
					Event Name:
				</td>
				<td>
					<bean:write name="event" property="eventName" />
				</td>
			</tr>
			<tr>
				<td>
					Issued at:
				</td>
				<td>
					<bean:write name="event" property="issuedTime" format="MM/dd/yy" />
					<span class="time"><bean:write name="event"
							property="issuedTime" format="HH:mm:ss" />
					</span>
				</td>
			</tr>
			<tr>
				<td>
					Starts at:
				</td>
				<td>
					<bean:write name="event" property="startTime" format="MM/dd/yy" />
					<span class="time"><bean:write name="event"
							property="startTime" format="HH:mm:ss" />
					</span>
				</td>
			</tr>
			<tr>
				<td>
					Ends at:
				</td>
				<td>
					<bean:write name="event" property="endTime" format="MM/dd/yy" />
					<span class="time"><bean:write name="event"
							property="endTime" format="HH:mm:ss" />
					</span>
				</td>
			</tr>
			<tr>
				<td>
					Status:
				</td>
				<td>
					<bean:write name="event" property="state" />
				</td>
			</tr>
		</tbody>
	</table>
</div>
<html:form action="/dbpNoBidEventDetail">
	<input name='dispatch' type='hidden' value="" />
	<html:hidden property="programName" />
	<html:hidden property="eventName" />
	<div class="tablestyle">
		<display:table name="currentBids" id="bid" cellspacing="0" cellpadding="0"
                       requestURI="dbpNoBidEventDetail.do" sort="list" defaultsort="1">
			<display:caption>
                Current Bid Status
            </display:caption>
			<display:column title="Participant" sortable="true">
                <a href="editBid.do?programName=${DbpNoBidEventDetailForm.programName}&eventName=${DbpNoBidEventDetailForm.eventName}&participantName=${bid.participantName}">
                    <bean:write name="bid" property="participantName"/>
                </a>
			</display:column>
            <display:column sortName="Account" title="Account" sortable="true" >
                    <bean:write name="bid" property="accountNumber" />&nbsp;
             </display:column>
			<logic:iterate name="blockTimes" id="blockTime" indexId="i">
				<display:column title="${blockTime}" property="reductions[${i}]" />
			</logic:iterate>
			<display:column title="Accepted" property="bidStatus" />
			<display:footer>
				<td colspan="2">
					Total kW
				</td>
				<logic:iterate name="reductionTotals" id="reductionTotal">
					<td>
						<bean:write name="reductionTotal" />
					</td>
				</logic:iterate>
				<td>
					&nbsp;
				</td>
			</display:footer>
		</display:table>
	</div>
    <p/>  

    <div class="tablestyle" >
        <display:table name="clients" id="client" cellspacing="0" cellpadding="0" 
                       requestURI="dbpNoBidEventDetail.do" sort="list" defaultsort="1">
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
</html:form>