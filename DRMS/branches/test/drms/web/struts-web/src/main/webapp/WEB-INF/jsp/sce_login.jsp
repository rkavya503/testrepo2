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
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link type="text/css" href="<%=request.getContextPath()%>/DR-style.css" rel="stylesheet" />
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/DRScript.js"></script>
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
<title>Open Automated Demand Response</title>
</head>
<body id="standard" onload="document.getElementById('fm-username').focus(); onloadHandler();">
    <div id="DR-topbanner"><img src="images/logo/DR-topbanner.jpg" width="1000" height="83" alt="Southern California Edison An Edison International Company | Akuacom" /></div>
    <div id="DR-pagetitle">Open Automated Demand Response</div>
    <div style="clear:both;"></div>
    <div id="DR-topbox">
    	<div id="DR-tbpopup">
    	<form name="login" action="j_security_check" method="post" onsubmit="trimSpaces()">
    		<div id="DR-tbpptitle">Demand Response Automation Server (DRAS) Login</div>
	        	<%
	            	if (request.getAttribute("loginerr") != null) {
	            %>
	            <div id="loginerror" style= "font-size:13px;margin: 10px 0 10px 20px;">
	            	<font color="red">
	                	Incorrect Username and/or Password.
	                </font>
	            </div>
	            <%
	            	}
	             %>
	            <div class="DR-tbrow"><input id="fm-username" type="text" name="j_username" placeholder="Username" />&nbsp;<input id="fm-password" type="password" name="j_password" placeholder="Password" />&nbsp;<input type="submit" id="DR-loginbtn" value="" /></div>
	            <div class="DR-tbrow"><div id="DR-tbline">If you are an OpenADR customer, sign in using your DRAS log in.</div></div>
	            <div class="DR-tbrow"><a href="#" id="DR-forgotpw"><!--Forgot Password?--></a><a href="#" id="DR-newtoADR">New To ADR?</a><img src="images/logo/akuacom-logo.png" width="165" height="38" alt="Akuacom" id="DR-poweredby"  /></div>
	      	
	      </form>
      </div>
    </div>
    <div id="DR-mainbody">
    	<p>Automated Demand Response (Auto-DR) technology is rapidly gaining popularity, and the OpenADR Alliance is setting a worldwide standard in <a href="http://www.sce.com/drp" target="_self">Demand Response</a> automation, connecting homes and businesses with their utilities to make it easy to power down during peak demand or electricity emergencies. Find out if you can benefit from connecting with <a href="#">OpenADR</a>, creating more savings for you, and more reliability for the grid.</p>
 
        <h3>Demand Response 101</h3>
        <p>Demand Response (DR) is action taken to reduce energy use when electricity demand outweighs supply or grid emergencies occur. We offer incentive programs your home or business can participate in, to help ease stress on the grid and prevent short-term energy shortages. You might also consider Automated Demand Response (Auto-DR). With Auto-DR, we typically connect directly to your energy management system (EMS) - a "smart" network that controls and communicates with your equipment  &mdash;  to reduce energy consumption during DR "events," peak-demand pricing, or emergencies. You can help reduce costs, conserve energy, and help keep Southern California's grid reliable.</p>
 
    	<div id="DR-left">   
              <h3>Open Automated Demand Response (OpenADR)</h3>
              <p>OpenADR is a worldwide adopted internet messaging protocol used by many utilities to communicate with equipment at customer facilities to automatically drop demand during DR program events.</p>
              <p>Here's how it works: We send OpenADR signals to your business' Energy Management System (EMS) through SCE's Demand Response Automation Server (DRAS). To receive OpenADR signals, you must first install and configure an OpenADR "client." This client logs into our DRAS server and maintains an</p>
		</div>
        <div id="DR-right">
        	<a href="https://www.youtube.com/watch?v=O59wLBB0EhQ&feature=youtu.be" target="_blank"><img src="images/logo/DR-video-how-openADR-works.png" width="417" height="222" alt="How OpenADR Works" border="0" /></a>
        </div>
        <div style="float:left; margin-top: -34px;">
        <p>ongoing connection. Whenever we initiate a DR event, the DRAS server sends the event signal to your installed client. Once the client receives the signal, it implements the response that you have programmed into it.</p>
      	<br />
        <p>For more information on OpenADR, visit <a href="http://www.openadr.org" target="_blank">www.openadr.org</a></p>
		</div>
        <div style="clear:both"></div>
        <h3>OpenADR Signals</h3>
        <p>When a DR event occurs, our DRAS server will send a signal to your device, which will then enable your equipment to respond as you've configured them. You can program your equipment to respond automatically when a signal is received. </p>

        <h3>Register My OpenADR Device</h3>
        <p>While some OpenADR compliant technologies may be able to communicate directly with the DRAS, most systems require you to first have an EMS or enroll in an energy management service; and second, to purchase and install a qualifying OpenADR device. Once your device is installed, you can <a href="/ssm/enrollment" style="font-weight: bold;text-decoration: underline;">register</a> it. We will connect you, and you can configure your preferences. For more information on devices and device registration, installation, costs and more, visit the <a href="#faq">FAQ</a> below.</p>

        <h3>Eligible DR Programs</h3>
        <p>The following DR programs are eligible for OpenADR communications:</p>
        <br />
        <p>Commercial and Industrial</p>
        <ul>
            <li>Demand Bidding Program (DBP)</li>
            <li>Summer Advantage Incentive (SAI) formerly known as Critical Peak Pricing (CPP)</li>
            <li>Capacity Bidding Program (CBP)</li>
            <li>Aggregated Managed Portfolio (AMP)</li>
            <li>Base Interruptible Program (BIP)</li>
            <li>Real Time Pricing (RTP)</li>
        </ul>
        <br />
        <p>Residential</p>
        <ul>
            <li>Save Power Days (SPD)</li>
            <li>Summer Advantage Incentive (SAI)</li>
        </ul>
        <br />
        <p>To learn more, visit the <a href="http://www.sce.com/drp">Demand Response</a> page. </p>     
    </div>
    <div id="DR-FAQbody">
    	<a name="faq"></a>
    	<h3>Frequently Asked Questions</h3>
    	<div class="section" id="DR-question1">
        	<div class="question">1. What is the Demand Response Automation Server (DRAS) and what does it do? <div class="plussign1"></div></div>
            <div class="answer">The DRAS server is our system that uses OpenADR technology to communicate a Demand Response event or price signal to an OpenADR device. Once you're connected, you can configure your DRAS account to receive event notifications, adjust shed signals, and enable automated response by your energy management system (EMS).</div>
        </div>
        <div style="clear:both; height:20px;"></div>
        <div class="section" id="DR-question2">
        	<div class="question">2. Where do I get OpenADR equipment and services? <div class="plussign2"></div></div>
            <div class="answer">
            	If you already have an energy management system (EMS), consulting the vendor that installed or services that equipment can be a good starting point.
            	<br /><br />Additionally, we can refer you to firms that we use on the Auto-DR programs. These companies have experience working with our DRAS server, and use of OpenADR for DR programs.</div>
        </div>
        <div style="clear:both; height:20px;"></div>
        <div class="section" id="DR-question3">
        	<div class="question">3. Does the OpenADR client have to be certified to work? <div class="plussign3"></div></div>
            <div class="answer">An OpenADR client should be certified with either OpenADR 2.0a or OpenADR 2.0b. Our system currently supports existing OpenADR 1.0-certified clients, but requires future installations to be OpenADR 2.0 certified. The OpenADR 2.0b profile offers more advanced features, such as the ability to monitor your electricity load in real-time.</div>
        </div>
        <div style="clear:both; height:20px;"></div>
        <div class="section" id="DR-question4">
        	<div class="question">4. Who pays for OpenADR equipment and installation? <div class="plussign4"></div></div>
            <div class="answer">It is your responsibility to purchase and install your OpenADR equipment; however, you may be eligible for incentives for non-residential customers through our Auto-DR program. You'll need to enroll in a price-responsive program to be eligible for incentives.</div>
        </div>
        <div style="clear:both; height:20px;"></div>
        <div class="section" id="DR-question5">
        	<div class="question">5. How much will it cost to install OpenADR equipment? <div class="plussign5"></div></div>
            <div class="answer">
            	Costs can vary. We recommend that you engage a third-party engineering or consulting firm to assess your home or business needs, budget, and operational requirements.
				<br /><br />
				A basic OpenADR device typically costs between $3,000 to $5,000 for hardware and installation. More advanced systems that include functionality for near real-time electricity load monitoring or programmed load reductions are likely to be more expensive.
            </div>
        </div>
        <div style="clear:both; height:20px;"></div>
        <div class="section" id="DR-question6">
        	<div class="question">6. Does SCE provide or install OpenADR equipment, services, or customer support? <div class="plussign6"></div></div>
            <div class="answer">
            	We do not provide or install OpenADR equipment or services; however, if you participate in the Auto-DR program, we can provide financial incentives and connect you with technical coordinators who can assist with equipment selection and configuration.
				<br /><br />
				Many utilities are adopting OpenADR, and our intention is to make this new technology available to our customers who will find it appropriate for their needs. For that reason, we provide OpenADR connectivity and DRAS accounts as an optional courtesy notification. Due to California Public Utilities Commission (CPUC) cost-effectiveness requirements, we cannot provide financial or technical support for customers that use OpenADR outside of the Auto-DR program.</div>
        </div>
        <div style="clear:both; height:20px;"></div>
        <div class="section" id="DR-question7">
        	<div class="question">7. How can I get started once I have installed my equipment? <div class="plussign7"></div></div>
            <div class="answer">
            	If you have already got your equipment installed, you can use our online form to register your device and apply to connect to our DRAS server. You can also contact your Account Representative or the Auto-DR Help Desk at 866-238-3605 or email us for more information on setting up a DRAS account.
           	</div>
        </div>
        <div style="clear:both; height:20px;"></div>
        <div class="section" id="DR-question8">
        	<div class="question">8. How many service accounts can I control with an OpenADR client? <div class="plussign8"></div></div>
            <div class="answer">
            	We recommend one OpenADR client per service account. For more information, please contact the Auto-DR Help Desk at 866-238-3605 or email us for more information on setting up a DRAS account.
            </div>
        </div>
        <div style="clear:both; height:20px;"></div>
        <div class="section" id="DR-question9">
        	<div class="question">9. Can I monitor my electrical load in real-time? <div class="plussign9"></div></div>
            <div class="answer">
            	OpenADR clients that are version 2.0b-certified are equipped to upload electrical load data in near real-time to your DRAS account, and display current load as well as a historical graph.
            </div>
        </div>
        <div style="clear:both; height:20px;"></div>
        <div class="section" id="DR-question10">
        	<div class="question">10. What if my OpenADR client fails or malfunctions during an event? <div class="plussign10"></div></div>
            <div class="answer">
            	OpenADR is a courtesy notification. Customers should understand that we cannot be held liable if equipment does not perform as expected. Please refer to the tariff of each DR program to understand compliance requirements and the means of event notification that we will use for billing purposes.
				<br /><br />
				There are several ways to implement this functionality. For example, you can ask us to install equipment that translates the meter's output into a signal that your OpenADR client can interpret. You can also purchase a more complex OpenADR client with this functionality and hardware built in.
				<br /><br />
				As this is an advanced feature, we suggest engaging a third-party consultant or technical coordinator to assess your needs and design the appropriate solution.
            </div>
        </div>
        <div style="clear:both; height:20px;"></div>
        <div class="section" id="DR-question11">
        	<div class="question">11. What are the terms and conditions for use of the DRAS? <div class="plussign11"></div></div>
            <div class="answer">
            	By logging in to the SCE Demand Response Automation Server website, you acknowledge and agree that your account(s) is/are Auto Demand Response enabled and you understand this account(s) will automatically participate in the events for enrolled demand response (DR) programs, unless you invoke an Auto-DR opt-out option. You also understand that you are responsible for making sure your system responds to the DRAS signal, for the correct configuration to the DR program(s) in which you are enrolled, and for any programmed load reduction sequence. You understand that irrespective of a signal from the DRAS, you are responsible for participation in DR events.
				<br /><br />
				You acknowledge and agree that SCE is not liable, and hereby waive any and all claims against SCE for any claims, demands, losses, damages, costs, expenses, or liability arising out of the initiation and performance of Auto-DR equipment enablement, however caused, whether active or passive. 
            </div>
        </div>
        <div style="clear:both; height:20px;"></div>
        <a name="deviceregistration"></a>
        <div class="section" id="DR-question12">
        	<div class="question">12. How do I register my OpenADR device? <div class="plussign12"></div></div>
            <div class="answer">
            	Prior to registering your device to connect to the DRAS system, please ensure to have the following information readily available:<br />
                <br />
                <ul>
                	<li>Customer Name</li>
                    <li>Customer Address</li>
                    <li>Service Account Number</li>
                    <li>Phone Number</li>
                    <li>Client Manufacturer</li>
                    <li>Device Model Version</li>
                    <li>Device OpenADR Version</li>
                </ul>
                <br />
                Then visit the following <a href="/ssm/enrollment" style="font-weight: bold;text-decoration: underline;">register</a> link to fill out the application.
            </div>
        </div>
        <div style="clear:both; height:20px;"></div>
    </div>
    <div style="background-color: #5d819d; height:2px; width: 1000px;"></div>
    <div id="DR-footer">
    	<img src="images/logo/scelogo.png" width="154" height="60" border="0" alt="Southern California Edison An EDISON INTERNATIONAL Company" />
        <div id="DR-footer1"><img src="images/logo/DR-footerlogo.jpg" width="187" height="55" alt="Akuacom" /></div><br /> 
        <div id="DR-footer2">&copy; 2007 - 2014 <span style="color: #27577d">Akuacom</span> All Rights Reserved.</div>
    </div>
</body>
</html>
