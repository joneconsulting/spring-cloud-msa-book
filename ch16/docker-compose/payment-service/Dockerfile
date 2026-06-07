FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /app

COPY ./target/payment-service-1.0.jar ./app.jar

EXPOSE 8084
ENTRYPOINT ["java", "-jar", "app.jar"]
