#!/usr/bin/env bash

cd jcef/tools && ./compile.sh linux64
./make_distrib.sh linux64
cd ../binary_distrib && strip ./linux64/bin/lib/linux64/libcef.so
zip -r jcef-distrib-linux64.zip linux64