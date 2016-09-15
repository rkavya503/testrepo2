<%@ page import="com.akuacom.pss2.itron.pge.dbp.in.ManualBidUtil" %>
<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="java.util.Map" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Simple jsp page</title>
</head>
<body>
<%
    Logger log = Logger.getLogger("manual.jsp");
    String eventId = "";
    String accountId = "";
    String bids = "0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5";

    String string = request.getMethod();
    log.info("getMethod: " + string);
    if ("POST".equalsIgnoreCase(string)) {
        eventId = request.getParameter("eventId");
        accountId = request.getParameter("accountId");
        bids = request.getParameter("bids");

        final String command = request.getParameter("command");
        final String BIDS_MAP = "BidsMap";
        Map map = (Map) session.getAttribute(BIDS_MAP);
        if (map == null) {
            map = ManualBidUtil.getMap(accountId, bids);
            session.setAttribute(BIDS_MAP, map);
        } else {
            ManualBidUtil.addBid(map, accountId, bids);
        }

        String text;
        String textTitle;
        if ("add".equals(command)) {
            text = ManualBidUtil.getBidsMapText(eventId, map);
            textTitle = "Buffered Bids:";
        } else {
            text = ManualBidUtil.getResultText(eventId, accountId, bids);
            log.info(text);
            textTitle = "Itron Response:";
            session.removeAttribute(BIDS_MAP);
        }

%>
<table>
    <tr>
        <td><%=textTitle%></td>
    </tr>
    <tr>
        <td><%=text%></td>
    </tr>
</table>
<%
    }

%>
<form action="manual.jsp" method="post">
    <input type="hidden" name="command" value="submit"/>
    <table>
        <tr>
            <td>Event Id:</td>
            <td>
                <input type="text" name="eventId" value="<%=eventId%>"/>
            </td>
        </tr>
        <tr>
            <td>Account Id</td>
            <td>
                <input type="text" name="accountId" value="<%=accountId%>"/>
            </td>
        </tr>
        <tr>
            <td colspan="2">Bids (KW) (manual rule: any bid that with KW > 0 is accepted)</td>
        </tr>
        <tr>
            <td colspan="2">
                <input type="text" name="bids" value="<%=bids%>" size="80"/>
            </td>
        </tr>
        <tr>
            <td>
                <input type="button" value="add" onclick="this.form.command.value='add';this.form.submit();"/> 
                <input type="submit"/>
            </td>
        </tr>

    </table>
</form>
</body>
</html>