pipeline {
    agent any
    
    // Environment variables for Docker build/push
    environment {
        DOCKER_REGISTRY = 'docker.io'
        // ===============================================
        // Ensure this matches your Docker Hub username/repository path
        DOCKER_HUB_USERNAME = 'anonymone/imt2023612' 
        // ===============================================
        
        IMAGE_NAME = "calculator-cli-app" 
        IMAGE_TAG = "0" 
        
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
                echo 'Compiling the Simple Calculator application and test file (using javac).'
                sh 'javac CalculatorApp.java CalculatorTest.java' 
                echo 'Compilation successful. Proceeding to Testing stage.'
            }
        }

        stage('Test Code') {
            steps {
                echo 'Running functional tests. The pipeline will stop if tests fail (non-zero exit code).'
                sh 'java CalculatorTest'
                echo 'Tests ran successfully. Proceeding to Docker stage.'
            }
        }
        
        stage('Create & Push Docker Image') {
            when {
                expression { currentBuild.result == 'SUCCESS' }
            }
            steps {
                script {
                    def fullImageName = "${DOCKER_HUB_USERNAME}:${IMAGE_TAG}"
                    
                    // 1. Build the Docker Image
                    echo "Building Docker image: ${fullImageName}"
                    def builtImage = docker.build(fullImageName, '.')
                    
                    // 2. Push the Docker Image using the credential ID defined in the environment
                    echo "Attempting Docker push to ${DOCKER_REGISTRY} using credential ID: ${DOCKER_CREDENTIAL_ID}"
                    
                    // CRITICAL FIX: Ensure the DOCKER_CREDENTIAL_ID variable is correctly interpolated and used.
                    docker.withRegistry("https://${DOCKER_REGISTRY}", DOCKER_CREDENTIAL_ID) {
                        builtImage.push() 
                    }
                    echo "Docker push command executed successfully for: ${fullImageName}"
                }
            }
        }
    }
    
    post {
        always {
            echo 'Cleaning up workspace...'
            cleanWs()
        }
        success {
            echo "Pipeline succeeded! Image ${DOCKER_HUB_USERNAME}:${IMAGE_TAG} pushed to Docker Hub."
        }
        failure {
            echo "Pipeline FAILED. Please check the Console Output for errors."
        }
    }
}
