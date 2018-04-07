#!/usr/bin/env groovy

docker.image('maven:3.5.2-jdk-8-alpine').inside {

    stage 'checkout'
    checkout scm


    stage 'build'
        sh 'mvn clean package org.springframework.boot:spring-boot-maven-plugin:1.5.9.RELEASE:repackage -DskipTests'
}

node {
    stage 'Build Docker Image'
    docker.withRegistry('https://docker.scienceroot.com', 'docker-registry') {
        sh "docker build --tag=docker.scienceroots.com/scienceroot:${env.BRANCH_NAME} ."
        sh "docker push docker.scienceroots.com/scienceroot:${env.BRANCH_NAME}"
    }
}