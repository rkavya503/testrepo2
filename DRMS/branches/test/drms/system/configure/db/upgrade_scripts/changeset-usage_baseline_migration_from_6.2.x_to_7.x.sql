-- copy usage/baseline data on the migration day from old tables in 6.2.x to new tables in 7.x
INSERT INTO pss2.dataentry_temp(UUID,dataset_uuid,datasource_uuid,time,value,creationTime,stringValue,valueType,actual)
SELECT REPLACE(UUID(), '-', '') AS UUID, 
dataset_uuid,datasource_uuid,time,value, creationTime, stringValue,valueType,actual
FROM pss2.dataentry d WHERE DATE(time) =(SELECT CURDATE())
ON DUPLICATE KEY UPDATE value = d.value;


INSERT INTO pss2.baseline_dataentry_temp(UUID,dataset_uuid,datasource_uuid,time,value,creationTime,stringValue,valueType,actual)
SELECT REPLACE(UUID(), '-', '') AS UUID, 
dataset_uuid,datasource_uuid,time,value, creationTime, stringValue,valueType,actual
FROM pss2.history_baseline_dataentry b WHERE DATE(time)=(SELECT CURDATE())
ON DUPLICATE  KEY UPDATE value = b.value;