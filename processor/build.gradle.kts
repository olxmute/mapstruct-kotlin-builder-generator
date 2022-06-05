plugins {
    kotlin("jvm")
    kotlin("kapt")
    `java-library`
    `maven-publish`
    signing
    id("io.codearte.nexus-staging") version "0.30.0"
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

java {
    withJavadocJar()
    withSourcesJar()
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            artifactId = rootProject.name
            groupId = rootProject.group.toString()
            version = rootProject.version.toString()
            from(components["java"])
            pom {
                packaging = "jar"
                name.set("mapstruct-kotlin-builder-generator")
                url.set("https://github.com/olxmute/mapstruct-kotlin-builder-generator")
                description.set("Generates builder classes for Kotlin data classes used in MapStruct mappers")

                licenses {
                    license {
                        name.set("MIT License")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }

                scm {
                    connection.set("scm:https://github.com/olxmute/mapstruct-kotlin-builder-generator.git")
                    developerConnection.set("scm:git@github.com:olxmute/mapstruct-kotlin-builder-generator.git")
                    url.set("https://github.com/olxmute/mapstruct-kotlin-builder-generator")
                }

                developers {
                    developer {
                        id.set("olxmute")
                        name.set("Oleksii Mutianov")
                        email.set("mutianov.o.o@gmail.com")
                    }
                }
            }
        }
    }
    repositories {
        maven {
            val releasesUrl = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
            val snapshotsUrl = uri("https://s01.oss.sonatype.org/content/repositories/snapshots/")
            url = if (version.toString().endsWith("SNAPSHOT")) snapshotsUrl else releasesUrl
            credentials {
                username = project.properties["ossrhUsername"].toString()
                password = project.properties["ossrhPassword"].toString()
            }
        }
    }
}

signing {
    sign(publishing.publications["mavenJava"])
}

nexusStaging {
    serverUrl = "https://s01.oss.sonatype.org/service/local/"
    username = project.properties["ossrhUsername"].toString()
    password = project.properties["ossrhPassword"].toString()
}