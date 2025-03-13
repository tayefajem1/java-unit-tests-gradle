pipeline {
    agent any
    
    environment {
        IMAGE_NAME = 'my-java-app'
        IMAGE_TAG = 'latest'
        DOCKER_REGISTRY = 'my-docker-registry'  // Change as needed
    }
    
    stages {
        stage('Checkout Code') {
            steps {
                git 'https://github.com/HandsOnDevOpsTraining/java-unit-tests-gradle.git' // Change to your repo
            }
        }
        
        stage('Build') {
            steps {
                sh './gradlew clean build'
            }
        }
        
        stage('Test') {
            steps {
                sh './gradlew test'
            }
        }
        
        stage('Build Docker Image') {
            steps {
                script {
                    sh "docker build -t $IMAGE_NAME:$IMAGE_TAG ."
                }
            }
        }
        
        stage('Push Docker Image') {
            steps {
                script {
                    sh "docker tag $IMAGE_NAME:$IMAGE_TAG $DOCKER_REGISTRY/$IMAGE_NAME:$IMAGE_TAG"
                    sh "docker push $DOCKER_REGISTRY/$IMAGE_NAME:$IMAGE_TAG"
                }
            }
        }
    }
}
