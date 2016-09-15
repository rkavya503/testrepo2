package com.akuacom.pss2.drw.validator;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

public class DREventCalendarValidator implements Validator,java.io.Serializable {
	
	private static final long serialVersionUID = -4259611077820028265L;
	
	@Override
	public void validate(FacesContext context, UIComponent component,Object value) throws ValidatorException {
		if ((context == null) || (component == null)) {
            throw new NullPointerException();
        }
	}

}
