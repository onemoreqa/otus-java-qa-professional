#!/usr/bin/env bash
# Отладочные варианты:
# Удаленный сервак (selenoid):
#mvn clean test -Dparallel=1 -Dbrowser=chrome -Dwebdriver.remote.url=http://95.181.151.41/wd/hub -Dbrowser.version=121.0

# В рамках перовой домашки локаль с поднятным selenoid
#mvn clean test -Dparallel=1 -Dbrowser=chrome -Dwebdriver.remote.url=http://0.0.0.0/wd/hub -Dbrowser.version=121.0

remote_url=https://onqa.su/wd/hub
parallel=1
browser=chrome
browser_version=120.0

# Parse command line arguments
while [[ $# -gt 0 ]]
do
  key="$1"

  case $key in
    --remote_url)
    # shellcheck disable=SC2077
    if [[ "$2" == "null" ]]; then
      echo "Use default value: remote_url=$remote_url"
    else
      remote_url="$2"
      shift # past argument
    fi
    ;;
    --parallel)
    if [[ "$2" == "null" ]]; then
      echo "Use default value: parallel=$parallel"
    else
      parallel="$2"
      shift # past argument
    fi
    ;;
    --browser)
    if [[ "$2" == "null" ]]; then
      echo "Use default value: browser=$browser"
    else
      browser="$2"
      shift # past argument
    fi
    ;;
    --browser_version)
    if [[ "$2" == "null" ]]; then
      echo "Use default value: browser_version=$browser_version"
    else
      browser_version="$2"
      shift # past argument
    fi
    ;;
    --help)
    echo "Usage: $0 [--remote_url <remote_url>] [--parallel <parallel>] [--browser <browser>] [--browser_version <browser_version>]"
    echo "  --remote_url <remote_url>   Set the remote_url (e.g. http://95.181.151.41/wd/hub)"
    echo "  --parallel <parallel>   Set the junit parallel threads (e.g. 2)"
    echo "  --browser <browser>   Set the browser (e.g. chrome/opera)"
    echo "  --browser_version <browser_version>   Set the browser_version (e.g. 120.0 or 121.0 for chrome and 105.0 for opera)"
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
command="mvn test -Dwebdriver.remote.url=$remote_url -Dparallel=$parallel -Dbrowser=$browser -Dbrowser.version=$browser_version"

echo "Run: " $command
eval $command
echo "End of running: " $command