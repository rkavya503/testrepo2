package com.akuacom.jsf.renderkit;

import java.io.IOException;
import java.util.LinkedHashSet;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.ajax4jsf.javascript.PrototypeScript;
import org.ajax4jsf.renderkit.HeaderResourceProducer;
import org.ajax4jsf.resource.InternetResource;
import org.ajax4jsf.resource.InternetResourceBuilder;
import org.ajax4jsf.resource.ResourceNotFoundException;

import com.sun.faces.renderkit.html_basic.FormRenderer;

public class AccessibleFormRenderer extends FormRenderer implements IFormConstants,HeaderResourceProducer {
	protected static final String[] EMPTY_ARRAY = {};
	private InternetResourceBuilder resourceBuilder = null;
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
	protected InternetResource getResource(String resourceURI ) throws FacesException {
		return getResourceBuilder().createResource(this,resourceURI);
	}
	private InternetResourceBuilder getResourceBuilder(){
		if (resourceBuilder == null) {
			resourceBuilder = InternetResourceBuilder.getInstance();
		}
		return resourceBuilder;
	}	
	@Override
	public void encodeEnd(FacesContext context, UIComponent component)
			throws IOException {
		super.encodeEnd(context, component);
		
		ResponseWriter writer=context.getResponseWriter();
		StringBuffer buffer =new StringBuffer();
		String form_id = ((UIForm)component).getId();
		//write script to focus the first field which didn't pass validation
		buffer.append("\n");
		buffer.append("<script type=\"text/javascript\">\n");
		buffer.append("//<![CDATA[\n");
		
		buffer.append("var msgs=document.getElementsByClassName('"+ERROR_MSG_CLS+"');\n");
		buffer.append("if(msgs && msgs.length>0){\n");
		buffer.append("	var msg= msgs[0];\n");
		buffer.append("	if(msg){\n");
		buffer.append("		var msg_id = msg.id;\n");
		int len=FOR_FORM_FIELD_ATT.length();
		buffer.append("		var relativedComponent_id = msg_id.substring('"+len+"');\n");
		buffer.append("		var relativedComponent = document.getElementById('"+form_id+":'"+"+relativedComponent_id+'InputDate');\n");
		buffer.append("		if(!relativedComponent){\n");
		buffer.append("			relativedComponent = document.getElementById('"+form_id+":'"+"+relativedComponent_id);\n");
		buffer.append("		}\n");
		buffer.append("		if(relativedComponent){\n");
		buffer.append("			relativedComponent.focus();\n");
		buffer.append("		}\n");
		buffer.append("	}\n");
		buffer.append("}\n");
		
		
		buffer.append("var firstMSG_in_messages = document.getElementById('"+ERROR_MESSAGES_FIRST_MESSAGE_ID+"');\n");
		buffer.append("if(firstMSG_in_messages){\n");
		buffer.append("	firstMSG_in_messages.focus();\n");
		buffer.append("}\n");
		
		//rewrite state input id since they are duplicated with each others if there are multiple forms in one view
		buffer.append("  var st=document.forms['"+form_id+"']['javax.faces.ViewState']; ");
		buffer.append("if(st) { st.setAttribute('id','"+form_id+".javax.faces.ViewState');}\n"); 
		
		buffer.append("//]]>");
		buffer.append("\n");
		buffer.append("</script>");
		buffer.append("\n");
		writer.write(buffer.toString());
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

	
}
