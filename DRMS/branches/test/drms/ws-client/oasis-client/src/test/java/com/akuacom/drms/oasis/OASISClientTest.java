package com.akuacom.drms.oasis;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import com.akuacom.pss2.oasis.client.OASISHASPClient;
import com.akuacom.pss2.program.rtp.RTPPriceConnector;
import com.akuacom.pss2.program.rtp.PriceTransition;

import junit.framework.TestCase;

@Ignore
public class OASISClientTest  extends TestCase {
	static final String HASP_RTP_PROGRAM = "RTP HASP";
	
	@Test
	public void testTimer()
	{
		RTPPriceConnector rtpClient = new OASISHASPClient();
		long interval = rtpClient.getRecommendedPollingInterval();
		assertTrue(interval != 0);
	}
	
	@Test
	public void testIntervals()
	{
		RTPPriceConnector rtpClient = new OASISHASPClient();
		int intervals = rtpClient.getIntervalsPerDay();
		assertTrue(intervals > -100);
		assertTrue(intervals <= 100);
	}	
	
	@Test
	public void testGetPRiceReport() 
	{
		RTPPriceConnector rtpClient = new OASISHASPClient();
		Calendar now = Calendar.getInstance();
		Calendar recentPast = Calendar.getInstance();
		Calendar wayPast = Calendar.getInstance();
		String requestedLocation = null;
		
		recentPast.add(Calendar.HOUR, -1);   // look an hour into the past
		wayPast.add(Calendar.DATE, -1);
		List<PriceTransition> priceList = rtpClient.getPriceReport(recentPast, requestedLocation);
		
		// Verify that the list of prices is not empty
		//assertTrue(priceList != null);
		//assertTrue(priceList.size() > 0);
		
		// if there are prices, verify that they are sane looking
		if (priceList != null) {
			for (PriceTransition price : priceList)
			{
				// Get a price transition from the list
				assertTrue(price != null);
				
				// Verify that the starting time is in the ballpark
				Date priceTime = price.getTime();
				assertTrue(priceTime != null);
				assertTrue(priceTime.before(now.getTime()));
				assertTrue(priceTime.after(wayPast.getTime()));
				
				// See if the price looks sane
				double cash = price.getPrice();
				assertTrue(cash >= 0);
				assertTrue(cash < 1000000);
				
				// Verify that there is a non-zero duration for the price
				assertTrue(price.getDuration() == 15*60);   // HASP should be fifteen minutes
				assertTrue(price.getLocationID() != null);
				//assertTrue(price.getLocationID().equals(requestedLocation));
			}
		}
 	}

}
