SELECT aclient.*,
  -- program name and program priority
  pp.programName,  pp.state,pg.priority /* program priority */ ,
  ep.eventName,ep.startTime, ep.endTime, ep.eventOptOut,
  s.signalName, s.levelSignal,
  s.stringValue, s.numberValue,s.signalStartTime, 
  manual_signal.name AS manualSignalName, manual_signal.value AS manualSignalValue,
  parent.ABank,parent.leadAccount,parent.testParticipant,parent.aggregator,parent.customer as customer,parent.non_auto_dr as nonAutoDR
  [, ${withcontacts} pc.address]
FROM 
(
 SELECT DISTINCT p.uuid, p.type,p.participantName AS name,p.parent,p.deviceType,p.status AS commsStatus,
 p.operationMode AS lastMode,p.eventStatus AS lastStatus,
 p.commTime AS lastContact,p.manualControl
 FROM participant p
 LEFT OUTER JOIN program_participant pp ON p.uuid=pp.participant_uuid
 WHERE p.client = 1 [and p.participantName like ${clientName}] [and p.type =${type}] 
	  [and p.status =${commsStatus}] [and p.parent like ${parent}] [ and p.eventStatus=${eventStatus}] 
	  [AND pp.programName like ${programName}]  [and pp.programName= ${exactProgramName}]  [${multiprograms}] [${multiParticipantNames}] [${multiClientNames}]  
 [${orderBy}] [${range}]
) aclient
  
 -- Get program participation
 LEFT OUTER JOIN  program_participant pp ON aclient.uuid=pp.participant_uuid
 LEFT OUTER JOIN  program pg ON pp.program_uuid = pg.uuid 
 LEFT JOIN participant parent ON parent.participantName = aclient.parent 
 -- Get event status 
 -- if not manual control, event participant signals decides the event status
 LEFT OUTER JOIN 
 ( 
   --  all active or pendding events 
   SELECT evt.eventName, evt.programName,evt.startTime,evt.endTime, ep.uuid,ep.event_uuid, ep.participant_uuid,ep.eventOptOut
   FROM event_participant ep, event evt WHERE evt.uuid = ep.event_uuid 
   -- AND evt.issuedTime <= NOW() AND evt.endTime>= NOW()  
  ) ep
 ON aclient.uuid = ep.participant_uuid AND aclient.manualControl = 0 and ep.programName=pp.programName

 LEFT OUTER JOIN 
 (
  -- get active signal 
  SELECT epse.uuid, eps.event_participant_uuid,epse.stringValue, epse.numberValue,epse.time AS signalStartTime,s.signalName,s.levelSignal
  FROM   event_participant_signal eps, event_participant_signal_entry epse, signal_def s 
  WHERE  eps.uuid = epse.eventParticipantSignalUuid AND epse.time <= NOW() 
  AND    eps.signal_def_uuid = s.uuid AND s.signalName IN ('pending','mode')  
 ) s
 ON ep.uuid = s.event_participant_uuid 
  
-- if manual control, participant manual signal decides the event status
LEFT OUTER JOIN participant_manualsignal manual_signal ON aclient.uuid = manual_signal.participant_uuid 
AND aclient.manualControl =1 AND manual_signal.name IN ('pending','mode')

[${withcontacts} left outer join participant_contact pc on pc.participant_uuid = aclient.uuid]