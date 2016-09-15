<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:f="http://java.sun.com/jsf/core"
	xmlns:a4j="http://richfaces.org/a4j"
	xmlns:rich="http://richfaces.org/rich"
	xmlns:richext="http://akuacom.com/richext"
	xmlns:h="http://java.sun.com/jsf/html" version="2.1">
	<jsp:directive.page contentType="text/html" pageEncoding="UTF-8"/>
	<head>
<jsp:include page="/jsp/head.jsp" />
<style>
.inplace {
	border: none;
}

.hover {
	color: #000000;
	background-color: #ececec;
}

.columns {
	width: 25%;
}
</style>
	</head>
    <f:view>

		<jsp:include page="/jsp/header_participants.jsp" />
		<rich:panel>
			<rich:tabPanel width="95%" activeTabClass="ParticipantsViewTabs"
				selectedTab="" tabClass="tab-titles" styleClass="content-tab"
                immediate="true" >
				<rich:tab label="List"  switchType="ajax">
					<jsp:include page="participantsList.jsp" />
				</rich:tab>
				<rich:tab label="Map"
					rendered="#{footer.participantsMapViewEnabled}">
					<jsp:include page="participantsMapView.jsp" />
				</rich:tab>
                <rich:tab action="#{participantImport.fileUploadStartAction}"
                          label="Import"
                          switchType="ajax"
                          >
					<jsp:include page="fileUploadView.jsp" />
				</rich:tab>

			</rich:tabPanel>
		</rich:panel>
		
		<jsp:include page="/jsp/footer.jsp" />
	</f:view>

</jsp:root>