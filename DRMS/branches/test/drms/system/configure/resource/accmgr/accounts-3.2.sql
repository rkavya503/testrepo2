-- MySQL dump 10.9
--
-- Host: localhost    Database: accounts
-- ------------------------------------------------------
-- Server version	4.1.18-nt

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `akua_users_domains`
--

DROP TABLE IF EXISTS `akua_users_domains`;
CREATE TABLE `akua_users_domains` (
  `rtd_user_id` bigint(20) default NULL,
  `rtd_domain_id` bigint(20) default NULL,
  KEY `fk_rtd_user_id` (`rtd_user_id`),
  KEY `fk_rtd_domain_id` (`rtd_domain_id`),
  CONSTRAINT `fk_rtd_domain_id` FOREIGN KEY (`rtd_domain_id`) REFERENCES `akuadomain` (`id`),
  CONSTRAINT `fk_rtd_user_id` FOREIGN KEY (`rtd_user_id`) REFERENCES `akuauser` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `akua_users_domains`
--


/*!40000 ALTER TABLE `akua_users_domains` DISABLE KEYS */;
LOCK TABLES `akua_users_domains` WRITE;
INSERT INTO `akua_users_domains` VALUES (1,1),(2,1),(3,1),(12,1),(13,1),(14,1),(15,1);
UNLOCK TABLES;
/*!40000 ALTER TABLE `akua_users_domains` ENABLE KEYS */;

--
-- Table structure for table `akua_users_roles`
--

DROP TABLE IF EXISTS `akua_users_roles`;
CREATE TABLE `akua_users_roles` (
  `rt_user_id` bigint(20) default NULL,
  `rt_role_id` bigint(20) default NULL,
  KEY `fk_rt_user_id` (`rt_user_id`),
  KEY `fk_rt_role_id` (`rt_role_id`),
  CONSTRAINT `fk_rt_role_id` FOREIGN KEY (`rt_role_id`) REFERENCES `akuaroles` (`id`),
  CONSTRAINT `fk_rt_user_id` FOREIGN KEY (`rt_user_id`) REFERENCES `akuauser` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `akua_users_roles`
--


/*!40000 ALTER TABLE `akua_users_roles` DISABLE KEYS */;
LOCK TABLES `akua_users_roles` WRITE;
INSERT INTO `akua_users_roles` VALUES (1,2),(1,4),(1,13),(2,2),(2,4),(2,5),(2,6),(2,8),(2,9),(2,10),(2,11),(2,12),(2,13),(2,1),(2,3),(3,14),(3,2),(3,4),(3,5),(3,6),(3,8),(3,9),(3,10),(3,11),(3,12),(3,13),(3,1),(3,3),(12,2),(12,4),(12,6),(12,8),(12,12),(12,13),(13,2),(13,4),(13,7),(13,12),(13,3),(14,2),(14,4),(14,7),(14,12),(14,3),(15,2),(15,4),(15,7),(15,12),(15,3);
UNLOCK TABLES;
/*!40000 ALTER TABLE `akua_users_roles` ENABLE KEYS */;

--
-- Table structure for table `akuadomain`
--

DROP TABLE IF EXISTS `akuadomain`;
CREATE TABLE `akuadomain` (
  `id` bigint(20) NOT NULL auto_increment,
  `domainname` varchar(24) default NULL,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `domainname` (`domainname`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `akuadomain`
--


/*!40000 ALTER TABLE `akuadomain` DISABLE KEYS */;
LOCK TABLES `akuadomain` WRITE;
INSERT INTO `akuadomain` VALUES (1,'PSS2');
UNLOCK TABLES;
/*!40000 ALTER TABLE `akuadomain` ENABLE KEYS */;

--
-- Table structure for table `akuafriends`
--

DROP TABLE IF EXISTS `akuafriends`;
CREATE TABLE `akuafriends` (
  `id` bigint(20) NOT NULL auto_increment,
  `userId` bigint(20) default NULL,
  `friendId` bigint(20) default NULL,
  PRIMARY KEY  (`id`),
  KEY `fk_userId` (`userId`),
  KEY `fk_friendId` (`friendId`),
  CONSTRAINT `fk_friendId` FOREIGN KEY (`friendId`) REFERENCES `akuauser` (`id`),
  CONSTRAINT `fk_userId` FOREIGN KEY (`userId`) REFERENCES `akuauser` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `akuafriends`
--


/*!40000 ALTER TABLE `akuafriends` DISABLE KEYS */;
LOCK TABLES `akuafriends` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `akuafriends` ENABLE KEYS */;

--
-- Table structure for table `akuaroles`
--

DROP TABLE IF EXISTS `akuaroles`;
CREATE TABLE `akuaroles` (
  `id` bigint(20) NOT NULL auto_increment,
  `rolename` varchar(24) default NULL,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `rolename` (`rolename`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `akuaroles`
--


/*!40000 ALTER TABLE `akuaroles` DISABLE KEYS */;
LOCK TABLES `akuaroles` WRITE;
INSERT INTO `akuaroles` VALUES (13,'about'),(14,'admin'),(9,'channels'),(6,'commdevstatus'),(8,'logs'),(7,'mysite'),(12,'options'),(1,'PGECPP'),(11,'program'),(3,'PSS2WS'),(5,'sastatus'),(4,'summary'),(10,'users'),(2,'website');
UNLOCK TABLES;
/*!40000 ALTER TABLE `akuaroles` ENABLE KEYS */;

--
-- Table structure for table `akuauser`
--

DROP TABLE IF EXISTS `akuauser`;
CREATE TABLE `akuauser` (
  `id` bigint(20) NOT NULL auto_increment,
  `username` varchar(24) default NULL,
  `password` varchar(24) default NULL,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `akuauser`
--


/*!40000 ALTER TABLE `akuauser` DISABLE KEYS */;
LOCK TABLES `akuauser` WRITE;
INSERT INTO `akuauser` VALUES (1,'Guest','Welcome'),(2,'operator','drasAkua'),(3,'admin','drasAkua'),(12,'installer1','installer1'),(13,'demo1','demo1'),(14,'demo2','demo2'),(15,'test','test');
UNLOCK TABLES;
/*!40000 ALTER TABLE `akuauser` ENABLE KEYS */;

--
-- Table structure for table `akuauser_tags`
--

DROP TABLE IF EXISTS `akuauser_tags`;
CREATE TABLE `akuauser_tags` (
  `id` bigint(20) NOT NULL auto_increment,
  `tag_user_id` bigint(20) default NULL,
  `tagname` varchar(24) default NULL,
  `tagvalue` varchar(24) default NULL,
  PRIMARY KEY  (`id`),
  KEY `fk_tag_user_id` (`tag_user_id`),
  CONSTRAINT `fk_tag_user_id` FOREIGN KEY (`tag_user_id`) REFERENCES `akuauser` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `akuauser_tags`
--


/*!40000 ALTER TABLE `akuauser_tags` DISABLE KEYS */;
LOCK TABLES `akuauser_tags` WRITE;
INSERT INTO `akuauser_tags` VALUES (1,1,'UserType','Public'),(2,2,'UserType','operator'),(3,3,'UserType','admin'),(4,NULL,'UserType','FacilityManager'),(5,NULL,'UserType','FacilityManager'),(6,NULL,'UserType','FacilityManager'),(7,NULL,'UserType','FacilityManager'),(8,NULL,'UserType','FacilityManager'),(9,NULL,'UserType','FacilityManager'),(10,NULL,'UserType','FacilityManager'),(11,NULL,'UserType','FacilityManager'),(12,NULL,'UserType','FacilityManager'),(13,NULL,'UserType','FacilityManager'),(14,NULL,'UserType','FacilityManager'),(15,NULL,'UserType','FacilityManager'),(16,NULL,'UserType','FacilityManager'),(17,NULL,'UserType','FacilityManager'),(18,NULL,'UserType','FacilityManager'),(19,NULL,'UserType','FacilityManager'),(20,NULL,'UserType','FacilityManager'),(21,NULL,'UserType','FacilityManager'),(22,NULL,'UserType','FacilityManager'),(23,NULL,'UserType','FacilityManager'),(24,NULL,'UserType','FacilityManager'),(25,NULL,'UserType','FacilityManager'),(26,NULL,'UserType','FacilityManager'),(27,NULL,'UserType','FacilityManager'),(28,NULL,'UserType','FacilityManager'),(29,NULL,'UserType','FacilityManager'),(30,NULL,'UserType','FacilityManager'),(31,NULL,'UserType','FacilityManager'),(32,NULL,'UserType','FacilityManager'),(33,NULL,'UserType','FacilityManager'),(34,NULL,'UserType','FacilityManager'),(35,NULL,'UserType','FacilityManager'),(36,NULL,'UserType','Public'),(37,12,'UserType','Installation Coordinator'),(38,13,'UserType','FacilityManager'),(39,14,'UserType','FacilityManager'),(40,15,'UserType','FacilityManager');
UNLOCK TABLES;
/*!40000 ALTER TABLE `akuauser_tags` ENABLE KEYS */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

