package com.akuacom.jsf.renderkit;

import java.io.IOException;
import java.util.LinkedHashSet;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.ajax4jsf.javascript.PrototypeScript;
import org.ajax4jsf.renderkit.ComponentVariables;
import org.ajax4jsf.renderkit.HeaderResourceProducer;
import org.ajax4jsf.renderkit.RendererUtils.HTML;
import org.ajax4jsf.resource.InternetResource;
import org.ajax4jsf.resource.InternetResourceBuilder;
import org.ajax4jsf.resource.ResourceNotFoundException;
import org.richfaces.component.UITab;
import org.richfaces.renderkit.TabPanelRendererBase;

import com.akuacom.jsf.component.HtmlTab;

public class TabHeaderRenderer extends org.richfaces.renderkit.html.TabHeaderRenderer 
								implements HeaderResourceProducer{
	private static final String ONMOUSEOVER = "RichFaces.overTab(this);";
	private static final String ONMOUSEOUT = "RichFaces.outTab(this);";
	
	protected static final String LABEL_SUFFIX = "_lbl";
	protected static final String CLASS_HEAD="tabHeader";
	
	protected static final String[] EMPTY_ARRAY = {};
	
	private InternetResourceBuilder resourceBuilder = null;
	
	/**
	 * has a dependency on rich faces and prototype.js 
	 */
	private InternetResource prototypeScript = getResource(PrototypeScript.class.getName());
	
	/**
	 * Base stub method for produce internet resource ( image, script ... )
	 * since resources must be implemented in "lightweight" pattern, it instances
	 * put in internal map to caching.
	 * @param resourceURI - relative ( to renderer class ) uri to resource in jar or
	 * key for generate ( in Java2D , for example ). 
	 * @return - resource instance for this uri.
	 * @throws ResourceNotFoundException - if reqested resource not instantiated.
	 */
	public InternetResource getResource(String resourceURI ) throws FacesException {
		return getResourceBuilder().createResource(this,resourceURI);
	}

	private InternetResourceBuilder getResourceBuilder(){
		if (resourceBuilder == null) {
			resourceBuilder = InternetResourceBuilder.getInstance();
		}
		return resourceBuilder;
	}	


	
//	public void doEncodeEnd(ResponseWriter writer, FacesContext context, org.richfaces.component.UITab component, ComponentVariables variables) throws IOException {
//		super.doEncodeEnd(writer, context, component, variables);
//		writeScriptSupportAltKey(context,component,context.getResponseWriter());
//	}
	/**
	 * Override to generate more javascript
	 */
	@Override
	public void doEncodeEnd(ResponseWriter writer, FacesContext context, org.richfaces.component.UITab component, ComponentVariables variables) throws IOException {
		  java.lang.String clientId = component.getClientId(context);
	variables.setVariable("spacer", getResource( "images/spacer.gif" ).getUri(context, component) );

	writer.startElement("td", component);
				getUtils().writeAttribute(writer, "id", convertToString(clientId) + "_cell" );
							getUtils().writeAttribute(writer, "style", "height: 100%; vertical-align: bottom;" );
				
	encodeCellClasses(context, component);

	writer.startElement("table", component);
				getUtils().writeAttribute(writer, "border", "0" );
							getUtils().writeAttribute(writer, "cellpadding", "0" );
							getUtils().writeAttribute(writer, "cellspacing", "0" );
							getUtils().writeAttribute(writer, "id", convertToString(clientId) + "_shifted" );
				
	encodeTabLabel(context, component);

	writer.startElement("tr", component);

	writer.startElement("td", component);
				getUtils().writeAttribute(writer, "class", "rich-tabhdr-side-border" );
				
	writer.startElement("img", component);
				getUtils().writeAttribute(writer, "alt", "" );
							getUtils().writeAttribute(writer, "height", "1" );
							getUtils().writeAttribute(writer, "src", variables.getVariable("spacer") );
							getUtils().writeAttribute(writer, "style", "border:0" );
							getUtils().writeAttribute(writer, "width", "1" );
				
	writer.endElement("img");
	writer.endElement("td");
	writer.startElement("td", component);
				getUtils().writeAttribute(writer, "class", "rich-tabhdr-side-cell" );
							getUtils().writeAttribute(writer, "style", encodeTabLabelWidth(context,component) );
				
	writer.startElement("table", component);
				getUtils().writeAttribute(writer, "border", "0" );
							getUtils().writeAttribute(writer, "cellpadding", "0" );
							getUtils().writeAttribute(writer, "cellspacing", "0" );
							getUtils().writeAttribute(writer, "style", "height: 100%; width: 100%;" );
				
	writer.startElement("tr", component);

	writer.startElement("td", component);
				getUtils().writeAttribute(writer, "id", convertToString(clientId) + "_lbl" );
							getUtils().writeAttribute(writer, "onclick", component.getAttributes().get("onlabelclick") );
							getUtils().writeAttribute(writer, "ondblclick", component.getAttributes().get("onlabeldblclick") );
							getUtils().writeAttribute(writer, "onkeydown", component.getAttributes().get("onlabelkeydown") );
							getUtils().writeAttribute(writer, "onkeypress", component.getAttributes().get("onlabelkeypress") );
							getUtils().writeAttribute(writer, "onkeyup", component.getAttributes().get("onlabelkeyup") );
							getUtils().writeAttribute(writer, "onmousedown", component.getAttributes().get("onlabelmousedown") );
							getUtils().writeAttribute(writer, "onmousemove", component.getAttributes().get("onlabelmousemove") );
							getUtils().writeAttribute(writer, "onmouseup", component.getAttributes().get("onlabelmouseup") );
				
	writeLabel(context, component);

	writer.endElement("td");
	writer.endElement("tr");
	writer.endElement("table");
	writer.endElement("td");
	writer.startElement("td", component);
				getUtils().writeAttribute(writer, "class", "rich-tabhdr-side-border" );
				
	writer.startElement("img", component);
				getUtils().writeAttribute(writer, "alt", "" );
							getUtils().writeAttribute(writer, "height", "1" );
							getUtils().writeAttribute(writer, "src", variables.getVariable("spacer") );
							getUtils().writeAttribute(writer, "style", "border:0" );
							getUtils().writeAttribute(writer, "width", "1" );
				
	writer.endElement("img");
	writer.endElement("td");
	writer.endElement("tr");
	writer.endElement("table");
	writer.endElement("td");
	writer.startElement("td", component);

	writer.startElement("img", component);
				getUtils().writeAttribute(writer, "alt", "" );
							getUtils().writeAttribute(writer, "height", "1" );
							getUtils().writeAttribute(writer, "src", variables.getVariable("spacer") );
							getUtils().writeAttribute(writer, "style", convertToString(encodeHeaderSpacing(context,component)) + ";border:0" );
				
	writer.endElement("img");
	
	writeScriptSupportAltKey(context,component,context.getResponseWriter());
	writer.endElement("td");

		}		
	/**
	 * Generate Javascript to support alt hot key 
	 * when alt and hot key are pressed at the same time, the link will be triggered
	 * immediately
	 * <p> 
	 * NOTE the generated javascript has a dependency on prototype.js 
	 * @param context the JSF conext
	 * @param component the UI component 
	 * @param writer RespnseWriter
	 * @throws IOException
	 */
	protected void writeScriptSupportAltKey(FacesContext context,UITab component,ResponseWriter writer)
		throws IOException{
		StringBuffer buffer = new StringBuffer();
		String label=(String) component.getAttributes().get("label");
		if(label==null) return;
		label= label.toUpperCase();
		int idx = label.toUpperCase().indexOf("&");
		String clientId = component.getClientId(context);
		
		HtmlTab tab = (HtmlTab) component;
		
		//TODO
		//if(idx>=0 && idx<label.length()-1){
			buffer.append("\n");
			buffer.append("<script type=\"text/javascript\">\n");
			buffer.append("//<![CDATA[\n");
			// when hot key is pressed, trigger the link immediately
			buffer.append("Event.observe(document,'keydown',function(){");
			buffer.append(" 	evt= window.event || event;");
			//buffer.append("  	if(evt.altKey && String.fromCharCode(evt.keyCode)=='" + label.substring(label.indexOf("&") + 1, label.indexOf("&") + 2) + "'){");
			buffer.append("  	if(evt.altKey && String.fromCharCode(evt.keyCode)=='" + tab.getAccesskey() + "'){");

			buffer.append("			$('" + clientId + "_shifted" + "').onclick();");
			buffer.append("		}");
			buffer.append(" });");
			
			if(tab.isActive() && !tab.isActivePreviously()){
				buffer.append("var tofocus=$('"+clientId+"_href'); if(tofocus) tofocus.focus();");
			}
			buffer.append("\n");
			buffer.append("//]]>");
			buffer.append("\n");
			buffer.append("</script>");
			buffer.append("\n");
			writer.write(buffer.toString());
		//}
	}
	
	@Override
	public LinkedHashSet<String> getHeaderScripts(FacesContext context,
			UIComponent component) {
		LinkedHashSet<String> scripts = new LinkedHashSet<String>() ; // Collections.singleton(ajaxScript.getUri(context, null));
		scripts.add(prototypeScript.getUri(context, null));
		String[] additionalScripts = getAdditionalScripts();
		for (int i = 0; i < additionalScripts.length; i++) {
			String resource = additionalScripts[i];
			scripts.add(getResource(resource).getUri(context, null));
		}
		return scripts;
	}

	protected String[] getAdditionalScripts() {
		return EMPTY_ARRAY;
	}

	@Override
	public LinkedHashSet<String> getHeaderStyles(FacesContext context,
			UIComponent component) {
		return new LinkedHashSet<String>();
	}
	
	@Override
	public void writeLabel(FacesContext context, UITab tab) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		
		String labelClass = "";
		if (tab.isDisabled()) {
			labelClass = TabPanelRendererBase.getDisabledTabClass(tab);
		} else {
			if (tab.isActive()) {
				labelClass = TabPanelRendererBase.getActiveTabClass(tab);
			} else {
				labelClass = TabPanelRendererBase.getInactiveTabClass(tab);
			}

			writer.writeAttribute(HTML.onmouseover_ATTRIBUTE, ONMOUSEOVER,
					"tabOnMouseOver");
			writer.writeAttribute(HTML.onmouseout_ATTRIBUTE, ONMOUSEOUT,
					"tabOnMouseOut");
		}
		writer.writeAttribute(HTML.class_ATTRIBUTE, labelClass, "tabClass");
		String title = tab.getTitle();
		if (title != null && title.length() != 0) {
			writer.writeAttribute(HTML.title_ATTRIBUTE, title, null);
		}

		// TODO make "label" constant
		UIComponent facet = tab.getFacet("label");

		if (facet != null && facet.isRendered()) {
			renderChild(context, facet);
		} else {
			String label = tab.getLabel();
			String lab = label;

			if (label == null || label.length() == 0) {
				// TODO to constant
				label = "&#160;";
				writer.write(label);
			} else {
				
				//Underline the hot key. 
				int len =label.length();
				int idx = label.toUpperCase().indexOf("&");
				if(idx>=0 && idx<len-1){
					//lab = label.substring(0,idx) + "<u>"+label.charAt(idx + 1) + "</u>";
					lab = label.substring(0,idx)+label.charAt(idx + 1);
					if(idx<len-2){
						lab+=label.substring(idx+2);						
					}
				}
				label=lab;
				String clientId= tab.getClientId(context);
				//writer.write(label);

				String tabHeading="";
				if (tab instanceof com.akuacom.jsf.component.HtmlTab) {
					String headingLevel=((com.akuacom.jsf.component.HtmlTab)tab).getHeadingLevel();
					if (headingLevel !=null && headingLevel.trim().length()!= 0){
						label = "<a class='"+CLASS_HEAD+"' id='"+clientId+"_href' href=\"#\"" +
						" onclick=\"$('" + clientId + "_shifted').onclick(); return false;\">" + label + "</a>";
						tabHeading="<" + headingLevel+">" + label+ "</" + headingLevel+">";
					}
					else{
						tabHeading=label;
					}
						
				}
				
//				label = "<a class='"+CLASS_HEAD+"' id='"+clientId+"_href' href=\"#\"" +
//				" onclick=\"$('" + clientId + "_shifted').onclick(); return false;\">" + tabHeading + "</a>";
				writer.write(tabHeading);
				
//				writer.write("<a class='"+CLASS_HEAD+"' id='"+clientId+"_href' href=\"#\"" +
//						" onclick=\"$('" + clientId + "_shifted').onclick(); return false;\">" + label + "</a>");
			}
		}
	}
	private String convertToString(Object obj ) {
		return ( obj == null ? "" : obj.toString() );
	}
	
}
