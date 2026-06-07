FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /app

COPY ./target/service-discovery-1.0.jar ./app.jar

EXPOSE 8761
ENTRYPOINT ["java", "-jar", "app.jar"]
