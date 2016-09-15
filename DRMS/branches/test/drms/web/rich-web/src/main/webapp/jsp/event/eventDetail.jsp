<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
    xmlns:f="http://java.sun.com/jsf/core"
    xmlns:a4j="http://richfaces.org/a4j"
    xmlns:rich="http://richfaces.org/rich"
    xmlns:h="http://java.sun.com/jsf/html" version="2.1">
    <jsp:directive.page language="java"
        contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />
    <head>
    <jsp:include page="/jsp/head.jsp" />
    </head>

    <f:view>
            <jsp:include page="/jsp/header_events.jsp" />
            <rich:spacer height="20px" width="700px" />

            <a4j:keepAlive beanName="EventDetailDataModel" />
			<h:panelGroup layout="block" style="overflow:auto;font-size:12px;padding-bottom: 10px;">
				<div style="clear:both">
				<rich:messages layout="table" globalOnly="false" style="z-index:100"
					infoClass="global-message-info"
					warnClass="global-message-warn"
					errorClass="global-message-error"
					fatalClass="global-message-fatal" />
				</div>	
			</h:panelGroup>
			<br/>
            <rich:panel styleClass="content-panel" id="content-panel">
                <f:facet name="header">
                    <h:outputText style="text-align:left;font-size: 15px;"
                                  value="Event: #{EventDetailDataModel.event.eventName}"/>
                </f:facet>
                <rich:tabPanel  width="90%" 
                                activeTabClass="CPPEventTabs"
                                selectedTab="" 
                                tabClass="tab-titles"
                                styleClass="content-tab" 
                                immediate="true">
                    <rich:tab label="Overview">
                        <jsp:include page="/jsp/event/eventDetail_OverviewTab.jsp" />
                    </rich:tab>
                    <rich:tab label="Usage"  rendered="#{EventDetailDataModel.usageEnabled}">
                        <jsp:include page="/jsp/event/eventDetail_UsageTab.jsp" />
                    </rich:tab>
                    <rich:tab label="Locations" rendered="#{EventDetailDataModel.showLocation}">
                        <jsp:include page="/jsp/event/eventDetail_LocationTab.jsp" />
                    </rich:tab>
                    <rich:tab label="Participants">
                        <jsp:include page="/jsp/event/eventDetail_ParticipantTab.jsp" />
                    </rich:tab>
                    <rich:tab label="Clients">
                        <jsp:include page="/jsp/event/eventDetail_ClientTab.jsp" />
                    </rich:tab>
                    <rich:tab label="Bidding" rendered="#{EventDetailDataModel.biddingEnabled}">
                        <jsp:include page="/jsp/event/eventDetail_BiddingTab.jsp" />
                    </rich:tab>
                    <rich:tab label="Opt-out Participants">
                        <jsp:include page="/jsp/event/eventDetail_OptoutParticipantTab.jsp" />
                    </rich:tab>
                    <rich:tab label="To Opt-in Participants" rendered="#{EventDetailDataModel.eventOptIn}">
                        <jsp:include page="/jsp/event/eventDetail_EnrollmentTab.jsp" />
                    </rich:tab>
                </rich:tabPanel>
            </rich:panel>

        <jsp:include page="/jsp/footer.jsp" />
    </f:view>
</jsp:root>