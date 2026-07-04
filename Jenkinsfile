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
    stage ('Package') {
      steps {
        sh '''
        docker rm -f kubernetes-performance-back || true
        docker run -d \
          --name kubernetes-performance-back \
          --restart unless-stopped \
          -p 8081:8080 \
          kubernetes-performance-back:latest
        '''
      }
    }
  }
}