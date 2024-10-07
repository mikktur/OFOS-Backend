pipeline {
    agent any

    environment {
        DOCKER_HUB_CREDENTIALS = 'docker-hub-credentials'
        DOCKERHUB_REPO = 'mikt90/ofos_backend'
        CONTAINER_NAME = 'ofos_backend'
    }

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

        stage('Build Docker Image') {
            steps {
                script {
                    docker.build("${DOCKERHUB_REPO}")
                }
            }
        }

        stage('Push to Docker Hub') {
            steps {
                script {
                    docker.withRegistry('https://registry.hub.docker.com', "${DOCKER_HUB_CREDENTIALS}") {
                        docker.image("${DOCKERHUB_REPO}").push()
                    }
                }
            }
        }

        stage('Run Latest Docker Image') {
            steps {
                script {
                    sh """
                    if [ \$(docker ps -q -f name=${CONTAINER_NAME}) ]; then
                        docker stop ${CONTAINER_NAME}
                        docker rm ${CONTAINER_NAME}
                    fi
                    """
                    sh "docker pull ${DOCKERHUB_REPO}:latest"
                    sh "docker run -d -p 8000:8000 --name ${CONTAINER_NAME} ${DOCKERHUB_REPO}:latest"
                }
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
    }

    post {
        always {
            echo 'Pipeline completeeeeed'
        }
    }
}