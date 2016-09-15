package com.akuacom.pss2.program.demo;
import com.akuacom.pss2.program.ProgramTest;
import static org.junit.Assert.*;

/**
 * Unit test for DemoProgram entity
 * 
 * @author Brian Chapman
 * 
 */
public class DemoProgramTest extends ProgramTest {

	@Override
	public DemoProgram generateRandomIncompleteEntity() {
		DemoProgram program = new DemoProgram();
		program = generateRandomIncompleteEntity(program);
		
		/* Add additional getter/finder tests as 
		 * required and if this entity ever get's it's own
		 * fields.
		 * 
		 */

		return program;
	}
	
}
