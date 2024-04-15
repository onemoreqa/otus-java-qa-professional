timeout(5) {
    node("maven-slave") {
        stage("Checkout") {
            checkout scm
        }
        stage("Deploy changes to jenkins") {
            dir("jenkins") {
                //Не заработает если на агент не накатывали бинарь jenkins-jobs
                sh "jenkins-jobs --conf ./jobs/jobs.ini update ./jobs"
            }
        }
    }
}