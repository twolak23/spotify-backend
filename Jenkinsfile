pipeline {
  agent any
  tools {
     jdk 'jdk21'
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
    stage ('Build Image') {
      steps {
        sh '''
          docker build -t kubernetes-performance-back:$IMAGE_TAG .
        '''
      }
    }
    stage ('Deploy development') {
      when {
          branch 'develop'
      }
      steps {
        sh '''
        docker rm -f kubernetes-performance-back || true
        docker run -d \
          --name kubernetes-performance-back \
          --restart unless-stopped \
          --label git.branch="$BRANCH_NAME" \
          --label git.commit="$GIT_COMMIT" \
          -p 8081:8080 \
          kubernetes-performance-back:$IMAGE_TAG
        '''
      }
    }
  }
}