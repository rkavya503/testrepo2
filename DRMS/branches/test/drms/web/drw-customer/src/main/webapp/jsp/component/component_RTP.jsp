<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://akuacom.com/richext" prefix="richext"%>
<div id="overlay">
    
</div>

	<div id="RTPForecastDIV" style="display:">	
		<div class="Evtshdr_Open" style="clear:both">
			<div class="popOpenedHeader_upper" style="cursor:move">
			</div>
			<div class="popOpenedHeader_titleBar">
				<div class="EvtsOpened_title2">
					Real Time Pricing - <h:outputText value="#{drTextContext.forecastSubtitleRTP}" />
				</div>
			</div>
			<div class="popTableDiv" onmousedown="evt= event|| window.event ;Event.stop(evt);">
				<richext:dataTable 
						value="#{drFrame.commercialUIBackingBean.activeRTPPREventGroup.rtpList}" 
						var="item" 
						width="100%" 
						styleClass="EvtRTPTable" 
						cellpadding="0" 
						cellspacing="0"
						summary="table with two columns indicated date and pricing of category"
						rowClasses="EvtTableCell,EvtTableCell_nobg">		
						<rich:column sortable="false" styleClass="rich-table-cell-first">
								<f:facet name="header" >
									<h:outputText value="#{drTextContext.tblcolDateOfUsage}" escape="false" />
								</f:facet>
								<h:outputText  value="#{item.dateString}">
								</h:outputText>
						</rich:column>
						<rich:column sortable="false" styleClass="rich-table-cell-last">
								<f:facet name="header">
									<h:outputText value="#{drTextContext.tblcolPricingCategory}" escape="false"  />
								</f:facet>
								<h:outputText  value="#{item.pricingCategoryString}">
								</h:outputText>
						</rich:column>
				</richext:dataTable>
				<div class="realpricingNotes">
					
					<h:outputText value="#{drTextContext.copyrightRTPForecast}" escape="false"/>
					
				</div>
				<div class="popClose">
					<h6>
						<a style="cursor: pointer;" 
							onclick="overlayHide();$('btnOverlayshow').focus();" 
							title="close 5-day forecast" 
							onkeydown="var evt= event|| window.event;if(evt.keyCode==Event.KEY_TAB) { $('btnClose5Days').focus(); Event.stop(evt);}">
							Close
						</a>
					</h6>
				</div>
				<div class="popPanelFoot_expd"></div>
			</div>
		</div>
	</div>

<script type="text/javascript">
	new Draggable('RTPForecastDIV');
</script>

