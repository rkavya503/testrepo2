SELECT  participantName AS ParticipantName  
  FROM program_participant pp 
  LEFT OUTER JOIN   participant p  ON p.uuid=pp.participant_uuid 
  WHERE  client = 1 and optStatus = 1
  and programName = ${programName} 
  