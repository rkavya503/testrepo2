SELECT count(*) 
FROM 
(
 SELECT DISTINCT p.uuid
 FROM participant p
 LEFT JOIN program_participant pp ON p.uuid=pp.participant_uuid
 WHERE p.client = 0 [AND p.participantName LIKE ${participantName}]  [and p.account LIKE ${account}] [AND pp.programName like ${programName}]  
 [and pp.programName =${exactProgramName}] [ ${multiprograms}] [${multiParticipantNames}] [${multiAccounts}] 
) participant
