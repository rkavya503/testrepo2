
USE drwebsite;

DROP TABLE IF EXISTS `district_temp`;

CREATE TABLE `district_temp` (
  `ID` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `zipcode` VARCHAR(25) NOT NULL,
  `district_number` VARCHAR(255) DEFAULT NULL,
  `district_name` VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=INNODB DEFAULT CHARSET=latin1;

LOAD DATA LOCAL INFILE 'SC3042_DISTRICT_ZIP.csv' INTO TABLE `drwebsite`.`district_temp` FIELDS ESCAPED BY '\\' TERMINATED BY ','LINES TERMINATED BY '\n' IGNORE 1 LINES (@zipcode, @district_number, @district_name) SET `zipcode`=@zipcode, `district_number`=@district_number, `district_name`=@district_name;

INSERT INTO location (type, number, NAME, parentID, creationTime) 
SELECT 'District', district_number, district_name, NULL, NOW() FROM 
(SELECT DISTINCT district_number, district_name FROM district_temp) a;

INSERT INTO zipcode (zipCode, locationType, locationNumber, locationName, creationTime)
SELECT DISTINCT zipCode, loc.type, loc.number, loc.name, NOW() FROM location loc, district_temp zip
WHERE loc.type='District' AND loc.number=zip.district_number ORDER BY zipCode;

DROP TABLE IF EXISTS `district_temp`;
