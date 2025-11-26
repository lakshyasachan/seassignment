pipeline {
    // Agent is set to ANY, assuming this will pick up your Windows machine
    agent any
    
    // Environment variables for Docker build/push
    environment {
        // --- !!! THE FIXED DOCKER.EXE PATH FOR WINDOWS !!! ---
        DOCKER_PATH = 'C:\\Program Files\\Docker\\Docker\\resources\\bin\\docker.exe' 
        // ------------------------------------------------------------
        
        DOCKER_REGISTRY = 'docker.io'
        DOCKER_HUB_USERNAME = 'anonymone/imt2023612' 
        IMAGE_TAG = "0" 
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
                // Using 'sh' for Java commands, as they often work cross-platform if Java is in the environment PATH
                sh 'javac CalculatorApp.java CalculatorTest.java' 
                echo 'Compilation successful. Proceeding to Testing stage.'
            }
        }

        stage('Test Code') {
            steps {
                echo 'Running functional tests.'
                sh 'java CalculatorTest'
                echo 'Tests ran successfully. Proceeding to Docker stage.'
            }
        }
        
        stage('Check Status and Run Docker') {
            steps {
                echo "DEBUG: Current build result before Docker stage check: ${currentBuild.result}"

                script {
                    def fullImageName = "${DOCKER_HUB_USERNAME}:${IMAGE_TAG}"

                    // 1. Build the Docker Image using the full Windows path and 'bat'
                    echo "Building Docker image: ${fullImageName}"
                    bat "\"${DOCKER_PATH}\" build -t ${fullImageName} ."
                    
                    // 2. Authenticate and Push using withCredentials block
                    echo "Authenticating and pushing to Docker Hub using credential ID: ${DOCKER_CREDENTIAL_ID}"
                    
                    // CRITICAL: Use 'withCredentials' to securely inject the username/password
                    withCredentials([usernamePassword(credentialsId: DOCKER_CREDENTIAL_ID, passwordVariable: 'DOCKER_PASSWORD', usernameVariable: 'DOCKER_USER')]) {
                        
                        // Explicitly log in using the full Windows path and bat runner
                        bat "echo %DOCKER_PASSWORD% | \"${DOCKER_PATH}\" login -u %DOCKER_USER% --password-stdin ${DOCKER_REGISTRY}"
                        
                        // Explicitly push the built image
                        bat "\"${DOCKER_PATH}\" push ${fullImageName}"
                        
                        // Logout 
                        bat "\"${DOCKER_PATH}\" logout ${DOCKER_REGISTRY}"
                    }
                    echo "Docker push command executed successfully for: ${fullImageName}"
                }
            }
            when {
                // Keep the relaxed condition to force execution
                expression { currentBuild.result != 'ABORTED' }
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
