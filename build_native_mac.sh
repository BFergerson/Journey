#!/usr/bin/env bash

cd jcef/jcef_build/native/Release
install_name_tool -change "@rpath/Frameworks/Chromium Embedded Framework.framework/Chromium Embedded Framework" "@loader_path/../Frameworks/Chromium Embedded Framework.framework/Chromium Embedded Framework" ./jcef_app.app/Contents/Java/libjcef.dylib
install_name_tool -change "@rpath/Frameworks/Chromium Embedded Framework.framework/Chromium Embedded Framework" "@executable_path/../../../Chromium Embedded Framework.framework/Chromium Embedded Framework" "./jcef_app.app/Contents/Frameworks/jcef Helper.app/Contents/MacOS/jcef Helper"
cd ../../../../

cd jcef/tools && ./make_distrib.sh macosx64
cd ../binary_distrib
zip -r jcef-distrib-macintosh64.zip macosx64