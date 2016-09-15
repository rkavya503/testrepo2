package com.akuacom.pss2.richsite;

import java.util.Iterator;

import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlCommandButton;
import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlInputTextarea;
import javax.faces.component.html.HtmlSelectBooleanCheckbox;
import javax.faces.component.html.HtmlSelectManyCheckbox;
import javax.faces.component.html.HtmlSelectManyListbox;
import javax.faces.component.html.HtmlSelectManyMenu;
import javax.faces.component.html.HtmlSelectOneListbox;
import javax.faces.component.html.HtmlSelectOneMenu;
import javax.faces.component.html.HtmlSelectOneRadio;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import org.ajax4jsf.component.html.HtmlAjaxCommandButton;
import org.ajax4jsf.component.html.HtmlAjaxCommandLink;
import org.richfaces.component.html.HtmlCalendar;
import org.richfaces.component.html.HtmlFileUpload;

import com.akuacom.pss2.util.DrasRole;


/**
 * Access UIComponents just before the render response phase to change some attributes of the UIComponents, 
 * change "disabled" and "visible".  
 *
 */
public class BeforeRenderPhaseListener implements PhaseListener {

	private static final long serialVersionUID = 1L;
	private static final String DISABLE_STYLE = "background-color:D4D0C8;"; // set background color to gray

	@Override
	public void afterPhase(PhaseEvent event) {
		//Do nothing
	}

	@Override
	public void beforePhase(PhaseEvent event) {
        if (event.getPhaseId() == PhaseId.RENDER_RESPONSE) {
            FacesContext facesContext = event.getFacesContext();
            
		   	if (facesContext.getExternalContext().isUserInRole(DrasRole.Operator.toString()) || 
		   			facesContext.getExternalContext().isUserInRole(DrasRole.Readonly.toString()) ||
		   			facesContext.getExternalContext().isUserInRole(DrasRole.Dispatcher.toString()))
		   	{
		   		//get the ViewRoot and iterate over it to access all UIComponents. 
		   		disableAllComponentsInRoot(event.getFacesContext());
		   	}
        }
	}

	@Override
	public PhaseId getPhaseId() {
		return PhaseId.RENDER_RESPONSE;
	}
	
	private static void disableAllComponentsInRoot(FacesContext facesContext) {
	    if (facesContext != null) {
	      UIComponent root = facesContext.getViewRoot();
	      disableAllChildren(root);
	    }
	}
	    
	private static void disableAllChildren(UIComponent base) {
	    UIComponent kid = null;
	    UIComponent result = null;
	    Iterator<UIComponent> kids = base.getFacetsAndChildren();
	    while (kids.hasNext() && (result == null)) {
	      kid = (UIComponent) kids.next();
	      
	      disableComponent(kid);
	      disableAllChildren(kid);
	    }
	}

	private static void disableComponent(UIComponent kid) {
		if (kid instanceof HtmlInputText) {
	    	  ((HtmlInputText) kid).setReadonly(true);
	    	  kid.getAttributes().put("style",DISABLE_STYLE);
	      }else if(kid instanceof HtmlInputTextarea){
	    	  ((HtmlInputText) kid).setReadonly(true);
	    	  kid.getAttributes().put("style",DISABLE_STYLE);
	      } else if(kid instanceof HtmlSelectBooleanCheckbox){
	    	  ((HtmlSelectBooleanCheckbox) kid).setReadonly(true);
	    	  ((HtmlSelectBooleanCheckbox) kid).setDisabled(true);
	      } else if(kid instanceof HtmlSelectManyCheckbox){
	    	  ((HtmlSelectManyCheckbox) kid).setReadonly(true);
	    	  ((HtmlSelectManyCheckbox) kid).setDisabled(true);
	      } else if(kid instanceof HtmlSelectManyListbox){
	    	  ((HtmlSelectManyListbox) kid).setReadonly(true);
	    	  ((HtmlSelectManyListbox) kid).setDisabled(true);
	      } else if(kid instanceof HtmlSelectManyMenu){
	    	  ((HtmlSelectManyMenu) kid).setReadonly(true);
	    	  ((HtmlSelectManyMenu) kid).setDisabled(true);
	      } else if(kid instanceof HtmlSelectOneListbox){
	    	  ((HtmlSelectOneListbox) kid).setReadonly(true);
	    	  ((HtmlSelectOneListbox) kid).setDisabled(true);
	      } else if(kid instanceof HtmlSelectOneMenu){
	    	  ((HtmlSelectOneMenu) kid).setReadonly(true);
	    	  ((HtmlSelectOneMenu) kid).setDisabled(true);
	      } else if(kid instanceof HtmlSelectOneRadio){
	    	  ((HtmlSelectOneRadio) kid).setReadonly(true);
	    	  ((HtmlSelectOneRadio) kid).setDisabled(true);
	      } else if(kid instanceof HtmlSelectOneRadio){
	    	  ((HtmlSelectOneRadio) kid).setReadonly(true);
	    	  ((HtmlSelectOneRadio) kid).setDisabled(true);
	      } else if(kid instanceof HtmlCommandButton){
	    	  ((HtmlCommandButton) kid).setReadonly(true);
	    	  ((HtmlCommandButton) kid).setDisabled(true);
	    	  kid.setRendered(false);
	      } else if(kid instanceof HtmlAjaxCommandButton){
	    	  if(((HtmlAjaxCommandButton) kid).getValue()==null||!"Cancel".equalsIgnoreCase(((HtmlAjaxCommandButton) kid).getValue().toString())){
	    		  ((HtmlAjaxCommandButton) kid).setDisabled(true);
		    	  kid.setRendered(false);
	    	  }
	      } else if(kid instanceof HtmlAjaxCommandLink){
	    	  ((HtmlAjaxCommandLink) kid).setDisabled(true);
	    	  kid.setRendered(false);
	      } else if(kid instanceof HtmlCalendar){
	    	  ((HtmlCalendar) kid).setDisabled(true);
	    	  kid.getAttributes().put("style",DISABLE_STYLE);
	      }else if(kid instanceof HtmlFileUpload){
	    	  ((HtmlFileUpload) kid).setDisabled(true);
	    	  kid.getAttributes().put("style",DISABLE_STYLE);
	      }
		
		//org.richfaces.component.html.HtmlFileUpload
	}

}
