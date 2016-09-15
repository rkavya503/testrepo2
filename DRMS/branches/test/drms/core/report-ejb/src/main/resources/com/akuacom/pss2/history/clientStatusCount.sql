SELECT count(DISTINCT all_client.participantName)
 FROM  
 (    	
	/*all clients which are enrolled in specified program directly or through aggregation*/
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
 ) all_client 
JOIN  
 (
    SELECT client_uuid, clientName,MIN(startTime) AS creationTime FROM client_status GROUP BY client_uuid,clientName
 ) client_created   
 ON all_client.participant_uuid = client_created.client_uuid 