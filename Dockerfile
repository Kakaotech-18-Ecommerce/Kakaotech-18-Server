# Base image로 OpenJDK 17 사용
FROM openjdk:17-jdk-slim

# JAR 파일을 복사할 경로를 지정
ARG JAR_FILE=golagola-0.0.1-SNAPSHOT.jar

COPY ${JAR_FILE} /app/app.jar

# 컨테이너가 실행될 때 JAR 파일을 실행하도록 설정
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
