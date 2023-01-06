FROM gradle:jdk17 as builder
COPY src /app/src
COPY build.gradle.kts /app
WORKDIR /app
RUN gradle bootJar

FROM amazoncorretto:17
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar /app/circuit_breaker_test.jar
CMD ["java","-jar", "circuit_breaker_test.jar"]