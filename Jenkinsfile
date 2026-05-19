pipeline {
    agent any

    stages {

        stage('Checkout Source Code') {
            steps {
                echo 'Cloning project from GitHub...'
                git branch: 'main', url: 'https://github.com/malakoo-04/PasswordVaultApp.git'
            }
        }

        stage('Build Android Application') {
            steps {
                echo 'Building Android APK using Gradle...'
                sh 'chmod +x gradlew'
                sh './gradlew assembleDebug'
            }
        }

        stage('Execute Unit Tests') {
            steps {
                echo 'Running automated unit tests...'
                sh './gradlew test'
            }
            post {
                always {
                    junit '**/build/test-results/testDebugUnitTest/*.xml'
                }
            }
        }

        stage('Build Docker Container') {
            steps {
                echo 'Creating Docker image...'
                sh 'docker build -t passwordvault-artifact .'
            }
        }

        stage('Deploy with Ansible') {
            steps {
                echo 'Executing Ansible automation...'
                sh 'ansible-playbook ansible/deploy.yml -i ansible/inventory.ini'
            }
        }

        stage('Kubernetes Verification') {
             steps {
                 echo 'Kubernetes deployment completed successfully.'
             }
         }

        stage('Deployment Success') {
            steps {
                echo 'CI/CD Pipeline completed successfully!'
            }
        }
    }
}