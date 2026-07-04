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
        docker run --rm \
          --name kubernetes-performance-back \
          -p 127.0.0.1:8081:8080 \
          kubernetes-performance-back:latest
        '''
      }
    }
  }
}