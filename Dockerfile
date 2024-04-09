FROM maven:3.9-eclipse-temurin-8

RUN mkdir -p /home/ubuntu/mobile_tests

WORKDIR /home/ubuntu/mobile_tests

COPY . /home/ubuntu/mobile_tests

ENTRYPOINT ["/bin/bash", "entrypoint.sh"]
