# Backend
Basics of the backend based on java with spring

Requires h2database http://www.h2database.com/html/main.html running as a service

checking of jwt token not yet enabled by default for the ease of debugging, can be activated by changing 'Application.java' line ~21ish:

registrationBean.addUrlPatterns("/PathToSecure/*");

Backend is using springboot, running 'Application.java' should suffice, will start committing .jars/.wars as well

## Development

### Run locally

`mvn org.springframework.boot:spring-boot-maven-plugin:1.5.9.RELEASE:run`

### Build

Creates an executable JAR file and builds a docker conatiner running the JAR.

#### Executable JAR

`mvn clean package org.springframework.boot:spring-boot-maven-plugin:1.5.9.RELEASE:repackage`
 
#### Docker container

Build the container
`docker build --build-arg JAR_FILE=target/SR-1.0-SNAPSHOT.jar --tag scienceroot:backend  .`

Run the container
`docker run -it -p 8080:8080 scienceroot:backend`
 
 
