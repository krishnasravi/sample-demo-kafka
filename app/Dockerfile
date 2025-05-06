
FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY app/target/kafka-springboot-app-1.0-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
