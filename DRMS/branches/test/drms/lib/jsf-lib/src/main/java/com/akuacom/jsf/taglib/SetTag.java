package com.akuacom.jsf.taglib;

import java.util.Map;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.el.VariableMapper;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

import org.ajax4jsf.Messages;
import org.ajax4jsf.event.AjaxPhaseListener;

public class SetTag extends TagSupport{

	private static final long serialVersionUID = -4850215126876200883L;

	
	private String beanName = null;
	
	private ValueExpression value = null;

	/**
	 * @return the ajaxOnly
	 */
	public ValueExpression getValue() {
		return value;
	}

	/**
	 * @param ajaxOnly the ajaxOnly to set
	 */
	public void setValue(ValueExpression value) {
		this.value = value;
	}

	/**
	 * @return the name
	 */
	public String getBeanName() {
		return beanName;
	}

	/**
	 * @param name the name to set
	 */
	public void setBeanName(String name) {
		this.beanName = name;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.TagSupport#doStartTag()
	 */
	public int doStartTag() throws JspException {
        if (beanName == null) {
            throw new JspException(Messages.getMessage(Messages.NULL_TYPE_ATTRIBUTE_ERROR));
        }
        
		FacesContext facesContext = FacesContext.getCurrentInstance();
		Application application = facesContext.getApplication();
		ExpressionFactory factory = application.getExpressionFactory();
		ELContext elContext = facesContext.getELContext();
		ValueExpression beanNameEL = 
			factory.createValueExpression(elContext, beanName, String.class);
        if(!beanNameEL.isLiteralText()){
            throw new JspException(Messages.getMessage(Messages.NAME_MUST_BE_LITERAL));
        }
        
		Object beanValue = null;
		if (null != value) {
            if (value.isLiteralText()){
            	beanValue = value.getExpressionString();
            }
            else{
            	beanValue = value.getValue(elContext);
            }
		}
		
		if(beanValue!=null){
			// Put bean instance to ViewRoot. 
	        String beanAttributeName =  AjaxPhaseListener.VIEW_BEAN_PREFIX+ beanName;
	        //so that the bean variable will be restored during view restore phase
	        facesContext.getViewRoot().getAttributes().put(beanAttributeName, beanValue);
	        
	        //for first request
	        Map<String, Object> requestMap = facesContext.getExternalContext().getRequestMap();
	        //refresh 
	        requestMap.put(beanName, beanValue);
		}
	    return Tag.SKIP_BODY;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.TagSupport#release()
	 */
	public void release() {
		beanName = null;
		value = null;
		super.release();
	}
	
}
