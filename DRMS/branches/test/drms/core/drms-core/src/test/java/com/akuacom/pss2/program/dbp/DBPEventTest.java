package com.akuacom.pss2.program.dbp;

import java.util.Date;

import com.akuacom.pss2.event.EventTest;
import static com.akuacom.test.TestUtil.*;
import static org.junit.Assert.*;

/**
 * Unit test for DBPEvent entity
 * 
 * @author Brian Chapman
 *
 */
public class DBPEventTest extends EventTest {

	@Override
	public DBPEvent generateRandomIncompleteEntity() {
		DBPEvent event = new DBPEvent();
		event = generateRandomIncompleteEntity(event);
		
		Date respondedBy = generateRandomDate();
		event.setRespondBy(respondedBy);
		assertEquals(respondedBy, event.getRespondBy());
		
		return event;
		
	}
}
