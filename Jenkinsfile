pipeline {
    // Agent is set to ANY, confirming the default agent (the Linux environment, likely WSL or Docker) is used.
    agent any
    
    // Environment variables for Docker build/push
    environment {
        // --- CRITICAL FIX for Linux/WSL environments ---
        // We explicitly use the common Linux location for the docker executable.
        DOCKER_PATH = '/usr/bin/docker' 
        // ------------------------------------------------------------
        
        DOCKER_REGISTRY = 'docker.io'
        // Using the image name from the original log: anonymone/imt2023612
        DOCKER_HUB_USERNAME = 'anonymone/imt2023612' 
        IMAGE_TAG = "0" 
        // Using the credential ID established in the conversation
        DOCKER_CREDENTIAL_ID = 'dockerhub-creds' 
    }

    stages {
        stage('Pull Code') {
            steps {
                echo 'Pulling code from GitHub via SCM configuration.'
                // Assuming standard SCM configuration handles the checkout, 
                // but adding a simple echo for completeness as per original log.
            }
        }
        
        stage('Compile Code') {
            steps {
                echo 'Compiling the Simple Calculator application and test file (using javac).'
                // Using sh (Linux Shell) for compilation
                sh 'javac CalculatorApp.java CalculatorTest.java' 
                echo 'Compilation successful. Proceeding to Testing stage.'
            }
        }

        stage('Test Code') {
            steps {
                echo 'Running functional tests.'
                // Using sh (Linux Shell) for running tests
                sh 'java CalculatorTest'
                echo 'Tests ran successfully. Proceeding to Docker stage.'
            }
        }
        
        stage('Build and Push Docker Image') {
            steps {
                script {
                    def fullImageName = "${DOCKER_HUB_USERNAME}:${IMAGE_TAG}"

                    echo "Building Docker image: ${fullImageName}"
                    // 1. Build the Docker Image using the full Linux path and 'sh'
                    sh "\"${DOCKER_PATH}\" build -t ${fullImageName} ."
                    
                    echo "Authenticating and pushing to Docker Hub securely using credential ID: ${DOCKER_CREDENTIAL_ID}"
                    
                    // 2. Authenticate and Push using secure withCredentials block
                    withCredentials([usernamePassword(credentialsId: DOCKER_CREDENTIAL_ID, passwordVariable: 'DOCKER_PASSWORD', usernameVariable: 'DOCKER_USER')]) {
                        
                        // Explicitly log in using the full Linux path and sh runner
                        sh "echo \$DOCKER_PASSWORD | \"${DOCKER_PATH}\" login -u \$DOCKER_USER --password-stdin ${DOCKER_REGISTRY}"
                        
                        // Explicitly push the built image
                        sh "\"${DOCKER_PATH}\" push ${fullImageName}"
                        
                        // Logout 
                        sh "\"${DOCKER_PATH}\" logout ${DOCKER_REGISTRY}"
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
