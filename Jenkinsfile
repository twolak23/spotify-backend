pipeline {
  agent any
  tools {
     jdk 'JDK21'
  }
  stages {
    stage ('Environment') {
        steps {
          sh '''
            java -version
            javac -version
            mvn -v
          '''
        }
    }
    stage ('Build and Test') {
      steps {
        sh 'mvn -B clean verify'
      }
    }
    stage ('Build image tag') {
      steps {
        script {
          def safeBranch = env.BRANCH_NAME
             .replaceAll('[^A-Za-z0-9_.-]', '-')
             .toLowerCase()
          env.IMAGE_TAG = "${safeBranch}-${env.BUILD_NUMBER}"
        }
      }
    }
    stage('Build Image') {
        steps {
            sh 'docker build -t spotify-backend:$IMAGE_TAG .'
        }
    }
    stage ('Deploy development') {
      when {
          branch 'main'
      }
      steps {
        sh '''
        docker rm -f spotify-backend || true
        '''
      }
    }
  }
}