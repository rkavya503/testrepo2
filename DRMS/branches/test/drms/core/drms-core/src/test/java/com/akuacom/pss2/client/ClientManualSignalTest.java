package com.akuacom.pss2.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.akuacom.ejb.BaseEntityFixture;
import com.akuacom.pss2.program.bidding.BidConfig;
import static com.akuacom.test.TestUtil.*;

/**
 * Tests the ClientManualSignal entity
 * 
 * @author brian Created on 2010.11.09
 * 
 */

public class ClientManualSignalTest extends BaseEntityFixture<ClientManualSignal> {

	@Override
	public ClientManualSignal generateRandomIncompleteEntity() {
		ClientManualSignal cms = new ClientManualSignal();
		
		String name = generateRandomStringOfLength(64);
		cms.setName(name);
		assertEquals(name, cms.getName());
		
		return cms;
	}

	@Test
	public void testCompareTo() {
		ClientManualSignal cms1 = new ClientManualSignal();
		ClientManualSignal cms2 = new ClientManualSignal();
		cms1.setUUID(null);
		cms2.setUUID("cms2_uuid");
		assertEquals(-1, cms1.compareTo(cms2));

		cms1.setUUID("cms1_uuid");
		assertTrue(cms1.compareTo(cms2) != 0);

		cms1.setUUID("cms2_uuid");
		assertTrue(cms1.compareTo(cms2) == 0);
	}

}
