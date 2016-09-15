SELECT evt_client.participant_uuid, participantName,
	IF(offline.times IS NULL,0,offline.times) AS times,
	IF(offline.totalTime IS NULL,0,offline.totalTime) AS totalTime,
	duringEvent AS timePercent, duringEvent 
FROM 
    /*all clients participated in specified event */
    (
	 SELECT eventName,participant_uuid,participantName,offlinePerEvent AS duringEvent FROM 
         history_event_participant WHERE eventName =${eventName} AND client=TRUE 
     ) evt_client
LEFT JOIN 
	(SELECT client_uuid,clientName, COUNT(*) AS times,SUM(TIMESTAMPDIFF(MINUTE,startTime,endTime)) AS totalTime FROM
	   (
	    /* all client offline period which has overlap with event time period*/
		SELECT a.client_uuid, a.clientName, GREATEST(startTime,${startTime}) AS startTime,LEAST(a.endTime,${endTime}) AS endTime
		FROM 
		/*replace endtime as now if it is null*/
		(
			SELECT client_uuid, clientName, startTime,IF(endTime IS NULL,NOW(),endTime) AS endTime 
			FROM client_status WHERE STATUS = FALSE
		)a
		WHERE  
		/*event which has overlap with specified time span */
		(startTime BETWEEN ${startTime} AND ${endTime} OR endTime BETWEEN ${startTime} AND ${endTime}  
		   OR ${startTime} BETWEEN a.startTime AND a.endTime OR ${endTime} BETWEEN a.startTime AND a.endTime ) 
	   ) offline_instance 
	   GROUP BY client_uuid,clientName
	 ) offline 
ON evt_client.participant_uuid = offline.client_uuid
[${orderBy}] [${range}]