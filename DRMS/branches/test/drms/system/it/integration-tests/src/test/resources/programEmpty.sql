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

-- Dump completed on 2010-12-28 12:06:41
