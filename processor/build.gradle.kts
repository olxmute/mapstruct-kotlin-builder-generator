plugins {
    kotlin("jvm")
    kotlin("kapt")
    `java-library`
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.6.21")
    implementation("org.jetbrains.kotlinx:kotlinx-metadata-jvm:0.4.2")

    implementation("com.squareup:javapoet:1.13.0")

    kapt("com.google.auto.service:auto-service:1.0.1")
    implementation("com.google.auto.service:auto-service:1.0.1")

    implementation("org.mapstruct:mapstruct:1.4.2.Final")
    implementation("org.mapstruct:mapstruct-processor:1.4.2.Final")
}
