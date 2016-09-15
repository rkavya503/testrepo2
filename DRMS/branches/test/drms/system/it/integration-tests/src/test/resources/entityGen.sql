-- -----------------------------------------------------------------------------
-- 
-- Script to generate test data.  
-- Generates x number of participants per n programs with y clients per participant
-- and z contacts per client
--
-- Update programMax, partMax, clientMax and contactMax to vary the loading.
--
-- Currently configured for SCE RTP.  You will need to change programMax and the
-- programNames in the if loops to test against other utilities or programs
--
-- -----------------------------------------------------------------------------

DELIMITER $$

DROP PROCEDURE IF EXISTS `pss2`.`entityGenerator`$$

CREATE PROCEDURE `pss2`.`entityGenerator`()
    BEGIN
        DECLARE programCount INT;
        DECLARE partCount INT;
        DECLARE clientCount INT;
        DECLARE contactCount INT;
        DECLARE programMax INT;
        DECLARE partMax INT;
        DECLARE clientMax INT;
        DECLARE contactMax INT;
        DECLARE partTotalCurr INT;
        DECLARE clientTotalCurr INT;
        DECLARE programName varchar(64);
        DECLARE partName varchar(64);
        DECLARE clientName varchar(64);
        DECLARE partCurrName varchar(64);
        DECLARE clientCurrName varchar(64);
        
        DECLARE v1 varchar(64);
        DECLARE v2 varchar(64);
    
        -- number of programs. Max is 4
        SET programMax = 1;
        -- number of participants per program
        SET partMax = 100;
        -- number of clients per participant
        SET clientMax = 1;
        -- number of contacts per client
        SET contactMax = 2;
        
        SET programCount = 1;
        SET partCount = 1;
        SET clientCount = 1;
        SET contactCount = 1;
        set partTotalCurr = 1;
        set clientTotalCurr = 1;
        
        set partName = 'part';
        set clientName = 'client';
        
        program_loop:WHILE(programCount <= programMax) DO
                -- These program names are for SCE. Change them if not running against SCE
                IF programCount = 1 THEN
                    set programName = 'RTP Agricultural';
                END IF;
                IF programCount = 2 THEN
                    set programName = 'RTP <2K';
                END IF;
                IF programCount = 3 THEN
                    set programName = 'RTP 2K-50K';
                END IF;
                IF programCount = 4 THEN
                    set programName = 'RTP >50K';
                END IF;
                
                -- loop through participants
                part_loop:WHILE(partCount <= partMax) DO
                        set partCurrName = concat(partName,partTotalCurr);
                        
                        -- for each participant insert into users, participant and program_participant
                        INSERT INTO `accounts`.`users` (`domainname`, `username`, `password`, `email`, `status`) VALUES ('PSS2',partCurrName,'14fb4e3c7b7f1954ed246e33a170f206',concat(partCurrName,'@akuacom.com'),1);
                        INSERT INTO `pss2`.`participant`
                        (`uuid`, `participantName`,`type`,`hostAddress`,`manualControl`,`manualControlExpires`,
                                `account`,`firstName`,`lastName`,`meterName`,`feedback`,`meterId`,`notificationLevel`,
                                `address`,`gridLocation`,`latitude`,`longitude`,`shedPerHourKW`,`lastPrice`,
                                `offlineWarning`,`client`,`parent`,`commTime`,`status`,`testAccount`,`activated`,
                                `version`,`creator`,`modifier`,`creationTime`,`modifiedTime`)
                        VALUES (REPLACE(uuid(), '-', ''),partCurrName,0,null,0,null,partCurrName,
                        '','',null,0,null,0,null,null,0,0,0,-1,1,0,null,null,2,0,1,0,'a',null,now(),null);
                        INSERT INTO `pss2`.`program_participant` 
                        (`uuid`,`version`,`programName`,`participant_uuid`,`creator`,`modifier`,`creationTime`,`modifiedTime`,
                        `program_uuid`,`state`)
                        VALUES (REPLACE(uuid(), '-', ''),0,programName,
                        (select p.uuid from pss2.participant p where p.participantName = partCurrName),'a',null,now(),null,
                        (select uuid from program where name = programName),1);
                        
                        -- loop through clients
                        client_loop:WHILE(clientCount <= clientMax) DO
                                set v2 = concat(clientName,clientTotalCurr);
                                set v1 = concat(partCurrName, '.');
                                set clientCurrName = concat(v1,v2);
                                
                                -- for each client insert into users, participant and program_participant
                                INSERT INTO `accounts`.`users` (`domainname`, `username`, `password`, `email`, `status`) VALUES ('CLIENT',clientCurrName,'14fb4e3c7b7f1954ed246e33a170f206',concat(clientCurrName,'@akuacom.com'),1);
                                INSERT INTO `pss2`.`participant` 
                                (`uuid`, `participantName`,`type`,`hostAddress`,`manualControl`,`manualControlExpires`,
                                `account`,`firstName`,`lastName`,`meterName`,`feedback`,`meterId`,`notificationLevel`,
                                `address`,`gridLocation`,`latitude`,`longitude`,`shedPerHourKW`,`lastPrice`,
                                `offlineWarning`,`client`,`parent`,`commTime`,`status`,`testAccount`,`activated`,
                                `version`,`creator`,`modifier`,`creationTime`,`modifiedTime`)
                                VALUES (REPLACE(uuid(), '-', ''),clientCurrName,0,null,0,null,clientCurrName,'','',null,0,
                                null,0,null,null,0,0,0,-1,1,1,partCurrName,null,0,0,1,0,'a',null,now(),null);
                                
                                -- loop through participant contacts
                                contact_loop:WHILE(contactCount <= contactMax) DO
                                
                                        INSERT INTO `pss2`.`participant_contact` 
                                        (uuid, `type`,`address`,`description`,`commNotification`,`eventNotification`,`offSeasonNotiHours`,`onSeasonNotiHours`,`offlineError`,`external`,`participant_uuid`, `creationTime`)
                                        VALUES (REPLACE(uuid(), '-', ''),'Email Address',concat(clientCurrName,contactCount,'@akuacom.com'),clientCurrName,1,'FullNotification',0.05,0.05,0,0,
                                        (select p.uuid from pss2.participant p where p.participantName = clientCurrName), now());
                                        set contactCount = contactCount + 1;
                                END WHILE contact_loop;
                                set contactCount = 1;
                                
                                INSERT INTO `pss2`.`program_participant` 
                                (`uuid`,`version`,`programName`,`participant_uuid`,`creator`,`modifier`,`creationTime`,`modifiedTime`,
                                `program_uuid`,`state`)
                                VALUES (REPLACE(uuid(), '-', ''),0,programName,
                                (select p.uuid from pss2.participant p where p.participantName = clientCurrName),'a',null,now(),null,
                                (select uuid from program where name = programName),1);
                                set clientCount = clientCount + 1;
                                set clientTotalCurr = clientTotalCurr + 1;
                        END WHILE client_loop;
                        SET clientCount = 1;
                        
                        set partCount = partCount + 1;
                        set partTotalCurr = partTotalCurr + 1;
                END WHILE part_loop;
                SET partCount = 1;
                
                set programCount = programCount + 1;
        END WHILE program_loop;
        SET programCount = 1;
        
    END$$

DELIMITER ;

call pss2.entityGenerator();

drop procedure pss2.entityGenerator;

