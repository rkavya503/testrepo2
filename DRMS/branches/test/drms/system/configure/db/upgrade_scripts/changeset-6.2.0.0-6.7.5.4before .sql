---DRMS-3707 Clean space in front or back of program names for history data.

--UPDATE program SET name = LTRIM(RTRIM(name));
--UPDATE event SET programName = LTRIM(RTRIM(programName));
--UPDATE report_event SET programName = LTRIM(RTRIM(programName));
--UPDATE program_participant SET programName = LTRIM(RTRIM(programName));
--UPDATE all_program_participant SET programName = LTRIM(RTRIM(programName));
SELECT DISTINCT programName FROM history_event;
UPDATE history_event SET programName = LTRIM(RTRIM(programName));
              




-- Checking for duplicates
SELECT * FROM dataset GROUP BY name HAVING COUNT(*) > 1 ;
SELECT * FROM datasource GROUP BY ownerid HAVING COUNT(*) > 1 ;
SELECT uuid, dataset_uuid, datasource_uuid FROM dataentry GROUP BY dataset_uuid HAVING COUNT(*) > 1 ;
SELECT uuid,name FROM program GROUP BY name HAVING COUNT(*) > 1 ;


-- dataset	   dataentry - dataset_uuid
--			   history_baseline_dataentry -- datasource_uuid
--			   dataentry_temp - datasource_uuid
-- datasource  participant -- ownerid
--			   dataentry  -- datasource_uuid
--			   history_baseline_dataentry -- datasource_uuid
--			   dataentry_temp - datasource_uuid
-- program     program_matrix - program1UUID, program2UUID, 
--             program_signal - program_uuid
--			   program_participant - program_uuid
--			   program_rtp_config - program_uuid
--			   program_rule -- program_uuid
--	
-- 				ALTER TABLE  dataentry drop FOREIGN KEY FK_datasource;
--				ALTER TABLE  dataentry drop FOREIGN KEY FK_dataset;
--				ALTER table history_baseline_dataentry drop FOREIGN KEY FK_historydataset;
--				ALTER table history_baseline_dataentry drop FOREIGN KEY FK_historydatasource;

--				