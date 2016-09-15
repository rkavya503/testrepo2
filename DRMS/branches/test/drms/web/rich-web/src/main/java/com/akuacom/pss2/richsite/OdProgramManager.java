package com.akuacom.pss2.richsite;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import scala.Tuple2;

import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.program.participant.ProgramParticipant;
import com.akuacom.pss2.program.participant.ProgramParticipantGenEAO;
import com.akuacom.pss2.program.participant.ProgramParticipantGenEAOBean;
import com.akuacom.utils.JsUtil;

/**
 * Mockup for testing the opdash
 * 
 */
public class OdProgramManager implements Serializable {

    private static final long serialVersionUID = 1L;
    private final static Logger log = Logger.getLogger(OdProgramManager.class);
    private List<OdProgram> activePrograms;
    private String selectedProgramName;

    private List<Tuple2<Integer, Float>> tupelizer(int[][] vals) {
        List<Tuple2<Integer, Float>> tups = new ArrayList<Tuple2<Integer, Float>>();
        for (int i = 0; i < vals.length; i++) {
            tups.add(new Tuple2<Integer, Float>(vals[i][0], 0f + vals[i][1]));
        }
        return tups;
    }

    public OdProgramManager() {
        activePrograms = new ArrayList<OdProgram>();
        OdProgram p24 = new OdProgram("CPP-24 Hour Notice");
        activePrograms.add(p24);
        OdProgram p4 = new OdProgram("CPP-4 Hour Notice");

        activePrograms.add(p4);
        ProgramParticipantGenEAO eao = EJBFactory
                .getBean(ProgramParticipantGenEAOBean.class);
        List<ProgramParticipant> pps = eao.findAll();
        for (ProgramParticipant pp : pps.subList(0, 15)) {
            p24.addParticipant(new OdParticipant(pp));
        }
        p24.optSomeOut();
        p24.calcCenter();
        long today = System.currentTimeMillis();
        long tomorrow = today
                + TimeUnit.MILLISECONDS.convert(1l, TimeUnit.DAYS);
        OdEvent e24 = new OdEvent(today, 6, 8, 14, 18, 21, p24.getProgramName());
        OdEvent e24b = new OdEvent(today, 6, 10, 18, 21.5f, 23.9f,
                p24.getProgramName());
        OdEvent e24p = new OdEvent(tomorrow, 6, 16, 18, 21.5f, 22,
                p24.getProgramName());
        p24.getCurrentEvents().add(e24);
        p24.getCurrentEvents().add(e24b);
        p24.getPendingEvents().add(e24p);
        for (ProgramParticipant pp : pps.subList(16, 30)) {
            p4.addParticipant(new OdParticipant(pp));
        }
        p4.optSomeOut();
        p4.calcCenter();
        OdEvent e4 = new OdEvent(today, 6, 10, 12, 16, 21, p4.getProgramName());
        OdEvent e4p = new OdEvent(tomorrow, 6, 8, 14, 18, 21,
                p24.getProgramName());
        p4.getCurrentEvents().add(e4);
        p4.getPendingEvents().add(e4p);

    }

    static int gapCtr = 0;

    public List<OdProgram> getActivePrograms() {
        // log.info("returning active progs " + ++gapCtr);
        return activePrograms;
    }

    public String getActiveProgramsJson() {
        log.info("returning active progs str");
        StringBuffer sb = new StringBuffer(
                "<script>var progSGuageIdx = 0, progLGaugeIdx =0, progChartIdx =0, progMapIdx = 0\nvar progs = {");
        for (OdProgram p : activePrograms) {
            sb.append(JsUtil.asJSVar(p.getProgramName())).append(":");
            sb.append(p.getJsonLite()).append(",");
        }
        sb.setLength(sb.length() - 1);
        sb.append("}\n");

        sb.append("</script>");

        return sb.toString();
    }

    public void setActivePrograms(List<OdProgram> activePrograms) {
        this.activePrograms = activePrograms;
    }

    public int getRowCount() {
        return activePrograms.size();
    }

    public void setRowCount(int rowCount) {
    }

    public String getSelectedProgramName() {
        return selectedProgramName;
    }

    public void setSelectedProgramName(String selectedProgramName) {
        this.selectedProgramName = selectedProgramName;
    }

    public void callEvent() {
        log.info("calling event " + selectedProgramName);
    }

}
