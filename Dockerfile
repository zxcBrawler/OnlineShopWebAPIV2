FROM maven:3.5-jdk-8-alpine AS build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:8-jre-alpine
COPY --from=build /target/shop-0.0.1-SNAPSHOT.jar shop.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "shop.jar"]