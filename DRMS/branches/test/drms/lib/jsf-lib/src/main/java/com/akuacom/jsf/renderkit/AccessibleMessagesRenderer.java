package com.akuacom.jsf.renderkit;

import java.io.IOException;
import java.util.Iterator;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIMessages;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import com.sun.faces.renderkit.AttributeManager;
import com.sun.faces.renderkit.RenderKitUtils;
import com.sun.faces.renderkit.html_basic.HtmlBasicRenderer;

public class AccessibleMessagesRenderer extends HtmlBasicRenderer 
								implements IFormConstants {
    private static final String[] ATTRIBUTES =
         AttributeManager.getAttributes(AttributeManager.Key.MESSAGESMESSAGES);

   
   @Override
   public void encodeBegin(FacesContext context, UIComponent component)
         throws IOException {
       rendererParamsNotNull(context, component);

   }
   
   @Override
   public void encodeEnd(FacesContext context, UIComponent component)
         throws IOException {

       rendererParamsNotNull(context, component);

       if (!shouldEncode(component)) {
           return;
       }

       UIMessages messages = (UIMessages) component;
       ResponseWriter writer = context.getResponseWriter();
       assert(writer != null);

       // String clientId = ((UIMessages) component).getFor();
       String clientId = null; // PENDING - "for" is actually gone now
       // if no clientId was included
       if (clientId == null) {
           // and the author explicitly only wants global messages
           if (messages.isGlobalOnly()) {
               // make it so only global messages get displayed.
               clientId = "";
           }
       }

       String id=FOR_FORM_FIELD_ATT+clientId;
       
       //"for" attribute optional for Messages
       Iterator messageIter = getMessageIter(context, clientId, component);

       assert(messageIter != null);
       
       if (!messageIter.hasNext()) {
           return;
       }

       String layout = (String) component.getAttributes().get("layout");
       boolean showSummary = messages.isShowSummary();
       boolean showDetail = messages.isShowDetail();
       String styleClass = (String) component.getAttributes().get(
             "styleClass");
       styleClass=ERROR_MSG_CLS +" "+styleClass;

       boolean wroteTable = false;

       //For layout attribute of "table" render as HTML table.
       //If layout attribute is not present, or layout attribute
       //is "list", render as HTML list. 
       if ((layout != null) && (layout.equals("table"))) {
           writer.startElement("table", component);
           wroteTable = true;
       } else {
           writer.startElement("ul", component);
       }

       //Render "table" or "ul" level attributes.
       writeIdAttributeIfNecessary(context, writer, component);
       if (null != styleClass) {
           writer.writeAttribute("class", styleClass, "styleClass");
       }
       // style is rendered as a passthru attribute
       RenderKitUtils.renderPassThruAttributes(writer,
                                               component,
                                               ATTRIBUTES);
       
       boolean isFirstMessageFlag = true;
       
       while (messageIter.hasNext()) {
           FacesMessage curMessage = (FacesMessage) messageIter.next();
           
           
           
           String severityStyle = null;
           String severityStyleClass = null;

           // make sure we have a non-null value for summary and
           // detail.
           String summary = (null != (summary = curMessage.getSummary())) ?
                     summary : "";
           // Default to summary if we have no detail
           String detail = (null != (detail = curMessage.getDetail())) ?
                    detail : summary;
           

           if (curMessage.getSeverity() == FacesMessage.SEVERITY_INFO) {
               severityStyle =
                     (String) component.getAttributes().get("infoStyle");
               severityStyleClass = (String)
                     component.getAttributes().get("infoClass");
           } else if (curMessage.getSeverity() == FacesMessage.SEVERITY_WARN) {
               severityStyle =
                     (String) component.getAttributes().get("warnStyle");
               severityStyleClass = (String)
                     component.getAttributes().get("warnClass");
           } else
           if (curMessage.getSeverity() == FacesMessage.SEVERITY_ERROR) {
               severityStyle =
                     (String) component.getAttributes().get("errorStyle");
               severityStyleClass = (String)
                     component.getAttributes().get("errorClass");
           } else
           if (curMessage.getSeverity() == FacesMessage.SEVERITY_FATAL) {
               severityStyle =
                     (String) component.getAttributes().get("fatalStyle");
               severityStyleClass = (String)
                     component.getAttributes().get("fatalClass");
           }

           //Done intializing local variables. Move on to rendering.

           if (wroteTable) {
               writer.startElement("tr", component);
           } else {
               writer.startElement("li", component);
           }

           if (severityStyle != null) {
               writer.writeAttribute("style", severityStyle, "style");
           }
           if (severityStyleClass != null) {
               styleClass = severityStyleClass;
               writer.writeAttribute("class", styleClass, "styleClass");
           }

           if (wroteTable) {
 
        	   writer.startElement("td", component);
               if(isFirstMessageFlag){
//            	   writer.write("<a id='"+ERROR_MESSAGES_FIRST_MESSAGE_ID+"' href='javascript:return false;' role='alert' tabindex='0'>");           	   \
//            	   writer.write("<span id='"+ERROR_MESSAGES_FIRST_MESSAGE_ID+"' role='alert' tabindex='0'>");
            	   writer.write("<a id='"+ERROR_MESSAGES_FIRST_MESSAGE_ID+"' tabindex='0' style='text-decoration: none;color: red;'>");
               }else{
//            	   writer.write("<a href='javascript:return false;' role='alert'>");  
//            	   writer.write("<span role='alert'>");  
            	   writer.write("<a style='text-decoration: none;color: red;'");  
               }              
           }
           
           //Set role to alert to ensure message can be spoken out by screen readers
           //see http://juicystudio.com/article/wai-aria-live-regions.php
//           writer.writeAttribute("role","alert","role");
//           writer.writeAttribute("id",id,"id");
           
           Object val = component.getAttributes().get("tooltip");
           boolean isTooltip = (val != null) && Boolean.valueOf(val.toString());

           boolean wroteTooltip = false;
           writer.startElement("span", component);
           writer.writeAttribute("class", "message-error", "styleClass");
           if (showSummary && showDetail && isTooltip) {
        	  
//        	   writer.startElement("span", component);
               String title = (String) component.getAttributes().get("title");
               if (title == null || title.length() == 0) {
                   writer.writeAttribute("title", summary, "title");
               }
               writer.flush();
               writer.writeText("\t", component, null);
               wroteTooltip = true;
           }

           if (!wroteTooltip && showSummary) {
               writer.writeText("\t", component, null);
               writer.writeText(summary, component, null);
               writer.writeText(" ", component, null);
           }
           if (showDetail) {
               writer.writeText(detail, component, null);
           }
           
           if (wroteTooltip) {
//               writer.endElement("span");

           }
           writer.endElement("span");
//           writer.endElement("a");
           //close table row if present
           if (wroteTable) {

//        	   writer.write("</span>"); 
        	   writer.write("</a>"); 
               if(isFirstMessageFlag){            	               	   
            	   isFirstMessageFlag = false;
               }                 
               writer.endElement("td");
               
               writer.endElement("tr");
           } else {
               writer.endElement("li");
           }

       } //messageIter

       //close table if present
       if (wroteTable) {
           writer.endElement("table");
       } else {
           writer.endElement("ul");
       }         	               	   
//       writer.write("<script>");
//       writer.write("		document.getElementById(\""+ ERROR_MESSAGES_FIRST_MESSAGE_ID +"\").focus();");
//       writer.write("</script>"); 


   }
}
