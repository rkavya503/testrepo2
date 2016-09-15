
select GROUP_CONCAT(eventlocation) eventlocations,temp2.*
from
(
SELECT event_uuid,startTime,CAST(startTime AS CHAR) startTimeStr,estimatedEndTime,actualEndTime, eventlocations as eventlocation,IF(endtime IS NOT NULL, CAST(endTime AS CHAR), 'TBD') AS endtime , 
 CAST(MD5(CONCAT(product,CAST(startTime AS CHAR),IF(endtime IS NOT NULL, CAST(endTime AS CHAR),'TBD'))) AS CHAR)eventKey 
 FROM ( 
 SELECT product,event_uuid,e.startTime startTime,estimatedEndTime,actualEndTime, GROUP_CONCAT(event_detail.uuid) eventlocations 
 , 
 IF(actualEndTime IS NOT NULL,actualEndTime, estimatedEndTime) AS endtime  
 FROM  
 event_detail INNER JOIN event e 
 ON e.uuid=event_detail.event_uuid 
 WHERE startTime <=NOW() AND (actualEndTime IS NULL OR actualEndTime > NOW()) 
 AND programName=${pram_programName}  AND product IN ${pram_products} 
 GROUP BY event_uuid,estimatedEndTime,actualEndTime 
 ) temp 
 ) temp2 group by eventKey
 