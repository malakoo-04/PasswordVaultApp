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