#!/bin/bash
JAR=agent-air.jar
PID=`ps -ef | grep $JAR | grep -v grep | awk '{print $2}'`

if [ "$PID" != "" ]
then
	echo already running...
else
	echo start agent...
	nohup java -jar $JAR --spring.config.location=./ --spring.config.name=application 1> /dev/null 2>&1 &
	echo done!!!
fi
