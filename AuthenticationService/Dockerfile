FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY gradlew .
COPY gradle gradle

RUN chmod +x gradlew

CMD ["./gradlew", "bootRun"]