pipeline {
    agent {
      docker {
        image 'maven:3.5.2-jdk-8-alpine'
      }
    }

    stages {
        stage('Clean') {
            steps {
                sh 'mvn clean'
            }
        }
        stage('Build') {
            steps {
                sh 'mvn package org.springframework.boot:spring-boot-maven-plugin:1.5.9.RELEASE:repackage'
            }
        }
        stage('Dockerize') {
            steps {
                sh 'docker build --build-arg JAR_FILE=target/SR-1.0-SNAPSHOT.jar --tag scienceroot:backend .'
            }
        }
    }
}
