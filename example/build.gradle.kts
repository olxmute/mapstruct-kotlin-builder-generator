plugins {
    kotlin("jvm")
    kotlin("kapt")
    id("com.netflix.dgs.codegen") version "5.1.17"
}

dependencies {
    implementation(platform("com.netflix.graphql.dgs:graphql-dgs-platform-dependencies:latest.release"))
    implementation("com.netflix.graphql.dgs:graphql-dgs-spring-boot-starter")

    kapt(project(":processor"))

    kapt("org.mapstruct:mapstruct-processor:1.4.2.Final")
    implementation("org.mapstruct:mapstruct:1.4.2.Final")

    kapt("com.driver733.mapstruct-fluent:processor:1.1.1")
    implementation("com.driver733.mapstruct-fluent:processor:1.1.1")

}

kapt {
    arguments {
        arg("mapstruct.unmappedTargetPolicy", "ERROR")
    }
}
