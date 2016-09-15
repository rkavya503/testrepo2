INSERT INTO location (TYPE, number, NAME, parentID, block, creationTime)
SELECT 'ABank', a_bank_no, a_bank_name, parentID, Block, NOW() FROM 
(SELECT DISTINCT a_bank_no, a_bank_name, zip.Block, zip.slap_id, IF(zip.slap_id="", NULL, loc.ID) AS parentID 
FROM zip_to_location_temp zip, location loc 
WHERE a_bank_no!="" AND a_bank_name!="" AND (zip.slap_id="" OR loc.number=zip.slap_id) ORDER BY a_bank_no) a;