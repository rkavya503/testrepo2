package com.akuacom.pss2.program.scertp;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class METARObservation {

	private String metar;
	private String stationID;
	private Calendar observationTime;
	private Integer currentTempC = null;
	private Double sixHourHighC = null;
	private Double prevDayHighC = null;
    private boolean isNil = false;

	// A METAR report looks like the following:
	// KCQT 281747Z AUTO 00000KT 5SM HZ OVC011 19/14 A2987 RMK AO2 SLP114 T01940144 10200 20183 53014 TSNO $ 
	// KCKB 290453Z 00000KT 8SM FEW060 SCT085 22/21 A2987 RMK AO2 RAE02 SLP098 P0000 T02170206 402940206
	// They vary considerable in content, and good documentation can be found at
	// http://www.met.tamu.edu/class/METAR/quick-metar.html
	public METARObservation(String rawMETAR) throws METARObservationException {
		
        if (rawMETAR == null || rawMETAR.trim().length()==0) {
            // Nil report is no report at all
            isNil = true;
            metar = null;
            return;            
        }
		metar = rawMETAR.trim();
        
		try {
			String stationStr = metar.substring(0,4);
			setStationID(stationStr);

            if (metar.substring(5).equals("NIL")) {
                // Nil report is no report at all
                isNil = true;
                return;
            }
			//********************************************
			// Set the date and time of the observation
			// This will be reported in the time zone of this computer
			//********************************************
			String dateStr = metar.substring(5, 7);
			String hrStr = metar.substring(7,9);
			String minStr = metar.substring(9,11);
			int da = Integer.parseInt(dateStr);
			int hr = Integer.parseInt(hrStr);
			int min = Integer.parseInt(minStr);
			Calendar metarCal = Calendar.getInstance();
			int dateToday = metarCal.get(Calendar.DAY_OF_MONTH);	// For reference to detect month rollover
			// deal with month rollover
			metarCal.setTimeZone(TimeZone.getTimeZone("UTC"));
			if (da > dateToday) // looking into past makes a bigger day of month if the month rolls over
			{
				metarCal.add(Calendar.MONTH, -1);  // This metar is from the end of previous month
			}
			metarCal.set(Calendar.DAY_OF_MONTH, da);
			metarCal.set(Calendar.HOUR_OF_DAY, hr);
			metarCal.set(Calendar.MINUTE, min);
			metarCal.set(Calendar.SECOND, 0);
			metarCal.getTime();	// Without this, the TZ change doesn't work right		
			metarCal.setTimeZone(TimeZone.getDefault());	// move to local time
			setObservationTime(metarCal);
			
			//**********************************************
			// Scan ahead to the beginning of the "remarks" section.
			// That's where six hour or previous day high temps may be found
			//**********************************************
			int rmkIdx = metar.indexOf(" RMK ");
			String baseReport = metar + "  ";  // trailing space helps with parsing
			String remark = null;
			if (rmkIdx > 0) {
				baseReport = metar.substring(0, rmkIdx) + "  ";  // trailing space helps with parsing
				remark = metar.substring(rmkIdx) + "  ";  // trailing space helps with parsing
			}
			
			// Look for the current observed temperature
			// It will be in the base report as [M]nn/[M]nn
			// That is, possibly an "M", then two digits,
			// then a slash, then possibly an "M", then two digits
			// all surrounded by spaces.  The first nn is the temp.
			// The second nn is the dew point.  Ms mean "minus"
			int slashIdx = 0;
			while (slashIdx >= 0) {
				slashIdx = baseReport.indexOf('/', slashIdx+1); // scan to the next slash until there aren't any more
				if (slashIdx < 0) break;
				boolean neg = false;
				if (baseReport.charAt(slashIdx-3) == 'M') neg = true;
				if ( (neg && baseReport.charAt(slashIdx-4) == ' ') || 
				     (!neg && baseReport.charAt(slashIdx-3) == ' '))
				{
					if (Character.isDigit(baseReport.charAt(slashIdx-2)) &&
					    Character.isDigit(baseReport.charAt(slashIdx-1)) &&
					    ( Character.isWhitespace(baseReport.charAt(slashIdx+1)) || Character.isDigit(baseReport.charAt(slashIdx+2)) ))
					{
						String st = baseReport.substring(slashIdx-2,slashIdx);
						int temp = Integer.parseInt(st);
						if (neg) temp = -temp;
						setCurrentTempC(temp);
						break;  // found the temp
					}
				}
			}
			
			
			if (remark != null)
			{
				String hiTemp = null;
				
				// The 6-hour high temp is a 1 followed by 4 digits, surrounded by spaces
				int hiTempIdx = remark.indexOf(" 1");		
				if (hiTempIdx > 0 && remark.length() > (hiTempIdx+6)){
					String maybeTemp = remark.substring(hiTempIdx, hiTempIdx+7);
					if (maybeTemp.startsWith(" ") && maybeTemp.endsWith(" ") 
							&& Character.isDigit(maybeTemp.charAt(1)) // the 1
							&& Character.isDigit(maybeTemp.charAt(2))
							&& Character.isDigit(maybeTemp.charAt(3))
							&& Character.isDigit(maybeTemp.charAt(4))
							&& Character.isDigit(maybeTemp.charAt(5)))
					{
						hiTemp = maybeTemp.substring(2);  // skip the initial " 1"
						double temp = Float.parseFloat(hiTemp);
						temp /= 10.0;
						if (temp >= 100) temp = 100 - temp;  // negative number
						setSixHourHighC(temp);
					}
				}

				// The 24-hour high/low temp is a 4 followed by 8 digits, surrounded by spaces
			    hiTempIdx = remark.indexOf(" 4");	
				if (hiTempIdx > 0 && remark.length() > (hiTempIdx+10)){
					String maybeTemp = remark.substring(hiTempIdx, hiTempIdx+11);
					if (maybeTemp.startsWith(" ") && maybeTemp.endsWith(" ") 
							&& Character.isDigit(maybeTemp.charAt(1))  // the 4
							&& Character.isDigit(maybeTemp.charAt(2))
							&& Character.isDigit(maybeTemp.charAt(3))
							&& Character.isDigit(maybeTemp.charAt(4))
							&& Character.isDigit(maybeTemp.charAt(5))
							&& Character.isDigit(maybeTemp.charAt(6))
							&& Character.isDigit(maybeTemp.charAt(7))
							&& Character.isDigit(maybeTemp.charAt(8))
							&& Character.isDigit(maybeTemp.charAt(9)))
					{
						hiTemp = maybeTemp.substring(2, 6);  // skip the initial " 4", and only get the high
						double temp = Float.parseFloat(hiTemp);
						temp /= 10.0;
						if (temp >= 100) temp = 100 - temp;  // negative number
						setPrevDayHighC(temp);
					}				
				}
			}
			} catch(Exception iox)
			{
				throw new METARObservationException("Exception parsing METAR ["+metar+"]", iox);
			}
	}
	
	private double CtoF(double Tc) {
		double factor = 9.0/5.0;
		double Tf = factor*Tc+32; 
		return Tf;
	}	
	
	public String getStationID() {
		return stationID;
	}

	public void setStationID(String stationID) {
		this.stationID = stationID;
	}

	public Calendar getObservationTime() {
		return observationTime;
	}

	public void setObservationTime(Calendar observationTime) {
		this.observationTime = observationTime;
	}

	public Integer getCurrentTempF() {
		if (currentTempC == null) return null;
		return (int)Math.round(CtoF(currentTempC));
	}

	public Integer getCurrentTempC() {
		return currentTempC;
	}

	public void setCurrentTempC(Integer currentTemp) {
		this.currentTempC = currentTemp;
	}


	public Double getSixHourHighF() {
		if (sixHourHighC == null) return null;
		return CtoF(sixHourHighC);
	}

	public Double getSixHourHighC() {
		return sixHourHighC;
	}

	public void setSixHourHighC(Double sixHourHigh) {
		this.sixHourHighC = sixHourHigh;
	}

	public Double getPrevDayHighF() {
		if (prevDayHighC == null) return null;
		return CtoF(prevDayHighC);
	}
	
	public Double getPrevDayHighC() {
		return prevDayHighC;
	}

	public void setPrevDayHighC(Double dayHigh) {
		this.prevDayHighC = dayHigh;
	}

	public String toString() {
		DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
		String dateString = df.format(getObservationTime().getTime());
		StringBuffer bufOut = new StringBuffer("METAR "+getStationID()+" "+dateString);
		if (getCurrentTempF() != null) bufOut.append("    tempF:"+getCurrentTempF());
		if (getSixHourHighF() != null) bufOut.append("    6HrHiF:"+getSixHourHighF());
		if (getPrevDayHighF() != null) bufOut.append("    PrevDayHiF:"+getPrevDayHighF());
        bufOut.append(" --- Raw METAR: ["+this.metar+"]");
		return bufOut.toString();
	}

    /**
     * @return the isNil
     */
    public boolean isNil() {
        return isNil;
    }

}
