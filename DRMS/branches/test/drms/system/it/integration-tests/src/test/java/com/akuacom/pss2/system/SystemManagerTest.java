/**
 * 
 */
package com.akuacom.pss2.system;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Course-grained system testing of the {@link SystemManager}.
 * 
 * @author roller
 * 
 */
public class SystemManagerTest extends SystemManagerFixture {

	@Test
	public void testManagerAvailable() {
		assertNotNull(systemManager);
	}

}
