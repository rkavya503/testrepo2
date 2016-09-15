package com.akuacom.jsf.renderkit;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Map;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.ajax4jsf.javascript.PrototypeScript;
import org.ajax4jsf.renderkit.HeaderResourceProducer;
import org.ajax4jsf.resource.InternetResource;
import org.ajax4jsf.resource.InternetResourceBuilder;
import org.ajax4jsf.resource.ResourceNotFoundException;

import com.sun.faces.renderkit.html_basic.CommandLinkRenderer;

/**
 *<em>AccessibleCommandLinkRender</em> provide much better accessibility support than
 * {@link CommandLinkRenderer}.
 * <p>
 * Different browser have different support for alt hot key for a link.
 * <li> IE : Alt + Hot key combination will firstly focus on target element, 
 *  Enter key is expected to trigger the link
 *  </li>
 * <li> FE: Alt + Shift + Hot key will trigger the link immediately.
 * </li>
 * <p>
 * <em>AccessibilityCommandLink</em> will unify the behavior. Alt + Hot Key 
 * will trigger the link directly. 
 * <p>Furthermore, <em>AccessibilityCommandLink</em> will underline the hot key in the 
 * link label, so that use don't need to remember or guess the hot key.
 * 
 */
public class AccessibleCommandLinkRender extends CommandLinkRenderer 
					implements HeaderResourceProducer {
	
	protected static final String[] EMPTY_ARRAY = {};
	
	private InternetResourceBuilder resourceBuilder = null;
	
	public final static String SESSION_CLICKED_ELEMENT_ID="__was_clicked";
	
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
	public void encodeBegin(FacesContext context, UIComponent component)
			throws IOException {
        if (!shouldEncode(component)) {
            return;
        }
        
        ResponseWriter writer = context.getResponseWriter();
        
        if (component instanceof com.akuacom.jsf.component.HtmlCommandLink) {
        	String headingLevel=((com.akuacom.jsf.component.HtmlCommandLink)component).getHeadingLevel();
			if (headingLevel !=null && headingLevel.trim().length()!= 0)
				writer.write("<" + headingLevel+">");
        }
        
        super.encodeBegin(context, component);
	}

	protected String getOnClickScript(String formClientId,
             String commandClientId,
             String target,
             Param[] params) {
		String script=super.getOnClickScript(formClientId, commandClientId, target, params);
		return "Event.stop(event || window.event);"+ script;
	}
	 
	@Override
	protected boolean shouldWriteIdAttribute(UIComponent component){
		//no id for <A> element in super implementation
		//add id generation
		return true;
	}
	
    @Override
    public void encodeEnd(FacesContext context, UIComponent component)
          throws IOException {
    	super.encodeEnd(context, component);
    	
        ResponseWriter writer = context.getResponseWriter();
        assert(writer != null);
        
        if (component instanceof com.akuacom.jsf.component.HtmlCommandLink) {
        	String headingLevel=((com.akuacom.jsf.component.HtmlCommandLink)component).getHeadingLevel();
			if (headingLevel !=null && headingLevel.trim().length()!= 0)
				writer.write("</" + headingLevel+">");
        }
        
        writer.startElement("script", component);
        writer.writeAttribute("type", "javascript", "");
    	writeAdditionalClientScript(context, component, writer);
        writer.endElement("script");
    }
    
	/**
	 * Generate Javascript to support auto-focus after clicking
	 * NOTE the generated javascript has a dependency on prototype.js 
	 * @param context the JSF conext
	 * @param component the UI component 
	 * @param writer RespnseWriter
	 * @throws IOException
	 */
	protected void writeAdditionalClientScript(FacesContext context,UIComponent component,ResponseWriter writer) 
		throws IOException{
		String clientId=component.getClientId(context);
		String id=(String) context.getExternalContext().getSessionMap().get(SESSION_CLICKED_ELEMENT_ID);
		StringBuffer buffer = new StringBuffer();
		if(id!=null && id.equals(clientId)){
			buffer.append("var tofocus=$('"+id+"'); if(tofocus) tofocus.focus();");
		}
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
	
    @Override
	public void decode(FacesContext context, UIComponent component) {
		super.decode(context, component);
		if(wasClicked(context,component)){
			context.getExternalContext().getSessionMap().put(SESSION_CLICKED_ELEMENT_ID, component.getClientId(context));
		}
	}
    
	protected static boolean wasClicked(FacesContext context,
                                      UIComponent component) {
        Map<String,String> requestParamMap =
              context.getExternalContext().getRequestParameterMap();
        return (requestParamMap.containsKey(component.getClientId(context)));

    }

}
