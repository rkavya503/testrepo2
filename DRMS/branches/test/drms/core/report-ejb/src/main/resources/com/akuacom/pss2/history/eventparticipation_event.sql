SELECT  he.programName, he.eventName, DATE(he.startTime) AS eventDate,
	hep.participation,hep.percentage,hep.participantName AS clientName    
 FROM history_event he, 
      history_event_participant hep,
      participant p
  WHERE  p.client = 1
         AND hep.eventName=${eventName}
         AND hep.history_event_uuid = he.uuid 
         AND hep.participant_uuid = p.uuid 
[${orderBy}] [${range}] 
 