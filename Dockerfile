FROM openjdk:21-jdk-slim

ARG JAR_FILE=build/libs/test_02-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} /app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]
EXPOSE 8080
