timeout(5) {
    node("maven-slave") {
        stage("Checkout") {
            checkout scm
        }
        stage("Run tests") {
            def exitCode = sh(
                    returnStatus: true,
                    script: """
                    docker run --rm --network=host -it apitests:0.0.1 $THREADS
                    """
            )
            if(exitCode == 1) {
                currentBuild.result = 'UNSTABLE'
            }
        }
        stage("Publish allure results") {
            allure([
                    includeProperties: false,
                    jdk: '',
                    properties: [],
                    reportBuildPolicy: 'ALWAYS',
                    results: [[path: 'target/allure-results']]
            ])
        }
    }
}