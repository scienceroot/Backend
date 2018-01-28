pipeline {
    agent {
      docker {
        image 'maven:3.5.2-jdk-8-alpine'
      }
    }

    stages {
        stage('Build') {
	        agent {
		      docker {
		        image 'node:9-alpine'
		      }
		    }
            steps {
                sh 'mvn clean package org.springframework.boot:spring-boot-maven-plugin:1.5.9.RELEASE:repackage'
            }
        }
        stage('Dockerize') {
        		agent any
            steps {
                sh 'docker build --build-arg JAR_FILE=target/SR-1.0-SNAPSHOT.jar --tag scienceroot:backend .'
            }
        }
    }
}
