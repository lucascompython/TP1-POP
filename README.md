# TP1 - POP

## TUI

Para fazer uma interface decente, tive que usar uma linguagem de baixo nivel para conseguir acesso total ao terminal. Neste caso usei Rust. Desenvolvi uma  pequena biblioteca para dar o acesso necessario ao Java.

A biblioteca em Rust "comunica" com a JVM usando o API [FFM](https://openjdk.org/jeps/454) introduzido no JDK 22.

Usei tambem a ferramenta [Jextract](https://jdk.java.net/jextract/) para gerar automaticamente "bindings" em Java ao ler um header em C. O Jextract roda a cada build (ver o [ficheiro de build do Gradle](/TP1/build.gradle.kts#L25)).  
Para gerar esse header usei uma build-time dependency, [cbindgen](https://github.com/mozilla/cbindgen) para gerar automaticamente o header em C ao ler as funções e structs em Rust que são sujeitas a ser partilhadas. O header é gerado a cada build da biblioteca (ver o [ficheiro de build da biblioteca](/terminal_utils/build.rs))

Ver diagrama a baixo.

![Diagrama do funcionamento tp1](/diagrama%20tp1.drawio.png)

## Instruções para compilar

- Transferir o Jextract

```bash
cd jextract
# Linux
./setup.sh
# Windows
./setup.ps1
```

- Compilar a biblioteca em Rust

```bash
cd terminal_utils
cargo build --release
```

- Compilar código em Java

```bash
cd TP1
./gradlew installDist
```

- Rodar o programa

```bash
cd TP1/build/install/bin
# Pode se copiar a biblioteca (ficheiro .dll no Windows e .so em Linux) para esta pasta

# Linux
cp ../../../../../terminal_utils/target/release/libterminal_utils.so .
# Windows
cp ../../../../../terminal_utils/target/release/terminal_utils.dll .

# Rodar o programa

# Linux
LD_LIBRARY_LOAD="." ./TP1

# Windows 
./TP1
```

- Ou então rodar o script `run.sh` ou `run.ps1` para compilar e rodar o programa. É preciso fazer o setup do Jextract antes de rodar o script.

```bash
# Linux
./run.sh

# Windows
./run.ps1

# Release build

# Linux
./run.sh --release
# Windows
./run.ps1 -release
```

## Notas

Aqui vou mencionar algumas coisas que podem ser interessantes.

- No novo API FFM, as Strings em Java são automaticamente convertidas para strings do estilo de C (NULL terminated).  
- O tipo `char` em Rust é de 4 bytes (32 bits), o que é diferente do C/Java que é de 1 byte (8 bits). Por isso usei o tipo `u8` em Rust para representar um `char`.
