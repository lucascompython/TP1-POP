param(
    [switch]$release = $false
)

$buildFolder = if ($release) { "release" } else { "debug" }

cd terminal_utils
cargo build $(if ($release) { "--release" })

cd ../TP1
./gradlew.bat installDist

cp ../terminal_utils/target/$buildFolder/terminal_utils.dll build/install/TP1/bin

cd build/install/TP1/bin
./TP1

cd ../../../../../