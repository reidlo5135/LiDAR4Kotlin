plugins {
    kotlin("jvm") version "1.9.0"
}

group = "net.wavem.rclkotlin"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation ("io.github.lambdaprime:jros2client:1.0")
    implementation("io.github.lambdaprime:jros2messages")
    implementation("io.github.pinorobotics:rtpstalk:4.0")
    implementation("io.github.lambdaprime:id.xfunction:20.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("io.reactivex:rxjava:1.3.8")
    implementation("com.google.code.gson:gson:2.8.9")
    implementation("org.slf4j:slf4j-api:2.0.7")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}