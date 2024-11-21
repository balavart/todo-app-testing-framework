# TodoApp Test Automation Project

### Overview

The TodoApp Test Automation Project is dedicated to automating the testing of the Todo application's RESTful API. By
utilizing a combination of Spring Boot for the application context, TestNG for the testing framework, Maven for build
automation, and RestAssured for API interactions, this project ensures that all aspects of the Todo API are rigorously
tested. Additionally, Allure is integrated for generating detailed and visually appealing test reports, while Mockito
facilitates the creation of mock objects for unit testing.

### Requirements

* Java Development Kit (JDK): Version 17
* Maven: Version 3.6.3 or higher
* IDE: IntelliJ IDEA, Eclipse, or any other preferred Java IDE
* Git: For version control and cloning the repository

### Installation and Setup

#### Clone the Repository:

```sh
   git clone https://github.com/balavart/todo-app-testing-framework.git
   cd todo-app-testing
```

#### Install Project Dependencies:

```sh
   mvn clean install
```

This command will compile the project, run tests, and install the project artifacts into your local Maven repository.

### Loading and Running the Docker Image

1. Install the Docker app.
2. Navigate to the Docker Image directory:
3. Load the Docker image from the todo-app.tar file:

```sh
docker load < todo-app.tar
```

4. You should see output similar to:

```
Loaded image: todo-app:latest
```

5. Run the Docker container:

```sh
docker run -e VERBOSE=1 -p 8080:4242 todo-app:latest
```

The -e VERBOSE=1 flag enables verbose logging.
The -p 8080:4242 flag maps port 4242 inside the container to port 8080 on your host machine.

6. Verify the application is running:
   Open a browser or use curl to access http://localhost:8080/todos. You should receive an empty JSON array [].

### Project Structure

Understanding the project structure is essential for navigating and contributing effectively. Below is an overview of
the key directories and files:

```
├── src/
│   ├── main/
│   │   └── java/
│   │       └── com.example.todoapp/
│   │           ├── clients/
│   │           │   └── TodoApiClient.java
│   │           ├── config/
│   │           │   └── AppConfig.java
│   │           ├── model/
│   │           │   └── TodoItem.java
│   │           ├── service/
│   │           │   ├── TodoService.java
│   │           │   ├── TodoServiceImpl.java
│   │           │   └── TodoTestingApplication.java
│   │           └── resources/
│   │               ├── application.properties
│   │               ├── categories.json
│   └── test/
│       ├── java/
│       │   └── com.example.todoapp/
│       │       ├── fixtures/
│       │       │   └── TestDataConstants.java
│       │       ├── tests/
│       │       │   ├── api/
│       │       │   │   ├── TodoCreationTests.java
│       │       │   │   ├── TodoDeletionTests.java
│       │       │   │   ├── TodoRetrievalTests.java
│       │       │   │   ├── TodoSecurityTests.java
│       │       │   │   └── TodoUpdateTests.java
│       │       │   ├── service/
│       │       │   │   └── TodoServiceTests.java
│       │       │   ├── utils/
│       │       │   │   └── TestDataFactory.java
│       │       │   └── BaseTodoTestingApplicationTests.java
│       └── resources/
│           └── testng.xml
├── pom.xml
└── README.md
```

### Key Components:

* src/main/java: Contains the application code.
* src/test/java: Houses all test classes, organized into api and service packages.
* src/test/resources/testng.xml: Configuration file for TestNG specifying test suites and group filters.
* pom.xml: Maven configuration file managing dependencies and build plugins.
* README.md: Project documentation.

### Running Tests

#### Running All Tests

To execute all tests without any group filters, use the following Maven commands:

```sh
  mvn clean test
```

```sh
  mvn test
```

These commands will trigger the Maven Surefire Plugin to execute all test classes defined in testng.xml.

#### Running Specific Groups of Tests

Example Command:

```sh
mvn clean test -Dgroups=positive,negative
```

#### Generating Allure Reports

Allure provides visually appealing and comprehensive test reports. Follow these steps to generate and view Allure
reports:

1. Run Tests with Allure Integration:

```sh
mvn clean test
```

2. Generate and Serve Allure Report:

```sh
mvn allure:serve
```

This command will generate the report and automatically open it in your default web browser.

### Logging

The project utilizes Lombok and slf4j for simplified and efficient logging.

#### Key Points:

* @Slf4j Annotation: Enables the creation of a log object for logging.
* Logging Levels: Utilize different logging levels (info, debug, warn, error) to capture various details.

### Maven Configuration

The pom.xml file is central to managing the project's dependencies and build configurations. Below are the essential
configurations and dependencies used in the project.

#### Key Properties

```
<java.version>17</java.version>
<spring.boot.version>3.1.4</spring.boot.version>
<lombok.version>1.18.28</lombok.version>
<restassured.version>5.3.0</restassured.version>
<testng.version>7.8.0</testng.version>
<allure.version>2.20.1</allure.version>
<mockito.version>5.5.0</mockito.version>
<jackson.databind.version>2.15.2</jackson.databind.version>
<commons-compress.version>1.24.0</commons-compress.version>
<maven.surefire.plugin.version>2.22.2</maven.surefire.plugin.version>
<allure.maven.plugin.version>2.11.2</allure.maven.plugin.version>
<awaitility.version>4.2.0</awaitility.version>
<maven-compiler-plugin.version>3.11.0</maven-compiler-plugin.version>
```

#### Essential Dependencies

* Spring Boot: Core dependencies for Spring Boot.
* TestNG: Testing framework.
* RestAssured: Library for API interactions.
* Allure: Tool for generating test reports.
* Lombok: Reduces boilerplate code with annotations.
* slf4j: Abstraction for logging.
* Mockito: Library for creating mocks and spies in tests.

### API Interaction

The project includes interaction with the Todo application's API through the TodoApiClient class, which utilizes
RestAssured for sending HTTP requests and handling responses. The primary functionalities of the client include:

* Create Todo: Sends a POST request to create a new Todo item.
* Get Todos: Sends a GET request to retrieve a list of existing Todos with support for pagination.
* Update Todo: Sends a PUT request to update an existing Todo item.
* Delete Todo: Sends a DELETE request to remove a Todo item by its ID.

#### Integration with Allure

RestAssured is integrated with Allure to automatically generate steps and log API requests and responses. This
integration facilitates the creation of detailed and visually appealing reports that capture the essence of each test
execution.

### TestDataConstants Interface

* Interface containing constants used across test classes.

### TodoApiClient Class

The TodoApiClient class is responsible for interacting with the Todo application's API using RestAssured. It provides
methods to create, retrieve, update, and delete Todo items, facilitating seamless API testing.

#### Key Classes:

* TodoApiClient.java
* Purpose: Handles API requests and responses.
* Location: src/test/java/com/example/todoapp/tests/clients/TodoApiClient.java

#### Class Overview:

* Logging Filters: Adds RequestLoggingFilter and ResponseLoggingFilter for detailed logging of API interactions.
* Authorization Headers: Example authorization header included for secured endpoints.
* HTTP Methods: Implements methods corresponding to API endpoints (CRUD operations).