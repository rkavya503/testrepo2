// $Revision$ $Date$
package com.akuacom.pss2.facdash;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;

import com.akuacom.common.exception.AppServiceException;
import com.akuacom.ejb.client.EJB3Factory;
import com.akuacom.pss2.core.ProgramValidationException;
import com.akuacom.pss2.event.Event;
import com.akuacom.pss2.event.EventInfo;
import com.akuacom.pss2.event.EventManager;
import com.akuacom.pss2.event.EventTimingImpl;
import com.akuacom.pss2.event.participant.signal.EventParticipantSignal;
import com.akuacom.pss2.participant.ParticipantManager;
import com.akuacom.pss2.program.Program;
import com.akuacom.pss2.program.ProgramManager;
import com.akuacom.pss2.program.scertp.NotConfiguredException;
import com.akuacom.pss2.program.scertp.SCERTPEventRateInfo;
import com.akuacom.pss2.program.scertp.SCERTPProgramEJB;
import com.akuacom.pss2.program.scertp.SCERTPProgramManager;
import com.akuacom.pss2.program.scertp.entities.Weather;
import com.akuacom.pss2.program.scertp.entities.WeatherEAO;
import com.akuacom.pss2.rule.Rule.Mode;
import com.akuacom.pss2.signal.Signal;
import com.akuacom.pss2.signal.SignalEntry;
import com.akuacom.pss2.system.SystemManager;
import com.akuacom.utils.lang.DateUtil;

public class SCERTPForecast
{
	private static final int FORECAST_DAYS = 6;
	private static final int TIME_BLOCKS = 24;

    private String[] dateStrings = new String[FORECAST_DAYS];
	private String[] seasonStrings = new String[FORECAST_DAYS];
	private String[] highTempStrings = new String[FORECAST_DAYS];
	private SCERTPForecastHour[] hours = new SCERTPForecastHour[TIME_BLOCKS];

	
	public SCERTPForecast(String programName)
	{
        SystemManager systemManager = EJB3Factory.getBean(SystemManager.class);
        WeatherEAO weatherEAO = EJB3Factory.getBean(WeatherEAO.class);
        ProgramManager programManager = EJB3Factory.getLocalBean(ProgramManager.class);
        Program program = programManager.getProgram(programName);
        
		SCERTPProgramEJB programEJB = 
			(SCERTPProgramEJB)systemManager.lookupProgramBean(programName);			
		SCERTPProgramManager sceRTPProgramManager = EJB3Factory.getLocalBean(SCERTPProgramManager.class);			
  	
        // figure out where to start
		Calendar nowCalendar = GregorianCalendar.getInstance();
		// get the weather for today
        Weather weather = weatherEAO.getWeatherByDate(nowCalendar.getTime());
        
        double[] forecastHighs = new double[FORECAST_DAYS];
        if(weather != null)
        {
			forecastHighs[0] = weather.getForecastHigh0();
			forecastHighs[1] = weather.getForecastHigh1();
			forecastHighs[2] = weather.getForecastHigh2();
			forecastHighs[3] = weather.getForecastHigh3();
			forecastHighs[4] = weather.getForecastHigh4();				
			forecastHighs[5] = weather.getForecastHigh5();	
        }
		
		try
		{
			for(int i = 0; i < TIME_BLOCKS; i++)
			{
				hours[i] = new SCERTPForecastHour();
                SimpleDateFormat timeFormat = new SimpleDateFormat("H:mm.ss.SSS");
                hours[i].setStartTime(timeFormat.parse(i + ":00.0.000"));
				hours[i].setEndTime(timeFormat.parse(i + ":59.59.999"));
			}

			for(int i = 0; i < FORECAST_DAYS; i++)
			{
				//DaySignals daySignals = getSignals(programName, nowCalendar.getTime());
				DaySignals daySignals = getSignals(program, nowCalendar.getTime());
				dateStrings[i] = new SimpleDateFormat("EEE, MMM d").format(nowCalendar.getTime());
				String seasonName =
					programEJB.getSeason(programName, nowCalendar.getTime());
				seasonStrings[i] = sceRTPProgramManager.getRTPConfigName(programName, seasonName, forecastHighs[i]);
				
				highTempStrings[i] = forecastHighs[i] + " F";
				for(int j = 0; j < TIME_BLOCKS; j++)
				{
					hours[j].entries[i] = new SCERTPForecastEntry();
					Calendar blockCal = new GregorianCalendar();
					blockCal.setTime(hours[j].getStartTime());
					blockCal.set(Calendar.YEAR, nowCalendar.get(Calendar.YEAR));
					blockCal.set(Calendar.DAY_OF_YEAR, nowCalendar.get(Calendar.DAY_OF_YEAR));
					hours[j].entries[i].setPrice(getPrice(daySignals.priceEntries, blockCal.getTime()));
					hours[j].entries[i].setMode(getMode(daySignals.modeEntries, blockCal.getTime()));
				}
				nowCalendar.add(Calendar.DATE, 1);				
			}
		}
		catch (NotConfiguredException e)
		{
		}
		catch (ProgramValidationException e)
		{
		}
		catch (ParseException e)
		{
		} catch (AppServiceException e) {
		}
	}
	
	private Mode getMode(List<SignalEntry> modeSignals, Date date)
	{
        Mode retVal = Mode.NORMAL;
		Date modeTime =  DateUtil.stripDate(date);
        // Scan backward from the end of the day
        // and find the first mode entry that is before the requested time.
        // (That will the the one in effect AT the requested time)
		for(int i = modeSignals.size() - 1; i >= 0; i--)
        {
            SignalEntry entry = modeSignals.get(i);
			Date entryTime = DateUtil.stripDate(entry.getTime());
			if(entryTime.before(modeTime) || entryTime.equals(modeTime))
			{
				retVal = Mode.valueOf(entry.getLevelValue().toUpperCase());
                break;
			}
		}
		return retVal;
	}
	
	private double getPrice(List<SignalEntry> priceSignals, Date date)
	{
        double retVal = 0.0;
		Date priceTime =  DateUtil.stripDate(date);
        // Scan backward from the end of the day
        // and find the first price entry that is before the requested time.
        // (That will the the one in effect AT the requested time)
		for(int i = priceSignals.size() - 1; i >= 0; i--)
        {
            SignalEntry entry = priceSignals.get(i);
			Date entryTime = DateUtil.stripDate(entry.getTime());
			if(entryTime.before(priceTime)|| entryTime.equals(priceTime))
			{
				retVal = entry.getNumberValue();
                break;
			}
		}
		return retVal;
	}

    private class DaySignals {
        public List<SignalEntry> modeEntries;
        public List<SignalEntry> priceEntries;
    }
	private DaySignals getSignals(String programName, Date date)
		throws ProgramValidationException
	{
        
        ProgramManager programManager = EJB3Factory.getLocalBean(ProgramManager.class);
        Program program = programManager.getProgram(programName);
        return getSignals(program, date);
        
		
	}
	

	
	
	
	
	private DaySignals getSignals(Program program, Date date)
			throws ProgramValidationException
		{
	        SystemManager systemManager = EJB3Factory.getLocalBean(SystemManager.class);
	        ParticipantManager participantManager = EJB3Factory.getLocalBean(ParticipantManager.class);
	        EventManager eventManager = EJB3Factory.getLocalBean(EventManager.class);
	        String programName = program.getProgramName();

	        Signal priceSignal = null;
	        Signal modeSignal = null;
	        
	        String clientName = FDUtils.getJSFClient().getName();
	        
	        Event event = null;
	        for(EventInfo eventInfo: participantManager.
	        		getEventsForParticipant(program, clientName, true))
	        {
	        	if(eventInfo.getProgramName().equals(programName))
	        	{
	        		Event tmpEvent = 
	        			eventManager.getEvent(programName, eventInfo.getEventName());
	        		if(DateUtil.stripTime(tmpEvent.getStartTime()).getTime() ==
	        			DateUtil.stripTime(date).getTime())
	        		{
	        			event = tmpEvent;
	        			break;
	        		}
	        	}
	        }
	                 
	        if(event == null)
	        {
	        	// forecast
				SCERTPProgramEJB programEJB = 
					(SCERTPProgramEJB)systemManager.lookupProgramBean(programName);
	            EventTimingImpl eventTiming = new EventTimingImpl();
				eventTiming.setReceivedTime(DateUtil.stripTime(date));
				eventTiming.setIssuedTime(DateUtil.stripTime(date));
				eventTiming.setStartTime(DateUtil.stripTime(date));
				eventTiming.setEndTime(DateUtil.endOfDay(date));
	      
				SCERTPEventRateInfo rateInfo = new SCERTPEventRateInfo();
	        	Set<? extends Signal> inputSignals = 
	        		programEJB.getPriceSignals(program, eventTiming, rateInfo);
				for(Signal inputSignal: inputSignals)
				{
					if("price".equals(inputSignal.getSignalDef().getSignalName()))
					{
						priceSignal = inputSignal;
					}
				}

				Set<EventParticipantSignal> outputSignals = 
					programEJB.getClientForecastSignals(program, eventTiming, 
					clientName, true, inputSignals, new Date());

				for(Signal outputSignal: outputSignals)
				{
	                String signalName = outputSignal.getSignalDef().getSignalName();
					if("mode".equals(signalName))
					{
						modeSignal = outputSignal;
					}
	            }
	        }
	        else
	        {
	        	// actual event
	        	Set<? extends Signal> signals = 
	        		eventManager.getEventParticipant(event.getEventName(), clientName, true).getSignals();
				for(Signal signal: signals)
				{
					if("mode".equals(signal.getSignalDef().getSignalName()))
					{
						modeSignal = signal;
					}
	            }

	        	signals = event.getEventSignals();
				for(Signal signal: signals)
				{
					if("price".equals(signal.getSignalDef().getSignalName()))
					{
						priceSignal = signal;
					}
				}
	        }

	        List<SignalEntry>sortedPrices = new ArrayList<SignalEntry>();
	        List<SignalEntry>sortedModes  = new ArrayList<SignalEntry>();
	        if (priceSignal != null) {
	            Set<? extends SignalEntry> unsortedEntries = priceSignal.getSignalEntries();
	            sortedPrices.addAll(unsortedEntries);
	            Collections.sort(sortedPrices);
	        }
	        if (modeSignal != null) {
	            Set<? extends SignalEntry> unsortedEntries = modeSignal.getSignalEntries();
	            sortedModes.addAll(unsortedEntries);
	            Collections.sort(sortedModes);
	        }

	        DaySignals retVal = new DaySignals();
	        retVal.priceEntries = sortedPrices;
	        retVal.modeEntries = sortedModes;
	        return retVal;
		}
	
	
	
	
	/**
	 * Cancel bid mapping action.
	 * 
	 * @return the string
	 */
	public String doneAction()
	{
		return "done";
	}

	public String[] getDateStrings()
	{
		return dateStrings;
	}

	public void setDateStrings(String[] dateStrings)
	{
		this.dateStrings = dateStrings;
	}

	public String[] getHighTempStrings()
	{
		return highTempStrings;
	}

	public void setHighTempStrings(String[] highTempStrings)
	{
		this.highTempStrings = highTempStrings;
	}

	public SCERTPForecastHour[] getHours()
	{
		return hours;
	}

	public void setHours(SCERTPForecastHour[] hours)
	{
		this.hours = hours;
	}

	public class  SCERTPForecastHour
	{
		private Date startTime;
		private Date endTime;
		private SCERTPForecastEntry[] entries = 
			new SCERTPForecastEntry[FORECAST_DAYS];

		public String getTimeBlockString()
		{
            SimpleDateFormat hourFormat = new SimpleDateFormat("HH:mm");
            return hourFormat.format(startTime) + " - " +
				hourFormat.format(endTime);
		}

		public SCERTPForecastEntry[] getEntries()
		{
			return entries;
		}

		public void setEntries(SCERTPForecastEntry[] entries)
		{
			this.entries = entries;
		}

		public Date getStartTime()
		{
			return startTime;
		}

		public void setStartTime(Date startTime)
		{
			this.startTime = startTime;
		}

		public Date getEndTime()
		{
			return endTime;
		}

		public void setEndTime(Date endTime)
		{
			this.endTime = endTime;
		}
	}
	
	public class SCERTPForecastEntry
	{
		private String price="NA";
		private Mode mode;
		
		public String getPrice()
		{
			return price;
		}
		public void setPrice(double price)
		{
			if (price!=0.0)
				this.price = Double.toString(price);
		}
		public Mode getMode()
		{
			return mode;
		}
		public void setMode(Mode mode)
		{
			this.mode = mode;
		}
	}

	public String[] getSeasonStrings()
	{
		return seasonStrings;
	}

	public void setSeasonStrings(String[] seasonStrings)
	{
		this.seasonStrings = seasonStrings;
	}
}

