package com.akuacom.pss2.participant.contact;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.akuacom.ejb.BaseEAO;


public interface ParticipantContactEAO extends BaseEAO<ParticipantContact>{
    @Remote
    public interface R extends ParticipantContactEAO {}
    @Local
    public interface L extends ParticipantContactEAO {}

    /**
     * get the message threshold for the contact specified by the contact id
     * 
     * @param contactUuid
     *            contact uuid
     * @return return threshold for specified contact, or -1 if no such contact
     *         exists
     */
    int getMsgThreshold(String contactUuid);
    
    ParticipantContact updateParticipant(ParticipantContact participantContact);
    
    void persistParticipantContact(ParticipantContact participantContact, String uuid, List<String> participant_uuids);
    
    void removeParticipantContact(String address, String desc, List<String> participant_uuids);
    
    void updateParticipantContact(ParticipantContact participantContact, String orig_type, String orig_address, String orig_desc, List<String> participant_uuids);
    
    int countParticipantContact(String address, String desc, String participant_uuid);
    
    List<ParticipantContact> getParticipantContacts(String uuid);
    void removeParticipantContact(List<String> participant_uuids);
    void persistParticipantContacts(List<ParticipantContact> pcs, List<String> participant_uuids);
}
