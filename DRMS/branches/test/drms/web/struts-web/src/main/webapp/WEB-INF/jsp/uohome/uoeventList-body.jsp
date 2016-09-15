<%@ taglib uri="/WEB-INF/displaytag.tld" prefix="display" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/tld/c.tld" prefix="c"%>

<script type="text/javascript">
    function viewEvent(pr, ev) {
        document.ScheduleFormALL.programName.value = pr;
        document.ScheduleFormALL.eventName.value = ev;
        document.ScheduleFormALL.submit();
    }
    function confirmDelete(frm) {
        var s = "";
        var j = 0;
        for (var i=0;i<frm.length;i++) {
            var e = frm.elements[i];
            if (e.name == 'eventNames') {
                if( e.checked ) {
                    if( j>0 ) {
                        s += ", "
                    }
                    s += e.value;
                    j++;
                }
            }
        }
        if (s == "") return false;
        return confirm("Are you sure you want to delete Event " + s + "?");
    }

</script>

<form action="eventDetail.do" name="ScheduleFormALL" method="post">
    <input type="hidden" name="dispatch" value="view"/>
    <input type="hidden" name="eventName" value=""/>
    <input type="hidden" name="programName" value=""/>
</form>

<html:form action="/uoEvent">
<input type='hidden' name="dispatch" value="delete"/>


<div class="tablestyle">
    <display:table name="${EventListForm.eventList}" id="theEvent" pagesize="20" cellspacing="0" cellpadding="0"
                   requestURI="/uoEvent.do" sort="list" export="true">

        <display:caption>
            <div class="edit_tools">
                <a href="#" title="Delete Selected Items">
                    <img src="secure/images/delete_device.gif"
                         onclick="if(confirmDelete(document.EventListForm)) document.EventListForm.submit();"/>
                </a>
                <%--
                <a href="uoEvent.do?dispatch=create" title="Issue an Event">
                    <img src="secure/images/add_device.gif"/>
                </a>
                --%>
            </div>
            Active Events
        </display:caption>


        <display:column title="" media="html">
            <input name="eventNames" type="checkbox" value="<bean:write name="theEvent" property="eventName"/>"/>
        </display:column>


        <display:column title="Name" sortable="true" media="html">
            <a href="#" onclick="viewEvent('<bean:write name="theEvent" property="programName" />', '<bean:write name="theEvent" property="eventName" />');">
                <bean:write name="theEvent" property="eventName" />&nbsp;
            </a>
        </display:column>
        <%--
        <display:column title="Name" media="excel">
            <bean:write name="theEvent" property="eventName"/>
        </display:column>
        --%>
        <display:column title="Program" property="programName" sortable="true" media="html">
        </display:column>
        <%--
        <display:column title="Program Name" media="excel">
            <bean:write name="theEvent" property="programName"/>
        </display:column>
        --%>
        <display:column title="Notification" sortable="true" media="html">
            <bean:write name="theEvent" property="issuedTime" format="MM/dd/yy"/>
            <span class="time"><bean:write name="theEvent" property="issuedTime" format="HH:mm:ss"/></span>
        </display:column>
        <%--
        <display:column title="Notification Time" media="excel">
            <bean:write name="theEvent" property="issuedTime" format="MM/dd/yy HH:mm:ss"/>
        </display:column>
        --%>
        <display:column title="Start" sortable="true" media="html">
            <bean:write name="theEvent" property="startTime" format="MM/dd/yy"/>
            <span class="time"><bean:write name="theEvent" property="startTime" format="HH:mm:ss"/></span>
        </display:column>
        <%--
        <display:column title="Start Time" media="excel">
            <bean:write name="theEvent" property="startTime" format="MM/dd/yy HH:mm:ss"/>
        </display:column>
        --%>
        <display:column title="End" sortable="true" media="html">
            <bean:write name="theEvent" property="endTime" format="MM/dd/yy"/>
            <span class="time"><bean:write name="theEvent" property="endTime" format="HH:mm:ss"/></span>
        </display:column>
        <%--
        <display:column title="End Time" media="excel">
            <bean:write name="theEvent" property="endTime" format="MM/dd/yy HH:mm:ss"/>
        </display:column>
        --%>
        <display:column title="Status" sortable="false">
            <bean:write name="theEvent" property="state"/>
        </display:column>

        <%--
        <display:column title="Monitor" sortable="false">
            Monitor
        </display:column>
        --%>

        <display:setProperty name="paging.banner.item_name" value="Event"/>
        <display:setProperty name="paging.banner.items_name" value="Events"/>
    </display:table>

    <a href="/pss2.website/export.do?dispatch=exportEvents" > Excel </a>

</div>
<p/>

<%--
<p/>
<div class="tablestyle">
    <display:table name="${EventListForm.eventList}" id="theEvent" pagesize="20" cellspacing="0" cellpadding="0"
                   requestURI="/uoEvent.do" sort="list" export="true">

        <display:caption>
            
            Past Events
        </display:caption>


        <display:column title="">
            <input name="eventNames" type="checkbox" value="<bean:write name="theEvent" property="eventName"/>"/>
        </display:column>


        <display:column title="Name" sortable="true">
            <a href="#" onclick="viewEvent('<bean:write name="theEvent" property="programName" />', '<bean:write name="theEvent" property="eventName" />');">
                <bean:write name="theEvent" property="eventName" />
            </a>
        </display:column>

        <display:column title="Program" property="programName" sortable="true">
        </display:column>

        <display:column title="Notification" sortable="true">
            <bean:write name="theEvent" property="issuedTime" format="MM/dd/yy"/>
            <span class="time"><bean:write name="theEvent" property="issuedTime" format="HH:mm:ss"/></span>
        </display:column>

        <display:column title="Start" sortable="true">
            <bean:write name="theEvent" property="startTime" format="MM/dd/yy"/>
            <span class="time"><bean:write name="theEvent" property="startTime" format="HH:mm:ss"/></span>
        </display:column>

        <display:column title="End" sortable="true">
            <bean:write name="theEvent" property="endTime" format="MM/dd/yy"/>
            <span class="time"><bean:write name="theEvent" property="endTime" format="HH:mm:ss"/></span>
        </display:column>

        <display:column title="Status" sortable="false">
            <bean:write name="theEvent" property="state"/>
        </display:column>
        <display:column title="Monitor" sortable="false">
            Monitor
        </display:column>

        <display:setProperty name="paging.banner.item_name" value="Event"/>
        <display:setProperty name="paging.banner.items_name" value="Events"/>
    </display:table>
</div>

--%>
</html:form>