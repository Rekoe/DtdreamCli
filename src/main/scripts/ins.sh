#!/usr/bin/env bash

#获取软件根目录
dtdream_home="$( cd "$( dirname "$0" )" && pwd )"

#设置环境变量
echo export DTDREAM_HOME=$dtdream_home >> /etc/profile
echo alias dtdream=\"sh $dtdream_home/startup.sh\" >> /etc/profile
echo " " >> /etc/profile
source /etc/profile

echo "install succeed!!!"
echo "have a good time with your 'dtdream' "
