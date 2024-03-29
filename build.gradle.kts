import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.21"
    kotlin("kapt") version "1.6.21"
}

repositories {
    mavenCentral()
}

allprojects {

    group = "io.github.olxmute"
    version = "0.0.3"

    repositories {
        mavenCentral()
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }

}
