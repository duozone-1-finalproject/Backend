FROM openjdk:21-jdk-slim

# 빌드된 실제 JAR 파일 이름으로 수정
ARG JAR_FILE=build/libs/test_02-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]
EXPOSE 8080
