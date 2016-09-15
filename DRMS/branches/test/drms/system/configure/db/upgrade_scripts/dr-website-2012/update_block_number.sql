
USE drwebsite;

DROP TABLE IF EXISTS `slap_mapping_temp`;

CREATE TABLE `slap_mapping_temp` (
  `ID` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `SLAP_CODE` VARCHAR(25) DEFAULT NULL,
  `SLAP_NAME` VARCHAR(255) DEFAULT NULL,
  `ABANK_NUM` VARCHAR(255) DEFAULT NULL,
  `ABANK_NAME` VARCHAR(25) DEFAULT NULL,
  `BBANK_NUM` VARCHAR(25) DEFAULT NULL,
  `BBANK_NAME` VARCHAR(255) DEFAULT NULL,
  `BLOCK_NUM` VARCHAR(25) DEFAULT NULL,
  `BLOCK_NAME` VARCHAR(25) DEFAULT NULL,
  `ZONE_CODE` VARCHAR(50) DEFAULT NULL,
  `ZONE_NAME` VARCHAR(25) DEFAULT NULL,
  `SLAP_START_DATE` VARCHAR(25) DEFAULT NULL,
  `SLAP_END_DATE` VARCHAR(25) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=INNODB DEFAULT CHARSET=latin1;

LOAD DATA LOCAL INFILE '2013 SLAP Mapping.csv' INTO TABLE `drwebsite`.`slap_mapping_temp` FIELDS ESCAPED BY '\\' TERMINATED BY ','LINES TERMINATED BY '\n' IGNORE 1 LINES 
(@SLAP_CODE, @SLAP_NAME, @ABANK_NUM, @ABANK_NAME, @BBANK_NUM, @BBANK_NAME, @BLOCK_NUM, @BLOCK_NAME, @ZONE_CODE, @ZONE_NAME, @SLAP_START_DATE, @SLAP_END_DATE) 
SET `SLAP_CODE`=@SLAP_CODE, `SLAP_NAME`=@SLAP_NAME, `ABANK_NUM`=@ABANK_NUM,`ABANK_NAME`=@ABANK_NAME,
`BBANK_NUM`=@BBANK_NUM,`BBANK_NAME`=@BBANK_NAME,`BLOCK_NUM`=@BLOCK_NUM,`BLOCK_NAME`=@BLOCK_NAME,
`ZONE_CODE`=@ZONE_CODE,
`ZONE_NAME`=@ZONE_NAME,`SLAP_START_DATE`=@SLAP_START_DATE,`SLAP_END_DATE`=@SLAP_END_DATE;

UPDATE location,slap_mapping_temp SET location.block=slap_mapping_temp.block_num
WHERE location.type='ABank' and location.number=slap_mapping_temp.ABANK_NUM;

update zipcode zc, location loc
set zc.block = loc.block
where zc.locationType=loc.type
  and zc.locationNumber=loc.number
  and loc.type='Abank';
  

update zipcode slap_zc, zipcode abank_zc, location slap_loc, location abank_loc
set slap_zc.block=abank_zc.block
where slap_zc.zipCode = abank_zc.zipCode 
  and slap_zc.locationType = slap_loc.type
  and slap_zc.locationNumber = slap_loc.number
  and slap_loc.ID = abank_loc.parentID
  and abank_loc.type = abank_zc.locationType
  and abank_loc.number = abank_zc.locationNumber
  and slap_zc.locationType = 'SLAP'
  and abank_zc.locationType = 'ABank';

update zipcode sub_zc, location sub_loc, location abank_loc
set sub_zc.block=abank_loc.block
where sub_zc.locationType = sub_loc.type
  and sub_zc.locationNumber = sub_loc.number
  and sub_loc.parentID = abank_loc.ID
  and sub_zc.locationType = 'Substation'
  and sub_loc.type = 'Substation';
  
DROP TABLE IF EXISTS `slap_mapping_temp`;
