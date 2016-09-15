package com.akuacom.pss2.web.event.dbp;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.actions.DispatchAction;
import org.apache.struts.action.*;

import com.akuacom.pss2.program.bidding.BiddingProgramManager;
import com.akuacom.pss2.program.dbp.BidEntry;
import com.akuacom.pss2.web.util.EJBFactory;
import com.akuacom.pss2.core.ValidationException;
import com.akuacom.pss2.core.ErrorUtil;

public class EditBidAction extends DispatchAction {

    @Override
    protected ActionForward unspecified(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        return view(actionMapping, actionForm, httpServletRequest, httpServletResponse);
    }

    public ActionForward view(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        EditBidForm form = (EditBidForm) actionForm;
        final String programName = form.getProgramName();
        final String eventName = form.getEventName();
        final String user = form.getParticipantName();
        BiddingProgramManager bidding = EJBFactory.getBiddingProgramManager();

        final List<BidEntry> bids = bidding.getCurrentBid(programName, eventName, user, false);
        Collections.sort(bids);
        boolean readonly = false;
        for(BidEntry be : bids)
        {
            Date bidStart = be.getBlockStart();
            if(bidStart.getTime() <= (new Date()).getTime())
            {
                readonly = true;
                break;
            }
        }

        if (readonly) {
            httpServletRequest.setAttribute("readonly", "true");
        }
        else
        {
            httpServletRequest.setAttribute("readonly", "false");
        }

        httpServletRequest.setAttribute("bids", bids);

        return actionMapping.findForward("success");
    }

    public ActionForward save(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        EditBidForm form = (EditBidForm) actionForm;
        final String programName = form.getProgramName();
        final String eventName = form.getEventName();
        final String user = form.getParticipantName();
        BiddingProgramManager bidding = EJBFactory.getBiddingProgramManager();

        final List<BidEntry> bids = bidding.getCurrentBid(programName, eventName, user, false);
        Collections.sort(bids);
        final Map<String,String> map = form.getBidMap();
        final Set<String> keySet = map.keySet();

        try
        {
            for (String key : keySet) {
                final String value = map.get(key);
                int i = Integer.parseInt(key.substring(1));
                double d = Double.parseDouble(value);
                bids.get(i).setReductionKW(d);
            }
            bidding.setCurrentBid(programName, eventName, user, false, bids, true);
        }
        catch (Exception e)
		{
            ValidationException ve = ErrorUtil.getValidationException(e);
            ActionErrors errors = new ActionErrors();
            ActionMessage error;
            if (ve != null) {
                error = new ActionMessage(ve.getLocalizedMessage(), false);
            } else {
                String errMsg = ErrorUtil.getErrorMessage(e);
                if(e instanceof NumberFormatException)
                {
                    errMsg = "Input format error: " +  errMsg;   
                }
                error = new ActionMessage(errMsg, false);
            }
            errors.add("bidValidation", error);
            addErrors(httpServletRequest, errors);
            return view(actionMapping, actionForm, httpServletRequest, httpServletResponse);
        }
        ActionForward modifiableActionForward = new ActionForward(actionMapping.findForward("events").getPath() + "?programName=" + programName + "&dispatch=view");
        return modifiableActionForward;
    }
}
