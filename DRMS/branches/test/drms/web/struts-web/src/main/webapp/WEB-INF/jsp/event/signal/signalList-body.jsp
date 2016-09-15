<%@ taglib uri="/WEB-INF/displaytag.tld" prefix="display" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tld/c.tld" prefix="c"%>
<form action="signalDetail.do" name="SignalDetailForm">
    <input type="hidden" name="dispatch" value="create"/>
    <input type="hidden" name="programName" value="${SignalListForm.programName}"/>
    <input type="hidden" name="eventName" value="${SignalListForm.eventName}"/>
    <input type="hidden" name="participantName" value="${SignalListForm.participantName}"/>
</form>

<html:form action="/signalList">
    <html:hidden property="programName"/>
    <html:hidden property="eventName"/>
    <html:hidden property="participantName"/>
    <input type="hidden" name="dispatch" value="delete"/>
    <div class="tablestyle">
        <display:table name="signalEntries" id="signal" sort="list" requestURI="" cellspacing="0" cellpadding="0">
            <display:caption>
            
                Signal Entries<br/>
                Program: ${SignalListForm.programName}, Event: ${SignalListForm.eventName}, Comm Dev: ${SignalListForm.participantName}
            </display:caption>
            <display:column>
                <c:choose>
                    <c:when test="${signal.expired}">
                        <input type="checkbox" name="signalEntryIds" value="${signal.UUID}" disabled="true"/>
                    </c:when>
                    <c:otherwise>
                        <input type="checkbox" name="signalEntryIds" value="${signal.UUID}"/>
                    </c:otherwise>
                </c:choose>
            </display:column>
            <display:column title="Time" sortable="true">
                <bean:write name="signal" property="time" format="MM/dd/yy"/>
                <span class="time"><bean:write name="signal" property="time" format="HH:mm:ss"/></span>
            </display:column>
            <display:column title="Signal" property="parentSignal.signalDef.signalName" sortable="true"/>
            <display:column title="Value" property="valueAsString" sortable="true"/>
        </display:table>
    </div>
</html:form>