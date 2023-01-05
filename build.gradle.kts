import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.0.1"
    id("io.spring.dependency-management") version "1.1.0"
    kotlin("jvm") version "1.7.22"
    kotlin("plugin.spring") version "1.7.22"
}

group = "si.blaz"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
}

dependencies {
    //External dependencies
    implementation("org.jsoup:jsoup:1.15.3")
    implementation("biz.paluch.logging:logstash-gelf:1.15.0")
    implementation("com.konghq:unirest-java:3.14.1")
    //Spring stuff
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-log4j2")
    implementation("org.springframework.boot:spring-boot-starter-graphql")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    //Micrometer
    implementation("io.micrometer:micrometer-registry-prometheus")
    implementation("io.micrometer:micrometer-tracing-bridge-otel")
    //Default things
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }


}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.processResources {
    filesMatching("application.properties") {
        expand(project.properties)
    }
}

configurations.implementation {
    exclude("org.springframework.boot", "spring-boot-starter-logging")
}

springBoot {
    mainClass.set("com.example.movieparserspring.MovieParserSpringApplicationKt")
}

