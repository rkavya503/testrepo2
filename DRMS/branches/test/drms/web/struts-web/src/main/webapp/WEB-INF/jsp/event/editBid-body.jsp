<%@ taglib uri="/WEB-INF/displaytag.tld" prefix="display" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/c.tld" prefix="c"%>

<html:form action="/editBid">
    <input name='dispatch' type='hidden' value='save'>
    <html:hidden property="programName"/>
    <html:hidden property="eventName"/>
    <html:hidden property="participantName"/>

    <div class="tablestyle">
        <display:table name="bids" id="bid" cellspacing="0" cellpadding="0">
            <display:caption>Edit Bid for ${EditBidForm.participantName}</display:caption>
            <display:column title="Start Time">
                <bean:write name="bid" property="blockStart" format="HH:mm:ss"/>
            </display:column>
            <display:column title="End Time">
                <bean:write name="bid" property="blockEnd" format="HH:mm:ss"/>
            </display:column>
            <display:column title="Reduction (kW)">
                <input type="text" name="bid(i${bid_rowNum - 1})" size="10" value="${bid.reductionKW}"/>
            </display:column>
        </display:table>
    </div>
    <div id="fm-submit" class="fm-req">
        <logic:equal name="readonly" value="false">
        <input type="submit" value="Save"/>
        </logic:equal>
        <input type="button" value="Back" onclick="document.ScheduleFormALL.submit()"/> 
    </div>
</html:form>
<form action="eventDetail.do" name="ScheduleFormALL" method="post">
    <input type="hidden" name="dispatch" value="view"/>
    <input type="hidden" name="eventName" value="${EditBidForm.eventName}"/>
    <input type="hidden" name="programName" value="${EditBidForm.programName}"/>
</form>
