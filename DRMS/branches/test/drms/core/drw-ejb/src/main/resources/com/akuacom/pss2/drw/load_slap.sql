INSERT INTO location (TYPE, number, NAME, parentID, block, creationTime)
SELECT 'SLAP', slap_id, slap_name, NULL, NULL, NOW() FROM 
(SELECT DISTINCT slap_id, slap_name FROM zip_to_location_temp WHERE slap_id!="" AND slap_name!="" ORDER BY slap_id) a;