def call() {
  node('workstation') {

    stage('Code Checkout') {
      if(env.TAG_NAME ==~ ".*") {
        env.gitbrname = "refs/tags/${env.TAG_NAME}"
      } else {
        env.gitbrname = "${env.BRANCH_NAME}"
      }
      checkout scm: [$class: 'GitSCM', userRemoteConfigs: [[url: 'https://github.com/raghudevopsb73/frontend']], branches: [[name: gitbrname]]], poll: false


//        sh 'env'
//        sh 'find . | grep "^./" |xargs rm -rf'
//        if(env.TAG_NAME ==~ ".*") {
//          git(
//              url: 'https://github.com/raghudevopsb73/frontend',
//              branch: 'v1',
//              changelog: false,
//              poll: false
//          )
//        } else {
//          //git branch: env.BRANCH_NAME , url: 'https://github.com/raghudevopsb73/frontend'
//          echo 'hello'
//        }

    }

    if (env.cibuild == "java") {

      stage('Build') {
        sh 'mvn package'
      }
    }

    stage('Unit tests') {
      echo 'unit tests'
      sh 'ls -ltr'
    }

    stage('Code Analysis') {
      echo 'sonar'
      //sh 'sonar-scanner -Dsonar.host.url=http://172.31.83.175:9000 -Dsonar.login=admin -Dsonar.password=admin123 -Dsonar.projectKey=frontend -Dsonar.qualitygate.wait=true'
    }

    stage('Security Scans') {
      echo 'Security Scans'
    }

    if(env.TAG_NAME ==~ ".*") {
      stage('Publish a Artifact') {
        if (env.cibuild == "nginx") {
          sh 'zip -r ${component}-${TAG_NAME}.zip *'
        }
      }
    }

  }
}