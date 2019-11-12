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
        stage ("Start mysql") {
            steps {
                sh 'docker-compose up -d'
            }
        }
        stage ("Deploy Docker Image to test locally") {
            steps {
                sh 'docker run -d --name=books-api --net=my-network  -p 9090:9090 -e "SPRING_PROFILES_ACTIVE=dev" books-api'
            }
        }
//         stage ("Cleanup") {
//             steps {
//                 sh 'docker stop books-api'
//                 sh 'docker rm books-api'
//                 sh 'docker rmi books-api:latest -f'
//             }
//         }
    }
}
