#!/usr/bin/env bash
mode=prod
topic=xyebank_relog
port=9092

BASE_DIR=$(cd "$(dirname "$0")";pwd)
JAVA_HOME=`${JAVA_HOME}`
if [${JAVA_HOME} == ""]

    then
        JAVA_HOME=/usr/java/jdk1.8.0_65
fi

while getopts "m:p:t:" arg
    do
        case $arg in
            "m")
                mode=$OPTARG
                ;;
            "p")
                port=$OPTARG
                ;;
            "t")
                topic=$OPTARG
                ;;
            "?")
                echo "unknow argument"
                ;;
        esac
    done

${JAVA_HOME}/bin/java -Xms2g -Xmx2g -cp ${BASE_DIR}:${BASE_DIR}/lib/*:${BASE_DIR}/conf  main.ProducerMain ${mode} ${topic} ${port}
