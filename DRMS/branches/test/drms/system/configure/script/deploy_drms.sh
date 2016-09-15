#!/bin/bash
# @(#) Deploy new version of DRMS app

function usage {
cat <<EOF
Usage: drms_deploy [OPTION]... VERSION
Deploy VERSION from Nexus to current OS.
Example: drms_deploy -u akua 8.1.1.0

  -u, --user                jira user name (prompts otherwise)

  -r, --recreate_db         Delete all databases and recreate them, load baseline data, update to current version.
                            Same as ant db:reset

  -t, --load_test_data util  Load test data into database and update to current version
                            Same as ant db:load:testData:<util>
						
 -i, -DinstallItronEar=true  Deploys the itron.ear to server. OPTIONAL and PGE Specific
 
 -d, -DinstallDrewebsiteEar=true  Deploys the drwebsite.ear to server. OPTIONAL and SCE Specific
                            
	

  -V, --version             print version information and exit
      --help                display this help and exit
	  

Open a Jira issue to Report bugs.

EOF
}

function checkUtility {

if [ "$UTILITY" == false ]; then

	var1="`ls -alrt | grep utility`"
	case ${var1} in
		(*utilities/sce*)
		   UTILITY="sce"
		   DEPLOY_DRWEBSITE=true
                   REDEPLOY_MOBILE=true
		;;
		(*utilities/pge*)
		UTILITY="pge"
		DEPLOY_ITRON=true
		;;
		(*)
		   echo "no match"
		   UTILITY="other"
		;;
	esac
	
else
	echo " $UTILITY is set : $UTILITY"
fi

}

function deploy {
  mkdir -p $ROOT_PATH
  mkdir -p $ROOT_PATH/$RESOURCES
  cd $ROOT_PATH

  svn_checkout
  
  # Changing the properties files according to utility 
  if [ "$UTILITY" == pge ]; then
     rm -rf /root/drms/current/resource/properties/properties-service.xml
	 rm -rf /root/drms/current/resource/properties/properties-service-all.xml
	 cd /root/drms/current/resource/properties
	 mv properties-service-pge.xml properties-service.xml
  else
    rm -rf /root/drms/current/resource/properties/properties-service.xml
	rm -rf /root/drms/current/resource/properties/properties-service-pge.xml
	cd /root/drms/current/resource/properties
	mv properties-service-all.xml properties-service.xml
  fi	

  rm -rf $ROOT_PATH/$CURRENT_LN
  ln -s $ROOT_PATH/$RELEASES_DIR/$TARGET_VERSION $ROOT_PATH/$CURRENT_LN

  ###Currently you can't deploy both itron and drwebsite but these are used for different clients.
  cd $ROOT_PATH/$CURRENT_LN
  if [ "$DEPLOY_ITRON" == true ]; then
	echo "Deploying Build $TARGET_VERSION with ITRON CLIENT EAR"
	ant server:stop server:backup server:install:all -DinstallRemoteEar=true -DinstallItronEar=true -DdrmsVersion=$TARGET_VERSION
  elif
    [ "$DEPLOY_DRWEBSITE" == true ]; then
	echo "Deploying Build $TARGET_VERSION with DRWEBSITE CLIENT EAR"
	ant server:stop server:backup server:install:all -DinstallRemoteEar=true -DinstallDrwebsiteEar=true -DinstallDrwebsiteEarFromNexus=true -DdrmsVersion=$TARGET_VERSION
  else
  echo "Deploying BASIC Build $TARGET_VERSION"
	ant server:stop server:backup server:install:all -DinstallRemoteEar=true -DdrmsVersion=$TARGET_VERSION
  fi
  
  if [ $? != 0 ]; then
    echo "Build Failure"
    exit 1
  fi

  deploy_db
## if [ "$REDEPLOY_MOBILE" == true ]; then
##  echo "Redeplying Mobile artifact"
##   cp $ROOT_PATH/scemobile/current/mobile-messenger-ear*.ear $JBOSS_HOME/server/production/deploy/mobile-messenger-ear.ear
##   cp $ROOT_PATH/scemobile/current/mysql-mobile-ds.xml $JBOSS_HOME/server/production/deploy/
##   cp $ROOT_PATH/scemobile/mobile-messenger.properties $JBOSS_HOME/server/production/conf/
 
## fi

  ant server:start
}

function svn_checkout {
  svn co https://akuasvn.openadr.com/svn/akuarepo/DRMS/$SVN_PATH/drms/system/configure $ROOT_PATH/$RELEASES_DIR/$TARGET_VERSION --username=$JIRA_LOGIN

  if [ $? != 0 ]; then
    echo "Failed to checkout build scripts from repository."
    exit 1
  fi
}

function deploy_db {
    ant db:backup
  if [ "$RECREATE_DB" == true ]; then
    ant db:reset -DdrmsVersion=$TARGET_VERSION
  elif [ "$LOAD_TEST_DATA" == true ]; then
    ant db:load:testData:$TEST_DATA_TYPE -DdrmsVersion=$TARGET_VERSION
  else
    ant db:update -DdrmsVersion=$TARGET_VERSION
  fi

  if [ $? != 0 ]; then
    echo "Build Failure"
    exit 1
  fi
}

###
### Main
###
RECREATE_DB=false
ROOT_PATH=/root/drms
RELEASES_DIR=releases
CURRENT_LN=current
DEPLOY_ITRON=false
DEPLOY_DRWEBSITE=false
REDEPLOY_MOBILE=false
UTILITY=false

while [ "$1" != "" ]; do
    case $1 in
	-u | --user )           shift
                                JIRA_LOGIN=$1
				;;
	-c | --utility )        shift
                                UTILITY=$1
				;;
	-r | --recreate_db )    RECREATE_DB=true
	;;
	-i | --deploy_itron )    DEPLOY_ITRON=true
	;;
	-d | --deploy_drwebsite )    DEPLOY_DRWEBSITE=true
	;;
	-t | --load_test_data ) LOAD_TEST_DATA=true
	shift
                                TEST_DATA_TYPE=$1
				;;
#        -i | --interactive )    interactive=1
#                                ;;
	-h | --help )           usage
                                exit
                                ;;
	* )                     break
    esac
    shift
done

checkUtility

echo "utility is" $UTILITY

if [ $# -eq 1 ]; then
  TARGET_VERSION=$1
  SVN_PATH=tags/akuacom-parent-$TARGET_VERSION
else
  usage
  exit 1
fi

deploy
