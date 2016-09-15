package com.akuacom.jsf.renderkit;

import java.io.IOException;
import java.util.Iterator;

import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIMessages;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.ajax4jsf.renderkit.RendererUtils.HTML;
import org.richfaces.component.UIRichMessages;
import org.richfaces.renderkit.RichMessageBaseRenderer;
import org.richfaces.renderkit.html.HtmlRichMessagesRenderer;

public class AccessibleRichMessagesRenderer extends RichMessageBaseRenderer {
	Boolean isFirstMessageFlag = null;
	
	public static final String ERROR_MESSAGES_FIRST_MESSAGE_ID="ERROR_MESSAGES_FIRST_MESSAGE_ID";	
	protected void doEncodeEnd(ResponseWriter writer, FacesContext context, UIComponent component) throws IOException {
		UIRichMessages uiMessages = (UIRichMessages)component;
		
		String layout = null;
		String forIds = null;
		String forClientId = uiMessages.getFor();
		if(null != forClientId){
			forIds = forClientId;
		}
		if(uiMessages.isGlobalOnly()){
			if(null == forClientId){
				forIds = "";
			}else{
				throw new FacesException("The rich:messages component must specify only one of 'for' or 'globalOnly' atribbute");
			}
		}
		Iterator<FacesMessage> messagesIter = uiMessages.getMessages(context); 
		boolean isDisplayNone = !messagesIter.hasNext();
		boolean isWroteTable = false;				
		layout = (String)uiMessages.getAttributes().get("layout");
		if((layout != null) && (layout.equals("table"))){
			 
			writer.startElement("table", uiMessages);
			getUtils().writeAttribute(writer,HTML.id_ATTRIBUTE ,uiMessages.getClientId(context));
			getUtils().writeAttribute(writer, HTML.cellpadding_ATTRIBUTE, "0");
			getUtils().writeAttribute(writer, HTML.cellspacing_ATTRIBUTE, "0");
			renderComponentOuterStyles(uiMessages, context, writer, isDisplayNone);
			writer.startElement("tbody", uiMessages);
			isWroteTable = true;

		}else if((layout != null) && (layout.equals("list"))){
			
			isWroteTable = false;
			writer.startElement(HTML.DL_ELEMENT, uiMessages);
			getUtils().writeAttribute(writer,HTML.id_ATTRIBUTE ,uiMessages.getClientId(context));
			renderComponentOuterStyles(uiMessages, context, writer, isDisplayNone);
		
		}
		
//		messagesIter = getMessageIterator(context, forIds, component);
		
		boolean hasErrorMessage=false;
		
		if(!messagesIter.hasNext()){
			renderBody(uiMessages, context, writer, null,isWroteTable);
			hasErrorMessage=false;
		}else{
			hasErrorMessage=true;
			while(messagesIter.hasNext()){
				if(isFirstMessageFlag ==null){
					isFirstMessageFlag = new Boolean(true);
				}else if(isFirstMessageFlag.booleanValue()==true){
					isFirstMessageFlag = new Boolean(false);
				}
				FacesMessage facesMsg = (FacesMessage)(messagesIter.next());
				
				renderBody(uiMessages, context, writer, facesMsg,isWroteTable);
				
			}
			
			isFirstMessageFlag=null;
		
		}	
		
		if(isWroteTable){
			writer.endElement("tbody");
			writer.endElement("table");
		}else{
			writer.endElement(HTML.DL_ELEMENT);
		}
		if(hasErrorMessage){
	      writer.write("<script>");
	      writer.write("		document.getElementById(\""+ ERROR_MESSAGES_FIRST_MESSAGE_ID +"\").focus();");
	      writer.write("</script>"); 	
		}

	}
	
	public void renderLabelHtml(UIComponent component, FacesContext context, ResponseWriter writer, 
								FacesMessage facesMsg, String labelClass, String labelStyle, String passLabel) throws IOException {
		
		String summary = null;
		
		String detail = null; 
		
		String title = null;
		
		UIRichMessages uiMessages = null;
		
		
		if(!(component instanceof UIRichMessages)){
			return;
		}
		
		uiMessages = (UIRichMessages)component;
		
		boolean isTooltip = getUtils().isBooleanAttribute(uiMessages, "tooltip");
		boolean showSummary = uiMessages.isShowSummary();
		boolean showDetail = uiMessages.isShowDetail();
		
		if(facesMsg != null){
			summary = (null != (summary = facesMsg.getSummary())) ? summary : "";
			detail = (null != (detail = facesMsg.getDetail())) ? detail : "";
		}	   
		   
		labelClass = labelClass == null ? "rich-messages-label" : "rich-messages-label " + labelClass;
		writer.startElement(HTML.SPAN_ELEM, uiMessages);
		getUtils().writeAttribute(writer, HTML.class_ATTRIBUTE,labelClass);
		getUtils().writeAttribute(writer, HTML.style_ATTRIBUTE, labelStyle);
		//DRMS 3099/3100/3101
		getUtils().writeAttribute(writer,"role","alert");
		getUtils().writeAttribute(writer,"tabindex","0");
		
		if(isFirstMessageFlag){
			getUtils().writeAttribute(writer,"id",ERROR_MESSAGES_FIRST_MESSAGE_ID);
        }else{
        } 
		
		title = (String) component.getAttributes().get("title");
		if(title != null) {
			writer.writeAttribute("title", title, "title");
		}

		if(facesMsg != null){
			boolean wroteTooltip = false;
			if (showSummary && showDetail && isTooltip) {
				if (title == null || title.length() == 0) {
					writer.writeAttribute("title", summary, "title");
				}
				wroteTooltip = true;
			} 

			if (!wroteTooltip && showSummary) {
				writer.writeText(summary, component, null);
			}
			
			if (showDetail) {
				writer.writeText(detail, component, null);
			}	
		} else if(passLabel != null){
				writer.writeText(passLabel, null);
				writer.writeText("\t", null);
		}
		
		writer.endElement(HTML.SPAN_ELEM);
		
	}

	public void renderMarkerHtml(UIComponent component, UIComponent markerFacet, FacesContext context,
								 ResponseWriter writer, String markerClass, String markerStyle)throws IOException {
		
		UIRichMessages uiMessages = null;
		
		if(!(component instanceof UIRichMessages)){
			return;
		}
		
		uiMessages = (UIRichMessages)component;
		writer.startElement(HTML.SPAN_ELEM, uiMessages);
		String classes = markerClass == null ? "rich-messages-marker" : "rich-messages-marker " + markerClass;
		getUtils().writeAttribute(writer, HTML.class_ATTRIBUTE, classes);
		getUtils().writeAttribute(writer, HTML.style_ATTRIBUTE, markerStyle);
		renderChild(context, markerFacet);
		writer.endElement(HTML.SPAN_ELEM);
		
	}
	
	public void renderComponentOuterStyles(UIComponent component, FacesContext context, ResponseWriter writer, boolean isDisplayNone)	throws IOException {
		
		UIRichMessages uiMessages = null;
		
		if(!(component instanceof UIRichMessages)){
			return;
		}
		
		uiMessages = (UIRichMessages)component;
	
		String parentStyle = (String)uiMessages.getAttributes().get("style");
		String parentClass = (String)uiMessages.getAttributes().get("styleClass");
		
		parentStyle = ((isDisplayNone) ? "display: none; " : "") + (parentStyle != null ? parentStyle : "");
		
		String richMessageClass = "rich-messages";
		parentClass = parentClass != null ? (richMessageClass + " " + parentClass) : richMessageClass;    
		   
		getUtils().writeAttribute(writer, HTML.class_ATTRIBUTE, parentClass);
		getUtils().writeAttribute(writer, HTML.style_ATTRIBUTE, parentStyle);
		
	}
	
	public void renderOuterStyles(UIComponent component, FacesContext context, ResponseWriter writer, 
	           					  String outerStyle, String outerClass)	throws IOException {
		
		String richMessageClass = null != outerClass ? outerClass:"";
		String richMessageStyle = (null != outerStyle ? outerStyle : "");
		  
		getUtils().writeAttribute(writer, HTML.class_ATTRIBUTE, richMessageClass);
		getUtils().writeAttribute(writer, HTML.style_ATTRIBUTE, richMessageStyle);
		
	}
	
	public void renderBody(UIRichMessages uiMessages, FacesContext context, ResponseWriter writer,FacesMessage facesMsg,boolean isWroteTable) throws IOException{
		if (isWroteTable) {
			writer.startElement(HTML.TR_ELEMENT, uiMessages);
			writer.startElement(HTML.td_ELEM, uiMessages);
		} else {
			writer.startElement(HTML.DT_ELEMENT, uiMessages);
		}

		if (facesMsg != null) {		
			outerStyles(uiMessages, context, writer, facesMsg);
			renderMarkerFacet(uiMessages, context, writer, facesMsg);	
			renderLabel(uiMessages, context, writer, facesMsg);
		}
		
		if (isWroteTable) {
			writer.endElement(HTML.td_ELEM);
			writer.endElement(HTML.TR_ELEMENT);
		} else {
			writer.endElement(HTML.DT_ELEMENT);
		}
	}
	
	protected Class<? extends UIComponent> getComponentClass() {
		return UIMessages.class;
	}
	

}
