cd terminal_utils
cargo build --release

cd ../TP1
./gradlew.bat installDist

cp ../terminal_utils/target/release/terminal_utils.dll build/install/TP1/bin

cd build/install/TP1/bin
./TP1

cd ../../../../../
