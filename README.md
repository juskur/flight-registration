# Developer Interview Task Solution: Flight Registration System

Solution is developed with spring boot and build with gradle.
Build and run solution with single gradle wrapper command 
inside root folder flight-registration:
> flight-registration> gradlew buildAndRun

Please use other gradle wrapper commands to build and test the code, 
build, run and stop docker image. Swagger UI of running application
can be accessed at http://localhost:8080/swagger-ui/index.htm.
Please note that all tests are in core module.

With Swagger UI register flight, mark it as arrived and get the flight 
to see that it has been assigned to a terminal. Depart flight, get flight
again to see that terminal is not assigned anymore. Get all terminals
to see what flight is assigned to what terminal.

##Gradle wrapper commands
Execute these gradle wrapper commands in the top folder flight-registration:
Build all modules and run all the tests
###Build all modules and run all the tests
> gradlew build
>
###Build docker image for spring boot application
> gradlew dockerBuild
> 
###Run image as a container
> gradlew dockerRun
> 
When spring boot starts inside container Swagger UI
is accessible at default url http://localhost:8080/swagger-ui/index.htm

###Stop container and remove it
> gradlew dockerStopAndRemoveContainer
> 
Stops container and removes it from docker so dockerRun can be executed again
###Build and run solution
> gradlew buildAndRun
>

##Modules
### :billofmaterials
Module where all dependency versions are defined
### :backendApplication:core
Core functionality with integrations tests
### :backendApplication:presentation
REST API as presentation of core functionality
### :backendApplication:deployable
Spring boot application and Dockerfile as deployable REST API.
