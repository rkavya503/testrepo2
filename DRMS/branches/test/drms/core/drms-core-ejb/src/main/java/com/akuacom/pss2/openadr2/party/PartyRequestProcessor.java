package com.akuacom.pss2.openadr2.party;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.honeywell.dras.vtn.api.common.FingerPrint;
import com.honeywell.dras.vtn.api.common.Response;
import com.honeywell.dras.vtn.api.registration.CancelPartyRegistration;
import com.honeywell.dras.vtn.api.registration.CanceledPartyRegistration;
import com.honeywell.dras.vtn.api.registration.CreatePartyRegistration;
import com.honeywell.dras.vtn.api.registration.CreatedPartyRegistration;
import com.honeywell.dras.vtn.api.registration.QueryRegistration;
import com.honeywell.dras.vtn.api.registration.Result;
import com.honeywell.dras.vtn.dras.service.VtnDrasServiceException;

public interface PartyRequestProcessor {
	@Remote
    public interface R extends PartyRequestProcessor {}
    @Local
    public interface L extends PartyRequestProcessor {}

	public boolean isVenValid(String venId, String certCommonName);

	public CreatedPartyRegistration queryRegistration(
			QueryRegistration queryRegistration) throws VtnDrasServiceException;

	public CreatedPartyRegistration createPartyRegistration(
			CreatePartyRegistration createPartyRegistration)
			throws VtnDrasServiceException;

	public CanceledPartyRegistration cancelPartyRegistration(
			CancelPartyRegistration cancelPartyRegistration)
			throws VtnDrasServiceException;

	public Response canceledPartyRegistration(
			CanceledPartyRegistration canceledPartyRegistration)
			throws VtnDrasServiceException;

	public Result registerFingerPrint(FingerPrint fingerPrint)
			throws VtnDrasServiceException;

}