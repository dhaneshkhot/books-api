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
                sh 'docker run -d --name=books-api --net=my-network  -p 9090:9090 -e "SPRING_PROFILES_ACTIVE=docker" books-api'
            }
        }
        stage('E2E Tests'){
            steps{
                dir("E2E"){
                    sh 'pwd'
                    sh 'ls -ltr'
                    sh 'ls ./E2E -ltr'
                    checkout([$class: 'GitSCM', branches: [[name: '*/master']], doGenerateSubmoduleConfigurations: false, extensions: [[$class: 'CloneOption', depth: 0, noTags: true, reference: '', shallow: false, timeout: 60], [$class: 'SubmoduleOption', disableSubmodules: false, parentCredentials: false, recursiveSubmodules: true, reference: '', timeout: 60, trackingSubmodules: true], [$class: 'RelativeTargetDirectory', relativeTargetDir: 'server-core'],[$class: 'CheckoutOption', timeout: 60]], submoduleCfg: [], userRemoteConfigs: [[url: 'https://github.com/dhaneshkhot/books-api-rest-assured.git']]])
                    sh 'pwd'
                    sh 'ls -ltr'
                    sh 'ls ./E2E -ltr'
                }
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
