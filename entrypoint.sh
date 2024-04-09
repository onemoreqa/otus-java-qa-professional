#!/usr/bin/env bash
command="mvn clean test"

echo "Run: " $command
eval $command
echo "End of running: " $command