FROM openjdk:8-jdk-alpine
VOLUME /tmp
ADD target/SR-1.0-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-Dspring.profiles.active=staging","-jar","/app.jar"]