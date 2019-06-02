#!/usr/bin/env bash

git clone https://bitbucket.org/chromiumembedded/java-cef.git jcef
cd jcef
git checkout 045302f591e53057a011b4b1df1d26fbee15e35d

mkdir jcef_build && cd jcef_build
cmake -G "Unix Makefiles" -DCMAKE_BUILD_TYPE=Release ..
make -j4

cd ../.. && ./gradlew patchJCEF