pipeline {
    agent any
    tools {
        maven 'Maven 3.6.2'
        jdk 'jdk8'
    }
    stages {
        stage ("initialize") {
            steps {
                sh '''
                echo "PATH = ${PATH}"
                echo "M2_HOME = ${M2_HOME}"
                '''
            }
         }
    stages {
        stage ('Package') {
            withMaven(maven : 'Maven 3.6.2'){
                sh 'mvn clean package'
            }
        }

    }
}
