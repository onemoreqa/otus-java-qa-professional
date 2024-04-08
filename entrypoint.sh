#!/usr/bin/env bash

if [[ -z "$1" ]]; then
  command="mvn clean test"
else
  command="mvn clean test -Dparallel=$1"
fi

echo "Run: " $command
eval $command
echo "End of running: " $command