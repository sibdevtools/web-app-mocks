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

    implementation("com.h2database:h2")

    implementation("com.github.simple-mocks:api-session:${project.property("lib_api_session_version")}")
    implementation("com.github.simple-mocks:api-storage:${project.property("lib_api_storage_version")}")

    implementation("com.github.simple-mocks:service-session-embedded:${project.property("lib_embedded_session_version")}")
    implementation("com.github.simple-mocks:service-storage-embedded:${project.property("lib_embedded_storage_version")}")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
