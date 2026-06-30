pipeline {
  agent any
  stages {
    stage ('Build') {
      steps {
        sh 'make'
        sh '''
          java --version
          mvn --version
        '''
      }
    }
    stage ('Install and Test') {
      steps {
        sh 'mvn -B clean verify'
      }
    }
  }
}