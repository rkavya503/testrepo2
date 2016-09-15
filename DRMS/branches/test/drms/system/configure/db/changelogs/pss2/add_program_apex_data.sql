CREATE DEFINER=`root`@`localhost` PROCEDURE `add_program_apex_data`(IN eventName VARCHAR(64))
BEGIN
   
    DECLARE l_programName  VARCHAR(255) ;
    DECLARE ls_utilityName varchar(200);
     /* DECLARE ls_event_id VARCHAR(64) ; */
	DECLARE v_finish_evt INTEGER DEFAULT 0; 
    DECLARE ll_participantname VARCHAR(64) ; 
    DECLARE ll_participantuuid VARCHAR(128) ; 
    DECLARE ll_account VARCHAR(128) ;
    DECLARE ll_eventname VARCHAR(64) ;
    DECLARE ll_starttime datetime;
    DECLARE ll_endtime datetime;
    DECLARE ll_issuedtime datetime;
    DECLARE ll_programName  VARCHAR(32) ;
    DECLARE ll_euuid  VARCHAR(32) ;
    DECLARE ll_Child_acc Varchar(128);
    DECLARE ll_ALL_Child_acc LongText;
    DECLARE ll_program_uuid  VARCHAR(32) ;
    DECLARE ll_secondary_account  VARCHAR(128) ;
    DECLARE ll_uuid  VARCHAR(250) ;
    declare no_more_rows boolean default false;
 
DEClARE pp_event CURSOR FOR
            SELECT distinct p.participantname, account, secondary_account, p.uuid
			FROM program_participant pp_cli, program_participant pp_agg, participant p
			where pp_cli.parent_uuid = pp_agg.uuid
			and pp_agg.participant_uuid = p.uuid
			and pp_cli.participant_uuid in ( select participant_uuid from event_participant where event_uuid = ll_euuid)
			and pp_cli.program_uuid = ll_program_uuid
			and pp_agg.program_uuid = ll_program_uuid;
	
    declare continue handler for not found  
		set no_more_rows := true;                        

SET SESSION TRANSACTION ISOLATION LEVEL READ UNCOMMITTED ;
    
SELECT 
    SUBSTRING(stringvalue, 1, 200)
INTO ls_utilityName FROM
    core_property
WHERE
    propertyname = 'utilityName';

if ls_utilityName = 'sce' then 

	select eventname, e.starttime, e.endtime, e.issuedtime, e.programname, e.uuid , p.uuid
		into ll_eventname,ll_starttime, ll_endtime,ll_issuedtime, ll_programName , ll_euuid, ll_program_uuid 
        from event e, program p
        where e.programname = p.name
        and e.eventname = eventName;
    
	open pp_event; 

	get_events: LOOP

	FETCH pp_event INTO ll_participantname, ll_account , ll_secondary_account, ll_participantuuid ;

	if no_more_rows then
		LEAVE get_events;
	END IF;
              
		call get_accounts_for_aggregator (ll_participantuuid, ll_program_uuid  , ll_euuid  , ll_ALL_Child_acc );
        
		SELECT FLOOR(RAND() * 7000000001) + 100 INTO ll_uuid;
		
		IF(ll_eventname IS NOT NULL) THEN
			INSERT INTO programaggregator ( uuid, creationtime, VERSION, aggregatorname,
								aggregatoraccountNumber, aggregatorclientidmappedwithendpoint, eventid, eventstarttime,
								eventendtime, eventissuetime, aggregatorresource, eventuuid , secondaryaccount , childrenaccounts )
			values (ll_uuid, now(), 1, ll_participantname ,
								ll_account, ll_account,  ll_eventname, ll_starttime, 
			ll_endtime,  ll_issuedtime, ll_account , ll_euuid, ll_secondary_account, ll_ALL_Child_acc); 
		End IF;
                

  END LOOP get_events;

 CLOSE pp_event;
 end if;
 
 SET SESSION TRANSACTION ISOLATION LEVEL REPEATABLE READ ; 
 
END

