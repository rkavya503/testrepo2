SELECT aclient.name, pc.address,pc.description,pc.eventNotification,pc.commNotification,demandLimitingNotifications
FROM 
(
 SELECT DISTINCT p.uuid, p.type,p.participantName AS name,p.parent,p.deviceType,p.status AS commsStatus,
 p.operationMode AS lastMode,p.eventStatus AS lastStatus,
 p.commTime AS lastContact,p.manualControl
 FROM participant p
 LEFT OUTER JOIN program_participant pp ON p.uuid=pp.participant_uuid
 WHERE p.client = 1 [and p.participantName like ${clientName}] [and p.type =${type}] 
	  [and p.status =${commsStatus}] [and p.parent like ${parent}] [ and p.eventStatus=${eventStatus}] 
	  [AND pp.programName like ${programName}]  [and pp.programName= ${exactProgramName}]  [${multiprograms}]
 [${orderBy}] [${range}]
) aclient
  
 LEFT OUTER JOIN participant_contact  pc 
	ON aclient.uuid = pc.participant_uuid
  