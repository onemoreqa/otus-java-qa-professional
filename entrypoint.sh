#!/usr/bin/env bash
# Отладочные варианты:
# Удаленный сервак (selenoid):
#mvn clean test -Dparallel=1 -Dbrowser=chrome -Dwebdriver.remote.url=http://95.181.151.41/wd/hub -Dbrowser.version=121.0

# В рамках перовой домашки локаль с поднятным selenoid
#mvn clean test -Dparallel=1 -Dbrowser=chrome -Dwebdriver.remote.url=http://0.0.0.0/wd/hub -Dbrowser.version=121.0

command="mvn clean test -Dparallel=$1 -Dbrowser=$2 -Dwebdriver.remote.url=$3 -Dbrowser.version=$4"

echo "Run: " $command
eval $command
echo "End of running: " $command