FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/*.jar app.jar
EXPOSE 8080
COPY target/meeting-buddy.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
