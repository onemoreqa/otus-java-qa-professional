timeout(3) {
    node("maven-slave") {

        wrap([$class: 'BuildUser']) {
            currentBuild.description = """
user: $BUILD_USER
branch: $BRANCH
        """
        }

        config = readYaml text: env.YAML_CONFIG

        if (config != null) {
            for(param in config.entrySet()) {
                env.setProperty(param.getKey(), param.getValue())
            }
        }

        stage("UI tests in docker image") {
            sh "docker run -v /root/.m2/repository:/root/.m2/repository -v ./surefire-reports:/home/ubuntu/ui_tests/target/surefire-reports -v ./allure-results:/home/ubuntu/ui_tests/target/allure-results localhost:5005/uitests:0.0.1 1 chrome http://95.181.151.41/wd/hub 120.0"
            //sh "sleep 300"
        }

        stage("Publish Allure Reports") {
            allure([
                    includeProperties: false,
                    jdk: '',
                    properties: [],
                    reportBuildPolicy: 'ALWAYS',
                    results: [[path: './allure-results']]
            ])
        }

        stage("Send to Telegram") {
            summary = junit testResults: "**/surefire-reports/*.xml", skipPublishingChecks: true
            resultText = "\u2705 Passed: ${summary.passCount} \n\uD83D\uDD34 Failed: ${summary.failCount} \n\u26AA Skipped: ${summary.skipCount}"
            withCredentials([string(credentialsId: 'telegram_chat', variable: 'CHAT_ID'), string(credentialsId: 'telegram_token', variable: 'TOKEN_BOT')]) {
                sh "curl -X POST -H 'Content-Type: application/json' -d '{\"chat_id\": ${CHAT_ID}, \"text\": \"UI tests result:\n\nRunning by ${BUILD_USER_EMAIL}\n${resultText}\nReport: ${env.BUILD_URL}allure/\nDuration: ${currentBuild.durationString} \", \"disable_notification\": true}'      https://api.telegram.org/bot${TOKEN_BOT}/sendMessage"
            }
        }
    }
}