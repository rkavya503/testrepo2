
 SELECT	type,name,number,kmlAvailable,creationTime,size FROM (	

SELECT 'ALL' AS type,'ALL' AS name,'ALL' AS number, MAX(creationTime) creationTime,SUM(size) size,
IF  (COUNT(UUID)>0, 'Y' , 'N') 	AS kmlAvailable  
FROM 
(
SELECT UUID,locationType,number,kml,creationTime, LENGTH(kml) AS size											
FROM location_kml WHERE location_kml.number ='ALL'

) temp	

GROUP BY locationType,number 	
		
UNION ALL
SELECT	location.type, location.name,location.number,
location_kml_table.creationTime,location_kml_table.size ,
IF  (location_kml_table.counts>0, 'Y' , 'N') 	AS kmlAvailable  
							
FROM location LEFT JOIN 
(SELECT locationType AS type ,number, SUM(size) size,
 COUNT(UUID)  AS counts                  
,MAX(creationTime) creationTime
FROM 
(
SELECT UUID,locationType,number,kml,creationTime, LENGTH(kml) AS size											
FROM location_kml WHERE location_kml.number <>'ALL'

) temp	

GROUP BY locationType,number )
AS location_kml_table
ON location_kml_table.type =location.type 		
AND location_kml_table.number=location.number
																												
WHERE location.type IN ('ABank','SLAP','Substation') 	
)temp 
ORDER BY FIELD(type,'ALL','SLAP','Abank','Substation' ),name																							
		