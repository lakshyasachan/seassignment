pipeline {
    agent any

    environment {
        DOCKER_REGISTRY = 'docker.io'
        DOCKER_HUB_USERNAME = 'anonymone/imt2023612'
        IMAGE_TAG = "0"
        DOCKER_CREDENTIAL_ID = 'dockerhub-creds'

        // ❗ Add your actual Docker.exe path here
        // Example:
        // DOCKER_PATH = 'C:\\Program Files\\Docker\\Docker\\resources\\bin\\docker.exe'
        DOCKER_PATH = 'C:\\Program Files\\Docker\\Docker\\resources\\bin\\docker.exe'
    }

    stages {

        stage('Pull Code') {
            steps {
                echo 'Pulling code from GitHub via SCM configuration.'
            }
        }

        stage('Compile Code') {
            steps {
                echo 'Compiling the Simple Calculator application and test file (javac).'

                // Windows = use bat NOT sh
                bat 'javac CalculatorApp.java CalculatorTest.java'

                echo 'Compilation successful.'
            }
        }

        stage('Test Code') {
            steps {
                echo 'Running functional tests.'

                bat 'java CalculatorTest'

                echo 'Tests passed successfully.'
            }
        }

        stage('Check Status and Run Docker') {
            steps {
                echo "DEBUG: Current build status: ${currentBuild.result}"

                script {
                    def fullImage = "${DOCKER_HUB_USERNAME}:${IMAGE_TAG}"

                    echo "Building Docker image: ${fullImage}"

                    // Build image
                    bat "\"${DOCKER_PATH}\" build -t ${fullImage} ."

                    echo "Authenticating to Docker Hub"

                    withCredentials([usernamePassword(
                        credentialsId: DOCKER_CREDENTIAL_ID,
                        usernameVariable: 'DOCKER_USER',
                        passwordVariable: 'DOCKER_PASSWORD'
                    )]) {

                        bat """
                            echo %DOCKER_PASSWORD% | "${DOCKER_PATH}" login -u %DOCKER_USER% --password-stdin ${DOCKER_REGISTRY}
                        """

                        bat "\"${DOCKER_PATH}\" push ${fullImage}"

                        bat "\"${DOCKER_PATH}\" logout ${DOCKER_REGISTRY}"
                    }

                    echo "Docker push completed for ${fullImage}"
                }
            }
            when {
                expression { currentBuild.result != 'ABORTED' }
            }
        }
    }

    post {
        always {
            echo 'Cleaning workspace...'
            cleanWs()
        }
        success {
            echo "Pipeline SUCCESS! Image pushed: ${DOCKER_HUB_USERNAME}:${IMAGE_TAG}"
        }
        failure {
            echo "Pipeline FAILED — check console logs."
        }
    }
}
