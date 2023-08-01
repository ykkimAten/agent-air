#!/bin/bash

JAR=agent-air.jar
PID=`ps -ef | grep $JAR | grep -v grep | awk '{print $2}'`

if [ "$PID" != "" ]
then
	kill -15 $PID
	echo done!!!
else
	echo already stopped
fi
