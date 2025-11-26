pipeline {
    agent any
    
    // Environment variables for Docker build/push
    environment {
        DOCKER_REGISTRY = 'docker.io'
        // ===============================================
        // Ensure this is your actual Docker Hub ID
        DOCKER_HUB_USERNAME = 'anonymone' 
        // ===============================================
        
        IMAGE_NAME = "calculator-cli-app" 
        IMAGE_TAG = "0" // Your Roll Number
        
        // ID of the Docker Hub Credentials set in Jenkins
        DOCKER_CREDENTIAL_ID = 'dockerhub-creds'
    }

    stages {
        stage('Pull Code') {
            steps {
                echo 'Pulling code from GitHub via SCM configuration.'
            }
        }
        
        stage('Compile Code') {
            steps {
                echo 'Compiling the Simple Calculator application (using javac).'
                // The single source file is compiled directly.
                // The compiled .class file is created in the workspace root.
                sh 'javac CalculatorApp.java' 
                echo 'Compilation successful. Proceeding to Docker stage.'
            }
        }
        
        stage('Create & Push Docker Image') {
            // This stage runs only if the Compile Code stage was successful
            when {
                expression { currentBuild.result == 'SUCCESS' }
            }
            steps {
                script {
                    def fullImageName = "${DOCKER_HUB_USERNAME}/${IMAGE_NAME}:${IMAGE_TAG}"
                    
                    // 1. Create the Docker Image using the Dockerfile
                    echo "Building Docker image: ${fullImageName}"
                    docker.build(fullImageName, '.')
                    
                    // 2. Push the Docker Image to Docker Hub
                    echo "Pushing Docker image to Docker Hub..."
                    withCredentials([usernamePassword(credentialsId: DOCKER_CREDENTIAL_ID, passwordVariable: 'DOCKER_PASSWORD', usernameVariable: 'DOCKER_USERNAME')]) {
                        docker.withRegistry("https://${DOCKER_REGISTRY}", DOCKER_CREDENTIAL_ID) {
                            sh "docker push ${fullImageName}"
                        }
                    }
                    echo "Docker image pushed successfully: ${fullImageName}"
                }
            }
        }
    }
    
    post {
        always {
            // Cleanup the workspace to free up disk space after the job completes
            echo 'Cleaning up workspace...'
            cleanWs()
        }
        success {
            echo "Pipeline succeeded! Image ${IMAGE_NAME}:${IMAGE_TAG} pushed to Docker Hub."
        }
        failure {
            echo "Pipeline FAILED. Please check the Console Output for errors."
        }
    }
}
