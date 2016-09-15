<%@ page import="java.util.Date" %>
<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="org.apache.struts.Globals" %>
<%@ taglib uri="/WEB-INF/tld/fmt.tld" prefix="fmt"%>
<%
    final Logger logger = Logger.getLogger("ExceptionPage");
    Throwable exception;
    Object obj = request.getAttribute(Globals.EXCEPTION_KEY);
    if (obj == null) {
        obj = request.getAttribute("javax.servlet.jsp.jspException");
    }
    if ((obj != null) && (obj instanceof Throwable)) {
        exception = (Throwable) obj;
        logger.error(exception.getMessage(), exception);
    }
%>

<div id="error-body">
    <strong>An internal error had occurred at <fmt:formatDate value="<%=new Date()%>" pattern="MM/dd/yy HH:mm:ss.SSS"/> </strong><br/>
    Go <a href="javascript:history.back()">back</a> and try again.
</div>


