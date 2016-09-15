
<%@ taglib uri="/WEB-INF/displaytag.tld" prefix="display" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>

<%--
<html:form action="/program">
<input type="hidden" name="dispatch"/>

<div class="tablestyle">
    <display:table name="${ProgramParticipantsForm.participants}" id="participant" pagesize="20"
        requestURI="/program.do?programName=${ProgramParticipantsForm.programName}" sort="list"
        defaultsort="1" export="true" cellspacing="0" cellpadding="0">
        <display:caption>
                Participant List for ${ProgramParticipantsForm.programName}
        </display:caption>
        <display:column title="Participant" sortable="true" media="html">
            <a href="commDevDetail.do?dispatch=edit&userName=<bean:write name="participant" property="user"/>">
                <bean:write name="participant" property="user"/>
            </a>
        </display:column>
        <display:column title="Participant" property="user" sortable="true" media="excel"/>
        <display:column title="Account #" sortable="true" media="html">
            <bean:write name="participant" property="accountNumber"/>&nbsp;
        </display:column>
        <display:column title="Account #" property="accountNumber" media="excel"/>
        <display:column title="mysite" media="html">
            <a href="mysite.do?user=<bean:write name="participant" property="user"/>">mysite</a>
        </display:column>

        <display:setProperty name="paging.banner.item_name" value="Participant"/>
        <display:setProperty name="paging.banner.items_name" value="Participants"/>
    </display:table>
</div>

</html:form>
--%>
<%
	    //get the right google apikey for url, default is http://localhost:8080/LocSrvWebsite

	    String apikey ="ABQIAAAAE1uPPWEnTjM4-o0TT5yJOBQ3x4ATc1w37aH-lhYN8uEKjCLVCxQSwWHfXOZoRTwLOsz7sVTP00T5QA";

	    String serverName = request.getServerName();     // hostname.com

	    int serverPort = request.getServerPort();        // 80

	    String callingUrl = serverName;


        System.out.println(" calling URL is = "+callingUrl);

		if (callingUrl.equals("www.photomapr.com"))

		            apikey = "ABQIAAAAkbYeOdRre6c-Rgijp4VrrxTA46HY6LEIKIoKWPGZpjzzrZgH3xRArR0QnvmqO-QIBX9iKVG-zWOpqw";

		else if (callingUrl.equals("http://64.84.39.215:80/LocSrvWebsite"))

		            apikey = "ABQIAAAAuT3vB5HXGAvvWvMAjYzv6hTu3q1Mh3RMPFp6KT5kdVTT-ObgZxR_zhByNJfNwMkG2Yk1hQJ8APgULQ";

		else if (callingUrl.equals("localhost"))

		            apikey = "ABQIAAAAkbYeOdRre6c-Rgijp4VrrxTwM0brOpm-All5BF6PoaKBxRWWERRP8-DeZLqoCAKVc_2eYIQzR5hiJA";

		else if (callingUrl.equals("www.hotglobs.com"))

		    apikey = "ABQIAAAA1ZHnBowHT2MBIWsW_65LLRQ1r6X76EUUFBGJPhi_wyjITqx4zRROJdzkb1kyXHgNmWGKvac7sabshw";

		else if (callingUrl.equals("www.geopeg.com"))

		    apikey = "ABQIAAAA1ZHnBowHT2MBIWsW_65LLRQYqAIh3RwtOgcBy1shpsSr39-vMhTPzIc0kpbW6lFy1VtaVDKi1GjbFw";

		else if (callingUrl.equals("geopeg.com"))

		    apikey = "ABQIAAAA1ZHnBowHT2MBIWsW_65LLRTRnGZHEiO8gcZvIVI2nbkW8nNDVRTy0v6u3CVrYRDh7JGnk60cGq4jYA";

        else if (callingUrl.equals("test1.openadr.com"))

                    apikey = "ABQIAAAAE1uPPWEnTjM4-o0TT5yJOBSeu93hOKq5l9IzOW-9m06_1fk79RTAKG3p0dmQ-nzew3mkBFv06NAriA";

        else if (callingUrl.equals("192.168.149.57"))
                    apikey = "ABQIAAAAE1uPPWEnTjM4-o0TT5yJOBQ9siKIZu1CYX52jcQVBxRp30wpwxRxPbuOcHCRUp9GNtzRcDdKyu5Aig";

        else if (callingUrl.equals("192.168.149.13"))
                    apikey = "ABQIAAAAE1uPPWEnTjM4-o0TT5yJOBRPoCjlEOs0O7BAKBptCFS4i66x1xQPE6uKe8O37JeCc9OMZR6yIdl4Nw";

        else if (callingUrl.equals("192.168.149.13:8080"))
                    apikey = "ABQIAAAAE1uPPWEnTjM4-o0TT5yJOBQM25JJdDKd0b5CsCI-2M6KJ3PLQhRv5O_ZVzwa0RQyA67qzTVLsYPt4w";

        String progNames = (String) request.getAttribute("ProgNames");

        String partURL = "../pss2.website/commDevDetail.do?dispatch=edit";
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
			"src", "../pss2/DRASParticipants",
			"width", "100%",
			"height", "500",
			"align", "middle",
			"id", "pss2",
			"quality", "high",
			"bgcolor", "#869ca7",
			"name", "DRASParticipants",
			"flashvars","mapDisplay=OFF&addDeleteParticipants=true&partURL=<%=partURL%>&progNames=<%=progNames%>&wsURL=../UtilityOperatorWS/UtilityOperatorWS&wsAkuaURL=../AkuaUtilityOperatorWS/AkuaUtilityOperatorWS",
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
