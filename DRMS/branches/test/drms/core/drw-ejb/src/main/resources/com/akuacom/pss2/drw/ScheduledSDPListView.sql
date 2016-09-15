SELECT product.*,  ec.cityName,ec.countyName,ec.zipCode FROM 
   ( 
      -- product start time and end time																		 	
      SELECT  p.utilityName AS utilityProgramName, p.programClass, p.longProgramName,  
	      MIN(e.startTime) AS startTime,e.issuedTime issueTime,		 
	      IF(MAX(ed.estimatedEndTime) IS NULL OR MAX(ed.actualEndTime)>=MAX(ed.estimatedEndTime), MAX(ed.actualEndTime), MAX(ed.estimatedEndTime)) AS endTime,
	      MAX(IF( ed.estimatedEndTime IS NULL AND ed.actualEndTime IS NULL,'1','0')) AS tbdFlag,
	      e.product AS eventKey, p.name AS product  																	
       FROM event e, event_detail ed, program p  	
	   WHERE p.name IN ${products} AND p.product = e.product AND e.programName=p.programClass  AND e.uuid = ed.event_uuid 
	       AND e.startTime<=NOW() AND (ed.actualEndTime IS NULL OR ed.actualEndTime > NOW())  		
	   GROUP BY e.product 																									
     ) product,
    event e, event_detail eed, zipcode_entry ec  											
WHERE e.product=product.eventKey AND e.uuid = eed.event_uuid AND ec.eventDetail_uuid=eed.uuid
    AND e.startTime >=product.startTime AND e.startTime<=NOW() 
    AND (eed.actualEndTime IS NULL OR eed.actualEndTime > NOW()) 
    [AND ec.zipCode IN ${zipCodes}] [AND ec.cityName=${city}] [AND ec.countyName=${county}] 	