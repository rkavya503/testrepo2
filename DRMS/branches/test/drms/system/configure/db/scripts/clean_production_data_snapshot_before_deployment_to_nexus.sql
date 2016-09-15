-- All of the following should be done performed on the dumps prior to deployment.
-- delete from pss2.event_participant;
-- delete from pss2.event; 
-- delete from pss2.event_participant_rules
-- delete from pss2.event_state
-- delete from pss2.event_template
-- delete from pss2.event_template_signalentry
-- random email address removed per DRMS-4232
update pss2.participant_contact set address=CONCAT('noops@akuacom.com');
update pss2.message set `to`=CONCAT('noops@',SUBSTRING(MD5(RAND()),20),'.com');
update pss2.message set `from`=CONCAT('noops@',SUBSTRING(MD5(RAND()),20),'.com');
-- delete from pss2.event_timeblock;
 
update accounts.users set email=CONCAT('noops@',SUBSTRING(MD5(RAND()),20),'.com');
-- random email address removed per DRMS-4232
update pss2.contact set address=CONCAT('noops@akuacom.com');

-- These are not cleaned right now. Kept for possible furture use.
-- update pss2.contacts set firstName=SUBSTRING(MD5(RAND()),20);
-- update pss2.contacts set lastName=SUBSTRING(MD5(RAND()),20);
-- update pss2.participant set participantName=SUBSTRING(MD5(RAND()),20);
-- update pss2.participant set firstname=SUBSTRING(MD5(RAND()),20);
-- update pss2.participant set lastname=SUBSTRING(MD5(RAND()),20);
-- update pss2.participant set parent=SUBSTRING(MD5(RAND()),20);
-- update pss2.participant set account=SUBSTRING(MD5(RAND()),20);
-- update pss2.participant_contact set description=SUBSTRING(MD5(RAND()),20);
-- update pss2.program set name=utilityProgramName;
-- update pss2.program_signal_entry set time='2010-01-01 00:00:00';
-- update pss2.program set programType='CPPProgram' where programType is NULL; 
