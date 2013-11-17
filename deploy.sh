#!/bin/bash
play dist
scp target/universal/ifcfg-api-1.0-SNAPSHOT.zip root@192.168.5.117:/opt/apps/
ssh root@192.168.5.117 'kill `ps ax | grep ifcfg | grep -v grep | awk "{ print $1 }"`'
ssh root@192.168.5.117 "cd /opt/apps; mv ifcfg-api-1.0-SNAPSHOT.zip ifcfg.net/; cd ifcfg.net; unzip -o ifcfg-api-1.0-SNAPSHOT.zip"
ssh root@192.168.5.117 "cd /opt/apps/ifcfg.net/; nohup ./ifcfg-api-1.0-SNAPSHOT/bin/ifcfg-api -Dhttp.port=9393 &"
