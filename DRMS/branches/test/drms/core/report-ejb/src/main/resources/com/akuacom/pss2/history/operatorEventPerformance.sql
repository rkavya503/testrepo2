-- all participants of the event, including aggregators
SELECT ep.participantName, ${eventName} AS eventName, ${programName} AS programName,ep.client,
	  ep.startTime, ep.endTime, ep.averageShed AS avgShed, ep.totalShed AS totalShed 
FROM history_event_participant ep									
WHERE ep.eventName = ${eventName} AND ep.client = false 