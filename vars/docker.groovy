def call() {
  node('workstation') {

    stage('Code Checkout') {

      sh 'find . | grep "^./" |xargs rm -rf'

      if(env.TAG_NAME ==~ ".*") {
        env.gitbrname = "refs/tags/${env.TAG_NAME}"
      } else {
        env.gitbrname = "${env.BRANCH_NAME}"
      }
      checkout scm: [$class: 'GitSCM', userRemoteConfigs: [[url: "https://github.com/raghudevopsb73/${env.component}"]], branches: [[name: gitbrname]]], poll: false

    }


    stage('Docker Build') {
      sh 'docker build -t ${component} .'
    }

     stage('Publish a Artifact') {
        sh 'docker tag ${component} 739561048503.dkr.ecr.us-east-1.amazonaws.com/${component}:latest'
        sh 'aws ecr get-login-password --region us-east-1 | docker login --username AWS --password-stdin 739561048503.dkr.ecr.us-east-1.amazonaws.com'
        sh 'docker push 739561048503.dkr.ecr.us-east-1.amazonaws.com/${component}:latest'
      }
    }

}
