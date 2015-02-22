#!/bin/bash
set -ex

echo no | android create avd --force -n test -t android-18 --abi armeabi-v7a
emulator -avd test -no-skin -no-audio -no-window &
curl http://is.gd/android_wait_for_emulator > android-wait-for-emulator
chmod u+x android-wait-for-emulator
./android-wait-for-emulator
adb shell input keyevent 82 &
 