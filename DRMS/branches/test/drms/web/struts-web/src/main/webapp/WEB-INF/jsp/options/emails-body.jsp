<%@ taglib uri="/WEB-INF/displaytag.tld" prefix="display" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/c.tld" prefix="c" %>

<div id="subnav_report_emails">
	<%@include file="../log/log-sub-tabs.jsp"%>
</div>
<br/>
<!-- link calendar resources -->
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/tcal.css" />
	<script type="text/javascript" src="<%=request.getContextPath()%>/tcal.js"></script> 
<html:form action="/emails">
    <input type="hidden" name="dispatch" value="list"/>
    <input type="hidden" name="UUID"/>

    <fieldset>
        <legend>Emails</legend>
        <div class="fm-req">
            <label for="startDate">
                Start Date:
            </label>
            <div id="startDate">
                <html:text property="startDate" size="40" readonly="true" styleClass="tcal"/>               
            </div>
            <label for="endDate">
                End Date:
            </label>
            <div id="endDate">
                <html:text property="endDate" size="40" readonly="true" styleClass="tcal"/>               
            </div>
        </div>
        <input type="submit" value="Search"/> 
    </fieldset>



    <div class="tablestyle">
        <display:table name="${EmailsForm.emails}" id="email" pagesize="20"
            requestURI="/emails.do" sort="list"
            defaultsort="2" export="false" cellspacing="0" cellpadding="0" defaultorder="descending">
            <display:caption>
                Email List
            </display:caption>
            <display:column title="Subject" sortable="true" sortProperty="subject">
                <a href="emails.do?messageId=${email.UUID}&dispatch=viewEmailDetails">
                    <bean:write name="email" property="subject"/>
                </a>
            </display:column>
            <display:column title="Date" sortable="true" sortProperty="creationTime">
                <bean:write name="email" property="creationTime" format="yyyy-MM-dd HH:mm:ss"/>
            </display:column>
            <display:column title="Contact" sortable="true" sortProperty="to">
                <bean:write name="email" property="to" />
            </display:column>
            <display:column title="Status" sortable="true" sortProperty="status">
            	<c:if test="${email.status == 0 }">New</c:if>
            	<c:if test="${email.status == 1 }">Sent</c:if>
            	<c:if test="${email.status == 2 }">Suspended</c:if>
            	<c:if test="${email.status == 3 }">Failed</c:if>
            	<c:if test="${email.status == 4 }">Ignored</c:if>
            </display:column>
            <display:column title="Priority" sortable="true" sortProperty="priority">
            	<c:if test="${email.priority == 10 }">Normal</c:if>
            	<c:if test="${email.priority == 20 }">Urgent</c:if>
            </display:column>
            <display:column title="Action">
                <a href="emails.do?messageId=${email.UUID}&dispatch=send">Send</a>
            </display:column>
            <display:setProperty name="paging.banner.item_name" value="Email"/>
            <display:setProperty name="paging.banner.items_name" value="Emails"/>
        </display:table>
    </div>

    <%--
    <c:if test="${not empty EmailsForm.emails}">
        <div id="fm-submit" class="fm-req">
            <input type="submit" value="Send All" 
                   onclick="form.dispatch.value='sendAll'"
             />
        </div>
    </c:if>
    --%>
</html:form>