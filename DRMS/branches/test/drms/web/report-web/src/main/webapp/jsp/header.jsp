<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
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
<script type="text/javascript">
function closeWindow(){
	 if(navigator.appVersion.indexOf("MSIE") != -1){
	 	 var version =parseFloat(navigator.appVersion.split("MSIE")[1]);
		if (version == "9" || version == "8" || version == "7") { 
			window.open('', '_self', ''); 
			window.close(); 
		} else if (version == "6") { 
			window.opener = null; 
			window.close(); 
		} else { 
			window.opener = ''; 
			window.close(); // attempt to close window first, show user warning message if fails 
		} 
	}else{
		window.opener = null; 
		window.close(); 
	}
}
</script>
<a4j:keepAlive beanName="report" />
<a4j:status id="waitStatus" forceId="true"
          onstart="javascript:Richfaces.showModalPanel('waitModalPanel');"
           onstop="javascript:Richfaces.hideModalPanel('waitModalPanel');" />
<h:panelGroup styleClass="#{report.context.headerStyle}" layout="block" id="page_wrapper">
	<div class="header"><br/>
		<h1>
		<h:outputText style="font-weight:bold;font-size:25"	value="DRAS Customer Report"/>
		</h1>
		
		<div id="navrightDiv">
			<ul id="navright">
			
				<li class="last"><a href="javascript:closeWindow();">Close</a>
				</li>
				
				<li class="topright"><h:outputText
					value="Welcome #{report.context.welcomeName} - (#{report.context.userRole})" />
				</li>
				
				<li class="topright"><h:outputText
					value="#{report.context.serverTime}">
					<f:convertDateTime pattern="#{report.headerDateTimeFormat}" />
					</h:outputText>
				</li>
			</ul>
		</div>
	</div>
</h:panelGroup>
