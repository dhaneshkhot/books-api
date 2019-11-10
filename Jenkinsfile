pipeline {
    agent any
    tools {
        maven 'Maven 3.6.2'
        jdk 'jdk8'
    }
    stages {
        stage ("Package") {
            steps {
                    sh 'mvn clean package'
            }
        }
        stage ("Build Docker Image") {
            steps {
                    sh 'docker build -t books-api .'
            }
        }
    }
}
