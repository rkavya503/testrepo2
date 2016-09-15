
USE drwebsite;

DROP TABLE IF EXISTS `zip_to_location_temp`;

CREATE TABLE `zip_to_location_temp` (
  `ID` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `SUBSTATION_NUM` VARCHAR(25) DEFAULT NULL,
  `SUB_NAME` VARCHAR(255) DEFAULT NULL,
  `MAILING_CITY_NAME` VARCHAR(255) DEFAULT NULL,
  `ZIP_CODE` VARCHAR(25) DEFAULT NULL,
  `COUNTY_NO` VARCHAR(25) DEFAULT NULL,
  `COUNTY_NAM` VARCHAR(255) DEFAULT NULL,
  `Block` VARCHAR(25) DEFAULT NULL,
  `slap_id` VARCHAR(25) DEFAULT NULL,
  `slap_name` VARCHAR(50) DEFAULT NULL,
  `a_bank_no` VARCHAR(25) DEFAULT NULL,
  `a_bank_name` VARCHAR(100) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=INNODB DEFAULT CHARSET=latin1;

LOAD DATA LOCAL INFILE 'Ramesh_2012_3_30_Final_draft_v4.csv' INTO TABLE `drwebsite`.`zip_to_location_temp` FIELDS ESCAPED BY '\\' TERMINATED BY ','LINES TERMINATED BY '\n' IGNORE 1 LINES 
(@SUBSTATION_NUM, @SUB_NAME, @MAILING_CITY_NAME, @ZIP_CODE, @COUNTY_NO, @COUNTY_NAM, @Block, @slap_id, @slap_name, @a_bank_no, @a_bank_name) 
SET `SUBSTATION_NUM`=@SUBSTATION_NUM, `SUB_NAME`=@SUB_NAME, `MAILING_CITY_NAME`=@MAILING_CITY_NAME,`ZIP_CODE`=@ZIP_CODE,
`COUNTY_NO`=@COUNTY_NO,`COUNTY_NAM`=@COUNTY_NAM,`Block`=@Block,`slap_id`=@slap_id,`slap_name`=@slap_name,
`a_bank_no`=@a_bank_no,`a_bank_name`=@a_bank_name;

TRUNCATE TABLE zipcode;
TRUNCATE TABLE location;

-- generate SLAP data
INSERT INTO location (TYPE, number, NAME, parentID, block, creationTime)
SELECT 'SLAP', slap_id, slap_name, NULL, NULL, NOW() FROM 
(SELECT DISTINCT slap_id, slap_name FROM zip_to_location_temp WHERE slap_id!="" AND slap_name!="" ORDER BY slap_id) a;

-- generate ABank data
INSERT INTO location (TYPE, number, NAME, parentID, block, creationTime)
SELECT 'ABank', a_bank_no, a_bank_name, parentID, Block, NOW() FROM 
(SELECT DISTINCT a_bank_no, a_bank_name, zip.Block, zip.slap_id, IF(zip.slap_id="", NULL, loc.ID) AS parentID 
FROM zip_to_location_temp zip, location loc 
WHERE a_bank_no!="" AND a_bank_name!="" AND (zip.slap_id="" OR loc.number=zip.slap_id) ORDER BY a_bank_no) a;

-- generate Substation data
INSERT INTO location (TYPE, number, NAME, parentID, block, creationTime)
SELECT 'Substation', SUBSTATION_NUM, SUB_NAME, parentID, NULL, NOW() FROM 
(SELECT DISTINCT SUBSTATION_NUM, SUB_NAME, IF(zip.a_bank_no="", NULL, loc.ID)  AS parentID FROM zip_to_location_temp zip, location loc 
WHERE SUBSTATION_NUM!="" AND SUB_NAME!="" AND (zip.a_bank_no="" OR loc.number=zip.a_bank_no) ORDER BY SUBSTATION_NUM) a;

-- generate Zip Code
INSERT INTO zipcode (zipCode, locationType, locationNumber, locationName, cityName, countyNo, countyName, block, creationTime)
SELECT DISTINCT ZIP_CODE, loc.type, loc.number, loc.name, MAILING_CITY_NAME, COUNTY_NO, COUNTY_NAM, zip.Block, NOW() 
FROM location loc, zip_to_location_temp zip
WHERE loc.type='Substation' AND loc.number=zip.SUBSTATION_NUM ORDER BY ZIP_CODE;

INSERT INTO zipcode (zipCode, locationType, locationNumber, locationName, cityName, countyNo, countyName, block, creationTime)
SELECT DISTINCT ZIP_CODE, loc.type, loc.number, loc.name, MAILING_CITY_NAME, COUNTY_NO, COUNTY_NAM, zip.Block, NOW() 
FROM location loc, zip_to_location_temp zip
WHERE loc.type='ABank' AND loc.number=zip.a_bank_no ORDER BY ZIP_CODE;

INSERT INTO zipcode (zipCode, locationType, locationNumber, locationName, cityName, countyNo, countyName, block, creationTime)
SELECT DISTINCT ZIP_CODE, loc.type, loc.number, loc.name, MAILING_CITY_NAME, COUNTY_NO, COUNTY_NAM, zip.Block, NOW() 
FROM location loc, zip_to_location_temp zip
WHERE loc.type='SLAP' AND loc.number=zip.slap_id ORDER BY ZIP_CODE;

DROP TABLE IF EXISTS `zip_to_location_temp`;
