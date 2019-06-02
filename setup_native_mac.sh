#!/usr/bin/env bash

git clone https://bitbucket.org/chromiumembedded/java-cef.git jcef
cd jcef
git checkout 045302f591e53057a011b4b1df1d26fbee15e35d

cd .. && ./gradlew patchJCEF

cd jcef && mkdir jcef_build && cd jcef_build
cmake -G "Xcode" -DPROJECT_ARCH="x86_64" -DCMAKE_BUILD_TYPE=Release ..
xcodebuild -project jcef.xcodeproj -configuration Release -target ALL_BUILD
