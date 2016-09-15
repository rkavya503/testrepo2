/**
 * 
 */
package com.akuacom.pss2.notifications.usecasetest;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Linda
 *
 */
public class FindProgramParticipants extends AbstractNotificationUseCase {

	private String program;
//	private String participant;
	
	public FindProgramParticipants() {
		this(null);
	}

	public FindProgramParticipants(String program) {
		super();
		this.program=program;
	}

	/* (non-Javadoc)
	 * @see com.akuacom.pss2.usecasetest.cases.AbstractUseCase#runCase()
	 */
	@Override
	public Object runCase() throws Exception {
		List<String> partNames=new ArrayList<String>();
		try {
			partNames=getProgPartMgr().getParticipantsForProgram(program);
		}catch(Exception e){
			e.printStackTrace();
		}
		return partNames;
		
	}

}
