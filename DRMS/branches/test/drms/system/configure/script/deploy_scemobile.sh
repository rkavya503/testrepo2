#!/bin/bash
# @(#) Deploy new version of GreenLantern app


function usage {
  cat <<EOF
Usage: deploy_GreenLantern -u xxxx VERSION
Deploy VERSION from Nexus to current OS.
Example: deploy_GreenLantern.sh -u akua 8.1.1.0

  -u, --user                jira user name (prompts otherwise)
  

  -V, --version             print version information and exit
      --help                display this help and exit

EOF
}


function deploy {
   echo "in deploy function"
  
  cp $ROOT_PATH/$RELEASES_DIR/$TARGET_VERSION/mobile-messenger-ear*.ear $JBOSS_HOME/server/production/deploy/mobile-messenger-ear.ear
  cp  $ROOT_PATH/$RELEASES_DIR/$TARGET_VERSION/mysql-mobile-ds.xml $JBOSS_HOME/server/production/deploy/
	
}

function svn_checkout {
#  echo "in checkout path is :  https://akuasvn.openadr.com/svn/akuarepo/DRMS/$SVN_PATH/mobile-configure "
  svn co https://akuasvn.openadr.com/svn/akuarepo/DRMS/$SVN_PATH/mobile-configure $ROOT_PATH/$RELEASES_DIR/$TARGET_VERSION --username=$JIRA_LOGIN

  if [ $? != 0 ]; then
    echo "Failed to checkout sce-mobile from repository."
    exit 1
  fi

}

###
### Main
###
ROOT_PATH=/root/drms/scemobile
RELEASES_DIR=releases
CURRENT_LN=current

service jboss7 stop
sleep 5 

while [ "$1" != "" ]; do
    case $1 in
        -u | --user )           shift
                                JIRA_LOGIN=$1
                                ;;
	
        -h | --help )           usage
                                exit
                                ;;
        * )                     break
    esac
    shift
done


if [ $# -eq 1 ]; then
  TARGET_VERSION=$1
  SVN_PATH=tags/mobile-messenger-parent-$TARGET_VERSION
#   echo "setting path svn path is :  $SVN_PATH"


else
  usage
  exit 1
fi
mkdir $ROOT_PATH/$RELEASES_DIR/$TARGET_VERSION
rm -rf $ROOT_PATH/$CURRENT_LN
ln -s $ROOT_PATH/$RELEASES_DIR/$TARGET_VERSION $ROOT_PATH/$CURRENT_LN


svn_checkout
ant sceMobile -DreleaseNumber=$TARGET_VERSION
deploy

service jboss start
echo "jtail to see successful startup  "

