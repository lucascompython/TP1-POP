import org.apache.tools.ant.taskdefs.condition.Os

plugins {
    id("java")
    application
}

group = "org.tp1"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

application {
    mainClass = "org.tp1.Main"
    applicationDefaultJvmArgs = listOf("--enable-native-access=ALL-UNNAMED")
}

java {
    sourceCompatibility = JavaVersion.VERSION_22
    targetCompatibility = JavaVersion.VERSION_22
}

tasks.register<Exec>("jextract") {
    val ext = if (Os.isFamily(Os.FAMILY_WINDOWS)) ".bat" else ""

    commandLine = listOf(
        "../jextract/jextract$ext",
        "--include-dir", "../terminal_utils/target/release",
        "--output", "src/main/java",
        "--target-package", "org.tp1.bindings",
        "--library", "terminal_utils",
        "../terminal_utils/bindings.h"
    )
}

tasks.named<JavaExec>("run") {
    standardInput = System.`in`
}

tasks.withType<JavaCompile> {
    dependsOn("jextract")
}

tasks.withType<JavaExec> {
    when {
        Os.isFamily(Os.FAMILY_WINDOWS) -> {
            val dllPath = "terminal_utils\\target\\release"
            val currentDir = System.getProperty("user.dir").split("\\").dropLast(2).joinToString("\\")
            val fullPath = "$currentDir\\$dllPath"
            val path = System.getenv("PATH")
            environment("PATH", "$path;$fullPath")
        }
        else -> environment("LD_LIBRARY_PATH", "../terminal_utils/target/release")

    }
}


