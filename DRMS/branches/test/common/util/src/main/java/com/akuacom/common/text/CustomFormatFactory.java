/**
 * 
 */
package com.akuacom.common.text;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

/**Delivers the custom default formats registered with the system.  
 * 
 * Other custom formats may be used in more specific areas, but this delivers those geneal formats that generally satisfy formatting.
 * 
 * 
 * @author roller
 *
 */
public class CustomFormatFactory {

	private static CustomFormatFactory instance;

	 CustomFormatFactory(){};
	public static synchronized CustomFormatFactory getInstance(){
		if(instance == null){
			instance = new CustomFormatFactory();
			//TODO:move this registration to a system initializer allowing more customization
			instance.register(new DoubleFormat());
			instance.register(new FloatFormat());
			instance.register(new IntegerFormat());
			instance.register(new LongFormat());
			
		}
		return instance;
	}
	
	private Map<Class<? extends Object>,CustomFormat> customFormats = new HashMap<Class<? extends Object>, CustomFormat>	();
	
	public CustomFormat getFormat(Class<? extends Object> typeBeingHandled){
		CustomFormat format = this.customFormats.get(typeBeingHandled);
		//do extension search if no direct match found
		if(format == null){
			Set<Entry<Class<? extends Object>, CustomFormat>> entries = this.customFormats.entrySet();
			Iterator<Entry<Class<? extends Object>, CustomFormat>> entryIterator = entries.iterator();
			
			while (entryIterator.hasNext() && format == null) {
				Entry<Class<? extends Object>, CustomFormat> entry =  entryIterator.next();
				Class<? extends Object> key = entry.getKey();
				if(key.isAssignableFrom(typeBeingHandled)){
					format = entry.getValue();
				}
			}
		}
		if(format == null){
			//can't register this because every object would qualify
			format = new DefaultFormat();
		}
		return format;
	}
	/**Puts the format into a map keyed off the type. 
	 * 
	 * @param format
	 * @return
	 */
	public CustomFormat register(CustomFormat format){
		return this.customFormats.put(format.handlesType(), format);
	}
	
}
