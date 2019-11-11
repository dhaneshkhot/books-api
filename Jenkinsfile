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
        stage ("Deploy Docker Image to test locally") {
            steps {
                sh 'docker run -d --name books-api -p 9090:9090 -e "SPRING_PROFILES_ACTIVE=dev" books-api'
                sh 'docker-compose up'
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
