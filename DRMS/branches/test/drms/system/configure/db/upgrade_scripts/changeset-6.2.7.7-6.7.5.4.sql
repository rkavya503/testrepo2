--
-- update 6.2.7.7 to 6.4.0.0
--



--
-- DRMS-2560 make baseline calculation missing data threashold configurable.
--


insert into core_property 
 (uuid, version, propertyName, stringValue, booleanValue, doubleValue, textValue, 
 type, creator, modifier, creationTime, modifiedTime) 
 values (REPLACE(uuid(), '-', ''), 0, 'sce.ftp.host', '', null, null, null, 'String', 'script', null, now(), null);
 
insert into core_property 
 (uuid, version, propertyName, stringValue, booleanValue, doubleValue, textValue, 
 type, creator, modifier, creationTime, modifiedTime) 
 values (REPLACE(uuid(), '-', ''), 0, 'sce.ftp.port', '', null, null, null, 'String', 'script', null, now(), null);

insert into core_property 
 (uuid, version, propertyName, stringValue, booleanValue, doubleValue, textValue, 
 type, creator, modifier, creationTime, modifiedTime) 
 values (REPLACE(uuid(), '-', ''), 0, 'sce.ftp.username', '', null, null, null, 'String', 'script', null, now(), null);

insert into core_property 
 (uuid, version, propertyName, stringValue, booleanValue, doubleValue, textValue, 
 type, creator, modifier, creationTime, modifiedTime) 
 values (REPLACE(uuid(), '-', ''), 0, 'sce.ftp.password', '', null, null, null, 'String', 'script', null, now(), null);


insert into core_property 
 (uuid, version, propertyName, stringValue, booleanValue, doubleValue, textValue, 
 type, creator, modifier, creationTime, modifiedTime) 
 values (REPLACE(uuid(), '-', ''), 0, 'sce.participant.synchronization.active', null, 0, null, null, 'Boolean', 'script', null, now(), null);


insert into core_property 
 (uuid, version, propertyName, stringValue, booleanValue, doubleValue, textValue, 
 type, creator, modifier, creationTime, modifiedTime) 
 values (REPLACE(uuid(), '-', ''), 0, 'sce.participant.sync.filepath', '', null, null, null, 'String', 'script', null, now(), null);

insert into core_property 
 (uuid, version, propertyName, stringValue, booleanValue, doubleValue, textValue, 
 type, creator, modifier, creationTime, modifiedTime) 
 values (REPLACE(uuid(), '-', ''), 0, 'sce.participant.sync.filename', '', null, null, null, 'String', 'script', null, now(), null);

insert into core_property 
 (uuid, version, propertyName, stringValue, booleanValue, doubleValue, textValue, 
 type, creator, modifier, creationTime, modifiedTime) 
 values (REPLACE(uuid(), '-', ''), 0, 'sce.participant.sync.scan.duration(hour)', '', null, null, null, 'String', 'script', null, now(), null);

insert into core_property 
 (uuid, version, propertyName, stringValue, booleanValue, doubleValue, textValue, 
 type, creator, modifier, creationTime, modifiedTime) 
 values (REPLACE(uuid(), '-', ''), 0, 'sce.participant.sync.scan.time', '', null, null, null, 'String', 'script', null, now(), null);

insert into core_property 
 (uuid, version, propertyName, stringValue, booleanValue, doubleValue, textValue, 
 type, creator, modifier, creationTime, modifiedTime) 
 values (REPLACE(uuid(), '-', ''), 0, 'sce.participant.customer.test.event', null, 1, null, null, 'Boolean', 'script', null, now(), null);

/*--------------------------------------------------------------------------------*/
/* 103-core_property.sql */

--
-- DRMS-2433 add sce dbp event auto creation related core properties
--

INSERT INTO core_property
(uuid, propertyName, stringValue, booleanValue, doubleValue, textValue, 
 type, creator, modifier, creationTime, modifiedTime, version)
VALUES 
(REPLACE(uuid(), '-', ''),'feature.scedbpEventAutoCreation',NULL,0,NULL,NULL,'Boolean','script',NULL,'2010-12-13 09:31:37',NULL,0),
(REPLACE(uuid(), '-', ''),'scedbp.event.filepath','dbp',NULL,NULL,NULL,'String','script',NULL,'2010-12-13 09:31:37',NULL,0),
(REPLACE(uuid(), '-', ''),'scedbp.event.filename','DBPEvent',NULL,NULL,NULL,'String','script',NULL,'2010-12-13 09:31:37',NULL,0),
(REPLACE(uuid(), '-', ''),'scedbp.event.scanDuration','24',NULL,NULL,NULL,'String','script',NULL,'2010-12-13 09:31:37',NULL,0),
(REPLACE(uuid(), '-', ''),'scedbp.event.scanStartTime','16:45',NULL,NULL,NULL,'String','script',NULL,'2010-12-13 09:31:37',NULL,0);


/*--------------------------------------------------------------------------------*/
/* 200-dbp_event_creation.sql */

DROP TABLE IF EXISTS `dbp_event_creation`;
CREATE TABLE `dbp_event_creation` (
  `uuid` varchar(32) NOT NULL,
  `date` datetime NOT NULL,
  `status` bit(1) NOT NULL,
  `fileName` varchar(255) NULL,
  `description` text,
  `creationTime` datetime DEFAULT NULL,
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



/*--------------------------------------------------------------------------------*/
/* 300-event.sql */

-- DRMS-2836
ALTER TABLE `event` ADD COLUMN `nearTime` datetime default NULL AFTER `receivedTime`;


/*--------------------------------------------------------------------------------*/
/* 950-baseline_config.sql */

UPDATE baseline_model SET dayPickerImplClass='com.akuacom.pss2.data.usage.calcimpl.EliminateHolidayDaysSelectorBean';

--
-- DRMS-2457 roll back the changeset of holiday eliminate calculation,since we will change baseline calculation base on program level in 6.4
--
UPDATE baseline_model SET dayPickerImplClass='com.akuacom.pss2.data.usage.calcimpl.DefaultDaysSelectorBean';

UPDATE core_property SET stringValue='firelog-3.2' where propertyName='firelog';


--
-- end update 6.2.7.7 to 6.4.0.0
--

--
-- update 6.4.0.0 to 6.4.3.2
--

--
-- DRMS-3127 Add "push" flag and IP address to facdash client config screen for ICCP project
--

ALTER TABLE participant ADD (push INT NULL DEFAULT 0);
ALTER TABLE participant ADD (ip VARCHAR(255) NULL );

--
-- DRMS-1094 an optOut field for program opt-out new feature.
--
ALTER TABLE participant ADD (optOut tinyint(1) NULL );
ALTER TABLE program_participant ADD (optStatus INT NULL DEFAULT 0);

-- DRMS-2407
update pss2.core_property set stringValue="" where propertyName='version';

-- DRMS-2406
delete from pss2.core_property where propertyName='usagedataWS.contextRoot';

--
-- DRMS-3220
-- NOTE: This should be deleted after DRMS-3220 is resolved
--
create database if not exists `jms_store` ;

-- 
-- DRMS-3001
--
grant all on jms_store.* to 'pss2'@'localhost';


--
-- DRMS-2979Develop UI
--

insert into core_property 
 (uuid, version, propertyName, stringValue, booleanValue, doubleValue, textValue, 
 type, creator, modifier, creationTime, modifiedTime) 
 values (REPLACE(uuid(), '-', ''), 0, 'simple.dashboard.pending.message', 'Event starts in {0} hour(s), {1} min(s)', null, null, null, 'String', 'script', null, now(), null);
 
insert into core_property 
 (uuid, version, propertyName, stringValue, booleanValue, doubleValue, textValue, 
 type, creator, modifier, creationTime, modifiedTime) 
 values (REPLACE(uuid(), '-', ''), 0, 'simple.dashboard.noevent.message', 'No active or pending events', null, null, null, 'String', 'script', null, now(), null);
 
insert into core_property 
 (uuid, version, propertyName, stringValue, booleanValue, doubleValue, textValue, 
 type, creator, modifier, creationTime, modifiedTime) 
 values (REPLACE(uuid(), '-', ''), 0, 'simple.dashboard.active.message', 'Active Event. Time remaining {0} hour(s), {1} min(s)', null, null, null, 'String', 'script', null, now(), null);
  
insert into core_property 
 (uuid, version, propertyName, stringValue, booleanValue, doubleValue, textValue, 
 type, creator, modifier, creationTime, modifiedTime) 
 values (REPLACE(uuid(), '-', ''), 0, 'simple.dashboard.link2', 'test', null, null, null, 'String', 'script', null, now(), null);
 
insert into core_property 
 (uuid, version, propertyName, stringValue, booleanValue, doubleValue, textValue, 
 type, creator, modifier, creationTime, modifiedTime) 
 values (REPLACE(uuid(), '-', ''), 0, 'simple.dashboard.link1', 'test', null, null, null, 'String', 'script', null, now(), null);
 
insert into core_property 
 (uuid, version, propertyName, stringValue, booleanValue, doubleValue, textValue, 
 type, creator, modifier, creationTime, modifiedTime) 
 values (REPLACE(uuid(), '-', ''), 0, 'simple.dashboard.usage', 'YY', null, null, null, 'String', 'script', null, now(), null);
 
insert into core_property 
 (uuid, version, propertyName, stringValue, booleanValue, doubleValue, textValue, 
 type, creator, modifier, creationTime, modifiedTime) 
 values (REPLACE(uuid(), '-', ''), 0, 'simple.dashboard.akualogo', null, '\0', null, null, 'Boolean', 'script', null, now(), null);
 
insert into core_property 
 (uuid, version, propertyName, stringValue, booleanValue, doubleValue, textValue, 
 type, creator, modifier, creationTime, modifiedTime) 
 values (REPLACE(uuid(), '-', ''), 0, 'simple.dashboard.rssfeed', null, '\0', null, null, 'Boolean', 'script', null, now(), null);
 
insert into core_property 
 (uuid, version, propertyName, stringValue, booleanValue, doubleValue, textValue, 
 type, creator, modifier, creationTime, modifiedTime) 
 values (REPLACE(uuid(), '-', ''), 0, 'simple.dashboard.optout', null, '\0', null, null, 'Boolean', 'script', null, now(), null);
 
insert into core_property 
 (uuid, version, propertyName, stringValue, booleanValue, doubleValue, textValue, 
 type, creator, modifier, creationTime, modifiedTime) 
 values (REPLACE(uuid(), '-', ''), 0, 'simple.dashboard.refreshinterval', null, null, 10000, null, 'Number', 'script', null, now(), null);

--
-- end update 6.4.0.0 to 6.4.3.2
--

--
-- update 6.4.3.2 to 6.5.3.0
--

update core_property set doubleValue="900000.0" where propertyName="simple.dashboard.refreshinterval";
--
-- DRMS-3220
-- NOTE: This should be deleted after DRMS-3220 is resolved
--
create database if not exists `jms_store` ;

-- 
-- DRMS-3001
--
GRANT ALL PRIVILEGES ON jms_store.* TO 'pss2'@'%' IDENTIFIED BY 'AKUAlogin';

--
-- DRMS-
--

ALTER TABLE program_participant ADD (name VARCHAR(64) NOT NULL);
ALTER TABLE program_participant ADD (value VARCHAR(64) NOT NULL);
ALTER TABLE program_participant ADD (ancestry VARCHAR(255));
ALTER TABLE program_participant ADD (parent_uuid VARCHAR(32)); 

--
-- DRMS-
--

ALTER TABLE event_participant ADD (name VARCHAR(64) NOT NULL);
ALTER TABLE event_participant ADD (value VARCHAR(64) NOT NULL);
ALTER TABLE event_participant ADD (ancestry VARCHAR(255));
ALTER TABLE event_participant ADD (parent_uuid VARCHAR(32));

--
-- DRMS-3316 msgThreshold must not be 0 for participants to receive emails.
--
update participant_contact set msgThreshold=(select msgThreshold from message_filter_setting limit 1);

--
-- DRMS-2979
--
insert into core_property 
 (uuid, version, propertyName, stringValue, booleanValue, doubleValue, textValue, type, creator, modifier, creationTime, modifiedTime) 
 values (REPLACE(uuid(), '-', ''), 0, 'simple.dashboard.link2name', 'HONEYWELL', null, null, null, 'String', 'script', null, now(), null);
 
insert into core_property 
 (uuid, version, propertyName, stringValue, booleanValue, doubleValue, textValue, type, creator, modifier, creationTime, modifiedTime) 
 values (REPLACE(uuid(), '-', ''), 0, 'simple.dashboard.link1name', 'AKUACOM', null, null, null, 'String', 'script', null, now(), null);
 
insert into core_property 
 (uuid, version, propertyName, stringValue, booleanValue, doubleValue, textValue, 
 type, creator, modifier, creationTime, modifiedTime) 
 values (REPLACE(uuid(), '-', ''), 0, 'simple.dashboard.linkenable', null, '\0', null, null, 'Boolean', 'script', null, now(), null);
 
 update core_property set stringValue="http://www.akuacom.com" where propertyName="simple.dashboard.link1";
 
 update core_property set stringValue="http://www.honeywell.com" where propertyName="simple.dashboard.link2";

--
-- DRMS-3526
--
update core_property set doubleValue="900000.0" where propertyName="simple.dashboard.refreshinterval";

--
-- end update 6.4.3.2 to 6.5.3.0
--

--
-- update 6.5.3.0 to 6.6.4.0
--

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

--
-- update 6.5.3.0 to 6.6.4.0
--

--
-- update 6.6.4.0 to 6.7.5.5
--

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


--
-- Note the DELIMITER statement here
--
DELIMITER $$
DROP FUNCTION IF EXISTS `getFormerTime`$$

CREATE FUNCTION `getFormerTime`(time1 DATETIME, time2 DATETIME) RETURNS DATETIME
DETERMINISTIC
BEGIN
	
	CASE WHEN
		(time1 <= time2)
	THEN
		RETURN time1;
	ELSE
		RETURN time2;
	END CASE;
	
    END$$


DROP FUNCTION IF EXISTS `getOlderTime`$$

CREATE FUNCTION `getOlderTime`(time1 DATETIME, time2 DATETIME) RETURNS DATETIME
DETERMINISTIC
BEGIN
	
	CASE WHEN
		(time1 >= time2)
	THEN
		RETURN time1;
	ELSE
		RETURN time2;
	END CASE;
	
    END$$

--
--
--

DROP PROCEDURE IF EXISTS `calculateShedForEventParticipant`$$

CREATE  PROCEDURE `calculateShedForEventParticipant`(param_year VARCHAR(4),param_month VARCHAR(2),param_day VARCHAR(2))
BEGIN
                DECLARE temp_eventName VARCHAR(32);
                DECLARE temp_start_time DATETIME;
                DECLARE temp_end_time DATETIME;       
                DECLARE temp_event_duration DOUBLE DEFAULT 0;
                DECLARE temp_partName VARCHAR(32); 
                DECLARE temp_uuid VARCHAR(32);				
                DECLARE temp_actual_avg DOUBLE;
                DECLARE temp_base_avg DOUBLE;
                DECLARE temp_shed_avg DOUBLE;                                      
                DECLARE no_row_fetched INT DEFAULT 0;    
				DECLARE loginfo VARCHAR(255); 
				DECLARE logdesc  TEXT;				
                
                DECLARE event_csr CURSOR FOR
                SELECT eventName, startTime, endTime, (TIME_TO_SEC(endtime)-TIME_TO_SEC(starttime) )/3600
                FROM history_event WHERE 
                YEAR(startTime)=param_year          
                AND MONTH(startTime)=param_month
                AND DAYOFMONTH(startTime)=param_day;    
                
                DECLARE event_part_csr CURSOR FOR
                SELECT UUID, participantName FROM 
                history_event_participant WHERE eventName= temp_eventName AND client=0;		
                
                DECLARE CONTINUE HANDLER FOR NOT FOUND SET no_row_fetched=1;
                      
				SET loginfo = CONCAT("TIME: ",param_year,param_month,param_day);
				SET logdesc =CONCAT("TIME: ",param_year,param_month,param_day);

                OPEN event_csr;                
                    event_loop: LOOP -- Loop through events
                                        
                    FETCH event_csr INTO temp_eventName, temp_start_time, temp_end_time, temp_event_duration;
                    
                    IF no_row_fetched=1 THEN
                    LEAVE event_loop;
                    END IF;
					
					SET logdesc = CONCAT(logdesc,"Event  name:", temp_eventName,"  event starttime: ",temp_start_time,"  event endtime: ",temp_end_time,"  event duration: ",temp_event_duration);
                    
                        OPEN event_part_csr;
                            event_part_loop: LOOP -- Loop through participant in event_participant by event.
                                -- **********************************************main block**************************************************
                                FETCH event_part_csr INTO temp_uuid,temp_partName;
                                IF no_row_fetched=1 THEN
                                LEAVE event_part_loop;
                                END IF;
								
								SET logdesc = CONCAT(logdesc,"[ participant name: ",temp_partName);
                                
                                SELECT AVG(VALUE) INTO temp_actual_avg FROM dataentry WHERE 
                                datasource_uuid=(SELECT UUID FROM datasource WHERE ownerID = temp_partName)
                                AND TIME BETWEEN temp_start_time AND temp_end_time;
                                
                                SELECT AVG(VALUE) INTO temp_base_avg FROM history_baseline_dataentry WHERE 
                                datasource_uuid=(SELECT UUID FROM datasource WHERE ownerID = temp_partName)
                                AND TIME BETWEEN temp_start_time AND temp_end_time;
                                
                                IF temp_base_avg =0 THEN SET temp_shed_avg = 0;
                                ELSE
                                SET temp_shed_avg = temp_base_avg-temp_actual_avg;
                                END IF;
                                
								SET logdesc =CONCAT( logdesc," Update participant averageShed: ",temp_shed_avg," total: ",temp_shed_avg*temp_event_duration ,"]");
								
                                UPDATE history_event_participant
                                SET averageShed=temp_shed_avg, totalShed=temp_shed_avg*temp_event_duration
                                WHERE UUID=temp_uuid;
                                
                                -- ************************************************************************************************
                            END LOOP; -- End of Loop through participant in event_participant by event.
                    CLOSE event_part_csr;
                    
                    SET no_row_fetched=0;
                       
                    END LOOP event_loop;-- End of Loop through events
             CLOSE event_csr;
			 
			INSERT INTO history_procedure_logentry 
			(uuid, logdate, loginfo, logdesc) 
			VALUES (REPLACE(uuid(), '-', ''), NOW(), loginfo, logdesc);
END $$

DELIMITER ;
