#!/usr/bin/env bash

if [[ -z "$1" ]]; then
  command="mvn test"
else
  command="mvn test -Dparallel=$1"
fi

echo "Run: " $command
eval $command
echo "End of running: " $command