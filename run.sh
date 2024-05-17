#!/usr/bin/env bash

cd terminal_utils
cargo build --release

cd ../TP1
./gradlew installDist

cp ../terminal_utils/target/release/terminal_utils build/install/TP1/bin

cd build/install/TP1/bin
LD_LIBRARY_PATH="." ./TP1

cd ../../../../../