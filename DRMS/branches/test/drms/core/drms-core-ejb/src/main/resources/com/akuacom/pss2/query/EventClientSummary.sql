-- union of two parts 
-- part 1 : for auto -control clients, 
SELECT aclient.*,
  s.signalName, s.levelSignal, s.stringValue, s.numberValue, s.signalStartTime,
  null AS manualSignalName, null AS manualSignalValue
FROM
(
  SELECT DISTINCT p.uuid, p.participantName AS clientName,p.parent AS parentName,
  p.type,p.deviceType,p.status AS commsStatus,p.operationMode AS lastMode,p.eventStatus AS lastStatus, p.commTime AS lastContact,
  p.manualControl,e.startTime,ep.uuid AS event_participant_uuid
  FROM participant p, event_participant ep, event e
  WHERE p.uuid = ep.participant_uuid  AND ep.event_uuid = e.uuid AND p.client = 1 AND ep.eventOptOut = 0
  -- auto control clients 
  AND p.manualControl = 0
  [AND e.eventName = ${eventName}] [${orderBy}] [${range}]
) aclient
 LEFT OUTER JOIN
 (
  -- get active signal
  SELECT epse.uuid, eps.event_participant_uuid,epse.stringValue, epse.numberValue,epse.time AS signalStartTime,s.signalName,s.levelSignal
  FROM   event evt,event_participant ep, event_participant_signal eps, event_participant_signal_entry epse, signal_def s
  WHERE  [evt.eventName=${eventName} AND ] evt.uuid = ep.event_uuid AND ep.uuid = eps.event_participant_uuid and eps.uuid = epse.eventParticipantSignalUuid AND epse.time <= NOW()
  AND    eps.signal_def_uuid = s.uuid AND s.signalName IN ('pending','mode')
 ) s
 ON aclient.event_participant_uuid = s.event_participant_uuid

 UNION 
 -- part II manual control, participant manual signal decides the event status
select aclient.*,null,null,null,null,null,
manual_signal.name AS manualSignalName, manual_signal.value AS manualSignalValue
FROM
(
  SELECT DISTINCT p.uuid, p.participantName AS clientName,p.parent AS parentName,
  p.type,p.deviceType,p.status AS commsStatus,p.operationMode AS lastMode,p.eventStatus AS lastStatus, p.commTime AS lastContact,
  p.manualControl,e.startTime,ep.uuid AS event_participant_uuid
  FROM participant p, event_participant ep, event e
  WHERE p.uuid = ep.participant_uuid  AND ep.event_uuid = e.uuid AND p.client = 1 AND ep.eventOptOut = 0
  -- manual control clients 
  AND p.manualControl = 1
  [AND e.eventName = ${eventName}] [${orderBy}] [${range}]
) aclient

-- if manual control, participant manual signal decides the event status
LEFT OUTER JOIN participant_manualsignal manual_signal ON aclient.uuid = manual_signal.participant_uuid
AND aclient.manualControl =1 AND manual_signal.name IN ('pending','mode')