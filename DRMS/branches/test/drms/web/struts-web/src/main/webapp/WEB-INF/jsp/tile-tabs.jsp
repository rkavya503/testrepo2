<%@ page import="java.util.Date" %>
<%@ page import="java.util.TimeZone" %>
<%@ taglib prefix="html" uri="http://struts.apache.org/tags-html" %>
<%@ taglib uri="/WEB-INF/tld/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tld/fmt.tld" prefix="fmt"%>

<%@ page import="com.akuacom.pss2.util.DrasRole" %>
<%@ page import="com.akuacom.pss2.core.EJBFactory" %>
<%@ page import="com.akuacom.pss2.system.SystemManager" %>
<%@ page import="com.akuacom.pss2.system.property.PSS2Features" %>
<%@ page import="com.akuacom.utils.lang.DateUtil"%>
<%@ page import="com.akuacom.pss2.price.australia.LocationPriceManager"%>
<%@ page import="com.akuacom.pss2.price.australia.PriceRecord"%>

<div class="header">
	<br>
    <h1>Demand Response Automation Server</h1>
    <ul id="nav">
        <c:forEach items="${requestScope.tabs}" var="tab">
            <li <c:if test="${tab.active}">id="current"</c:if>>
                <html:link action="${tab.action}">${tab.title}</html:link>
            </li>
        </c:forEach>
    </ul>
    <ul id="navright">
        <li class="last"><a href="logout.do?uid=<%=session.getAttribute("uid")%>">Logout</a></li>
        <li><a href="#" title=""><%=application.getAttribute("utility")%></a></li>
        <li><a href="#" title="">Welcome <%=request.getRemoteUser()%> - (<%=request.getAttribute("usrRole")%>)</a></li>
		<%
		  PSS2Features pss2Features = EJBFactory.getBean(SystemManager.class).getPss2Features();
		   if(pss2Features.isFeatureAustraliaPriceEnabled().booleanValue()) {
		   PriceRecord pr=EJBFactory.getBean(LocationPriceManager.class).getPrice();
		   if(pr==null|| pr.getPrice()==null) {
					pageContext.setAttribute("price","N/A");
					pageContext.setAttribute("tip","No price available");
				}
				else{
					pageContext.setAttribute("price",pr.getPrice()+"$/MW");
					String format=(String)application.getAttribute("headerDateTimeFormat");	
					String tip="Price for "+pr.getLocation()+",updated to"+ 
						DateUtil.format(pr.getTime(), format, TimeZone.getTimeZone("GMT+10"));
					pageContext.setAttribute("tip",tip);
				}
		   
			%>
			 <li><a href="#" title="<%=pageContext.getAttribute("tip")%>"> <%=pageContext.getAttribute("price")%></a></li>
			<%}%>
        <li><a href="#" title=""><fmt:formatDate value="<%=new Date()%>" pattern="${applicationScope.headerDateTimeFormat}"/></a></li>
    </ul>
	<%
    if (pss2Features.isInterruptibleProgramsEnabled().booleanValue()) {
    %>   
    <a  target="_blank"
        style="margin-left: 8px;margin-top: 8px;font-weight: bold;font-family: Arial, Verdana, sans-serif;font-size: 11px;cursor:hand;color: #000000;" 
        href="/drw.admin/jsp/programs.jsf">Interruptible Programs</a>
	<% } %>
	
    
    
    <%
    PSS2Features features = EJBFactory.getBean(SystemManager.class).getPss2Features();
    if (features.isScorecardEnabled().booleanValue()) {
    %>   
    <a  target="_blank"
        style="right: 18px;position: absolute;font-weight: bold;font-family: Arial, Verdana, sans-serif;font-size: 11px;cursor:hand;color: #000000;" 
        href="/pss2.utility/jsp/reports/scoreCard.jsf">Scorecard</a>
	<% } %>
	
</div>
<!-- header end -->


