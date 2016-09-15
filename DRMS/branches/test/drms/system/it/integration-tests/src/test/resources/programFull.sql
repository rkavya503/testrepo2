-- MySQL dump 10.13  Distrib 5.1.52, for redhat-linux-gnu (x86_64)
--
-- Host: localhost    Database: pss2
-- ------------------------------------------------------
-- Server version	5.1.52

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
  `testProgram` bit(1) DEFAULT NULL,
  `validatorClass` varchar(255) DEFAULT NULL,
  `validatorConfigFile` varchar(255) DEFAULT NULL,
  `uiConfigureProgramString` varchar(255) DEFAULT NULL,
  `uiConfigureEventString` varchar(255) DEFAULT NULL,
  `uiScheduleEventString` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `repeatingEvents` bit(1) DEFAULT NULL,
  `autoRepeatTimeOfDay` time DEFAULT NULL,
  `lastErrorOpContact` datetime DEFAULT NULL,
  `autoAccept` bit(1) DEFAULT b'0',
  `programType` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`uuid`),
  UNIQUE KEY `priorityKey` (`priority`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `program`
--

LOCK TABLES `program` WRITE;
/*!40000 ALTER TABLE `program` DISABLE KEYS */;
INSERT INTO `program` VALUES ('ff8080812d2e745a012d2e7489c5000f',0,0,'CPP',NULL,NULL,0,'',23,14,14,18,18,59,0,0,0,0,360,360,21,0,'','system',NULL,'2010-12-28 11:29:05',NULL,'','CPP','com.akuacom.pss2.program.cpp.CPPProgramEJB',1,'\0','com.akuacom.pss2.program.cpp.CPPValidator','cpp_vcd.validator.config','','','CPPSchedulePage',NULL,'\0',NULL,NULL,'\0','CPPProgram'),('ff8080812d2e745a012d2e7489fc0013',0,1,'DBP DA',NULL,NULL,0,'',23,12,18,14,20,59,0,0,0,0,120,480,21,0,'','system',NULL,'2010-12-28 11:29:05',NULL,'','DBP DA','com.akuacom.pss2.program.dbp.DBPNoBidProgramEJB',1,'\0','com.akuacom.pss2.program.dbp.DBPNoBidValidator','dbp-da-single.validator.config','','','DBPNoBidSchedulePage',NULL,NULL,NULL,NULL,'\0','DBPProgram'),('ff8080812d2e745a012d2e748a290020',0,2,'RTP Agricultural',NULL,NULL,0,'',23,0,23,0,23,59,0,59,0,59,1,1440,21,0,'','system',NULL,'2010-12-28 11:29:05',NULL,'','RTP Agricultural','com.akuacom.pss2.program.scertp.SCERTPProgramEJB',1,'\0','com.akuacom.pss2.program.scertp.SCERTPValidator','cpp.validator.config','','','ButtonOnlySchedulePage',NULL,NULL,NULL,NULL,'\0','SCERTPProgram'),('ff8080812d2e745a012d2e748a3c0025',0,3,'RTP <2K',NULL,NULL,0,'',23,0,23,0,23,59,0,59,0,59,1,1440,21,0,'','system',NULL,'2010-12-28 11:29:05',NULL,'','RTP <2K','com.akuacom.pss2.program.scertp.SCERTPProgramEJB',1,'\0','com.akuacom.pss2.program.scertp.SCERTPValidator','cpp.validator.config','','','ButtonOnlySchedulePage',NULL,NULL,NULL,NULL,'\0','SCERTPProgram'),('ff8080812d2e745a012d2e748a4a002a',0,4,'RTP 2K-50K',NULL,NULL,0,'',23,0,23,0,23,59,0,59,0,59,1,1440,21,0,'','system',NULL,'2010-12-28 11:29:05',NULL,'','RTP 2K-50K','com.akuacom.pss2.program.scertp.SCERTPProgramEJB',1,'\0','com.akuacom.pss2.program.scertp.SCERTPValidator','cpp.validator.config','','','ButtonOnlySchedulePage',NULL,NULL,NULL,NULL,'\0','SCERTPProgram'),('ff8080812d2e745a012d2e748a64002f',0,5,'RTP >50K',NULL,NULL,0,'',23,0,23,0,23,59,0,59,0,59,1,1440,21,0,'','system',NULL,'2010-12-28 11:29:05',NULL,'','RTP >50K','com.akuacom.pss2.program.scertp.SCERTPProgramEJB',1,'\0','com.akuacom.pss2.program.scertp.SCERTPValidator','cpp.validator.config','','','ButtonOnlySchedulePage',NULL,NULL,NULL,NULL,'\0','SCERTPProgram'),('ff8080812d2e745a012d2e748a910034',0,6,'DEMO',NULL,NULL,0,'\0',23,0,23,0,23,59,0,59,0,59,1,1440,21,0,'','system',NULL,'2010-12-28 11:29:05',NULL,'','DEMO','com.akuacom.pss2.program.demo.DemoProgramEJB',1,'\0','com.akuacom.pss2.program.demo.DemoValidator','cpp.validator.config','','','DemoSchedulePage',NULL,NULL,NULL,NULL,'\0','DemoProgram'),('ff8080812d2e745a012d2e748a9c003a',0,7,'DRC-ENODO',NULL,NULL,0,'\0',23,11,18,12,19,59,0,0,0,0,60,480,21,0,'','system',NULL,'2010-12-28 11:29:05',NULL,'','DRC-ENODO','com.akuacom.pss2.program.cpp.CPPProgramEJB',1,'\0','com.akuacom.pss2.program.cpp.CPPValidator','cpp_vcd.validator.config','','','CPPSchedulePage',NULL,'\0',NULL,NULL,'\0','CPPProgram'),('ff8080812d2e745a012d2e748aa5003d',0,8,'DRC-CPIDA',NULL,NULL,0,'',23,11,18,12,19,59,0,0,0,0,60,480,21,0,'','system',NULL,'2010-12-28 11:29:05',NULL,'','DRC-CPIDA','com.akuacom.pss2.program.cpp.CPPProgramEJB',1,'\0','com.akuacom.pss2.program.cpp.CPPValidator','cpp_vcd.validator.config','','','CPPSchedulePage',NULL,'\0',NULL,NULL,'\0','CPPProgram'),('ff8080812d2e745a012d2e748aae0040',0,9,'DRC-CPIDO',NULL,NULL,0,'\0',23,11,18,12,19,59,0,0,0,0,60,480,21,0,'','system',NULL,'2010-12-28 11:29:05',NULL,'','DRC-CPIDO','com.akuacom.pss2.program.cpp.CPPProgramEJB',1,'\0','com.akuacom.pss2.program.cpp.CPPValidator','cpp_vcd.validator.config','','','CPPSchedulePage',NULL,'\0',NULL,NULL,'\0','CPPProgram'),('ff8080812d2e745a012d2e748ab60043',0,10,'DRC-NAPDO',NULL,NULL,0,'\0',23,10,17,11,18,59,0,0,0,0,60,480,21,0,'','system',NULL,'2010-12-28 11:29:05',NULL,'','DRC-NAPDO','com.akuacom.pss2.program.cpp.CPPProgramEJB',1,'\0','com.akuacom.pss2.program.cpp.CPPValidator','cpp_vcd.validator.config','','','CPPSchedulePage',NULL,'\0',NULL,NULL,'\0','CPPProgram'),('ff8080812d2e745a012d2e748ac00046',0,11,'DRC-ECIDA',NULL,NULL,0,'',23,10,17,11,18,59,0,0,0,0,60,480,21,0,'','system',NULL,'2010-12-28 11:29:05',NULL,'','DRC-ECIDA','com.akuacom.pss2.program.cpp.CPPProgramEJB',1,'\0','com.akuacom.pss2.program.cpp.CPPValidator','cpp_vcd.validator.config','','','CPPSchedulePage',NULL,'\0',NULL,NULL,'\0','CPPProgram'),('ff8080812d2e745a012d2e748acf0049',0,12,'DRC-AERDO',NULL,NULL,0,'\0',23,11,18,12,19,59,0,0,0,0,60,480,21,0,'','system',NULL,'2010-12-28 11:29:05',NULL,'','DRC-AERDO','com.akuacom.pss2.program.cpp.CPPProgramEJB',1,'\0','com.akuacom.pss2.program.cpp.CPPValidator','cpp_vcd.validator.config','','','CPPSchedulePage',NULL,'\0',NULL,NULL,'\0','CPPProgram'),('ff8080812d2e745a012d2e748ad9004c',0,13,'CBP 1-4 DO',NULL,NULL,0,'\0',23,11,18,12,19,59,0,0,0,0,60,240,21,0,'','system',NULL,'2010-12-28 11:29:05',NULL,'','CBP 1-4 DO','com.akuacom.pss2.program.cpp.CPPProgramEJB',1,'\0','com.akuacom.pss2.program.cpp.CPPValidator','cpp_vcd.validator.config','','','CPPSchedulePage',NULL,'\0',NULL,NULL,'\0','CPPProgram'),('ff8080812d2e745a012d2e748ae8004f',0,14,'CBP 2-6 DO',NULL,NULL,0,'\0',23,11,17,13,19,59,0,0,0,0,120,360,21,0,'','system',NULL,'2010-12-28 11:29:05',NULL,'','CBP 2-6 DO','com.akuacom.pss2.program.cpp.CPPProgramEJB',1,'\0','com.akuacom.pss2.program.cpp.CPPValidator','cpp_vcd.validator.config','','','CPPSchedulePage',NULL,'\0',NULL,NULL,'\0','CPPProgram'),('ff8080812d2e745a012d2e748af70052',0,15,'CBP 4-8 DO',NULL,NULL,0,'\0',23,11,15,15,19,59,0,0,0,0,240,480,21,0,'','system',NULL,'2010-12-28 11:29:05',NULL,'','CBP 4-8 DO','com.akuacom.pss2.program.cpp.CPPProgramEJB',1,'\0','com.akuacom.pss2.program.cpp.CPPValidator','cpp_vcd.validator.config','','','CPPSchedulePage',NULL,'\0',NULL,NULL,'\0','CPPProgram'),('ff8080812d2e745a012d2e748b010055',0,16,'CBP 1-4 DA',NULL,NULL,0,'',23,11,18,12,19,59,0,0,0,0,60,240,21,0,'','system',NULL,'2010-12-28 11:29:05',NULL,'','CBP 1-4 DA','com.akuacom.pss2.program.cpp.CPPProgramEJB',1,'\0','com.akuacom.pss2.program.cpp.CPPValidator','cpp_vcd.validator.config','','','CPPSchedulePage',NULL,'\0',NULL,NULL,'\0','CPPProgram'),('ff8080812d2e745a012d2e748b090058',0,17,'CBP 2-6 DA',NULL,NULL,0,'',23,11,17,13,19,59,0,0,0,0,120,360,21,0,'','system',NULL,'2010-12-28 11:29:05',NULL,'','CBP 2-6 DA','com.akuacom.pss2.program.cpp.CPPProgramEJB',1,'\0','com.akuacom.pss2.program.cpp.CPPValidator','cpp_vcd.validator.config','','','CPPSchedulePage',NULL,'\0',NULL,NULL,'\0','CPPProgram'),('ff8080812d2e745a012d2e748b12005b',0,18,'CBP 4-8 DA',NULL,NULL,0,'',23,11,15,15,19,59,0,0,0,0,240,480,21,0,'','system',NULL,'2010-12-28 11:29:05',NULL,'','CBP 4-8 DA','com.akuacom.pss2.program.cpp.CPPProgramEJB',1,'\0','com.akuacom.pss2.program.cpp.CPPValidator','cpp_vcd.validator.config','','','CPPSchedulePage',NULL,'\0',NULL,NULL,'\0','CPPProgram'),('ff8080812d2e745a012d2e749419005e',0,10722,'wxuvcytc','2010-12-28 11:29:08','2010-12-28 11:29:08',418325715,'\0',964196286,1333538712,1774122014,294904211,1862015935,200285990,1565825669,1222877858,1921490302,441596308,420240594,742860513,1429690932,2117066896,'leonmnwb','a','sdkmjuxc','2010-12-28 11:29:08','2010-12-28 11:29:08','\0','wxuvcytc','CPPProgramEJBBean',1084640366,'\0','com.akuacom.pss2.program.cpp.CPPValidator','tgatmwhu','wxuvcytc','leonmnwb','uhmxowsm','wxuvcytc','\0','11:29:08','2010-12-28 11:29:08','','Program'),('ff8080812d2e745a012d2e749433005f',0,25294,'gephdlpn','2010-12-28 11:29:08','2010-12-28 11:29:08',1124941430,'',133870300,1165429751,186451728,1238543655,2065565744,313478532,1453487477,251145721,372906406,347846639,1830410354,882497088,1088281102,1426916613,'gephdlpn','a','gephdlpn','2010-12-28 11:29:08','2010-12-28 11:29:08','','gephdlpn','CPPProgramEJBBean',110799822,'','com.akuacom.pss2.program.cpp.CPPValidator','gephdlpn','gephdlpn','gephdlpn','gephdlpn','gephdlpn','\0','11:29:08','2010-12-28 11:29:08','\0','Program');
/*!40000 ALTER TABLE `program` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2010-12-28 12:01:47
