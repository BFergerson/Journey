#!/usr/bin/env bash

git clone https://bitbucket.org/chromiumembedded/java-cef.git jcef
cd jcef
git checkout 1fda5d8f948670d08ef86bc4e8637b8581995ce9

mkdir jcef_build && cd jcef_build
cmake -G "Unix Makefiles" -DCMAKE_BUILD_TYPE=Release ..
make -j4

cd ../.. && ./gradlew patchJCEF