#!/bin/bash

BACKUPPARENTDIR=/tmp/db_backup/@UTILITY_NAME@;
if [ ! -f $BACKUPPARENTDIR ]; then
   mkdir -p $BACKUPPARENTDIR ;
fi ;

CURDATE=`date +"%m%d%Y"`;
MINDISKSPACE=1000;
FILESYSTEM="/dev/mapper/VolGroup_ID_14503-LogVolHome";
SENDEMAIL=false;
BACKUPDIR=$BACKUPPARENTDIR/$CURDATE
LOGBACKUPDIR=$BACKUPPARENTDIR/firelogs
STARTFILE=/tmp/db_backup/start.out ;
RESULTFILE=/tmp/db_backup/end.out ;
OPEMAIL=ops@akuacom.com
LASTDATE=`date --date "now -1 days" +"%Y-%m-%d"`;
THISDATE=`date +"%Y-%m-%d"`;
DELETEDATE=`date --date "now -7 days" +"%Y-%m-%d"`;

VAILABLESPACE=`df -H -m $FILESYSTEM | grep -A 1 -i $FILESYSTEM | grep -v $FILESYSTEM | awk '{ print $3 }'`

if [ $AVAILABLESPACE -lt $MINDISKSPACE ]; then
    echo "Not enough diskspace for backup. " >> $RESULTFILE;
    SENDEMAIL=true;
    mail -s "$UTIL_DISPLAY_NAME: End DB Backup" $OPEMAIL  < $RESULTFILE;
    exit 1;
fi ;

if [ ! -f $BACKUPPARENTDIR/$CURDATE ]; then
   mkdir -p $BACKUPPARENTDIR/$CURDATE ;
fi ;

if [ ! -f $LOGBACKUPDIR ]; then
   mkdir -p $LOGBACKUPDIR ;
fi ;



echo "$UTIL_DISPLAY_NAME: Start DB backup at `date`" > $STARTFILE

# mail -s "$UTIL_DISPLAY_NAME: Start DB Backup" $OPEMAIL < $STARTFILE

echo "$UTIL_DISPLAY_NAME: Start DB backup at `date`" > $RESULTFILE
echo " " >> $RESULTFILE

echo "stop slave: " >> $RESULTFILE
mysql -pKanaEki7 -e "stop slave" 2> OUT;
if [ -s OUT ]; then
        SENDEMAIL=true;
        cat OUT >> $RESULTFILE
fi;
# mysql -pKanaEki7 -e "stop slave" 2>> $RESULTFILE
echo " " >> $RESULTFILE

echo "Flush tables: " >> $RESULTFILE    
# mysql -pKanaEki7 -e "FLUSH TABLES WITH READ LOCK" 2>> $RESULTFILE
mysql -pKanaEki7 -e "FLUSH TABLES WITH READ LOCK" 2> OUT;
if [ -s OUT ]; then
        SENDEMAIL=true;
        cat OUT >> $RESULTFILE
fi;
echo " " >> $RESULTFILE

echo "Backup @PSS2_DB@: " >> $RESULTFILE
mysqldump -pKanaEki7 @PSS2_DB@ > $BACKUPDIR/@PSS2_DB@.sql 2> OUT;
if [ -s OUT ]; then
        SENDEMAIL=true;
        cat OUT >> $RESULTFILE
fi;
# mysqldump -pKanaEki7 @PSS2_DB@ > $BACKUPDIR/@PSS2_DB@.sql 2>> $RESULTFILE
echo " " >> $RESULTFILE

echo "Backup @ACCOUNTS_DB@: " >> $RESULTFILE
mysqldump -pKanaEki7 @ACCOUNTS_DB@ > $BACKUPDIR/@ACCOUNTS_DB@.sql 2> OUT;
if [ -s OUT ]; then
        SENDEMAIL=true;
        cat OUT >> $RESULTFILE
fi;
# mysqldump -pKanaEki7 @ACCOUNTS_DB@ > $BACKUPDIR/@ACCOUNTS_DB@.sql 2>> $RESULTFILE
echo " " >> $RESULTFILE

echo "Backup @JBOSSCACHE_DB@: " >> $RESULTFILE
mysqldump -pKanaEki7 @JBOSSCACHE_DB@ > $BACKUPDIR/@JBOSSCACHE_DB@.sql 2> OUT;
if [ -s OUT ]; then
        SENDEMAIL=true;
        cat OUT >> $RESULTFILE
fi;
# mysqldump -pKanaEki7 @JBOSSCACHE_DB@ > $BACKUPDIR/@JBOSSCACHE_DB@.sql 2>> $RESULTFILE
echo " " >> $RESULTFILE

echo "Backup @SC_DB@: " >> $RESULTFILE
mysqldump -pKanaEki7 @SC_DB@ > $BACKUPDIR/@SC_DB@.sql 2> OUT;
if [ -s OUT ]; then
        SENDEMAIL=true;
        cat OUT >> $RESULTFILE
fi;
# mysqldump -pKanaEki7 @SC_DB@ > $BACKUPDIR/@SC_DB@.sql 2>> $RESULTFILE
echo " " >> $RESULTFILE

echo "Backup @PARTDATA_DB@: " >> $RESULTFILE
mysqldump -pKanaEki7 @PARTDATA_DB@ > $BACKUPDIR/@PARTDATA_DB@.sql 2> OUT;
if [ -s OUT ]; then
        SENDEMAIL=true;
        cat OUT >> $RESULTFILE
fi;
# mysqldump -pKanaEki7 @PARTDATA_DB@ > $BACKUPDIR/@PARTDATA_DB@.sql 2>> $RESULTFILE
echo " " >> $RESULTFILE

echo "Backup @FIRELOG_DB@: " >> $RESULTFILE
mysqldump -pKanaEki7 @FIRELOG_DB@  --tables firelogentry --where="logdate > '$LASTDATE'" -c --insert-ignore --extended-insert --no-create-db --no-create-info > $LOGBACKUPDIR/@FIRELOG_DB@_$CURDATE.sql 2> OUT;
if [ -s OUT ]; then
        SENDEMAIL=true;
        cat OUT >> $RESULTFILE
fi;
# mysqldump -pKanaEki7 @FIRELOG_DB@  --tables firelogentry --where="logdate > '$LASTDATE'" -c --insert-ignore --extended-insert --no-create-db --no-create-info > $LOGBACKUPDIR/@FIRELOG_DB@_$CURDATE.sql 2>> $RESULTFILE
echo " " >> $RESULTFILE

echo "Start Slave: " >> $RESULTFILE
mysql -pKanaEki7 -e "start slave" 2> OUT;
if [ -s OUT ]; then
        SENDEMAIL=true;
        cat OUT >> $RESULTFILE
fi;
# mysql -pKanaEki7 -e "start slave" 2>> $RESULTFILE
echo " " >> $RESULTFILE

echo "DB Backup ends at `date`" >> $RESULTFILE

if [ -f $BACKUPPARENTDIR/$DELETEDATE ]; then
   rm -fR $BACKUPPARENTDIR/$DELETEDATE 2>> $RESULTFILE;
fi ;

if [ $SENDEMAIL ]; then
   mail -s "$UTIL_DISPLAY_NAME: End DB Backup" $OPEMAIL  < $RESULTFILE
fi ;


echo "DB Backup Ends"

