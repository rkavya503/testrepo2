SELECT e.uuid AS parentUuid,event_detail.uuid AS uuid, programName AS program, product, startTime, issuedTime AS issueTime,
 IF(actualEndTime IS NOT NULL,actualEndTime, estimatedEndTime) AS endTime, CAST(IF(locationID IS NOT NULL,locationID, allLocationType) AS CHAR)AS locationId,
 location.type locationType,location.number locationNumber,location.block locationBlock,IF(actualEndTime IS NOT NULL,FALSE, TRUE) AS estimated
 ,zipcode_entry.zipCode zipCode,zipcode_entry.cityName cityName,zipcode_entry.countyName countyName
 FROM event_detail INNER JOIN event e 
 ON e.uuid=event_detail.event_uuid 
 LEFT OUTER JOIN location ON location.id = event_detail.locationID
 LEFT OUTER JOIN zipcode_entry ON event_detail.uuid = zipcode_entry.eventDetail_uuid
 WHERE startTime <=NOW() AND (actualEndTime IS NULL OR actualEndTime > NOW()) 
 AND programName=${pram_programName}  AND product IN ${pram_products} 