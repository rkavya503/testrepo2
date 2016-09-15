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
-- DRMS-3001
--
grant all on jms_store.* to 'pss2'@'localhost';

--
-- DRMS-3220
-- NOTE: This should be deleted after DRMS-3220 is resolved
--
create database if not exists `jms_store` ;

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