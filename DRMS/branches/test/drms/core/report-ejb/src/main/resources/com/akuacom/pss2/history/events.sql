SELECT  DISTINCT he.eventName, he.programName,he.startTime,he.endTime,he.scheduledStartTime,he.scheduledEndTime,he.cancelled
FROM history_event he, 
history_event_participant hep,
(
	-- program participants including aggregation
	SELECT  DISTINCT pchild.participantName,child.programName
	FROM  participant pchild,participant pparent, program_participant child, program_participant parent
	WHERE  pchild.uuid = child.participant_uuid   
		AND pchild.client =0 
		AND (
			LOCATE(parent.uuid,child.ancestry)>0   -- aggregation
			||  parent.uuid=child.uuid             -- include itself 
		) 
		[ AND child.programName in ${programNames} ]    
		AND pparent.uuid=parent.participant_uuid 
	    [ AND pparent.participantName IN ${participantNames} ]
) program_participant

WHERE hep.client = FALSE
    AND he.uuid = hep.history_event_uuid AND he.programName = program_participant.programName
    AND hep.participantName = program_participant.participantName 
    AND he.programName = program_participant.programName
    AND he.startTime >= ${startTime} AND he.endTime<=${endTime}

[${orderBy}] [${range}]