CREATE DEFINER=`root`@`localhost` PROCEDURE `get_accounts_for_aggregator`(IN ll_participantuuid VARCHAR(64) , ll_program_uuid  VARCHAR(32) , ll_euuid  VARCHAR(250) , OUT valido longtext )
BEGIN

DECLARE ll_Child_acc Varchar(128);
DECLARE ll_ALL_Child_acc LongText;
declare updateDone int default 0;

DECLARE pp_participant cursor for
SELECT 
    c.account
FROM
    participant p,
    event_participant ep,
    participant c
WHERE
               p.participantname = c.parent and
    p.uuid = ep.participant_uuid
        AND event_uuid = ll_euuid
        AND participant_uuid IN (SELECT 
            participant_uuid
        FROM
            program_participant
        WHERE
             program_uuid = ll_program_uuid
                AND optstatus = 0
                AND parent_uuid = (SELECT 
                    uuid
                FROM
                    program_participant pp
                WHERE
                    participant_uuid = ll_participantuuid
                        AND program_uuid = ll_program_uuid));
                        
       DECLARE CONTINUE HANDLER FOR SQLSTATE '02000' SET updateDone = 1;
          
       SET ll_ALL_Child_acc = '';
    
               open pp_participant; 

               get_participant: LOOP

               FETCH pp_participant INTO ll_Child_acc ;

               IF updateDone =1 THEN
                              close pp_participant;
                              LEAVE get_participant;
               END IF;
                              if ll_ALL_Child_acc = '' then
                                             set ll_ALL_Child_acc = ll_Child_acc ;
        else
                                             select CONCAT(ll_ALL_Child_acc, ',',ll_Child_acc ) into ll_ALL_Child_acc ;
        End if;
  END LOOP get_participant;
  
               SELECT ll_ALL_Child_acc INTO valido ;
                        
END
