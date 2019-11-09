pipeline {
    agent {
        docker {
                image 'maven:3-alpine'
                args '-u root'
        }
    }
    tools {
        maven 'Maven 3.6.2'
        jdk 'jdk8'
    }
    stages {
        stage ('Package') {
            steps {
                sh 'mvn clean package'
            }
        }

    }
}
