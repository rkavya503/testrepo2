package com.akuacom.pss2.event.participant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;

import com.akuacom.pss2.event.Event;
import com.akuacom.pss2.participant.Participant;

@Stateless
public class EventParticipantEAOBean extends EventParticipantGenEAOBean
        implements EventParticipantEAO.R, EventParticipantEAO.L {

    public EventParticipantEAOBean() {
        super(EventParticipant.class);
    }

    public List<String> findPartNamesByEvent(java.lang.String eventName, boolean isClient) {
        Query q = em.createNamedQuery( "EventParticipant.findPartNameByEvent" );
        q.setParameter("eventName", eventName);
        q.setParameter("client", isClient);
        return q.getResultList();
    }

    
    public EventParticipant getEventParticipant(String eventName,
            String participantName, boolean isClient) {
        final List<EventParticipant> resultList = findByKeys(eventName,participantName,isClient);
        EventParticipant dao = null;
        if (resultList.size() == 1) {
            dao = resultList.get(0);
//            dao.getBidEntries().size();
//            dao.getSignalEntries().size();
        }
        return dao;
    }

    public EventParticipant getEventParticipant(Event event,
            String participantName, boolean client) {
        final Set<EventParticipant> eventParticipantSet = event
                .getEventParticipants();
        for (EventParticipant ep : eventParticipantSet) {
            final Participant p = ep.getParticipant();
            if (p.getParticipantName().equals(participantName)
                    && p.isClient() == client) {
                return ep;
            }
        }
        return null;
    }

    @Override
    public void addChild(EventParticipant parent, EventParticipant child) {
        checkProgram(parent, child);
        checkRecursiveAdd(parent, child);

        EntityManager entityManager = getEm();
        // update child's parent and ancestry fields
        String oldAncestry = child.getAncestry();
        String parentAncestry = parent.getAncestry();
        if (parentAncestry == null) {
            parentAncestry = "";
        }
        child.setAncestry(parentAncestry + parent.getUUID());
        child.setParent(parent);
        entityManager.merge(child);
        // update child's all descendants' ancestry fields
        Set<EventParticipant> set = getFlatDescendants(child);
        for (EventParticipant pp : set) {
            pp.setAncestry(pp.getAncestry().replaceFirst(oldAncestry, child.getAncestry()));
            entityManager.merge(pp);
        }
    }

    private void checkRecursiveAdd(EventParticipant parent, EventParticipant child) {
        String ancestry = parent.getAncestry();
        if (ancestry != null && ancestry.contains(child.getUUID())) {
            throw new EJBException("can't added " + child.getUUID() + " to its child " + parent.getUUID());
        }
    }

    private void checkProgram(EventParticipant parent, EventParticipant child) {
        Event parentProgram = parent.getEvent();
        String parentProgramProgramName = parentProgram.getProgramName();

        Event childProgram = child.getEvent();
        String childProgramProgramName = childProgram.getProgramName();

        if (!parentProgramProgramName.equals(childProgramProgramName)) {
            throw new EJBException("can't operate aggregation across different program. " +
                    "parent program name: " + parentProgramProgramName +
                    ", child program name: " + childProgramProgramName);
        }
    }

    
    
    
    @Override
    public Set<EventParticipant> getFlatDescendants(EventParticipant parent) {
        List<EventParticipant> list = findAllAggregationDescendants(parent.getEvent().getEventName(),
                parent.getAncestry() == null ? parent.getUUID() : parent.getAncestry() + parent.getUUID());
        HashSet<EventParticipant> set = new HashSet<EventParticipant>();
        for (EventParticipant pp : list) {
            set.add(pp);
        }
        return set;
    }


    @Override
    public EventParticipant findParentEventParticipant(String eventName, String parentName) {
        Query q = em.createNamedQuery( "EventParticipant.findParentEventParticipant.Single" );
        q.setParameter("eventName", eventName);
        q.setParameter("parentName", parentName);
        List<EventParticipant> val = q.getResultList();
        if(val.isEmpty()) {
            return null;
        } else if (val.size() == 1) {
            return val.get(0);
        } else if (val.size() == 2) {
            // DRMS-6371: the old logic can't handle legacy data where client has the same
            // with participant. here is the hack that deals with it.
            // if there are entities, and one is client and one is part, return part.
            // otherwise, throw an exception.
            if (val.get(0).getParticipant().isClient() && !val.get(1).getParticipant().isClient()) {
                return val.get(1);
            } else if (val.get(1).getParticipant().isClient() && !val.get(0).getParticipant().isClient()) {
                return val.get(0);
            }
            throw new NonUniqueResultException(q.toString());
        } else {
            throw new NonUniqueResultException(q.toString());
        }
    }

    @SuppressWarnings({"unchecked"})
    public List<EventParticipant> findAllAggregationDescendants(java.lang.String programName, Object ancestor) {
        Query q = em.createNamedQuery( "EventParticipant.findAllAggregationDescendants.list" );
        q.setParameter("eventName", programName);
        q.setParameter("ancestor", ancestor);
        return q.getResultList();
    }

    @Override
    public void removeChildren(EventParticipant parent, Set<EventParticipant> children) {
        String programName = parent.getEvent().getEventName();
        String parentAncestry = parent.getAncestry() + parent.getUUID();
        for (EventParticipant child : children) {
            if (!programName.equals(child.getEvent().getEventName())) {
                throw new EJBException("child has different program name than parent");
                // todo add detail info
            }
            if (!isAncestor(parent, child)) {
                throw new EJBException("child is not direct descent of parent");
            }
        }

        for (EventParticipant child : children) {
            removeParent(child);
        }
    }

    @Override
    public void removeParent(EventParticipant child) {
        EntityManager entityManager = getEm();
        child.setParent(null);
        child.setAncestry(null);
        entityManager.merge(child);
    }

    @Override
    public Set<EventParticipant> getDescendants(EventParticipant parent) {
        List<EventParticipant> descendants = findAllAggregationDescendants(parent.getEvent().getEventName(),
                parent.getAncestry() == null ? parent.getUUID() : parent.getAncestry() + parent.getUUID());
        HashSet<EventParticipant> set = new HashSet<EventParticipant>();
        for (EventParticipant child : descendants) {
            if (child.getAncestry().endsWith(parent.getUUID())) {
                set.add(child);
            }
        }
        return set;
    }

    @Override
    public EventParticipant getAncestor(EventParticipant child) {
        String ancestry = child.getAncestry();
        if (ancestry == null) {
            return null;
        } else {
            String uuid = ancestry.substring(ancestry.length() - 32);
            return getEm().find(EventParticipant.class, uuid);
        }
    }

    @Override
    public EventParticipant getRoot(EventParticipant child) {
        String ancestry = child.getAncestry();
        if (ancestry == null) {
            return null;
        } else {
            String uuid = ancestry.substring(0, 32);
            return getEm().find(EventParticipant.class, uuid);
        }
    }

    @Override
    public EventParticipant getCommonAncestor(EventParticipant pp1, EventParticipant pp2) {
        String a1 = pp1.getAncestry();
        String a2 = pp2.getAncestry();

        if (a1 == null || a2 == null) {
            return null;
        } else {
            int minLength = Math.min(a1.length(), a2.length());
            int limit = minLength / 32;
            String common = null;
            for (int i = 0; i < limit; i++) {
                String s1 = a1.substring(i * 32, (i + 1) * 32);
                String s2 = a2.substring(i * 32, (i + 1) * 32);
                if (s1.equals(s2)) {
                    common = s1;
                } else {
                    break;
                }
            }
            if (common != null) {
                return getEm().find(EventParticipant.class, common);
            } else {
                return null;
            }
        }
    }

    @Override
    public boolean isDescendant(EventParticipant parent, EventParticipant child) {
        String ancestry = child.getAncestry();
        return ancestry != null && ancestry.contains(parent.getUUID());
    }

    @Override
    public boolean isAncestor(EventParticipant parent, EventParticipant child) {
        return isDescendant(parent, child);
    }

	@Override
	public List<EventParticipant> findEventParticipantWithSignalsByAllClientUUID(
			List<String> clinets) {		
		Query q = em.createNamedQuery( "EventParticipant.findEventParticipantWithSignalsByAllEventClientUUID" );
		q.setParameter("uuid", clinets);
		return q.getResultList();
	}
	
	@Override
	public List<EventParticipant> findEventParticipantWithSignalsByAllClientUUIDforEvent(
			List<String> clinets, String eventId) {		
		Query q = em.createNamedQuery( "EventParticipant.findEventParticipantWithSignalsByAllEventClientUUIDForEvent" );
		q.setParameter("uuid", clinets);
		q.setParameter("event_uuid", eventId);
		return q.getResultList();
	}
	
	@Override
	public List<EventParticipant> findEventWithoutOptOut(String eventName){
		Query q = em.createNamedQuery( "EventParticipant.findEventWithoutOptOut" );
		q.setParameter("eventName", eventName);
		return q.getResultList();
	}

	@Override
	public List<EventParticipant> findEventParticipantWithSignalsByAllEventIds(List<String> event_uuid) {
		Query q = em.createNamedQuery( "EventParticipant.findEventParticipantWithSignalsByAllEventIds" );
		q.setParameter("event_uuid", event_uuid);
		return q.getResultList();
	}
/*
    @Override
    public void deleteEventParticipant(EventParticipant pp) {
        Set<EventParticipant> set = getFlatDescendants(pp);
        if (set.size() > 0) {
            for (EventParticipant next : set) {
                String ancestry = next.getAncestry();
                // if direct descendant, set to null
                if (next.getParent().getUUID().equals(pp.getUUID())) {
                    next.setAncestry(null);
                    next.setParent(null);
                } else { // if not, just set ancestry
                    String replace = ancestry.replace(pp.getAncestry() + pp.getUUID(), "");
                    next.setAncestry(replace);
                }
                getEm().merge(next);
            }
        }
        super.delete(pp);
    }
*/

	@Override
	public List<EventParticipant> findEventParticipantWithSignalsByAllClientUUIDAndEventUUID(
			List<String> clinets, String eventName) {
		Query q = em.createNamedQuery( "EventParticipant.findEventParticipantWithSignalsByAllEventClientUUIDAndEventName" );
		q.setParameter("uuid", clinets);
		q.setParameter("eventName", eventName);
		return q.getResultList();
	}
	@Override
	public Map<String,List<EventParticipant>> findEventParticipantWithSignalsByAllEventNameAndClientUUIDForEvent(List<String> participantName, Set<String> eventNames) {
		Query q = em.createNamedQuery( "EventParticipant.findEventParticipantWithSignalsByAllEventNameAndClientUUIDForEvent");
		q.setParameter("participantName", participantName);
		q.setParameter("eventName", eventNames);
		Map<String,List<EventParticipant>> eventIdEventParticipantMap = new HashMap<String,List<EventParticipant>>();
		List<EventParticipant> evPartList = q.getResultList();
		for(EventParticipant evPart : evPartList){
			List<EventParticipant> partList = new ArrayList<EventParticipant>();
			String eventName = evPart.getEvent().getEventName();
			if(eventIdEventParticipantMap.containsKey(eventName)){
				partList = eventIdEventParticipantMap.get(eventName);
				partList.add(evPart);
				eventIdEventParticipantMap.put(eventName, partList);
			}else{
				partList.add(evPart);
				eventIdEventParticipantMap.put(eventName,partList);
			}
		}
		return eventIdEventParticipantMap;
	}
	@Override
	public Map<String,List<Participant>> findEventParticipantWithParticipantByAccont(Set<String> accounts) {
		Query q = em.createNamedQuery( "EventParticipant.findEventParticipantWithParticipantByAccont");
		q.setParameter("accounts", accounts);
		Map<String,List<Participant>> eventIdParticipantMap = new HashMap<String, List<Participant>>();
		List<EventParticipant> evPartList = q.getResultList();
		for(EventParticipant evPart : evPartList){
			List<Participant> partList = new ArrayList<Participant>();
			String eventName = evPart.getEvent().getEventName();
			if(eventIdParticipantMap.containsKey(eventName)){
				partList = eventIdParticipantMap.get(eventName);
				partList.add(evPart.getParticipant());
				eventIdParticipantMap.put(eventName, partList);
			}else{
				partList.add(evPart.getParticipant());
				eventIdParticipantMap.put(eventName,partList);
			}
		}
		return eventIdParticipantMap;
	}
	@Override
	public List<EventParticipant> findEventParticipantForParent(String parentName){
		 Query q = em.createNamedQuery( "EventParticipant.findEventParticipantForParent" );
	        q.setParameter("parentName", parentName);
	        List<EventParticipant> val = q.getResultList();
	        return val;
	}
}
