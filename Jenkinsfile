pipeline {
	agent none
    stages {
        stage('Build') {
        		agent {
		      docker {
		        image 'maven:3.5.2-jdk-8-alpine'
		      }
		    }
            steps {
                sh 'mvn clean package org.springframework.boot:spring-boot-maven-plugin:1.5.9.RELEASE:repackage'
            }
        }
        stage('Dockerize') {
        		agent docker
            steps {
                docker.build('scienceroot:backend')
            }
        }
    }
}
