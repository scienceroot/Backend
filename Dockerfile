FROM openjdk:8-jdk-alpine
VOLUME /tmp
ADD target/SR-1.0-SNAPSHOT.jar app.jar
EXPOSE 8080

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-DSCR_GENESIS_SEED=$${SCR_GENESIS_SEED}","-jar","/app.jar"]