pipeline {
    agent any

    tools {
        // Install the Maven version configured as "M3" and add it to the path.
        maven "mvn"
    }

    environment {
		DOCKERHUB_CREDENTIALS=credentials('dockerhub_sboudfor')
	}

    stages {
        stage('Build mvn and tests') {

            when {
                expression { tests == 'true' }
            }
            steps {
                // Get some code from a GitHub repository
                git 'https://github.com/saadBoudfor/b4-expenses-api.git/'
                sh "mvn -Dmaven.test.failure.ignore=true clean install"
            }
        }

       stage('Build mvn') {

            when {
                expression { tests == 'false' }
            }

            steps {
                // Get some code from a GitHub repository
                git 'https://github.com/saadBoudfor/b4-expenses-api.git/'
                sh "mvn -Dmaven.test.failure.ignore=true clean install -DskipTests -DskipIts"
            }
        }
        stage('Sonar') {

            when {
                expression { sonar == 'true' }
            }
            steps {
                sh "mvn sonar:sonar -Dsonar.projectKey=b4_expenses_api  -Dsonar.host.url=http://apps.boudfor.fr:9000 -Dsonar.login=8e7c7c6425e13db538fbc7801998937ba0b6e9ef"
            }
        }
        stage('Build docker') {
            steps {
                sh "docker build -t sboudfor/b4-expenses-api:prod ."
            }
        }

		stage('Login') {

			steps {
				sh 'echo $DOCKERHUB_CREDENTIALS_PSW | docker login -u $DOCKERHUB_CREDENTIALS_USR --password-stdin'
			}
		}

		stage('Push') {

			steps {
				sh 'docker push sboudfor/b4-expenses-api:prod'
			}
		}

    }
}
