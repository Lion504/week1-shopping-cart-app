pipeline {
    agent any

    tools {
        maven 'Maven3'
    }
    
    environment {
        DOCKER_IMAGE_NAME = 'timo2233/shopping-cart-app'
        DOCKER_HUB_REPO = 'docker.io/timo2233/shopping-cart-app'
        SONARQUBE_SERVER = 'SonarQubeServer'
        SONAR_TOKEN = credentials('sonar-t')
    }
    
    stages {
        stage('Checkout') {
            steps {
                deleteDir()
                checkout scm
            }
        }

        stage('Prepare Test Config') {
            steps {
                withCredentials([file(credentialsId: 'test-config-properties', variable: 'TEST_CFG')]) {
                    sh '''
                        mkdir -p src/test/resources
                        chmod u+w src/test/resources || true
                        cp "$TEST_CFG" src/test/resources/config.properties
                    '''
                }
            }
        }
        
        stage('Build') {
            steps {
                sh 'mvn -DskipTests clean package'
            }
        }
        
        stage('Test') {
            steps {
                sh 'mvn test'
            }
        }
        
        stage('JaCoCo Coverage') {
            steps {
                sh 'mvn jacoco:report'
                publishHTML([
                    allowMissing: false,
                    alwaysLinkToLastBuild: true,
                    keepAll: true,
                    reportDir: 'target/site/jacoco',
                    reportFiles: 'index.html',
                    reportName: 'JaCoCo Coverage Report'
                ])
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv("${SONARQUBE_SERVER}") {
                    sh '''
                        mvn -B sonar:sonar \
                          -Dsonar.token=$SONAR_TOKEN \
                          -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml
                    '''
                }
            }
        }

        stage('Quality Gate') {
            steps {
                timeout(time: 10, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }
        
        stage('Build Docker Image') {
            steps {
                sh "docker build -t ${DOCKER_IMAGE_NAME}:${env.BUILD_NUMBER} ."
                sh "docker build -t ${DOCKER_IMAGE_NAME}:latest ."
            }
        }
        
        stage('Push to Docker Hub') {
            when {
                branch 'main'
            }
            steps {
                withCredentials([usernamePassword(credentialsId: 'docker-hub-credentials', usernameVariable: 'DOCKER_HUB_USER', passwordVariable: 'DOCKER_HUB_PASS')]) {
                    sh '''
                        echo "$DOCKER_HUB_PASS" | docker login -u "$DOCKER_HUB_USER" --password-stdin
                        docker push ${DOCKER_IMAGE_NAME}:${env.BUILD_NUMBER}
                        docker push ${DOCKER_IMAGE_NAME}:latest
                        docker logout
                    '''
                }
            }
        }
    }
    
    post {
        always {
            junit 'target/surefire-reports/*.xml'
            archiveArtifacts artifacts: 'target/**/*.jar', fingerprint: true
        }
    }
}
