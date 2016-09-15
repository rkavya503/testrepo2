package com.akuacom.pss2.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Marker annotation; notes something that will be fixed in the future,
 * at which time whatever marked 'Throwaway' will be removed
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Throwaway {}
