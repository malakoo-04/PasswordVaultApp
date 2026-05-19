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
            post {
                always {
                    junit '**/build/test-results/testDebugUnitTest/*.xml'
                }
            }
        }

        stage('Docker Build') {
            steps {
                echo 'Building Docker image...'
                sh 'docker build -t passwordvault-artifact .'
            }
        }

        stage('Ansible Deployment') {
            steps {
                echo 'Deploying with Ansible...'
                sh 'ansible-playbook ansible/deploy.yml -i ansible/inventory.ini'
            }
        }

        stage('Verification') {
            steps {
                echo 'Verifying deployment...'
                sh 'kubectl get pods || true'
                sh 'kubectl get svc || true'
            }
        }
    }
}