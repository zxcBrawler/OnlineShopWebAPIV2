FROM maven:3.8.5-openjdk-17 AS build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:17.0.1-jdk-slim
COPY --frombuild /target/shop-0.0.1-SNAPSHOT.jar shop.jar
EXPOSE 8080
ENTRYPOINT["java", "-jar", "shop.jar"]