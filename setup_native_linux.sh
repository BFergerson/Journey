#!/usr/bin/env bash

git clone https://bitbucket.org/chromiumembedded/java-cef.git jcef
./gradlew checkoutJCEF

cd jcef && mkdir jcef_build && cd jcef_build
cmake -G "Unix Makefiles" -DCMAKE_BUILD_TYPE=Release ..
make -j4

cd ../.. && ./gradlew patchJCEF