package com.akuacom.pss2.program.dlc;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.program.dlc.customers.Customer;
import com.akuacom.pss2.program.dlc.customers.Customers;
import com.akuacom.pss2.program.participant.ProgramParticipant;

public interface DlcManager {
    @Local
    public interface L extends DlcManager {}
    @Remote
    public interface R extends DlcManager {}
    
	boolean createParticipant(Participant part, char[] partpwd,
			Participant client, char[] clientpwd);

	void removeParticipants(List<String> serviceIds);

	boolean updateParticipant(Participant part, char[] partpwd,
			Participant client, char[] clientpwd, Participant updated);

	List<Customer> findParticipant(List<String> serviceIds);

    Customer findParticipant(String serviceId);

	void updateParticipant(Customers customers);

	void createParticipant(Customers customers);

	void removeParticipant(String serviceId);
	
	ProgramParticipant updateProgramParticipant(ProgramParticipant value);
	
	ProgramParticipant findRtpStrategyByProgAndPartiForClient(String programName, String participantName, boolean client);
}