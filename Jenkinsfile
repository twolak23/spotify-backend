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
  }
}