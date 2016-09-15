DELETE FROM program_matrix WHERE program1UUID=(SELECT UUID FROM program WHERE NAME='RTP <2k B') 
OR program2UUID=(SELECT UUID FROM program WHERE NAME='RTP <2k B');
DELETE FROM program_rtp_config WHERE program_uuid =(SELECT UUID FROM program WHERE NAME='RTP <2k B');
DELETE FROM program_signal WHERE program_uuid =(SELECT UUID FROM program WHERE NAME='RTP <2k B');

DELETE FROM program WHERE NAME='RTP <2k B';


DELETE FROM program_matrix WHERE program1UUID='4028f8b23347910701336f8a63a8441e' 
OR program2UUID='4028f8b23347910701336f8a63a8441e';
DELETE FROM program_matrix WHERE program1UUID='4028f8b23401e3740134140e05c7341b' 
OR program2UUID='4028f8b23401e3740134140e05c7341b';


DELETE FROM program_rtp_config WHERE program_uuid = '4028f8b23347910701336f8a63a8441e';
DELETE FROM program_rtp_config WHERE program_uuid = '4028f8b23401e3740134140e05c7341b';


DELETE FROM program_signal WHERE program_uuid ='4028f8b23347910701336f8a63a8441e';
DELETE FROM program_signal WHERE program_uuid ='4028f8b23401e3740134140e05c7341b';

DELETE FROM program WHERE NAME='Demo 5';



