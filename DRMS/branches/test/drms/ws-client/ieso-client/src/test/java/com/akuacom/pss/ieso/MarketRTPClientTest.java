package com.akuacom.pss.ieso;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import junit.framework.TestCase;
import org.junit.Ignore;
import org.junit.Test;

import com.akuacom.pss2.program.rtp.PriceTransition;

@Ignore
public class MarketRTPClientTest extends TestCase
{
	
	@Test
	public void testTimer()
	{
		MarketRTPClient rtpClient = new MarketRTPClient();
		long interval = rtpClient.getRecommendedPollingInterval();
		assertTrue(interval != 0);
	}
	
	@Test
	public void testIntervals()
	{
		MarketRTPClient rtpClient = new MarketRTPClient();
		int intervals = rtpClient.getIntervalsPerDay();
		assertTrue(intervals > 0);
		assertTrue(intervals <= 100);
	}	
	
	@Test
	public void testGetPRiceReport() {
		MarketRTPClient rtpClient = new MarketRTPClient();
		Calendar now = Calendar.getInstance();
		Calendar recentPast = Calendar.getInstance();
		Calendar wayPast = Calendar.getInstance();
		String requestedLocation = "Ontario";
		
		recentPast.add(Calendar.HOUR, -1);   // look an hour into the past
		wayPast.add(Calendar.DATE, -1);
		List<PriceTransition> priceList = rtpClient.getPriceReport(recentPast, requestedLocation);
		
		// Verify that the list of prices is not empty
		//assertTrue(priceList != null);
		//assertTrue(priceList.size() > 0);
		
		if (priceList != null)
		{
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
				assertTrue(price.getDuration() > 0);
				assertTrue(price.getLocationID() != null);
				assertTrue(price.getLocationID().equals(requestedLocation));
			}
		}
 	}
	
}
