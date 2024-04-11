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

        stage("API tests in docker image") {
            sh "docker run -v /root/.m2/repository:/root/.m2/repository -v ./surefire-reports:/home/ubuntu/api_tests/target/surefire-reports -v ./allure-results:/home/ubuntu/api_tests/target/allure-results localhost:5005/apitests:0.0.1"
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
            resultText = "🟢 Passed: ${summary.passCount} \n🔴 Failed: ${summary.failCount} \n⚪️ Skipped: ${summary.skipCount}"
            withCredentials([string(credentialsId: 'telegram_chat', variable: 'CHAT_ID'), string(credentialsId: 'telegram_token', variable: 'TOKEN_BOT')]) {
                httpRequest httpMode: 'POST',
                        requestBody: """{\"chat_id\": ${CHAT_ID}, \"text\": \"API tests result:\n\nRunning by ${BUILD_USER_EMAIL}\n${resultText}\nReport: ${env.BUILD_URL}allure/\nDuration: ${currentBuild.durationString} \"}""",
                        contentType: 'APPLICATION_JSON',
                        url: "https://api.telegram.org/bot${TOKEN_BOT}/sendMessage",
                        validResponseCodes: '200'
            }
        }
    }
}