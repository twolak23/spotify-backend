
pipeline {
    agent any

    tools {
        jdk 'JDK21'
    }

    environment {
        AWS_REGION = 'eu-north-1'
        AWS_ACCOUNT_ID = '960980275868'
        ECR_REPOSITORY = 'spotify-app/backend'

        ECS_CLUSTER = 'spotify-backend-cluster'
        ECS_SERVICE = 'spotify-backend-service-aarceogn'
        CONTAINER_NAME = 'Main'
    }

    stages {
        stage('Environment') {
            steps {
                sh '''
                    java -version
                    mvn -version
                    docker version
                    aws --version
                    jq --version
                '''
            }
        }

        stage('Build and Test') {
            steps {
                sh 'mvn -B clean verify'
            }
        }

        stage('Generate Image Tag') {
            steps {
                script {
                    def safeBranch = env.BRANCH_NAME
                        .replaceAll('[^A-Za-z0-9_.-]', '-')
                        .toLowerCase()

                    // Commit SHA avoids collisions if Jenkins build
                    // numbers are reset after recreating a job.
                    def shortSha = env.GIT_COMMIT.take(12)

                    env.IMAGE_TAG = "${safeBranch}-${env.BUILD_NUMBER}-${shortSha}"
                    env.ECR_REGISTRY = "${env.AWS_ACCOUNT_ID}.dkr.ecr.${env.AWS_REGION}.amazonaws.com"
                    env.IMAGE_URI = "${env.ECR_REGISTRY}/${env.ECR_REPOSITORY}:${env.IMAGE_TAG}"
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                sh '''
                    docker build \
                      -t "$IMAGE_URI" \
                      .
                '''
            }
        }

        stage('Push to ECR') {
//            when {
//                branch 'main'
//            }
            steps {
                sh '''
                    set -eu

                    aws ecr get-login-password \
                      --region "$AWS_REGION" |
                    docker login \
                      --username AWS \
                      --password-stdin "$ECR_REGISTRY"

                    docker push "$IMAGE_URI"
                '''
            }
        }

        stage('Register ECS Task Definition') {
//            when {
//                branch 'main'
//            }
            steps {
                sh '''
                    set -eu

                    TASK_DEFINITION_ARN=$(aws ecs describe-services \
                      --cluster "$ECS_CLUSTER" \
                      --services "$ECS_SERVICE" \
                      --region "$AWS_REGION" \
                      --query 'services[0].taskDefinition' \
                      --output text)

                    test "$TASK_DEFINITION_ARN" != "None"

                    aws ecs describe-task-definition \
                      --task-definition "$TASK_DEFINITION_ARN" \
                      --region "$AWS_REGION" \
                      --query 'taskDefinition' \
                      --output json > current-task.json

                    jq \
                      --arg IMAGE "$IMAGE_URI" \
                      --arg NAME "$CONTAINER_NAME" \
                      '
                      .containerDefinitions |= map(
                        if .name == $NAME
                        then .image = $IMAGE
                        else .
                        end
                      )
                      | del(
                          .taskDefinitionArn,
                          .revision,
                          .status,
                          .requiresAttributes,
                          .compatibilities,
                          .registeredAt,
                          .registeredBy,
                          .deregisteredAt
                        )
                      ' current-task.json > new-task.json

                    jq -e \
                      --arg NAME "$CONTAINER_NAME" \
                      --arg IMAGE "$IMAGE_URI" \
                      '.containerDefinitions
                       | any(.name == $NAME and .image == $IMAGE)' \
                      new-task.json > /dev/null

                    aws ecs register-task-definition \
                      --cli-input-json file://new-task.json \
                      --region "$AWS_REGION" \
                      --query 'taskDefinition.taskDefinitionArn' \
                      --output text > task-definition-arn.txt
                '''
            }
        }

        stage('Deploy to ECS') {
//            when {
//                branch 'main'
//            }
            steps {
                sh '''
                    set -eu

                    TASK_DEFINITION_ARN=$(cat task-definition-arn.txt)

                    aws ecs update-service \
                      --cluster "$ECS_CLUSTER" \
                      --service "$ECS_SERVICE" \
                      --task-definition "$TASK_DEFINITION_ARN" \
                      --region "$AWS_REGION"

                    aws ecs wait services-stable \
                      --cluster "$ECS_CLUSTER" \
                      --services "$ECS_SERVICE" \
                      --region "$AWS_REGION"
                '''
            }
        }
    }

    post {
        always {
            sh '''
                rm -f current-task.json new-task.json task-definition-arn.txt
            '''
        }
    }
}
