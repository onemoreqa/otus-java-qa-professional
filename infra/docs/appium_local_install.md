#### How to setup Appium in Ubuntu 22.04

[Source](https://aurigait.com/blog/how-to-setup-appium-in-ubuntu/)

###### Step 1: Install Java in your system.

```sh
# jdk 8
sudo apt update
sudo apt install openjdk-11-jdk
#Once the installation is complete, you can verify it by running the following command:
java -version # expected -> openjdk version "11.0.7" 2020-04-14
```

```sh
# jdk 11
sudo apt update
sudo apt install openjdk-8-jdk
#Once the installation is complete, you can verify it by running the following command:
java -version # expected -> openjdk version "1.8.0_252"
```

###### Step 2: Install Android Studio in your system.

```shell
sudo snap install android-studio --classic
# select “Standard” install type
```
Settings:
- Open Android Studio
- "Welcome to Android Studio" window  -> "More Actions" dropdown  -> select "SDK Manager"
- "Settings" page -> -> at “SDK platforms” tab , select Android 14 and Android 8.1

###### Step 3: For real device (optional)
- Connect your mobile device with your system(laptop) through the data cable.
- Open your mobile device settings  -> click on “About phone”  -> click on “Software information”  -> click on “Build Number” 7 times to open the     developer options in your mobile device.
- Now at settings page, you will get an “Developer options”  -> click on it  -> unable the “USB debugging” mode.

###### Step 4: Installing appium desktop and appium inspector.
```shell
sudo apt install libfuse2 #for running AppImage on Ubuntu 22.04
```
- [appiumServer](https://github.com/appium/appium-desktop/releases/tag/v1.22.3-4) -> go to this site and download the Appium-server-GUI -linux AppImage.
- [appiumInspector](https://github.com/appium/appium-inspector/releases) -> go to this site and download the Appium-Inspector-GUI- linux AppImage.
- (right click on the AppImage)  -> click on “Permissions” tab  -> select “Allow executing file as program”.

###### Step 5: Set up the environment variables in the bash file.
```shell
nano .bashrc
```
add this to the end:
```shell
export ANDROID_HOME="$HOME/Android/Sdk"
export JAVA_HOME="/usr/lib/jvm/java-11-openjdk-amd64"
export ADB="$ANDROID_HOME/platform-tools/adb"
export PATH=$JAVA_HOME/bin:$PATH
```
```shell
source ~/.profile
echo $JAVA_HOME # -> /usr/lib/jvm/java-11-openjdk-amd64
echo $ANDROID_HOME # -> 
echo $ADB # -> 
```

###### Step 6: Adb devices
```shell
sudo apt install android-tools-adb android-tools-fastboot
adb devices # -> List of devices attached
```

###### Step 7: Open the Appium Desktop (Server) GUI
- Edit Configurations -> set fields from bash -> save
- Host => Enter 127.0.0.1 or 0.0.0.0
- Port => Enter 4723
- Click on “startServer” button.

###### Step 8: Copy Andy.apk to appium folder
```shell
cd ~
mkdir appium
cp ${this_project_path}/src/main/resources/Andy.apk ~/appium/Andy.apk
# now we have copy of our *.apk in some place on local machine with Ubuntu 
```

###### Step 9 :- Open the Appium Inspector.
- Remote Host -> Enter 127.0.0.1 or 0.0.0.0
- Remote Port -> Enter “4723”
- Remote Path -> Enter “/wd/hub/”
- Desired Capabilities: ->  Copy the below json in JSON Representation box and after that click on “save” icon given in the box.
```json
{
  "appium:platformName": "Android",
  "appium:deviceName": "android",
  "appium:automationName": "UIAutomator2",
  "appium:app": "/home/egor/appium/Andy.apk",
  "appium:avd": "Pixel_3a_API_34_extension_level_7_x86_64"
}
```

- As result: we can see android app inside Appium inspector
```shell
adb devices 
#return our virtual device:
#List of devices attached
#emulator-5554   device
```

###### Step 10: change capabilities after *.apk installation
- repeat step9 with Capabilities:
```json
{
  "appium:platformName": "Android",
  "appium:deviceName": "android",
  "appium:automationName": "UIAutomator2",
  "appium:appPackage": "com.pyankoff.andy",
  "appium:appActivity": ".MainActivity",
  "appium:avd": "Pixel_3a_API_34_extension_level_7_x86_64"
}
```
- As result: You'll see Andy.apk on android 14 device

###### Step 11: add new device
- Open Android studio -> more actions -> virtual device manager 
- add new device with Android 8.1 with avd.name = small_android_8.1
- start device in studio, close it
```shell
adb devices
# return more than one device:
#emulator-5554   device
#emulator-5556   device
```
- repeat step 9 with capabilities:
```json
{
  "appium:platformName": "Android",
  "appium:deviceName": "emulator-5556",
  "appium:automationName": "UIAutomator2",
  "appium:pVersion": "8.1",
  "appium:avd": "small_android_8.1"
}
```
- As result: starting android 8.1
- Close session inside Appium inspector
- Turn off new device emulator
- repeat step 9 with capabilities:

```json
{
"appium:platformName": "Android",
"appium:deviceName": "emulator-5556",
"appium:automationName": "UIAutomator2",
"appium:pVersion": "8.1",
"appium:avd": "small_android_8.1",
"appium:app": "/home/egor/appium/Andy.apk"
}
```
- As result: You'll see Andy.apk on android 8.1 device

#### Conclusion
- We spent 6-11 Gb memory for each android virtual device in folders
- - ~/.android/avd/Pixel_3a_API_34_extension_level_7_x86_64.avd/
- - ~/.android/avd/small_android_8.1.avd/
- Capabilities for running Andy.apk on android 14:
```json
{
  "appium:platformName": "Android",
  "appium:deviceName": "emulator-5554",
  "appium:automationName": "UIAutomator2",
  "appium:pVersion": "14.0",
  "appium:appPackage": "com.pyankoff.andy",
  "appium:appActivity": ".MainActivity",
  "appium:avd": "big_android_14.0"
}
```
- Capabilities for running Andy.apk on android 8.1:
```json
{
  "appium:platformName": "Android",
  "appium:deviceName": "emulator-5556",
  "appium:automationName": "UIAutomator2",
  "appium:pVersion": "8.1",
  "appium:appPackage": "com.pyankoff.andy",
  "appium:appActivity": ".MainActivity",
  "appium:avd": "small_android_8.1"
}
```
- Some reasons, each time adb devices returning: emuator-5554 -_-