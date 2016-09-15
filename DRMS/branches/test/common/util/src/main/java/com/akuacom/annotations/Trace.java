package com.akuacom.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
/**
 * Marks methods or classes to be traced
 *  (c) 2010 Akuacom. All rights reserved.
 *  Redistribution and use in source and binary forms, with or without modification, is prohibited.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Trace {
	boolean value() default false;  // true -> indicate executing thread
}