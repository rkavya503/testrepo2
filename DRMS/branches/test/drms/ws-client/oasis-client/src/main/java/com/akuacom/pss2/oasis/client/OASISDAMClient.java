package com.akuacom.pss2.oasis.client;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import com.akuacom.pss2.program.rtp.PriceTransition;
import com.akuacom.pss2.program.rtp.RTPPriceConnector;

public class OASISDAMClient extends OASISClient implements RTPPriceConnector {

	private static final int MINUTE = 60*1000;
	private static final int HOUR = 60*MINUTE;
 
	@Override
	public int getIntervalsPerDay() {
		return 1;		// DAM is a daily report
	}

	@Override
	public long getRecommendedPollingInterval() {
		return 2 * HOUR;		// every couple hours should be plenty
	}
	
	public int getNumFutureIntervals(){
		return 1;
	}
	
	@Override
	public List<PriceTransition> getPriceReport(Calendar startingAt,String locationID) {
		
		String dateString = new SimpleDateFormat("yyyyMMdd").format(startingAt.getTime());
		if (locationID == null)
		{
			locationID = "LAPLMG1_7_B2";
		}
		String endPoint =
            "http://oasissta.caiso.com/mrtu-oasis/" + 
     	   "SingleZip?queryname=PRC_LMP&startdate=" + 
     	    dateString + "&enddate=" + dateString + 
     	    "&market_run_id=DAM&node="+locationID;
		
		List<PriceTransition> prices = super.getPrices(endPoint);
		return prices;
	}
}
