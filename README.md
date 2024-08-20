# quarkus-angular-demo

A Demo Quarkus / Angular fullstack mini project.
The application performs crud operations between authors and their books that exist in a many-to-many relationship
through a RESTful API.

### Prerequisites

* Java 21
* PostgreSQL (or any other RDBMS)
* Angular 18.2.0

### Quarkus backend Setup

Create an `application.properties` file under src/main/resources with details in regards to the dataSource of your
liking.

### Packaging and running the application

To run the quarkus application in dev mode execute the following:

```shell script
./mvnw compile quarkus:dev
```

The application can be packaged using:

```shell script
./mvnw package
```

Afterward the application can be run using the command `java -jar target/quarkus-app/quarkus-run.jar`.

### Angular setup

Add your quarkus URL to the environment files and run the angular app (after ```cd angular-demo-app```) using: 
```shell script
ng serve
```