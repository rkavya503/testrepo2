
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://akuacom.com/richext" prefix="richext"%>

<h:panelGroup layout="block" id="DRCPanelGroup">
              
		  <div id="form:DRCOpenPanelGroup" style="display:none;">
          <div class="EventWrapper_OpenPanels" style="clear:both">
            <div class="EventOpenedUpperHeader">
              <div class="EventControlIcon" style="float:right; margin:0px"> <a  aria-expanded="true"  id="maxIconDRC" name="maxIconDRC" href="#minIconDRC" style="cursor: pointer;" title="collapse" onclick="switchDisplayDRCMaxToMin()"> <img src="images/icons/collapseIcon.png" alt="collapse" title="collapse" /> </a> </div>
            </div>
            <div class="EventsOpenedHeaderTitleBg">
              <div class="EventsMainTitle">
				<h3 style="font-family:Arial, Helvetica, sans-serif;font-size:14px;font-weight:bold;color:#417300;margin-top: -2px;position: relative;">
					Aggregator Managed Portfolio (AMP)
				</h3>
				<div style="margin-top:-14px;">
					<label class="EventsSubTitle" for="panelSAIExpandNoEvents">(Also known as Demand Response Contracts)</label>
				</div>
			  </div>
            </div>
            <div class="EventsOpenContDiv">
			  
			  <div class="noReportWrapper">
                <div class="noReportIcon"><img src="images/icons/NoEventIcon.png" alt="no report"/></div>
                <div class="noReportInfo" style="width:450px;">
					Contact your AMP Aggregator for more information on AMP events.  
					<br/><br/>
					For more information on SCE's AMP program, please visit 
					<a href="http://www.sce.com/wps/portal/home/business/savings-incentives/demand-response"> SCE Demand Response website </a>.
                </div>
              </div>
              <div class="clearfix"></div>
            </div>
            <div class="EventsOpenFooterBg"></div>
          </div>
          </div>
		  
		  	
          <div id="form:DRCClosePanelGroup" >
            <div class="EventWrapper_ClosePanels">
              <div class="EventOpenedUpperHeader">
                <div class="EventControlIcon" style="float:right; margin:0px"> <a  aria-expanded="false"  id="minIconDRC" name="minIconDRC" href="#maxIconDRC"  style="cursor: pointer;" title="expand" onclick="switchDisplayDRCMinToMax()"> <img src="images/icons/expandIcon.png" alt="expand" title="expand" /> </a> </div>
              </div>
              <div class="EventsCloseHeaderTitleBg">
                <div class="EventsMainTitle">
				<h3 style="font-family:Arial, Helvetica, sans-serif;font-size:14px;font-weight:bold;color:#417300;margin-top: -2px;position: relative;">
					Aggregator Managed Portfolio (AMP)
				</h3>
				<div style="margin-top:-14px;">
					<label class="EventsSubTitle" for="panelSAIExpandNoEvents">(Also known as Demand Response Contracts)</label>
				</div>
			  </div>
              </div>
              <div class="EventsCloseFooterBg"></div>
            </div>
          </div>
</h:panelGroup>		  

