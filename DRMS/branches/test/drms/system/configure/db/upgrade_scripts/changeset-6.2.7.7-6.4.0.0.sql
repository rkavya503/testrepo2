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


