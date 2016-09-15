<%@ page import="com.akuacom.pss2.util.DrasRole" %>
<%@ page import="com.akuacom.utils.config.RuntimeSwitches" %>
<%@ page import="com.akuacom.pss2.core.EJBFactory" %>
<%@ page import="com.akuacom.pss2.system.SystemManager" %>
<%@ page import="com.akuacom.pss2.system.property.PSS2Features" %>
<%@ page import="com.akuacom.pss2.system.property.PSS2Properties" %>
<div id="subnav">
		<ul>
	        <%
            final PSS2Features pss2features = EJBFactory.getBean(SystemManager.class).getPss2Features();
            final PSS2Properties pss2Properties = EJBFactory.getBean(SystemManager.class).getPss2Properties();
	        %>
		
			<li><a href="options.do" id="subnav_options_password">Password</a></li>
			
			 <%
             if (!(request.isUserInRole(DrasRole.Readonly.toString()) || request.isUserInRole(DrasRole.Dispatcher.toString())))
             {
            %>
            <li><a href="optionsAbout.do" id="subnav_options_about">About Info</a></li>
			<li><a href="optionsContacts.do" id="subnav_options_contacts">Contacts</a></li>
			<% } %>         
            <%
             if (request.isUserInRole(DrasRole.Admin.toString()))
             {
            %>
            <li><a href="programMatrix.do" id="subnav_options_pmatrix">Program Matrix</a></li>
            <%
                      if(pss2features.isSCERTPTempUpdate()) {
            %>
			<li><a href="/pss2.utility/jsp/system/sceFTPConfigure.jsf" id="subnav_options_sceFTPConfigure">SCE RTP Configuration</a></li>
            <%
                      }
            %>
            <li><a href="optionsProperties.do" id="subnav_options_properties">Core Properties</a></li>
            <li><a href="optionsSignals.do" id="subnav_options_signals">Signals</a></li>
            <li><a href="nssettings.do" id="subnav_options_nssettings">Notification System Settings</a></li>
            <li><a href="/pss2.utility/jsp/system/whackLog.jsf" id="subnav_options_whackLog">Log file Admin</a></li>
            <%
                      if(RuntimeSwitches.OUTPUT_PROFILING_INFO) {
            %>
            <li><a href="/pss2.utility/jsp/system/profiling.jsf" id="subnav_options_whackLog">Profiling</a></li>
            <%
                      }
                      if(RuntimeSwitches.ENABLE_QODS) {
            %>
            <li><a href="/pss2.utility/jsp/system/quotation.jsf" id="subnav_options_qods">Quotations</a></li>
            <%
                      }
            %>
            <%
                      if(pss2features.isSCEDBPEventAutoCreationEnabled()) {
            %>
            <li><a href="/pss2.utility/jsp/option/sceFTPConfiguration.jsf" id="subnav_options_dbpEvent">SCE FTP Configuration</a></li>
            <%
                      }
            %>
            
            
            <%
                      if(pss2Properties.isGeInterfaceEnabled()) {
            %>
             <li><a href="/pss2.utility/jsp/option/geInterfaceConfiguration.jsf" id="subnav_options_dbpEvent">GE Interface Configuration</a></li>
            <%
                      }
            %>
            
            
            
            
            <%
                      if(pss2features.isProgramAutoDispatch()) {
            %>
            <li><a href="/pss2.utility/jsp/option/progAutoDispatchConfig.jsf" id="subnav_options_progAutoDisp">Program Auto Dispatch Configuration</a></li>
            <%
                      }
            %>
			<%
                      if(pss2features.isEnableDataService() && pss2features.isUsageEnabled()) {
            %>
            <li><a href="/pss2.utility/jsp/gridpoint/GridPointConfigurePage.jsf" id="subnav_options_gridpoint">GridPoint</a></li>
            <%
                      }
            %>
			
			<%
                      if(pss2features.isTempConfigEnabled()) {
            %>
            <li><a href="/pss2.utility/jsp/option/weatherConfig.jsf" id="weatherConfig">Weather Configuration</a></li>
            <%
                      }
            %>
			
			
            <%
                }
            %>
			<%
            // DRMS-7575: move CIF out side admin group, and allow operator to use it.
                      if(pss2features.isFeatureCustomerInfoUploadEnabled() && !(request.isUserInRole(DrasRole.Readonly.toString()) || request.isUserInRole(DrasRole.Dispatcher.toString()))) {
            %>
            <li><a href="/pss2.utility/customerInfoUpload.jsf" id="subnav_options_customerInfoUpload">CIF Upload</a></li>
            <%
                      }
            %>
			
            <li><a href="/pss2.utility/jsp/option/emailDigestSetting.jsf" id="subnav_options_emailDigestSetting">Operator Email Setting</a></li>
           
			<%
                      if(pss2features.isClientOfflineNotificationEnabled() || pss2features.isClientOfflineReportEnabled() ) {
            %>
            <li><a href="/pss2.utility/jsp/option/timerConfig.jsf" id="subnav_options_timerConfig">Client Offline Configuration</a></li>
            <%
                      }
            %>
	</div>
