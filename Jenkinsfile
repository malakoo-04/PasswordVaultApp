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
                sh 'docker build -t passwordvault-artifact .'
            }
        }

        stage('Kubernetes Deploy') {
            steps {
                sh 'minikube image load passwordvault-artifact'
                sh 'kubectl apply -f k8s/'
            }
        }

       stage('Verification') {
           steps {
               sh 'kubectl get pods'
               sh 'kubectl get svc'
               sh 'kubectl rollout status deployment/passwordvault-deployment'
           }
       }

    }
}