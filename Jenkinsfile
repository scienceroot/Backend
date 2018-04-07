#!/usr/bin/env groovy

node {
    stage('Build source') {
        docker.withRegistry('https://docker.scienceroots.com', 'docker-registry') {
            docker.image('maven:3.5.2-jdk-8-alpine').inside {
                checkout scm

                sh 'mvn clean package org.springframework.boot:spring-boot-maven-plugin:1.5.9.RELEASE:repackage -DskipTests'
            }

            def image = docker.build("docker.scienceroots.com/scienceroot:${env.BRANCH_NAME}")
            image.push()
        }
    }
}