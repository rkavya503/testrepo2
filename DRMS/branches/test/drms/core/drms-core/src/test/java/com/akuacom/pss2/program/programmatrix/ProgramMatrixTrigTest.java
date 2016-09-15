/**
 * 
 */
package com.akuacom.pss2.program.programmatrix;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.akuacom.pss2.program.matrix.ProgramMatrix;
import com.akuacom.pss2.program.matrix.ProgramMatrixTrig;

/**
 * @author Linda
 *
 */
//8.31.2010 Linda: DRMS-1382 add test class for DRMS-866
public class ProgramMatrixTrigTest {

	@Test
	public void testCoexist(){
		
	    List<ProgramMatrix> programMatrixList=new ArrayList<ProgramMatrix>();

		ProgramMatrix programMatrix1=new ProgramMatrix();		
		programMatrix1.setCoexist(true);
		programMatrix1.setUUID("programMatrix_uuid");
		programMatrix1.setProgram1UUID("program1_uuid");
		programMatrix1.setProgram2UUID("program2_uuid");
		programMatrixList.add(programMatrix1);

		ProgramMatrix programMatrix2=new ProgramMatrix();		
		programMatrix2.setCoexist(true);
		programMatrix2.setUUID("programMatrix_uuid");
		programMatrix2.setProgram1UUID("program3_uuid");
		programMatrix2.setProgram2UUID("program4_uuid");
		programMatrixList.add(programMatrix2);
		
		ProgramMatrixTrig trig=new ProgramMatrixTrig();
		trig.setProgramMatrix(programMatrixList);
		
		boolean ret=trig.coexist("program3_uuid", "program4_uuid");
		assertTrue(ret);
		
		ret=trig.coexist("program1_uuid", "program2_uuid");
		assertTrue(ret);
		
		ret=trig.coexist("program3_uuid", "program2_uuid");
		assertFalse(ret);
		
	}
	
}
