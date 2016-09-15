#!/bin/bash

function testTarget() {
    echo "*******************************"
    echo "Testing target $1"
    echo "*******************************"
    ant $1
    if [ $? -eq 1 ]; then
	exit 1
    fi
}

testTarget "server:install"               
testTarget "server:install:all"           
testTarget "server:install:autoGen"       
testTarget "server:install:cache"         
testTarget "server:install:cloudCluster"  
testTarget "server:install:min"           
testTarget "server:install:native"        
testTarget "server:backup"                
testTarget "server:remove"                
#testTarget "server:restart"               
#testTarget "server:start"                 
#testTarget "server:stop"                  
testTarget "configure:install"            
testTarget "configure:install:all"        
testTarget "configure:install:autoGen"    
testTarget "configure:install:cache"      
testTarget "configure:install:min"        
testTarget "configure:backup"             
testTarget "configure:remove"             
testTarget "db:create"                    
#testTarget "db:diff"                      
testTarget "db:drop"                      
testTarget "db:reset"                     
testTarget "db:dump"                      
testTarget "db:dump:all"                  
testTarget "db:dump:baseline"             
testTarget "db:dump:data"                 
testTarget "db:dump:routines"             
testTarget "db:dump:schema"               
#testTarget "db:generate:changelog"        
testTarget "db:load:testData"             
testTarget "db:load:testData:fl"          
testTarget "db:load:testData:sce"         
testTarget "db:load:utilityData"          
testTarget "db:backup"                    
#testTarget "db:rollback"                  
#testTarget "db:rollback:singleDatabase"   
testTarget "db:setAdminPass"              
#testTarget "db:tag"                       
#testTarget "db:tag:singleDatabase"        
testTarget "db:update"                    
#testTarget "install.jb5"                  
testTarget "load.data"                    
#testTarget "static:deploy:remote"        
#testTarget "static:remote:backup"         
#testTarget "test.maven"                   

#sucess
echo "test successful"
exit 0