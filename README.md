# **Rabobank Interview Exercise**

I have completed all the "MUSTs" (M1-M5)

I have completed all the "SHOULDs" (S1-S4)

I have not yet completed any of the "COULDs" although I have started C1 and created the bi-directional association
between Person and Pet, cascading operations from Person to Pet.
To complete the requirement, representing  the association in the REST api may invite the use of HATEOS for ease
of navigation.

For requirement C2 I would suggest a feature toggle

As for the bonus section, the application and it's database can be deployed using **docker-compose**

To run the application carry out the following steps:

1) clone the project

2) run `mvn clean install` from the top level directory

3) `cd person-data-web/deploy/` and run `./build_and_run_using_docker-compose.sh`

...the application should now be running on port 8080 with the database running on port 1521. The http interface to the database
is available on port 81

**The following tooling versions were used to develop the application:**

java version: "11.0.4" 2019-07-16 LTS

Maven version: 3.6.2

Docker version 19.03.4, build 9013bf5

docker-compose version 1.24.1, build 4667896b
