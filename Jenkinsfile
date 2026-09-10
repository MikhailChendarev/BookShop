pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/MikhailChendarev/BookShop.git'
            }
        }
        stage('Build') {
            steps {
                sh './mvnv clean package -DskipTests'
            }
        }
        stage('Test') {
            steps {
                sh './mvnv test'
            }
        }
        stage('Archive') {
            steps {
                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
            }
        }
    }
}