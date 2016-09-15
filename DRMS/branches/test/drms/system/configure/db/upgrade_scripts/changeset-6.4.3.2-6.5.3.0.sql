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
-- DRMS-3395 DR website API development
--

ALTER TABLE program ADD (longProgramName varchar(255) NULL DEFAULT NULL);
ALTER TABLE program ADD (programClass varchar(255) NULL DEFAULT NULL);

--
-- DRMS-3395 DR website API development
--

ALTER TABLE report_event ADD (cancelled bit(1) NOT NULL DEFAULT b'0');

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
 (uuid, version, propertyName, stringValue, booleanValue, doubleValue, textValue, 
 type, creator, modifier, creationTime, modifiedTime) 
 values (REPLACE(uuid(), '-', ''), 0, 'simple.dashboard.link2name', 'HONEYWELL', null, null, null, 'String', 'script', null, now(), null);
 
insert into core_property 
 type, creator, modifier, creationTime, modifiedTime) 
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

