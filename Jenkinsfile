pipeline {
  agent any

  environment {
    IMAGE_NAME = "kafka-producer"
    IMAGE_TAG = "v1.${BUILD_NUMBER}"
    DOCKER_REGISTRY = "krishnasravi"
    MANIFEST_REPO = "https://github.com/krishnasravi/sample-demo-kafka/manifests"
    APP_DIR = "app" // update if needed
  }

  stages {
    stage('Checkout') {
      steps {
        git url: 'https://github.com/krishnasravi/sample-demo-kafka.git', branch: 'main'
      }
    }

    stage('Maven Build') {
      steps {
        dir(APP_DIR) {
          sh 'mvn clean package -DskipTests'
        }
      }
    }

    stage('Docker Build') {
      steps {
        dir(APP_DIR) {
          sh 'docker build -t $DOCKER_REGISTRY/$IMAGE_NAME:$IMAGE_TAG .'
        }
      }
    }    

    stage('Docker Push') {
      steps {
        withDockerRegistry([credentialsId: 'docker-cred-id', url: '']) {
          sh "docker push $DOCKER_REGISTRY/$IMAGE_NAME:$IMAGE_TAG"
        }
      }
    }

    stage('Update K8s Manifests') {
      steps {
        sh """
          git clone $MANIFEST_REPO
          cd k8s-manifests
          sed -i 's|image: .*|image: $DOCKER_REGISTRY/$IMAGE_NAME:$IMAGE_TAG|' manifests/kafka-app.yaml
          git config user.name "jenkins"
          git config user.email "jenkins@example.com"
          git commit -am "Update image to $IMAGE_TAG"
          git push
        """
      }
    }
  }
}
