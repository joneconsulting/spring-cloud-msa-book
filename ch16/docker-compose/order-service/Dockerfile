FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /app

COPY ./target/order-service-1.0.jar ./app.jar

EXPOSE 8082
ENTRYPOINT ["java", "-jar", "app.jar"]
