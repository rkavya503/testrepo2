// $Revision$ $Date$
package com.akuacom.pss2.facdash;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import com.akuacom.pss2.rule.Rule;

public class EventRuleConverter implements javax.faces.convert.Converter
{

	public Object getAsObject(FacesContext context, UIComponent component, String value)
	{
		try
		{
			String[] values = value.split("_");
			JSFEventRule rule = new JSFEventRule();
			rule.setDelete(Boolean.parseBoolean(values[0]));
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
            rule.getRule().setStart(dateFormat.parse(values[1]));
			rule.getRule().setEnd(dateFormat.parse(values[2]));
			rule.getRule().setMode(Rule.Mode.valueOf(values[3]));
			rule.getRule().setVariable(values[4]);
			rule.setOperator(values[5]);
			rule.getRule().setValue(Double.parseDouble(values[6]));
			return rule;
		}
		catch (ParseException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public String getAsString(FacesContext context, UIComponent component, Object value)
	{
		JSFEventRule rule = (JSFEventRule)value;
		return rule.toString();
	}
	
}
