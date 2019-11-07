# **Rabobank Interview Exercise**

I have completed all the "MUSTs" (M1-M5)

I have completed all the "SHOULDs" (S1-S4), although I have not run the Pet REST api through integration tests (yet)

I have not yet attempted any of the "COULDs" although I can see that for C1 this requirement would involve a one-to-many association 
from Person to Pet, and many-to-one association the other way. I can see that in the database this would warrant foreign keys
and cascading deletes in the ORM. Representing the association in the REST api may invite the use of HATEOS for ease
of navigation.

For requirement C2 I would suggest a feature toggle

As for the bonus section, the application and it's database can be deployed using **docker-compose**

To run the application carry out the following steps:

1) clone the project

2) run `mvn clean install` from the top level directory

3) `cd person-data-web/deploy/` and run `./build_and_run_using_docker-compose.sh`

...the application should now be running on port 8080 with the database running on port 1521. The http interface to the database
is available on port 81

