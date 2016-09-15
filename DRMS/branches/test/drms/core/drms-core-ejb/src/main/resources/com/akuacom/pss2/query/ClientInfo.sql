        	SELECT jointable1.*,pc1.address FROM 
        	( 
        	SELECT DISTINCT  
        	pClient.priority,pClient.eventOptOut,pClient.eventStartTime,pClient.eventEndTime,pParticipant.testParticipant,pParticipant.aggregator,pParticipant.customer as customer,pParticipant.non_auto_dr as nonAutoDR,pParticipant.leadAccount,pParticipant.ABank,autoControlValue,manualControlValue,pClient.manualControl, 
        	pClient.uuid,pClient.participantName AS CLIENT,pClient.parent AS participant,pParticipant.account,IFNULL(p2.participantName, '') AS parent, 
        	pParticipant.premiseNumber, pParticipant.startDate, pParticipant.endDate,pp0.programName,pp0.state,  
        	pClient.eventStatus,pClient.operationMode,pClient.status,pClient.commTime,pClient.type,pClient.deviceType  
        	FROM 
        	( 
        	SELECT DISTINCT table1.programName,table1.priority, table1.eventOptOut,table1.startTime AS eventStartTime,table1.endTime AS eventEndTime,table1.stringValue AS autoControlValue,table2.value AS manualControlValue,p.*  
        	FROM participant p  
        	LEFT JOIN  
        	(SELECT epse.stringValue,ep.participant_uuid,ep.eventOptOut,event.startTime,event.endTime,program.priority,program.name AS programName,event.uuid 
        	FROM event_participant ep 
        	LEFT OUTER JOIN event
			ON event.uuid = ep.event_uuid
			LEFT OUTER JOIN program
			ON program.name=event.programName
        	LEFT OUTER JOIN event_participant_signal eps 
        	ON ep.uuid=eps.event_participant_uuid 
        	LEFT OUTER JOIN event_participant_signal_entry epse 
        	ON eps.uuid=epse.eventParticipantSignalUuid 
        	LEFT OUTER JOIN signal_def sd 
        	ON eps.signal_def_uuid=sd.uuid 
        	WHERE sd.signalName = 'mode' AND epse.stringValue IS NOT NULL  
        	) table1 
        	ON p.uuid = table1.participant_uuid 
        	LEFT JOIN 
        	(SELECT pm.value,pm.participant_uuid FROM participant_manualsignal pm WHERE pm.name ='mode') table2 
        	ON p.uuid = table2.participant_uuid 

        	WHERE p.client = '1' 
        	) pClient 
        	INNER JOIN participant pParticipant ON pParticipant.participantName = pClient.parent 
        	LEFT OUTER JOIN program_participant pp0  
        	ON pClient.uuid = pp0.participant_uuid  
        	LEFT OUTER JOIN program_participant pp1  
        	ON pParticipant.uuid = pp1.participant_uuid  
        	LEFT OUTER JOIN program_participant pp2  
        	ON pp2.uuid = pp1.parent_uuid   
        	LEFT OUTER JOIN participant p2  
        	ON p2.uuid = pp2.participant_uuid 
        	) jointable1 
        	 
        	LEFT OUTER JOIN participant_contact pc1  
        	ON pc1.participant_uuid = jointable1.uuid  
        	ORDER BY jointable1.client 