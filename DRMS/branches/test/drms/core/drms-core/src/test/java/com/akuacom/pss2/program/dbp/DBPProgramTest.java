package com.akuacom.pss2.program.dbp;

import com.akuacom.pss2.program.ProgramTest;
import static org.junit.Assert.*;

/**
 * Unit test for DBPEvent entity
 * 
 * @author Brian Chapman
 * 
 */
public class DBPProgramTest extends ProgramTest {

	@Override
	public DBPProgram generateRandomIncompleteEntity() {
		DBPProgram program = new DBPProgram();
		program = generateRandomIncompleteEntity(program);
		
		/* Add additional getter/finder tests as 
		 * required and if this entity ever get's it's own
		 * fields.
		 * 
		 */

		return program;
	}
	
}
