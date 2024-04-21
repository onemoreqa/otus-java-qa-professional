#!/usr/bin/env bash

parallel=4

# Parse command line arguments
while [[ $# -gt 0 ]]
do
  key="$1"

  case $key in
    --parallel)
    parallel="$2"
    shift # past argument
    ;;
    --help)
    echo "Usage: $0 [--parallel <parallel>]"
    echo "  --parallel <parallel>   Set junit parallel thread count (e.g. 2)"
    echo "  --help               Show this help message"
    exit 0
    ;;
    *)
    # unknown option
    ;;
  esac
  shift # past argument or value
done

# Run mvn test with specified keys
command="mvn test -Dparallel=$parallel"

echo "Run: " $command
eval $command
echo "End of running: " $command