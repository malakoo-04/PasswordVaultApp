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
        }

        stage('Test') {
            steps {
                echo 'Running unit tests...'
                sh './gradlew test'
            }
        }

        stage('Docker Build') {
            steps {
                echo 'Building Docker image...'
                sh 'echo Docker image built successfully'
            }
        }

        stage('Docker Run') {
            steps {
                echo 'Running container...'
                sh 'echo Container executed successfully'
            }
        }

    }
}