
SELECT all_client.participantName,offline.startTime,offline.duration,offline.offlineTillNow
FROM 
(   
    /* all clients which are clients of give participant or enrolled in through aggregation from this participant*/
    SELECT DISTINCT pchild.participantName, cp.participant_uuid FROM 
	   participant pchild, participant pparent, program_participant cp,
	   (		   
	    /* all participants enrolled in program directly or through aggregation */
		SELECT ppchild.participant_uuid,child.participantName AS participantName, ppchild.programName as programName
		FROM   program_participant ppchild, program_participant ppparent,participant parent,participant child
		WHERE  parent.participantName IN ${participantName} AND ppparent.participant_uuid = parent.uuid 
			[AND  ppchild.programName =${programName}] 
			AND ( LOCATE(ppparent.uuid,ppchild.ancestry)>0  ||  ppchild.uuid=ppparent.uuid ) -- include itself
			AND child.uuid = ppchild.participant_uuid
	    ) pp  
		WHERE cp.state = 1 [AND cp.programName =${programName}] AND cp.participant_uuid = pchild.uuid
	    AND pchild.parent=pparent.participantName AND pparent.uuid = pp.participant_uuid
)   
all_client
JOIN 
(

SELECT a.client_uuid, a.clientName, a.startTime, a.endTime,a.offlineTillNow,
    TIMESTAMPDIFF(MINUTE,startTime, endTime)  AS duration
FROM
	/*replace endtime as now if it is null*/ 
	( 
		SELECT client_uuid, clientName, startTime,IF(endTime is NULL, TRUE,FALSE) as offlineTillNow, IF(endTime IS NULL,NOW(),endTime) AS endTime  
		FROM client_status WHERE STATUS = FALSE 
	) a  
WHERE 
    /*event offline period which has overlap with specified time span */
    ( startTime BETWEEN ${startTime} AND ${endTime} OR endTime BETWEEN ${startTime} AND ${endTime} 
       OR ${startTime} BETWEEN startTime AND endTime OR ${endTime} BETWEEN startTime AND endTime ) 
)offline

ON all_client.participant_uuid = offline.client_uuid
[${orderBy}] [${range}]