

-- ******************************************************
-- START OF SCHEMA CHANGES MERGED FROM 6.2.4.0 through 6.2.5.12.
-- ******************************************************

-- ********************************
-- Changeset 6.2.3.23 to 6.2.4.4
-- ********************************

-- ---------------------------
-- DRMS-4689 remove db_version
-- ---------------------------
-- increate length of column so that it doesn't fail on windows machines.
ALTER TABLE db_version CHANGE COLUMN upgradeScriptFilename upgradeScriptFilename VARCHAR(255); 

-- ---------------------------
-- DRMS-3579: Add two attribute to program: program name and program class 
-- ---------------------------
/* already in 6.7 */
-- ALTER TABLE program ADD (longProgramName varchar(255) NULL DEFAULT NULL);
-- ALTER TABLE program ADD (programClass varchar(255) NULL DEFAULT NULL);

-- ---------------------------
-- DRMS-3316 Fix msgThreshold;
-- ---------------------------
/* ok to merge from 6.2.5, no associated code */
alter table participant_contact ALTER msgThreshold set DEFAULT 10;

-- ********************************
-- Changesets 6.2.4.7 to 6.2.5.9 -1
-- ********************************

-- ---------------------------
-- DRMS-3501 change existing noDupRecKey to UNIQUE
-- ---------------------------
/* THIS IS NOT NEEDED, THE FOREIGN KEY IS ALREADY IN THE CORRECT FORMAT */

 /*
DROP PROCEDURE IF EXISTS `DropFK` |

CREATE PROCEDURE `DropFK`()
BEGIN
IF EXISTS (SELECT NULL FROM information_schema.TABLE_CONSTRAINTS
WHERE CONSTRAINT_SCHEMA = DATABASE() AND CONSTRAINT_NAME = 'FK_dataset') THEN
    ALTER TABLE `dataentry` DROP FOREIGN KEY `FK_dataset`
END IF
END
|

call DropFK()|
ALTER TABLE `dataentry` ADD CONSTRAINT `FK_dataset` FOREIGN KEY (`dataset_uuid`) REFERENCES `dataset` (`uuid`)|

DROP PROCEDURE IF EXISTS `DropFK` |
 */

-- ********************************
-- Changesets 6.2.4.7 to 6.2.5.9 -2
-- ********************************

-- ---------------------------
-- DRMS-3501
-- ---------------------------
/* not going to hurt anything if this is added now, just make sure the 
   code gets merged */
DROP TABLE IF EXISTS `Task`;
CREATE TABLE `Task` (
  `uuid` char(32) NOT NULL,
  `creationTime` datetime DEFAULT NULL,
  `version` int(11) NOT NULL,
  `modifiedTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `creator` varchar(255) DEFAULT NULL,
  `modifier` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `value` varchar(255) DEFAULT NULL,
  `ancestry` varchar(320) DEFAULT NULL,
  `startMillis` bigint(20) NOT NULL,
  `stopMillis` bigint(20) NOT NULL,
  `lastTrigger` bigint(20) NOT NULL,
  `period` bigint(20) NOT NULL,
  `type` int(11) DEFAULT NULL,
  `parent_uuid` char(32) DEFAULT NULL,
  PRIMARY KEY (`uuid`),
  UNIQUE KEY `name` (`name`,`type`),
  KEY `FK27A9A590799D78` (`parent_uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ---------------------------
-- DRMS-3501 change existing noDupRecKey to UNIQUE
-- ---------------------------
/* this index is already in the correct format. */
/*
DROP INDEX `noDupRecKey` on `dataentry`
ALTER TABLE `dataentry` ADD UNIQUE INDEX `noDupRecKey`(`dataset_uuid`, `datasource_uuid`, `time`)
*/

-- ---------------------------
-- DRMS-3459
-- ---------------------------
/* can these be merged now? Is there code that must go along with this change? */
ALTER TABLE baseline_config ADD COLUMN `excludeEventDay` TINYINT(1) DEFAULT '1' NULL AFTER `description`,
CHANGE `excludeHoliday` `excludeHoliday` TINYINT(1) DEFAULT '1' NULL ;

ALTER TABLE baseline_config ADD COLUMN `eventEndTime` INT NULL DEFAULT 72000000 AFTER `description` , 
ADD COLUMN `eventStartTime` INT NULL DEFAULT 43200000 AFTER `description` , 
ADD COLUMN `maEndTime` INT NULL DEFAULT 39600000 AFTER `eventEndTime` , 
ADD COLUMN `maStartTime` INT NULL DEFAULT 28800000 AFTER `eventEndTime` , 
ADD COLUMN `maxMARate` FLOAT NULL DEFAULT 0.0 AFTER `maEndTime` , 
ADD COLUMN `minMARate` FLOAT NULL DEFAULT 0.0 AFTER `maEndTime` ;

-- ---------------------------
-- No JIRA
-- ---------------------------
/* This is no longer needed because the noDupRecKey exists */
/*
--  remove duplicate if they exist in preparation of the noDupRecKey unique index. This check was introduced in 6.2.5.5 and backported to 6.2.4.4.
create table dataentry1 as select * from dataentry where 1 group by `dataset_uuid`, `datasource_uuid`, `time`, `actual`
drop table dataentry
rename table dataentry1 to dataentry
*/

-- ---------------------------
-- DRMS-4130
-- ---------------------------
DROP TABLE IF EXISTS `drwebsite_property`;

CREATE TABLE `drwebsite_property` (
  `uuid` varchar(32) NOT NULL,
  `version` int(11) default NULL,
  `propertyName` varchar(255) NOT NULL,
  `stringValue` varchar(255) default NULL,
  `textValue` text default NULL,
  `creator` varchar(255) default NULL,
  `modifier` varchar(255) default NULL,
  `creationTime` datetime default NULL,
  `modifiedTime` datetime default NULL,
  PRIMARY KEY  (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


--
-- DRMS-4128 DR website API development
--
/* Already in the baseline, no need to merge */
/*
ALTER TABLE report_event ADD (cancelled bit(1) NOT NULL DEFAULT b'0')
*/
-- ********************************
-- Changesets 6.2.5.9 to 6.2.5.11
-- ********************************

/*
none
*/


-- ******************************************************
-- START OF DATA CHANGES MERGED FROM 6.2.4.0 through 6.2.5.12.
-- ******************************************************                                                                             

-- ********************************
-- Changeset 6.2.3.23 to 6.2.4.4
-- ********************************


insert into pss2.core_property
  (uuid, version, propertyName, stringValue, booleanValue, doubleValue, textValue,
  type, creator, modifier, creationTime, modifiedTime)
  values (REPLACE(uuid(), '-', ''), 0, 'emailFromAddress', 'noreply@openadr.com', null, null, null, 'String', 'script', null, now(), null);

--
-- DRMS-3563 usage data url fixes
--
/* These was already applied to the baseline */
-- delete from pss2.core_property where propertyName='usagedataWS.contextRoot';
-- update pss2.core_property set stringValue="" where propertyName='version';

--
-- DRMS-3316 Fix msgThreshold;
--
/* not needed, once applied to existing customers, it shouldn't be applied again. Applied in 6.2 */
-- update participant_contact set msgThreshold=(select msgThreshold from message_filter_setting limit 1);

--
-- DRMS-3767
-- 
insert IGNORE into core_property
 (uuid, version, propertyName, stringValue, booleanValue, doubleValue, textValue,
 type, creator, modifier, creationTime, modifiedTime)
 values (REPLACE(uuid(), '-', ''), 0, 'baseline.model', 'ThreeTen', null, null, null, 'String', 'script', null, now(), null);

--
-- DRMS-3767
--
/* not needed, once applied to existing customers, it shouldn't be applied again. Applied in 6.2 */
-- update core_property set stringValue='ThreeTen' where propertyName='baseline.model';

--
-- DRMS-3776
-- 
/* not needed, once applied to existing customers, it shouldn't be applied again. Applied in 6.2 */
-- update core_property set stringValue='clients' where propertyName='feature.facdashDefaultPage';


-- ********************************
-- Changesets 6.2.4.7 to 6.2.5.9 -1
-- ********************************

/*
none
*/

-- ********************************
-- Changesets 6.2.4.7 to 6.2.5.9 -2
-- ********************************

--
-- DRMS-3741 enable the core.property feature.reportEventList to true by default for all customers
--
update core_property set booleanValue = true where propertyName = 'feature.reportEventList';

-- DRMS-3502
--
insert into core_property 
   (uuid, version, propertyName, stringValue, booleanValue, doubleValue, textValue, 
   type, creator, modifier, creationTime, modifiedTime) 
  values (REPLACE(uuid(), '-', ''), 0, 'excludedProgramsForEventLine', 'RTP Agricultural,RTP <2K,RTP 2K-50K,RTP >50K', null, null, null, 'String', 'script', null, now(), null);

--
-- DRMS-3459
--
/* not needed, once applied to existing customers, it shouldn't be applied again. Applied in 6.2 */
-- UPDATE baseline_config SET excludeHoliday=1;

--
-- DRMS-3508
--

 insert into core_property 
 (uuid, version, propertyName, stringValue, booleanValue, doubleValue, textValue, 
 type, creator, modifier, creationTime, modifiedTime) 
 values (REPLACE(uuid(), '-', ''), 0, 'excludedProgramsForBaseline', 'DEMO', null, null, null, 'String', 'script', null, now(), null);
 
 insert into core_property 
 (uuid, version, propertyName, stringValue, booleanValue, doubleValue, textValue, 
 type, creator, modifier, creationTime, modifiedTime) 
 values (REPLACE(uuid(), '-', ''), 0, 'holidays', '05/30/2011,07/04/2011,09/05/2011', null, null, null, 'String', 'script', null, now(), null);


-- DRMS-4202 integrate firelogger
/*can't merge this until firelogger gets updated. */
-- DRMS-4703
-- update core_property set stringValue = 'firelog-4.0.0.0' where propertyName = 'firelog';

-- 
-- DRMS-4104
--

/* not needed, once applied to existing customers, it shouldn't be applied again. Applied in 6.2 */
-- update program set utilityProgramName = 'DBP - Auto Day-of WG2 Single' where name = 'DBP DO Single' and (select stringValue from core_property where propertyName = 'utilityName') = 'pge';
-- update program set utilityProgramName = 'DBP - Auto Day-of WG2 Aggregate' where name = 'DBP DO Aggregate' and (select stringValue from core_property where propertyName = 'utilityName') = 'pge';
-- update program set utilityProgramName = 'DBP - Auto Day-Ahead WG2 Single' where name = 'DBP DA Single' and (select stringValue from core_property where propertyName = 'utilityName') = 'pge';
-- update program set utilityProgramName = 'DBP - Auto Day-Ahead WG2 Aggregate' where name = 'DBP DA Aggregate' and (select stringValue from core_property where propertyName = 'utilityName') = 'pge';

-- DRMS-4275
/* not needed, once applied to existing customers, it shouldn't be applied again. Applied in 6.2 */
-- insert into core_property 
-- (uuid, version, propertyName, stringValue, booleanValue, doubleValue, textValue, 
-- type, creator, modifier, creationTime, modifiedTime) 
-- values (REPLACE(uuid(), '-', ''), 0, 'baseline.missingDataThreshold', null, null, 0.3, null, 'Number', 'script', null, now(), null);


-- DRMS-4130
insert into core_property
(uuid, version, propertyName, stringValue, booleanValue, doubleValue, textValue,
type, creator, modifier, creationTime, modifiedTime)
values (REPLACE(uuid(), '-', ''), 0, 'feature.storebackEnable', null, false, null, null, 'Boolean', 'script', null, now(), null);

-- DRMS-4130

insert into drwebsite_property 
 (uuid, version, propertyName, stringValue,textValue, 
 creator, modifier, creationTime, modifiedTime) 
 values (REPLACE(uuid(), '-', ''),null,'COPYRIGHT_RTP', null, '*Southern California Edison may apply the RTP rates that correspond to the above Downtown Los Angeles temperatures (or from Long Beach Airport if high temperatures are unavailable from Downtown Los Angeles) from the National Weather Service at the end of the day. As an RTP-2 or PA-RTP participating customer, you are responsible for knowing the temperature in Downtown Los Angeles and the accompanying rates that will be applied the following day. SCE is not responsible for any loss or damages to you and your business for inaccurate or changed information and provides this notification as a courtesy.', 'script', null, now(), null);

 
 insert into drwebsite_property 
 (uuid, version, propertyName, stringValue,textValue, 
 creator, modifier, creationTime, modifiedTime) 
 values (REPLACE(uuid(), '-', ''),null,'COPYRIGHT_SCE', null, 'Copyright @ 2010 Southern California Edison. All Rights Reserved', 'script', null, now(), null);

 insert into drwebsite_property 
 (uuid, version, propertyName, stringValue,textValue, 
 creator, modifier, creationTime, modifiedTime) 
 values (REPLACE(uuid(), '-', ''),null,'COPYRIGHT_ACTIVE_EVENTS_TITLE', null, 'Demand Response events occur when demand exceeds supply, electricity costs are high, or the state\'s electrical system is constrained. Here you can review current, scheduled, and past events.', 'script', null, now(), null);

 insert into drwebsite_property 
 (uuid, version, propertyName, stringValue,textValue, 
 creator, modifier, creationTime, modifiedTime) 
 values (REPLACE(uuid(), '-', ''),null,'COPYRIGHT_HISTORY_EVENTS_TITLE', null, 'Demand Response events occur when demand exceeds supply, electricity costs are high, or the state\'s electrical system is constrained. Here you can review current, scheduled, and past events.', 'script', null, now(), null);

 
insert into drwebsite_property 
 (uuid, version, propertyName, stringValue,textValue, 
 creator, modifier, creationTime, modifiedTime) 
 values (REPLACE(uuid(), '-', ''),null,'COPYRIGHT_ACTIVE_EVENTS_DBP', null, 'For this program, scheduled events are listed after 4 p.m. the day before they occur.', 'script', null, now(), null);

 insert into drwebsite_property 
 (uuid, version, propertyName, stringValue,textValue, 
 creator, modifier, creationTime, modifiedTime) 
 values (REPLACE(uuid(), '-', ''),null,'COPYRIGHT_HISTORY_EVENTS_SEARCH_TITLE', null, 'Search and export event histories by program.', 'script', null, now(), null);

insert into drwebsite_property 
(uuid, version, propertyName, stringValue,textValue, 
creator, modifier, creationTime, modifiedTime) 
values (REPLACE(uuid(), '-', ''),null,'TITLE_EVENT_STATUS_PAGE', null, 'Price Response Event Status', 'script', null, now(), null);

insert into drwebsite_property 
(uuid, version, propertyName, stringValue,textValue, 
creator, modifier, creationTime, modifiedTime) 
values (REPLACE(uuid(), '-', ''),null,'TITLE_EVENT_HISTORY_PAGE', null, 'Event History', 'script', null, now(), null);

insert into drwebsite_property 
(uuid, version, propertyName, stringValue,textValue, 
creator, modifier, creationTime, modifiedTime) 
values (REPLACE(uuid(), '-', ''),null,'TITLE_EVENT_STATUS_RTP', null, 'Forecasted Applicable Temperature:', 'script', null, now(), null);

insert into drwebsite_property 
(uuid, version, propertyName, stringValue,textValue, 
creator, modifier, creationTime, modifiedTime) 
values (REPLACE(uuid(), '-', ''),null,'CONTACT_NO', null, '1-800-555-55555', 'script', null, now(), null);

insert into drwebsite_property 
(uuid, version, propertyName, stringValue,textValue, 
creator, modifier, creationTime, modifiedTime) 
values (REPLACE(uuid(), '-', ''),null,'TBLCOL_PROGRAM_NAME', null, 'Program Name', 'script', null, now(), null);

insert into drwebsite_property 
(uuid, version, propertyName, stringValue,textValue, 
creator, modifier, creationTime, modifiedTime) 
values (REPLACE(uuid(), '-', ''),null,'TBLCOL_REGIONS', null, 'Regions Affected', 'script', null, now(), null);

insert into drwebsite_property 
(uuid, version, propertyName, stringValue,textValue, 
creator, modifier, creationTime, modifiedTime) 
values (REPLACE(uuid(), '-', ''),null,'TBLCOL_DATE', null, 'Date', 'script', null, now(), null);

insert into drwebsite_property 
(uuid, version, propertyName, stringValue,textValue, 
creator, modifier, creationTime, modifiedTime) 
values (REPLACE(uuid(), '-', ''),null,'TBLCOL_START_TIME', null, 'Start Time', 'script', null, now(), null);

insert into drwebsite_property 
(uuid, version, propertyName, stringValue,textValue, 
creator, modifier, creationTime, modifiedTime) 
values (REPLACE(uuid(), '-', ''),null,'TBLCOL_END_TIME', null, 'End Time', 'script', null, now(), null);

insert into drwebsite_property 
(uuid, version, propertyName, stringValue,textValue, 
creator, modifier, creationTime, modifiedTime) 
values (REPLACE(uuid(), '-', ''),null,'TBLCOL_TEMPERATURE', null, 'Temperature', 'script', null, now(), null);

insert into drwebsite_property 
(uuid, version, propertyName, stringValue,textValue, 
creator, modifier, creationTime, modifiedTime) 
values (REPLACE(uuid(), '-', ''),null,'TBLCOL_PRICE_CATEGORY', null, 'Pricing Category', 'script', null, now(), null);

--
-- DRMS-4128 DR website API development
--

insert into core_property 
 (uuid, version, propertyName, stringValue, booleanValue, doubleValue, textValue, 
 `type`, creator, modifier, creationTime, modifiedTime) 
 values (REPLACE(uuid(), '-', ''), 0, 'drwebsite.enabledDRC', null,false, null, null, 'String', 'script', null, now(), null);

--
-- DRMS-4329 Core Properties: Not able to Save
--
UPDATE core_property SET `type`='Boolean' WHERE propertyname='drwebsite.enabledDRC';

--
-- DRMS-4337
--
 insert into core_property
 (uuid, version, propertyName, stringValue, booleanValue, doubleValue, textValue,
 type, creator, modifier, creationTime, modifiedTime)
 values (REPLACE(uuid(), '-', ''), 0, 'disableUsageDataInterpolation', null, true, null, null, 'Boolean', 'script', null, now(), null);

-- ********************************
-- Changesets 6.2.5.9 to 6.2.5.11
-- ********************************

UPDATE drwebsite_property SET textValue = 'Demand Response Event Status' WHERE propertyName = 'TITLE_EVENT_STATUS_PAGE';
UPDATE drwebsite_property SET textValue = 'Welcome to the SCE DR Event Website.  It is recommended that you reload or refresh this site often to ensure you are viewing the most recent available information. This website is not intended as a substitute for receiving SCE event notifications.' WHERE propertyName = 'COPYRIGHT_ACTIVE_EVENTS_TITLE';
UPDATE drwebsite_property SET textValue = 'Search for event history by program and date range.  Past events do not indicate nor guarantee future event activity.' WHERE propertyName = 'COPYRIGHT_HISTORY_EVENTS_TITLE';
UPDATE drwebsite_property SET textValue = 'Based on Applicable Temperature of:' WHERE propertyName = 'TITLE_EVENT_STATUS_RTP';
UPDATE drwebsite_property SET textValue = '*Actual billed prices may vary based on updated temperature information.Refer to the RTP tariff at <a href="http://www.sce.com/tariffbooks">www.sce.com/tariffbooks</a> for current rate schedules.SCE is not responsible for any loss or damages resulting from inaccurate or changed information.' WHERE propertyName = 'COPYRIGHT_RTP' ;

UPDATE drwebsite_property SET textValue = '1-866-334-7827' WHERE propertyName = 'CONTACT_NO';
UPDATE drwebsite_property SET textValue = 'Applicable <br>Temperature'WHERE propertyName = 'TBLCOL_TEMPERATURE';
UPDATE drwebsite_property SET textValue = 'Expected Pricing Category*' WHERE propertyName = 'TBLCOL_PRICE_CATEGORY';
UPDATE drwebsite_property SET textValue = 'DBP Events scheduled for tomorrow will not appear until after 6PM today.' WHERE propertyName = 'COPYRIGHT_ACTIVE_EVENTS_DBP';

INSERT INTO `drwebsite_property` VALUES ('cfbff33475da11e098f8cce30e9adb7c',NULL,'COPYRIGHT_DRC',NULL,'Contact your DRC Aggregator for more information on DRC events.  <br><br>For more information on SCE\'s DRC program, please visit <a href="http://www.sce.com/drc">www.sce.com/drc</a>.','script',NULL,'2011-05-03 16:12:26',NULL);
INSERT INTO `drwebsite_property` VALUES ('cfbff4w475da11e098f8cce30e9adb7c',NULL,'TBLCOL_Date_Of_Usage',NULL,'Date of Usage','script',NULL,'2011-05-03 16:12:26',NULL);
INSERT INTO `drwebsite_property` VALUES ('cfbffbe475da11e098f8cce30e9adb7c',NULL,'RTPFORECAST_SUBTITLE',NULL,'5-Day Pricing Category Forecast','script',NULL,'2011-05-03 16:12:26',NULL);
INSERT INTO `drwebsite_property` VALUES ('cfbff9e475da11e098f8cce30e9adb7c',NULL,'COPYRIGHT_RTPFORECAST',NULL,'*SCE considers Expected Pricing Categories to be forward-looking statements, which are subject to risk and uncertainty; actual billed prices will be based on actual recorded high temperatures.   SCE is not responsible for any loss or damages resulting from inaccurate or changed information.  Refer to the RTP tariff at <a href="http://www.sce.com/tariffbooks">www.sce.com/tariffbooks</a> for current rate schedules.','script',NULL,'2011-05-03 16:12:26',NULL);
INSERT INTO `drwebsite_property` VALUES ('cfbffwe475da11e098f8cce30e9adb7c',NULL,'COPYRIGHT_HISTORY_RTP_SEARCH',NULL,'*Historical data is provided as a reference only and is not intended to be a promise or guarantee of future activity or pricing.','script',NULL,'2011-05-03 16:12:26',NULL);
INSERT INTO `drwebsite_property` VALUES ('cfbff4w475da11e092f8cce30e9adb7c',NULL,'TBLCOL_PRICE_CATEGORY_HISTORY',NULL,'Pricing Category*','script',NULL,'2011-05-03 16:12:26',NULL);
