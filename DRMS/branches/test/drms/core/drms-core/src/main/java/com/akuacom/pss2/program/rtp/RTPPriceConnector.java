/*
 * www.akuacom.com - Automating Demand Response
 * 
 * Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 *
 */
package com.akuacom.pss2.program.rtp;

import java.util.Calendar;
import java.util.List;

/**
 * 
 * @author pierson
 *
 * The RTPPriceConnector interface is implemented by data providers that feed
 * real time price programs.  The price connector encapsulates knowledge of 
 * how to communicate with some remote pricing source and turn its data
 * into a standardize stream of PriceTransition pojos
 */
public interface RTPPriceConnector {
	
	/**
	 * Return a suggested time the caller should wait between
	 * instancing of polling this source of price data.
	 * Some price sources might change on a minute-by-minute basis,
	 * while others might only be updated daily
	 * 
	 * If a value less than zero is returned, the report
	 * might not be polled at all
	 *	 
	 * @return  suggested polling interval
	 */
	public long getRecommendedPollingInterval(); 
	
	/**
	 * Return the number of report intervals per day
	 * for this data source
	 * For example, an hourly RTP report would have 24 intervals
	 * but a daily RTP report might have 1
	 * 
	 * @return      the number of intervals
	 */
	public int getIntervalsPerDay();
	
	
	/**
	 * Return the number of future report intervals that
	 * can normally be expected to be available for this data source
	 * (Future meaning beyond the current interval.)
	 * For example, a 1-hour interval data source might promise
	 * prices for six hours into the future, which would be six future intervals.
	 * On the other hand, a day-ahead service with one interval per day
	 * might only be expected to have one interval into the future (tomorrow).
	 * Likewise, an hour-ahead RTP service might also only 
	 * promise one future interval.
	 *	 
	 * @return      the number of intervals that should be expected beyond the current one
	 */
	public int getNumFutureIntervals();
	
	
	/**
	 * Return a list of PriceTransition objects, representing
	 * a report of real time prices
	 * The connector implementation is responsible for contacting
	 * a source of price data, obtaining a report, and parsing
	 * the prices into the standard form
	 *	 
	 * @param  startingAt The the returned list of prices should begin at
	 * @param  locationID A string identifying a node, zone, etc. within which
	 * 	       to request prices
	 * @return  list of PriceTransition objects representing price periods
	 */
	public List<PriceTransition> getPriceReport(Calendar startingAt, String locationID);
}
