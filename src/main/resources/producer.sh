#!/usr/bin/env bash

BASE_DIR=$(cd "$(dirname "$0")";pwd)
JAVA_HOME=`${JAVA_HOME}`
if [${JAVA_HOME} == ""]

    then
        JAVA_HOME=/usr/java/jdk1.8.0_65
fi

${JAVA_HOME}/bin/java -Xms2g -Xmx2g -cp ${BASE_DIR}:${BASE_DIR}/lib/*:${BASE_DIR}/conf  main.ProducerMain
