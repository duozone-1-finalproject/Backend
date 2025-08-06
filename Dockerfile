# backend-or-ai/Dockerfile
FROM openjdk:21-jdk-slim

ARG JAR_FILE=build/libs/app.jar

COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]

EXPOSE 8080


# 이미지 빌드
#docker build -t backend-image --build-arg JAR_FILE=build/libs/backend.jar .