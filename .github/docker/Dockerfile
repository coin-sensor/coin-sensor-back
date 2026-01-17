# OpenJDK 21 기반 이미지 사용
FROM eclipse-temurin:21-jdk

# 필수 패키지 설치
RUN apt-get update && apt-get install -y wget curl unzip

WORKDIR /app

# JAR 파일 복사
COPY target/coinsensor-back-0.0.1-SNAPSHOT.jar coin-sensor-back.jar

# 애플리케이션 실행
ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "coin-sensor-back.jar"]