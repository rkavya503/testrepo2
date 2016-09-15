package com.akuacom.pss2.data.gridpoint;

import com.akuacom.ejb.AbstractBaseEAOTest;
import com.akuacom.test.TestUtil;

public class GridPointConfigurationEAOTest extends
	AbstractBaseEAOTest<GridPointConfigurationGenEAO, GridPointConfiguration> {


	
	public GridPointConfigurationEAOTest() {
		super(GridPointConfigurationGenEAO.class);
	}

	@Override
	protected void assertEntityValuesNotEquals(GridPointConfiguration created,
			GridPointConfiguration found) {
		
	}

	@Override
	protected void mutate(GridPointConfiguration found) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void assertEntityValuesEquals(GridPointConfiguration created,
			GridPointConfiguration found) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected GridPointConfiguration generateRandomEntity() {
		
		GridPointConfiguration e=new GridPointConfiguration();
		e.setAuthenticationURL(TestUtil.generateRandomString());
		
		e.setUUID(TestUtil.generateRandomStringOfLength(8));
		return e;
	}


}
