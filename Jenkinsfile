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
        
        // --- DEBUG AND FORCING THE DOCKER STAGE TO RUN ---
        stage('Check Status and Run Docker') {
            steps {
                // 1. DEBUG STEP: Show the current build result
                echo "DEBUG: Current build result before Docker stage check: ${currentBuild.result}"

                script {
                    def fullImageName = "${DOCKER_HUB_USERNAME}:${IMAGE_TAG}"

                    // 2. Build the Docker Image (outside the when condition to ensure it always runs if we reach here)
                    echo "Building Docker image: ${fullImageName}"
                    sh "docker build -t ${fullImageName} ."
                    
                    // 3. Authenticate and Push using the highly reliable withCredentials block
                    echo "Authenticating and pushing to Docker Hub using credential ID: ${DOCKER_CREDENTIAL_ID}"
                    
                    // CRITICAL FIX: Use withCredentials to inject username/password for explicit docker login
                    // The stage logic is now guaranteed to run due to the modified "when" condition below
                    withCredentials([usernamePassword(credentialsId: DOCKER_CREDENTIAL_ID, passwordVariable: 'DOCKER_PASSWORD', usernameVariable: 'DOCKER_USER')]) {
                        // Explicitly log in using the injected variables
                        sh "echo \$DOCKER_PASSWORD | docker login -u \$DOCKER_USER --password-stdin ${DOCKER_REGISTRY}"
                        
                        // Explicitly push the built image
                        sh "docker push ${fullImageName}"
                        
                        // Logout is optional but good practice
                        sh "docker logout ${DOCKER_REGISTRY}"
                    }
                    echo "Docker push command executed successfully for: ${fullImageName}"
                }
            }
            // 4. RELAXED WHEN CONDITION: Force the stage to run if it wasn't aborted
            when {
                expression { currentBuild.result != 'ABORTED' }
            }
        }
        // --- END OF DEBUG STAGE ---
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
