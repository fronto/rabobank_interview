#!/bin/bash

function getMyIP() {
    local _ip _line
    while IFS=$': \t' read -a _line ;do
        [ -z "${_line%inet}" ] &&
           _ip=${_line[${#_line[1]}>4?1:2]} &&
           [ "${_ip#127.0.0.1}" ] && echo $_ip && return 0
      done< <(LANG=C /sbin/ifconfig)
}

IP_ADDRESS=$(getMyIP)
DATABASE_URL="jdbc:h2:tcp://${IP_ADDRESS}:1521/test"

docker run -d \
--name person_data_app \
-p 8080:8080 \
-e DATABASE_URL=${DATABASE_URL} \
-e DATABASE_USERNAME=sa
-e DATABASE_PASSWORD= \
person_data_app
