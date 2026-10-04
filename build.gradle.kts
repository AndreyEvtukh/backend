plugins {
    java
    id("org.springframework.boot") version "4.1.1"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "com.portfolio"
version = "1.0.1"
description = "portfolio-java-spring"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

val openapiVersion = "3.1.0"

dependencies {
    // Spring Web MVC
    implementation("org.springframework.boot:spring-boot-starter-webmvc")

    // Spring Data JPA
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    // DB
    implementation("org.flywaydb:flyway-database-postgresql")
    runtimeOnly("org.postgresql:postgresql")

    implementation("org.springframework.boot:spring-boot-starter-flyway")
    implementation("org.springframework.boot:spring-boot-starter-security")

    implementation("org.springframework.boot:spring-boot-starter-graphql")

    implementation("io.jsonwebtoken:jjwt-impl:0.13.0")
    implementation("io.jsonwebtoken:jjwt-api:0.13.0")
    implementation("io.jsonwebtoken:jjwt-jackson:0.13.0")

    implementation("org.springframework.boot:spring-boot-starter-webflux")

    implementation("org.eclipse.angus:angus-mail:2.0.4")
    implementation("jakarta.mail:jakarta.mail-api:2.1.3")
    implementation("org.springframework.boot:spring-boot-starter-mail")

    // MapStruct
    implementation("org.mapstruct:mapstruct:1.6.3")
    annotationProcessor("org.mapstruct:mapstruct-processor:1.6.3")

    // Swagger
//    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:${openapiVersion}")

    // Lombok
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok-mapstruct-binding:0.2.0")

    // Tests
//    testImplementation("org.springframework.boot:spring-boot-starter-data-jpa-test")
//    testImplementation("org.springframework.boot:spring-boot-starter-flyway-test")
//    testImplementation("org.springframework.boot:spring-boot-starter-security-test")
//    testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")

    testCompileOnly("org.projectlombok:lombok")
    testAnnotationProcessor("org.projectlombok:lombok")

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
