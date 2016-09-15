package com.akuacom.ejb;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;

import com.akuacom.utils.lang.FieldUtil;


public class EntityUtil {
	public static <T> T neux(Class<T> c) {
		try {
			return c.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static <T> T randomlyPopulate(T o, String... ignoredFields) {
		List<String> ignored = Arrays.asList(ignoredFields);
		for(Field f : FieldUtil.getLinealFields(o.getClass()) ) {
			if(ignored.contains(f.getName())) {
				continue;
			}
			int mods = f.getModifiers();
			Class<?> c = f.getType();
			if(Modifier.isPrivate(mods) && !Modifier.isStatic(mods) && !Modifier.isFinal(mods)
					&& !Collection.class.isAssignableFrom(c) 
					&& f.getAnnotation(Id.class) == null) {
				f.setAccessible(true);
				try {
					if(c.getAnnotation(Entity.class) != null) {
						JoinColumn jc = f.getAnnotation(JoinColumn.class);
						if(jc == null) {
							continue;
						} else if(jc.nullable()) {
							continue;
						}
						f.set(o, randomlyPopulate( FieldUtil.random(c) ) );
					} else {
						f.set(o, FieldUtil.random(c));
					}
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
		return o;
	}

}
