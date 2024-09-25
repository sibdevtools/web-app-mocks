plugins {
    id("java")
    id("application")
    id("org.springframework.boot") version "3.3.3"
}

apply(plugin = "io.spring.dependency-management")

dependencies {
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    implementation(project(":web-app"))

    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")

    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    compileOnly("jakarta.servlet:jakarta.servlet-api")

    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:${project.property("lib_springdoc_version")}")

    implementation("com.h2database:h2")

    implementation("com.github.sibdevtools:api-session:${project.property("lib_api_session_version")}")
    implementation("com.github.sibdevtools:api-storage:${project.property("lib_api_storage_version")}")

    implementation("com.github.sibdevtools:service-session-embedded:${project.property("lib_embedded_session_version")}")
    implementation("com.github.sibdevtools:service-storage-embedded:${project.property("lib_embedded_storage_version")}")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
