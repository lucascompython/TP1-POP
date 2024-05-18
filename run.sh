#!/usr/bin/env bash

if [ "$1" == "--release" ] || [ "$1" == "-r" ]; then
    BUILD_TYPE="release"
    CARGO_FLAG="--release"
else
    BUILD_TYPE="debug"
    CARGO_FLAG=""
fi

cd terminal_utils
cargo build $CARGO_FLAG

if [ $? -ne 0 ]; then
    exit $?
fi

cd ../TP1
./gradlew installDist

if [ $? -ne 0 ]; then
    exit $?
fi

cp ../terminal_utils/target/$BUILD_TYPE/libterminal_utils.so build/install/TP1/bin

cd build/install/TP1/bin
LD_LIBRARY_PATH="." ./TP1

cd ../../../../../