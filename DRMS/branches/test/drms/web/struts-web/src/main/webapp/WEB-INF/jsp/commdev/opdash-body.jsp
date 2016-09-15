<%@ page import="com.akuacom.pss2.util.DrasRole" %>
<%@ page import="com.akuacom.pss2.web.util.EJBFactory" %>
<%@ page import="com.akuacom.pss2.system.SystemManager" %>
<%@ page import="com.akuacom.pss2.system.property.PSS2Properties" %>

<%
	    //get the right google apikey for url, default is http://localhost:8080/LocSrvWebsite

        String apikey = com.akuacom.pss2.core.EJBFactory.getBean(SystemManager.class).getPss2Properties().getStringValue(PSS2Properties.PropertyName.MAP_KEY);

	    String serverName = request.getServerName();     // hostname.com

	    int serverPort = request.getServerPort();        // 80

	    String callingUrl = serverName;


        System.out.println(" calling URL is = "+callingUrl);	

    String partURL = "../pss2.website/commDevDetail.do?dispatch=edit";

    String fetchType = "participant";
    if(request.getParameter("fetchType") != null && request.getParameter("fetchType").equalsIgnoreCase("client"))
    {
        fetchType = "client";  
    }

    String progNames = request.getParameter("ProgNames");
    if(progNames == null ) progNames = "";

    String subProfile = "";
    if(request.isUserInRole(DrasRole.Admin.toString()) || request.isUserInRole(DrasRole.Operator.toString()))
    {
        subProfile = "SUPER__USER";
    }

    String profile = request.getParameter("profile");
    if(! "PL_UI".equals(profile))
    {
        profile = EJBFactory.getBean(SystemManager.class).getPss2Properties().getUtilityName().toUpperCase();
    }

    String uid = (String) request.getSession().getAttribute("uid");
    String dashboardURL = "/facdash/jsp/clients.jsf?uid=" + uid + "&user=";
%>



<script src="../pss2/AC_OETags.js" language="javascript"></script>

<!--  BEGIN Browser History required section -->
<%--<script src="../pss2/history.js" language="javascript"></script>--%>
<!--  END Browser History required section -->

<script language="JavaScript" type="text/javascript">
<!--
// -----------------------------------------------------------------------------
// Globals
// Major version of Flash required
var requiredMajorVersion = 9;
// Minor version of Flash required
var requiredMinorVersion = 0;
// Minor version of Flash required
var requiredRevision = 28;
// -----------------------------------------------------------------------------
// -->
</script>

<script language="JavaScript" type="text/javascript">
<!--
// Version check for the Flash Player that has the ability to start Player Product Install (6.0r65)
var hasProductInstall = DetectFlashVer(6, 0, 65);

// Version check based upon the values defined in globals
var hasRequestedVersion = DetectFlashVer(requiredMajorVersion, requiredMinorVersion, requiredRevision);

if ( hasProductInstall && !hasRequestedVersion ) {
	// DO NOT MODIFY THE FOLLOWING FOUR LINES
	// Location visited after installation is complete if installation is required
	var MMPlayerType = (isIE == true) ? "ActiveX" : "PlugIn";
	var MMredirectURL = window.location;
    document.title = document.title.slice(0, 47) + " - Flash Player Installation";
    var MMdoctitle = document.title;

	AC_FL_RunContent(
		"src", "playerProductInstall",
		"FlashVars", "MMredirectURL="+MMredirectURL+'&MMplayerType='+MMPlayerType+'&MMdoctitle='+MMdoctitle+"",
		"width", "100%",
		"height", "100%",
		"align", "middle",
		"id", "pss2",
		"quality", "high",
		"bgcolor", "#869ca7",
		"name", "pss2",
		"allowScriptAccess","sameDomain",
		"type", "application/x-shockwave-flash",
		"pluginspage", "http://www.adobe.com/go/getflashplayer"
	);
} else if (hasRequestedVersion) {
	// if we've detected an acceptable version
	// embed the Flash Content SWF when all tests are passed
	AC_FL_RunContent(
			"src", "../pss2/OPDash",
			"width", "100%",
			"height", "610",
			"align", "middle",
			"id", "pss2",
			"quality", "high",
			"bgcolor", "#869ca7",
			"name", "OPDash",
			"flashvars",'version=2&uiProfile=<%=profile%>&uiSubProfile=<%=subProfile%>&fetchType=<%=fetchType%>&dashboardURL=<%=dashboardURL%>%>&excelURL=../pss2.website/export.do?dispatch=exportParticipant&mapKey=<%=apikey%>&partURL=<%=partURL%>&progNames=<%=progNames%>&pdWsURL=../pss2-drms-core-ejb-6.2-SNAPSHOT/UsageDataServicerBean&wsURL=../UtilityOperatorWS/UtilityOperatorWS&wsAkuaURL=../AkuaUtilityOperatorWS/AkuaUtilityOperatorWS&isDemo=true',
			"allowScriptAccess","sameDomain",
			"type", "application/x-shockwave-flash",
			"pluginspage", "http://www.adobe.com/go/getflashplayer"
	);
  } else {  // flash is too old or we can't detect the plugin
    var alternateContent = 'Alternate HTML content should be placed here. '
  	+ 'This content requires the Adobe Flash Player. '
   	+ '<a href=http://www.adobe.com/go/getflash/>Get Flash</a>';
    document.write(alternateContent);  // insert non-flash content
  }
// -->
</script>
<noscript>
  	<object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000"
			id="pss2" width="100%" height="100%"
			codebase="http://fpdownload.macromedia.com/get/flashplayer/current/swflash.cab">
			<param name="movie" value="pss2.swf" />
			<param name="quality" value="high" />
			<param name="bgcolor" value="#869ca7" />
			<param name="allowScriptAccess" value="sameDomain" />
			<embed src="http://localhost:8080/pss2/pss2.swf" quality="high" bgcolor="#869ca7"
				width="100%" height="100%" name="pss2" align="middle"
				play="true"
				loop="false"
				quality="high"
				allowScriptAccess="sameDomain"
				type="application/x-shockwave-flash"
				pluginspage="http://www.adobe.com/go/getflashplayer">
			</embed>
	</object>
</noscript>

