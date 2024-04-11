import com.google.inject.Stage
import groovy.json.JsonSlurperClassic
import org.apache.groovy.io.StringBuilderWriter

timeout(10) {
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

        testTypes = env.getProperty("TEST_TYPES").replace("[", "").replace("]", "").split(",\\s*") //["ui", "mobile", "api"]


        def jobs = [:]

        def triggerdJobs = [:]

        for(type in testTypes) {
            jobs[type] = {
                node('maven-slave') {
                    stage("Running $type tests") {
                        triggerdJobs[type] = build(job: "$type-tests", parameters: [
                                text(name: "YAML_CONFIG", value: env.YAML_CONFIG)
                        ])
                    }
                }
            }
        }
//
//    parallel jobs
//

        stage("Create additional allure report artifacts") {
            dir("allure-results") {
                sh "echo BROWSER=${env.getProperty('BROWSER')} > environments.txt"
                sh "echo TEST_VERSION=${env.getProperty('TEST_VERSION')} >> environments.txt"
                sh "cat environments.txt"
            }
        }
//
//    stage("Copy allure results") {
//        dir("allure-results") {
//
//            for(type in testTypes) {
//                copyArtifacts filter: "allure-report.zip", projectName: "${triggerdJobs[type].projecName}", selector: LastSucceful(), optional: true
//                sh "unzip ./allure-report -d ."
//                sh "rm -rf ./allure-report"
//            }
//        }
//    }


        stage("Api tests in docker image") {
            sh "docker run -v /root/.m2/repository:/root/.m2/repository -v ./surefire-reports:/home/ubuntu/api_tests/target/surefire-reports -v ./allure-results:/home/ubuntu/api_tests/target/allure-results localhost:5005/apitests:${env.getProperty('TEST_VERSION')}"
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
            resultText = "RESULTS - Total: ${summary.totalCount} Passed: ${summary.passCount} Failed: ${summary.failCount} Skipped: ${summary.skipCount}"
            withCredentials([string(credentialsId: 'telegram_chat', variable: 'CHAT_ID'), string(credentialsId: 'telegram_token', variable: 'TOKEN_BOT')]) {
                httpRequest httpMode: 'POST',
                        requestBody: """{\"chat_id\": ${CHAT_ID}, \"text\": \"Tests result:\nRunning by ${BUILD_USER_EMAIL}\n${resultText}\nReport: ${env.BUILD_URL}allure/ \"}""",
                        contentType: 'APPLICATION_JSON',
                        url: "https://api.telegram.org/bot${TOKEN_BOT}/sendMessage",
                        validResponseCodes: '200'
            }
        }

    }
}