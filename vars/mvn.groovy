#!/usr/bin/env groovy

def call() {
    properties([parameters([
        string(defaultValue: 'mvn', description: 'Version of Maven to build(ex. apache-maven-3.6.0)', name: 'YourName', trim: true),
        string(defaultValue: 'java', description: 'openjdk version for compile(ex. openjdk8, openjdk9 openjdk10)', name: 'YourName', trim: true)
    ])])

    node() {
        stage('checkout') {
            git branch: 'master', credentialsId: 'hyunil-shin-integration', url: 'https://github.com/hyunil-shin/java-maven-junit-helloworld.git'
        }
        
        stage('build') {
            withEnv(["PATH+MAVEN=${tool 'apache-maven-3.6.0'}/bin", "JAVA_HOME=${tool 'openjdk8'}/bin"]) {
                sh 'mvn --version'           
                sh "mvn clean ${params.testType} -Dmaven.test.failure.ignore=true"
            }
        }
        
        stage('test') {
            junit 'target/surefire-reports/*.xml'
            jacoco execPattern: 'target/**.exec'
        }
        
        stage('report') {
            if(currentBuild.result == "FAILURE" || currentBuild.result == "UNSTABLE") {
                
            }
        }
    }

}
