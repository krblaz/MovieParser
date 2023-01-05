FROM gradle:jdk17 as builder
COPY src /app/src
COPY build.gradle.kts /app
WORKDIR /app
RUN gradle bootJar

FROM amazoncorretto:17
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar /app/movie_parser.jar
CMD ["java","-jar", "movie_parser.jar"]