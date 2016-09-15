/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.program.dbp.DBPDataAccessBean.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.program.dbp;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;

import com.akuacom.pss2.client.ClientEAO;
import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.event.Event;
import com.akuacom.pss2.event.participant.EventParticipant;
import com.akuacom.pss2.event.participant.EventParticipantEAO;
import com.akuacom.pss2.event.participant.EventParticipantGenEAO;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.participant.bid.ParticipantBidState;
import com.akuacom.pss2.program.participant.ProgramParticipant;
import com.akuacom.pss2.program.participant.ProgramParticipantBidEntry;
import com.akuacom.pss2.program.participant.ProgramParticipantBidLevelMap;
import com.akuacom.pss2.program.participant.ProgramParticipantBidLevelMapGenEAO;
import com.akuacom.pss2.program.participant.ProgramParticipantEAO;
import com.akuacom.pss2.program.participant.ProgramParticipantManager;
import com.akuacom.pss2.report.ReportManager;

/**
 * The Class DBPDataAccessBean.
 */
@Stateless
@SuppressWarnings({ "unchecked" })
public class DBPDataAccessBean implements
		DBPDataAccess.R, DBPDataAccess.L {

	/** The Constant log. */
	private static final Logger log = Logger.getLogger(DBPDataAccessBean.class);

	@EJB
	private ProgramParticipantEAO.L ppEAO;
	
	@EJB
	private DBPEventGenEAO.L dbpEventEAO;
	
	@EJB(beanName="EventParticipantGenEAOBean")
	private EventParticipantGenEAO.L eventParticipantEAO;

    @EJB
    private EventParticipantEAO.L epEAO;
	
	@EJB(beanName="ProgramParticipantBidLevelMapGenEAOBean")
	private ProgramParticipantBidLevelMapGenEAO.L ppbidLevelMapEAO;

    @EJB
    protected ClientEAO.L clientEAO;

    @EJB
    protected ReportManager.L report;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.akuacom.pss2.program.dbp.DBPDataAccess#updateEventState(java.lang
	 * .String, java.lang.String, com.akuacom.pss2.core.model.BidState)
	 */
	public DBPEvent updateEventState(String programName, String eventName,
			BidState bidState) {

		checkProgramName(programName);
		checkEventName(eventName);

        try {
            DBPEvent dao = dbpEventEAO.findByEventName(eventName);
            dao.setCurrentBidState(bidState);
            return dbpEventEAO.merge(dao);
        } catch (Exception ex) {
            String message = "error updating event bid state for event " +
                    eventName + " in program " + programName;
            //DRMS-1664
            log.debug(message, ex);
            throw new EJBException(message, ex);
        }
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.akuacom.pss2.program.dbp.DBPDataAccess#getDefaultBid(java.lang.String
	 * , java.lang.String)
	 */
	public List<BidEntry> getDefaultBid(String programName,
			String participantName, boolean isClient) {
		checkProgramName(programName);
		checkParticipantName(participantName);

		try {

			ProgramParticipant pp = ppEAO
					.findProgramParticipantsByProgramNameAndPartName(
							programName, participantName);
			Set<ProgramParticipantBidEntry> bidEntries = pp.getBidEntries();
			if (bidEntries == null || bidEntries.size() == 0) {
				return null;
			} else {
				ArrayList<BidEntry> retList = new ArrayList<BidEntry>();
				for (ProgramParticipantBidEntry impl : bidEntries) {
					BidEntry bidEntry = new BidEntry();
					bidEntry.setActive(impl.isActive());
					bidEntry.setBlockEnd(impl.getBlockEnd());
					bidEntry.setBlockStart(impl.getBlockStart());
					bidEntry.setPriceLevel(impl.getPriceLevel());
					bidEntry.setReductionKW(impl.getReductionKW());
					bidEntry.setProgramName(programName);
					bidEntry.setParticipantName(participantName);
					retList.add(bidEntry);
				}
				return retList;
			}

		} catch (Exception ex) {
			String message = "error getting default bids for participant "
					+ participantName + " in program " + programName;
			log.error(message, ex);
			throw new EJBException(message, ex);
		}
	}

    public Set<EventParticipantBidEntry> setCurrentBid(String programName, Event event,
            EventParticipant eventParticipant, List<BidEntry> bids)
    {
        try {
            Set<EventParticipantBidEntry> bidEntries = eventParticipant.getBidEntries();
            // create new, only when bid entries return null
            if (bidEntries == null) {
                bidEntries = new HashSet<EventParticipantBidEntry>();
                eventParticipant.setBidEntries(bidEntries);
            }

            if (bidEntries.size() == 0) {
                for (BidEntry entry : bids) {
                    EventParticipantBidEntry entryDao = new EventParticipantBidEntry();
                    entryDao.setActive(entry.isActive());
                    entryDao.setEndTime(entry.getBlockEnd());
                    entryDao.setEventParticipant(eventParticipant);
                    entryDao.setPriceLevel(entry.getPriceLevel());
                    entryDao.setReductionKW(entry.getReductionKW());
                    entryDao.setStartTime(entry.getBlockStart());
                    bidEntries.add(entryDao);
                }
            } else {
                for (BidEntry entry : bids) {
                    for (EventParticipantBidEntry entity : bidEntries) {
                        Date time = entity.getStartTime();
                        Date blockStart = entry.getBlockStart();
                        if (time.equals(blockStart)) {
                            entity.setActive(entry.isActive());
                            entity.setPriceLevel(entry.getPriceLevel());
                            entity.setReductionKW(entry.getReductionKW());
                        }
                    }
                }
            }

            if (event.getUUID() != null) {
                eventParticipantEAO.update(eventParticipant);
            }
            return bidEntries;
        } catch (Exception ex) {
            String message = "error setting bids for participant "
                    + eventParticipant.getParticipant().getParticipantName() + " for event "
                    + event.getEventName() + " in program " + programName;
            log.error(message, ex);
            throw new EJBException(message, ex);
        }

    }

	public void setDefaultBid(String programName, String participantName,
			boolean isClient, List<BidEntry> bids) {
		checkProgramName(programName);
		checkParticipantName(participantName);
		if (bids == null) {
			String message = "bids list is null";
			// DRMS-1664
			log.warn(message);
			throw new EJBException(message);
		}

		try {
			ProgramParticipantManager ppManager = EJBFactory
					.getBean(ProgramParticipantManager.class);
			ProgramParticipant pp = ppManager.getProgramParticipant(
					programName, participantName, isClient);
			Set<ProgramParticipantBidEntry> bidEntries = pp.getBidEntries();
			if (bidEntries != null && bidEntries.size() > 0) {
				pp.getBidEntries().clear();

				ppEAO.updateProgramParticipant(pp);
			}
			bidEntries = new HashSet<ProgramParticipantBidEntry>();
			for (BidEntry entry : bids) {
				ProgramParticipantBidEntry be = new ProgramParticipantBidEntry();
				be.setPriceLevel(entry.getPriceLevel());
				be.setProgramParticipant(pp);
				be.setReductionKW(entry.getReductionKW());
				be.setBlockStart(entry.getBlockStart());
				be.setBlockEnd(entry.getBlockEnd());
				be.setActive(entry.isActive());
				bidEntries.add(be);
			}
			pp.setBidEntries(bidEntries);
			ppEAO.updateProgramParticipant(pp);

		} catch (Exception e) {
			String message = "error setting default bids for participant "
					+ participantName + " in program " + programName;
			log.error(message, e);
			throw new EJBException(message, e);
		}
	}

	// This retrieves the 'current' bids for the event for a participant
	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.akuacom.pss2.program.dbp.DBPDataAccess#getCurrentBid(java.lang.String
	 * , java.lang.String, java.lang.String)
	 */
	public List<BidEntry> getCurrentBid(String programName, Event event,
			Participant participant, boolean isClient) {
		checkProgramName(programName);

		try {
            EventParticipant eventParticipant = null;
            final String participantName = participant.getParticipantName();
            for (EventParticipant ep : epEAO.findByEvent(event.getEventName())) {
                if (ep != null && participantName.equals(ep.getParticipant().getParticipantName())
                        && isClient == ep.getParticipant().isClient()) {
                    eventParticipant = ep;
                    break;
                }
            }

            if (eventParticipant == null) {
                String message = "participant " + participantName
                        + " for event " + event
                        + " in program " + programName
                        + " doesn't exist";
                //DRMS-1664
                log.warn(message);
                throw new EJBException(message);
            }

            final ArrayList<BidEntry> results = new ArrayList<BidEntry>();
            for (EventParticipantBidEntry entryDao : eventParticipant.getBidEntries()) {
                final BidEntry entry = new BidEntry();
                entry.setActive(entryDao.isActive());
                entry.setBlockEnd(entryDao.getEndTime());
                entry.setBlockStart(entryDao.getStartTime());
                entry.setId(entryDao.getUUID());
                entry.setPriceLevel(entryDao.getPriceLevel());
                entry.setReductionKW(entryDao.getReductionKW());
                entry.setParticipantName(participantName);
                entry.setProgramName(programName);
                results.add(entry);
            }
			return results;
		} catch (Exception ex) {
			String message = "error getting bids for participant "
					+ participant + " for event " + event
					+ " in program " + programName;
			// DRMS-1664
			log.warn(message, ex);
			throw new EJBException(message, ex);
		}

	}

	private EventParticipant getEventParticipant(Event event,
			String participantName) {
		final Set<EventParticipant> set = event.getEventParticipants();
		for (EventParticipant ep : set) {
            if (participantName.equals(ep.getParticipant().getParticipantName())) {
                return ep;
            }
        }
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.akuacom.pss2.program.dbp.DBPDataAccess#isBidAcknowledged(java.lang
	 * .String, java.lang.String, java.lang.String)
	 */
    public boolean isBidAcknowledged(String programName, Event event,
			String participantName, boolean isClient) {

		checkProgramName(programName);
        checkParticipantName(participantName);

		try {
            EventParticipant eventParticipant = getEventParticipant(
					event, participantName);
			if (eventParticipant == null) {
				String message = "participant " + participantName
                        + " for event " + event.getEventName() + " in program " +
                        programName + " doesn't exist";
				// DRMS-1664
				log.warn(message);
				throw new EJBException(message);
			}
            final ParticipantBidState bidState = eventParticipant.getBidState();
			return bidState.ordinal() > ParticipantBidState.Pending.ordinal();
		} catch (Exception ex) {
			String message = "error getting bid acknowldegment for participant "
                    + participantName + " for event " + event.getEventName() +
                    " in program "  + programName;
			log.error(message, ex);
			throw new EJBException(message, ex);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.akuacom.pss2.program.dbp.DBPDataAccess#setBidAcknowledged(java.lang
	 * .String, java.lang.String, java.lang.String, boolean)
	 */
	public void setBidAcknowledged(String programName, Event event,
                                   Participant participant, boolean isClient, boolean acknowledged, EventParticipant eventParticipant) {

		checkProgramName(programName);

		try {
            if (eventParticipant == null) {
				String message = "participant " + participant + " for event "
						+ event + " in program " + programName
						+ " doesn't exist";
				// DRMS-1664
				log.warn(message);
				throw new EJBException(message);
			}
			if (acknowledged) {
				eventParticipant.setBidState(ParticipantBidState.Acknowledged);
			} else {
				eventParticipant.setBidState(ParticipantBidState.Pending);
			}
            if (event.getUUID() != null) {
                eventParticipantEAO.update(eventParticipant);
            }
		} catch (Exception ex) {
			String message = "error setting bid acknowledgement for participant "
                    + participant.getParticipantName() + " for event " + event.getEventName() +
                    " in program " + programName;
			log.error(message, ex);
			throw new EJBException(message, ex);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.akuacom.pss2.program.dbp.DBPDataAccess#isBidAccepted(java.lang.String
	 * , java.lang.String, java.lang.String)
	 */
    public boolean isBidAccepted(String programName, Event event,
			String participantName, boolean isClient) {

		checkProgramName(programName);
        checkParticipantName(participantName);

		try {
            EventParticipant dao = getEventParticipant(
            	event, participantName);
            if (dao == null) {
				String message = "participant " + participantName
                        + " for event " + event.getEventName() + " in program " +
                        programName + " doesn't exist";
				// DRMS-1664
				log.warn(message);
				throw new EJBException(message);
			}
            final ParticipantBidState bidState = dao.getBidState();
			return bidState == ParticipantBidState.Accepted;
		} catch (Exception ex) {
			String message = "error getting bid acknowldegment for participant "
                    + participantName + " for event " + event.getEventName() +
                    " in program " + programName;
			log.error(message, ex);
			throw new EJBException(message, ex);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.akuacom.pss2.program.dbp.DBPDataAccess#setBidAccepted(java.lang.String
	 * , java.lang.String, java.lang.String, boolean)
	 */
	public void setBidAccepted(String programName, Event event,
                               Participant participant, boolean isClient, boolean accepted, EventParticipant eventParticipant) {

		checkProgramName(programName);

		try {
            if (eventParticipant == null) {
				String message = "participant " + participant
						+ " for event " + event + " in program "
						+ programName + " doesn't exist";
				// DRMS-1664
				log.warn(message);
				throw new EJBException(message);
			}
			if (accepted) {
				eventParticipant.setBidState(ParticipantBidState.Accepted);
			} else {
				// don't do anything for now. keep the old state
			}
            if (event.getUUID() != null) {
                eventParticipantEAO.update(eventParticipant);
            }
		} catch (Exception ex) {
			String message = "error setting bid acknowledgement for participant "
					+ participant
					+ " for event "
					+ event
					+ " in program " + programName;
			log.error(message, ex);
			throw new EJBException(message, ex);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.akuacom.pss2.program.dbp.DBPDataAccess#isBidDeclined(java.lang.String
	 * , java.lang.String, java.lang.String)
	 */
	public boolean isBidDeclined(String programName, String eventName,
			String participantName, boolean isClient) {

		checkProgramName(programName);
		checkEventName(eventName);
        checkParticipantName(participantName);

		try {
			EventParticipant dao = epEAO.getEventParticipant(
					eventName, participantName, isClient);
			if (dao == null) {
				String message = "participant " + participantName
						+ " for event " + eventName + " in program "
						+ programName + " doesn't exist";
				// DRMS-1664
				log.warn(message);
				throw new EJBException(message);
			}
			final ParticipantBidState bidState = dao.getBidState();
			return bidState == ParticipantBidState.Rejected;
		} catch (Exception ex) {
			String message = "error getting bid acknowldegment for participant "
					+ participantName
					+ " for event "
					+ eventName
					+ " in program " + programName;
			log.error(message, ex);
			throw new EJBException(message, ex);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.akuacom.pss2.program.dbp.DBPDataAccess#setBidDeclined(java.lang.String
	 * , java.lang.String, java.lang.String, boolean)
	 */
	public void setBidDeclined(String programName, String eventName,
                               String participantName, boolean declined, EventParticipant eventParticipant) {
		checkProgramName(programName);
		checkEventName(eventName);
		checkParticipantName(participantName);

		try {
            if (eventParticipant == null) {
				String message = "participant " + participantName
						+ " for event " + eventName + " in program "
						+ programName + " doesn't exist";
				// DRMS-1664
				log.warn(message);
				throw new EJBException(message);
			}
			if (declined) {
				eventParticipant.setBidState(ParticipantBidState.Rejected);
			} else {
				// don't do anything for now. keep the old state
			}
            eventParticipantEAO.update(eventParticipant);
		} catch (Exception ex) {
			String message = "error setting bid acknowledgement for participant "
					+ participantName
					+ " for event "
					+ eventName
					+ " in program " + programName;
			log.error(message, ex);
			throw new EJBException(message, ex);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.akuacom.pss2.program.dbp.DBPDataAccess#getLevelMap(java.lang.String,
	 * java.lang.String)
	 */
	public Map<String, List<String>> getLevelMap(String programName,
			String participantName, boolean isClient) {
		try {
			checkProgramName(programName);
			checkParticipantName(participantName);

			final HashMap<String, List<String>> map = new HashMap<String, List<String>>();
			final List<ProgramParticipantBidLevelMap> list = ppbidLevelMapEAO.findByProgramNameAndParticipantNameAndClient(programName,participantName,isClient);
			for (ProgramParticipantBidLevelMap bidLevelMap : list) {
                final ArrayList<String> strings = new ArrayList<String>(3);
				strings.add(bidLevelMap.getNormal());
				strings.add(bidLevelMap.getModerate());
				strings.add(bidLevelMap.getHigh());
				map.put(bidLevelMap.getTimeBlock(), strings);
			}
			return map;
		} catch (Exception ex) {
			String message = "error getting bids for participant "
					+ participantName + " for program " + programName;
			log.error(message, ex);
			throw new EJBException(message, ex);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.akuacom.pss2.program.dbp.DBPDataAccess#setLevelMap(java.lang.String,
	 * java.lang.String, java.util.Map)
	 */
	public void setLevelMap(String programName, String participantName,
			boolean isClient, Map<String, List<String>> ruleMap) {
		try {
			checkProgramName(programName);
			checkParticipantName(participantName);
			if (ruleMap == null) {
				String message = "ruleMap is null";
				log.error(message);
				throw new EJBException(message);
			}

			// remove old mapping
			ppbidLevelMapEAO.deleteByProgramNameAndParticipantNameAndClient( programName, participantName, isClient);

			// set new mapping
			final Set<String> keySet = ruleMap.keySet();
			for (String key : keySet) {
				final ProgramParticipantBidLevelMap bidLevelMap = new ProgramParticipantBidLevelMap();
				bidLevelMap.setProgramName(programName);
				bidLevelMap.setParticipant(clientEAO
						.findByNameAndClient(participantName, isClient));
				bidLevelMap.setTimeBlock(key);
				final List<String> list = ruleMap.get(key);
				bidLevelMap.setNormal(list.get(0));
				bidLevelMap.setModerate(list.get(1));
				bidLevelMap.setHigh(list.get(2));
				ppbidLevelMapEAO.create(bidLevelMap);
			}
		} catch (Exception ex) {
			String message = "error setting bids for participant "
					+ participantName + " for program " + programName;
			log.error(message, ex);
			throw new EJBException(message, ex);
		}
	}

    /**
	 * Check participant name.
	 * 
	 * @param participantName
	 *            the participant name
	 */
	private void checkParticipantName(String participantName) {
		if (participantName == null) {
			String message = "participant name is null";
			// DRMS-1664
			log.warn(message);
			throw new EJBException(message);
		}
	}

	/**
	 * Check program name.
	 * 
	 * @param programName
	 *            the program name
	 */
	private void checkProgramName(String programName) {
		if (programName == null) {
			String message = "program name is null";
			// DRMS-1664
			log.warn(message);
			throw new EJBException(message);
		}
	}

	/**
	 * Check event name.
	 * 
	 * @param eventName
	 *            the event name
	 */
	private void checkEventName(String eventName) {
		if (eventName == null) {
			String message = "event name is null";
			// DRMS-1664
			log.warn(message);
			throw new EJBException(message);
		}
	}
}
