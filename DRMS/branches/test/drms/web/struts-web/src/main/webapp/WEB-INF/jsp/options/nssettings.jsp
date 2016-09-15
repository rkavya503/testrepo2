<%@ page language="java" %>
<%@ page import = "com.akuacom.pss2.web.nssettings.NSSettingsForm" %>

<%
	NSSettingsForm nssettings = new NSSettingsForm();
	String cleanMsgHour = "00";
	String cleanMsgMinute = "00";
	
	if(request.getAttribute("nssettings") != null){
		nssettings = (NSSettingsForm)request.getAttribute("nssettings");
		
		if(nssettings.getCleanMsgHour() < 10){
			cleanMsgHour = "0" + nssettings.getCleanMsgHour();
		}
		else{
			cleanMsgHour = "" + nssettings.getCleanMsgHour();
		}
		if(nssettings.getCleanMsgMinute() < 10){
			cleanMsgMinute = "0" + nssettings.getCleanMsgMinute();
		}
		else{
			cleanMsgMinute = "" + nssettings.getCleanMsgMinute();
		}
	}
%>

<div id=subpage_options_nssettings>
	<%@include file="options-sub-links.jsp"%>
</div>
<br/>

<html>
	<form action="/pss2.website/nssettings.do" name="NSSettingsForm" method="post">
		<input type="hidden" name="actiontype" id="actiontype" value=""/>
		<input type="hidden" name="filterStatus" id="filterStatus" value="<%=nssettings.getFilterStatus() %>"/>
		<input type="hidden" name="UUID" id="UUID" value="<%=nssettings.getUUID() %>"/>
		

	    <div class="tablestyle">
	        <table cellspacing="0">
	            <caption>Notification System Global Settings</caption>
	            <tbody>
	                <tr class="alt">
	                    <td width="40%">Filter Status</td>
	                    <td align="left">
							<input type="radio" name="filterStatus1" id="filterStatus1" <%if(nssettings.getFilterStatus() == 1){ %>value="1" checked="true" <%} %> onclick="NSSettingsForm.filterStatus.value='1'"/> On 
							<input type="radio" name="filterStatus1" id="filterStatus1" <%if(nssettings.getFilterStatus() == 0){ %>value="0"  checked="true" <%} %> onclick="NSSettingsForm.filterStatus.value='0'"/> Off 
	                    </td>
	                </tr>
                <!-- <tr>
                    <td>Message Expire Time</td>
                    <td-->
                        <input name="msgExpireTime" id="msgExpireTime" value="1440" size="40" type="hidden" /><!-- Minutes
                    </td>
                </tr> -->
                <tr class="alt">
                    <td>Daily Maximum Messages With Same Priority Per Contact</td>
                    <td align="left">
                        <input name="msgThreshold" id="msgThreshold" value="<%=nssettings.getMsgThreshold() %>" size="20"/>  
                        <input name="frequency" id="frequency" value="1440" size="5" type="hidden" /><!-- minutes -->
                    </td>
                </tr>
                <tr class="alt">
                    <td>Max Days Before Messages Removed</td>
                    <td>
                        <input name="duration" id="duration" value="<%=nssettings.getDuration() %>" maxlength="4" size="1" />  Day(s)
                    </td>
                </tr>
                <tr class="alt">
                    <td>Message Clearing Time (Daily)</td>
                    <td align="left">
                        <input name="cleanMsgHour" id="cleanMsgHour" value="<%=cleanMsgHour %>" maxlength="2" size="1"/> : <input name="cleanMsgMinute" id="cleanMsgMinute" value="<%=cleanMsgMinute %>" maxlength="2" size="1"/> <font color="blue"> Note: The configuration should between 00:00 to 23:59. for example 22:45 </font> 
                    </td>
                </tr>
	            </tbody>
	        </table>
	    </div>
	    <div id="fm-submit" class="fm-req">
	        <input type="submit" value="Save" onclick="this.form.actiontype.value='save'"/>
	        <input type="submit" value="Cancel" onclick="response.sendRedirect('')"/>   
	    </div>
	</form>
</html>
