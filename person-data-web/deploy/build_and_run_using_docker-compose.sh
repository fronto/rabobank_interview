#!/bin/bash

set -e

if [ -d work_area ]
then
  rm -Rf work_area
fi
mkdir work_area

JAR_NAME=person-data-web-1.0-SNAPSHOT.jar
cp "../target/${JAR_NAME}" work_area
cp Dockerfile work_area

docker-compose -f docker-compose.yml build

function getMyIP() {
    local _ip _line
    while IFS=$': \t' read -a _line ;do
        [ -z "${_line%inet}" ] &&
           _ip=${_line[${#_line[1]}>4?1:2]} &&
           [ "${_ip#127.0.0.1}" ] && echo $_ip && return 0
      done< <(LANG=C /sbin/ifconfig)
}

IP_ADDRESS=$(getMyIP)

export DATABASE_URL="jdbc:h2:tcp://${IP_ADDRESS}:1521/test"
export DATABASE_USERNAME=sa
export DATABASE_PASSWORD=

docker-compose -f docker-compose.yml up -d
