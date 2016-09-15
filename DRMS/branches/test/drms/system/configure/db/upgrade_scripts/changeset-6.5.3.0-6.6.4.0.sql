--
-- DRMS-3681
--

 ALTER TABLE participant ADD COLUMN eventStatus VARCHAR(20) DEFAULT 'NONE';
 ALTER TABLE participant ADD COLUMN operationMode VARCHAR(20) DEFAULT 'UNKNOWN';

--
-- DRMS-3045 push client support
--

ALTER TABLE event_state ADD (push bit(1) NOT NULL DEFAULT b'0');

--
-- DRMS-3790 Remove properties from program
--

ALTER TABLE program DROP COLUMN testProgram;
ALTER TABLE program DROP COLUMN repeatingEvents;

-- DRMS-2708:Create core property to assign the baseline type for each utility
insert IGNORE into core_property
 	 	(uuid, version, propertyName, stringValue, booleanValue, doubleValue, textValue,
 	 	type, creator, modifier, creationTime, modifiedTime)
 	 	values (REPLACE(uuid(), '-', ''), 0, 'baseline.model', 'ThreeTen', null, null, null, 'String', 'script', null, now(), null);
 	 	
update core_property set stringValue='ThreeTen' where propertyName='baseline.model';

-- DRMS-3609:Usage: Active RTP event for client results in current usage line not appearing.
insert into core_property 
 (uuid, version, propertyName, stringValue, booleanValue, doubleValue, textValue, 
 type, creator, modifier, creationTime, modifiedTime) 
 values (REPLACE(uuid(), '-', ''), 0, 'excludedProgramsForEventLine', 'RTP Agricultural,RTP <2K,RTP 2K-50K,RTP >50K', null, null, null, 'String', 'script', null, now(), null);

