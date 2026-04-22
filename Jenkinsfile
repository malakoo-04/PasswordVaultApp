pipeline {
    agent any

    stages {

        stage('Checkout') {
            steps {
                echo 'Getting code from GitHub...'
                git 'https://github.com/malakoo-04/PasswordVaultApp.git'
            }
        }

        stage('Build') {
            steps {
                echo 'Simulating build...'
                sh 'echo Build successful'
            }
        }

        stage('Test') {
            steps {
                echo 'Simulating tests...'
                sh 'echo Tests passed'
            }
        }

        stage('Docker Build') {
            steps {
                echo 'Building Docker image...'
                sh 'docker build -t password-manager-image .'
            }
        }

        stage('Docker Run') {
            steps {
                echo 'Running Docker container...'
                sh 'docker run password-manager-image'
            }
        }
    }

    post {
        success {
            echo 'Pipeline completed successfully!'
        }
    }
}