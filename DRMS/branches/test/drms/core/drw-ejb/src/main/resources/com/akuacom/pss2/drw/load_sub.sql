-- generate Substation data
INSERT INTO location (TYPE, number, NAME, parentID, block, creationTime)
SELECT 'Substation', SUBSTATION_NUM, SUB_NAME, parentID, NULL, NOW() FROM 
(SELECT DISTINCT SUBSTATION_NUM, SUB_NAME, IF(zip.a_bank_no="", NULL, loc.ID)  AS parentID FROM zip_to_location_temp zip, location loc 
WHERE SUBSTATION_NUM!="" AND SUB_NAME!="" AND (zip.a_bank_no="" OR loc.number=zip.a_bank_no) ORDER BY SUBSTATION_NUM) a;