package com.akuacom.jsf.renderkit;

import java.io.IOException;
import java.util.logging.Level;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.ajax4jsf.renderkit.ComponentVariables;
import org.ajax4jsf.renderkit.RendererUtils.HTML;
import org.richfaces.component.UISimpleTogglePanel;
import org.richfaces.renderkit.html.SimpleToggleControlTemplate;

public class AccessibleSimpleToggleControlTemplate extends
		SimpleToggleControlTemplate {
	
	public static final String SRC_ICON_ARROW_LEFT="/com/akuacom/jsf/renderkit/images/arrow_left.png";
	public static final String SRC_ICON_ARROW_RIGHT="/com/akuacom/jsf/renderkit/images/arrow_right.png";
	@Override
	public void doEncodeEnd(ResponseWriter writer, FacesContext context,
			UIComponent component) throws IOException {

		super.doEncodeEnd(writer, context, component);
	}
	@Override
	public void doEncodeBegin(ResponseWriter writer, FacesContext context,
			org.richfaces.component.UISimpleTogglePanel component,
			ComponentVariables variables) throws IOException {
		java.lang.String clientId = component.getClientId(context);
		
		encodeDivStart(writer, context, component);

		writer.startElement("script", component);
		getUtils().writeAttribute(writer, "type", "text/javascript");

		writer.writeText(
				convertToString("SimpleTogglePanelManager.add(new SimpleTogglePanel(\""
						+ convertToString(clientId)
						+ "\", \n            													\""
						+ convertToString(component.getAttributes().get(
								"opened")) + "\",{"), null);

		writeEventHandlerFunction(context, component, "onexpand");

		writer.writeText(convertToString(","), null);

		writeEventHandlerFunction(context, component, "oncollapse");

		writer.writeText(
				convertToString("}\n            													\n            							 ));"),
				null);

		writer.endElement("script");
		getUtils().encodeBeginFormIfNessesary(context, component);

		writer.startElement("div", component);
		getUtils().writeAttribute(
				writer,
				"class",
				"rich-stglpanel-header "
						+ convertToString(component.getAttributes().get(
								"headerClass")));
		getUtils().writeAttribute(writer, "id",
				convertToString(clientId) + "_header");
		getUtils().writeAttribute(writer, "onclick",
				getOnClick(context, component));
		
		//DRMS-5776 
		String onkeydownScript="if((event || window.event).keyCode==13) {"+getOnClick(context, component)+"}";
		getUtils().writeAttribute(writer, HTML.onkeypress_ATTRIBUTE,onkeydownScript);
		
		writer.startElement("div", component);
		getUtils().writeAttribute(writer, "class", "rich-stglpanel-marker");

		encodeSwitchOnDivStart(writer, context, component);

		if (component.getFacet("closeMarker") != null
				&& component.getFacet("closeMarker").isRendered()) {

			UIComponent indexChildren_6 = component.getFacet("closeMarker");
			if (null != indexChildren_6 && indexChildren_6.isRendered()) {
				renderChild(context, indexChildren_6);
			}

		} else {
//			writer.write("LEFT &#171;");
			
			
			String collapseImage=getResource(SRC_ICON_ARROW_LEFT).getUri(context, null);
			writer.startElement(HTML.IMG_ELEMENT, component);
			writer.writeAttribute(HTML.src_ATTRIBUTE, collapseImage, null);
			writer.writeAttribute(HTML.alt_ATTRIBUTE, "Collapse the toggle panel", null);
			writer.writeAttribute(HTML.title_ATTRIBUTE, "Collapse the toggle panel", null);
			// there is no attribute "tabindex"  DRMS-6740
			//writer.writeAttribute(HTML.tabindex_ATTRIBUTE, "0", null);
			writer.endElement(HTML.IMG_ELEMENT);
			
			
		}

		encodeDivEnd(writer);

		encodeSwitchOffDivStart(writer, context, component);

		if (component.getFacet("openMarker") != null
				&& component.getFacet("openMarker").isRendered()) {

			UIComponent indexChildren_7 = component.getFacet("openMarker");
			if (null != indexChildren_7 && indexChildren_7.isRendered()) {
				renderChild(context, indexChildren_7);
			}

		} else {
//			writer.write("RIGHT &#187;");
			String expandImage=getResource(SRC_ICON_ARROW_RIGHT).getUri(context, null);
			writer.startElement(HTML.IMG_ELEMENT, component);
			writer.writeAttribute(HTML.src_ATTRIBUTE, expandImage, null);
			writer.writeAttribute(HTML.alt_ATTRIBUTE, "Expand the toggle panel", null);
			writer.writeAttribute(HTML.title_ATTRIBUTE, "Expand the toggle panel", null);
			// there is no attribute "tabindex" DRMS-6740
			//writer.writeAttribute(HTML.tabindex_ATTRIBUTE, "0", null);
			writer.endElement(HTML.IMG_ELEMENT);
			
			
		}

		encodeDivEnd(writer);

		writer.endElement("div");

		if (component.getFacet("header") != null
				&& component.getFacet("header").isRendered()) {

			UIComponent indexChildren_8 = component.getFacet("header");
			if (null != indexChildren_8 && indexChildren_8.isRendered()) {
				renderChild(context, indexChildren_8);
			}

		} else {
			String label =convertToString(component.getAttributes().get("label"));
			
			if (label != null && label.length() != 0) {
				String headingLabel="";
				if (component instanceof com.akuacom.jsf.component.HtmlSimpleTogglePanel) {
					String headingLevel=((com.akuacom.jsf.component.HtmlSimpleTogglePanel)component).getHeadingLevel();
					if (headingLevel !=null && headingLevel.trim().length()!= 0)
						headingLabel="<" + headingLevel+">" + label+ "</" + headingLevel+">";
				}
				if (!headingLabel.equals(""))
					label=headingLabel;
				
				writer.write(label);
			}
			
//			
//			
//			writer.writeText(label,null);
//			writer.write("&#160;");
		}

		writer.endElement("div");

		if ((component.getSwitchType() != null)
				&& (component.getSwitchType().equals(
						UISimpleTogglePanel.CLIENT_SWITCH_TYPE) == true)) {

			writer.startElement("div", component);
			getUtils().writeAttribute(writer, "style", "display: none;");

			writer.startElement("input", component);
			//DRMS-6740
			//getUtils().writeAttribute(writer, "autocomplete", "off");
			getUtils().writeAttribute(writer, "id",
					convertToString(clientId) + "_input");
			getUtils().writeAttribute(writer, "name", clientId);
			getUtils().writeAttribute(writer, "type", "hidden");
			getUtils().writeAttribute(writer, "value",
					component.getAttributes().get("opened"));

			writer.endElement("input");
			writer.endElement("div");

		}

		getUtils().encodeEndFormIfNessesary(context, component);

		encodeBodyDivStart(writer, context, component);

	}

	@Override
	public void encodeSwitchOnDivStart(ResponseWriter writer,
			FacesContext context, UISimpleTogglePanel component)
			throws IOException {
		encodeSwitchDivStart(writer, context, component, true);
	}

	@Override
	public void encodeSwitchOffDivStart(ResponseWriter writer,
			FacesContext context, UISimpleTogglePanel component)
			throws IOException {
		encodeSwitchDivStart(writer, context, component, false);
	}

	private void encodeSwitchDivStart(ResponseWriter writer,
			FacesContext context, UISimpleTogglePanel component,
			boolean isSwitchOn) throws IOException {
		String clientId = component.getClientId(context);
		writer.startElement("div", component);

		getUtils().writeAttribute(writer, "class", "rich-stglpnl-marker");
		getUtils().writeAttribute(
				writer,
				"id",
				convertToString(clientId) + "_switch_"
						+ (isSwitchOn ? "on" : "off"));

		String display = convertToString(
				getSwitchStatus(context, component, isSwitchOn)).trim();
		if (!isEmpty(display)) {
			display = "display: " + display;
		}
		getUtils().writeAttribute(writer, "style", display);

	}

	private String convertToString(Object obj) {
		return (obj == null ? "" : obj.toString());
	}

	private boolean isEmpty(String str) {
		return str == null || str.length() == 0;
	}
	
	
	

}
