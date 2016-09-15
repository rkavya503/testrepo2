package com.akuacom.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Valid only in conjunction with @Test methods, causes the profile to dump the top 100 after method execution
 *  
 * (c) 2010 Akuacom. All rights
 * reserved. Redistribution and use in source and binary forms, with or without
 * modification, is prohibited.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
public @interface DumpProfile {}
