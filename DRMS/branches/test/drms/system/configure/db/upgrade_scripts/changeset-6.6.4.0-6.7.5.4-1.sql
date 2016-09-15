-- DRMS-3910 add new fields in participant table
--
ALTER table participant 
ADD installer tinyint(1) unsigned DEFAULT '0',
ADD userType varchar(20) DEFAULT 'ADVANCED';

-- DRMS-4146 add new feature for iir client push in core property
--
insert into core_property
 (uuid, version, propertyName, stringValue, booleanValue, doubleValue, textValue,
 type, creator, modifier, creationTime, modifiedTime)
 values (REPLACE(uuid(), '-', ''), 0, 'iir.client.push', null, '\0', null, null, 'Boolean', 'script', null, now(), null);

--     DRMS-4373 Programs: New core property to enable/disable the program priority reorder feature
--
insert into core_property
 (uuid, version, propertyName, stringValue, booleanValue, doubleValue, textValue,
 type, creator, modifier, creationTime, modifiedTime)
 values (REPLACE(uuid(), '-', ''), 0, 'utility.program.sort', null, '\0', null, null, 'Boolean', 'script', null, now(), null);

-- DRMS-4224 add new fields in event_state table
--
ALTER table event_state
RENAME AS client_conversation_state,
CHANGE COLUMN  `eventStateId` `conversationStateId` int(10) unsigned NOT NULL,
ADD  `eventStatus` int DEFAULT '0',
ADD  `operationModeValue` int DEFAULT '0';

-- DRMS-3974: Customer report
--
DROP TABLE IF EXISTS `history_event`;
CREATE TABLE `history_event` (
  `uuid` varchar(32) NOT NULL,
  `program_uuid` varchar(32) NOT NULL,
  `programName` varchar(255) DEFAULT NULL,
  `eventName` varchar(64) NOT NULL,
  `startTime` datetime DEFAULT NULL,
  `endTime` datetime DEFAULT NULL,
  `scheduledStartTime` datetime DEFAULT NULL,
  `scheduledEndTime` datetime DEFAULT NULL,
  `cancelled` bit(1) NOT NULL DEFAULT b'0',
  `creationTime` datetime DEFAULT NULL,
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `history_event_participant`;
CREATE TABLE `history_event_participant` (
  `uuid` varchar(32) NOT NULL,
  `history_event_uuid` varchar(32) DEFAULT NULL,
  `eventName` varchar(64) NOT NULL,
  `participant_uuid` varchar(32) NOT NULL,
  `participantName` varchar(64) NOT NULL,
  `client` bit(1) NOT NULL,
  `participation` int NOT NULL,
  `percentage` double DEFAULT 0,
  `startTime` datetime DEFAULT NULL,
  `endTime` datetime DEFAULT NULL,
  `averageShed` double DEFAULT 0,
  `totalShed` double DEFAULT 0,
  `creationTime` datetime DEFAULT NULL,
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `history_event_participant_signal`;
CREATE TABLE `history_event_participant_signal` (
  `uuid` varchar(32) NOT NULL,
  `history_event_participant_uuid` varchar(32) DEFAULT NULL,
  `eventName` varchar(64) NOT NULL,
  `participant_uuid` varchar(32) NOT NULL,
  `participantName` varchar(64) NOT NULL,
  `signalName` varchar(255) NOT NULL,
  `signalTime` datetime NOT NULL,
  `signalValue` varchar(64) NOT NULL,
  `creationTime` datetime DEFAULT NULL,
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `client_status`;
CREATE TABLE `client_status` (
  `uuid` varchar(32) NOT NULL,
  `client_uuid` varchar(32) DEFAULT NULL,
  `status` bit(1) NOT NULL,
  `startTime` datetime DEFAULT NULL,
  `endTime` datetime DEFAULT NULL,
  `duration` int DEFAULT 0,
  `creationTime` datetime DEFAULT NULL,
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `history_baseline_dataentry`;

CREATE TABLE `history_baseline_dataentry` (
  `uuid` varchar(32) NOT NULL,
  `dataset_uuid` varchar(32) NOT NULL,
  `datasource_uuid` varchar(32) NOT NULL,
  `time` datetime NOT NULL,
  `value` double DEFAULT NULL,
  `creationTime` datetime DEFAULT NULL,
  `stringValue` varchar(128) DEFAULT NULL,
  `valueType` varchar(255) NOT NULL,
  `actual` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `history_procedure_logentry`;

CREATE  TABLE `history_procedure_logentry` (

  `uuid` VARCHAR(32) NOT NULL ,

  `logdate` DATETIME NULL ,

  `loginfo` VARCHAR(255) NULL ,

  `logdesc` TEXT NULL ,

  PRIMARY KEY (`uuid`) 
 )ENGINE=InnoDB DEFAULT CHARSET=latin1;


-- 3 new core properties for the following JIRAs

-- DRMS-4325 Set the Client to be automatically enrolled in the program rather than have the Installer enable the user
-- DRMS-4309 Aggregation should be controlled by a core property

insert into core_property 
 (uuid, version, propertyName, stringValue, booleanValue, doubleValue, textValue, 
 type, creator, modifier, creationTime, modifiedTime) 
 values (REPLACE(uuid(), '-', ''), 0, 'enableClientsAutoEnrollInProgram', null, '\0', null, null, 'Boolean', 'script', null, now(), null);
 
insert into core_property 
 (uuid, version, propertyName, stringValue, booleanValue, doubleValue, textValue, 
 type, creator, modifier, creationTime, modifiedTime) 
 values (REPLACE(uuid(), '-', ''), 0, 'enableAggregation', null, '\0', null, null, 'Boolean', 'script', null, now(), null);
 
-- DRMS-4206 Need location for Simplified Dashboard user 

insert into core_property 
 (uuid, version, propertyName, stringValue, booleanValue, doubleValue, textValue, 
 type, creator, modifier, creationTime, modifiedTime) 
 values (REPLACE(uuid(), '-', ''), 0, 'simple.dashboard.default.zipcode', '90001', null, null, null, 'String', 'script', null, now(), null);

-- DRMS-4515

insert into core_property 
 (uuid, version, propertyName, stringValue, booleanValue, doubleValue, textValue, 
 type, creator, modifier, creationTime, modifiedTime) 
 values (REPLACE(uuid(), '-', ''), 0, 'feature.clientOptOut', null, '\0', null, null, 'Boolean', 'script', null, now(), null);

