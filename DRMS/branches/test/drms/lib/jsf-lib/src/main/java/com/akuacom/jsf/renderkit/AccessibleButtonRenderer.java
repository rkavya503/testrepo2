package com.akuacom.jsf.renderkit;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;

import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlForm;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.ActionEvent;

import com.sun.faces.renderkit.AttributeManager;
import com.sun.faces.renderkit.RenderKitUtils;
import com.sun.faces.renderkit.html_basic.ButtonRenderer;

/**
 *<b>AccessibleButtonRenderer</b> provide much better accessibility support
 * than {@link ButtonRenderer}.
 * <p>
 * <b>AccessibilityCommandLink</b> will underline the hot key in the button
 * label, so that user doesn't need to remember or guess the hot key.
 * 
 */
public class AccessibleButtonRenderer extends ButtonRenderer {

	private static final String[] ATTRIBUTES = AttributeManager
			.getAttributes(AttributeManager.Key.COMMANDBUTTON);

	@Override
	public void encodeBegin(FacesContext context, UIComponent component)
			throws IOException {

		rendererParamsNotNull(context, component);

		if (!shouldEncode(component)) {
			return;
		}

		// Which button type (SUBMIT, RESET, or BUTTON) should we generate?
		String type = getButtonType(component);

		ResponseWriter writer = context.getResponseWriter();
		assert (writer != null);

		String label = "";
		Object value = ((UICommand) component).getValue();
		if (value != null) {
			label = value.toString();
		}
		String imageSrc = (String) component.getAttributes().get("image");
		writer.startElement("button", component);
		writeIdAttributeIfNecessary(context, writer, component);
		String clientId = component.getClientId(context);
		if (imageSrc != null) {
			writer.writeAttribute("type", type, "type");
			//writer.writeAttribute("name", clientId, "clientId");
			// writer.writeAttribute("id", clientId, "clientId");
		} else {
			writer.writeAttribute("type", type, "type");
			// writer.writeAttribute("id", clientId, "clientId");
			//writer.writeAttribute("name", clientId, "clientId");
		}

		HtmlForm form = getForm(component);
		if (form != null) {
			String formId = form.getId();
			writer.writeAttribute("onclick",
					"var fform=document.getElementById('" + formId + "');"
							+ "fform['" + clientId + "'].value='" + clientId +"';"
							+ "fform.submit();", "value");
		}
		
		RenderKitUtils.renderPassThruAttributes(writer, component, ATTRIBUTES);
		RenderKitUtils.renderXHTMLStyleBooleanAttributes(writer, component);
		String styleClass = (String) component.getAttributes()
				.get("styleClass");
		if (styleClass != null && styleClass.length() > 0) {
			writer.writeAttribute("class", styleClass, "styleClass");
		}

		if (imageSrc != null) {
			// writer.write("<IMG src=&quot;src(context, imageSrc)&quot;>");
		} else {
			writer.write(getLabel(label, component));
		}
		writer.endElement("button");

		if (form != null) {
			//hidden form item is used to indicate which button is click in event processing
			writer.startElement("input", component);
			writer.writeAttribute("type", "hidden", "hidden");
			writer.writeAttribute("name", clientId, "clientId");
			writer.endElement("input");
		}
	}
	
	 protected String getOnClickScript(String formClientId,
             String commandClientId,
             String target,
             Param[] params) {
		   return null;
	 }
	

	protected String getLabel(String label, UIComponent component) {

		if (label != null && label.length() != 0) {
			if (logger.isLoggable(Level.FINE)) {
				logger.fine("Value to be rendered " + label);
			}
			String accesskey = (String) component.getAttributes().get(
					"accesskey");
			if (accesskey != null) {
				int idx = label.toUpperCase().indexOf(accesskey.toUpperCase());
				if (idx >= 0) {
					label = label.substring(0, idx) + "<u>" + label.charAt(idx)
							+ "</u>" + label.substring(idx + 1);
				}
			}
		}
		return label;
	}

	public HtmlForm getForm(UIComponent component) {
		UIComponent parent = component.getParent();
		if (parent == null)
			return null;
		if (parent instanceof HtmlForm)
			return (HtmlForm) component.getParent();
		else
			return getForm(parent);
	}

	/**
	 * @param context
	 *            the <code>FacesContext</code> for the current request
	 * @param imageURI
	 *            the base URI of the image to use for the button
	 * @return the encoded result for the base imageURI
	 */
	private static String src(FacesContext context, String imageURI) {

		if (imageURI == null) {
			return "";
		}

		String u = context.getApplication().getViewHandler().getResourceURL(
				context, imageURI);
		return (context.getExternalContext().encodeResourceURL(u));

	}

	private static String getButtonType(UIComponent component) {

		String type = (String) component.getAttributes().get("type");
		if (type == null
				|| (!"reset".equals(type) && !"submit".equals(type) && !"button"
						.equals(type))) {
			type = "submit";
			// This is needed in the decode method
			component.getAttributes().put("type", type);
		}
		return type;
	}

	private static boolean wasClicked(FacesContext context,
			UIComponent component) {
		// Was our command the one that caused this submission?
		// we don' have to worry about getting the value from request parameter
		// because we just need to know if this command caused the submission.
		// We
		// can get the command name by calling currentValue. This way we can
		// get around the IE bug.
		String clientId = component.getClientId(context);
		Map<String, String> requestParameterMap = context.getExternalContext()
				.getRequestParameterMap();
		if (requestParameterMap.get(clientId) == null || requestParameterMap.get(clientId).equals("")) {
			StringBuilder builder = new StringBuilder(clientId);
			String xValue = builder.append(".x").toString();
			builder.setLength(clientId.length());
			String yValue = builder.append(".y").toString();
			return (requestParameterMap.get(xValue) != null && requestParameterMap
					.get(yValue) != null);
		}
		return true;
	}

	/**
     * @param component the component of interest
     * @return <code>true</code> if the button represents a <code>reset</code>
     *  button, otherwise <code>false</code>
     */
    private static boolean isReset(UIComponent component) {

        return ("reset".equals(component.getAttributes().get("type")));

    }
    
    
	@Override
	public void decode(FacesContext context, UIComponent component) {
		rendererParamsNotNull(context, component);

		if (!shouldDecode(component)) {
			return;
		}
		if (wasClicked(context, component) && !isReset(component)) {
			component.queueEvent(new ActionEvent(component));

			if (logger.isLoggable(Level.FINE)) {
				logger.fine("This command resulted in form submission "
						+ " ActionEvent queued.");
				logger.log(Level.FINE, "End decoding component {0}", component
						.getId());
			}
		}
	}
}
