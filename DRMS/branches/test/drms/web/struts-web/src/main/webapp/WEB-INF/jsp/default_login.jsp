<%@ page import="java.util.Date" %>
<%@ page import="com.akuacom.pss2.system.SystemManager" %>
<%@ page import="com.akuacom.pss2.web.util.EJBFactory" %>
<%@ page import="com.akuacom.pss2.system.SystemManager" %>
<%@ page import="com.akuacom.pss2.system.property.PSS2Properties" %>
<%@ page import="com.akuacom.pss2.system.property.PSS2Features" %>
<%@ taglib uri="/WEB-INF/tld/fmt.tld" prefix="fmt"%>
<jsp:useBean id="pss2Config" class="com.akuacom.pss2.web.common.Config" scope="request"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>

		<meta content="text/html; charset=iso-8859-1" http-equiv="Content-Type">
		<title>DR Automation Server: Login Form</title>
        <link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/style.css" />
        <link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/nav.css" />
        <link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/forms.css" />
        <script type="text/javascript" src="<%=request.getContextPath()%>/trim-spaces.js"></script>
        <script type="text/javascript">
        /*
         * Description: Little helper function to return details about IE 8 and its various compatibility settings either use as it is
         * or incorporate into a browser object. Remember browser sniffing is not the best way to detect user-settings as spoofing is
         * very common so use with caution.
        */
        	
        function IEVersion(){
            var _n=navigator,_w=window,_d=document;
            var version="NA";
            var na=_n.userAgent;
            var ieDocMode="NA";
            var ie8BrowserMode="NA";
            var isIe = "NA";
            // Look for msie and make sure its not opera in disguise
            if(/msie/i.test(na) && (!_w.opera)){
                isIe = "true";
                // also check for spoofers by checking known IE objects
                if(_w.attachEvent && _w.ActiveXObject){     
                    // Get version displayed in UA although if its IE 8 running in 7 or compat mode it will appear as 7
                    version = (na.match( /.+ie\s([\d.]+)/i ) || [])[1];
                    // Its IE 8 pretending to be IE 7 or in compat mode     
                    if(parseInt(version)==7){               
                        // documentMode is only supported in IE 8 so we know if its here its really IE 8
                        if(_d.documentMode){
                            version = 8; //reset? change if you need to
                            // IE in Compat mode will mention Trident in the useragent
                            if(/trident\/\d/i.test(na)){
                                ie8BrowserMode = "Compat Mode";
                            // if it doesn't then its running in IE 7 mode
                            }else{
                                ie8BrowserMode = "IE 7 Mode";
                            }
                        }
                    }else if(parseInt(version)==8){
                        // IE 8 will always have documentMode available
                        if(_d.documentMode){ ie8BrowserMode = "IE 8 Mode";}
                    }
                    // If we are in IE 8 (any mode) or previous versions of IE we check for the documentMode or compatMode for pre 8 versions           
                    ieDocMode = (_d.documentMode) ? _d.documentMode : (_d.compatMode && _d.compatMode=="CSS1Compat") ? 7 : 5;//default to quirks mode IE5                           
                }
            }
            return {
                "UserAgent" : na,
                "IsIE" : isIe,
                "Version" : version,
                "BrowserMode" : ie8BrowserMode,
                "DocMode": ieDocMode
            }           
        }
        function onloadHandler(){  
            var obj = IEVersion();
            var isIe = obj.IsIE;
            var version=obj.Version;
            var na=obj.UserAgent;
            var ieDocMode=obj.DocMode;
            var ie8BrowserMode=obj.BrowserMode;
            
            if("true" != isIe) {
                return;
            }
            
            if(parseInt(version)<8){
                alert("Internet Explorer 8.0+ is required, otherwise some DRAS features may not have full functionality.");
                return;
            }             
            if(parseInt(version)==8 && ie8BrowserMode=="IE 7 Mode"){
                alert("Internet Explorer 8.0 'Compatibility View' is not supported, and some features may not have full functionality.");
                return;
            }            
            if(parseInt(version)==8 && ie8BrowserMode=="Compat Mode"){
                alert("Internet Explorer 8.0 'Compatibility View' is not supported, and some features may not have full functionality.");
                return;
            }       
        }   
     </script>
    </head>


	<body id="standard" onload="document.getElementById('fm-username').focus(); onloadHandler();">
		
		    <%
		    PSS2Features features = EJBFactory.getBean(SystemManager.class).getPss2Features();
		    if (features.isProductionServer().booleanValue()) {
		    %>   
		    	<div id="page_wrapper" class="productionServerUtilityOperator"> 
			<% } else{%>
				<div id="page_wrapper" class="testServerUtilityOperator"> 
			<%} %>
            <div class="header">
                <br/>   
                <h1>Demand Response Automation Server</h1>
                <ul id="navright">
                    <li class="last"><a href="#" title=""><%=application.getAttribute("utility")%></a></li>
                </ul>
            </div>
            <div id="content_wrapper">
				<div id="fm-container">
					<form name="login" action="j_security_check" method="post" onsubmit="trimSpaces()" >
						<fieldset>
							<legend>
								Login
							</legend>
                            <%
                                if (request.getAttribute("loginerr") != null) {
                            %>
                            <div id="loginerr">
                                <font color="red">
                                    Incorrect Username and/or Password.
                                </font>
                            </div>
                            <%
                                }
                            %>
							<div class="fm-req">
								<label for="fm-username" class="overlabel">
									Username:
								</label>
								<input id="fm-username" type="text" name="j_username"
									title="Username" maxlength="24" />
							</div>
							<div class="fm-req">
								<label for="fm-password" class="overlabel">
									Password:
								</label>
								<input id="fm-password" type="password" name="j_password"
									title="Password" maxlength="24" />
							</div>
						</fieldset>

                        <div id="fm-submit" class="fm-req" >
                            <input name="Submit" value="Login" type="submit" />
		                </div>
                        <div id="fm-submit-login" class="fm-req" >
                            <font >
                                    <%=EJBFactory.getBean(SystemManager.class).getPss2Properties().getLoginMessage()%>
                            </font>
                        </div>
                        <!--// end fm-submit -->

					</form>
				</div>
				<!--// end fm_container -->
			</div>
			<!--// end content_wrapper -->

		<div id="page_wrapper">
		
			<div id="page_footer">
			     <p style="margin-top:0;width: 300px;float:left;text-align: left;">
			        <a href="<jsp:getProperty name="pss2Config" property="link"/> " >
			               <img src="images/logo/<jsp:getProperty name="pss2Config" property="logo"/>" 
			               alt="<jsp:getProperty name="pss2Config" property="alt"/>" />
			         </a>
    			</p>

				<div class="akua">
					<p>
						Powered by:
						<a href="http://www.akuacom.com" target="_blank"><img
								src="images/logo/Akuacom-logo.gif" alt="Akuacom" width="100"
								height="44" longdesc="http://www.akuacom.com" />
						</a>
					</p>
					<p>
						<jsp:getProperty name="pss2Config" property="copyright"/>
					</p>
				</div>
				<!--// end akua -->
			</div>
			<!--// end page_footer -->
		</div>
	</div>

	</body>

</html>
