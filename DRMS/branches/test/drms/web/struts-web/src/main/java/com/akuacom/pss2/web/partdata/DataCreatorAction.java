package com.akuacom.pss2.web.partdata;

import java.io.CharArrayReader;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.akuacom.pss2.data.DataGeneratorManager;

public class DataCreatorAction extends DispatchAction {
    private static final Logger log = Logger.getLogger(DataCreatorAction.class);

    @Override
    protected ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        DataCreatorForm f = (DataCreatorForm) form;
        if (f.getProps() == null) {
            f.setProps("participant=01usage_demo\n" +
                    "baseValue=2.0\n" +
                    "peakIncrementMin=18.0\n" +
                    "peakIncrementMax=24.0\n" +
                    "peakReduction=0\n" +
                    "reductionStartHour=0\n" +
                    "reductionEndHour=0\n" +
                    "startDate=2010-09-08\n" +
                    "endDate=2010-09-08\n" +
                    "intervalInMillis=900000");
        }
        return mapping.findForward("success");
    }

    public ActionForward generate(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        DataCreatorForm f = (DataCreatorForm) form;
        log.debug(f.getProps());
        final Properties props = new Properties();
        props.load(new CharArrayReader(f.getProps().toCharArray()));
        DataGeneratorManager.process(props);
        return mapping.findForward("success");
    }

}
