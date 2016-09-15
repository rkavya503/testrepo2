SELECT p.name AS programName,p.className,p.priority,client_count.clientCount, part_count.participantCount
FROM program p
LEFT OUTER JOIN 
(
     -- client count 
	SELECT programName, COUNT(participantName) AS clientCount
	FROM(
		SELECT p.participantName, pp.programName
		FROM  participant p 
		LEFT  JOIN program_participant pp
		ON    p.uuid = pp.participant_uuid 
		WHERE p.client = TRUE AND pp.programName IS NOT NULL 
	) p
	GROUP BY programName

)client_count 
ON p.name = client_count.programName

LEFT OUTER JOIN 
(
-- participant count
	SELECT programName, COUNT(participantName) AS participantCount
	FROM
	(
		SELECT p.participantName, pp.programName
		FROM  participant p 
		LEFT  JOIN program_participant pp
		ON    p.uuid = pp.participant_uuid 
		WHERE p.client = FALSE AND pp.programName IS NOT NULL 
	) p
	GROUP BY programName
)part_count
 ON p.name = part_count.programName
 WHERE p.name <> ${testProgram} 
 AND p.state=1
-- client test program, all participant and clients are in the program
UNION 
SELECT ${testProgram}  AS programName, 
  (SELECT className FROM pss2.program WHERE NAME =${testProgram}) AS className,
  (SELECT priority FROM program WHERE NAME =${testProgram}) AS priority,
  (SELECT COUNT(*) FROM participant p WHERE p.client = TRUE) AS clientCount,
  (SELECT COUNT(*) FROM participant p WHERE p.client = FALSE) AS participantCount

[${orderBy}];
-- ORDER BY priority;

