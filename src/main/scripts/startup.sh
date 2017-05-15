#!/usr/bin/env bash

para=$*

#获取软件根目录
DTDREAM_HOME="$( cd "$( dirname "$0" )" && pwd )"
#echo "dtdream_home: $DTDREAM_HOME"
java -Dconfig_location="$DTDREAM_HOME/config/config.properties" -jar $DTDREAM_HOME/DtdreamCli-1.0.0.jar dtdream $para
