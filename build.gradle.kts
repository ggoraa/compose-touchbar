import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.21"
    id("org.jetbrains.compose") version "1.0.0-alpha1"
    `maven-publish`
}

group = "dev.ggoraa.compose"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
    mavenLocal()
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "dev.ggoraa.compose"
            artifactId = "touchbar"
            version = "1.0-SNAPSHOT"

            from(components["kotlin"])
        }
    }
}

dependencies {
    implementation(compose.desktop.currentOs)
    // Not on a public repository, build manually from
    // https://github.com/shannah/Java-Objective-C-Bridge.
    implementation("ca.weblite:java-objc-bridge:1.1-SNAPSHOT")

    // Local repo, need to build manually from
    // https://github.com/ggoraa/kappkit
    implementation("dev.ggoraa.kappkit:kappkit:1.0-SNAPSHOT")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}

compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "ComposeTouchbar"
            packageVersion = "1.0.0"
        }
    }
}