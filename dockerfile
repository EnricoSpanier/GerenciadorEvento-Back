
FROM maven:3.9.4-eclipse-temurin-17 AS build
WORKDIR /build


COPY pom.xml .
COPY src ./src

RUN mvn -B -DskipTests clean package

FROM openjdk:17-jdk-slim
WORKDIR /app

COPY --from=build /build/target/*.jar ./app.jar

# Expose the port Spring Boot should listen on inside the container
EXPOSE 8081

# Start the app and force Spring to listen on 8081 via a system property
ENTRYPOINT ["java", "-Dserver.port=8081", "-jar", "/app/app.jar"]
