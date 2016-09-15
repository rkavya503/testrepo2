package com.akuacom.pss2.event;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.akuacom.ejb.BaseEAO;


public interface ClientConversationStateEAO extends BaseEAO<ClientConversationState> {
    @Remote
    public interface R extends ClientConversationStateEAO {}
    @Local
    public interface L extends ClientConversationStateEAO {}

    ClientConversationState findByConversationStateId(int eventStateId);

    List<ClientConversationState> findByDrasClientId(String drasClientId);

    List<ClientConversationState> findByTimedOut();

    public List<ClientConversationState> findByPushAndTimedOut();
}
