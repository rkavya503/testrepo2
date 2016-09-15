package com.akuacom.jsf.renderkit;

import java.io.IOException;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.richfaces.component.UITree;
import org.richfaces.renderkit.html.TreeRenderer;

public class AccessibileTreeRenderer extends TreeRenderer {

	public String encodeSelectionStateInput(FacesContext context, UITree tree) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		writer.startElement("input", tree);
		writer.writeAttribute("type", "hidden", null);
		String selectionHolderInputId = tree.getSelectionStateInputName(context);
		writer.writeAttribute("id", selectionHolderInputId, null);
		//writer.writeAttribute(HTML.autocomplete_ATTRIBUTE, "off", null);
		writer.writeAttribute("name", selectionHolderInputId, null);
		writer.writeAttribute("value", getSelectionValue(context, tree), null);
		writer.endElement("input");
		return selectionHolderInputId;
	}
}
