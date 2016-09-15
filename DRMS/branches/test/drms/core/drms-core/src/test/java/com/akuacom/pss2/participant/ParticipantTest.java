/**
 * 
 */
package com.akuacom.pss2.participant;

import static com.akuacom.test.TestUtil.generateRandomBoolean;
import static com.akuacom.test.TestUtil.generateRandomDate;
import static com.akuacom.test.TestUtil.generateRandomDouble;
import static com.akuacom.test.TestUtil.generateRandomInt;
import static com.akuacom.test.TestUtil.generateRandomString;
import static com.akuacom.test.TestUtil.generateRandomStringOfLength;
import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Test;

import com.akuacom.ejb.BaseEntityFixture;

/**
 * POJO test for {@link Participant}
 * 
 * @author roller
 * 
 */
public class ParticipantTest extends BaseEntityFixture<Participant> {

	public Participant generateRandomIncompleteEntity() {
		Participant participant = new Participant();

		String name = generateRandomStringOfLength(64);
		participant.setParticipantName(name);
		assertEquals(name, participant.getParticipantName());

		int type = generateRandomInt(2).byteValue();
		participant.setType((byte) type);
		assertEquals(type, participant.getType());

		boolean control = generateRandomBoolean();
		participant.setManualControl(control);
		assertEquals(control, participant.isManualControl());

		String hostAddress = generateRandomString();
		participant.setHostAddress(hostAddress);
		assertEquals(hostAddress, participant.getHostAddress());

		String accountNumber = generateRandomStringOfLength(128);
		participant.setAccountNumber(accountNumber);
		assertEquals(accountNumber, participant.getAccountNumber());

		String firstName = generateRandomStringOfLength(45);
		participant.setFirstName(firstName);
		assertEquals(firstName, participant.getFirstName());

		String lastName = generateRandomStringOfLength(45);
		participant.setLastName(lastName);
		assertEquals(lastName, participant.getLastName());

		String meterName = generateRandomStringOfLength(128);
		participant.setMeterName(meterName);
		assertEquals(meterName, participant.getMeterName());

		boolean feedback = generateRandomBoolean();
		participant.setFeedback(feedback);
		assertEquals(feedback, participant.isFeedback());

		String meter = generateRandomStringOfLength(128);
		participant.setMeterId(meter);
		assertEquals(meter, participant.getMeterId());

		int notification = generateRandomInt(1024);
		participant.setNotificationLevel(notification);
		assertEquals(notification, participant.getNotificationLevel());

		String address = generateRandomStringOfLength(128);
		participant.setAddress(address);
		assertEquals(address, participant.getAddress());

		String grid = generateRandomStringOfLength(45);
		participant.setGridLocation(grid);
		assertEquals(grid, participant.getGridLocation());

		double lat = generateRandomDouble();
		participant.setLatitude(lat);
		assertEquals(lat, participant.getLatitude(), 0.01);

		double lon = generateRandomDouble();
		participant.setLongitude(lon);
		assertEquals(lon, participant.getLongitude(), 0.01);

		double shed = generateRandomDouble();
		participant.setShedPerHourKW(shed);
		assertEquals(shed, participant.getShedPerHourKW(), 0.01);

		double lastPrice = generateRandomDouble();
		participant.setLastPrice(lastPrice);
		assertEquals(lastPrice, participant.getLastPrice(), 0.01);

		boolean warning = generateRandomBoolean();
		participant.setOfflineWarning(warning);
		assertEquals(warning, participant.isOfflineWarning());

		boolean client = generateRandomBoolean();
		participant.setClient(client);
		assertEquals(client, participant.isClient());

		Date comTime = generateRandomDate();
		participant.setCommTime(comTime);
		assertEquals(comTime, participant.getCommTime());

		Integer status = generateRandomInt(8);
		participant.setStatus(status);
		assertEquals(status, participant.getStatus());

		boolean testAccount = generateRandomBoolean();
		participant.setTestAccount(testAccount);
		assertEquals(testAccount, participant.getTestAccount());

		boolean activated = generateRandomBoolean();
		participant.setActivated(activated);
		assertEquals(activated, participant.getActivated());

		boolean dataEnabler = generateRandomBoolean();
		participant.setDataEnabler(dataEnabler);
		assertEquals(dataEnabler, participant.getDataEnabler());

		// TODO: populate participant.setContacts
		// TODO: populate parent
		return participant;
	}

	/**
	 * Related issues DRMS-1171, DRMS-1394 Initial date Aug 30, 2010
	 * 
	 * @author Li Fei
	 */

	@Test
	public void testGetAddress() {
		Participant participant = new Participant();

		participant.setAddress("");
		assertEquals("", participant.getAddress());

		participant.setAddress("Not Null");
		assertEquals("Not Null", participant.getAddress());

		participant.setAddress(null);
		assertEquals("", participant.getAddress());
	}
}
