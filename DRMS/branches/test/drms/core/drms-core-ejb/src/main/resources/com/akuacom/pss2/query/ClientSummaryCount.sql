 SELECT  COUNT(DISTINCT p.uuid) 
 FROM participant p
 LEFT OUTER JOIN program_participant pp ON p.uuid=pp.participant_uuid
 WHERE p.client = 1  [ and p.participantName like ${clientName}] [ and p.type =${type}]   [ and p.eventStatus=${eventStatus}]
  [ and p.status =${commsStatus}] [and p.parent like ${parent}] [ AND pp.programName like ${programName}]  
  [ and pp.programName= ${exactProgramName} ] 
  [${multiprograms}] [${multiParticipantNames}] [${multiClientNames}] 

 
 
	  