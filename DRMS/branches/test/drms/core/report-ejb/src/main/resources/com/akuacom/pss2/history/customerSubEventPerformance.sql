SELECT  DISTINCT shed.participantName,shed.client,${eventName} AS eventName, ${programName} AS programName,hep.startTime, hep.endTime,
	    IF(hep.client=TRUE,NULL,shed.avgShed) AS avgShed, IF(hep.client=TRUE,NULL,shed.totalShed) AS totalShed
FROM 
  -- shed value
  (
      -- Get average shed and total shed of the aggregator
	  SELECT allParticipant.participantName, allParticipant.client, SUM(ep.averageShed) AS avgShed, SUM(ep.totalShed) AS totalShed                           
	  FROM 
	  history_event_participant ep,  
	  -- select direct child participants 
	  ( 
			SELECT aggregator.participantName,pchild.participantName AS descendantName,aggregator.client 
			FROM  
			(                   
			      -- direct children and itself 
			      -- if its direct child and none of the descendants are not in the event
			      -- this child /aggregator should not appear in the performance report
			      SELECT pchild.participantName,pchild.client FROM  
			      participant pchild,participant pparent, program_participant child, program_participant parent 
			      WHERE pchild.uuid = child.participant_uuid     
			      AND  child.parent_uuid = parent.uuid  AND  child.programName=${programName} 
			      AND pparent.uuid=parent.participant_uuid AND pparent.participantName=${parent} 
					      
			      -- all clients 
			      UNION  
			      SELECT child.participantName,child.client FROM  participant parent, participant child 
			      WHERE child.parent=parent.participantName AND child.client =1 AND parent.participantName=${parent} 
				       
			) aggregator,     
		   participant pchild,participant pparent, program_participant child, program_participant parent 
		   WHERE  
				  pchild.uuid = child.participant_uuid     
			   AND ( LOCATE(parent.uuid,child.ancestry)>0  /* aggregation */ ||  parent.uuid=child.uuid /* include itself */)       
			   AND child.programName=${programName}     
			   AND pparent.uuid=parent.participant_uuid AND pparent.participantName=aggregator.participantName 	   
	 ) allParticipant 
	 WHERE allParticipant.descendantName =  ep.participantName AND ep.eventName=${eventName} 
	 GROUP BY  allParticipant.participantName 
 )shed
   -- join to get start Time, endTime, client etc
   -- aggregator may not be in the event
 LEFT JOIN history_event_participant hep
 ON hep.eventName=${eventName} AND shed.participantName =  hep.participantName
 ORDER BY hep.client desc
