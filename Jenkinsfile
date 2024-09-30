pipeline {
    agent any

    tools {
        maven 'Maven'
        jdk 'JDK17'
    }

    stages {
        stage('Checkout') {
            steps {

                checkout scm
            }
        }

        stage('Build') {
            steps {

                sh 'mvn clean package'
            }
        }

        stage('Test') {
            steps {

                sh 'mvn test'
            }
        }

        stage('Code Coverage') {
            steps {

                sh 'mvn jacoco:report'
                jacoco execPattern: '**/target/jacoco.exec'
            }
        }

        stage('Publish Test Results') {
            steps {
                junit '**/target/surefire-reports/*.xml'
            }
        }

        stage('Archive Artifacts') {
            steps {
                // Optionally archive the built artifacts (e.g., JAR files)
                archiveArtifacts artifacts: '**/target/*.jar', allowEmptyArchive: true
            }
        }
    }

    post {
        always {
            echo 'Pipeline completed.'
        }
    }
}