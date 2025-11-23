plugins {
    kotlin("jvm") version "2.1.0"
}

group = "ie.setu"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("javazoom:jlayer:1.0.1")
    implementation("org:jaudiotagger:2.0.1")
}

tasks.test {
    useJUnitPlatform()
}