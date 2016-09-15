package com.akuacom.pss2.program.scertp;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import com.akuacom.ejb.BaseEntityFixture;
import static com.akuacom.test.TestUtil.*;


public class RTPConfigTest  extends BaseEntityFixture<RTPConfig> {

	@Override
	public RTPConfig generateRandomIncompleteEntity() {
		RTPConfig rtp = new RTPConfig();
		
		String name = generateRandomString();
		rtp.setName(name);
		assertEquals(name, rtp.getName());
		
		double startTemp = generateRandomDouble();
		rtp.setStartTemperature(startTemp);
		assertEquals(startTemp, rtp.getStartTemperature(), 0.01);
		
		double endTemp = generateRandomDouble();
		rtp.setEndTemperature(endTemp);
		assertEquals(endTemp, rtp.getEndTemperature(), 0.01);
		
		Date startTime = new Date();
		rtp.setStartTime(startTime);
		assertEquals(startTime, rtp.getStartTime());
		
		Date endTime = new Date();
		rtp.setEndTime(endTime);
		assertEquals(endTime, rtp.getEndTime());
		
		double rate = generateRandomDouble();
		rtp.setRate(rate);
		assertEquals(rate, rtp.getRate(), 0.01);
		
		String unit = generateRandomString();
		rtp.setUnit(unit);
		assertEquals(unit, rtp.getUnit());
		
		String seasonName = generateRandomString();
		rtp.setSeasonName(seasonName);
		assertEquals(seasonName, rtp.getSeasonName());
		
		String programVersion = generateRandomStringOfLength(32);
		rtp.setProgramVersionUuid(programVersion);
		assertEquals(programVersion, rtp.getProgramVersionUuid());
		
		return rtp;
	}

}
