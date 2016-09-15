<!DOCTYPE html>
<%@ page import="com.akuacom.pss2.drw.util.DRWUtil" %>
<html> 
<head> 
	<title>SCE Demand Response Event Status</title> 
	<meta name="viewport" content="width=device-width, initial-scale=1"> 
	<link rel="stylesheet" href="css/themes/default/jquery.mobile-1.3.0.css" />   
	<script src="js/jquery.js"></script>
	<script src="js/jquery.mobile-1.3.0.js"></script>
	<script type='text/javascript' src='js/knockout-2.3.0.js'></script>

	<script type="text/javascript" src="js/geoxml3_v1.js"></script>
	<script type="text/javascript" src="js/progressBar.js"></script>
</head> 
<body onload="loadMapsAPI()"> 
<script>
	function addScript( url, callback ) {
	    var script = document.createElement( 'script' );
	    if( callback ) script.onload = callback;
	    script.type = 'text/javascript';
	    script.src = url;
	    document.body.appendChild( script );  
	}

	function loadMapsAPI() {
		var is_chrome = navigator.userAgent.indexOf('Chrome') > -1;
		var is_explorer = navigator.userAgent.indexOf('MSIE') > -1;
		var is_firefox = navigator.userAgent.indexOf('Firefox') > -1;
		var is_safari = navigator.userAgent.indexOf("Safari") > -1;
		var is_Opera = navigator.userAgent.indexOf("Presto") > -1;
		if ((is_chrome)&&(is_safari)) {is_safari=false;}
		if(is_safari){// safari not support https
			addScript('http://maps.googleapis.com/maps/api/js?key=<%=DRWUtil.getSystemManager().getPropertyByName("mapKey").getStringValue()%>&sensor=false&callback=initConfig' );
		}else if( window.location.protocol == "http:"){
			addScript('http://maps.googleapis.com/maps/api/js?key=<%=DRWUtil.getSystemManager().getPropertyByName("mapKey").getStringValue()%>&sensor=false&callback=initConfig' );
		}else{
			addScript('https://maps.googleapis.com/maps/api/js?key=<%=DRWUtil.getSystemManager().getPropertyByName("mapKey").getStringValue()%>&sensor=false&callback=initConfig' );
		}
	}

</script>
<div data-role="page"  id="list-view">
<form id="form">	
	<div data-role="header" data-theme="c">
		<div class="logoHolder"><img src="images/sce-logo.png" width="127" height="39"/></div>
        <div class="ui-btn-right" data-role="controlgroup" data-type="horizontal" style="margin-top:15px;">
     
      <a href="index.html"  data-ajax="false" rel="external"><img src="images/home-btn.png" width="23" height="24"/></a>
    </div>
       
	</div><!-- /header -->
		  <div data-role="content" style="clear:both">
          <div data-role="navbar">
            <ul>
                <li><a id="mapviewButton" data-theme="b"  data-ajax="false" onClick="switchView(false)">Map</a></li>
                <li><a id="listviewButton" data-theme="b" data-ajax="false" onClick="switchView(true)">List</a></li>				
            </ul>
          </div><!-- /navbar -->
          </div>
          <div data-role="content">
          	<!-- radio selection start here -->
          	<div align="center" style="padding-top:10px;padding-bottom:10px;">
           
                <fieldset data-role="controlgroup" data-mini="true" data-type="horizontal" >
                   
					<input name="radio-choice-t-6" id="radio-choice-t-6a" type="radio" checked="checked" value="on" data-theme="c" onClick="reloadData('A')">
                    <label for="radio-choice-t-6a" id="activeLabel">Active Events</label>
                    <input name="radio-choice-t-6" id="radio-choice-t-6b" type="radio" value="off" data-theme="c" onClick="reloadData('S')">
                    <label for="radio-choice-t-6b" id="scheduleLabel">Scheduled Events</label>
                </fieldset>
            
            </div>
            <!-- radio selection ends here -->
            <!-- map search field start here -->
            <div class="mapview">
				<div class="mapview-search-wrapper">
					<table style="width:100%">
					<tr>
						<td><input data-clear-btn="true" name="address" id="address" value="" type="text" ></td>
						<td width="30"><a href="#" onClick="codeAddress()" data-role="button"  data-mini="true" data-theme="d" data-corners="false">Search</a></td>
					</tr>
					</table>
				</div>
			</div>
            <!-- legend start here -->
            <div id="legendDIV" data-role="collapsible" class="collapseHeader" data-inset="false" data-iconpos="right" data-theme="c">
                <h4>Legend</h4>
				<!----------------------------------------------------- LEGEND[SDPR]  ------------------------------------------------------->
				<div class="sdprActiveTableList">
                	<table class="outsideCheckboxesTable">
                    	<tr>
                        	<td width="50" class="mapViewMainCheckbox">
								<input onClick="checkAllHandler(this)" type="checkbox" align="absmiddle" name="SDPRProgramCheckbox" />
							</td>
                            <td width="30"><img src="images/icons/S1.png" width="19" height="19" alt="S" align="absmiddle" /></td>
                            <td><label data-bind="text: sdpTitle"></label> - Residential</td>
                        </tr>
                    </table>
					<table class="insideCheckboxesTable" data-bind="foreach: sdprEvts">
                    	<tr>
                        	<td width="40" class="mapViewMainCheckbox">
								<input 
									data-bind="click:$parent.clickFunction,attr:{id:eventKey+'checkbox'},checked: $data.check" 
									type="checkbox" align="absmiddle" name="sdprEventCheckbox" onClick="loadEventKML(this,'SDPR','SDPRProgramCheckbox');"/>
							</td>
                            <td>
								<label data-bind="text: startDate.substring(0,5).replace('-','/')+': '+startTime+' - '+endTime"></label>
							</td>
                        </tr>
                    </table>
                </div>
				<!----------------------------------------------------- LEGEND[SDPC]  ------------------------------------------------------->
				<div class="sdpcActiveTableList">
                	<table class="outsideCheckboxesTable">
                    	<tr>
                        	<td width="50" class="mapViewMainCheckbox">
								<input onClick="checkAllHandler(this)" type="checkbox" align="absmiddle" name="SDPCProgramCheckbox" />
							</td>
                            <td width="30"><img src="images/icons/S2.png" width="19" height="19" alt="S" align="absmiddle" /></td>
                            <td><label data-bind="text: sdpTitle"></label> - Commercial</td>
                            
                        </tr>
                    </table>
                    <table class="insideCheckboxesTable" data-bind="foreach: sdpcEvts">
                    	<tr>
                        	<td width="40" class="mapViewMainCheckbox">
								<input 
									data-bind="click:$parent.clickFunction,attr:{id:eventKey+'checkbox'},checked: $data.check" 
									type="checkbox" align="absmiddle" name="sdpcEventCheckbox" onClick="loadEventKML(this,'SDPC','SDPCProgramCheckbox');"/>
							</td>
                            <td><label data-bind="text: startDate.substring(0,5).replace('-','/')+': '+startTime+' - '+endTime"></label>
							</td>
                        </tr>
                    </table>
                </div>
				<!----------------------------------------------------- LEGEND[API]  ------------------------------------------------------->
				<div class="apiActiveTableList">
                	<table class="outsideCheckboxesTable">
                    	<tr>
                        	<td width="50" class="mapViewMainCheckbox">
								<input onClick="checkAllHandler(this)" type="checkbox" align="absmiddle" name="APIProgramCheckbox" />
							</td>
                            <td width="30"><img src="images/icons/A.png" width="19" height="19" alt="S" align="absmiddle" /></td>
                            <td><label data-bind="text: apiTitle"></label></td>
                        </tr>
                    </table>
                    <table class="insideCheckboxesTable" data-bind="foreach: apiEvts">
                    	<tr>
                        	<td width="40" class="mapViewMainCheckbox">
								<input 
									data-bind="click:$parent.clickFunction,attr:{id:eventKey+'checkbox'},checked: $data.check" 
									type="checkbox" align="absmiddle" name="apiEventCheckbox" onClick="loadEventKML(this,'API','APIProgramCheckbox');"/>
							</td>
                            <td><label data-bind="text: startDate.substring(0,5).replace('-','/')+': '+startTime+' - '+endTime"></label></td>
                        </tr>
                    </table>
                </div>
				<!----------------------------------------------------- LEGEND[CBP]  ------------------------------------------------------->
				<div class="cbpActiveTableList">
                	<table class="outsideCheckboxesTable">
                    	<tr>
                        	<td width="50" class="mapViewMainCheckbox">
								<input onClick="checkAllHandler(this)" type="checkbox" align="absmiddle" name="CBPProgramCheckbox" />
							</td>
                            <td width="30"><img src="images/icons/C.png" width="19" height="19" alt="S" align="absmiddle" /></td>
                            <td><label data-bind="text: cbpTitle"></label></td>
                        </tr>
                    </table>
                    <table class="insideCheckboxesTable" data-bind="foreach: cbpEvts">
                    	<tr>
                        	<td width="40" class="mapViewMainCheckbox">
								<input 
									data-bind="click:$parent.clickFunction,attr:{id:eventKey+'checkbox'},checked: $data.check" 
									type="checkbox" align="absmiddle" name="cbpEventCheckbox" onClick="loadEventKML(this,'CBP','CBPProgramCheckbox');"/>
							</td>
                            <td><label data-bind="text: startDate.substring(0,5).replace('-','/')+': '+startTime+' - '+endTime"></label></td>
                        </tr>
                    </table>
                </div>
                <!----------------------------------------------------- LEGEND[BIP]  ------------------------------------------------------->
				<div class="bipActiveTableList">
                	<table class="outsideCheckboxesTable">
                    	<tr>
                        	<td width="50" class="mapViewMainCheckbox">
								<input onClick="checkAllHandler(this)" type="checkbox" align="absmiddle" name="BIPProgramCheckbox" />
							</td>
                            <td width="30"><img src="images/icons/B.png" width="19" height="19" alt="S" align="absmiddle" /></td>
                            <td><label data-bind="text: bipTitle"></label></td>
                        </tr>
                    </table>
					<table class="insideCheckboxesTable" data-bind="foreach: bipEvts">
                    	<tr>
                        	<td width="40" class="mapViewMainCheckbox">
								<input 
									data-bind="click:$parent.clickFunction,attr:{id:eventKey+'checkbox'},checked: $data.check" 
									type="checkbox" align="absmiddle" name="bipEventCheckbox" onClick="loadEventKML(this,'BIP','BIPProgramCheckbox');"/>
							</td>
                            <td><label data-bind="text: startDate.substring(0,5).replace('-','/')+': '+startTime+' - '+endTime"></label></td>
                        </tr>
                    </table>
				</div>
                 
                <!-- legend for home icon -->
				<div class="mapview">
					<div class="homeIconHolder">
						<table class="outsideCheckboxesTable">
							<tr>
								
								<td width="30"><img src="images/icons/home.png" width="24" height="30" alt="Home Icon" align="absmiddle" /></td>
								<td>Your Location</td>
								
							</tr>
						</table>
					</div>
				</div>
            </div>

            <!-- legend ends here -->
			
            <!-- map ends here -->
			
			<div class="listview">
			<!----------------------------------------------------- DIV[SDPR]  ------------------------------------------------------->
			<div id="SDPRDIV" style="margin-left: 2%;margin-right: 2%;margin-top: 10px;">
					<table border="0" cellpadding="0" cellspacing="0" width="100%">
						<tr><td style="border: solid 1px #CCC;">
							<table border="0" cellpadding="0" cellspacing="0" width="100%">
								<tr valign="middle">
									<td width="30" height="50" bgcolor="#fb1be7"></td>
									<td width="10"></td>
									<td class="list-h1" style="padding-right:10px;"><label data-bind="text: sdpTitle"></label> - Residential</td>
								</tr>
							</table>
						</td></tr>
					 </table>
					 <!-- start of dynamically generated events for bip active-->
					 <div  data-bind="foreach: sdprEvts">
					 <div data-bind="attr:{name:eventKey+'div'}" class="eventLoopDIV" style="display:none">
					  <hr>
					 <table class="list-result-table">
							<tr class="list-result-alt">
							  <th width="30%">Product</th>
							  <td width="30%"><label data-bind="text: formatProduct(product)"></label></td>
							</tr> 
							<tr>
							  <th>Start Date</th>
							  <td><label data-bind="text: startDate"></label></td>
							</tr> 
							<tr class="list-result-alt">
							  <th>End Date</th>
							  <td><label data-bind="text: endDate"></label></td>
							</tr> 
							<tr>  
							  <th>Start Time</th>
							  <td><label data-bind="text: startTime"></label></td>
							</tr> 
							<tr class="list-result-alt">  
								  <th>Estimated End Time</th>
								  <td><label data-bind="text: endTime"></label></td>
							</tr> 
						</table>
						<hr>
						<table class="list-result-table">
							<tr class="list-result-alt">
							  <th width="30%">County</th>
							  <td width="30%"><label data-bind="text: formatString(countyNames)"></label></td>
							</tr> 
							<tr>
							  <th>City</th>
							  <td><label data-bind="text: formatString(cityNames)"></label></td>
							</tr> 
							<tr class="list-result-alt">
							  <th>ZIP Code</th>
							  <td><label data-bind="text: formatString(zipCodes)"></label></td>
							</tr> 
						</table> 
						<div class="list-events-divider"></div>
					 </div>	
					 </div>
					<div data-bind="visible: sdprEvts().length==0">
						<hr style="height:1px"/>
						<label>No Events in Progress</label>
						<hr style="height:1px;margin-top: 10px;"/>
                    </div> 
					<div class="list-legal" data-bind="visible: sdprEvts().length>0" >
						End times shown for SDP Residential events are estimates only and subject to change without notice. SCE may not be able to estimate end times for all SDP Residential events.
					</div> 
			</div>
			<!----------------------------------------------------- DIV[SDPC]  ------------------------------------------------------->
			<div id="SDPCDIV" style="margin-left: 2%;margin-right: 2%;margin-top: 10px;">
            	<table border="0" cellpadding="0" cellspacing="0" width="100%">
                	<tr><td style="border: solid 1px #CCC;">
                        <table border="0" cellpadding="0" cellspacing="0" width="100%">
                            <tr valign="middle">
                                <td width="30" height="50" bgcolor="#8d1d99"></td>
                                <td width="10"></td>
                                <td class="list-h1" style="padding-right:10px;"><label data-bind="text: sdpTitle"></label> - Commercial</td>
                            </tr>
                        </table>
                	</td></tr>
                 </table>
                 <div data-bind="foreach: sdpcEvts">
						 <div data-bind="attr:{name:eventKey+'div'}" class="eventLoopDIV" style="display:none">
						  <hr>
						 <table class="list-result-table">
								<tr class="list-result-alt">
								  <th width="30%">Product</th>
								  <td width="30%"><label data-bind="text: formatProduct(product)"></label></td>
								</tr> 
								<tr>
								  <th>Start Date</th>
								  <td><label data-bind="text: startDate"></label></td>
								</tr> 
								<tr class="list-result-alt">
								  <th>End Date</th>
								  <td><label data-bind="text: endDate"></label></td>
								</tr> 
								<tr>  
								  <th>Start Time</th>
								  <td><label data-bind="text: startTime"></label></td>
								</tr> 
								<tr class="list-result-alt">  
								  <th>Estimated End Time</th>
								  <td><label data-bind="text: endTime"></label></td>
								</tr> 
							</table>
							<hr>
							<table class="list-result-table">
							<tr class="list-result-alt">
							  <th width="30%">County</th>
							  <td width="30%"><label data-bind="text: formatString(countyNames)"></label></td>
							</tr> 
							<tr>
							  <th>City</th>
							  <td><label data-bind="text: formatString(cityNames)"></label></td>
							</tr> 
							<tr class="list-result-alt">
							  <th>ZIP Code</th>
							  <td><label data-bind="text: formatString(zipCodes)"></label></td>
							</tr> 
						</table> 
							<div class="list-events-divider"></div>
						 </div>	
					 </div>
					<div data-bind="visible: sdpcEvts().length==0">
						<hr style="height:1px"/>
						<label>No Events in Progress</label>
						<hr style="height:1px;margin-top: 10px;"/>
                    </div> 
					<div class="list-legal" data-bind="visible: sdpcEvts().length>0" >
						End Times for SDP Commercial events are determined by grid reliability and cannot be estimated by SCE.
					</div>
            </div>
			<!----------------------------------------------------- DIV[API]  ------------------------------------------------------->
			<div id="APIDIV" style="margin-left: 2%;margin-right: 2%;margin-top: 10px;">
            	<table border="0" cellpadding="0" cellspacing="0" width="100%">
                	<tr><td style="border: solid 1px #CCC;">
                        <table border="0" cellpadding="0" cellspacing="0" width="100%">
                            <tr valign="middle">
                                <td width="30" height="50" bgcolor="#f12420"></td>
                                <td width="10"></td>
                                <td class="list-h1" style="padding-right:10px;"><label data-bind="text: apiTitle"></label></td>
                            </tr>
                        </table>
                	</td></tr>
                 </table>
                 <div data-bind="foreach: apiEvts">
						 <div data-bind="attr:{name:eventKey+'div'}" class="eventLoopDIV" style="display:none">
						  <hr>
						 <table class="list-result-table">
								<tr class="list-result-alt">
								  <th width="30%">Product</th>
								  <td width="30%"><label data-bind="text: formatProduct(product)"></label></td>
								</tr> 
								<tr>
								  <th>Start Date</th>
								  <td><label data-bind="text: startDate"></label></td>
								</tr> 
								<tr class="list-result-alt">
								  <th>End Date</th>
								  <td><label data-bind="text: endDate"></label></td>
								</tr> 
								<tr>  
								  <th>Start Time</th>
								  <td><label data-bind="text: startTime"></label></td>
								</tr> 
								<tr class="list-result-alt">  
								  <th>End Time</th>
								  <td><label data-bind="text: endTime"></label></td>
								</tr> 
								<tr>  
								  <th>Blocks</th>
								  <td><label data-bind="text: locationBlock"></label></td>
								</tr> 
							</table>
							<hr>
							<table class="list-result-table">
							<tr class="list-result-alt">
							  <th width="30%">County</th>
							  <td width="30%"><label data-bind="text: formatString(countyNames)"></label></td>
							</tr> 
							<tr>
							  <th>City</th>
							  <td><label data-bind="text: formatString(cityNames)"></label></td>
							</tr> 
							<tr class="list-result-alt">
							  <th>ZIP Code</th>
							  <td><label data-bind="text: formatString(zipCodes)"></label></td>
							</tr> 
						</table> 
							<div class="list-events-divider"></div>
						 </div>	
					 </div>
					
					<div data-bind="visible: apiEvts().length==0">
						<hr style="height:1px"/>
						<label>No Events in Progress</label>
						<hr style="height:1px;margin-top: 10px;"/>
                    </div> 
					
					<div class="list-legal" data-bind="visible: apiEvts().length>0" >
						End Times for AP-I events are determined by grid reliability and cannot be estimated by SCE. Courtesy notifications will be sent to subscribed participants at the end of the event.
					</div>	
            </div>
			<!----------------------------------------------------- DIV[CBP]  ------------------------------------------------------->
			<div id="CBPDIV" style="margin-left: 2%;margin-right: 2%;margin-top: 10px;">
					<table border="0" cellpadding="0" cellspacing="0" width="100%">
						<tr><td style="border: solid 1px #CCC;">
							<table border="0" cellpadding="0" cellspacing="0" width="100%">
								<tr valign="middle">
									<td width="30" height="50" bgcolor="#CC9900"></td>
									<td width="10"></td>
									<td class="list-h1" style="padding-right:10px;"><label data-bind="text: cbpTitle"></label></td>
								</tr>
							</table>
						</td></tr>
					 </table>
					 <div class="bip-event-active" data-bind="foreach: cbpEvts">
						 <div data-bind="attr:{name:eventKey+'div'}" class="eventLoopDIV" style="display:none">
						  <hr>
						 <table class="list-result-table">
								<tr class="list-result-alt">
								  <th width="30%">Product</th>
								  <td width="30%"><label data-bind="text: formatProduct(product)"></label></td>
								</tr> 
								<tr>
								  <th>Start Date</th>
								  <td><label data-bind="text: startDate"></label></td>
								</tr> 
								<tr class="list-result-alt">
								  <th>End Date</th>
								  <td><label data-bind="text: endDate"></label></td>
								</tr> 
								<tr>  
								  <th>Start Time</th>
								  <td><label data-bind="text: startTime"></label></td>
								</tr> 
								<tr class="list-result-alt">  
								  <th>End Time</th>
								  <td><label data-bind="text: endTime"></label></td>
								</tr> 
								<tr>  
								  <th>Blocks</th>
								  <td><label data-bind="text: locationBlock"></label></td>
								</tr> 
							</table>
							<hr>
							<table class="list-result-table">
							<tr class="list-result-alt">
							  <th width="30%">County</th>
							  <td width="30%"><label data-bind="text: formatString(countyNames)"></label></td>
							</tr> 
							<tr>
							  <th>City</th>
							  <td><label data-bind="text: formatString(cityNames)"></label></td>
							</tr> 
							<tr class="list-result-alt">
							  <th>ZIP Code</th>
							  <td><label data-bind="text: formatString(zipCodes)"></label></td>
							</tr> 
						</table> 
							<div class="list-events-divider"></div>
						 </div>	
					 </div>
					<div data-bind="visible: cbpEvts().length==0">
						<hr style="height:1px"/>
						<label>No Events in Progress</label>
						<hr style="height:1px;margin-top: 10px;"/>
                    </div> 
					<div class="list-legal" data-bind="visible: cbpEvts().length>0" >
						This web site displays CBP events activated by SCE. Check with your CBP Aggregator for details of your participation in CBP events.
					</div>
			</div>
			<!----------------------------------------------------- DIV[BIP]  ------------------------------------------------------->
			<div id="BIPDIV" style="margin-left: 2%;margin-right: 2%;margin-top: 10px;">
					<table border="0" cellpadding="0" cellspacing="0" width="100%">
						<tr><td style="border: solid 1px #CCC;">
							<table border="0" cellpadding="0" cellspacing="0" width="100%">
								<tr valign="middle">
									<td width="30" height="50" bgcolor="#000000"></td>
									<td width="10"></td>
									<td class="list-h1" style="padding-right:10px;"><label data-bind="text: bipTitle"></label></td>
								</tr>
							</table>
						</td></tr>
					 </table>
					 <!-- start of dynamically generated events for bip active-->
					 <div class="bip-event-active" data-bind="foreach: bipEvts">
					 <div data-bind="attr:{name:eventKey+'div'}" class="eventLoopDIV" style="display:none">
					  <hr>
					 <table class="list-result-table">
							<tr class="list-result-alt">
							  <th width="30%">Product</th>
							  <td width="30%"><label data-bind="text: formatProduct(product)"></label></td>
							</tr> 
							<tr>
							  <th>Start Date</th>
							  <td><label data-bind="text: startDate"></label></td>
							</tr> 
							<tr class="list-result-alt">
							  <th>End Date</th>
							  <td><label data-bind="text: endDate"></label></td>
							</tr> 
							<tr>  
							  <th>Start Time</th>
							  <td><label data-bind="text: startTime"></label></td>
							</tr> 
							<tr class="list-result-alt">  
							  <th>End Time</th>
							  <td><label data-bind="text: endTime"></label></td>
							</tr> 
							<tr>  
							  <th>Blocks</th>
							  <td><label data-bind="text: locationBlock"></label></td>
							</tr> 
						</table>
						<hr>
						<table class="list-result-table">
							<tr class="list-result-alt">
							  <th width="30%">County</th>
							  <td width="30%"><label data-bind="text: formatString(countyNames)"></label></td>
							</tr> 
							<tr>
							  <th>City</th>
							  <td><label data-bind="text: formatString(cityNames)"></label></td>
							</tr> 
							<tr class="list-result-alt">
							  <th>ZIP Code</th>
							  <td><label data-bind="text: formatString(zipCodes)"></label></td>
							</tr> 
						</table> 
						<div class="list-events-divider"></div>
					 </div>	
					 </div>  
					 <div data-bind="visible: bipEvts().length==0">
						<hr style="height:1px"/>
						<label>No Events in Progress</label>
						<hr style="height:1px;margin-top: 10px;"/>
                    
                    </div> 
					<div class="list-legal" data-bind="visible: bipEvts().length>0" >
						End Times for BIP events are determined by grid reliability and cannot be estimated by SCE. Customers will be notified when the event has been terminated. 
					</div>
			</div>
			</div>
			<!-- map start here -->
			<div class="mapview">
				<div id="map" style="height:300px;; width:90%; margin-left:5%; margin-right:5%; margin-top:20px;"></div>
			</div>
          </div>
	<div class="listview">	  
		<hr>
		<div class="footerLinks" style="text-align:left;font-size:11px;padding:10px;padding-top:20px; font-weight:normal;color:#666 !important;">ZIP Codes listed above indicate some customers within the area are impacted by an event. Not all customers in a listed ZIP Code will necessarily be impacted. SCE makes this data available as a courtesy only and cannot guarantee complete accuracy in the information provided.
		</div>
		<br><br><br>
	</div>
	<div class="mapview">	  
		<hr>
		<div class="footerLinks" style="text-align:left;font-size:11px;padding:10px;padding-top:20px; font-weight:normal;color:#666 !important;">Shaded figures in the map above show an approximation of an event area. SCE makes this data available as a courtesy only and cannot guarantee complete accuracy in the information provided.
		</div>
		<br><br><br>
	</div>
</form>
<div data-role="footer" data-position="fixed" data-fullscreen="true">
    	<div class="footerWrapper">
		<div class="footerNavLeft"><a href="#relative-link-page" data-rel="dialog" style="color:#FFF; text-decoration:none;">Related Links</a></div>
        </div>
	</div>	
</div><!-- /page -->
<div id="relative-link-page">
<table style="width:100%">
              	
</table>
       
</div>

<script>
    var map;
	var pyrmont;
	var geocoder;
	var keyword;
	var markersArray=[];	
	var inputLat;
	var inputLong;
	var address;
	var marker;	
	var geoXml = null;
	var pb = null;
	var lb = null;
	var cur = 0;
	var geoXmlDoc = null;
	var drawingHoleEnable = true;
	var defaultBounds;// = new google.maps.LatLngBounds(new google.maps.LatLng(33.198609, -120.52905399999997), new google.maps.LatLng(38.405354, -114.13122199999998));
	var $j=jQuery.noConflict();
	/**
	*	function for parse the url request paramters from the other url
	*/
	function getURLParameter(name) {
		return decodeURIComponent((new RegExp('[?|&]' + name + '=' + '([^&;]+?)(&|#|;|$)').exec(location.search)||[,""])[1].replace(/\+/g, '%20'))||null;
	}


	//------------------------------------------------------------------listview function---------------------------------------------------------
	/**
	*	map for interaction between program checkbox and event checkbox
	*/
	var checkboxInteractionMap = {
		'SDPRProgramCheckbox':'sdprEventCheckbox',
		'SDPCProgramCheckbox':'sdpcEventCheckbox',
		'APIProgramCheckbox':'apiEventCheckbox',
		'CBPProgramCheckbox':'cbpEventCheckbox',
		'BIPProgramCheckbox':'bipEventCheckbox'
	};
	/**
	*	map for interaction between program checkbox and program name
	*/
	var checkboxProgramMap = {
		'SDPRProgramCheckbox':'sdpr',
		'SDPCProgramCheckbox':'sdpc',
		'APIProgramCheckbox':'api',
		'CBPProgramCheckbox':'cbp',
		'BIPProgramCheckbox':'bip'
	};
	/**
	*	map for interaction between program checkbox and list view program div
	*/
	var displayInteractionMap = {
		'SDPRProgramCheckbox':'SDPRDIV',
		'SDPCProgramCheckbox':'SDPCDIV',
		'APIProgramCheckbox':'APIDIV',
		'CBPProgramCheckbox':'CBPDIV',
		'BIPProgramCheckbox':'BIPDIV'
	};
	var initFlag = true;
	/**
	*	function for initialize the whole page component
	*/
	function initialize(){
		$j.each( $j( ":checkbox" ), function( index, value ){
			value.checked=false;
		});
		$j( "#APIDIV" ).css( "display", "none" );
		$j( "#BIPDIV" ).css( "display", "none" );
		$j( "#CBPDIV" ).css( "display", "none" );
		$j( "#SDPRDIV" ).css( "display", "none" );
		$j( "#SDPCDIV" ).css( "display", "none" );
		var eventKey = getURLParameter("eventKey");
		var isListView = getURLParameter("isListView");
		var isActive = getURLParameter("isActive");
		if(isListView=="true"||isListView==null){
			$j( "#listviewButton" ).attr( "class", "ui-btn-active" );
			//switchView(true);
		}else{
			$j( "#mapviewButton" ).attr( "class", "ui-btn-active" );
			//switchView(false);
		}
		if(eventKey){
			vm.eventUUID=eventKey;
		}
		if(isActive=="true"||isActive==null){
			vm.isActive=true;
			//$j( "#radio-choice-t-6a" ).click(); // load data
		}else{
			vm.isActive=false;
			//$j( "#radio-choice-t-6b" ).click(); // load data
		}
	}
	function initConfig(){
		addScript('js/label.js');
		var eventKey = getURLParameter("eventKey");
		var isListView = getURLParameter("isListView");
		var isActive = getURLParameter("isActive");
		if(isListView=="true"||isListView==null){
			switchView(true);
		}else{
			switchView(false);
		}
		if(eventKey){
			vm.eventUUID=eventKey;
		}
		if(isActive=="true"||isActive==null){
			$j( "#radio-choice-t-6a" ).click(); // load data
			$j("#activeLabel").attr("class","ui-radio-on ui-first-child ui-btn-active ui-btn ui-btn-corner-all ui-mini ui-btn-icon-left ui-btn-up-c");
			$j("#scheduleLabel").attr("class","ui-radio-off ui-last-child ui-btn ui-btn-corner-all ui-mini ui-btn-icon-left ui-btn-up-c");
		}else{
			$j( "#radio-choice-t-6b" ).click(); // load data
			$j("#scheduleLabel").attr("class","ui-radio-on ui-last-child ui-btn-active ui-btn ui-btn-corner-all ui-mini ui-btn-icon-left ui-btn-up-c");
			$j("#activeLabel").attr("class","ui-radio-off ui-first-child ui-btn ui-btn-corner-all ui-mini ui-btn-icon-left ui-btn-up-c");
		}
	}
	/**
	*	function for listening the program checkbox click event
	*/
	function checkAllHandler(obj){
		var checked = obj.checked;
		if($j( ".listview" ).css( "display")=="none"){
			//map view 
			for(key in checkboxInteractionMap){
				if(obj.name==key){
					$j.each( $j( "input[name='"+checkboxInteractionMap[key]+"']" ), function( index, value ){
						value.checked=checked;
					});
				}
			}
			resetMap();
			refreshMapView();
		}else{
			//list view
			for(key in checkboxInteractionMap){
				if(obj.name==key){
					$j.each( $j( "input[name='"+checkboxInteractionMap[key]+"']" ), function( index, value ){
						if(value.checked!=checked){
							value.click();
						}
					});
				}
			}
			for(key in displayInteractionMap){
				if(obj.name==key){
					if(checked){
						$j( "#"+displayInteractionMap[key]+"" ).css( "display", "" );
					}else{
						$j( "#"+displayInteractionMap[key]+"" ).css( "display", "none" );
					}
				}
			}
		}
	} 
	/**
	*	function for parse json ajax response data
	*/
	function parseResult(input){
		if(input) {
			var output = [].concat(input);
			$j.each( output, function( index, value ){
				value.check=false;
			});
			return output;
		}
		return [];
	}
	/**
	*	warp function for json ajax request
	*/
	function addTask(url_value,callback){
		$j.ajax({
			url: url_value,
			dataType: "json"
		}).done(function(response) {
			callback(response);
		}).fail(function(){
			alert( "ERROR:Retrieve system service error,can't fetch response data." );
		});
	}	
	/**
	*	VIEWMODEL instance
	*/
	function ViewModel(myMap){
		var self=this;
		self.eventUUID = ko.observable("");
		self.isActive = true;
		
		self.apiEvts=ko.observableArray();
		self.cbpEvts=ko.observableArray();
		self.bipEvts=ko.observableArray();
		self.sdprEvts=ko.observableArray();
		self.sdpcEvts=ko.observableArray();
		
		self.timestamp_apiEvts=ko.observable(0);
		self.timestamp_cbpEvts=ko.observable(0);
		self.timestamp_bipEvts=ko.observable(0);
		self.timestamp_sdprEvts=ko.observable(0);
		self.timestamp_sdpcEvts=ko.observable(0);
		
		self.apiTitle=ko.observable(0);
		self.bipTitle=ko.observable(0);
		self.cbpTitle=ko.observable(0);
		self.sdpTitle=ko.observable(0);
		/**
		*	function for synchronize the event data from server to client
		*/
		self.sync= function() {
			var url="resteasy/services/getEventStore?";
			if(self.isActive){
				url += "categoryNames=API_COMMERCIAL_ACTIVE&categoryNames=CBP_COMMERCIAL_ACTIVE&categoryNames=SDP_COMMERCIAL_ACTIVE&categoryNames=SDP_RESIDENTIAL_ACTIVE&categoryNames=BIP_COMMERCIAL_ACTIVE";
			}else{
				url += "categoryNames=API_COMMERCIAL_SCHEDULED&categoryNames=CBP_COMMERCIAL_SCHEDULED&categoryNames=SDP_COMMERCIAL_SCHEDULED&categoryNames=SDP_RESIDENTIAL_SCHEDULED&categoryNames=BIP_COMMERCIAL_SCHEDULED";
			}
			addTask(url, function(allData) {
				$j.map(parseResult(allData.eventStore.events.map),function(item,index) {
					if (("API_COMMERCIAL_ACTIVE" == item.key)||("API_COMMERCIAL_SCHEDULED"== item.key)){
						self.apiEvts(parseResult(item.eventsList));
					}
					if (("CBP_COMMERCIAL_ACTIVE" == item.key)||("CBP_COMMERCIAL_SCHEDULED"== item.key)){
						self.cbpEvts(parseResult(item.eventsList));
					}
					if (("SDP_COMMERCIAL_ACTIVE" == item.key)||("SDP_COMMERCIAL_SCHEDULED"== item.key)){
						self.sdpcEvts(parseResult(item.eventsList));
					}
					if (("SDP_RESIDENTIAL_ACTIVE" == item.key)||("SDP_RESIDENTIAL_SCHEDULED"== item.key)){
						self.sdprEvts(parseResult(item.eventsList));
					}
					if (("BIP_COMMERCIAL_ACTIVE" == item.key)||("BIP_COMMERCIAL_SCHEDULED"== item.key)){
						self.bipEvts(parseResult(item.eventsList));
					}
				});
				
				$j.map(parseResult(allData.eventStore.lastUpdateTime.item),function(item,index) {
					if (("API_COMMERCIAL_ACTIVE" == item.key)||("API_COMMERCIAL_SCHEDULED"== item.key)){
						self.timestamp_apiEvts(item.value);
					}
					if (("CBP_COMMERCIAL_ACTIVE" == item.key)||("CBP_COMMERCIAL_SCHEDULED"== item.key)){
						self.timestamp_cbpEvts(item.value);
					}
					if (("SDP_COMMERCIAL_ACTIVE" == item.key)||("SDP_COMMERCIAL_SCHEDULED"== item.key)){
						self.timestamp_sdpcEvts(item.value);
					}
					if (("SDP_RESIDENTIAL_ACTIVE" == item.key)||("SDP_RESIDENTIAL_SCHEDULED"== item.key)){
						self.timestamp_sdprEvts(item.value);
					}
					if (("BIP_COMMERCIAL_ACTIVE" == item.key)||("BIP_COMMERCIAL_SCHEDULED"== item.key)){
						self.timestamp_bipEvts(item.value);
					}
				});
				if(self.eventUUID){
					$j("#"+self.eventUUID+"checkbox").click();
				}
			}); 
			
			addTask("resteasy/services/getPrograms", function(allData) {
				
				$j.each(allData, function(key, val) {
					//self.programs.push(val.program);
					if(val.program.programClass=="API"){
						self.apiTitle(val.program.utilityName);
					}
					if(val.program.programClass=="BIP"){
						self.bipTitle(val.program.utilityName);
					}
					if(val.program.programClass=="CBP"){
						self.cbpTitle(val.program.utilityName);
					}
					if(val.program.programClass=="SDP"){
						self.sdpTitle(val.program.utilityName);
					}
				});
			}); 
			
			if($j( ".listview" ).css( "display")=="none"){
								//map view
								resetMap();
								refreshMapView();
			}
			return self;
		};
		
		
		/**
		* function for listening the event checkbox click event
		*/
		self.clickFunction = function(model){
			var checked = false;
			if(typeof model.check == 'undefined'){
				checked = true;
			}else{
				checked = !model.check;
			}
			//var eventUUID=model.uuid;
			if($j( ".listview" ).css( "display")=="none"){
				if(checked){
					var product = model.product;
					if(product=="APS"||product=="APS-E"){
						$j("input[name='SDPCProgramCheckbox']").prop('checked',true).checkboxradio("refresh");
					}else if(product=="SDP"){
						$j("input[name='SDPRProgramCheckbox']").prop('checked',true).checkboxradio("refresh");
					}else{
						$j("input[name='"+model.program+"ProgramCheckbox']").prop('checked',true).checkboxradio("refresh");
					}
				}
			}else{
				//list view
				if(checked){
					$j("div[name='"+model.eventKey+"div']").css( "display", "" );
					var product = model.product;
					if(product=="APS"||product=="APS-E"){
						$j("input[name='SDPCProgramCheckbox']").prop('checked',true).checkboxradio("refresh");
						$j( "#SDPCDIV" ).css( "display", "" );
					}else if(product=="SDP"){
						$j("input[name='SDPRProgramCheckbox']").prop('checked',true).checkboxradio("refresh");
						$j( "#SDPRDIV" ).css( "display", "" );
					}else{
						$j("input[name='"+model.program+"ProgramCheckbox']").prop('checked',true).checkboxradio("refresh");
						$j( "#"+model.program+"DIV" ).css( "display", "" );
					}
				}else{
					$j("div[name='"+model.eventKey+"div']").css( "display", "none" );
					
					checkCheckbox();		
					
				}
			}
			
			checkCheckbox();
			return true;
		}
		
		
		self.getLastUpdateTime= function() {
			var _url="resteasy/services/getLastUpdateTime?";
			for (key in self.map){
				_url += "category="+key+"&";
			}
			if(self.isActive){
				_url+="category=API_COMMERCIAL_ACTIVE&category=CBP_COMMERCIAL_ACTIVE&category=SDP_COMMERCIAL_ACTIVE&category=SDP_RESIDENTIAL_ACTIVE&category=BIP_COMMERCIAL_ACTIVE";
			}else{
				_url+= "category=API_COMMERCIAL_SCHEDULED&category=CBP_COMMERCIAL_SCHEDULED&category=SDP_COMMERCIAL_SCHEDULED&category=SDP_RESIDENTIAL_SCHEDULED&category=BIP_COMMERCIAL_SCHEDULED";
			} 
			$j.getJSON(_url, function(allData) {
				var change=0;
				var url="resteasy/services/getEventStore?";
				
					if(self.isActive){
						if(self.timestamp_apiEvts()!=allData.timeStamp.API_COMMERCIAL_ACTIVE){
							change=1; url += "categoryNames=API_COMMERCIAL_ACTIVE&";
						}
						if(self.timestamp_bipEvts()!=allData.timeStamp.BIP_COMMERCIAL_ACTIVE){
							change=1; url += "categoryNames=BIP_COMMERCIAL_ACTIVE&";
						}
						if(self.timestamp_cbpEvts()!=allData.timeStamp.CBP_COMMERCIAL_ACTIVE){
							change=1; url += "categoryNames=CBP_COMMERCIAL_ACTIVE&";
						}
						if(self.timestamp_sdpcEvts()!=allData.timeStamp.SDP_COMMERCIAL_ACTIVE){
							change=1; url += "categoryNames=SDP_COMMERCIAL_ACTIVE&";
						}
						if(self.timestamp_sdprEvts()!=allData.timeStamp.SDP_RESIDENTIAL_ACTIVE){
							change=1; url += "categoryNames=SDP_RESIDENTIAL_ACTIVE&";
						}
					}else{
						if(self.timestamp_apiEvts()!=allData.timeStamp.API_COMMERCIAL_SCHEDULED){
							change=1; url += "categoryNames=API_COMMERCIAL_SCHEDULED&";
						}
						if(self.timestamp_bipEvts()!=allData.timeStamp.BIP_COMMERCIAL_SCHEDULED){
							change=1; url += "categoryNames=BIP_COMMERCIAL_SCHEDULED&";
						}
						if(self.timestamp_cbpEvts()!=allData.timeStamp.CBP_COMMERCIAL_SCHEDULED){
							change=1; url += "categoryNames=CBP_COMMERCIAL_SCHEDULED&";
						}
						if(self.timestamp_sdpcEvts()!=allData.timeStamp.SDP_COMMERCIAL_SCHEDULED){
							change=1; url += "categoryNames=SDP_COMMERCIAL_SCHEDULED&";
						}
						if(self.timestamp_sdprEvts()!=allData.timeStamp.SDP_RESIDENTIAL_SCHEDULED){
							change=1; url += "categoryNames=SDP_RESIDENTIAL_SCHEDULED&";
						}
					}
					if(change ==1){
						addTask(url, function(allData) {
							$j.map(parseResult(allData.eventStore.events.map),function(item,index) {
								if (("API_COMMERCIAL_ACTIVE" == item.key)||("API_COMMERCIAL_SCHEDULED"== item.key)){
									var resultArray = buildArray(parseResult(item.eventsList),self.apiEvts);
									self.apiEvts(resultArray);
								}
								if (("CBP_COMMERCIAL_ACTIVE" == item.key)||("CBP_COMMERCIAL_SCHEDULED"== item.key)){
									var resultArray = buildArray(parseResult(item.eventsList),self.cbpEvts);
									self.cbpEvts(resultArray);
								}
								if (("SDP_COMMERCIAL_ACTIVE" == item.key)||("SDP_COMMERCIAL_SCHEDULED"== item.key)){
									var resultArray = buildArray(parseResult(item.eventsList),self.sdpcEvts);
									self.sdpcEvts(resultArray);
								}
								if (("SDP_RESIDENTIAL_ACTIVE" == item.key)||("SDP_RESIDENTIAL_SCHEDULED"== item.key)){
									var resultArray = buildArray(parseResult(item.eventsList),self.sdprEvts);
									self.sdprEvts(resultArray);
								}
								if (("BIP_COMMERCIAL_ACTIVE" == item.key)||("BIP_COMMERCIAL_SCHEDULED"== item.key)){
									var resultArray = buildArray(parseResult(item.eventsList),self.bipEvts);
									self.bipEvts(resultArray);
								}
							});
							
							$j.map(parseResult(allData.eventStore.lastUpdateTime.item),function(item,index) {
								if (("API_COMMERCIAL_ACTIVE" == item.key)||("API_COMMERCIAL_SCHEDULED"== item.key)){
									self.timestamp_apiEvts(item.value);
								}
								if (("CBP_COMMERCIAL_ACTIVE" == item.key)||("CBP_COMMERCIAL_SCHEDULED"== item.key)){
									self.timestamp_cbpEvts(item.value);
								}
								if (("SDP_COMMERCIAL_ACTIVE" == item.key)||("SDP_COMMERCIAL_SCHEDULED"== item.key)){
									self.timestamp_sdpcEvts(item.value);
								}
								if (("SDP_RESIDENTIAL_ACTIVE" == item.key)||("SDP_RESIDENTIAL_SCHEDULED"== item.key)){
									self.timestamp_sdprEvts(item.value);
								}
								if (("BIP_COMMERCIAL_ACTIVE" == item.key)||("BIP_COMMERCIAL_SCHEDULED"== item.key)){
									self.timestamp_bipEvts(item.value);
								}
							});
							
							if($j( ".listview" ).css( "display")=="none"){
								//map view
								resetMap();
								refreshMapView();
							}else{
								refreshListView();
							}
						}); 
					}
			}); 
			return self;
		};
		return self;
	}
	
	/**
	*	function for synchronize the checkbox status with client and server
	*/
	function buildArray(arrayFromServer,arrayFromClient){
		var result = [];
		for(var i =0;i<arrayFromServer.length;i++){
			var itemS = arrayFromServer[i];
			var uuidS = itemS["eventKey"];
			for(var j =0;j<arrayFromClient().length;j++){
				var itemC = arrayFromClient()[j];
				var uuidC = itemC["eventKey"];
				if(uuidS == uuidC){
					itemS.check=itemC.check;
				}
			}
			result.push(itemS);			
		}
		return result;
	}
	/**
	*	function for construct the map load url parameters
	*/
	function constructParameterForLoadMap(checkBoxName,program){
		var result="";				
			var eventKey="&eventKey"+program.toLowerCase()+"=";
			$j.each( $j( "input[name='"+checkBoxName+"']" ), function( index, value ){
				if(value.checked){
					var eventUUID = value.id.substring(0,value.id.length-8);
					if(eventKey==("&eventKey"+program.toLowerCase()+"=")){
						eventKey=eventKey+eventUUID;
					}else{
						eventKey=eventKey+","+eventUUID;
					}
				}
			});
			result=eventKey;		
		return result;
	}
	
	/**
	*	function for refresh map view page elements
	*/
	function refreshMapView(){
		var isActive = vm.isActive;
		var param="";	
		param+=constructParameterForLoadMap("apiEventCheckbox","api");
		param+=constructParameterForLoadMap("bipEventCheckbox","bip");
		param+=constructParameterForLoadMap("cbpEventCheckbox","cbp");
		param+=constructParameterForLoadMap("sdprEventCheckbox","sdpr");
		param+=constructParameterForLoadMap("sdpcEventCheckbox","sdpc");
		var urlString = "getMapData.map?"+param+"&active="+isActive+"&eventKey=test";
		downloadUrl(urlString); 
	}
	
	/**
	*	function for refresh list view page elements
	*/
	function refreshListView(){	
		for(key in displayInteractionMap){
			if($j("input[name='"+key+"']")[0].checked){
				$j( "#"+displayInteractionMap[key]+"" ).css( "display", "" );
				$j.each( $j( "input[name='"+checkboxInteractionMap[key]+"']" ), function( index, value ){
						var eventUUID = value.id.substring(0,value.id.length-8);
						if(value.checked){
							$j("div[name='"+eventUUID+"div']").css( "display", "" );
						}else{
							$j("div[name='"+eventUUID+"div']").css( "display", "none" );
						}
				});
			}else{
			
			}
			
		}
	}
	var vm = new ViewModel();
	ko.applyBindings(vm);
	initialize();
	/**
	*	function for reload data between active and scheduled event flag.
	*	1, clean the page elements
	*	2, invoke vm sync function to load data	
	*/
	function reloadData(param){
		if(param=="A"){
			vm.isActive=true;
		}
		if(param=="S"){
			vm.isActive=false;
		}
		vm.apiEvts.removeAll();
		vm.cbpEvts.removeAll();
		vm.bipEvts.removeAll();
		vm.sdprEvts.removeAll();
		vm.sdpcEvts.removeAll();
		if(initFlag){
			initFlag=false;
		}else{
			//vm.eventUUID="";
		}
		$j.each( $j( ":checkbox" ), function( index, value ){
			value.checked=false;
		});
		$j( "#APIDIV" ).css( "display", "none" );
		$j( "#BIPDIV" ).css( "display", "none" );
		$j( "#CBPDIV" ).css( "display", "none" );
		$j( "#SDPRDIV" ).css( "display", "none" );
		$j( "#SDPCDIV" ).css( "display", "none" );
		vm.sync();
	}	
	/**
	*	function to swith the page view between list and map
	*/
	function switchView(isListView){
		if(isListView){
			$j( ".listview" ).css( "display", "" );
			$j( ".mapview" ).css( "display", "none" );
			refreshListView();
		}else{
			$j( ".listview" ).css( "display", "none" );
			$j( ".mapview" ).css( "display", "" );
			initializeGMap();
			resetMap();
			refreshMapView();
		}		
	}
	//------------------------------------------------------------------mapview function---------------------------------------------------------
	/**
	*	function for initialize the google map component
	*/
    function initializeGMap() {
		defaultBounds = new google.maps.LatLngBounds(new google.maps.LatLng(33.198609, -120.52905399999997), new google.maps.LatLng(38.405354, -114.13122199999998));
		geocoder = new google.maps.Geocoder();
        pyrmont = new google.maps.LatLng(35.0139, -117.7033);

        map = new google.maps.Map(document.getElementById('map'), {
          mapTypeId: google.maps.MapTypeId.ROADMAP,
          center: pyrmont,
          zoom: 7
        });	
		
        lb = new progressBar();
        map.controls[google.maps.ControlPosition.RIGHT].push(lb.getDiv());
    }
	/**
	*	function for clean the overlays on the location
	*/
    function clearOverlays() {
      if (markersArray) {
        for (var i = 0; i < markersArray.length; i++ ) {
          markersArray[i].setMap(null);
        }
      }
    }
	
	/**
	*	function for code address,this function will be invoked after the search zipcode action
	*/
	function codeAddress(event) {
        clearOverlays();
        address = document.getElementById("address").value;
        keyword=address;
        if($j.trim(keyword)=="") return;//don't send any request, just clear the current markers
        
        geocoder.geocode( { 'address': address}, function(results, status) {
			if (status == google.maps.GeocoderStatus.OK) {
				var indexOfComa= parseFloat(String(results[0].geometry.location).indexOf(',',0));
				var indexOfLastParenthesis= parseFloat(String(results[0].geometry.location).indexOf(')',0));
				inputLat = String(results[0].geometry.location).slice(1,indexOfComa);
				inputLong = String(results[0].geometry.location).slice(indexOfComa+1,indexOfLastParenthesis);
				address=results[0].geometry.location;
				map.setCenter(results[0].geometry.location);
				map.setZoom(11);     
				marker = new google.maps.Marker({
					map: map,
					position: results[0].geometry.location                          
				});         
				
				marker.setIcon('images/icons/home.png');
				markersArray.push(marker);
			} else {
				alert("Geocode was not successful for the following reason: " + status);
			}
        });
    }

	/**
	*	function for load KML data from the server
	*/
	function downloadUrl(urlInput) {
		lb.start(1);
		disableAllCheckBox();
		$j.ajax({
			url: urlInput,
			type: "GET",
			complete: function( data ) {
				geoXml = new geoXML3.parser({
                    map: map,
                    singleInfoWindow: true,
                    drawingHoles: true,
                    afterParse: useTheData
                    });    
				var xml = data.responseText;
				var result = xml.split("^", 2);
				geoXml.parseKmlString(result[1]);
			}
		});	
    }
	
	/**
	* 	this function will be invoked as a callback after the google geoXML3 parse the server data.
	*/
	function useTheData(doc){
      // Geodata handling goes here, using JSON properties of the doc object
      geoXmlDoc = doc[0];
      if(geoXmlDoc.gpolygons.length<1){
        // no any polygon
        var latlng = new google.maps.LatLng(35.0139, -117.7033);
        map.setCenter(latlng);
      }  
	  enableAllCheckBox();	
	  lb.hide();		
    }
	
	/**
	* 	this function will be invoked by the event checkbox changed.
	*	checkbox un selected, remove the KML data from client;
	*	checkbox selected, load the KML data from server
	*/
	function loadEventKML(checkbox,eventTitle,checkAllBoxName){
		if(checkbox&&($j( ".listview" ).css( "display")=="none")){
			var checked = checkbox.checked;
			if(checked){
				resetMap();
				refreshMapView();
			}else{
				//remove kml data from map
				var eventUUID = checkbox.id.substring(0,checkbox.id.length-8);
				removeKML(eventUUID);
				
				
				//----------------------DRMS 8240-----------------------
				checkCheckbox();
				
			}
		}
	}

	/**
	*function to remove the select KML by event key from the google map
	*/
	function removeKML(eventkey){
		
		//remove kml data from map
		disableAllCheckBox();
		
		lb.start(1);
		
		if(geoXmlDoc){
		
			var bounds = new google.maps.LatLngBounds();
			
			var gpolygons  = [];
			
			for (var i=0;i<geoXmlDoc.gpolygons.length;i++) {

				var arrayOfStrings = geoXmlDoc.gpolygons[i].title.split(':');
				
				if (arrayOfStrings.length>1&&arrayOfStrings[1]==eventkey) {
					geoXmlDoc.gpolygons[i].setMap(null);            
				} else {
					bounds.union(geoXmlDoc.gpolygons[i].bounds);  
					gpolygons.push(geoXmlDoc.gpolygons[i]);             
				}		
			}//end for loop
			map.fitBounds(bounds);
			geoXmlDoc.gpolygons = [];
			geoXmlDoc.gpolygons = gpolygons;
			if(geoXmlDoc.gpolygons.length<1){
				// no any polygon
				map.fitBounds(defaultBounds);
			}
		}
		lb.hide();
		enableAllCheckBox();
	}
	/**
	* function to reset the map, this function will be invoked after page auto refresh and checkbox status changed
	*/
	function resetMap() {
		if(geoXmlDoc){
		   var polygons = geoXmlDoc.gpolygons;
		   for (var i=0;i<geoXmlDoc.gpolygons.length;i++) {      
			   geoXmlDoc.gpolygons[i].setMap(null);     
		   }
		   geoXmlDoc.gpolygons  = [];
		   return polygons;
		}else{
			return [];
		}
    }
	
	function checkCheckbox(){
							//DRMS---8240
							for(key in checkboxInteractionMap){
								var checkFlag=false;
								$j.each( $j( "input[name='"+checkboxInteractionMap[key]+"']" ), function( index, value ){
									if(value.checked){
										checkFlag = true;
									}
								});
								if(!checkFlag){
									$j("input[name='"+key+"']").prop('checked',false).checkboxradio("refresh");
								}
							}
	}
	//------------------------------------------------------------------auto refresh function---------------------------------------------------------
	/**
	*	initialize the auto refresh function
	*	the auto refresh time listener interval set to 30 seconds
	*/
	$j(document).ready(function(){
		var auto_refresh = setInterval(function() {
			vm.getLastUpdateTime();
		},30000);
	});	
	/**
	* function for format the event model
	*/
	function formatProduct(value) {
		var result="";
		switch(value)
		{	
			case "SDP":
			  result="Residential"
			  break;
			case "APS":
			  result="Commercial Base"
			  break;
			case "APS-E":
			  result="Commercial Enhanced"
			  break;
			case "BIP2013":
			  result="BIP"
			  break; 
			case "API":
			  result="AP-I"
			  break;   
			default:
			  result=value;
		}
        return result;
    }
	function disableAllCheckBox() {
		$j( ":checkbox" ).css( "display", "none" );	
	}	
	function enableAllCheckBox() {
		$j( ":checkbox" ).css( "display", "" );	
	}
	
	function formatString(value){
		value = value.replace("[","");
		value = value.replace("]","");
		return value;
	}
</script>
</body>

</html>