/**
 * 
 */
package com.akuacom.pss2.obix.simulator;

import java.util.TimeZone;

import obix.Abstime;
import obix.Obj;
import obix.Real;
import obix.Uri;
import obix.io.ObixEncoder;
import obix.net.ObixSession;

import org.apache.log4j.Appender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

/**
 * @author e333812
 * 
 */
public class ObixClientSimulator {

	private long updateInterval = 15 * 60 * 1000;

	private int recordCount = 96;

	private String meterName;

	private String baseUri;

	private ObixSession session;

	private Obj usage;

    /** The logger. */
    public Logger logger = null;
	
	public ObixClientSimulator(String meterName, String baseUri,
			ObixSession session, String logFile) {
		super();
		this.meterName = meterName;
		this.baseUri = baseUri;
		this.session = session;

		usage = new Obj("usage");
		usage.setHref(new Uri(baseUri));
		
		try {
			PatternLayout layout = new PatternLayout();
			layout.setConversionPattern("%d %-5p %m%n");
			
			Appender fileAppender=new FileAppender(layout, logFile);
			
			logger = Logger.getLogger(DRASObixClient.class);
			logger.addAppender(fileAppender);
			
		}catch(Exception e){
			e.printStackTrace();
		}

	}

	public void start() {
		logger.debug("start data sending...");
		
		int value = 1;

		obix.List meter = new obix.List(meterName);
		usage.add(meter);

		for (int i = 0; i < recordCount; i++) {
			try {
				generateData(meter, value, i + 1);
				value++;
				Thread.sleep(updateInterval);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			logger.debug("end");
		}
	}

	public void generateData(obix.List meter, int value, int num) {
		try {
			Obj obj = new Obj(String.valueOf(num));

			Real real = new Real("value");
			real.setReal(value);

			long currenttime = System.currentTimeMillis();
			// System.out.println(currenttime);
			Abstime at = new Abstime("time", currenttime);
			at.setTz(TimeZone.getDefault().getID());

			obj.add(real);
			obj.add(at);

			meter.add(obj);

			String xml = ObixEncoder.toString(usage);
			logger.debug("data " + num +": \n" + xml);
			Obj response = session.write(usage);

			xml = ObixEncoder.toString(response);
			logger.debug("response: \n" + xml);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			Obj[] kids = meter.list();
			int count = kids.length;
			for (int i = 0; i < count; i++) {
				meter.remove(kids[i]);
			}
		}
	}

	public String getBaseUri() {
		return baseUri;
	}

	public void setBaseUri(String baseUri) {
		this.baseUri = baseUri;
	}

	public String getMeterName() {
		return meterName;
	}

	public void setMeterName(String meterName) {
		this.meterName = meterName;
	}

	public long getUpdateInterval() {
		return updateInterval;
	}

	public void setUpdateInterval(long updateInterval) {
		this.updateInterval = updateInterval;
	}

}
