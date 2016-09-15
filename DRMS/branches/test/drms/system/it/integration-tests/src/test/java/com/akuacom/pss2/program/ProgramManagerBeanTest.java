/**
 * 
 */
package com.akuacom.pss2.program;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.akuacom.common.exception.AppServiceException;
import com.akuacom.ejb.jboss.test.JBossFixture;

/**
 *
 */
public class ProgramManagerBeanTest {

	protected ProgramManager programManager = JBossFixture.lookupSessionRemote(ProgramManagerBean.class);
	
	protected Map<String, Integer> programClassMap = new HashMap<String, Integer>();
	
	@Before
	public void setup(){
		List<Program> allPrograms=programManager.getProgramsAsPrograms();
		String classNameBackup=null;
		int count=0;
		for (Program program:allPrograms){
			String programClass=program.getProgramClass();
			if (programClass==null || programClass.trim().length()==0)
				continue;
			
			if (classNameBackup==null){
				classNameBackup=program.getProgramClass();
			}
				
			if (!classNameBackup.equals(programClass)) {
				if (programClassMap.get(classNameBackup) !=null) 
					count = programClassMap.get(classNameBackup) + count;
				programClassMap.put(classNameBackup, count);
				classNameBackup=program.getProgramClass();
				count=0;
			}
			count++;
		}
		// For the last program class in the list
		if (count > 0){
			if (programClassMap.get(classNameBackup) !=null) 
				count = programClassMap.get(classNameBackup) + count;
			programClassMap.put(classNameBackup, count);
		}
	}

	@Test
	public void testFindProgramsByProgramClass() throws AppServiceException{
		List<String> programClasses = new ArrayList<String>();
		int count = 0;
		for (String key : programClassMap.keySet()) {
			programClasses.add(key);
			count += programClassMap.get(key);
		}

		if (programClasses.size() != 0) {
			List<Program> programs = programManager
					.findProgramsByProgramClass(programClasses);
			assertEquals(count, programs.size());
			System.out.println("done");
		}
	}
}
