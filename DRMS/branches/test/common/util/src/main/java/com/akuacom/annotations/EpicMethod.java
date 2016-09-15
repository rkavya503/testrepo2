package com.akuacom.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks methods that are expected to take a long time to execute
 * These are ignored by the ProfilingMaps.BlockedMethodThread
 *  (c) 2010 Akuacom. All rights reserved.
 *  Redistribution and use in source and binary forms, with or without modification, is prohibited.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
public @interface EpicMethod {}
