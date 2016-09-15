SELECT ep.participantName, ${eventName} AS eventName, ${programName} AS programName,ep.startTime, ep.endTime,  
IF(ep.client=TRUE,NULL,ep.averageShed) AS avgShed, IF(ep.client=TRUE,NULL,ep.totalShed) AS totalShed,
-- ensure the order - participant followed by its clients
IF(ep.parent IS NULL, participantName, CONCAT(parent,participantName)) AS path
FROM history_event_participant ep
									
WHERE ep.eventName = ${eventName}
AND ep.participantName IN
(
SELECT DISTINCT child.participantName AS participantName
FROM   program_participant ppchild, program_participant ppparent,participant parent,participant child 
WHERE  parent.participantName = ${participantName} AND ppparent.participant_uuid = parent.uuid  
	AND  ppchild.programName = ${programName}
 	AND (LOCATE(ppparent.uuid,ppchild.ancestry)>0 ||  ppchild.uuid=ppparent.uuid) 
 	AND child.uuid = ppchild.participant_uuid
 			
UNION
 			
SELECT participantName FROM participant WHERE parent IN 
(
 SELECT DISTINCT child.participantName AS participantName 
 FROM   program_participant ppchild, program_participant ppparent,participant parent,participant child 
 WHERE  parent.participantName = ${participantName} AND ppparent.participant_uuid = parent.uuid  
 	AND  ppchild.programName = ${programName}
 	AND (LOCATE(ppparent.uuid,ppchild.ancestry)>0 ||  ppchild.uuid=ppparent.uuid) 
 	AND child.uuid = ppchild.participant_uuid			
 ) 
)
ORDER BY path 