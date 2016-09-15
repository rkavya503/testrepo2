package com.akuacom.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks methods require a specific database (comprised of one or more sql files) to be loaded
 * AFTER the annotated method executes
 * These are ignored by the ProfilingMaps.BlockedMethodThread
 *  (c) 2010 Akuacom. All rights reserved.
 *  Redistribution and use in source and binary forms, with or without modification, is prohibited.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
public @interface SaveDbBefore {
	/**
	 * source db
	 * @return
	 */
	String db() default "pss2";
	
	/**
	 * Path to sql file
	 * @return
	 */
	String[] tables() default {""};
	
	String filePath();
	
	boolean appendTimeMillis() default false;
	
}
