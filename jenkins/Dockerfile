FROM ubuntu:18.04
RUN mkdir -p /var/jenkins_home
COPY $PWD/jenkinsdata/ /var/jenkins_home
VOLUME /var/jenkins_home
ENTRYPOINT /usr/bin/tail -f /dev/null