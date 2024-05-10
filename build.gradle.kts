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
}

tasks.named<JavaExec>("run") {
    standardInput = System.`in`
}

//dependencies {
//    testImplementation(platform("org.junit:junit-bom:5.10.0"))
//    testImplementation("org.junit.jupiter:junit-jupiter")
//}
//
//tasks.test {
//    useJUnitPlatform()
//}