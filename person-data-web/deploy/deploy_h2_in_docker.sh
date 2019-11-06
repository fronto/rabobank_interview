#!/bin/bash

docker run -d --name database -p 1521:1521 -p 81:81 -v $PWD/data:/opt/h2-data -e H2_OPTIONS=-ifNotExists oscarfonts/h2:1.4.199
