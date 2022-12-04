FROM gradle:jdk17 as builder
COPY src /app/src
COPY build.gradle.kts /app
WORKDIR /app
RUN gradle bootJar

FROM amazoncorretto:17
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar /app
CMD ["java","-jar", "app-0.0.1-SNAPSHOT.jar"]