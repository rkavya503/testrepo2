-- MySQL dump 10.13  Distrib 5.5.2-m2, for apple-darwin10.7.0 (i386)
--
-- Host: localhost    Database: pss2
-- ------------------------------------------------------
-- Server version	5.5.2-m2

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `DATABASECHANGELOG`
--

DROP TABLE IF EXISTS `DATABASECHANGELOG`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `DATABASECHANGELOG` (
  `ID` varchar(63) NOT NULL,
  `AUTHOR` varchar(63) NOT NULL,
  `FILENAME` varchar(200) NOT NULL,
  `DATEEXECUTED` datetime NOT NULL,
  `MD5SUM` varchar(35) DEFAULT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  `COMMENTS` varchar(255) DEFAULT NULL,
  `TAG` varchar(255) DEFAULT NULL,
  `LIQUIBASE` varchar(20) DEFAULT NULL,
  `ORDEREXECUTED` int(11) NOT NULL,
  `EXECTYPE` varchar(10) NOT NULL,
  PRIMARY KEY (`ID`,`AUTHOR`,`FILENAME`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `DATABASECHANGELOGLOCK`
--

DROP TABLE IF EXISTS `DATABASECHANGELOGLOCK`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `DATABASECHANGELOGLOCK` (
  `ID` int(11) NOT NULL,
  `LOCKED` tinyint(1) NOT NULL,
  `LOCKGRANTED` datetime DEFAULT NULL,
  `LOCKEDBY` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Task`
--

DROP TABLE IF EXISTS `Task`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Temporary table structure for view `all_program_participant`
--

DROP TABLE IF EXISTS `all_program_participant`;
/*!50001 DROP VIEW IF EXISTS `all_program_participant`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE TABLE `all_program_participant` (
  `uuid` varchar(36),
  `participantName` varchar(64),
  `client` bigint(20),
  `participant_uuid` varchar(32),
  `programName` varchar(255),
  `parent_uuid` varchar(32),
  `program_uuid` varchar(32),
  `ancestry` varchar(287)
) ENGINE=MyISAM */;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `baseline_config`
--

DROP TABLE IF EXISTS `baseline_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `baseline_config` (
  `uuid` varchar(32) NOT NULL,
  `baseline_model_uuid` varchar(32) NOT NULL,
  `owner_uuid` varchar(32) DEFAULT NULL,
  `name` varchar(256) NOT NULL,
  `excludeHolidayFromCalc` tinyint(1) DEFAULT '1',
  `excludedDaysOfWeekFromCalc` int(11) DEFAULT '1000001',
  `excludeHoliday` tinyint(1) DEFAULT '1',
  `excludedDaysOfWeek` int(11) DEFAULT '0',
  `excludeAbnormalDayImplClass` varchar(256) DEFAULT NULL,
  `description` varchar(256) DEFAULT NULL,
  `eventStartTime` int(11) DEFAULT '43200000',
  `eventEndTime` int(11) DEFAULT '72000000',
  `maStartTime` int(11) DEFAULT '28800000',
  `maEndTime` int(11) DEFAULT '39600000',
  `minMARate` float DEFAULT '0',
  `maxMARate` float DEFAULT '0',
  `excludeEventDay` tinyint(1) DEFAULT '1',
  `version` int(11) NOT NULL,
  `creator` varchar(255) DEFAULT NULL,
  `modifier` varchar(255) DEFAULT NULL,
  `creationTime` datetime DEFAULT NULL,
  `modifiedTime` datetime DEFAULT NULL,
  PRIMARY KEY (`uuid`),
  KEY `FK_baseline_model` (`baseline_model_uuid`),
  CONSTRAINT `FK_baseline_model` FOREIGN KEY (`baseline_model_uuid`) REFERENCES `baseline_model` (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `baseline_model`
--

DROP TABLE IF EXISTS `baseline_model`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `baseline_model` (
  `uuid` varchar(32) NOT NULL,
  `name` varchar(256) NOT NULL,
  `implClass` varchar(256) DEFAULT NULL,
  `dayPickerImplClass` varchar(256) DEFAULT NULL,
  `calcImplClass` varchar(256) DEFAULT NULL,
  `description` varchar(256) DEFAULT NULL,
  `version` int(11) NOT NULL,
  `creator` varchar(255) DEFAULT NULL,
  `modifier` varchar(255) DEFAULT NULL,
  `creationTime` datetime DEFAULT NULL,
  `modifiedTime` datetime DEFAULT NULL,
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `bid_block`
--

DROP TABLE IF EXISTS `bid_block`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `bid_block` (
  `uuid` varchar(32) NOT NULL,
  `version` int(11) NOT NULL,
  `startTimeM` int(11) DEFAULT NULL,
  `startTimeH` int(11) DEFAULT NULL,
  `endTimeM` int(11) DEFAULT NULL,
  `endTimeH` int(11) DEFAULT NULL,
  `creator` varchar(255) DEFAULT NULL,
  `modifier` varchar(255) DEFAULT NULL,
  `creationTime` datetime DEFAULT NULL,
  `modifiedTime` datetime DEFAULT NULL,
  `bid_config_uuid` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`uuid`),
  KEY `FKD7DBA6B1EC3B7970` (`bid_config_uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `bid_config`
--

DROP TABLE IF EXISTS `bid_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `bid_config` (
  `uuid` varchar(32) NOT NULL,
  `version` int(11) NOT NULL,
  `minBidKW` double DEFAULT NULL,
  `defaultBidKW` double DEFAULT NULL,
  `minConsectutiveBlocks` int(11) DEFAULT NULL,
  `respondByTimeH` int(11) DEFAULT NULL,
  `respondByTimeM` int(11) DEFAULT NULL,
  `drasRespondByPeriodM` int(11) DEFAULT NULL,
  `acceptTimeoutPeriodM` int(11) DEFAULT NULL,
  `drasBidding` bit(1) DEFAULT NULL,
  `creator` varchar(255) DEFAULT NULL,
  `modifier` varchar(255) DEFAULT NULL,
  `creationTime` datetime DEFAULT NULL,
  `modifiedTime` datetime DEFAULT NULL,
  `program_uuid` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `bid_level_mapping`
--

DROP TABLE IF EXISTS `bid_level_mapping`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `bid_level_mapping` (
  `uuid` varchar(32) NOT NULL,
  `creationTime` datetime DEFAULT NULL,
  `programName` varchar(255) NOT NULL,
  `participant_uuid` varchar(32) NOT NULL,
  `timeBlock` varchar(45) NOT NULL,
  `normal` varchar(45) NOT NULL,
  `moderate` varchar(45) NOT NULL,
  `high` varchar(45) NOT NULL,
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `client_conversation_state`
--

DROP TABLE IF EXISTS `client_conversation_state`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `client_conversation_state` (
  `uuid` varchar(32) NOT NULL,
  `creationTime` datetime DEFAULT NULL,
  `eventIdentifier` varchar(64) NOT NULL,
  `programName` varchar(255) NOT NULL,
  `drasClientId` varchar(64) DEFAULT NULL,
  `eventModNumber` int(10) unsigned NOT NULL DEFAULT '0',
  `conversationStateId` int(10) unsigned NOT NULL,
  `commTime` datetime NOT NULL,
  `push` bit(1) NOT NULL DEFAULT b'0',
  `eventStatus` int(11) DEFAULT '0',
  `operationModeValue` int(11) DEFAULT '0',
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `client_status`
--

DROP TABLE IF EXISTS `client_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `client_status` (
  `uuid` varchar(32) NOT NULL,
  `client_uuid` varchar(32) DEFAULT NULL,
  `status` bit(1) NOT NULL,
  `startTime` datetime DEFAULT NULL,
  `endTime` datetime DEFAULT NULL,
  `duration` int(11) DEFAULT '0',
  `creationTime` datetime DEFAULT NULL,
  `clientName` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `constraint_invalid_date`
--

DROP TABLE IF EXISTS `constraint_invalid_date`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `constraint_invalid_date` (
  `uuid` varchar(32) NOT NULL,
  `invalidDate` datetime NOT NULL,
  `version` int(11) NOT NULL,
  `creator` varchar(255) DEFAULT NULL,
  `modifier` varchar(255) DEFAULT NULL,
  `creationTime` datetime DEFAULT NULL,
  `modifiedTime` datetime DEFAULT NULL,
  `program_participant_constraint_uuid` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`uuid`),
  KEY `FKPPCID_TO_PPC` (`program_participant_constraint_uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `contact`
--

DROP TABLE IF EXISTS `contact`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `contact` (
  `uuid` varchar(32) NOT NULL,
  `version` int(11) NOT NULL,
  `firstName` varchar(255) DEFAULT NULL,
  `lastName` varchar(255) DEFAULT NULL,
  `supportType` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `commNotification` bit(1) DEFAULT NULL,
  `eventNotification` varchar(75) DEFAULT NULL,
  `offSeasonNotiHours` double DEFAULT NULL,
  `onSeasonNotiHours` double DEFAULT NULL,
  `creator` varchar(255) DEFAULT NULL,
  `creationTime` datetime DEFAULT NULL,
  `modifier` varchar(255) DEFAULT NULL,
  `modifiedTime` datetime DEFAULT NULL,
  `ownerID` varchar(64) DEFAULT NULL,
  `ownerType` varchar(64) DEFAULT NULL,
  `external` bit(1) DEFAULT NULL,
  `state` int(11) DEFAULT NULL,
  `priority` int(11) DEFAULT NULL,
  `defaultMsgThreshold` tinyint(1) DEFAULT '1',
  `msgThreshold` bigint(20) DEFAULT '0',
  PRIMARY KEY (`uuid`),
  KEY `FK16D1B63CFEA63EE0` (`ownerID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `contacts_offline_error`
--

DROP TABLE IF EXISTS `contacts_offline_error`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `contacts_offline_error` (
  `uuid` varchar(32) NOT NULL,
  `offlineError` tinyint(1) NOT NULL DEFAULT '0',
  `participant_uuid` varchar(32) NOT NULL,
  `programName` varchar(255) NOT NULL,
  `contactsUuid` varchar(32) NOT NULL,
  `version` int(11) DEFAULT NULL,
  `creator` varchar(255) DEFAULT NULL,
  `modifier` varchar(255) DEFAULT NULL,
  `creationTime` datetime DEFAULT NULL,
  `modifiedTime` datetime DEFAULT NULL,
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `core_property`
--

DROP TABLE IF EXISTS `core_property`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `core_property` (
  `uuid` varchar(32) NOT NULL,
  `propertyName` varchar(255) NOT NULL,
  `stringValue` varchar(5000) DEFAULT NULL,
  `booleanValue` bit(1) DEFAULT NULL,
  `doubleValue` double DEFAULT NULL,
  `textValue` text,
  `type` varchar(255) NOT NULL,
  `creator` varchar(255) DEFAULT NULL,
  `modifier` varchar(255) DEFAULT NULL,
  `creationTime` datetime DEFAULT NULL,
  `modifiedTime` datetime DEFAULT NULL,
  `version` int(11) NOT NULL,
  PRIMARY KEY (`uuid`),
  UNIQUE KEY `propertyName_UNIQUE` (`propertyName`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `custom_rule`
--

DROP TABLE IF EXISTS `custom_rule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `custom_rule` (
  `uuid` varchar(32) NOT NULL,
  `start` datetime DEFAULT NULL,
  `end` datetime DEFAULT NULL,
  `mode` varchar(60) DEFAULT NULL,
  `variable` varchar(60) DEFAULT NULL,
  `operator` varchar(60) DEFAULT NULL,
  `value` double DEFAULT NULL,
  `source` varchar(60) DEFAULT NULL,
  `sortOrder` int(11) DEFAULT NULL,
  `version` int(11) NOT NULL,
  `creator` varchar(255) DEFAULT NULL,
  `modifier` varchar(255) DEFAULT NULL,
  `creationTime` datetime DEFAULT NULL,
  `modifiedTime` datetime DEFAULT NULL,
  `program_participant_uuid` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`uuid`),
  KEY `FKPPR_TO_CUST_RULES` (`program_participant_uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `dataentry`
--

DROP TABLE IF EXISTS `dataentry`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dataentry` (
  `uuid` varchar(32) NOT NULL,
  `dataset_uuid` varchar(32) NOT NULL,
  `datasource_uuid` varchar(32) NOT NULL,
  `time` datetime NOT NULL,
  `value` double DEFAULT NULL,
  `creationTime` datetime DEFAULT NULL,
  `stringValue` varchar(128) DEFAULT NULL,
  `valueType` varchar(255) NOT NULL,
  `actual` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`uuid`),
  UNIQUE KEY `noDupRecKey` (`dataset_uuid`,`datasource_uuid`,`time`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `dataentry_temp`
--

DROP TABLE IF EXISTS `dataentry_temp`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dataentry_temp` (
  `uuid` varchar(32) NOT NULL,
  `dataset_uuid` varchar(32) NOT NULL,
  `datasource_uuid` varchar(32) NOT NULL,
  `time` datetime NOT NULL,
  `value` double DEFAULT NULL,
  `stringValue` varchar(128) DEFAULT NULL,
  `valueType` varchar(255) NOT NULL,
  `actual` tinyint(1) DEFAULT '1',
  `creationTime` datetime DEFAULT NULL,
  PRIMARY KEY (`uuid`),
  KEY `FK_dataset_temp` (`dataset_uuid`),
  KEY `FK_datasource_temp` (`datasource_uuid`),
  CONSTRAINT `FK_dataset_temp` FOREIGN KEY (`dataset_uuid`) REFERENCES `dataset` (`uuid`),
  CONSTRAINT `FK_datasource_temp` FOREIGN KEY (`datasource_uuid`) REFERENCES `datasource` (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `dataset`
--

DROP TABLE IF EXISTS `dataset`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dataset` (
  `uuid` varchar(32) NOT NULL,
  `version` int(11) DEFAULT NULL,
  `name` varchar(128) NOT NULL,
  `unit` varchar(45) NOT NULL,
  `sync` tinyint(1) NOT NULL,
  `period` bigint(20) unsigned NOT NULL,
  `creator` varchar(255) DEFAULT NULL,
  `modifier` varchar(255) DEFAULT NULL,
  `creationTime` datetime DEFAULT NULL,
  `modifiedTime` datetime DEFAULT NULL,
  `calcImplClass` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `datasource`
--

DROP TABLE IF EXISTS `datasource`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `datasource` (
  `uuid` varchar(32) NOT NULL,
  `ownerID` varchar(64) NOT NULL,
  `name` varchar(255) NOT NULL,
  `creationTime` datetime DEFAULT NULL,
  `enabled` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `dbp_event_creation`
--

DROP TABLE IF EXISTS `dbp_event_creation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dbp_event_creation` (
  `uuid` varchar(32) NOT NULL,
  `date` datetime NOT NULL,
  `status` bit(1) NOT NULL,
  `fileName` varchar(255) DEFAULT NULL,
  `description` text,
  `creationTime` datetime DEFAULT NULL,
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `dbpevent`
--

DROP TABLE IF EXISTS `dbpevent`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dbpevent` (
  `uuid` varchar(32) NOT NULL,
  `respondBy` datetime NOT NULL,
  `drasRespondBy` datetime NOT NULL,
  `currentBidState` varchar(45) NOT NULL,
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `drwebsite_property`
--

DROP TABLE IF EXISTS `drwebsite_property`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `drwebsite_property` (
  `uuid` varchar(32) NOT NULL,
  `version` int(11) DEFAULT NULL,
  `propertyName` varchar(255) NOT NULL,
  `stringValue` varchar(255) DEFAULT NULL,
  `textValue` text,
  `creator` varchar(255) DEFAULT NULL,
  `modifier` varchar(255) DEFAULT NULL,
  `creationTime` datetime DEFAULT NULL,
  `modifiedTime` datetime DEFAULT NULL,
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `event`
--

DROP TABLE IF EXISTS `event`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `event` (
  `uuid` varchar(32) NOT NULL,
  `eventName` varchar(64) NOT NULL,
  `programName` varchar(255) NOT NULL,
  `startTime` datetime NOT NULL,
  `endTime` datetime NOT NULL,
  `issuedTime` datetime NOT NULL,
  `receivedTime` datetime NOT NULL,
  `nearTime` datetime DEFAULT NULL,
  `manual` tinyint(1) NOT NULL,
  `status` varchar(45) NOT NULL,
  `creationTime` datetime DEFAULT NULL,
  PRIMARY KEY (`uuid`),
  UNIQUE KEY `evtUnqName` (`eventName`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `event_bid_block`
--

DROP TABLE IF EXISTS `event_bid_block`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `event_bid_block` (
  `UUID` varchar(255) NOT NULL,
  `creationTime` datetime DEFAULT NULL,
  `startTime` int(11) NOT NULL,
  `endTime` int(11) NOT NULL,
  `eventName` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`UUID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `event_participant`
--

DROP TABLE IF EXISTS `event_participant`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `event_participant` (
  `uuid` varchar(32) NOT NULL,
  `event_uuid` varchar(32) NOT NULL,
  `participant_uuid` varchar(32) NOT NULL,
  `bidState` varchar(45) DEFAULT NULL,
  `eventModNumber` int(10) unsigned NOT NULL DEFAULT '0',
  `version` int(11) NOT NULL,
  `creator` varchar(255) DEFAULT NULL,
  `modifier` varchar(255) DEFAULT NULL,
  `creationTime` datetime DEFAULT NULL,
  `modifiedTime` datetime DEFAULT NULL,
  `name` varchar(64) NOT NULL,
  `value` varchar(64) NOT NULL,
  `ancestry` varchar(255) DEFAULT NULL,
  `parent_uuid` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`uuid`),
  KEY `FK2_event_participant` (`event_uuid`),
  CONSTRAINT `FK2_event_participant` FOREIGN KEY (`event_uuid`) REFERENCES `event` (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `event_participant_bid_entry`
--

DROP TABLE IF EXISTS `event_participant_bid_entry`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `event_participant_bid_entry` (
  `uuid` varchar(32) NOT NULL,
  `creationTime` datetime NOT NULL,
  `startTime` datetime NOT NULL,
  `endTime` datetime NOT NULL,
  `reductionKW` double(22,0) NOT NULL,
  `priceLevel` double(22,0) NOT NULL,
  `active` bit(1) NOT NULL,
  `event_participant_uuid` varchar(32) NOT NULL,
  PRIMARY KEY (`uuid`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `event_participant_rule`
--

DROP TABLE IF EXISTS `event_participant_rule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `event_participant_rule` (
  `uuid` varchar(32) NOT NULL,
  `start` datetime DEFAULT NULL,
  `end` datetime DEFAULT NULL,
  `mode` varchar(60) DEFAULT NULL,
  `variable` varchar(60) DEFAULT NULL,
  `operator` varchar(60) DEFAULT NULL,
  `value` double DEFAULT NULL,
  `source` varchar(60) DEFAULT NULL,
  `sortOrder` int(11) DEFAULT NULL,
  `version` int(11) NOT NULL,
  `creator` varchar(255) DEFAULT NULL,
  `modifier` varchar(255) DEFAULT NULL,
  `creationTime` datetime DEFAULT NULL,
  `modifiedTime` datetime DEFAULT NULL,
  `event_participant_uuid` varchar(32) NOT NULL,
  PRIMARY KEY (`uuid`),
  KEY `FKEPR_TO_EP` (`event_participant_uuid`),
  CONSTRAINT `FKEPR_TO_EP` FOREIGN KEY (`event_participant_uuid`) REFERENCES `event_participant` (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `event_participant_signal`
--

DROP TABLE IF EXISTS `event_participant_signal`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `event_participant_signal` (
  `uuid` varchar(32) NOT NULL,
  `version` int(11) NOT NULL,
  `creator` varchar(255) DEFAULT NULL,
  `modifier` varchar(255) DEFAULT NULL,
  `creationTime` datetime DEFAULT NULL,
  `modifiedTime` datetime DEFAULT NULL,
  `event_participant_uuid` varchar(32) NOT NULL,
  `signal_def_uuid` varchar(32) NOT NULL,
  PRIMARY KEY (`uuid`),
  KEY `FK1_event_participant_signal` (`signal_def_uuid`),
  KEY `FK2_event_participant_signal` (`event_participant_uuid`),
  CONSTRAINT `FK2_event_participant_signal` FOREIGN KEY (`event_participant_uuid`) REFERENCES `event_participant` (`uuid`),
  CONSTRAINT `FK1_event_participant_signal` FOREIGN KEY (`signal_def_uuid`) REFERENCES `signal_def` (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `event_participant_signal_entry`
--

DROP TABLE IF EXISTS `event_participant_signal_entry`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `event_participant_signal_entry` (
  `uuid` varchar(32) NOT NULL,
  `eventParticipantSignalUuid` varchar(32) NOT NULL,
  `time` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `expired` tinyint(3) unsigned NOT NULL,
  `stringValue` varchar(45) DEFAULT NULL,
  `numberValue` double DEFAULT NULL,
  `version` int(11) NOT NULL,
  `creator` varchar(255) DEFAULT NULL,
  `modifier` varchar(255) DEFAULT NULL,
  `creationTime` datetime DEFAULT NULL,
  `modifiedTime` datetime DEFAULT NULL,
  `ruleId` int(11) DEFAULT NULL,
  PRIMARY KEY (`uuid`),
  KEY `FK1_event_participant_signal_entry` (`eventParticipantSignalUuid`),
  CONSTRAINT `FK1_event_participant_signal_entry` FOREIGN KEY (`eventParticipantSignalUuid`) REFERENCES `event_participant_signal` (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `event_signal`
--

DROP TABLE IF EXISTS `event_signal`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `event_signal` (
  `uuid` varchar(32) NOT NULL,
  `version` int(11) NOT NULL,
  `creator` varchar(255) DEFAULT NULL,
  `modifier` varchar(255) DEFAULT NULL,
  `creationTime` datetime DEFAULT NULL,
  `modifiedTime` datetime DEFAULT NULL,
  `event_uuid` varchar(64) NOT NULL,
  `signal_def_uuid` varchar(32) NOT NULL,
  PRIMARY KEY (`uuid`),
  KEY `FK1_event_signal` (`signal_def_uuid`),
  KEY `FK2_event_signal` (`event_uuid`),
  CONSTRAINT `FK2_event_signal` FOREIGN KEY (`event_uuid`) REFERENCES `event` (`uuid`),
  CONSTRAINT `FK1_event_signal` FOREIGN KEY (`signal_def_uuid`) REFERENCES `signal_def` (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `event_signal_entry`
--

DROP TABLE IF EXISTS `event_signal_entry`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `event_signal_entry` (
  `uuid` varchar(32) NOT NULL,
  `eventSignalUuid` varchar(32) NOT NULL,
  `time` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `expired` tinyint(3) unsigned NOT NULL,
  `stringValue` varchar(45) DEFAULT NULL,
  `numberValue` double DEFAULT NULL,
  `version` int(11) NOT NULL,
  `creator` varchar(255) DEFAULT NULL,
  `modifier` varchar(255) DEFAULT NULL,
  `creationTime` datetime DEFAULT NULL,
  `modifiedTime` datetime DEFAULT NULL,
  PRIMARY KEY (`uuid`),
  KEY `FK1_event_signal_entry_eventSignalUuid` (`eventSignalUuid`),
  CONSTRAINT `FK1_event_signal_entry_eventSignalUuid` FOREIGN KEY (`eventSignalUuid`) REFERENCES `event_signal` (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `event_template`
--

DROP TABLE IF EXISTS `event_template`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `event_template` (
  `uuid` varchar(32) NOT NULL,
  `version` int(11) NOT NULL,
  `name` varchar(64) NOT NULL,
  `programName` varchar(255) NOT NULL,
  `creator` varchar(255) DEFAULT NULL,
  `modifier` varchar(255) DEFAULT NULL,
  `creationTime` datetime DEFAULT NULL,
  `modifiedTime` datetime DEFAULT NULL,
  PRIMARY KEY (`uuid`),
  UNIQUE KEY `nameKey` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `event_template_signalentry`
--

DROP TABLE IF EXISTS `event_template_signalentry`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `event_template_signalentry` (
  `uuid` varchar(32) NOT NULL,
  `version` int(11) NOT NULL,
  `name` varchar(64) NOT NULL,
  `signalType` varchar(64) NOT NULL,
  `relativeStartTime` bigint(20) unsigned NOT NULL,
  `value` varchar(45) NOT NULL,
  `owner_id` varchar(64) DEFAULT NULL,
  `creator` varchar(255) DEFAULT NULL,
  `modifier` varchar(255) DEFAULT NULL,
  `creationTime` datetime DEFAULT NULL,
  `modifiedTime` datetime DEFAULT NULL,
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `event_timeblock`
--

DROP TABLE IF EXISTS `event_timeblock`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `event_timeblock` (
  `uuid` varchar(32) NOT NULL,
  `creationTime` datetime DEFAULT NULL,
  `eventName` varchar(64) NOT NULL,
  `startTime` int(10) unsigned NOT NULL,
  `endTime` int(10) unsigned NOT NULL,
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `history_baseline_dataentry`
--

DROP TABLE IF EXISTS `history_baseline_dataentry`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `history_event`
--

DROP TABLE IF EXISTS `history_event`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `history_event_participant`
--

DROP TABLE IF EXISTS `history_event_participant`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `history_event_participant` (
  `uuid` varchar(32) NOT NULL,
  `history_event_uuid` varchar(32) DEFAULT NULL,
  `eventName` varchar(64) NOT NULL,
  `participant_uuid` varchar(32) NOT NULL,
  `participantName` varchar(64) NOT NULL,
  `client` bit(1) NOT NULL,
  `participation` int(11) NOT NULL,
  `percentage` double DEFAULT '0',
  `startTime` datetime DEFAULT NULL,
  `endTime` datetime DEFAULT NULL,
  `averageShed` double DEFAULT '0',
  `totalShed` double DEFAULT '0',
  `creationTime` datetime DEFAULT NULL,
  `offlinePerEvent` double DEFAULT NULL,
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `history_event_participant_signal`
--

DROP TABLE IF EXISTS `history_event_participant_signal`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
  `signalEndTime` datetime DEFAULT NULL,
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `history_procedure_logentry`
--

DROP TABLE IF EXISTS `history_procedure_logentry`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `history_procedure_logentry` (
  `uuid` varchar(32) NOT NULL,
  `logdate` datetime DEFAULT NULL,
  `loginfo` varchar(255) DEFAULT NULL,
  `logdesc` text,
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `message`
--

DROP TABLE IF EXISTS `message`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `message` (
  `uuid` varchar(32) NOT NULL,
  `from` varchar(256) DEFAULT NULL,
  `to` text,
  `type` varchar(45) DEFAULT NULL,
  `subject` varchar(512) DEFAULT NULL,
  `content` text,
  `contentType` varchar(64) DEFAULT NULL,
  `userName` varchar(64) DEFAULT NULL,
  `programName` varchar(255) DEFAULT NULL,
  `eventName` varchar(64) DEFAULT NULL,
  `creationTime` datetime NOT NULL,
  `status` int(11) NOT NULL DEFAULT '0',
  `priority` int(11) NOT NULL DEFAULT '4',
  `sentTime` datetime DEFAULT NULL,
  `contactId` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `message_filter_setting`
--

DROP TABLE IF EXISTS `message_filter_setting`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `message_filter_setting` (
  `uuid` varchar(32) NOT NULL,
  `filterStatus` int(11) DEFAULT '0',
  `msgThreshold` int(11) DEFAULT '0',
  `frequency` int(11) DEFAULT '0',
  `duration` int(11) DEFAULT '0',
  `msgExpireTime` int(11) DEFAULT '0',
  `creationTime` datetime NOT NULL,
  `cleanMsgHour` int(11) DEFAULT '0',
  `cleanMsgMinute` int(11) DEFAULT '0',
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `noaa_weather`
--

DROP TABLE IF EXISTS `noaa_weather`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `noaa_weather` (
  `uuid` varchar(32) NOT NULL,
  `version` int(11) DEFAULT NULL,
  `creationTime` datetime DEFAULT NULL,
  `modifiedTime` datetime DEFAULT NULL,
  `creator` varchar(255) DEFAULT NULL,
  `modifier` varchar(255) DEFAULT NULL,
  `date` date NOT NULL,
  `reportingStation` varchar(8) DEFAULT NULL,
  `high` double DEFAULT NULL,
  `isFinal` tinyint(1) DEFAULT '0',
  `forecastHigh0` double NOT NULL,
  `forecastHigh1` double NOT NULL,
  `forecastHigh2` double NOT NULL,
  `forecastHigh3` double NOT NULL,
  `forecastHigh4` double NOT NULL,
  `forecastHigh5` double NOT NULL,
  PRIMARY KEY (`uuid`),
  KEY `weatherDateInx` (`date`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `participant`
--

DROP TABLE IF EXISTS `participant`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `participant` (
  `uuid` varchar(32) NOT NULL,
  `participantName` varchar(64) NOT NULL,
  `type` varchar(12) NOT NULL,
  `hostAddress` varchar(256) DEFAULT NULL,
  `manualControl` tinyint(1) NOT NULL,
  `manualControlExpires` datetime DEFAULT NULL,
  `account` varchar(128) NOT NULL,
  `firstName` varchar(45) DEFAULT NULL,
  `lastName` varchar(45) DEFAULT NULL,
  `meterName` varchar(128) DEFAULT NULL,
  `feedback` tinyint(1) NOT NULL,
  `meterId` varchar(128) DEFAULT NULL,
  `notificationLevel` int(10) unsigned DEFAULT NULL,
  `address` varchar(128) DEFAULT NULL,
  `gridLocation` varchar(45) DEFAULT NULL,
  `latitude` double NOT NULL,
  `longitude` double NOT NULL,
  `shedPerHourKW` double NOT NULL,
  `lastPrice` double NOT NULL,
  `offlineWarning` tinyint(1) NOT NULL,
  `client` tinyint(1) NOT NULL,
  `parent` varchar(64) DEFAULT NULL,
  `commTime` datetime DEFAULT NULL,
  `status` tinyint(3) unsigned DEFAULT '2',
  `testAccount` tinyint(1) NOT NULL,
  `activated` tinyint(1) NOT NULL,
  `dataEnabler` tinyint(1) DEFAULT NULL,
  `version` int(11) NOT NULL,
  `creator` varchar(255) DEFAULT NULL,
  `modifier` varchar(255) DEFAULT NULL,
  `creationTime` datetime DEFAULT NULL,
  `modifiedTime` datetime DEFAULT NULL,
  `push` int(11) DEFAULT '0',
  `ip` varchar(255) DEFAULT NULL,
  `optOut` tinyint(1) DEFAULT NULL,
  `eventStatus` varchar(20) DEFAULT 'NONE',
  `operationMode` varchar(20) DEFAULT 'UNKNOWN',
  `installer` tinyint(1) unsigned DEFAULT '0',
  `userType` varchar(20) DEFAULT 'ADVANCED',
  `aggregator` bit(1) DEFAULT NULL,
  `clientAllowedToOptOut` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`uuid`),
  UNIQUE KEY `nameKey` (`participantName`,`client`),
  UNIQUE KEY `accountKey` (`account`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `participant_contact`
--

DROP TABLE IF EXISTS `participant_contact`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `participant_contact` (
  `uuid` varchar(32) NOT NULL,
  `creationTime` datetime DEFAULT NULL,
  `type` varchar(45) NOT NULL,
  `address` varchar(128) NOT NULL,
  `description` varchar(128) DEFAULT NULL,
  `commNotification` tinyint(1) NOT NULL,
  `eventNotification` varchar(75) DEFAULT NULL,
  `offSeasonNotiHours` double NOT NULL DEFAULT '0.05',
  `onSeasonNotiHours` double NOT NULL DEFAULT '0.05',
  `offlineError` tinyint(1) NOT NULL DEFAULT '0',
  `external` tinyint(1) unsigned NOT NULL,
  `participant_uuid` varchar(32) NOT NULL,
  `msgThreshold` int(11) DEFAULT '0',
  `defaultMsgThreshold` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `participant_manualsignal`
--

DROP TABLE IF EXISTS `participant_manualsignal`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `participant_manualsignal` (
  `uuid` varchar(32) NOT NULL,
  `creationTime` datetime DEFAULT NULL,
  `name` varchar(64) NOT NULL,
  `value` varchar(64) NOT NULL,
  `participant_uuid` varchar(32) NOT NULL,
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `peakchoice_message`
--

DROP TABLE IF EXISTS `peakchoice_message`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `peakchoice_message` (
  `uuid` varchar(32) NOT NULL,
  `from` varchar(256) DEFAULT NULL,
  `to` text,
  `type` varchar(45) DEFAULT NULL,
  `subject` varchar(512) DEFAULT NULL,
  `content` text,
  `userName` varchar(64) DEFAULT NULL,
  `programName` varchar(255) DEFAULT NULL,
  `eventName` varchar(64) DEFAULT NULL,
  `creationTime` datetime NOT NULL,
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `program`
--

DROP TABLE IF EXISTS `program`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `program` (
  `uuid` varchar(32) NOT NULL,
  `version` int(11) NOT NULL,
  `priority` int(11) NOT NULL DEFAULT '0',
  `name` varchar(255) DEFAULT NULL,
  `beginEffectiveTime` datetime DEFAULT NULL,
  `endEffectiveTime` datetime DEFAULT NULL,
  `minIssueToStartM` int(11) DEFAULT NULL,
  `mustIssueBDBE` bit(1) DEFAULT NULL,
  `maxIssueTimeH` int(11) DEFAULT NULL,
  `minStartTimeH` int(11) DEFAULT NULL,
  `maxStartTimeH` int(11) DEFAULT NULL,
  `minEndTimeH` int(11) DEFAULT NULL,
  `maxEndTimeH` int(11) DEFAULT NULL,
  `maxIssueTimeM` int(11) DEFAULT NULL,
  `minStartTimeM` int(11) DEFAULT NULL,
  `maxStartTimeM` int(11) DEFAULT NULL,
  `minEndTimeM` int(11) DEFAULT NULL,
  `maxEndTimeM` int(11) DEFAULT NULL,
  `minDurationM` int(11) DEFAULT NULL,
  `maxDurationM` int(11) DEFAULT NULL,
  `pendingTimeDBEH` int(11) DEFAULT NULL,
  `pendingTimeDBEM` int(11) DEFAULT NULL,
  `notificationParam1` varchar(255) DEFAULT NULL,
  `creator` varchar(255) DEFAULT NULL,
  `modifier` varchar(255) DEFAULT NULL,
  `creationTime` datetime DEFAULT NULL,
  `modifiedTime` datetime DEFAULT NULL,
  `manualCreatable` bit(1) DEFAULT NULL,
  `utilityProgramName` varchar(255) DEFAULT NULL,
  `className` varchar(255) DEFAULT NULL,
  `state` int(11) DEFAULT NULL,
  `validatorClass` varchar(255) DEFAULT NULL,
  `validatorConfigFile` varchar(255) DEFAULT NULL,
  `uiConfigureProgramString` varchar(255) DEFAULT NULL,
  `uiConfigureEventString` varchar(255) DEFAULT NULL,
  `uiScheduleEventString` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `autoRepeatTimeOfDay` time DEFAULT NULL,
  `lastErrorOpContact` datetime DEFAULT NULL,
  `autoAccept` bit(1) DEFAULT b'0',
  `programType` varchar(64) DEFAULT NULL,
  `longProgramName` varchar(255) DEFAULT NULL,
  `programClass` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`uuid`),
  UNIQUE KEY `priorityKey` (`priority`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `program_matrix`
--

DROP TABLE IF EXISTS `program_matrix`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `program_matrix` (
  `uuid` varchar(32) NOT NULL,
  `version` int(11) DEFAULT NULL,
  `program1UUID` varchar(32) DEFAULT NULL,
  `program2UUID` varchar(32) DEFAULT NULL,
  `coexist` tinyint(1) DEFAULT '1',
  `creationTime` datetime DEFAULT NULL,
  `modifiedTime` datetime DEFAULT NULL,
  `creator` varchar(255) DEFAULT NULL,
  `modifier` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`uuid`),
  KEY `FK_program_matrix_1` (`program1UUID`),
  KEY `FK_program_matrix_2` (`program2UUID`),
  CONSTRAINT `FK_program_matrix_2` FOREIGN KEY (`program2UUID`) REFERENCES `program` (`uuid`),
  CONSTRAINT `FK_program_matrix_1` FOREIGN KEY (`program1UUID`) REFERENCES `program` (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `program_participant`
--

DROP TABLE IF EXISTS `program_participant`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `program_participant` (
  `uuid` varchar(32) NOT NULL,
  `version` int(11) NOT NULL,
  `programName` varchar(255) DEFAULT NULL,
  `participant_uuid` varchar(32) NOT NULL,
  `creator` varchar(255) DEFAULT NULL,
  `modifier` varchar(255) DEFAULT NULL,
  `creationTime` datetime DEFAULT NULL,
  `modifiedTime` datetime DEFAULT NULL,
  `program_uuid` varchar(32) DEFAULT NULL,
  `state` int(11) DEFAULT NULL,
  `clientConfig` int(11) DEFAULT NULL,
  `optStatus` int(11) DEFAULT '0',
  `name` varchar(64) NOT NULL,
  `value` varchar(64) NOT NULL,
  `ancestry` varchar(255) DEFAULT NULL,
  `parent_uuid` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`uuid`),
  KEY `FK_program_participant` (`program_uuid`),
  KEY `programNameInx` (`programName`),
  KEY `stateInx` (`state`),
  CONSTRAINT `FK_ program_participant` FOREIGN KEY (`program_uuid`) REFERENCES `program` (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `program_participant_bid_entry`
--

DROP TABLE IF EXISTS `program_participant_bid_entry`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `program_participant_bid_entry` (
  `uuid` varchar(32) NOT NULL,
  `version` int(11) NOT NULL,
  `reductionKW` double DEFAULT NULL,
  `priceLevel` double DEFAULT NULL,
  `blockStart` datetime DEFAULT NULL,
  `blockEnd` datetime DEFAULT NULL,
  `active` bit(1) DEFAULT NULL,
  `creator` varchar(255) DEFAULT NULL,
  `modifier` varchar(255) DEFAULT NULL,
  `creationTime` datetime DEFAULT NULL,
  `modifiedTime` datetime DEFAULT NULL,
  `program_participant_uuid` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`uuid`),
  KEY `FKEE375F7E8345F316` (`program_participant_uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `program_participant_bid_level_map`
--

DROP TABLE IF EXISTS `program_participant_bid_level_map`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `program_participant_bid_level_map` (
  `uuid` varchar(32) NOT NULL,
  `version` int(11) NOT NULL,
  `startKW` double DEFAULT NULL,
  `endKW` double DEFAULT NULL,
  `creator` varchar(255) DEFAULT NULL,
  `modifier` varchar(255) DEFAULT NULL,
  `creationTime` datetime DEFAULT NULL,
  `modifiedTime` datetime DEFAULT NULL,
  `signal_level_uuid` varchar(32) DEFAULT NULL,
  `program_participant_bid_entry_uuid` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`uuid`),
  KEY `FK1954F3589D6F284` (`signal_level_uuid`),
  KEY `FK1954F358FCD2992B` (`program_participant_bid_entry_uuid`),
  CONSTRAINT `FK1954F3589D6F284` FOREIGN KEY (`signal_level_uuid`) REFERENCES `signal_level_def` (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `program_participant_constraint`
--

DROP TABLE IF EXISTS `program_participant_constraint`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `program_participant_constraint` (
  `uuid` varchar(32) NOT NULL,
  `minActive` datetime DEFAULT NULL,
  `maxActive` datetime DEFAULT NULL,
  `activeAction` varchar(60) DEFAULT NULL,
  `minNotify` datetime DEFAULT NULL,
  `maxNotify` datetime DEFAULT NULL,
  `notifyAction` varchar(60) DEFAULT NULL,
  `minDuration` datetime DEFAULT NULL,
  `maxDuration` datetime DEFAULT NULL,
  `durationAction` varchar(60) DEFAULT NULL,
  `maxConsecutiveD` int(11) DEFAULT NULL,
  `consecutiveAction` varchar(60) DEFAULT NULL,
  `version` int(11) NOT NULL,
  `creator` varchar(255) DEFAULT NULL,
  `modifier` varchar(255) DEFAULT NULL,
  `creationTime` datetime DEFAULT NULL,
  `modifiedTime` datetime DEFAULT NULL,
  `program_participant_uuid` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`uuid`),
  KEY `FKPPC_TO_PP` (`program_participant_uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `program_participant_rule`
--

DROP TABLE IF EXISTS `program_participant_rule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `program_participant_rule` (
  `uuid` varchar(32) NOT NULL,
  `start` datetime DEFAULT NULL,
  `end` datetime DEFAULT NULL,
  `mode` varchar(60) DEFAULT NULL,
  `variable` varchar(60) DEFAULT NULL,
  `operator` varchar(60) DEFAULT NULL,
  `value` double DEFAULT NULL,
  `source` varchar(60) DEFAULT NULL,
  `sortOrder` int(11) DEFAULT NULL,
  `version` int(11) NOT NULL,
  `creator` varchar(255) DEFAULT NULL,
  `modifier` varchar(255) DEFAULT NULL,
  `creationTime` datetime DEFAULT NULL,
  `modifiedTime` datetime DEFAULT NULL,
  `program_participant_uuid` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`uuid`),
  KEY `FKPPR_TO_PP` (`program_participant_uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `program_rtp_config`
--

DROP TABLE IF EXISTS `program_rtp_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `program_rtp_config` (
  `uuid` varchar(32) NOT NULL,
  `version` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `startTemperature` double DEFAULT NULL,
  `endTemperature` double DEFAULT NULL,
  `startTime` time DEFAULT NULL,
  `endTime` time DEFAULT NULL,
  `rate` double DEFAULT NULL,
  `unit` varchar(255) DEFAULT NULL,
  `creationTime` datetime DEFAULT NULL,
  `modifiedTime` datetime DEFAULT NULL,
  `creator` varchar(255) DEFAULT NULL,
  `modifier` varchar(255) DEFAULT NULL,
  `program_uuid` varchar(32) DEFAULT NULL,
  `seasonName` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`uuid`),
  KEY `FK_program_rtp_config` (`program_uuid`),
  CONSTRAINT `FK_program_rtp_config` FOREIGN KEY (`program_uuid`) REFERENCES `program` (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `program_rule`
--

DROP TABLE IF EXISTS `program_rule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `program_rule` (
  `uuid` varchar(32) NOT NULL,
  `start` datetime DEFAULT NULL,
  `end` datetime DEFAULT NULL,
  `mode` varchar(60) DEFAULT NULL,
  `variable` varchar(60) DEFAULT NULL,
  `operator` varchar(60) DEFAULT NULL,
  `value` double DEFAULT NULL,
  `source` varchar(60) DEFAULT NULL,
  `sortOrder` int(11) DEFAULT NULL,
  `version` int(11) NOT NULL,
  `creator` varchar(255) DEFAULT NULL,
  `modifier` varchar(255) DEFAULT NULL,
  `creationTime` datetime DEFAULT NULL,
  `modifiedTime` datetime DEFAULT NULL,
  `program_uuid` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`uuid`),
  KEY `FKPPR_TO_PROG` (`program_uuid`),
  CONSTRAINT `FKPPR_TO_PROG` FOREIGN KEY (`program_uuid`) REFERENCES `program` (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `program_signal`
--

DROP TABLE IF EXISTS `program_signal`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `program_signal` (
  `uuid` varchar(32) NOT NULL,
  `version` int(11) NOT NULL,
  `creator` varchar(255) DEFAULT NULL,
  `modifier` varchar(255) DEFAULT NULL,
  `creationTime` datetime DEFAULT NULL,
  `modifiedTime` datetime DEFAULT NULL,
  `program_uuid` varchar(32) NOT NULL,
  `signal_def_uuid` varchar(32) NOT NULL,
  PRIMARY KEY (`uuid`),
  KEY `FK1_program_signal` (`signal_def_uuid`),
  KEY `FK2_program_signal` (`program_uuid`),
  CONSTRAINT `FK2_program_signal` FOREIGN KEY (`program_uuid`) REFERENCES `program` (`uuid`),
  CONSTRAINT `FK1_program_signal` FOREIGN KEY (`signal_def_uuid`) REFERENCES `signal_def` (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `program_signal_entry`
--

DROP TABLE IF EXISTS `program_signal_entry`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `program_signal_entry` (
  `uuid` varchar(32) NOT NULL,
  `programSignalUuid` varchar(32) NOT NULL,
  `time` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `expired` tinyint(3) unsigned NOT NULL,
  `stringValue` varchar(45) NOT NULL,
  `version` int(11) NOT NULL,
  `creator` varchar(255) DEFAULT NULL,
  `modifier` varchar(255) DEFAULT NULL,
  `creationTime` datetime DEFAULT NULL,
  `modifiedTime` datetime DEFAULT NULL,
  `numberValue` double DEFAULT NULL,
  PRIMARY KEY (`uuid`),
  KEY `FK_program_signal_entry_prog_signal` (`programSignalUuid`),
  CONSTRAINT `FK_program_signalentry_prog_signal` FOREIGN KEY (`programSignalUuid`) REFERENCES `program_signal` (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `report_event`
--

DROP TABLE IF EXISTS `report_event`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `report_event` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `programName` varchar(255) DEFAULT NULL,
  `eventName` varchar(64) DEFAULT NULL,
  `startTime` datetime DEFAULT NULL,
  `endTime` datetime DEFAULT NULL,
  `cancelled` bit(1) NOT NULL DEFAULT b'0',
  PRIMARY KEY (`id`),
  KEY `IndexReportEvent_eventName` (`eventName`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `report_event_participation`
--

DROP TABLE IF EXISTS `report_event_participation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `report_event_participation` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `programName` varchar(255) NOT NULL,
  `eventName` varchar(64) NOT NULL,
  `participantName` varchar(64) NOT NULL,
  `accountId` varchar(128) NOT NULL,
  `entryTime` datetime NOT NULL,
  `reason` varchar(64) NOT NULL,
  `type` varchar(64) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `IndexReportEventParticipant_partName` (`participantName`),
  KEY `IndexReportEventParticipant_eventName` (`eventName`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `report_event_signal`
--

DROP TABLE IF EXISTS `report_event_signal`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `report_event_signal` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `programName` varchar(255) NOT NULL,
  `eventName` varchar(64) NOT NULL,
  `participantName` varchar(64) NOT NULL,
  `accountId` varchar(128) NOT NULL,
  `signalTime` datetime NOT NULL,
  `signalLevel` varchar(64) NOT NULL,
  `actualLevel` varchar(64) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `rtp_ftp_configuration`
--

DROP TABLE IF EXISTS `rtp_ftp_configuration`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rtp_ftp_configuration` (
  `uuid` varchar(32) NOT NULL,
  `url` varchar(32) DEFAULT NULL,
  `port` varchar(32) DEFAULT NULL,
  `path` varchar(255) DEFAULT NULL,
  `fileName` varchar(32) DEFAULT NULL,
  `userName` varchar(32) DEFAULT NULL,
  `password` varchar(32) DEFAULT NULL,
  `scanStartTime` varchar(32) DEFAULT NULL,
  `scanEndTime` varchar(32) DEFAULT NULL,
  `scanInterval` varchar(32) DEFAULT NULL,
  `required` bit(1) DEFAULT NULL,
  `creationTime` datetime DEFAULT NULL,
  `minTemperature` double DEFAULT NULL,
  `maxTemperature` double DEFAULT NULL,
  `lastProcessTime` datetime DEFAULT NULL,
  `sendNotification` bit(1) DEFAULT NULL,
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `rtp_remote_price`
--

DROP TABLE IF EXISTS `rtp_remote_price`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rtp_remote_price` (
  `uuid` varchar(32) NOT NULL,
  `price` double NOT NULL,
  `intervalTime` datetime NOT NULL,
  `programName` varchar(255) NOT NULL,
  `eventName` varchar(64) DEFAULT NULL,
  `version` int(11) NOT NULL,
  `creator` varchar(255) DEFAULT NULL,
  `modifier` varchar(255) DEFAULT NULL,
  `creationTime` datetime DEFAULT NULL,
  `modifiedTime` datetime DEFAULT NULL,
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `scedbp_dispatch_config`
--

DROP TABLE IF EXISTS `scedbp_dispatch_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `scedbp_dispatch_config` (
  `uuid` varchar(32) NOT NULL,
  `host` varchar(30) DEFAULT NULL,
  `port` int(11) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  `password` varchar(100) DEFAULT NULL,
  `backupPath` varchar(255) DEFAULT NULL,
  `formattedFilename` varchar(255) DEFAULT NULL,
  `scanStartTime` varchar(10) DEFAULT NULL,
  `scanEndTime` varchar(10) DEFAULT NULL,
  `scanInterval` int(11) DEFAULT NULL,
  `required` bit(1) DEFAULT b'0',
  `creationTime` datetime DEFAULT NULL,
  `upload` bit(1) NOT NULL DEFAULT b'0',
  `fixedFilename` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`uuid`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `scertp_event`
--

DROP TABLE IF EXISTS `scertp_event`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `scertp_event` (
  `uuid` varchar(32) NOT NULL,
  `reportingWeatherStation` varchar(8) DEFAULT NULL,
  `highTemperature` double DEFAULT NULL,
  `amended` tinyint(1) NOT NULL,
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `season_config`
--

DROP TABLE IF EXISTS `season_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `season_config` (
  `uuid` varchar(32) NOT NULL,
  `version` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `startDate` date DEFAULT NULL,
  `endDate` date DEFAULT NULL,
  `creator` varchar(255) DEFAULT NULL,
  `modifier` varchar(255) DEFAULT NULL,
  `creationTime` datetime DEFAULT NULL,
  `modifiedTime` datetime DEFAULT NULL,
  `program_uuid` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `signal_def`
--

DROP TABLE IF EXISTS `signal_def`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `signal_def` (
  `uuid` varchar(32) NOT NULL,
  `version` int(11) NOT NULL,
  `type` varchar(255) DEFAULT NULL,
  `signalName` varchar(255) DEFAULT NULL,
  `creator` varchar(255) DEFAULT NULL,
  `modifier` varchar(255) DEFAULT NULL,
  `creationTime` datetime DEFAULT NULL,
  `modifiedTime` datetime DEFAULT NULL,
  `levelSignal` tinyint(1) DEFAULT '1',
  `inputSignal` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `signal_level_def`
--

DROP TABLE IF EXISTS `signal_level_def`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `signal_level_def` (
  `uuid` varchar(32) NOT NULL,
  `version` int(11) NOT NULL,
  `stringValue` varchar(255) DEFAULT NULL,
  `defaultValue` bit(1) DEFAULT NULL,
  `creator` varchar(255) DEFAULT NULL,
  `modifier` varchar(255) DEFAULT NULL,
  `creationTime` datetime DEFAULT NULL,
  `modifiedTime` datetime DEFAULT NULL,
  `signal_def_uuid` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`uuid`),
  KEY `FK1_signal_level_def` (`signal_def_uuid`),
  CONSTRAINT `FK1_signal_level_def` FOREIGN KEY (`signal_def_uuid`) REFERENCES `signal_def` (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sub_account`
--

DROP TABLE IF EXISTS `sub_account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sub_account` (
  `uuid` varchar(64) NOT NULL,
  `version` int(11) NOT NULL,
  `participant_uuid` varchar(32) NOT NULL,
  `subAccountId` varchar(256) NOT NULL,
  `premiseNumber` varchar(256) NOT NULL,
  `comment` text,
  `startDate` datetime DEFAULT NULL,
  `endDate` datetime DEFAULT NULL,
  `enrollmentDate` datetime DEFAULT NULL,
  `creator` varchar(255) DEFAULT NULL,
  `creationTime` datetime DEFAULT NULL,
  `modifier` varchar(255) DEFAULT NULL,
  `modifiedTime` datetime DEFAULT NULL,
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Final view structure for view `all_program_participant`
--

/*!50001 DROP TABLE IF EXISTS `all_program_participant`*/;
/*!50001 DROP VIEW IF EXISTS `all_program_participant`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = latin1 */;
/*!50001 SET character_set_results     = latin1 */;
/*!50001 SET collation_connection      = latin1_swedish_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `all_program_participant` AS select `pp`.`uuid` AS `uuid`,`p`.`participantName` AS `participantName`,`p`.`client` AS `client`,`pp`.`participant_uuid` AS `participant_uuid`,`pp`.`programName` AS `programName`,`pp`.`parent_uuid` AS `parent_uuid`,`pp`.`program_uuid` AS `program_uuid`,if(isnull(`pp`.`ancestry`),`pp`.`uuid`,`pp`.`ancestry`) AS `ancestry` from (`program_participant` `pp` join `participant` `p`) where ((`pp`.`participant_uuid` = `p`.`uuid`) and (`p`.`client` = 0)) union all select replace(uuid(),'-','') AS `UUID`,`p1`.`participantName` AS `participantName`,1 AS `CLIENT`,`p1`.`uuid` AS `participant_uuid`,`pp1`.`programName` AS `programName`,`pp2`.`uuid` AS `parent_uuid`,`pp1`.`program_uuid` AS `program_uuid`,if(isnull(`pp2`.`ancestry`),`pp2`.`uuid`,concat(`pp2`.`ancestry`,`pp2`.`uuid`)) AS `ancestry` from (((`participant` `p1` join `participant` `p2`) join `program_participant` `pp1`) join `program_participant` `pp2`) where ((`p1`.`parent` = `p2`.`participantName`) and (`pp1`.`state` = 1) and isnull(`pp1`.`parent_uuid`) and (`p1`.`uuid` = `pp1`.`participant_uuid`) and (`p2`.`uuid` = `pp2`.`participant_uuid`) and (`pp1`.`programName` = `pp2`.`programName`)) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2011-08-01 15:30:56
