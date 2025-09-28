pipeline {
    agent any

    environment {
        MOTHER_REPO_URL = "https://github.com/urutare/stockm-backend.git"
        REMOTE_BASE_DIR = "/root/stock"
        DEV_DIR = "/root/stock/dev"
        PROD_DIR = "/root/stock/production"
        JAVA_VERSION = "21"
        MAVEN_OPTS = "-Xmx2048m -XX:MaxMetaspaceSize=512m"
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: "${env.BRANCH_NAME}", url: "${env.MOTHER_REPO_URL}"
            }
        }

        stage('Set Branch & Environment') {
            steps {
                script {
                    if (env.BRANCH_NAME == "main") {
                        env.environment = "production"
                        env.target_dir = "${env.PROD_DIR}"
                        env.profile = "prod"
                    } else if (env.BRANCH_NAME == "develop") {
                        env.environment = "development"
                        env.target_dir = "${env.DEV_DIR}"
                        env.profile = "dev"
                    } else {
                        error "Unsupported branch: ${env.BRANCH_NAME}"
                    }
                }
            }
        }

        stage('Create Remote Directory Structure') {
            steps {
                sshagent(['deploy-server-ssh']) {
                    sh """
                        ssh -o StrictHostKeyChecking=no ${DEPLOY_SERVER_USER}@${DEPLOY_SERVER} "
                        mkdir -p ${env.target_dir} &&
                        chmod 755 ${env.target_dir} &&
                        ls -la ${env.REMOTE_BASE_DIR}
                        "
                    """
                }
            }
        }

        stage('Clone Mother Repository on Remote Server') {
            steps {
                sshagent(['deploy-server-ssh']) {
                    sh """
                        ssh ${DEPLOY_SERVER_USER}@${DEPLOY_SERVER} "
                        cd ${env.target_dir} &&
                        rm -rf mother-repo &&
                        git clone ${env.MOTHER_REPO_URL} mother-repo &&
                        cd mother-repo &&
                        git checkout ${env.BRANCH_NAME}
                        "
                    """
                }
            }
        }

        stage('Setup Services and Modules') {
            steps {
                sshagent(['deploy-server-ssh']) {
                    sh """
                        ssh ${DEPLOY_SERVER_USER}@${DEPLOY_SERVER} "
                        cd ${env.target_dir}/mother-repo &&
                        chmod +x setup.sh &&
                        ./setup.sh
                        "
                    """
                }
            }
        }

        stage('Install Java and Maven on Remote Server') {
            steps {
                sshagent(['deploy-server-ssh']) {
                    sh """
                        ssh ${DEPLOY_SERVER_USER}@${DEPLOY_SERVER} "
                        sudo apt-get update &&
                        sudo apt-get install -y openjdk-21-jdk maven &&
                        java -version &&
                        mvn -version
                        "
                    """
                }
            }
        }

        stage('Build All Services and Modules') {
            steps {
                sshagent(['deploy-server-ssh']) {
                    sh """
                        ssh ${DEPLOY_SERVER_USER}@${DEPLOY_SERVER} "
                        cd ${env.target_dir}/mother-repo &&
                        export JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64 &&
                        export MAVEN_OPTS='${MAVEN_OPTS}' &&
                        mvn clean install -DskipTests -P${env.profile} -e
                        "
                    """
                }
            }
        }

        stage('Verify JAR Files') {
            steps {
                sshagent(['deploy-server-ssh']) {
                    sh """
                        ssh ${DEPLOY_SERVER_USER}@${DEPLOY_SERVER} "
                        cd ${env.target_dir}/mother-repo &&
                        find . -name '*.jar' -type f | grep target/ || exit 1
                        "
                    """
                }
            }
        }

        stage('Stop Existing Containers') {
            steps {
                sshagent(['deploy-server-ssh']) {
                    sh """
                        ssh ${DEPLOY_SERVER_USER}@${DEPLOY_SERVER} "
                        cd ${env.target_dir}/mother-repo &&
                        docker compose down --remove-orphans || true &&
                        docker system prune -f || true
                        "
                    """
                }
            }
        }

        stage('Build and Start Docker Services') {
            steps {
                sshagent(['deploy-server-ssh']) {
                    sh """
                        ssh ${DEPLOY_SERVER_USER}@${DEPLOY_SERVER} "
                        cd ${env.target_dir}/mother-repo &&
                        export SPRING_PROFILES_ACTIVE=${env.profile} &&
                        export ENVIRONMENT=${env.environment} &&
                        docker compose build --no-cache &&
                        docker compose up -d &&
                        sleep 30 &&
                        docker compose ps
                        "
                    """
                }
            }
        }

        stage('Health Check') {
            steps {
                sshagent(['deploy-server-ssh']) {
                    sh """
                        ssh ${DEPLOY_SERVER_USER}@${DEPLOY_SERVER} "
                        cd ${env.target_dir}/mother-repo &&
                        sleep 60 &&
                        docker compose ps &&
                        docker compose logs --tail=20 | grep -i error || echo 'No errors found'
                        "
                    """
                }
            }
        }

        stage('Cleanup Build Artifacts') {
            steps {
                sshagent(['deploy-server-ssh']) {
                    sh """
                        ssh ${DEPLOY_SERVER_USER}@${DEPLOY_SERVER} "
                        rm -rf ~/.m2/repository/com/yourcompany/* || true &&
                        docker builder prune -f || true
                        "
                    """
                }
            }
        }
    }

    post {
        always {
            echo "==================================="
            echo "Deployment Summary"
            echo "Branch: ${env.BRANCH_NAME}"
            echo "Environment: ${env.environment}"
            echo "Target Directory: ${env.target_dir}"
            echo "Profile: ${env.profile}"
            echo "Timestamp: ${new Date()}"
            echo "==================================="
        }
    }
}
