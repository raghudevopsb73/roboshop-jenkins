def call() {
  pipeline {

    agent {
      node { label 'workstation'}
    }

    stages {

      stage('Build') {
        steps {
          sh 'npm install'
        }
      }

      stage('Unit tests') {
        steps {
          echo 'unit tests'
          // sh 'npm test'
        }
      }

      stage('Code Analysis') {
        steps {
          echo 'sonar'
          //sh 'sonar-scanner -Dsonar.host.url=http://172.31.83.175:9000 -Dsonar.login=admin -Dsonar.password=admin123 -Dsonar.projectKey=cart'
        }
      }

      stage('Security Scans') {
        steps {
          echo 'Security Scans'
        }
      }

      stage('Publish a Artifact') {
        when {
          expression {
            env.TAG_NAME ==~ ".*"
          }
        }
        steps {
          echo 'Publish a Artifact'
          sh 'env'
        }
      }


    }

  }


}