INSERT INTO event_dataentry(uuid,dataset_uuid,eventName,valueType,time,value,actual)
SELECT	 REPLACE(UUID(), '-', '') AS uuid,
	${datasetId} AS dataset_uuid, 
	${eventName} AS eventName,'double' AS valueType,
	entry.time AS time, SUM(entry.value) AS value, MIN(entry.actual) AS actual											
FROM ${entryTable} AS entry, datasource source,  																			
		(SELECT * FROM datasource_usage WHERE baseline_state=1) du,															
	    participant p ,event_participant ep, event e																		
WHERE e.eventName= ${eventName}																				
	AND ep.event_uuid = e.uuid																							
	AND p.uuid = ep.participant_uuid																					
	AND DATE(entry.time)= DATE(e.startTime)																																						
	AND source.ownerid = p.participantName  																			
	AND du.datasource_uuid = source.uuid																				
	AND DATE(du.date)= DATE(e.startTime)																																								
	AND entry.dataSource_uuid=source.uuid																			
GROUP BY entry.time