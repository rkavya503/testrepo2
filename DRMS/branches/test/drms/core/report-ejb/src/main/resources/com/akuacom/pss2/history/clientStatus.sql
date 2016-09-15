 SELECT all_client.*, 
      IF(offline.times is NULL,0,offline.times) as times,
      IF(offline.totalTime IS NULL, 0, offline.totalTime) as totalTime,
	  /* if the start time is before client creation time, then valid time range should be from creation time to end time */
	  IF(offline.totalTime IS NULL, 0, offline.totalTime/TIMESTAMPDIFF(MINUTE,GREATEST(creationTime,${startTime}),${endTime})) AS timePercent,
 	  offline_during_event.duringEvent  
 FROM  
 (    	
	select p.participantName, p.uuid as participant_uuid from participant p where p.uuid in ${clientIds}
 )    
 all_client 
 JOIN 
 (
    SELECT client_uuid, clientName,MIN(startTime) AS creationTime
	FROM client_status  where client_uuid in ${clientIds} GROUP BY client_uuid,clientName
	
 ) client_created   
 ON all_client.participant_uuid = client_created.client_uuid  
 
LEFT JOIN 
/* times, totalTime of offline*/
(
 SELECT client_uuid,clientName,COUNT(*) AS times,SUM(TIMESTAMPDIFF(MINUTE,startTime,endTime)) AS totalTime FROM
   (
     /* all client offline period which has overlap with specified time span */
    SELECT a.client_uuid, a.clientName, GREATEST(startTime,${startTime}) AS startTime,
    LEAST(a.endTime,${endTime}) AS endTime
    FROM
	/*replace endtime as now if it is null*/ 
	( 
		SELECT client_uuid, clientName, startTime,IF(endTime IS NULL,NOW(),endTime) AS endTime  
		FROM client_status WHERE STATUS = FALSE  AND client_uuid in ${clientIds}
	) a  WHERE
    /*event which has overlap with specified time span */
    ( startTime BETWEEN ${startTime} AND ${endTime} OR endTime BETWEEN ${startTime} AND ${endTime} 
       OR ${startTime} BETWEEN startTime AND endTime OR ${endTime} BETWEEN startTime AND endTime ) 
   ) offline_instance  
    GROUP BY client_uuid,clientName
) 
  
 offline ON all_client.participant_uuid = offline.client_uuid 
  
LEFT JOIN 
/* offline during event */ 
( 
	SELECT client_uuid,clientName,SUM(off_duringEvent)/SUM(eventDuration) AS duringEvent FROM 
	(
		SELECT evt.eventName, off.client_uuid,off.clientName, TIMESTAMPDIFF(MINUTE,evt.startTime,evt.endTime) AS eventDuration,
		SUM(CASE 
		 WHEN (evt.startTime < off.endTime   AND off.startTime <=evt.startTime AND off.endTime<=evt.endTime)  
		  THEN  TIMESTAMPDIFF(MINUTE,evt.startTime,off.endTime)
		 WHEN (off.startTime < evt.endTime   AND evt.endTime  <= off.endTime AND evt.startTime<=off.startTime)  
		   THEN  TIMESTAMPDIFF(MINUTE,off.startTime,evt.endTime)
		 WHEN (evt.startTime <= off.startTime AND off.endTime <= evt.endTime)      
			THEN  TIMESTAMPDIFF(MINUTE,off.startTime,off.endTime)
		 WHEN (off.startTime < evt.startTime AND evt.endTime < off.endTime)      
		   THEN  TIMESTAMPDIFF(MINUTE,evt.startTime,evt.endTime)
		 ELSE 0
		END)  AS off_duringEvent
		FROM 
		  (  
			/* all events which has overlap with specified time span */
			SELECT a.eventName, a.participant_uuid,a.participantName, GREATEST(b.startTime,${startTime}) AS startTime, 
			LEAST(b.endTime,${endTime}) AS endTime
			FROM history_event_participant a,history_event b WHERE a.CLIENT = TRUE AND a.eventName=b.eventName 
			AND a.participant_uuid in ${clientIds} AND
			( b.startTime BETWEEN ${startTime} AND ${endTime} OR b.endTime BETWEEN ${startTime} AND ${endTime}  
			   OR ${startTime} BETWEEN b.startTime AND b.endTime OR ${endTime} BETWEEN b.startTime AND b.endTime ) 
		  )evt,
		  (
			/*replace endtime as now if it is null*/
			SELECT a.client_uuid, a.clientName, a.startTime,
			IF(a.endTime IS NULL,NOW(),a.endTime) AS endTime 
			FROM client_status a WHERE STATUS = FALSE and a.client_uuid in ${clientIds}
		  )off
		WHERE off.client_uuid = evt.participant_uuid
		GROUP BY eventName, off.client_uuid
	) offlineDuringEvent
	GROUP BY client_uuid,clientName
)offline_during_event 
ON all_client.participant_uuid = offline_during_event.client_uuid

[${orderBy}] [${range}]
