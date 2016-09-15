-- MySQL dump 10.9
--
-- Host: localhost    Database: a
-- ------------------------------------------------------
-- Server version	4.1.12-nt

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `domains`
--

DROP TABLE IF EXISTS `domains`;
CREATE TABLE `domains` (
  `domainname` varchar(24) NOT NULL default '',
  PRIMARY KEY  (`domainname`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `domains`
--


/*!40000 ALTER TABLE `domains` DISABLE KEYS */;
LOCK TABLES `domains` WRITE;
INSERT INTO `domains` VALUES ('PSS2');
UNLOCK TABLES;
/*!40000 ALTER TABLE `domains` ENABLE KEYS */;

--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
CREATE TABLE `roles` (
  `rolename` varchar(24) NOT NULL default '',
  PRIMARY KEY  (`rolename`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `roles`
--


/*!40000 ALTER TABLE `roles` DISABLE KEYS */;
LOCK TABLES `roles` WRITE;
INSERT INTO `roles` VALUES ('about'),('admin'),('channels'),('commdevstatus'),('logs'),('mysite'),('options'),('PGECPP'),('program'),('PSS2WS'),('sastatus'),('summary'),('users'),('website');
UNLOCK TABLES;
/*!40000 ALTER TABLE `roles` ENABLE KEYS */;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `id` bigint(20) NOT NULL auto_increment,
  `domainname` varchar(24) default NULL,
  `username` varchar(24) NOT NULL default '',
  `password` varchar(24) NOT NULL default '',
  `email` varchar(64) NOT NULL default '',
  `status` tinyint(4) NOT NULL default '0',
  PRIMARY KEY  (`id`),
  UNIQUE KEY `username` (`username`,`domainname`),
  UNIQUE KEY `email` (`email`,`domainname`),
  KEY `domainname` (`domainname`),
  CONSTRAINT `users_ibfk_1` FOREIGN KEY (`domainname`) REFERENCES `domains` (`domainname`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `users`
--


/*!40000 ALTER TABLE `users` DISABLE KEYS */;
LOCK TABLES `users` WRITE;
INSERT INTO `users` VALUES (1,'PSS2','Guest','Welcome','Guest',1),(2,'PSS2','operator','drasAkua','operator',1),(3,'PSS2','admin','drasAkua','admin',1),(12,'PSS2','installer1','installer1','installer1',1),(13,'PSS2','demo1','demo1','demo1',1),(14,'PSS2','demo2','demo2','demo2',1),(15,'PSS2','test','test','test',1);
UNLOCK TABLES;
/*!40000 ALTER TABLE `users` ENABLE KEYS */;

--
-- Table structure for table `tags`
--

DROP TABLE IF EXISTS `tags`;
CREATE TABLE `tags` (
  `id` bigint(20) NOT NULL auto_increment,
  `user_id` bigint(20) default NULL,
  `tagname` varchar(64) NOT NULL default '',
  `value` text NOT NULL,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `user_id` (`user_id`,`tagname`),
  CONSTRAINT `tags_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tags`
--


/*!40000 ALTER TABLE `tags` DISABLE KEYS */;
LOCK TABLES `tags` WRITE;
INSERT INTO `tags` VALUES (1,1,'UserType','Public'),(2, 2, 'UserType', 'operator'),(3, 3, 'UserType', 'admin'),(12, 12, 'UserType', 'Installation Coordinator'),(13, 13, 'UserType', 'FacilityManager'),(14,14,'UserType','FacilityManager'),(15,15,'UserType','FacilityManager');
UNLOCK TABLES;
/*!40000 ALTER TABLE `tags` ENABLE KEYS */;

--
-- Table structure for table `userrole`
--

DROP TABLE IF EXISTS `userrole`;
CREATE TABLE `userrole` (
  `user_id` bigint(20) NOT NULL default '0',
  `rolename` varchar(24) NOT NULL default '',
  UNIQUE KEY `user_id` (`user_id`,`rolename`),
  KEY `rolename` (`rolename`),
  CONSTRAINT `userrole_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `userrole_ibfk_2` FOREIGN KEY (`rolename`) REFERENCES `roles` (`rolename`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `userrole`
--


/*!40000 ALTER TABLE `userrole` DISABLE KEYS */;
LOCK TABLES `userrole` WRITE;
INSERT INTO `userrole` VALUES (1,'about'),(2,'about'),(3,'about'),(12,'about'),(3,'admin'),(2,'channels'),(3,'channels'),(2,'commdevstatus'),(3,'commdevstatus'),(12,'commdevstatus'),(2,'logs'),(3,'logs'),(12,'logs'),(13,'mysite'),(14,'mysite'),(15,'mysite'),(2,'options'),(3,'options'),(12,'options'),(13,'options'),(14,'options'),(15,'options'),(2,'PGECPP'),(3,'PGECPP'),(2,'program'),(3,'program'),(2,'PSS2WS'),(3,'PSS2WS'),(13,'PSS2WS'),(14,'PSS2WS'),(15,'PSS2WS'),(2,'sastatus'),(3,'sastatus'),(1,'summary'),(2,'summary'),(3,'summary'),(12,'summary'),(13,'summary'),(14,'summary'),(15,'summary'),(2,'users'),(3,'users'),(1,'website'),(2,'website'),(3,'website'),(12,'website'),(13,'website'),(14,'website'),(15,'website');
UNLOCK TABLES;
/*!40000 ALTER TABLE `userrole` ENABLE KEYS */;

--
-- Table structure for table `friends`
--

DROP TABLE IF EXISTS `friends`;
CREATE TABLE `friends` (
  `id` bigint(20) NOT NULL auto_increment,
  `user_id` bigint(20) NOT NULL default '0',
  `friend_id` bigint(20) NOT NULL default '0',
  PRIMARY KEY  (`id`),
  UNIQUE KEY `user_id` (`user_id`,`friend_id`),
  KEY `friend_id` (`friend_id`),
  CONSTRAINT `friends_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `friends_ibfk_2` FOREIGN KEY (`friend_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `friends`
--


/*!40000 ALTER TABLE `friends` DISABLE KEYS */;
LOCK TABLES `friends` WRITE;
UNLOCK TABLES;
/*!40000 ALTER TABLE `friends` ENABLE KEYS */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

