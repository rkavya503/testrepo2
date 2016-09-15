
SELECT all_client.participantName,offline.startTime,offline.duration
FROM 
(   
    /* all clients which are clients of give participant or enrolled in through aggregation from this participant*/
    SELECT participantName,participant_uuid FROM 
         history_event_participant WHERE eventName =${eventName} AND CLIENT=TRUE 
)   
all_client
JOIN 
(
SELECT a.client_uuid, a.clientName,a.startTime,a.endTime,
    TIMESTAMPDIFF(MINUTE,startTime,endTime)  AS duration
FROM
	/*replace endtime as now if it is null*/ 
	( 
		SELECT client_uuid, clientName, startTime,IF(endTime IS NULL,NOW(),endTime) AS endTime  
		FROM client_status WHERE STATUS = FALSE 
	) a  
WHERE 
    /*event offline period which has overlap with specified time span */
    ( startTime BETWEEN ${startTime} AND ${endTime} OR endTime BETWEEN ${startTime} AND ${endTime} 
       OR ${startTime} BETWEEN startTime AND endTime OR ${endTime} BETWEEN startTime AND endTime ) 
)offline

ON all_client.participant_uuid = offline.client_uuid
[${orderBy}] [${range}]