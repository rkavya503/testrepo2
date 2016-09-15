package com.akuacom.pss2.richsite;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;

import scala.Tuple2;

import com.akuacom.utils.JsUtil;
import com.akuacom.utils.NumberFormats;


/**
 * wrapper for Program class for Op-dash prototype
 * TODO merge with Program when prototype stabilizes
 *
 */
public class OdProgram implements Serializable {
    final private static String DIV_SHED_GAUGE ="divShedGauge";
    final private static String DIV_LARGE_SHED_GAUGE = "divLShedGauge";
    final private static String DIV_MAP = "divMap";
    final private static String DIV_UC= "divUc";
    final private static String DIV_UC_LEGEND= "divUcL";
    final public static String DIV_OFFLINE = "divOL";
    final public static String DIV_OPTOUT = "divOO";
    
    
    final static Logger log = Logger.getLogger(OdProgram.class);
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String programName;
    private List<OdParticipant> participants = new ArrayList<OdParticipant>();
    private Tuple2<Float, Float> center;
    private List<Tuple2<Integer, Float>> baseline = new ArrayList<Tuple2<Integer, Float>>();
    private List<Tuple2<Integer, Float>> actual = new ArrayList<Tuple2<Integer, Float>>();

    private List<OdEvent> currentEvents = new ArrayList<OdEvent>();
    private List<OdEvent> pendingEvents = new ArrayList<OdEvent>();
    
    public OdProgram() {
    }

    public OdProgram(String name ) {
        this.programName = name;
    }

    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String name) {
        this.programName = name;
    }


    public int getOfflineClients() {
        return getOfflineList().size();
    }

    public String getShedGaugeDiv() {
        return "<div id=\"" + DIV_SHED_GAUGE + JsUtil.asJSVar(programName) + "\" />";
    }

    public String getLargeShedGaugeDiv() {
        return "<div id=\"" + DIV_LARGE_SHED_GAUGE+ JsUtil.asJSVar(programName) + "\" />";
    }
    public String getShedGaugeMethod() {
        return "akuacom.widgets.drawShedGauge({id:\"" + DIV_SHED_GAUGE + JsUtil.asJSVar(programName) + "\",w:60, m:" + getTotalShed() + ", v:" + getAvailableShed() + ",g:true})";
    }
    public String getShedGaugeScript() {
        return "<script>"+getShedGaugeMethod() +"</script>";
     }

    public String getShedGaugeMethodIdx() {
        return "akuacom.widgets.drawShedGauge({id:\"" + DIV_SHED_GAUGE + JsUtil.asJSVar(programName) + "\",w:60,prog:progs[\""+ JsUtil.asJSVar(programName) +"\"],g:true})";
    }

    public String getShedGaugeScriptIdx() {
        return "<script>"+getShedGaugeMethodIdx() +"</script>";
    }

    public String getLargeShedGaugeScript() {
        return "<script>akuacom.widgets.drawShedGauge({id:\"" + DIV_LARGE_SHED_GAUGE + JsUtil.asJSVar(programName) + "\",w:100, m:" + getTotalShed()+ ", v:" + getAvailableShed()  + ",g:true})</script>";
    }

    public String getLargeShedGaugeMethodIdx() {
        return "akuacom.widgets.drawShedGauge({id:\"" + DIV_LARGE_SHED_GAUGE + JsUtil.asJSVar(programName) + "\",w:100,prog:progs[\"" 
            + JsUtil.asJSVar(programName)+ "\"],g:true})";
    }

    public String getLargeShedGaugeScriptIdx() {
        return "<script>"+getLargeShedGaugeMethodIdx()+"</script>";
    }

    public List<OdParticipant> getParticipants() {
        return participants;
    }

    public void setParticipants(
            List<OdParticipant> participants) {
        this.participants =participants;
    }

    public void addParticipant(OdParticipant p) {
        participants.add(p);
        p.setProgram(this);
    }
    
    public Tuple2<Float, Float> getCenter() {
        return center;
    }

    public String getCenterLatLong() {
        if (center == null) {
            calcCenter();
        }
        return "new google.maps.LatLng(" + center._1 + "," + center._2 + ")";
    }

    public void setCenter(Tuple2<Float, Float> center) {
        this.center = center;
    }

    public List<OdEvent> getCurrentEvents() {
        return currentEvents;
    }

    public void setCurrentEvents(List<OdEvent> currentEvents) {
        this.currentEvents = currentEvents;
    }

    public List<OdEvent> getPendingEvents() {
        return pendingEvents;
    }

    public void setPendingEvents(List<OdEvent> pendingEvents) {
        this.pendingEvents = pendingEvents;
    }

    public List<Tuple2<Integer, Float>> getBaseline() {
        return baseline;
    }

    public void setBaseline(List<Tuple2<Integer, Float>> baseline) {
        this.baseline = baseline;
    }

    public List<Tuple2<Integer, Float>> getActual() {
        return actual;
    }

    public void setActual(List<Tuple2<Integer, Float>> actual) {
        this.actual = actual;
    }

    public void calcCenter() {
        if (participants.isEmpty()) {
            center = new Tuple2<Float, Float>(0f, 0f);
        } else {
            float latitude = 0;
            float longitude = 0;
            for (OdParticipant p : participants) {
                latitude += p.getLatitude();
                longitude += p.getLongitude();
            }
            center = new Tuple2<Float, Float>(latitude
                    / participants.size(), longitude
                    / participants.size());
        }
    }
    
    public double getAvailableShed() {
        double shed = 0.;
        if (participants.isEmpty()) {
            return shed;
        } 
        for (OdParticipant p : participants) {
            if(!p.isInIt() && !p.isOffline()) {
                shed += p.getShed();
            } 
        }
       // log.info(programName + " has " + participants.size() + " with " + shed + " avail");
        return shed;
    }
    
    public String getAvailableShedFmt() {
        return NumberFormats.dec2(getAvailableShed());
    }
    

    public void setAvailableShed(double availableShed) {
    }

    public double getTotalShed() {
        double shed = 0.;
        if (participants.isEmpty()) {
            return shed;
        } 

        for (OdParticipant p : participants) {
            shed += p.getShed();
        }
        return shed;
    }
    public String getTotalShedFmt() {
        return NumberFormats.dec2(getTotalShed());
    }

    public void setTotalShed(double totalShed) {
    }
    
    
    public List<OdParticipant> getOfflineList() {
        List<OdParticipant> offline = new ArrayList<OdParticipant> ();
        
        if (participants.isEmpty()) {
            return offline;
        } 
        for (OdParticipant p : participants) {
            if(p.isOffline()) {
                offline.add(p);
            }
        }
        return offline;
    }

    protected void optSomeOut() {
        Random rnd = new Random(System.currentTimeMillis());
        for (OdParticipant p : participants) {
            if(rnd.nextDouble() < .5) {
                p.setInIt(false);
            } else {
                p.setInIt(true);
            }
        }
    }
    
    public List<OdParticipant> getOptoutList() {
        List<OdParticipant> outs = new ArrayList<OdParticipant> ();
        
        if (participants.isEmpty()) {
            return outs;
        }
        for (OdParticipant p : participants) {
            if(!p.isInIt()) {
                outs.add(p);
            }
        }
        return outs;
    }

    public String getMarkerData() {
        StringBuffer sb = new StringBuffer("[");
        for (OdParticipant p : participants) {
            sb.append("[");
            sb.append(p.getLatitude());
            sb.append(",");
            sb.append(p.getLongitude());
            sb.append(",\"");
            sb.append(p.getName());
            sb.append("\"],");
        }
        sb.setLength(sb.length() - 1);
        sb.append("]");
        return sb.toString();
    }

    private String getDataStr(double[] data) {
        StringBuffer sb = new StringBuffer("[");
        for(double d : data) {
              sb.append(NumberFormats.dec2(d)).append(",");
        }
        sb.setLength(sb.length()-1);
        sb.append("]");
        return sb.toString();
    }
    
    public String getBaselineData() {
        double[] sum = new double[96];
        Arrays.fill(sum,0);
        double[] curr;
        for(OdParticipant p : participants) {
            curr=p.getBaseline();
            for(int i = 0; i <96; i++ ){
                sum[i] += curr[i];
            }
        }
        return getDataStr(sum);
    }
    
    public String getActualData() {
        double[] sum = new double[96];
        Arrays.fill(sum,0);
        double[] curr;
        for(OdParticipant p : participants) {
            curr=p.getActual();
            for(int i = 0; i <96; i++ ){
                sum[i] += curr[i];
            }
        }
        return getDataStr(sum);
    }
    
    public String getMapDiv() {
        return "<div id=\"" + DIV_MAP
                + JsUtil.asJSVar(programName)
                + "\" style=\"width: 250px; height: 250px\"><img src=\"../../images/loading.jpg\"/></div>";
    }

    public String getMapScript() {
        return "<script>akuacom.widgets.drawMap(\"" + DIV_MAP + JsUtil.asJSVar(programName) + "\"," + getCenterLatLong() + ", " + getMarkerData() + ")</script>";
    }
    
    public List<Float> getStartTimes() {
        List<Float> starts = new ArrayList<Float>(currentEvents.size());
        for(OdEvent e : currentEvents) {
            starts.add(e.getStart());
        }
        return starts;
    }
    
    public List<Float> getStopTimes() {
        List<Float> ends = new ArrayList<Float>(currentEvents.size());
        for(OdEvent e : currentEvents) {
            ends.add(e.getStop());
        }
        return ends;
    }
    
    public String getChartLegendDiv() {
        return "<div id=\"" + DIV_UC_LEGEND + JsUtil.asJSVar(programName) + "\" style=\"width: 75px; height: 75px\"/>";
    }

    public String getChartDiv() {
        return "<div id=\"" + DIV_UC + JsUtil.asJSVar(programName) + "\" style=\"width: 300px; height: 120px\"/>";
    }

    public String getLegend() {
        return "<script>akuacom.widgets.shedLegend({id:\"" + DIV_UC_LEGEND + JsUtil.asJSVar(programName) + "\",w:75,h:75})</script>";
    }

    public String getUsageChart() {
        //      shedGraph: function (id, w, h, xoff, yoff, startXs, stopXs, blData, aData, useGrid) {
        return "<script>akuacom.widgets.drawShedGraph({id:\"" + DIV_UC + JsUtil.asJSVar(programName) + "\",w:300,h:100,xoff:35,yoff:10,starts:"
            +  JsUtil.toJsArray(getStartTimes()) + ", stops:" + JsUtil.toJsArray(getStopTimes()) + ", blData:" 
            + getBaselineData() + ", aData:" 
            + getActualData() + ", useGrid:true})</script>";
    }

    public String getUsageChartIdx() {
        //      shedGraph: function (id, w, h, xoff, yoff, startXs, stopXs, blData, aData, useGrid) {
        return "<script>akuacom.widgets.drawShedGraph({id:\"" + DIV_UC + JsUtil.asJSVar(programName) + "\",w:300,h:100,xoff:35,yoff:10, prog:progs[\""+ JsUtil.asJSVar(programName) + "\"], useGrid:true})</script>";
    }

    public String getOfflineDiv() {
        String r =  "<div id=\""+ DIV_OFFLINE + JsUtil.asJSVar(programName)+ "\" style=\"font-size: small\"/>";
        return r;
    }
    
    public String getOfflineScript() {
        String r =  "<script>akuacom.data.offline(\"" + DIV_OFFLINE + JsUtil.asJSVar(programName) + "\", progs[\"" + JsUtil.asJSVar(programName) + "\"])</script>";
        return r;
    }
    
    public String getOptOutDiv() {
        String r =  "<div id=\""+ DIV_OPTOUT+JsUtil.asJSVar(programName)+ "\" style=\"font-size: small\"/>"; 
        return r;
    }
    
    public String getOptOutScript() {
        String r =  "<script>akuacom.data.optOut(\"" + DIV_OPTOUT + JsUtil.asJSVar(programName) + "\", progs[\"" + JsUtil.asJSVar(programName) + "\"])</script>";
        return r;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((programName == null) ? 0 : programName.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        OdProgram other = (OdProgram) obj;
        if (programName == null) {
            if (other.programName != null)
                return false;
        } else if (!programName.equals(other.programName))
            return false;
        return true;
    }
    
    public String getJsonLite() {
        StringBuffer sb = new StringBuffer("{");
        sb.append("name:\"").append(JsUtil.asJSVar(programName)).append("\"");
        sb.append(",totShed:").append(NumberFormats.dec2(getTotalShed()));
        sb.append(",starts:").append(JsUtil.toJsArray(getStartTimes()));
        sb.append(",stops:").append(JsUtil.toJsArray(getStopTimes()));
        sb.append(", mbrs:[");
        for(OdParticipant p : participants) {
           if(!p.isOffline()) {
               sb.append("{ name: \"").append(p.getName()).append("\"");
               sb.append(",shed:").append( NumberFormats.dec2(p.getShed()));
               sb.append(",inIt:").append( p.isInIt() ? 1 : 0);
               sb.append(",offline:").append( p.isOffline() ? 1 : 0);
               sb.append(",baseline:").append(JsUtil.toJsArray(p.getBaseline()));
               sb.append(",actual:").append(JsUtil.toJsArray(p.getActual()));
               sb.append("},");
           }
        }
        sb.setLength(sb.length()-1);
        sb.append("]}");
        //log.info(sb.toString());
        return sb.toString();
    }
    
    public String getDrawTab() {
        StringBuffer sb = new StringBuffer();
        // draw largeGraph
        sb.append(getLargeShedGaugeMethodIdx()).append("\n");
        // draw graph
        return sb.toString();
    }


}