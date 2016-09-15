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
-- Dumping data for table `DATABASECHANGELOG`
--

LOCK TABLES `DATABASECHANGELOG` WRITE;
/*!40000 ALTER TABLE `DATABASECHANGELOG` DISABLE KEYS */;
INSERT INTO `DATABASECHANGELOG` (`ID`, `AUTHOR`, `FILENAME`, `DATEEXECUTED`, `MD5SUM`, `DESCRIPTION`, `COMMENTS`, `TAG`, `LIQUIBASE`, `ORDEREXECUTED`, `EXECTYPE`) VALUES ('1','brian','src/main/resources/pss2/6.4.xml','2011-02-14 16:28:38',NULL,'Add Column','DRMS-3127 Add \"push\" flag and IP address to facdash\n            client\n            config screen for ICCP project',NULL,'1.9.5',-1,'EXECUTED'),('2','brian','src/main/resources/pss2/6.4.xml','2011-02-14 16:28:38',NULL,'Add Column','DRMS-3127 Add \"push\" flag and IP address to facdash\n            client\n            config screen for ICCP project',NULL,'1.9.5',-1,'EXECUTED'),('3','brian','src/main/resources/pss2/6.4.xml','2011-02-14 16:28:38',NULL,'Add Column','DRMS-3127 Add \"push\" flag and IP address to facdash\n            client\n            config screen for ICCP project',NULL,'1.9.5',-1,'EXECUTED'),('4','brian','src/main/resources/pss2/6.4.xml','2011-02-14 16:28:38',NULL,'Update Data','DRMS-2407 set version to empty string, default comes\n            from\n            build.',NULL,'1.9.5',-1,'EXECUTED'),('5','brian','src/main/resources/pss2/6.4.xml','2011-02-14 16:28:38',NULL,'Delete Data','DRMS-2406 The usagedataWS.contextRoot is now auto\n            generated.',NULL,'1.9.5',-1,'EXECUTED'),('6','brian','src/main/resources/pss2/6.4.xml','2011-02-14 16:28:38',NULL,'Add Column','DRMS-2980 Add participant opt-out flag',NULL,'1.9.5',-1,'EXECUTED'),('7','brian','src/main/resources/pss2/6.4.xml','2011-02-14 16:28:38',NULL,'Insert Row','DRMS-2979 Develop UI',NULL,'1.9.5',-1,'EXECUTED'),('8','brian','src/main/resources/pss2/6.4.xml','2011-02-14 16:28:38',NULL,'Insert Row','DRMS-2979 Develop UI',NULL,'1.9.5',-1,'EXECUTED'),('9','brian','src/main/resources/pss2/6.4.xml','2011-02-14 16:28:38',NULL,'Insert Row','DRMS-2979 Develop UI',NULL,'1.9.5',-1,'EXECUTED'),('10','brian','src/main/resources/pss2/6.4.xml','2011-02-14 16:28:38',NULL,'Insert Row','DRMS-2979 Develop UI',NULL,'1.9.5',-1,'EXECUTED'),('11','brian','src/main/resources/pss2/6.4.xml','2011-02-14 16:28:38',NULL,'Insert Row','DRMS-2979 Develop UI',NULL,'1.9.5',-1,'EXECUTED'),('12','brian','src/main/resources/pss2/6.4.xml','2011-02-14 16:28:38',NULL,'Insert Row','DRMS-2979 Develop UI',NULL,'1.9.5',-1,'EXECUTED'),('13','brian','src/main/resources/pss2/6.4.xml','2011-02-14 16:28:38',NULL,'Insert Row','DRMS-2979 Develop UI',NULL,'1.9.5',-1,'EXECUTED'),('14','brian','src/main/resources/pss2/6.4.xml','2011-02-14 16:28:38',NULL,'Insert Row','DRMS-2979 Develop UI',NULL,'1.9.5',-1,'EXECUTED'),('15','brian','src/main/resources/pss2/6.4.xml','2011-02-14 16:28:38',NULL,'Insert Row','DRMS-2979 Develop UI',NULL,'1.9.5',-1,'EXECUTED'),('16','brian','src/main/resources/pss2/6.4.xml','2011-02-14 16:28:38',NULL,'Insert Row','DRMS-2979 Develop UI',NULL,'1.9.5',-1,'EXECUTED'),('1','brian','db/changelogs/pss2/6.8.xml','2011-08-01 14:36:11','3:d41d8cd98f00b204e9800998ecf8427e','Empty','DRMS-4452 TEST',NULL,'2.0.1',0,'EXECUTED'),('1','Veera','db/changelogs/pss2/6.9.xml','2011-08-01 14:36:11','3:2d7cfbb9effa7640c3515761a41049e0','Update Data','DRMS-4438 Core Property feature.reportClientNonParticipation typo update',NULL,'2.0.1',1,'EXECUTED'),('2','Veera','db/changelogs/pss2/6.9.xml','2011-08-01 14:36:11','3:386152286b9ae9d7b345f6e22305bd73','Modify data type','DRMS-4438 Change the log-in text for SCE',NULL,'2.0.1',2,'EXECUTED'),('5','liu','db/changelogs/pss2/6.9.xml','2011-08-01 14:36:11','3:9c45ff14ff4b863c5f62feb68749979d','Add Column','DRMS-4618 CBP Signals Sent to Aggregators',NULL,'2.0.1',3,'EXECUTED'),('6','Frank','db/changelogs/pss2/6.9.xml','2011-08-01 14:36:11','3:579379be370314b548ddb68e0fe437d8','Custom SQL','DRMS-4926 Merge Usage Data CLONES from 6.2.5 to Trunk',NULL,'2.0.1',4,'EXECUTED'),('7','Linda','db/changelogs/pss2/6.9.xml','2011-08-01 14:36:11','3:f184985c24df762a0809c117e98a58af','Custom SQL','DRMS-4619 DBP Event Dispatch via FTP',NULL,'2.0.1',5,'EXECUTED'),('8','Linda','db/changelogs/pss2/6.9.xml','2011-08-01 14:36:11','3:43d21e30a560f1a38fadb84a81a40f7c','Add Column','DRMS-5029 Operator Report API development',NULL,'2.0.1',6,'EXECUTED'),('9','Linda','db/changelogs/pss2/6.9.xml','2011-08-01 14:36:11','3:a75b41a5ba93c109c10354b03746e8cf','Add Column','DRMS-5029 Operator Report API development',NULL,'2.0.1',7,'EXECUTED'),('10','Linda','db/changelogs/pss2/6.9.xml','2011-08-01 14:36:11','3:f904a51d924df7bc54ff369b23b7e120','Add Column','DRMS-5029 Operator Report API development',NULL,'2.0.1',8,'EXECUTED'),('11','Veera','db/changelogs/pss2/6.9.xml','2011-08-01 14:36:11','3:63989aa4e83e9792e6df051e5d31b07e','Add Column, Update Data','DRMS-3315 Inherit default message threshold',NULL,'2.0.1',9,'EXECUTED'),('12','Veera','db/changelogs/pss2/6.9.xml','2011-08-01 14:36:11','3:63c6aec3c4bd60c433778e123b1ea8fd','Add Column, Update Data','DRMS-3315 Define default message threshold',NULL,'2.0.1',10,'EXECUTED'),('13','Veera','db/changelogs/pss2/6.9.xml','2011-08-01 14:36:11','3:70d5d33483869e8dc91a077eeada7ed5','Add Column, Update Data','DRMS-3315 Inherit default message threshold',NULL,'2.0.1',11,'EXECUTED'),('14','Veera','db/changelogs/pss2/6.9.xml','2011-08-01 14:36:12','3:48624b94451e995b49257a03fe26c4e2','Drop Default Value, Add Default Value, Update Data','DRMS-3315 Redefine default message threshold',NULL,'2.0.1',12,'EXECUTED'),('15','Veera','db/changelogs/pss2/6.9.xml','2011-08-01 14:36:12','3:9379dcc81bafe283ba5a1d34deaebb6c','Update Data','DRMS-3315 Update default message threshold setting',NULL,'2.0.1',13,'EXECUTED'),('15','Daoping','db/changelogs/pss2/6.9.xml','2011-08-01 14:36:12','3:cc5e42533cab46cc392438b70d67ee9e','Custom SQL','DRMS-5027 database view:all_program_participant for Reporting',NULL,'2.0.1',14,'EXECUTED'),('16','liu','db/changelogs/pss2/6.9.xml','2011-08-01 14:36:12','3:f6bddd91bbdd5342d19eaf772f40bbcb','Rename Column','DRMS-5223 [SCE] change \"CBP Aggregator\" to Aggregator',NULL,'2.0.1',15,'EXECUTED'),('17','veera','db/changelogs/pss2/6.9.xml','2011-08-01 14:36:12','3:bee5b883925e184c6d3f7689141ab21a','Update Data','DRMS-5247',NULL,'2.0.1',16,'EXECUTED'),('18','liu','db/changelogs/pss2/6.9.xml','2011-08-01 14:36:12','3:4109a61c0b9fc9bd3c9bad9f3e3c2c5a','Delete Data','DRMS-5210 DR-Website - Event History search dialog - DRC missing from Program list',NULL,'2.0.1',17,'EXECUTED'),('19','Veera','db/changelogs/pss2/6.9.xml','2011-08-01 14:36:12','3:b31ddfca21505445204858eb52513822','Delete Data, Custom SQL','DRMS-5221 SuppressAllEmails flag',NULL,'2.0.1',18,'EXECUTED'),('20','Chris','db/changelogs/pss2/6.9.xml','2011-08-01 14:36:12','3:ea89ca05c51df50bda18e3b669c09e45','Add Column, Update Data','DRMS-5266 JACE polling causes participant\'s client opt-out when event is issued',NULL,'2.0.1',19,'EXECUTED'),('22','brian','db/changelogs/pss2/6.9.xml','2011-08-01 14:36:12','3:34babeb4b18948341bd959b35e91fab3','Drop Table','DRMS-4689',NULL,'2.0.1',20,'EXECUTED'),('23','Linda','db/changelogs/pss2/6.9.xml','2011-08-01 14:36:12','3:e31d39dc7b6117509df5f03e598a5aa1','Delete Data','DRMS-4619 remove sce dbp configuration from core property',NULL,'2.0.1',21,'EXECUTED'),('24','Linda','db/changelogs/pss2/6.9.xml','2011-08-01 14:36:12','3:94efb49361ad828a142559c12355b69e','Add Column, Rename Column','DRMS-4619 add upload flag',NULL,'2.0.1',22,'EXECUTED'),('1','Veera','db/changelogs/pss2/6.10.xml','2011-08-01 14:36:12','3:b3726f3ce2126160590880c8bf6b7cd0','Delete Data, Custom SQL','DRMS-5338 feature.participantsMapView flag',NULL,'2.0.1',23,'EXECUTED'),('2','Veera','db/changelogs/pss2/6.10.xml','2011-08-01 14:36:12','3:67fb26577c77bc409b59b7fa779b517a','Update Data','DRMS-5247',NULL,'2.0.1',24,'EXECUTED'),('1','brian','resource/utilities/default/data/pss2.xml','2011-08-01 14:36:21','3:d41d8cd98f00b204e9800998ecf8427e','Empty','DRMS-4452 TEST',NULL,'2.0.1',25,'EXECUTED');
/*!40000 ALTER TABLE `DATABASECHANGELOG` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `DATABASECHANGELOGLOCK`
--

LOCK TABLES `DATABASECHANGELOGLOCK` WRITE;
/*!40000 ALTER TABLE `DATABASECHANGELOGLOCK` DISABLE KEYS */;
INSERT INTO `DATABASECHANGELOGLOCK` (`ID`, `LOCKED`, `LOCKGRANTED`, `LOCKEDBY`) VALUES (1,0,NULL,NULL);
/*!40000 ALTER TABLE `DATABASECHANGELOGLOCK` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `Task`
--

LOCK TABLES `Task` WRITE;
/*!40000 ALTER TABLE `Task` DISABLE KEYS */;
/*!40000 ALTER TABLE `Task` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `baseline_config`
--

LOCK TABLES `baseline_config` WRITE;
/*!40000 ALTER TABLE `baseline_config` DISABLE KEYS */;
INSERT INTO `baseline_config` (`uuid`, `baseline_model_uuid`, `owner_uuid`, `name`, `excludeHolidayFromCalc`, `excludedDaysOfWeekFromCalc`, `excludeHoliday`, `excludedDaysOfWeek`, `excludeAbnormalDayImplClass`, `description`, `eventStartTime`, `eventEndTime`, `maStartTime`, `maEndTime`, `minMARate`, `maxMARate`, `excludeEventDay`, `version`, `creator`, `modifier`, `creationTime`, `modifiedTime`) VALUES ('df6c858a06de11e09bc70382820593d2','df6c6b6806de11e09bc70382820593d2',NULL,'Default 3/10 Config',1,1000001,1,0,'com.akuacom.pss2.data.usage.calcimpl.DefaultAbnormalDayHandlerBean','Default 3/10 Config',43200000,72000000,28800000,39600000,0,0,1,0,'script',NULL,'2010-12-13 09:31:52',NULL),('df6ca79a06de11e09bc70382820593d2','df6c983606de11e09bc70382820593d2',NULL,'Default 10/10 Config',1,1000001,1,0,'com.akuacom.pss2.data.usage.calcimpl.DefaultAbnormalDayHandlerBean','Default 10/10 Config',43200000,72000000,28800000,39600000,0,0,1,0,'script',NULL,'2010-12-13 09:31:52',NULL),('df6cc39206de11e09bc70382820593d2','df6cb6ae06de11e09bc70382820593d2',NULL,'Default 5/10 Config',1,1000001,1,0,'com.akuacom.pss2.data.usage.calcimpl.DefaultAbnormalDayHandlerBean','Default 5/10 Config',43200000,72000000,28800000,39600000,0,0,1,0,'script',NULL,'2010-12-13 09:31:52',NULL);
/*!40000 ALTER TABLE `baseline_config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `baseline_model`
--

LOCK TABLES `baseline_model` WRITE;
/*!40000 ALTER TABLE `baseline_model` DISABLE KEYS */;
INSERT INTO `baseline_model` (`uuid`, `name`, `implClass`, `dayPickerImplClass`, `calcImplClass`, `description`, `version`, `creator`, `modifier`, `creationTime`, `modifiedTime`) VALUES ('df6c6b6806de11e09bc70382820593d2','ThreeTen','com.akuacom.pss2.data.usage.calcimpl.BaselineThreeTenModelBean','com.akuacom.pss2.data.usage.calcimpl.DefaultDaysSelectorBean','com.akuacom.pss2.data.usage.calcimpl.DefaultBaselineCalculatorBean','3/10 baseline calculation model',0,'script',NULL,'2010-12-13 09:31:52',NULL),('df6c983606de11e09bc70382820593d2','TenTen','com.akuacom.pss2.data.usage.calcimpl.BaselineTenTenModelBean','com.akuacom.pss2.data.usage.calcimpl.DefaultDaysSelectorBean','com.akuacom.pss2.data.usage.calcimpl.DefaultBaselineCalculatorBean','10/10 baseline calculation model',0,'script',NULL,'2010-12-13 09:31:52',NULL),('df6cb6ae06de11e09bc70382820593d2','FiveTen','com.akuacom.pss2.data.usage.calcimpl.BaselineFiveTenModelBean','com.akuacom.pss2.data.usage.calcimpl.DefaultDaysSelectorBean','com.akuacom.pss2.data.usage.calcimpl.DefaultBaselineCalculatorBean','5/10 baseline calculation model',0,'script',NULL,'2010-12-13 09:31:52',NULL);
/*!40000 ALTER TABLE `baseline_model` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `bid_block`
--

LOCK TABLES `bid_block` WRITE;
/*!40000 ALTER TABLE `bid_block` DISABLE KEYS */;
/*!40000 ALTER TABLE `bid_block` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `bid_config`
--

LOCK TABLES `bid_config` WRITE;
/*!40000 ALTER TABLE `bid_config` DISABLE KEYS */;
/*!40000 ALTER TABLE `bid_config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `bid_level_mapping`
--

LOCK TABLES `bid_level_mapping` WRITE;
/*!40000 ALTER TABLE `bid_level_mapping` DISABLE KEYS */;
/*!40000 ALTER TABLE `bid_level_mapping` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `client_conversation_state`
--

LOCK TABLES `client_conversation_state` WRITE;
/*!40000 ALTER TABLE `client_conversation_state` DISABLE KEYS */;
/*!40000 ALTER TABLE `client_conversation_state` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `client_status`
--

LOCK TABLES `client_status` WRITE;
/*!40000 ALTER TABLE `client_status` DISABLE KEYS */;
/*!40000 ALTER TABLE `client_status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `constraint_invalid_date`
--

LOCK TABLES `constraint_invalid_date` WRITE;
/*!40000 ALTER TABLE `constraint_invalid_date` DISABLE KEYS */;
/*!40000 ALTER TABLE `constraint_invalid_date` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `contact`
--

LOCK TABLES `contact` WRITE;
/*!40000 ALTER TABLE `contact` DISABLE KEYS */;
/*!40000 ALTER TABLE `contact` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `contacts_offline_error`
--

LOCK TABLES `contacts_offline_error` WRITE;
/*!40000 ALTER TABLE `contacts_offline_error` DISABLE KEYS */;
/*!40000 ALTER TABLE `contacts_offline_error` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `core_property`
--

LOCK TABLES `core_property` WRITE;
/*!40000 ALTER TABLE `core_property` DISABLE KEYS */;
INSERT INTO `core_property` (`uuid`, `propertyName`, `stringValue`, `booleanValue`, `doubleValue`, `textValue`, `type`, `creator`, `modifier`, `creationTime`, `modifiedTime`, `version`) VALUES ('400de3a4bc8611e097d4c402baa3011a','emailFromAddress','noreply@openadr.com',NULL,NULL,NULL,'String','script',NULL,'2011-08-01 14:36:00',NULL,0),('400df7ccbc8611e097d4c402baa3011a','baseline.model','ThreeTen',NULL,NULL,NULL,'String','script',NULL,'2011-08-01 14:36:00',NULL,0),('400e20f8bc8611e097d4c402baa3011a','excludedProgramsForEventLine','RTP Agricultural,RTP <2K,RTP 2K-50K,RTP >50K',NULL,NULL,NULL,'String','script',NULL,'2011-08-01 14:36:00',NULL,0),('400e36e2bc8611e097d4c402baa3011a','excludedProgramsForBaseline','DEMO',NULL,NULL,NULL,'String','script',NULL,'2011-08-01 14:36:00',NULL,0),('400e4a9cbc8611e097d4c402baa3011a','holidays','05/30/2011,07/04/2011,09/05/2011',NULL,NULL,NULL,'String','script',NULL,'2011-08-01 14:36:00',NULL,0),('400e64d2bc8611e097d4c402baa3011a','feature.storebackEnable',NULL,'\0',NULL,NULL,'Boolean','script',NULL,'2011-08-01 14:36:00',NULL,0),('400fb5d0bc8611e097d4c402baa3011a','disableUsageDataInterpolation',NULL,'',NULL,NULL,'Boolean','script',NULL,'2011-08-01 14:36:00',NULL,0),('4010a0f8bc8611e097d4c402baa3011a','feature.rtp.temperature.update',NULL,'\0',NULL,NULL,'Boolean','script',NULL,'2011-08-01 14:36:00',NULL,0),('4111a88019ed11e0a1595daf5ab26f15','baseline.missingDataThreshold',NULL,NULL,0.3,NULL,'Number','script',NULL,'2011-01-06 15:32:41',NULL,0),('418d49fe19ed11e0a1595daf5ab26f15','sce.ftp.host','',NULL,NULL,NULL,'String','script',NULL,'2011-01-06 15:32:41',NULL,0),('419ca36819ed11e0a1595daf5ab26f15','sce.ftp.port','',NULL,NULL,NULL,'String','script',NULL,'2011-01-06 15:32:41',NULL,0),('41c39f2219ed11e0a1595daf5ab26f15','sce.ftp.username','',NULL,NULL,NULL,'String','script',NULL,'2011-01-06 15:32:42',NULL,0),('41c3c7ea19ed11e0a1595daf5ab26f15','sce.ftp.password','',NULL,NULL,NULL,'String','script',NULL,'2011-01-06 15:32:42',NULL,0),('41c3d80c19ed11e0a1595daf5ab26f15','sce.participant.synchronization.active',NULL,'\0',NULL,NULL,'Boolean','script',NULL,'2011-01-06 15:32:42',NULL,0),('41c3e77019ed11e0a1595daf5ab26f15','sce.participant.sync.filepath','',NULL,NULL,NULL,'String','script',NULL,'2011-01-06 15:32:42',NULL,0),('41c3faa819ed11e0a1595daf5ab26f15','sce.participant.sync.filename','',NULL,NULL,NULL,'String','script',NULL,'2011-01-06 15:32:42',NULL,0),('41c40b2e19ed11e0a1595daf5ab26f15','sce.participant.sync.scan.duration(hour)','',NULL,NULL,NULL,'String','script',NULL,'2011-01-06 15:32:42',NULL,0),('41c4323e19ed11e0a1595daf5ab26f15','sce.participant.sync.scan.time','',NULL,NULL,NULL,'String','script',NULL,'2011-01-06 15:32:42',NULL,0),('41c446c019ed11e0a1595daf5ab26f15','sce.participant.customer.test.event',NULL,'',NULL,NULL,'Boolean','script',NULL,'2011-01-06 15:32:42',NULL,0),('422197e419ed11e0a1595daf5ab26f15','feature.scedbpEventAutoCreation',NULL,'\0',NULL,NULL,'Boolean','script',NULL,'2010-12-13 09:31:37',NULL,0),('426ab5aa19ed11e0a1595daf5ab26f15','feature.reportEMailList',NULL,'',NULL,NULL,'Boolean','script',NULL,'2011-01-06 15:32:43',NULL,0),('427f6c6619ed11e0a1595daf5ab26f15','feature.cloneEnabledForOp',NULL,'',NULL,NULL,'Boolean','script',NULL,'2011-01-06 15:32:43',NULL,0),('42a22c9219ed11e0a1595daf5ab26f15','feature.nonAutoClientSupported',NULL,'',NULL,NULL,'Boolean','script',NULL,'2011-01-06 15:32:43',NULL,0),('42d0bbe819ed11e0a1595daf5ab26f15','feature.specialModeEnabled',NULL,'\0',NULL,NULL,'Boolean','script',NULL,'2011-01-06 15:32:44',NULL,0),('46f2b12cbc8611e097d4c402baa3011a','SuppressAllEmails',NULL,'',NULL,NULL,'Boolean','script',NULL,'2011-08-01 14:36:12',NULL,0),('4728430abc8611e097d4c402baa3011a','feature.participantsMapView',NULL,'\0',NULL,NULL,'Boolean','script',NULL,'2011-08-01 14:36:12',NULL,0),('8856abb6389a11e0a394b8ac6faea91c','simple.dashboard.pending.message','Event starts in {0} hour(s), {1} min(s)',NULL,NULL,NULL,'String','script',NULL,'2011-02-14 16:28:38',NULL,0),('88573d56389a11e0a394b8ac6faea91c','simple.dashboard.noevent.message','No active or pending events',NULL,NULL,NULL,'String','script',NULL,'2011-02-14 16:28:38',NULL,0),('885819e2389a11e0a394b8ac6faea91c','simple.dashboard.active.message','Active Event. Time remaining {0} hour(s), {1} min(s)',NULL,NULL,NULL,'String','script',NULL,'2011-02-14 16:28:38',NULL,0),('8858a934389a11e0a394b8ac6faea91c','simple.dashboard.link2','http://www.honeywell.com',NULL,NULL,NULL,'String','script',NULL,'2011-02-14 16:28:38',NULL,0),('8859295e389a11e0a394b8ac6faea91c','simple.dashboard.link1','http://www.akuacom.com',NULL,NULL,NULL,'String','script',NULL,'2011-02-14 16:28:38',NULL,0),('8859e8a8389a11e0a394b8ac6faea91c','simple.dashboard.usage','YY',NULL,NULL,NULL,'String','script',NULL,'2011-02-14 16:28:38',NULL,0),('885a663e389a11e0a394b8ac6faea91c','simple.dashboard.akualogo',NULL,'\0',NULL,NULL,'Boolean','script',NULL,'2011-02-14 16:28:38',NULL,0),('885b0300389a11e0a394b8ac6faea91c','simple.dashboard.rssfeed',NULL,'\0',NULL,NULL,'Boolean','script',NULL,'2011-02-14 16:28:38',NULL,0),('885b80d2389a11e0a394b8ac6faea91c','simple.dashboard.optout',NULL,'\0',NULL,NULL,'Boolean','script',NULL,'2011-02-14 16:28:38',NULL,0),('885c07fa389a11e0a394b8ac6faea91c','simple.dashboard.refreshinterval',NULL,NULL,900000,NULL,'Number','script',NULL,'2011-02-14 16:28:38',NULL,0),('c0c17a9487af11e0abba98e6b6de3777','iir.client.push',NULL,'\0',NULL,NULL,'Boolean','script',NULL,'2011-05-26 08:49:34',NULL,0),('c0c1ba5e87af11e0abba98e6b6de3777','utility.program.sort',NULL,'\0',NULL,NULL,'Boolean','script',NULL,'2011-05-26 08:49:34',NULL,0),('c141dad687af11e0abba98e6b6de3777','enableClientsAutoEnrollInProgram',NULL,'\0',NULL,NULL,'Boolean','script',NULL,'2011-05-26 08:49:35',NULL,0),('c142d89687af11e0abba98e6b6de3777','enableAggregation',NULL,'\0',NULL,NULL,'Boolean','script',NULL,'2011-05-26 08:49:35',NULL,0),('c143303487af11e0abba98e6b6de3777','simple.dashboard.default.zipcode','90001',NULL,NULL,NULL,'String','script',NULL,'2011-05-26 08:49:35',NULL,0),('c143f5dc87af11e0abba98e6b6de3777','feature.clientOptOut',NULL,'\0',NULL,NULL,'Boolean','script',NULL,'2011-05-26 08:49:35',NULL,0),('cd2a042a4a8f11e0abf438e5d477d8d5','simple.dashboard.link2name','HONEYWELL',NULL,NULL,NULL,'String','script',NULL,'2011-03-09 12:57:10',NULL,0),('cd2a1e1a4a8f11e0abf438e5d477d8d5','simple.dashboard.link1name','AKUACOM',NULL,NULL,NULL,'String','script',NULL,'2011-03-09 12:57:10',NULL,0),('cd2a32f64a8f11e0abf438e5d477d8d5','simple.dashboard.linkenable',NULL,'\0',NULL,NULL,'Boolean','script',NULL,'2011-03-09 12:57:10',NULL,0),('d6929b5206de11e09bc70382820593d2','utilityName','demo',NULL,NULL,NULL,'String','script',NULL,'2010-12-13 09:31:37',NULL,0),('d692b02e06de11e09bc70382820593d2','utilityDisplayName','Akuacom',NULL,NULL,NULL,'String','script',NULL,'2010-12-13 09:31:37',NULL,0),('d692c2e406de11e09bc70382820593d2','version','',NULL,NULL,NULL,'String','script',NULL,'2010-12-13 09:31:37',NULL,0),('d692d1f806de11e09bc70382820593d2','contactURLDisplayName','Automated Demand Response Program',NULL,NULL,NULL,'String','script',NULL,'2010-12-13 09:31:37',NULL,0),('d692e0d006de11e09bc70382820593d2','contactURLLink','http://www.auto-dr.com/',NULL,NULL,NULL,'String','script',NULL,'2010-12-13 09:31:37',NULL,0),('d692f10606de11e09bc70382820593d2','contactPhone','1-877-789-6237',NULL,NULL,NULL,'String','script',NULL,'2010-12-13 09:31:37',NULL,0),('d69300d806de11e09bc70382820593d2','contactEMail','noop@akuacom.com',NULL,NULL,NULL,'String','script',NULL,'2010-12-13 09:31:37',NULL,0),('d6930ff606de11e09bc70382820593d2','offlineWarningThresholdM',NULL,NULL,3,NULL,'Number','script',NULL,'2010-12-13 09:31:37',NULL,0),('d6931f0a06de11e09bc70382820593d2','logCategories','AccountManagerServerLoginModule,comms,event,ProgramManagerBean,webservice,SlowMethodInterceptor',NULL,NULL,NULL,'String','script',NULL,'2010-12-13 09:31:37',NULL,0),('d6932e2806de11e09bc70382820593d2','logoUrls','/images/logos/pierlogo.gif,/images/logos/lbl_logo.gif,/images/logos/drrclogo.gif',NULL,NULL,NULL,'String','script',NULL,'2010-12-13 09:31:37',NULL,0),('d6933d0a06de11e09bc70382820593d2','firelog','firelog-3.2',NULL,NULL,NULL,'String','script',NULL,'2010-12-13 09:31:37',NULL,0),('d6935f6006de11e09bc70382820593d2','loginMessage','For technical support please contact us at 1-877-789-6237 or email us at ops@akuacom.com  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;',NULL,NULL,NULL,'String','script',NULL,'2010-12-13 09:31:37',NULL,0),('d693702c06de11e09bc70382820593d2','offSeasonStartMonth',NULL,NULL,10,NULL,'Number','script',NULL,'2010-12-13 09:31:37',NULL,0),('d693829c06de11e09bc70382820593d2','offSeasonStartDay',NULL,NULL,1,NULL,'Number','script',NULL,'2010-12-13 09:31:37',NULL,0),('d693922806de11e09bc70382820593d2','onSeasonStartMonth',NULL,NULL,6,NULL,'Number','script',NULL,'2010-12-13 09:31:37',NULL,0),('d693a13c06de11e09bc70382820593d2','onSeasonStartDay',NULL,NULL,1,NULL,'Number','script',NULL,'2010-12-13 09:31:37',NULL,0),('d693b10e06de11e09bc70382820593d2','serverHost','http://localhost:8080',NULL,NULL,NULL,'String','script',NULL,'2010-12-13 09:31:37',NULL,0),('d693c00406de11e09bc70382820593d2','mapKey','ABQIAAAAkbYeOdRre6c-Rgijp4VrrxTwM0brOpm-All5BF6PoaKBxRWWERRP8-DeZLqoCAKVc_2eYIQzR5hiJA',NULL,NULL,NULL,'String','script',NULL,'2010-12-13 09:31:37',NULL,0),('d693cfe006de11e09bc70382820593d2','feature.feedback',NULL,'\0',NULL,NULL,'Boolean','script',NULL,'2010-12-13 09:31:37',NULL,0),('d693de6806de11e09bc70382820593d2','feature.location',NULL,'\0',NULL,NULL,'Boolean','script',NULL,'2010-12-13 09:31:37',NULL,0),('d693ecc806de11e09bc70382820593d2','feature.shedInfo',NULL,'\0',NULL,NULL,'Boolean','script',NULL,'2010-12-13 09:31:37',NULL,0),('d693fb2806de11e09bc70382820593d2','feature.rules',NULL,'\0',NULL,NULL,'Boolean','script',NULL,'2010-12-13 09:31:37',NULL,0),('d694099206de11e09bc70382820593d2','feature.extendedNotification',NULL,'\0',NULL,NULL,'Boolean','script',NULL,'2010-12-13 09:31:37',NULL,0),('d69417ac06de11e09bc70382820593d2','feature.facdashDefaultPage','clients',NULL,NULL,NULL,'String','script',NULL,'2010-12-13 09:31:37',NULL,0),('d69425bc06de11e09bc70382820593d2','feature.subAccounts',NULL,'\0',NULL,NULL,'Boolean','script',NULL,'2010-12-13 09:31:37',NULL,0),('d694341206de11e09bc70382820593d2','feature.news',NULL,'\0',NULL,NULL,'Boolean','script',NULL,'2010-12-13 09:31:37',NULL,0),('d69451d606de11e09bc70382820593d2','feature.usage',NULL,'\0',NULL,NULL,'Boolean','script',NULL,'2010-12-13 09:31:37',NULL,0),('d694606806de11e09bc70382820593d2','feature.reportEventList',NULL,'',NULL,NULL,'Boolean','script',NULL,'2010-12-13 09:31:37',NULL,0),('d6946ee606de11e09bc70382820593d2','feature.reportAccountInfo',NULL,'\0',NULL,NULL,'Boolean','script',NULL,'2010-12-13 09:31:37',NULL,0),('d6947c7e06de11e09bc70382820593d2','feature.reportEventParticipation',NULL,'\0',NULL,NULL,'Boolean','script',NULL,'2010-12-13 09:31:37',NULL,0),('d6948ade06de11e09bc70382820593d2','feature.reportClientNonParticipation',NULL,'\0',NULL,NULL,'Boolean','script',NULL,'2010-12-13 09:31:37',NULL,0),('d694993406de11e09bc70382820593d2','feature.reportOffline',NULL,'\0',NULL,NULL,'Boolean','script',NULL,'2010-12-13 09:31:37',NULL,0),('d694a76206de11e09bc70382820593d2','feature.weather',NULL,'\0',NULL,NULL,'Boolean','script',NULL,'2010-12-13 09:31:37',NULL,0),('d694b57206de11e09bc70382820593d2','clientConfirmationLevel','MEDIUM',NULL,NULL,NULL,'String','script',NULL,'2010-12-13 09:31:37',NULL,0),('d694c33206de11e09bc70382820593d2','feature.peakchoice',NULL,'\0',NULL,NULL,'Boolean','script',NULL,'2010-12-13 09:31:37',NULL,0),('d694d17e06de11e09bc70382820593d2','emailContentType','text/plain',NULL,NULL,NULL,'String','script',NULL,'2010-12-13 09:31:37',NULL,0),('d694df2006de11e09bc70382820593d2','itronWSHostName','http://inter-act.pge.com/IEEWebServices/WebServices/CurtailmentOperations.asmx',NULL,NULL,NULL,'String','script',NULL,'2010-12-13 09:31:37',NULL,0),('db26c82806de11e09bc70382820593d2','rule.cppPrice.default.price',NULL,NULL,1.36229,NULL,'Number','script',NULL,'2010-12-13 09:31:45',NULL,0),('db26dee406de11e09bc70382820593d2','rule.cpp.default.mode','HIGH',NULL,NULL,NULL,'String','script',NULL,'2010-12-13 09:31:45',NULL,0),('db26eea206de11e09bc70382820593d2','feature.useCacheStore',NULL,'\0',NULL,NULL,'Boolean','script',NULL,'2010-12-13 09:31:45',NULL,0);
/*!40000 ALTER TABLE `core_property` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `custom_rule`
--

LOCK TABLES `custom_rule` WRITE;
/*!40000 ALTER TABLE `custom_rule` DISABLE KEYS */;
/*!40000 ALTER TABLE `custom_rule` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `dataentry`
--

LOCK TABLES `dataentry` WRITE;
/*!40000 ALTER TABLE `dataentry` DISABLE KEYS */;
/*!40000 ALTER TABLE `dataentry` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `dataentry_temp`
--

LOCK TABLES `dataentry_temp` WRITE;
/*!40000 ALTER TABLE `dataentry_temp` DISABLE KEYS */;
/*!40000 ALTER TABLE `dataentry_temp` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `dataset`
--

LOCK TABLES `dataset` WRITE;
/*!40000 ALTER TABLE `dataset` DISABLE KEYS */;
INSERT INTO `dataset` (`uuid`, `version`, `name`, `unit`, `sync`, `period`, `creator`, `modifier`, `creationTime`, `modifiedTime`, `calcImplClass`) VALUES ('d65cb1c206de11e09bc70382820593d2',0,'Usage','kWh',1,900,'script',NULL,'2010-12-13 09:31:37',NULL,NULL);
/*!40000 ALTER TABLE `dataset` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `datasource`
--

LOCK TABLES `datasource` WRITE;
/*!40000 ALTER TABLE `datasource` DISABLE KEYS */;
/*!40000 ALTER TABLE `datasource` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `dbp_event_creation`
--

LOCK TABLES `dbp_event_creation` WRITE;
/*!40000 ALTER TABLE `dbp_event_creation` DISABLE KEYS */;
/*!40000 ALTER TABLE `dbp_event_creation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `dbpevent`
--

LOCK TABLES `dbpevent` WRITE;
/*!40000 ALTER TABLE `dbpevent` DISABLE KEYS */;
/*!40000 ALTER TABLE `dbpevent` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `drwebsite_property`
--

LOCK TABLES `drwebsite_property` WRITE;
/*!40000 ALTER TABLE `drwebsite_property` DISABLE KEYS */;
INSERT INTO `drwebsite_property` (`uuid`, `version`, `propertyName`, `stringValue`, `textValue`, `creator`, `modifier`, `creationTime`, `modifiedTime`) VALUES ('400e7e04bc8611e097d4c402baa3011a',NULL,'COPYRIGHT_RTP',NULL,'*Actual billed prices may vary based on updated temperature information.Refer to the RTP tariff at <a href=\"http://www.sce.com/tariffbooks\">www.sce.com/tariffbooks</a> for current rate schedules.SCE is not responsible for any loss or damages resulting from inaccurate or changed information.','script',NULL,'2011-08-01 14:36:00',NULL),('400e92e0bc8611e097d4c402baa3011a',NULL,'COPYRIGHT_SCE',NULL,'Copyright @ 2010 Southern California Edison. All Rights Reserved','script',NULL,'2011-08-01 14:36:00',NULL),('400ea604bc8611e097d4c402baa3011a',NULL,'COPYRIGHT_ACTIVE_EVENTS_TITLE',NULL,'Welcome to the SCE DR Event Website.  It is recommended that you reload or refresh this site often to ensure you are viewing the most recent available information. This website is not intended as a substitute for receiving SCE event notifications.','script',NULL,'2011-08-01 14:36:00',NULL),('400eb496bc8611e097d4c402baa3011a',NULL,'COPYRIGHT_HISTORY_EVENTS_TITLE',NULL,'Search for event history by program and date range.  Past events do not indicate nor guarantee future event activity.','script',NULL,'2011-08-01 14:36:00',NULL),('400ec53abc8611e097d4c402baa3011a',NULL,'COPYRIGHT_ACTIVE_EVENTS_DBP',NULL,'DBP Events scheduled for tomorrow will not appear until after 6PM today.','script',NULL,'2011-08-01 14:36:00',NULL),('400ed84abc8611e097d4c402baa3011a',NULL,'COPYRIGHT_HISTORY_EVENTS_SEARCH_TITLE',NULL,'Search and export event histories by program.','script',NULL,'2011-08-01 14:36:00',NULL),('400ee740bc8611e097d4c402baa3011a',NULL,'TITLE_EVENT_STATUS_PAGE',NULL,'Demand Response Event Status','script',NULL,'2011-08-01 14:36:00',NULL),('400ef564bc8611e097d4c402baa3011a',NULL,'TITLE_EVENT_HISTORY_PAGE',NULL,'Event History','script',NULL,'2011-08-01 14:36:00',NULL),('400f0356bc8611e097d4c402baa3011a',NULL,'TITLE_EVENT_STATUS_RTP',NULL,'Based on Applicable Temperature of:','script',NULL,'2011-08-01 14:36:00',NULL),('400f10f8bc8611e097d4c402baa3011a',NULL,'CONTACT_NO',NULL,'1-866-334-7827','script',NULL,'2011-08-01 14:36:00',NULL),('400f1ea4bc8611e097d4c402baa3011a',NULL,'TBLCOL_PROGRAM_NAME',NULL,'Program Name','script',NULL,'2011-08-01 14:36:00',NULL),('400f2c46bc8611e097d4c402baa3011a',NULL,'TBLCOL_REGIONS',NULL,'Regions Affected','script',NULL,'2011-08-01 14:36:00',NULL),('400f39f2bc8611e097d4c402baa3011a',NULL,'TBLCOL_DATE',NULL,'Date','script',NULL,'2011-08-01 14:36:00',NULL),('400f4910bc8611e097d4c402baa3011a',NULL,'TBLCOL_START_TIME',NULL,'Start Time','script',NULL,'2011-08-01 14:36:00',NULL),('400f595abc8611e097d4c402baa3011a',NULL,'TBLCOL_END_TIME',NULL,'End Time','script',NULL,'2011-08-01 14:36:00',NULL),('400f6bdebc8611e097d4c402baa3011a',NULL,'TBLCOL_TEMPERATURE',NULL,'Applicable <br>Temperature','script',NULL,'2011-08-01 14:36:00',NULL),('400f7e4ebc8611e097d4c402baa3011a',NULL,'TBLCOL_PRICE_CATEGORY',NULL,'Expected Pricing Category*','script',NULL,'2011-08-01 14:36:00',NULL),('cfbff33475da11e098f8cce30e9adb7c',NULL,'COPYRIGHT_DRC',NULL,'Contact your DRC Aggregator for more information on DRC events.  <br><br>For more information on SCE\'s DRC program, please visit <a href=\"http://www.sce.com/drc\">www.sce.com/drc</a>.','script',NULL,'2011-05-03 16:12:26',NULL),('cfbff4w475da11e092f8cce30e9adb7c',NULL,'TBLCOL_PRICE_CATEGORY_HISTORY',NULL,'Pricing Category*','script',NULL,'2011-05-03 16:12:26',NULL),('cfbff4w475da11e098f8cce30e9adb7c',NULL,'TBLCOL_Date_Of_Usage',NULL,'Date of Usage','script',NULL,'2011-05-03 16:12:26',NULL),('cfbff9e475da11e098f8cce30e9adb7c',NULL,'COPYRIGHT_RTPFORECAST',NULL,'*SCE considers Expected Pricing Categories to be forward-looking statements, which are subject to risk and uncertainty; actual billed prices will be based on actual recorded high temperatures.   SCE is not responsible for any loss or damages resulting from inaccurate or changed information.  Refer to the RTP tariff at <a href=\"http://www.sce.com/tariffbooks\">www.sce.com/tariffbooks</a> for current rate schedules.','script',NULL,'2011-05-03 16:12:26',NULL),('cfbffbe475da11e098f8cce30e9adb7c',NULL,'RTPFORECAST_SUBTITLE',NULL,'5-Day Pricing Category Forecast','script',NULL,'2011-05-03 16:12:26',NULL),('cfbffwe475da11e098f8cce30e9adb7c',NULL,'COPYRIGHT_HISTORY_RTP_SEARCH',NULL,'*Historical data is provided as a reference only and is not intended to be a promise or guarantee of future activity or pricing.','script',NULL,'2011-05-03 16:12:26',NULL);
/*!40000 ALTER TABLE `drwebsite_property` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `event`
--

LOCK TABLES `event` WRITE;
/*!40000 ALTER TABLE `event` DISABLE KEYS */;
/*!40000 ALTER TABLE `event` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `event_bid_block`
--

LOCK TABLES `event_bid_block` WRITE;
/*!40000 ALTER TABLE `event_bid_block` DISABLE KEYS */;
/*!40000 ALTER TABLE `event_bid_block` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `event_participant`
--

LOCK TABLES `event_participant` WRITE;
/*!40000 ALTER TABLE `event_participant` DISABLE KEYS */;
/*!40000 ALTER TABLE `event_participant` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `event_participant_bid_entry`
--

LOCK TABLES `event_participant_bid_entry` WRITE;
/*!40000 ALTER TABLE `event_participant_bid_entry` DISABLE KEYS */;
/*!40000 ALTER TABLE `event_participant_bid_entry` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `event_participant_rule`
--

LOCK TABLES `event_participant_rule` WRITE;
/*!40000 ALTER TABLE `event_participant_rule` DISABLE KEYS */;
/*!40000 ALTER TABLE `event_participant_rule` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `event_participant_signal`
--

LOCK TABLES `event_participant_signal` WRITE;
/*!40000 ALTER TABLE `event_participant_signal` DISABLE KEYS */;
/*!40000 ALTER TABLE `event_participant_signal` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `event_participant_signal_entry`
--

LOCK TABLES `event_participant_signal_entry` WRITE;
/*!40000 ALTER TABLE `event_participant_signal_entry` DISABLE KEYS */;
/*!40000 ALTER TABLE `event_participant_signal_entry` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `event_signal`
--

LOCK TABLES `event_signal` WRITE;
/*!40000 ALTER TABLE `event_signal` DISABLE KEYS */;
/*!40000 ALTER TABLE `event_signal` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `event_signal_entry`
--

LOCK TABLES `event_signal_entry` WRITE;
/*!40000 ALTER TABLE `event_signal_entry` DISABLE KEYS */;
/*!40000 ALTER TABLE `event_signal_entry` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `event_template`
--

LOCK TABLES `event_template` WRITE;
/*!40000 ALTER TABLE `event_template` DISABLE KEYS */;
/*!40000 ALTER TABLE `event_template` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `event_template_signalentry`
--

LOCK TABLES `event_template_signalentry` WRITE;
/*!40000 ALTER TABLE `event_template_signalentry` DISABLE KEYS */;
/*!40000 ALTER TABLE `event_template_signalentry` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `event_timeblock`
--

LOCK TABLES `event_timeblock` WRITE;
/*!40000 ALTER TABLE `event_timeblock` DISABLE KEYS */;
/*!40000 ALTER TABLE `event_timeblock` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `history_baseline_dataentry`
--

LOCK TABLES `history_baseline_dataentry` WRITE;
/*!40000 ALTER TABLE `history_baseline_dataentry` DISABLE KEYS */;
/*!40000 ALTER TABLE `history_baseline_dataentry` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `history_event`
--

LOCK TABLES `history_event` WRITE;
/*!40000 ALTER TABLE `history_event` DISABLE KEYS */;
/*!40000 ALTER TABLE `history_event` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `history_event_participant`
--

LOCK TABLES `history_event_participant` WRITE;
/*!40000 ALTER TABLE `history_event_participant` DISABLE KEYS */;
/*!40000 ALTER TABLE `history_event_participant` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `history_event_participant_signal`
--

LOCK TABLES `history_event_participant_signal` WRITE;
/*!40000 ALTER TABLE `history_event_participant_signal` DISABLE KEYS */;
/*!40000 ALTER TABLE `history_event_participant_signal` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `history_procedure_logentry`
--

LOCK TABLES `history_procedure_logentry` WRITE;
/*!40000 ALTER TABLE `history_procedure_logentry` DISABLE KEYS */;
/*!40000 ALTER TABLE `history_procedure_logentry` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `message`
--

LOCK TABLES `message` WRITE;
/*!40000 ALTER TABLE `message` DISABLE KEYS */;
/*!40000 ALTER TABLE `message` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `message_filter_setting`
--

LOCK TABLES `message_filter_setting` WRITE;
/*!40000 ALTER TABLE `message_filter_setting` DISABLE KEYS */;
INSERT INTO `message_filter_setting` (`uuid`, `filterStatus`, `msgThreshold`, `frequency`, `duration`, `msgExpireTime`, `creationTime`, `cleanMsgHour`, `cleanMsgMinute`) VALUES ('0',1,10,1440,3,1440,'2010-12-13 09:31:46',0,0);
/*!40000 ALTER TABLE `message_filter_setting` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `noaa_weather`
--

LOCK TABLES `noaa_weather` WRITE;
/*!40000 ALTER TABLE `noaa_weather` DISABLE KEYS */;
/*!40000 ALTER TABLE `noaa_weather` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `participant`
--

LOCK TABLES `participant` WRITE;
/*!40000 ALTER TABLE `participant` DISABLE KEYS */;
/*!40000 ALTER TABLE `participant` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `participant_contact`
--

LOCK TABLES `participant_contact` WRITE;
/*!40000 ALTER TABLE `participant_contact` DISABLE KEYS */;
/*!40000 ALTER TABLE `participant_contact` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `participant_manualsignal`
--

LOCK TABLES `participant_manualsignal` WRITE;
/*!40000 ALTER TABLE `participant_manualsignal` DISABLE KEYS */;
/*!40000 ALTER TABLE `participant_manualsignal` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `peakchoice_message`
--

LOCK TABLES `peakchoice_message` WRITE;
/*!40000 ALTER TABLE `peakchoice_message` DISABLE KEYS */;
/*!40000 ALTER TABLE `peakchoice_message` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `program`
--

LOCK TABLES `program` WRITE;
/*!40000 ALTER TABLE `program` DISABLE KEYS */;
/*!40000 ALTER TABLE `program` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `program_matrix`
--

LOCK TABLES `program_matrix` WRITE;
/*!40000 ALTER TABLE `program_matrix` DISABLE KEYS */;
/*!40000 ALTER TABLE `program_matrix` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `program_participant`
--

LOCK TABLES `program_participant` WRITE;
/*!40000 ALTER TABLE `program_participant` DISABLE KEYS */;
/*!40000 ALTER TABLE `program_participant` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `program_participant_bid_entry`
--

LOCK TABLES `program_participant_bid_entry` WRITE;
/*!40000 ALTER TABLE `program_participant_bid_entry` DISABLE KEYS */;
/*!40000 ALTER TABLE `program_participant_bid_entry` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `program_participant_bid_level_map`
--

LOCK TABLES `program_participant_bid_level_map` WRITE;
/*!40000 ALTER TABLE `program_participant_bid_level_map` DISABLE KEYS */;
/*!40000 ALTER TABLE `program_participant_bid_level_map` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `program_participant_constraint`
--

LOCK TABLES `program_participant_constraint` WRITE;
/*!40000 ALTER TABLE `program_participant_constraint` DISABLE KEYS */;
/*!40000 ALTER TABLE `program_participant_constraint` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `program_participant_rule`
--

LOCK TABLES `program_participant_rule` WRITE;
/*!40000 ALTER TABLE `program_participant_rule` DISABLE KEYS */;
/*!40000 ALTER TABLE `program_participant_rule` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `program_rtp_config`
--

LOCK TABLES `program_rtp_config` WRITE;
/*!40000 ALTER TABLE `program_rtp_config` DISABLE KEYS */;
/*!40000 ALTER TABLE `program_rtp_config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `program_rule`
--

LOCK TABLES `program_rule` WRITE;
/*!40000 ALTER TABLE `program_rule` DISABLE KEYS */;
/*!40000 ALTER TABLE `program_rule` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `program_signal`
--

LOCK TABLES `program_signal` WRITE;
/*!40000 ALTER TABLE `program_signal` DISABLE KEYS */;
/*!40000 ALTER TABLE `program_signal` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `program_signal_entry`
--

LOCK TABLES `program_signal_entry` WRITE;
/*!40000 ALTER TABLE `program_signal_entry` DISABLE KEYS */;
/*!40000 ALTER TABLE `program_signal_entry` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `report_event`
--

LOCK TABLES `report_event` WRITE;
/*!40000 ALTER TABLE `report_event` DISABLE KEYS */;
/*!40000 ALTER TABLE `report_event` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `report_event_participation`
--

LOCK TABLES `report_event_participation` WRITE;
/*!40000 ALTER TABLE `report_event_participation` DISABLE KEYS */;
/*!40000 ALTER TABLE `report_event_participation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `report_event_signal`
--

LOCK TABLES `report_event_signal` WRITE;
/*!40000 ALTER TABLE `report_event_signal` DISABLE KEYS */;
/*!40000 ALTER TABLE `report_event_signal` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `rtp_ftp_configuration`
--

LOCK TABLES `rtp_ftp_configuration` WRITE;
/*!40000 ALTER TABLE `rtp_ftp_configuration` DISABLE KEYS */;
INSERT INTO `rtp_ftp_configuration` (`uuid`, `url`, `port`, `path`, `fileName`, `userName`, `password`, `scanStartTime`, `scanEndTime`, `scanInterval`, `required`, `creationTime`, `minTemperature`, `maxTemperature`, `lastProcessTime`, `sendNotification`) VALUES ('4010c452bc8611e097d4c402baa3011a',NULL,NULL,NULL,'yyyyMMdd_RTPprices.txt',NULL,NULL,'08:30','09:30','10','','2011-08-01 14:36:00',-80,130,NULL,'');
/*!40000 ALTER TABLE `rtp_ftp_configuration` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `rtp_remote_price`
--

LOCK TABLES `rtp_remote_price` WRITE;
/*!40000 ALTER TABLE `rtp_remote_price` DISABLE KEYS */;
/*!40000 ALTER TABLE `rtp_remote_price` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `scedbp_dispatch_config`
--

LOCK TABLES `scedbp_dispatch_config` WRITE;
/*!40000 ALTER TABLE `scedbp_dispatch_config` DISABLE KEYS */;
/*!40000 ALTER TABLE `scedbp_dispatch_config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `scertp_event`
--

LOCK TABLES `scertp_event` WRITE;
/*!40000 ALTER TABLE `scertp_event` DISABLE KEYS */;
/*!40000 ALTER TABLE `scertp_event` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `season_config`
--

LOCK TABLES `season_config` WRITE;
/*!40000 ALTER TABLE `season_config` DISABLE KEYS */;
/*!40000 ALTER TABLE `season_config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `signal_def`
--

LOCK TABLES `signal_def` WRITE;
/*!40000 ALTER TABLE `signal_def` DISABLE KEYS */;
/*!40000 ALTER TABLE `signal_def` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `signal_level_def`
--

LOCK TABLES `signal_level_def` WRITE;
/*!40000 ALTER TABLE `signal_level_def` DISABLE KEYS */;
/*!40000 ALTER TABLE `signal_level_def` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `sub_account`
--

LOCK TABLES `sub_account` WRITE;
/*!40000 ALTER TABLE `sub_account` DISABLE KEYS */;
/*!40000 ALTER TABLE `sub_account` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2011-08-01 15:31:02
