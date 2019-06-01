#!/usr/bin/env bash

git clone https://bitbucket.org/chromiumembedded/java-cef.git jcef
cd jcef
git checkout 1fda5d8f948670d08ef86bc4e8637b8581995ce9

cd .. && ./gradlew patchJCEF

cd jcef && mkdir jcef_build && cd jcef_build
cmake -G "Xcode" -DPROJECT_ARCH="x86_64" -DCMAKE_BUILD_TYPE=Release ..
xcodebuild -project jcef.xcodeproj -configuration Release -target ALL_BUILD
