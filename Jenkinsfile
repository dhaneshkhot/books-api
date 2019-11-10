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
        stage ("Build Docker Image and Push") {
            steps {
                sh 'docker build -t books-api .'
                // sh 'docker push ' // Idea is to push to docker registry
            }
        }
        stage ("Deploy Docker Image") {
            steps {
                // Idea is to run another jenkins job which pulls image from registry
                sh 'docker run -p 9090:9090 books-api'
            }
        }
    }
}
