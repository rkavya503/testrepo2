package com.akuacom.jsf.renderkit;

import java.io.IOException;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.ajax4jsf.renderkit.RendererBase;
import org.richfaces.component.UITab;
import org.richfaces.renderkit.html.TabRenderer;

import com.akuacom.jsf.component.HtmlTab;

public class AccessibleTabRenderer extends TabRenderer {
	public AccessibleTabRenderer(){
		super();
	}
	
    private RendererBase tabHeaderRenderer;
    
    /**
     * Encode this tab header in Panel switch pane.
     *
     * @param context
     * @param tab
     * @param active
     * @throws IOException
     */
    public void encodeTab(FacesContext context, UITab tab, boolean active) throws IOException {
    	HtmlTab htab = null;
    	if(tab instanceof HtmlTab){
    		 htab = (HtmlTab) tab;
    	}
    	if(htab==null || (htab !=null && htab.isShowHeader())){
    		getHeaderRenderer().encodeBegin(context, tab);
    		getHeaderRenderer().encodeEnd(context, tab);
    	}
    }
    
    private synchronized RendererBase getHeaderRenderer() {
    	if (tabHeaderRenderer == null) {
            Package pkg = this.getClass().getPackage();
            try {
                tabHeaderRenderer = (RendererBase) Class.forName(pkg.getName() + ".TabHeaderRenderer").newInstance();
            } catch (InstantiationException e) {
                throw new FacesException(e);
            } catch (IllegalAccessException e) {
                throw new FacesException(e);
            } catch (ClassNotFoundException e) {
                throw new FacesException(e);
            }
    	}
    	return tabHeaderRenderer;
    }

	@Override
	public void encodeEnd(FacesContext context, UIComponent component)
			throws IOException {
		super.encodeEnd(context, component);
	}
}
