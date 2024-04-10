import com.google.inject.Stage
import groovy.json.JsonSlurperClassic
import org.apache.groovy.io.StringBuilderWriter

timeout(2) {
    node("maven-slave") {
        wrap([$class: 'BuildUser']) {
            currentBuild.description = """
user: $BUILD_USER
branch: $BRANCH
"""
        }

//        config = readYaml text: env.YAML_CONFIG
//        BUILD_USER_EMAIL = $BUILD_USER_EMAIL
//
//        if (config != null) {
//            for(param in config.entrySet()) {
//                env.setProperty(param.getKey(), param.getValue())
//            }
//        }
//
//        testTypes = env.getProperty("TEST_TYPES").replace("[", "").replace("]", "").split(",\\s*") //["ui", "mobile", "api"]
//
//    }
//
//    def jobs = [:]
//
//    def triggerdJobs = [:]
//
//    for(type in testTypes) {
//        jobs[type] = {
//            node('maven-slave') {
//                stage("Running $type tests") {
//                    triggerdJobs[type] = build(job: "$type-tests", parameters: [
//                            text(name: "YAML_CONFIG", value: env.YAML_CONFIG)
//                    ])
//                }
//            }
//        }
//    }
//
//    parallel jobs
//
//    stage("Create additional allure report artifacts") {
//        dir("allure-results") {
//            sh "echo BROWSER=${env.getProperty('BROWSER')} > environments.txt"
//            sh "echo TEST_VERSION=${env.getProperty('TEST_VERSION')} >> environments.txt"
//            sh "cat environments.txt"
//        }
//    }
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

/*    stage("Publish allure reports") {
        dir("allure-results") {
            result: ["."],
            reportBuildPolicy: ALWAYS
        }
    }*/

        stage("Api tests") {
            sh "pwd"
            sh "ls -al"
            sh "mvn -v"
            //docker.withRegistry('localhost:5005') {

            sh "usermod -a -G docker admin"
            docker.image('localhost:5005/apitests:0.0.1').inside {
                sh 'mvn test'
            }
            //}
            sh "docker -v"
            //sh "dockerd" // не хватает прав
//            systemctl start docker
//            systemctl enable docker
//            systemctl restart docker
            //sh "docker run localhost:5005/apitests:0.0.1"
        }

        stage("Send to Telegram") {
//        summary = junit testResults: "**/target/surefire-reports/*.xml", skipPublishingChecks: true
//        resultText = "RESULTS - Total: ${summary.totalCount} Passed: ${summary.passCount} Failed: ${summary.failCount} Skipped: ${summary.skipCount}"
//        allureReportUrl = "${env.BUILD_URL.replace('localhost', '127.0.0.1')}allure/"
            withCredentials([string(credentialsId: 'telegram_chat', variable: 'CHAT_ID'), string(credentialsId: 'telegram_token', variable: 'TOKEN_BOT')]) {
                httpRequest httpMode: 'POST',
                        requestBody: """{\"chat_id\": ${CHAT_ID}, \"text\": \"AUTOTESTS RUNNING FINISHED\n\"}""",
                        contentType: 'APPLICATION_JSON',
                        url: "https://api.telegram.org/bot${TOKEN_BOT}/sendMessage",
                        validResponseCodes: '200'
            }
        }

    }
}