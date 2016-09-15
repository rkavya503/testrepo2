package com.akuacom.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Marks methods or classes to NOT be traced
 *  (c) 2011 Akuacom. All rights reserved.
 *  Redistribution and use in source and binary forms, with or without modification, is prohibited.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface NoTrace {

}
