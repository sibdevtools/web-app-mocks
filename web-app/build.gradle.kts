import java.text.SimpleDateFormat
import java.util.*

plugins {
    id("java")
    id("jacoco")
    id("maven-publish")
}

dependencies {
    compileOnly("org.projectlombok:lombok")
    compileOnly("jakarta.servlet:jakarta.servlet-api")

    annotationProcessor("org.projectlombok:lombok")

    implementation("org.springframework:spring-context")
    implementation("org.springframework:spring-core")
    implementation("org.springframework:spring-web")
    implementation("org.springframework:spring-webmvc")
    implementation("org.springframework.boot:spring-boot-autoconfigure")
    implementation("org.springframework.boot:spring-boot-starter-validation")

    implementation("org.springframework.data:spring-data-jpa")

    implementation("org.flywaydb:flyway-core")

    implementation("com.fasterxml.jackson.core:jackson-databind")

    implementation("com.fasterxml.jackson.module:jackson-module-parameter-names")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jdk8")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")

    implementation("jakarta.annotation:jakarta.annotation-api")
    implementation("jakarta.persistence:jakarta.persistence-api")

    implementation("org.graalvm.js:js:${project.property("lib_graalvm_js")}")
    implementation("org.graalvm.js:js-scriptengine:${project.property("lib_graalvm_js")}")

    implementation("org.graalvm.python:python:${project.property("lib_graalvm_python")}")

    implementation("com.github.sibdevtools:api-common:${project.property("lib_api_common_version")}")
    implementation("com.github.sibdevtools:api-error:${project.property("lib_api_error_version")}")
    implementation("com.github.sibdevtools:api-localization:${project.property("lib_api_localization_version")}")
    implementation("com.github.sibdevtools:api-session:${project.property("lib_api_session_version")}")
    implementation("com.github.sibdevtools:api-storage:${project.property("lib_api_storage_version")}")
    implementation("com.github.sibdevtools:api-web-app:${project.property("lib_api_web_app_version")}")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-starter-web")

    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testImplementation("org.junit.jupiter:junit-jupiter-params")

    testImplementation("org.mockito:mockito-core")

    testCompileOnly("org.projectlombok:lombok")
    testAnnotationProcessor("org.projectlombok:lombok")

    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.register<Copy>("copyFrontendResources") {
    group = "build"
    description = "Copies the frontend build resources to the Spring Boot static directory"

    dependsOn(":web-app-frontend:build")

    from(project(":web-app-frontend").file("build/out"))
    into(layout.buildDirectory.dir("resources/main/web/app/mocks/static"))
}

tasks.named("processResources") {
    dependsOn("copyFrontendResources")
}

tasks.jar {
    dependsOn("copyFrontendResources")
    from("LICENSE") {
        rename { "${it}_${project.property("project_name")}" }
    }
    manifest {
        attributes(
            mapOf(
                "Specification-Title" to project.name,
                "Specification-Vendor" to project.property("author"),
                "Specification-Version" to project.version,
                "Specification-Timestamp" to SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").format(Date()),
                "Timestamp" to System.currentTimeMillis(),
                "Built-On-Java" to "${System.getProperty("java.vm.version")} (${System.getProperty("java.vm.vendor")})"
            )
        )
    }
}


tasks.test {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            artifactId = "web-app-mocks"
            pom {
                packaging = "jar"
                url = "https://github.com/sibdevtools/web-app-mocks"

                licenses {
                    license {
                        name.set("The MIT License (MIT)")
                        url.set("https://www.mit.edu/~amini/LICENSE.md")
                    }
                }

                scm {
                    connection.set("scm:https://github.com/sibdevtools/web-app-mocks.git")
                    developerConnection.set("scm:git:ssh://github.com/sibdevtools")
                    url.set("https://github.com/sibdevtools/web-app-mocks")
                }

                developers {
                    developer {
                        id.set("sibmaks")
                        name.set("Maksim Drobyshev")
                        email.set("sibmaks@vk.com")
                    }
                }
            }
        }
    }
    repositories {
        maven {
            val releasesUrl = uri("https://nexus.sibmaks.ru/repository/maven-releases/")
            val snapshotsUrl = uri("https://nexus.sibmaks.ru/repository/maven-snapshots/")
            url = if (version.toString().endsWith("SNAPSHOT")) snapshotsUrl else releasesUrl
            credentials {
                username = project.findProperty("nexus_username")?.toString() ?: System.getenv("NEXUS_USERNAME")
                password = project.findProperty("nexus_password")?.toString() ?: System.getenv("NEXUS_PASSWORD")
            }
        }
    }
}
