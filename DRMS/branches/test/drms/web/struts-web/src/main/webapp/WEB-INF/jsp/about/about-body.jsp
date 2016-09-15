<%@ page import="com.akuacom.utils.DateUtil" %>
<%@ taglib prefix="bean" uri="http://struts.apache.org/tags-bean" %>
<%@ taglib prefix="html" uri="http://struts.apache.org/tags-html" %>
<%@ taglib uri="/WEB-INF/tld/c.tld" prefix="c"%>

<html:form action="/about" styleClass="about">
    <h2 align='center'>Utility:</h2>
    <h4 align='center'><bean:write name="AboutForm" property="utility"/></h4>
    <h2 align='center'>Version:</h2>
        <h4 align='center'><bean:write name="AboutForm" property="version"/></h4>
        <p>Build:<bean:write name="AboutForm" property="buildVersion"/></p>
        <p>Date:<bean:write name="AboutForm" property="buildTime"/></p>
        <p>Rev:<bean:write name="AboutForm" property="revision"/>@<bean:write name="AboutForm" property="buildBranch"/></p>
        <p>&nbsp;</p>
    <h2 align='center'>Configuration:</h2>
        <p>&nbsp;</p>
        <p>Server timezone: <%=DateUtil.getServerTimeZoneInfo()%></p>
        <p>&nbsp;</p>
        <p>Client Data Reporting Interval2: <bean:write name="AboutForm" property="clientDataReportingInterval"/> minutes</p>
        <p>&nbsp;</p>
    <h2 align='center'>For More Information, Contact:</h2>
    <h4 align='center'>
        <a href='<bean:write name="AboutForm" property="href1"/>' target='_blank'>
           <bean:write name="AboutForm" property="href1DisplayName"/>
        </a>
    </h4>
    <h4 align='center'><bean:write name="AboutForm" property="phone1"/></h4>
    <h4 align='center'><bean:write name="AboutForm" property="mailto1"/></h4>
</html:form>
<%= (request.getAttribute("aboutInformation") == null ? "" : (String) request.getAttribute("aboutInformation"))%>
      
<div class="partners">
    <c:forEach items="${AboutForm.logoUrls}" var="url">
        <img src="${url}"/>
    </c:forEach>
</div>
