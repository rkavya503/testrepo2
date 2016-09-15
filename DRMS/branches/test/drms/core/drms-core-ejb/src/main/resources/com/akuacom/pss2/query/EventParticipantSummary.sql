SELECT  evt_participant.*,
	IF(clientCount IS NULL,0,clientCount) clientCount,
	IF(validClientCount IS NULL,0,validClientCount) validClientCount
FROM 
(
	-- all  participant in the event 
	SELECT p.participantName,p.UUID,account,p.shedPerHourKW AS registerShed
	FROM event_participant ep, participant p, event e 
	WHERE ep.participant_uuid = p.uuid AND ep.event_uuid = e.uuid AND p.client = 0
	     AND e.eventName=${eventName} AND ep.eventOptOut = 0
            	
)evt_participant

 -- left join in case of participant has no clients
 -- count of all clients per participant
 LEFT JOIN 
   (
       SELECT p.participantName,COUNT(c.participantName) AS clientCount 
       FROM   participant p, participant c
       WHERE  c.parent=p.participantName
       GROUP BY p.participantName
   ) allClient 
   ON evt_participant.participantName = allClient.participantName 
   
    -- left join in case of participant has no on line clients
    -- count of all on line clients in this event
   LEFT JOIN 
   (
       SELECT p.participantName,COUNT(c.participantName) AS validClientCount 
       FROM participant p, participant c, event_participant ep, event e
       WHERE c.parent = p.participantName AND c.status = 0 /* client on line*/ 
             AND e.eventName=${eventName} AND ep.event_uuid =e.uuid 
             AND ep.participant_uuid = c.uuid  AND ep.eventOptOut = 0 /*event no opt out */   
     
       GROUP BY p.participantName
   ) validClient
   ON evt_participant.participantName = validClient.participantName 

   ORDER BY participantName