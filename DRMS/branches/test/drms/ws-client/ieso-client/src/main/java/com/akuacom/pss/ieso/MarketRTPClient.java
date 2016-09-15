package com.akuacom.pss.ieso;

import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.XMLGregorianCalendar;

import com.akuacom.pss2.ieso.jaxb.DocBody;
import com.akuacom.pss2.ieso.jaxb.DocBody.IntertieZonalPrices;
import com.akuacom.pss2.ieso.jaxb.DocBody.IntertieZonalPrices.Prices;
import com.akuacom.pss2.ieso.jaxb.DocBody.IntertieZonalPrices.Prices.IntervalPrice;

import org.apache.log4j.Logger;
import com.akuacom.pss2.util.LogUtils;

import com.akuacom.pss2.program.rtp.PriceTransition;
import com.akuacom.pss2.program.rtp.RTPPriceConnector;

public class MarketRTPClient implements RTPPriceConnector {
	private static final Logger log = Logger.getLogger(MarketRTPClient.class);
	private static final String IESO_TIME_ZONE = "EST";

	public MarketRTPClient() {
	}

	public long getRecommendedPollingInterval() {
		return 5 * 60 * 1000; // five minutes
	}

	public int getIntervalsPerDay() {
		return 24;
	}
	
	public int getNumFutureIntervals(){
		return 1;
	}

	/**
	 * 
	 * @param sampleTime
	 *            The requested time period. Prices are in 12 5 minute blocks
	 *            for the requested hour, beginning on the hour.
	 * 
	 *            Samples from the past are available. A null sampleTime will
	 *            return the latest report.
	 * 
	 *            NOTE: Times in the returned data appear to be in Ontario local
	 *            time.
	 * 
	 * @return the oASIS report
	 */
	public List<PriceTransition> getPriceReport(Calendar startingAt,String locationID) {
		List<PriceTransition> priceList = new ArrayList<PriceTransition>();

		if (locationID == null)
		{
			locationID = "Ontario";
		}
		
		// startingAt is in the local time zone of this server 
		// (Ideally, it should the local time zone of the locationID)
		// So it needs to be adjusted to reflect Ontario, Canada standard time
		TimeZone ontarioCATimeZone = TimeZone.getTimeZone(IESO_TIME_ZONE);
		
		com.akuacom.pss2.ieso.jaxb.Document report = null;
		try {
			String reportID = "";
			if (startingAt != null) {
				Calendar reportDate = Calendar.getInstance();
				reportDate.setTime(startingAt.getTime());
				reportDate.setTimeZone(ontarioCATimeZone);
				
				DateFormat df = new SimpleDateFormat("yyyyMMdd");
				int hourOfDay = reportDate.get(Calendar.HOUR_OF_DAY);
				if (hourOfDay == 0){
					// An oddity of this data source is that hour
					// zero of today must be requested as hour 24 of yesterday
					reportDate.add(Calendar.DATE,-1);
					hourOfDay = 24;
				}
				String dateString =	df.format(reportDate.getTime());
				NumberFormat nf = NumberFormat.getIntegerInstance();
				nf.setMinimumIntegerDigits(2);
				String intervalString = nf.format(hourOfDay);
				reportID = "_" + dateString + intervalString;
			}

			String endPoint = "http://reports.ieso.ca/public/RealtimeMktPrice/PUB_RealtimeMktPrice"
					+ reportID + ".xml";

			URLConnection connection = new URL(endPoint).openConnection();
			InputStream streamIn = connection.getInputStream();

			JAXBContext jc = JAXBContext.newInstance("com.akuacom.pss2.ieso.jaxb");
			Unmarshaller unmarshaller = jc.createUnmarshaller();
			report = (com.akuacom.pss2.ieso.jaxb.Document) unmarshaller.unmarshal(streamIn);

		} catch (Exception e) {
			log.warn(LogUtils.createLogEntry("", LogUtils.CATAGORY_EVENT,
					"error connecting to ieso web service", null));
			log.debug(LogUtils.createExceptionLogEntry("",
					LogUtils.CATAGORY_EVENT, e));
		}

		if (report != null) {
			List<Object> handB = report.getDocHeaderAndDocBody();
			DocBody body = (DocBody) handB.get(1);
			XMLGregorianCalendar deliveryDate = body.getDeliveryDate();
			Calendar deliveryCal = Calendar.getInstance(ontarioCATimeZone);
			GregorianCalendar gc = deliveryDate.toGregorianCalendar(ontarioCATimeZone, null, null);
			deliveryCal.setTime(gc.getTime());  // This just set the date part
			// The time of day is within the report hour, 
			int reportHour = body.getDeliveryHour().get(0);   // This will be in Ontario time			
			deliveryCal.set(Calendar.HOUR_OF_DAY, reportHour);
			deliveryCal.getTime();	// Without this, the TZ change doesn't work right	
			deliveryCal.setTimeZone(TimeZone.getDefault());   // shift back to local time			

			List<IntertieZonalPrices> zones = body.getIntertieZonalPrices();
			for(IntertieZonalPrices zonePrices : zones)
			{
				String zoneName = zonePrices.getIntertieZoneName();
				if (zoneName.equalsIgnoreCase(locationID))
				{
					List<Prices> prices = zonePrices.getPrices();
					List<IntervalPrice> p = prices.get(0).getIntervalPrice();
					
					for (IntervalPrice price : p)
					{
						BigDecimal mcp = price.getMCP();
						double mwPrice = mcp.floatValue();  // Price of five megawatt minutes
						
						PriceTransition trans = new PriceTransition();
						trans.setTime(deliveryCal.getTime());
						deliveryCal.add(Calendar.MINUTE, 5);
						trans.setPrice(mwPrice);
						trans.setDurationSeconds(60*5);  // Five minute intervals  
						trans.setLocationID(zoneName);
						priceList.add(trans);
					}
				}
			}
		}

		return priceList;
	}

}
