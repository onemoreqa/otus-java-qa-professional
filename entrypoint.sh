#!/usr/bin/env bash
# Отладочные варианты:

# В рамках шестой домашки локаль с Appium server
# mvn test -Dremote.url=http://0.0.0.0:4723/wd/hub -Dplatform.name=Android -Dplatform.version=8.1 -Davd.name=nexus -Ddevice.name=emulator-5554 -Dapp.package=com.pyankoff.andy -Dapp.activity=.MainActivity -Dapk.path=/home/egor/Desktop/otus-java-qa-professional/src/main/java/resources/Andy.apk

remote_url=http://0.0.0.0:4723/wd/hub
platform_name=Android
platform_version=8.1
avd_name=android8.1-1
device_name=android
app_package=com.pyankoff.andy
app_activity=.MainActivity
apk_path=/Andy.apk

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
    --platform_name)
    if [[ "$2" == "null" ]]; then
      echo "Use default value: platform_name=$platform_name"
    else
      platform_name="$2"
      shift # past argument
    fi
    ;;
    --platform_version)
    if [[ "$2" == "null" ]]; then
      echo "Use default value: platform_version=$platform_version"
    else
      platform_version="$2"
      shift # past argument
    fi
    ;;
    --avd_name)
    if [[ "$2" == "null" ]]; then
      echo "Use default value: avd_name=$avd_name"
    else
      avd_name="$2"
      shift # past argument
    fi
    ;;
    --device_name)
    if [[ "$2" == "null" ]]; then
      echo "Use default value: device_name=$device_name"
    else
      device_name="$2"
      shift # past argument
    fi
    ;;
    --app_package)
    if [[ "$2" == "null" ]]; then
      echo "Use default value: app_package=$app_package"
    else
      app_package="$2"
      shift # past argument
    fi
    ;;
    --app_activity)
    if [[ "$2" == "null" ]]; then
      echo "Use default value: app_activity=$app_activity"
    else
      app_activity="$2"
      shift # past argument
    fi
    ;;
    --apk_path)
    if [[ "$2" == "null" ]]; then
      echo "Use default value: apk_path=$apk_path"
    else
      apk_path="$2"
      shift # past argument
    fi
    ;;
    --help)
    echo "Usage: $0 [--remote_url <remote_url>] [--platform_name <platform_name>] [--platform_version <platform_version>] [--avd_name <avd_name>] [--device_name <device_name>] [--app_package <app_package>] [--app_activity  <app_activity >] [--apk_path <apk_path>]"
    echo "  --remote_url <remote_url>   Set the remote_url (e.g. http://95.181.151.41/wd/hub)"
    echo "  --platform_name <platform_name>   Set the platform_name to use (e.g. Android)"
    echo "  --platform_version <platform_version>   Set the platform.version to use (e.g. 8.1)"
    echo "  --avd_name <avd_name>   Set the avd.name to use (e.g. android8.1-1)"
    echo "  --device_name <device_name>   Set the device_name to use (e.g. android)"
    echo "  --app_package <app_package>   Set the app_package to use (e.g. com.pyankoff.andy)"
    echo "  --app_activity <app_activity>   Set the app_activity to use (e.g. .MainActivity)"
    echo "  --apk_path <apk_path>   Set the apk_path to use (e.g. /Andy.apk)"
    echo "  --help               Show this help message"
    exit 0
    ;;
    *)
    # unknown option
    ;;
  esac
  shift # past argument or value
done


command="mvn test -Dremote.url=$remote_url -Dplatform.name=$platform_name -Dplatform.version=$platform_version -Davd.name=$avd_name -Ddevice.name=$device_name -Dapp.package=$app_package -Dapp.activity=$app_activity -Dapk.path=$apk_path"

echo "Run: " $command
eval $command
echo "End of running: " $command