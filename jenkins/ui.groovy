timeout(3) {
    node("maven-slave") {

        wrap([$class: 'BuildUser']) {
            currentBuild.description = """
user: $BUILD_USER
branch: $REFSPEC
        """
        }

        params = readYaml text: env.YAML_CONFIG ?: null
        if (params != null) {
            for(param in params.entrySet()) {
                env.setProperty(param.getKey(), param.getValue())
            }
        }

        stage("checkout") {
            checkout scm
        }

        stage("Create configurations") {
            dir("selenium-otus") {
                sh "echo BROWSER=${env.getProperty('BROWSER')} > ./.env"
                sh "echo BROWSER_VERSION=${env.getProperty('BROWSER_VERSION')} >> ./.env"
                sh "echo REMOTE_URL=${env.getProperty('REMOTE_URL')} >> ./.env"
            }
        }

        stage("Run UI tests") {
            sh "mkdir ./reports"
            sh "docker run --rm --env-file ./.env -v ./reports:/root/ui_tests/allure_results -t ui_tests:${env.getProperty('TEST_VERSION')}"
        }

        stage("Publish allure report") {
            REPORT_DISABLE = Boolean.parseBoolean(env.getProperty('REPORT_DISABLE')) ?: false
            allure([
                    results: ["./allure-results"],
                    disabled: REPORT_DISABLE,
                    reportBuildPolicy: ALWAYS
            ])
        }
        stage("elegram Notifications") {

        }
    }
}