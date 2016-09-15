/**
 * 
 */
package com.akuacom.common.resource;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * A singleton that provides managed resources available in the system.
 * 
 * ManagedResources must be registered to the singleton so they are available
 * for inspection when the factory methods are queried.
 * 
 * Typically, registration is done at system initialization providing all
 * managed resources that should be used in the system.
 * 
 * 
 * @author roller
 * 
 */
public class ManagedResourceBundleFactory {

	private static ManagedResourceBundleFactory instance;

	private Set<ManagedResourceBundle> bundles = new HashSet<ManagedResourceBundle>();

	public static ManagedResourceBundleFactory getInstance() {
		if (instance == null) {
			instance = new ManagedResourceBundleFactory();
		}
		return instance;
	}

	/**
	 * Inspects all the registerd managed bundles to see if they have a matching
	 * key. Returns the FIRST BUNDLE FOUND so keys not unique will be
	 * error-proned.
	 * 
	 * @param key
	 * @return
	 */
	public ManagedResourceBundle getManagedResourceBundle(String key) {

		Iterator<ManagedResourceBundle> bundleIterator = this.bundles
				.iterator();
		ManagedResourceBundle result = null;
		while (bundleIterator.hasNext() && result == null) {
			ManagedResourceBundle bundle = bundleIterator.next();
			if (bundle.containsKey(key)) {
				result = bundle;
			}
		}
		return result;
	}

	public ManagedResourceBundle getManagedResourceBundle(
			ResourceBundleMessage key) {
		return getManagedResourceBundle(key.toString());
	}

	public void register(ManagedResourceBundle bundle) {
		bundles.add(bundle);
	}
	
}
