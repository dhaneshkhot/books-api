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
                sh 'docker build --rm -t books-api .'
            }
        }
        stage ("Start mysql") {
            steps {
                sh 'docker-compose up -d'
            }
        }
        stage ("Deploy Docker Image to test locally") {
            steps {
                sh 'docker run -d --rm --name=books-api --net=my-network  -p 9090:9090 -e "SPRING_PROFILES_ACTIVE=docker" books-api'
            }
        }
        stage('E2E Tests'){
            steps{
                checkout([$class: 'GitSCM', branches: [[name: '*/master']], doGenerateSubmoduleConfigurations: false, extensions: [[$class: 'CloneOption', depth: 0, noTags: true, reference: '', shallow: false, timeout: 60], [$class: 'SubmoduleOption', disableSubmodules: false, parentCredentials: false, recursiveSubmodules: true, reference: '', timeout: 60, trackingSubmodules: true], [$class: 'RelativeTargetDirectory', relativeTargetDir: 'E2E'],[$class: 'CheckoutOption', timeout: 60]], submoduleCfg: [], userRemoteConfigs: [[url: 'https://github.com/dhaneshkhot/books-api-rest-assured.git']]])
                dir("E2E"){
                    sh 'ls -ltr'
                    sh 'mvn test -Dtest="com.example.tests.books.BooksEndToEndTests" -Denv=docker -DdbUsername=root -DdbPassword=password'
                }
            }
        }
        stage ("Cleanup") {
            steps {
                sh 'docker stop books-api'
                sh 'docker rm books-api'
                sh 'docker rmi books-api:latest -f'
                sh 'docker-compose down'
            }
        }
    }
}
