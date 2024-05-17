#!/usr/bin/env bash

curl -L https://download.java.net/java/early_access/jextract/22/4/openjdk-22-jextract+4-30_linux-x64_bin.tar.gz -o jextract.tar.gz

tar -xvzf jextract.tar.gz --strip-components=1

rm jextract.tar.gz

chmod +x jextract