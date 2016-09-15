-- all clients for giving event participant
SELECT ep.participantName, ${eventName} AS eventName, ${programName} AS programName,ep.client,
	  ep.startTime, ep.endTime, NULL AS avgShed, NULL AS totalShed 
FROM history_event_participant ep									
WHERE ep.parent=${parent} and ep.eventName = ${eventName} AND ep.client = true 