package com.akuacom.pss2.oasis.client;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import com.akuacom.pss2.program.rtp.PriceTransition;
import com.akuacom.pss2.program.rtp.RTPPriceConnector;

public class OASISHASPClient extends OASISClient implements RTPPriceConnector {

	private static final int MINUTE = 60*1000;
	private static final int HOUR = 60*MINUTE;
 
	@Override
	public int getIntervalsPerDay() {
		return 24;		// HASP is an hourly report
	}

	@Override
	public long getRecommendedPollingInterval() {
		return 10 * MINUTE;		
	}
	
	public int getNumFutureIntervals(){
		return 1;
	}
	
	@Override
	public List<PriceTransition> getPriceReport(Calendar startingAt,String locationID) {
    	int interval = startingAt.get(Calendar.HOUR_OF_DAY) + 1;  // intervals start at 1
		String dateString = new SimpleDateFormat("yyyyMMdd").format(startingAt.getTime());
		if (locationID == null)
		{
			locationID = "LAPLMG1_7_B2";
		}
		String endPoint = "http://oasissta.caiso.com/mrtu-oasis/" + 
	 	   "SingleZip?queryname=PRC_HASP_LMP&startdate=" + 
		    dateString + "&enddate=" + dateString + "&opr_hr=" + 
		    interval + "&market_run_id=HASP&node="+locationID;	
		
		List<PriceTransition> prices = getPrices(endPoint);
		return prices;
	}

}
