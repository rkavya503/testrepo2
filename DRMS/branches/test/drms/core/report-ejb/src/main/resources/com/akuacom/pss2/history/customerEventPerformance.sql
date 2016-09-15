SELECT  DISTINCT ${participantName} AS participantName,${eventName} AS eventName, ${programName} AS programName,hep.startTime, hep.endTime,
	    shed.avgShed, shed.totalShed
FROM 
  -- shed value
  (
      -- Get average shed and total shed of the aggregator
		SELECT ep.participantName, SUM(ep.averageShed) AS avgShed, SUM(ep.totalShed) AS totalShed                           
		FROM history_event_participant ep 
		WHERE ep.participantName IN(
                SELECT child.participantName AS participantName
                FROM   program_participant ppchild, program_participant ppparent,participant parent,participant child
				WHERE  parent.participantName = ${participantName} AND ppparent.participant_uuid = parent.uuid 
				[AND  ppchild.programName =${programName}] 
				AND ( LOCATE(ppparent.uuid,ppchild.ancestry)>0  ||  ppchild.uuid=ppparent.uuid ) -- include itself
				AND child.uuid = ppchild.participant_uuid
		)
		and ep.eventName=${eventName}
 )shed
   -- join to get start Time, endTime, client etc
   -- aggregator may not be in the event
 LEFT JOIN history_event_participant hep
 ON hep.eventName=${eventName} AND shed.participantName =  hep.participantName 