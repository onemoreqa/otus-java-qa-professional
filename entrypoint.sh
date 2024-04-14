#!/usr/bin/env bash
# Отладочные варианты:

# В рамках шестой домашки локаль с Appium server
# mvn test -Dremote.url=http://0.0.0.0:4723/wd/hub -Dplatform.name=Android -Dplatform.version=8.1 -Davd.name=nexus -Ddevice.name=emulator-5554 -Dapp.package=com.pyankoff.andy -Dapp.activity=.MainActivity -Dapk.path=/home/egor/Desktop/otus-java-qa-professional/src/main/java/resources/Andy.apk

command="mvn test -Dremote.url=$1 -Dplatform.name=$2 -Dplatform.version=$3 -Davd.name=$4 -Ddevice.name=$5 -Dapp.package=$6 -Dapp.activity=$7 -Dapk.path=$8"

echo "Run: " $command
eval $command
echo "End of running: " $command