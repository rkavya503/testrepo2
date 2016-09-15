<!doctype html public "-//w3c//dtd html 4.0 transitional//en">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>

<div id="page_wrapper"  style="margin-top:10px">
	<div style="margin-top:0;width: 300px;float:left;">
		<h:graphicImage id="logoGraphicImage" alt="#{pss2Config.alt}" title="#{pss2Config.title}" rendered="true" value="../facdash/images/logo/#{pss2Config.logo}"  />
	</div>
	<div id="page_footer">
			Powered by:
			<a href="http://www.akuacom.com" target="_blank"> <img
					src="/facdash/images/layout/Akuacom-logo.gif" alt="Akuacom" title="Akuacom"
					width="100" height="44" longdesc="http://www.akuacom.com" /> </a>
                    <div>
                        <h:outputText style="color:#ececec" value="#{pss2Config.hostName}" />
                    </div>
		<br/>
		<h:outputText value="#{pss2Config.copyright}" escape="false"/>
	</div>
</div>