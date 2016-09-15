<%@ taglib uri="/WEB-INF/displaytag.tld" prefix="display" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<script type="text/javascript">
function confirmDelete(frm) {
    var s = "";
    var j = 0;
    for (var i=0;i<frm.length;i++) {
        var e = frm.elements[i];
        if (e.name == 'usernames') {
            if( e.checked ) {
                if( j>0 ) {
                    s += ", ";
                }
                s += e.value;
                j++;
            }
        }
    }
    if (s == "") return false;
    return confirm("Are you sure you want to delete the Comm Dev " + s + "?");
}

function myRefresh() {
    location.reload();
}

window.setTimeout("myRefresh()", 60000);

</script>
<html:form action="/clientList">
<input type='hidden' name="dispatch" value="delete"/>

<div class="tablestyle">

<display:table name="CommDeviceListForm.commDevList" id="thePart" pagesize="20" cellspacing="0" cellpadding="0"
	requestURI="clientList.do" sort="list" defaultsort="2" export="true">

    <display:caption media="html">
            <div class="edit_tools">
                <a href="#" title="Delete Selected Items">
                    <img src="secure/images/delete_device.gif"
                         onclick="if(confirmDelete(document.CommDeviceListForm)) document.CommDeviceListForm.submit();"/>
                </a>
                <a href="commDevDetail.do?dispatch=create" title="Add a New Device">
                    <img src="secure/images/add_device.gif"/>
                </a>
            </div>
        Participants
    </display:caption>

    <display:column title="&nbsp;" media="html">
        <input name="usernames" type="checkbox" value="<bean:write name="thePart" property="user"/>"/>
    </display:column>
    <display:column title="Name" sortable="true" media="html" class="edit_item">
        <a href="commDevDetail.do?dispatch=edit&userName=<bean:write name="thePart" property="user"/>" title="Edit Device">
            <bean:write name="thePart" property="user"/>
        </a>
    </display:column>
    <display:column title="Name" property="user" media="excel"/>
    <display:column title="Account#" sortable="true" media="html">
        <logic:empty name="thePart" property="accountNumber">&nbsp;</logic:empty>
        <logic:notEmpty name="thePart" property="accountNumber">
            <bean:write name="thePart" property="accountNumber"/>
        </logic:notEmpty>
    </display:column>
    <display:column title="Account#" property="accountNumber" sortable="true" media="excel"/>
    <display:column title="Programs" sortable="true" media="html">
        <logic:empty name="thePart" property="programs">&nbsp;</logic:empty>
        <logic:notEmpty name="thePart" property="programs">
            <bean:write name="thePart" property="programs"/>
        </logic:notEmpty>
    </display:column>
    <display:column title="Programs" property="programs" sortable="true" media="excel"/>
    <display:column title="Type" property="typeString" sortable="true"/>
    <display:column title="First Name" property="firstName" media="excel"/>
    <display:column title="Last Name" property="lastName" media="excel"/>
    <display:column title="Contact 1" property="contact1" media="excel"/>
    <display:column title="Contact 2" property="contact2" media="excel"/>
    <display:column title="Contact 3" property="contact3" media="excel"/>
    <display:column title="Contact 4" property="contact4" media="excel"/>
    <display:column title="Signal Levels" sortable="true" media="html">
        <logic:empty name="thePart" property="signalLevels">&nbsp;</logic:empty>
        <logic:notEmpty name="thePart" property="signalLevels">
            <bean:write name="thePart" property="signalLevels"/>
        </logic:notEmpty>
        <!-- <a href='#'
           onclick='popupHTML("clientList.do?participantID=<bean:write name="thePart" property="user"/>'>
            <bean:write name="thePart" property="user"/>
        </a> -->
    </display:column>
    <display:column title="Signal Levels" property="signalLevels" media="excel"/>
    <display:column title="Last Contact" sortable="true">
        <logic:empty name="thePart" property="lastContact">&nbsp;</logic:empty>
        <logic:notEmpty name="thePart" property="lastContact">
            <logic:equal name="thePart" property="active" value="true">
                <bean:write name="thePart" property="lastContactInMinute"/> mins
            </logic:equal>
            <logic:equal name="thePart" property="active" value="false">
                <bean:write name="thePart" property="lastContact" format="${applicationScope.dateFormat}"/>
            </logic:equal>
            <span class="time"><bean:write name="thePart" property="lastContact" format="HH:mm:ss"/></span>
        </logic:notEmpty>
    </display:column>
    <display:column title="Feedbacks" media="html">
        <a href="usages.do?userName=<bean:write name="thePart" property="user"/>">
            <img src="images/graph_icon.gif" alt="usage list"
                 onmouseover="this.src = 'images/graph_icon-gray.gif'"
                 onmouseout="this.src = 'images/graph_icon.gif'"/>
            <img src="images/spreadsheet_icon.gif" alt="usage list"
                 onmouseover="this.src = 'images/spreadsheet_icon-gray.gif'"
                 onmouseout="this.src = 'images/spreadsheet_icon.gif'"/>
        </a>
    </display:column>
    <display:column title="Mysite" media="html">
        <a href="mysite.do?user=<bean:write name="thePart" property="user"/>">mysite</a>
    </display:column>
    <display:column title="Status" property="status" sortable="true" class="status-${thePart.statusColor}" />
    <display:setProperty name="paging.banner.item_name" value="Comm Device"/>
    <display:setProperty name="paging.banner.items_name" value="Comm Devices"/>
</display:table>

</div> <!--  Table Style -->
</html:form>
