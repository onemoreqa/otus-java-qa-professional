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

        stage("Start jobs notification") {
            withCredentials([string(credentialsId: 'telegram_chat', variable: 'CHAT_ID'), string(credentialsId: 'telegram_token', variable: 'TOKEN_BOT')]) {
                sh "curl -X POST -H 'Content-Type: application/json' -d '{\"chat_id\": ${CHAT_ID}, \"text\": \"\uD83D\uDE4F Ready to start ${env.getProperty('TEST_TYPES')} \", \"disable_notification\": true}'      https://api.telegram.org/bot${TOKEN_BOT}/sendMessage"
            }
        }

        testTypes = env.getProperty("TEST_TYPES").replace("[", "").replace("]", "").split(",\\s*") //["ui", "mobile", "api"]

        def jobs = [:]
        def triggerdJobs = [:]

        for(type in testTypes) {
            jobs[type] = {
                node('maven-slave') {
                    stage("Running $type tests") {
                        triggerdJobs[type] = build(job: "$type", parameters: [
                                text(name: "YAML_CONFIG", value: env.YAML_CONFIG)
                        ])
                    }
                }
            }
        }

        parallel jobs

    }
}