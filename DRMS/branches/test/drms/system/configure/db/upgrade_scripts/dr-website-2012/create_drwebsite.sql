
CREATE database drwebsite;

grant all on drwebsite.* to 'pss2'@'localhost';
              
USE drwebsite;

SOURCE /root/drms/current/db/baseline/drwebsite-schema.sql
SOURCE /root/drms/current/db/baseline/drwebsite-data.sql
SOURCE /root/drms/current/db/baseline/drwebsite-routines.sql
SOURCE import_zip_to_subs.sql
SOURCE import_blocks.sql
SOURCE import_district.sql
SOURCE import_history.sql

