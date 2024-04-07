import com.google.inject.Stage
import groovy.json.JsonSlurperClassic
import org.apache.groovy.io.StringBuilderWriter

timeout(3) {
    node("maven-slave") {
        wrap([$class: 'BuildUser']) {
            currentBuild.description = """
user: $BUILD_USER
branch: $REFSPEC
"""
        }

        config = readYaml text: env.YAML_CONFIG
        BUILD_USER_EMAIL = $BUILD_USER_EMAIL

        if (config != null) {
            for(param in config.entrySet()) {
                env.setProperty(param.getKey(), param.getValue())
            }
        }

        testTypes = env.getProperty("TEST_TYPES").replace("[", "").replace("]", "").split(",\\s*") //["ui", "mobile", "api"]

    }

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

    parallel jobs

    stage("Create additional allure report artifacts") {
        dir("allure-results") {
            sh "echo BROWSER=${env.getProperty('BROWSER')} > environments.txt"
            sh "echo TEST_VERSION=${env.getProperty('TEST_VERSION')} >> environments.txt"
        }
    }

    stage("Copy allure results") {
        dir("allure-results") {

            for(type in testTypes) {
                copyArtifacts filter: "allure-report.zip", projectName: "${triggerdJobs[type].projecName}", selector: LastSucceful(), optional: true
                sh "unzip ./allure-report -d ."
                sh "rm -rf ./allure-report"
            }
        }
    }

    stage("Publish allure reports") {
        dir("allure-results") {
            result: ["."],
            reportBuildPolicy: ALWAYS
        }
    }
    Stage("Send notification") {
        def message = "+++++++++++ Test Result ++++++++++++\n"
        message += "Test running ${String.join(", ", jobs.setKeys())}"
        message += "BRANCH: ${BRANCH}\n"

        def slurper = new JsonSlurperClassic().parseText("./allure-reports/widgets/summary.json")
        if (slurper['Failed'] > 0) {
            message += "🔴 Status: FAILED"
            message += "\n@${BUILD_USER_EMAIL}"
        } else if ((slurper['skipped'] as Integer) > 0 && (slurper['TOTAL'] as Integer) == 0) {
            message += "⚪️ Status: SKIPPED"
        } else {
            message += "🟢 Status: PASSED"
        }

        withCredentials([string(credentialsId: telegram_token, valueVar: "TELEGRAM_TOKEN")])

        def stringBuilder = new StringBuilderWriter()
        def url = "https://api.telegram.org/bot${TELEGRAM_TOKEN}"
        stringBuilder.append(url)
        def urlConnection = new Url(stringBuilder.toString()).openConnection() as HttpURLConnection

        urlConnection.setRequestMethod("GET")
        urlConnection.setDoOutput(true)

    }

}