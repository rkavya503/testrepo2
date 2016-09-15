SELECT DISTINCT he.programName, he.eventName, DATE(he.startTime) AS eventDate,hep.participation,hep.percentage,hep.participantName AS clientName   
FROM history_event he,
     history_event_participant hep, 
     (
	/*all clients which are enrolled in specified program directly or through aggregation*/ 
 	SELECT  pchild.participantName, pchild.uuid,pp.programName FROM  
 	   participant pchild, participant pparent,
 	   (		    
 	    /* all participants enrolled in program directly or through aggregation */ 
 		SELECT DISTINCT ppchild.participant_uuid,child.participantName,ppchild.programName AS programName  
 		FROM   program_participant ppchild, program_participant ppparent,participant parent,participant child 
 		WHERE  parent.participantName IN ${participantName} AND ppparent.participant_uuid = parent.uuid  
 			AND  ppchild.programName IN ${programName}
 			AND (LOCATE(ppparent.uuid,ppchild.ancestry)>0 ||  ppchild.uuid=ppparent.uuid) 
 			AND child.uuid = ppchild.participant_uuid 
 	    ) pp   
 		WHERE
 		 -- don't need to check whether client is in the program or not. 
 		 -- because this relationship only reflects current participation, however, we search for historical event
 	         pchild.parent=pparent.participantName AND pparent.uuid = pp.participant_uuid 
    )all_clients
 
 WHERE all_clients.uuid = hep.participant_uuid 
          AND hep.history_event_uuid = he.uuid
		  AND  he.programName = all_clients.programName
          AND he.startTime >=${startTime} AND he.endTime <=${endTime}
          
 [${orderBy}] [${range}] 
          

       
    