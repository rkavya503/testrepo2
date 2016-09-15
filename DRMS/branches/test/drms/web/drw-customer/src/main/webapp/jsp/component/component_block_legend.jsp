<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://akuacom.com/richext" prefix="richext"%>
<h:panelGroup id="listViewLegendPanel" layout="block">
<div class="sideHeaderBg">Blocks</div>
      <div class="sideMiddleBgBlock">
      	<div class="sideMiddleBlockContent">

        <div class="sideFilterItemBlock">
		  <div class="block1Expand"><a href="#" onclick="expandBlock(1);"><img src="images/icons/leftGreenArrow.png" alt="expand" title="expand" /></a></div>	
          <div class="block1collapse"><a href="#" onclick="collapseBlock(1);"><img src="images/icons/downArrow.png" alt="collapse" title="collapse" align="absmiddle" /></a></div>	
          <div class="sideFCheckboxBlock"><input id="scecCheckBox" class="scec" type="checkbox" align="absmiddle" onClick="kmlShowPolyBySlab('SCEC','scecCheckBox','mapform:scecLegendPanelGroup')" /></div>
          <div class="siconBlock"><img src="images/icons/blockLegend1.gif" align="absmiddle" /></div>
          <div class="sFtextBlock">100 - 199 (SCEC)</div>
          <div class="insideBlockViewCheckBoxesHolder1">
			<h:panelGroup id="scecLegendPanelGroup" layout="block">
				<rich:dataList var="block" value="#{drBlockView.scecBlocks}" rows="100" style="list-style-type:none;">
					<h:selectBooleanCheckbox value="#{block}" style="vertical-align: middle; margin: 0px; margin-right:10px;">
						<a4j:support event="onclick" onsubmit="kmlShowPoly(this,#{block})" oncomplete="checkboxCheck('mapform:scecLegendPanelGroup','scecCheckBox')" />
					</h:selectBooleanCheckbox>
					<h:outputText  value="  #{block}"  style="vertical-align: middle; margin: 0px; margin-right:10px;align:absmiddle"></h:outputText>
				</rich:dataList>
			</h:panelGroup>
          </div>
          
        </div>
		<div class="sideFilterItemBlock">
          <div class="block5Expand"><a href="#" onclick="expandBlock(5);"><img src="images/icons/leftGreenArrow.png" alt="expand" title="expand" /></a></div>	
          <div class="block5collapse"><a href="#" onclick="collapseBlock(5);"><img src="images/icons/downArrow.png" alt="collapse" title="collapse" align="absmiddle" /></a></div>		
          <div class="sideFCheckboxBlock"><input id="schdCheckBox" class="schd" type="checkbox" align="absmiddle" onClick="kmlShowPolyBySlab('SCHD','schdCheckBox','mapform:schdLegendPanelGroup')" /></div>
          <div class="siconBlock"><img src="images/icons/blockLegend2.gif" align="absmiddle" /></div>
          <div class="sFtextBlock">200 - 299 (SCHD)</div>
          <div class="insideBlockViewCheckBoxesHolder5">
			  <h:panelGroup id="schdLegendPanelGroup" layout="block">
					<rich:dataList var="block" value="#{drBlockView.schdBlocks}" rows="100" style="list-style-type:none;">
						<h:selectBooleanCheckbox style="vertical-align: middle; margin: 0px; margin-right:10px;">	
							<a4j:support event="onclick" onsubmit="kmlShowPoly(this,#{block})" oncomplete="checkboxCheck('mapform:schdLegendPanelGroup','schdCheckBox')" />
						</h:selectBooleanCheckbox>
						<h:outputText  value="  #{block}"  style="vertical-align: middle; margin: 0px; margin-right:10px;align:absmiddle"></h:outputText>
					</rich:dataList>
				</h:panelGroup>
            </div>
        </div>
         <div class="sideFilterItemBlock">
          <div class="block6Expand"><a href="#" onclick="expandBlock(6);"><img src="images/icons/leftGreenArrow.png" alt="expand" title="expand" /></a></div>	
          <div class="block6collapse"><a href="#" onclick="collapseBlock(6);"><img src="images/icons/downArrow.png" alt="collapse" title="collapse" align="absmiddle" /></a></div>	
          <div class="sideFCheckboxBlock"><input id="scldCheckBox" class="scld" type="checkbox" align="absmiddle" onClick="kmlShowPolyBySlab('SCLD','scldCheckBox','mapform:scldLegendPanelGroup')" /></div>
          <div class="siconBlock"><img src="images/icons/blockLegend3.gif" align="absmiddle" /></div>
          <div class="sFtextBlock">300 - 399 (SCLD)</div>
          <div class="insideBlockViewCheckBoxesHolder6">
			  <h:panelGroup id="scldLegendPanelGroup" layout="block">
					<rich:dataList var="block" value="#{drBlockView.scldBlocks}" rows="100" style="list-style-type:none;">
						<h:selectBooleanCheckbox style="vertical-align: middle; margin: 0px; margin-right:10px;">
							<a4j:support event="onclick"  onsubmit="kmlShowPoly(this,#{block})" oncomplete="checkboxCheck('mapform:scldLegendPanelGroup','scldCheckBox')" >
							</a4j:support>	
						</h:selectBooleanCheckbox>
						<h:outputText  value="  #{block}"  style="vertical-align: middle; margin: 0px; margin-right:10px;align:absmiddle"></h:outputText>
					</rich:dataList>
				</h:panelGroup>
            </div>
        </div>
		<div class="sideFilterItemBlock">
          <div class="block2Expand"><a href="#" onclick="expandBlock(2);"><img src="images/icons/leftGreenArrow.png" alt="expand" title="expand" /></a></div>	
          <div class="block2collapse"><a href="#" onclick="collapseBlock(2);"><img src="images/icons/downArrow.png" alt="collapse" title="collapse" align="absmiddle" /></a></div>	
          <div class="sideFCheckboxBlock"><input id="scenCheckBox" class="scen" type="checkbox" align="absmiddle" onClick="kmlShowPolyBySlab('SCEN','scenCheckBox','mapform:scenLegendPanelGroup')" /></div>
          <div class="siconBlock"><img src="images/icons/blockLegend4V2.gif" align="absmiddle" /></div>
          <div class="sFtextBlock">400 - 499 (SCEN)</div>
          <div class="insideBlockViewCheckBoxesHolder2">
          	<h:panelGroup id="scenLegendPanelGroup" layout="block">
				<rich:dataList var="block" value="#{drBlockView.scenBlocks}" rows="100" style="list-style-type:none;">
					<h:selectBooleanCheckbox style="vertical-align: middle; margin: 0px; margin-right:10px;">	
						<a4j:support event="onclick"  onsubmit="kmlShowPoly(this,#{block})" oncomplete="checkboxCheck('mapform:scenLegendPanelGroup','scenCheckBox')" >
						</a4j:support>	
					</h:selectBooleanCheckbox>
					<h:outputText  value="  #{block}"  style="vertical-align: middle; margin: 0px; margin-right:10px;align:absmiddle"></h:outputText>
				</rich:dataList>
			</h:panelGroup>
          </div>
          
        </div>
        <div class="sideFilterItemBlock">
         <div class="block3Expand"><a href="#" onclick="expandBlock(3);"><img src="images/icons/leftGreenArrow.png" alt="expand" title="expand" /></a></div>	
          <div class="block3collapse"><a href="#" onclick="collapseBlock(3);"><img src="images/icons/downArrow.png" alt="collapse" title="collapse" align="absmiddle" /></a></div>	
          <div class="sideFCheckboxBlock"><input id="scnwCheckBox" class="scnw"type="checkbox" align="absmiddle" onClick="kmlShowPolyBySlab('SCNW','scnwCheckBox','mapform:scnwLegendPanelGroup')" /></div>
          <div class="siconBlock"><img src="images/icons/blockLegend5V2.gif" align="absmiddle" /></div>
          <div class="sFtextBlock">500 - 599 (SCNW)</div>
          <div class="insideBlockViewCheckBoxesHolder3">
			  <h:panelGroup id="scnwLegendPanelGroup" layout="block">
					<rich:dataList var="block" value="#{drBlockView.scnwBlocks}" rows="100" style="list-style-type:none;">
						<h:selectBooleanCheckbox style="vertical-align: middle; margin: 0px; margin-right:10px;">	
							<a4j:support event="onclick" onsubmit="kmlShowPoly(this,#{block})"  oncomplete="checkboxCheck('mapform:scnwLegendPanelGroup','scnwCheckBox')" />
						</h:selectBooleanCheckbox>
						<h:outputText  value="  #{block}"  style="vertical-align: middle; margin: 0px; margin-right:10px;align:absmiddle"></h:outputText>
					</rich:dataList>
				</h:panelGroup>
            </div>
        </div>
     <div class="sideFilterItemBlock">
          <div class="block4Expand"><a href="#" onclick="expandBlock(4);"><img src="images/icons/leftGreenArrow.png" alt="expand" title="expand" /></a></div>	
          <div class="block4collapse"><a href="#" onclick="collapseBlock(4);"><img src="images/icons/downArrow.png" alt="collapse" title="collapse" align="absmiddle" /></a></div>	
          <div class="sideFCheckboxBlock"><input id="scewCheckBox" class="scew" type="checkbox" align="absmiddle" onClick="kmlShowPolyBySlab('SCEW','scewCheckBox','mapform:scewLegendPanelGroup')" /></div>
          <div class="siconBlock"><img src="images/icons/blockLegend7.png" align="absmiddle" /></div>
          <div class="sFtextBlock">600 - 699 (SCEW)</div>
          <div class="insideBlockViewCheckBoxesHolder4">
          	<h:panelGroup id="scewLegendPanelGroup" layout="block">
				<rich:dataList var="block" value="#{drBlockView.scewBlocks}" rows="100" style="list-style-type:none;">
					<h:selectBooleanCheckbox style="vertical-align: middle; margin: 0px; margin-right:10px;">	
						<a4j:support event="onclick" onsubmit="kmlShowPoly(this,#{block})"  oncomplete="checkboxCheck('mapform:scewLegendPanelGroup','scewCheckBox')" />
					</h:selectBooleanCheckbox>
					<h:outputText  value="  #{block}"  style="vertical-align: middle; margin: 0px; margin-right:10px;align:absmiddle"></h:outputText>
				</rich:dataList>
			</h:panelGroup>
          </div>
        </div>
        </div>
        <div class="clearfix"></div>
      </div>
</h:panelGroup>	  