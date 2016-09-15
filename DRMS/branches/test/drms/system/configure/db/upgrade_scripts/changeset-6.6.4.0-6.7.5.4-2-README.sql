-- This is a seperate file because of a limitation (or bug?) in ant
-- where the delimiter cannot be changed using the DELIMITER command.
-- The delimiter must be set on the command line or in the ant sql statement.

-- Use on command line:
-- mysql -u root --delimiter=\$\$ pss2 < $THIS_FILE_NAME

-- Use with Ant
-- add the "delimiter="$$"" attribute to the sql task.


DROP FUNCTION IF EXISTS `getFormerTime`$$

CREATE FUNCTION `getFormerTime`(time1 DATETIME, time2 DATETIME) RETURNS DATETIME
DETERMINISTIC
BEGIN
	
	CASE WHEN
		(time1 <= time2)
	THEN
		RETURN time1;
	ELSE
		RETURN time2;
	END CASE;
	
    END$$


DROP FUNCTION IF EXISTS `getOlderTime`$$

CREATE FUNCTION `getOlderTime`(time1 DATETIME, time2 DATETIME) RETURNS DATETIME
DETERMINISTIC
BEGIN
	
	CASE WHEN
		(time1 >= time2)
	THEN
		RETURN time1;
	ELSE
		RETURN time2;
	END CASE;
	
    END$$

--
--
--

DROP PROCEDURE IF EXISTS `calculateShedForEventParticipant`$$

CREATE  PROCEDURE `calculateShedForEventParticipant`(param_year VARCHAR(4),param_month VARCHAR(2),param_day VARCHAR(2))
BEGIN
                DECLARE temp_eventName VARCHAR(32);
                DECLARE temp_start_time DATETIME;
                DECLARE temp_end_time DATETIME;       
                DECLARE temp_event_duration DOUBLE DEFAULT 0;
                DECLARE temp_partName VARCHAR(32); 
                DECLARE temp_uuid VARCHAR(32);				
                DECLARE temp_actual_avg DOUBLE;
                DECLARE temp_base_avg DOUBLE;
                DECLARE temp_shed_avg DOUBLE;                                      
                DECLARE no_row_fetched INT DEFAULT 0;    
				DECLARE loginfo VARCHAR(255); 
				DECLARE logdesc  TEXT;				
                
                DECLARE event_csr CURSOR FOR
                SELECT eventName, startTime, endTime, (TIME_TO_SEC(endtime)-TIME_TO_SEC(starttime) )/3600
                FROM history_event WHERE 
                YEAR(startTime)=param_year          
                AND MONTH(startTime)=param_month
                AND DAYOFMONTH(startTime)=param_day;    
                
                DECLARE event_part_csr CURSOR FOR
                SELECT UUID, participantName FROM 
                history_event_participant WHERE eventName= temp_eventName AND client=0;		
                
                DECLARE CONTINUE HANDLER FOR NOT FOUND SET no_row_fetched=1;
                      
				SET loginfo = CONCAT("TIME: ",param_year,param_month,param_day);
				SET logdesc =CONCAT("TIME: ",param_year,param_month,param_day);

                OPEN event_csr;                
                    event_loop: LOOP -- Loop through events
                                        
                    FETCH event_csr INTO temp_eventName, temp_start_time, temp_end_time, temp_event_duration;
                    
                    IF no_row_fetched=1 THEN
                    LEAVE event_loop;
                    END IF;
					
					SET logdesc = CONCAT(logdesc,"Event  name:", temp_eventName,"  event starttime: ",temp_start_time,"  event endtime: ",temp_end_time,"  event duration: ",temp_event_duration);
                    
                        OPEN event_part_csr;
                            event_part_loop: LOOP -- Loop through participant in event_participant by event.
                                -- **********************************************main block**************************************************
                                FETCH event_part_csr INTO temp_uuid,temp_partName;
                                IF no_row_fetched=1 THEN
                                LEAVE event_part_loop;
                                END IF;
								
								SET logdesc = CONCAT(logdesc,"[ participant name: ",temp_partName);
                                
                                SELECT AVG(VALUE) INTO temp_actual_avg FROM dataentry WHERE 
                                datasource_uuid=(SELECT UUID FROM datasource WHERE ownerID = temp_partName)
                                AND TIME BETWEEN temp_start_time AND temp_end_time;
                                
                                SELECT AVG(VALUE) INTO temp_base_avg FROM history_baseline_dataentry WHERE 
                                datasource_uuid=(SELECT UUID FROM datasource WHERE ownerID = temp_partName)
                                AND TIME BETWEEN temp_start_time AND temp_end_time;
                                
                                IF temp_base_avg =0 THEN SET temp_shed_avg = 0;
                                ELSE
                                SET temp_shed_avg = temp_base_avg-temp_actual_avg;
                                END IF;
                                
								SET logdesc =CONCAT( logdesc," Update participant averageShed: ",temp_shed_avg," total: ",temp_shed_avg*temp_event_duration ,"]");
								
                                UPDATE history_event_participant
                                SET averageShed=temp_shed_avg, totalShed=temp_shed_avg*temp_event_duration
                                WHERE UUID=temp_uuid;
                                
                                -- ************************************************************************************************
                            END LOOP; -- End of Loop through participant in event_participant by event.
                    CLOSE event_part_csr;
                    
                    SET no_row_fetched=0;
                       
                    END LOOP event_loop;-- End of Loop through events
             CLOSE event_csr;
			 
			INSERT INTO history_procedure_logentry 
			(uuid, logdate, loginfo, logdesc) 
			VALUES (REPLACE(uuid(), '-', ''), NOW(), loginfo, logdesc);
END $$

