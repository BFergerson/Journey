#!/usr/bin/env bash

git clone https://bitbucket.org/chromiumembedded/java-cef.git jcef
./gradlew checkoutJCEF
./gradlew patchJCEF

echo "Using Java:"
java -version

cd jcef && mkdir jcef_build && cd jcef_build
cmake -G "Xcode" -DPROJECT_ARCH="x86_64" -DCMAKE_BUILD_TYPE=Release ..
xcodebuild -project jcef.xcodeproj -configuration Release -target ALL_BUILD
