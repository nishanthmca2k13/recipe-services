# recipe-services
Recipe is designed to add new recipe details in the database and filter the recipes based on the filter criteria

### System Design
Recipe Web Service is microservice based layered architectured RESTful Web Service. 
    - Top layer, which is main interface available for intgeration and interaction with front-end or end user to consume APIs
- Service Layer
    - This layer sits in between API layer and Data access layer with some utility functionality
    - Mainly responsible for interacting with Data Access Layer and transferring the recipes data as required by top and below layers
    - It's just another module added to decouple business logic of recipes data transfer and mapping from/to API layer
- Data Access Layer
    - Responsible to provide Object Relationship Mapping (ORM) between higher level recipe Java objects and persistence layer tables
    - [Springboot-starter-data-JPA](https://spring.io/guides/gs/accessing-data-jpa/) module is used to implement mappings between objects and tables
    - This layer contains recipe entity classes and JPA repositories which implement lower level functionality of storing/retrieving recipes data
- Persistence Layer
    - Bottom most layer, responsible for physically storing the recipes data onto database table
    - Just one physical table - `recipes` is used to store the recipes data for the service
    - [MySQL]((https://www.mysql.com/) is configured to be used as database service
    - For development and testing purposes, the Embedded H2 Database provided by Spring Boot framework is also utilized
    
### Supported Features
Feature | Software Module Used
------------ | -------------
ReSTful API | [Springboot](https://spring.io/projects/spring-boot)
Object Relationship Mapping | [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
Logging | [SLF4J](http://www.slf4j.org/manual.html) Logger
Unit Tests | Junit 5 with [AssertJ](https://assertj.github.io/doc/)
Integration Tests | Rest Assured [RestAssured](https://rest-assured.io/)
Documentation |  Swagger [Swagger](https://swagger.io/tools/swaggerhub/)

### Prerequisites
* [JDK 11](https://www.oracle.com/in/java/technologies/javase/javase-jdk8-downloads.html)
* [Apache Maven](https://maven.apache.org/)
* [MySQL](https://www.mysql.com/)

### Steps to build Web Service
* clone the repository from github or download code zip 
* move to the project root folder and then `mvn clean package` or we can import to IDE and do the same process
* On successfull build completion, one should have web service jar in `target` directory named as `Recipes-Service-0.0.1-SNAPSHOT.jar`

### Steps to execute Web Service
* **Execution on Development profile with Embedded H2 Database**
    - In Development Mode, by default web service uses [Embedded H2 database](https://spring.io/guides/gs/accessing-data-jpa/) for persisting and retrieving recipes details.
    - Command to execute:
   ```
        java -jar target/Recipes-Service-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev --logging.level.root=INFO
   ```
    - On successfull start, one should notice log message on console `Tomcat started on port(s): 8080 (http)` and have web service listening for web requests at port 8080
    - you can access swagger documentation with [swagger](http://localhost:8080/swagger-ui/index.html) 
    - you can access [h2-db console](http://localhost:8080/h2-console/)

### Web Service ReST API End Points
Recipe Webservice comes with ReST API Ends points for authentication, creating a new recipe, retrieveing an existing recipe, retrieving all existing recieps as list, updating an an existing recipe and deleting an existing recipe. Below table lists and describes on the implemented ReST APIs:
**Note: With all given below api end points request, make sure to include header `Content-Type as application/json`**
API End Point | Method | Purpose | Request | Response
------------ | ------------- | ------------- | ------------ | -------------
/recipe | POST | Create a new recipe | Recipe Model | Recipe Model with 201 Created on Success, 400 Bad request on failure
/recipe/filter | POST | filter recipes based on search criteria | List of RecipeVO Model with 200 on Success, 204 if no results found with search
