<%@ taglib uri="/WEB-INF/displaytag.tld" prefix="display" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/c.tld" prefix="c" %>

<div id="subnav_report_emails">
	<%@include file="../log/log-sub-tabs.jsp"%>
</div>
<br/>
<html:form action="/emails">
    <input type="hidden" name="dispatch"/>
    <input type="hidden" name="UUID"/>
    <div class="tablestyle">
	    <table cellpadding="0" cellspacing="0">
			<caption>Message Details</caption>
		    <tr>
			  <td width="20%"><div align="right">To:</div></td>
		      <td><bean:write name="EmailsForm" property="email.to"/></td>
		    </tr>
		    <tr>
		      <td width="20%"><div align="right">Subject:</div></td>
		      <td><bean:write name="EmailsForm" property="email.subject"/></td>
		    </tr>
		    <tr>
		      <td width="20%"><div align="right">User Name:</div></td>
		      <td><bean:write name="EmailsForm" property="email.userName"/></td>
		    </tr>
		    <tr>
		      <td width="20%"><div align="right">Status:</div></td>
		      <td><bean:write name="EmailsForm" property="email.status"/></td>
		    </tr>
		    <tr>
		      <td width="20%"><div align="right">Priority:</div></td>
		      <td><bean:write name="EmailsForm" property="email.priority"/></td>
		    </tr>
		    <tr>
		      <td width="20%"><div align="right">Content:</div></td>
		      <td><bean:write name="EmailsForm" property="email.content"/></td>
		    </tr>
	    </table>
    </div>
    <div id="fm-submit" class="fm-req">
        <input type="submit" value="Send" onclick="form.dispatch.value='send'"/>
    </div>
    
</html:form>