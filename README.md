# JSON Diff Tool

This a project that enables through a microservices architecture based in spring boot and spring cloud the comparison of json object pairs. 

## Technical summary

The project is designed in a microservices architecture with the following components:

	 __________________ EurekaServer ________________
	|		       |		        |
LeftJsonService		RightJsonService	ComparatorService
	|		       |	           	|
	|______________ StoreServer (DB) _______________|

EurekaServer: Is the server that register all microservices location. Whenever a service wants to communicate with another service it ask EurekaServer for the location of the other service.
LeftJsonService: Service that expose endpoint for storing the json left part. It communicates with the StoreServer for persisting the information.
RightJsonService: Service that expose endpoint for storing the json right part. It communicates with the StoreServer for persisting the information.
ComparatorService: Service that expose endpoint for performing a comparison of the left and right json parts identified by an id.
StoreServer: This server hosts an H2 in memory database and exposes endpoints of mainly crud operations that are used by the other services.

###Technologies used:
Spring boot: mainly for having an embedded server which eases the development of microservices.
Lombok: Library for using annotations that avoid having to write common class methods (getters, setters, constructors).
Spring MVC: Exposes all the functionality of this kind of architecture (It for example has the controllers that enable the rest endpoints)
Junit5: For enabling unit and integration testing.
Spring cloud: exposes a server (Eureka) that manage multiple microservices. It also eases the communication between the services registered.
Spring boot devtools: functionality that eases the development process.
Apache commons codec: Library that has encoding/decoding utilities.
Gradle: all the project configuration and dependency management was designed.

## The projects in this repository:

For each one of the components show in the diagram of the previous section you will find a correspondent project in this respository. For the development was used the IDE STS 4, though, the projects are gradle based so that they can be
executed for the command line (Except for unit/integration testing which for the moment must be executed in the IDE). For the execution you must have installed JDK 1.8. 

### Executing the application

For executing the whole application go to terminal and execute the following commands:
1. In the ./JsonDiffEurekaServer directory:
gradlew.bat bootJar
java -jar build/libs/JsonDiffEurekaServer-0.0.1-SNAPSHOT.jar eureka

2. In the ./JsonDiffStore directory:
gradlew.bat bootJar
java -jar build/libs/JsonDiffStore-0.0.1-SNAPSHOT.jar

3. In the ./JsonDiffLeftJsonService directory:
gradlew.bat bootJar
java -jar build/libs/JsonDiffLeftJsonService-0.0.1-SNAPSHOT.jar

4. In the ./JsonDiffRightJsonService directory:
gradlew.bat bootJar
java -jar build/libs/JsonDiffRightJsonService-0.0.1-SNAPSHOT.jar

5. In the ./JsonDiffComparatorService directory:
gradlew.bat bootJar
java -jar build/libs/JsonDiffComparatorService-0.0.1-SNAPSHOT.jar

Now the application is deployed you can use postman for interacting with the application:
Example of usage (with id = 20):
1. Create left json:
POST http://localhost:8084/diff/20/left
Headers: Content-Type: application/x-www-form-urlencoded
Body: ew0KCSJqc29uUHJvcGVydHkiOiAiTGVmdCBqc29uIHN0cmluZyA0NTQiLA0KCSJqc29uUHJvcGVydHkyIjogew0KCQkianNvblByb3BlcnR5IjogIkxlZnQganNvbiBzdHJpbmcgNDU0Ig0KCX0NCn0=
(Body decoded match the following json: "{\r\n\t\"jsonProperty\": \"Left json string 454\",\r\n\t\"jsonProperty2\": {\r\n\t\t\"jsonProperty\": \"Left json string 454\"\r\n\t}\r\n}")

2. Create right json:
POST http://localhost:8085/diff/20/right
Headers: Content-Type: application/x-www-form-urlencoded
Body: ew0KCSJqc29uUHJvcGVydHkiOiAiTGVmdCBqc29uIHN0cmluZyA0NTQiLA0KCSJqc29uUHJvcGVydHkyIjogew0KCQkianNvblByb3BlcnR5IjogIkxlZnQganNvbiBzdHJpbmcgNDU0Ig0KCX0NCn0=
(Body decoded match the following json: "{\r\n\t\"jsonProperty\": \"Left json string 454\",\r\n\t\"jsonProperty2\": {\r\n\t\t\"jsonProperty\": \"Left json string 454\"\r\n\t}\r\n}")

3. Execute compare of the two previous jsons created:
GET http://localhost:8086/diff/20
It must return the following json:
{
    "equal": true,
    "jsonReport": "Both json parts are equal."
}

### Executing unit/integration testing:

For the execution of the test you must have installed STS and perform the following configuration:

#### IDE configuration (STS 4):
For the proper loading of the projects it is necessary to add lombok library 1.18.6.  
1. Download lombok-1.18.6.jar+
2. Rename the jar to lombok.jar
3. Add the jar to the root folder of the STS installation:
4. In the STS ini file add the following line aat the end (In the STS installation folder in the file SpringToolSuite4.ini):
-javaagent:lombok.jar
5. Restart STS

#### Execution of integration tests:
In the test folder of each project you will find a file with name of the main class of each project with the sufix IT, 
for example for the project ./JsonDiffRightJsonService you will find the integration test JsonDiffLeftJsonServiceApplicationIT.java. 
You can run the test right clicking in the file and selection Run as -> Junit test. 
**Note:** for the execution of tests for the projects ./JsonDiffLeftJsonService, ./JsonDiffRightJsonService and ./JsonDiffComparatorService you must have running the eureka server and the store server.

#### Execution of unit tests:
In the test folder of each project you will find a file with name of the main class of each project with the sufix Test, 
for example for the project ./JsonDiffRightJsonService you will find the unit test LeftJsonServiceTest.java
You can run the test right clicking in the file and selection Run as -> Junit test. 

## Things for improvement:
- Add Swagger documentation
- Configure tests tasks for be able to run them from the terminal
- Dockerize solution