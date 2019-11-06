#!/bin/bash

JAR_FILE_NAME='person-data-web-1.0-SNAPSHOT.jar'

DATABASE_URL=jdbc:h2:mem:memorydb
DATABASE_USERNAME=sa
DATABASE_PASSWORD=password

java \
 -Dspring.datasource.url=${DATABASE_URL} \
 -Dspring.datasource.username=${DATABASE_USERNAME} \
 -Dspring.datasource.password=${DATABASE_PASSWORD} \
 -jar "../target/${JAR_FILE_NAME}" 
