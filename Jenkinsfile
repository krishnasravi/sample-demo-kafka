pipeline {
  agent any

  environment {
    IMAGE_NAME = "kafka-producer"
    IMAGE_TAG = "v1.${BUILD_NUMBER}"
    DOCKER_REGISTRY = "krishnasravi"
    MANIFEST_REPO = "https://github.com/krishnasravi/sample-demo-kafka.git"
    APP_DIR = "app" // update if needed
  }

  stages {
    stage('Checkout') {
      steps {
        git credentialsId: 'github-pat', url: "${MANIFEST_REPO}", branch: 'main'
      }
    }

    stage('Maven Build') {
      steps {
        dir("${APP_DIR}") {
          bat 'mvn clean package -DskipTests'
        }
      }
    }

    stage('Docker Build') {
      steps {
        dir("${APP_DIR}") {
          bat 'docker build -t %DOCKER_REGISTRY%/%IMAGE_NAME%:%IMAGE_TAG% .'
        }
      }
    }

    stage('Docker Push') {
      steps {
        withDockerRegistry([credentialsId: 'docker-cred-id', url: '']) {
          bat 'docker push %DOCKER_REGISTRY%/%IMAGE_NAME%:%IMAGE_TAG%'
        }
      }
    }

    stage('Update K8s Manifests') {
      steps {
        bat '''
          rmdir /S /Q manifests
          git clone %MANIFEST_REPO%
          cd sample-demo-kafka\\manifests
          powershell -Command "(Get-Content kafka-app.yaml) -replace 'image: .*', 'image: %DOCKER_REGISTRY%/%IMAGE_NAME%:%IMAGE_TAG%' | Set-Content kafka-app.yaml"
          git config user.name "jenkins"
          git config user.email "jenkins@example.com"
          git add kafka-app.yaml
          git commit -m "Update image to %IMAGE_TAG%"
          git push
        '''
      }
    }
  }
}
