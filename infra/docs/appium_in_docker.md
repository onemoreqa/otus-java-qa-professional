#### Запуск андроид тестов в контейнере selenoid

<details>
  <summary>Dockerfile</summary>

```shell
# Use the official Appium Docker image as the base image
FROM appium/appium:latest

# Install required dependencies
RUN apt-get update && \
    apt-get install -y openjdk-8-jdk && \
    apt-get install -y wget && \
    apt-get install -y unzip && \
    rm -rf /var/lib/apt/lists/*

# Set the Java home environment variable
ENV JAVA_HOME /usr/lib/jvm/java-8-openjdk-amd64/

# Download the Android SDK
RUN wget https://dl.google.com/android/repository/sdk-tools-linux-4333796.zip && \
    unzip sdk-tools-linux-4333796.zip -d /usr/local/ && \
    rm sdk-tools-linux-4333796.zip

# Add the Android SDK tools to the PATH environment variable
ENV PATH /usr/local/tools/bin:$PATH

# Accept the Android SDK license agreement
RUN yes | sdkmanager --licenses

# Install the required Android platform and build tools
RUN sdkmanager "platforms;android-29" "build-tools;29.0.3"

# Copy the Appium test scripts and the APK to the Docker image
COPY ./tests /tests
COPY ./app.apk /app.apk

# Set the Appium desired capabilities
ENV APPIUM_DESIRED_CAPABILITIES='{"platformName":"Android","deviceName":"Android Emulator","platformVersion":"10.0","app":"/app.apk","appPackage":"com.example.myapp","appActivity":"com.example.myapp.MainActivity"}'

# Set the Appium server command
ENV APPIUM_COMMAND="appium --relaxed-security --command-timeout 60 --debug-log-spacing --log-timestamp --local-timezone --session-override"

# Set the working directory
WORKDIR /tests

# Expose the Appium server port
EXPOSE 4723

# Run the Appium server and execute the tests
CMD ["/bin/bash", "-c", "${APPIUM_COMMAND} && mocha test/specs/**/*.js"]
```
- запуск
```shell
docker build -t my-appium-tests .
docker run -p 4723:4723 my-appium-tests
```
</details>

<details>
  <summary>Compose</summary>

```yaml
version: '3'
services:
  appium:
    build:
      context: .
      dockerfile: Dockerfile
    ports:
      - "4723:4723"
    volumes:
      - /dev/bus/usb:/dev/bus/usb
    environment:
      - APPIUM_DESIRED_CAPABILITIES='{"platformName":"Android","deviceName":"Android Emulator","platformVersion":"10.0","app":"/app.apk","appPackage":"com.example.myapp","appActivity":"com.example.myapp.MainActivity"}'
    privileged: true
```
- Докерфайл для композа
```shell
FROM appium/appium:latest

RUN apk add --no-cache openjdk-8-jre

WORKDIR /app

COPY app.apk /app/

CMD ["appium", "--relaxed-security", "--command-timeout", "60", "--debug-log-spacing", "--log-timestamp", "--local-timezone", "--session-override", "--device-name", "Android Emulator", "--platform-name", "Android", "--platform-version", "10.0", "--app", "/app/app.apk", "--app-package", "com.example.myapp", "--app-activity", "com.example.myapp.MainActivity"]
```
</details>