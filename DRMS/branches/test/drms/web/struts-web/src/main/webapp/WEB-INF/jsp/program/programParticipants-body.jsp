<%@ taglib uri="/WEB-INF/displaytag.tld" prefix="display" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>

<html:form action="/program">
<input type="hidden" name="dispatch"/>

<div class="tablestyle">
    <display:table name="${ProgramParticipantsForm.participants}" id="participant" pagesize="20"
        requestURI="/program.do?programName=${ProgramParticipantsForm.programName}" sort="list"
        defaultsort="1" export="true" cellspacing="0" cellpadding="0">
        <display:caption>
            Participant List for ${ProgramParticipantsForm.programName}
        </display:caption>
        <display:column title="Participant" sortable="true" media="html">
            <a href="commDevDetail.do?dispatch=edit&userName=<bean:write name="participant" property="user"/>">
                <bean:write name="participant" property="user"/>
            </a>
        </display:column>
        <display:column title="Participant" property="user" sortable="true" media="excel"/>
        <display:column title="Account #" sortable="true" media="html">
            <bean:write name="participant" property="accountNumber"/>&nbsp;
        </display:column>
        <display:column title="Account #" property="accountNumber" media="excel"/>
        <display:setProperty name="paging.banner.item_name" value="Participant"/>
        <display:setProperty name="paging.banner.items_name" value="Participants"/>
    </display:table>
</div>

</html:form>