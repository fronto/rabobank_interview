#!/bin/bash

JAR_FILE_NAME='person-data-web-1.0-SNAPSHOT.jar'

DATABASE_URL=jdbc:h2:tcp://localhost:1521/test
DATABASE_USERNAME=sa
DATABASE_PASSWORD=

java \
 -Dspring.datasource.url=${DATABASE_URL} \
 -Dspring.datasource.username=${DATABASE_USERNAME} \
 -Dspring.datasource.password=${DATABASE_PASSWORD} \
 -jar "../target/${JAR_FILE_NAME}" 
