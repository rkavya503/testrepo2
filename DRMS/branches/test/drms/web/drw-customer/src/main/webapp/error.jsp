<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<html xmlns="http://www.w3.org/1999/xhtml" lang="en-US" xml:lang="en-US">
<f:view>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title></title>
<link href="css/templatemo_style.css" rel="stylesheet" type="text/css" />
</head>
<body>
<div id="templatemo_wrapper_inner">
  
    <div class="pageHeader">
      <!--TOP BANNER-->

      <div class="nav">
        <!--SITE LOGO-->
        <div class="logoDiv">
			<div class="skipNavdiv">
				<a id="skipNavLink" href="#templatemo_content_wrapper" class="skipNav">Skip navigation</a>
			</div>
			<a href="http://www.sce.com" title="Southern California Edison">
				<img src="images/pagelogo.gif" width="150" height="53" class="pagelogo" alt="SCE logo"/>
			</a>
		</div>
        <!--SITE NAVIGATION MENU-->
        <div class="wrapper">
          <ul>
            <li>
				<a href="https://www.sce.com/ssop/PayBillOnline.aspx">
				Pay My Bill
				</a>
			</li>
            <li>
				<a href="http://www.sce.com/CustomerService/customer-service.htm">
				Customer Service
				</a>
			</li>
            <li>
				<a href="http://www.sce.com/PowerOutageCenter/poweroutagecenter.htm">
				Outage Center
				</a>
			</li>
            <li>
				<a href="http://www.edison.com/careers/default.asp">
				Jobs
				</a>
			</li>
            <li class="last">
				<a href="http://www.edison.com/pressroom/default.asp">
				News Room
				</a>
			</li>         
          </ul>
        </div>
        <!-- SEARCH FIELD-->
        <DIV class="tsearch-field">
          <input autocomplete="off" autofocus="true" />
        </DIV>
        <a href="http://www.sce.com/_search/default.htm">
				<input name="search" type="image" src="images/Icon_search.gif" alt="Search" class="tsearch-submitBtn"/>
		</a>
      </div>
      <!-- end NAVIGATION MENU-->
    </div>
    <!-- end of banner -->
    <!-- MAIN CONTENT-->
    <div id="templatemo_content_wrapper">
	<div class="pageTitle" style="height:300px">
        <h1 style="color:red">Internal Error!</h1>
      </div>
	</div>
	
	 <div class="templatemo_footer">
        <ul>
            <li><a href="http://www.sce.com/AboutSCE/default.htm">
              <h5>About</h5></a></li>
            <li><a href="http://www.sce.com/_SiteMap/default.htm"><h5>Site Map</h5>
            </a></li>
            <li><a href="#"><h5>Library</h5>
            </a></li>
            <li><a href="http://www.sce.com/PrivacyPolicy/privacypolicy.htm"><h5>Privacy</h5>
            </a></li>
            <li><a href="#"><h5>Terms &amp; Conditions</h5>
            </a></li>
            <li><a href="http://www.sce.com/AboutSCE/Regulatory/default.htm"><h5>Regulatory</h5>
            </a></li>
            <li><a href="http://www.edison.com/"><h5>Edison International</h5>
            </a></li>
            <li><a href="#"><h5>Edison Sites</h5>
	
            </a></li>
            <li class="last">
			<!--<h5>Copyright © 2010 Southern California Edison. All Rights Reserved</h5>-->
				<h5><h:outputText value="#{prCopyText.copyrightSCE}" /></h5>
            </li>
          </ul>
      </div>
</div>
</body>
</f:view>
