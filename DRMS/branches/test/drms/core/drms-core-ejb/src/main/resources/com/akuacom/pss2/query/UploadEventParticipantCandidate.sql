SELECT  evt_participant.*,optInClient.clientName,optInClient.clientUUID,
	IF(clientCount IS NULL,0,clientCount) clientCount,
	IF(validClientCount IS NULL,0,validClientCount) validClientCount,
	ps.*
FROM 
(
	-- all event participant 
	SELECT participantName,UUID,account,registerShed,shedType,participantOptOut, programOptOut,
		-- sum(ancestorOptOut)>0 means at least one ancestor has been opt out
		IF(SUM(ancestorOptOut)>0,TRUE,FALSE) AS ancestorOptOut, 
		IF(SUM(ancestorProgramOptOut)>0,TRUE,FALSE) AS ancestorProgramOptOut
	FROM 
	 (
	 -- all participants 
	 SELECT -- parent.participantName AS ancestor,
		p.participantName,p.UUID,p.account,p.shedPerHourKW AS registerShed,p.shedType,
		if(p.optOut is NULL, 0, p.optOut) AS participantOptOut,
		if(pp.optStatus is NULL, 0, pp.optStatus) AS programOptOut,
		0 AS ancestorOptOut, 0 AS ancestorProgramOptOut
	 FROM 
		participant p, program_participant pp
	 WHERE
		p.client=0    
		AND p.uuid=pp.participant_uuid   
		AND pp.programName=${programName} 
		AND pp.optStatus=0
		AND pp.state=1
		AND (p.aggregator=1 [OR p.account IN ${accounts}])
	) all_participant
	GROUP BY participantName
) evt_participant

 -- left join in case of participant has no clients
 -- count of all clients per participant
 LEFT JOIN 
   (
       SELECT p.participantName,COUNT(c.participantName) AS clientCount 
       FROM   participant p, participant c
       WHERE  c.parent=p.participantName
       GROUP BY p.participantName
   ) allClient 
   ON evt_participant.participantName = allClient.participantName 
   
    -- left join in case of participant has no on line clients
    -- count of all on line clients which is not opt out
   LEFT JOIN 
   (
       SELECT p.participantName,COUNT(c.participantName) AS validClientCount 
       FROM   participant p, participant c,program_participant pc
       WHERE  c.parent=p.participantName AND c.status = 0 /*client online*/
              AND pc.participant_uuid = c.uuid AND pc.programName=${programName} AND pc.state=1 /* client opt in */
       GROUP BY p.participantName
   ) validClient
   ON evt_participant.participantName = validClient.participantName 

   -- fetch all clients which join the program. use left join in case no clients for participant
   LEFT JOIN 
   (
       SELECT p.participantName AS parent,c.participantName AS clientName,p.UUID AS clientUUID,c.account AS clientAccount
       FROM   participant p, participant c,program_participant pc
       WHERE  c.parent=p.participantName 
              AND pc.participant_uuid = c.uuid AND pc.programName=${programName} AND pc.state=1 /* client opt in */
   )optInClient
   ON  evt_participant.participantname = optInClient.parent
    -- fetch all ADVANCED shed value 
   LEFT JOIN 
	(
	SELECT participant_uuid, 
	SUM(IF(hourIndex=0, VALUE,0)) AS 'shedHourIndex0',
	SUM(IF(hourIndex=1, VALUE,0)) AS 'shedHourIndex1',
	SUM(IF(hourIndex=2, VALUE,0)) AS 'shedHourIndex2',
	SUM(IF(hourIndex=3, VALUE,0)) AS 'shedHourIndex3',
	SUM(IF(hourIndex=4, VALUE,0)) AS 'shedHourIndex4',
	SUM(IF(hourIndex=5, VALUE,0)) AS 'shedHourIndex5',
	SUM(IF(hourIndex=6, VALUE,0)) AS 'shedHourIndex6',
	SUM(IF(hourIndex=7, VALUE,0)) AS 'shedHourIndex7',
	SUM(IF(hourIndex=8, VALUE,0)) AS 'shedHourIndex8',
	SUM(IF(hourIndex=9, VALUE,0)) AS 'shedHourIndex9',
	SUM(IF(hourIndex=10, VALUE,0)) AS 'shedHourIndex10',
	SUM(IF(hourIndex=11, VALUE,0)) AS 'shedHourIndex11',
	SUM(IF(hourIndex=12, VALUE,0)) AS 'shedHourIndex12',
	SUM(IF(hourIndex=13, VALUE,0)) AS 'shedHourIndex13',
	SUM(IF(hourIndex=14, VALUE,0)) AS 'shedHourIndex14',
	SUM(IF(hourIndex=15, VALUE,0)) AS 'shedHourIndex15',
	SUM(IF(hourIndex=16, VALUE,0)) AS 'shedHourIndex16',
	SUM(IF(hourIndex=17, VALUE,0)) AS 'shedHourIndex17',
	SUM(IF(hourIndex=18, VALUE,0)) AS 'shedHourIndex18',
	SUM(IF(hourIndex=19, VALUE,0)) AS 'shedHourIndex19',
	SUM(IF(hourIndex=20, VALUE,0)) AS 'shedHourIndex20',
	SUM(IF(hourIndex=21, VALUE,0)) AS 'shedHourIndex21',
	SUM(IF(hourIndex=22, VALUE,0)) AS 'shedHourIndex22',
	SUM(IF(hourIndex=23, VALUE,0)) AS 'shedHourIndex23'
	FROM participant_shed_entry 
	GROUP BY participant_uuid
	) ps
	ON evt_participant.uuid = ps.participant_uuid
   ORDER BY participantName