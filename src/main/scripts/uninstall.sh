#!/usr/bin/env bash

#获取软件根目录
DTDREAM_HOME="$( cd "$( dirname "$0" )" && pwd )"

echo "清除环境变量"
sed -i '/^export DTDREAM_HOME*/d' /etc/profile
sed -i '/^alias dtdream*/d' /etc/profile
source /etc/profile

echo "清空工作目录"
rm -rf $DTDREAM_HOME
