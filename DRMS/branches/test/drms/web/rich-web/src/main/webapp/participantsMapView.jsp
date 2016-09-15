<%@ page import="java.util.Date" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    long serverTime = (new Date()).getTime();

%>

<!DOCTYPE html>
<html>
    <head>
        <style type="text/css" media="screen"> 
			html, body	{ height:100%; }
			body { margin:0; padding:0; overflow:auto; text-align:center; 
			       background-color: #ffffff; }   
			object:focus { outline:none; }
        </style>

    </head>
    <body>
		
        <script type="text/javascript" src="swfobject.js"></script>
        <script type="text/javascript">
            <!-- For version detection, set to min. required Flash Player version, or 0 (or 0.0.0), for no version detection. --> 
            var swfVersionStr = "10.0.0";
            <!-- To use express install, set to playerProductInstall.swf, otherwise the empty string. -->
            var xiSwfUrlStr = "playerProductInstall.swf";
            var flashvars = {};
            flashvars.centerLat = "22.35";
            flashvars.centerLon = "114.1667";
            flashvars.zoomLevel = "11";
            flashvars.partURL = "/pss2.website/export.do?dispatch=participantListMapInfo&timestamp=<%=serverTime%>"
            var params = {};
            params.quality = "high";
            params.bgcolor = "#ffffff";
            params.allowscriptaccess = "sameDomain";
            params.allowfullscreen = "true";
            var attributes = {};
            attributes.id = "flexmap";
            attributes.name = "flexmap";
            attributes.align = "middle";
            swfobject.embedSWF(
                "flexmap.swf", "flashContent", 
                "1100px", "800px", 
                swfVersionStr, xiSwfUrlStr, 
                flashvars, params, attributes);
        </script>
        <div id="flashContent">
        </div>
   </body>

</html>
