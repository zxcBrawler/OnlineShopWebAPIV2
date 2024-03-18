FROM openjdk:17.0.1-jdk-slim
COPY target/shop-0.0.1-SNAPSHOT.jar shop.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "shop.jar"]