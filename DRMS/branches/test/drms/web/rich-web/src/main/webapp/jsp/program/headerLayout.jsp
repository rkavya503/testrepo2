<!doctype html public "-//w3c//dtd html 4.0 transitional//en">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<div>
	<rich:spacer height="30px" width="100%" />

	<h:panelGrid id="header_CPP" columns="9" styleClass="event-panel-grid" rendered="#{programConfigureDataModel.headFlagCPP}">
			<h:panelGroup>
				<h:outputLabel for=""  value="Program Template" styleClass="program_edit_label"/>
			</h:panelGroup>
			<h:panelGroup>
				<h:selectOneMenu disabled="true" value="#{programConfigureDataModel.programName}">                     
                    <f:selectItems value="#{programConfigureDataModel.programNameList}"/> 
                </h:selectOneMenu>
			</h:panelGroup>
			<h:panelGroup>
				<h:outputLabel value="Start" styleClass="program_icon_label"/><br/>
				<p align="center">
					<a4j:commandButton id="button_start_CPP"
						image="#{programConfigureDataModel.iconURLForStart}"
						alt="Start" 
						action="#{programConfigureDataModel.businessLogicIconClickAction}"
						actionListener="#{programConfigureDataModel.businessLogicIconClickListener}"
						styleClass=""
						title=""
						reRender="button_start_CPP,button_signal_CPP,button_mode_CPP,programBasicAttributesTable">
						<f:attribute name="ICON_TYPE" value="ICON_TYPE_START" />
					</a4j:commandButton>
				</p>
			</h:panelGroup>
			<h:panelGroup >
				<rich:separator height="2" lineType="solid" width="100px"/>
			</h:panelGroup>
			<h:panelGroup >
				<h:outputLabel value="Configure Signals" styleClass="program_icon_label"/><br/>
				<p align="center">
					<a4j:commandButton id="button_signal_CPP"
						image="#{programConfigureDataModel.iconURLForSignal}"
						alt="Configure Signals" 
						action="#{programConfigureDataModel.businessLogicIconClickAction}"
						actionListener="#{programConfigureDataModel.businessLogicIconClickListener}"
						styleClass=""
						title=""
						reRender="button_start_CPP,button_signal_CPP,button_mode_CPP">
						<f:attribute name="ICON_TYPE" value="ICON_TYPE_SIGNAL" />
					</a4j:commandButton>
				</p>
			</h:panelGroup>
			<h:panelGroup >
				<rich:separator height="2" lineType="solid" width="100px"/>
			</h:panelGroup>
			<h:panelGroup >
				<h:outputLabel value="Configure Mode Transitions" styleClass="program_icon_label"/><br/>
				<p align="center">
					<a4j:commandButton id="button_mode_CPP"
						image="#{programConfigureDataModel.iconURLForMode}"
						alt="Configure Mode Transitions" 
						action="#{programConfigureDataModel.businessLogicIconClickAction}"
						actionListener="#{programConfigureDataModel.businessLogicIconClickListener}"
						styleClass=""
						title=""
						reRender="button_start_CPP,button_signal_CPP,button_mode_CPP">
						<f:attribute name="ICON_TYPE" value="ICON_TYPE_MODE" />
					</a4j:commandButton>
				</p>
			</h:panelGroup>
	</h:panelGrid>
	
	<h:panelGrid id="header_DBPNoBid" columns="9" styleClass="event-panel-grid" rendered="#{programConfigureDataModel.headFlagDBPNoBid}">
			<h:panelGroup>
			<br/>
				<h:outputLabel for=""  value="Program Template" styleClass="program_edit_label"/>
			</h:panelGroup>
			<h:panelGroup>
				<h:selectOneMenu disabled="true" value="#{programConfigureDataModel.programName}">                     
                    <f:selectItems value="#{programConfigureDataModel.programNameList}"/> 
                </h:selectOneMenu>
			</h:panelGroup>
			<h:panelGroup >
				<h:outputLabel value="Start" styleClass="program_icon_label"/><br/>
				<p align="center">
					<a4j:commandButton id="button_start_DBPNoBid"
						image="#{programConfigureDataModel.iconURLForStart}"
						alt="Start" 
						action="#{programConfigureDataModel.businessLogicIconClickAction}"
						actionListener="#{programConfigureDataModel.businessLogicIconClickListener}"
						styleClass=""
						title=""
						reRender="button_start_DBPNoBid,button_signal_DBPNoBid,button_bid_DBPNoBid">
						<f:attribute name="ICON_TYPE" value="ICON_TYPE_START" />
					</a4j:commandButton>
				</p>
			</h:panelGroup>
			<h:panelGroup >
				<rich:separator height="2" lineType="solid" width="100px"/>
			</h:panelGroup>
			<h:panelGroup >
				<h:outputLabel value="Configure Signals" styleClass="program_icon_label"/><br/>
				<p align="center">
					<a4j:commandButton id="button_signal_DBPNoBid"
						image="#{programConfigureDataModel.iconURLForSignal}"
						alt="Configure Signals" 
						action="#{programConfigureDataModel.businessLogicIconClickAction}"
						actionListener="#{programConfigureDataModel.businessLogicIconClickListener}"
						styleClass=""
						title=""
						reRender="button_start_DBPNoBid,button_signal_DBPNoBid,button_bid_DBPNoBid">
						<f:attribute name="ICON_TYPE" value="ICON_TYPE_SIGNAL" />
					</a4j:commandButton>
				</p>
			</h:panelGroup>
			<h:panelGroup >
				<rich:separator height="2" lineType="solid" width="100px"/>
			</h:panelGroup>
			<h:panelGroup >
				<h:outputLabel value="Configure Bid" styleClass="program_icon_label"/><br/>
				<p align="center">
					<a4j:commandButton id="button_bid_DBPNoBid"
						image="#{programConfigureDataModel.iconURLForBid}"
						alt="Configure Bid" 
						action="#{programConfigureDataModel.businessLogicIconClickAction}"
						actionListener="#{programConfigureDataModel.businessLogicIconClickListener}"
						styleClass=""
						title=""
						reRender="button_start_DBPNoBid,button_signal_DBPNoBid,button_bid_DBPNoBid">
						<f:attribute name="ICON_TYPE" value="ICON_TYPE_BID" />
					</a4j:commandButton>
				</p>
			</h:panelGroup>
	</h:panelGrid>
	
	<h:panelGrid id="header_DBP" columns="9" styleClass="event-panel-grid" rendered="#{programConfigureDataModel.headFlagDBP}">
			<h:panelGroup>
			<br/>
				<h:outputLabel for=""  value="Program Template" styleClass="program_edit_label"/>
			</h:panelGroup>
			<h:panelGroup>
				<h:selectOneMenu disabled="true" value="#{programConfigureDataModel.programName}">                     
                    <f:selectItems value="#{programConfigureDataModel.programNameList}"/> 
                </h:selectOneMenu>
			</h:panelGroup>
						<h:panelGroup >
				<h:outputLabel value="Start" styleClass="program_icon_label"/><br/>
				<p align="center">
					<a4j:commandButton id="button_start_DBP"
						image="#{programConfigureDataModel.iconURLForStart}"
						alt="Start" 
						action="#{programConfigureDataModel.businessLogicIconClickAction}"
						actionListener="#{programConfigureDataModel.businessLogicIconClickListener}"
						styleClass=""
						title=""
						reRender="button_start_DBP,button_signal_DBP,button_bid_DBP">
						<f:attribute name="ICON_TYPE" value="ICON_TYPE_START" />
					</a4j:commandButton>
				</p>
			</h:panelGroup>
			<h:panelGroup >
				<rich:separator height="2" lineType="solid" width="100px"/>
			</h:panelGroup>
			<h:panelGroup >
				<h:outputLabel value="Configure Signals" styleClass="program_icon_label"/><br/>
				<p align="center">
					<a4j:commandButton id="button_signal_DBP"
						image="#{programConfigureDataModel.iconURLForSignal}"
						alt="Configure Signals" 
						action="#{programConfigureDataModel.businessLogicIconClickAction}"
						actionListener="#{programConfigureDataModel.businessLogicIconClickListener}"
						styleClass=""
						title=""
						reRender="button_start_DBP,button_signal_DBP,button_bid_DBP">
						<f:attribute name="ICON_TYPE" value="ICON_TYPE_SIGNAL" />
					</a4j:commandButton>
				</p>
			</h:panelGroup>
			<h:panelGroup >
				<rich:separator height="2" lineType="solid" width="100px"/>
			</h:panelGroup>
			<h:panelGroup >
				<h:outputLabel value="Configure Bid" styleClass="program_icon_label"/><br/>
				<p align="center">
					<a4j:commandButton id="button_bid_DBP"
						image="#{programConfigureDataModel.iconURLForBid}"
						alt="Configure Bid" 
						action="#{programConfigureDataModel.businessLogicIconClickAction}"
						actionListener="#{programConfigureDataModel.businessLogicIconClickListener}"
						styleClass=""
						title=""
						reRender="button_start_DBP,button_signal_DBP,button_bid_DBP">
						<f:attribute name="ICON_TYPE" value="ICON_TYPE_BID" />
					</a4j:commandButton>
				</p>
			</h:panelGroup>
	</h:panelGrid>	
	
	<h:panelGrid id="header_RTP" columns="9" styleClass="event-panel-grid" rendered="#{programConfigureDataModel.headFlagRTP}">
			<h:panelGroup>
			<br/>
				<h:outputLabel for=""  value="Program Template" styleClass="program_edit_label"/>
			</h:panelGroup>
			<h:panelGroup>
				<h:selectOneMenu disabled="true" value="#{programConfigureDataModel.programName}">                     
                    <f:selectItems value="#{programConfigureDataModel.programNameList}"/> 
                </h:selectOneMenu>
			</h:panelGroup>
			<h:panelGroup >
				<h:outputLabel value="Start" styleClass="program_icon_label"/><br/>
				<p align="center">
					<a4j:commandButton id="button_start_RTP"
						image="#{programConfigureDataModel.iconURLForStart}"
						alt="Start" 
						action="#{programConfigureDataModel.businessLogicIconClickAction}"
						actionListener="#{programConfigureDataModel.businessLogicIconClickListener}"
						styleClass=""
						title=""
						reRender="button_start_RTP,button_signal_RTP,button_Season_RTP,button_RTP_RTP">
						<f:attribute name="ICON_TYPE" value="ICON_TYPE_START" />
					</a4j:commandButton>
				</p>
			</h:panelGroup>
			<h:panelGroup >
				<rich:separator height="2" lineType="solid" width="100px"/>
			</h:panelGroup>
			<h:panelGroup >
				<h:outputLabel value="Configure Signals" styleClass="program_icon_label"/><br/>
				<p align="center">
					<a4j:commandButton id="button_signal_RTP"
						image="#{programConfigureDataModel.iconURLForSignal}"
						alt="Configure Signals" 
						action="#{programConfigureDataModel.businessLogicIconClickAction}"
						actionListener="#{programConfigureDataModel.businessLogicIconClickListener}"
						styleClass=""
						title=""
						reRender="button_start_RTP,button_signal_RTP,button_Season_RTP,button_RTP_RTP">
						<f:attribute name="ICON_TYPE" value="ICON_TYPE_SIGNAL" />
					</a4j:commandButton>
				</p>
			</h:panelGroup>
			<h:panelGroup >
				<rich:separator height="2" lineType="solid" width="100px"/>
			</h:panelGroup>
			<h:panelGroup >
				<h:outputLabel value="Configure Season" styleClass="program_icon_label"/><br/>
				<p align="center">
					<a4j:commandButton id="button_Season_RTP"
						image="#{programConfigureDataModel.iconURLForSeason}"
						alt="Configure Season" 
						action="#{programConfigureDataModel.businessLogicIconClickAction}"
						actionListener="#{programConfigureDataModel.businessLogicIconClickListener}"
						styleClass=""
						title=""
						reRender="button_start_RTP,button_signal_RTP,button_Season_RTP,button_RTP_RTP">
						<f:attribute name="ICON_TYPE" value="ICON_TYPE_SEASON" />
					</a4j:commandButton>
				</p>
			</h:panelGroup>
			<h:panelGroup >
				<rich:separator height="2" lineType="solid" width="100px"/>
			</h:panelGroup>
			<h:panelGroup >
				<h:outputLabel value="Configure RTP" styleClass="program_icon_label"/><br/>
				<p align="center">
					<a4j:commandButton id="button_RTP_RTP"
						image="#{programConfigureDataModel.iconURLForRTP}"
						alt="Configure RTP" 
						action="#{programConfigureDataModel.businessLogicIconClickAction}"
						actionListener="#{programConfigureDataModel.businessLogicIconClickListener}"
						styleClass=""
						title=""
						reRender="button_start_RTP,button_signal_RTP,button_Season_RTP,button_RTP_RTP">
						<f:attribute name="ICON_TYPE" value="ICON_TYPE_RTP" />
					</a4j:commandButton>
				</p>
			</h:panelGroup>
	</h:panelGrid>
	
	<h:panelGrid id="header_DEMO" columns="9" styleClass="event-panel-grid" rendered="#{programConfigureDataModel.headFlagDEMO}">
			<h:panelGroup>
			<br/>
				<h:outputLabel for=""  value="Program Template" styleClass="program_edit_label"/>
			</h:panelGroup>
			<h:panelGroup>
				<h:selectOneMenu disabled="true" value="#{programConfigureDataModel.programName}">                     
                    <f:selectItems value="#{programConfigureDataModel.programNameList}"/> 
                </h:selectOneMenu>
			</h:panelGroup>
			<h:panelGroup >
				<h:outputLabel value="Start" styleClass="program_icon_label"/><br/>
				<p align="center">
					<a4j:commandButton id="button_start_DEMO"
						image="#{programConfigureDataModel.iconURLForStart}"
						alt="Start" 
						action="#{programConfigureDataModel.businessLogicIconClickAction}"
						actionListener="#{programConfigureDataModel.businessLogicIconClickListener}"
						styleClass=""
						title=""
						reRender="button_start_DEMO,button_signal_DEMO">
						<f:attribute name="ICON_TYPE" value="ICON_TYPE_START" />
					</a4j:commandButton>
				</p>
			</h:panelGroup>
			<h:panelGroup >
				<rich:separator height="2" lineType="solid" width="100px"/>
			</h:panelGroup>
			<h:panelGroup >
				<h:outputLabel value="Configure Signals" styleClass="program_icon_label"/><br/>
				<p align="center">
					<a4j:commandButton id="button_signal_DEMO"
						image="#{programConfigureDataModel.iconURLForSignal}"
						alt="Configure Signals" 
						action="#{programConfigureDataModel.businessLogicIconClickAction}"
						actionListener="#{programConfigureDataModel.businessLogicIconClickListener}"
						styleClass=""
						title=""
						reRender="button_start_DEMO,button_signal_DEMO">
						<f:attribute name="ICON_TYPE" value="ICON_TYPE_SIGNAL" />
					</a4j:commandButton>
				</p>
			</h:panelGroup>
	</h:panelGrid>
	
	<h:panelGrid id="header_TESTProgram" columns="9" styleClass="event-panel-grid" rendered="#{programConfigureDataModel.headFlagTESTProgram}">
			<h:panelGroup>
			<br/>
				<h:outputLabel for=""  value="Program Template" styleClass="program_edit_label"/>
			</h:panelGroup>
			<h:panelGroup>
				<h:selectOneMenu disabled="true" value="#{programConfigureDataModel.programName}">                     
                    <f:selectItems value="#{programConfigureDataModel.programNameList}"/> 
                </h:selectOneMenu>
			</h:panelGroup>
			<h:panelGroup >
				<h:outputLabel value="Start" styleClass="program_icon_label"/><br/>
				<p align="center">
					<a4j:commandButton id="button_start_TESTProgram"
						image="#{programConfigureDataModel.iconURLForStart}"
						alt="Start" 
						action="#{programConfigureDataModel.businessLogicIconClickAction}"
						actionListener="#{programConfigureDataModel.businessLogicIconClickListener}"
						styleClass=""
						title=""
						reRender="button_start_TESTProgram,button_signal_TESTProgram">
						<f:attribute name="ICON_TYPE" value="ICON_TYPE_START" />
					</a4j:commandButton>
				</p>
			</h:panelGroup>
			<h:panelGroup >
				<rich:separator height="2" lineType="solid" width="100px"/>
			</h:panelGroup>
			<h:panelGroup >
				<h:outputLabel value="Configure Signals" styleClass="program_icon_label"/><br/>
				<p align="center">
					<a4j:commandButton id="button_signal_TESTProgram"
						image="#{programConfigureDataModel.iconURLForSignal}"
						alt="Configure Signals" 
						action="#{programConfigureDataModel.businessLogicIconClickAction}"
						actionListener="#{programConfigureDataModel.businessLogicIconClickListener}"
						styleClass=""
						title=""
						reRender="button_start_TESTProgram,button_signal_TESTProgram">
						<f:attribute name="ICON_TYPE" value="ICON_TYPE_SIGNAL" />
					</a4j:commandButton>
				</p>
			</h:panelGroup>
	</h:panelGrid>
	
	<h:panelGrid id="header_ISSUE" columns="9" styleClass="event-panel-grid" rendered="#{programConfigureDataModel.headFlagISSUE}">
			<h:panelGroup>
				<h:outputLabel for=""  value="Program Template" styleClass="program_edit_label"/>
			</h:panelGroup>
			<h:panelGroup>
				<h:selectOneMenu disabled="true" value="#{programConfigureDataModel.programName}">                     
                    <f:selectItems value="#{programConfigureDataModel.programNameList}"/> 
                </h:selectOneMenu>
			</h:panelGroup>
			<h:panelGroup>
				<h:outputLabel value="Start" styleClass="program_icon_label"/><br/>
				<p align="center">
					<a4j:commandButton id="button_start_ISSUE"
						image="#{programConfigureDataModel.iconURLForStart}"
						alt="Start" 
						action="#{programConfigureDataModel.businessLogicIconClickAction}"
						actionListener="#{programConfigureDataModel.businessLogicIconClickListener}"
						styleClass=""
						title=""
						reRender="button_start_ISSUE,button_signal_ISSUE,button_mode_ISSUE,programBasicAttributesTable">
						<f:attribute name="ICON_TYPE" value="ICON_TYPE_START" />
					</a4j:commandButton>
				</p>
			</h:panelGroup>
			<h:panelGroup >
				<rich:separator height="2" lineType="solid" width="100px"/>
			</h:panelGroup>
			<h:panelGroup >
				<h:outputLabel value="Configure Signals" styleClass="program_icon_label"/><br/>
				<p align="center">
					<a4j:commandButton id="button_signal_ISSUE"
						image="#{programConfigureDataModel.iconURLForSignal}"
						alt="Configure Signals" 
						action="#{programConfigureDataModel.businessLogicIconClickAction}"
						actionListener="#{programConfigureDataModel.businessLogicIconClickListener}"
						styleClass=""
						title=""
						reRender="button_start_ISSUE,button_signal_ISSUE,button_mode_ISSUE">
						<f:attribute name="ICON_TYPE" value="ICON_TYPE_SIGNAL" />
					</a4j:commandButton>
				</p>
			</h:panelGroup>
			<h:panelGroup >
				<rich:separator height="2" lineType="solid" width="100px"/>
			</h:panelGroup>
			<h:panelGroup >
				<h:outputLabel value="Configure Mode Transitions" styleClass="program_icon_label"/><br/>
				<p align="center">
					<a4j:commandButton id="button_mode_ISSUE"
						image="#{programConfigureDataModel.iconURLForMode}"
						alt="Configure Mode Transitions" 
						action="#{programConfigureDataModel.businessLogicIconClickAction}"
						actionListener="#{programConfigureDataModel.businessLogicIconClickListener}"
						styleClass=""
						title=""
						reRender="button_start_ISSUE,button_signal_ISSUE,button_mode_ISSUE">
						<f:attribute name="ICON_TYPE" value="ICON_TYPE_MODE" />
					</a4j:commandButton>
				</p>
			</h:panelGroup>
	</h:panelGrid>	

	<h:panelGrid id="header_TEST" columns="9" styleClass="event-panel-grid" rendered="#{programConfigureDataModel.headFlagTEST}">
			<h:panelGroup>
				<h:outputLabel for=""  value="Program Template" styleClass="program_edit_label"/>
			</h:panelGroup>
			<h:panelGroup>
				<h:selectOneMenu disabled="true" value="#{programConfigureDataModel.programName}">                     
                    <f:selectItems value="#{programConfigureDataModel.programNameList}"/> 
                </h:selectOneMenu>
			</h:panelGroup>
			<h:panelGroup>
				<h:outputLabel value="Start" styleClass="program_icon_label"/><br/>
				<p align="center">
					<a4j:commandButton id="button_start_TEST"
						image="#{programConfigureDataModel.iconURLForStart}"
						alt="Start" 
						action="#{programConfigureDataModel.businessLogicIconClickAction}"
						actionListener="#{programConfigureDataModel.businessLogicIconClickListener}"
						styleClass=""
						title=""
						reRender="button_start_TEST,button_signal_TEST,button_mode_TEST,programBasicAttributesTable">
						<f:attribute name="ICON_TYPE" value="ICON_TYPE_START" />
					</a4j:commandButton>
				</p>
			</h:panelGroup>
			<h:panelGroup >
				<rich:separator height="2" lineType="solid" width="100px"/>
			</h:panelGroup>
			<h:panelGroup >
				<h:outputLabel value="Configure Signals" styleClass="program_icon_label"/><br/>
				<p align="center">
					<a4j:commandButton id="button_signal_TEST"
						image="#{programConfigureDataModel.iconURLForSignal}"
						alt="Configure Signals" 
						action="#{programConfigureDataModel.businessLogicIconClickAction}"
						actionListener="#{programConfigureDataModel.businessLogicIconClickListener}"
						styleClass=""
						title=""
						reRender="button_start_TEST,button_signal_TEST,button_mode_TEST">
						<f:attribute name="ICON_TYPE" value="ICON_TYPE_SIGNAL" />
					</a4j:commandButton>
				</p>
			</h:panelGroup>
			<h:panelGroup >
				<rich:separator height="2" lineType="solid" width="100px"/>
			</h:panelGroup>
			<h:panelGroup >
				<h:outputLabel value="Configure Mode Transitions" styleClass="program_icon_label"/><br/>
				<p align="center">
					<a4j:commandButton id="button_mode_TEST"
						image="#{programConfigureDataModel.iconURLForMode}"
						alt="Configure Mode Transitions" 
						action="#{programConfigureDataModel.businessLogicIconClickAction}"
						actionListener="#{programConfigureDataModel.businessLogicIconClickListener}"
						styleClass=""
						title=""
						reRender="button_start_TEST,button_signal_TEST,button_mode_TEST">
						<f:attribute name="ICON_TYPE" value="ICON_TYPE_MODE" />
					</a4j:commandButton>
				</p>
			</h:panelGroup>
	</h:panelGrid>		
</div>
