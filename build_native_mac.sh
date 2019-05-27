#!/usr/bin/env bash

cd jcef/tools && ./make_distrib.sh macosx64
cd ../binary_distrib
zip -r jcef-distrib-macintosh64.zip macosx64
ls
pwd