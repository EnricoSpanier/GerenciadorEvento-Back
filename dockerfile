## Estágio de build: compila o jar com Maven dentro do container
FROM maven:sapmachine AS build
WORKDIR /build

COPY pom.xml .
COPY src ./src

# Com BuildKit você pode cachear ~/.m2 para acelerar downloads futuros:
RUN --mount=type=cache,target=/root/.m2 mvn -B -DskipTests clean package

# Estágio runtime: imagem pequena apenas para executar o jar
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /build/target/*.jar ./app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-Dserver.port=8081", "-jar", "/app/app.jar"]
