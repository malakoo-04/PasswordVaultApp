pipeline {
    agent any

    stages {

        stage('Checkout') {
            steps {
                echo 'Getting code from GitHub...'
                git branch: 'main', url: 'https://github.com/malakoo-04/PasswordVaultApp.git'
            }
        }

        stage('Build') {
            steps {
                echo 'Building Android project...'
                sh 'chmod +x gradlew'
                sh './gradlew assembleDebug'
            }
            post {
                success {
                    archiveArtifacts artifacts: 'app/build/outputs/apk/debug/*.apk'
                }
            }
        }

        stage('Test') {
            steps {
                echo 'Running unit tests...'
                sh './gradlew test'
            }
            post {
                always {
                    junit '**/build/test-results/testDebugUnitTest/*.xml'
                }
            }
        }

        stage('Docker Build') {
            steps {
                sh 'docker build -t passwordvault-artifact .'
            }
        }

        stage('Docker Run') {
            steps {
                sh 'docker run --rm passwordvault-artifact'
            }
        }
    }
}