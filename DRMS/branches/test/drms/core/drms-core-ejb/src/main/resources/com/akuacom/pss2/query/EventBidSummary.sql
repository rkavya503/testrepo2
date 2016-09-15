SELECT participantName,account AS accountNumber,ep.bidState,bid.startTime AS blockStart,
	bid.endTime AS blockEnd,bid.reductionKW,bid.priceLevel,bid.active
FROM event e, event_participant ep, event_participant_bid_entry bid,participant p

WHERE e.eventName=${eventName}
    AND e.uuid = ep.event_uuid AND ep.participant_uuid = p.uuid
    AND ep.uuid= bid.event_participant_uuid AND ep.eventOptOut = 0
 ORDER BY participantName, bid.startTime