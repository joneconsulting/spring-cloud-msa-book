FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /app

COPY ./target/config-service-1.0.jar ./app.jar

EXPOSE 8888
ENTRYPOINT ["java", "-jar", "app.jar"]
