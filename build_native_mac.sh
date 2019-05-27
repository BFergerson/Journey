#!/usr/bin/env bash

cd jcef/tools && ./make_distrib.sh macosx64
cd ../binary_distrib && strip ./macosx64/bin/lib/macosx64/libcef.so
zip -r jcef-distrib-macosx64.zip macosx64