
DROP TABLE IF EXISTS `history_temp`;

CREATE TABLE `history_temp` (
  `ID` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `program` VARCHAR(25) DEFAULT NULL,
  `product` VARCHAR(25) DEFAULT NULL,
  `dispatchType` VARCHAR(25) DEFAULT NULL,
  `dispatchLocation` VARCHAR(25) DEFAULT NULL,
  `eventStartDate` VARCHAR(25) DEFAULT NULL,
  `eventStartTime` VARCHAR(25) DEFAULT NULL,
  `eventEndDate` VARCHAR(25) DEFAULT NULL,
  `eventEndTime` VARCHAR(25) DEFAULT NULL,
  `comments` VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=INNODB DEFAULT CHARSET=latin1;

LOAD DATA LOCAL INFILE 'API BIP SDP History Information.csv' INTO TABLE `drwebsite`.`history_temp` FIELDS ESCAPED BY '\\' TERMINATED BY ',' ENCLOSED BY '"' LINES TERMINATED BY '\n' IGNORE 1 LINES 
(@program, @product, @dispatchType, @dispatchLocation, @eventStartDate, @eventStartTime, @eventEndDate, @eventEndTime, @comments) 
SET `program`=@program, `product`=@product, `dispatchType`=@dispatchType, `dispatchLocation`=@dispatchLocation,
`eventStartDate`=@eventStartDate,`eventStartTime`=@eventStartTime, `eventEndDate`=@eventEndDate,`eventEndTime`=@eventEndTime,`comments`=@comments;

INSERT INTO event (UUID, programName, startTime, product, COMMENT, creationTime)
SELECT REPLACE(UUID(), '-', ''), program, startTime, product, comments, NOW() FROM (
SELECT DISTINCT program, product, TIMESTAMP(eventStartDate, IF(eventStartTime='', '00:00:00', eventStartTime)) AS startTime, comments
FROM history_temp) a;

INSERT INTO event_detail(UUID, event_uuid, estimatedEndTime, actualEndTime, 
lastModifiedTime, locationID, allLocationType, blockNames)
SELECT REPLACE(UUID(), '-', ''), e.uuid, NULL, TIMESTAMP(eventEndDate, IF(eventEndTime='', '00:00:00', eventEndTime)) AS endTime,
NULL, l.ID, NULL, NULL
FROM location l, history_temp t, event e
WHERE t.program!='BIP' AND t.dispatchLocation!='All' AND t.dispatchType=l.type AND t.dispatchLocation=l.number 
AND e.programName=t.program AND e.product=t.product 
AND e.startTime=TIMESTAMP(eventStartDate, IF(eventStartTime='', '00:00:00', eventStartTime));

INSERT INTO zipcode_entry (UUID, eventDetail_uuid, zipCode)
SELECT REPLACE(UUID(), '-', ''), e.uuid, z.zipCode
FROM event_detail e, location l, zipcode z
WHERE e.locationID=l.ID AND l.type=z.locationType AND l.number=z.locationNumber;

INSERT INTO event_detail(UUID, event_uuid, estimatedEndTime, actualEndTime, 
lastModifiedTime, locationID, allLocationType, blockNames)
SELECT REPLACE(UUID(), '-', ''), e.uuid, NULL, TIMESTAMP(eventEndDate, IF(eventEndTime='', '00:00:00', eventEndTime)) AS endTime,
NULL, NULL, t.dispatchType, NULL
FROM history_temp t, event e
WHERE t.program!='BIP' AND t.dispatchLocation='All' 
AND e.programName=t.program AND e.product=t.product 
AND e.startTime=TIMESTAMP(eventStartDate, IF(eventStartTime='', '00:00:00', eventStartTime));

INSERT INTO zipcode_entry (UUID, eventDetail_uuid, zipCode)
SELECT REPLACE(UUID(), '-', ''), e.uuid, z.zipCode
FROM event_detail e, zipcode z
WHERE z.locationType=e.allLocationType;

INSERT INTO event_detail(UUID, event_uuid, estimatedEndTime, actualEndTime, 
lastModifiedTime, locationID, allLocationType, blockNames)
SELECT REPLACE(UUID(), '-', ''), e.uuid, NULL, TIMESTAMP(eventEndDate, IF(eventEndTime='', '00:00:00', eventEndTime)) AS endTime,
NULL, NULL, NULL, t.dispatchLocation
FROM history_temp t, event e
WHERE t.program='BIP' AND e.programName=t.program AND e.product=t.product 
AND e.startTime=TIMESTAMP(eventStartDate, IF(eventStartTime='', '00:00:00', eventStartTime));

DROP TABLE IF EXISTS `history_temp`;

