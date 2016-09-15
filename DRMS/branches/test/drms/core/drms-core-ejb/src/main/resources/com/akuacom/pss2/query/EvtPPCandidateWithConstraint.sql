SELECT  evt_participant.*,
	optInClient.clientName,optInClient.clientUUID,
 	IF(clientCount IS NULL,1,clientCount) clientCount, 
 	IF(validClientCount IS NULL,0,validClientCount) validClientCount,
 	pconstraint.*
 FROM  
 ( 
 	-- all event participant  
 	SELECT participantName,UUID,aggPath,parent,account,registerShed,participantOptOut, programOptOut, 
 		-- sum(ancestorOptOut)>0 means at least one ancestor has been opt out 
 		IF(SUM(ancestorOptOut)>0,TRUE,FALSE) AS ancestorOptOut,  
 		IF(SUM(ancestorProgramOptOut)>0,TRUE,FALSE) AS ancestorProgramOptOut 
 	FROM  
 	 ( 
 	 -- all participants  
 	 SELECT -- parent.participantName AS ancestor, 
 		child.participantName,pchild.parent_uuid AS parent,
 		CONCAT(IF(pchild.ancestry IS NULL,'', pchild.ancestry),pchild.uuid) AS aggPath,
 		child.UUID,child.account,child.shedPerHourKW AS registerShed, 
 		child.optOut AS participantOptOut,pchild.optStatus AS programOptOut, 
 		parent.optOut AS ancestorOptOut, pparent.optStatus AS ancestorProgramOptOut 
 	 FROM  
 		participant child,participant parent, program_participant pchild, program_participant pparent  
 	 WHERE 
 		child.uuid = pchild.participant_uuid     
 		AND child.client =0   
 		AND (LOCATE(pparent.uuid,pchild.ancestry)>0   /* aggregation */ || pparent.uuid=pchild.uuid /* include itself*/)  
 		AND pchild.programName=${programName}       
 		AND parent.uuid=pparent.participant_uuid  
 	)all_participant 
 	GROUP BY participantName 
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
     -- count of all on line clients which is not opt out 
    LEFT JOIN  
    ( 
        SELECT p.participantName,COUNT(c.participantName) AS validClientCount  
        FROM   participant p, participant c,program_participant pc 
        WHERE  c.parent=p.participantName AND c.status = 0 /*client online*/ 
               AND pc.participant_uuid = c.uuid AND pc.programName=${programName} AND pc.state=1 /* client opt in */ 
        GROUP BY p.participantName 
    ) validClient 
    ON evt_participant.participantName = validClient.participantName  
  
    -- fetch constraints 
    LEFT JOIN  
    ( 
	SELECT pconstraint.uuid AS constraintUUID,pp.participant_uuid, pdate.uuid AS dateUUID,pdate.invalidDate,
	minActive,maxActive,activeAction,minNotify,maxNotify,notifyAction,minDuration,maxDuration,durationAction,maxConsecutiveD,consecutiveAction
	FROM  program_participant pp,program_participant_constraint pconstraint,constraint_invalid_date pdate
        WHERE pp.uuid = pconstraint.program_participant_uuid AND  pdate.program_participant_constraint_uuid = pconstraint.uuid  		
    )pconstraint
    ON  evt_participant.UUID = pconstraint.participant_uuid
    
   
    -- fetch all clients which join the program. use left join in case no clients for participant
   LEFT JOIN 
   (
       SELECT p.participantName AS parent,c.participantName AS clientName, c.uuid AS clientUUID, c.account AS clientAccount
       FROM   participant p, participant c,program_participant pc
       WHERE  c.parent=p.participantName 
              AND pc.participant_uuid = c.uuid AND pc.programName=${programName} AND pc.state=1 /* client opt in */
   )optInClient
   ON  evt_participant.participantname = optInClient.parent
 
   ORDER BY participantName,clientName,constraintUUID