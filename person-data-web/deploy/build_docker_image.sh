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

cd work_area
DOCKER_IMAGE_ID=$(docker build . | grep built | awk '{print $NF}')
docker tag $DOCKER_IMAGE_ID person_data_app
cd ..

echo "Built docker with image id $DOCKER_IMAGE_ID tagged as 'person_data_app'"
