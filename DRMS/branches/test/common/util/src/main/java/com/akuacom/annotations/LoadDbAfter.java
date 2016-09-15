package com.akuacom.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Loads a specific database (comprised of one or more sql files) AFTER executing annotated method
 * AFTER the annotated method executes
 *  (c) 2010 Akuacom. All rights reserved.
 *  Redistribution and use in source and binary forms, with or without modification, is prohibited.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
public @interface LoadDbAfter {
    /**
     * target db
     * @return
     */
    String db() default "pss2";
    
    /**
     * Path to sql file
     * @return
     */
    String[] files();    
}
