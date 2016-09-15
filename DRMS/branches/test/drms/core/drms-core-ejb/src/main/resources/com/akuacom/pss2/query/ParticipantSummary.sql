SELECT parent.*, pparent.programName, pparent.optStatus as optOut,ep.eventName,child.participantName AS childParticipant
FROM 
(
 SELECT DISTINCT p.uuid, p.requestId, p.participantName AS name,p.bcdRepName AS bcdRepName, p.autoDrProfileStartDate AS autoDrProfileStartDate, p.account AS accountNumber,p.installer,p.userType,p.notes,p.type,
 p.customerName,p.serviceStreetAddress as address,p.serviceCityName,p.ABank as abank,p.startDate,p.endDate,p.enrollmentDate,p.aggregator,p.dataEnabler,p.testParticipant,p.customer as customer,p.non_auto_dr as nonAutoDR
 FROM participant p
 LEFT JOIN program_participant pp ON p.uuid=pp.participant_uuid
 WHERE p.client = 0 [AND p.participantName LIKE ${participantName}]  [and p.account LIKE ${account}] [AND pp.programName like ${programName}]  
 [and pp.programName =${exactProgramName}] [${multiprograms}] [${multiParticipantNames}] [${multiAccounts}]  
[${orderBy}] [${range}]
) parent

-- get non opt out programs 
LEFT OUTER JOIN  program_participant pparent ON parent.uuid =  pparent.participant_uuid
-- get aggregation child in this program 
LEFT OUTER JOIN  program_participant pchild  ON pchild.parent_uuid = pparent.uuid AND pchild.programName=pparent.programName
LEFT OUTER JOIN  participant child ON child.uuid = pchild.participant_uuid

-- get non-opt-out events created for the program 
LEFT OUTER JOIN 
(
 SELECT evt.eventName, ep.participant_uuid, evt.programName
 FROM event_participant ep, event evt WHERE evt.uuid = ep.event_uuid AND (ep.eventOptOut = 0 OR ep.eventOptOut is NULL)
)ep ON parent.uuid = ep.participant_uuid  AND ep.programName = pparent.programName