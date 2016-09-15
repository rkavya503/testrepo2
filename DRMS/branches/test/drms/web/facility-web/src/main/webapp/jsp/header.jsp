<%@ page import="java.util.Date"%>
<%@ page import="com.akuacom.pss2.util.DrasRole" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://akuacom.com/richext" prefix="richext"%>
<script type="text/javascript">
/* <![CDATA[ */
    var IS_IE9 = Prototype.Browser.IE && (parseFloat(navigator.appVersion.substring(navigator.appVersion.indexOf("MSIE")+5))) >= 9;

    //*****************************************************************************************
    //****extends Event.isLeftClick in prototype.js to support IE9
    //*****************************************************************************************
    Object.extend(Event, {
        isLeftClick: function(event) {
        	var code = 0;
            if(IS_IE9){
                //Handle IE9
                //http://www.w3.org/TR/DOM-Level-2-Events/events.html#Events-MouseEvent
                //button is used to indicate which mouse button changed state. 
                //The values for button range from zero to indicate the left button of the mouse, 
                //one to indicate the middle button if present, 
                //and two to indicate the right button. 
                //For mice configured for left handed use in which the button actions are reversed the values are instead read from right to left.
                switch (code) {
                    case 0: return event.button == 0;
                    case 1: return event.button == 1;
                    case 2: return event.button == 2;
                    default: return false;
                }
            } else if (Prototype.Browser.IE) {
                var buttonMap = { 0: 1, 1: 4, 2: 2 };
                return event.button == buttonMap[code];
            } else if (Prototype.Browser.WebKit) {
                switch (code) {
                    case 0: return event.which == 1 && !event.metaKey;
                    case 1: return event.which == 1 && event.metaKey;
                    default: return false;
                }
            } else {
                return event.which ? (event.which === code + 1) : (event.button === code);
            }//end of if
        }//end of isLeftClick
      }
    );        

    //*****************************************************************************************
    //****extends RichFaces.createEvent in 
    //3.3.2.SR1\framework\impl\src\main\resources\org\richfaces\renderkit\html\scripts\events.js
    //*****************************************************************************************
    Richfaces.createEventSuper = Richfaces.createEvent;

    Object.extend(Richfaces, {
       createEvent: function(type, component, baseEvent, props){
                
        if (!IS_IE9) {
        	return this.createEventSuper(type, component, baseEvent, props);
        } else {
            var eventObj;
            var bubbles = baseEvent && baseEvent.bubbles || false;
            var cancelable = baseEvent && baseEvent.cancelable || true;     
            
            switch (type) {
                case 'abort':
                case 'blur':
                case 'change':
                case 'error':
                case 'focus':
                case 'load':
                case 'reset':
                case 'resize':
                case 'scroll':
                case 'select':
                case 'submit':
                case 'unload':

                    eventObj = document.createEvent('HTMLEvents');
                    eventObj.initEvent(type, bubbles, cancelable);
                
                break;
                
                case 'DOMActivate':
                case 'DOMFocusIn':
                case 'DOMFocusOut': 
                case 'keydown':
                case 'keypress':
                case 'keyup':

                    eventObj = document.createEvent('UIEvents');
                    if (baseEvent) {
                        eventObj.initUIEvent(type, bubbles, cancelable, baseEvent.windowObject, 
                            baseEvent.detail);
                    } else {
                        eventObj.initEvent(type, bubbles, cancelable);
                    }
                
                break;
        
                case 'click':
                case 'mousedown':
                case 'mousemove':
                case 'mouseout':
                case 'mouseover':
                case 'mouseup':
                
                    eventObj = document.createEvent('MouseEvents');
                    if (baseEvent) {
                        eventObj.initMouseEvent(type, bubbles, cancelable, 
                            baseEvent.windowObject, 
                            baseEvent.detail, 
                            baseEvent.screenX, 
                            baseEvent.screenY, 
                            baseEvent.clientX, 
                            baseEvent.clientY, 
                            baseEvent.ctrlKey, 
                            baseEvent.altKey, 
                            baseEvent.shiftKey, 
                            baseEvent.metaKey, 
                            baseEvent.button, 
                            baseEvent.relatedTarget);
                    } else {
                        eventObj.initEvent(type, bubbles, cancelable) 
                    }
                
                break;
                
                case 'DOMAttrModified':
                case 'DOMNodeInserted':
                case 'DOMNodeRemoved':
                case 'DOMCharacterDataModified':
                case 'DOMNodeInsertedIntoDocument':
                case 'DOMNodeRemovedFromDocument':
                case 'DOMSubtreeModified':
                    
                    eventObj = document.createEvent('MutationEvents');
                    if (baseEvent) {
                        eventObj.initMutationEvent(type, bubbles, cancelable, 
                            baseEvent.relatedNode, 
                            baseEvent.prevValue, 
                            baseEvent.newValue, 
                            baseEvent.attrName, 
                            baseEvent.attrChange
                            );
                    } else {
                        eventObj.initEvent(type, bubbles, cancelable) 
                    }

                break;
                
                default:
                    eventObj = document.createEvent('Events');
                    eventObj.initEvent(type, bubbles, cancelable);
            }
            
            if (props) {
                Object.extend(eventObj, props);
            }
         
            eventObj[Richfaces.SYNTHETIC_EVENT] = true;
                    
            return {
                event: eventObj,
                
                fire: function() {
                        component.dispatchEvent(this.event);
                },
                
                destroy: function() {
                    if (props) {
                        for (var name in props) {
                            this.event[name] = undefined;
                        }
                    }
                }
            };
        }//end of else
        
       }//end
     }
    ); 
/* ]]> */
</script>

<h:panelGroup styleClass="#{header1.headerStyle}" layout="block" id="page_wrapper">
	<div class="header">
        <br/>   
                <h1>
					<h:outputText id="titleOutputText" style="font-weight:bold;font-size:25"  value="#{header1.header_label}" />
				</h1>
     

		<ul class="nav">
		    <h:panelGroup rendered="#{header1.batchUpdateAggregation}">
				<li class="${header1.aggregationTree}" id="${header1.aggregationTree}" value="14" >
					<richext:commandLink value="Aggregation" action="#{header1.aggregationTreeAction}" 
						immediate="true"  id="navaggregationTree" headingLevel="h2"/>
				</li>
			</h:panelGroup>
			
            <h:panelGroup rendered="#{header1.displayStatus}">
				<li class="${header1.status}" id="${header1.status}" value="1" >
					<richext:commandLink value="Dashboard" action="#{header1.statusAction}" 
						immediate="true"  id="navStatus" headingLevel="h2"/>
				</li> 
			</h:panelGroup>
            
            <h:panelGroup rendered="#{header1.displaySimpleContacts}">
                <li class="${header1.simpleContacts}" id="${header1.simpleContacts}" value="2" >
                    <richext:commandLink value="Contacts" action="#{header1.simpleContactsAction}" 
                        immediate="true"  id="navSimpleContacts" headingLevel="h2"/>
                </li> 
            </h:panelGroup>

            <h:panelGroup rendered="#{header1.clientEnabled}">
    			<li class="${header1.clients}" id="${header1.clients}" value="3"  type="li">
    				<richext:commandLink value="Clients" action="#{header1.clientsAction}" 
    					immediate="true"  id="navClients" headingLevel="h2"/>
    			</li>
            </h:panelGroup>

            <h:panelGroup rendered="#{header1.eventsEnabled}">
    			<li class="${header1.events}" id="${header1.events}" value="4" >
    				<richext:commandLink value="Events" action="#{header1.eventsAction}" 
    					immediate="true"  id="navEvents" headingLevel="h2"/>
    			</li>
            </h:panelGroup>

            <h:panelGroup rendered="#{header1.programsEnabled}" >
    			<li class="${header1.programs}" id="${header1.programs}" value="5" >
    				<richext:commandLink value="Programs" action="#{header1.programsAction}" 
    					immediate="true"  id="navPrograms" headingLevel="h2"/>
    			</li>
            </h:panelGroup>

			<h:panelGroup rendered="false">
				<li class="${header1.usage}" id="${header1.usage}"  value="6" >
					<richext:commandLink value="Usage" action="#{header1.usageAction}" 
						immediate="true"  id="navUsage" headingLevel="h2"/>
				</li>
			</h:panelGroup>
			
			<h:panelGroup rendered="#{header1.displayUsage && header1.userDataEnabled}">
				<li class="${header1.irrUsage}" id="${header1.irrUsage}"  value="7" >
					<richext:commandLink value="Telemetry" action="#{header1.irrUsageAction}" 
						immediate="true"  id="navirrUsage" headingLevel="h2"/>
				</li>
			</h:panelGroup>			

			<h:panelGroup rendered="#{header1.displayNews}">
				<li class="${header1.news}" id="${header1.news}" value="8" >
					<richext:commandLink value="News & Info" action="#{header1.newsAction}"
						immediate="true"  id="navNews" headingLevel="h2"/>
				</li>
			</h:panelGroup>

			<h:panelGroup rendered="#{header1.displaySubAccounts}">
				<li class="${header1.subAccounts}" id="${header1.subAccounts}" value="9" >
					<richext:commandLink value="Sub Accounts" action="#{header1.subAccountsAction}" 
						immediate="true"  id="navSubAccounts" headingLevel="h2"/>
				</li>
			</h:panelGroup>
			
			<h:panelGroup rendered="#{header1.displayCustomerTestEvent and header1.enableClientTestTab}">
				<li class="${header1.demoEvent}" id="${header1.demoEvent}" value="10" >
					<richext:commandLink value="Client Test" action="#{header1.demoEventAction}"
					  immediate="true" id="demoEvent" headingLevel="h2">
					</richext:commandLink>
				</li>
			</h:panelGroup>
			
			<h:panelGroup rendered="#{header1.shedInfoEnabled}">
    			<li class="${header1.participantShed}" id="${header1.participantShed}" value="11" >
				<richext:commandLink value="Participant Shed" action="#{header1.participantShedAction}" 
					immediate="true"  id="navParticipantShed" headingLevel="h2"/>
			</li>
            </h:panelGroup>
			
			 <h:panelGroup rendered="#{header1.enableClientOptionTab}">
			<li class="${header1.options}" id="${header1.options}" value="12"  >
				<richext:commandLink value="Options" action="#{header1.optionsAction}" 
					immediate="true"  id="navOptions" headingLevel="h2"/>
			</li>
			</h:panelGroup>
			
			<li class="${header1.about}" id="${header1.about}" value="13" >
				<richext:commandLink value="About" action="#{header1.aboutAction}" 
					immediate="true"  id="navAbout" headingLevel="h2"/>
			</li>
						
		</ul>		
		
		<script type="text/javascript">
			var currentTab = document.getElementById("current");
			if(currentTab==null){
				currentTab = document.getElementByClass("current");
			}
			var title = document.getElementById("navForm:titleOutputText");
			
			if(title==null){
				title = document.getElementById("form:titleOutputText");
			}
			if(title==null){
				title = document.getElementByClass("form:titleOutputText");
			}
			if(title==null){
				title = document.getElementByClass("form:titleOutputText");
			}
			if(title!=null){
				var originalTitle = title.innerText;
				var index = currentTab.attributes.getNamedItem("value").nodeValue;
				
				
				if(index==14){title.innerText = "Aggregation - " +originalTitle;}
				if(index==1){title.innerText = "Dashboard - " +originalTitle;}
				if(index==2){title.innerText = "Contacts - " +originalTitle;}
				if(index==3){title.innerText = "Clients - " +originalTitle;}
				if(index==4){title.innerText = "Events - " +originalTitle;}
				if(index==5){title.innerText = "Programs - " +originalTitle;}
				if(index==6){title.innerText = "Usage - " +originalTitle;}
				if(index==7){title.innerText = "Telemetry - " +originalTitle;}
				if(index==8){title.innerText = "News - " +originalTitle;}
				if(index==9){title.innerText = "Sub Accounts - " +originalTitle;}
				if(index==10){title.innerText = "Client Test - " +originalTitle;}
				if(index==11){title.innerText = "Participant Shed - " +originalTitle;}
				if(index==12){title.innerText = "Options - " +originalTitle;}
				if(index==13){title.innerText = "About - " +originalTitle;}
			}
			
			
		</script>
		
		<h:outputLink id="link1" value="/report/jsp/framework.jsf"
			target="_blank"
			styleClass="viewReportLink" >
			<f:param name="user" value="#{header1.participantName}" />
			<f:param name="isFacilityOperator" value="#{header1.facilityOperator}" />
			<h:outputText value="View Reports"/>
		</h:outputLink>

              
		<div id="navrightDiv">
            <ul id="navright">
                 <li class="topright">
                     <a4j:region  rendered="#{header1.installer}" >
                           <a4j:commandButton
                               oncomplete="#{rich:component('installCompleteConfirmPanel')}.show();" reRender="installCompleteConfirmPanel"
                               value="Complete Installation" rendered="#{header1.enableCompleteInstallation}"
                               >
                                 
                           </a4j:commandButton>
                    </a4j:region>
                  </li>               
                
               <li class="last">
                     <a4j:commandLink
                            immediate="true"
                             action="#{header1.logoutAction}"
                             value="#{header1.logoutDisplay}"
                             onclick="this.disabled=true"
                             oncomplete="#{facesContext.maximumSeverity == null ? '' : 'this.disabled=false'}" >
                        </a4j:commandLink>
                </li>


                <li class="topright">
                    <h:outputText value="Welcome #{header1.welcomeName} - (#{header1.userRole})" />
                </li>
                <li class="topright">
                    <h:outputText value="#{header1.serverTime}">
                            <f:convertDateTime pattern="#{applicationScope.headerDateTimeFormat}" />
                        </h:outputText>
                </li>
                
                
                   <a4j:region id="aggDisplay" rendered="#{header1.batchUpdateAggregation}" >
                   <li class="topright">                      
                       <h:outputText value="Aggregation Level:"/>
                       <rich:spacer width="5px" />
                       <h:outputText id="aggName" value="#{ aggTree.aggDisplay } " style="font-weight:bold"/>
                       </li>
                   </a4j:region>
                   
                   
                  <a4j:region rendered="#{header1.individualUpdateAggregation}" >
                   <li class="topright">
                       <a4j:commandButton
                           oncomplete="#{rich:component('aggModelPanel')}.show()" 
                           reRender="aggModelPanel"
                           value="Aggregation">
                             <a4j:actionparam name="currParticipant" value="#{header1.parentParticipant}" assignTo="#{aggTree.currParticipant}"/>
                       </a4j:commandButton>
                       <rich:spacer width="5px" />
                       <h:outputText value="Aggregation Level:"/>
                       <rich:spacer width="5px" />
                       <h:outputText id="aggName_original" value="#{ aggTree.aggDisplay } " style="font-weight:bold"/>
                       </li>
                   </a4j:region>
                   
                
                 <!--       
                    <li class="skip" id="skiphide" title="skip to content">
                        <a tabindex="1" id='skiplinkhref' href="#skipAim" 
                           onclick="document.getElementById('skipAim').scrollIntoView();"
                           >
                            <div id="skiplinkdiv">Skip Link</div>
                        </a>  
                        <script>
                            if(document.URL.indexOf('#skipAim')<0)
                                document.getElementById('skiplinkhref').href=document.URL+"#skipAim";
                        </script>
                    </li>
                -->
            </ul>  
        </div>      
	</div> 
	<div style="clear: both;"></div>   
</h:panelGroup>
<a name="skipAim" id="skipAim"></a>

<rich:modalPanel id="installCompleteConfirmPanel" autosized="true"
	keepVisualState="false"
	width="300"
	height="80"
	>
	<div id="installCompleteConfirmForm">
	    <a id="dialogTitle" href="javascript:void(0);" style="font-weight: bold;">Confirmation Dialog</a>
	    <rich:panel>
	        <a href="javascript:void(0);">
	            <h:outputText value="Do you want to log out of the installer portal? "/>
	        </a>
	    </rich:panel>
	    <a4j:commandButton value="Yes" reRender="clients"
			action="#{header1.logoutAction}"
	        oncomplete="#{rich:component('installCompleteConfirmPanel')}.hide();"
			title="Complete Installation">
            <f:setPropertyActionListener
                value="#{header1.installer}"
                target="#{header1.completeInstallation}"
            />
        </a4j:commandButton>	
	    <a4j:commandButton value="No" oncomplete="#{rich:component('installCompleteConfirmPanel')}.hide();" title="Cancel Installation"/>
	</div>
</rich:modalPanel>
