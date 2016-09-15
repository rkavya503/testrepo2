package com.akuacom.pss2.richsite.event.demo;

import javax.el.ELContext;
import javax.el.ELResolver;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;


public class DemoEventPhaseListener implements PhaseListener {

    public void beforePhase(PhaseEvent event){
        ELContext context = event.getFacesContext().getELContext();
        ELResolver resolver = context.getELResolver();

        JSFDemoEvent demoEvent = (JSFDemoEvent) resolver.getValue(context, null, "demoEvent");

        if (demoEvent == null) {
            return;
        }
        
        if(event.getPhaseId() == PhaseId.INVOKE_APPLICATION  ){
            demoEvent.updateModel();
        }
    }

    public void afterPhase(PhaseEvent event){

    }
    
    public PhaseId getPhaseId(){
        return PhaseId.INVOKE_APPLICATION;
    }
}
