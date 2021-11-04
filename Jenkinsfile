pipeline {
    agent any

    tools {
        // Install the Maven version configured as "M3" and add it to the path.
        maven "mvn"
    }

    stages {
        stage('Build and tests') {

            when {
                expression { tests == 'true' }
            }
            steps {
                // Get some code from a GitHub repository
                git 'https://github.com/saadBoudfor/b4-expenses-api.git/'
                sh "mvn -Dmaven.test.failure.ignore=true clean install"
            }
        }

       stage('Build') {

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

//         stage ('Send jar to b4-server') {
//             steps {
//                 sshPublisher(publishers: [sshPublisherDesc(configName: 'my ssh server', transfers: [sshTransfer(cleanRemote: false, excludes: '', execCommand: '', execTimeout: 120000, flatten: false, makeEmptyDirs: false, noDefaultExcludes: false, patternSeparator: '[, ]+', remoteDirectory: 'backend', remoteDirectorySDF: false, removePrefix: 'target', sourceFiles: 'target/*.jar')], usePromotionTimestamp: false, useWorkspaceInPromotion: false, verbose: false)])
//             }
//         }
        stage('Build image') {
            steps {
                echo 'Starting to build docker image'

                script {
                    def customImage = docker.build("b4-expenses/api")
                    customImage.push()
                }
            }
        }
    }
}
