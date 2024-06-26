# TP1 - POP

## TUI

Para fazer uma interface decente, tive que usar uma linguagem de baixo nível para conseguir acesso total ao terminal. Neste caso usei [Rust](https://www.rust-lang.org/). Desenvolvi uma pequena biblioteca para dar o acesso necessário ao Java.

A biblioteca em Rust "comunica" com a JVM usando o API [FFM](https://openjdk.org/jeps/454) introduzido no JDK 22.

Usei também a ferramenta [Jextract](https://jdk.java.net/jextract/) para gerar automaticamente "bindings" em Java ao ler um header em C. O Jextract roda a cada build (ver o [ficheiro de build do Gradle](/TP1/build.gradle.kts#L30)).  
Para gerar esse header usei uma build-time dependency, [cbindgen](https://github.com/mozilla/cbindgen) para gerar automaticamente o header em C ao ler as funções e structs em Rust que são sujeitas a ser partilhadas. O header é gerado a cada build da biblioteca (ver o [ficheiro de build da biblioteca](/terminal_utils/build.rs))

Ver diagrama a baixo.

![Diagrama do funcionamento tp1](/diagrama%20tp1.png)  

Decidi usar a biblioteca [crossterm](https://github.com/crossterm-rs/crossterm) em Rust para facilitar a portabilidade do programa.  
Usei também o [Gradle](https://gradle.org/) como build system para o Java.

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

- Ou então rodar o script `build.sh` ou `build.ps1` para compilar e rodar o programa. É preciso fazer o setup do Jextract antes de rodar o script.
- Ao dar build os ficheiros json são resetados. Para não resetar os ficheiros json, rodar o programa sem compilar.

```bash
# Linux
./build.sh

# Windows
./build.ps1

# Release build

# Linux
./build.sh --release
# Windows
./build.ps1 -release


# Ou rodar o programa sem compilar

# Linux
./run.sh

# Windows
./run.ps1
```

## Notas

Aqui vou mencionar algumas coisas que podem ser interessantes.

- No novo API FFM, ao alocar uma nova String (`arena.allocateFrom`) as Strings em Java são automaticamente convertidas para strings do estilo de C (NULL terminated). O que facilita a manipulação de strings que são passadas para a biblioteca em Rust.  
- O tipo `char` em Rust é de 4 bytes (32 bits), o que é diferente do C (1 byte) e do Java (2 bytes).

## Otimizações

Um dos meus objetivos principais foi otimizar o programa ao máximo.  

Devido à versão mínima do JDK ser 22 neste projeto (que ainda é muito recente), o [Proguard](https://github.com/Guardsquare/proguard) ainda não suporta o JDK 22. Senão, teria usado o Proguard para otimizar ainda mais o programa em Java.

Aqui estão algumas coisas que fiz.

- Usar o API FFM em vez de JNI.
- Alocar o mínimo de memória possível. Alcançado em parte ao usar código `unsafe` em Rust para evitar cópias desnecessárias.
- Renderizar para o terminal apenas quando é preciso. Alcançado com o uso de um buffer (macro `queue!` fornecida pelo crossterm) a dar `flush` ao `stdout` apenas quando necessário.
- Quando possível não dar clear ao terminal. Pelo menos não dar clear a tela toda. Muitas vezes o programa consegue escrever por cima do que já está no terminal. Outras vezes, o programa consegue apagar apenas o que é preciso.
- Usar a biblioteca [FastJson](https://github.com/alibaba/fastjson2/blob/main/README_EN.md) que é mais [rápida para JSON em Java](https://github.com/fabienrenaud/java-json-benchmark).
