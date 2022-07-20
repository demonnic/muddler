FROM eclipse-temurin:18-jdk-alpine
COPY ./build/libs/muddle*-all.jar /app
ENTRYPOINT ["java", "-jar", "/app"]
