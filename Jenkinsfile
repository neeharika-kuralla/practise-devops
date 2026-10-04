pipeline{
    agent any 
    tools{
        maven 'maven'
        jdk 'JDK21'
    }
    stages{
        stage('Checkout'){
            steps{
                checkout scm
            }
        }
        stage('Build and Test'){
            steps{
                bat 'mvn clean package'
            }
        }
        stage('Docker Build'){
            steps{
                bat 'docker build -t student-management .'
            }
        }
        stage('Deploy'){
            steps{
                bat 'docker rm -f student-app || exit /b 0'
                bat 'docker run-d -p 8081:8081 --name student-app student-management'
            }
        }
    }
}