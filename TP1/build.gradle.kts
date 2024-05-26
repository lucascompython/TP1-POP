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

dependencies {
    implementation("com.alibaba.fastjson2:fastjson2:2.0.50")

}

tasks.register<Exec>("jextract") {
    val ext = if (Os.isFamily(Os.FAMILY_WINDOWS)) ".bat" else ""

    commandLine = listOf(
        "../jextract/jextract$ext",
        "--output", "src/main/java",
        "--target-package", "org.tp1.bindings",
        "--library", "terminal_utils",
        "../terminal_utils/bindings.h"
    )
}

tasks.withType<JavaCompile> {
    dependsOn("jextract")
}

