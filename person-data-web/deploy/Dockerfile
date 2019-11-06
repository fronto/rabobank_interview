FROM openjdk:11

WORKDIR /

COPY ./person-data-web-1.0-SNAPSHOT.jar /person-data-web.jar

EXPOSE 8080 80

ENV DATABASE_URL=jdbc:h2:tcp://localhost:1521/test
ENV DATABASE_USERNAME=sa
ENV DATABASE_PASSWORD=

CMD java \
 -Dspring.datasource.url=${DATABASE_URL} \
 -Dspring.datasource.username=${DATABASE_USERNAME} \
 -Dspring.datasource.password=${DATABASE_PASSWORD} \
 -jar person-data-web.jar
