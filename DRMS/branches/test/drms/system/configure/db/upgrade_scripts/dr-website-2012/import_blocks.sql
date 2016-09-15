
USE drwebsite;

DROP TABLE IF EXISTS `blocks_temp`;

CREATE TABLE `blocks_temp` (
  `ID` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `number` int(11) NOT NULL,
  `name` varchar(25) DEFAULT NULL,
  `productName` varchar(25) DEFAULT NULL,
  `history_status` varchar(25) DEFAULT NULL,
  `main_status` varchar(25) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=INNODB DEFAULT CHARSET=latin1;

LOAD DATA LOCAL INFILE 'I6_BLOCK_NAMES.csv' INTO TABLE `drwebsite`.`blocks_temp` FIELDS ESCAPED BY '\\' TERMINATED BY ','LINES TERMINATED BY '\n' IGNORE 1 LINES 
(@number, @name, @productName, @history_status, @main_status) SET `number`=@number, `name`=@name, `productName`=@productName,`history_status`=@history_status,`main_status`=@main_status;

INSERT INTO block (number, NAME, productName, STATUS, creationTime) 
SELECT number, NAME, productName, IF(TRIM(main_status)='A', TRUE, FALSE), NOW() FROM blocks_temp;

DROP TABLE IF EXISTS `blocks_temp`;
