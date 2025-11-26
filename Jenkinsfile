pipeline {
    agent any
    
    // Environment variables for Docker build/push
    environment {
        DOCKER_REGISTRY = 'docker.io'
        // ===============================================
        DOCKER_HUB_USERNAME = 'anonymone/imt2023612' 
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
                echo 'Compiling the Simple Calculator application and test file (using javac).'
                // CRITICAL FIX: Must compile both the application and the test file
                sh 'javac CalculatorApp.java CalculatorTest.java' 
                echo 'Compilation successful. Proceeding to Testing stage.'
            }
        }

        stage('Test Code') {
            steps {
                echo 'Running functional tests. The pipeline will stop if tests fail (non-zero exit code).'
                // CRITICAL FIX: Running the test class. If tests fail (exit code 1), the build fails here.
                sh 'java CalculatorTest'
                echo 'Tests ran successfully. Proceeding to Docker stage.'
            }
        }
        
        stage('Create & Push Docker Image') {
            // This stage runs only if the Test Code stage was successful (i.e., currentBuild.result is SUCCESS)
            when {
                expression { currentBuild.result == 'SUCCESS' }
            }
            steps {
                script {
                    // fullImageName combines the repo path and the tag
                    def fullImageName = "${DOCKER_HUB_USERNAME}:${IMAGE_TAG}"
                    
                    // 1. Create the Docker Image using the Dockerfile
                    echo "Building Docker image: ${fullImageName}"
                    // The '.' at the end specifies that the Dockerfile is in the current directory
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
            echo "Pipeline succeeded! Image ${DOCKER_HUB_USERNAME}:${IMAGE_TAG} pushed to Docker Hub."
        }
        failure {
            echo "Pipeline FAILED. Please check the Console Output for errors."
        }
    }
}
