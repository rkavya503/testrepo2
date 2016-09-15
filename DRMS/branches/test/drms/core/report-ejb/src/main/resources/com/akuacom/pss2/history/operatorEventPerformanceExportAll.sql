SELECT ep.participantName, ${eventName} AS eventName, ${programName} AS programName,ep.startTime, ep.endTime,  
IF(ep.client=TRUE,NULL,ep.averageShed) AS avgShed, IF(ep.client=TRUE,NULL,ep.totalShed) AS totalShed,
-- ensure the order - participant followed by its clients
IF(ep.parent IS NULL, participantName, CONCAT(parent,participantName)) AS path
FROM history_event_participant ep
									
WHERE ep.eventName = ${eventName}
ORDER BY path
		    